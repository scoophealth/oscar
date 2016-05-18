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
@Table(name="teleplanC12")
public class TeleplanC12 extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="c12_id")
	private Integer id;

	@Column(name="s21_id")
	private Integer s21Id;

	@Column(name="filename")
	private String fileName;

	@Column(name="t_datacenter")
	private String dataCentre;

	@Column(name="t_dataseq")
	private String dataSeq;

	@Column(name="t_payeeno")
	private String payeeNo;

	@Column(name="t_practitioner_no")
	private String practitionerNo;

	@Column(name="t_exp1")
	private String exp1;

	@Column(name="t_exp2")
	private String exp2;

	@Column(name="t_exp3")
	private String exp3;

	@Column(name="t_exp4")
	private String exp4;

	@Column(name="t_exp5")
	private String exp5;

	@Column(name="t_exp6")
	private String exp6;

	@Column(name="t_exp7")
	private String exp7;

	@Column(name="t_officefolioclaimno")
	private String officeFolioClaimNo;

	@Column(name="t_filler")
	private String filler;

	@Column(name="status")
	private Character status = 'O';

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

	public String getPayeeNo() {
    	return payeeNo;
    }

	public void setPayeeNo(String payeeNo) {
    	this.payeeNo = payeeNo;
    }

	public String getPractitionerNo() {
    	return practitionerNo;
    }

	public void setPractitionerNo(String practitionerNo) {
    	this.practitionerNo = practitionerNo;
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

	public String getOfficeFolioClaimNo() {
    	return officeFolioClaimNo;
    }

	public void setOfficeFolioClaimNo(String officeFolioClaimNo) {
    	this.officeFolioClaimNo = officeFolioClaimNo;
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

	public String[] getExps() {
		return new String[] { getExp1(), getExp2(), getExp3(), getExp4(), getExp5(), getExp6(), getExp7() };
	}
}
