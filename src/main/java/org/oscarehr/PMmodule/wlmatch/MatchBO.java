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

package org.oscarehr.PMmodule.wlmatch;

public class MatchBO {
	int clientID=0;
	String clientName=null;
	
	int daysInWaitList=0;
	int daysSinceLastContact=0;
	int contactAttempts=0;
	double percentageMatch=0;
	String proportion = "";
	int formDataID=0;
	
	public String getProportion() {
		return proportion;
	}
	public void setProportion(String proportion) {
		this.proportion = proportion;
	}
	
	public int getFormDataID() {
		return formDataID;
	}
	public void setFormDataID(int formDataID) {
		this.formDataID = formDataID;
	}
	public int getClientID() {
		return clientID;
	}
	public void setClientID(int clientID) {
		this.clientID = clientID;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public int getDaysInWaitList() {
		return daysInWaitList;
	}
	public void setDaysInWaitList(int daysInWaitList) {
		this.daysInWaitList = daysInWaitList;
	}
	public int getDaysSinceLastContact() {
		return daysSinceLastContact;
	}
	public void setDaysSinceLastContact(int daysSinceLastContact) {
		this.daysSinceLastContact = daysSinceLastContact;
	}
	public int getContactAttempts() {
		return contactAttempts;
	}
	public void setContactAttempts(int contactAttempts) {
		this.contactAttempts = contactAttempts;
	}
	public double getPercentageMatch() {
		return percentageMatch;
	}
	public void setPercentageMatch(double percentageMatch) {
		this.percentageMatch = percentageMatch;
	}
	@Override
    public String toString() {
	    return "MatchBO [clientID=" + clientID + ", clientName=" + clientName + 
	    ", daysInWaitList=" + daysInWaitList + ", daysSinceLastContact=" + daysSinceLastContact + 
	    ", contactAttempts=" + contactAttempts + ", percentageMatch=" + percentageMatch + ",proportion=" + proportion + 
	    ", formDataID=" + formDataID + "]";
    }
	
	
}
