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
@Table(name="billingdetail")
public class BillingDetail extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="billing_dt_no")
	private Integer id;

	@Column(name="billing_no")
	private int billingNo;

	@Column(name="service_code")
	private String serviceCode;

	@Column(name="service_desc")
	private String serviceDesc;

	@Column(name="billing_amount")
	private String billingAmount;

	@Column(name="diagnostic_code")
	private String diagnosticCode;

	@Column(name="appointment_date")
	@Temporal(TemporalType.DATE)
	private Date appointmentDate;

	private String status;

	@Column(name="billingunit")
	private String billingUnit;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
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

	public String getDiagnosticCode() {
    	return diagnosticCode;
    }

	public void setDiagnosticCode(String diagnosticCode) {
    	this.diagnosticCode = diagnosticCode;
    }

	public Date getAppointmentDate() {
    	return appointmentDate;
    }

	public void setAppointmentDate(Date appointmentDate) {
    	this.appointmentDate = appointmentDate;
    }

	public String getStatus() {
    	return status;
    }

	public void setStatus(String status) {
    	this.status = status;
    }

	public String getBillingUnit() {
    	return billingUnit;
    }

	public void setBillingUnit(String billingUnit) {
    	this.billingUnit = billingUnit;
    }


}
