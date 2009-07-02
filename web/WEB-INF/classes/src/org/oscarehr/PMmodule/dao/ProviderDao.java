/*
 * 
 * Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
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
 * <OSCAR TEAM>
 * 
 * This software was written for 
 * Centre for Research on Inner City Health, St. Michael's Hospital, 
 * Toronto, Ontario, Canada 
 */

package org.oscarehr.PMmodule.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.oscarehr.common.model.Provider;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import oscar.OscarProperties;
import oscar.util.SqlUtils;

public class ProviderDao extends HibernateDaoSupport {
	private static Log log = LogFactory.getLog(ProviderDao.class);

	public boolean providerExists(String providerNo) {
		boolean exists = (((Long) getHibernateTemplate().iterate(
				"select count(*) from Provider p where p.ProviderNo = "
						+ providerNo).next()) == 1);
		log.debug("providerExists: " + exists);

		return exists;
	}

	public Provider getProvider(String providerNo) {
		if (providerNo == null || providerNo.length() <= 0) {
			throw new IllegalArgumentException();
		}

		Provider provider = (Provider) getHibernateTemplate().get(Provider.class, providerNo);

		if (log.isDebugEnabled()) {
			log.debug("getProvider: providerNo=" + providerNo + ",found=" + (provider != null));
		}

		return provider;
	}

	public String getProviderName(String providerNo) {
		if (providerNo == null || providerNo.length() <= 0) {
			throw new IllegalArgumentException();
		}

		Provider provider = getProvider(providerNo);
		String providerName = "";

		if (provider != null && provider.getFirstName() != null) {
			providerName = provider.getFirstName() + " ";
		}

		if (provider != null && provider.getLastName() != null) {
			providerName += provider.getLastName();
		}

		if (log.isDebugEnabled()) {
			log.debug("getProviderName: providerNo=" + providerNo + ",result=" + providerName);
		}

		return providerName;
	}

	public List<Provider> getProviders() {
		@SuppressWarnings("unchecked")
		List<Provider> rs = getHibernateTemplate().find(
				"FROM  Provider p ORDER BY p.LastName");

		if (log.isDebugEnabled()) {
			log.debug("getProviders: # of results=" + rs.size());
		}
		return rs;
	}

    public List getActiveProviders(Integer programId) {
        ArrayList paramList = new ArrayList();

    	String sSQL="FROM  SecProvider p where p.status='1' and p.providerNo in " +
    	"(select sr.providerNo from secUserRole sr, LstOrgcd o " +
    	" where o.code = 'P' || ? " +
    	" and o.codecsv  like '%' || sr.orgcd || ',%' " +
    	" and not (sr.orgcd like 'R%' or sr.orgcd like 'O%'))" +
    	" ORDER BY p.lastName";
    		 	
    	paramList.add(programId);
    	Object params[] = paramList.toArray(new Object[paramList.size()]);

    	return  getHibernateTemplate().find(sSQL ,params);
	}

	public List<Provider> getActiveProviders(String facilityId, String programId) {
		ArrayList paramList = new ArrayList();

		String sSQL;
		List<Provider> rs;
		if (programId != null && "0".equals(programId) == false) {
			sSQL = "FROM  Provider p where p.Status='1' and p.ProviderNo in "
					+ "(select c.ProviderNo from ProgramProvider c where c.ProgramId =?) ORDER BY p.LastName";
			paramList.add(Long.valueOf(programId));
			Object params[] = paramList.toArray(new Object[paramList.size()]);
			rs = getHibernateTemplate().find(sSQL, params);
		} else if (facilityId != null && "0".equals(facilityId) == false) {
			sSQL = "FROM  Provider p where p.Status='1' and p.ProviderNo in "
					+ "(select c.ProviderNo from ProgramProvider c where c.ProgramId in "
					+ "(select a.id from Program a where a.facilityId=?)) ORDER BY p.LastName";
			// JS 2192700 - string facilityId seems to be throwing class cast
			// exception
			Integer intFacilityId = Integer.valueOf(facilityId);
			paramList.add(intFacilityId);
			Object params[] = paramList.toArray(new Object[paramList.size()]);
			rs = getHibernateTemplate().find(sSQL, params);
		} else {
			sSQL = "FROM  Provider p where p.Status='1' ORDER BY p.LastName";
			rs = getHibernateTemplate().find(sSQL);
		}
		// List<Provider> rs =
		// getHibernateTemplate().find("FROM  Provider p ORDER BY p.LastName");

		return rs;
	}

	public List<Provider> getActiveProviders() {
		@SuppressWarnings("unchecked")
		List<Provider> rs = getHibernateTemplate().find(
				"FROM  Provider p where p.Status='1' ORDER BY p.LastName");

		if (log.isDebugEnabled()) {
			log.debug("getProviders: # of results=" + rs.size());
		}
		return rs;
	}

