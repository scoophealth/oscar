package org.caisi.casemgmt.dao;

import java.util.List;

import org.caisi.model.Role;

public interface ProviderCaisiRoleDAO
{
	public Role getProviderCaisiRole(String providerNo);
	public List getAllProviderCaisiRole(String providerNo);
	public List getAllCaisiRole();
}
