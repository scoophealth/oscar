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

/**
 * Represents a WSPatientResult3 ID number used by the External Prescriber web services to
 * indicate the status of a patient data request.
 */
public enum WSPatientResult3 {
	/**
	 * Request is successful, patient demographics were received and saved.
	 */
	SUCCESS(1),
	/**
	 * Request is unsuccessful because it has generated an unhandled exception
	 * in the External Prescriber's web service (i.e.: System Error).
	 */
	ERROR_UNMANAGED(4),
	/**
	 * Request is unsuccessful because the supplied credentials in the request
	 * are erroneous (i.e.: bad username or password).
	 */
	ERROR_AUTHENTICATION(5),
	/**
	 * Request is unsuccessful because the user's profile is restricted from
	 * making this request.
	 */
	ERROR_AUTHORIZATION(6),
	/**
	 * Request is unsuccessful because this user profile is restricted from
	 * making this request to this clinic specified by the clientNumber
	 * parameter.
	 */
	ERROR_UNAUTHORIZED_CLINIC(7),
	/**
	 * Request is unsuccessful because the clinic specified by the clientNumber
	 * parameter is invalid.
	 * 
	 * It can also be returned when the clinic is disabled.
	 */
	ERROR_UNKNOWN_CLINIC(8),
	/**
	 * Request is unsuccessful because the supplied LocaleId parameter is
	 * invalid.
	 */
	ERROR_INVALIDLOCALID(9),
	/**
	 * Request is unsuccessful because the request was made through an insecure
	 * channel.
	 */
	ERROR_NONSECUREACCESS(11);

	/**
	 * Convert a string (passed in a response) to it's corresponding
	 * WSPatientResult3/
	 * 
	 * @param toParse
	 *            The string to convert.
	 * @return The WSPatientResult3 corresponding to the given string.
	 * @throws IllegalArgumentException
	 *             Throws an IllegalArgumentException if the string doesn't
	 *             match a known WSPatientResult3.
	 */
	public static WSPatientResult3 parseString(String toParse)
	    throws IllegalArgumentException {
		switch (Integer.parseInt(toParse)) {
			case 1:
				return SUCCESS;
			case 4:
				return ERROR_UNMANAGED;
			case 5:
				return ERROR_AUTHENTICATION;
			case 6:
				return ERROR_AUTHORIZATION;
			case 7:
				return ERROR_UNAUTHORIZED_CLINIC;
			case 8:
				return ERROR_UNKNOWN_CLINIC;
			case 9:
				return ERROR_INVALIDLOCALID;
			case 11:
				return ERROR_NONSECUREACCESS;
			default:
				throw new IllegalArgumentException(
				    "Unrecognized WSPatientResult3 " + toParse);
		}
	}

	/**
	 * The value to pass or parse when talking with the External Prescriber functions.
	 */
	public final int patientResultId;

	/**
	 * Create a WSPatientResult3.
	 * 
	 * @param patientResultId
	 *            An integer value which must be passed in a request or parsed
	 *            in a response.
	 */
	WSPatientResult3(int patientResultId) {
		this.patientResultId = patientResultId;
	}

	/**
	 * Convert a WSPatientResult3 to a string suitable for sending in a request.
	 * 
	 * @return A string representing this value, suitable for sending in a
	 *         request.
	 */
	public String getString() {
		return Integer.toString(this.patientResultId);
	}
}
