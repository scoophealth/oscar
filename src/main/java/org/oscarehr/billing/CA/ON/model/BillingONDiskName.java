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
