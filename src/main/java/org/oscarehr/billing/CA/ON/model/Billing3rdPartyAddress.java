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


package org.oscarehr.billing.CA.ON.model;

import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.oscarehr.common.model.AbstractModel;

@Entity
@Table(name="billing_on_3rdPartyAddress")
public class Billing3rdPartyAddress extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String attention;

	@Column(name="company_name")
	private String companyName;

	private String address;

	private String city;

	private String province;

	@Column(name="postcode")
	private String postalCode;

	private String telephone;

	private String fax;

	@Override
    public Integer getId() {
		return id;
	}

	public String getAttention() {
    	return attention;
    }

	public void setAttention(String attention) {
    	this.attention = attention;
    }

	public String getCompanyName() {
    	return companyName;
    }

	public void setCompanyName(String companyName) {
    	this.companyName = companyName;
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

	public String getTelephone() {
    	return telephone;
    }

	public void setTelephone(String telephone) {
    	this.telephone = telephone;
    }

	public String getFax() {
    	return fax;
    }

	public void setFax(String fax) {
    	this.fax = fax;
    }


	public static final Comparator<Billing3rdPartyAddress> COMPANY_NAME_COMPARATOR = new Comparator<Billing3rdPartyAddress>() {
		public int compare(Billing3rdPartyAddress p1, Billing3rdPartyAddress p2) {
			return (p1.getCompanyName().compareTo(p2.getCompanyName()));
		}
	};   
	
}
