package org.oscarehr.casemgmt.dao.hibernate;

import java.util.List;

import org.oscarehr.casemgmt.dao.RoleProgramAccessDAO;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class RoleProgramAccessDAOHibernate extends HibernateDaoSupport
		implements RoleProgramAccessDAO
{


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
}
