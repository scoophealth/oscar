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

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppointmentExtTo {
	private Integer appointmentNo;
	private String providerNo;
	private Date appointmentDate;
	private Date startTime;
	private Integer demographicNo;
	private String notes;
	private String location;
	private String resources;
	private String status; 
	private String lastName;
	private String firstName; 
	private String phone;
	private String phone2;
	private String email; 
	private String demoCell;
	private String reminderPreference;
	private String hPhoneExt;
	private String wPhoneExt;
	
	public AppointmentExtTo() {
		super();
	}

//	SELECT a.appointment_no, a.provider_no, a.appointment_date, a.start_time, a.demographic_no, a.notes, a.location, 
//	a.resources, a.status, " + 
//	"d.last_name, d.first_name, d.phone, d.phone2, d.email, " + 
//	"e1.value as demo_cell, e2.value as reminderPreference, e3.value as hPhoneExt, e4.value as wPhoneExt 
	public AppointmentExtTo(Integer appointmentNo, String providerNo, Date appointmentDate, Date startTime, 
			Integer demographicNo, String notes, String location,String resources, Character status,
			String lastName, String firstName, String phone, String phone2, String email,
			String demoCell, String reminderPreference, String hPhoneExt, String wPhoneExt) {
		super();
		this.appointmentNo = appointmentNo;
		this.providerNo = providerNo;
		this.appointmentDate = appointmentDate;
		this.startTime = startTime;
		this.demographicNo = demographicNo;
		this.notes = notes;
		this.location = location;
		this.resources = resources;
		this.status = status != null ? Character.toString(status) : null;
		this.lastName = lastName;
		this.firstName = firstName;
		this.phone = phone;
		this.phone2 = phone2;
		this.email = email;
		this.demoCell = demoCell;
		this.reminderPreference = reminderPreference;
		this.hPhoneExt = hPhoneExt;
		this.wPhoneExt = wPhoneExt;
	}

	public Integer getAppointmentNo() {
		return appointmentNo;
	}

	public void setAppointmentNo(Integer appointmentNo) {
		this.appointmentNo = appointmentNo;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public Date getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(Date appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Integer getDemographicNo() {
		return demographicNo;
	}

	public void setDemographicNo(Integer demographicNo) {
		this.demographicNo = demographicNo;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getResources() {
		return resources;
	}

	public void setResources(String resources) {
		this.resources = resources;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDemoCell() {
		return demoCell;
	}

	public void setDemoCell(String demoCell) {
		this.demoCell = demoCell;
	}

	public String getReminderPreference() {
		return reminderPreference;
	}

	public void setReminderPreference(String reminderPreference) {
		this.reminderPreference = reminderPreference;
	}

	public String gethPhoneExt() {
		return hPhoneExt;
	}

	public void sethPhoneExt(String hPhoneExt) {
		this.hPhoneExt = hPhoneExt;
	}

	public String getwPhoneExt() {
		return wPhoneExt;
	}

	public void setwPhoneExt(String wPhoneExt) {
		this.wPhoneExt = wPhoneExt;
	}

	@Override
	public String toString() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
		return "AppointmentExtTo2 [appointmentNo=" + appointmentNo + ", providerNo=" + providerNo
				+ ", appointmentDate=" + appointmentDate != null ? df.format(appointmentDate) : "null"
				+ ", startTime=" + startTime != null ? dft.format(startTime) : "null" 
				+ ", demographicNo=" + demographicNo + ", notes=" + notes
				+ ", location=" + location + ", resources=" + resources + ", status=" + status 
				+ ", lastName=" + lastName + ", firstName=" + firstName + ", phone=" + phone + ", phone2=" + phone2
				+ ", email=" + email + ", demoCell=" + demoCell + ", reminderPreference=" + reminderPreference 
				+ ", hPhoneExt=" + hPhoneExt + ", wPhoneExt=" + wPhoneExt + "]";
	}
	
	

}
