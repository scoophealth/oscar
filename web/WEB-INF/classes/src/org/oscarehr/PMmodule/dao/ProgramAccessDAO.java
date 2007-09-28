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

package org.oscarehr.PMmodule.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.dao.ProgramAccessDAO;
import org.oscarehr.PMmodule.model.AccessType;
import org.oscarehr.PMmodule.model.ProgramAccess;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ProgramAccessDAO extends HibernateDaoSupport {

    private static Log log = LogFactory.getLog(ProgramAccessDAO.class);

    public List getProgramAccesses(Long programId) {

        if (programId == null || programId.intValue() <= 0) {
            throw new IllegalArgumentException();
        }

        List results = this.getHibernateTemplate().find("from ProgramAccess pa where pa.ProgramId = ?", programId);

        if (log.isDebugEnabled()) {
            log.debug("getProgramAccesses: programId=" + programId + ",# of results=" + results.size());
        }

        return results;
    }

    public ProgramAccess getProgramAccess(Long id) {

        if (id == null || id.intValue() <= 0) {
            throw new IllegalArgumentException();
        }

        ProgramAccess result = (ProgramAccess)this.getHibernateTemplate().get(ProgramAccess.class, id);

        if (log.isDebugEnabled()) {
            log.debug("getProgramAccess: id=" + id + ",found=" + (result != null));
        }
        return result;
    }

    public ProgramAccess getProgramAccess(Long programId, Long accessTypeId) {
        if (programId == null || programId.intValue() <= 0) {
            throw new IllegalArgumentException();
        }
        if (accessTypeId == null || accessTypeId.intValue() <= 0) {
            throw new IllegalArgumentException();
        }

        ProgramAccess result = null;
        List results = this.getHibernateTemplate().find("from ProgramAccess pa where pa.ProgramId = ? and pa.AccessTypeId = ?", new Object[] {programId, accessTypeId});
        if (results.size() > 0) {
            result = (ProgramAccess)results.get(0);
        }

        if (log.isDebugEnabled()) {
            log.debug("getProgramAccess: programId=" + programId + ",accessTypeId=" + accessTypeId + ",found=" + (result != null));
        }

        return result;
    }

    public void saveProgramAccess(ProgramAccess pa) {
        if (pa == null) {
            throw new IllegalArgumentException();
        }

        this.getHibernateTemplate().saveOrUpdate(pa);

        if (log.isDebugEnabled()) {
            log.debug("saveProgramAccess:" + pa.getId());
        }
    }

    public void deleteProgramAccess(Long id) {
        if (id == null || id.intValue() <= 0) {
            throw new IllegalArgumentException();
        }

        this.getHibernateTemplate().delete(getProgramAccess(id));

        if (log.isDebugEnabled()) {
            log.debug("deleteProgramAccess:" + id);
        }

    }

    public List getAccessTypes() {
        List results = this.getHibernateTemplate().find("from AccessType at");

        if (log.isDebugEnabled()) {
            log.debug("getAccessTypes: # of results=" + results.size());
        }
        return results;
    }

    public AccessType getAccessType(Long id) {
        if (id == null || id.intValue() <= 0) {
            throw new IllegalArgumentException();
        }

        AccessType result = (AccessType)this.getHibernateTemplate().get(AccessType.class, id);

        if (log.isDebugEnabled()) {
            log.debug("getAccessType: id=" + id + ",found=" + (result != null));
        }

        return result;
    }
}
