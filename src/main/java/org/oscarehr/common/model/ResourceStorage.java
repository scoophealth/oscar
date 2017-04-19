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

import javax.persistence.*;

import org.oscarehr.drools.RuleBaseFactory;

@Entity
public class ResourceStorage extends AbstractModel<Integer> {
	public static final String PREVENTION_RULES = "PREVENTION_RULES";

	public static final String LU_CODES = "LU_CODES";
	
	/*create table ResourceStorage (
			id int(10)  NOT NULL auto_increment primary key,
			resourceType varchar(100),
			resourceName varchar(100),
			uuid varchar(40),
			fileContents mediumblob,
			uploadDate datetime,
			active boolean
			);
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(columnDefinition = "mediumblob")
	private byte[] fileContents;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date uploadDate;

	private String resourceType = null; 
	private String resourceName = null;
	private String uuid = null;
	private boolean active;

	public Date getUploadDate() {
    	return uploadDate;
    }

	public void setUploadDate(Date uploadDate) {
    	this.uploadDate = uploadDate;
    }

	@Override
	public Integer getId() {
		return id;
	}

	public byte[] getFileContents() {
		return fileContents;
	}

	public void setFileContents(byte[] fileContents) {
		this.fileContents = fileContents;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	
	@PostUpdate
	public void removeFromCache()
	{
		RuleBaseFactory.removeRuleBase("ResourceStorage:"+getId());
	}
}
