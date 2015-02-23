/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

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
		return (providerNo + System.currentTimeMillis());
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

		if (loggedInInfo.getCurrentFacility().isEnableDigitalSignatures()) {
			FileInputStream fileInputStream = null;
			try {
				String filename = DigitalSignatureUtils.getTempFilePath(signatureRequestId);
				fileInputStream = new FileInputStream(filename);
				byte[] image = new byte[1024 * 256];
				fileInputStream.read(image);

				digitalSignature = new DigitalSignature();
				digitalSignature.setDateSigned(new Date());
				digitalSignature.setDemographicId(demographicId);
				digitalSignature.setFacilityId(loggedInInfo.getCurrentFacility().getId());
				digitalSignature.setProviderNo(loggedInInfo.getLoggedInProviderNo());
				digitalSignature.setSignatureImage(image);

				DigitalSignatureDao digitalSignatureDao = (DigitalSignatureDao) SpringUtils.getBean("digitalSignatureDao");
				digitalSignatureDao.persist(digitalSignature);

				return (digitalSignature);
			} catch (FileNotFoundException e) {
	            logger.debug("Signature file not found. User probably didn't collect a signature.", e);
	            return null;
			} catch (Exception e) {
	            logger.error("UnexpectedError.", e);
	            return null;
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
