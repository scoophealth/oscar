package org.oscarehr.casemgmt.web;

import org.oscarehr.casemgmt.model.CaseManagementIssue;

public class CheckBoxBean
{
	private String checked = "off";
	private CaseManagementIssue issue = new CaseManagementIssue();
	private boolean isUsed = false;
	
	
	public CaseManagementIssue getIssue()
	{
		return issue;
	}
	public void setIssue(CaseManagementIssue issue)
	{
		this.issue = issue;
	}
	
	public boolean isUsed()
	{
		return isUsed;
	}
	
	public void setUsed(boolean used)
	{
		this.isUsed=used;
	}
	public String getChecked()
	{
		return checked;
	}
	public void setChecked(String checked)
	{
		this.checked = checked;
	}
	

	

}
