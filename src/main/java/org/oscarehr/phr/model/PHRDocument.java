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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.indivo.xml.phr.contact.ConciseContactInformationType;

import oscar.oscarClinic.ClinicData;

/**
 * @author jay
 */
public class PHRDocument implements Serializable {

	public static final int TYPE_NOT_SET = 0;
	// for senderOscar and receiverOscar
	public static final int TYPE_PROVIDER = 1;
	public static final int TYPE_DEMOGRAPHIC = 2;
	// for status sent msgs
	public static final String PHR_DOCUMENTS = "phr_documents";

	public static final String PHR_ROLE_PROVIDER = "provider";
	public static final String PHR_ROLE_DEMOGRAPHIC = "patient";

	public static final int STATUS_RECIEVED_SAVED_IN_OSCAR_TABLE = 6;
	public static final int STATUS_RECIEVED_NOT_SAVED_IN_OSCAR_TABLE = 5;
	public static final int STATUS_NOT_SENT_DELETED = PHRAction.STATUS_NOT_SENT_DELETED;
	public static final int STATUS_NOT_AUTHORIZED = PHRAction.STATUS_NOT_AUTHORIZED;
	public static final int STATUS_SENT = PHRAction.STATUS_SENT;
	public static final int STATUS_SEND_PENDING = PHRAction.STATUS_SEND_PENDING;
	public static final int STATUS_NOT_SET = PHRAction.STATUS_NOT_SET;

	public static final String CODE_ATC = "ATC";
	public static final String CODE_GCN_SEQNO = "GCN_SEQNO";
	public static final String CODE_REGIONALIDENTIFIER = "RegionalIdentifier";

	private int id;
	private String phrIndex;
	private String phrClassification;
	private Date dateExchanged;
	private Date dateSent;
	private String senderOscar;
	private int senderType;
	private Long senderMyOscarUserId;
	private Integer receiverOscar;
	private int receiverType;
	private Long receiverMyOscarUserId;
	private String docSubject;
	private String docContent;
	private int status;
	private int sent;
	private Map exts;

	/**
	 * Creates a new instance of PHRDocument
	 */
	public PHRDocument() {
	}

	public PHRAction getAction(int actionType, int actionStatus) {
		PHRAction action = new PHRAction();
		action.setActionType(actionType);
		action.setDateQueued(new Date());
		action.setDocContent(this.getDocContent());
		action.setReceiverOscar(""+this.getReceiverOscar());
		action.setReceiverType(this.getReceiverType());
		action.setReceiverMyOscarUserId(this.getReceiverMyOscarUserId());
		action.setSenderOscar(this.getSenderOscar());
		action.setSenderType(this.getSenderType());
		action.setSenderMyOscarUserId(this.getSenderMyOscarUserId());
		action.setPhrClassification(this.getPhrClassification());
		action.setStatus(actionStatus);
		return action;
	}

	public PHRAction getAction2(int actionType, int actionStatus) {
		PHRAction action = new PHRAction();
		action.setActionType(actionType);
		action.setDateQueued(new Date());
		action.setDocContent(this.getDocContent());

		action.setReceiverOscar(this.getSenderOscar());
		action.setReceiverType(this.getSenderType());
		action.setReceiverMyOscarUserId(this.getSenderMyOscarUserId());

		action.setSenderOscar(""+this.getReceiverOscar());
		action.setSenderType(this.getReceiverType());
		action.setSenderMyOscarUserId(this.getReceiverMyOscarUserId());
		action.setPhrClassification(this.getPhrClassification());
		action.setPhrIndex(this.getPhrIndex());
		action.setStatus(actionStatus);
		return action;
	}

	protected static XMLGregorianCalendar dateToXmlGregorianCalendar(Date date) throws DatatypeConfigurationException {
		DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(date);
		return datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
	}

	// not sure what to send here, just sending clinic name for tracking puproses
	protected static ConciseContactInformationType getClinicOrigin() {
		ConciseContactInformationType ccit = new ConciseContactInformationType();
		ClinicData clinic = new ClinicData();
		clinic.refreshClinicData();
		String clinicName = clinic.getClinicName();
		ccit.setOrganizationName(clinicName);
		return ccit;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Date getDateExchanged() {
		return dateExchanged;
	}

	public void setDateExchanged(Date dateExchanged) {
		this.dateExchanged = dateExchanged;
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

	public Integer getReceiverOscar() {
		return receiverOscar;
	}

	public void setReceiverOscar(Integer receiverOscar) {
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

	public String getDocSubject() {
		return docSubject;
	}

	public void setDocSubject(String docSubject) {
		this.docSubject = docSubject;
	}

	public String getDocContent() {
		return docContent;
	}

	public void setDocContent(String docContent) {
		this.docContent = docContent;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getSent() {
		return sent;
	}

	public void setSent(int sent) {
		this.sent = sent;
	}

	public Map getExts() {
		return exts;
	}

	public void setExts(Map exts) {
		this.exts = exts;
	}

	public boolean getRead()
	{
		return(PHRMessage.STATUS_READ==status);
	}
	
	public boolean getReplied()
	{
		return(PHRMessage.STATUS_REPLIED==status);
	}
	
	@Override
	public String toString() {
		return (ReflectionToStringBuilder.toString(this));
	}

}
