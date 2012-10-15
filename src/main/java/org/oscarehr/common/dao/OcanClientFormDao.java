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

import org.oscarehr.common.model.OcanClientForm;
import org.springframework.stereotype.Repository;

@Repository
public class OcanClientFormDao extends AbstractDao<OcanClientForm> {

	public OcanClientFormDao() {
		super(OcanClientForm.class);
	}

	public OcanClientForm findLatestByFacilityClient(Integer facilityId, Integer clientId) {

		String sqlCommand = "select * from OcanClientForm where facilityId=?1 and clientId=?2 order by created desc";

		Query query = entityManager.createNativeQuery(sqlCommand, modelClass);
		query.setParameter(1, facilityId);
		query.setParameter(2, clientId);
		
		return getSingleResultOrNull(query);
	}

    public List<OcanClientForm> findByFacilityClient(Integer facilityId, Integer clientId) {

		String sqlCommand = "select x from OcanClientForm x where x.facilityId=?1 and x.clientId=?2 order by x.created desc";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, facilityId);
		query.setParameter(2, clientId);
		
		@SuppressWarnings("unchecked")
		List<OcanClientForm> results=query.getResultList();
		
		return (results);
	}

    public List<OcanClientForm> findLatestSignedOcanForms(Integer facilityId, String formVersion, Date startDate, Date endDate) {
		
		String sqlCommand="select x from OcanClientForm x where x.facilityId=?1 and x.signed=?2 and x.ocanFormVersion=?3 and x.startDate>=?4 and x.startDate<?5";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, facilityId);
		query.setParameter(2, true);
		query.setParameter(3, formVersion);
		query.setParameter(4, startDate);
		query.setParameter(5, endDate);
		
		@SuppressWarnings("unchecked")
		List<OcanClientForm> results=query.getResultList();
		
		return(results);
    }
    
    public OcanClientForm findLatestSignedOcanForm(Integer facilityId, Integer demographicNo, String formVersion, Date startDate, Date endDate) {
		
		String sqlCommand="select x from OcanClientForm x where x.facilityId=?1 and x.clientId=?2 and x.ocanFormVersion=?3 and x.completionDate>=?4 and x.completionDate<?5 order by x.created DESC";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, facilityId);
		query.setParameter(2, demographicNo);
		query.setParameter(3, formVersion);
		query.setParameter(4, startDate);
		query.setParameter(5, endDate);
		
		@SuppressWarnings("unchecked")
		List<OcanClientForm> results=query.getResultList();
		
		return (results.size()>0?results.get(0):null);
	
    }
}
