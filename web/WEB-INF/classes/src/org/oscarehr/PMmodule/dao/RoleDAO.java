package org.oscarehr.PMmodule.dao;

import java.util.List;

import org.caisi.model.Role;

public interface RoleDAO {
	
	public List getRoles();
	
	public Role getRole(Long id);
}
