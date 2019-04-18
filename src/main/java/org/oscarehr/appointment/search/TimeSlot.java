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
import java.util.Comparator;

public class TimeSlot{
	String providerNo = null;
	private Integer demographicNo = null;
	String providerName = null;
	Calendar availableApptTime = null;
	Character code = null;
	Long appointmentType = null;

	public TimeSlot(){}

	public TimeSlot(String providerNo, String providerName, Calendar cal){
		this.providerNo = providerNo;
		this.providerName = providerName;
		this.availableApptTime = cal;
	}

	public TimeSlot(String providerNo, String providerName, Calendar cal, Character code){
		this.providerNo = providerNo;
		this.providerName = providerName;
		this.availableApptTime = cal;
		this.code = code;
	}

	public String getProviderNo(){
		return providerNo;
	}

	public void setProviderNo(String providerNo){
		this.providerNo = providerNo;
	}

	public String getProviderName(){
		return providerName;
	}

	public void setProviderName(String providerName){
		this.providerName = providerName;
	}

	public Calendar getAvailableApptTime(){
		return availableApptTime;
	}

	public void setAvailableApptTime(Calendar availableApptTime){
		this.availableApptTime = availableApptTime;
	}

	public Character getCode(){
		return code;
	}

	public void setCode(Character code){
		this.code = code;
	}

	public Long getAppointmentType(){
		return appointmentType;
	}

	public void setAppointmentType(Long appointmentType){
		this.appointmentType = appointmentType;
	}

	private static final Comparator<TimeSlot> TIMESLOT_DATE_COMPARATOR = new Comparator<TimeSlot>(){
		@Override
		public int compare(TimeSlot arg0, TimeSlot arg1){
			if (arg0 == null) return 1;
			if (arg1 == null) return -1;

			if (arg0.getAvailableApptTime().before(arg1.getAvailableApptTime())) return -1;
			if (arg1.getAvailableApptTime().before(arg0.getAvailableApptTime())) return 1;
			return 0;
		}
	};

	public static Comparator<TimeSlot> getTimeSlotComparator(){
		return TIMESLOT_DATE_COMPARATOR;
	}

	public Integer getDemographicNo() {
		return demographicNo;
	}

	public void setDemographicNo(Integer demographicNo) {
		this.demographicNo = demographicNo;
	}

}
