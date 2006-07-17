package org.caisi.PMmodule.service;

import java.util.List;

public interface OscarSecurityManager {
	public List getUserRoles(String providerNo);
	public boolean hasAdminRole(String providerNo);
}
