package org.caisi.service;

import java.util.List;

import org.caisi.model.CaisiRole;
import org.caisi.model.Role;

public interface CaisiRoleManager {
	public List getProviders();
	public void saveRoleAssignment(CaisiRole role);
	public String saveRole(Role role);
	public List getRoles();
	public void assignRole(String provider_no, String role_no);
}
