/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.decisionSupport.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.decisionSupport.model.DSGuideline;
import org.oscarehr.decisionSupport.model.DSGuidelineFactory;
import org.oscarehr.decisionSupport.model.DSGuidelineProviderMapping;
import org.oscarehr.decisionSupport.model.DecisionSupportException;
import oscar.oscarRx.pageUtil.RxMyDrugrefInfoAction;

/**
 *
 * @author apavel
 */
public class DSServiceMyDrugref extends DSService {
    private static Log _log = LogFactory.getLog(DSServiceMyDrugref.class);
    private UserPropertyDAO  userPropertyDAO;

    public DSServiceMyDrugref() {

    }

    public void fetchGuidelinesFromService(String providerNo) {

        Vector<String> params = new Vector();
        params.addElement(this.getMyDrugrefId(providerNo));
        RxMyDrugrefInfoAction myDrugrefAction = new RxMyDrugrefInfoAction();
        try {
            _log.debug("CALLING MYDRUGREF");
            Vector<Hashtable> providerGuidelines = (Vector) myDrugrefAction.callWebserviceLite("GetGuidelineIds", params);
            if (providerGuidelines == null) {
                _log.error("Could not get provider decision support guidelines from MyDrugref.");
                return;
            }
            _log.debug("MyDrugref call returned: " + providerGuidelines.size() + " guidelines");
            ArrayList<String> guidelinesToFetch = new ArrayList();
            for (Hashtable providerGuideline: providerGuidelines) {

                String uuid = (String) providerGuideline.get("uuid");
                String versionNumberStr = (String) providerGuideline.get("version");
                Integer versionNumber = Integer.parseInt(versionNumberStr);

                _log.debug("uuid: " + uuid);
                _log.debug("version: " + versionNumber);

                DSGuideline matchedGuideline = dsGuidelineDAO.getDSGuidelineByUUID(uuid);
                if (matchedGuideline == null) {
                    guidelinesToFetch.add(uuid);
                } else if (matchedGuideline.getVersion() < versionNumber) {
                    matchedGuideline.setStatus('I');
                    matchedGuideline.setDateDecomissioned(new Date());
                    dsGuidelineDAO.update(matchedGuideline);
                    guidelinesToFetch.add(uuid);
                }
            }
            //fetch the new ones
            List<DSGuideline> newGuidelines = this.fetchGuidelines(guidelinesToFetch);
            for (DSGuideline newGuideline: newGuidelines) {
                dsGuidelineDAO.save(newGuideline);
            }
            //Do mappings-guideline mappings;
            List<DSGuidelineProviderMapping> uuidsMapped = dsGuidelineDAO.getMappingsByProvider(providerNo);
            for (Hashtable newMapping: providerGuidelines) {
                String newUuid = (String) newMapping.get("uuid");
                System.out.println("newUuid: " + newUuid);
                DSGuidelineProviderMapping newUuidObj = new DSGuidelineProviderMapping(newUuid, providerNo);
                if (uuidsMapped.contains(newUuidObj)) {
                    uuidsMapped.remove(newUuidObj);
                } else {
                    dsGuidelineDAO.save(newUuidObj);
                }
            }
            //remove ones left over
            for (DSGuidelineProviderMapping uuidLeft: uuidsMapped) {
                dsGuidelineDAO.delete(uuidLeft);
            }
        } catch (Exception e) {
            _log.error("Unable to fetch guidelines from MyDrugref", e);
        }

    }

    public List<DSGuideline> fetchGuidelines(List<String> uuids) throws Exception {
        
        RxMyDrugrefInfoAction myDrugrefAction = new RxMyDrugrefInfoAction();
        Vector params = new Vector();
        params.addElement(new Vector(uuids));
        
        Vector<Hashtable> fetchedGuidelines = (Vector<Hashtable>) myDrugrefAction.callWebserviceLite("GetGuidelines", params);
        ArrayList newGuidelines = new ArrayList();
        for (Hashtable fetchedGuideline: fetchedGuidelines) {
            _log.debug("Title: " + (String) fetchedGuideline.get("name"));
            _log.debug("Author: " + (String) fetchedGuideline.get("author"));
            _log.debug("UUID: " + (String) fetchedGuideline.get("uuid"));
            _log.debug("Version: " + (String) fetchedGuideline.get("version"));

            DSGuidelineFactory factory = new DSGuidelineFactory();
            DSGuideline newGuideline = factory.createBlankGuideline();
            try { 
                newGuideline.setUuid((String) fetchedGuideline.get("uuid"));
                newGuideline.setTitle((String) fetchedGuideline.get("name"));
                newGuideline.setVersion(Integer.parseInt((String) fetchedGuideline.get("version")));
                newGuideline.setAuthor((String) fetchedGuideline.get("author"));
                newGuideline.setXml((String) fetchedGuideline.get("body"));
                newGuideline.setSource("mydrugref");
                newGuideline.setDateStart(new Date());
                newGuideline.setStatus('A');
                
                newGuideline.setXml((String) fetchedGuideline.get("body"));
                newGuidelines.add(newGuideline);

                newGuideline.parseFromXml();

                
            } catch (Exception e) {
                DecisionSupportException newException = new DecisionSupportException("Error parsing drug with with title: '" + (String) fetchedGuideline.get("name") + "' uuid: '" + (String) fetchedGuideline.get("uuid") + "'", e);
                _log.error(newException);
                newGuideline.setStatus('F');
                newGuideline.setDateDecomissioned(new Date());
            }
        }
        return newGuidelines;
    }

    public String getMyDrugrefId(String providerNo) {
        UserProperty prop = userPropertyDAO.getProp(providerNo, UserProperty.MYDRUGREF_ID);
        String myDrugrefId = null;
        if (prop != null) {
            myDrugrefId = prop.getValue();
        }
        return myDrugrefId;
    }

    /**
     * @param userPropertyDAO the userPropertyDAO to set
     */
    public void setUserPropertyDAO(UserPropertyDAO userPropertyDAO) {
        this.userPropertyDAO = userPropertyDAO;
    }

}
