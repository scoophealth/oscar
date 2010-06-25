/*
 * Copyright (c) 2010. Department of Family Medicine, McMaster University. All Rights Reserved.
 * 
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
package org.oscarehr.common.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;

@Entity
@Table(name = "consultationRequests")
public class ConsultationRequest extends AbstractModel<Integer> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "requestId")
	private Integer id;
	
	@Column(name = "referalDate")
	private Date referralDate;
	
	private Integer serviceId;

	@Column(name = "specId")
	private Integer specialistId;

	@Temporal(TemporalType.DATE)
	private Date appointmentDate;	
	@Temporal(TemporalType.TIME)
	private Date appointmentTime;

	@Column(name = "reason")
	private String reasonForReferral;
	
	private String clinicalInfo;
	private String currentMeds;
	private String allergies;
	private String providerNo;

	@Column(name = "demographicNo")
	private Integer demographicId;

	private String status;
	private String statusText;
	private String sendTo;
	private String concurrentProblems;
	private String urgency;
	private boolean patientWillBook;
	
	@Override
    public Integer getId() {
	    return(id);
    }

	public Date getReferralDate() {
    	return referralDate;
    }

	public void setReferralDate(Date referralDate) {
    	this.referralDate = referralDate;
    }

	public Integer getServiceId() {
    	return serviceId;
    }

	public void setServiceId(Integer serviceId) {
    	this.serviceId = serviceId;
    }

	public Integer getSpecialistId() {
    	return specialistId;
    }

	public void setSpecialistId(Integer specialistId) {
    	this.specialistId = specialistId;
    }

	public Date getAppointmentDate() {
    	return appointmentDate;
    }

	public void setAppointmentDate(Date appointmentDate) {
    	this.appointmentDate = appointmentDate;
    }

	public Date getAppointmentTime() {
    	return appointmentTime;
    }

	public void setAppointmentTime(Date appointmentTime) {
    	this.appointmentTime = appointmentTime;
    }

	public String getReasonForReferral() {
    	return reasonForReferral;
    }

	public void setReasonForReferral(String reasonForReferral) {
    	this.reasonForReferral = StringUtils.trimToNull(reasonForReferral);
    }

	public String getClinicalInfo() {
    	return clinicalInfo;
    }

	public void setClinicalInfo(String clinicalInfo) {
    	this.clinicalInfo = StringUtils.trimToNull(clinicalInfo);
    }

	public String getCurrentMeds() {
    	return currentMeds;
    }

	public void setCurrentMeds(String currentMeds) {
    	this.currentMeds = StringUtils.trimToNull(currentMeds);
    }

	public String getAllergies() {
    	return allergies;
    }

	public void setAllergies(String allergies) {
    	this.allergies = StringUtils.trimToNull(allergies);
    }

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = StringUtils.trimToNull(providerNo);
    }

	public Integer getDemographicId() {
    	return demographicId;
    }

	public void setDemographicId(Integer demographicId) {
    	this.demographicId = demographicId;
    }

	public String getStatus() {
    	return status;
    }

	public void setStatus(String status) {
    	this.status = StringUtils.trimToNull(status);
    }

	public String getStatusText() {
    	return statusText;
    }

	public void setStatusText(String statusText) {
    	this.statusText = StringUtils.trimToNull(statusText);
    }

	public String getSendTo() {
    	return sendTo;
    }

	public void setSendTo(String sendTo) {
    	this.sendTo = StringUtils.trimToNull(sendTo);
    }

	public String getConcurrentProblems() {
    	return concurrentProblems;
    }

	public void setConcurrentProblems(String concurrentProblems) {
    	this.concurrentProblems = StringUtils.trimToNull(concurrentProblems);
    }

	public String getUrgency() {
    	return urgency;
    }

	public void setUrgency(String urgency) {
    	this.urgency = StringUtils.trimToNull(urgency);
    }

	public boolean isPatientWillBook() {
    	return patientWillBook;
    }

	public void setPatientWillBook(boolean patientWillBook) {
    	this.patientWillBook = patientWillBook;
    }
}
