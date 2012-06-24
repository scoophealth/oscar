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
import org.oscarehr.PMmodule.model.ProgramQueue;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ProgramQueueDao extends HibernateDaoSupport {

    private Logger log=MiscUtils.getLogger();


    public ProgramQueue getProgramQueue(Long queueId) {
        if (queueId == null || queueId.intValue() <= 0) {
            throw new IllegalArgumentException();
        }

        ProgramQueue result = getHibernateTemplate().get(ProgramQueue.class, queueId);

        if (log.isDebugEnabled()) {
            log.debug("getProgramQueue: queueId=" + queueId + ",found=" + (result != null));
        }

        return result;
    }

    public List<ProgramQueue> getProgramQueuesByProgramId(Long programId) {
        if (programId == null) {
            throw new IllegalArgumentException();
        }

        String queryStr = " FROM ProgramQueue q WHERE q.ProgramId=? ORDER BY  q.Id  ";
        List results = getHibernateTemplate().find(queryStr, programId);

        if (log.isDebugEnabled()) {
            log.debug("getProgramQueue: programId=" + programId + ",# of results=" + results.size());
        }

        return results;
    }

    public List<ProgramQueue> getActiveProgramQueuesByProgramId(Long programId) {
        if (programId == null) {
            throw new IllegalArgumentException();
        }

        List results = this.getHibernateTemplate().find("from ProgramQueue pq where pq.ProgramId = ? and pq.Status = 'active' order by pq.ReferralDate", Long.valueOf(programId));

        if (log.isDebugEnabled()) {
            log.debug("getActiveProgramQueuesByProgramId: programId=" + programId + ",# of results=" + results.size());
        }

        return results;
    }

    public void saveProgramQueue(ProgramQueue programQueue) {
        if (programQueue == null) {
            return;
        }

        getHibernateTemplate().saveOrUpdate(programQueue);

        if (log.isDebugEnabled()) {
            log.debug("saveProgramQueue: id=" + programQueue.getId());
        }

    }

    public ProgramQueue getQueue(Long programId, Long clientId) {
        if (programId == null) {
            throw new IllegalArgumentException();
        }
        if (clientId == null) {
            throw new IllegalArgumentException();
        }

        ProgramQueue result = null;
        List results = this.getHibernateTemplate().find("from ProgramQueue pq where pq.ProgramId = ? and pq.ClientId = ?",
                new Object[]{Long.valueOf(programId), Long.valueOf(clientId)});

        if (!results.isEmpty()) {
            result = (ProgramQueue) results.get(0);
        }

        if (log.isDebugEnabled()) {
            log.debug("getQueue: programId=" + programId + ",clientId=" + clientId + ",found=" + (result != null));
        }

        return result;
    }

    public ProgramQueue getActiveProgramQueue(Long programId, Long demographicNo) {
        if (programId == null || programId.intValue() <= 0) {
            throw new IllegalArgumentException();
        }
        if (demographicNo == null || demographicNo.intValue() <= 0) {
            throw new IllegalArgumentException();
        }

        ProgramQueue result = null;

        List results = this.getHibernateTemplate().find("from ProgramQueue pq where pq.ProgramId = ? and pq.ClientId = ? and pq.Status='active'",
                new Object[]{programId, demographicNo});
        if (!results.isEmpty()) {
            result = (ProgramQueue) results.get(0);
        }

        if (log.isDebugEnabled()) {
            log.debug("getActiveProgramQueue: programId=" + programId + ",demogaphicNo=" + demographicNo + ",found=" + (result != null));
        }

        return result;
    }
}
