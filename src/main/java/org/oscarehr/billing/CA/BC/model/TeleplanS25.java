package org.oscarehr.billing.CA.BC.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.oscarehr.common.model.AbstractModel;

@Entity
@Table(name="teleplanS25")
public class TeleplanS25 extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="s25_id")
	private Integer id;

	@Column(name="s21_id")
	private Integer s21Id;

	@Column(name="filename")
	private String fileName;

	@Column(name="t_s25type")
	private String s25Type;

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

	@Column(name="t_message")
	private String message;

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

	public String getS25Type() {
    	return s25Type;
    }

	public void setS25Type(String s25Type) {
    	this.s25Type = s25Type;
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

	public String getMessage() {
    	return message;
    }

	public void setMessage(String message) {
    	this.message = message;
    }

	public String getFiller() {
    	return filler;
    }

	public void setFiller(String filler) {
    	this.filler = filler;
    }


}
