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
import java.util.Collection;
import java.util.List;

import org.oscarehr.PMmodule.model.ProgramClientRestriction;
import org.oscarehr.common.dao.DemographicDao;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 */
public class ProgramClientRestrictionDAO extends HibernateDaoSupport {
    private DemographicDao demographicDao;
    private ProgramDao programDao;
    private ProviderDao providerDao;

    public Collection<ProgramClientRestriction> find(int programId, int demographicNo) {

        List<ProgramClientRestriction> pcrs = getHibernateTemplate().find("from ProgramClientRestriction pcr where pcr.enabled = true and pcr.programId = ? and pcr.demographicNo = ? order by pcr.startDate", new Object[]{programId, demographicNo});
        for (ProgramClientRestriction pcr : pcrs) {
            setRelationships(pcr);
        }
        return pcrs;
    }

    public void save(ProgramClientRestriction restriction) {
        getHibernateTemplate().saveOrUpdate(restriction);
    }

    public ProgramClientRestriction find(int restrictionId) {
        return setRelationships(getHibernateTemplate().get(ProgramClientRestriction.class, restrictionId));
    }

    public Collection<ProgramClientRestriction> findForProgram(int programId) {
        Collection<ProgramClientRestriction> pcrs = getHibernateTemplate().find("from ProgramClientRestriction pcr where pcr.enabled = true and pcr.programId = ? order by pcr.demographicNo", programId);
        for (ProgramClientRestriction pcr : pcrs) {
            setRelationships(pcr);
        }
        return pcrs;
    }

    public Collection<ProgramClientRestriction> findDisabledForProgram(int programId) {
        Collection<ProgramClientRestriction> pcrs = getHibernateTemplate().find("from ProgramClientRestriction pcr where pcr.enabled = false and pcr.programId = ? order by pcr.demographicNo", programId);
        for (ProgramClientRestriction pcr : pcrs) {
            setRelationships(pcr);
        }
        return pcrs;
    }

    public Collection<ProgramClientRestriction> findForClient(int demographicNo) {
        Collection<ProgramClientRestriction> pcrs = getHibernateTemplate().find("from ProgramClientRestriction pcr where pcr.enabled = true and pcr.demographicNo = ? order by pcr.programId", demographicNo);
        for (ProgramClientRestriction pcr : pcrs) {
            setRelationships(pcr);
        }
        return pcrs;
    }

    public Collection<ProgramClientRestriction> findForClient(int demographicNo, int facilityId) {
        ArrayList<Object> paramList = new ArrayList<Object>();
        String sSQL="from ProgramClientRestriction pcr where pcr.enabled = true and " +
  		 "pcr.demographicNo = ? and pcr.programId in (select s.id from Program s where s.facilityId = ? or s.facilityId is null) " +
         "order by pcr.programId";
          paramList.add(Integer.valueOf(demographicNo));
          paramList.add(facilityId);
          Object params[] = paramList.toArray(new Object[paramList.size()]);
          Collection<ProgramClientRestriction> pcrs= getHibernateTemplate().find(sSQL, params);
          for (ProgramClientRestriction pcr : pcrs) {
              setRelationships(pcr);
          }
          return pcrs;
/*
    	Collection<ProgramClientRestriction> pcrs = getHibernateTemplate().find("from ProgramClientRestriction pcr where pcr.enabled = true and pcr.demographicNo = ? order by pcr.programId", demographicNo);
        for (ProgramClientRestriction pcr : pcrs) {
            setRelationships(pcr);
        }
        return pcrs;
*/
    }

    public Collection<ProgramClientRestriction> findDisabledForClient(int demographicNo) {
        Collection<ProgramClientRestriction> pcrs = getHibernateTemplate().find("from ProgramClientRestriction pcr where pcr.enabled = false and pcr.demographicNo = ? order by pcr.programId", demographicNo);
        for (ProgramClientRestriction pcr : pcrs) {
            setRelationships(pcr);
        }
        return pcrs;
    }

    private ProgramClientRestriction setRelationships(ProgramClientRestriction pcr) {
        pcr.setClient(demographicDao.getDemographic("" + pcr.getDemographicNo()));
        pcr.setProgram(programDao.getProgram(pcr.getProgramId()));
        pcr.setProvider(providerDao.getProvider(pcr.getProviderNo()));

        return pcr;
    }

    @Required
    public void setDemographicDao(DemographicDao demographicDao) {
        this.demographicDao = demographicDao;
    }

    @Required
    public void setProgramDao(ProgramDao programDao) {
        this.programDao = programDao;
    }

    @Required
    public void setProviderDao(ProviderDao providerDao) {
        this.providerDao = providerDao;
    }

}
