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


package org.oscarehr.eyeform.web;

import java.util.List;

import org.oscarehr.common.model.Provider;

public class ConsultationReportFormBean {

	private String status;
	private String providerNo;
	private String demographicNo;
	private String startDate;
	private String endDate;
	private String demographicName;
	
	private List<Provider> providerList;
	
	
	
	public List<Provider> getProviderList() {
		return providerList;
	}
	public void setProviderList(List<Provider> providerList) {
		this.providerList = providerList;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getProviderNo() {
		return providerNo;
	}
	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}
	public String getDemographicNo() {
		return demographicNo;
	}
	public void setDemographicNo(String demographicNo) {
		this.demographicNo = demographicNo;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getDemographicName() {
		return demographicName;
	}
	public void setDemographicName(String demographicName) {
		this.demographicName = demographicName;
	}
	
	
	
	
}
