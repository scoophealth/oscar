package org.caisi.service;

import java.util.List;

import org.caisi.dao.ProviderDAO;
import org.oscarehr.PMmodule.model.Provider;

/**
 * Manager Interface for Providers
 * Implementing class will provide the business logic
 *
 * @author Marc Dumontier <a href="mailto:marc@mdumontier.com">marc@mdumontier.com</a>
 *
 */
public interface ProviderManagerTickler {
	
	public void setProviderDAO(ProviderDAO dao);
	
	/**
	 * Get all providers in the system
	 * @return a List of Provider objects
	 */
	public List getProviders();
	
	/**
	 * Find a specific provider
	 * @param provider_no
	 * @return The provider
	 */
	public Provider getProvider(String provider_no);
	
	
	public List getProgramDomain(String providerNo);
}
