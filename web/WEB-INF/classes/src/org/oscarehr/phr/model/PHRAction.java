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
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
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
    //Action type
    public static final int ACTION_NOT_SET = 0;
    public static final int ACTION_ADD = 1;
    public static final int ACTION_UPDATE = 2;

            
    public static final int STATUS_SENT = 2;
    public static final int STATUS_SENT_PENDING = 1;
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
    private int sent;

    /** Creates a new instance of PHRAction */
    public PHRAction() {
    }

    public IndivoDocumentType getPhrDocument() throws JAXBException {
        JAXBContext docContext = JAXBContext.newInstance("org.indivo.xml.phr.document");
        Unmarshaller unmarshaller = docContext.createUnmarshaller();
        StringReader strr = new StringReader(this.getDocContent());
        JAXBElement docEle = (JAXBElement) unmarshaller.unmarshal(strr);
        IndivoDocumentType doc = (IndivoDocumentType) docEle.getValue();
        return doc;
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

    public int getSent() {
        return sent;
    }

    public void setSent(int sent) {
        this.sent = sent;
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
    
}
