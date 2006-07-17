package org.caisi.casemgmt.web.formbeans;

import org.apache.struts.action.ActionForm;
import org.caisi.casemgmt.model.CaseManagementCPP;

public class CaseManagementViewFormBean extends ActionForm {
	private String demographicNo;
	private String providerNo;
	private String issues[];
	private String note_view = "summary";
	private String prescipt_view="current";
	private String tab;
	private String vlCountry="";
	private String rootCompURL="";
	private String hideActiveIssue="true"; 
	private CaseManagementCPP cpp=new CaseManagementCPP();
	public static final String[] tabs = {"Current Issues","Patient History","Allergies","Prescriptions","Reminders","Ticklers"};
	
	
	public CaseManagementCPP getCpp(){
		return this.cpp;
	}
	
	public void setCpp(CaseManagementCPP cpp){
		this.cpp=cpp;
	}
	
	public String getVlCountry() {
		return vlCountry;
	}
	public void setVlCountry(String vlCountry) {
		this.vlCountry = vlCountry;
	}
	public String getRootCompURL() {
		return rootCompURL;
	}
	public void setRootCompURL(String rootCompURL) {
		this.rootCompURL = rootCompURL;
	}
	public String getDemographicNo() {
		return demographicNo;
	}
	public void setDemographicNo(String demographicNo) {
		this.demographicNo = demographicNo;
	}
	public String[] getIssues() {
		return issues;
	}
	public void setIssues(String[] issues) {
		this.issues = issues;
	}
	public String getNote_view() {
		return note_view;
	}
	public void setNote_view(String note_view) {
		this.note_view = note_view;
	}
	public String getTab() {
		return tab;
	}
	public void setTab(String tab) {
		this.tab = tab;
	}

	public String getHideActiveIssue()
	{
		return hideActiveIssue;
	}

	public void setHideActiveIssue(String hideActiveIssue)
	{
		this.hideActiveIssue = hideActiveIssue;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public String getPrescipt_view() {
		return prescipt_view;
	}

	public void setPrescipt_view(String prescipt_view) {
		this.prescipt_view = prescipt_view;
	}
}
