package org.oscarehr.PMmodule.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.caisi_integrator.ws.CachedFacility;
import org.oscarehr.common.dao.IntegratorConsentDao;
import org.oscarehr.common.model.IntegratorConsent;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

public class ManageConsent {
	private static Logger logger = LogManager.getLogger(ManageConsent.class);
	private static CaisiIntegratorManager caisiIntegratorManager = (CaisiIntegratorManager) SpringUtils.getBean("caisiIntegratorManager");
	private static IntegratorConsentDao integratorConsentDao = (IntegratorConsentDao) SpringUtils.getBean("integratorConsentDao");

	private List<CachedFacility> allRemoteFacilities = new ArrayList<CachedFacility>();
	private HashMap<Integer, IntegratorConsent> currentConsents = new HashMap<Integer, IntegratorConsent>();
	private Integer showConsentId = null;

	public ManageConsent(int clientId, Integer showConsentId) {
		try {
			LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
			
			if (showConsentId != null) {
				this.showConsentId = showConsentId;

				IntegratorConsent consent = integratorConsentDao.find(showConsentId);
				if (consent != null) {
					currentConsents.put(consent.getIntegratorFacilityId(), consent);
					allRemoteFacilities.add(caisiIntegratorManager.getRemoteFacility(loggedInInfo.currentFacility.getId(), consent.getIntegratorFacilityId()));
				}
			} else {
				allRemoteFacilities = caisiIntegratorManager.getRemoteFacilities(loggedInInfo.currentFacility.getId());

				for (CachedFacility cachedFacility : allRemoteFacilities) {
					IntegratorConsent consent = integratorConsentDao.findLatestByFacilityDemographicAndRemoteFacility(loggedInInfo.currentFacility.getId(), clientId, cachedFacility.getIntegratorFacilityId());
					if (consent != null) currentConsents.put(consent.getIntegratorFacilityId(), consent);
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error.", e);
		}
	}

	public List<CachedFacility> getAllRemoteFacilities() {
		return (allRemoteFacilities);
	}

	public boolean displayAsChecked(int remoteFacilityId, String consentField) {
		IntegratorConsent consent = currentConsents.get(remoteFacilityId);
		if (consent == null) {
			if ("hic".equals(consentField)) return (false);
			return (true);
		}

		if ("hic".equals(consentField)) return (consent.isRestrictConsentToHic());
		else if ("search".equals(consentField)) return (consent.isConsentToSearches());
		else if ("nonDomain".equals(consentField)) return (consent.isConsentToAllNonDomainData());
		else if ("mental".equals(consentField)) return (consent.isConsentToMentalHealthData());
		else if ("hnr".equals(consentField)) return (consent.isConsentToHealthNumberRegistry());
		else logger.error("unexpected consent bit : " + consentField);

		return (false);
	}

	public boolean isReadOnly() {
		return (showConsentId != null);
	}

	public boolean useDigitalSignatures() {
		return (LoggedInInfo.loggedInInfo.get().currentFacility.isEnableDigitalSignatures());
	}
}
