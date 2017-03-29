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

public class DemographicExtra {

	private String middleName="";
	private String preferredName="";
	private String lastNameAtBirth="";
	private String maritalStatus="";
	private String recipientLocation="";
	private String lhinConsumerResides="";
	private String address2="";
	
	public String getMiddleName() {
    	return middleName;
    }
	public void setMiddleName(String middleName) {
    	this.middleName = middleName;
    }
	public String getPreferredName() {
    	return preferredName;
    }
	public void setPreferredName(String preferredName) {
    	this.preferredName = preferredName;
    }
	public String getLastNameAtBirth() {
    	return lastNameAtBirth;
    }
	public void setLastNameAtBirth(String lastNameAtBirth) {
    	this.lastNameAtBirth = lastNameAtBirth;
    }
	public String getMaritalStatus() {
    	return maritalStatus;
    }
	public void setMaritalStatus(String maritalStatus) {
    	this.maritalStatus = maritalStatus;
    }
	public String getRecipientLocation() {
    	return recipientLocation;
    }
	public void setRecipientLocation(String recipientLocation) {
    	this.recipientLocation = recipientLocation;
    }
	public String getLhinConsumerResides() {
    	return lhinConsumerResides;
    }
	public void setLhinConsumerResides(String lhinConsumerResides) {
    	this.lhinConsumerResides = lhinConsumerResides;
    }
	public String getAddress2() {
    	return address2;
    }
	public void setAddress2(String address2) {
    	this.address2 = address2;
    }
	
	
} 
