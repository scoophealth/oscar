/**
 * Copyright (C) 2011-2012  PeaceWorks Technology Solutions
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


package org.oscarehr.oscarRx.erx;

import java.net.MalformedURLException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.oscarRx.erx.controller.ERxCommunicator;
import org.oscarehr.oscarRx.erx.controller.ERxPatientRecordTranslator;
import org.oscarehr.oscarRx.erx.interfaces.PatientRecordSynchronizer;
import org.oscarehr.oscarRx.erx.model.ERxApplicationPreferences;
import org.oscarehr.oscarRx.erx.model.ERxDoctorPreferences;
import org.oscarehr.oscarRx.erx.model.ERxFacilityPreferences;
import org.oscarehr.oscarRx.erx.model.ERxPatientData;
import org.oscarehr.oscarRx.erx.model.request.Transaction3;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

/**
 * An object to manage sending patient data to the remote prescription provider.
 */
public class ERxPatientRecordSynchronizer implements PatientRecordSynchronizer {
    /**
     * A logger that we will use to record info and errors.
     */
    private static Logger logger = MiscUtils.getLogger();

    /**
     * Create an instance of a ERxPatientRecordSynchronizer object.
     */
    public ERxPatientRecordSynchronizer() {
        super();
    }

    /**
     * Send a patient's data to the remote prescription provider.
     * 
     * @param patient
     *            The patient data to send to the remote prescription provider.
     * @param providerId
     *            The OSCAR ID of the provider who the patient is seeing and who
     *            may prescribe something for the patient using the external
     *            prescription provider.
     */
    @Override
    public void sendRecord(Demographic patient, String providerId) {
        // Information about the application
        ERxApplicationPreferences oscarInfo = ERxApplicationPreferences
                .getInstance();
        /*
         * The credentials of the facility who administers the medical records
         * of the patient
         * 
         * FUTURE: this should get credential objects from a factory class that
         * knows the provider's preferred external prescription provider; where
         * the credential objects returned conform to the signature of
         * ERxFacilityPreferences.
         */
        ERxFacilityPreferences facilityCredentials;

        /*
         * The credentials of the doctor who will be seeing (and possibly
         * prescribing-something-to) the patient
         * 
         * FUTURE: this should get credential objects from a factory class that
         * knows the provider's preferred external prescription provider; where
         * the credential objects returned conform to the signature of
         * ERxDoctorPreferences.
         */
        ERxDoctorPreferences doctorCredentials;

        /*
         * The object which will facilitate communications
         * 
         * FUTURE: this should get communicator objects from a factory class
         * that knows the provider's preferred external prescription provider;
         * where the communicator objects returned conform to the signature of
         * ERxCommunicator.
         */
        ERxCommunicator communicator;

        // Store the translated patient
        ERxPatientData translatedPatient;
        Date patientLastModifiedDate;

        DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");

        try {
            // Log what we're doing
            logger.info("Attempting to send a patient record to an external prescription provider.");

            // Get the credentials we will use for communicating
            doctorCredentials = ERxDoctorPreferences.getInstance(providerId);

            facilityCredentials = ERxFacilityPreferences.getInstance();

            // Create a communicator to facilitate communications
            communicator = new ERxCommunicator(
                    doctorCredentials.getConnectionURL(),
                    doctorCredentials.getUsername(),
                    doctorCredentials.getPassword(),
                    facilityCredentials.getLocale());

            try {

                // Translate the patient to send
                translatedPatient = ERxPatientRecordTranslator
                        .translateToExternal(patient);
                patientLastModifiedDate = demographicDao.getDemographicById(
                        patient.getDemographicNo()).getLastUpdateDate();

                try {
                    // Create a request and send it
                    communicator.sendPatientData(
                            translatedPatient,
                            doctorCredentials.getClientNumber(),
                            doctorCredentials.isInTrainingMode(),
                            new Transaction3(ERxFacilityPreferences
                                    .getNextTransactionId(), oscarInfo
                                    .getSoftwareName(), oscarInfo.getVendor(),
                                    oscarInfo.getVersion(),
                                    patientLastModifiedDate));
                } catch (SecurityException e) {
                    logger.error("Failed to send a patient record to an external prescription provider because the remote web service denied us access: "
                                    + e.getMessage()
                                    + " This is likely due to incorrectly-configured provider or facility credentials.",e);
                }
            } catch (NumberFormatException e) {
                logger.error("Failed to translate a patient because an unparsable number was given: "
                                + e.getMessage(),e);
            }
        } catch (IllegalArgumentException e) {
            logger.error("Failed to send a patient record to an external prescription provider because the doctor ID given was not valid, or did not have any external prescription provider data stored.",e);
        } catch (MalformedURLException e) {
            logger.error("Failed to send a patient record to an external prescription provider because the web service URL was not valid.",e);
        } finally {
            logger.info("Completed attempt to send a patient record to an external prescription provider.");
        }
    }
}
