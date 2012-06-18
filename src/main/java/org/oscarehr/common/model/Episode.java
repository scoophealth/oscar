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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;

@Entity
public class Episode extends AbstractModel<Integer> {

	@Transient
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private int demographicNo;
	private Date startDate;
	private Date endDate;
	private String code;
	private String codingSystem;
	private String description;
	private String status;
	private String lastUpdateUser;
	private Date lastUpdateTime;
	private String notes;

	public Integer getId() {
    	return id;
    }
	public void setId(Integer id) {
    	this.id = id;
    }
	public Date getStartDate() {
    	return startDate;
    }
	public void setStartDate(Date startDate) {
    	this.startDate = startDate;
    }
	public Date getEndDate() {
    	return endDate;
    }
	public void setEndDate(Date endDate) {
    	this.endDate = endDate;
    }
	public String getCode() {
    	return code;
    }
	public void setCode(String code) {
    	this.code = code;
    }
	public String getCodingSystem() {
    	return codingSystem;
    }
	public void setCodingSystem(String codingSystem) {
    	this.codingSystem = codingSystem;
    }
	public String getDescription() {
    	return description;
    }
	public void setDescription(String description) {
    	this.description = description;
    }
	public String getStatus() {
    	return status;
    }
	public void setStatus(String status) {
    	this.status = status;
    }
	public int getDemographicNo() {
    	return demographicNo;
    }
	public void setDemographicNo(int demographicNo) {
    	this.demographicNo = demographicNo;
    }
	public String getLastUpdateUser() {
    	return lastUpdateUser;
    }
	public void setLastUpdateUser(String lastUpdateUser) {
    	this.lastUpdateUser = lastUpdateUser;
    }
	public Date getLastUpdateTime() {
    	return lastUpdateTime;
    }
	public void setLastUpdateTime(Date lastUpdateTime) {
    	this.lastUpdateTime = lastUpdateTime;
    }
	@PrePersist
	@PreUpdate
	public void updateTime() {
		setLastUpdateTime(new Date());
	}

	public void setStartDateStr(String val) {
		try {
			setStartDate(formatter.parse(val));
		}catch(ParseException e) {
			//MiscUtils.getLogger().warn("error",e);
		}
	}

	public void setEndDateStr(String val) {
		try {
			setEndDate(formatter.parse(val));
		}catch(ParseException e) {
			//MiscUtils.getLogger().warn("error",e);
		}
	}

	public String getStartDateStr() {
		if(getStartDate()!=null)
			return formatter.format(getStartDate());
		return "";
	}

	public String getEndDateStr() {
		if(getEndDate() != null)
			return formatter.format(getEndDate());
		return "";
	}
	public String getNotes() {
    	return notes;
    }
	public void setNotes(String notes) {
    	this.notes = notes;
    }
	
	
}
