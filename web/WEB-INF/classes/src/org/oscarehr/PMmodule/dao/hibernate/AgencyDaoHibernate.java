package org.oscarehr.PMmodule.dao.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.dao.AgencyDao;
import org.oscarehr.PMmodule.model.Agency;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class AgencyDaoHibernate extends HibernateDaoSupport implements AgencyDao {

	private Log log = LogFactory.getLog(AgencyDaoHibernate.class);

	public Agency getAgency(Long agencyId) {
		if (agencyId == null || agencyId.longValue() < 0) {
			throw new IllegalArgumentException();
		}

		Agency agency = (Agency) getHibernateTemplate().get(Agency.class, agencyId);

		if (log.isDebugEnabled()) {
			log.debug("getAgency: agencyId=" + agencyId + ", found=" + (agency != null));
		}

		return agency;
	}

	public Agency getLocalAgency() {
		Agency agency = null;

		List results = getHibernateTemplate().find("from Agency a where a.Local = true");

		if (!results.isEmpty()) {
			agency = (Agency) results.get(0);
		}

		if (log.isDebugEnabled()) {
			log.debug("getLocalAgency:found=" + (agency != null));
		}

		return agency;
	}

	public void saveAgency(Agency agency) {
		if (agency == null) {
			throw new IllegalArgumentException();
		}

		getHibernateTemplate().saveOrUpdate(agency);

		if (log.isDebugEnabled()) {
			log.debug("saveAgency : id=" + agency.getId());
		}

	}

	public List getAgencies() {
		List results = this.getHibernateTemplate().find("from Agency");

		if (log.isDebugEnabled()) {
			log.debug("getAgencies : # of results=" + results.size());
		}

		return results;
	}

}
