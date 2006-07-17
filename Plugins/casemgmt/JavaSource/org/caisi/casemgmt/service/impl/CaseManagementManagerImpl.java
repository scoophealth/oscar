package org.caisi.casemgmt.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.struts.util.LabelValueBean;
import org.caisi.PMmodule.dao.ProgramAccessDAO;
import org.caisi.PMmodule.model.AccessType;
import org.caisi.PMmodule.model.ProgramAccess;
import org.caisi.PMmodule.model.ProgramProvider;
import org.caisi.casemgmt.dao.AllergyDAO;
import org.caisi.casemgmt.dao.CaseManagementCPPDAO;
import org.caisi.casemgmt.dao.CaseManagementIssueDAO;
import org.caisi.casemgmt.dao.CaseManagementNoteDAO;
import org.caisi.casemgmt.dao.EchartDAO;
import org.caisi.casemgmt.dao.EncounterFormDAO;
import org.caisi.casemgmt.dao.IssueDAO;
import org.caisi.casemgmt.dao.MessagetblDAO;
import org.caisi.casemgmt.dao.PrescriptionDAO;
import org.caisi.casemgmt.dao.ProviderCaisiRoleDAO;
import org.caisi.casemgmt.dao.ProviderSignitureDao;
import org.caisi.casemgmt.dao.RoleProgramAccessDAO;
import org.caisi.model.CaisiRole;
import org.caisi.model.Demographic;
import org.caisi.model.Program;
import org.caisi.model.Provider;
import org.caisi.model.ProviderRoleProgram;
import org.caisi.model.Role;
import org.caisi.casemgmt.model.CaseManagementCPP;
import org.caisi.casemgmt.model.CaseManagementIssue;
import org.caisi.casemgmt.model.CaseManagementNote;
import org.caisi.casemgmt.model.DefaultRoleAccess;
import org.caisi.casemgmt.model.Issue;
import org.caisi.casemgmt.model.Messagetbl;
import org.caisi.dao.DemographicDAO;
import org.caisi.dao.ProviderDAO;
import org.caisi.casemgmt.web.ProviderAccessRight;
import org.caisi.dao.ProviderRoleProgramDao;

