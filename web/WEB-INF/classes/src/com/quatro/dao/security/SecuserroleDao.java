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

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
//import org.oscarehr.PMmodule.web.formbean.IncidentForm;
import org.oscarehr.PMmodule.web.formbean.StaffForm;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.quatro.model.security.Secuserrole;

/**
 * A data access object (DAO) providing persistence and search support for
 * Secuserrole entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.quatro.model.Secuserrole
 * @author MyEclipse Persistence Tools
 */

public class SecuserroleDao extends HibernateDaoSupport {
	private static final Logger log = LogManager.getLogger(SecuserroleDao.class);
	// property constants
	public static final String PROVIDER_NO = "providerNo";
	public static final String ROLE_NAME = "roleName";
	public static final String ORGCD = "orgcd";
	public static final String ACTIVEYN = "activeyn";

	public void saveAll(List list) {
		log.debug("saving ALL Secuserrole instances");
		try {
			for(int i =0; i< list.size(); i++){
				Secuserrole obj = (Secuserrole)list.get(i);
				
				int rowcount = update(obj);
				
				if(rowcount <= 0){
					getSession().save(obj);
				}
				
			}
			//this.getHibernateTemplate().saveOrUpdateAll(list);
			log.debug("save ALL successful");
		} catch (RuntimeException re) {
			log.error("save ALL failed", re);
			throw re;
		}
	}
	public void save(Secuserrole transientInstance) {
		log.debug("saving Secuserrole instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	
	public void delete(Secuserrole persistentInstance) {
		log.debug("deleting Secuserrole instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	public int deleteByOrgcd(String orgcd) {
		log.debug("deleting Secuserrole by orgcd");
		try {
			
			return getHibernateTemplate().bulkUpdate("delete Secuserrole as model where model.orgcd =?", orgcd);
			
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	public int deleteByProviderNo(String providerNo) {
		log.debug("deleting Secuserrole by providerNo");
		try {
			
			return getHibernateTemplate().bulkUpdate("delete Secuserrole as model where model.providerNo =?", providerNo);
			
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	
	public int deleteById(Integer id) {
		log.debug("deleting Secuserrole by ID");
		try {
			
			return getHibernateTemplate().bulkUpdate("delete Secuserrole as model where model.id =?", id);
			
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	public int update(Secuserrole instance) {
		log.debug("Update Secuserrole instance");
		try {
			String queryString = "update Secuserrole as model set model.activeyn ='" + instance.getActiveyn() + "'"
				+ " where model.providerNo ='" + instance.getProviderNo() + "'"
				+ " and model.roleName ='" + instance.getRoleName() + "'"
				+ " and model.orgcd ='" + instance.getOrgcd() + "'";
			
			Query queryObject = getSession().createQuery(queryString);
			
			return queryObject.executeUpdate();
						
		} catch (RuntimeException re) {
			log.error("Update failed", re);
			throw re;
		}
	}
	public Secuserrole findById(java.lang.Integer id) {
		log.debug("getting Secuserrole instance with id: " + id);
		try {
			Secuserrole instance = (Secuserrole) getSession().get(
					"com.quatro.model.Secuserrole", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Secuserrole instance) {
		log.debug("finding Secuserrole instance by example");
		try {
			List results = getSession().createCriteria(
					"com.quatro.model.Secuserrole").add(
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
		log.debug("finding Secuserrole instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Secuserrole as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByProviderNo(Object providerNo) {
		return findByProperty(PROVIDER_NO, providerNo);
	}

	public List findByRoleName(Object roleName) {
		return findByProperty(ROLE_NAME, roleName);
	}

	public List findByOrgcd(Object orgcd, boolean activeOnly) {
		//return findByProperty(ORGCD, orgcd);
		/* SQL:
		select * from secUserRole s,
		(select codecsv from lst_orgcd where code = 'P200011') b
		where b.codecsv like '%' || s.orgcd || ',%'
		and not (s.orgcd like 'R%' or s.orgcd like 'O%')
		
		*/
		log.debug("Find staff instance .");
		try {
			
			String queryString = "select a from Secuserrole a, LstOrgcd b, SecProvider p"
				+ " where a.providerNo=p.providerNo and b.code ='" + orgcd + "'";
			if (activeOnly) queryString += " and p.status='1'";

			queryString = queryString 	
				+ " and b.codecsv like '%' || a.orgcd || ',%'"
				+ " and not (a.orgcd like 'R%' or a.orgcd like 'O%')";
						
						
			return this.getHibernateTemplate().find(queryString);
			
		} catch (RuntimeException re) {
			log.error("Find staff failed", re);
			throw re;
		}
		
	}
	public List searchByCriteria(StaffForm staffForm){
		
		log.debug("Search staff instance .");
		try {
			
			
			String AND = " and ";
			//String OR = " or ";
			
			
			String orgcd = staffForm.getOrgcd();
			
			String queryString = "select a from Secuserrole a, LstOrgcd b"
				+ " where b.code ='" + orgcd + "'"
				+ " and b.codecsv like '%' || a.orgcd || ',%'"
				+ " and not (a.orgcd like 'R%' or a.orgcd like 'O%')";
			
			String fname = staffForm.getFirstName();
			String lname = staffForm.getLastName();
			
			if (fname != null && fname.length() > 0) {
				fname = StringEscapeUtils.escapeSql(fname);
				fname = fname.toLowerCase();
				queryString = queryString + AND + "lower(a.providerFName) like '%" + fname + "%'";
			}
			if (lname != null && lname.length() > 0) {
				lname = StringEscapeUtils.escapeSql(lname);
				lname = lname.toLowerCase();
				queryString = queryString + AND + "lower(a.providerLName) like '%" + lname + "%'";
			}
			
			return this.getHibernateTemplate().find(queryString);
			
		} catch (RuntimeException re) {
			log.error("Search staff failed", re);
			throw re;
		}
	}
	
	public List findByActiveyn(Object activeyn) {
		return findByProperty(ACTIVEYN, activeyn);
	}

	public List findAll() {
		log.debug("finding all Secuserrole instances");
		try {
			String queryString = "from Secuserrole";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Secuserrole merge(Secuserrole detachedInstance) {
		log.debug("merging Secuserrole instance");
		try {
			Secuserrole result = (Secuserrole) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Secuserrole instance) {
		log.debug("attaching dirty Secuserrole instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Secuserrole instance) {
		log.debug("attaching clean Secuserrole instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}
