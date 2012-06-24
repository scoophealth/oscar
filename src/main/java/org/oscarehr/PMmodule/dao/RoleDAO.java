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

import org.apache.log4j.Logger;
import org.caisi.model.Role;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class RoleDAO extends HibernateDaoSupport {

    private Logger log=MiscUtils.getLogger();

    public List<Role> getRoles() {
        @SuppressWarnings("unchecked")
        List<Role> results = this.getHibernateTemplate().find("from Role r");

        log.debug("getRoles: # of results=" + results.size());

        return results;
    }

    public Role getRole(Long id) {
        if (id == null || id.intValue() <= 0) {
            throw new IllegalArgumentException();
        }

        Role result = this.getHibernateTemplate().get(Role.class, id);

        log.debug("getRole: id=" + id + ",found=" + (result != null));

        return result;
    }

    @SuppressWarnings("unchecked")
    public List<Role> getDefaultRoles() {
        return this.getHibernateTemplate().find("from Role r where r.userDefined=0");
    }

}
