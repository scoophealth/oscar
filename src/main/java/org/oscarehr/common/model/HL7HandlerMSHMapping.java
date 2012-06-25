/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
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
