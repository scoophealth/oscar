package org.oscarehr.PMmodule.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.caisi.dao.DemographicDAO;
import org.oscarehr.PMmodule.model.ProgramClientRestriction;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.Collection;
import java.util.List;

/**
 */
public class ProgramClientRestrictionDAO extends HibernateDaoSupport {
    private DemographicDAO demographicDAOT;
    private ProgramDao programDao;
    private ProviderDao providerDao;

    private static Log log = LogFactory.getLog(ProgramClientRestrictionDAO.class);

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


    public Collection<ProgramClientRestriction> findForClient(int demographicNo) {
        Collection<ProgramClientRestriction> pcrs = getHibernateTemplate().find("from ProgramClientRestriction pcr where pcr.enabled = true and pcr.demographicNo = ? order by pcr.programId", demographicNo);
        for (ProgramClientRestriction pcr : pcrs) {
            setRelationships(pcr);
        }
        return pcrs;
    }

    private ProgramClientRestriction setRelationships(ProgramClientRestriction pcr) {
        pcr.setClient(demographicDAOT.getDemographic("" + pcr.getDemographicNo()));
        pcr.setProgram(programDao.getProgram(pcr.getProgramId()));
        pcr.setProvider(providerDao.getProvider(pcr.getProviderNo()));
        
        return pcr;
    }

    @Required
    public void setDemographicDAOT(DemographicDAO demographicDAOT) {
        this.demographicDAOT = demographicDAOT;
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
