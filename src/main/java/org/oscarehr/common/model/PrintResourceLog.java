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

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
public class PrintResourceLog extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String providerNo;
	  
	private String resourceName;
	  
	private String resourceId;
	  
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateTime;
	
	private String externalLocation;
	  
	private String externalMethod;

	@Transient
	private String providerName;
	
 
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

	public String getResourceName() {
    	return resourceName;
    }

	public void setResourceName(String resourceName) {
    	this.resourceName = resourceName;
    }

	public String getResourceId() {
    	return resourceId;
    }

	public void setResourceId(String resourceId) {
    	this.resourceId = resourceId;
    }

	public Date getDateTime() {
    	return dateTime;
    }

	public void setDateTime(Date dateTime) {
    	this.dateTime = dateTime;
    }

	public String getExternalLocation() {
    	return externalLocation;
    }

	public void setExternalLocation(String externalLocation) {
    	this.externalLocation = externalLocation;
    }

	public String getExternalMethod() {
    	return externalMethod;
    }

	public void setExternalMethod(String externalMethod) {
    	this.externalMethod = externalMethod;
    }

	public String getFormattedDateString() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:ss");
		return formatter.format(getDateTime());
	}

	public String getProviderName() {
    	return providerName;
    }

	public void setProviderName(String providerName) {
    	this.providerName = providerName;
    }
	
	
	
}
