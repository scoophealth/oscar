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


package org.oscarehr.ws.transfer_objects;

import java.util.List;

import org.oscarehr.common.model.AppointmentType;

public final class AppointmentTypeTransfer {

	private Integer id=null;
	private String name = null;
	private String notes = null;
	private String reason = null;
	private String location = null;
	private String resources = null;
	private int duration;

	public Integer getId() {
		return (id);
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return (name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNotes() {
		return (notes);
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getReason() {
		return (reason);
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getLocation() {
		return (location);
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getResources() {
		return (resources);
	}

	public void setResources(String resources) {
		this.resources = resources;
	}

	public int getDuration() {
		return (duration);
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public static AppointmentTypeTransfer toTransfer(AppointmentType appointmentType) {
		if (appointmentType==null) return(null);
		
		AppointmentTypeTransfer appointmentTypeTransfer = new AppointmentTypeTransfer();
		appointmentTypeTransfer.setDuration(appointmentType.getDuration());
		appointmentTypeTransfer.setId(appointmentType.getId());
		appointmentTypeTransfer.setLocation(appointmentType.getLocation());
		appointmentTypeTransfer.setName(appointmentType.getName());
		appointmentTypeTransfer.setNotes(appointmentType.getNotes());
		appointmentTypeTransfer.setReason(appointmentType.getReason());
		appointmentTypeTransfer.setResources(appointmentType.getResources());

		return (appointmentTypeTransfer);
	}

	public static AppointmentTypeTransfer[] toTransfer(List<AppointmentType> appointmentTypes) {
		AppointmentTypeTransfer[] result = new AppointmentTypeTransfer[appointmentTypes.size()];

		for (int i = 0; i < appointmentTypes.size(); i++) {
			result[i] = toTransfer(appointmentTypes.get(i));
		}

		return (result);
	}
}
