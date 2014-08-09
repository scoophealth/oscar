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
@Table(name="demographiccustArchive")
public class DemographicCustArchive extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="demographic_no")
	private Integer demographicNo;

	@Column(name="cust1")
	private String nurse;

	@Column(name="cust2")
	private String resident;

	@Column(name="cust3")
	private String alert;

	@Column(name="cust4")
	private String midwife;

	@Column(name="content")
	private String notes;

	
	@Override
	public Integer getId() {
		return id;
	}
	
	public Integer getDemographicNo() {
		return demographicNo;
	}
	
	public void setDemographicNo(Integer demographicNo) {
		this.demographicNo = demographicNo;
	}

	public String getNurse() {
    	return nurse;
    }

	public void setNurse(String nurse) {
    	this.nurse = nurse;
    }

	public String getResident() {
    	return resident;
    }

	public void setResident(String resident) {
    	this.resident = resident;
    }

	public String getAlert() {
    	return alert;
    }

	public void setAlert(String alert) {
    	this.alert = alert;
    }

	public String getMidwife() {
    	return midwife;
    }

	public void setMidwife(String midwife) {
    	this.midwife = midwife;
    }

	public String getNotes() {
    	return notes;
    }

	public void setNotes(String notes) {
    	this.notes = notes;
    }
	
	//constructors
	public DemographicCustArchive() {}
	
	public DemographicCustArchive(DemographicCust dc) {
		this.demographicNo = dc.getId();
		this.alert = dc.getAlert();
		this.midwife = dc.getMidwife();
		this.nurse = dc.getNurse();
		this.resident = dc.getResident();
		this.notes = dc.getNotes();
	}
}
