package org.oscarehr.PMmodule.dao;

import java.util.List;

import org.oscarehr.PMmodule.model.Provider;

public interface ProviderDao {
	
	public boolean providerExists(String providerNo);
	
	public Provider getProvider(String providerNo);

	public String getProviderName(String providerNo);

	public List getProviders();

	public List search(String name);

	public List getProvidersByType(String type);
	
}
