/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package org.oscarehr.phr.model;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.indivo.IndivoException;
import org.indivo.xml.JAXBUtils;
import org.indivo.xml.phr.DocumentGenerator;
import org.indivo.xml.phr.annotation.DocumentReferenceType;
import org.indivo.xml.phr.document.DocumentClassificationType;
import org.indivo.xml.phr.document.DocumentHeaderType;
import org.indivo.xml.phr.document.DocumentVersionType;
import org.indivo.xml.phr.document.IndivoDocument;
import org.indivo.xml.phr.document.IndivoDocumentType;
import org.indivo.xml.phr.message.Message;
import org.indivo.xml.phr.message.MessageContentType;
import org.indivo.xml.phr.message.MessageType;
import org.indivo.xml.phr.message.TextMessage;
import org.indivo.xml.phr.urns.ContentTypeQNames;
import org.indivo.xml.phr.urns.DocumentClassificationUrns;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.w3c.dom.Element;

import oscar.oscarDemographic.data.DemographicData;
import oscar.oscarProvider.data.ProviderData;
import oscar.oscarProvider.data.ProviderMyOscarIdData;

/**
 *
 * @author jay
 */
public class PHRMessage  extends PHRDocument implements Serializable{
    private static Logger log = MiscUtils.getLogger();
    //for status received msgs
    public static final int STATUS_NEW = 1;
    public static final int STATUS_READ = 2;
    public static final int STATUS_REPLIED = 4;
    public static final int STATUS_ARCHIVED = 8;

    public static final String MESSAGE_ID = "MESSAGE_ID";
    /*
     Message retrieved from indivo should be of status either  0 or 1.
     Once a message is read READ indicator should be switched on.
     */


    private MessageType msg = null;

    /** Creates a new instance of PHRMessage */
    public PHRMessage() {
        super();
    }

    public PHRMessage(PHRDocument doc ) throws Exception{
        super();

        JAXBContext docContext = JAXBContext.newInstance(IndivoDocumentType.class.getPackage().getName());
        Unmarshaller unmarshaller = docContext.createUnmarshaller();
        //IndivoDocumentType indivoDocument = (IndivoDocumentType)
        JAXBElement jaxment = (JAXBElement) unmarshaller.unmarshal(new StringReader(doc.getDocContent()));
        IndivoDocumentType indivoDocument = (IndivoDocumentType)  jaxment.getValue();
        parseDocument(indivoDocument);
        this.setStatus(doc.getStatus());
        this.setId(doc.getId());
        log.debug("ID IS NOW SET TO "+this.getId() );
    }

    public void setRead(){
        msg.setRead(true);
        addStatus(PHRMessage.STATUS_READ);
    }

    public void setReplied(){
        msg.setReplied(true);
        addStatus(PHRMessage.STATUS_REPLIED);
    }


    public PHRMessage(String senderOscarId, int senderType, Long senderPhrId, Integer recipientOscarId, int recipientType, Long recipientPhrId, String docContentStr) {
        this();
        this.setSenderOscar(senderOscarId);
        this.setSenderType(senderType);
        this.setSenderMyOscarUserId(senderPhrId);
        this.setReceiverOscar(recipientOscarId);
        this.setReceiverType(recipientType);
        this.setReceiverMyOscarUserId(recipientPhrId);
        this.setDocContent(docContentStr);
    }




    public void reDocContent() throws Exception{

        JAXBContext docContext = JAXBContext.newInstance(IndivoDocumentType.class.getPackage().getName());
        Unmarshaller unmarshaller = docContext.createUnmarshaller();
        //IndivoDocumentType indivoDocument = (IndivoDocumentType)
        JAXBElement jaxment = (JAXBElement) unmarshaller.unmarshal(new StringReader(this.getDocContent()));
        IndivoDocumentType indivoDocument = (IndivoDocumentType)  jaxment.getValue();

        DocumentVersionType dvt =  indivoDocument.getDocumentVersion().get(indivoDocument.getDocumentVersion().size() -1);


        JAXBContext messageContext = JAXBContext.newInstance(MessageType.class.getPackage().getName());



        org.indivo.xml.JAXBUtils jaxbUtils = new org.indivo.xml.JAXBUtils();
        org.indivo.xml.phr.message.ObjectFactory msgFactory = new org.indivo.xml.phr.message.ObjectFactory();
        Message msgelement = msgFactory.createMessage(msg);

        Element element = JAXBUtils.marshalToElement(msgelement, messageContext);


        dvt.getVersionBody().setAny(element);

        parseDocument(indivoDocument);


//        JAXBContext docContext = JAXBContext.newInstance(IndivoDocumentType.class.getPackage().getName());
//        IndivoDocumentType document = getPhrMessageDocument(sender, msg);
//        byte[] docContentBytes = JAXBUtils.marshalToByteArray((JAXBElement) new IndivoDocument(document), docContext);
//        String docContentStr = new String(docContentBytes);
//        this.setDocContent(docContentStr);
    }

