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

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.model.Facility;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ProgramProviderDAO extends HibernateDaoSupport {

    private Log log = LogFactory.getLog(ProgramProviderDAO.class);

    public List getProgramProviders(Long programId) {
        if (programId == null || programId.intValue() < 0) {
            throw new IllegalArgumentException();
        }

        List results = this.getHibernateTemplate().find("from ProgramProvider pp where pp.ProgramId = ?", programId);

        if (log.isDebugEnabled()) {
            log.debug("getProgramProviders: programId=" + programId + ",# of results=" + results.size());
        }
        return results;
    }

    public List getProgramProvidersByProvider(String providerNo) {
        if (providerNo == null || Integer.valueOf(providerNo) <= 0) {
            throw new IllegalArgumentException();
        }

        List results = this.getHibernateTemplate().find("from ProgramProvider pp where pp.ProviderNo = ?", providerNo);

        if (log.isDebugEnabled()) {
            log.debug("getProgramProvidersByProvider: providerNo=" + providerNo + ",# of results=" + results.size());
        }
        return results;
    }

    public ProgramProvider getProgramProvider(Long id) {
        if (id == null || id.intValue() <= 0) {
            throw new IllegalArgumentException();
        }

        ProgramProvider result = (ProgramProvider)this.getHibernateTemplate().get(ProgramProvider.class, id);

        if (log.isDebugEnabled()) {
            log.debug("getProgramProvider: id=" + id + ",found=" + (result != null));
        }

        return result;
    }

    public ProgramProvider getProgramProvider(String providerNo, Long programId) {
        if (providerNo == null || Integer.parseInt(providerNo) <= 0) {
            throw new IllegalArgumentException();
        }
        if (programId == null || programId.intValue() <= 0) {
            throw new IllegalArgumentException();
        }

        ProgramProvider result = null;
        List results = this.getHibernateTemplate().find("from ProgramProvider pp where pp.ProviderNo = ? and pp.ProgramId = ?", new Object[] { providerNo, programId });
        if (!results.isEmpty()) {
            result = (ProgramProvider) results.get(0);
        }

        if (log.isDebugEnabled()) {
            log.debug("getProgramProvider: providerNo=" + providerNo + ",programId=" + programId + ",found=" + (result != null));
        }

        return result;
    }

    public void saveProgramProvider(ProgramProvider pp) {
        if (pp == null) {
            throw new IllegalArgumentException();
        }

        this.getHibernateTemplate().saveOrUpdate(pp);

        if (log.isDebugEnabled()) {
            log.debug("saveProgramProvider: id=" + pp.getId());
        }

    }

    public void deleteProgramProvider(Long id) {
        if (id == null || id.intValue() <= 0) {
            throw new IllegalArgumentException();
        }

        Object o = getProgramProvider(id);
        if (o != null) {
            this.getHibernateTemplate().delete(o);
        }

        if (log.isDebugEnabled()) {
            log.debug("deleteProgramProvider id=" + id);
        }
    }

    public void deleteProgramProviderByProgramId(Long programId) {
        if (programId == null || programId.intValue() <= 0) {
            throw new IllegalArgumentException();
        }

        List o = getProgramProviders(programId);
        if (o != null) {
            Iterator it = o.iterator();
            while (it.hasNext()) {
                Object o1 = it.next();
                this.getHibernateTemplate().delete(o1);
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("deleteProgramProvider programId=" + programId);
        }
    }

    public List getProgramProvidersInTeam(Integer programId, Integer teamId) {
        if (programId == null || programId <= 0) {
            throw new IllegalArgumentException();
        }
        if (teamId == null || teamId <= 0) {
            throw new IllegalArgumentException();
        }
        Long pId = programId.longValue();

        List results = this.getHibernateTemplate().find("select pp from ProgramProvider pp left join pp.teams as team where pp.ProgramId = ? and team.id = ?", new Object[] {pId, teamId});

        if (log.isDebugEnabled()) {
            log.debug("getProgramProvidersInTeam: programId=" + programId + ",teamId=" + teamId + ",# of results=" + results.size());
        }

        return results;
    }



    @SuppressWarnings("unchecked")
    public List<ProgramProvider> getProgramDomain(String providerNo) {
        if (providerNo == null || Long.valueOf(providerNo) == null) {
            throw new IllegalArgumentException();
        }

        List results = this.getHibernateTemplate().find("from ProgramProvider pp where pp.ProviderNo = ?", providerNo);

        if (log.isDebugEnabled()) {
            log.debug("getProgramDomain: providerNo=" + providerNo + ",# of results=" + results.size());
        }
        return results;
    }

    @SuppressWarnings("unchecked")
    public List<Facility> getFacilitiesInProgramDomain(String providerNo) {
        if (providerNo == null || Long.valueOf(providerNo) == null) {
            throw new IllegalArgumentException();
        }
        List results = this.getHibernateTemplate().find("select distinct f from Facility f, Room r, ProgramProvider pp where pp.ProgramId = r.programId and f.id = r.facilityId and pp.ProviderNo = ?", providerNo);

        return results;
    }
}
