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
import org.oscarehr.PMmodule.model.ProgramClientStatus;
import org.oscarehr.common.model.Admission;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ProgramClientStatusDAO extends HibernateDaoSupport {

    private Logger log=MiscUtils.getLogger();

    public List<ProgramClientStatus> getProgramClientStatuses(Integer programId) {
        return this.getHibernateTemplate().find("from ProgramClientStatus pcs where pcs.programId=?", programId);
    }

    public void saveProgramClientStatus(ProgramClientStatus status) {
        this.getHibernateTemplate().saveOrUpdate(status);
    }

    public ProgramClientStatus getProgramClientStatus(String id) {
        if (id == null || Integer.valueOf(id) < 0) {
            throw new IllegalArgumentException();
        }

        ProgramClientStatus pcs = null;
        pcs = this.getHibernateTemplate().get(ProgramClientStatus.class, new Integer(id));
        if (pcs != null) return pcs;
        else return null;
    }

    public void deleteProgramClientStatus(String id) {
        this.getHibernateTemplate().delete(getProgramClientStatus(id));
    }

    public boolean clientStatusNameExists(Integer programId, String statusName) {
        if (programId == null || programId.intValue() <= 0) {
            throw new IllegalArgumentException();
        }

        if (statusName == null || statusName.length() <= 0) {
            throw new IllegalArgumentException();
        }

        Session session = getSession();
        List teams = new ArrayList();
        try {
	        Query query =session.createQuery("select pt.id from ProgramClientStatus pt where pt.programId = ? and pt.name = ?");
	        query.setLong(0, programId.longValue());
	        query.setString(1, statusName);
	
	        teams = query.list();
	
	        if (log.isDebugEnabled()) {
	            log.debug("teamNameExists: programId = " + programId + ", statusName = " + statusName + ", result = " + !teams.isEmpty());
	        }
        }finally {
        	releaseSession(session);
        }
        return !teams.isEmpty();
    }

    public List<Admission> getAllClientsInStatus(Integer programId, Integer statusId) {
        if (programId == null || programId <= 0) {
            throw new IllegalArgumentException();
        }

        if (statusId == null || statusId <= 0) {
            throw new IllegalArgumentException();
        }

        List<Admission> results = this.getHibernateTemplate().find("from Admission a where a.ProgramId = ? and a.TeamId = ? and a.AdmissionStatus='current'", new Object[] {programId, statusId});

        if (log.isDebugEnabled()) {
            log.debug("getAdmissionsInTeam: programId= " + programId + ",statusId=" + statusId + ",# results=" + results.size());
        }

        return results;
    }
}
