package org.caisi.PMmodule.service;

import java.util.List;

import org.caisi.PMmodule.model.Agency;


public interface AgencyManager 
{
	public Agency getAgency(String agencyId);
	
	public Agency getLocalAgency();

	public void saveLocalAgency(Agency agency);
	
	public void saveAgency(Agency agency);
	
	public List getAgencies();

}
