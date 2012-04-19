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


package org.oscarehr.ws.transfer_objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.oscarehr.common.model.Demographic;
import org.springframework.beans.BeanUtils;

public final class DemographicTransfer {

	private Integer demographicNo;
	private String phone;
	private String patientStatus;
	private Date patientStatusDate;
	private String rosterStatus;
	private String providerNo;
	private String myOscarUserName;
	private String hin;
	private String address;
	private String province;
	private String monthOfBirth;
	private String ver;
	private String dateOfBirth;
	private String sex;
	private String sexDesc;
	private Date dateJoined;
	private String familyDoctor;
	private String city;
	private String firstName;
	private String postal;
	private Date hcRenewDate;
	private String phone2;
	private String pcnIndicator;
	private Date endDate;
	private String lastName;
	private String hcType;
	private String chartNo;
	private String email;
	private String yearOfBirth;
	private Date effDate;
	private Date rosterDate;
	private Date rosterTerminationDate;
	private String rosterTerminationReason;
	private String links;
	private String alias;
	private String previousAddress;
	private String children;
	private String sourceOfIncome;
	private String citizenship;
	private String sin;
	private Integer headRecord;
	private String anonymous;
	private String spokenLanguage;
	private int activeCount;
	private int hsAlertCount;
	private String displayName;
	private String lastUpdateUser;
	private Date lastUpdateDate;
	private String title;
	private String officialLanguage;

	
	public Integer getDemographicNo() {
    	return (demographicNo);
    }

	public void setDemographicNo(Integer demographicNo) {
    	this.demographicNo = demographicNo;
    }

	public String getPhone() {
    	return (phone);
    }

	public void setPhone(String phone) {
    	this.phone = phone;
    }

	public String getPatientStatus() {
    	return (patientStatus);
    }

	public void setPatientStatus(String patientStatus) {
    	this.patientStatus = patientStatus;
    }

	public Date getPatientStatusDate() {
    	return (patientStatusDate);
    }

	public void setPatientStatusDate(Date patientStatusDate) {
    	this.patientStatusDate = patientStatusDate;
    }

	public String getRosterStatus() {
    	return (rosterStatus);
    }

	public void setRosterStatus(String rosterStatus) {
    	this.rosterStatus = rosterStatus;
    }

