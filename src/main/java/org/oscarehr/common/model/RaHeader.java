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

        @Override
	public Integer getId() {
    	return id;
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
