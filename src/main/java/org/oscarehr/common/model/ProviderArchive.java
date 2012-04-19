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
@Table(name="providerArchive")
public class ProviderArchive extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name="provider_no")
	private String providerNo;
	@Column(name="last_name")
	private String lastName;
	@Column(name="first_name")
	private String firstName;
	@Column(name="provider_type")
	private String providerType;
	private String specialty;
	private String team;
	private String sex;
	@Temporal(TemporalType.DATE)
	private Date dob;
	private String address;
	private String phone;
	@Column(name="work_phone")
	private String workPhone;
	@Column(name="ohip_no")
	private String ohipNo;
	@Column(name="rma_no")
	private String rmaNo;
	@Column(name="billing_no")
	private String billingNo;
	@Column(name="hso_no")
	private String hsoNo;
	private String status;
	private String comments;
	@Column(name="provider_activity")
	private String providerActivity;
	private String practitionerNo;
	private String init;
	@Column(name="job_title")
	private String jobTitle;
	private String email;
	private String title;
	private String lastUpdateUser;
	private Date lastUpdateDate;
	@Column(name="signed_confidentiality")
    private Date SignedConfidentiality;
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
	public String getProviderType() {
    	return providerType;
    }
	public void setProviderType(String providerType) {
    	this.providerType = providerType;
    }
	public String getSpecialty() {
    	return specialty;
    }
	public void setSpecialty(String specialty) {
    	this.specialty = specialty;
    }
	public String getTeam() {
    	return team;
    }
	public void setTeam(String team) {
    	this.team = team;
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
	public String getAddress() {
    	return address;
    }
	public void setAddress(String address) {
    	this.address = address;
    }
	public String getPhone() {
    	return phone;
    }
	public void setPhone(String phone) {
    	this.phone = phone;
    }
	public String getWorkPhone() {
    	return workPhone;
    }
	public void setWorkPhone(String workPhone) {
    	this.workPhone = workPhone;
    }
	public String getOhipNo() {
    	return ohipNo;
    }
	public void setOhipNo(String ohipNo) {
    	this.ohipNo = ohipNo;
    }
	public String getRmaNo() {
    	return rmaNo;
    }
	public void setRmaNo(String rmaNo) {
    	this.rmaNo = rmaNo;
    }
	public String getBillingNo() {
    	return billingNo;
    }
	public void setBillingNo(String billingNo) {
    	this.billingNo = billingNo;
    }
	public String getHsoNo() {
    	return hsoNo;
    }
	public void setHsoNo(String hsoNo) {
    	this.hsoNo = hsoNo;
    }
	public String getStatus() {
    	return status;
    }
	public void setStatus(String status) {
    	this.status = status;
    }
	public String getComments() {
    	return comments;
    }
	public void setComments(String comments) {
    	this.comments = comments;
    }
	public String getProviderActivity() {
    	return providerActivity;
    }
	public void setProviderActivity(String providerActivity) {
    	this.providerActivity = providerActivity;
    }
	public String getPractitionerNo() {
    	return practitionerNo;
    }
	public void setPractitionerNo(String practitionerNo) {
    	this.practitionerNo = practitionerNo;
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
	public String getEmail() {
    	return email;
    }
	public void setEmail(String email) {
    	this.email = email;
    }
	public String getTitle() {
    	return title;
    }
	public void setTitle(String title) {
    	this.title = title;
    }
	public String getLastUpdateUser() {
    	return lastUpdateUser;
    }
	public void setLastUpdateUser(String lastUpdateUser) {
    	this.lastUpdateUser = lastUpdateUser;
    }
	public Date getLastUpdateDate() {
    	return lastUpdateDate;
    }
	public void setLastUpdateDate(Date lastUpdateDate) {
    	this.lastUpdateDate = lastUpdateDate;
    }
	public Date getSignedConfidentiality() {
    	return SignedConfidentiality;
    }
	public void setSignedConfidentiality(Date signedConfidentiality) {
    	SignedConfidentiality = signedConfidentiality;
    }




}
