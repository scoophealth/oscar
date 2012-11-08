/**
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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
 */

package org.oscarehr.common.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Query;

import org.oscarehr.common.model.Billing;
import org.springframework.stereotype.Repository;

import oscar.util.ConversionUtils;

@Repository
public class BillingDao extends AbstractDao<Billing> {

	public BillingDao() {
		super(Billing.class);
	}
	
	public List<Billing> findActive(int billingNo) {
		Query q = entityManager.createQuery("select x from Billing x where x.id=? and x.status <> ?");
		q.setParameter(1, billingNo);
		q.setParameter(2, "D");
		
		@SuppressWarnings("unchecked")
		List<Billing> results = q.getResultList();
		
		return results;
	}
	
	public List<Billing> findByBillingType(String type) {
		Query q = entityManager.createQuery("select x from Billing x where x.billingType=?");
		q.setParameter(1, type);
		
		@SuppressWarnings("unchecked")
		List<Billing> results = q.getResultList();
		
		return results;
	}
	
	public List<Billing> findByAppointmentNo(int apptNo) {
		Query q = entityManager.createQuery("select x from Billing x where x.appointmentNo=?");
		q.setParameter(1, apptNo);
		
		@SuppressWarnings("unchecked")
		List<Billing> results = q.getResultList();
		
		return results;
	}

	public List<Billing> findSet(List<String> list) { 
		Query query = entityManager.createQuery("SELECT b FROM  Billing b  where b.billingmasterNo in (:billingNumbers)");
		query.setParameter("billingNumbers", ConversionUtils.toIntList(list));
		
		@SuppressWarnings("unchecked")
		List<Billing> results = query.getResultList();
		
		return results;
	}
	
	@SuppressWarnings("unchecked")
    public List<Object[]> findBillings(Integer demoNo, List<String> serviceCodes) {    	
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder serviceCodeValues = new StringBuilder();
		for (int i = 0; i < serviceCodes.size(); i++) {
			if (serviceCodeValues.length() != 0) {
				serviceCodeValues.append(" OR ");
			}

			String param = "serviceCode" + (i + 1);
			serviceCodeValues.append("bd.serviceCode = :").append(param);
			params.put(param, serviceCodes.get(i));
		}

		StringBuilder buf = new StringBuilder("FROM Billing b, BillingDetail bd where b.demographicNo = :demoNo and bd.billingNo = b.id");
		params.put("demoNo", demoNo);

		if (serviceCodeValues.length() != 0) {
			buf.append(" AND ( ").append(serviceCodeValues).append(" ) ");
		}

		buf.append(" AND bd.status != :deletedFlag and b.status != :deletedFlag order by b.billingDate desc");
		params.put("deletedFlag", "D");
		
		Query query = entityManager.createQuery(buf.toString());
		query.setMaxResults(1);

		for (Entry<String, Object> e : params.entrySet()) {
			query.setParameter(e.getKey(), e.getValue());
		}		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
    public List<Billing> findBillings(Integer demoNo, String statusType, String providerNo, Date startDate, Date endDate) {
		String providerQuery = "", startDateQuery = "", endDateQuery = "", demoQuery = "";
		
		Map<String, Object> params = new HashMap<String, Object>();
		if (providerNo != null && !providerNo.trim().equalsIgnoreCase("all")) {
			providerQuery = " and b.apptProviderNo = :providerNo";
			
			params.put("providerNo", providerNo);
		}

		if (startDate != null) {
			startDateQuery = " and ( b.billingDate >= :startDate ) ";
			params.put("startDate", startDate);
		}

		if (endDate != null) {
			endDateQuery = " and ( b.billingDate <= :endDate) ";
			params.put("endDate", endDate);
		}
		
		if (demoNo != null) {
			demoQuery = " and b.demographicNo = :demoNo";
			params.put("demoNo", demoNo);
		}

		String queryString = "FROM Billing b WHERE b.status like :status "
				+ providerQuery				+ startDateQuery
				+ endDateQuery
				+ demoQuery;
		params.put("status", statusType);
		
		Query query = entityManager.createQuery(queryString);
		for(Entry<String, Object> param : params.entrySet()) {
			query.setParameter(param.getKey(), param.getValue());
		}
		
		return query.getResultList();
    }

}
