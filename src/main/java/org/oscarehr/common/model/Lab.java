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
