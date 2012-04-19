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

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.quatro.model.security.Security;
import com.quatro.web.admin.UserSearchFormBean;

/**
 * 
 * @author JZhang
 *
 */

public class SecurityDao extends HibernateDaoSupport {
	private static final Logger logger = MiscUtils.getLogger();
	// property constants
	public static final String USER_NAME = "userName";
	public static final String PROVIDER_NO = "providerNo";
	public static final String PASSWORD = "password";
	public static final String PIN = "pin";
	public static final String _BREMOTELOCKSET = "BRemotelockset";
	public static final String _BLOCALLOCKSET = "BLocallockset";
	public static final String _BEXPIRESET = "BExpireset";

	
	
	public List getProfile(String providerNo) {
		logger.debug("All User list");
		try {
			// String queryString = "select securityNo, userName, providerNo from Security";
			
			String queryString =  "select sur.providerNo, sur.roleName, org.description, s.userName, sur.orgcd"
				+ " from Secuserrole sur, Security s, LstOrgcd org"
				+ " where sur.providerNo = '" + providerNo + "'"
				+ " and s.providerNo = sur.providerNo"
				+ " and sur.orgcd = org.code"
				+ " order by sur.orgcd";
			
			/* SQL: for displaying users with or without roles
			select s.security_No, s.user_Name, p.last_Name, p.first_Name, sur.role_Name, sur.orgcd 
			from Security s, Provider p LEFT OUTER JOIN secUserRole sur ON p.provider_No = sur.provider_No
			where s.provider_No = p.provider_No
			*/
			return this.getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			logger.error("find All User list failed", re);
			throw re;
		}
	}
	
	public List getAllUsers() {
		logger.debug("All User list");
		try {
			// String queryString = "select securityNo, userName, providerNo from Security";
			String queryString =  "select s.securityNo, s.userName, p.lastName, p.firstName, p.providerNo, p.status"
				+ " from Security s, SecProvider p"
				+ " where s.providerNo = p.providerNo"
				+ " order by 3,4";
				
			return this.getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			logger.error("find All User list failed", re);
			throw re;
		}
	}
	
	public List search(UserSearchFormBean bean) {
		logger.debug("User search");
		try {

			String userName = "";
			String firstName = "";
			String lastName = "";
			String active = "";
			String roleName = "";
			
			
			String AND = " and ";
			
			String sql0 = "";
			String sql1 = "";
			String sql2 = "";
			String sql3 = "";
			String sql4 = "";
			
			String queryString =  "select s.securityNo, s.userName, p.lastName, p.firstName, p.providerNo, p.status"
				+ " from Security s, SecProvider p"
				+ " where s.providerNo = p.providerNo";
			
			if (bean.getUserName() != null && bean.getUserName().length() > 0) {
				userName = bean.getUserName();
				userName = StringEscapeUtils.escapeSql(userName);
				userName = userName.toLowerCase();
				sql0 = "lower(s.userName) like '" + userName + "%'";
				
			}
			
			if (bean.getFirstName() != null && bean.getFirstName().length() > 0) {
				firstName = bean.getFirstName();
				firstName = StringEscapeUtils.escapeSql(firstName);
				firstName = firstName.toLowerCase();
				
				sql1 = "(lower(p.firstName) like '" + firstName + "%' or lower(p.lastName) like '" + firstName + "%')";
				
			}
			
			if (bean.getLastName() != null && bean.getLastName().length() > 0) {
				lastName = bean.getLastName();
				lastName = StringEscapeUtils.escapeSql(lastName);
				lastName = lastName.toLowerCase();
				
				sql2 = "(lower(p.lastName) like '" + lastName + "%' or lower(p.firstName) like '" + lastName + "%')";
				
			}
			
			if (bean.getActive() != null && bean.getActive().length() > 0) {
				
				active = bean.getActive();
				
				sql3 = "p.status ='" + active + "'";
				
			}
			
			if (bean.getRoleName() != null && bean.getRoleName().length() > 0) {
				
				roleName = bean.getRoleName();
				
				sql4 = "p.providerNo IN (select r.providerNo from Secuserrole r where r.roleName ='" + roleName + "')";
				
			}
			
			
			if (userName.length() > 0 )	{
				queryString = queryString + AND + sql0;
			}
			
			if (firstName.length() > 0)	{
				queryString = queryString + AND + sql1;
			}
			
			if (lastName.length()>0) {
				queryString = queryString + AND + sql2;
			}
			
			if (active.length() > 0) {
				queryString = queryString + AND + sql3;
			}
			
			if (roleName.length() > 0) {
				queryString = queryString + AND + sql4;
			}
			
			queryString += " order by 3,4";
			
			return this.getHibernateTemplate().find(queryString);

			
		} catch (RuntimeException re) {
			logger.error("User search failed", re);
			throw re;
		}
	}
	
	
	public void save(Security transientInstance) {
		logger.debug("saving Security instance");
		try {
			this.getHibernateTemplate().save(transientInstance);
			logger.debug("save successful");
		} catch (RuntimeException re) {
			logger.error("save failed", re);
			throw re;
		}
	}
	
