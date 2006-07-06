package org.caisi.dao;

import java.util.List;

import org.caisi.model.CaisiRole;
import org.caisi.model.Role;

public interface CaisiRoleDAO {
	public CaisiRole getRole(Long id);
	public CaisiRole getRoleByProviderNo(String provider_no);
	public List getRolesByRole(String role);
	public void saveRoleAssignment(CaisiRole role);
	public void saveRole(Role role);
	public boolean hasExist(String roleName);
	public List getRoles();
}
