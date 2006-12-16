package org.oscarehr.PMmodule.dao.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.dao.OscarSecurityDAO;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class OscarSecurityDAOHibernate extends HibernateDaoSupport implements
		OscarSecurityDAO {

	private static Log log = LogFactory.getLog(OscarSecurityDAOHibernate.class);

	
	public List getUserRoles(String providerNo) {
		if(providerNo == null) {
			throw new IllegalArgumentException();
		}
		
		List results =  this.getHibernateTemplate().find("from SecUserRole s where s.ProviderNo = ?",providerNo);
		
		if(log.isDebugEnabled()) {
			log.debug("getUserRoles: providerNo=" + providerNo + ",# of results=" + results.size());
		}
		
		return results;
	}

	public boolean hasAdminRole(String providerNo) {
		if(providerNo == null) {
			throw new IllegalArgumentException();
		}
		
		boolean result = false;
		List results = this.getHibernateTemplate().find("from SecUserRole s where s.ProviderNo = ? and s.RoleName = 'admin'",providerNo);
		if(!results.isEmpty()) {
			result =  true;
		}
		
		if(log.isDebugEnabled()) {
			log.debug("hasAdminRole: providerNo=" + providerNo + ",result=" + result);
		}
		
		return result;
	}

}
