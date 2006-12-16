package org.oscarehr.PMmodule.service;

import java.util.List;

import org.oscarehr.PMmodule.model.Provider;


public interface ProviderManager 
{
	public Provider getProvider(String providerNo);
	
	public String getProviderName(String providerNo);
	
	public List getProviders();
	
	public List search(String name);
	
	public List getProgramDomain(String providerNo);
	
	public List getAgencyDomain(String providerNo);
	
	public List getProvidersByType(String type);

}
