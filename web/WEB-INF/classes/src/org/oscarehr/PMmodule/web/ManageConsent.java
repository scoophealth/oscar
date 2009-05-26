package org.oscarehr.PMmodule.web;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.caisi_integrator.ws.CachedFacility;
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
	/**
	 * consentToView is null if editing consent, if viewing consent it should have the consent to view. In reality it will view all consents done at the same date as the consent specified so any consent at the given time will produce the same viewed set of
	 * results.
	 */
	private List<IntegratorConsent> previousConsentsToView = null;

	public ManageConsent(int clientId) {
		this.clientId = clientId;
	}

	/**
	 * It's safe to pass in null, it's ignore it. The string should be a number though and will be casted to one. It just seemed better to cast it here than in the jsp to reduce code in the jsp.
	 */
	public void setViewConsentId(String consentId) {
		if (consentId != null) {
			IntegratorConsent consentToView = integratorConsentDao.find(Integer.parseInt(consentId));
			previousConsentsToView = integratorConsentDao.findByFacilityDemographicAndDate(loggedInInfo.currentFacility.getId(), clientId, consentToView.getCreatedDate());
		}
	}

	public CachedFacility getLocalCachedFacility() throws MalformedURLException {
		if (localCachedFacility == null) {
			FacilityWs facilityWs = caisiIntegratorManager.getFacilityWs(loggedInInfo.currentFacility.getId());
			localCachedFacility = facilityWs.getMyFacility();
		}

		return (localCachedFacility);
	}

	public Collection<CachedFacility> getAllFacilitiesToDisplay() throws MalformedURLException {
		if (previousConsentsToView == null) return (getAllRemoteFacilities());
		else return (getPreviousConsentFacilities());
	}

	private Collection<CachedFacility> getPreviousConsentFacilities() throws MalformedURLException {
		ArrayList<CachedFacility> results = new ArrayList<CachedFacility>();

		CachedFacility localCachedFacility = getLocalCachedFacility();

		for (IntegratorConsent consent : previousConsentsToView) {
			CachedFacility cachedFacility = caisiIntegratorManager.getRemoteFacility(loggedInInfo.currentFacility.getId(), consent.getIntegratorFacilityId());
			if (!cachedFacility.getIntegratorFacilityId().equals(localCachedFacility.getIntegratorFacilityId())) {
				results.add(cachedFacility);
			}
		}

		return (results);
	}

	private Collection<CachedFacility> getAllRemoteFacilities() throws MalformedURLException {
		List<CachedFacility> results=caisiIntegratorManager.getRemoteFacilities(loggedInInfo.currentFacility.getId());
		return(results);
	}

	public boolean displayAsCheckedConsentToShareData(int remoteFacilityId) throws MalformedURLException {
		if (previousConsentsToView == null) return (displayAsCheckedConsentToShareDataMostRecent(remoteFacilityId));
		else return (displayAsCheckedConsentToShareDataPrevious(remoteFacilityId));
	}

	private boolean displayAsCheckedConsentToShareDataPrevious(int remoteFacilityId) {
		// get the local consent for this remote facility
		for (IntegratorConsent consent : previousConsentsToView) {
			if (consent.getIntegratorFacilityId().equals(remoteFacilityId)) {
				return (consent.isConsentToShareData());
			}
		}

		throw (new IllegalStateException("This should not be possible. Previous consent is missing data. remoteFacilityId=" + remoteFacilityId + ", prevConsentId(group)=" + previousConsentsToView.get(0).getId()));
	}

	public boolean displayAsCheckedConsentToShareDataMostRecent(int remoteFacilityId) throws MalformedURLException {
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
		if (previousConsentsToView == null) return (displayAsCheckedExcludeMentalHealthDataMostRecent(remoteFacilityId));
		else return (displayAsCheckedExcludeMentalHealthDataPrevious(remoteFacilityId));
	}

	public boolean displayAsCheckedExcludeMentalHealthDataPrevious(int remoteFacilityId) throws MalformedURLException {
		// get the local consent for this remote facility
		for (IntegratorConsent consent : previousConsentsToView) {
			if (consent.getIntegratorFacilityId().equals(remoteFacilityId)) {
				return (consent.isExcludeMentalHealthData());
			}
		}

		throw (new IllegalStateException("This should not be possible. Previous consent is missing data. remoteFacilityId=" + remoteFacilityId + ", prevConsentId(group)=" + previousConsentsToView.get(0).getId()));
	}

	public boolean displayAsCheckedExcludeMentalHealthDataMostRecent(int remoteFacilityId) throws MalformedURLException {
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

	public Integer getPreviousConsentDigitalSignatureId() {
		return (previousConsentsToView.get(0).getDigitalSignatureId());
	}
}
