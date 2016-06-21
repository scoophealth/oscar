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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Query;

import org.oscarehr.common.model.ProviderData;
import org.springframework.stereotype.Repository;

import oscar.util.ConversionUtils;

@Repository
public class ProviderDataDao extends AbstractDao<ProviderData> {

	private static final String ACTIVE_WHERE_CLAUSE = " p.status = '1'";

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

		if (results.size() > 0) return results.get(0);
		return null;
	}

	public List<ProviderData> findByProviderNo(String providerNo, String status, int limit, int offset) {

		String sqlCommand = "From ProviderData p where p.id like ?";

		if(status != null) 
			sqlCommand += " and p.status = :status ";

		Query query = entityManager.createQuery(sqlCommand);
		query.setFirstResult(offset);
		query.setMaxResults(limit);
		query.setParameter(1, providerNo + "%");
		if(status != null)
			query.setParameter("status", status);

		@SuppressWarnings("unchecked")
		List<ProviderData> results = query.getResultList();

		return results;
	}

	public  List<ProviderData> findByProviderName(String searchStr, String status, int limit, int offset) {
		
		String queryString = "From ProviderData p where p.lastName like :lastName ";
		

		String[] name = searchStr.split(",");
		if(name.length==2)
			queryString += " and p.firstName like :firstName ";

		if(status != null) 
			queryString += " and p.status = :status ";
		
		Query query = entityManager.createQuery(queryString);
		query.setFirstResult(offset);
		query.setMaxResults(limit);
		
		query.setParameter("lastName", name[0].trim() + "%");
		if(name.length==2)
			query.setParameter("firstName", name[1].trim() + "%");
		if(status != null)
			query.setParameter("status", status);
		
		List list = query.getResultList();
		return list;
	}
	
	
	public List<ProviderData> findAllOrderByLastName() {

		String sqlCommand = "select x from ProviderData x order by x.lastName";

		Query query = entityManager.createQuery(sqlCommand);

		@SuppressWarnings("unchecked")
		List<ProviderData> results = query.getResultList();

		return results;
	}
	
    public List<ProviderData> findByProviderSite(String providerNo) {
    	
		String queryStr = "select * from provider p inner join providersite s on s.provider_no = p.provider_no " 
				 + " where s.site_id in (select site_id from providersite where provider_no=?)";
			

		Query query = entityManager.createNativeQuery(queryStr, modelClass);
        query.setParameter(1, providerNo);

    	@SuppressWarnings("unchecked")
        List<ProviderData> proList = query.getResultList();
    	
    	return proList;
    }

    public List<Object[]> findProviderSecUserRoles(String lastName, String firstName) {
    	
		String queryStr = "select u.id, u.role_name, p.provider_no, p.first_name, p.last_name from provider p LEFT JOIN secUserRole u ON  p.provider_no=u.provider_no " 
				+ " where p.last_name like '" + lastName + "' and p.first_name like '" + firstName + "' and p.status='1' order by p.first_name, p.last_name, u.role_name";

		Query query = entityManager.createNativeQuery(queryStr);

    	@SuppressWarnings("unchecked")
        List<Object[]> proList = query.getResultList();
    	
    	return proList;
    }

    public List<ProviderData> findByProviderTeam(String providerNo) {
    	
		String queryStr = "select * from provider p  " +
				"where team in (select team from provider where team is not null and team <> '' and provider_no=?)";

		Query query = entityManager.createNativeQuery(queryStr, modelClass);
        query.setParameter(1, providerNo);

    	@SuppressWarnings("unchecked")
        List<ProviderData> proList = query.getResultList();
    	
    	return proList;
    }
    
    public List<ProviderData> findAllBilling(String active) {
        Query query = createQuery("p", "p.ohipNo is not null and p.ohipNo != '' and p.status = :active order by p.lastName");
        query.setParameter("active", active);
        return query.getResultList();
    }
    
    

	/**
	 * Finds all providers for the specified type and insurance no, ordered by last name.
	 * 
	 * @param providerType
	 * 		Provider type (doctor, nurse, etc.)
	 * @param insuranceNo
	 * 		Provider's insurance number 
	 * @return
	 * 		Returns all matching providers
	 */
	@SuppressWarnings("unchecked")
	public List<ProviderData> findByTypeAndOhip(String providerType, String insuranceNo) {
		Query query = createQuery("p", "p.providerType = :pt and p.ohipNo like :in order by p.lastName");
		query.setParameter("pt", providerType);
		query.setParameter("in", insuranceNo);
		return query.getResultList();
	}

	/**
	 * Finds all providers with the specified provider type 
	 * 
	 * @param providerType
	 * 		Provider type to be found
	 * @return
	 * 		Returns all the active matching providers.
	 */
	@SuppressWarnings("unchecked")
	public List<ProviderData> findByType(String providerType) {
		Query query = createQuery("p", "p.providerType = :pt and p.status = '1' order by p.lastName, p.firstName");
		query.setParameter("pt", providerType);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<ProviderData> findByName(String firstName, String lastName, boolean onlyActive) {
		StringBuilder buf = createQueryString("p", "");
		boolean isAppended = false;
		Map<String, Object> params = new HashMap<String, Object>();
		if (firstName != null && !firstName.trim().equals("")) {
			buf.append("WHERE p.firstName like :fn");
			params.put("fn", firstName + "%");
			isAppended = true;
		}

		if (lastName != null && !lastName.trim().equals("")) {
			if (isAppended) {
				buf.append(" AND");
			} else {
				buf.append(" WHERE ");
			}
			buf.append(" p.lastName like :ln");
			params.put("ln", lastName + "%");
			isAppended = true;
		}

		if (onlyActive) {
			if (isAppended) {
				buf.append(" AND");
			} else {
				buf.append(" WHERE ");
			}
			buf.append(ACTIVE_WHERE_CLAUSE);
		}

		buf.append(" ORDER BY p.lastName, p.firstName");

		Query query = entityManager.createQuery(buf.toString());
		for (Entry<String, Object> param : params.entrySet()) {
			query.setParameter(param.getKey(), param.getValue());
		}

		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<ProviderData> findAll() {
		Query query = entityManager.createQuery("FROM " + modelClass.getSimpleName());
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<ProviderData> findAll(boolean inactive) {
		if (inactive) return findAll();
		Query query = createQuery("p", ACTIVE_WHERE_CLAUSE);
		return query.getResultList();
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
