package org.caisi.casemgmt.web;

import org.caisi.casemgmt.model.Issue;


public class CheckIssueBoxBean
{
	private boolean isChecked = false;
	private Issue issue = new Issue();
	
	public boolean isChecked()
	{
		return isChecked;
	}
	public void setChecked(boolean isChecked)
	{
		this.isChecked = isChecked;
	}
	public Issue getIssue()
	{
		return issue;
	}
	public void setIssue(Issue issue)
	{
		this.issue = issue;
	}

	

}
