package org.caisi.casemgmt.service;

import java.util.List;

import org.caisi.casemgmt.dao.CaseManagementCPPDAO;
import org.caisi.casemgmt.model.CaseManagementCPP;
import org.caisi.casemgmt.model.CaseManagementIssue;
import org.caisi.casemgmt.model.CaseManagementNote;
import org.caisi.casemgmt.model.Issue;

public interface CaseManagementManager {
	public String saveNote(CaseManagementCPP cpp, CaseManagementNote note,String cproviderNo, String userName,String lastStr,String roleName);
	public List getNotes(String demographic_no);
	public List getFilteredNotes(String providerNo,String demographic_no);
	public List getNotes(String demographic_no,String[] issues);
	public List getIssues(String providerNo,String demographic_no,List accessRight);
	public List getActiveIssues(String providerNo,String demographic_no,List accessRight);
	public Issue getIssue(String issue_id);
	public CaseManagementNote getNote(String note_id);
	public CaseManagementCPP getCPP(String demographic_no);
	public List getAllergies(String demographic_no);
	public List getPrescriptions(String demographic_no, boolean all);
	
	public List getEncounterFormBeans();
	public List getMsgBeans(Integer demographicNo);
	public void deleteIssueById(CaseManagementIssue issue);
	
	public void saveAndUpdateCaseIssues(List issuelist);
	public Issue getIssueInfo(Long l);
	public List getAllIssueInfo();
	public void saveCPP(CaseManagementCPP cpp,String providerNo);
	public List getIssueInfoBySearch(String providerNo,String search,List accessRight);
	public void addNewIssueToConcern(String demoNo,String issueName);
	public void updateCurrentIssueToCPP(String demoNo,List issueList);
	public boolean haveIssue(Long issid, String DemoNo);
	public boolean greaterEqualLevel(int level, String providerNo);
	public String getRoleName(String providerNo,String program_id);
	
	public String getProviderName(String providerNo);
	public String getDemoName(String demoNo);
	public String getDemoAge(String demoNo);
	public String getDemoDOB(String demoNo);
	
	
	public List getAccessRight(String providerNo, String demoNo, String programId);
	public boolean hasAccessRight(String accessName, String accessType,String providerNo,String demoNo,String programId);
}
