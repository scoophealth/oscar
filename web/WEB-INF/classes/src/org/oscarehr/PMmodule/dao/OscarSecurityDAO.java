package org.oscarehr.PMmodule.dao;

import java.util.List;

public interface OscarSecurityDAO {

	public List getUserRoles(String providerNo);
	public boolean hasAdminRole(String providerNo);
}
