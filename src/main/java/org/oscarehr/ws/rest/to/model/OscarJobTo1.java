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
package org.oscarehr.ws.rest.to.model;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="OscarJob")
public class OscarJobTo1 implements Serializable {
   
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private String name;
	
	private String description;
	
	private int oscarJobTypeId;
	
	private OscarJobTypeTo1 oscarJobType;
		
	private String cronExpression;
	
	private boolean enabled = false;

	private String providerNo;
	
	/* for web */
	private Date nextPlannedExecutionDate;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getOscarJobTypeId() {
		return oscarJobTypeId;
	}

	public void setOscarJobTypeId(int oscarJobTypeId) {
		this.oscarJobTypeId = oscarJobTypeId;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public OscarJobTypeTo1 getOscarJobType() {
		return oscarJobType;
	}

	public void setOscarJobType(OscarJobTypeTo1 oscarJobType) {
		this.oscarJobType = oscarJobType;
	}

	public Date getNextPlannedExecutionDate() {
		return nextPlannedExecutionDate;
	}

	public void setNextPlannedExecutionDate(Date nextPlannedExecutionDate) {
		this.nextPlannedExecutionDate = nextPlannedExecutionDate;
	}
	
	
	
}
