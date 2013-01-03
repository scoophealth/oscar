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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="oncall_questionnaire")
public class OnCallQuestionnaire extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id = null;

	private String providerNo;
	
	private String type;
	
	@Column(name="health_type")
	private String healthType;
	
	@Column(name="nurse_involved")
	private String nurseInvolved;
	
	@Column(name="course_of_action")
	private String courseOfAction;
	
	@Column(name="physician_consultation_reqd")
	private String physicialConsultationRequired;
	
	@Column(name="call_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date callTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public String getHealthType() {
		return healthType;
	}

	public void setHealthType(String healthType) {
		this.healthType = healthType;
	}

	public String getNurseInvolved() {
		return nurseInvolved;
	}

	public void setNurseInvolved(String nurseInvolved) {
		this.nurseInvolved = nurseInvolved;
	}

	public String getCourseOfAction() {
		return courseOfAction;
	}

	public void setCourseOfAction(String courseOfAction) {
		this.courseOfAction = courseOfAction;
	}

	public String getPhysicialConsultationRequired() {
		return physicialConsultationRequired;
	}

	public void setPhysicialConsultationRequired(String physicialConsultationRequired) {
		this.physicialConsultationRequired = physicialConsultationRequired;
	}

	public Date getCallTime() {
		return callTime;
	}

	public void setCallTime(Date callTime) {
		this.callTime = callTime;
	}
	
	
	
}
