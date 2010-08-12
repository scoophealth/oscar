/**
 * Copyright (c) 2007-2009. CAISI, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * This software was written for 
 * CAISI, 
 * Toronto, Ontario, Canada 
 */

package org.oscarehr.common.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.OcanStaffForm;
import org.springframework.stereotype.Repository;

@Repository
public class OcanStaffFormDao extends AbstractDao<OcanStaffForm> {

	public OcanStaffFormDao() {
		super(OcanStaffForm.class);
	}

	public OcanStaffForm findLatestByFacilityClient(Integer facilityId, Integer clientId) {

		String sqlCommand = "select * from OcanStaffForm where facilityId=?1 and clientId=?2 order by created desc limit 1";

		Query query = entityManager.createNativeQuery(sqlCommand, modelClass);
		query.setParameter(1, facilityId);
		query.setParameter(2, clientId);
		
		return getSingleResultOrNull(query);
	}

    public List<OcanStaffForm> findByFacilityClient(Integer facilityId, Integer clientId) {

		String sqlCommand = "select x from OcanStaffForm x where x.facilityId=?1 and x.clientId=?2 order by x.created desc";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, facilityId);
		query.setParameter(2, clientId);
		
		@SuppressWarnings("unchecked")
		List<OcanStaffForm> results=query.getResultList();
		
		return (results);
	}

    public List<OcanStaffForm> findLatestSignedOcanForms(Integer facilityId, String formVersion, Date startDate, Date endDate) {
		
		String sqlCommand="select x from OcanStaffForm x where x.facilityId=?1 and x.assessmentStatus=?2 and x.ocanFormVersion=?3 and x.completionDate>=?4 and x.completionDate<?5";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, facilityId);
		query.setParameter(2, "Complete");
		query.setParameter(3, formVersion);
		query.setParameter(4, startDate);
		query.setParameter(5, endDate);
		
		@SuppressWarnings("unchecked")
		List<OcanStaffForm> results=query.getResultList();
		
		return(results);
    }
}
