package org.oscarehr.casemgmt.dao;

import java.util.List;

public interface RoleProgramAccessDAO extends DAO
{
	public List getProgramProviderByProviderProgramID(String providerNo, Long programId);
	
	public List getProgramProviderByProviderNo(String providerNo);
	
	public List getAccessListByProgramID(Long programId);
	
	public List getDefaultAccessRightByRole(Long roleId);
		
	public List getAllRoleName();
}
