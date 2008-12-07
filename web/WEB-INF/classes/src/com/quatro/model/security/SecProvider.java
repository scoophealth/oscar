package com.quatro.model.security;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Provider entity.
 * 
 * @author JZhang
 */


public class SecProvider implements java.io.Serializable {

	// Fields

	private String providerNo;
	private String lastName;
	private String firstName;
	private String providerType;
	private String specialty;
	private String team;
	private String sex;
	private Date dob;
	private String address;
	private String phone;
	private String workPhone;
	private String ohipNo;
	private String rmaNo;
	private String billingNo;
	private String hsoNo;
	private String status;
	private String comments;
	private String providerActivity;
	
	private String init;
	private String title;
	private String jobTitle;
	private String email;

	private String formattedName;
	private String fullName;
	
	// Constructors

	/** default constructor */
	public SecProvider() {
	}

	/** full constructor */
	public SecProvider(String lastName, String firstName, String providerType,
			String specialty, String team, String sex, Date dob,
			String address, String phone, String workPhone, String ohipNo,
			String rmaNo, String billingNo, String hsoNo, String status,
			String comments, String providerActivity) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.providerType = providerType;
		this.specialty = specialty;
		this.team = team;
		this.sex = sex;
		this.dob = dob;
		this.address = address;
		this.phone = phone;
		this.workPhone = workPhone;
		this.ohipNo = ohipNo;
		this.rmaNo = rmaNo;
		this.billingNo = billingNo;
		this.hsoNo = hsoNo;
		this.status = status;
		this.comments = comments;
		this.providerActivity = providerActivity;
	}

	// Property accessors

	public String getProviderNo() {
		return this.providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getProviderType() {
		return this.providerType;
	}

	public void setProviderType(String providerType) {
		this.providerType = providerType;
	}

	public String getSpecialty() {
		return this.specialty;
	}

	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}

	public String getTeam() {
		return this.team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Date getDob() {
		return this.dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWorkPhone() {
		return this.workPhone;
	}

	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}

	public String getOhipNo() {
		return this.ohipNo;
	}

	public void setOhipNo(String ohipNo) {
		this.ohipNo = ohipNo;
	}

	public String getRmaNo() {
		return this.rmaNo;
	}

	public void setRmaNo(String rmaNo) {
		this.rmaNo = rmaNo;
	}

	public String getBillingNo() {
		return this.billingNo;
	}

	public void setBillingNo(String billingNo) {
		this.billingNo = billingNo;
	}

	public String getHsoNo() {
		return this.hsoNo;
	}

	public void setHsoNo(String hsoNo) {
		this.hsoNo = hsoNo;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getProviderActivity() {
		return this.providerActivity;
	}

	public void setProviderActivity(String providerActivity) {
		this.providerActivity = providerActivity;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getInit() {
		return init;
	}

	public void setInit(String init) {
		this.init = init;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public String getFormattedName() {
		return getLastName() + ", " + getFirstName();
	}
	
	public String getFullName() {
		return getFirstName() + " " + getLastName();
	}

}