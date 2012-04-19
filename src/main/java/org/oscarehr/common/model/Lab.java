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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Lab {
	private Long id;
	private String labName;
	private String labNotes;
	private String accession;
	private Date labReqDate;
	
	private String lastName;
	private String firstName;
	private String hin;
	private String sex;
	private Date dob;
	private String phone;
	
	private String billingNo;
	private String providerLastName;
	private String providerFirstName;
	private String cc;
	private String providerNotes;
	
	private List<LabTest> tests = new ArrayList<LabTest>();

	public Long getId() {
    	return id;
    }

	public void setId(Long id) {
    	this.id = id;
    }

	public String getLabName() {
    	return labName;
    }

	public void setLabName(String labName) {
    	this.labName = labName;
    }

	public String getLabNotes() {
    	return labNotes;
    }

	public void setLabNotes(String labNotes) {
    	this.labNotes = labNotes;
    }

	public String getAccession() {
    	return accession;
    }

	public void setAccession(String accession) {
    	this.accession = accession;
    }

	public Date getLabReqDate() {
    	return labReqDate;
    }

	public void setLabReqDate(Date labReqDate) {
    	this.labReqDate = labReqDate;
    }

	public String getLastName() {
    	return lastName;
    }

	public void setLastName(String lastName) {
    	this.lastName = lastName;
    }

	public String getFirstName() {
    	return firstName;
    }

	public void setFirstName(String firstName) {
    	this.firstName = firstName;
    }

	public String getHin() {
    	return hin;
    }

	public void setHin(String hin) {
    	this.hin = hin;
    }

	public String getSex() {
    	return sex;
    }

	public void setSex(String sex) {
    	this.sex = sex;
    }

	public Date getDob() {
    	return dob;
    }

	public void setDob(Date dob) {
    	this.dob = dob;
    }

	public String getPhone() {
    	return phone;
    }

	public void setPhone(String phone) {
    	this.phone = phone;
    }

	public String getBillingNo() {
    	return billingNo;
    }

	public void setBillingNo(String billingNo) {
    	this.billingNo = billingNo;
    }

	public String getProviderLastName() {
    	return providerLastName;
    }

	public void setProviderLastName(String providerLastName) {
    	this.providerLastName = providerLastName;
    }

	public String getProviderFirstName() {
    	return providerFirstName;
    }

	public void setProviderFirstName(String providerFirstName) {
    	this.providerFirstName = providerFirstName;
    }

	public String getCc() {
    	return cc;
    }

	public void setCc(String cc) {
    	this.cc = cc;
    }

	public String getProviderNotes() {
    	return providerNotes;
    }

	public void setProviderNotes(String providerNotes) {
    	this.providerNotes = providerNotes;
    }

	public List<LabTest> getTests() {
    	return tests;
    }

	public void setTests(List<LabTest> tests) {
    	this.tests = tests;
    }
	
	
}
