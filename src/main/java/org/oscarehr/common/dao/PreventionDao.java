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

import org.oscarehr.common.NativeSql;
import org.oscarehr.common.model.Prevention;

public class PreventionDao extends AbstractDao<Prevention> {

	public PreventionDao() {
		super(Prevention.class);
	}

	public List<Prevention> findByDemographicId(Integer demographicId) {
		Query query = entityManager.createQuery("select x from "+modelClass.getSimpleName()+" x where demographicId=?1");
		query.setParameter(1, demographicId);

		@SuppressWarnings("unchecked")
		List<Prevention> results = query.getResultList();
		
		return (results);
	}
    
	/**
	 * @return results ordered by lastUpdateDate
	 */
	public List<Prevention> findByUpdateDate(Date updatedAfterThisDateExclusive, int itemsToReturn) {
		String sqlCommand = "select x from "+modelClass.getSimpleName()+" x where x.lastUpdateDate>?1 order by x.lastUpdateDate";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, updatedAfterThisDateExclusive);
		setLimit(query, itemsToReturn);
		
		@SuppressWarnings("unchecked")
		List<Prevention> results = query.getResultList();
		return (results);
	}

    public List<Prevention> findByDemographicIdAfterDatetime(Integer demographicId, Date dateTime) {
    	Query query = entityManager.createQuery("select x from Prevention x where demographicId=?1 and lastUpdateDate>=?2 and deleted='0'");
    	query.setParameter(1, demographicId);
		query.setParameter(2, dateTime);

		@SuppressWarnings("unchecked")
        List<Prevention> results = query.getResultList();

		return (results);
	}
    
	/*
	 * for integrator
	 */
	public List<Integer> findDemographicIdsAfterDatetime(Date dateTime) {
		Query query = entityManager.createQuery("select x.demographicId from Prevention x where x.lastUpdateDate > ?1");
		query.setParameter(1, dateTime);

		@SuppressWarnings("unchecked")
		List<Integer> results = query.getResultList();

		return (results);
	}
	
	public List<Prevention> findByProviderDemographicLastUpdateDate(String providerNo, Integer demographicId, Date updatedAfterThisDateExclusive, int itemsToReturn) {
		String sqlCommand = "select x from "+modelClass.getSimpleName()+" x where x.demographicId=:demographicId and x.providerNo=:providerNo and x.lastUpdateDate>:updatedAfterThisDateExclusive order by x.lastUpdateDate";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter("demographicId", demographicId);
		query.setParameter("providerNo", providerNo);
		query.setParameter("updatedAfterThisDateExclusive", updatedAfterThisDateExclusive);
		setLimit(query, itemsToReturn);
		
		@SuppressWarnings("unchecked")
		List<Prevention> results = query.getResultList();
		return (results);
	}

	public List<Prevention> findNotDeletedByDemographicIdAfterDatetime(Integer demographicId, Date dateTime) {
		Query query = entityManager.createQuery("select x from Prevention x where demographicId=?1 and lastUpdateDate> ?2");
		query.setParameter(1, demographicId);
		query.setParameter(2, dateTime);

		@SuppressWarnings("unchecked")
		List<Prevention> results = query.getResultList();

		return (results);
	}
	
	public List<Integer> findNonDeletedIdsByDemographic(Integer demographicId) {
		Query query = entityManager.createQuery("select x.id from Prevention x where demographicId=?1 and deleted='0'");
		query.setParameter(1, demographicId);
	
		@SuppressWarnings("unchecked")
		List<Integer> results = query.getResultList();

		return (results);
	}
	

	public List<Prevention> findNotDeletedByDemographicId(Integer demographicId) {
		Query query = entityManager.createQuery("select x from "+modelClass.getSimpleName()+" x where demographicId=?1 and deleted=?2");
		query.setParameter(1, demographicId);
		query.setParameter(2, '0');

		@SuppressWarnings("unchecked")
        List<Prevention> results = query.getResultList();

		return (results);
	}

	public List<Prevention> findByTypeAndDate(String preventionType, Date startDate, Date endDate) {
		Query query = entityManager.createQuery("select x from "+modelClass.getSimpleName()+" x where preventionType=?1 and preventionDate>=?2 and preventionDate<=?3 and deleted='0' and refused='0' order by preventionDate");
		query.setParameter(1, preventionType);
		query.setParameter(2, startDate);
		query.setParameter(3, endDate);

		@SuppressWarnings("unchecked")
        List<Prevention> results = query.getResultList();

		return (results);
	}

	public List<Prevention> findByTypeAndDemoNo(String preventionType, Integer demoNo) {
		Query query = entityManager.createQuery("select x from "+modelClass.getSimpleName()+" x where preventionType=?1 and demographicId=?2 and deleted='0' order by preventionDate");
		query.setParameter(1, preventionType);
		query.setParameter(2, demoNo);
		
		@SuppressWarnings("unchecked")
        List<Prevention> results = query.getResultList();
		return (results);
	}

	@SuppressWarnings("unchecked")
    public List<Prevention> findActiveByDemoId(Integer demoId) {
		Query query = createQuery("p", "p.demographicId = :demoNo and p.deleted <> '1' ORDER BY p.preventionType, p.preventionDate");
		query.setParameter("demoNo", demoId);
		return query.getResultList();
	}
	

	public List<Prevention> findUniqueByDemographicId(Integer demographicId) {
		Query query = entityManager.createQuery("select x from "+modelClass.getSimpleName()+" x where demographicId=?1 and deleted='0' GROUP BY preventionType ORDER BY preventionDate DESC");
		query.setParameter(1, demographicId);

		@SuppressWarnings("unchecked")
		List<Prevention> results = query.getResultList();
		
		return (results);
	}
	
	
	@NativeSql("preventions")
	public List<Integer> findNewPreventionsSinceDemoKey(String keyName) {
		
		String sql = "select distinct dr.demographic_no from preventions dr,demographic d,demographicExt e where dr.demographic_no = d.demographic_no and d.demographic_no = e.demographic_no and e.key_val=? and dr.lastUpdateDate > e.value";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter(1,keyName);
		return query.getResultList();
	}
	
	
}
