package org.oscarehr.PMmodule.web;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.caisi_integrator.ws.CachedFacility;
import org.oscarehr.caisi_integrator.ws.DemographicTransfer;
import org.oscarehr.caisi_integrator.ws.DemographicWs;
import org.oscarehr.caisi_integrator.ws.FacilityWs;
import org.oscarehr.common.dao.IntegratorConsentDao;
import org.oscarehr.common.model.IntegratorConsent;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

public class ManageConsent {
	private static CaisiIntegratorManager caisiIntegratorManager = (CaisiIntegratorManager) SpringUtils.getBean("caisiIntegratorManager");
	private static IntegratorConsentDao integratorConsentDao = (IntegratorConsentDao) SpringUtils.getBean("integratorConsentDao");

	private CachedFacility localCachedFacility = null;
	private LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
	private int clientId = -1;

	public ManageConsent(int clientId) {
		this.clientId = clientId;
	}

	public CachedFacility getLocalCachedFacility() throws MalformedURLException {
		if (localCachedFacility == null) {
			FacilityWs facilityWs = caisiIntegratorManager.getFacilityWs(loggedInInfo.currentFacility.getId());
			localCachedFacility = facilityWs.getMyFacility();
		}

		return (localCachedFacility);
	}

	public Collection<CachedFacility> getAllDirectlyLinkedFacilities() throws MalformedURLException {
		DemographicWs demographicWs = caisiIntegratorManager.getDemographicWs(loggedInInfo.currentFacility.getId());
		List<DemographicTransfer> linkedDemographics = demographicWs.getDirectlyLinkedDemographicsByDemographicId(clientId);
		HashMap<Integer, CachedFacility> linkedFacilities = new HashMap<Integer, CachedFacility>();
		for (DemographicTransfer demographicTransfer : linkedDemographics) {
			Integer remoteFacilityId = demographicTransfer.getIntegratorFacilityId();
			if (!linkedFacilities.containsKey(remoteFacilityId)) {
				CachedFacility cachedFacility = caisiIntegratorManager.getRemoteFacility(loggedInInfo.currentFacility.getId(), remoteFacilityId);
				linkedFacilities.put(remoteFacilityId, cachedFacility);
			}
		}

		return (linkedFacilities.values());
	}

	public boolean displayAsCheckedConsentToShareDate(int remoteFacilityId) throws MalformedURLException {
		// get the local consent for this remote facility
		IntegratorConsent result = integratorConsentDao.findLatestByFacilityDemographicAndRemoteFacility(loggedInInfo.currentFacility.getId(), clientId, remoteFacilityId);
		if (result != null) {
			return (result.isConsentToShareData());
		}

		// try to get the local consent for the local facility as the default (specifically requested feature, not my design decision)
		result = integratorConsentDao.findLatestByFacilityDemographicAndRemoteFacility(loggedInInfo.currentFacility.getId(), clientId, getLocalCachedFacility().getIntegratorFacilityId());
		if (result != null) {
			return (result.isConsentToShareData());
		}

		// specifically requested to default general consent to true, not my design decision
		return (true);
	}

	public boolean displayAsCheckedExcludeMentalHealthData(int remoteFacilityId) throws MalformedURLException {
		// get the
		IntegratorConsent result = integratorConsentDao.findLatestByFacilityDemographicAndRemoteFacility(loggedInInfo.currentFacility.getId(), clientId, remoteFacilityId);
		if (result != null) {
			return (result.isExcludeMentalHealthData());
		}

		// try to get the local consent for the local facility as the default (specifically requested feature, not my design decision)
		result = integratorConsentDao.findLatestByFacilityDemographicAndRemoteFacility(loggedInInfo.currentFacility.getId(), clientId, getLocalCachedFacility().getIntegratorFacilityId());
		if (result != null) {
			return (result.isExcludeMentalHealthData());
		}

		return (false);
	}

	public boolean useDigitalSignatures() {
		return (loggedInInfo.currentFacility.isEnableDigitalSignatures());
	}
}
