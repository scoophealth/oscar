package org.oscarehr.PMmodule.web;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.caisi.dao.IssueGroupDao;
import org.caisi.model.IssueGroup;
import org.caisi.model.Role;
import org.caisi.util.EncounterUtil;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.RoleDAO;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.web.PopulationReportDataObjects.EncounterTypeDataGrid;
import org.oscarehr.PMmodule.web.PopulationReportDataObjects.EncounterTypeDataRow;
import org.oscarehr.PMmodule.web.PopulationReportDataObjects.RoleDataGrid;
import org.springframework.context.ApplicationContext;

public class PopulationReportUIBean {

	private ProgramDao programDao = null;
	private RoleDAO roleDAO = null;
	private IssueGroupDao issueGroupDao = null;		
	private int programId=-1;
	
	public PopulationReportUIBean(ApplicationContext applicationContext, int programId)
	{
		this.programId=programId;

		programDao = (ProgramDao) applicationContext.getBean("programDao");
		roleDAO = (RoleDAO) applicationContext.getBean("roleDAO");
		issueGroupDao = (IssueGroupDao) applicationContext.getBean("issueGroupDao");		
	}
	
	private Set<IssueGroup> allIssueGroups=null;
	public Set<IssueGroup> getIssueGroups()
	{
		if (allIssueGroups==null) allIssueGroups=new TreeSet<IssueGroup>(issueGroupDao.findAll());
		return(allIssueGroups);
	}
	
	private Program program=null;
	public Program getProgram()
	{
		if (program==null) program=programDao.getProgram(programId);
		return(program);	
	}
		
	private List<Role> allRoles=null;
	public List<Role> getRoles()
	{
		if (allRoles==null) allRoles=roleDAO.getRoles();
		return(allRoles);
	}
	
	public RoleDataGrid getRoleDataGrid()
	{
		RoleDataGrid roleDataGrid=new RoleDataGrid();
		
		for (Role role : getRoles())
		{
			roleDataGrid.put(role, getEncounterTypeDataGrid(role));
		}
		
		return(roleDataGrid);
	}
	
	private EncounterTypeDataGrid getEncounterTypeDataGrid(Role role)
	{
		EncounterTypeDataGrid result=new EncounterTypeDataGrid();
		
		for (EncounterUtil.EncounterType encounterType : EncounterUtil.EncounterType.values())
		{
			result.put(encounterType, getEncounterTypeDataRow(role, encounterType));
		}

		return(result);
	}
	
	private EncounterTypeDataRow getEncounterTypeDataRow(Role role, EncounterUtil.EncounterType encounterType)
	{
		EncounterTypeDataRow result=new EncounterTypeDataRow();
		
		for (IssueGroup issueGroup : getIssueGroups())
		{
			result.put(issueGroup, getEncounterCountByRoleEncounterTypeIssueGroup(role, encounterType, issueGroup));
		}
		
		return(result);
	}
	
	private int getEncounterCountByRoleEncounterTypeIssueGroup(Role role, EncounterUtil.EncounterType encounterType, IssueGroup issueGroup)
	{
		return(-1);
	}
}
