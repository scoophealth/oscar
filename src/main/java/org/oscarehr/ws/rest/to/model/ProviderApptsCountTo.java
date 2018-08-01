/**
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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
 */


package org.oscarehr.ws.rest.to.model;

public class ProviderApptsCountTo {
	private String providerNo;
	private String providerName;
	private Long appointmentCount = 0L;
	
	public ProviderApptsCountTo() {
		super();
	}

	public ProviderApptsCountTo(String providerNo, String providerName, Long appointmentCount) {
		super();
		this.providerNo = providerNo;
		this.providerName = providerName;
		this.appointmentCount = appointmentCount;
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

	public Long getAppointmentCount() {
		return appointmentCount;
	}

	public void setAppointmentCount(Long appointmentCount) {
		this.appointmentCount = appointmentCount;
	}

	@Override
	public String toString() {
		return "ApptProviderCountTo [providerNo=" + providerNo + ", appointmentCount=" + appointmentCount + "]";
	}
	
}
