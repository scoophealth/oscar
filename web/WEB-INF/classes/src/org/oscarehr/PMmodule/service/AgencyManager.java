package org.oscarehr.PMmodule.service;

import org.oscarehr.PMmodule.model.Agency;

public interface AgencyManager {
	
	public Agency getAgency(String agencyId);

	public Agency getLocalAgency();

	public void saveLocalAgency(Agency agency);

	public void saveAgency(Agency agency);
	
}