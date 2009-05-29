package org.oscarehr.PMmodule.web;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.caisi_integrator.ws.CachedFacility;
import org.oscarehr.common.dao.IntegratorConsentDao;
import org.oscarehr.common.model.DigitalSignature;
import org.oscarehr.common.model.IntegratorConsent;
import org.oscarehr.util.DigitalSignatureUtils;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

public class ManageConsentAction {
	private static Logger logger = LogManager.getLogger(ManageConsentAction.class);
	private static IntegratorConsentDao integratorConsentDao = (IntegratorConsentDao) SpringUtils.getBean("integratorConsentDao");
	private static CaisiIntegratorManager caisiIntegratorManager = (CaisiIntegratorManager) SpringUtils.getBean("caisiIntegratorManager");

	private IntegratorConsent consent = new IntegratorConsent();
	private String signatureRequestId = null;
	private Integer clientId = null;
	private LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();

	public ManageConsentAction(Integer clientId) throws MalformedURLException {
		this.clientId = clientId;

		consent.setDemographicId(clientId);
		consent.setFacilityId(loggedInInfo.currentFacility.getId());
		consent.setProviderNo(loggedInInfo.loggedInProvider.getProviderNo());

		for (CachedFacility cachedFacility : caisiIntegratorManager.getRemoteFacilities(loggedInInfo.currentFacility.getId())) {
			consent.getConsentToShareData().put(cachedFacility.getIntegratorFacilityId(), false);
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

		if ("consentToShareData".equals(splitTemp[2])) {
			consent.getConsentToShareData().put(remoteFacilityId, true);
		} else {
			logger.error("unexpected consent bit : " + s);
		}
	}

	/**
	 * @throws IOException if expecting a signature but missing one
	 */
	public void storeAllConsents() throws IOException {

		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();

		DigitalSignature digitalSignature = DigitalSignatureUtils.storeDigitalSignatureFromTempFileToDB(loggedInInfo, signatureRequestId, clientId);
		if (digitalSignature != null) consent.setDigitalSignatureId(digitalSignature.getId());

		integratorConsentDao.persist(consent);
	}

	public void setExcludeMentalHealthData(Boolean b) {
		consent.setExcludeMentalHealthData(b);
	}

	public void setConsentStatus(String s) {
		consent.setClientConsentStatus(IntegratorConsent.ConsentStatus.valueOf(s));
	}

	public void setExpiry(String s) {
		int months = Integer.parseInt(s);

		if (months == -1) consent.setExpiry(null);
		else {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(consent.getCreatedDate());
			cal.add(Calendar.MONTH, months);
			consent.setExpiry(cal.getTime());
		}
	}

	public void setSignatureRequestId(String signatureRequestId) {
		this.signatureRequestId = signatureRequestId;
	}
}
