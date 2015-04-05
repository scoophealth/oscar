/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.common.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.Prescription;
import org.springframework.stereotype.Repository;

@Repository
public class PrescriptionDao extends AbstractDao<Prescription> {

	public PrescriptionDao() {
		super(Prescription.class);
	}

	public List<Prescription> findByDemographicId(Integer demographicId) {

		String sqlCommand = "select x from " + modelClass.getSimpleName() + " x where x.demographicId=?1";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, demographicId);

		@SuppressWarnings("unchecked")
		List<Prescription> results = query.getResultList();
		return (results);
	}

	public List<Prescription> findByDemographicIdUpdatedAfterDate(Integer demographicId, Date afterThisDate) {
		String sqlCommand = "select x from " + modelClass.getSimpleName() + " x where x.demographicId=?1 and x.lastUpdateDate>=?2";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, demographicId);
		query.setParameter(2, afterThisDate);

		@SuppressWarnings("unchecked")
		List<Prescription> results = query.getResultList();
		return (results);
	}

	public int updatePrescriptionsByScriptNo(Integer scriptNo, String comment) {
		Query query = entityManager.createQuery("UPDATE Prescription p SET p.comments = :comments WHERE p.id = :id");
		query.setParameter("comments", comment);
		query.setParameter("id", scriptNo);
		return query.executeUpdate();
	}

	/**
	 * @return results ordered by lastUpdateDate
	 */
	public List<Prescription> findByUpdateDate(Date updatedAfterThisDateExclusive, int itemsToReturn) {
		String sqlCommand = "select x from "+modelClass.getSimpleName()+" x where x.lastUpdateDate>?1 order by x.lastUpdateDate";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, updatedAfterThisDateExclusive);
		setLimit(query, itemsToReturn);
		
		@SuppressWarnings("unchecked")
		List<Prescription> results = query.getResultList();
		return (results);
	}
	
	/**
	 * @return results ordered by lastUpdateDate asc
	 */
	public List<Prescription> findByProviderDemographicLastUpdateDate(String providerNo, Integer demographicId, Date updatedAfterThisDateExclusive, int itemsToReturn) {
		String sqlCommand = "select x from "+modelClass.getSimpleName()+" x where x.demographicId=:demographicId and x.providerNo=:providerNo and x.lastUpdateDate>:updatedAfterThisDateExclusive order by x.lastUpdateDate";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter("demographicId", demographicId);
		query.setParameter("providerNo", providerNo);
		query.setParameter("updatedAfterThisDateExclusive", updatedAfterThisDateExclusive);
		setLimit(query, itemsToReturn);
		
		@SuppressWarnings("unchecked")
		List<Prescription> results = query.getResultList();
		return (results);
	}

}
