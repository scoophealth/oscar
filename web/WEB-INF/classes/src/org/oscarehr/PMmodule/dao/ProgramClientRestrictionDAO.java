package org.oscarehr.PMmodule.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.model.ProgramClientRestriction;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 */
public class ProgramClientRestrictionDAO extends HibernateDaoSupport {
    private DemographicDao demographicDao;
    private ProgramDao programDao;
    private ProviderDao providerDao;

    private static Logger log = MiscUtils.getLogger();

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
        return setRelationships((ProgramClientRestriction) getHibernateTemplate().get(ProgramClientRestriction.class, restrictionId));
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
        ArrayList paramList = new ArrayList();
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
