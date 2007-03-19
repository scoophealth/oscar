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

package org.oscarehr.casemgmt.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.struts.util.LabelValueBean;
import org.caisi.model.Role;
import org.oscarehr.PMmodule.model.AccessType;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.model.DefaultRoleAccess;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.ProgramAccess;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.model.Provider;
import org.oscarehr.casemgmt.model.CaseManagementCPP;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementSearchBean;
import org.oscarehr.casemgmt.model.CaseManagementTmpSave;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.casemgmt.model.Messagetbl;

public class CaseManagementManagerImpl extends BaseCaseManagementManager implements org.oscarehr.casemgmt.service.CaseManagementManager {



	public String saveNote(CaseManagementCPP cpp, CaseManagementNote note,
			String cproviderNo, String userName, String lastStr,String roleName)
	{

		SimpleDateFormat dt = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		String noteStr = note.getNote();
		String noteHistory = note.getHistory();
		Date now = new Date();
		// process noteStr, remove existing signed on string
		// noteStr = removeSignature(noteStr);
		if (note.isSigned())
		{
			// add the time, signiture and role at the end of note
			String rolename="";
			rolename= roleName;
			if (rolename == null)
				rolename = "";
			// if have signiture setting, use signiture as username
			String tempS = null;
			if (providerSignitureDao.isOnSig(cproviderNo))
				tempS = providerSignitureDao.getProviderSig(cproviderNo);
			if (tempS != null && !"".equals(tempS.trim()))
				userName = tempS;

			if (userName != null && !"".equals(userName.trim()))
			{
				noteStr = noteStr + "\n[[Signed on " + dt.format(now) + " "
						+ "by " + userName + ", " + rolename + "]]\n";
			} else
				noteStr = noteStr + "\n[[" + dt.format(now) + "]]\n";

		} else
		{

			// add time at the end of note
			noteStr = noteStr + "\n[[" + dt.format(now) + "]]\n";
		}

		/* formate the "/n" in noteStr */
		noteStr = noteStr.replaceAll("\r\n", "\n");
		noteStr = noteStr.replaceAll("\r", "\n");
		
		if (noteHistory==null)
			noteHistory=noteStr;
		else
		noteHistory = noteStr+"\n"+"   ----------------History Record----------------   \n"+noteHistory+"\n";
		
		note.setNote(noteStr);
		note.setHistory(noteHistory);
		
		
		caseManagementNoteDAO.saveNote(note);
		return echartDAO.saveEchart(note, cpp, userName, lastStr);

	}



	public List getNotes(String demographic_no)
	{
		return this.caseManagementNoteDAO.getNotesByDemographic(demographic_no);
	}

	public List getNotes(String demographic_no, String[] issues)
	{
		//List notesNoLocked = new ArrayList();
		List notes =  this.caseManagementNoteDAO.getNotesByDemographic(demographic_no,issues);
		/*
		for(Iterator iter=notes.iterator();iter.hasNext();) {
			CaseManagementNote note = (CaseManagementNote)iter.next();
			if(!note.isLocked()) {
				notesNoLocked.add(note);
			}
		}
		return notesNoLocked;
		*/
		return notes;
	}

	public List getIssues(String providerNo,String demographic_no) {
		return caseManagementIssueDAO.getIssuesByDemographicOrderActive(demographic_no);

	}
	
	public List getActiveIssues(String providerNo,String demographic_no) {
		return caseManagementIssueDAO.getActiveIssuesByDemographic(demographic_no);
	}
	
	public List getIssues(String providerNo, String demographic_no, List accessRight) {
		return filterIssueList(getIssues(providerNo,demographic_no), providerNo, accessRight);
	}

	public List getActiveIssues(String providerNo, String demographic_no, List accessRight) {		
		return filterIssueList(getActiveIssues(providerNo,demographic_no), providerNo, accessRight);
	}

	/* return true if have the right to access issues */
	public boolean inAccessRight(String right,String issueAccessType, List accessRight)
	{
		boolean rt = false;
		if (accessRight==null) return rt;
		Iterator itr = accessRight.iterator();
		while (itr.hasNext())
		{
			AccessType par = (AccessType) itr.next();
			if (right.equalsIgnoreCase(par.getName()) &&issueAccessType.equalsIgnoreCase(par.getType()))
				return true;
		}
		return rt;
	}