    public PHRMessage(String subject, String priorThreadMessage, String messageBody, ProviderData sender, Integer recipientOscarId, int recipientType, Long myOscarUserId) throws JAXBException, IndivoException {
        this(subject, priorThreadMessage, messageBody, sender, recipientOscarId, recipientType, myOscarUserId, new ArrayList<String>());
    }

    public PHRMessage(String subject, String priorThreadMessage, String messageBody, ProviderData sender, Integer recipientOscarId, int recipientType, Long myOscarUserId, List<String> attachedDocumentActionIds) throws JAXBException, IndivoException {
        this();
        JAXBContext docContext = JAXBContext.newInstance(IndivoDocumentType.class.getPackage().getName());
        MessageType message = getPhrMessage(myOscarUserId, priorThreadMessage, subject, messageBody, attachedDocumentActionIds);
        IndivoDocumentType document = getPhrMessageDocument(sender, message);
        byte[] docContentBytes = JAXBUtils.marshalToByteArray(new IndivoDocument(document), docContext);
        String docContentStr = new String(docContentBytes);

        this.setPhrClassification("MESSAGE");

        this.setSenderOscar(sender.getProviderNo());
        this.setSenderType(PHRDocument.TYPE_PROVIDER);
        this.setSenderMyOscarUserId(Long.parseLong(sender.getMyOscarId()));
        this.setReceiverOscar(recipientOscarId);
        this.setReceiverType(recipientType);
        this.setReceiverMyOscarUserId(myOscarUserId);
        this.setDocContent(docContentStr);
    }




    /**
     *Don't want to be able to add multiple statuses
     * ie READ + READ != REPLIED
     *
     * logic
     * if result has status and status < 0 the if it should go
     *   or
     * if result does not have status and status > 0 it should go
     */
    public void addStatus(int status ){
        log.debug("id :"+ this.getId()+"  ST :"+this.getStatus()+ "  adding  "   +status);

        if ( !hasStatus(status) && status > 0  ){
           //log.debug("add statuss");
           this.setStatus(this.getStatus() +status );
        }else if (hasStatus(-status) && status < 0  ){
           //log.debug("subtract statuss");
           this.setStatus(this.getStatus() +status );
        }


        log.debug(" new Stat :"+this.getStatus());
    }

    public boolean isReplied(){
        return hasStatus(PHRMessage.STATUS_REPLIED);
    }

    public boolean isRead(){
        return hasStatus(PHRMessage.STATUS_READ);
    }

    public boolean isArchived(){
        return hasStatus(PHRMessage.STATUS_ARCHIVED);
    }

    public boolean isNew(){
        return hasStatus(PHRMessage.STATUS_NEW);
    }




    public boolean hasStatus(int status) {
      return (this.getStatus() & status) == status;
    }

    public void checkImportStatus(){
        if(msg.isRead() ){
            addStatus(PHRMessage.STATUS_READ);
        } else {
            addStatus(PHRMessage.STATUS_NEW);
        }
        if(msg.isReplied() ){
            addStatus(PHRMessage.STATUS_REPLIED);
        }

        log.debug("STATUS IS "+this.getStatus());
    }





