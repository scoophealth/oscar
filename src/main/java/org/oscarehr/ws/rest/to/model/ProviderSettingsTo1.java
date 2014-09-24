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

public class ProviderSettingsTo1 {

	private String rxAddress;
	private String rxCity;
	private String rxProvince;
	private String rxPostal;
	private String rxPhone;
	private String faxNumber;
	private String newTicklerWarningWindow;
	private String workloadManagement;
	private String ticklerWarningProvider;
	public String getRxAddress() {
		return rxAddress;
	}
	public void setRxAddress(String rxAddress) {
		this.rxAddress = rxAddress;
	}
	public String getRxCity() {
		return rxCity;
	}
	public void setRxCity(String rxCity) {
		this.rxCity = rxCity;
	}
	public String getRxProvince() {
		return rxProvince;
	}
	public void setRxProvince(String rxProvince) {
		this.rxProvince = rxProvince;
	}
	public String getRxPostal() {
		return rxPostal;
	}
	public void setRxPostal(String rxPostal) {
		this.rxPostal = rxPostal;
	}
	public String getRxPhone() {
		return rxPhone;
	}
	public void setRxPhone(String rxPhone) {
		this.rxPhone = rxPhone;
	}
	public String getFaxNumber() {
		return faxNumber;
	}
	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}
	public String getNewTicklerWarningWindow() {
		return newTicklerWarningWindow;
	}
	public void setNewTicklerWarningWindow(String newTicklerWarningWindow) {
		this.newTicklerWarningWindow = newTicklerWarningWindow;
	}
	public String getWorkloadManagement() {
		return workloadManagement;
	}
	public void setWorkloadManagement(String workloadManagement) {
		this.workloadManagement = workloadManagement;
	}
	public String getTicklerWarningProvider() {
		return ticklerWarningProvider;
	}
	public void setTicklerWarningProvider(String ticklerWarningProvider) {
		this.ticklerWarningProvider = ticklerWarningProvider;
	}
	
}
