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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.oscarehr.PMmodule.model.FunctionalUserType;
import org.oscarehr.PMmodule.model.ProgramFunctionalUser;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ProgramFunctionalUserDAO extends HibernateDaoSupport {

    private static Logger log = MiscUtils.getLogger();

    public List<FunctionalUserType> getFunctionalUserTypes() {
        List<FunctionalUserType> results = this.getHibernateTemplate().find("from FunctionalUserType");

        if (log.isDebugEnabled()) {
            log.debug("getFunctionalUserTypes: # of results=" + results.size());
        }
        return results;
    }

    public FunctionalUserType getFunctionalUserType(Long id) {
        if (id == null || id.intValue() <= 0) {
            throw new IllegalArgumentException();
        }

        FunctionalUserType result = this.getHibernateTemplate().get(FunctionalUserType.class, id);

        if (log.isDebugEnabled()) {
            log.debug("getFunctionalUserType: id=" + id + ",found=" + (result != null));
        }

        return result;
    }

    public void saveFunctionalUserType(FunctionalUserType fut) {
        if (fut == null) {
            throw new IllegalArgumentException();
        }

        this.getHibernateTemplate().saveOrUpdate(fut);

        if (log.isDebugEnabled()) {
            log.debug("saveFunctionalUserType:" + fut.getId());
        }
    }

    public void deleteFunctionalUserType(Long id) {
        if (id == null || id.intValue() <= 0) {
            throw new IllegalArgumentException();
        }

        this.getHibernateTemplate().delete(getFunctionalUserType(id));

        if (log.isDebugEnabled()) {
            log.debug("deleteFunctionalUserType:" + id);
        }
    }

    public List<FunctionalUserType> getFunctionalUsers(Long programId) {
        if (programId == null || programId.intValue() <= 0) {
            throw new IllegalArgumentException();
        }

        List<FunctionalUserType> results = this.getHibernateTemplate().find("from ProgramFunctionalUser pfu where pfu.ProgramId = ?", programId);

        if (log.isDebugEnabled()) {
            log.debug("getFunctionalUsers: programId=" + programId + ",# of results=" + results.size());
        }
        return results;
    }

    public ProgramFunctionalUser getFunctionalUser(Long id) {
        if (id == null || id.intValue() <= 0) {
            throw new IllegalArgumentException();
        }

        ProgramFunctionalUser result = this.getHibernateTemplate().get(ProgramFunctionalUser.class, id);

        if (log.isDebugEnabled()) {
            log.debug("getFunctionalUser: id=" + id + ",found=" + (result != null));
        }

        return result;
    }

    public void saveFunctionalUser(ProgramFunctionalUser pfu) {
        if (pfu == null) {
            throw new IllegalArgumentException();
        }

        this.getHibernateTemplate().saveOrUpdate(pfu);

        if (log.isDebugEnabled()) {
            log.debug("saveFunctionalUser:" + pfu.getId());
        }
    }

    public void deleteFunctionalUser(Long id) {
        if (id == null || id.intValue() <= 0) {
            throw new IllegalArgumentException();
        }

        this.getHibernateTemplate().delete(getFunctionalUser(id));

        if (log.isDebugEnabled()) {
            log.debug("deleteFunctionalUser:" + id);
        }
    }

    public Long getFunctionalUserByUserType(Long programId, Long userTypeId) {
        if (programId == null || programId.intValue() <= 0) {
            throw new IllegalArgumentException();
        }
        if (userTypeId == null || userTypeId.intValue() <= 0) {
            throw new IllegalArgumentException();
        }

        Long result = null;

        Session session = getSession();
        Query q = session.createQuery("select pfu.ProgramId from ProgramFunctionalUser pfu where pfu.ProgramId = ? and pfu.UserTypeId = ?");
        q.setLong(0, programId.longValue());
        q.setLong(1, userTypeId.longValue());
        List results = new ArrayList();
        try {
        	 results = q.list();
        }finally {
        	releaseSession(session);
        }
        if (results.size() > 0) {
            result = (Long)results.get(0);
        }

        if (log.isDebugEnabled()) {
            log.debug("getFunctionalUserByUserType: programId=" + programId + ",userTypeId=" + userTypeId + ",result=" + result);
        }

        return result;
    }
}
