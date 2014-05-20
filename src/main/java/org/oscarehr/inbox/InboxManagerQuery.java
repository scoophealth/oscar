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
package org.oscarehr.inbox;

import java.util.Date;

public class InboxManagerQuery {
	
	private String providerNo;
	private String searchProviderNo;
	private String status;
	private Integer demographicNo;
	private String scannedDocStatus = "I";
	private String patientLastName;
	private String patientFirstName;
	private String patientHIN;
	
	private int page=0;
	private int pageSize=20;
	private Date startDate;
	private Date endDate;
	
	private String view;
	
	private Date newestDate;
	
	public String getProviderNo() {
		return providerNo;
	}
	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}
	public String getSearchProviderNo() {
		return searchProviderNo;
	}
	public void setSearchProviderNo(String searchProviderNo) {
		this.searchProviderNo = searchProviderNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getDemographicNo() {
		return demographicNo;
	}
	public void setDemographicNo(Integer demographicNo) {
		this.demographicNo = demographicNo;
	}
	public String getScannedDocStatus() {
		return scannedDocStatus;
	}
	public void setScannedDocStatus(String scannedDocStatus) {
		this.scannedDocStatus = scannedDocStatus;
	}
	public String getPatientLastName() {
		return patientLastName;
	}
	public void setPatientLastName(String patientLastName) {
		this.patientLastName = patientLastName;
	}
	public String getPatientFirstName() {
		return patientFirstName;
	}
	public void setPatientFirstName(String patientFirstName) {
		this.patientFirstName = patientFirstName;
	}
	public String getPatientHIN() {
		return patientHIN;
	}
	public void setPatientHIN(String patientHIN) {
		this.patientHIN = patientHIN;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
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
	public String getView() {
		return view;
	}
	public void setView(String view) {
		this.view = view;
	}
	
	public Date getNewestDate() {
		return newestDate;
	}
	public void setNewestDate(Date newestDate) {
		this.newestDate = newestDate;
	}
}