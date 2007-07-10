/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/

package org.oscarehr.casemgmt.service;

import java.util.List;

import org.oscarehr.casemgmt.model.CaseManagementCPP;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementSearchBean;
import org.oscarehr.casemgmt.model.Issue;

import org.oscarehr.PMmodule.model.Provider;

public interface CaseManagementManager {
	
	public String saveNote(CaseManagementCPP cpp, CaseManagementNote note,String cproviderNo, String userName,String lastStr,String roleName);
	public void saveNoteSimple(CaseManagementNote note);
	
	public List getNotes(String demographic_no);
	public List getFilteredNotes(String providerNo,String demographic_no);
	public List getNotes(String demographic_no,String[] issues);
	public CaseManagementNote getNote(String note_id);
	public List filterNotes(List notes, String providerNo, String programId);
	
        public void updateAppointment(String apptId, String status, String type);
	
	public List getIssues(String providerNo,String demographic_no,List accessRight);
	public List getActiveIssues(String providerNo,String demographic_no,List accessRight);
	public List getIssues(String providerNo,String demographic_no);
	public List getActiveIssues(String providerNo,String demographic_no);
	public List filterIssues(List issues, String providerNo, String programId);
	
	public Issue getIssue(String issue_id);
	public void deleteIssueById(CaseManagementIssue issue);
	public void saveAndUpdateCaseIssues(List issuelist);
	public void saveCaseIssue(CaseManagementIssue issue);
	public Issue getIssueInfo(Long l);
	public List getAllIssueInfo();
	public List getIssueInfoBySearch(String providerNo,String search,List accessRight);
	public void addNewIssueToConcern(String demoNo,String issueName);
	public boolean haveIssue(Long issid, String DemoNo);
	public List searchIssues(String providerNo, String programId, String search);
	public List searchIssuesNoRolesConcerned(String providerNo, String programId, String search);
	
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
	
	public boolean getEnabled();
	
	public void updateIssue(String demographicNo, Long originalIssueId, Long newIssueId);
}

