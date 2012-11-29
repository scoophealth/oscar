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
@Table(name = "teleplanS00")
public class TeleplanS00 extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "s00_id")
	private Integer id;

	@Column(name = "s21_id")
	private Integer s21Id;

	@Column(name = "filename")
	private String fileName;

	@Column(name = "t_s00type")
	private String s00Type;

	@Column(name = "t_datacenter")
	private String dataCentre;

	@Column(name = "t_dataseq")
	private String dataSeq;

	@Column(name = "t_payment")
	private String payment;

	@Column(name = "t_linecode")
	private Character lineCode;

	@Column(name = "t_payeeno")
	private String payeeNo;

	@Column(name = "t_mspctlno")
	private String mspCtlNo;

	@Column(name = "t_practitionerno")
	private String practitionerNo;

	@Column(name = "t_msprcddate")
	private String mspRcdDate;

	@Column(name = "t_initial")
	private String initial;

	@Column(name = "t_surname")
	private String surname;

	@Column(name = "t_phn")
	private String phn;

	@Column(name = "t_phndepno")
	private String phnDepNo;

	@Column(name = "t_servicedate")
	private String serviceDate;

	@Column(name = "t_today")
	private String today;

	@Column(name = "t_billnoservices")
	private String billNoServices;

	@Column(name = "t_billClafCode")
	private String billClafCode;

	@Column(name = "t_billfeeschedule")
	private String billFeeSchedule;

	@Column(name = "t_billamt")
	private String billAmount;

	@Column(name = "t_paidNoServices")
	private String paidNoServices;

	@Column(name = "t_paidClafCode")
	private String paidClafCode;

	@Column(name = "t_paidfeeschedule")
	private String paidFeeSchedule;

	@Column(name = "t_paidamt")
	private String paidAmount;

	@Column(name = "t_officeno")
	private String officeNo;

	@Column(name = "t_exp1")
	private String exp1;

	@Column(name = "t_exp2")
	private String exp2;

	@Column(name = "t_exp3")
	private String exp3;

	@Column(name = "t_exp4")
	private String exp4;

	@Column(name = "t_exp5")
	private String exp5;

	@Column(name = "t_exp6")
	private String exp6;

	@Column(name = "t_exp7")
	private String exp7;

	@Column(name = "t_ajc1")
	private String ajc1;

	@Column(name = "t_aja1")
	private String aja1;

	@Column(name = "t_ajc2")
	private String ajc2;

	@Column(name = "t_aja2")
	private String aja2;

	@Column(name = "t_ajc3")
	private String ajc3;

	@Column(name = "t_aja3")
	private String aja3;

	@Column(name = "t_ajc4")
	private String ajc4;

	@Column(name = "t_aja4")
	private String aja4;

	@Column(name = "t_ajc5")
	private String ajc5;

	@Column(name = "t_aja5")
	private String aja5;

	@Column(name = "t_ajc6")
	private String ajc6;

	@Column(name = "t_aja6")
	private String aja6;

	@Column(name = "t_ajc7")
	private String ajc7;

	@Column(name = "t_aja7")
	private String aja7;

	@Column(name = "t_paidRate")
	private String paidRate;

	@Column(name = "t_planrefno")
	private String planRefNo;

	@Column(name = "t_claimsource")
	private String claimSource;

	@Column(name = "t_previouspaiddate")
	private String previousPaidDate;

	@Column(name = "t_icbcwcb")
	private String icBcWcb;

	@Column(name = "t_insurercode")
	private String insurerCode;

	@Column(name = "t_filler")
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

	public String getS00Type() {
		return s00Type;
	}

	public void setS00Type(String s00Type) {
		this.s00Type = s00Type;
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

	public String getMspRcdDate() {
		return mspRcdDate;
	}

	public void setMspRcdDate(String mspRcdDate) {
		this.mspRcdDate = mspRcdDate;
	}

	public String getInitial() {
		return initial;
	}

	public void setInitial(String initial) {
		this.initial = initial;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPhn() {
		return phn;
	}

	public void setPhn(String phn) {
		this.phn = phn;
	}

	public String getPhnDepNo() {
		return phnDepNo;
	}

	public void setPhnDepNo(String phnDepNo) {
		this.phnDepNo = phnDepNo;
	}

	public String getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(String serviceDate) {
		this.serviceDate = serviceDate;
	}

	public String getToday() {
		return today;
	}

	public void setToday(String today) {
		this.today = today;
	}

	public String getBillNoServices() {
		return billNoServices;
	}

	public void setBillNoServices(String billNoServices) {
		this.billNoServices = billNoServices;
	}

	public String getBillClafCode() {
		return billClafCode;
	}

	public void setBillClafCode(String billClafCode) {
		this.billClafCode = billClafCode;
	}

	public String getBillFeeSchedule() {
		return billFeeSchedule;
	}

	public void setBillFeeSchedule(String billFeeSchedule) {
		this.billFeeSchedule = billFeeSchedule;
	}

	public String getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(String billAmount) {
		this.billAmount = billAmount;
	}

	public String getPaidNoServices() {
		return paidNoServices;
	}

	public void setPaidNoServices(String paidNoServices) {
		this.paidNoServices = paidNoServices;
	}

	public String getPaidClafCode() {
		return paidClafCode;
	}

	public void setPaidClafCode(String paidClafCode) {
		this.paidClafCode = paidClafCode;
	}

	public String getPaidFeeSchedule() {
		return paidFeeSchedule;
	}

	public void setPaidFeeSchedule(String paidFeeSchedule) {
		this.paidFeeSchedule = paidFeeSchedule;
	}

	public String getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(String paidAmount) {
		this.paidAmount = paidAmount;
	}

	public String getOfficeNo() {
		return officeNo;
	}

	public void setOfficeNo(String officeNo) {
		this.officeNo = officeNo;
	}

	public String getExp1() {
		return exp1;
	}

	public void setExp1(String exp1) {
		this.exp1 = exp1;
	}

	public String getExp2() {
		return exp2;
	}

	public void setExp2(String exp2) {
		this.exp2 = exp2;
	}

	public String getExp3() {
		return exp3;
	}

	public void setExp3(String exp3) {
		this.exp3 = exp3;
	}

	public String getExp4() {
		return exp4;
	}

	public void setExp4(String exp4) {
		this.exp4 = exp4;
	}

	public String getExp5() {
		return exp5;
	}

	public void setExp5(String exp5) {
		this.exp5 = exp5;
	}

	public String getExp6() {
		return exp6;
	}

	public void setExp6(String exp6) {
		this.exp6 = exp6;
	}

	public String getExp7() {
		return exp7;
	}

	public void setExp7(String exp7) {
		this.exp7 = exp7;
	}

	public String getAjc1() {
		return ajc1;
	}

	public void setAjc1(String ajc1) {
		this.ajc1 = ajc1;
	}

	public String getAja1() {
		return aja1;
	}

	public void setAja1(String aja1) {
		this.aja1 = aja1;
	}

	public String getAjc2() {
		return ajc2;
	}

	public void setAjc2(String ajc2) {
		this.ajc2 = ajc2;
	}

	public String getAja2() {
		return aja2;
	}

	public void setAja2(String aja2) {
		this.aja2 = aja2;
	}

	public String getAjc3() {
		return ajc3;
	}

	public void setAjc3(String ajc3) {
		this.ajc3 = ajc3;
	}

	public String getAja3() {
		return aja3;
	}

	public void setAja3(String aja3) {
		this.aja3 = aja3;
	}

	public String getAjc4() {
		return ajc4;
	}

	public void setAjc4(String ajc4) {
		this.ajc4 = ajc4;
	}

	public String getAja4() {
		return aja4;
	}

	public void setAja4(String aja4) {
		this.aja4 = aja4;
	}

	public String getAjc5() {
		return ajc5;
	}

	public void setAjc5(String ajc5) {
		this.ajc5 = ajc5;
	}

	public String getAja5() {
		return aja5;
	}

	public void setAja5(String aja5) {
		this.aja5 = aja5;
	}

	public String getAjc6() {
		return ajc6;
	}

	public void setAjc6(String ajc6) {
		this.ajc6 = ajc6;
	}

	public String getAja6() {
		return aja6;
	}

	public void setAja6(String aja6) {
		this.aja6 = aja6;
	}

	public String getAjc7() {
		return ajc7;
	}

	public void setAjc7(String ajc7) {
		this.ajc7 = ajc7;
	}

	public String getAja7() {
		return aja7;
	}

	public void setAja7(String aja7) {
		this.aja7 = aja7;
	}

	public String getPaidRate() {
		return paidRate;
	}

	public void setPaidRate(String paidRate) {
		this.paidRate = paidRate;
	}

	public String getPlanRefNo() {
		return planRefNo;
	}

	public void setPlanRefNo(String planRefNo) {
		this.planRefNo = planRefNo;
	}

	public String getClaimSource() {
		return claimSource;
	}

	public void setClaimSource(String claimSource) {
		this.claimSource = claimSource;
	}

	public String getPreviousPaidDate() {
		return previousPaidDate;
	}

	public void setPreviousPaidDate(String previousPaidDate) {
		this.previousPaidDate = previousPaidDate;
	}

	public String getIcBcWcb() {
		return icBcWcb;
	}

	public void setIcBcWcb(String icBcWcb) {
		this.icBcWcb = icBcWcb;
	}

	public String getInsurerCode() {
		return insurerCode;
	}

	public void setInsurerCode(String insurerCode) {
		this.insurerCode = insurerCode;
	}

	public String getFiller() {
		return filler;
	}

	public void setFiller(String filler) {
		this.filler = filler;
	}

	public String[] getExps() {
		return new String[] { getExp1(), getExp2(), getExp3(), getExp4(), getExp5(), getExp6(), getExp7() };
	}

}
