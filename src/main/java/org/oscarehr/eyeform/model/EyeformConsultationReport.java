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


package org.oscarehr.eyeform.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Table;
import org.oscarehr.common.model.AbstractModel;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Provider;

@Entity
@Table(name="EyeformConsultationReport")
public class EyeformConsultationReport extends AbstractModel<Integer> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	private int referralId;
	private int greeting;
	private int appointmentNo;
	
	@Temporal(TemporalType.DATE)
	private Date appointmentDate;
	
	@Temporal(TemporalType.TIME)
	private Date appointmentTime;
	
	private int demographicNo;
	private String reason;
	private String type;
	private String cc;
	private String memo;
	private String clinicalInfo;
	private String currentMeds;
	private String allergies;
	private String providerNo;
	private String status;
	private String sendTo;
	private String examination;
	private String concurrentProblems;
	private String impression;
	private String plan;
	private String urgency;
	private int patientWillBook;
	
	@Transient
	private Demographic demographic = null;
	@Transient
	private Provider provider = null;
	@Transient
	private String referralNo;
	@Transient
	private String referralFax;
	
	private Integer siteId;
	
	private String contactId; // demographic's contacts which will be displayed as the first cc doctor
	@Column(name="contact_type", length=10)
	private int contactType; // 0: providers (Internal)  2: professional (external, different from the specialist in admin page)
	private int otherReferralId; // people from the specialist in admin page
	
	public EyeformConsultationReport() {
		date = new Date();
	}
	
	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public int getReferralId() {
		return referralId;
	}


	public void setReferralId(int referralId) {
		this.referralId = referralId;
	}


	public int getGreeting() {
		return greeting;
	}


	public void setGreeting(int greeting) {
		this.greeting = greeting;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getAppointmentNo() {
		return appointmentNo;
	}


	public void setAppointmentNo(int appointmentNo) {
		this.appointmentNo = appointmentNo;
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


	public String getReason() {
		return reason;
	}


	public void setReason(String reason) {
		this.reason = reason;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getCc() {
		return cc;
	}


	public void setCc(String cc) {
		this.cc = cc;
	}


	public String getMemo() {
		return memo;
	}


	public void setMemo(String memo) {
		this.memo = memo;
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


	public String getExamination() {
		return examination;
	}


	public void setExamination(String examination) {
		this.examination = examination;
	}


	public String getConcurrentProblems() {
		return concurrentProblems;
	}


	public void setConcurrentProblems(String concurrentProblems) {
		this.concurrentProblems = concurrentProblems;
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


	public String getUrgency() {
		return urgency;
	}


	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}


	public int getPatientWillBook() {
		return patientWillBook;
	}


	public void setPatientWillBook(int patientWillBook) {
		this.patientWillBook = patientWillBook;
	}


	public Integer getId() {
		return id;
	}


	public int getDemographicNo() {
		return demographicNo;
	}


	public void setDemographicNo(int demographicNo) {
		this.demographicNo = demographicNo;
	}


	public Demographic getDemographic() {
		return demographic;
	}


	public void setDemographic(Demographic demographic) {
		this.demographic = demographic;
	}


	public Provider getProvider() {
		return provider;
	}


	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public String getReferralNo() {
		return referralNo;
	}

	public void setReferralNo(String referralNo) {
		this.referralNo = referralNo;
	}
	
	public String getReferralFax() {
		return referralFax;
	}

	public void setReferralFax(String referralFax) {
		this.referralFax = referralFax;
	}

	public Integer getSiteId() {
		return this.siteId;
	}
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public int getContactType() {
		return contactType;
	}

	public void setContactType(int contactType) {
		this.contactType = contactType;
	}

	public int getOtherReferralId() {
		return otherReferralId;
	}

	public void setOtherReferralId(int otherReferralId) {
		this.otherReferralId = otherReferralId;
	}
	
	
}
