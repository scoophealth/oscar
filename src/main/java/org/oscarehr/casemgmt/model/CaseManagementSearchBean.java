/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.casemgmt.model;

public class CaseManagementSearchBean {

	private String demographicNo;
	
	private String searchStartDate;
	private String searchEndDate;
	private int searchRoleId;
	private String searchProviderNo;
	private int searchProgramId;
	private String searchText;
	private String searchEncounterType;
	
	public CaseManagementSearchBean(String demographicNo) {
		this.demographicNo = demographicNo;
	}
	
	public String getSearchEncounterType() {
		return searchEncounterType;
	}
	public String getSearchEndDate() {
		return searchEndDate;
	}
	public int getSearchProgramId() {
		return searchProgramId;
	}
	public String getSearchProviderNo() {
		return searchProviderNo;
	}
	public int getSearchRoleId() {
		return searchRoleId;
	}
	public String getSearchStartDate() {
		return searchStartDate;
	}
	public String getSearchText() {
		return searchText;
	}
	public void setSearchEncounterType(String searchEncounterType) {
		this.searchEncounterType = searchEncounterType;
	}
	public void setSearchEndDate(String searchEndDate) {
		this.searchEndDate = searchEndDate;
	}
	public void setSearchProgramId(int searchProgramId) {
		this.searchProgramId = searchProgramId;
	}
	public void setSearchProviderNo(String searchProviderNo) {
		this.searchProviderNo = searchProviderNo;
	}
	public void setSearchRoleId(int searchRoleId) {
		this.searchRoleId = searchRoleId;
	}
	public void setSearchStartDate(String searchStartDate) {
		this.searchStartDate = searchStartDate;
	}
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}
	public String getDemographicNo() {
		return demographicNo;
	}
	public void setDemographicNo(String demographicNo) {
		this.demographicNo = demographicNo;
	}
	
	
}
