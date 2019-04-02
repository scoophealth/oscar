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



/*
 * This stores prevention report json objects 
 */

@Entity
public class PreventionReport extends AbstractModel<Integer> {
	
	/*
	 create table PreventionReport (
				id int(10)  NOT NULL auto_increment primary key,
				providerNo varchar(6),
				reportName varchar(255),
				json text,
				updateDate datetime,
				createDate datetime,
				active boolean,
				archived boolean,
				uuid varchar(50)
				);
	*/

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String providerNo;
	private String reportName;
	private String uuid;
	
	@Column(columnDefinition = "text")
	private String json;
		
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate = new Date();
		
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate = new Date();
	
	public Date getUpdateDate() {
	return updateDate;
	}
	
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	private boolean archived = false;
	private boolean active =true;
	
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

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public String getJson() {
		return json;
	}
	
	public void setJson(String fileContents) {
			this.json = fileContents;
	}
	
}
