/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.phr.indivo.service.accesspolicies;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.indivo.security.ProfileUtils;
import org.indivo.security.SecurityUtils;
import org.indivo.xml.JAXBUtils;
import org.indivo.xml.phr.AccessPolicyDocumentGenerator;
import org.indivo.xml.phr.document.DocumentHeaderType;
import org.indivo.xml.phr.document.DocumentVersionType;
import org.indivo.xml.phr.document.IndivoDocument;
import org.indivo.xml.phr.document.IndivoDocumentType;
import org.indivo.xml.security.ProfileCollection;
import org.indivo.xml.security.ProfileCollectionType;
import org.oscarehr.phr.PHRAuthentication;
import org.oscarehr.phr.PHRConstants;
import org.oscarehr.phr.indivo.service.impl.IndivoServiceImpl;
import org.oscarehr.phr.model.PHRAction;
import org.oscarehr.phr.model.PHRDocument;
import org.oscarehr.phr.service.PHRService;
import org.oscarehr.util.MiscUtils;

import oscar.oscarProvider.data.ProviderData;
import oscar.util.StringUtils;

import com.sun.xacml.AbstractPolicy;
import com.sun.xacml.PolicySet;
import com.sun.xacml.combine.PolicyCombiningAlgorithm;

/**
 *
 * @author apavel
 */
public class IndivoAPService extends IndivoServiceImpl {
    
    /*Setting a provider access policy occurs in 4 stages:
     * 1.  ProposeAccessPolicy - Adds proposal to the actions
     * 2.  AcceptAccessPolicy/DenyAccessPolicy - provider accepts AP, gets marked as "ON HOLD" or rejects it, and it gets marked as "deleted"
     * 3.  PackageAccessPolicy - Called by indivoService just before documents are sent to indivo.  This retrieves existing policies, makes an
     *                          indivo document with the new policies, puts it in actions and marks it as an UPDATE action.
     * 4.  SendAccessPolicy - IndivoService sends the action policy via UPDATE action along with other documents.
     
     * All this occurs in one stage for sending demographic access policies
     */
    
    private static Logger log = MiscUtils.getLogger(); ///IndivoServiceImpl.class
    
    public static final String LEVEL_NOT_SET = "";
    public static final String LEVEL_PATIENT = "org:indivo:profile:patient";
    public static final String LEVEL_PROVIDER = "org:indivo:profile:primary-care-provider";
    //if you want to add more of these, see indivo-security-profiles.xml in oscarehr/phr/indivo/accesspolicies
    
    private static final String DOC_CONTENT_DELIMITER = ", ";
    
    public IndivoAPService() {
        
    }
    
    public IndivoAPService(PHRService phrService) {
        this.phrActionDAO = phrService.getPhrActionDao();
        this.phrDocumentDAO = phrService.getPhrDocumentDAO();
    }
    
    /*adds the access policy to the PHRActions table.  The action waits for approval from the provider
    //before it is queued and sent*/
    public void proposeAccessPolicy(String providerOscarId, String permissionRecipientPhrId, String newPolicy, String providerIdPerformingAction) {
        PHRAction action = new PHRAction();
        action.setActionType(PHRAction.ACTION_UPDATE);
        action.setStatus(PHRAction.STATUS_APPROVAL_PENDING);
        action.setPhrClassification(PHRConstants.DOCTYPE_ACCESSPOLICIES());
        action.setDateQueued(new Date());
        action.setReceiverOscar(providerOscarId);
        //TOTHINK: might have to get Id later
        action.setReceiverType(PHRDocument.TYPE_PROVIDER);
        action.setSenderType(PHRDocument.TYPE_PROVIDER);
        action.setSenderOscar(providerIdPerformingAction);
        //make a csv store permissionRecipientPhrId and newPolicy
        action.setDocContent(permissionRecipientPhrId + DOC_CONTENT_DELIMITER + newPolicy);
        //User receiving permissions is stored as senderPhr
        action.setSenderPhr(permissionRecipientPhrId);
        phrActionDAO.save(action);
    }
    
    public void approveAccessPolicy(PHRAction action) {
        action.setStatus(PHRAction.STATUS_SEND_PENDING);
        phrActionDAO.update(action);
    }
    
    public void denyAccessPolicy(PHRAction action) {
        this.denyAction(action);
    }
    
    
    //user who is giving permissions must be authenticated with the indivo server (valid auth object)
    //permissionRecipientId is the user to whom permissions will be granted
    //queues as an action, awaits provider approval
    public PHRAction packageAccessPolicy(PHRAuthentication auth, PHRAction action) throws Exception {
        //PHRAction action = phrActionDAO.getActionById(actionId);
        
        //action.getDocContent stores the permissionRecipient Indivo ID and permissionType
        ArrayList al = StringUtils.split(action.getDocContent(), DOC_CONTENT_DELIMITER);
        String permissionRecipientId = (String) al.get(0);
        String newPolicy = (String) al.get(1);
        
        //getIndivoId
        ProviderData providerData = new ProviderData();
        providerData.setProviderNo(action.getReceiverOscar());
        String receiverPhr = providerData.getMyOscarId();
        
        IndivoDocumentType newPolicyDoc = prepareAccessPolicy(auth, permissionRecipientId, newPolicy);
        JAXBContext docContext = JAXBContext.newInstance(IndivoDocumentType.class.getPackage().getName());
        byte[] newPolicyDocBytes = JAXBUtils.marshalToByteArray((JAXBElement) new IndivoDocument(newPolicyDoc), docContext);
        String newPolicyDocStr = new String(newPolicyDocBytes);
        //log.debug("PolicyFile: " + newPolicyDocStr);
        action.setReceiverPhr(receiverPhr);
        action.setStatus(PHRAction.STATUS_SEND_PENDING);
        action.setDocContent(newPolicyDocStr);
        action.setDateQueued(new Date());  //reset date
        action.setPhrIndex(newPolicyDoc.getDocumentHeader().getDocumentIndex());
        phrActionDAO.update(action);
        return action;
    }
    
