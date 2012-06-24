/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.caisi.model;

import java.util.Date;

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
