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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="scheduledate")
public class ScheduleDate extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Temporal(TemporalType.DATE)
	@Column(name="sdate")
	private Date date;
	@Column(name="provider_no")
	private String providerNo;
	private char available;
	private char priority;
	private String reason;
	private String hour;
	private String creator;
	private char status;
	
	public Integer getId() {
		return id;
	}

	public Date getDate() {
    	return date;
    }

	public void setDate(Date date) {
    	this.date = date;
    }

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	public char getAvailable() {
    	return available;
    }

	public void setAvailable(char available) {
    	this.available = available;
    }

	public char getPriority() {
    	return priority;
    }

	public void setPriority(char priority) {
    	this.priority = priority;
    }

	public String getReason() {
    	return reason;
    }

	public void setReason(String reason) {
    	this.reason = reason;
    }

	public String getHour() {
    	return hour;
    }

	public void setHour(String hour) {
    	this.hour = hour;
    }

	public String getCreator() {
    	return creator;
    }

	public void setCreator(String creator) {
    	this.creator = creator;
    }

	public char getStatus() {
    	return status;
    }

	public void setStatus(char status) {
    	this.status = status;
    }
	

}
