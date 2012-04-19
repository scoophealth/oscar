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
@Table(name="mdsPV1")
public class MdsPV1 extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="segmentID")
	private Integer id;

	private String patientClass;

	private String patientLocation;

	private String refDoctor;

	private String conDoctor;

	private String admDoctor;

	private String vNumber;

	private String accStatus;

	private String admDateTime;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getPatientClass() {
    	return patientClass;
    }

	public void setPatientClass(String patientClass) {
    	this.patientClass = patientClass;
    }

	public String getPatientLocation() {
    	return patientLocation;
    }

	public void setPatientLocation(String patientLocation) {
    	this.patientLocation = patientLocation;
    }

	public String getRefDoctor() {
    	return refDoctor;
    }

	public void setRefDoctor(String refDoctor) {
    	this.refDoctor = refDoctor;
    }

	public String getConDoctor() {
    	return conDoctor;
    }

	public void setConDoctor(String conDoctor) {
    	this.conDoctor = conDoctor;
    }

	public String getAdmDoctor() {
    	return admDoctor;
    }

	public void setAdmDoctor(String admDoctor) {
    	this.admDoctor = admDoctor;
    }

	public String getvNumber() {
    	return vNumber;
    }

	public void setvNumber(String vNumber) {
    	this.vNumber = vNumber;
    }

	public String getAccStatus() {
    	return accStatus;
    }

	public void setAccStatus(String accStatus) {
    	this.accStatus = accStatus;
    }

	public String getAdmDateTime() {
    	return admDateTime;
    }

	public void setAdmDateTime(String admDateTime) {
    	this.admDateTime = admDateTime;
    }


}
