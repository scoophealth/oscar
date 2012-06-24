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

package org.oscarehr.casemgmt.model;

import java.util.Date;

import org.caisi.model.BaseObject;

public class OnCallQuestionnaire extends BaseObject {
	private Long id;
	private String providerNo;
	private String type_health;
	private String nurse;
	private String course_action;
	private String physician_consult;
	private Date datetime;
	private String type;
	
	public String getType_health() {
		return type_health;
	}
	public void setType_health(String type_health) {
		this.type_health = type_health;
	}
	public String getNurse() {
		return nurse;
	}
	public void setNurse(String nurse) {
		this.nurse = nurse;
	}
	public String getCourse_action() {
		return course_action;
	}
	public void setCourse_action(String course_action) {
		this.course_action = course_action;
	}
	public String getPhysician_consult() {
		return physician_consult;
	}
	public void setPhysician_consult(String physician_consult) {
		this.physician_consult = physician_consult;
	}
	public Date getDatetime() {
		return datetime;
	}
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProviderNo() {
		return providerNo;
	}
	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
