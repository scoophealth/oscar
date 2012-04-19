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
@Table(name="teleplanS23")
public class TeleplanS23 extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="s23_id")
	private Integer id;

	@Column(name="s21_id")
	private Integer s21Id;

	@Column(name="filename")
	private String fileName;

	@Column(name="t_s23type")
	private String s23Type;

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

	@Column(name="t_ajc")
	private String ajc;

	@Column(name="t_aji")
	private String aji;

	@Column(name="t_ajm")
	private String ajm;

	@Column(name="t_calcMethod")
	private String calcMethod;

	@Column(name="t_rpercent")
	private String rPercent;

	@Column(name="t_opercent")
	private String oPercent;

	@Column(name="t_gamount")
	private String gAmount;

	@Column(name="t_ramount")
	private String rAmount;

	@Column(name="t_oamount")
	private String oAmount;

	@Column(name="t_balancefwd")
	private String balanceForward;

	@Column(name="t_adjmade")
	private String adjMade;

	@Column(name="t_adjoutstanding")
	private String adjOutstanding;

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

	public String getS23Type() {
    	return s23Type;
    }

	public void setS23Type(String s23Type) {
    	this.s23Type = s23Type;
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

	public String getAjc() {
    	return ajc;
    }

	public void setAjc(String ajc) {
    	this.ajc = ajc;
    }

	public String getAji() {
    	return aji;
    }

	public void setAji(String aji) {
    	this.aji = aji;
    }

	public String getAjm() {
    	return ajm;
    }

	public void setAjm(String ajm) {
    	this.ajm = ajm;
    }

	public String getCalcMethod() {
    	return calcMethod;
    }

	public void setCalcMethod(String calcMethod) {
    	this.calcMethod = calcMethod;
    }

	public String getrPercent() {
    	return rPercent;
    }

	public void setrPercent(String rPercent) {
    	this.rPercent = rPercent;
    }

	public String getoPercent() {
    	return oPercent;
    }

	public void setoPercent(String oPercent) {
    	this.oPercent = oPercent;
    }

	public String getgAmount() {
    	return gAmount;
    }

	public void setgAmount(String gAmount) {
    	this.gAmount = gAmount;
    }

	public String getrAmount() {
    	return rAmount;
    }

	public void setrAmount(String rAmount) {
    	this.rAmount = rAmount;
    }

	public String getoAmount() {
    	return oAmount;
    }

	public void setoAmount(String oAmount) {
    	this.oAmount = oAmount;
    }

	public String getBalanceForward() {
    	return balanceForward;
    }

	public void setBalanceForward(String balanceForward) {
    	this.balanceForward = balanceForward;
    }

	public String getAdjMade() {
    	return adjMade;
    }

	public void setAdjMade(String adjMade) {
    	this.adjMade = adjMade;
    }

	public String getAdjOutstanding() {
    	return adjOutstanding;
    }

	public void setAdjOutstanding(String adjOutstanding) {
    	this.adjOutstanding = adjOutstanding;
    }

	public String getFiller() {
    	return filler;
    }

	public void setFiller(String filler) {
    	this.filler = filler;
    }


}
