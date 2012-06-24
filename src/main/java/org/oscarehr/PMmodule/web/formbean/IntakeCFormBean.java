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

public class IntakeCFormBean {
	private String tab;
	public static final String[] tabs = {"Step 1","Step 2","Step 3","Step 4","Step 5","Step 6"};
	public static final String[] tabDescr = {"Identifying  Information","Presenting and  Referral Information","Legal and  ID Status","Medical Status","Social and Living  Arrangements","Employment, Education  and Income Status"};
	private int numPastAddresses;
	private int numContacts;
	private int numIdentification;
	private long admissionProgram;
	private int numHospitalization;
	private final String version = "1.1.0";
	
	public String getVersion() {
		return version;
	}
	public IntakeCFormBean() {
		numContacts = 1;
		numIdentification = 1;
		numHospitalization=1;
	}
	/**
	 * @return Returns the tab.
	 */
	public String getTab() {
		return tab;
	}
	/**
	 * @param tab The tab to set.
	 */
	public void setTab(String tab) {
		this.tab = tab;
	}
	public int getNumPastAddresses() {
		return numPastAddresses;
	}
	public void setNumPastAddresses(int numPastAddresses) {
		this.numPastAddresses = numPastAddresses;
	}
	public int getNumContacts() {
		return numContacts;
	}
	public void setNumContacts(int numContacts) {
		this.numContacts = numContacts;
	}
	public int getNumIdentification() {
		return numIdentification;
	}
	public void setNumIdentification(int numIdentification) {
		this.numIdentification = numIdentification;
	}
	public static String[] getTabDescr() {
		return tabDescr;
	}
	public long getAdmissionProgram() {
		return admissionProgram;
	}
	public void setAdmissionProgram(long admissionProgram) {
		this.admissionProgram = admissionProgram;
	}
	public int getNumHospitalization() {
		return numHospitalization;
	}
	public void setNumHospitalization(int numHospitalization) {
		this.numHospitalization = numHospitalization;
	}
}