    public List getActiveProviders(String providerNo, Integer shelterId) {
    	//@SuppressWarnings("unchecked")
    	String sql;
    	if (shelterId == null || shelterId.intValue() == 0)
    		sql = "FROM  Provider p where p.Status='1'" +
    				" and p.ProviderNo in (select sr.providerNo from Secuserrole sr " +
    				" where sr.orgcd in (select o.code from LstOrgcd o, Secuserrole srb " +
    				" where o.codecsv  like '%' || srb.orgcd || ',%' and srb.providerNo =?))" + 
    				" ORDER BY p.LastName";
    	else
    		sql = "FROM  Provider p where p.Status='1'" +
			" and p.ProviderNo in (select sr.providerNo from Secuserrole sr " +
			" where sr.orgcd in (select o.code from LstOrgcd o, Secuserrole srb " +
			" where o.codecsv like '%S" + shelterId.toString()+ ",%' and o.codecsv like '%' || srb.orgcd || ',%' and srb.providerNo =?))" + 
			" ORDER BY p.LastName";
    	
    	ArrayList paramList = new ArrayList();
    	paramList.add(providerNo);

    	Object params[] = paramList.toArray(new Object[paramList.size()]);
    	
    	List rs = getHibernateTemplate().find(sql,params);

		if (log.isDebugEnabled()) {
			log.debug("getProviders: # of results=" + rs.size());
		}
		return rs;
	}

	public List<Provider> search(String name) {
		boolean isOracle = OscarProperties.getInstance().getDbType().equals(
				"oracle");
		Criteria c = this.getSession().createCriteria(Provider.class);
		if (isOracle) {
			c.add(Restrictions.or(Expression.ilike("FirstName", name + "%"),
					Expression.ilike("LastName", name + "%")));
		} else {
			c.add(Restrictions.or(Expression.like("FirstName", name + "%"),
					Expression.like("LastName", name + "%")));
		}
		c.addOrder(Order.asc("ProviderNo"));

		@SuppressWarnings("unchecked")
		List<Provider> results = c.list();

		if (log.isDebugEnabled()) {
			log.debug("search: # of results=" + results.size());
		}
		return results;
	}

	public List<Provider> getProvidersByType(String type) {
		@SuppressWarnings("unchecked")
		List<Provider> results = this.getHibernateTemplate().find(
				"from Provider p where p.ProviderType = ?", type);

		if (log.isDebugEnabled()) {
			log.debug("getProvidersByType: type=" + type + ",# of results="
					+ results.size());
		}

		return results;
	}
	
	public List getShelterIds(String provider_no)
	{
		/*
		String sql = "select distinct substr(codetree,18,7) as shelter_id from lst_orgcd" ;
		sql += " where code in (select orgcd from secuserrole where provider_no=?)";
		sql += " and fullcode like '%S%'";
		*/
		String sql ="select distinct c.id as shelter_id from lst_shelter c, lst_orgcd a, secUserRole b  where instr('RO',substr(b.orgcd,1,1)) = 0 and a.codecsv like '%' || b.orgcd || ',%'" + 
				" and b.provider_no=? and a.codecsv like '%S' || c.id  || ',%'";
	
		Query query = getSession().createSQLQuery(sql);
    	((SQLQuery) query).addScalar("shelter_id", Hibernate.INTEGER); 
    	query.setString(0, provider_no);
        List lst=query.list();
        return lst;

	}
	
	public List<Provider> getActiveProvidersByType(String type) {
		@SuppressWarnings("unchecked")
		List<Provider> results = this.getHibernateTemplate().find(
				"from Provider p where p.Status='1' and p.ProviderType = ?",
				type);

		return results;
	}

	public static void addProviderToFacility(String provider_no, int facilityId) {
		try {
			SqlUtils.update("insert into provider_facility values ('"
					+ provider_no + "'," + facilityId + ')');
		} catch (RuntimeException e) {
			// chances are it's a duplicate unique entry exception so it's safe
			// to ignore.
			// this is still unexpected because duplicate calls shouldn't be
			// made
			log.warn("Unexpected exception occurred.", e);
		}
	}

	public static void removeProviderFromFacility(String provider_no,
			int facilityId) {
		SqlUtils.update("delete from provider_facility where provider_no='"
				+ provider_no + "' and facility_id=" + facilityId);
	}

	public static List<Integer> getFacilityIds(String provider_no) {
		return (SqlUtils
				.selectIntList("select facility_id from provider_facility,Facility where Facility.id=provider_facility.facility_id and Facility.disabled=0 and provider_no='"
						+ provider_no + '\''));
	}

	public static List<String> getProviderIds(int facilityId) {
		return (SqlUtils
				.selectStringList("select provider_no from provider_facility where facility_id="
						+ facilityId));
	}

    public void updateProvider( Provider provider) {        
        this.getHibernateTemplate().update(provider);
    }
}
