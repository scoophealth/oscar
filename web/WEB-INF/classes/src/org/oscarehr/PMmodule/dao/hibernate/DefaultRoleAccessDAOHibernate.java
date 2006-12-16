package org.oscarehr.PMmodule.dao.hibernate;

import java.util.List;

import org.oscarehr.PMmodule.dao.DefaultRoleAccessDAO;
import org.oscarehr.PMmodule.model.DefaultRoleAccess;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class DefaultRoleAccessDAOHibernate extends HibernateDaoSupport
		implements DefaultRoleAccessDAO {

	public void deleteDefaultRoleAccess(Long id) {
		this.getHibernateTemplate().delete(getDefaultRoleAccess(id));
	}

	public DefaultRoleAccess getDefaultRoleAccess(Long id) {
		return (DefaultRoleAccess)this.getHibernateTemplate().get(DefaultRoleAccess.class, id);
	}

	public List getDefaultRoleAccesses() {
		return this.getHibernateTemplate().find("from DefaultRoleAccess dra ORDER BY dra.id");
	}

	public void saveDefaultRoleAccess(DefaultRoleAccess dra) {
		this.getHibernateTemplate().saveOrUpdate(dra);
	}
	
	public DefaultRoleAccess find(Long roleId, Long accessTypeId) {
		List results= this.getHibernateTemplate().find("from DefaultRoleAccess dra where dra.roleId=? and dra.accessTypeId=?"
				,new Object[] {roleId, accessTypeId});
		
		if(!results.isEmpty()) {
			return (DefaultRoleAccess)results.get(0);
		}
		return null;
	}

}