    private void parseDocument(IndivoDocumentType document) throws Exception{
        JAXBContext docContext = JAXBContext.newInstance("org.indivo.xml.phr.document");
        byte[] docContentBytes = JAXBUtils.marshalToByteArray(new IndivoDocument(document), docContext);
        String docContent = new String(docContentBytes);

        log.debug(docContent);

        DocumentHeaderType docHeaderType = document.getDocumentHeader();
        DocumentClassificationType theType = docHeaderType.getDocumentClassification();
        String classification = theType.getClassification();
        String documentIndex  = docHeaderType.getDocumentIndex();

        JAXBContext messageContext = JAXBContext.newInstance("org.indivo.xml.phr.message");
         msg = (MessageType) org.indivo.xml.phr.DocumentUtils.getDocumentAnyObject(document,messageContext.createUnmarshaller());


        HashMap m = new HashMap();
        String rawMessageId = msg.getId();
        if (rawMessageId != null){
           String indexStr =  null;
           try {
               if ((rawMessageId.indexOf("[") == -1 || rawMessageId.indexOf("]") == -1) && !rawMessageId.equals("")) {
                   indexStr = rawMessageId;
               } else {
                   indexStr = rawMessageId.substring(rawMessageId.lastIndexOf("[")+1, rawMessageId.lastIndexOf("]"));
               }
           } catch (Exception e) {
               log.error("ERROR: rawMessageId is formatted poorly: " + rawMessageId);
               MiscUtils.getLogger().error("Error", e);
           }
           m.put(PHRMessage.MESSAGE_ID,indexStr);
           this.setExts(m);
        }
        if (docHeaderType.getCreationDateTime() == null)
            this.setDateSent(null);
        else {
            this.setDateSent(docHeaderType.getCreationDateTime().toGregorianCalendar().getTime());
            log.debug("Date Created set to "+docHeaderType.getCreationDateTime().toGregorianCalendar().getTime());
        }
        this.setDateExchanged(new Date());
        this.setPhrClassification(classification);
        this.setPhrIndex(documentIndex);
// This code can't possibly be run... it's using the wrong library
//        this.setSenderMyOscarUserId(msg.getSender());
        this.setDocSubject(msg.getSubject());

        this.setSenderType(indivoRoleToOscarType(docHeaderType.getAuthor().getRole().getValue()));
// This code can't possibly be run... it's using the wrong library
//        this.setReceiverMyOscarUserId(msg.getRecipient());
        Hashtable result = new Hashtable();
        String newOscarId;
        int newIdType;
       //find sender oscar id
// This code can't possibly be run... it's using the wrong library
//        result = findOscarId(this.getSenderType(), this.getSenderPhr());
        newOscarId = (String) result.get("oscarId");
        this.setSenderOscar(newOscarId);
        newIdType = ((Integer) result.get("idType")).intValue();
        this.setSenderType(newIdType);
       //find receiver oscar id
// This code can't possibly be run... it's using the wrong library
//        result = findOscarId(this.getReceiverType(), this.getReceiverMyOscarUserId());
        newOscarId = (String) result.get("oscarId");
        this.setReceiverOscar(Integer.valueOf(newOscarId));
        newIdType = ((Integer) result.get("idType")).intValue();
        this.setReceiverType(newIdType);
        this.setDocContent(docContent);
    }

    public PHRMessage(IndivoDocumentType document ) throws Exception{
        super();
        parseDocument(document);
    }


    public String getBody(){
        String  ret = msg.getMessageContent().getAny().getTextContent();
        if ( ret == null){
            return "";
        }
        return ret;
    }

    public boolean isFromDemographic(){
        return PHRDocument.TYPE_DEMOGRAPHIC == this.getSenderType();
    }

// this code could never have worked, it's mixing up ID / NAME
//    public String getSenderDemographicNo(){
//        Hashtable h = findOscarId(PHRDocument.TYPE_DEMOGRAPHIC,this.getSenderMyOscarUserId());
//        return (String) h.get("oscarId");
//    }

