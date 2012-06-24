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

package org.caisi.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.caisi.model.IssueAdmin;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class IssueAdminDAO extends HibernateDaoSupport {
    private Logger log=MiscUtils.getLogger();

    public List getIssueAdmins() {
        return getHibernateTemplate().find("from IssueAdmin");
    }

    public IssueAdmin getIssueAdmin(Long id) {
        return getHibernateTemplate().get(IssueAdmin.class, id);
    }

    public void saveIssueAdmin(IssueAdmin issueAdmin) {
        getHibernateTemplate().saveOrUpdate(issueAdmin);

        if (log.isDebugEnabled()) {
            log.debug("issueAdminId set to:" + issueAdmin.getId());
        }
    }

    public void removeIssueAdmin(Long id) {
        Object issueAdmin = getHibernateTemplate().load(IssueAdmin.class, id);
        getHibernateTemplate().delete(issueAdmin);
    }
}
