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
import java.util.GregorianCalendar;

import oscar.OscarProperties;

/**
 * Provides an abstract way to reference the the External Prescriber preferences specific to a
 * clinic/facility. These are usually used during batch operations.
 * 
 * FUTURE: org.oscarehr.oscarRx.erx should, in the future, support more than one
 * external prescription service , which may not necessarily need the same
 * facility preference object as the External Prescriber. When this support is added, this class
 * should be renamed to
 * org.oscarehr.oscarRx.erx.model.FacilityPreferences; which should
 * implement a new interface named
 * org.oscarehr.oscarRx.erx.model.ERxFacilityPreferences. This new interface
 * should define getters/setters for an authentication structure (in this case
 * username, password, facilityId and clientNumber), and a service URL (and
 * org.oscarehr.oscarRx.erx.model.FacilityPreferences should be
 * re-factored as such.
 */
public class ERxFacilityPreferences {
    /**
     * Create an instance of an ERxFacilityPreferences object.
     */
	
	public static ERxFacilityPreferences getInstance()
            throws MalformedURLException {
        // The object we will return
    	
    	//logger.info("ERxFacilityPref1");
    	
        ERxFacilityPreferences answer = new ERxFacilityPreferences();
        //logger.info("ERxFacilityPref2");
        OscarProperties properties = OscarProperties.getInstance();
        //logger.info("ERxFacilityPref3");
        // Set data in the new object
        answer.setFacilityId(Integer.parseInt(properties
                .getProperty("util.erx.clinic_facility_id")));
        answer.setRemoteURL(new URL(properties.getProperty("util.erx.webservice_url")));
        answer.setUsername(properties.getProperty("util.erx.clinic_username"));
        answer.setPassword(properties.getProperty("util.erx.clinic_password"));
        answer.setClientNumber(properties.getProperty("util.erx.clinic_facility_id"));
        answer.setIsTraining(Boolean.parseBoolean(properties.getProperty("util.erx.clinic_training_mode")));
        answer.setLocale(properties.getProperty("util.erx.clinic_locale"));
        
        return answer;
    }

    /**
     * Get a unique transaction ID. According to the XSD/SOAP specification, an
     * integer is a signed number between (-2^32) and 2^31-1.
     * 
     * Note: There's a possibility that the External Prescriber will choke on this after Tue, 19
     * January 2038 at 03:14:07 UTC.
     * 
     * Note: Java will choke on this after Sun, 04 December 292,277,026,596 at
     * 15:30:08 UTC.
     * 
     * @return A unique transaction ID between 0 and 2147483647.
     */
    public static long getNextTransactionId() {
        return new GregorianCalendar().getTimeInMillis();
    }

    /**
     * The unique facility identifier corresponding to this facility, provided
     * by the External Prescriber.
     */
    private int facilityId;
    /**
     * The URL of the web service to connect to, provided by the External Prescriber.
     */
    private URL remoteURL;
    /**
     * The facility's username, which will be used to connect to the web service
     * and is provided by the External Prescriber.
     */
    private String username;
    /**
     * The facility's password, which will be used to connect to the web service
     * and is provided by the External Prescriber.
     */
    private String password;
    /**
     * The unique clinic number corresponding to this facility, provided by
     * the External Prescriber.
     */
    private String clientNumber;
    /**
     * Whether or not the facility is in training mode.
     */
    private boolean IsTraining;
    /**
     * The locale (language) that the facility uses.
     */
    private String locale;

    /**
     * Create an instance of an ERxFacilityPreferences object.
     */
    protected ERxFacilityPreferences() {
        super();
    }

    /**
     * Create an instance of a ERxFacilityPreferences object.
     * 
     * @param facilityId
     *            The unique facility identifier corresponding to this facility,
     *            provided by the External Prescriber.
     * @param remoteURL
     *            The URL of the web service to connect to, provided by the External Prescriber.
     * @param username
     *            The facility's username, which will be used to connect to the
     *            web service and is provided by the External Prescriber.
     * @param password
     *            The facility's password, which will be used to connect to the
     *            web service and is provided by the External Prescriber.
     * @param clientNumber
     *            The unique clinic number corresponding to this facility,
     *            provided by the External Prescriber.
     * @param isTraining
     *            Whether or not the facility is in training mode.
     * @param locale
     *            The locale (language) that the facility uses.
     */
    public ERxFacilityPreferences(int facilityId, URL remoteURL,
            String username, String password, String clientNumber,
            boolean isTraining, String locale) {
        super();
        this.facilityId = facilityId;
        this.remoteURL = remoteURL;
        this.username = username;
        this.password = password;
        this.clientNumber = clientNumber;
        this.IsTraining = isTraining;
        this.locale = locale;
    }

    /**
     * Returns the unique clinic number corresponding to this facility, provided
     * by the External Prescriber.
     * 
     * @return The current value of clientNumber.
     */
    public String getClientNumber() {
        return this.clientNumber;
    }

    /**
     * Returns the unique facility identifier corresponding to this facility,
     * provided by the External Prescriber.
     * 
     * @return The current value of facilityId.
     */
    public int getFacilityId() {
        return this.facilityId;
    }

    /**
     * Returns the locale.
     * 
     * @return The current value of locale.
     */
    public String getLocale() {
        return this.locale;
    }

    /**
     * Returns the facility's password, which will be used to connect to the web
     * service and is provided by the External Prescriber.
     * 
     * @return The current value of password.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Returns the URL of the web service to connect to, provided by the External Prescriber.
     * 
     * @return The current value of remoteURL.
     */
    public URL getRemoteURL() {
        return this.remoteURL;
    }

    /**
     * Returns the facility's username, which will be used to connect to the web
     * service and is provided by the External Prescriber.
     * 
     * @return The current value of username.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Returns whether or not the facility is in training mode.
     * 
     * @return The current value of isTraining.
     */
    public boolean isIsTraining() {
        return this.IsTraining;
    }

    /**
     * Changes the value of the unique clinic number corresponding to this
     * facility, provided by the External Prescriber.
     * 
     * @param clientNumber
     *            The new clientNumber.
     */
    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    /**
     * Changes the value of the unique facility identifier corresponding to this
     * facility, provided by the External Prescriber.
     * 
     * @param facilityId
     *            The new facilityId.
     */
    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }

    /**
     * Changes whether or not the facility is in training mode.
     * 
     * @param isTraining
     *            The new value for isTraining.
     */
    public void setIsTraining(boolean isTraining) {
        this.IsTraining = isTraining;
    }

    /**
     * Changes the value of locale.
     * 
     * @param locale
     *            The new locale.
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * Changes the value of the facility's password, which will be used to
     * connect to the web service and is provided by the External Prescriber.
     * 
     * @param password
     *            The new password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Changes the value of the URL of the web service to connect to, provided
     * by the External Prescriber.
     * 
     * @param remoteURL
     *            The new remoteURL.
     */
    public void setRemoteURL(URL remoteURL) {
        this.remoteURL = remoteURL;
    }

    /**
     * Changes the value of the facility's username, which will be used to
     * connect to the web service and is provided by the External Prescriber.
     * 
     * @param username
     *            The new username.
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
