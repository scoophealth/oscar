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
@Table(name="pharmacyInfo")
public class PharmacyInfo extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="recordID")
	private Integer id;

	@Column(name="ID")
	private int id2;

	private String name;

	private String address;

	private String city;

	private String province;

	private String postalCode;

	private String phone1;

	private String phone2;

	public String fax;

	private String email;

	private String serviceLocationIdentifier;

	private String notes;

	@Temporal(TemporalType.TIMESTAMP)
	private Date addDate;

	private String status;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public int getId2() {
    	return id2;
    }

	public void setId2(int id2) {
    	this.id2 = id2;
    }

	public String getName() {
    	return name;
    }

	public void setName(String name) {
    	this.name = name;
    }

	public String getAddress() {
    	return address;
    }

	public void setAddress(String address) {
    	this.address = address;
    }

	public String getCity() {
    	return city;
    }

	public void setCity(String city) {
    	this.city = city;
    }

	public String getProvince() {
    	return province;
    }

	public void setProvince(String province) {
    	this.province = province;
    }

	public String getPostalCode() {
    	return postalCode;
    }

	public void setPostalCode(String postalCode) {
    	this.postalCode = postalCode;
    }

	public String getPhone1() {
    	return phone1;
    }

	public void setPhone1(String phone1) {
    	this.phone1 = phone1;
    }

	public String getPhone2() {
    	return phone2;
    }

	public void setPhone2(String phone2) {
    	this.phone2 = phone2;
    }

	public String getFax() {
    	return fax;
    }

	public void setFax(String fax) {
    	this.fax = fax;
    }

	public String getEmail() {
    	return email;
    }

	public void setEmail(String email) {
    	this.email = email;
    }

	public String getServiceLocationIdentifier() {
    	return serviceLocationIdentifier;
    }

	public void setServiceLocationIdentifier(String serviceLocationIdentifier) {
    	this.serviceLocationIdentifier = serviceLocationIdentifier;
    }

	public String getNotes() {
    	return notes;
    }

	public void setNotes(String notes) {
    	this.notes = notes;
    }

	public Date getAddDate() {
    	return addDate;
    }

	public void setAddDate(Date addDate) {
    	this.addDate = addDate;
    }

	public String getStatus() {
    	return status;
    }

	public void setStatus(String status) {
    	this.status = status;
    }


}