	public void saveOrUpdate(Security transientInstance) {
		logger.debug("saving/update Security instance");
		try {
			this.getHibernateTemplate().saveOrUpdate(transientInstance);
			logger.debug("save/update successful");
		} catch (RuntimeException re) {
			logger.error("save/update failed", re);
			throw re;
		}
	}

	public void delete(Security persistentInstance) {
		logger.debug("deleting Security instance");
		try {
			this.getHibernateTemplate().delete(persistentInstance);
			logger.debug("delete successful");
		} catch (RuntimeException re) {
			logger.error("delete failed", re);
			throw re;
		}
	}

	public Security findById(java.lang.Integer id) {
		logger.debug("getting Security instance with id: " + id);
		try {
			Security instance = (Security) this.getHibernateTemplate().get(
					"com.quatro.model.security.Security", id);
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Security instance) {
		logger.debug("finding Security instance by example");
		try {
			List results =  getSession().createCriteria(
					"com.quatro.model.security.Security").add(
					Example.create(instance)).list();
			logger.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			logger.error("find by example failed", re);
			throw re;
		}
	}

    public List<Security> findByProperty(String propertyName, Object value) {
		logger.debug("finding Security instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Security as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);

			@SuppressWarnings("unchecked")
			List<Security> result=queryObject.list();
			return result;
		} catch (RuntimeException re) {
			logger.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Security> findByUserName(Object userName) {
		List<Security> results= findByProperty(USER_NAME, userName);		
		return(results);
	}
	public List<Security> findByProviderNo(Object providerNo) {
		return findByProperty(PROVIDER_NO, providerNo);
	}
	public List<Security> findByPassword(Object password) {
		return findByProperty(PASSWORD, password);
	}

	public List<Security> findByPin(Object pin) {
		return findByProperty(PIN, pin);
	}

	public List<Security> findByBRemotelockset(Object BRemotelockset) {
		return findByProperty(_BREMOTELOCKSET, BRemotelockset);
	}

	public List<Security> findByBLocallockset(Object BLocallockset) {
		return findByProperty(_BLOCALLOCKSET, BLocallockset);
	}

	public List<Security> findByBExpireset(Object BExpireset) {
		return findByProperty(_BEXPIRESET, BExpireset);
	}

	public List<Security> findAll() {
		logger.debug("finding all Security instances");
		try {
			String queryString = "from Security";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			logger.error("find all failed", re);
			throw re;
		}
	}

	public Security merge(Security detachedInstance) {
		logger.debug("merging Security instance");
		try {
			Security result = (Security) getSession().merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Security instance) {
		logger.debug("attaching dirty Security instance");
		try {
			getSession().saveOrUpdate(instance);
			logger.debug("attach successful");
		} catch (RuntimeException re) {
			logger.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Security instance) {
		logger.debug("attaching clean Security instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			logger.debug("attach successful");
		} catch (RuntimeException re) {
			logger.error("attach failed", re);
			throw re;
		}
	}
	
	public Security getByProviderNo(String providerNo) {
		String sql = "select s from Security s where s.providerNo=?";
		
		@SuppressWarnings("unchecked")
		List<Security> results = getHibernateTemplate().find(sql,new Object[]{providerNo});
		
		if(!results.isEmpty()) {
			return results.get(0);
		}
		return null;
	}
}
