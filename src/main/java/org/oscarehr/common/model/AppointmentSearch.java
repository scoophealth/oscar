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

@Entity
public class AppointmentSearch extends AbstractModel<Integer> {
	public static final String ONLINE = "ONLINE";

	
	
	/*create table AppointmentSearch (
			id int(10)  NOT NULL auto_increment primary key,
			providerNo varchar(6),
			searchType varchar(100),
			searchName varchar(100),
			fileContents mediumblob,
			updateDate datetime,
			createDate datetime,
			active boolean
			);
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String providerNo;
	private String searchType;
	private String searchName;
	private String uuid;

	@Column(columnDefinition = "mediumblob")
	private byte[] fileContents;
	
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

	
	private boolean active =true;

	

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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	

}
