package org.caisi.dao.hibernate;

import java.util.List;

import org.caisi.dao.CaisiRoleDAO;
import org.caisi.model.CaisiRole;
import org.caisi.model.Role;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class CaisiRoleDAOHibernate extends HibernateDaoSupport implements
		CaisiRoleDAO {

	public CaisiRole getRole(Long id) {
		return (CaisiRole)this.getHibernateTemplate().get(CaisiRole.class,id);
	}

	public CaisiRole getRoleByProviderNo(String provider_no) {
		return (CaisiRole)this.getHibernateTemplate().find("from CaisiRole cr where cr.provider_no = ?",new Object[] {provider_no}).get(0);
	}

	public List getRolesByRole(String role) {
		return this.getHibernateTemplate().find("from CaisiRole cr where cr.role = ?",new Object[] {role});
	}

	public void saveRoleAssignment(CaisiRole role) {
		this.getHibernateTemplate().saveOrUpdate(role);
	}

	public void saveRole(Role role) {
		this.getHibernateTemplate().saveOrUpdate(role);
	}
	
	
	public List getRoles() {
		return this.getHibernateTemplate().find("from Role");
	}
	public boolean hasExist(String roleName) {
		String q="from Role r where lower(r.name)=lower(?)";
		List rs=getHibernateTemplate().find(q,roleName);
		if (rs.size()>0) return true;
		else return false;
	}
}
