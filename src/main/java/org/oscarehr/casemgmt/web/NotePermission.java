/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package org.oscarehr.casemgmt.web;

import org.oscarehr.PMmodule.model.ProgramProvider;

public class NotePermission {
	public enum AccessType { ALL_ROLES, ROLE, ROLE_GLOBAL, NO_ACCESS  };

	private String providerLastName;
	private String providerFirstName;
	private String providerNo;
	private String roleName;
	private String roleId;
	private AccessType accessType;

	public NotePermission(ProgramProvider programProvider, AccessType accessType) {
		this.providerLastName = programProvider.getProvider().getLastName();
		this.providerFirstName = programProvider.getProvider().getFirstName();
		this.providerNo = programProvider.getProviderNo();
		this.roleName = programProvider.getRole().getName();
		this.roleId = programProvider.getRoleId() + "";

		this.accessType = accessType;
	}

	public void setAccessType(AccessType accessType) {
		this.accessType = accessType;
	}

	public AccessType getAccessType() {
		return accessType;
	}

	public String getProviderLastName() {
		return providerLastName;
	}

	public void setProviderLastName(String providerLastName) {
		this.providerLastName = providerLastName;
	}

	public String getProviderFirstName() {
		return providerFirstName;
	}

	public void setProviderFirstName(String providerFirstName) {
		this.providerFirstName = providerFirstName;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}


}
