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


package org.oscarehr.oscarRx.erx.model;

import java.net.MalformedURLException;
import java.net.URL;

import org.oscarehr.common.dao.ProviderPreferenceDao;
import org.oscarehr.common.model.ProviderPreference;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;

/**
 * Provides an abstract way to reference the the External Prescriber preferences specific to a
 * single provider (doctor).
 * 
 * FUTURE: org.oscarehr.oscarRx.erx should, in the future, support more than one
 * external prescription service, which may not necessarily need the same doctor
 * preference objects as the External Prescriber. When this support is added, this class should
 * be renamed to org.oscarehr.oscarRx.erx.model.DoctorPreferences;
 * which should implement a new interface named
 * org.oscarehr.oscarRx.erx.model.ERxDoctorPreferences. This new interface should
 * define getters/setters for an authentication structure (in this case,
 * containing username, password, and clientNumber), and a service URL (and
 * org.oscarehr.oscarRx.erx.model.DoctorPreferences should be
 * re-factored as such.
 */
public class ERxDoctorPreferences {
    /**
     * Create an instance of an ERxDoctorPreferences object.
     * 
     * @param providerId
     *            The ID of the doctor whose preferences should be loaded.
     */
    public static ERxDoctorPreferences getInstance(String providerId)
            throws IllegalArgumentException, MalformedURLException {
        // The object we will return
        ERxDoctorPreferences answer;
        // The object we will use to get the data
        ProviderPreference providerPreference = ((ProviderPreferenceDao) SpringUtils
                .getBean("providerPreferenceDao")).find(providerId);

        // Die if the provider was not found
        if (providerPreference == null) {
            throw new IllegalArgumentException("Provider " + providerId
                    + " not found.");
        }

        String eRx_SSO_URL = OscarProperties.getInstance().getProperty("util.erx.webservice_url");
        String eRxUsername = OscarProperties.getInstance().getProperty("util.erx.clinic_username");
        String eRxPassword = OscarProperties.getInstance().getProperty("util.erx.clinic_password");
        String eRxFacility = OscarProperties.getInstance().getProperty("util.erx.clinic_facility_id");
        boolean eRxTrainingMode = Boolean.valueOf(OscarProperties.getInstance().getProperty("util.erx.clinic_training_mode"));
        
        // Construct a new ERxDoctorPreference object
        answer = new ERxDoctorPreferences(providerPreference.getId(), new URL(
                eRx_SSO_URL),
                eRxUsername,
                eRxPassword,
                eRxFacility,
                eRxTrainingMode);

        return answer;
    }

    /**
     * The unique doctor number, associated with the doctor, provided by
     * the External Prescriber.
     */
    private String providerId;
    /**
     * The URL of the web service to connect to, provided by the External Prescriber.
     */
    private URL connectionURL;
    /**
     * The doctor's username, which will be used to connect to the web service.
     * The doctor would negotiate this with the External Prescriber when their account is
     * created on the the External Prescriber servers.
     */
    private String username;
    /**
     * The doctor's password, which will be used to connect to the web service.
     * This is set by the doctor when their account is created on the the External Prescriber
     * servers.
     */
    private String password;
    /**
     * The unique clinic number that the doctor is associated with, provided by
     * the External Prescriber.
     */
    private String clientNumber;
    /**
     * Whether or not the doctor has turned on training mode.
     */
    private boolean isInTrainingMode;

    /**
     * Create an instance of an ERxDoctorPreferences object.
     */
    protected ERxDoctorPreferences() {
        super();
    }

    /**
     * Create an instance of an ERxDoctorPreferences object.
     * 
     * @param providerId
     *            A unique doctor number, provided by the External Prescriber.
     * @param connectionURL
     *            The URL of the web service to connect to.
     * @param username
     *            The doctor's username, which will be used to connect to the
     *            web service.
     * @param password
     *            The doctor's password, which will be used to connect to the
     *            web service.
     * @param clientNumber
     *            The unique clinic number that the doctor is associated with,
     *            provided by the External Prescriber.
     * @param isInTrainingMode
     *            Whether or not the doctor has turned on training mode.
     */
    public ERxDoctorPreferences(String providerId, URL connectionURL,
            String username, String password, String clientNumber,
            boolean isInTrainingMode) {
        super();
        this.providerId = providerId;
        this.connectionURL = connectionURL;
        this.username = username;
        this.password = password;
        this.clientNumber = clientNumber;
        this.isInTrainingMode = isInTrainingMode;
    }

    /**
     * Returns the unique clinic number that the doctor is associated with,
     * provided by the External Prescriber.
     * 
     * @return The current value of clientNumber.
     */
    public String getClientNumber() {
        return this.clientNumber;
    }

    /**
     * Returns the URL of the web service to connect to.
     * 
     * @return The current value of connectionURL.
     */
    public URL getConnectionURL() {
        return this.connectionURL;
    }

    /**
     * Returns the doctor's password, which will be used to connect to the web
     * service.
     * 
     * @return The current value of password.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Returns the unique doctor number, associated with the doctor, provided by
     * the External Prescriber.
     * 
     * @return The current value of providerId.
     */
    public String getProviderId() {
        return this.providerId;
    }

    /**
     * Returns the doctor's username, which will be used to connect to the web
     * service.
     * 
     * @return The current value of username.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Returns whether or not the doctor has turned on training mode.
     * 
     * @return TRUE if training mode is enabled; FALSE otherwise.
     */
    public boolean isInTrainingMode() {
        return this.isInTrainingMode;
    }

    /**
     * Changes the value of the unique clinic number that the doctor is
     * associated with.
     * 
     * @param clientNumber
     *            The new clientNumber.
     */
    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    /**
     * Changes the value of the URL of the web service to connect to.
     * 
     * @param connectionURL
     *            The new connectionURL.
     */
    public void setConnectionURL(URL connectionURL) {
        this.connectionURL = connectionURL;
    }

    /**
     * Changes whether or not the doctor has turned on training mode.
     * 
     * @param isInTrainingMode
     *            The new isInTrainingMode.
     */
    public void setInTrainingMode(boolean isInTrainingMode) {
        this.isInTrainingMode = isInTrainingMode;
    }

    /**
     * Changes the value of the doctor's password, which will be used to connect
     * to the web service.
     * 
     * @param password
     *            The new password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Changes the value of the unique doctor number, associated with the
     * doctor, provided by the External Prescriber.
     * 
     * @param providerID
     *            The new providerId.
     */
    public void setProviderId(String providerID) {
        this.providerId = providerID;
    }

    /**
     * Changes the value of the doctor's username, which will be used to connect
     * to the web service.
     * 
     * @param username
     *            The new username.
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
