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
import org.oscarehr.PMmodule.model.ProgramTeam;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ProgramTeamDAO extends HibernateDaoSupport {

    private Logger log=MiscUtils.getLogger();

    /*
     * (non-Javadoc)
     *
     * @see org.oscarehr.PMmodule.dao.ProgramTeamDAO#teamExists(java.lang.Integer)
     */
    public boolean teamExists(Integer teamId) {
        boolean exists = getHibernateTemplate().get(ProgramTeam.class, teamId) != null;
        log.debug("teamExists: " + exists);

        return exists;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.oscarehr.PMmodule.dao.ProgramTeamDAO#teamNameExists(java.lang.Integer, java.lang.String)
     */
    public boolean teamNameExists(Integer programId, String teamName) {
        if (programId == null || programId.intValue() <= 0) {
            throw new IllegalArgumentException();
        }

        if (teamName == null || teamName.length() <= 0) {
            throw new IllegalArgumentException();
        }
        Session session = getSession();
        Query query = session.createQuery("select pt.id from ProgramTeam pt where pt.programId = ? and pt.name = ?");
        query.setLong(0, programId.longValue());
        query.setString(1, teamName);

        List teams = new ArrayList();
        try {
        	teams = query.list();
        }finally{
        	this.releaseSession(session);
        }

        if (log.isDebugEnabled()) {
            log.debug("teamNameExists: programId = " + programId + ", teamName = " + teamName + ", result = " + !teams.isEmpty());
        }

        return !teams.isEmpty();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.oscarehr.PMmodule.dao.ProgramTeamDAO#getProgramTeam(java.lang.Integer)
     */
    public ProgramTeam getProgramTeam(Integer id) {
        if (id == null || id.intValue() <= 0) {
            throw new IllegalArgumentException();
        }

        ProgramTeam result = this.getHibernateTemplate().get(ProgramTeam.class, id);

        if (log.isDebugEnabled()) {
            log.debug("getProgramTeam: id=" + id + ",found=" + (result != null));
        }

        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.oscarehr.PMmodule.dao.ProgramTeamDAO#getProgramTeams(java.lang.Integer)
     */
    public List<ProgramTeam> getProgramTeams(Integer programId) {
        if (programId == null || programId.intValue() <= 0) {
            throw new IllegalArgumentException();
        }

        List<ProgramTeam> results = this.getHibernateTemplate().find("from ProgramTeam tp where tp.programId = ?", programId);

        if (log.isDebugEnabled()) {
            log.debug("getProgramTeams: programId=" + programId + ",# of results=" + results.size());
        }

        return results;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.oscarehr.PMmodule.dao.ProgramTeamDAO#saveProgramTeam(org.oscarehr.PMmodule.model.ProgramTeam)
     */
    public void saveProgramTeam(ProgramTeam team) {
        if (team == null) {
            throw new IllegalArgumentException();
        }

        this.getHibernateTemplate().saveOrUpdate(team);

        if (log.isDebugEnabled()) {
            log.debug("saveProgramTeam: id=" + team.getId());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.oscarehr.PMmodule.dao.ProgramTeamDAO#deleteProgramTeam(java.lang.Integer)
     */
    public void deleteProgramTeam(Integer id) {
        if (id == null || id.intValue() <= 0) {
            throw new IllegalArgumentException();
        }

        this.getHibernateTemplate().delete(getProgramTeam(id));

        if (log.isDebugEnabled()) {
            log.debug("deleteProgramTeam: id=" + id);
        }
    }

}
