/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 *  Jason Gallagher
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada
 *
 * IndivoServiceImpl.java
 *
 * Created on April 27, 2007, 4:24 PM
 *
 */

package org.oscarehr.phr.indivo.service.impl;

import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.indivo.client.TalkClient;
import org.indivo.client.TalkClientImpl;
import org.indivo.xml.phr.DocumentUtils;
import org.indivo.xml.phr.DocumentVersionGenerator;
import org.indivo.xml.phr.document.DocumentClassificationType;
import org.indivo.xml.phr.document.DocumentHeaderType;
import org.indivo.xml.phr.document.DocumentVersionType;
import org.indivo.xml.phr.document.IndivoDocumentType;
import org.indivo.xml.phr.document.VersionBodyType;
import org.indivo.xml.phr.message.MessageType;
import org.indivo.xml.phr.record.IndivoRecordType;
import org.indivo.xml.talk.AddDocumentResultType;
import org.indivo.xml.talk.AuthenticateResultType;
import org.indivo.xml.talk.ReadResultType;
import org.indivo.xml.talk.SendMessageResultType;
import org.indivo.xml.talk.UpdateDocumentResultType;
import org.oscarehr.phr.PHRAuthentication;
import org.oscarehr.phr.PHRConstants;
import org.oscarehr.phr.dao.PHRActionDAO;
import org.oscarehr.phr.dao.PHRDocumentDAO;
import org.oscarehr.phr.indivo.IndivoAuthentication;
import org.oscarehr.phr.indivo.IndivoConstantsImpl;
import org.oscarehr.phr.model.PHRAction;
import org.oscarehr.phr.model.PHRDocument;
import org.oscarehr.phr.model.PHRMessage;
import org.oscarehr.phr.service.PHRService;
import org.w3c.dom.Element;
import oscar.OscarProperties;
import oscar.indivoMessenger.IndivoRetrieveAsyncAction;
import oscar.oscarDemographic.data.DemographicData;
import oscar.oscarEncounter.data.EctProviderData;
import oscar.oscarProvider.data.ProviderData;

/**
 *
 * @author jay
 */
public class IndivoServiceImpl  implements PHRService{
    
    private static Log log = LogFactory.getLog(IndivoServiceImpl.class);
    PHRDocumentDAO phrDocumentDAO;
    PHRActionDAO  phrActionDAO;
    
    /** Creates a new instance of IndivoServiceImpl */
    public IndivoServiceImpl() {
    }
    
    public boolean canAuthenticate(String providerNo){
        EctProviderData.Provider prov = new EctProviderData().getProvider(providerNo);
        String indivoId = prov.getIndivoId();
        if (indivoId == null){
            return false;
        }else{
            return true;
        }
    }
    
    public boolean validAuthentication(PHRAuthentication auth){
        Date now= new Date();
        if (auth == null){
            return false;
        }//else if ( now.after(auth.getExpirationDate() )){
         //   return false;
        //}
        
        try{
            log.debug(auth.getExpirationDate());
        }catch(Exception e){log.debug("ERROR",e);}
        
        //Also should do quick check to see if connection is active
        
        return true;
    }
    
    public PHRAuthentication authenticate(String providerNo,String password){
        EctProviderData.Provider prov = new EctProviderData().getProvider(providerNo);
        String indivoId = prov.getIndivoId();
        AuthenticateResultType authResult = null;
        PHRAuthentication auth =null;
        try{
           TalkClient client = getTalkClient();
           authResult = client.authenticate(indivoId, password);
           log.debug("actor ticket "+authResult.getActorTicket());
           auth = new IndivoAuthentication(authResult);
           auth.setProviderNo(providerNo);
        }catch(Exception e){
            e.printStackTrace();
        }
        return auth;
        
    }
    
     public void sendAddMessage(String subject, String priorThreadMessage, String messageBody, ProviderData sender, String recipientOscarId, int recipientType, String recipientPhrId) throws Exception{
        PHRMessage message = new PHRMessage(subject, priorThreadMessage, messageBody, sender, recipientOscarId, recipientType, recipientPhrId);
        PHRAction action = message.getAction(PHRAction.ACTION_ADD);
        phrActionDAO.save(action);
     }
     
     public void sendUpdateMessage(PHRMessage msg) throws Exception{
         PHRAction action = msg.getAction2(PHRAction.ACTION_UPDATE);
         phrActionDAO.save(action);
     }
     
