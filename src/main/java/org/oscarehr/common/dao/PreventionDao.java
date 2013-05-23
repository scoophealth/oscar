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

import org.oscarehr.common.model.Prevention;

@SuppressWarnings("unchecked")
public class PreventionDao extends AbstractDao<Prevention> {

	public PreventionDao() {
		super(Prevention.class);
	}

	public List<Prevention> findByDemographicId(Integer demographicId) {
		Query query = entityManager.createQuery("select x from Prevention x where demographicId=?1");
		query.setParameter(1, demographicId);

		List<Prevention> results = query.getResultList();

		return (results);
	}

	/**
	 * This method will return a list of items starting from the provided ID.
	 * It is an efficient method for iterating through all items (more efficient than using a startIndex).
	 */
    public List<Prevention> findByIdStart(Boolean archived, Integer startIdInclusive, int itemsToReturn) {
    	String sql = "select x from Prevention x where x.id>=?1 ";
    	if (archived!=null)	sql=sql+"and x.deleted=?2 ";
    	sql=sql+"order by x.id";
    	
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1, startIdInclusive);
    	if (archived!=null)	query.setParameter(2, archived?'0':'1');
    	
    	setLimit(query, itemsToReturn);

        @SuppressWarnings("unchecked")
        List<Prevention> preventions = query.getResultList();
        return preventions;
    }
    
    public List<Prevention> findByDemographicIdAfterDatetime(Integer demographicId, Date dateTime) {
		Query query = entityManager.createQuery("select x from Prevention x where demographicId=?1 and lastUpdateDate>=?2");
		query.setParameter(1, demographicId);
		query.setParameter(2, dateTime);

		List<Prevention> results = query.getResultList();

		return (results);
	}

	public List<Prevention> findNotDeletedByDemographicId(Integer demographicId) {
		Query query = entityManager.createQuery("select x from Prevention x where demographicId=?1 and deleted=?2");
		query.setParameter(1, demographicId);
		query.setParameter(2, '0');

		List<Prevention> results = query.getResultList();

		return (results);
	}

	public List<Prevention> findByTypeAndDate(String preventionType, Date startDate, Date endDate) {
		Query query = entityManager.createQuery("select x from Prevention x where preventionType=?1 and preventionDate>=?2 and preventionDate<=?3 and deleted='0' and refused='0' order by preventionDate");
		query.setParameter(1, preventionType);
		query.setParameter(2, startDate);
		query.setParameter(3, endDate);

		List<Prevention> results = query.getResultList();

		return (results);
	}

	public List<Prevention> findByTypeAndDemoNo(String preventionType, Integer demoNo) {
		Query query = entityManager.createQuery("select x from Prevention x where preventionType=?1 and demographicId=?2 and deleted='0' order by preventionDate");
		query.setParameter(1, preventionType);
		query.setParameter(2, demoNo);
		List<Prevention> results = query.getResultList();
		return (results);
	}

	public List<Prevention> findActiveByDemoId(Integer demoId) {
		Query query = createQuery("p", "p.demographicId = :demoNo and p.deleted <> '1' ORDER BY p.preventionType, p.preventionDate");
		query.setParameter("demoNo", demoId);
		return query.getResultList();
	}
}
