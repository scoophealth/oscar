package org.oscarehr.PMmodule.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.dao.AgencyDao;
import org.oscarehr.PMmodule.model.Agency;
import org.oscarehr.PMmodule.service.AgencyManager;

public class AgencyManagerImpl implements AgencyManager {
	
	private static Log log = LogFactory.getLog(AgencyManagerImpl.class);

	private AgencyDao dao;

	public void setAgencyDao(AgencyDao dao) {
		this.dao = dao;
	}

	public Agency getAgency(String agencyId) {
		return dao.getAgency(Long.valueOf(agencyId));
	}

	public Agency getLocalAgency() {
		Agency agency = dao.getLocalAgency();
		
		if (agency == null) {
			log.warn("No local agency has been saved.");
			return new Agency(new Long(0));
		}
		
		return agency;
	}

	public void saveLocalAgency(Agency agency) {
		log.debug("Saving agency information");

		agency.setLocal(true);
		dao.saveAgency(agency);
	}

	public void saveAgency(Agency agency) {
		dao.saveAgency(agency);
	}
}