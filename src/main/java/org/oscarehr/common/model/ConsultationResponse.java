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


package org.oscarehr.common.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.util.Date;

/**
 *
 * @author Ronnie Cheng
 */
@Entity
@Table(name = "consultationResponse")
public class ConsultationResponse extends AbstractModel<Integer> implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "responseId")
    private Integer id;
    @Temporal(TemporalType.DATE)
    private Date responseDate;
    @Temporal(TemporalType.DATE)
    private Date referralDate;
    private Integer referringDocId; //reference professionalSpecialist, service="Referring Doctor"
	@Temporal(TemporalType.DATE)
	private Date appointmentDate;	
	@Temporal(TemporalType.TIME)
	private Date appointmentTime;
	private Date followUpDate;
	private String appointmentNote;
    private String referralReason; 
    private String examination;
    private String impression;
    private String plan;
    private String clinicalInfo; 
	private String currentMeds;
    private String concurrentProblems;
    private String allergies;
    private String providerNo;
    private Integer demographicNo;
    private String status;
    private String sendTo;
    private String urgency;
    private String signatureImg;
    private String letterheadName;
    private String letterheadAddress;
    private String letterheadPhone;
    private String letterheadFax;
    
    @Override
    public Integer getId() {
		return id;
	}

	public Date getResponseDate() {
		return responseDate;
	}

	public void setResponseDate(Date responseDate) {
		this.responseDate = responseDate;
	}

	public Date getReferralDate() {
		return referralDate;
	}

	public void setReferralDate(Date referralDate) {
		this.referralDate = referralDate;
	}

	public Integer getReferringDocId() {
		return referringDocId;
	}

	public void setReferringDocId(Integer referringDocId) {
		this.referringDocId = referringDocId;
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

	public Date getFollowUpDate() {
		return followUpDate;
	}

	public void setFollowUpDate(Date followUpDate) {
		this.followUpDate = followUpDate;
	}
	
	public String getAppointmentNote() {
		return appointmentNote;
	}
	
	public void setAppointmentNote(String appointmentNote) {
		this.appointmentNote = appointmentNote;
	}

	public String getReferralReason() {
		return referralReason;
	}

	public void setReferralReason(String referralReason) {
		this.referralReason = referralReason;
	}

	public String getExamination() {
		return examination;
	}

	public void setExamination(String examination) {
		this.examination = examination;
	}

	public String getImpression() {
		return impression;
	}

	public void setImpression(String impression) {
		this.impression = impression;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public String getClinicalInfo() {
		return clinicalInfo;
	}

	public void setClinicalInfo(String clinicalInfo) {
		this.clinicalInfo = clinicalInfo;
	}

	public String getCurrentMeds() {
		return currentMeds;
	}

	public void setCurrentMeds(String currentMeds) {
		this.currentMeds = currentMeds;
	}

	public String getConcurrentProblems() {
		return concurrentProblems;
	}

	public void setConcurrentProblems(String concurrentProblems) {
		this.concurrentProblems = concurrentProblems;
	}

	public String getAllergies() {
		return allergies;
	}

	public void setAllergies(String allergies) {
		this.allergies = allergies;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public Integer getDemographicNo() {
		return demographicNo;
	}

	public void setDemographicNo(Integer demographicNo) {
		this.demographicNo = demographicNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSendTo() {
		return sendTo;
	}

	public void setSendTo(String sendTo) {
		this.sendTo = sendTo;
	}

	public String getUrgency() {
		return urgency;
	}

	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}

	public String getSignatureImg() {
		return signatureImg;
	}

	public void setSignatureImg(String signatureImg) {
		this.signatureImg = signatureImg;
	}

	public String getLetterheadName() {
		return letterheadName;
	}

	public void setLetterheadName(String letterheadName) {
		this.letterheadName = letterheadName;
	}

	public String getLetterheadAddress() {
		return letterheadAddress;
	}

	public void setLetterheadAddress(String letterheadAddress) {
		this.letterheadAddress = letterheadAddress;
	}

	public String getLetterheadPhone() {
		return letterheadPhone;
	}

	public void setLetterheadPhone(String letterheadPhone) {
		this.letterheadPhone = letterheadPhone;
	}

	public String getLetterheadFax() {
		return letterheadFax;
	}

	public void setLetterheadFax(String letterheadFax) {
		this.letterheadFax = letterheadFax;
	}
}
