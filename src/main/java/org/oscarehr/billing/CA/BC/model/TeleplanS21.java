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
@Table(name="teleplanS21")
public class TeleplanS21 extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="s21_id")
	private Integer id;

	@Column(name="filename")
	private String fileName;

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

	@Column(name="t_payeename")
	private String payeeName;

	@Column(name="t_amtbilled")
	private String amountBilled;

	@Column(name="t_amtpaid")
	private String amountPaid;

	@Column(name="t_balancefwd")
	private String balanceForward;

	@Column(name="t_cheque")
	private String cheque;

	@Column(name="t_newbalance")
	private String newBalance;

	@Column(name="t_filler")
	private String filler;

	private Character status;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getFileName() {
    	return fileName;
    }

	public void setFileName(String fileName) {
    	this.fileName = fileName;
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

	public String getPayeeName() {
    	return payeeName;
    }

	public void setPayeeName(String payeeName) {
    	this.payeeName = payeeName;
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

	public String getBalanceForward() {
    	return balanceForward;
    }

	public void setBalanceForward(String balanceForward) {
    	this.balanceForward = balanceForward;
    }

	public String getCheque() {
    	return cheque;
    }

	public void setCheque(String cheque) {
    	this.cheque = cheque;
    }

	public String getNewBalance() {
    	return newBalance;
    }

	public void setNewBalance(String newBalance) {
    	this.newBalance = newBalance;
    }

	public String getFiller() {
    	return filler;
    }

	public void setFiller(String filler) {
    	this.filler = filler;
    }

	public Character getStatus() {
    	return status;
    }

	public void setStatus(Character status) {
    	this.status = status;
    }



}