     public boolean isIndivoRegistered(String classification, String oscarId) {
        phrActionDAO.isIndivoRegistered(classification, oscarId);
        return true;
     }
    
    
    public void retrieveDocuments(PHRAuthentication auth,String providerNo) throws Exception{
       log.info("In retrieveDocuments");
       TalkClient client = getTalkClient();
       ReadResultType readResult = client.readRecord(auth.getToken(),auth.getUserId());
       
       if (readResult != null){
          IndivoRecordType record = readResult.getIndivoRecord();
          List<IndivoDocumentType>list = record.getIndivoDocument();
          log.info("getMessages:Msgs List has "+list.size());
          
          
          for(IndivoDocumentType document:list){
             DocumentHeaderType docHeaderType = document.getDocumentHeader();
             DocumentClassificationType theType = docHeaderType.getDocumentClassification();
             String classification = theType.getClassification();
             String documentIndex  = docHeaderType.getDocumentIndex();
    
             log.debug("Document is of type "+classification+ " index is "+documentIndex);
             
             // Check if this document has been imported before
             boolean importStatus = checkImportStatus(documentIndex);
             
             if (importStatus && checkClassification(classification)){
                log.debug("PASSED check ");  
                PHRMessage mess =  new PHRMessage(document);
                mess.checkImportStatus();
                PHRDocument phrdoc = new PHRDocument();
                BeanUtils.copyProperties(phrdoc,mess);
                phrDocumentDAO.save( phrdoc);
             }else if (importStatus){
                 log.info("need to check for updates on this message");
             }
          }      
       }
    }
    
    
    
    public void sendQueuedDocuments(PHRAuthentication auth,String providerNo) throws Exception {
        List<PHRAction> actions = phrActionDAO.getQueuedActions(providerNo);
        TalkClient client = getTalkClient();
        PHRConstants constants = new IndivoConstantsImpl();
        boolean updated = false;
        log.debug("Processing "+actions.size()+" actions ");
        for (PHRAction action :actions) {
            //handle messages differently
            log.debug("ACTION classification "+action.getPhrClassification()+ " action type "+action.getActionType() );
            log.debug("BB"+constants.DOCTYPE_MESSAGE());
            log.debug("AA "+PHRAction.ACTION_ADD);
            
            if (action.getPhrClassification().equalsIgnoreCase(constants.DOCTYPE_MESSAGE()) && action.getActionType() == PHRAction.ACTION_ADD) {
                log.debug("Sending Add Message");
                IndivoDocumentType doc = action.getPhrDocument();
                log.debug("doc is null?? "+doc);
                JAXBContext messageContext =JAXBContext.newInstance(MessageType.class.getPackage().getName());
                MessageType msg = (MessageType) org.indivo.xml.phr.DocumentUtils.getDocumentAnyObject(doc, messageContext.createUnmarshaller());
                log.debug("doc is msg?? "+msg);
                client.sendMessage(auth.getToken(), msg);
                log.debug("message is going to "+msg.getRecipient());
                //client.sendMessage(auth.getToken(),msg.getRecipient(),msg.getPriorThreadMessageId(),msg.getSubject(),msg.getMessageContent().getAny().getTextContent() );  
                updated = true;
            } else if (action.getActionType() == PHRAction.ACTION_ADD) {
                //if adding
                IndivoDocumentType doc = action.getPhrDocument();
                
                AddDocumentResultType result = client.addDocument(auth.getToken(), action.getReceiverPhr(), doc);
                String resultIndex = result.getDocumentIndex();
                action.setPhrIndex(result.getDocumentIndex());
                //updates indexes to handle the case where two operations on this file are queued
                phrActionDAO.updatePhrIndexes(action.getPhrClassification(), action.getOscarId(), action.getSenderOscar(), resultIndex);
                updated = true;
            //if updating
            }else if (action.getPhrClassification().equalsIgnoreCase(constants.DOCTYPE_MESSAGE()) && action.getActionType() == PHRAction.ACTION_UPDATE) {    
                log.debug("HERE MESSAGE UPDATE");
                org.indivo.xml.JAXBUtils jaxbUtils  = new   org.indivo.xml.JAXBUtils(); 
                                
                IndivoDocumentType document = action.getPhrDocument();
                
                JAXBContext messageContext = JAXBContext.newInstance("org.indivo.xml.phr.message");
                MessageType msg = (MessageType) org.indivo.xml.phr.DocumentUtils.getDocumentAnyObject(document,messageContext.createUnmarshaller());
                //??? Should i use reflection to abstract the call to ObjectFactory??? how will in know what method to call?  the one that will take MessageType as a param???
                log.debug("IS READ "+msg.isRead());
                Element element = jaxbUtils.marshalToElement(new org.indivo.xml.phr.message.ObjectFactory().createMessage(msg), 
                                                     JAXBContext.newInstance("org.indivo.xml.phr.message"));   
        
                DocumentVersionGenerator dvg = new DocumentVersionGenerator();
                DocumentVersionType newVersion = dvg.generateDefaultDocumentVersion(auth.getUserId(),auth.getName(),auth.getRole(),element);
                log.debug("BEFORE UPDATE DOCUMENT calling with token "+auth.getToken()+" id "+auth.getUserId()+" idx "+ action.getPhrIndex()+" v "+ newVersion);
                UpdateDocumentResultType updateDocRes = client.updateDocument(auth.getToken(),auth.getUserId(), action.getPhrIndex(), newVersion); 
                if (updateDocRes == null){
                    log.debug("UPDATE DOC IS NULL");
                }else{
                    log.debug("UPDATE DOC IS NOT NULL"+updateDocRes.toString());    
                }
                log.debug("AFTER UPDATE DOCUMENT");
                updated = true;
                
                
            } else if (action.getActionType() == PHRAction.ACTION_UPDATE) {
                if (action.getPhrIndex() == null) throw new Exception("Error: PHR index not set");
                IndivoDocumentType doc = action.getPhrDocument();
                Element documentElement = DocumentUtils.getDocumentAnyElement(doc);
                //Retrieve current file record from indivo
                log.debug("phr index "+action.getPhrIndex());
                //ReadDocumentResultType readResult = client.readDocument(auth.getToken(), action.getSenderPhr(), action.getPhrIndex());
                //IndivoDocumentType phrDoc = readResult.getIndivoDocument();
                
                PHRDocument phrd = phrDocumentDAO.getDocumentByIndex(action.getPhrIndex());
                JAXBContext docContext = JAXBContext.newInstance(IndivoDocumentType.class.getPackage().getName());
                Unmarshaller unmarshaller = docContext.createUnmarshaller();
       
                JAXBElement jaxment = (JAXBElement) unmarshaller.unmarshal(new StringReader(phrd.getDocContent()));
                IndivoDocumentType phrDoc = (IndivoDocumentType)  jaxment.getValue();
                
                DocumentVersionType version = phrDoc.getDocumentVersion().get(phrDoc.getDocumentVersion().size() - 1);

                //send new version
                VersionBodyType body = version.getVersionBody();
                body.setAny(documentElement);
                version.setVersionBody(body);
                client.updateDocument(auth.getToken(),auth.getUserId(), action.getPhrIndex(), version); 
                updated = true;
            }else{
                log.debug("NOTHING IS GETTING CALLED FOR THIS ");
                
            }
            if (updated) {
                action.setSent(PHRAction.STATUS_SENT);
                phrActionDAO.update(action);
            }
        }
    }
    

