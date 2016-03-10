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

//@XmlRootElement(name = "eformReportTool")
public class EFormReportToolTo1 implements Serializable {

	private Integer id;

	private String name;

	private String tableName;

	private int eformId;

	private Date expiryDate;

	private Date dateCreated;

	private String providerNo;

	private String eformName;

	private Date dateLastPopulated;

	private boolean latestMarked;

	private int numRecordsInTable;
	
	private String expiryDateString;
	
	private String startDateString;
	
	private String endDateString;
	
	private Boolean useNameAsTableName = false;
	

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

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public int getEformId() {
		return eformId;
	}

	public void setEformId(int eformId) {
		this.eformId = eformId;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public String getEformName() {
		return eformName;
	}

	public void setEformName(String eformName) {
		this.eformName = eformName;
	}

	public Date getDateLastPopulated() {
		return dateLastPopulated;
	}

	public void setDateLastPopulated(Date dateLastPopulated) {
		this.dateLastPopulated = dateLastPopulated;
	}

	public boolean isLatestMarked() {
		return latestMarked;
	}

	public void setLatestMarked(boolean latestMarked) {
		this.latestMarked = latestMarked;
	}

	public int getNumRecordsInTable() {
		return numRecordsInTable;
	}

	public void setNumRecordsInTable(int numRecordsInTable) {
		this.numRecordsInTable = numRecordsInTable;
	}

	public String getExpiryDateString() {
		return expiryDateString;
	}

	public void setExpiryDateString(String expiryDateString) {
		this.expiryDateString = expiryDateString;
	}

	public String getStartDateString() {
		return startDateString;
	}

	public void setStartDateString(String startDateString) {
		this.startDateString = startDateString;
	}

	public String getEndDateString() {
		return endDateString;
	}

	public void setEndDateString(String endDateString) {
		this.endDateString = endDateString;
	}

	public boolean isUseNameAsTableName() {
		return useNameAsTableName;
	}

	public void setUseNameAsTableName(boolean useNameAsTableName) {
		this.useNameAsTableName = useNameAsTableName;
	}
	
	
	
}
