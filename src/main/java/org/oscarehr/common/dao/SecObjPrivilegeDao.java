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


package org.oscarehr.common.dao;

import java.util.Collection;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.SecObjPrivilege;
import org.springframework.stereotype.Repository;

@Repository
public class SecObjPrivilegeDao extends AbstractDao<SecObjPrivilege> {

	public SecObjPrivilegeDao() {
		super(SecObjPrivilege.class);
	}

	public List<SecObjPrivilege> findByObjectNames(Collection<String> objectNames) {
		String sql = "select s FROM SecObjPrivilege s WHERE s.id.objectName IN (:obj) order by s.priority desc";

		Query query = entityManager.createQuery(sql);
		query.setParameter("obj",  objectNames);

		@SuppressWarnings("unchecked")
		List<SecObjPrivilege> result =  query.getResultList();

		return result;
	}

	public List<SecObjPrivilege> findByRoleUserGroup(String roleUserGroup) {
		String sql = "select s FROM SecObjPrivilege s WHERE s.id.roleUserGroup like ? order by s.id.roleUserGroup, s.id.objectName";

		Query query = entityManager.createQuery(sql);
		query.setParameter(1, roleUserGroup);
		@SuppressWarnings("unchecked")
		List<SecObjPrivilege> result =  query.getResultList();

		return result;
	}

	public List<SecObjPrivilege> findByObjectName(String objectName) {
		String sql = "select s FROM SecObjPrivilege s WHERE s.id.objectName like ? order by s.id.objectName, s.id.roleUserGroup";

		Query query = entityManager.createQuery(sql);
		query.setParameter(1, objectName);
		@SuppressWarnings("unchecked")
		List<SecObjPrivilege> result =  query.getResultList();

		return result;
	}
}
