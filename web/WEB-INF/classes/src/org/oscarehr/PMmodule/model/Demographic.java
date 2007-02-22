/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/

package org.oscarehr.PMmodule.model;

import java.util.Calendar;
import java.util.Date;

import org.oscarehr.PMmodule.model.base.BaseDemographic;
import org.oscarehr.PMmodule.utility.Utility;

/**
 * This is the object class that relates to the demographic table. Any customizations belong here.
 */
public class Demographic extends BaseDemographic {

	private static final long serialVersionUID = 1L;
	
	private static final String SEPERATOR = "-";

	/* [CONSTRUCTOR MARKER END] */
	public Demographic() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Demographic(java.lang.Integer _demographicNo) {
		super(_demographicNo);
	}

	/**
	 * Constructor for required fields
	 */
	public Demographic(java.lang.Integer _demographicNo, java.lang.String _firstName, java.lang.String _lastName) {
		super(_demographicNo, _firstName, _lastName);
	}
	/* [CONSTRUCTOR MARKER BEGIN] */

	private long agencyId;
	private String links = "";
	private DemographicExt[] extras;

	public String addZero(String text, int num) {
		text = text.trim();
		String zero = "0";
		for (int i = text.length(); i < num; i++) {
			text = zero + text;
		}
		return text;
	}

	public String getAge() {
		return (String.valueOf(Utility.calcAge(Utility.convertToReplaceStrIfEmptyStr(super.getYearOfBirth(), "0001"), Utility.convertToReplaceStrIfEmptyStr(super.getMonthOfBirth(), "01"), Utility.convertToReplaceStrIfEmptyStr(super.getDateOfBirth(), "01"))));
	}

	public String getAgeAsOf(Date asofDate) {
		return Utility.calcAgeAtDate(Utility.calcDate(Utility.convertToReplaceStrIfEmptyStr(super.getYearOfBirth(), "0001"), Utility.convertToReplaceStrIfEmptyStr(super.getMonthOfBirth(), "01"), Utility.convertToReplaceStrIfEmptyStr(super.getDateOfBirth(), "01")), asofDate);
	}

	public int getAgeInYears() {
		return Utility.getNumYears(Utility.calcDate(Utility.convertToReplaceStrIfEmptyStr(super.getYearOfBirth(), "0001"), Utility.convertToReplaceStrIfEmptyStr(super.getMonthOfBirth(), "01"), Utility.convertToReplaceStrIfEmptyStr(super.getDateOfBirth(), "01")), Calendar.getInstance().getTime());
	}

	public int getAgeInYearsAsOf(Date asofDate) {
		return Utility.getNumYears(Utility.calcDate(Utility.convertToReplaceStrIfEmptyStr(super.getYearOfBirth(), "0001"), Utility.convertToReplaceStrIfEmptyStr(super.getMonthOfBirth(), "01"), Utility.convertToReplaceStrIfEmptyStr(super.getDateOfBirth(), "01")), asofDate);
	}

	public long getAgencyId() {
		return agencyId;
	}

	public String getAgencyName() {
		return Agency.getAgencyName(agencyId);
	}
	
	public DemographicExt[] getExtras() {
		return extras;
	}

	public String getFormattedDob() {
		return this.getYearOfBirth() + SEPERATOR + this.getMonthOfBirth() + SEPERATOR + this.getDateOfBirth();
	}

	public String getFormattedLinks() {
		StringBuilder response = new StringBuilder();

		if (getNumLinks() > 0) {
			String[] links = getLinks().split(",");
			for (int x = 0; x < links.length; x++) {
				String components[] = links[x].split("/");
				String agencyId = components[0];
				if (response.length() > 0) {
					response.append(",");
				}
				response.append(Agency.getAgencyName(Long.valueOf(agencyId)));

			}
		}

		return response.toString();
	}

	public String getFormattedName() {
		return getLastName() + ", " + getFirstName();
	}

	public String getLinks() {
		return links;
	}

	public int getNumLinks() {
		if (getLinks() == null) {
			return 0;
		}

		if (getLinks().equals("")) {
			return 0;
		}

		return getLinks().split(",").length;
	}

	public void setAgencyId(long agencyId) {
		this.agencyId = agencyId;
	}

	public void setExtras(DemographicExt[] extras) {
		this.extras = extras;
	}

	public void setLinks(String links) {
		this.links = links;
	}

}