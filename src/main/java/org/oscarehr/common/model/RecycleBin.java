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
@Table(name="recyclebin")
public class RecycleBin extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="recyclebin_no")
	private Integer id;

	@Column(name="provider_no")
	private String providerNo;

	@Column(name="updatedatetime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDateTime;

	@Column(name="table_name")
	private String tableName;

	private String keyword;

	@Column(name="table_content")
	private String tableContent;

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

	public Date getUpdateDateTime() {
    	return updateDateTime;
    }

	public void setUpdateDateTime(Date updateDateTime) {
    	this.updateDateTime = updateDateTime;
    }

	public String getTableName() {
    	return tableName;
    }

	public void setTableName(String tableName) {
    	this.tableName = tableName;
    }

	public String getKeyword() {
    	return keyword;
    }

	public void setKeyword(String keyword) {
    	this.keyword = keyword;
    }

	public String getTableContent() {
    	return tableContent;
    }

	public void setTableContent(String tableContent) {
    	this.tableContent = tableContent;
    }


}
