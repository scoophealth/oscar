/**
 * Copyright (c) 2005, 2009 IBM Corporation and others.
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
 * Contributors:
 *     <Quatro Group Software Systems inc.>  <OSCAR Team>
 */

package com.quatro.dao.security;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.quatro.model.security.SecProvider;

/**
 * 
 * @author JZhang
 *
 */

public class SecProviderDao extends HibernateDaoSupport {
	private static final Logger logger = MiscUtils.getLogger();
	// property constants
	public static final String LAST_NAME = "lastName";
	public static final String FIRST_NAME = "firstName";
	public static final String PROVIDER_TYPE = "providerType";
	public static final String SPECIALTY = "specialty";
	public static final String TEAM = "team";
	public static final String SEX = "sex";
	public static final String ADDRESS = "address";
	public static final String PHONE = "phone";
	public static final String WORK_PHONE = "workPhone";
	public static final String OHIP_NO = "ohipNo";
	public static final String RMA_NO = "rmaNo";
	public static final String BILLING_NO = "billingNo";
	public static final String HSO_NO = "hsoNo";
	public static final String STATUS = "status";
	public static final String COMMENTS = "comments";
	public static final String PROVIDER_ACTIVITY = "providerActivity";

	public void save(SecProvider transientInstance) {
		logger.debug("saving Provider instance");
		try {
			this.getHibernateTemplate().save(transientInstance);
			logger.debug("save successful");
		} catch (RuntimeException re) {
			logger.error("save failed", re);
			throw re;
		}
	}
	
	public void saveOrUpdate(SecProvider transientInstance) {
		logger.debug("saving Provider instance");
		try {
			this.getHibernateTemplate().saveOrUpdate(transientInstance);
			logger.debug("save successful");
		} catch (RuntimeException re) {
			logger.error("save failed", re);
			throw re;
		}
	}
	public void delete(SecProvider persistentInstance) {
		logger.debug("deleting Provider instance");
		try {
			this.getHibernateTemplate().delete(persistentInstance);
			logger.debug("delete successful");
		} catch (RuntimeException re) {
			logger.error("delete failed", re);
			throw re;
		}
	}

	public SecProvider findById(java.lang.String id) {
		logger.debug("getting Provider instance with id: " + id);
		try {
			SecProvider instance = (SecProvider) this.getHibernateTemplate().get(
					"com.quatro.model.security.SecProvider", id);
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public SecProvider findById(java.lang.String id,String status) {
		logger.debug("getting Provider instance with id: " + id);
		try {
			String sql ="from SecProvider where id=? and status=?";
			List lst=this.getHibernateTemplate().find(sql,new Object[]{id,status});			
	        if(lst.size()==0)
	          return null;
	        else
	          return (SecProvider) lst.get(0);
				
			} catch (RuntimeException re) {
				logger.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(SecProviderDao instance) {
		logger.debug("finding Provider instance by example");
		Session session = getSession();
		try {
			List results = session.createCriteria(
					"com.quatro.model.security.SecProvider").add(
					Example.create(instance)).list();
			logger.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			logger.error("find by example failed", re);
			throw re;
		} finally {
			this.releaseSession(session);
		}
	}

	public List findByProperty(String propertyName, Object value) {
		logger.debug("finding Provider instance with property: " + propertyName
				+ ", value: " + value);
		Session session = getSession();
		try {
			String queryString = "from Provider as model where model."
					+ propertyName + "= ?";
			Query queryObject = session.createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			logger.error("find by property name failed", re);
			throw re;
		} finally {
			this.releaseSession(session);
		}
	}

	public List findByLastName(Object lastName) {
		return findByProperty(LAST_NAME, lastName);
	}

	public List findByFirstName(Object firstName) {
		return findByProperty(FIRST_NAME, firstName);
	}

	public List findByProviderType(Object providerType) {
		return findByProperty(PROVIDER_TYPE, providerType);
	}

	public List findBySpecialty(Object specialty) {
		return findByProperty(SPECIALTY, specialty);
	}

	public List findByTeam(Object team) {
		return findByProperty(TEAM, team);
	}

	public List findBySex(Object sex) {
		return findByProperty(SEX, sex);
	}

	public List findByAddress(Object address) {
		return findByProperty(ADDRESS, address);
	}

	public List findByPhone(Object phone) {
		return findByProperty(PHONE, phone);
	}

	public List findByWorkPhone(Object workPhone) {
		return findByProperty(WORK_PHONE, workPhone);
	}

	public List findByOhipNo(Object ohipNo) {
		return findByProperty(OHIP_NO, ohipNo);
	}

	public List findByRmaNo(Object rmaNo) {
		return findByProperty(RMA_NO, rmaNo);
	}

	public List findByBillingNo(Object billingNo) {
		return findByProperty(BILLING_NO, billingNo);
	}

	public List findByHsoNo(Object hsoNo) {
		return findByProperty(HSO_NO, hsoNo);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findByComments(Object comments) {
		return findByProperty(COMMENTS, comments);
	}

	public List findByProviderActivity(Object providerActivity) {
		return findByProperty(PROVIDER_ACTIVITY, providerActivity);
	}

	public List findAll() {
		logger.debug("finding all Provider instances");
		Session session = getSession();
		try {
			String queryString = "from Provider";
			Query queryObject = session.createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			logger.error("find all failed", re);
			throw re;
		} finally {
			this.releaseSession(session);
		}
	}

	public SecProviderDao merge(SecProviderDao detachedInstance) {
		logger.debug("merging Provider instance");
		Session session = getSession();
		try {
			SecProviderDao result = (SecProviderDao) session.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		} finally {
			this.releaseSession(session);
		}
	}

	public void attachDirty(SecProviderDao instance) {
		logger.debug("attaching dirty Provider instance");
		Session session = getSession();
		try {
			session.saveOrUpdate(instance);
			logger.debug("attach successful");
		} catch (RuntimeException re) {
			logger.error("attach failed", re);
			throw re;
		} finally {
			this.releaseSession(session);
		}
	}

	public void attachClean(SecProviderDao instance) {
		logger.debug("attaching clean Provider instance");
		Session session = getSession();
		try {
			session.lock(instance, LockMode.NONE);
			logger.debug("attach successful");
		} catch (RuntimeException re) {
			logger.error("attach failed", re);
			throw re;
		} finally {
			this.releaseSession(session);
		}
	}
}
