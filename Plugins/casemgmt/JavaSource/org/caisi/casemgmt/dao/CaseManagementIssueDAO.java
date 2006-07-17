package org.caisi.casemgmt.dao;

import java.util.List;

import org.caisi.casemgmt.model.CaseManagementIssue;

public interface CaseManagementIssueDAO extends DAO {
	public List getIssuesByDemographic(String demographic_no);
	public List getIssuesByDemographicOrderActive(String demographic_no);
	public List getActiveIssuesByDemographic(String demographic_no);
	public void deleteIssueById(CaseManagementIssue issue);
	public void saveAndUpdateCaseIssues(List issuelist);
}
