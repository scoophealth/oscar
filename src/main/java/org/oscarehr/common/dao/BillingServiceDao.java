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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.BillingService;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;

import oscar.util.UtilDateUtilities;

@Repository
public class BillingServiceDao extends AbstractDao<BillingService> {
	static public final String BC = "BC";

	public BillingServiceDao() {
		super(BillingService.class);
	}

	public boolean codeRequiresSLI(String code) {
		Query query = entityManager.createQuery("select bs  from BillingService bs where bs.serviceCode like (:code) and sliFlag = TRUE");
		query.setParameter("code", code + "%");

		@SuppressWarnings("unchecked")
		List<BillingService> list = query.getResultList();
		return list.size() > 0;
	}

	public List<BillingService> findBillingCodesByCode(String code, String region) {
		Query query = entityManager.createQuery("select bs  from BillingService bs where bs.serviceCode like (:code) and region = (:region) order by bs.billingserviceDate");
		query.setParameter("code", code + "%");
		query.setParameter("region", region);

		@SuppressWarnings("unchecked")
		List<BillingService> list = query.getResultList();
		return list;
	}

	public List<BillingService> findByServiceCode(String code) {
		Query query = entityManager.createQuery("select bs  from BillingService bs where bs.serviceCode = ? order by bs.billingserviceDate desc");
		query.setParameter(1, code);

		@SuppressWarnings("unchecked")
		List<BillingService> list = query.getResultList();
		return list;
	}

	public List<BillingService> findByServiceCodeAndDate(String code, Date date) {
		Query query = entityManager.createQuery("select bs  from BillingService bs where bs.serviceCode = ? and bs.billingserviceDate = ? order by bs.billingserviceDate desc");
		query.setParameter(1, code);
		query.setParameter(2, date);

		
		@SuppressWarnings("unchecked")
        List<BillingService> list = query.getResultList();
		return list;
	}
	
	public List<BillingService> findBillingCodesByCode(String code, String region, int order) {
		return findBillingCodesByCode(code, region, new Date(), order);
	}

	public List<BillingService> findBillingCodesByCode(String code, String region, Date billingDate, int order) {
		String orderByClause = order == 1 ? "desc" : "";
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

	public String searchDescBillingCode(String code, String region) {
		
		Query query = entityManager.createQuery("select bs  from BillingService bs where bs.description <> '' AND bs.description <> '----' AND bs.region = (:region) and bs.serviceCode like (:code) and bs.billingserviceDate = (select max(b2.billingserviceDate) from BillingService b2 where b2.serviceCode = bs.serviceCode and b2.billingserviceDate <= (:billDate))  order by bs.billingserviceDate desc");
		query.setParameter("region", region);
		query.setParameter("code", code + "%");
		query.setParameter("billDate", Calendar.getInstance().getTime());

		@SuppressWarnings("unchecked")
		List<BillingService> list = query.getResultList();
		if (list.size() == 0) { return "----"; }
		return list.get(0).getDescription();
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

	public Date getLatestServiceDate(Date endDate, String serviceCode) {
		String sql = "select max(bs.billingserviceDate) from BillingService bs where bs.billingserviceDate <= ? and bs.serviceCode = ?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, endDate);
		query.setParameter(2, serviceCode);
		Date date = (Date)query.getSingleResult();
		return date;
	}

	public Object[] getUnitPrice(String bcode, String referralDate) {
		String sql = "select bs from BillingService bs where bs.serviceCode = ? and bs.billingserviceDate = ?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1,bcode);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date = sdf.parse(referralDate);
		}catch(ParseException e) {
			MiscUtils.getLogger().error("error",e);
		}
		query.setParameter(2, getLatestServiceDate(date,bcode));

		@SuppressWarnings("unchecked")
		List<BillingService> results = query.getResultList();

		if (results.size() > 0) {
			BillingService bs = results.get(0);
			return new Object[] {bs.getValue(),bs.getGstFlag()};
		} else
			return null;
	}

	public String getUnitPercentage(String bcode, String referralDate) {
		String sql = "select bs from BillingService bs where bs.serviceCode = ? and bs.billingserviceDate = ?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1,bcode);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date = sdf.parse(referralDate);
		}catch(ParseException e) {
			MiscUtils.getLogger().error("error",e);
		}
		query.setParameter(2, getLatestServiceDate(date,bcode));

		@SuppressWarnings("unchecked")
		List<BillingService> results = query.getResultList();

		if(results.size()>0) {
			return results.get(0).getPercentage();
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
    public List<BillingService> findBillingCodesByFontStyle(Integer styleId) {
		String sql = "select bs from BillingService bs where bs.displayStyle = ?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, styleId);

		return query.getResultList();
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
