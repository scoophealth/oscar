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
