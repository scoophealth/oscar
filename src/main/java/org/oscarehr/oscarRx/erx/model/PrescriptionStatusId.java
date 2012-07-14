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
 * Represents a Prescription's StatusID field
 */
public enum PrescriptionStatusId {
	/**
	 * This prescription is active.
	 */
	ACTIVE(1),
	/**
	 * This prescription is a renewal.
	 */
	RENEWED(2),
	/**
	 * This prescription has been permanently discontinued.
	 */
	DEFINITIVELYCEASED(3),
	/**
	 * This prescription has been temporarily discontinued.
	 */
	TEMPORARILYCEASED(4),
	/**
	 * This prescription has been canceled.
	 */
	CANCELED(5);

	/**
	 * Convert a string (passed in a response) to it's corresponding
	 * PrescriptionStatusId.
	 * 
	 * @param toParse
	 *            The string to convert.
	 * @return The PrescriptionStatusId corresponding to the given string.
	 * @throws IllegalArgumentException
	 *             Throws an IllegalArgumentException if the string doesn't
	 *             match a known PrescriptionStatusId.
	 */
	public static PrescriptionStatusId parseString(String toParse)
	    throws IllegalArgumentException {
		switch (Integer.parseInt(toParse)) {
			case 1:
				return ACTIVE;
			case 2:
				return RENEWED;
			case 3:
				return DEFINITIVELYCEASED;
			case 4:
				return TEMPORARILYCEASED;
			case 5:
				return CANCELED;
			default:
				throw new IllegalArgumentException(
				    "Unrecognized PrescriptionStatusId " + toParse);
		}
	}

	/**
	 * The value to pass or parse when talking with the External Prescriber functions.
	 */
	public final int prescriptionStatusId;

	/**
	 * Construct a PrescriptionStatusId.
	 * 
	 * @param prescriptionStatusId
	 *            An integer value which must be passed in a request or parsed
	 *            in a response.
	 */
	PrescriptionStatusId(int prescriptionStatusId) {
		this.prescriptionStatusId = prescriptionStatusId;
	}

	/**
	 * Convert a PrescriptionStatusId to a string suitable for sending in a
	 * request.
	 * 
	 * @return A string representing this value, suitable for sending in a
	 *         request.
	 */
	public String getString() {
		return Integer.toString(this.prescriptionStatusId);
	}

}
