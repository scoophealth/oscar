package org.oscarehr.PMmodule.dao;

import java.util.List;

import org.oscarehr.PMmodule.model.Agency;

public interface AgencyDao {

	public Agency getAgency(Long agencyId);

	public Agency getLocalAgency();

	public void saveAgency(Agency agency);

	public List getAgencies();
	
}
