package org.oscarehr.PMmodule.web.formbean;

public class DefaultRoleAccessFormBean {
	private int id;
	private int roleId;
	private int accessTypeId;
	
	public int getAccessTypeId() {
		return accessTypeId;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setAccessTypeId(int accessTypeId) {
		this.accessTypeId = accessTypeId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	
	
}
