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

import org.oscarehr.common.model.OLISResults;
import org.springframework.stereotype.Repository;

@Repository
public class OLISResultsDao extends AbstractDao<OLISResults>{

	public OLISResultsDao() {
		super(OLISResults.class);
	}

	public boolean hasExistingResult(String requestingHICProviderNo, String queryType, String hash) {
		Query query = entityManager.createQuery("select x from OLISResults x where x.requestingHICProviderNo=? and x.queryType=? and x.hash = ?");
		query.setParameter(1, requestingHICProviderNo);
		query.setParameter(2, queryType);
		query.setParameter(3, hash);

		if(!query.getResultList().isEmpty()) {
			return true;
		}
		
		return false;
	}
	
	public List<OLISResults> getResultList(String requestingHICProviderNo, String queryType) {
		Query query = entityManager.createQuery("select x from OLISResults x where x.requestingHICProviderNo=? and x.queryType=? and status IS NULL");
		query.setParameter(1, requestingHICProviderNo);
		query.setParameter(2, queryType);
		
		@SuppressWarnings("unchecked")
		List<OLISResults> results = query.getResultList();
		
		return results;
	}
	
	public OLISResults findByUUID(String uuid) {
		Query query = entityManager.createQuery("select x from OLISResults x where x.uuid=?");
		query.setParameter(1, uuid);
		
		return this.getSingleResultOrNull(query);
	}
}
