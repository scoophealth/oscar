package org.caisi.service.impl;

import java.util.List;

import org.caisi.dao.AccessTypeDAO;
import org.caisi.dao.CaisiRoleDAO;
import org.caisi.dao.ProviderDAO;
import org.caisi.model.CaisiRole;
import org.caisi.model.Role;
import org.caisi.service.CaisiRoleManager;

public class CaisiRoleManagerImpl implements CaisiRoleManager {

	private CaisiRoleDAO roleDAO;
	private ProviderDAO providerDAO;
	private AccessTypeDAO accessTypeDAO;
	
	public void setAccessTypeDAO(AccessTypeDAO dao) {
		this.accessTypeDAO = dao;
	}
	
	public void setCaisiRoleDAO(CaisiRoleDAO roleDAO) {
		this.roleDAO = roleDAO;
	}
	
	public void setProviderDAO(ProviderDAO providerDAO) {
		this.providerDAO = providerDAO;
	}
	
	public List getProviders() {
		return this.providerDAO.getProviders();
	}

	public void saveRoleAssignment(CaisiRole role) {
		this.roleDAO.saveRoleAssignment(role);
	}

	public String saveRole(Role role) {
		String rolename=role.getName();
		if ("".equals(rolename.trim())) return "role.empty";
		if (roleDAO.hasExist(rolename)) return "role.hasexist";
		this.roleDAO.saveRole(role);
		
		accessTypeDAO.addAccessType("write " + role.getName() + " issues", "access");
		accessTypeDAO.addAccessType("read " + role.getName() + " issues", "access");
		accessTypeDAO.addAccessType("read " + role.getName() + " notes", "access");
		
		return null;
	}
	public List getRoles() {
		return this.roleDAO.getRoles();
	}
	
	public void assignRole(String provider_no, String role_no) {
		//if choose 0 as role id, do nothing.
		if (Integer.valueOf(role_no).intValue()==0) return ;
		CaisiRole cr = new CaisiRole();
		cr.setProvider_no(provider_no);
		cr.setRole_id(Integer.valueOf(role_no).intValue());
		saveRoleAssignment(cr);
	}
}
