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

import java.util.Collection;

import org.apache.struts.action.ActionForm;
import org.apache.struts.util.LabelValueBean;
import org.oscarehr.common.model.Demographic;

public class GenericIntakeSearchFormBean extends ActionForm {

	private LabelValueBean[] months;
	private LabelValueBean[] days;

	private String method;

	private String firstName;
	private String lastName;
	private String monthOfBirth;
	private String dayOfBirth;
	private String yearOfBirth;
	private String healthCardNumber;
	private String healthCardVersion;
	private String gender;

    private boolean searchPerformed = false;

    private Collection<Demographic> localMatches;

    private String localAgencyUsername;

    private Integer demographicId;

	public GenericIntakeSearchFormBean() {
		setMonths(GenericIntakeConstants.MONTHS);
		setDays(GenericIntakeConstants.DAYS);
    }

    public boolean isSearchPerformed() {
        return searchPerformed;
    }

    public void setSearchPerformed(boolean searchPerformed) {
        this.searchPerformed = searchPerformed;
    }

    public LabelValueBean[] getMonths() {
		return months;
	}

	public void setMonths(LabelValueBean[] months) {
		this.months = months;
	}

	public LabelValueBean[] getDays() {
		return days;
	}

	public void setDays(LabelValueBean[] days) {
		this.days = days;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getMonthOfBirth() {
		return monthOfBirth;
	}

	public String getDayOfBirth() {
		return dayOfBirth;
	}

	public String getYearOfBirth() {
		return yearOfBirth;
	}

	public String getHealthCardNumber() {
		return healthCardNumber;
	}

	public String getHealthCardVersion() {
		return healthCardVersion;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setMonthOfBirth(String monthOfBirth) {
		this.monthOfBirth = monthOfBirth;
	}

	public void setDayOfBirth(String dayOfBirth) {
		this.dayOfBirth = dayOfBirth;
	}

	public void setYearOfBirth(String yearOfBirth) {
		this.yearOfBirth = yearOfBirth;
	}

	public void setHealthCardNumber(String healthCardNumber) {
		this.healthCardNumber = healthCardNumber;
	}

	public void setHealthCardVersion(String healthCardVersion) {
		this.healthCardVersion = healthCardVersion;
	}


    public Collection<Demographic> getLocalMatches() {
        return localMatches;
    }

    public void setLocalMatches(Collection<Demographic> localMatches) {
        this.localMatches = localMatches;
    }

    public Integer getDemographicId() {
        return demographicId;
    }

    public void setDemographicId(Integer demographicId) {
        this.demographicId = demographicId;
    }

    public String getLocalAgencyUsername() {
        return localAgencyUsername;
    }

    public void setLocalAgencyUsername(String localAgencyUsername) {
        this.localAgencyUsername = localAgencyUsername;
    }

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
}
