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
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.DrugProduct;
import org.oscarehr.rx.dispensary.LotBean;
import org.springframework.stereotype.Repository;

@Repository
public class DrugProductDao extends AbstractDao<DrugProduct>{

	public DrugProductDao() {
		super(DrugProduct.class);
	}
	
	
	public List<DrugProduct> findAvailable() {
		Query query = entityManager.createQuery("SELECT x FROM DrugProduct x where x.dispensingEvent is null");
		
		@SuppressWarnings("unchecked")
        List<DrugProduct> results = query.getResultList();
		return results;
	}
	
	public List<DrugProduct> findAvailableByCode(String code) {
		Query query = entityManager.createQuery("SELECT x FROM DrugProduct x where x.code = ?1  and x.dispensingEvent is null");
		query.setParameter(1, code);
		
		@SuppressWarnings("unchecked")
        List<DrugProduct> results = query.getResultList();
		return results;
	}
	
	public List<Object[]> findAllAvailableUnique() {
		Query query = entityManager.createQuery("SELECT distinct x.code, x.name FROM DrugProduct x  where x.dispensingEvent is null order by x.name");
		
		@SuppressWarnings("unchecked")
        	List<Object[]> results = query.getResultList();
		return results;
	}
	
	public List<Object[]> findAllUnique() {
		Query query = entityManager.createQuery("SELECT distinct x.code, x.name FROM DrugProduct x order by x.name");
		
		@SuppressWarnings("unchecked")
	        List<Object[]> results = query.getResultList();
		return results;
	}
	
	public List<String> findUniqueDrugProductNames() {
		Query query = entityManager.createQuery("SELECT distinct x.name FROM DrugProduct x");
		
		@SuppressWarnings("unchecked")
	        List<String> results = query.getResultList();
		return results;
	}
	
	public int getAvailableCount(String lotNumber, Date expiryDate, int amount) {
		Query query = entityManager.createQuery("SELECT count(*)  FROM DrugProduct x  where x.dispensingEvent is null and x.lotNumber = ?1 and x.expiryDate = ?2 and x.amount = ?3");
		query.setParameter(1, lotNumber);
		query.setParameter(2, expiryDate);
		query.setParameter(3, amount);
		
		 Long count = (Long)query.getSingleResult();
		 
		 return count.intValue();
	}
	
	public List<DrugProduct> getAvailableDrugProducts(String lotNumber, Date expiryDate, int amount) {
		Query query = entityManager.createQuery("SELECT x  FROM DrugProduct x  where x.dispensingEvent is null and x.lotNumber = ?1 and x.expiryDate = ?2 and x.amount = ?3");
		query.setParameter(1, lotNumber);
		query.setParameter(2, expiryDate);
		query.setParameter(3, amount);
		
		@SuppressWarnings("unchecked")
		List<DrugProduct> results = query.getResultList();
		
		return results;
	}
	
	public List<LotBean> findDistinctLotsAvailableByCode(String code) {
		List<LotBean> results = new ArrayList<LotBean>();
		Query query = entityManager.createQuery("SELECT distinct x.lotNumber, x.expiryDate, x.amount  FROM DrugProduct x  where x.dispensingEvent is null and x.code = ?1 order by x.lotNumber,x.expiryDate,x.amount");
		query.setParameter(1, code);
		
		@SuppressWarnings("unchecked")
		List<Object[]> tmp = query.getResultList();
		for(Object[] result:tmp) {
			results.add(new LotBean((String)result[0],(Date)result[1],(Integer)result[2],getAvailableCount((String)result[0],(Date)result[1],(Integer)result[2])));
		}
		return results;
	}
	
	public DrugProduct findByCodeAndLotNumber(String code, String lotNumber) {
		Query query = entityManager.createQuery("SELECT x FROM DrugProduct x where x.code=?1 and x.lotNumber = ?2");
		query.setParameter(1, code);
		query.setParameter(2, lotNumber);
		
		return this.getSingleResultOrNull(query);

	}
	
	public List<DrugProduct> findByDispensingId(Integer id) {
		Query query = entityManager.createQuery("SELECT x FROM DrugProduct x where x.dispensingEvent = ?1");
		query.setParameter(1, id);
		
		@SuppressWarnings("unchecked")
        List<DrugProduct> results = query.getResultList();
        return results;

	}
	
	public List<DrugProduct> findByName(int offset, int limit, String name) {
		Query query = entityManager.createQuery("SELECT x FROM DrugProduct x where x.name = ?1");
		query.setParameter(1,name);
		query.setFirstResult(offset);
		query.setMaxResults(limit);
		@SuppressWarnings("unchecked")
        List<DrugProduct> results = query.getResultList();
        return results;

	}
	
	public List<DrugProduct> findAll(int offset, int limit) {
		Query query = entityManager.createQuery("SELECT x FROM DrugProduct x");
		query.setFirstResult(offset);
		query.setMaxResults(limit);
		@SuppressWarnings("unchecked")
        List<DrugProduct> results = query.getResultList();
        return results;

	}
	
	public List<DrugProduct> findByNameAndLot(int offset, int limit, String name, String lotNumber, Integer location, boolean availableOnly) {
		
		String sqlStart = "from DrugProduct x where 1=1";
		String sql = "";
		List<Object> params = new ArrayList<Object>();
		int index = 1;
		
		if(name != null && !"".equals(name)) {
			sql += " and x.name = ?"+index;
			params.add(name);
			index++;
		}
		if(lotNumber != null && !"".equals(lotNumber)) {
			sql += " and x.lotNumber = ?"+index;
			params.add(lotNumber);
			index++;
		}
		
		if(location != null && location.intValue()>0) {
			sql += " and x.location = ?"+index;
			params.add(location);
			index++;
		}
		
		if(availableOnly) {
			sql += " and x.dispensingEvent IS NULL ";
		}
		
		Integer result = null;
		
		Query query = entityManager.createQuery(sqlStart + sql);
		for(int x=0;x<params.size();x++) {
			query.setParameter(x+1, params.get(x));
		}
		query.setFirstResult(offset);
		query.setMaxResults(limit);
		@SuppressWarnings("unchecked")
        List<DrugProduct> results = query.getResultList();
        return results;

	}
	
	public Integer findByNameAndLotCount(String name, String lotNumber, Integer location, boolean availableOnly) {
		
		String sqlStart = "select count(x) from DrugProduct x where 1=1";
		String sql = "";
		List<Object> params = new ArrayList<Object>();
		int index = 1;
		
		if(name != null && !"".equals(name)) {
			sql += " and x.name = ?"+index;
			params.add(name);
			index++;
		}
		if(lotNumber != null && !"".equals(lotNumber)) {
			sql += " and x.lotNumber = ?"+index;
			params.add(lotNumber);
			index++;
		}
		
		if(location != null && location.intValue()>0) {
			sql += " and x.location = ?"+index;
			params.add(location);
			index++;
		}
		
		if(availableOnly) {
			sql += " and x.dispensingEvent IS NULL ";
		}
		
		Integer result = null;
		
		Query query = entityManager.createQuery(sqlStart + sql);
		for(int x=0;x<params.size();x++) {
			query.setParameter(x+1, params.get(x));
		}
		
		result = this.getCountResult(query).intValue();
		
		
		return result;

	}
	
	public List<String> findUniqueDrugProductLotsByName(String productName) {
		Query query = entityManager.createQuery("select distinct(x.lotNumber) from DrugProduct x where x.name = ?1");
		query.setParameter(1,productName);
		
		@SuppressWarnings("unchecked")
        List<String> results = query.getResultList();
        return results;
	}
	
}
