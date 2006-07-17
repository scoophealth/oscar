package org.caisi.PMmodule.service;

import java.util.List;

import org.caisi.PMmodule.model.Provider;


public interface ProviderManager 
{
	public Provider getProvider(String providerNo);
	
	public String getProviderName(String providerNo);
	
	public List getProviders();
	
	public List getProvidersInfo();

	public boolean addProvider(Provider provider);

	public boolean updateProvider(Provider provider);

	public boolean removeProvider(String providerNo);
	
	public List search(String name);
	
	public List getProgramDomain(String providerNo);
	
	public List getAgencyDomain(String providerNo);

}
