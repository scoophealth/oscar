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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="radetail")
public class RaDetail extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="radetail_no")
	private Integer id;

	@Column(name="raheader_no")
	private int raHeaderNo;

	@Column(name="providerohip_no")
	private String providerOhipNo;

	@Column(name="billing_no")
	private int billingNo;

	@Column(name="service_code")
	private String serviceCode;

	@Column(name="service_count")
	private String serviceCount;

	private String hin;

	@Column(name="amountclaim")
	private String amountClaim;

	@Column(name="amountpay")
	private String amountPay;

	@Column(name="service_date")
	private String serviceDate;

	@Column(name="error_code")
	private String errorCode;

	@Column(name="billtype")
	private String billType;

	@Column(name="claim_no")
	private String claimNo;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public int getRaHeaderNo() {
    	return raHeaderNo;
    }

	public void setRaHeaderNo(int raHeaderNo) {
    	this.raHeaderNo = raHeaderNo;
    }

	public String getProviderOhipNo() {
    	return providerOhipNo;
    }

	public void setProviderOhipNo(String providerOhipNo) {
    	this.providerOhipNo = providerOhipNo;
    }

	public int getBillingNo() {
    	return billingNo;
    }

	public void setBillingNo(int billingNo) {
    	this.billingNo = billingNo;
    }

	public String getServiceCode() {
    	return serviceCode;
    }

	public void setServiceCode(String serviceCode) {
    	this.serviceCode = serviceCode;
    }

	public String getServiceCount() {
    	return serviceCount;
    }

	public void setServiceCount(String serviceCount) {
    	this.serviceCount = serviceCount;
    }

	public String getHin() {
    	return hin;
    }

	public void setHin(String hin) {
    	this.hin = hin;
    }

	public String getAmountClaim() {
    	return amountClaim;
    }

	public void setAmountClaim(String amountClaim) {
    	this.amountClaim = amountClaim;
    }

	public String getAmountPay() {
    	return amountPay;
    }

	public void setAmountPay(String amountPay) {
    	this.amountPay = amountPay;
    }

	public String getServiceDate() {
    	return serviceDate;
    }

	public void setServiceDate(String serviceDate) {
    	this.serviceDate = serviceDate;
    }

	public String getErrorCode() {
    	return errorCode;
    }

	public void setErrorCode(String errorCode) {
    	this.errorCode = errorCode;
    }

	public String getBillType() {
    	return billType;
    }

	public void setBillType(String billType) {
    	this.billType = billType;
    }

	public String getClaimNo() {
    	return claimNo;
    }

	public void setClaimNo(String claimNo) {
    	this.claimNo = claimNo;
    }



}
