package org.caisi.model;

import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;

public class EChart extends BaseObject {
	private Long id;
	private Date timeStamp;
	private int demographicNo;
	private String providerNo;
	private String subject;
	private String socialHistory;
	private String familyHistory;
	private String medicalHistory;
	private String reminders;
	private String encounter;
	private String ongoingConcerns;

	public int getDemographicNo() {
		return demographicNo;
	}
	public void setDemographicNo(int demographicNo) {
		this.demographicNo = demographicNo;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long chartId) {
		id = chartId;
	}
	public String getEncounter() {
		return encounter;
	}
	public void setEncounter(String encounter) {
		this.encounter = encounter;
	}
	public String getFamilyHistory() {
		return familyHistory;
	}
	public void setFamilyHistory(String familyHistory) {
		this.familyHistory = familyHistory;
	}
	public String getMedicalHistory() {
		return medicalHistory;
	}
	public void setMedicalHistory(String medicalHistory) {
		this.medicalHistory = medicalHistory;
	}
	public String getProviderNo() {
		return providerNo;
	}
	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}
	public String getReminders() {
		return reminders;
	}
	public void setReminders(String reminders) {
		this.reminders = reminders;
	}
	public String getSocialHistory() {
		return socialHistory;
	}
	public void setSocialHistory(String socialHistory) {
		this.socialHistory = socialHistory;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getOngoingConcerns() {
		return ongoingConcerns;
	}
	public void setOngoingConcerns(String ongoingConcerns) {
		this.ongoingConcerns = ongoingConcerns;
	}
	
}
