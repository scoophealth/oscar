package org.oscarehr.PMmodule.web;

import java.util.List;

import org.caisi.dao.IssueGroupDao;
import org.caisi.model.IssueGroup;
import org.caisi.model.Role;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.RoleDAO;
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
		
	public List<Role> getRoles()
	{
		RoleDAO roleDAO = (RoleDAO) applicationContext.getBean("roleDAO");
		return(roleDAO.getRoles());
	}
}
