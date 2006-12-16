package org.oscarehr.PMmodule.dao;

import java.util.List;

import org.oscarehr.PMmodule.model.DefaultRoleAccess;

public interface DefaultRoleAccessDAO {
	public List getDefaultRoleAccesses();
	public DefaultRoleAccess getDefaultRoleAccess(Long id);
	public void saveDefaultRoleAccess(DefaultRoleAccess dra);
	public void deleteDefaultRoleAccess(Long id);
	public DefaultRoleAccess find(Long roleId, Long accessTypeId);
}
