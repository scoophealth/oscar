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

package org.oscarehr.PMmodule.dao;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.model.SecUserRole;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class SecUserRoleDao extends HibernateDaoSupport {

    private static Logger log = MiscUtils.getLogger();

    public List<SecUserRole> getUserRoles(String providerNo) {
        if (providerNo == null) {
            throw new IllegalArgumentException();
        }

        @SuppressWarnings("unchecked")
        List<SecUserRole> results = getHibernateTemplate().find("from SecUserRole s where s.ProviderNo = ?", providerNo);

        if (log.isDebugEnabled()) {
            log.debug("getUserRoles: providerNo=" + providerNo + ",# of results=" + results.size());
        }

        return results;
    }

    public List<SecUserRole> getSecUserRolesByRoleName(String roleName) {
        @SuppressWarnings("unchecked")
        List<SecUserRole> results = getHibernateTemplate().find("from SecUserRole s where s.RoleName = ?", roleName);

        return results;
    }
    
    public List<SecUserRole> findByRoleNameAndProviderNo(String roleName, String providerNo) {
        @SuppressWarnings("unchecked")
        List<SecUserRole> results = getHibernateTemplate().find("from SecUserRole s where s.RoleName = ? and s.ProviderNo=?", new Object[]{roleName,providerNo});

        return results;
    }

    public boolean hasAdminRole(String providerNo) {
        if (providerNo == null) {
            throw new IllegalArgumentException();
        }

        boolean result = false;
        @SuppressWarnings("unchecked")
        List<SecUserRole> results = this.getHibernateTemplate().find("from SecUserRole s where s.ProviderNo = ? and s.RoleName = 'admin'", providerNo);
        if (!results.isEmpty()) {
            result = true;
        }

        if (log.isDebugEnabled()) {
            log.debug("hasAdminRole: providerNo=" + providerNo + ",result=" + result);
        }

        return result;
    }

    public SecUserRole find(Long id) {
    	return this.getHibernateTemplate().get(SecUserRole.class, id);
    }

    public void save(SecUserRole sur) {
    	sur.setLastUpdateDate(new Date());
    	this.getHibernateTemplate().save(sur);
    }
    
    public List<String> getRecordsAddedAndUpdatedSinceTime(Date date) {
		@SuppressWarnings("unchecked")
		List<String> records = getHibernateTemplate().find("select p.ProviderNo From SecUserRole p WHERE p.lastUpdateDate > ?",date);
		
		return records;
	}
    
}
