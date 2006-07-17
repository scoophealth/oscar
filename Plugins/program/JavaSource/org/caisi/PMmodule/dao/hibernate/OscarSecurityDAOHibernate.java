package org.caisi.PMmodule.dao.hibernate;

import java.util.List;

import org.caisi.PMmodule.dao.OscarSecurityDAO;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class OscarSecurityDAOHibernate extends HibernateDaoSupport implements
		OscarSecurityDAO {

	public List getUserRoles(String providerNo) {
		return this.getHibernateTemplate().find("from SecUserRole s where s.ProviderNo = ?",providerNo);
	}

	public boolean hasAdminRole(String providerNo) {
		List results = this.getHibernateTemplate().find("from SecUserRole s where s.ProviderNo = ? and s.RoleName = 'admin'",providerNo);
		if(results.size()>0) {
			return true;
		}
		return false;
	}

}
