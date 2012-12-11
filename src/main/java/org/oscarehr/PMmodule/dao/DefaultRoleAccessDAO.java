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

import java.util.List;

import org.oscarehr.PMmodule.model.DefaultRoleAccess;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

@SuppressWarnings("unchecked")
public class DefaultRoleAccessDAO extends HibernateDaoSupport {

    public void deleteDefaultRoleAccess(Long id) {
        this.getHibernateTemplate().delete(getDefaultRoleAccess(id));
    }

    public DefaultRoleAccess getDefaultRoleAccess(Long id) {
        return this.getHibernateTemplate().get(DefaultRoleAccess.class, id);
    }

    public List<DefaultRoleAccess> getDefaultRoleAccesses() {
        return this.getHibernateTemplate().find("from DefaultRoleAccess dra ORDER BY role_id");
    }
    
    public List<DefaultRoleAccess> findAll() {
        return this.getHibernateTemplate().find("from DefaultRoleAccess dra");
    }

    public void saveDefaultRoleAccess(DefaultRoleAccess dra) {
        this.getHibernateTemplate().saveOrUpdate(dra);
    }

    public DefaultRoleAccess find(Long roleId, Long accessTypeId) {
        List results = this.getHibernateTemplate().find("from DefaultRoleAccess dra where dra.roleId=? and dra.accessTypeId=?", new Object[] {roleId, accessTypeId});

        if (!results.isEmpty()) {
            return (DefaultRoleAccess)results.get(0);
        }
        return null;
    }
    
    public List<Object[]> findAllRolesAndAccessTypes(){
    	return getHibernateTemplate().find("FROM DefaultRoleAccess a, AccessType b WHERE a.id = b.Id");
    }

}
