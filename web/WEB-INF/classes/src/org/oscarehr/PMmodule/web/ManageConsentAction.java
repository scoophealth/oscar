package org.oscarehr.PMmodule.web;

import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.caisi_integrator.ws.client.CachedFacility;
import org.oscarehr.common.dao.IntegratorConsentDao;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.IntegratorConsent;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.SpringUtils;

public class ManageConsentAction {
	private static Logger logger = LogManager.getLogger(ManageConsent.class);
	private static CaisiIntegratorManager caisiIntegratorManager = (CaisiIntegratorManager) SpringUtils.getBean("caisiIntegratorManager");
	private static IntegratorConsentDao integratorConsentDao = (IntegratorConsentDao) SpringUtils.getBean("integratorConsentDao");

	private HashMap<Integer,IntegratorConsent> consents=new HashMap<Integer,IntegratorConsent>();
	
	public ManageConsentAction(Facility facility, Provider provider, int clientId) throws MalformedURLException
	{
		for (CachedFacility cachedFacility : caisiIntegratorManager.getRemoteFacilities(facility.getId()))
		{
			IntegratorConsent consent=new IntegratorConsent();
		    consent.setIntegratorFacilityId(cachedFacility.getIntegratorFacilityId());
		    consent.setFormVersion("DETAILED");
		    consent.setCreatedDate(new Date());
		    consent.setDemographicId(clientId);
		    consent.setFacilityId(facility.getId());
		    consent.setProviderNo(provider.getProviderNo());
		    consents.put(cachedFacility.getIntegratorFacilityId(),consent);
		}
	}
	
	/**
	 * @param s is of the format "consent.<remoteFacilityId>.<consentField>", i.e. "consent.1.hic"
	 */
	public void addConsent(String s)
	{
		String[] splitTemp=s.split("\\.");
		int remoteFacilityId=Integer.parseInt(splitTemp[1]);
		
		IntegratorConsent consent=consents.get(remoteFacilityId);
		
		if ("hic".equals(splitTemp[2])) consent.setRestrictConsentToHic(true);
		else if ("search".equals(splitTemp[2])) consent.setConsentToSearches(true);
		else if ("personal".equals(splitTemp[2])) consent.setConsentToBasicPersonalData(true);
		else if ("mental".equals(splitTemp[2])) consent.setConsentToMentalHealthData(true);
		else if ("hnr".equals(splitTemp[2])) consent.setConsentToHealthNumberRegistry(true);
		else logger.error("unexpected consent bit : "+s);
	}
	
	public void storeAllConsents()
	{
	    for (IntegratorConsent consent : consents.values())
	    {
	    	integratorConsentDao.persist(consent);
	    }
	}

}