	/* filter the issues by caisi role */
	public List filterIssueList(List allIssue, String providerNo,
			List accessRight)
	{
		List role = roleProgramAccessDAO.getAllRoleName();
		List filteredIssue = new ArrayList();

		for (int i = 0; i < role.size(); i++)
		{
			Iterator itr = allIssue.iterator();
			String rl = (String) role.get(i);
			String right =rl.trim() + "issues";
			boolean inaccessRight = inAccessRight(right, issueAccessType, accessRight);
			if (inaccessRight)
			{

				String iRole = rl;
				while (itr.hasNext())
				{
					CaseManagementIssue iss = (CaseManagementIssue) itr.next();

					if (iss.getIssue().getRole().trim().equalsIgnoreCase(
							iRole.trim()))
					{
						filteredIssue.add(iss);

					}
				}
			}
		}
		return filteredIssue;
	}

	public Issue getIssue(String issue_id)
	{
		return this.issueDAO.getIssue(Long.valueOf(issue_id));
	}

	public CaseManagementNote getNote(String note_id)
	{
		return this.caseManagementNoteDAO.getNote(Long.valueOf(note_id));

	}

	public CaseManagementCPP getCPP(String demographic_no)
	{
		return this.caseManagementCPPDAO.getCPP(demographic_no);
	}

	public List getAllergies(String demographic_no)
	{
		return this.allergyDAO.getAllergies(demographic_no);
	}

	public List getPrescriptions(String demographic_no, boolean all)
	{
		if (all) { 
			return this.prescriptionDAO.getPrescriptions(demographic_no);
		}
		return this.prescriptionDAO.getUniquePrescriptions(demographic_no);
	}

	public List getEncounterFormBeans()
	{
		return encounterFormDAO.getAllForms();
	}

	public List getMsgBeans(Integer demographicNo)
	{
		Iterator iter = messagetblDAO.getMsgByDemoNo(demographicNo).iterator();
		ArrayList al = new ArrayList();
		int i = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.mm.dd");
		while (iter.hasNext())
		{
			Messagetbl mtbl = (Messagetbl) iter.next();
			al.add(new LabelValueBean(new Integer(i).toString(), mtbl
					.getThesubject()
					+ "-" + sdf.format(mtbl.getThedate())));
			i++;
		}
		return al;
	}

	public void deleteIssueById(CaseManagementIssue issue)
	{
		caseManagementIssueDAO.deleteIssueById(issue);
	}

	public void saveAndUpdateCaseIssues(List issuelist)
	{
		caseManagementIssueDAO.saveAndUpdateCaseIssues(issuelist);
	}

	public Issue getIssueInfo(Long l)
	{
		return issueDAO.getIssue(l);
	}

	public List getAllIssueInfo()
	{
		return issueDAO.getIssues();
	}

	public void saveCPP(CaseManagementCPP cpp, String providerNo)
	{
		caseManagementCPPDAO.saveCPP(cpp);
		echartDAO.saveCPPIntoEchart(cpp, providerNo);
	}

	public List getIssueInfoBySearch(String providerNo, String search,
			List accessRight)
	{
		List issList = issueDAO.findIssueBySearch(search);
		// filter the issue list by role
		List role = roleProgramAccessDAO.getAllRoleName();
		List filteredIssue = new ArrayList();
		

		for (int i = 0; i < role.size(); i++)
		{
			Iterator itr = issList.iterator();
			String rl = (String) role.get(i);
			String right =rl.trim() + "issues";
			boolean inaccessRight = inAccessRight(right, issueAccessType, accessRight);
			if (inaccessRight)
			{

				String iRole = rl;
				while (itr.hasNext())
				{
					Issue iss = (Issue) itr.next();

					if (iss.getRole().trim().equalsIgnoreCase(iRole.trim()))
					{
						filteredIssue.add(iss);

					}
				}
			}
		}
		return filteredIssue;

	}

	public void addNewIssueToConcern(String demoNo, String issueName)
	{
		CaseManagementCPP cpp = caseManagementCPPDAO.getCPP(demoNo);
		if (cpp == null)
		{
			cpp = new CaseManagementCPP();
			cpp.setDemographic_no(demoNo);
		}
		String ongoing = (cpp.getOngoingConcerns() == null) ? "" : cpp
				.getOngoingConcerns();
		ongoing = ongoing + issueName + "\n";
		cpp.setOngoingConcerns(ongoing);
		caseManagementCPPDAO.saveCPP(cpp);
		echartDAO.updateEchartOngoing(cpp);

	}

