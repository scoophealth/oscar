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
