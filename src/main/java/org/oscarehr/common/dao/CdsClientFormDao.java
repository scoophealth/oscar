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

import org.oscarehr.common.model.CdsClientForm;
import org.springframework.stereotype.Repository;

@Repository
public class CdsClientFormDao extends AbstractDao<CdsClientForm> {

	public CdsClientFormDao() {
		super(CdsClientForm.class);
	}

	public CdsClientForm findLatestByFacilityClient(Integer facilityId, Integer clientId) {

		String sqlCommand = "select * from CdsClientForm where facilityId=?1 and clientId=?2 order by created desc";

		Query query = entityManager.createNativeQuery(sqlCommand, modelClass);
		query.setParameter(1, facilityId);
		query.setParameter(2, clientId);
		query.setMaxResults(1);
		
		return getSingleResultOrNull(query);
	}

	/**	
	* will return the latest cds form or null if none match the criteria	
	* @param signed can be null for either signed or unsigned	
	*/
	public CdsClientForm findLatestByFacilityAdmissionId(Integer facilityId, Integer admissionId, Boolean signed) {
		String sqlCommand = "select x from CdsClientForm x where x.facilityId=?1 and x.admissionId=?2" + (signed != null ? " and signed=?3" : "") + " order by x.created desc";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, facilityId);
		query.setParameter(2, admissionId);
		if (signed != null) query.setParameter(3, signed);

		@SuppressWarnings("unchecked")
		List<CdsClientForm> results = query.getResultList();
		if (results.size() > 0) return (results.get(0));
		else return (null);
	}
	
    public List<CdsClientForm> findByFacilityClient(Integer facilityId, Integer clientId) {

		String sqlCommand = "select x from CdsClientForm x where x.facilityId=?1 and x.clientId=?2 order by x.created desc";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, facilityId);
		query.setParameter(2, clientId);
		
		@SuppressWarnings("unchecked")
		List<CdsClientForm> results=query.getResultList();
		
		return (results);
	}

    public List<CdsClientForm> findSignedCdsForms(Integer facilityId, String formVersion, Date startDate, Date endDate) {
		
		//String sqlCommand="select x from CdsClientForm x where x.facilityId=?1 and x.signed=?2 and x.cdsFormVersion=?3 and x.created>=?4 and x.created<?5";
    	//do not need to use date range here as it will be considered in admissionMap in Cds4ReportUIBean.java
		String sqlCommand="select x from CdsClientForm x where x.facilityId=?1 and x.signed=?2 and x.cdsFormVersion=?3 ";
		
		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, facilityId);
		query.setParameter(2, true);
		query.setParameter(3, formVersion);
		//query.setParameter(4, startDate);
		//query.setParameter(5, endDate);
		
		@SuppressWarnings("unchecked")
		List<CdsClientForm> results=query.getResultList();
		
		return(results);
    }
    
    public CdsClientForm findLatestByClientIdAndAdmisionId(Integer clientId, Integer admissionId) {

  		String sqlCommand = "select x from CdsClientForm x where x.clientId=?1 and x.admissionId=?2 order by x.created desc";

  		Query query = entityManager.createQuery(sqlCommand);
  		query.setParameter(1, clientId);
  		query.setParameter(2, admissionId);
  		
  		@SuppressWarnings("unchecked")
  		List<CdsClientForm> results=query.getResultList();
  		
  		if(results.size()>0)
  			return (results.get(0));
  		else 
  			return null;
  	}
    
 
    public CdsClientForm findCdsFormsByAdmissionId(Integer clientId, Integer admissionId) {
		
		String sqlCommand="select x from CdsClientForm x where x.admissionId=?1 and x.clientId=?2 order by x.id desc";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, admissionId);
		query.setParameter(2, clientId);
		query.setMaxResults(1);
		return getSingleResultOrNull(query);				
    }
    
}
