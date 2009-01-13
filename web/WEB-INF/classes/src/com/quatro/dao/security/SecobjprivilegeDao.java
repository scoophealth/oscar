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

package com.quatro.dao.security;

import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.quatro.model.security.Secobjprivilege;

public class SecobjprivilegeDao extends HibernateDaoSupport {

    private Logger log = LogManager.getLogger(SecobjprivilegeDao.class);

  
    public void save(Secobjprivilege secobjprivilege) {
        if (secobjprivilege == null) {
            throw new IllegalArgumentException();
        }
        
        getHibernateTemplate().saveOrUpdate(secobjprivilege);

        if (log.isDebugEnabled()) {
            log.debug("SecobjprivilegeDao : save: " + secobjprivilege.getRoleusergroup() + ":" + secobjprivilege.getObjectname_desc());
        }
        
    }
    public void saveAll(List list) {
		log.debug("saving ALL Secobjprivilege instances");
		try {
			for(int i =0; i< list.size(); i++){
				Secobjprivilege obj = (Secobjprivilege)list.get(i);
				
				int rowcount = update(obj);
				
				if(rowcount <= 0){
					save(obj);
				}
				
			}
						
			log.debug("save ALL successful");
		} catch (RuntimeException re) {
			log.error("save ALL failed", re);
			throw re;
		}
	}
    public int update(Secobjprivilege instance) {
		log.debug("update Secobjprivilege instance");
		try {
			String queryString = "update Secobjprivilege as model set model.providerNo ='" + instance.getProviderNo() + "'"
				+ " where model.objectname_code ='" + instance.getObjectname_code() + "'"
				+ " and model.privilege_code ='" + instance.getPrivilege_code() + "'"
				+ " and model.roleusergroup ='" + instance.getRoleusergroup() + "'";
			
			Query queryObject = getSession().createQuery(queryString);
			
			return queryObject.executeUpdate();
						
		} catch (RuntimeException re) {
			log.error("Update failed", re);
			throw re;
		}
	}
    public int deleteByRoleName(String roleName) {
		log.debug("deleting Secobjprivilege by roleName");
		try {
			
			return getHibernateTemplate().bulkUpdate("delete Secobjprivilege as model where model.roleusergroup =?", roleName);
			
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
    public void delete(Secobjprivilege persistentInstance) {
		log.debug("deleting Secobjprivilege instance");
		try {
			//getSession().delete(persistentInstance);
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
    public String getFunctionDesc(String function_code) {
		try {
			String queryString = "select description from Secobjectname obj where obj.objectname='" + function_code+"'";
			
			List lst = getHibernateTemplate().find(queryString);
			if(lst.size()>0 && lst.get(0)!=null)
				return lst.get(0).toString();
			else
				return "";
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	public String getAccessDesc(String accessType_code) {
		try {
			String queryString = "select description from Secprivilege obj where obj.privilege='" + accessType_code +"'";
			
			List lst = getHibernateTemplate().find(queryString);
			if(lst.size()>0 && lst.get(0)!=null)
				return lst.get(0).toString();
			else
				return "";
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
    public List getFunctions(String roleName) {
        if (roleName == null) {
            throw new IllegalArgumentException();
        }
        
        List result = findByProperty("roleusergroup", roleName);
        if (log.isDebugEnabled()) {
            log.debug("SecobjprivilegeDao : getFunctions: ");
        }
        return result;
    }
    public List findByProperty(String propertyName, Object value) {
		log.debug("finding Secobjprivilege instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Secobjprivilege as model where model."
					+ propertyName + "= ? order by objectname_code";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
}
