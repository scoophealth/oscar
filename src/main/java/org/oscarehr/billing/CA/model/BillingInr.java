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


package org.oscarehr.billing.CA.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.oscarehr.common.model.AbstractModel;

@Entity
@Table(name="billinginr")
public class BillingInr extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="billinginr_no")
	private Integer id;

	@Column(name="demographic_no")
	private int demographicNo;

	@Column(name="demographic_name")
	private String demographicName;

	private String hin;

	private String dob;

	@Column(name="provider_no")
	private String providerNo;

	@Column(name="provider_ohip_no")
	private String providerOhipNo;

	@Column(name="provider_rma_no")
	private String providerRmaNo;

	private String creator;

	@Column(name="diagnostic_code")
	private String diagnosticCode;

	@Column(name="service_code")
	private String serviceCode;

	@Column(name="service_desc")
	private String serviceDesc;

	@Column(name="billing_amount")
	private String billingAmount;

	@Column(name="billing_unit")
	private String billingUnit;

	@Column(name="createdatetime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDateTime;

	private String status;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public int getDemographicNo() {
    	return demographicNo;
    }

	public void setDemographicNo(int demographicNo) {
    	this.demographicNo = demographicNo;
    }

	public String getDemographicName() {
    	return demographicName;
    }

	public void setDemographicName(String demographicName) {
    	this.demographicName = demographicName;
    }

	public String getHin() {
    	return hin;
    }

	public void setHin(String hin) {
    	this.hin = hin;
    }

	public String getDob() {
    	return dob;
    }

	public void setDob(String dob) {
    	this.dob = dob;
    }

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	public String getProviderOhipNo() {
    	return providerOhipNo;
    }

	public void setProviderOhipNo(String providerOhipNo) {
    	this.providerOhipNo = providerOhipNo;
    }

	public String getProviderRmaNo() {
    	return providerRmaNo;
    }

	public void setProviderRmaNo(String providerRmaNo) {
    	this.providerRmaNo = providerRmaNo;
    }

	public String getCreator() {
    	return creator;
    }

	public void setCreator(String creator) {
    	this.creator = creator;
    }

	public String getDiagnosticCode() {
    	return diagnosticCode;
    }

	public void setDiagnosticCode(String diagnosticCode) {
    	this.diagnosticCode = diagnosticCode;
    }

	public String getServiceCode() {
    	return serviceCode;
    }

	public void setServiceCode(String serviceCode) {
    	this.serviceCode = serviceCode;
    }

	public String getServiceDesc() {
    	return serviceDesc;
    }

	public void setServiceDesc(String serviceDesc) {
    	this.serviceDesc = serviceDesc;
    }

	public String getBillingAmount() {
    	return billingAmount;
    }

	public void setBillingAmount(String billingAmount) {
    	this.billingAmount = billingAmount;
    }

	public String getBillingUnit() {
    	return billingUnit;
    }

	public void setBillingUnit(String billingUnit) {
    	this.billingUnit = billingUnit;
    }

	public Date getCreateDateTime() {
    	return createDateTime;
    }

	public void setCreateDateTime(Date createDateTime) {
    	this.createDateTime = createDateTime;
    }

	public String getStatus() {
    	return status;
    }

	public void setStatus(String status) {
    	this.status = status;
    }


}
