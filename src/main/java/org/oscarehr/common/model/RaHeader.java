package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="raheader")
public class RaHeader extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="raheader_no")
	private Integer id;

	private String filename;

	@Column(name="paymentdate")
	private String paymentDate;

	private String payable;

	@Column(name="totalamount")
	private String totalAmount;

	private String records;

	private String claims;

	private String status;

	@Column(name="readdate")
	private String readDate;

	private String content;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getFilename() {
    	return filename;
    }

	public void setFilename(String filename) {
    	this.filename = filename;
    }

	public String getPaymentDate() {
    	return paymentDate;
    }

	public void setPaymentDate(String paymentDate) {
    	this.paymentDate = paymentDate;
    }

	public String getPayable() {
    	return payable;
    }

	public void setPayable(String payable) {
    	this.payable = payable;
    }

	public String getTotalAmount() {
    	return totalAmount;
    }

	public void setTotalAmount(String totalAmount) {
    	this.totalAmount = totalAmount;
    }

	public String getRecords() {
    	return records;
    }

	public void setRecords(String records) {
    	this.records = records;
    }

	public String getClaims() {
    	return claims;
    }

	public void setClaims(String claims) {
    	this.claims = claims;
    }

	public String getStatus() {
    	return status;
    }

	public void setStatus(String status) {
    	this.status = status;
    }

	public String getReadDate() {
    	return readDate;
    }

	public void setReadDate(String readDate) {
    	this.readDate = readDate;
    }

	public String getContent() {
    	return content;
    }

	public void setContent(String content) {
    	this.content = content;
    }


}
