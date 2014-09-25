/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
package org.oscarehr.ws.rest.to.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tools.ant.util.DateUtils;

public class DemographicSearchResult {

	private SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.ISO8601_DATE_PATTERN);
	
	private Integer demographicNo;
	private String lastName;
	private String firstName;
	private String chartNo;
	private String sex;
	private Date dob;
	private String providerNo;
	private String providerName;
	private String rosterStatus;
	private String patientStatus;
	private String phone;
	private String hin;
	
	private Integer remoteFacilityId;
	
	
	public DemographicSearchResult() {
		
	}
	
	public DemographicSearchResult(Integer demographicNo, String lastName, String firstName, String chartNo, String sex, String providerNo, 
			String rosterStatus, String patientStatus, String phone, Date dob, String providerLastName, String providerFirstName, String hin) {
		setDemographicNo(demographicNo);
		setLastName(lastName);
		setFirstName(firstName);
		setChartNo(chartNo);
		setSex(sex);
		setProviderNo(providerNo);
		setRosterStatus(rosterStatus);
		setPatientStatus(patientStatus);
		setPhone(phone);
		setDob(dob);
		setHin(hin);
		
		if(providerLastName != null && providerFirstName != null) {
			setProviderName(providerLastName + "," + providerFirstName);
		}
	}

	public Integer getDemographicNo() {
		return demographicNo;
	}
	public void setDemographicNo(Integer demographicNo) {
		this.demographicNo = demographicNo;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getChartNo() {
		return chartNo;
	}
	public void setChartNo(String chartNo) {
		this.chartNo = chartNo;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public String getProviderNo() {
		return providerNo;
	}
	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}
	public String getProviderName() {
		return providerName;
	}
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
	public String getRosterStatus() {
		return rosterStatus;
	}
	public void setRosterStatus(String rosterStatus) {
		this.rosterStatus = rosterStatus;
	}
	public String getPatientStatus() {
		return patientStatus;
	}
	public void setPatientStatus(String patientStatus) {
		this.patientStatus = patientStatus;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
	
	public String getHin() {
		return hin;
	}

	public void setHin(String hin) {
		this.hin = hin;
	}

	public String getFormattedDOB() {
		return getDob()!=null? sdf.format(getDob()):null;
	}

	public Integer getRemoteFacilityId() {
		return remoteFacilityId;
	}

	public void setRemoteFacilityId(Integer remoteFacilityId) {
		this.remoteFacilityId = remoteFacilityId;
	}
	
	
}
