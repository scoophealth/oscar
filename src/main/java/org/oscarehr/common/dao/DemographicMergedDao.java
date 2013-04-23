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

import org.oscarehr.common.model.DemographicMerged;
import org.springframework.stereotype.Repository;

@Repository
public class DemographicMergedDao extends AbstractDao<DemographicMerged> {

	public DemographicMergedDao() {
		super(DemographicMerged.class);
	}
	
	public List<DemographicMerged> findCurrentByMergedTo(int demographicNo) {
		Query q = entityManager.createQuery("select d from DemographicMerged d where d.mergedTo=? and d.deleted=0");
		q.setParameter(1, demographicNo);
		
		@SuppressWarnings("unchecked")
		List<DemographicMerged> results = q.getResultList();
		
		return results;
	}
	
	public List<DemographicMerged> findCurrentByDemographicNo(int demographicNo) {
		Query q = entityManager.createQuery("select d from DemographicMerged d where d.demographicNo=? and d.deleted=0");
		q.setParameter(1, demographicNo);
		
		@SuppressWarnings("unchecked")
		List<DemographicMerged> results = q.getResultList();
		
		return results;
	}
	
	public List<DemographicMerged> findByDemographicNo(int demographicNo) {
		Query q = entityManager.createQuery("select d from DemographicMerged d where d.demographicNo=?");
		q.setParameter(1, demographicNo);
		
		@SuppressWarnings("unchecked")
		List<DemographicMerged> results = q.getResultList();
		
		return results;
	}

	@SuppressWarnings("unchecked")
    public List<DemographicMerged> findByParentAndChildIds(Integer parentId, Integer childId) {
		Query q = createQuery("d", "d.demographicNo = :childId AND d.mergedTo = :parentId");
		q.setParameter("parentId", parentId);
		q.setParameter("childId", childId);
		return q.getResultList();
    }
}
