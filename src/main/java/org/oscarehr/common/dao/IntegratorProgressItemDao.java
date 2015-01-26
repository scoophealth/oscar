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

import org.oscarehr.common.model.IntegratorProgressItem;
import org.springframework.stereotype.Repository;

@Repository
public class IntegratorProgressItemDao extends AbstractDao<IntegratorProgressItem> {

	public IntegratorProgressItemDao() {
		super(IntegratorProgressItem.class);
	}

	public IntegratorProgressItem find(int integratorProgressId, int demographicNo) {
		Query query = entityManager.createQuery("SELECT f FROM IntegratorProgressItem f WHERE f.integratorProgressId=?1 and f.demographicNo = ?2");
		query.setParameter(1, integratorProgressId);
		query.setParameter(2, demographicNo);

		return getSingleResultOrNull(query);
	}
	
	public List<Integer> findOutstandingDemographicNos(int integratorProgressId) {
		Query query = entityManager.createQuery("SELECT f.demographicNo FROM IntegratorProgressItem f WHERE f.integratorProgressId=?1 and f.status != ?2");
		query.setParameter(1, integratorProgressId);
		query.setParameter(2, IntegratorProgressItem.STATUS_COMPLETED);

		@SuppressWarnings("unchecked")
		List<Integer> results = query.getResultList();
		
		return results;
		
	}
	
	public Integer findTotalOutstandingDemographicNos(int integratorProgressId) {
		Query query = entityManager.createQuery("SELECT count(f) FROM IntegratorProgressItem f WHERE f.integratorProgressId=?1 and f.status != ?2");
		query.setParameter(1, integratorProgressId);	
		query.setParameter(2, IntegratorProgressItem.STATUS_COMPLETED);

		Long total = (Long)query.getSingleResult();
		
		return total.intValue();
	}
	
	public Integer findTotalDemographicNos(int integratorProgressId) {
		Query query = entityManager.createQuery("SELECT count(f) FROM IntegratorProgressItem f WHERE f.integratorProgressId=?1");
		query.setParameter(1, integratorProgressId);	
		
		Long total = (Long)query.getSingleResult();
		
		return total.intValue();
	}

}
