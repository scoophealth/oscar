package org.oscarehr.PMmodule.dao.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.caisi.model.Role;
import org.oscarehr.PMmodule.dao.RoleDAO;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class RoleDAOHibernate extends HibernateDaoSupport implements RoleDAO {

	private Log log = LogFactory.getLog(ProviderDaoHibernate.class);

	
	public List getRoles() {
		List results =  this.getHibernateTemplate().find("from Role r");
		
		if(log.isDebugEnabled()) {
			log.debug("getRoles: # of results=" + results.size());
		}
		
		return results;
	}
	
	public Role getRole(Long id) {
		if(id == null || id.intValue() <= 0) {
			throw new IllegalArgumentException();
		}
		
		Role result =  (Role)this.getHibernateTemplate().get(Role.class,id);

		if(log.isDebugEnabled()) {
			log.debug("getRole: id=" + id + ",found=" + (result!=null));
		}
		
		return result;
	}

}
