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
@Table(name="billing_on_header")
public class BillingONHeader extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="disk_id")
	private int diskId;

	@Column(name="transc_id")
	private String transactionId;

	@Column(name="rec_id")
	private String recordId;

	@Column(name="spec_id")
	private String specId;

	@Column(name="moh_office")
	private String mohOffice;

	@Column(name="batch_id")
	private String batchId;

	private String operator;

	@Column(name="group_num")
	private String groupNum;

	@Column(name="provider_reg_num")
	private String providerRegNum;

	private String specialty;

	@Column(name="h_count")
	private String hCount;

	@Column(name="r_count")
	private String rCount;

	@Column(name="t_count")
	private String tCount;

	@Column(name="batch_date")
	@Temporal(TemporalType.DATE)
	private Date batchDate;

	@Column(name="createdatetime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDateTime;

	@Column(name="updatedatetime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDateTime;

	private String creator;

	private String action;

	private String comment;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public int getDiskId() {
    	return diskId;
    }

	public void setDiskId(int diskId) {
    	this.diskId = diskId;
    }

	public String getTransactionId() {
    	return transactionId;
    }

	public void setTransactionId(String transactionId) {
    	this.transactionId = transactionId;
    }

	public String getRecordId() {
    	return recordId;
    }

	public void setRecordId(String recordId) {
    	this.recordId = recordId;
    }

	public String getSpecId() {
    	return specId;
    }

	public void setSpecId(String specId) {
    	this.specId = specId;
    }

	public String getMohOffice() {
    	return mohOffice;
    }

	public void setMohOffice(String mohOffice) {
    	this.mohOffice = mohOffice;
    }

	public String getBatchId() {
    	return batchId;
    }

	public void setBatchId(String batchId) {
    	this.batchId = batchId;
    }

	public String getOperator() {
    	return operator;
    }

	public void setOperator(String operator) {
    	this.operator = operator;
    }

	public String getGroupNum() {
    	return groupNum;
    }

	public void setGroupNum(String groupNum) {
    	this.groupNum = groupNum;
    }

	public String getProviderRegNum() {
    	return providerRegNum;
    }

	public void setProviderRegNum(String providerRegNum) {
    	this.providerRegNum = providerRegNum;
    }

	public String getSpecialty() {
    	return specialty;
    }

	public void setSpecialty(String specialty) {
    	this.specialty = specialty;
    }

	public String gethCount() {
    	return hCount;
    }

	public void sethCount(String hCount) {
    	this.hCount = hCount;
    }

	public String getrCount() {
    	return rCount;
    }

	public void setrCount(String rCount) {
    	this.rCount = rCount;
    }

	public String gettCount() {
    	return tCount;
    }

	public void settCount(String tCount) {
    	this.tCount = tCount;
    }

	public Date getBatchDate() {
    	return batchDate;
    }

	public void setBatchDate(Date batchDate) {
    	this.batchDate = batchDate;
    }

	public Date getCreateDateTime() {
    	return createDateTime;
    }

	public void setCreateDateTime(Date createDateTime) {
    	this.createDateTime = createDateTime;
    }

	public Date getUpdateDateTime() {
    	return updateDateTime;
    }

	public void setUpdateDateTime(Date updateDateTime) {
    	this.updateDateTime = updateDateTime;
    }

	public String getCreator() {
    	return creator;
    }

	public void setCreator(String creator) {
    	this.creator = creator;
    }

	public String getAction() {
    	return action;
    }

	public void setAction(String action) {
    	this.action = action;
    }

	public String getComment() {
    	return comment;
    }

	public void setComment(String comment) {
    	this.comment = comment;
    }



}
