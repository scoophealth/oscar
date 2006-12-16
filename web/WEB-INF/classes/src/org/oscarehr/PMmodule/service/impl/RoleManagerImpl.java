package org.oscarehr.PMmodule.service.impl;

import java.util.List;

import org.caisi.model.Role;
import org.oscarehr.PMmodule.dao.ProgramAccessDAO;
import org.oscarehr.PMmodule.dao.RoleDAO;
import org.oscarehr.PMmodule.service.RoleManager;

public class RoleManagerImpl implements RoleManager {

	private RoleDAO dao;
	private ProgramAccessDAO paDAO;
	
	public void setProgramAccessDAO(ProgramAccessDAO dao) {
		this.paDAO = dao;
	}
	
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
