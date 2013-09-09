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

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.ProviderData;
import org.springframework.stereotype.Repository;

import oscar.util.ConversionUtils;

@Repository
public class ProviderDataDao extends AbstractDao<ProviderData> {

	public ProviderDataDao() {
		super(ProviderData.class);
	}
	
	

	@SuppressWarnings("unchecked")
	public ProviderData findByOhipNumber(String ohipNumber) {
		Query query;
		List<ProviderData> results;
		String sqlCommand = "SELECT x FROM ProviderData x WHERE x.ohipNo=?";
		
		query = this.entityManager.createQuery(sqlCommand);
		query.setParameter(1, ohipNumber);
		
		results = query.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		// If we get here, there were no results
		return null;
	}
	
    public ProviderData findByProviderNo(String providerNo) {

    	String sqlCommand = "select x from ProviderData x where x.id=?1";

        Query query = entityManager.createQuery(sqlCommand);
        query.setParameter(1, providerNo);

        @SuppressWarnings("unchecked")
        List<ProviderData> results = query.getResultList();

        if (results.size()>0) return results.get(0);
        return null;
    }

    public List<ProviderData> findAllOrderByLastName() {

    	String sqlCommand = "select x from ProviderData x order by x.lastName";

        Query query = entityManager.createQuery(sqlCommand);

        @SuppressWarnings("unchecked")
        List<ProviderData> results = query.getResultList();

        return results;
    }

	public Integer getLastId() {
		Query query = entityManager.createQuery("SELECT p.id FROM ProviderData p ORDER BY CAST(p.id AS integer) ASC");
		query.setMaxResults(1);
		String result = (String ) query.getSingleResult();
		if (result == null)
			return 0;
		return ConversionUtils.fromIntString(result);
	}
}
