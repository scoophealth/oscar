package org.oscarehr.casemgmt.service;

import java.util.List;

import org.oscarehr.casemgmt.model.CaseManagementCPP;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementSearchBean;
import org.oscarehr.casemgmt.model.Issue;

public interface CaseManagementManager {
	
	public String saveNote(CaseManagementCPP cpp, CaseManagementNote note,String cproviderNo, String userName,String lastStr,String roleName);
	public void saveNoteSimple(CaseManagementNote note);
	
	public List getNotes(String demographic_no);
	public List getFilteredNotes(String providerNo,String demographic_no);
	public List getNotes(String demographic_no,String[] issues);
	public CaseManagementNote getNote(String note_id);
	public List filterNotes(List notes, String providerNo, String programId);
	
	
	public List getIssues(String providerNo,String demographic_no,List accessRight);
	public List getActiveIssues(String providerNo,String demographic_no,List accessRight);
	public List getIssues(String providerNo,String demographic_no);
	public List getActiveIssues(String providerNo,String demographic_no);
	public List filterIssues(List issues, String providerNo, String programId);
	
	public Issue getIssue(String issue_id);
	public void deleteIssueById(CaseManagementIssue issue);
	public void saveAndUpdateCaseIssues(List issuelist);
	public Issue getIssueInfo(Long l);
	public List getAllIssueInfo();
	public List getIssueInfoBySearch(String providerNo,String search,List accessRight);
	public void addNewIssueToConcern(String demoNo,String issueName);
	public boolean haveIssue(Long issid, String DemoNo);
	public List searchIssues(String providerNo, String programId, String search);
	
	public CaseManagementCPP getCPP(String demographic_no);
	public List getAllergies(String demographic_no);
	public List getPrescriptions(String demographic_no, boolean all);
	public void saveCPP(CaseManagementCPP cpp,String providerNo);
	public void updateCurrentIssueToCPP(String demoNo,List issueList);
	
	public List getEncounterFormBeans();
	public List getMsgBeans(Integer demographicNo);
	
	public String getProviderName(String providerNo);
	public String getDemoName(String demoNo);
	public String getDemoAge(String demoNo);
	public String getDemoDOB(String demoNo);
	
	
	public List getAccessRight(String providerNo, String demoNo, String programId);
	public boolean hasAccessRight(String accessName, String accessType,String providerNo,String demoNo,String programId);
	public boolean greaterEqualLevel(int level, String providerNo);
	public String getRoleName(String providerNo,String program_id);
	public String getCaisiRoleById(String id);

	
	public List search(CaseManagementSearchBean searchBean);
	public List filterNotesByAccess(List notes, String providerNo);
	
	public void tmpSave(String providerNo, String demographicNo, String programId, String note);
	public void deleteTmpSave(String providerNo, String demographicNo, String programId);
	public String  restoreTmpSave(String providerNo, String demographicNo, String programId);
		
	public boolean isClientInProgramDomain(String providerNo, String demographicNo);
	public boolean unlockNote(int noteId, String password);
}

