package org.caisi.PMmodule.service;

import java.util.List;

import org.caisi.model.Role;

public interface RoleManager {
	public List getRoles();
	public Role getRole(String id);
}
