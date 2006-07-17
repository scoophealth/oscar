package org.caisi.casemgmt.dao;

import java.util.List;

public interface RoleProgramAccessDAO extends DAO
{
	public List getRoleByProgram(String programId, String demoNo);
	public List getProgramProviderByProviderProgramID(String providerNo, Long programId);
	public List getRoleListByProviderProgramID(String providerNo, Long programId);
	public List getAccessListByProgramID(String accessName, String accessType,
			Long programId);
	public List getProgramProviderByProviderNo(String providerNo);
	public List getAccessListByProgramID(Long programId);
	public List getDefaultAccessRightByRole(Long roleId);
	public List getDefaultAccessRight();
	
	public List getRoleNameByProviderProgramID(String providerNo, Long programId);
	public List getRoleNameByProviderProgramID(String providerNo);
	public List getAllRoleName();
}
