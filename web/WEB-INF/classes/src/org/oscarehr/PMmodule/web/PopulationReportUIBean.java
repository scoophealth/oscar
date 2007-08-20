package org.oscarehr.PMmodule.web;

import java.util.List;

import org.caisi.dao.CaisiRoleDAO;
import org.caisi.dao.IssueGroupDao;
import org.caisi.model.CaisiRole;
import org.caisi.model.IssueGroup;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.model.Program;
import org.springframework.context.ApplicationContext;

public class PopulationReportUIBean {

	private ApplicationContext applicationContext=null;
	private int programId=-1;
	
	public PopulationReportUIBean(ApplicationContext applicationContext, int programId)
	{
		this.applicationContext=applicationContext;
		this.programId=programId;
	}
	
	private List<IssueGroup> allIssueGroups=null;
	public List<IssueGroup> getIssueGroups()
	{
		if (allIssueGroups==null)
		{
			IssueGroupDao issueGroupDao = (IssueGroupDao) applicationContext.getBean("issueGroupDao");		
			allIssueGroups=issueGroupDao.findAll();
		}
		
		return(allIssueGroups);
	}
	
	public Program getProgram()
	{
		ProgramDao programDao = (ProgramDao) applicationContext.getBean("programDao");
		return(programDao.getProgram(programId));	
	}
		
	public List<CaisiRole> getCaisiRoles()
	{
		CaisiRoleDAO caisiRoleDAO = (CaisiRoleDAO) applicationContext.getBean("CaisiRoleDAO");
		return(caisiRoleDAO.getRoles());
	}
}
