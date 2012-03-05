package org.oscarehr.managers;

import java.util.List;

import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.model.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.log.LogAction;


@Service
public class ProviderManager2
{
	@Autowired
	private ProviderDao providerDao;
	
	public List<Provider> getProviders(boolean active)
	{
		List<Provider> results=providerDao.getProviders(active);
		
		//--- log action ---
		LogAction.addLogSynchronous("ProviderManager.getProviders, active="+active, null);
		
		return(results);
	}
}