	public void updateCurrentIssueToCPP(String demoNo, List issueList)
	{
		CaseManagementCPP cpp = caseManagementCPPDAO.getCPP(demoNo);
		if (cpp == null)
		{
			cpp = new CaseManagementCPP();
			cpp.setDemographic_no(demoNo);
		}
		String ongoing = "";
		Iterator itr = issueList.iterator();
		while (itr.hasNext())
		{
			CaseManagementIssue iss = (CaseManagementIssue) itr.next();
			ongoing = ongoing + iss.getIssue().getDescription() + "\n";
		}
		cpp.setOngoingConcerns(ongoing);
		caseManagementCPPDAO.saveCPP(cpp);
		echartDAO.updateEchartOngoing(cpp);
	}

	/* get the filtered Notes by caisi role */
	public List getFilteredNotes(String providerNo, String demographic_no)
	{
		List allNotes = caseManagementNoteDAO
				.getNotesByDemographic(demographic_no);
		List role = roleProgramAccessDAO.getAllRoleName();
		List filteredNotes = new ArrayList();
		Iterator itr = allNotes.iterator();
		boolean added = false;
		while (itr.hasNext())
		{
			CaseManagementNote note = (CaseManagementNote) itr.next();
			added = false;
			Set se = note.getIssues();
			if (se == null || se.size() == 0)
			{
				Iterator isit = se.iterator();
				while (isit.hasNext())
				{
					CaseManagementIssue iss = (CaseManagementIssue) isit.next();
					for (int i = 0; i < role.size(); i++)
					{
						String rl = (String) role.get(i);
						if (iss.getIssue().getRole().trim().equalsIgnoreCase(
								rl.trim()))
						{
							filteredNotes.add(iss);
							added = true;
							break;
						}

					}
					if (added)
						break;
				}
			}
		}
		return filteredNotes;
	}

	public boolean haveIssue(Long issid, String demoNo)
	{
		List allNotes = caseManagementNoteDAO.getNotesByDemographic(demoNo);
		Iterator itr = allNotes.iterator();
		while (itr.hasNext())
		{
			CaseManagementNote note = (CaseManagementNote) itr.next();
			Set issues = note.getIssues();
			Iterator its = issues.iterator();
			while (its.hasNext())
			{
				CaseManagementIssue iss = (CaseManagementIssue) its.next();
				if (iss.getId().intValue() == issid.intValue())
					return true;
			}
		}
		return false;
	}

	public boolean greaterEqualLevel(int level, String providerNo)
	{
		if (level < 1 || level > 4)
			return false;
		List pcrList = roleProgramAccessDAO.getAllRoleName();
		if (pcrList.size() == 0)
			return false;
		Iterator itr = pcrList.iterator();
		while (itr.hasNext())
		{
			String pcr = (String) itr.next();
			String role = pcr;
			int secuL = 0, rtSecul = 0;
			if (role.equalsIgnoreCase("doctor"))
				secuL = 4;
			if (role.equalsIgnoreCase("nurse"))
				secuL = 3;
			if (role.equalsIgnoreCase("counsellor"))
				secuL = 2;
			if (role.equalsIgnoreCase("CSW"))
				secuL = 1;
			/* get provider's highest level */
			if (secuL > rtSecul)
				rtSecul = secuL;
			if (rtSecul >= level)
				return true;
		}
		return false;
	}

	public List getAccessRight(String providerNo, String demoNo, String programId) {
		List<Integer> progList = new ArrayList<Integer>();
		
		if (programId == null) {
			for (Object o : demographicDAO.getProgramIdByDemoNo(demoNo)) {
				progList.add((Integer) o);
			}
		} else {
			progList.add(Integer.valueOf(programId));
		}

		if (progList.isEmpty()) {
			return null;
		}

		List rt = new ArrayList();
		Iterator<Integer> itr = progList.iterator();

		while (itr.hasNext()) {
			Integer pId = itr.next();
			
			List ppList = roleProgramAccessDAO.getProgramProviderByProviderProgramID(providerNo, pId.longValue());
			List paList = roleProgramAccessDAO.getAccessListByProgramID(pId.longValue());

			for (int i = 0; i < ppList.size(); i++) {
				ProgramProvider pp = (ProgramProvider) ppList.get(i);
				// add default role access
				List arList = roleProgramAccessDAO.getDefaultAccessRightByRole(pp.getRoleId());
				for (int j = 0; j < arList.size(); j++) {
					DefaultRoleAccess ar = (DefaultRoleAccess) arList.get(j);
					addrt(rt, ar.getAccess_type());
				}
				for (int k = 0; k < paList.size(); k++) {
					ProgramAccess pa = (ProgramAccess) paList.get(k);
					if (pa.isAllRoles()) {
						addrt(rt, pa.getAccessType());
					} else if (roleInAccess(pp.getRoleId(), pa)) {
						addrt(rt, pa.getAccessType());
					}
				}
			}
		}
		
		return rt;
	}
	
	
	public boolean roleInAccess(Long roleId, ProgramAccess pa)
	{
		boolean rt=false;
		Set roleset=pa.getRoles();
		Iterator itr=roleset.iterator();
		while (itr.hasNext()){
			Role rl=(Role) itr.next();
			if (roleId.compareTo(rl.getId())==0) return true;
		}
		return rt;
	}

