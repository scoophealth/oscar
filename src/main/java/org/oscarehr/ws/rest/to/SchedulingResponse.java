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
package org.oscarehr.ws.rest.to;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.oscarehr.ws.rest.to.model.AppointmentStatusTo1;
import org.oscarehr.ws.rest.to.model.AppointmentTo1;
import org.oscarehr.ws.rest.to.model.AppointmentTypeTo1;
import org.oscarehr.ws.rest.to.model.LookupListItemTo1;

@XmlRootElement
public class SchedulingResponse  implements Serializable {

	private static final long serialVersionUID = 1L;

	private AppointmentTo1 appointment;
	
	private List<AppointmentStatusTo1> statuses;
	
	private List<AppointmentTypeTo1> types;
	
	private List<LookupListItemTo1> reasons;
	
	private List<AppointmentTo1> appointments;
	
	
	public SchedulingResponse() {
		
	}

	public AppointmentTo1 getAppointment() {
		return appointment;
	}

	public void setAppointment(AppointmentTo1 appointment) {
		this.appointment = appointment;
	}

	public List<AppointmentStatusTo1> getStatuses() {
		return statuses;
	}

	public void setStatuses(List<AppointmentStatusTo1> statuses) {
		this.statuses = statuses;
	}

	public List<AppointmentTypeTo1> getTypes() {
		return types;
	}

	public void setTypes(List<AppointmentTypeTo1> types) {
		this.types = types;
	}

	public List<LookupListItemTo1> getReasons() {
		return reasons;
	}

	public void setReasons(List<LookupListItemTo1> reasons) {
		this.reasons = reasons;
	}

	public List<AppointmentTo1> getAppointments() {
		return appointments;
	}

	public void setAppointments(List<AppointmentTo1> appointments) {
		this.appointments = appointments;
	}
	
	
	
}
