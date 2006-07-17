package org.caisi.PMmodule.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.caisi.PMmodule.dao.AgencyDao;
import org.caisi.PMmodule.dao.ProgramProviderDAO;
import org.caisi.PMmodule.dao.ProviderDao;
import org.caisi.PMmodule.model.Agency;
import org.caisi.PMmodule.model.Provider;
import org.caisi.PMmodule.service.ProviderManager;


public class ProviderManagerImpl implements ProviderManager
{
//	private static Log log = LogFactory.getLog(ProviderManagerImpl.class);
	private ProviderDao dao;
	private AgencyDao agencyDAO;
	private ProgramProviderDAO programProviderDAO;
	
	
	public void setProviderDao(ProviderDao dao)
	{
		this.dao = dao;
	}
	
	public void setAgencyDAO(AgencyDao dao) {
		this.agencyDAO = dao;
	}
	
	public void setProgramProviderDAO(ProgramProviderDAO dao) {
		this.programProviderDAO = dao;
	}
	
	public Provider getProvider(String providerNo)
	{
		return dao.getProvider(providerNo);
	}
	
	public String getProviderName(String providerNo)
	{
		return dao.getProviderName(providerNo);
	}
	
	public List getProviders()
	{
		return dao.getProviders();
	}

	public List getProvidersInfo()
	{
		return dao.getProvidersInfo();
	}

	public boolean addProvider(Provider provider)
	{
		return dao.addProvider(provider);
	}

	public boolean updateProvider(Provider provider)
	{
		return dao.updateProvider(provider);
	}

	public boolean removeProvider(String providerNo)
	{
		return dao.removeProvider(providerNo);
	}

	public List search(String name) {
		return dao.search(name);
	}
	
	public List getProgramDomain(String providerNo) {
		return programProviderDAO.getProgramDomain(Long.valueOf(providerNo));
	}
	
	public List getAgencyDomain(String providerNo) {
		Agency localAgency =  agencyDAO.getLocalAgency();
		List agencies = new ArrayList();
		agencies.add(localAgency);
		return agencies;
	}
}
