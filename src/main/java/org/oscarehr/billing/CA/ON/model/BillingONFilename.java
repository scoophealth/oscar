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
@Table(name="billing_on_filename")
public class BillingONFilename extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="disk_id")
	private int diskId;

	@Column(name="htmlfilename")
	private String htmlFilename;

	@Column(name="providerohipno")
	private String providerOhipNo;

	@Column(name="providerno")
	private String providerNo;

	@Column(name="claimrecord")
	private String claimRecord;

	private String status;

	private String total;

	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;

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

	public String getHtmlFilename() {
    	return htmlFilename;
    }

	public void setHtmlFilename(String htmlFilename) {
    	this.htmlFilename = htmlFilename;
    }

	public String getProviderOhipNo() {
    	return providerOhipNo;
    }

	public void setProviderOhipNo(String providerOhipNo) {
    	this.providerOhipNo = providerOhipNo;
    }

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	public String getClaimRecord() {
    	return claimRecord;
    }

	public void setClaimRecord(String claimRecord) {
    	this.claimRecord = claimRecord;
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


}