    //PROVIDER needs to be authed with the indivoServer, and providerNo must be set in the auth object
    public void packageAllAccessPolicies(PHRAuthentication auth) throws Exception {
        List<PHRAction> policiesOnHold = phrActionDAO.getActionsByStatus(PHRAction.STATUS_ON_HOLD, auth.getProviderNo(), PHRConstants.DOCTYPE_ACCESSPOLICIES());
        for (PHRAction curPolicy: policiesOnHold) {
            packageAccessPolicy(auth, curPolicy);
        }
    }
    
    public static List getProposalIdAndPermission(PHRAction action) {
        return StringUtils.split(action.getDocContent(), DOC_CONTENT_DELIMITER);
    }
    

    //used for directly sending demographic access policies
    public void sendAddAccessPolicy(PHRAuthentication auth, String permissionRecipientId, String newPolicy) throws Exception {
        IndivoDocumentType newPolicyDoc = prepareAccessPolicy(auth, permissionRecipientId, newPolicy);
        updateDocumentDirect(auth, newPolicyDoc.getDocumentVersion().get(0), org.indivo.xml.urns.DocumentClassificationUrns.ACCESS_POLICIES, newPolicyDoc.getDocumentHeader().getDocumentIndex());
    }
    
    //user who is giving permissions must be authenticated with the indivo server (valid auth object)
    //permissionRecipientId is the user to whom permissions will be granted
    protected IndivoDocumentType prepareAccessPolicy(PHRAuthentication auth, String permissionRecipientId, String newPolicy) throws Exception {
        ProfileCollection profiles = getPoliciesFromFile();
        
        PolicySet newPolicySet = (PolicySet) SecurityUtils.createPolicyFromProfile(permissionRecipientId, newPolicy, profiles);
        
        Map profileMap = ProfileUtils.mapNamesToProfiles(profiles);
        
        List<DocumentHeaderType> accessPolicyHeaders = getDocumentHeadersDirect(auth, org.indivo.xml.urns.DocumentClassificationUrns.ACCESS_POLICIES);
        String policyDocIndex = accessPolicyHeaders.get(0).getDocumentIndex();
        DocumentVersionType latestPolicyTypeVersion = getDocumentVersionDirect(auth, policyDocIndex, -1);
        PolicySet policySet = PolicySet.getInstance(latestPolicyTypeVersion.getVersionBody().getAny());
        
        ArrayList newPolicies = new ArrayList();
        
        List oldPolicies = policySet.getChildren();
        
        //removing old sharing with this user
        log.debug("oldPolicies type: " + oldPolicies.get(0).getClass().toString());
        for (int i=0; i<oldPolicies.size(); i++) {
            AbstractPolicy oldPolicy = (AbstractPolicy) oldPolicies.get(i);
            
            if (oldPolicy.getDescription() != null && profileMap.containsKey(oldPolicy.getDescription())) {
                if (oldPolicy.getTarget().getSubjects().contains(permissionRecipientId)) {
                    continue;
                }
            }
            newPolicies.add(oldPolicy);
        }
        newPolicies.add(newPolicySet);
        
        newPolicySet = new PolicySet(policySet.getId(),
                                    (PolicyCombiningAlgorithm) policySet.getCombiningAlg(),
                                    policySet.getDescription(),
                                    policySet.getTarget(),
                                    (List) newPolicies,
                                    policySet.getDefaultVersion(),
                                    policySet.getObligations());
        log.debug("access policy name: " + accessPolicyHeaders.get(0).getAuthor().getName());
        IndivoDocumentType newPolicyDoc = AccessPolicyDocumentGenerator.generateAccessPolicyDocument(auth.getUserId(), accessPolicyHeaders.get(0).getAuthor().getName(), auth.getRole(), newPolicySet);
        newPolicyDoc.getDocumentHeader().setDocumentIndex(policyDocIndex);
        //DocumentVersionType newPolicyVersion = newPolicyDoc.getDocumentVersion().get(0);
        
        //indivoService.updateDocument(auth, newPolicyVersion, org.indivo.xml.urns.DocumentClassificationUrns.ACCESS_POLICIES, policyDocIndex);
        return newPolicyDoc;
    }
    
    private ProfileCollection getPoliciesFromFile() throws JAXBException {
        //TODO:  If OscarProperties variable...
        //String policyFilePath = "";
        //fs = new FileInputStream(configpath);  
        ClassLoader loader = this.getClass().getClassLoader();
        InputStream fs = loader.getResourceAsStream(this.getClass().getPackage().getName().replace('.', '/') + "/indivo-security-profiles.xml"); 
        
        //unmarshal the file
        JAXBContext factoryContext = JAXBContext.newInstance(org.indivo.xml.security.ObjectFactory.class.getPackage().getName());
        Unmarshaller unmarshaller = factoryContext.createUnmarshaller();
        JAXBElement jaxment = (JAXBElement) unmarshaller.unmarshal(fs);
        ProfileCollectionType profileCollectionType = (ProfileCollectionType) jaxment.getValue();
        ProfileCollection policyList = new ProfileCollection(profileCollectionType);
        
        return policyList;
    }

}
