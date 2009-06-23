/*******************************************************************************
 * Copyright (c) 2008, 2009 Quatro Group Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU General Public License
 * which accompanies this distribution, and is available at
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * Contributors:
 *     <Quatro Group Software Systems inc.>  <OSCAR Team>
 *******************************************************************************/
package com.quatro.dao.security;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.oscarehr.PMmodule.model.Admission;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.quatro.model.security.SecProvider;

/**
 * 
 * @author JZhang
 *
 */

public class SecProviderDao extends HibernateDaoSupport {
	private static final Logger log = LogManager.getLogger(SecProviderDao.class);
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
		log.debug("saving Provider instance");
		try {
			this.getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	
	public void saveOrUpdate(SecProvider transientInstance) {
		log.debug("saving Provider instance");
		try {
			this.getHibernateTemplate().saveOrUpdate(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	public void delete(SecProvider persistentInstance) {
		log.debug("deleting Provider instance");
		try {
			this.getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public SecProvider findById(java.lang.String id) {
		log.debug("getting Provider instance with id: " + id);
		try {
			SecProvider instance = (SecProvider) this.getHibernateTemplate().get(
					"com.quatro.model.security.SecProvider", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	public SecProvider findById(java.lang.String id,String status) {
		log.debug("getting Provider instance with id: " + id);
		try {
			String sql ="from SecProvider where id=? and status=?";
			List lst=this.getHibernateTemplate().find(sql,new Object[]{id,status});			
	        if(lst.size()==0)
	          return null;
	        else
	          return (SecProvider) lst.get(0);
				
			} catch (RuntimeException re) {
				log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(SecProviderDao instance) {
		log.debug("finding Provider instance by example");
		try {
			List results = getSession().createCriteria(
					"com.quatro.model.security.SecProvider").add(
					Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Provider instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Provider as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
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
		log.debug("finding all Provider instances");
		try {
			String queryString = "from Provider";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public SecProviderDao merge(SecProviderDao detachedInstance) {
		log.debug("merging Provider instance");
		try {
			SecProviderDao result = (SecProviderDao) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(SecProviderDao instance) {
		log.debug("attaching dirty Provider instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(SecProviderDao instance) {
		log.debug("attaching clean Provider instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}