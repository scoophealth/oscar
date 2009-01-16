package org.oscarehr.PMmodule.web;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.caisi_integrator.ws.client.CachedFacility;
import org.oscarehr.common.dao.IntegratorConsentDao;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.IntegratorConsent;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.SpringUtils;

public class ManageConsent {
	private static Logger logger = LogManager.getLogger(ManageConsent.class);
	private static CaisiIntegratorManager caisiIntegratorManager = (CaisiIntegratorManager) SpringUtils.getBean("caisiIntegratorManager");
	private static IntegratorConsentDao integratorConsentDao = (IntegratorConsentDao) SpringUtils.getBean("integratorConsentDao");

	private List<CachedFacility> allRemoteFacilities=null;
	private HashMap<Integer,IntegratorConsent> currentConsents=new HashMap<Integer,IntegratorConsent>();
	
	public ManageConsent(Facility facility, Provider provider, int clientId) throws MalformedURLException
	{
		allRemoteFacilities=caisiIntegratorManager.getRemoteFacilities(facility.getId());
		
		for (CachedFacility cachedFacility : allRemoteFacilities)
		{
			IntegratorConsent consent=integratorConsentDao.findLatestByFacilityDemographicAndRemoteFacility(facility.getId(), clientId, cachedFacility.getIntegratorFacilityId());
			if (consent!=null) currentConsents.put(consent.getIntegratorFacilityId(), consent);
		}
	}
	
	public List<CachedFacility> getAllRemoteFacilities()
	{
		return(allRemoteFacilities);
	}
	
	public boolean wasPreviouslyChecked(int remoteFacilityId, String consentField)
	{
		IntegratorConsent consent=currentConsents.get(remoteFacilityId);
		if (consent==null) return(false);
		
		if ("hic".equals(consentField)) return(consent.isRestrictConsentToHic());
		else if ("search".equals(consentField)) return(consent.isConsentToSearches());
		else if ("personal".equals(consentField)) return(consent.isConsentToBasicPersonalData());
		else if ("mental".equals(consentField)) return(consent.isConsentToMentalHealthData());
		else if ("hnr".equals(consentField)) return(consent.isConsentToHealthNumberRegistry());
		else logger.error("unexpected consent bit : "+consentField);
		
		return(false);
	}
}
