package org.oscarehr.common.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class SecObjPrivilegePrimaryKey implements Serializable {
	private String roleUserGroup = null;
	private String objectName = null;

	public SecObjPrivilegePrimaryKey()
	{
		// do nothing, required by jpa
	}
	
	public SecObjPrivilegePrimaryKey(String roleUserGroup, String objectName)
	{
		this.roleUserGroup=roleUserGroup;
		this.objectName=objectName;
	}

	public String getRoleUserGroup() {
		return roleUserGroup;
	}

	public void setRoleUserGroup(String roleUserGroup) {
		this.roleUserGroup = roleUserGroup;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	@Override
	public String toString() {
		return ("roleUserGroup=" + roleUserGroup + ", objectName=" + objectName);
	}

	@Override
	public int hashCode() {
		return (toString().hashCode());
	}

	@Override
	public boolean equals(Object o) {
		try {
			SecObjPrivilegePrimaryKey o1 = (SecObjPrivilegePrimaryKey) o;
			return ((roleUserGroup == o1.roleUserGroup) && (roleUserGroup == o1.roleUserGroup));
		} catch (RuntimeException e) {
			return (false);
		}
	}

}