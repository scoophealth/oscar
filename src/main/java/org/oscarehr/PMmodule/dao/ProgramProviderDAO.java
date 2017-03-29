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
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.QueueCache;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ProgramProviderDAO extends HibernateDaoSupport {

    private Logger log=MiscUtils.getLogger();

	private static QueueCache<String, List<ProgramProvider>> programProviderByProviderProgramIdCache = new QueueCache<String, List<ProgramProvider>>(4, 100, DateUtils.MILLIS_PER_HOUR, null);

	private static String makeCacheKey(String providerNo, Long programId)
	{
		return(providerNo+':'+programId);
	}

	@SuppressWarnings("unchecked")
    public List<ProgramProvider> getProgramProviderByProviderProgramId(String providerNo, Long programId) {
    	String cacheKey=makeCacheKey(providerNo, programId);

		List<ProgramProvider> results = programProviderByProviderProgramIdCache.get(cacheKey);

    	if (results==null)
    	{
    		String q = "select pp from ProgramProvider pp where pp.ProgramId=? and pp.ProviderNo=?";
    		results=getHibernateTemplate().find(q, new Object[] {programId, providerNo});
			if (results != null) programProviderByProviderProgramIdCache.put(cacheKey, results);
    	}

        return results;
    }

	@SuppressWarnings("unchecked")
	public List<ProgramProvider> getAllProgramProviders() {
		return getHibernateTemplate().find("FROM ProgramProvider");
	}

    @SuppressWarnings("unchecked")
    public List<ProgramProvider> getProgramProviderByProviderNo(String providerNo) {
        String q = "select pp from ProgramProvider pp where pp.ProviderNo=?";
        return getHibernateTemplate().find(q, providerNo);
    }

    public List<ProgramProvider> getProgramProviders(Long programId) {
        if (programId == null || programId.intValue() < 0) {
            throw new IllegalArgumentException();
        }

        @SuppressWarnings("unchecked")
        List<ProgramProvider> results = this.getHibernateTemplate().find("from ProgramProvider pp where pp.ProgramId = ?", programId);

        if (log.isDebugEnabled()) {
            log.debug("getProgramProviders: programId=" + programId + ",# of results=" + results.size());
        }
        return results;
    }

    public List<ProgramProvider> getProgramProvidersByProvider(String providerNo) {
        if (providerNo == null) {
            throw new IllegalArgumentException();
        }

        List<ProgramProvider> results = this.getHibernateTemplate().find("from ProgramProvider pp where pp.ProviderNo = ?", providerNo);

        if (log.isDebugEnabled()) {
            log.debug("getProgramProvidersByProvider: providerNo=" + providerNo + ",# of results=" + results.size());
        }
        return results;
    }

    public List getProgramProvidersByProviderAndFacility(String providerNo, Integer facilityId) {
        if (providerNo == null) {
            throw new IllegalArgumentException();
        }

        String queryStr = "from ProgramProvider pp where pp.ProviderNo = ? and pp.ProgramId in " +
                      "(select s.id from Program s where s.facilityId=? or s.facilityId is null)";
        List results = getHibernateTemplate().find(queryStr, new Object[] { providerNo, facilityId });

        if (log.isDebugEnabled()) {
            log.debug("getProgramProvidersByProviderAndFacility: providerNo=" + providerNo + ",# of results=" + results.size());
        }
        return results;
    }

    public ProgramProvider getProgramProvider(Long id) {
        if (id == null || id.intValue() < 0) {
            throw new IllegalArgumentException();
        }

        ProgramProvider result = this.getHibernateTemplate().get(ProgramProvider.class, id);

        if (log.isDebugEnabled()) {
            log.debug("getProgramProvider: id=" + id + ",found=" + (result != null));
        }

        return result;
    }

    public ProgramProvider getProgramProvider(String providerNo, Long programId) {
        if (providerNo == null) {
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

	public ProgramProvider getProgramProvider(String providerNo, long programId, long roleId) {

    	ProgramProvider result = null;

    	@SuppressWarnings("unchecked")
        List<ProgramProvider> results = getHibernateTemplate().find("from ProgramProvider pp where pp.ProviderNo = ? and pp.ProgramId = ? and pp.RoleId=?", new Object[] { providerNo, programId, roleId });

        if (!results.isEmpty()) {
            result = results.get(0);
        }

        return result;
    }

    public void saveProgramProvider(ProgramProvider pp) {
        if (pp == null) {
            throw new IllegalArgumentException();
        }

        programProviderByProviderProgramIdCache.remove(makeCacheKey(pp.getProviderNo(), pp.getProgramId()));
        getHibernateTemplate().saveOrUpdate(pp);

        if (log.isDebugEnabled()) {
            log.debug("saveProgramProvider: id=" + pp.getId());
        }

    }

    public void deleteProgramProvider(Long id) {
        if (id == null || id.intValue() < 0) {
            throw new IllegalArgumentException();
        }

        ProgramProvider pp = getProgramProvider(id);
        if (pp != null) {
            programProviderByProviderProgramIdCache.remove(makeCacheKey(pp.getProviderNo(), pp.getProgramId()));
            getHibernateTemplate().delete(pp);
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
            	ProgramProvider pp = (ProgramProvider) it.next();
                programProviderByProviderProgramIdCache.remove(makeCacheKey(pp.getProviderNo(), pp.getProgramId()));
                getHibernateTemplate().delete(pp);
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("deleteProgramProvider programId=" + programId);
        }
    }

    public List<ProgramProvider> getProgramProvidersInTeam(Integer programId, Integer teamId) {
        if (programId == null || programId <= 0) {
            throw new IllegalArgumentException();
        }
        if (teamId == null || teamId <= 0) {
            throw new IllegalArgumentException();
        }
        Long pId = programId.longValue();

        List<ProgramProvider> results = this.getHibernateTemplate().find("select pp from ProgramProvider pp left join pp.teams as team where pp.ProgramId = ? and team.id = ?", new Object[] {pId, teamId});

        if (log.isDebugEnabled()) {
            log.debug("getProgramProvidersInTeam: programId=" + programId + ",teamId=" + teamId + ",# of results=" + results.size());
        }

        return results;
    }



    @SuppressWarnings("unchecked")
    public List<ProgramProvider> getProgramDomain(String providerNo) {
        if (providerNo == null) {
            throw new IllegalArgumentException();
        }

        List results = this.getHibernateTemplate().find("from ProgramProvider pp where pp.ProviderNo = ?", providerNo);

        if (log.isDebugEnabled()) {
            log.debug("getProgramDomain: providerNo=" + providerNo + ",# of results=" + results.size());
        }
        return results;
    }

    public List<ProgramProvider> getActiveProgramDomain(String providerNo) {
        if (providerNo == null || Long.valueOf(providerNo) == null) {
            throw new IllegalArgumentException();
        }

        List results = this.getHibernateTemplate().find("select pp from ProgramProvider pp, Program p where pp.ProgramId=p.id and p.programStatus='active' and pp.ProviderNo = ?", providerNo);

        if (log.isDebugEnabled()) {
            log.debug("getProgramDomain: providerNo=" + providerNo + ",# of results=" + results.size());
        }
        return results;
    }

    public List<ProgramProvider> getProgramDomainByFacility(String providerNo, Integer facilityId) {
        if (providerNo == null || Long.valueOf(providerNo) == null) {
            throw new IllegalArgumentException();
        }

        String queryStr = "from ProgramProvider pp where pp.ProviderNo = ? and pp.ProgramId in " +
                    "(select s.id from Program s where s.facilityId=? or s.facilityId is null)";
        List results = getHibernateTemplate().find(queryStr, new Object[] { providerNo, facilityId });

        if (log.isDebugEnabled()) {
            log.debug("getProgramDomainByFacility: providerNo=" + providerNo + ",# of results=" + results.size());
        }
        return results;
    }

    public boolean isThisProgramInProgramDomain(String providerNo, Integer programId)
	{
		if (providerNo == null || Long.valueOf(providerNo) == null)
		{
			throw new IllegalArgumentException();
		}

		String queryStr = "from ProgramProvider pp where pp.ProviderNo = ? and pp.ProgramId = ?";
		List results = getHibernateTemplate().find(queryStr, new Object[]{providerNo, Long.valueOf(programId.longValue())});
		if(results!=null && results.size()>0) {
			return true;
		} else {
			return false;
		}

	}


    @SuppressWarnings("unchecked")
    public List<Facility> getFacilitiesInProgramDomain(String providerNo) {
        if (providerNo == null || Long.valueOf(providerNo) == null) {
            throw new IllegalArgumentException();
        }
        List results = this.getHibernateTemplate().find("select distinct f from Facility f, Room r, ProgramProvider pp where pp.ProgramId = r.programId and f.id = r.facilityId and pp.ProviderNo = ?", providerNo);

        return results;
    }



	public void updateProviderRoles(Long providerId, Long roleId) {
		getHibernateTemplate().bulkUpdate("UPDATE ProgramProvider pp SET pp.RoleId = ? WHERE pp.Id = ?", new Object[] { roleId, providerId });
	}
	
	@SuppressWarnings("unchecked")
    public List<Provider> getProvidersByPrograms(List<Integer> programNos) {
	
		List<Long> lProgramNos = new ArrayList<Long>();
		for(Integer i:programNos) {
			lProgramNos.add(i.longValue());
		}
		List<Provider> results = new ArrayList<Provider>();
		String queryString = "select distinct p from ProgramProvider pp, Provider p WHERE pp.ProgramId IN (:domain) and pp.ProviderNo = p.ProviderNo order by p.LastName, p.FirstName";
		Session session = this.getSession();
		try {
			Query q = session.createQuery(queryString);
			q.setParameterList("domain", lProgramNos);
			
			results = q.list();
		}  finally {
			this.releaseSession(session);
		}
       
        return results;
    }

	
}
