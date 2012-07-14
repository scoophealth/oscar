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
 * Represents a format number used by the External Prescriber web services to indicate which
 * format to output the FormattedPrescriptionToString field.
 * 
 */
public enum PrescriptionFormat {
	/**
	 * A plain-text format, with newlines separated by CRLFs.
	 */
	PLAIN_TEXT(1),
	/**
	 * RichText format.
	 */
	RICH_TEXT(2);

	/**
	 * Convert a string (passed in a response) to it's corresponding
	 * prescriptionFormat.
	 * 
	 * @param toParse
	 *            The string to convert.
	 * @return The prescriptionFormat corresponding to the given string.
	 * @throws IllegalArgumentException
	 *             Throws an IllegalArgumentException if the string doesn't
	 *             match a known prescriptionFormat.
	 */
	public static PrescriptionFormat parseString(String toParse)
		throws IllegalArgumentException {
		switch (Integer.parseInt(toParse)) {
			case 1:
				return PrescriptionFormat.PLAIN_TEXT;
			case 2:
				return PrescriptionFormat.RICH_TEXT;
			default:
				throw new IllegalArgumentException(
					"Unrecognized PrescriptionFormat " + toParse);
		}
	}

	/**
	 * The value to pass or parse when talking to the External Prescriber functions.
	 */
	private final int prescriptionFormat;

	/**
	 * Create a PrescriptionFormat.
	 */
	PrescriptionFormat(int value) {
		this.prescriptionFormat = value;
	}

	/**
	 * Convert a prescriptionFormat for a string suitable for sending in a
	 * request.
	 * 
	 * @return A string representing this value, suitable for sending in a
	 *         request.
	 */
	public String getString() {
		return Integer.toString(this.prescriptionFormat);
	}

}
