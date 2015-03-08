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
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;

/*
 * Author: Dennis Warren 
 * Company: Colcamex Resources
 * Date: November 2014
 * For: UBC Pharmacy Clinic and McMaster Department of Family Medicine
 */
@Entity
@Table(name="formBPMH")
public class FormBPMH extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@Column(name="demographic_no")
	private int demographicNo;
	@Column(name="provider_no")
	private int providerNo;
	@Temporal(TemporalType.DATE)
	@Column(name="formCreated")
	private Date formCreated;
	@Temporal(TemporalType.DATE)
	@Column(name="formEdited")
	private Date formEdited;
	@Column(name="note")
	private String note;
	@Column(name="allergies")
	private String allergies;
	@Column(name="drugs")
	private String drugs;
	@Column(name="familyDrName")
	private String familyDrName;
	@Column(name="familyDrPhone")
	private String familyDrPhone;
	@Column(name="familyDrFax")
	private String familyDrFax;
	
	@Transient
	private String what;
	@Transient
	private String how;
	@Transient
	private String why;
	@Transient
	private String instructions;

	@Override
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public int getDemographicNo() {
		return demographicNo;
	}
	public void setDemographicNo(int demographicNo) {
		this.demographicNo = demographicNo;
	}
	public int getProviderNo() {
		return providerNo;
	}
	public void setProviderNo(int providerNo) {
		this.providerNo = providerNo;
	}
	public Date getFormCreated() {
		return formCreated;
	}
	public void setFormCreated(Date formCreated) {
		this.formCreated = formCreated;
	}
	public Date getFormEdited() {
		return formEdited;
	}
	public void setFormEdited(Date formEdited) {
		this.formEdited = formEdited;
	}

	public String getAllergies() {
		return allergies;
	}
	public void setAllergies(String allergies) {
		this.allergies = allergies;
	}
	public String getWhat() {
		return what;
	}
	public void setWhat(String what) {
		this.what = what;
	}
	public String getHow() {
		return how;
	}
	public void setHow(String how) {
		this.how = how;
	}
	public String getWhy() {
		return why;
	}
	public void setWhy(String why) {
		this.why = why;
	}
	public String getInstructions() {
		return instructions;
	}
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}

	public String getDrugs() {
		return drugs;
	}
	public void setDrugs(String drugs) {
		this.drugs = drugs;
	}

	public String getFamilyDrName() {
		if(familyDrName == null) {
			return "";
		}
		return familyDrName;
	}
	public void setFamilyDrName(String familyDrName) {
		this.familyDrName = familyDrName;
	}
	public String getFamilyDrPhone() {
		if(familyDrPhone == null) {
			return "";
		}
		return familyDrPhone;
	}
	public void setFamilyDrPhone(String familyDrPhone) {
		this.familyDrPhone = familyDrPhone;
	}
	public String getFamilyDrFax() {
		if(familyDrFax == null) {
			return "";
		}
		return familyDrFax;
	}
	public void setFamilyDrFax(String familyDrFax) {
		this.familyDrFax = familyDrFax;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}	
}
