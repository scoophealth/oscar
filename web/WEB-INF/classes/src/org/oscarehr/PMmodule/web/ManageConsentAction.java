package org.oscarehr.PMmodule.web;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.oscarehr.common.dao.IntegratorConsentDao;
import org.oscarehr.common.model.DigitalSignature;
import org.oscarehr.common.model.IntegratorConsent;
import org.oscarehr.util.DigitalSignatureUtils;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

public class ManageConsentAction {
	private static Logger logger = LogManager.getLogger(ManageConsent.class);
	private static IntegratorConsentDao integratorConsentDao = (IntegratorConsentDao) SpringUtils.getBean("integratorConsentDao");

	private HashMap<Integer, IntegratorConsent> consents = new HashMap<Integer, IntegratorConsent>();
	private String signatureRequestId = null;
	private Integer clientId = null;
	private boolean excludeMentalHealth=false;
	private LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
	/** the created date must be the same for all entries so they can be grouped in the display */
	private Date createDate = new Date();

	public ManageConsentAction(Integer clientId) throws MalformedURLException {
		this.clientId = clientId;
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
		if (consent == null) {
			consent = new IntegratorConsent();
			consent.setIntegratorFacilityId(remoteFacilityId);
			// the created date must be the same for all entries so they can be grouped in the display
			consent.setCreatedDate(createDate);
			consent.setDemographicId(clientId);
			consent.setFacilityId(loggedInInfo.currentFacility.getId());
			consent.setProviderNo(loggedInInfo.loggedInProvider.getProviderNo());
			consents.put(remoteFacilityId, consent);
		}

		if ("placeholder".equals(splitTemp[2])) {
			// do nothing, the above initialisation is all we need
		} else if ("consentToShareData".equals(splitTemp[2])) {
			consent.setConsentToShareData(true);
		} else if ("excludeMentalHealth".equals(splitTemp[2])) {
			excludeMentalHealth=true;
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

		for (IntegratorConsent consent : consents.values()) {
			consent.setExcludeMentalHealthData(excludeMentalHealth);
			
			if (digitalSignature != null) consent.setDigitalSignatureId(digitalSignature.getId());
			
			integratorConsentDao.persist(consent);
		}
	}

	public void setSignatureRequestId(String signatureRequestId) {
		this.signatureRequestId = signatureRequestId;
	}
}
