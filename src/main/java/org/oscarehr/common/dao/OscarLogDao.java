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

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.AbstractModel;
import org.oscarehr.common.model.OscarLog;
import org.oscarehr.util.SpringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class OscarLogDao extends AbstractDao<OscarLog> {

	public OscarLogDao() {
		super(OscarLog.class);
	}

    public List<OscarLog> findByDemographicId(Integer demographicId) {

    	String sqlCommand="select x from "+modelClass.getSimpleName()+" x where x.demographicId=?1";
    	
    	Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, demographicId);

	    @SuppressWarnings("unchecked")
		List<OscarLog> results=query.getResultList();
		
		return(results);
    }
    
    
    public boolean hasRead(String providerNo, String content, String contentId){
    	String sqlCommand="select x from "+modelClass.getSimpleName()+" x where x.action = 'read' and  x.providerNo=?1 and x.content = ?2 and x.contentId = ?3";
    	Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, providerNo);
		query.setParameter(2, content);
		query.setParameter(3, contentId);
		
	    @SuppressWarnings("unchecked")
		List<OscarLog> results=query.getResultList();
	    if(results.size() == 0){
	    	return false;
	    }
		
		return true;
    }
    
    public List<OscarLog> findByActionAndData(String action, String data) {
    	String sqlCommand="select x from "+modelClass.getSimpleName()+" x where x.action = ?1 and x.data = ?2 order by x.created DESC";
    	Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, action);
		query.setParameter(2, data);
		
	    @SuppressWarnings("unchecked")
		List<OscarLog> results=query.getResultList();
	    
	    return results;
    }
    
    
    public List<OscarLog> findByActionContentAndDemographicId(String action, String content, Integer demographicId) {

    	String sqlCommand="select x from "+modelClass.getSimpleName()+" x where x.action=?1 and x.content = ?2 and x.demographicId=?3 order by x.created desc";
    	
    	Query query = entityManager.createQuery(sqlCommand);
    	query.setParameter(1, action);
    	query.setParameter(2, content);
		query.setParameter(3, demographicId);

	    @SuppressWarnings("unchecked")
		List<OscarLog> results=query.getResultList();
		
		return(results);
    }

	public List<Integer> getDemographicIdsOpenedSinceTime(Date value) {
		String sqlCommand="select distinct demographicId from "+modelClass.getSimpleName()+" where dateTime >= ?1";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, value);

		@SuppressWarnings("unchecked")
		List<Integer> results=query.getResultList();
		results.removeAll(Collections.singleton(null));

		return(results);
	}

	public List<Integer> getRecentDemographicsAccessedByProvider(String providerNo, int startPosition, int itemsToReturn) {
		String sqlCommand = null; 
		Query query = null;
		if (org.oscarehr.common.IsPropertiesOn.isMultisitesEnable()) {
			SiteRoleMpgDao siteRoleMpgDao = SpringUtils.getBean(SiteRoleMpgDao.class);
			SecRoleDao secRoleDao = SpringUtils.getBean(SecRoleDao.class);
			DemographicSiteDao demoSiteDao = SpringUtils.getBean(DemographicSiteDao.class);
			do {
				List<Integer> roleNos = secRoleDao.findRoleNosByProviderNo(providerNo);
				if (roleNos.isEmpty()) {
					break;
				}
				List<Integer> siteIds = siteRoleMpgDao.getSiteIdByAccRoleId(roleNos);
				if (siteIds.isEmpty()) {
					break;
				}
				List<Integer> demoNos = demoSiteDao.findDemoNosBySiteIds(siteIds);
				if (demoNos.isEmpty()) {
					break;
				}
				sqlCommand = "select distinct demographicId from " + modelClass.getSimpleName() + " l where l.providerNo = :providerNo and l.demographicId in (:demoNos) order by dateTime desc";
				query = entityManager.createQuery(sqlCommand);
				query.setParameter("demoNos", demoNos);
			} while (false);
		} 
		if (sqlCommand == null) { // if there's no site can be accessed by current user or no demographic in accessed site or multisites is off
			sqlCommand = "select distinct demographicId from " + modelClass.getSimpleName() + " l where l.providerNo = :providerNo and l.demographicId is not null and l.demographicId != '-1' order by dateTime desc";
			query = entityManager.createQuery(sqlCommand);
		}
		query.setParameter("providerNo", providerNo);
		query.setFirstResult(startPosition);
		setLimit(query, itemsToReturn);

		@SuppressWarnings("unchecked")
		List<Integer> results = query.getResultList();

		return (results);
	}
	/**
	 * 
	 * @param providerNo
	 * @param startPosition
	 * @param itemsToReturn
	 * @return List of Object array [demographicId (Integer), lastDateViewed Date]
	 */
	public List<Object[]> getRecentDemographicsViewedByProvider(String providerNo, int startPosition, int itemsToReturn) {
		String sqlCommand="select l.demographicId,MAX(l.created) as dt from "+modelClass.getSimpleName()+" l where l.providerNo = ?1 and l.demographicId is not null and l.demographicId != '-1' group by l.demographicId order by MAX(l.created) desc";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, providerNo);
		query.setFirstResult(startPosition);
		setLimit(query,itemsToReturn);

		@SuppressWarnings("unchecked")
		List<Object[]> results=query.getResultList();
		
		return(results);
	}


	/**
	 *
	 * @param providerNo
	 * @param startPosition
	 * @param itemsToReturn
	 * @return List of Object array [demographicId (Integer), lastDateViewed Date]
	 */
	public List<Object[]> getRecentDemographicsViewedByProviderAfterDateIncluded(String providerNo, Date date, int startPosition, int itemsToReturn) {
		String sqlCommand="select l.demographicId,MAX(l.created) as dt from "+modelClass.getSimpleName()+" l where l.providerNo = :providerNo and l.created >= :date and l.demographicId is not null and l.demographicId != '-1' group by l.demographicId order by MAX(l.created) desc";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter("providerNo", providerNo);
		query.setParameter("date", date);
		query.setFirstResult(startPosition);
		setLimit(query,itemsToReturn);

		@SuppressWarnings("unchecked")
		List<Object[]> results=query.getResultList();

		return(results);
	}

	/*
	 * Warning. Don't use this. It's only for the log purging feature.
	 */
	public int purgeLogEntries(Date maxDateToRemove) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String sqlCommand = "delete from " + modelClass.getSimpleName() + " WHERE dateTime <= ?";
		
		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, formatter.format(maxDateToRemove));
		int ret = query.executeUpdate();
		
		return ret;
		
		
	}
	
	@Override
    public void remove(AbstractModel<?> o) {
	    throw new SecurityException("Cannot remove audit log entries!");
    }

	@Override
    public boolean remove(Object id) {
		 throw new SecurityException("Cannot remove audit log entries!");
    }
	
	
}
