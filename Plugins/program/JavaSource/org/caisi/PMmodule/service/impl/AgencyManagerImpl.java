package org.caisi.PMmodule.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.caisi.PMmodule.dao.AgencyDao;
import org.caisi.PMmodule.model.Agency;
import org.caisi.PMmodule.service.AgencyManager;

public class AgencyManagerImpl implements AgencyManager
{
	private static Log log = LogFactory.getLog(AgencyManagerImpl.class);
	private AgencyDao dao;
	
	public void setAgencyDao(AgencyDao dao)
	{
		this.dao = dao;
	}
	
	public Agency getAgency(String agencyId)
	{
		return dao.getAgency(agencyId);
	}

	public Agency getLocalAgency() {
		Agency agency =  dao.getLocalAgency();
		if(agency == null) {
			log.warn("No local agency has been saved.");
			return new Agency(new Long(0));
		}
		return agency;
	}

	public void saveLocalAgency(Agency agency) {
		log.debug("Saving agency information");
		dao.deleteLocalAgency();
		agency.setLocal(true);
		dao.saveAgency(agency);
	}
	
	public void saveAgency(Agency agency) {
		log.debug("Saving agency information");
		dao.saveAgency(agency);
	}

	public List getAgencies() {
		return null;
	}
}