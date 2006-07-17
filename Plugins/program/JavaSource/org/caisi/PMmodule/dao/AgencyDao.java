

package org.caisi.PMmodule.dao;


import java.util.List;

import org.caisi.PMmodule.model.Agency;

//###############################################################################

public interface AgencyDao
{
	public Agency getAgency(String agencyId);
	
	public Agency getLocalAgency();
	
	public void saveAgency(Agency agency);
	
	public List getAgencies();
	
	public void deleteLocalAgency();
	
}

