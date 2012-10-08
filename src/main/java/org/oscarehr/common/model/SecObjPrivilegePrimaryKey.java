/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


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
			return ((roleUserGroup.equals(o1.roleUserGroup)) && (roleUserGroup.equals(o1.roleUserGroup)));
		} catch (RuntimeException e) {
			return (false);
		}
	}

}
