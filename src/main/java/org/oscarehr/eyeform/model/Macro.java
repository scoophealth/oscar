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


package org.oscarehr.eyeform.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.oscarehr.common.model.AbstractModel;

@Entity
@Table(name="EyeformMacro")
public class Macro extends AbstractModel<Integer>{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String label;
	private int displayOrder;
	private String impression;
	private short followupNo;
	private String followupUnit;

	@Column(name="followupDoctor")
	private String followupDoctorId;
	private String followupReason;
	private String billingVisitType;
	private String billingVisitLocation;
	private String billingCodes;
	private String billingDxcode;
	private String billingTotal;
	private String billingComment;
	private String billingBilltype;
	private String billingPayMethod;
	private String billingBillto;
	private String billingRemitto;
	private String billingGstBilledTotal;
	private String billingPayment;
	private String billingRefund;
	private String billingGst;
	private String testRecords;

	private String statFlag;
	private String optFlag;
	private String dischargeFlag;
	@Column(name="ticklerStaff")
	private String ticklerRecipient;

	private String sliCode;

	public String getTicklerRecipient() {
		return ticklerRecipient;
	}
	public void setTicklerRecipient(String ticklerRecipient) {
		this.ticklerRecipient = ticklerRecipient;
	}
	public String getStatFlag() {
		return statFlag;
	}
	public void setStatFlag(String statFlag) {
		this.statFlag = statFlag;
	}
	public String getOptFlag() {
		return optFlag;
	}
	public void setOptFlag(String optFlag) {
		this.optFlag = optFlag;
	}
	public String getDischargeFlag() {
		return dischargeFlag;
	}
	public void setDischargeFlag(String dischargeFlag) {
		this.dischargeFlag = dischargeFlag;
	}
	public String getFollowupDoctorId() {
		return followupDoctorId;
	}
	public void setFollowupDoctorId(String followupDoctorId) {
		this.followupDoctorId = followupDoctorId;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public int getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}
	public String getImpression() {
		return impression;
	}
	public void setImpression(String impression) {
		this.impression = impression;
	}
	public short getFollowupNo() {
		return followupNo;
	}
	public void setFollowupNo(short followupNo) {
		this.followupNo = followupNo;
	}
	public String getFollowupUnit() {
		return followupUnit;
	}
	public void setFollowupUnit(String followupUnit) {
		this.followupUnit = followupUnit;
	}

	public String getFollowupReason() {
		return followupReason;
	}
	public void setFollowupReason(String followupReason) {
		this.followupReason = followupReason;
	}
	public String getBillingVisitType() {
		return billingVisitType;
	}
	public void setBillingVisitType(String billingVisitType) {
		this.billingVisitType = billingVisitType;
	}
	public String getBillingVisitLocation() {
		return billingVisitLocation;
	}
	public void setBillingVisitLocation(String billingVisitLocation) {
		this.billingVisitLocation = billingVisitLocation;
	}
	public String getBillingCodes() {
		return billingCodes;
	}
	public void setBillingCodes(String billingCodes) {
		this.billingCodes = billingCodes;
	}
	public String getBillingDxcode() {
		return billingDxcode;
	}
	public void setBillingDxcode(String billingDxcode) {
		this.billingDxcode = billingDxcode;
	}
	public String getBillingTotal() {
		return billingTotal;
	}
	public void setBillingTotal(String billingTotal) {
		this.billingTotal = billingTotal;
	}
	public String getBillingComment() {
		return billingComment;
	}
	public void setBillingComment(String billingComment) {
		this.billingComment = billingComment;
	}
	public String getBillingBilltype() {
		return billingBilltype;
	}
	public void setBillingBilltype(String billingBilltype) {
		this.billingBilltype = billingBilltype;
	}
	public String getBillingPayMethod() {
		return billingPayMethod;
	}
	public void setBillingPayMethod(String billingPayMethod) {
		this.billingPayMethod = billingPayMethod;
	}
	public String getBillingBillto() {
		return billingBillto;
	}
	public void setBillingBillto(String billingBillto) {
		this.billingBillto = billingBillto;
	}
	public String getBillingRemitto() {
		return billingRemitto;
	}
	public void setBillingRemitto(String billingRemitto) {
		this.billingRemitto = billingRemitto;
	}
	public String getBillingGstBilledTotal() {
		return billingGstBilledTotal;
	}
	public void setBillingGstBilledTotal(String billingGstBilledTotal) {
		this.billingGstBilledTotal = billingGstBilledTotal;
	}
	public String getBillingPayment() {
		return billingPayment;
	}
	public void setBillingPayment(String billingPayment) {
		this.billingPayment = billingPayment;
	}
	public String getBillingRefund() {
		return billingRefund;
	}
	public void setBillingRefund(String billingRefund) {
		this.billingRefund = billingRefund;
	}
	public String getBillingGst() {
		return billingGst;
	}
	public void setBillingGst(String billingGst) {
		this.billingGst = billingGst;
	}
	public String getTestRecords() {
		return testRecords;
	}
	public void setTestRecords(String testRecords) {
		this.testRecords = testRecords;
	}
	public String getSliCode() {
    	return sliCode;
    }
	public void setSliCode(String sliCode) {
    	this.sliCode = sliCode;
    }



}
