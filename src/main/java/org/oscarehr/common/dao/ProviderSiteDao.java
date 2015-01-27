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

import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.ProviderSite;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("unchecked")
public class ProviderSiteDao extends AbstractDao<ProviderSite>{

	public ProviderSiteDao() {
		super(ProviderSite.class);
	}

	public List<ProviderSite> findByProviderNo(String providerNo) {
    	String sql = "select x from ProviderSite x where x.id.providerNo=?";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,providerNo);

        List<ProviderSite> results = query.getResultList();
        return results;
    }
	
	public List<ProviderSite> findBySiteId(Integer siteId) {
		String sql = "select x from ProviderSite x where x.id.siteId=?";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,siteId);

        @SuppressWarnings("unchecked")
        List<ProviderSite> results = query.getResultList();
        return results;
	}
	
	public List<Provider> findActiveProvidersWithSites(String provider_no) { 
		String sql = "FROM Provider p where p.Status = '1' AND p.OhipNo != '' " +
						"AND EXISTS( " +
						"	FROM ProviderSite s WHERE p.ProviderNo = s.id.providerNo " +
						"	AND s.id.siteId IN ( " +
						"		SELECT ss.id.siteId FROM ProviderSite ss WHERE ss.id.providerNo = :pNo " +
						"	)" +
						")" +
						"ORDER BY p.LastName, p.FirstName";
		Query query = entityManager.createQuery(sql);
		query.setParameter("pNo", provider_no);
		return query.getResultList();
	}

	public List<String> findByProviderNoBySiteName(String siteName) {
	    	String sql = "select x.id.providerNo from ProviderSite x, Site s where x.id.siteId=s.siteId and s.name=?";
    		Query query = entityManager.createQuery(sql);
   	 	query.setParameter(1,siteName);

       		@SuppressWarnings("unchecked")
       	 	List<String> results = query.getResultList();
        	return results;
    }
		
}
