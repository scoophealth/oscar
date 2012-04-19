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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name= "HL7HandlerMSHMapping")
public class HL7HandlerMSHMapping  extends AbstractModel<Integer> implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "hospital_site")
	private String hospitalSite;
	
	
	private String facility;
	
	@Column(name = "facility_name")
	private String facilityName;
	

	private String notes;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getHospitalSite() {
    	return hospitalSite;
    }

	public void setHospitalSite(String hospitalSite) {
    	this.hospitalSite = hospitalSite;
    }

	public String getFacility() {
    	return facility;
    }

	public void setFacility(String facility) {
    	this.facility = facility;
    }

	public String getFacilityName() {
    	return facilityName;
    }

	public void setFacilityName(String facilityName) {
    	this.facilityName = facilityName;
    }

	public String getNotes() {
    	return notes;
    }

	public void setNotes(String notes) {
    	this.notes = notes;
    }
	
}
