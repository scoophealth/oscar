package org.oscarehr.PMmodule.service.impl;

import java.util.List;

import org.oscarehr.PMmodule.dao.OscarSecurityDAO;
import org.oscarehr.PMmodule.service.OscarSecurityManager;

public class OscarSecurityManagerImpl implements OscarSecurityManager {

	private OscarSecurityDAO dao;
	
	public void setOscarSecurityDAO(OscarSecurityDAO dao) {
		this.dao = dao;
	}
	
	public List getUserRoles(String providerNo) {
		return dao.getUserRoles(providerNo);
	}

	public boolean hasAdminRole(String providerNo) {
		return dao.hasAdminRole(providerNo);
	}

}
