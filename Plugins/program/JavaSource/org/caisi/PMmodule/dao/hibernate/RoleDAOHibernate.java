package org.caisi.PMmodule.dao.hibernate;

import java.util.List;

import org.caisi.PMmodule.dao.RoleDAO;
import org.caisi.model.Role;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class RoleDAOHibernate extends HibernateDaoSupport implements RoleDAO {

	public List getRoles() {
		return this.getHibernateTemplate().find("from Role r");
	}
	
	public Role getRole(Long id) {
		return (Role)this.getHibernateTemplate().get(Role.class,id);
	}

}
