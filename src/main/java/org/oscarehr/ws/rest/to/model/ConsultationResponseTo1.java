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


package org.oscarehr.ws.rest.to.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ConsultationResponseTo1 implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_NOTHING = "1";
	private static final String DEFAULT_NON_URGENT = "2";
	private static final String DEFAULT_1 = "-1";
	
	private Integer id;
    private Date responseDate;
    private Date referralDate;
	private ProfessionalSpecialistTo1 referringDoctor;
	private Date appointmentDate;	
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
	private DemographicTo1 demographic;
	private String status = DEFAULT_NOTHING;
	private String sendTo = DEFAULT_1;
	private String urgency = DEFAULT_NON_URGENT;
    private String signatureImg;
    private String letterheadName;
    private String letterheadAddress;
    private String letterheadPhone;
    private String letterheadFax;
    private List<ConsultationAttachmentTo1> attachments;
	
	private List<LetterheadTo1> letterheadList;
	private List<ProfessionalSpecialistTo1> referringDoctorList;
	private List<FaxConfigTo1> faxList;
	private List<String> sendToList;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public ProfessionalSpecialistTo1 getReferringDoctor() {
		return referringDoctor;
	}
	public void setReferringDoctor(ProfessionalSpecialistTo1 referringDoctor) {
		this.referringDoctor = referringDoctor;
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
	public DemographicTo1 getDemographic() {
		return demographic;
	}
	public void setDemographic(DemographicTo1 demographic) {
		this.demographic = demographic;
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
	public List<ConsultationAttachmentTo1> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<ConsultationAttachmentTo1> attachments) {
		this.attachments = attachments;
	}
	
	public List<LetterheadTo1> getLetterheadList() {
		return letterheadList;
	}
	public void setLetterheadList(List<LetterheadTo1> letterheadList) {
		this.letterheadList = letterheadList;
	}
	public List<ProfessionalSpecialistTo1> getReferringDoctorList() {
		return referringDoctorList;
	}
	public void setReferringDoctorList(List<ProfessionalSpecialistTo1> referringDoctorList) {
		this.referringDoctorList = referringDoctorList;
	}
	public List<FaxConfigTo1> getFaxList() {
		return faxList;
	}
	public void setFaxList(List<FaxConfigTo1> faxList) {
		this.faxList = faxList;
	}
	public List<String> getSendToList() {
		return sendToList;
	}
	public void setSendToList(List<String> sendToList) {
		this.sendToList = sendToList;
	}
}
