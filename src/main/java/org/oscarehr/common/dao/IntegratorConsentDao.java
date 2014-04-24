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

import org.oscarehr.common.model.IntegratorConsent;
import org.springframework.stereotype.Repository;

@Repository
public class IntegratorConsentDao extends AbstractDao<IntegratorConsent> {

	public IntegratorConsentDao() {
		super(IntegratorConsent.class);
	}

	/**
     * results are ordered by newest first
     */
    public IntegratorConsent findLatestByFacilityDemographic(int facilityId, int demographicId) {

    	String sqlCommand="select * from IntegratorConsent where facilityId=?1 and demographicId=?2 order by createdDate desc";
    	
    	Query query = entityManager.createNativeQuery(sqlCommand, modelClass);
		query.setParameter(1, facilityId);
		query.setParameter(2, demographicId);

		return(getSingleResultOrNull(query));
    }

    /**
     * results are ordered by newest first
     */
	public List<IntegratorConsent> findByFacilityAndDemographic(int facilityId, int demographicId) {
		Query query = entityManager.createQuery("select x from IntegratorConsent x where x.facilityId=?1 and x.demographicId=?2 order by x.createdDate desc");
		query.setParameter(1, facilityId);
		query.setParameter(2, demographicId);

	    @SuppressWarnings("unchecked")
		List<IntegratorConsent> results=query.getResultList();
		
		return(results);
    }

	public List<IntegratorConsent> findByFacilityAndDemographicSince(int facilityId, int demographicId, Date lastDataUpdated) {
		Query query = entityManager.createQuery("select x from IntegratorConsent x where x.facilityId=?1 and x.demographicId=?2 and x.createdDate > ?3 order by x.createdDate desc");
		query.setParameter(1, facilityId);
		query.setParameter(2, demographicId);
		query.setParameter(3, lastDataUpdated);

	    @SuppressWarnings("unchecked")
		List<IntegratorConsent> results=query.getResultList();
		
		return(results);
    }

	/**
	 * This is for the Integrator.
	 */
	public List<Integer> findDemographicIdsByFacilitySince(int facilityId, Date lastDataUpdated) {
		Query query = entityManager.createQuery("select distinct x.demographicId from IntegratorConsent x where x.facilityId=?1 and x.createdDate > ?2 order by x.createdDate desc");
		query.setParameter(1, facilityId);
		query.setParameter(2, lastDataUpdated);

	    @SuppressWarnings("unchecked")
		List<Integer> results=query.getResultList();
		
		return(results);
    }
}
