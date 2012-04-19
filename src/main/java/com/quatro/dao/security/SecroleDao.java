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


package com.quatro.dao.security;

import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.quatro.model.security.Secrole;

public class SecroleDao extends HibernateDaoSupport {

    private Logger logger = MiscUtils.getLogger();

    public List<Secrole> getRoles() {
        @SuppressWarnings("unchecked")
        List<Secrole> results = this.getHibernateTemplate().find("from Secrole r order by roleName");

        logger.debug("getRoles: # of results=" + results.size());

        return results;
    }

    public Secrole getRole(Integer id) {
        if (id == null || id.intValue() <= 0) {
            throw new IllegalArgumentException();
        }

        Secrole result = this.getHibernateTemplate().get(Secrole.class, new Long(id));

        logger.debug("getRole: id=" + id + ",found=" + (result != null));

        return result;
    }

    public Secrole getRoleByName(String roleName) {
    	Secrole result = null;
    	if (roleName == null || roleName.length() <= 0) {
            throw new IllegalArgumentException();
        }

        List lst = this.getHibernateTemplate().find("from Secrole r where r.roleName='" + roleName + "'");
        if(lst != null && lst.size() > 0)
        	result = (Secrole) lst.get(0);

        logger.debug("getRoleByName: roleName=" + roleName + ",found=" + (result != null));

        return result;
    }


    public List getDefaultRoles() {
        return this.getHibernateTemplate().find("from Secrole r where r.userDefined=0");
    }

    public void save(Secrole secrole) {
        if (secrole == null) {
            throw new IllegalArgumentException();
        }

        getHibernateTemplate().saveOrUpdate(secrole);

    }

}
