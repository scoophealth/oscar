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
@Table(name="billing_on_diskname")
public class BillingONDiskName extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String monthCode;

	@Column(name="batchcount")
	private int batchCount;

	@Column(name="ohipfilename")
	private String ohipFilename;

	@Column(name="groupno")
	private String groupNo;

	private String creator;

	@Column(name="claimrecord")
	private String claimRecord;

	@Column(name="createdatetime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDateTime;

	private String status;

	private String total;

	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;

	public String getMonthCode() {
    	return monthCode;
    }

	public void setMonthCode(String monthCode) {
    	this.monthCode = monthCode;
    }

	public int getBatchCount() {
    	return batchCount;
    }

	public void setBatchCount(int batchCount) {
    	this.batchCount = batchCount;
    }

	public String getOhipFilename() {
    	return ohipFilename;
    }

	public void setOhipFilename(String ohipFilename) {
    	this.ohipFilename = ohipFilename;
    }

	public String getGroupNo() {
    	return groupNo;
    }

	public void setGroupNo(String groupNo) {
    	this.groupNo = groupNo;
    }

	public String getCreator() {
    	return creator;
    }

	public void setCreator(String creator) {
    	this.creator = creator;
    }

	public String getClaimRecord() {
    	return claimRecord;
    }

	public void setClaimRecord(String claimRecord) {
    	this.claimRecord = claimRecord;
    }

	public Date getCreateDateTime() {
    	return createDateTime;
    }

	public void setCreateDateTime(Date createDateTime) {
    	this.createDateTime = createDateTime;
    }

	public String getStatus() {
    	return status;
    }

	public void setStatus(String status) {
    	this.status = status;
    }

	public String getTotal() {
    	return total;
    }

	public void setTotal(String total) {
    	this.total = total;
    }

	public Date getTimestamp() {
    	return timestamp;
    }

	public void setTimestamp(Date timestamp) {
    	this.timestamp = timestamp;
    }

	public Integer getId() {
    	return id;
    }



}
