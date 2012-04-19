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
@Table(name="billing_on_eareport")
public class BillingONEaReport extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="providerohip_no")
	private String providerOhipNo;

	@Column(name="group_no")
	private String groupNo;

	private String specialty;

	@Column(name="process_date")
	private Date processDate;

	private String hin;

	private String ver;

	@Temporal(TemporalType.DATE)
	private Date dob;

	@Column(name="billing_no")
	private int billingNo;

	@Column(name="ref_no")
	private String refNo;

	private String facility;

	@Column(name="admitted_date")
	private String admittedDate;

	@Column(name="claim_error")
	private String claimError;

	private String code;

	private String fee;

	private String unit;

	@Column(name="code_date")
	@Temporal(TemporalType.DATE)
	private Date codeDate;

	private String dx;

	private String exp;

	@Column(name="code_error")
	private String codeError;

	@Column(name="report_name")
	private String reportName;

	private String status;

	private String comment;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getProviderOhipNo() {
    	return providerOhipNo;
    }

	public void setProviderOhipNo(String providerOhipNo) {
    	this.providerOhipNo = providerOhipNo;
    }

	public String getGroupNo() {
    	return groupNo;
    }

	public void setGroupNo(String groupNo) {
    	this.groupNo = groupNo;
    }

	public String getSpecialty() {
    	return specialty;
    }

	public void setSpecialty(String specialty) {
    	this.specialty = specialty;
    }

	public Date getProcessDate() {
    	return processDate;
    }

	public void setProcessDate(Date processDate) {
    	this.processDate = processDate;
    }

	public String getHin() {
    	return hin;
    }

	public void setHin(String hin) {
    	this.hin = hin;
    }

	public String getVer() {
    	return ver;
    }

	public void setVer(String ver) {
    	this.ver = ver;
    }

	public Date getDob() {
    	return dob;
    }

	public void setDob(Date dob) {
    	this.dob = dob;
    }

	public int getBillingNo() {
    	return billingNo;
    }

	public void setBillingNo(int billingNo) {
    	this.billingNo = billingNo;
    }

	public String getRefNo() {
    	return refNo;
    }

	public void setRefNo(String refNo) {
    	this.refNo = refNo;
    }

	public String getFacility() {
    	return facility;
    }

	public void setFacility(String facility) {
    	this.facility = facility;
    }

	public String getAdmittedDate() {
    	return admittedDate;
    }

	public void setAdmittedDate(String admittedDate) {
    	this.admittedDate = admittedDate;
    }

	public String getClaimError() {
    	return claimError;
    }

	public void setClaimError(String claimError) {
    	this.claimError = claimError;
    }

	public String getCode() {
    	return code;
    }

	public void setCode(String code) {
    	this.code = code;
    }

	public String getFee() {
    	return fee;
    }

	public void setFee(String fee) {
    	this.fee = fee;
    }

	public String getUnit() {
    	return unit;
    }

	public void setUnit(String unit) {
    	this.unit = unit;
    }

	public Date getCodeDate() {
    	return codeDate;
    }

	public void setCodeDate(Date codeDate) {
    	this.codeDate = codeDate;
    }

	public String getDx() {
    	return dx;
    }

	public void setDx(String dx) {
    	this.dx = dx;
    }

	public String getExp() {
    	return exp;
    }

	public void setExp(String exp) {
    	this.exp = exp;
    }

	public String getCodeError() {
    	return codeError;
    }

	public void setCodeError(String codeError) {
    	this.codeError = codeError;
    }

	public String getReportName() {
    	return reportName;
    }

	public void setReportName(String reportName) {
    	this.reportName = reportName;
    }

	public String getStatus() {
    	return status;
    }

	public void setStatus(String status) {
    	this.status = status;
    }

	public String getComment() {
    	return comment;
    }

	public void setComment(String comment) {
    	this.comment = comment;
    }




}
