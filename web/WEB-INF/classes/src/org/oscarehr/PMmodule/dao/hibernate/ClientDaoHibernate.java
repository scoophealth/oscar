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

package org.oscarehr.PMmodule.dao.hibernate;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.oscarehr.PMmodule.dao.ClientDao;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.DemographicExt;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.model.Provider;
import org.oscarehr.PMmodule.web.formbean.ClientSearchFormBean;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ClientDaoHibernate extends HibernateDaoSupport implements ClientDao {

	private Log log = LogFactory.getLog(ClientDaoHibernate.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.oscarehr.PMmodule.dao.ClientDao#exists(java.lang.Integer)
	 */
	public boolean clientExists(Integer demographicNo) {
		boolean exists = getHibernateTemplate().get(Demographic.class, demographicNo) != null;
		log.debug("exists: " + exists);
		
		return exists;
	}

	public Demographic getClientByDemographicNo(Integer demographicNo) {
		if (demographicNo == null || demographicNo.intValue() <= 0) {
			throw new IllegalArgumentException();
		}

		Demographic result = (Demographic) getHibernateTemplate().get(Demographic.class, demographicNo);

		if (log.isDebugEnabled()) {
			log.debug("getClientByDemographicNo: id=" + demographicNo + ", found=" + (result != null));
		}

		return result;
	}

	public List getClients() {
		String queryStr = " FROM Demographic";
		List rs = getHibernateTemplate().find(queryStr);

		if (log.isDebugEnabled()) {
			log.debug("getClients: # of results=" + rs.size());
		}

		return rs;
	}

	/*
	 * use program_client table to do domain based search
	 */
	public List search(ClientSearchFormBean bean, boolean returnOptinsOnly) {
		Criteria criteria = getSession().createCriteria(Demographic.class);
		String firstName = "";
		String lastName = "";

		if (bean.getFirstName() != null && bean.getFirstName().length() > 0) {
			firstName = bean.getFirstName();
			firstName = StringEscapeUtils.escapeSql(firstName);
		}
		if (bean.getLastName() != null && bean.getLastName().length() > 0) {
			lastName = bean.getLastName();
			lastName = StringEscapeUtils.escapeSql(lastName);
		}

		if (!bean.isSearchUsingSoundex()) {
			if (firstName.length() > 0) {
				criteria.add(Expression.like("FirstName", firstName + "%"));
			}
			if (lastName.length() > 0) {
				criteria.add(Expression.like("LastName", lastName + "%"));
			}
		} else { // soundex variation
			String sql;
			if (firstName.length() > 0) {
				sql = "((LEFT(SOUNDEX(first_name),4) = LEFT(SOUNDEX('" + firstName + "'),4))" + " OR (first_name like '" + firstName + "%'))";
				criteria.add(Restrictions.sqlRestriction(sql));
			}
			if (lastName.length() > 0) {
				sql = "((LEFT(SOUNDEX(last_name),4) = LEFT(SOUNDEX('" + lastName + "'),4))" + " OR (last_name like '" + lastName + "%'))";
				criteria.add(Restrictions.sqlRestriction(sql));
			}
		}

		if (bean.getDob() != null && bean.getDob().length() > 0) {
			criteria.add(Expression.eq("YearOfBirth", bean.getYearOfBirth()));
			criteria.add(Expression.eq("MonthOfBirth", bean.getMonthOfBirth()));
			criteria.add(Expression.eq("DateOfBirth", bean.getDayOfBirth()));
		}

		if (bean.getHealthCardNumber() != null && bean.getHealthCardNumber().length() > 0) {
			criteria.add(Expression.eq("Hin", bean.getHealthCardNumber()));
		}

		if (bean.getHealthCardVersion() != null && bean.getHealthCardVersion().length() > 0) {
			criteria.add(Expression.eq("Ver", bean.getHealthCardVersion()));
		}
		if (bean.getProgramDomain() != null && !bean.getProgramDomain().isEmpty() && !bean.isSearchOutsideDomain()) {
			// program domain search
			StringBuffer programIds = new StringBuffer();

			for (int x = 0; x < bean.getProgramDomain().size(); x++) {
				ProgramProvider p = (ProgramProvider) bean.getProgramDomain().get(x);
				if (x > 0) {
					programIds.append(",");
				}
				programIds.append(p.getProgramId());
			}
			String sql = "{alias}.demographic_no in (select client_id from admission where program_id in (" + programIds.toString() + "))";
			criteria.add(Restrictions.sqlRestriction(sql));
		}

		if (bean.getProgramDomain() != null && bean.getProgramDomain().isEmpty() && !bean.isSearchOutsideDomain()) {
			String sql = "{alias}.demographic_no = 0";
			criteria.add(Restrictions.sqlRestriction(sql));
		}
		
		criteria.add(Expression.ne("PatientStatus", "IN"));
		
		criteria.addOrder(Order.asc("LastName"));
		List results = criteria.list();

// TODO : determine the best way to filter results here
// options are post sql filtering or joining on the sql query
				
		if (log.isDebugEnabled()) {
			log.debug("search: # of results=" + results.size());
		}

		return results;
	}

	public Date getMostRecentIntakeADate(Integer demographicNo) {
		Date date = null;

		if (demographicNo == null || demographicNo.intValue() <= 0) {
			throw new IllegalArgumentException();
		}

		Query q = getSession().createQuery("select intake.FormEdited from Formintakea intake where intake.DemographicNo = ? order by intake.FormEdited DESC");
		q.setLong(0, demographicNo.longValue());
		List results = q.list();

		if (!results.isEmpty()) {
			date = (Date) results.get(0);
		}

		if (log.isDebugEnabled()) {
			log.debug("getMostRecentIntakeADate: demographicNo=" + demographicNo + ",found=" + (date != null));
		}

		return date;
	}

	public Date getMostRecentIntakeCDate(Integer demographicNo) {
		Date date = null;

		if (demographicNo == null || demographicNo.intValue() <= 0) {
			throw new IllegalArgumentException();
		}

		Query q = getSession().createQuery("select intake.FormEdited from Formintakec intake where intake.DemographicNo = ? order by intake.FormEdited DESC");
		q.setLong(0, demographicNo.longValue());

		List result = q.list();
		if (!result.isEmpty()) {
			date = (Date) result.get(0);
		}

		if (log.isDebugEnabled()) {
			log.debug("getMostRecentIntakeCDate: demographicNo=" + demographicNo + ",found=" + (date != null));
		}

		return date;
	}

	public void saveClient(Demographic client) {
		if (client == null) {
			throw new IllegalArgumentException();
		}

		if (client.getYearOfBirth() != null && client.getYearOfBirth().equals("")) {
			client.setYearOfBirth("0001");
		}

		if (client.getMonthOfBirth() != null && client.getMonthOfBirth().equals("")) {
			client.setMonthOfBirth("01");
		}

		if (client.getDateOfBirth() != null && client.getDateOfBirth().equals("")) {
			client.setDateOfBirth("01");
		}

		this.getHibernateTemplate().saveOrUpdate(client);

		if (log.isDebugEnabled()) {
			log.debug("saveClient: id=" + client.getDemographicNo());
		}
	}

	public String getMostRecentIntakeAProvider(Integer demographicNo) {
		String providerName = null;

		if (demographicNo == null || demographicNo.intValue() <= 0) {
			throw new IllegalArgumentException();
		}

		Query q = getSession().createQuery("select intake.ProviderNo from Formintakea intake where intake.DemographicNo = ? order by intake.FormEdited DESC");
		q.setLong(0, demographicNo.longValue());
		List result = q.list();
		if (!result.isEmpty()) {
			Long providerNo = (Long) result.get(0);
			Provider provider = (Provider) this.getHibernateTemplate().get(Provider.class, String.valueOf(providerNo));
			if (provider != null) {
				providerName = provider.getFormattedName();
			}

		}

		if (log.isDebugEnabled()) {
			log.debug("getMostRecentIntakeAProvider: demographicNo=" + demographicNo + ",result=" + providerName);
		}

		return providerName;
	}

	public String getMostRecentIntakeCProvider(Integer demographicNo) {
		String providerName = null;

		if (demographicNo == null || demographicNo.intValue() <= 0) {
			throw new IllegalArgumentException();
		}

		Query q = getSession().createQuery("select intake.ProviderNo from Formintakec intake where intake.DemographicNo = ? order by intake.FormEdited DESC");
		q.setLong(0, demographicNo.longValue());
		List result = q.list();
		if (!result.isEmpty()) {
			Long providerNo = (Long) result.get(0);
			Provider provider = (Provider) this.getHibernateTemplate().get(Provider.class, String.valueOf(providerNo));
			if (provider != null) {
				providerName = provider.getFormattedName();
			}

		}

		if (log.isDebugEnabled()) {
			log.debug("getMostRecentIntakeCProvider: demographicNo=" + demographicNo + ",result=" + providerName);
		}

		return providerName;
	}

	public DemographicExt getDemographicExt(Integer id) {
		if (id == null || id.intValue() <= 0) {
			throw new IllegalArgumentException();
		}

		DemographicExt result = (DemographicExt) this.getHibernateTemplate().get(DemographicExt.class, id);

		if (log.isDebugEnabled()) {
			log.debug("getDemographicExt: id=" + id + ",found=" + (result != null));
		}

		return result;
	}

	public List getDemographicExtByDemographicNo(Integer demographicNo) {
		if (demographicNo == null || demographicNo.intValue() <= 0) {
			throw new IllegalArgumentException();
		}

		List results = this.getHibernateTemplate().find("from DemographicExt d where d.demographicNo = ?", demographicNo);

		if (log.isDebugEnabled()) {
			log.debug("getDemographicExtByDemographicNo: demographicNo=" + demographicNo + ",# of results=" + results.size());
		}

		return results;
	}

	public DemographicExt getDemographicExt(Integer demographicNo, String key) {
		if (demographicNo == null || demographicNo.intValue() <= 0) {
			throw new IllegalArgumentException();
		}

		if (key == null || key.length() <= 0) {
			throw new IllegalArgumentException();
		}

		List results = this.getHibernateTemplate().find("from DemographicExt d where d.demographicNo = ? and d.key = ?", new Object[] { demographicNo, key });
		if (results.isEmpty())
			return null;
		DemographicExt result = (DemographicExt) results.get(0);

		if (log.isDebugEnabled()) {
			log.debug("getDemographicExt: demographicNo=" + demographicNo + ",key=" + key + ",found=" + (result != null));
		}

		return result;
	}

	public void updateDemographicExt(DemographicExt de) {
		if (de == null) {
			throw new IllegalArgumentException();
		}

		this.getHibernateTemplate().saveOrUpdate(de);

		if (log.isDebugEnabled()) {
			log.debug("updateDemographicExt: id=" + de.getId());
		}
	}

	public void saveDemographicExt(Integer demographicNo, String key, String value) {
		if (demographicNo == null || demographicNo.intValue() <= 0) {
			throw new IllegalArgumentException();
		}

		if (key == null || key.length() <= 0) {
			throw new IllegalArgumentException();
		}

		if (value == null) {
			return;
		}

		DemographicExt existingDe = this.getDemographicExt(demographicNo, key);

		if (existingDe != null) {
			existingDe.setDateCreated(new Date());
			existingDe.setValue(value);
			this.getHibernateTemplate().update(existingDe);
		} else {
			DemographicExt de = new DemographicExt();
			de.setDateCreated(new Date());
			de.setDemographicNo(demographicNo);
			de.setHidden(false);
			de.setKey(key);
			de.setValue(value);
			this.getHibernateTemplate().save(de);
		}

		if (log.isDebugEnabled()) {
			log.debug("saveDemographicExt: demographicNo=" + demographicNo + ",key=" + key + ",value=" + value);
		}
	}

	public void removeDemographicExt(Integer id) {
		if (id == null || id.intValue() <= 0) {
			throw new IllegalArgumentException();
		}

		this.getHibernateTemplate().delete(getDemographicExt(id));

		if (log.isDebugEnabled()) {
			log.debug("removeDemographicExt: id=" + id);
		}
	}

	public void removeDemographicExt(Integer demographicNo, String key) {
		if (demographicNo == null || demographicNo.intValue() <= 0) {
			throw new IllegalArgumentException();
		}

		if (key == null || key.length() <= 0) {
			throw new IllegalArgumentException();
		}

		this.getHibernateTemplate().delete(getDemographicExt(demographicNo, key));

		if (log.isDebugEnabled()) {
			log.debug("removeDemographicExt: demographicNo=" + demographicNo + ",key=" + key);
		}
	}

	public List getProgramIdByDemoNo(String demoNo) {
		String q = "Select a.ProgramId From Admission a " + "Where a.ClientId=? and a.AdmissionDate<=? and " + "(a.DischargeDate>=? or (a.DischargeDate is null) or a.DischargeDate=?)";
		/* default time is Oscar default null time 0001-01-01. */
		Date defdt = new GregorianCalendar(1, 0, 1).getTime();
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE,59);
		cal.set(Calendar.SECOND, 59);
		Date dt = cal.getTime();
		
		List rs = (List) getHibernateTemplate().find(q, new Object[] { demoNo, dt, dt, defdt });
		return rs;
	}

}