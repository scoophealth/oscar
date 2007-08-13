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

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.model.Provider;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ProviderDao extends HibernateDaoSupport {
	private Log log = LogFactory.getLog(ProviderDao.class);
	
	public boolean providerExists(String providerNo) {
		boolean exists = (((Integer) getHibernateTemplate().iterate("select count(*) from Provider p where p.ProviderNo = " + providerNo).next()) == 1);
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
		List<Provider> rs = getHibernateTemplate().find("FROM  Provider p ORDER BY p.LastName");

		if (log.isDebugEnabled()) {
			log.debug("getProviders: # of results=" + rs.size());
		}
		return rs;
	}

	public List<Provider> search(String name) {
		Criteria c = this.getSession().createCriteria(Provider.class);
		c.add(Restrictions.or(Expression.like("FirstName", name + "%"), Expression.like("LastName", name + "%")));
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
		List<Provider> results = this.getHibernateTemplate().find("from Provider p where p.ProviderType = ?", type);

		if (log.isDebugEnabled()) {
			log.debug("getProvidersByType: type=" + type + ",# of results=" + results.size());
		}

		return results;
	}
}
