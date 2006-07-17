package org.caisi.casemgmt.dao;

import java.util.List;

import org.caisi.casemgmt.model.Issue;

public interface IssueDAO extends DAO {
	public Issue getIssue(Long id);
	public List getIssues();
	public Issue findIssueByCode(String code);
	
	public void saveIssue(Issue issue);
	public List findIssueBySearch(String search);
}
