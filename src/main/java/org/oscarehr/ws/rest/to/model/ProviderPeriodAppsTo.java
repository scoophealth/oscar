/**
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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
 */


package org.oscarehr.ws.rest.to.model;

import java.util.Date;

import org.oscarehr.common.model.Appointment;

public class ProviderPeriodAppsTo {
	
	private Integer appointmentNo;
	private String providerNo;
	private Date appointmentDate;
	private Integer demographicNo;
	private String notes;
	private String location;
	private String resources;
	private String status;
	private String lastUpdateUser;
	private Long updateDatetime;
	private String name;
	
	public ProviderPeriodAppsTo() {
		super();
	}

	public ProviderPeriodAppsTo(Appointment a) {
		super();
		this.appointmentNo = a.getId();
		this.providerNo = a.getProviderNo();
		this.appointmentDate = a.getAppointmentDate();
		this.demographicNo = a.getDemographicNo();
		this.notes = a.getNotes();
		this.location = a.getLocation();
		this.resources = a.getResources();
		this.status = a.getStatus();
		this.lastUpdateUser = a.getLastUpdateUser();
		this.updateDatetime = a.getUpdateDateTime().getTime()/1000;
		this.name = a.getName();
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

	public String getLastUpdateUser() {
		return lastUpdateUser;
	}

	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	public Long getUpdateDatetime() {
		return updateDatetime;
	}

	public void setUpdateDatetime(Long updateDatetime) {
		this.updateDatetime = updateDatetime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ProviderPeriodAppsTo [appointmentNo=" + appointmentNo + ", providerNo=" + providerNo
				+ ", appointmentDate=" + appointmentDate + ", demographicNo=" + demographicNo + ", notes=" + notes
				+ ", location=" + location + ", resources=" + resources + ", status=" + status + ", lastUpdateUser="
				+ lastUpdateUser + ", updateDatetime=" + updateDatetime + ", name=" + name + "]";
	}

}
