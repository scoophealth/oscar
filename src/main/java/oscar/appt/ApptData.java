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

package oscar.appt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ApptData {
	String appointment_no;
	String provider_no;
	String appointment_date;
	String start_time;
	String end_time;
	String name;
	String demographic_no;
	String notes;
	String reasonCode;
	String reason;
	String location;
	String resources;
	String type;
	String style;
	String billing;
	String status;
	String createdatetime;
	String creator;
	String program;
	String remarks;
	String duration;
	String chart_no;
	String providerLastName;
	String providerFirstName;
	String ohipNo;
	String urgency;

	public String getAppointment_date() {
		return appointment_date;
	}

	public void setAppointment_date(String appointment_date) {
		this.appointment_date = appointment_date;
	}

	public String getAppointment_no() {
		return appointment_no;
	}

	public void setAppointment_no(String appointment_no) {
		this.appointment_no = appointment_no;
	}

	public String getBilling() {
		return billing;
	}

	public void setBilling(String billing) {
		this.billing = billing;
	}

	public String getCreatedatetime() {
		return createdatetime;
	}

	public void setCreatedatetime(String createdatetime) {
		this.createdatetime = createdatetime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getDemographic_no() {
		return demographic_no;
	}

	public void setDemographic_no(String demographic_no) {
		this.demographic_no = demographic_no;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getProviderNo() {
		return provider_no;
	}

	public void setProviderNo(String provider_no) {
		this.provider_no = provider_no;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getResources() {
		return resources;
	}

	public void setResources(String resources) {
		this.resources = resources;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getChart_no() {
		return chart_no;
	}

	public void setChart_no(String chart_no) {
		this.chart_no = chart_no;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	
	public Date getDateAppointmentDate() throws ParseException {
	    return stringToDateOrTime(this.appointment_date, true);
	}
	
	public Date getDateStartTime() throws ParseException {
	    return stringToDateOrTime(this.start_time, false);
	}
	
	public Date getDateEndTime() throws ParseException {
	    return stringToDateOrTime(this.end_time, false);
	}
	
	public String getProviderFirstName() {
	    return this.providerFirstName;
	}
	
	public void setProviderFirstName(String name) {
		this.providerFirstName = name;
	}
	
	public String getProviderLastName() {
	    return this.providerLastName;
	}
	
	public void setProviderLastName(String name) {
		this.providerLastName = name;
	}
	
	public String getOhipNo() {
	    return this.ohipNo;
	}
	
	public void setOhipNo(String ohipNo) {
		this.ohipNo = ohipNo;
	}

	private Date stringToDateOrTime(String s, boolean isDate) throws ParseException {
	    if (isDate) return new SimpleDateFormat("yyyy-MM-dd").parse(s);
	    else return new SimpleDateFormat("HH:mm:ss").parse(s);
	}

	public String getUrgency() {
    	return urgency;
    }

	public void setUrgency(String urgency) {
    	this.urgency = urgency;
    }

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}
	
	
}
