package org.caisi.service.impl;

import java.util.List;

import org.caisi.dao.ProviderDAO;
import org.caisi.model.Provider;
import org.caisi.service.ProviderManagerTickler;

/**
 * Implements the ProviderManagerTickler interface 
 * @author Marc Dumontier <a href="mailto:marc@mdumontier.com">marc@mdumontier.com</a>
 *
 */
public class ProviderManagerImplTickler implements ProviderManagerTickler {

	private ProviderDAO providerDAO = null;
	
	public void setProviderDAO(ProviderDAO providerDAO) {
		this.providerDAO = providerDAO;
	}
	public List getProviders() {
		return providerDAO.getProviders();
	}
	public Provider getProvider(String provider_no) {
		return providerDAO.getProvider(provider_no);
	}

}
