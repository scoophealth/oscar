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


package org.oscarehr.ws.transfer_objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.oscarehr.common.model.Provider;
import org.springframework.beans.BeanUtils;

public final class ProviderTransfer {
	private String providerNo;
	private String comments;
	private String phone;
	private String billingNo;
	private String workPhone;
	private String address;
	private String team;
	private String status;
	private String lastName;
	private String providerType;
	private String sex;
	private String ohipNo;
	private String specialty;
	private Date dob;
	private String hsoNo;
	private String providerActivity;
	private String firstName;
	private String rmaNo;
	private Date SignedConfidentiality;
	private String practitionerNo;
	private String email;
	private String title;

	public String getProviderNo() {
		return (providerNo);
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public String getComments() {
		return (comments);
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getPhone() {
		return (phone);
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBillingNo() {
		return (billingNo);
	}

	public void setBillingNo(String billingNo) {
		this.billingNo = billingNo;
	}

	public String getWorkPhone() {
		return (workPhone);
	}

	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}

	public String getAddress() {
		return (address);
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTeam() {
		return (team);
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public String getStatus() {
		return (status);
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLastName() {
		return (lastName);
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getProviderType() {
		return (providerType);
	}

	public void setProviderType(String providerType) {
		this.providerType = providerType;
	}

	public String getSex() {
		return (sex);
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getOhipNo() {
		return (ohipNo);
	}

	public void setOhipNo(String ohipNo) {
		this.ohipNo = ohipNo;
	}

	public String getSpecialty() {
		return (specialty);
	}

	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}

	public Date getDob() {
		return (dob);
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getHsoNo() {
		return (hsoNo);
	}

	public void setHsoNo(String hsoNo) {
		this.hsoNo = hsoNo;
	}

	public String getProviderActivity() {
		return (providerActivity);
	}

	public void setProviderActivity(String providerActivity) {
		this.providerActivity = providerActivity;
	}

	public String getFirstName() {
		return (firstName);
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getRmaNo() {
		return (rmaNo);
	}

	public void setRmaNo(String rmaNo) {
		this.rmaNo = rmaNo;
	}

	public Date getSignedConfidentiality() {
		return (SignedConfidentiality);
	}

	public void setSignedConfidentiality(Date signedConfidentiality) {
		SignedConfidentiality = signedConfidentiality;
	}

	public String getPractitionerNo() {
		return (practitionerNo);
	}

	public void setPractitionerNo(String practitionerNo) {
		this.practitionerNo = practitionerNo;
	}

	public String getEmail() {
		return (email);
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTitle() {
		return (title);
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public static ProviderTransfer toTransfer(Provider provider) {
		if (provider==null) return(null);
		
		ProviderTransfer providerTransfer = new ProviderTransfer();

		BeanUtils.copyProperties(provider, providerTransfer);

		return (providerTransfer);
	}

	public static ProviderTransfer[] toTransfers(List<Provider> providers) {
		ArrayList<ProviderTransfer> results = new ArrayList<ProviderTransfer>();

		for (Provider provider : providers) {
			results.add(toTransfer(provider));
		}

		return (results.toArray(new ProviderTransfer[0]));
	}

	@Override
	public String toString() {
		return (ReflectionToStringBuilder.toString(this));
	}
}
