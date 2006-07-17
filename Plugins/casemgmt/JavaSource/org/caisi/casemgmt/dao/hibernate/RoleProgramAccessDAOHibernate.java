package org.caisi.casemgmt.dao.hibernate;

import java.util.List;

import org.caisi.PMmodule.model.ProgramAccess;
import org.caisi.casemgmt.dao.RoleProgramAccessDAO;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class RoleProgramAccessDAOHibernate extends HibernateDaoSupport
		implements RoleProgramAccessDAO
{

	public List getRoleByProgram(String programId, String demoNo)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public List getRoleListByProviderProgramID(String providerNo, Long programId)
	{
		String q = "select pp.RoleId from ProgramProvider pp where pp.ProgramId=? and pp.ProviderNo=?";
		List pRoleList = (List) getHibernateTemplate().find(q, new Object[]
		{ programId, providerNo });

		return pRoleList;
	}
	public List getRoleNameByProviderProgramID(String providerNo, Long programId)
	{
		String q = "select pp.role.name from ProgramProvider pp where pp.ProgramId=? and pp.ProviderNo=?";
		List pRoleList = (List) getHibernateTemplate().find(q, new Object[]
		{ programId, providerNo });

		return pRoleList;
	}
	public List getRoleNameByProviderProgramID(String providerNo)
	{
		String q = "select pp.role.name from ProgramProvider pp where pp.ProviderNo=?";
		List pRoleList = (List) getHibernateTemplate().find(q, providerNo);

		return pRoleList;
	}
	public List getAllRoleName(){
		String q="select rl.name from Role rl";
		List pRoleList = (List) getHibernateTemplate().find(q);
		return pRoleList;
	}
	
	public List getProgramProviderByProviderProgramID(String providerNo, Long programId)
	{
		String q = "select pp from ProgramProvider pp where pp.ProgramId=? and pp.ProviderNo=?";
		List pRoleList = (List) getHibernateTemplate().find(q, new Object[]
		{programId, providerNo});

		return pRoleList;
	}
	public List getProgramProviderByProviderNo(String providerNo)
	{
		String q = "select pp from ProgramProvider pp where pp.ProviderNo=?";
		List pRoleList = (List) getHibernateTemplate().find(q, providerNo);
		return pRoleList;
	}

	public List getAccessListByProgramID(String accessName, String accessType,
			Long programId)
	{
		String q = "select pp from ProgramAccess pp,AccessType at where pp.ProgramId=? and pp.AccessTypeId=at.Id and at.Name=? and at.Type=?";
		List pAccessList = (List) getHibernateTemplate().find(q, new Object[]
		{ programId, accessName, accessType });
		for (int i = 0; i < pAccessList.size(); i++)
		{
			getHibernateTemplate().initialize(
					((ProgramAccess) pAccessList.get(i)).getRoles());
		}
		return pAccessList;
	}
	public List getAccessListByProgramID(Long programId)
	{
		String q = "select pp from ProgramAccess pp where pp.ProgramId=?";
		List pAccessList = (List) getHibernateTemplate().find(q, new Object[]
		{ programId});
		
		return pAccessList;
	}

	public List getDefaultAccessRightByRole(Long roleId)
	{
		String q = "from DefaultRoleAccess da where da.caisi_role.id=?";
		return (List) getHibernateTemplate().find(q,roleId);
	}
	public List getDefaultAccessRight()
	{
		String q = "from DefaultRoleAccess";
		return (List) getHibernateTemplate().find(q);
	}

}
