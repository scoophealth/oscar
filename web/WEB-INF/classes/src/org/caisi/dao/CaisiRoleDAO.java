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

package org.caisi.dao;

import java.util.List;

import org.caisi.model.CaisiRole;
import org.caisi.model.Role;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class CaisiRoleDAO extends HibernateDaoSupport {

	public CaisiRole getRole(Long id) {
		return (CaisiRole)this.getHibernateTemplate().get(CaisiRole.class,id);
	}

	public CaisiRole getRoleByProviderNo(String provider_no) {
		return (CaisiRole)this.getHibernateTemplate().find("from CaisiRole cr where cr.provider_no = ?",new Object[] {provider_no}).get(0);
	}

	@SuppressWarnings("unchecked")
    public List<CaisiRole> getRolesByRole(String role) {
		return this.getHibernateTemplate().find("from CaisiRole cr where cr.role = ?",new Object[] {role});
	}

	public void saveRoleAssignment(CaisiRole role) {
		this.getHibernateTemplate().saveOrUpdate(role);
	}

	public void saveRole(Role role) {
		this.getHibernateTemplate().saveOrUpdate(role);
/*
		SQLQuery query = getSession().createSQLQuery("insert into acces_type (name,type) values (\'write " + role.getName() + " issues\',\'access\'");
		query.executeUpdate();
		
		query = getSession().createSQLQuery("insert into acces_type (name,type) values (\'read " + role.getName() + " issues\',\'access\'");
		query.executeUpdate();
		
		query = getSession().createSQLQuery("insert into acces_type (name,type) values (\'read " + role.getName() + " notes\',\'access\'");
		query.executeUpdate();
*/
	}
	
	@SuppressWarnings("unchecked")
    public List<CaisiRole> getRoles() {
		return this.getHibernateTemplate().find("from Role");
	}
	public boolean hasExist(String roleName) {
		String q="from Role r where lower(r.name)=lower(?)";
		List rs=getHibernateTemplate().find(q,roleName);
		if (rs.size()>0) return true;
		else return false;
	}
}
