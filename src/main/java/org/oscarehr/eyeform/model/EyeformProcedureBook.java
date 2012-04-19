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


package org.oscarehr.eyeform.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.oscarehr.common.model.AbstractModel;
import org.oscarehr.common.model.Demographic;

import oscar.util.StringUtils;

@Entity
public class EyeformProcedureBook extends AbstractModel<Integer> {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="eyeform_id")
	private long eyeformId;
	
	private String provider;
	
	@Column(name="appointment_no")
	private int appointmentNo;
	
	@Transient
	private Demographic demographic;
	@Column(name="procedure_name")
	private String procedureName;
	private String eye;
	private String location;
	private String comment;
	private String urgency;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date date = new Date();
	
	@Column(name="demographic_no")
	private int demographicNo;
	
	
	public int getDemographicNo() {
		return demographicNo;
	}
	public void setDemographicNo(int demographicNo) {
		this.demographicNo = demographicNo;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public long getEyeformId() {
		return eyeformId;
	}
	public void setEyeformId(long eyeformId) {
		this.eyeformId = eyeformId;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	
	public int getAppointmentNo() {
		return appointmentNo;
	}
	public void setAppointmentNo(int appointmentNo) {
		this.appointmentNo = appointmentNo;
	}
	public Demographic getDemographic() {
		return demographic;
	}
	public void setDemographic(Demographic demographic) {
		this.demographic = demographic;
	}
	public String getProcedureName() {
		return procedureName;
	}
	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}
	public String getEye() {
		return eye;
	}
	public void setEye(String eye) {
		this.eye = eye;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getUrgency() {
		return urgency;
	}
	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}
	
	public String getCommentStr() {
		return StringUtils.maxLenString(getComment(), 23, 20, "...");  
	}
	
}
