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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class FaxClientLog extends AbstractModel<Integer>{
	@Column(name="faxLogId")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name="provider_no")
	private String providerNo;
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;
	private String result;
	private String requestId;
	private String faxId;


	@Override
    public Integer getId() {
	   return id;
    }


	public String getProviderNo() {
    	return providerNo;
    }


	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }


	public Date getStartTime() {
    	return startTime;
    }


	public void setStartTime(Date startTime) {
    	this.startTime = startTime;
    }


	public Date getEndTime() {
    	return endTime;
    }


	public void setEndTime(Date endTime) {
    	this.endTime = endTime;
    }


	public String getResult() {
    	return result;
    }


	public void setResult(String result) {
    	this.result = result;
    }


	public String getRequestId() {
    	return requestId;
    }


	public void setRequestId(String requestId) {
    	this.requestId = requestId;
    }


	public String getFaxId() {
    	return faxId;
    }


	public void setFaxId(String faxId) {
    	this.faxId = faxId;
    }



}
