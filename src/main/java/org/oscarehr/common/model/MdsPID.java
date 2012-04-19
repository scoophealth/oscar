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
import javax.persistence.Table;

@Entity
@Table(name="mdsPID")
public class MdsPID extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="segmentID")
	private Integer id;

	@Column(name="intPatientID")
	private String internalPatientId;

	@Column(name="altPatientID")
	private String alternatePatientId;

	private String patientName;

	@Column(name="dOB")
	private String dob;

	private String sex;

	private String homePhone;

	private String healthNumber;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getInternalPatientId() {
    	return internalPatientId;
    }

	public void setInternalPatientId(String internalPatientId) {
    	this.internalPatientId = internalPatientId;
    }

	public String getAlternatePatientId() {
    	return alternatePatientId;
    }

	public void setAlternatePatientId(String alternatePatientId) {
    	this.alternatePatientId = alternatePatientId;
    }

	public String getPatientName() {
    	return patientName;
    }

	public void setPatientName(String patientName) {
    	this.patientName = patientName;
    }

	public String getDob() {
    	return dob;
    }

	public void setDob(String dob) {
    	this.dob = dob;
    }

	public String getSex() {
    	return sex;
    }

	public void setSex(String sex) {
    	this.sex = sex;
    }

	public String getHomePhone() {
    	return homePhone;
    }

	public void setHomePhone(String homePhone) {
    	this.homePhone = homePhone;
    }

	public String getHealthNumber() {
    	return healthNumber;
    }

	public void setHealthNumber(String healthNumber) {
    	this.healthNumber = healthNumber;
    }


}
