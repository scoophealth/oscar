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

import oscar.util.ConversionUtils;

@Entity
@Table(name="billing")
public class Billing extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="billing_no")
	private Integer id;

	@Column(name="clinic_no")
	private int clinicNo;

	@Column(name="demographic_no")
	private int demographicNo;

	@Column(name="provider_no")
	private String providerNo;

	@Column(name="appointment_no")
	private int appointmentNo;

	@Column(name="organization_spec_code")
	private String organizationSpecCode;

	@Column(name="demographic_name")
	private String demographicName;

	@Column(name="hin")
	private String hin;

	@Column(name="update_date")
	@Temporal(TemporalType.DATE)
	private Date updateDate;

	@Column(name="update_time")
	@Temporal(TemporalType.TIME)
	private Date updateTime;

	@Column(name="billing_date")
	@Temporal(TemporalType.DATE)
	private Date billingDate;

	@Column(name="billing_time")
	@Temporal(TemporalType.TIME)
	private Date billingTime;

	@Column(name="clinic_ref_code")
	private String clinicRefCode;

	private String content;

	private String total;

	private String status;

	private String dob;

	@Column(name="visitdate")
	@Temporal(TemporalType.DATE)
	private Date visitDate;

	@Column(name="visittype")
	private String visitType;

	@Column(name="provider_ohip_no")
	private String providerOhipNo;

	@Column(name="provider_rma_no")
	private String providerRmaNo;

	@Column(name="apptProvider_no")
	private String apptProviderNo;

	@Column(name="asstProvider_no")
	private String asstProviderNo;

	private String creator;

	private String billingtype;


	public Integer getId() {
    	return id;
    }

	public int getClinicNo() {
    	return clinicNo;
    }

	public void setClinicNo(int clinicNo) {
    	this.clinicNo = clinicNo;
    }

	public int getDemographicNo() {
    	return demographicNo;
    }

	public void setDemographicNo(int demographicNo) {
    	this.demographicNo = demographicNo;
    }

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	public int getAppointmentNo() {
    	return appointmentNo;
    }

	public void setAppointmentNo(int appointmentNo) {
    	this.appointmentNo = appointmentNo;
    }

	public String getOrganizationSpecCode() {
    	return organizationSpecCode;
    }

	public void setOrganizationSpecCode(String organizationSpecCode) {
    	this.organizationSpecCode = organizationSpecCode;
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

	public Date getUpdateDate() {
    	return updateDate;
    }

	public void setUpdateDate(Date updateDate) {
    	this.updateDate = updateDate;
    }

	public Date getUpdateTime() {
    	return updateTime;
    }

	public void setUpdateTime(Date updateTime) {
    	this.updateTime = updateTime;
    }

	public Date getBillingDate() {
    	return billingDate;
    }

	public void setBillingDate(Date billingDate) {
    	this.billingDate = billingDate;
    }

	public Date getBillingTime() {
    	return billingTime;
    }

	public void setBillingTime(Date billingTime) {
    	this.billingTime = billingTime;
    }

	public String getClinicRefCode() {
    	return clinicRefCode;
    }

	public void setClinicRefCode(String clinicRefCode) {
    	this.clinicRefCode = clinicRefCode;
    }

	public String getContent() {
    	return content;
    }

	public void setContent(String content) {
    	this.content = content;
    }

	public String getTotal() {
    	return total;
    }

	public void setTotal(String total) {
    	this.total = total;
    }

	public String getStatus() {
    	return status;
    }
	
	public boolean getStatusAsBoolean() {
		return ConversionUtils.fromBoolString(getStatus());
	}

	public void setStatus(String status) {
    	this.status = status;
    }

	public String getDob() {
    	return dob;
    }

	public void setDob(String dob) {
    	this.dob = dob;
    }

	public Date getVisitDate() {
    	return visitDate;
    }

	public void setVisitDate(Date visitDate) {
    	this.visitDate = visitDate;
    }

	public String getVisitType() {
    	return visitType;
    }

	public void setVisitType(String visitType) {
    	this.visitType = visitType;
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

	public String getApptProviderNo() {
    	return apptProviderNo;
    }

	public void setApptProviderNo(String apptProviderNo) {
    	this.apptProviderNo = apptProviderNo;
    }

	public String getAsstProviderNo() {
    	return asstProviderNo;
    }

	public void setAsstProviderNo(String asstProviderNo) {
    	this.asstProviderNo = asstProviderNo;
    }

	public String getCreator() {
    	return creator;
    }

	public void setCreator(String creator) {
    	this.creator = creator;
    }

	public String getBillingtype() {
    	return billingtype;
    }

	public void setBillingtype(String billingtype) {
    	this.billingtype = billingtype;
    }


}
