/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package org.oscarehr.web;

import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.ClinicDAO;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.dao.PrescriptionDao;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.OmpO09;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.OscarToOscarUtils;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.Prescription;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.ProviderPreference;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.QrCodeUtils;
import org.oscarehr.util.QrCodeUtils.QrCodesOrientation;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.web.admin.ProviderPreferencesUIBean;

import oscar.OscarProperties;
import ca.uhn.hl7v2.model.v26.message.OMP_O09;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public final class PrescriptionQrCodeUIBean {

	private static final Logger logger = MiscUtils.getLogger();

	private static ClinicDAO clinicDAO = (ClinicDAO) SpringUtils.getBean("clinicDAO");
	private static ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");
	private static DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
	private static PrescriptionDao prescriptionDao = (PrescriptionDao) SpringUtils.getBean("prescriptionDao");
	private static DrugDao drugDao = (DrugDao) SpringUtils.getBean("drugDao");

	private PrescriptionQrCodeUIBean() {
		// not meant to be instantiated, just a utility class
	}

	public static byte[] getPrescriptionHl7QrCodeImage(int prescriptionId) {

		logger.debug("Display QR Code for prescriptionId="+prescriptionId);	
		
		try {
			Clinic clinic = clinicDAO.getClinic();
			Prescription prescription = prescriptionDao.find(prescriptionId);
			Provider provider = providerDao.getProvider(prescription.getProviderNo());
			Demographic demographic = demographicDao.getDemographicById(prescription.getDemographicId());
			List<Drug> drugs=drugDao.findByPrescriptionId(prescription.getId().intValue());
						
			OMP_O09 hl7PrescriptionMessage=OmpO09.makeOmpO09(clinic, provider, demographic, prescription, drugs);
			String hl7PrescriptionString = OscarToOscarUtils.pipeParser.encode(hl7PrescriptionMessage);
			logger.debug(hl7PrescriptionString);
			
			int qrCodeScale=Integer.valueOf(OscarProperties.getInstance().getProperty("QR_CODE_IMAGE_SCALE_FACTOR"));
			
			byte[] image=QrCodeUtils.toMultipleQrCodePngs(hl7PrescriptionString, getEcLevel(), QrCodesOrientation.VERTICAL, qrCodeScale);
			
			return(image);
		} catch (Exception e) {
			logger.error("Unexpected error.", e);
		}

		return (null);
	}

	/**
	 * This method is required and is coded funny because XING - the library we're using,
	 * decided not to use enum's for the ec levels because they wanted to support 
	 * jdk 1.4 which doesn't have enums. As a result we manuallt valueOf the property.
	 * 
	 * We need to return the static instances just in case XING decided to do what java-enums
	 * do when checking equality - which is to check the in memory class instance value since 
	 * enums are defined as singletons.
	 */
	private static ErrorCorrectionLevel getEcLevel() {
		String ecLevelString=OscarProperties.getInstance().getProperty("QR_CODE_ERROR_CORRECTION_LEVEL");

		if ("L".equals(ecLevelString)) return(ErrorCorrectionLevel.L);
		if ("M".equals(ecLevelString)) return(ErrorCorrectionLevel.M);
		if ("Q".equals(ecLevelString)) return(ErrorCorrectionLevel.Q);
		if ("H".equals(ecLevelString)) return(ErrorCorrectionLevel.H);
		
		return null;
    }
	
	public static boolean isPrescriptionQrCodeEnabledForProvider(String providerNo)
	{
		ProviderPreference providerPreference=ProviderPreferencesUIBean.getProviderPreference(providerNo);
		if (providerPreference==null) providerPreference=new ProviderPreference();
			
		return(providerPreference.isPrintQrCodeOnPrescriptions());
	}
}