	public void addrt(List rt, AccessType at)
	{
		if (at==null) return;

		boolean hasIt=false;
		for (int i=0; i<rt.size();i++){
			AccessType ac=(AccessType) rt.get(i);
			if (ac.getId().compareTo(at.getId())==0) hasIt=true;
		}
		if (!hasIt) rt.add(at);
	}
	
	public boolean hasAccessRight(String accessName, String accessType,
			String providerNo, String demoNo,String pId)
	{
		if (accessName==null || accessType==null || pId ==null || pId.equals("")) return false;
		if (new Long(pId).intValue()==0) pId=null;
		List arList=getAccessRight(providerNo,demoNo,pId);
		for (int i=0;i<arList.size();i++){
			AccessType at=(AccessType)arList.get(i);
			if (accessName.equalsIgnoreCase(at.getName()) && accessType.equalsIgnoreCase(at.getType()))
				return true;
		}
		return false;
	}

	public String getRoleName(String providerNo,String program_id)
	{
		String rt="";
		List ppList=null;
		if (program_id==null || "".equalsIgnoreCase(program_id)||"null".equalsIgnoreCase(program_id))
			ppList=roleProgramAccessDAO.getProgramProviderByProviderNo(providerNo);
		else {
			Long pid=new Long(program_id);
			ppList=roleProgramAccessDAO.getProgramProviderByProviderProgramID(providerNo, pid);
		}
		if (ppList!=null && ppList.size()>0) rt=((ProgramProvider)ppList.get(0)).getRole().getName();
		return rt;
	}
	
	
	public String getProviderName(String providerNo) {
		Provider pv=providerDAO.getProvider(providerNo);
		if (pv!=null) return pv.getFirstName()+" "+pv.getLastName(); 
		return null;
	}
	
	public String getDemoName(String demoNo) {
		Demographic dg=demographicDAO.getClientByDemographicNo(new Integer(demoNo));
		if (dg==null) return "";
		else return dg.getFirstName()+" "+dg.getLastName();
	}

	public String getDemoAge(String demoNo) {
		String age = "";
		
		Demographic demo = demographicDAO.getClientByDemographicNo(new Integer(demoNo));
		if (demo != null) {
			age = demo.getAge();
		}
		
		return age;
	}
	
	public String getDemoDOB(String demoNo) {
		Demographic dg=demographicDAO.getClientByDemographicNo(new Integer(demoNo));
		if (dg==null) return "";
		else return dg.getYearOfBirth()+"-"+dg.getMonthOfBirth()+"-"+dg.getDateOfBirth();
	}
		
	



	public String getCaisiRoleById(String id)
	{
		//return providerCaisiRoleDAO.getCaisiRoleById(id);
		return roleManager.getRole(id).getName();
	}

	public List search(CaseManagementSearchBean searchBean) {
		return this.caseManagementNoteDAO.search(searchBean);
	}
	
	public List filterNotesByAccess(List notes, String providerNo) {
		List filteredNotes = new ArrayList();
		for(Iterator iter=notes.iterator();iter.hasNext();) {
			CaseManagementNote note = (CaseManagementNote)iter.next();
			if(hasAccessRight(removeFirstSpace(getCaisiRoleById(note.getReporter_caisi_role()))+"notes","access",providerNo,note.getDemographic_no(),note.getProgram_no())){				
				filteredNotes.add(note);					
			}
		}
		return filteredNotes;
	}
	
