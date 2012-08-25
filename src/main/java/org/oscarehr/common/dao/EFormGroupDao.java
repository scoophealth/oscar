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

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.EFormGroup;
import org.springframework.stereotype.Repository;

@Repository
public class EFormGroupDao extends AbstractDao<EFormGroup> {

	public EFormGroupDao() {
		super(EFormGroup.class);
	}

	/**
	 * Deletes groups with the specified name and, optionally, form ID.
	 * 
	 * @param groupName
	 * 		Name of the group to delete
	 * @param formId
	 * 		ID of the form for the group to be deleted. In case this value is set to null, only the group name is used for 
	 * 		deletion selection
	 * @return
	 * 		Returns the number of the deleted groups
	 */
	public int deleteByNameAndFormId(String groupName, Integer formId) {
		StringBuilder buf = new StringBuilder("DELETE FROM " + modelClass.getSimpleName() + " g WHERE g.groupName = :groupName");
		if (formId != null)
			buf.append(" AND g.formId = :formId");
		
		Query query = entityManager.createQuery(buf.toString());
		query.setParameter("groupName", groupName);
		if (formId != null)
			query.setParameter("formId", formId);
		
		return query.executeUpdate();
	}
	
	public int deleteByName(String groupName) {
		return deleteByNameAndFormId(groupName, null);
	}

	public List<EFormGroup> getByGroupName(String groupName) {
		String sql = "select eg from EFormGroup eg where eg.groupName=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, groupName);

		@SuppressWarnings("unchecked")
		List<EFormGroup> results = query.getResultList();

		return results;
	}

	public List<String> getGroupNames() {
		String sql = "select distinct eg.groupName from EFormGroup eg";
		Query query = entityManager.createQuery(sql);

		@SuppressWarnings("unchecked")
		List<String> results = query.getResultList();

		return results;
	}

}
