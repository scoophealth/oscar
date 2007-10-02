
/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/


package org.oscarehr.casemgmt.model;

import java.util.Date;

import org.caisi.model.BaseObject;

public class CaseManagementCPP extends BaseObject {
	private Long id;
	private String demographic_no;
        private String provider_no;
	private String socialHistory="";
	private String familyHistory="";
	private String medicalHistory="";
	private String ongoingConcerns="";
	private String reminders="";
	private Date update_date;
	
	private String primaryPhysician="";
	private String primaryCounsellor="";
	private String pastMedications="";
	private String otherFileNumber="";
	private String otherSupportSystems="";
	
	public CaseManagementCPP() {
		
	}
	
        public String getProvider_no() {
            return this.provider_no;
        }
        
        public void setProvider_no(String provider_no) {
            this.provider_no = provider_no;
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
	
	public String getPastMedications() {
		return pastMedications;
	}

	public void setPastMedications(String pastMedications) {
		this.pastMedications = pastMedications;
	}

	public String getOtherFileNumber() {
		return otherFileNumber;
	}

	public void setOtherFileNumber(String otherFileNumber) {
		this.otherFileNumber = otherFileNumber;
	}

	public String getOtherSupportSystems() {
		return otherSupportSystems;
	}

	public void setOtherSupportSystems(String otherSupportSystems) {
		this.otherSupportSystems = otherSupportSystems;
	}
	
}