	public void tmpSave(String providerNo, String demographicNo, String programId, String note) {
		CaseManagementTmpSave tmp = new CaseManagementTmpSave();
		tmp.setProviderNo(providerNo);
		tmp.setDemographicNo(new Long(demographicNo).longValue());
		tmp.setProgramId(new Long(programId).longValue());
		tmp.setNote(note);
		tmp.setUpdate_date(new Date());
		caseManagementTmpSaveDAO.save(tmp);
	}

	public void deleteTmpSave(String providerNo, String demographicNo, String programId) {
		caseManagementTmpSaveDAO.delete(providerNo, new Long(demographicNo), new Long(programId));
	}
	
	public String  restoreTmpSave(String providerNo, String demographicNo, String programId) {
		CaseManagementTmpSave obj = caseManagementTmpSaveDAO.load(providerNo, new Long(demographicNo),new Long(programId));
		if(obj != null) {
			return obj.getNote();
		}
		return null;
	}
	
	/**
	 * 
	 * 
	 * @param issues Unfiltered Set of issues
	 * @param providerNo provider reading issues
	 * @param programId program provider is logged into
	 * 	
	 */
	public List filterNotes(List notes, String providerNo, String programId){
		List filteredNotes = new ArrayList();
		
		if(notes.isEmpty()) { return notes;}
		
		//Get Role - if no ProgramProvider record found, show no issues.
		List ppList = this.roleProgramAccessDAO.getProgramProviderByProviderProgramID(providerNo,new Long(programId));
		if(ppList == null || ppList.isEmpty()) {
			return new ArrayList();
		}
		
		ProgramProvider pp = (ProgramProvider)ppList.get(0);
		Role role = pp.getRole();
		
		//Load up access list from program
		List programAccessList = roleProgramAccessDAO.getAccessListByProgramID(new Long(programId));
		Map programAccessMap = convertProgramAccessListToMap(programAccessList);
		
		//iterate through the issue list
		for(Iterator iter=notes.iterator();iter.hasNext();) {
			CaseManagementNote cmNote = (CaseManagementNote)iter.next();
			String noteRole = cmNote.getReporter_caisi_role();
			String noteRoleName = roleManager.getRole(noteRole).getName();
			ProgramAccess pa = null;
			boolean add = false;
			
			//write
			/*
			pa = (ProgramAccess)programAccessMap.get("write " + noteRoleName + " notes");
			if(pa != null) {
				if(pa.isAllRoles() || isRoleIncludedInAccess(pa,role)) {
					cmIssue.setWriteAccess(true);
					add = true;
				}
			} else {
				if(issueRole.equals(role.getName())) {
					//default
					cmIssue.setWriteAccess(true);
					add=true;
				}
			}
			*/
			pa = null;
			//read
			pa = (ProgramAccess)programAccessMap.get("read " + noteRoleName + " notes");
			if(pa != null) {
				if(pa.isAllRoles() || isRoleIncludedInAccess(pa,role)) {
					//filteredIssues.add(cmIssue);
					add = true;
				}
			} else {
				if(new Long(noteRole).intValue() == role.getId().intValue()) {
					//default
					add=true;
				}
			}
		
			//apply defaults
			if(!add) {
				if(new Long(noteRole).intValue() == role.getId().intValue()) {
					//cmNote.setWriteAccess(true);
					add=true;
				}
			}
			
			//did it pass the test?
			if(add) {
				filteredNotes.add(cmNote);
			}
		}
		return filteredNotes;
	}

	private boolean isRoleIncludedInAccess(ProgramAccess pa, Role role) {
		boolean result = false;
		
		for(Iterator iter=pa.getRoles().iterator();iter.hasNext();) {
			Role accessRole = (Role)iter.next();
			if(role.getId() == accessRole.getId()) {
				return true;
			}
		}
		return result;
	}
	
	private Map convertProgramAccessListToMap(List paList) {
		Map map = new HashMap();
		
		for(Iterator iter=paList.iterator();iter.hasNext();) {
			ProgramAccess pa = (ProgramAccess)iter.next();
			map.put(pa.getAccessType().getName(),pa);
		}
		return map;
	}
	
