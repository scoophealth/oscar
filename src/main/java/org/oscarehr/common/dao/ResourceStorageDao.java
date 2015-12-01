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

import org.oscarehr.common.model.ResourceStorage;
import org.springframework.stereotype.Repository;

@Repository
public class ResourceStorageDao extends AbstractDao<ResourceStorage> {

	public ResourceStorageDao() {
		super(ResourceStorage.class);
	}

	public ResourceStorage findActive(String resourceType){
		Query query = entityManager.createQuery("FROM " + modelClass.getSimpleName() + " r WHERE r.resourceType = :resourceType AND r.active = true");
		query.setParameter("resourceType", resourceType);
		return getSingleResultOrNull(query);
	}
	
	public List<ResourceStorage> findActiveAll(String resourceType){
		Query query = entityManager.createQuery("FROM " + modelClass.getSimpleName() + " r WHERE r.resourceType = :resourceType AND r.active = true");
		query.setParameter("resourceType", resourceType);
		return query.getResultList();
	}

}
