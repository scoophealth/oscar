package org.oscarehr.casemgmt.model;

import java.util.Date;

import org.caisi.model.BaseObject;

public class CaseManagementCPP extends BaseObject {
	private Long id;
	private String demographic_no;
	private String socialHistory="";
	private String familyHistory="";
	private String medicalHistory="";
	private String ongoingConcerns="";
	private String reminders="";
	private Date update_date;
	
	private String primaryPhysician="";
	private String primaryCounsellor="";
	
	
	public CaseManagementCPP() {
		
	}
	
	public String getPrimaryCounsellor() {
		return primaryCounsellor;
	}
	public String getPrimaryPhysician() {
		return primaryPhysician;
	}
	public void setPrimaryCounsellor(String primaryCounsellor) {
		this.primaryCounsellor = primaryCounsellor;
	}
	public void setPrimaryPhysician(String primaryPhysician) {
		this.primaryPhysician = primaryPhysician;
	}
	public String getFamilyHistory() {
		return familyHistory;
	}
	public void setFamilyHistory(String familyHistory) {
		this.familyHistory = familyHistory;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMedicalHistory() {
		return medicalHistory;
	}
	public void setMedicalHistory(String medicalHistory) {
		this.medicalHistory = medicalHistory;
	}
	public String getOngoingConcerns() {
		return ongoingConcerns;
	}
	public void setOngoingConcerns(String ongoingConcerns) {
		this.ongoingConcerns = ongoingConcerns;
	}
	public String getDemographic_no() {
		return demographic_no;
	}
	public void setDemographic_no(String provider_no) {
		this.demographic_no = provider_no;
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
	public Date getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}
}