	public List searchIssues(String providerNo, String programId, String search) {
		//get Role
		//Get Role - if no ProgramProvider record found, show no issues.
		List ppList = this.roleProgramAccessDAO.getProgramProviderByProviderProgramID(providerNo,new Long(programId));
		if(ppList == null || ppList.isEmpty()) {
			return new ArrayList();
		}
		ProgramProvider pp = (ProgramProvider)ppList.get(0);
		Role role = pp.getRole();
		
		//get program accesses
		List paList = roleProgramAccessDAO.getAccessListByProgramID(new Long(programId));		
		Map paMap = convertProgramAccessListToMap(paList);
		
		//get all roles
		List allRoles = this.roleManager.getRoles();
		
		List allowableSearchRoles = new ArrayList();
		for(Iterator iter = allRoles.iterator();iter.hasNext();) {
			Role r = (Role)iter.next();
			String key = "write " + r.getName() + " issues";
			ProgramAccess pa = (ProgramAccess)paMap.get(key);
			if(pa != null) {
				if(pa.isAllRoles() || isRoleIncludedInAccess(pa,role)) {
					allowableSearchRoles.add(r);
				} 
			}
			if(pa == null && r.getId().intValue() == role.getId().intValue()) {
				allowableSearchRoles.add(r);
			}
		}
		
		List issList = issueDAO.search(search,allowableSearchRoles);
		
		return issList;
	}
	
	public List searchIssuesNoRolesConcerned(String providerNo, String programId, String search) {
	
		List issList = issueDAO.searchNoRolesConcerned(search);
		
		return issList;
	}
	
	public List filterIssues(List issues, String providerNo, String programId){
		List filteredIssues = new ArrayList();
		
		if(issues.isEmpty()) { return issues;}
		
		//Get Role - if no ProgramProvider record found, show no issues.
		List ppList = this.roleProgramAccessDAO.getProgramProviderByProviderProgramID(providerNo,new Long(programId));
		if(ppList == null || ppList.isEmpty()) {
			return new ArrayList();
		}
		
		ProgramProvider pp = (ProgramProvider)ppList.get(0);
		Role role = pp.getRole();
		
		//Load up access list from program
		List programAccessList = roleProgramAccessDAO.getAccessListByProgramID(new Long(programId));
		Map programAccessMap = convertProgramAccessListToMap(programAccessList);
		
		//iterate through the issue list
		for(Iterator iter=issues.iterator();iter.hasNext();) {
			CaseManagementIssue cmIssue = (CaseManagementIssue)iter.next();
			String issueRole = cmIssue.getIssue().getRole();
			ProgramAccess pa = null;
			boolean add = false;
			
			//write
			pa = (ProgramAccess)programAccessMap.get("write " + issueRole + " issues");
			if(pa != null) {
				if(pa.isAllRoles() || isRoleIncludedInAccess(pa,role)) {
					cmIssue.setWriteAccess(true);
					add = true;
				}
			} else {
				if(issueRole.equals(role.getName())) {
					//default
					cmIssue.setWriteAccess(true);
					add=true;
				}
			}
			pa = null;
			//read
			pa = (ProgramAccess)programAccessMap.get("read " + issueRole + " issues");
			if(pa != null) {
				if(pa.isAllRoles() || isRoleIncludedInAccess(pa,role)) {
					//filteredIssues.add(cmIssue);
					add = true;
				}
			} else {
				if(issueRole.equals(role.getName())) {
					//default
					add=true;
				}
			}
		
			//apply defaults
			if(!add) {
				if(issueRole.equals(role.getName())) {
					cmIssue.setWriteAccess(true);
					add=true;
				}
			}
			
			//did it pass the test?
			if(add) {
				filteredIssues.add(cmIssue);
			}
		}
		return filteredIssues;
	}
	
	public void saveNoteSimple(CaseManagementNote note) {
		this.caseManagementNoteDAO.saveNote(note);
	}
	
	public boolean isClientInProgramDomain(String providerNo, String demographicNo) {
		
		List providerPrograms = roleProgramAccessDAO.getProgramProviderByProviderNo(providerNo);

		List allAdmissions = this.admissionManager.getAdmissions(Integer.valueOf(demographicNo));
		
		for(int x=0;x<providerPrograms.size();x++) {
			ProgramProvider pp = (ProgramProvider)providerPrograms.get(x);
			long programId = pp.getProgramId().longValue();
			
			for(int y=0;y<allAdmissions.size();y++) {
				Admission admission = (Admission)allAdmissions.get(y);
				long admissionProgramId = admission.getProgramId().longValue();
				
				if(programId == admissionProgramId) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public boolean unlockNote(int noteId, String password) {
		CaseManagementNote note = this.caseManagementNoteDAO.getNote(new Long(noteId));
		if(note != null)  {
			if(note.isLocked() && note.getPassword().equals(password)) {
				return true;
			}
		}
		return false;
	}
}
