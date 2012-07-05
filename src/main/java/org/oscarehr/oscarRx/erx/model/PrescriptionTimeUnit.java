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
 * 
 */
public enum PrescriptionTimeUnit {
	/**
	 * The time unit is per-day (daily).
	 */
	DAY(1),
	/**
	 * The time unit is per-week (weekly).
	 */
	WEEK(2),
	/**
	 * The time unit is per-month (monthly).
	 */
	MONTH(3),
	/**
	 * The time unit is per-year (yearly).
	 */
	YEAR(4),
	/**
	 * The time unit was not given.
	 */
	EMPTY_VALUE(-1);

	/**
	 * Convert a string (passed in a response) to it's corresponding
	 * PrescriptionTimeUnit.
	 * 
	 * @param toParse
	 *            The string to convert.
	 * @return The PrescriptionTimeUnit corresponding to the given string.
	 * @throws IllegalArgumentException
	 *             Throws an IllegalArgumentException if the string doesn't
	 *             match a known PrescriptionTimeUnit.
	 */
	public static PrescriptionTimeUnit parseString(String toParse)
	    throws IllegalArgumentException {
		switch (Integer.parseInt(toParse)) {
			case -1:
				return EMPTY_VALUE;
			case 1:
				return DAY;
			case 2:
				return WEEK;
			case 3:
				return MONTH;
			case 4:
				return YEAR;
			default:
				throw new IllegalArgumentException(
				    "Unrecognized PrescriptionTimeUnit " + toParse);
		}
	}

	/**
	 * The value to pass or parse when talking with the External Prescriber functions.
	 */
	public final int prescriptionTimeUnitId;

	/**
	 * Construct a PrescriptionTimeUnit.
	 * 
	 * @param prescriptionTimeUnitId
	 *            An integer value which must be passed in a request or parsed
	 *            in a response.
	 */
	PrescriptionTimeUnit(int prescriptionTimeUnitId) {
		this.prescriptionTimeUnitId = prescriptionTimeUnitId;
	}

	/**
	 * Convert a PrescriptionTimeUnit to a string suitable for sending in a
	 * request.
	 * 
	 * @return A string representing this value, suitable for sending in a
	 *         request.
	 */
	public String getString() {
		return Integer.toString(this.prescriptionTimeUnitId);
	}
}
