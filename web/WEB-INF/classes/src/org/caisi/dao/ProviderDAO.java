package org.caisi.dao;

import java.util.List;

import org.caisi.model.Provider;

/**
 * DAO interface for working with Provider table in OSCAR
 * @author Marc Dumontier <a href="mailto:marc@mdumontier.com">marc@mdumontier.com</a>
 *
 */
public interface ProviderDAO extends DAO {
	
	/**
	 * Get all the providers
	 * @return The list of providers
	 */
	public List getProviders();
	
	/**
	 * Get provider using it's primary id.
	 * @param provider_no The Id
	 * @return The Provider object
	 */
	public Provider getProvider(String provider_no);
	
	/**
	 * Search for a provider using first and last names
	 * @param lastName Last Name
	 * @param firstName First Name
	 * @return A Provider object if found
	 */
	public Provider getProviderByName(String lastName,String firstName);
	
}
