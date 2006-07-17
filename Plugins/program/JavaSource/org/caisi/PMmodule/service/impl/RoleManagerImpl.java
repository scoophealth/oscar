package org.caisi.PMmodule.service.impl;

import java.util.List;

import org.caisi.PMmodule.dao.RoleDAO;
import org.caisi.PMmodule.service.RoleManager;
import org.caisi.model.Role;

public class RoleManagerImpl implements RoleManager {

	private RoleDAO dao;
	
	public void setRoleDAO(RoleDAO dao) {
		this.dao = dao;
	}
	public List getRoles() {
		return dao.getRoles();
	}
	public Role getRole(String id) {
		return dao.getRole(Long.valueOf(id));
	}

}
