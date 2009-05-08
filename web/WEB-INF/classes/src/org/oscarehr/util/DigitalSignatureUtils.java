package org.oscarehr.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.DigitalSignatureDao;
import org.oscarehr.common.model.DigitalSignature;

public class DigitalSignatureUtils {

	private static Logger logger = MiscUtils.getLogger();

	public static final String SIGNATURE_REQUEST_ID_KEY = "signatureRequestId";

	public static String generateSignatureRequestId(String providerNo) {
		return (providerNo + "_" + System.currentTimeMillis());
	}

	public static String getTempFilePath(String signatureRequestId) {
		return (System.getProperty("java.io.tmpdir") + "/signature_" + signatureRequestId + ".jpg");
	}

	/**
	 * This method will check if digital signatures is enabled or not. It will only attempt to save it if it's enabled.
	 * 
	 * @param demographicId of the owner of this signature
	 * @throws IOException if missing or error in image when one was expected
	 */
	public static DigitalSignature storeDigitalSignatureFromTempFileToDB(LoggedInInfo loggedInInfo, String signatureRequestId, int demographicId) {
		DigitalSignature digitalSignature = null;

		if (loggedInInfo.currentFacility.isEnableDigitalSignatures()) {
			FileInputStream fileInputStream = null;
			try {
				String filename = DigitalSignatureUtils.getTempFilePath(signatureRequestId);
				fileInputStream = new FileInputStream(filename);
				byte[] image = new byte[1024 * 256];
				fileInputStream.read(image);

				digitalSignature = new DigitalSignature();
				digitalSignature.setDateSigned(new Date());
				digitalSignature.setDemographicId(demographicId);
				digitalSignature.setFacilityId(loggedInInfo.currentFacility.getId());
				digitalSignature.setProviderNo(loggedInInfo.loggedInProvider.getProviderNo());
				digitalSignature.setSignatureImage(image);

				DigitalSignatureDao digitalSignatureDao = (DigitalSignatureDao) SpringUtils.getBean("digitalSignatureDao");
				digitalSignatureDao.persist(digitalSignature);

				return (digitalSignature);
			} catch (FileNotFoundException e) {
	            logger.debug("Signature file not found. User probably didn't collect a signature.", e);
			} catch (Exception e) {
	            logger.error("UnexpectedError.", e);
			} finally {
				if (fileInputStream != null) {
					try {
						fileInputStream.close();
					} catch (IOException e) {
						logger.error("Unexpected error.", e);
					}
				}
			}
		}

		return (digitalSignature);
	}

}
