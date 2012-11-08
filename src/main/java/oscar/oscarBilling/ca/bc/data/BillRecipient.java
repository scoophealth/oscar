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

package oscar.oscarBilling.ca.bc.data;

import java.util.Date;

import org.oscarehr.billing.CA.BC.model.BillRecipients;
import org.oscarehr.util.MiscUtils;

import oscar.util.ConversionUtils;

public class BillRecipient {
	
	private Integer id;
	private String name;
	private String address;
	private String city;
	private String province;
	private String postal;
	private Date creationTime;
	private Date updateTime;
	private String billingNo;

	public BillRecipient() {
	}

	public BillRecipient(BillRecipients b) {
		id = b.getId();
		name = b.getName();
		address = b.getAddress();
		city = b.getCity();
		province = b.getProvince();
		postal = b.getPostal();
		creationTime = b.getCreationTime();
		updateTime = b.getUpdateTime();
		billingNo = ConversionUtils.toIntString(b.getBillingNo());
	}

	public void setId(int id) {
		MiscUtils.getLogger().debug("int id");
		this.id = new Integer(id);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public void setPostal(String postal) {
		this.postal = postal;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void setBillingNo(Integer billingNo) {
		this.billingNo = billingNo.toString();
	}

	public void setBillingNoString(String billingNo) {
		this.billingNo = billingNo;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getCity() {
		return city;
	}

	public String getProvince() {
		return province;
	}

	public String getPostal() {
		return postal;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public String getBillingNo() {
		return billingNo;
	}
}
