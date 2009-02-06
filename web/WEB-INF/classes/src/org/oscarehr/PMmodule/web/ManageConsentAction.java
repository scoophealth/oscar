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

	private HashMap<Integer, IntegratorConsent> consents = new HashMap<Integer, IntegratorConsent>();

	public ManageConsentAction(Facility facility, Provider provider, Integer clientId, String formType) throws MalformedURLException {
		try {
			for (CachedFacility cachedFacility : caisiIntegratorManager.getRemoteFacilities(facility.getId())) {
				IntegratorConsent consent = new IntegratorConsent();
				consent.setIntegratorFacilityId(cachedFacility.getIntegratorFacilityId());
				consent.setFormVersion(formType);
				consent.setCreatedDate(new Date());
				consent.setDemographicId(clientId);
				consent.setFacilityId(facility.getId());
				consent.setProviderNo(provider.getProviderNo());
				consents.put(cachedFacility.getIntegratorFacilityId(), consent);
			}
		} catch (Exception e) {
			logger.error("Unexpected Error.", e);
		}
	}

	/**
	 * This method is expected to be used by the detail consent where each check box maps to each consent bit and every entry must be set manually.
	 * 
	 * @param s is of the format "consent.<remoteFacilityId>.<consentField>", i.e. "consent.1.hic"
	 */
	public void addConsent(String s) {
		String[] splitTemp = s.split("\\.");
		int remoteFacilityId = Integer.parseInt(splitTemp[1]);

		IntegratorConsent consent = consents.get(remoteFacilityId);

		if ("hic".equals(splitTemp[2])) consent.setRestrictConsentToHic(true);
		else if ("search".equals(splitTemp[2])) consent.setConsentToSearches(true);
		else if ("personal".equals(splitTemp[2])) consent.setConsentToBasicPersonalData(true);
		else if ("mental".equals(splitTemp[2])) consent.setConsentToMentalHealthData(true);
		else if ("hnr".equals(splitTemp[2])) consent.setConsentToHealthNumberRegistry(true);
		else logger.error("unexpected consent bit : " + s);
	}

	public void addHnrConsent()
	{
		for (IntegratorConsent consent : consents.values()) {
			consent.setConsentToHealthNumberRegistry(true);
		}
	}
	
	public void storeAllConsents() {
		for (IntegratorConsent consent : consents.values()) {
			integratorConsentDao.persist(consent);
		}
	}

	/**
	 * This method is meant to be called by the complex consent forms to set all consents on all agencies to 1 value.
	 * 
	 * @param complexSelection
	 */
	public void setConsent(String complexSelection, String formLocation, boolean refusedToSign) {
		for (IntegratorConsent consent : consents.values()) {
			if ("ALL".equals(complexSelection)) {
				consent.setConsentToAll();
			} else if ("HIC_ALL".equals(complexSelection)) {
				consent.setConsentToAll();
				consent.setRestrictConsentToHic(true);
			} else if ("NONE".equals(complexSelection)) {
				consent.setConsentToNone();
			} else {
				logger.error("Error, missing consent option. option=" + complexSelection);
			}

			consent.setPrintedFormLocation(formLocation);
			consent.setRefusedToSign(refusedToSign);
		}
	}
}
