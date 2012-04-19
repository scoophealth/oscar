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


package org.oscarehr.appointment.web;

public class NextAppointmentSearchBean {
	private String providerNo;
	private String dayOfWeek;
	private String startTimeOfDay;
	private String endTimeOfDay;
	private String code;
	private int numResults;
	
	public String getProviderNo() {
    	return providerNo;
    }
	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }
	public String getDayOfWeek() {
    	return dayOfWeek;
    }
	public void setDayOfWeek(String dayOfWeek) {
    	this.dayOfWeek = dayOfWeek;
    }
	public String getStartTimeOfDay() {
    	return startTimeOfDay;
    }
	public void setStartTimeOfDay(String startTimeOfDay) {
    	this.startTimeOfDay = startTimeOfDay;
    }
	public String getEndTimeOfDay() {
    	return endTimeOfDay;
    }
	public void setEndTimeOfDay(String endTimeOfDay) {
    	this.endTimeOfDay = endTimeOfDay;
    }
	public String getCode() {
    	return code;
    }
	public void setCode(String code) {
    	this.code = code;
    }
	public int getNumResults() {
    	return numResults;
    }
	public void setNumResults(int numResults) {
    	this.numResults = numResults;
    }
	
	
}
