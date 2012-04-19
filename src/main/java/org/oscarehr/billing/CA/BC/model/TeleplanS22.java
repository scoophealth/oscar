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


package org.oscarehr.billing.CA.BC.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.oscarehr.common.model.AbstractModel;

@Entity
@Table(name="teleplanS22")
public class TeleplanS22 extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="s22_id")
	private Integer id;

	@Column(name="s21_id")
	private Integer s21Id;

	@Column(name="filename")
	private String fileName;

	@Column(name="t_s22type")
	private String s22Type;

	@Column(name="t_datacenter")
	private String dataCentre;

	@Column(name="t_dataseq")
	private String dataSeq;

	@Column(name="t_payment")
	private String payment;

	@Column(name="t_linecode")
	private Character lineCode;

	@Column(name="t_payeeno")
	private String payeeNo;

	@Column(name="t_mspctlno")
	private String mspCtlNo;

	@Column(name="t_practitionerno")
	private String practitionerNo;

	@Column(name="t_practitionername")
	private String practitionerName;

	@Column(name="t_amtbilled")
	private String amountBilled;

	@Column(name="t_amtpaid")
	private String amountPaid;

	@Column(name="t_filler")
	private String filler;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public Integer getS21Id() {
    	return s21Id;
    }

	public void setS21Id(Integer s21Id) {
    	this.s21Id = s21Id;
    }

	public String getFileName() {
    	return fileName;
    }

	public void setFileName(String fileName) {
    	this.fileName = fileName;
    }

	public String getS22Type() {
    	return s22Type;
    }

	public void setS22Type(String s22Type) {
    	this.s22Type = s22Type;
    }

	public String getDataCentre() {
    	return dataCentre;
    }

	public void setDataCentre(String dataCentre) {
    	this.dataCentre = dataCentre;
    }

	public String getDataSeq() {
    	return dataSeq;
    }

	public void setDataSeq(String dataSeq) {
    	this.dataSeq = dataSeq;
    }

	public String getPayment() {
    	return payment;
    }

	public void setPayment(String payment) {
    	this.payment = payment;
    }

	public Character getLineCode() {
    	return lineCode;
    }

	public void setLineCode(Character lineCode) {
    	this.lineCode = lineCode;
    }

	public String getPayeeNo() {
    	return payeeNo;
    }

	public void setPayeeNo(String payeeNo) {
    	this.payeeNo = payeeNo;
    }

	public String getMspCtlNo() {
    	return mspCtlNo;
    }

	public void setMspCtlNo(String mspCtlNo) {
    	this.mspCtlNo = mspCtlNo;
    }

	public String getPractitionerNo() {
    	return practitionerNo;
    }

	public void setPractitionerNo(String practitionerNo) {
    	this.practitionerNo = practitionerNo;
    }

	public String getPractitionerName() {
    	return practitionerName;
    }

	public void setPractitionerName(String practitionerName) {
    	this.practitionerName = practitionerName;
    }

	public String getAmountBilled() {
    	return amountBilled;
    }

	public void setAmountBilled(String amountBilled) {
    	this.amountBilled = amountBilled;
    }

	public String getAmountPaid() {
    	return amountPaid;
    }

	public void setAmountPaid(String amountPaid) {
    	this.amountPaid = amountPaid;
    }

	public String getFiller() {
    	return filler;
    }

	public void setFiller(String filler) {
    	this.filler = filler;
    }


}
