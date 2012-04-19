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
@Table(name="rschedule")
public class RSchedule extends AbstractModel<Integer> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="provider_no")
	private String providerNo;

	@Temporal(TemporalType.DATE)
	private Date sDate;

	@Temporal(TemporalType.DATE)
	private Date eDate;

	private String available;

	@Column(name="day_of_week")
	private String dayOfWeek;

	@Column(name="avail_hour")
	private String availHour;

	@Column(name="avail_hourB")
	private String availHourB;

	private String creator;

	private String status;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	public Date getsDate() {
    	return sDate;
    }

	public void setsDate(Date sDate) {
    	this.sDate = sDate;
    }

	public Date geteDate() {
    	return eDate;
    }

	public void seteDate(Date eDate) {
    	this.eDate = eDate;
    }

	public String getAvailable() {
    	return available;
    }

	public void setAvailable(String available) {
    	this.available = available;
    }

	public String getDayOfWeek() {
    	return dayOfWeek;
    }

	public void setDayOfWeek(String dayOfWeek) {
    	this.dayOfWeek = dayOfWeek;
    }

	public String getAvailHour() {
    	return availHour;
    }

	public void setAvailHour(String availHour) {
    	this.availHour = availHour;
    }

	public String getAvailHourB() {
    	return availHourB;
    }

	public void setAvailHourB(String availHourB) {
    	this.availHourB = availHourB;
    }

	public String getCreator() {
    	return creator;
    }

	public void setCreator(String creator) {
    	this.creator = creator;
    }

	public String getStatus() {
    	return status;
    }

	public void setStatus(String status) {
    	this.status = status;
    }


}
