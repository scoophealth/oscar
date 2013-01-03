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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

public class ClientSearchFormBean {
	private static Logger log = MiscUtils.getLogger();

	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
	private Calendar calendar = Calendar.getInstance();
	
	private String demographicNo;
	private String firstName;
	private String lastName;
	private String dob;
	private String healthCardNumber;
	private String healthCardVersion;
	private String gender;
	private String active;
	private boolean searchOutsideDomain;
	private boolean searchUsingSoundex;
	private String bedProgramId; 
	private String assignedToProviderNo; 
	private String dateFrom;
	private String dateTo;
	private List programDomain;
	private List genders;
	private String chartNo;
	
	public ClientSearchFormBean() {
		//setSearchOutsideDomain(true);
		//setSearchUsingSoundex(true);
	}
	
	/**
	 * @return Returns the dob.
	 */
	public String getDob() {
		return dob;
	}
	/**
	 * @param dob The dob to set.
	 */
	public void setDob(String dob) {
		this.dob = dob;
	}
	/**
	 * @return Returns the firstName.
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName The firstName to set.
	 */
	public void setFirstName(String firstName) {
		//this.firstName = firstName.replace('\'',' ');
		this.firstName = firstName;
	}
	/**
	 * @return Returns the healthCardNumber.
	 */
	public String getHealthCardNumber() {
		return healthCardNumber;
	}
	/**
	 * @param healthCardNumber The healthCardNumber to set.
	 */
	public void setHealthCardNumber(String healthCardNumber) {
		this.healthCardNumber = healthCardNumber;
	}
	/**
	 * @return Returns the healthCardVersion.
	 */
	public String getHealthCardVersion() {
		return healthCardVersion;
	}
	/**
	 * @param healthCardVersion The healthCardVersion to set.
	 */
	public void setHealthCardVersion(String healthCardVersion) {
		this.healthCardVersion = healthCardVersion;
	}
	/**
	 * @return Returns the lastName.
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName The lastName to set.
	 */
	public void setLastName(String lastName) {
		//this.lastName = lastName.replace('\'',' ');
		this.lastName = lastName;
	}
	/**
	 * @return Returns the searchOutsideDomain.
	 */
	public boolean isSearchOutsideDomain() {
		return searchOutsideDomain;
	}
	/**
	 * @param searchOutsideDomain The searchOutsideDomain to set.
	 */
	public void setSearchOutsideDomain(boolean searchOutsideDomain) {
		this.searchOutsideDomain = searchOutsideDomain;
	}
	
	/**
	 * @return Returns the searchUsingSoundex.
	 */
	public boolean isSearchUsingSoundex() {
		return searchUsingSoundex;
	}

	public void setSearchUsingSoundex(boolean searchUsingSoundex) {
		this.searchUsingSoundex = searchUsingSoundex;
	}
	
	public String getYearOfBirth() {
		try {
			Date d = formatter.parse(getDob());
			calendar.setTime(d);
			return String.valueOf(calendar.get(Calendar.YEAR));
		}catch(Exception e) {
			log.error("Error", e);
		}
		return null;
	}
	
	public String getMonthOfBirth() {
		try {
			Date d = formatter.parse(getDob());
			calendar.setTime(d);
			String value =  String.valueOf(calendar.get(Calendar.MONTH)+1);
			if(value.length()==1) {value= "0" + value;}
 			return value;
		}catch(Exception e) {
			log.error("Error", e);
		}
		return null;
	}
	
	public String getDayOfBirth() {
		try {
			Date d = formatter.parse(getDob());
			calendar.setTime(d);
			String value = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
			if(value.length()==1) {value= "0" + value;}
			return value;
		}catch(Exception e) {
			log.error("Error", e);
		}
		return null;
	}
	/**
	 * @return Returns the programDomain.
	 */
	public List getProgramDomain() {
		return programDomain;
	}
	/**
	 * @param programDomain The programDomain to set.
	 */
	public void setProgramDomain(List programDomain) {
		this.programDomain = programDomain;
	}
	/**
	 * @return Returns the bedProgramId.
	 */
	public String getBedProgramId() {
		return bedProgramId;
	}

	/**
	 * @param bedProgramId The bedProgramId to set.
	 */
	public void setBedProgramId(String bedProgramId) {
		this.bedProgramId = bedProgramId;
	}
	/**
	 * @return Returns the dateFrom.
	 */
	public String getDateFrom() {
		return dateFrom;
	}
	/**
	 * @param dateFrom The dateFrom to set.
	 */
	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}
	/**
	 * @return Returns the dateTo.
	 */
	public String getDateTo() {
		return dateTo;
	}
	/**
	 * @param dateTo The dateTo to set.
	 */
	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}

	public String getDemographicNo() {
		return demographicNo;
	}

	public void setDemographicNo(String demographicNo) {
		this.demographicNo = demographicNo;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public List getGenders() {
		return genders;
	}

	public void setGenders(List genders) {
		this.genders = genders;
	}
	public String getAssignedToProviderNo() {
		return assignedToProviderNo;
	}

	public void setAssignedToProviderNo(String assignedToProviderNo) {
		this.assignedToProviderNo = assignedToProviderNo;
	}

	public String getChartNo() {
    	return chartNo;
    }

	public void setChartNo(String chartNo) {
    	this.chartNo = chartNo;
    }
	
	
}
