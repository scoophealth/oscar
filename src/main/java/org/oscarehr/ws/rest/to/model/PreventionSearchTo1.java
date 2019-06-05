package org.oscarehr.ws.rest.to.model;

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

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class PreventionSearchTo1 {

	private Integer id;
	private String reportName;
	private Integer ageStyle;
	private String age1;
	private String age2;
	
	private Integer ageCalc;
	private Date ageAsOf;
	private String rosterStat;
	private Date rosterAsOf;
	private String measurementTrackingType;
	
	private boolean letter1 = false;
	private boolean letter2 = false;
	private boolean phone1 = false;

	
		    
	private Integer sex;
	
	private List<PreventionSearchConfigTo1> preventions;
	private List<String> exclusionCodes;
	private List<String> trackingCodes;
	
	private Date billingCodeStart;
	private Date billingCodeEnd; 
	
	private Date billingTrackingCodeStart;
	private Date billingTrackingCodeEnd;
	
	private String providerNo;
	private String providerName;
	
	/*
	FLU
	flu prevention by Jan 31st of patients that are 65> by Dec 31st
	
	pap prevention within 42 Months prior to March 31st of patients that are 21 to 69 as of march 31st
	
	mam prevention within 30 months prior to march 31st of patients that are 50 to 74 as of March 31st
	
	Child
	All ministry supplied imms by the age of 30 months of patients 30 to 42 months as of march 31
	
	FOBT preventin with 30 months prior to March 31st of patients 50 to 74 months as of march 31
	*/
	/*
     * 5 States:  How to find out which bucket the patient goes in?
     *   1. No Info   --no exclusion codes --no tracking codes -- no previous prevs
     *   2. Overdue   --no exclusion codes --no tracking codes --previous prevs
     *   3. Refused   --one of the last preventions is refused
     *   4. Up to date --tracking code present in the time period --preventions meeting the requirements
     *   5. Ineligible --exclusion codes
     
     * Process Map
     * two ways do to this.:
     *  1. run through list and figure out which bucket each patient should go in
     *  2. for each state run through the list and find which patients are in that state. removing the patients from the list as it goes
     *  
     Are there any exclusion codes for this Search
     */
	

	public Integer getAgeStyle() {
		return ageStyle;
	}

	public void setAgeStyle(Integer ageStyle) {
		this.ageStyle = ageStyle;
	}

	public String getAge1() {
		return age1;
	}

	public void setAge1(String age1) {
		this.age1 = age1;
	}

	public String getAge2() {
		return age2;
	}

	public void setAge2(String age2) {
		this.age2 = age2;
	}

	public Integer getAgeCalc() {
		return ageCalc;
	}

	public void setAgeCalc(Integer ageCalc) {
		this.ageCalc = ageCalc;
	}

	public Date getAgeAsOf() {
		return ageAsOf;
	}

	public void setAgeAsOf(Date ageAsOf) {
		this.ageAsOf = ageAsOf;
	}

	public String getRosterStat() {
		return rosterStat;
	}

	public void setRosterStat(String rosterStat) {
		this.rosterStat = rosterStat;
	}

	public Date getRosterAsOf() {
		return rosterAsOf;
	}

	public void setRosterAsOf(Date rosterAsOf) {
		this.rosterAsOf = rosterAsOf;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<PreventionSearchConfigTo1> getPreventions() {
		return preventions;
	}

	public void setPreventions(List<PreventionSearchConfigTo1> preventions) {
		this.preventions = preventions;
	}

	public List<String> getTrackingCodes() {
		return trackingCodes;
	}

	public void setTrackingCodes(List<String> trackingCodes) {
		this.trackingCodes = trackingCodes;
	}

	public List<String> getExclusionCodes() {
		return exclusionCodes;
	}

	public void setExclusionCodes(List<String> exclusionCodes) {
		this.exclusionCodes = exclusionCodes;
	}
    		 
	
	@Override
	public String toString(){
		return(ReflectionToStringBuilder.toString(this));
	}

	public String getMeasurementTrackingType() {
		return measurementTrackingType;
	}

	public void setMeasurementTrackingType(String measurementTrackingType) {
		this.measurementTrackingType = measurementTrackingType;
	}

	public Date getBillingCodeStart() {
		return billingCodeStart;
	}

	public void setBillingCodeStart(Date billingCodeStart) {
		this.billingCodeStart = billingCodeStart;
	}

	public Date getBillingCodeEnd() {
		return billingCodeEnd;
	}

	public void setBillingCodeEnd(Date billingCodeEnd) {
		this.billingCodeEnd = billingCodeEnd;
	}

	public boolean isLetter1() {
		return letter1;
	}

	public void setLetter1(boolean letter1) {
		this.letter1 = letter1;
	}

	public boolean isLetter2() {
		return letter2;
	}

	public void setLetter2(boolean letter2) {
		this.letter2 = letter2;
	}

	public boolean isPhone1() {
		return phone1;
	}

	public void setPhone1(boolean phone1) {
		this.phone1 = phone1;
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

	public Date getBillingTrackingCodeStart() {
		return billingTrackingCodeStart;
	}

	public void setBillingTrackingCodeStart(Date billingTrackingCodeStart) {
		this.billingTrackingCodeStart = billingTrackingCodeStart;
	}

	public Date getBillingTrackingCodeEnd() {
		return billingTrackingCodeEnd;
	}

	public void setBillingTrackingCodeEnd(Date billingTrackingCodeEnd) {
		this.billingTrackingCodeEnd = billingTrackingCodeEnd;
	}
	
	
}
