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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.DemographicSite;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class DemographicSiteDao extends AbstractDao<DemographicSite> {

	public DemographicSiteDao() {
		super(DemographicSite.class);
	}
	

    @SuppressWarnings("unchecked")
	public List<DemographicSite> findDemographicBySiteId(Integer siteId) {
    	Query q = entityManager.createQuery("select x from DemographicSite x where x.siteId=?");
    
    	q.setParameter(1, siteId);
    	List<DemographicSite> results = q.getResultList();
    	return results;
    }
    
    @SuppressWarnings("unchecked")
   	public List<DemographicSite> findSitesByDemographicId(Integer demoId) {
       	Query q = entityManager.createQuery("select x from DemographicSite x where x.demographicId=?");
       
       	q.setParameter(1, demoId);
       	List<DemographicSite> results = q.getResultList();
       	return results;
    }
    
    public void removeSitesByDemographicId(Integer demoId) {
    	
    		Query q = entityManager.createQuery("delete from DemographicSite x where x.demographicId = ?");
    		q.setParameter(1, demoId);
        	q.executeUpdate();
    	
    }
   /* 
    public void remove(DemographicSite ds) {
    	entityManager.remove(entityManager.getReference(DemographicSite.class, ds.getId()));
    }
    */
    
    public List<Integer> findDemoNosBySiteIds(List<Integer> siteIds) {
    	if (siteIds == null || siteIds.size() == 0) {
    		return new ArrayList<Integer>();
    	}
    	Query q = entityManager.createQuery("select demographicId from DemographicSite where siteId in (:siteIds)");
    	q.setParameter("siteIds", siteIds);
    	return q.getResultList();
    }
}
