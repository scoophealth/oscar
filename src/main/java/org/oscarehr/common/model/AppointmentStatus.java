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
package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.Table;

import oscar.appt.status.service.impl.AppointmentStatusMgrImpl;


@Entity
@Table(name="appointment_status")
public class AppointmentStatus extends AbstractModel<Integer> {

    public static final String APPOINTMENT_STATUS_HERE = "H";
    public static final String APPOINTMENT_STATUS_CANCELLED = "C";


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String status;
	
	private String description;
	
	private String color;
	
	private String icon;
	
	private int active;
	
	private int editable;
	
	@Column(name="short_letters")
	private String shortLetters;

	@Column(name="short_letter_colour")
	private String shortLetterColour;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public int getEditable() {
		return editable;
	}

	public void setEditable(int editable) {
		this.editable = editable;
	}
	
    public String getShortLetters() {
		return shortLetters;
	}

	public void setShortLetters(String shortLetters) {
		this.shortLetters = shortLetters;
	}

	public String getShortLetterColour() {
		return shortLetterColour;
	}

	public void setShortLetterColour(String shortLetterColour) {
		this.shortLetterColour = shortLetterColour;
	}

	@PostPersist
    @PostUpdate
    public void on_jpa_update() {
    	AppointmentStatusMgrImpl.setCacheIsDirty(true);
    }
}
