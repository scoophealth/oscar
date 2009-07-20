/*
 * PHRAction.java
 *
 * Created on June 12, 2007, 1:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.oscarehr.phr.model;

import java.io.StringReader;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.indivo.IndivoException;
import org.indivo.xml.JAXBUtils;
import org.indivo.xml.phr.document.IndivoDocument;
import org.indivo.xml.phr.document.IndivoDocumentType;

/**
 *
 * @author Paul
 */

/*
CREATE TABLE `phr_actions` (
  `id` int(11) NOT NULL auto_increment,
  `datetime_queued` datetime default NULL,
  `datetime_sent` datetime default NULL,
  `sender_oscar` varchar(11) default NULL,
  `sender_type` int(1) default NULL,
  `sender_phr` varchar(255) default NULL,
  `receiver_oscar` varchar(11) default NULL,
  `receiver_type` int(1) default NULL,
  `receiver_phr` varchar(255) default NULL,
  `action_type` int(1) default '0',
  `phr_classification` varchar(250) default NULL,
  `oscar_id` varchar(100) default NULL,
  `phr_index` varchar(70) default NULL,
  `doc_content` text,
  `sent` tinyint(1) default 0,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM;*/

public class PHRAction {
    
    Logger log = Logger.getLogger(PHRAction.class);
    
    //Action type
    public static final int ACTION_NOT_SET = 0;
    public static final int ACTION_ADD = 1;
    public static final int ACTION_UPDATE = 2;

    public static final int STATUS_OTHER_ERROR = 7; //i.e. the annotation failed to find the document it referenced when it was being sent
    public static final int STATUS_ON_HOLD = 6;  //usually means the provider approved an action, but something has to be done before it is sent off
    public static final int STATUS_APPROVAL_PENDING = 5;
    public static final int STATUS_NOT_SENT_DELETED = 4;
    public static final int STATUS_NOT_AUTHORIZED = 3;
    public static final int STATUS_SENT = 2;
    public static final int STATUS_SEND_PENDING = 1;
    public static final int STATUS_NOT_SET = 0;
    
    private int id;
    private Date dateQueued;
    private Date dateSent;
    private String senderOscar;
    private int senderType;
    private String senderPhr;
    private String receiverOscar;
    private int receiverType;
    private String receiverPhr;
    private int actionType;
    private String phrClassification;
    private String oscarId;
    private String phrIndex;  //if updating
    private String docContent;
    private int status;
    private String phrType;
    
    private PHRMessage phrMessage = null;    //usually null

    /** Creates a new instance of PHRAction */
    public PHRAction() {
    }

    public IndivoDocumentType getIndivoDocument() throws JAXBException {
        JAXBContext docContext = JAXBContext.newInstance("org.indivo.xml.phr.document");
        Unmarshaller unmarshaller = docContext.createUnmarshaller();
        StringReader strr = new StringReader(this.getDocContent());
        JAXBElement docEle = (JAXBElement) unmarshaller.unmarshal(strr);
        IndivoDocumentType doc = (IndivoDocumentType) docEle.getValue();
        return doc;
    }

    public void setIndivoDocument(IndivoDocumentType document) throws JAXBException, IndivoException {
        JAXBContext docContext = JAXBContext.newInstance(IndivoDocumentType.class.getPackage().getName());
        byte[] docContentBytes = JAXBUtils.marshalToByteArray((JAXBElement) new IndivoDocument(document), docContext);
        String docContentStr = new String(docContentBytes);
        this.setDocContent(docContentStr);
    }
    
    public PHRMessage getPhrMessage() throws Exception {
        //parses only once
        if (phrMessage == null) {
            log.debug("Parsing Message");
            this.phrMessage = new PHRMessage(getIndivoDocument());
        }
        return phrMessage;
    }
    
    public boolean sameOscarObject(PHRAction action) {
        if (action.getPhrClassification().equals(this.getPhrClassification()) && (action.getOscarId().equals(this.getOscarId()))) {
            return true;
        }
        return false;
    }
    
    public static List updateIndexes(String classification, String oscarId, String newPhrIndex, List<PHRAction> actions) {
        for (PHRAction action :actions) {
            if (action.getPhrClassification().equals(classification) && (action.getOscarId() != null) && (action.getOscarId().equals(oscarId))) {
                action.setPhrIndex(newPhrIndex);
            }
        }
        return actions;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateQueued() {
        return dateQueued;
    }

    public void setDateQueued(Date dateQueued) {
        this.dateQueued = dateQueued;
    }

    public Date getDateSent() {
        return dateSent;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

    public String getSenderOscar() {
        return senderOscar;
    }

    public void setSenderOscar(String senderOscar) {
        this.senderOscar = senderOscar;
    }

    public int getSenderType() {
        return senderType;
    }

    public void setSenderType(int senderType) {
        this.senderType = senderType;
    }

    public String getSenderPhr() {
        return senderPhr;
    }

    public void setSenderPhr(String senderPhr) {
        this.senderPhr = senderPhr;
    }

    public String getReceiverOscar() {
        return receiverOscar;
    }

    public void setReceiverOscar(String receiverOscar) {
        this.receiverOscar = receiverOscar;
    }

    public int getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(int receiverType) {
        this.receiverType = receiverType;
    }

    public String getReceiverPhr() {
        return receiverPhr;
    }

    public void setReceiverPhr(String receiverPhr) {
        this.receiverPhr = receiverPhr;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public String getDocContent() {
        return docContent;
    }

    public void setDocContent(String docContent) {
        this.docContent = docContent;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPhrIndex() {
        return phrIndex;
    }

    public void setPhrIndex(String phrIndex) {
        this.phrIndex = phrIndex;
    }

    public String getPhrClassification() {
        return phrClassification;
    }

    public void setPhrClassification(String phrClassification) {
        this.phrClassification = phrClassification;
    }

    public String getOscarId() {
        return oscarId;
    }

    public void setOscarId(String oscarId) {
        this.oscarId = oscarId;
    }
    
    public String getPhrType() {
        return phrType;
    }

    public void setPhrType(String phrType) {
        this.phrType = phrType;
    }
    
}