    public Hashtable findOscarId(LoggedInInfo loggedInInfo, int idType, String myOscarUserName) {
       DemographicData demographicData = new DemographicData();
       Hashtable results = new Hashtable();
       String oscarId = "";
       if (idType == PHRDocument.TYPE_PROVIDER) {
           oscarId = ProviderMyOscarIdData.getProviderNo(myOscarUserName);
           log.debug("OSCAR ID "+oscarId);
       } else if(idType == PHRDocument.TYPE_DEMOGRAPHIC) {
           oscarId = demographicData.getDemographicNoByIndivoId(loggedInInfo, myOscarUserName);
       } else if (idType == PHRDocument.TYPE_NOT_SET) {
           //try provider:
           String searchNo = ProviderMyOscarIdData.getProviderNo(myOscarUserName);
           if (!searchNo.equals("")) {
               oscarId = searchNo;
               idType = PHRDocument.TYPE_PROVIDER;
           }
           //try demographic
           searchNo = demographicData.getDemographicNoByIndivoId(loggedInInfo, myOscarUserName);
           if (!searchNo.equals("")) {
               oscarId = searchNo;
               idType = PHRDocument.TYPE_DEMOGRAPHIC;
           }
       }
       results.put("idType", idType);
       results.put("oscarId", oscarId);
       return results;
   }


    private int indivoRoleToOscarType(String role) {
        if (role.equalsIgnoreCase("provider") || role.equalsIgnoreCase("administrator"))
            return PHRDocument.TYPE_PROVIDER;
        else if (role.equalsIgnoreCase("patient"))
            return PHRDocument.TYPE_DEMOGRAPHIC;
        log.warn("Unknown role: " +role);
        return -1;
   }

   public String getReferenceMessage(){
       log.debug(" GET REF FROM PHRMESSAGE "+msg.getPriorThreadMessageId());
      return msg.getPriorThreadMessageId();
   }

   private MessageContentType toTextMessageContent(String content) {
        MessageContentType msgBod = new MessageContentType();


        try {
            JAXBContext messageContext = JAXBContext.newInstance(MessageType.class.getPackage().getName());
            // create JAXB object view of the MessageContent XML
            TextMessage textMessage = new TextMessage(content);

            Element w3cMessageContentElement = null;
            // create dom object view of the MessageContent XML
            w3cMessageContentElement = JAXBUtils.marshalToElement(textMessage, messageContext);

            msgBod.setAny(w3cMessageContentElement);
        } catch (IndivoException pe) {
            throw new RuntimeException(pe);
        } catch (JAXBException je) {
            throw new RuntimeException(je);
        }

        return msgBod;
    }



    private IndivoDocumentType getPhrMessageDocument(ProviderData sender, MessageType message) throws JAXBException, IndivoException {
        String providerFullName = sender.getFirst_name() + " " + sender.getLast_name();
        return getPhrMessageDocument(sender.getMyOscarId(), providerFullName, message);
    }

    public static IndivoDocumentType getPhrMessageDocument(String phrId, String providerFullName,MessageType message) throws JAXBException, IndivoException {
        JAXBContext messageContext = JAXBContext.newInstance(MessageType.class.getPackage().getName());
        JAXBContext documentContext = JAXBContext.newInstance(IndivoDocumentType.class.getPackage().getName());

        org.indivo.xml.phr.DocumentGenerator generator = new org.indivo.xml.phr.DocumentGenerator();
        org.indivo.xml.JAXBUtils jaxbUtils = new org.indivo.xml.JAXBUtils();
        org.indivo.xml.phr.message.ObjectFactory msgFactory = new org.indivo.xml.phr.message.ObjectFactory();
        Message msgelement = msgFactory.createMessage(message);

        Element element = JAXBUtils.marshalToElement(msgelement, messageContext);
        IndivoDocumentType document = DocumentGenerator.generateDefaultDocument(phrId, providerFullName, PHRDocument.PHR_ROLE_PROVIDER, DocumentClassificationUrns.MESSAGE, ContentTypeQNames.MESSAGE, element);
        return document;
    }

    private MessageType getPhrMessage(Long myOscarUserId, String priorThreadMessage, String subject, String messageBody, List<String> attachedDocumentIds) {
        MessageType message = new MessageType();
// this code can't possibly be run, its using the wrong client
        message.setRecipient(""+myOscarUserId);
        message.setPriorThreadMessageId(priorThreadMessage);
        message.setSubject(subject);
        message.setMessageContent(toTextMessageContent(messageBody));
        message.setContentType(org.indivo.xml.phr.urns.ContentTypeQNames.TEXT);
        for (String attachedDocumentId: attachedDocumentIds) {
            DocumentReferenceType reference = new DocumentReferenceType();
            reference.setDocumentIndex(attachedDocumentId);
            message.getReferencedIndivoDocuments().add(reference);
        }
        return message;
    }

}
