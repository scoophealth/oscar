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
package org.oscarehr.billing.CA.BC.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.oscarehr.common.model.AbstractModel;

@Entity
@Table(name="hl7_msh")
public class Hl7Msh extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="msh_id")
	private Integer id;

	@Column(name="message_id")
	private int messageId;
	
	private String seperator;
	
	@Column(name="encoding_characters")
	private String encoding;
	
	@Column(name="sending_application")
	private String sendingApp;
	
	@Column(name="sending_facility")
	private String sendingFacility;
	
	@Column(name="receiving_application")
	private String receivingApp;
	
	@Column(name="receiving_facility")
	private String receivingFacility;
	
	@Column(name="date_time_of_message")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateTime;
	
	private String security;
	
	@Column(name="message_type")
	private String messageType;
	
	@Column(name="message_control_id")
	private String controlId;
	
	@Column(name="processing_id")
	private String processingId;
	
	@Column(name="version_id")
	private String versionId;
	
	@Column(name="sequence_number")
	private String sequenceNumber;
	
	@Column(name="continuation_pointer")
	private String continuationPointer;
	
	@Column(name="accept_acknowledgment_type")
	private String acceptAckType;
	
	@Column(name="application_acknowledge_type")
	private String applicationAckType;
	
	@Column(name="country_code")
	private String countryCode;
	
	@Column(name="character_set")
	private String characterSet;
	
	@Column(name="principal_language_of_message")
	private String language;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public String getSeperator() {
		return seperator;
	}

	public void setSeperator(String seperator) {
		this.seperator = seperator;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getSendingApp() {
		return sendingApp;
	}

	public void setSendingApp(String sendingApp) {
		this.sendingApp = sendingApp;
	}

	public String getSendingFacility() {
		return sendingFacility;
	}

	public void setSendingFacility(String sendingFacility) {
		this.sendingFacility = sendingFacility;
	}

	public String getReceivingApp() {
		return receivingApp;
	}

	public void setReceivingApp(String receivingApp) {
		this.receivingApp = receivingApp;
	}

	public String getReceivingFacility() {
		return receivingFacility;
	}

	public void setReceivingFacility(String receivingFacility) {
		this.receivingFacility = receivingFacility;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getSecurity() {
		return security;
	}

	public void setSecurity(String security) {
		this.security = security;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getControlId() {
		return controlId;
	}

	public void setControlId(String controlId) {
		this.controlId = controlId;
	}

	public String getProcessingId() {
		return processingId;
	}

	public void setProcessingId(String processingId) {
		this.processingId = processingId;
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public String getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getContinuationPointer() {
		return continuationPointer;
	}

	public void setContinuationPointer(String continuationPointer) {
		this.continuationPointer = continuationPointer;
	}



	public String getAcceptAckType() {
		return acceptAckType;
	}

	public void setAcceptAckType(String acceptAckType) {
		this.acceptAckType = acceptAckType;
	}

	public String getApplicationAckType() {
		return applicationAckType;
	}

	public void setApplicationAckType(String applicationAckType) {
		this.applicationAckType = applicationAckType;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCharacterSet() {
		return characterSet;
	}

	public void setCharacterSet(String characterSet) {
		this.characterSet = characterSet;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
	
}