    public boolean checkImportStatus(String documentIndex){
       return !phrDocumentDAO.hasIndex(documentIndex);
    }
    
        
    public boolean checkClassification(String classification){
       boolean classImported = false;
      
       if ("urn:org:indivo:document:classification:message".equalsIgnoreCase(classification)){
           classImported = true;
       }
       return classImported;
    }
                 
    
    
    public void  sendDemographicMessage(PHRAuthentication auth,String demographic, String priorThreadMessage, String subject,String messageText) {
        DemographicData dd = new DemographicData();
        DemographicData.Demographic demo = dd.getDemographic(demographic);
        String recipientId = demo.getIndivoId();
        try{
           TalkClient client = getTalkClient();
           SendMessageResultType sendMessageResultType = client.sendMessage(auth.getToken(),recipientId,priorThreadMessage,subject,messageText); 
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void  sendMessage(PHRAuthentication auth,String recipientId, String priorThreadMessage, String subject,String messageText) {
        try{
           TalkClient client = getTalkClient();
           SendMessageResultType sendMessageResultType = client.sendMessage(auth.getToken(),recipientId,priorThreadMessage,subject,messageText); 
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
        private TalkClient getTalkClient() throws Exception{
            Map m = new HashMap();
            String indivoServer = OscarProperties.getInstance().getProperty("INDIVO_SERVER");           
            m.put(TalkClient.SERVER_LOCATION,indivoServer);
            TalkClient client = new TalkClientImpl(m);
            return client;
        }

        TalkClient getTalkClient(String providerNo) throws Exception{
            return getTalkClient();
        }

        TalkClient getTalkClient(String providerNo,String demographicNo)throws Exception{
            return getTalkClient();

        }

    public void setPhrDocumentDAO(PHRDocumentDAO phrDocumentDAO) {
        this.phrDocumentDAO = phrDocumentDAO;
        
    }
    
    public void setPhrActionDAO(PHRActionDAO phrActionDAO){
        this.phrActionDAO = phrActionDAO;
    }
    
}
