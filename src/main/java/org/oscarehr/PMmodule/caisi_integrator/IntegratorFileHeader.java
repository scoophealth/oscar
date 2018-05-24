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
package org.oscarehr.PMmodule.caisi_integrator;

import java.io.Serializable;
import java.util.Date;

public class IntegratorFileHeader  implements Serializable{
	
	private Date lastDate;
	
	private Date date;

	private String dependsOn;

	private Integer cachedFacilityId;

	private String cachedFacilityName;
	
	private String username;
	

	public static final int VERSION = 1;
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getLastDate() {
		return lastDate;
	}

	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}

	public String getDependsOn() {
		return dependsOn;
	}

	public void setDependsOn(String dependsOn) {
		this.dependsOn = dependsOn;
	}
	
	public int getVersion() {
		return VERSION;
	}

	public Integer getCachedFacilityId()
	{
		return cachedFacilityId;
	}

	public void setCachedFacilityId(Integer cachedFacilityId)
	{
		this.cachedFacilityId = cachedFacilityId;
	}

	public String getCachedFacilityName()
	{
		return cachedFacilityName;
	}

	public void setCachedFacilityName(String cachedFacilityName)
	{
		this.cachedFacilityName = cachedFacilityName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	

}
