/*
 * 
 * Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for 
 * Centre for Research on Inner City Health, St. Michael's Hospital, 
 * Toronto, Ontario, Canada 
 */
package org.oscarehr.common.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Set;
import java.util.Date;

/**
 * This is the object class that relates to the provider table. Any customizations belong here.
 */
public class Provider implements Serializable, Comparable<Provider>{

	public static final String SYSTEM_PROVIDER_NO = "-1";

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

	private Set<Site> sites;

	public Set<Site> getSites() {
		return sites;
	}

	public void setSites(Set<Site> sites) {
		this.sites = sites;
	}

	// constructors
	public Provider() {
	}

	/**
	 * Constructor for primary key
	 */
	public Provider(String providerNo) {
		this.setProviderNo(providerNo);
	}

	/**
	 * Constructor for required fields
	 */
	public Provider(String providerNo, String lastName, String providerType, String sex, String specialty, String firstName) {

		this.setProviderNo(providerNo);
		this.setLastName(lastName);
		this.setProviderType(providerType);
		this.setSex(sex);
		this.setSpecialty(specialty);
		this.setFirstName(firstName);
	}

	public String getFormattedName() {
		return getLastName() + ", " + getFirstName();
	}

	public String getFullName() {
		return getFirstName() + " " + getLastName();
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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

	public String getWorkPhone() {
		return workPhone;
	}

	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getProviderType() {
		return providerType;
	}

	public void setProviderType(String providerType) {
		this.providerType = providerType;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getOhipNo() {
		return ohipNo;
	}

	public void setOhipNo(String ohipNo) {
		this.ohipNo = ohipNo;
	}

	public String getSpecialty() {
		return specialty;
	}

	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}

	public java.util.Date getDob() {
		return dob;
	}

	public void setDob(java.util.Date dob) {
		this.dob = dob;
	}

	public String getHsoNo() {
		return hsoNo;
	}

	public void setHsoNo(String hsoNo) {
		this.hsoNo = hsoNo;
	}

	public String getProviderActivity() {
		return providerActivity;
	}

	public void setProviderActivity(String providerActivity) {
		this.providerActivity = providerActivity;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getRmaNo() {
		return rmaNo;
	}

	public void setRmaNo(String rmaNo) {
		this.rmaNo = rmaNo;
	}

    public Date getSignedConfidentiality() {
        return this.SignedConfidentiality;
    }

    public void setSignedConfidentiality( Date SignedConfidentiality ) {
        this.SignedConfidentiality = SignedConfidentiality;
    }

	public ComparatorName ComparatorName() {
		return new ComparatorName();
	}

	public boolean equals(Provider provider) {
		try {
			return (providerNo.equals(provider.providerNo));
		} catch (Exception e) {
			return (false);
		}
	}

	public int hashCode() {
		if (providerNo==null) return(super.hashCode());
		else return(providerNo.hashCode());
	}

	public class ComparatorName implements Comparator<Provider>, Serializable {

		public int compare(Provider o1, Provider o2) {
			Provider bp1 = o1;
			Provider bp2 = o2;
			String lhs = bp1.getLastName() + bp1.getFirstName();
			String rhs = bp2.getLastName() + bp2.getFirstName();

			return lhs.compareTo(rhs);
		}
	}

	public int compareTo(Provider o) {
		if (providerNo==null) return(0);
	    return(providerNo.compareTo(o.providerNo));
    }
}