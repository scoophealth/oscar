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
package org.oscarehr.appointment.search;

import java.util.Calendar;

public class AppointmentOptionTransfer {
	private Calendar availableTime = null;
	private String encString = null;
	private String providerName = null;
	private String timeDisplay = null;
	private boolean isCancelled;
	
	public AppointmentOptionTransfer(){}
	
	
	public String getTimeDisplay() {
		return timeDisplay;
	}


	public AppointmentOptionTransfer(Calendar availableTime,String timeDisplay ,String providerName,String encString){
		this.availableTime = availableTime;
		this.encString = encString;
		this.providerName = providerName;
		this.timeDisplay =timeDisplay;
	}
	
	
	public Calendar getAvailableTime() {
		return availableTime;
	}
	public String getEncString() {
		return encString;
	}
	public String getProviderName() {
		return providerName;
	}


	public void setAvailableTime(Calendar availableTime) {
		this.availableTime = availableTime;
	}


	public void setEncString(String encString) {
		this.encString = encString;
	}


	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}


	public void setTimeDisplay(String timeDisplay) {
		this.timeDisplay = timeDisplay;
	}


	public boolean isCancelled() {
		return isCancelled;
	}


	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}
}