public class CaseManagementManagerImpl implements
		org.caisi.casemgmt.service.CaseManagementManager
{

	protected CaseManagementNoteDAO caseManagementNoteDAO;

	protected CaseManagementIssueDAO caseManagementIssueDAO;

	protected IssueDAO issueDAO;

	protected CaseManagementCPPDAO caseManagementCPPDAO;

	protected AllergyDAO allergyDAO;

	protected PrescriptionDAO prescriptionDAO;

	protected EncounterFormDAO encounterFormDAO;

	protected MessagetblDAO messagetblDAO;

	protected EchartDAO echartDAO;
	
	protected ProviderDAO providerDAO;
	protected DemographicDAO demographicDAO;
	
	//protected ProviderCaisiRoleDAO providerCaisiRoleDAO;

	protected ProviderSignitureDao providerSignitureDao;

	protected RoleProgramAccessDAO roleProgramAccessDAO;

	protected ProviderRoleProgramDao providerRoleProgramDao;

	private String issueAccessType="access";
	
	public void setEchartDAO(EchartDAO echartDAO)
	{
		this.echartDAO = echartDAO;
	}

	/*public void setProviderCaisiRoleDAO(
			ProviderCaisiRoleDAO providerCaisiRoleDAO)
	{
		this.providerCaisiRoleDAO = providerCaisiRoleDAO;
	}*/

	public void setEncounterFormDAO(EncounterFormDAO dao)
	{
		this.encounterFormDAO = dao;
	}

	public void setMessagetblDAO(MessagetblDAO dao)
	{
		this.messagetblDAO = dao;
	}

	public void setCaseManagementNoteDAO(CaseManagementNoteDAO dao)
	{
		this.caseManagementNoteDAO = dao;
	}

	public void setCaseManagementIssueDAO(CaseManagementIssueDAO dao)
	{
		this.caseManagementIssueDAO = dao;
	}

	public void setIssueDAO(IssueDAO dao)
	{
		this.issueDAO = dao;
	}

	public void setCaseManagementCPPDAO(CaseManagementCPPDAO dao)
	{
		this.caseManagementCPPDAO = dao;
	}

	public void setAllergyDAO(AllergyDAO dao)
	{
		this.allergyDAO = dao;
	}

	public void setPrescriptionDAO(PrescriptionDAO dao)
	{
		this.prescriptionDAO = dao;
	}

	public String saveNote(CaseManagementCPP cpp, CaseManagementNote note,
			String cproviderNo, String userName, String lastStr,String roleName)
	{

		SimpleDateFormat dt = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		String noteStr = note.getNote();
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
		note.setNote(noteStr);
		caseManagementNoteDAO.saveNote(note);
		return echartDAO.saveEchart(note, cpp, userName, lastStr);

	}



	public List getNotes(String demographic_no)
	{
		return this.caseManagementNoteDAO.getNotesByDemographic(demographic_no);
	}

	public List getNotes(String demographic_no, String[] issues)
	{
		return this.caseManagementNoteDAO.getNotesByDemographic(demographic_no,
				issues);
	}

	public List getIssues(String providerNo, String demographic_no,
			List accessRight)
	{
		List allIssue = caseManagementIssueDAO
				.getIssuesByDemographicOrderActive(demographic_no);

		return filterIssueList(allIssue, providerNo, accessRight);
	}

	public List getActiveIssues(String providerNo, String demographic_no,
			List accessRight)
	{
		List ll = caseManagementIssueDAO
				.getActiveIssuesByDemographic(demographic_no);
		/*
		 * for (int i=0; i<ll.size();i++){ Set s =
		 * ((CaseManagementIssue)ll.get(i)).getNotes();
		 * System.out.println("---------"+s.size()); }
		 */
		return filterIssueList(ll, providerNo, accessRight);
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
		if (all) return this.prescriptionDAO.getPrescriptions(demographic_no);
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
		// TODO Auto-generated method stub

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

	public ProviderSignitureDao getProviderSignitureDao()
	{
		return providerSignitureDao;
	}

	public void setProviderSignitureDao(
			ProviderSignitureDao providerSignitureDao)
	{
		this.providerSignitureDao = providerSignitureDao;
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

	public List getAccessRight(String providerNo, String demoNo,
			String programId)
	{
		List progList=null;
		if (programId==null)
		{
			progList = demographicDAO.getProgramIdByDemoNo(demoNo);
		}else{
			progList= new ArrayList();
			progList.add(new Long(programId));
		}
		 if (progList==null)return null;
		 List rt=new ArrayList();
		 Iterator itr= progList.iterator();
		 while (itr.hasNext()){
			 Long pId=(Long)itr.next();
			 List ppList=roleProgramAccessDAO.getProgramProviderByProviderProgramID(providerNo, pId);
			 List paList=roleProgramAccessDAO.getAccessListByProgramID(pId);
			 for(int i=0; i<ppList.size();i++){
				 
				 ProgramProvider pp=(ProgramProvider)ppList.get(i);
				 //add default role access
				 List arList=roleProgramAccessDAO.getDefaultAccessRightByRole(pp.getRoleId());
				 for (int j=0;j<arList.size();j++){
					 DefaultRoleAccess ar=(DefaultRoleAccess)arList.get(j);
					 addrt(rt,ar.getAccess_type());
				 }
				 for (int k=0;k<paList.size();k++){
					 ProgramAccess pa=(ProgramAccess)paList.get(k);
					 if (pa.isAllRoles()) addrt(rt,pa.getAccessType());
					 else if (roleInAccess(pp.getRoleId(),pa)) addrt(rt,pa.getAccessType());
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
		if (accessName==null || accessType==null) return false;
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
	
	public void setRoleProgramAccessDAO(
			RoleProgramAccessDAO roleProgramAccessDAO)
	{
		this.roleProgramAccessDAO = roleProgramAccessDAO;
	}
	
	public String getProviderName(String providerNo) {
		Provider pv=providerDAO.getProvider(providerNo);
		if (pv!=null) return pv.getFirstName()+" "+pv.getLastName(); 
		return null;
	}
	
	public String getDemoName(String demoNo) {
		Demographic dg=demographicDAO.getDemographic(demoNo);
		if (dg==null) return "";
		else return dg.getFirstName()+" "+dg.getLastName();
	}

	public String getDemoAge(String demoNo) {
		
		Calendar rightNow = Calendar.getInstance();
		int CurrentYear = rightNow.get(Calendar.YEAR);
		
		
		Demographic dg=demographicDAO.getDemographic(demoNo);
		int age=CurrentYear-Integer.parseInt(dg.getYearOfBirth());
		if (dg==null) return "";
		else if(age<127)
			return Integer.toString(age);
		else return (age+" What?I was born 100 years ago!Please check my birthday.");
	}
	
	public String getDemoDOB(String demoNo) {
		Demographic dg=demographicDAO.getDemographic(demoNo);
		if (dg==null) return "";
		else return dg.getYearOfBirth()+"-"+dg.getMonthOfBirth()+"-"+dg.getDateOfBirth();
	}
		
	
	public void setDemographicDAO(DemographicDAO demographicDAO)
	{
		this.demographicDAO = demographicDAO;
	}

	public void setProviderRoleProgramDao(
			ProviderRoleProgramDao providerRoleProgramDao)
	{
		this.providerRoleProgramDao = providerRoleProgramDao;
	}

	public ProviderDAO getProviderDAO()
	{
		return providerDAO;
	}

	public void setProviderDAO(ProviderDAO providerDAO)
	{
		this.providerDAO = providerDAO;
	}

	public DemographicDAO getDemographicDAO()
	{
		return demographicDAO;
	}
}
