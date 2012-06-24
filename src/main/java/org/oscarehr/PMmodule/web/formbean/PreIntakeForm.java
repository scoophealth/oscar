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

package org.oscarehr.PMmodule.web.formbean;

public class PreIntakeForm {
	
	private String demographicId;
	private String dayOfBirth;
	private String firstName;
	private String healthCardNumber;
	private String healthCardVersion;
	private String lastName;
	private String monthOfBirth;
	private String yearOfBirth;

	public String getDemographicId() {
		return demographicId;
	}

	public String getDayOfBirth() {
		return dayOfBirth;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getHealthCardNumber() {
		return healthCardNumber;
	}

	public String getHealthCardVersion() {
		return healthCardVersion;
	}

	public String getLastName() {
		return lastName;
	}

	public String getMonthOfBirth() {
		return monthOfBirth;
	}

	public String getYearOfBirth() {
		return yearOfBirth;
	}

	public void setDemographicId(String demographicId) {
		this.demographicId = demographicId;
	}

	public void setDayOfBirth(String dayOfBirth) {
		this.dayOfBirth = dayOfBirth;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setHealthCardNumber(String healthCardNumber) {
		this.healthCardNumber = healthCardNumber;
	}

	public void setHealthCardVersion(String healthCardVersion) {
		this.healthCardVersion = healthCardVersion;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setMonthOfBirth(String monthOfBirth) {
		this.monthOfBirth = monthOfBirth;
	}

	public void setYearOfBirth(String yearOfBirth) {
		this.yearOfBirth = yearOfBirth;
	}

}
