/**
 * Copyright (c) 2007-2008. CAISI, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * This software was written for 
 * CAISI, 
 * Toronto, Ontario, Canada 
 */

package org.oscarehr.common.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.BillingService;
import org.springframework.stereotype.Repository;

import oscar.util.UtilDateUtilities;

@Repository
public class BillingServiceDao extends AbstractDao<BillingService> {
	static public final String BC = "BC";

	public BillingServiceDao() {
		super(BillingService.class);
	}
	
	public List<BillingService> findBillingCodesByCode(String code, String region) {
		Query query = entityManager.createQuery("select bs  from BillingService bs where bs.serviceCode like (:code) and region = (:region) order by bs.billingserviceDate");
		query.setParameter("code", code + "%");
		query.setParameter("region", region);

		@SuppressWarnings("unchecked")
		List<BillingService> list = query.getResultList();
		return list;
	}

	public List<BillingService> findBillingCodesByCode(String code, String region, int order) {
		return findBillingCodesByCode(code, region, new Date(), order);
	}

	public List<BillingService> findBillingCodesByCode(String code, String region, Date billingDate, int order) {
		String orderByClause = order == 1 ? "desc" : "";
		System.out.println("Order by " + orderByClause + " code " + code);
		Query query = entityManager
		        .createQuery("select bs  from BillingService bs where bs.region = (:region) and bs.serviceCode like (:code) and bs.billingserviceDate = (select max(b2.billingserviceDate) from BillingService b2 where b2.serviceCode = bs.serviceCode and b2.billingserviceDate <= (:billDate))  order by bs.serviceCode "
		                + orderByClause);// (:order) ");
		query.setParameter("region", region);
		query.setParameter("code", code + "%");
		query.setParameter("billDate", billingDate);
		// query.setParameter("order", orderByClause);

		@SuppressWarnings("unchecked")
		List<BillingService> list = query.getResultList();

		return list;
	}

	public List<BillingService> search(String str, String region,Date billingDate) {

		Query query = entityManager.createQuery("select bs from BillingService bs where bs.region = (:region) and (bs.serviceCode like (:searchString) or bs.description like (:searchString)) and bs.billingserviceDate = (select max(b2.billingserviceDate) from BillingService b2 where b2.serviceCode = bs.serviceCode and b2.billingserviceDate <= (:billDate))");
		query.setParameter("region", region);
		query.setParameter("searchString", str);
                query.setParameter("billDate", billingDate);
		// String sql = "select * from billingservice where service_code like '"+str+"' or description like '%"+str+"%' ";

		@SuppressWarnings("unchecked")
		List<BillingService> list = query.getResultList();
		return list;
	}

	public BillingService searchBillingCode(String str, String region) {
		return searchBillingCode(str, region, new Date());
	}

	public BillingService searchBillingCode(String str, String region, Date billingDate) {
		Query query = entityManager.createQuery("select bs from BillingService bs where bs.region = (:region) and bs.serviceCode like (:searchStr) and bs.billingserviceDate = (select max(b2.billingserviceDate) from BillingService b2 where b2.serviceCode = bs.serviceCode and b2.billingserviceDate <= (:billDate))");

		query.setParameter("region", region);
		query.setParameter("searchStr", str + "%");
		query.setParameter("billDate", billingDate);

		@SuppressWarnings("unchecked")
		List<BillingService> list = query.getResultList();
		if (list == null || list.size() < 1) {
			return null;
		}

		return list.get(list.size() - 1);
	}

	public boolean editBillingCodeDesc(String desc, String val, String codeId) {
		boolean retval = true;
		BillingService billingservice = find(Integer.parseInt(codeId));
		billingservice.setValue(val);
		billingservice.setDescription(desc);
		merge(billingservice);
		return retval;
	}

	public boolean editBillingCode(String val, String codeId) {
		boolean retval = true;
		BillingService billingservice = find(Integer.parseInt(codeId));
		billingservice.setValue(val);
		merge(billingservice);
		return retval;
	}

	public boolean insertBillingCode(String code, String date, String description, String termDate, String region) {
		boolean retval = true;
		BillingService bs = new BillingService();
		bs.setServiceCode(code);
		bs.setDescription(description);
		bs.setTerminationDate(UtilDateUtilities.StringToDate(termDate));
		bs.setBillingserviceDate(UtilDateUtilities.StringToDate(date));
		bs.setRegion(region);
		entityManager.persist(bs);
		return retval;
	}

	// /
	// public int searchNumBillingCode(String str){
	// String sql =
	// "select count(*) as coun from billingservice b where b.service_code like '"+str+"%' and b.billingservice_date = (select max(b2.billingservice_date) from billingservice b2 where b2.service_code = b.service_code and b2.billingservice_date <= now())";
	// Query query = entityManager.createNativeQuery(sql);
	// List<BillingService> list = query.getResultList();
	// return list.size();
	//
	// }

}
