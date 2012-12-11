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


package org.oscarehr.billing.CA.model;

import java.util.Comparator;
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
@Table(name="billactivity")
public class BillActivity extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String monthCode;

	@Column(name="batchcount")
	private int batchCount;

	@Column(name="htmlfilename")
	private String htmlFilename;

	@Column(name="ohipfilename")
	private String ohipFilename;

	@Column(name="providerohipno")
	private String providerOhipNo;

	@Column(name="groupno")
	private String groupNo;

	@Column(name="creator")
	private String creator;

	@Column(name="htmlcontext")
	private String htmlContext;

	@Column(name="ohipcontext")
	private String ohipContext;

	@Column(name="claimrecord")
	private String claimRecord;

	@Column(name="updatedatetime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDateTime;

	private String status;

	private String total;

	@Column(name="sentdate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date sentDate;


	@Override
    public Integer getId() {
		return id;
	}

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

	public String getHtmlFilename() {
    	return htmlFilename;
    }

	public void setHtmlFilename(String htmlFilename) {
    	this.htmlFilename = htmlFilename;
    }

	public String getOhipFilename() {
    	return ohipFilename;
    }

	public void setOhipFilename(String ohipFilename) {
    	this.ohipFilename = ohipFilename;
    }

	public String getProviderOhipNo() {
    	return providerOhipNo;
    }

	public void setProviderOhipNo(String providerOhipNo) {
    	this.providerOhipNo = providerOhipNo;
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

	public String getHtmlContext() {
    	return htmlContext;
    }

	public void setHtmlContext(String htmlContext) {
    	this.htmlContext = htmlContext;
    }

	public String getOhipContext() {
    	return ohipContext;
    }

	public void setOhipContext(String ohipContext) {
    	this.ohipContext = ohipContext;
    }

	public String getClaimRecord() {
    	return claimRecord;
    }

	public void setClaimRecord(String claimRecord) {
    	this.claimRecord = claimRecord;
    }

	public Date getUpdateDateTime() {
    	return updateDateTime;
    }

	public void setUpdateDateTime(Date updateDateTime) {
    	this.updateDateTime = updateDateTime;
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

	public Date getSentDate() {
    	return sentDate;
    }

	public void setSentDate(Date sentDate) {
    	this.sentDate = sentDate;
    }


    public static final Comparator<BillActivity> UpdateDateTimeComparator = new Comparator<BillActivity>() {
        public int compare(BillActivity o1, BillActivity o2) {
                return o2.getUpdateDateTime().compareTo(o1.getUpdateDateTime());
        }
    }; 

}
