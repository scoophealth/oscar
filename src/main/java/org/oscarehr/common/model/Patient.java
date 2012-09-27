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

package org.oscarehr.common.model;

import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.util.SpringUtils;

/**
 * @author Jeremy Ho
 * This class is meant to be a model for a "patient" which contains all data required to define a "patient"
 */
public class Patient {
	private Integer demographicNo;
	private Demographic demographic;
	private DemographicDao demographicDao = (DemographicDao)SpringUtils.getBean("demographicDao");
	
	public Patient() {
	}
	
	public Patient(String demoNo) {
		this.demographicNo = new Integer(demoNo);
		initialize(demoNo);
	}

	private void initialize(String demoNo) {
		this.demographic = demographicDao.getDemographic(demoNo);
	}
	
	/*
	 * Directly mappable functions
	 */
	
	public String getDemographicNo() {
		return demographicNo.toString();
	}
	
	public void setDemographicNo(String demoNo) {
		demographicNo = Integer.parseInt(demoNo);
		demographic.setDemographicNo(demographicNo);
	}
	
	public String getFirstName() {
		return demographic.getFirstName();
	}

	public void setFirstName(String firstName) {
		demographic.setFirstName(firstName);
	}
	
	public String getLastName() {
		return demographic.getLastName();
	}

	public void setLastName(String lastName) {
		demographic.setLastName(lastName);
	}
	
	public String getGender() {
		return demographic.getSex();
	}

	public void setGender(String sex) {
		demographic.setSex(sex);
	}
	
	public String getDateOfBirth() {
		return demographic.getDateOfBirth();
	}

	public void setDateOfBirth(String dateOfBirth) {
		demographic.setDateOfBirth(dateOfBirth);
	}
	
	public String getMonthOfBirth() {
		return demographic.getMonthOfBirth();
	}

	public void setMonthOfBirth(String monthOfBirth) {
		demographic.setMonthOfBirth(monthOfBirth);
	}
	
	public String getYearOfBirth() {
		return demographic.getYearOfBirth();
	}

	public void setYearOfBirth(String yearOfBirth) {
		demographic.setYearOfBirth(yearOfBirth);
	}
	
	public String getHin() {
		return demographic.getHin();
	}
	
	public void setHin(String hin) {
		demographic.setHin(hin);
	}
	
	/*
	 * Output get convenience functions
	 */
	
	public String getBirthDate() {
		return demographic.getYearOfBirth() + demographic.getMonthOfBirth() + demographic.getDateOfBirth();
	}
	
	public String getGenderDesc() {
		if(demographic.getSex().equals("M")) return "Male";
		else return "Female";
	}	
}
