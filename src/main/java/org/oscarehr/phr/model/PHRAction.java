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
import org.oscarehr.util.MiscUtils;

public class PHRAction {

	private static final Logger logger = MiscUtils.getLogger();

	// Action type
	public static final int ACTION_NOT_SET = 0;
	public static final int ACTION_ADD = 1;
	public static final int ACTION_UPDATE = 2;

	public static final int STATUS_OTHER_ERROR = 7; // i.e. the annotation failed to find the document it referenced when it was being sent
	public static final int STATUS_ON_HOLD = 6; // usually means the provider approved an action, but something has to be done before it is sent off
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
	private Long senderMyOscarUserId;
	private String receiverOscar;
	private int receiverType;
	private Long receiverMyOscarUserId;
	private int actionType;
	private String phrClassification;
	private String oscarId;
	private String phrIndex; // if updating
	private String docContent;
	private int status;
	private String phrType;

	private PHRMessage phrMessage = null; // usually null

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
		byte[] docContentBytes = JAXBUtils.marshalToByteArray(new IndivoDocument(document), docContext);
		String docContentStr = new String(docContentBytes);
		this.setDocContent(docContentStr);
	}

	public PHRMessage getPhrMessage() throws Exception {
		// parses only once
		if (phrMessage == null) {
			logger.debug("Parsing Message");
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
		for (PHRAction action : actions) {
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


	public Long getSenderMyOscarUserId() {
    	return (senderMyOscarUserId);
    }

	public void setSenderMyOscarUserId(Long senderMyOscarUserId) {
    	this.senderMyOscarUserId = senderMyOscarUserId;
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

	public Long getReceiverMyOscarUserId() {
		return (receiverMyOscarUserId);
	}

	public void setReceiverMyOscarUserId(Long receiverMyOscarUserId) {
		this.receiverMyOscarUserId = receiverMyOscarUserId;
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