	public String getProviderNo() {
    	return (providerNo);
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	public String getMyOscarUserName() {
    	return (myOscarUserName);
    }

	public void setMyOscarUserName(String myOscarUserName) {
    	this.myOscarUserName = myOscarUserName;
    }

	public String getHin() {
    	return (hin);
    }

	public void setHin(String hin) {
    	this.hin = hin;
    }

	public String getAddress() {
    	return (address);
    }

	public void setAddress(String address) {
    	this.address = address;
    }

	public String getProvince() {
    	return (province);
    }

	public void setProvince(String province) {
    	this.province = province;
    }

	public String getMonthOfBirth() {
    	return (monthOfBirth);
    }

	public void setMonthOfBirth(String monthOfBirth) {
    	this.monthOfBirth = monthOfBirth;
    }

	public String getVer() {
    	return (ver);
    }

	public void setVer(String ver) {
    	this.ver = ver;
    }

	public String getDateOfBirth() {
    	return (dateOfBirth);
    }

	public void setDateOfBirth(String dateOfBirth) {
    	this.dateOfBirth = dateOfBirth;
    }

	public String getSex() {
    	return (sex);
    }

	public void setSex(String sex) {
    	this.sex = sex;
    }

	public String getSexDesc() {
    	return (sexDesc);
    }

	public void setSexDesc(String sexDesc) {
    	this.sexDesc = sexDesc;
    }

	public Date getDateJoined() {
    	return (dateJoined);
    }

	public void setDateJoined(Date dateJoined) {
    	this.dateJoined = dateJoined;
    }

	public String getFamilyDoctor() {
    	return (familyDoctor);
    }

	public void setFamilyDoctor(String familyDoctor) {
    	this.familyDoctor = familyDoctor;
    }

	public String getCity() {
    	return (city);
    }

	public void setCity(String city) {
    	this.city = city;
    }

	public String getFirstName() {
    	return (firstName);
    }

	public void setFirstName(String firstName) {
    	this.firstName = firstName;
    }

	public String getPostal() {
    	return (postal);
    }

	public void setPostal(String postal) {
    	this.postal = postal;
    }

	public Date getHcRenewDate() {
    	return (hcRenewDate);
    }

	public void setHcRenewDate(Date hcRenewDate) {
    	this.hcRenewDate = hcRenewDate;
    }

	public String getPhone2() {
    	return (phone2);
    }

	public void setPhone2(String phone2) {
    	this.phone2 = phone2;
    }

	public String getPcnIndicator() {
    	return (pcnIndicator);
    }

	public void setPcnIndicator(String pcnIndicator) {
    	this.pcnIndicator = pcnIndicator;
    }

	public Date getEndDate() {
    	return (endDate);
    }

	public void setEndDate(Date endDate) {
    	this.endDate = endDate;
    }

	public String getLastName() {
    	return (lastName);
    }

	public void setLastName(String lastName) {
    	this.lastName = lastName;
    }

	public String getHcType() {
    	return (hcType);
    }

	public void setHcType(String hcType) {
    	this.hcType = hcType;
    }

	public String getChartNo() {
    	return (chartNo);
    }

	public void setChartNo(String chartNo) {
    	this.chartNo = chartNo;
    }

	public String getEmail() {
    	return (email);
    }

	public void setEmail(String email) {
    	this.email = email;
    }

	public String getYearOfBirth() {
    	return (yearOfBirth);
    }

	public void setYearOfBirth(String yearOfBirth) {
    	this.yearOfBirth = yearOfBirth;
    }

	public Date getEffDate() {
    	return (effDate);
    }

	public void setEffDate(Date effDate) {
    	this.effDate = effDate;
    }

	public Date getRosterDate() {
    	return (rosterDate);
    }

	public void setRosterDate(Date rosterDate) {
    	this.rosterDate = rosterDate;
    }

	public Date getRosterTerminationDate() {
    	return (rosterTerminationDate);
    }

	public void setRosterTerminationDate(Date rosterTerminationDate) {
    	this.rosterTerminationDate = rosterTerminationDate;
    }

	public String getRosterTerminationReason() {
    	return (rosterTerminationReason);
    }

	public void setRosterTerminationReason(String rosterTerminationReason) {
    	this.rosterTerminationReason = rosterTerminationReason;
    }

	public String getLinks() {
    	return (links);
    }

	public void setLinks(String links) {
    	this.links = links;
    }

	public String getAlias() {
    	return (alias);
    }

	public void setAlias(String alias) {
    	this.alias = alias;
    }

	public String getPreviousAddress() {
    	return (previousAddress);
    }

	public void setPreviousAddress(String previousAddress) {
    	this.previousAddress = previousAddress;
    }

	public String getChildren() {
    	return (children);
    }

	public void setChildren(String children) {
    	this.children = children;
    }

	public String getSourceOfIncome() {
    	return (sourceOfIncome);
    }

	public void setSourceOfIncome(String sourceOfIncome) {
    	this.sourceOfIncome = sourceOfIncome;
    }

	public String getCitizenship() {
    	return (citizenship);
    }

	public void setCitizenship(String citizenship) {
    	this.citizenship = citizenship;
    }

	public String getSin() {
    	return (sin);
    }

	public void setSin(String sin) {
    	this.sin = sin;
    }

	public Integer getHeadRecord() {
    	return (headRecord);
    }

	public void setHeadRecord(Integer headRecord) {
    	this.headRecord = headRecord;
    }

	public String getAnonymous() {
    	return (anonymous);
    }

	public void setAnonymous(String anonymous) {
    	this.anonymous = anonymous;
    }

	public String getSpokenLanguage() {
    	return (spokenLanguage);
    }

	public void setSpokenLanguage(String spokenLanguage) {
    	this.spokenLanguage = spokenLanguage;
    }

	public int getActiveCount() {
    	return (activeCount);
    }

	public void setActiveCount(int activeCount) {
    	this.activeCount = activeCount;
    }

	public int getHsAlertCount() {
    	return (hsAlertCount);
    }

	public void setHsAlertCount(int hsAlertCount) {
    	this.hsAlertCount = hsAlertCount;
    }

	public String getDisplayName() {
    	return (displayName);
    }

	public void setDisplayName(String displayName) {
    	this.displayName = displayName;
    }

	public String getLastUpdateUser() {
    	return (lastUpdateUser);
    }

	public void setLastUpdateUser(String lastUpdateUser) {
    	this.lastUpdateUser = lastUpdateUser;
    }

	public Date getLastUpdateDate() {
    	return (lastUpdateDate);
    }

	public void setLastUpdateDate(Date lastUpdateDate) {
    	this.lastUpdateDate = lastUpdateDate;
    }

	public String getTitle() {
    	return (title);
    }

	public void setTitle(String title) {
    	this.title = title;
    }

	public String getOfficialLanguage() {
    	return (officialLanguage);
    }

	public void setOfficialLanguage(String officialLanguage) {
    	this.officialLanguage = officialLanguage;
    }

	public static DemographicTransfer toTransfer(Demographic demographic) {
		if (demographic==null) return(null);
		
		DemographicTransfer demographicTransfer = new DemographicTransfer();

		BeanUtils.copyProperties(demographic, demographicTransfer);

		return (demographicTransfer);
	}

	public static DemographicTransfer[] toTransfers(List<Demographic> demographics) {
		ArrayList<DemographicTransfer> results = new ArrayList<DemographicTransfer>();

		for (Demographic demographic : demographics) {
			results.add(toTransfer(demographic));
		}

		return (results.toArray(new DemographicTransfer[0]));
	}

	@Override
	public String toString() {
		return (ReflectionToStringBuilder.toString(this));
	}
}
