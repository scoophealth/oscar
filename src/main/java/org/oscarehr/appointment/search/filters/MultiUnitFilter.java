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
package org.oscarehr.appointment.search.filters;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.oscarehr.appointment.search.SearchConfig;
import org.oscarehr.appointment.search.TimeSlot;
import org.oscarehr.managers.DayWorkSchedule;


public class MultiUnitFilter  implements AvailableTimeSlotFilter{
		
	@Override
	public List<TimeSlot> filterAvailableTimeSlots(SearchConfig clinic,String mrp,String providerId, Long appointmentTypeId, DayWorkSchedule dayWorkScheduleTransfer, List<TimeSlot> currentlyAllowedTimeSlots, Calendar date,Map<String,String> params){
		int timeSlotLen = dayWorkScheduleTransfer.getTimeSlotDurationMin();
		// allowed time codes
		ArrayList<TimeSlot> filteredResults = new ArrayList<TimeSlot>();
		for(int i =0; i < currentlyAllowedTimeSlots.size();i++){
			TimeSlot entry = currentlyAllowedTimeSlots.get(i);
			Calendar apptStartTime = entry.getAvailableApptTime();
			Character code = entry.getCode();
			int apptLength = clinic.getAppointmentDuration(mrp, providerId, appointmentTypeId, code);
			
			if(apptLength>timeSlotLen){
				Calendar nextAppt = (Calendar) apptStartTime.clone();
				int apptBlockLen = timeSlotLen;
				for(int j = (i+1);j< currentlyAllowedTimeSlots.size();j++){
					apptBlockLen = apptBlockLen + timeSlotLen;
					
					TimeSlot nextTimeSlot = currentlyAllowedTimeSlots.get(j);
					nextAppt.add(Calendar.MINUTE, timeSlotLen);
					if(nextAppt.equals(nextTimeSlot.getAvailableApptTime()) && (apptLength <= apptBlockLen)){
						filteredResults.add(entry);
						i = j;  // Move the loop index to end of the multi slot appt
						break;
					}
				}
			}else{
				filteredResults.add(entry);
			}
		}
		return(filteredResults);
	}
	
	public List<TimeSlot> filterAvailableTimeSlots2(SearchConfig clinic,String mrp,String providerId, Long appointmentTypeId, DayWorkSchedule dayWorkScheduleTransfer, List<TimeSlot> currentlyAllowedTimeSlots, Calendar date,Map<String,String> params){
		int timeSlotLen = dayWorkScheduleTransfer.getTimeSlotDurationMin();
		// allowed time codes
		ArrayList<TimeSlot> filteredResults = new ArrayList<TimeSlot>();
		for(int i =0; i < currentlyAllowedTimeSlots.size();i++){
			TimeSlot entry = currentlyAllowedTimeSlots.get(i);
			Calendar apptStartTime = entry.getAvailableApptTime();
			Character code = entry.getCode();
			int apptLength = clinic.getAppointmentDuration(mrp, providerId, appointmentTypeId, code);
			
			if(apptLength>timeSlotLen){
				if(i+1 <currentlyAllowedTimeSlots.size()){
					TimeSlot nextTimeSlot = currentlyAllowedTimeSlots.get(i+1);
					Calendar nextAppt = (Calendar) apptStartTime.clone();
					nextAppt.add(Calendar.MINUTE, timeSlotLen);
										
					if(nextAppt.equals(nextTimeSlot.getAvailableApptTime())){
						filteredResults.add(entry);
					}
					nextTimeSlot.getCode();
				}				
			}
		}
		return(filteredResults);
	}
}
