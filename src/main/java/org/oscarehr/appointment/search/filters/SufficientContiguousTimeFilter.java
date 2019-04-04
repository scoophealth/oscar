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


public class SufficientContiguousTimeFilter  implements AvailableTimeSlotFilter{

	public List<TimeSlot> filterAvailableTimeSlots(SearchConfig clinic,String mrp,String providerId, Long appointmentTypeId, DayWorkSchedule dayWorkScheduleTransfer, List<TimeSlot> currentlyAllowedTimeSlots, Calendar date,Map<String,String> params){
		// check for enough contiguous time
		ArrayList<TimeSlot> allowedTimesFilteredByContiguousDuration = new ArrayList<TimeSlot>();
		if (dayWorkScheduleTransfer != null){
			long workScheduleTimeSlotDurationMs = dayWorkScheduleTransfer.getTimeSlotDurationMin() * 60 * 1000;
			for (int i = 0; i < currentlyAllowedTimeSlots.size(); i++){
				long requireTimeMillisSeconds  = (clinic.getAppointmentDuration(mrp, providerId, appointmentTypeId, currentlyAllowedTimeSlots.get(i).getCode()) *60 *1000);
				if (hasEnoughContiguousTime(i,currentlyAllowedTimeSlots, workScheduleTimeSlotDurationMs, requireTimeMillisSeconds)){
					allowedTimesFilteredByContiguousDuration.add(currentlyAllowedTimeSlots.get(i));
				}
			}
		}

		return allowedTimesFilteredByContiguousDuration;
	}
	

	
	/**
	 * @param index is the entry in allowedTimesFilteredByExistingAppointments we are checking
	 * @param duration
	 * @param allowedTimesFilteredByExistingAppointments
	 * @return
	 */
	private static boolean hasEnoughContiguousTime(int index, List<TimeSlot> allowedTimesFilteredByExistingAppointments, long timeSlotDurationMs, long requiredTimeMs){
		// algorithm
		//----------
		// slots         |-A-|-B-|-C-|-D-|-E-|
		// desired           |----X----|
		//
		// given a desired time frame X,
		// we must check each subsequent time slot until endTime<=timeSlot.startTime (B,C,D)
		// must check that each subsequent time slot must start when the previous one ended (as there can be gaps in the list from existing appointments or lunch break etc).

		Calendar startTime = allowedTimesFilteredByExistingAppointments.get(index).getAvailableApptTime();	

		// we start processing from that time onwards, no point in looking at earlier time slots
		for (int i = index; i < allowedTimesFilteredByExistingAppointments.size(); i++){
			Calendar timeSlotStart = allowedTimesFilteredByExistingAppointments.get(i).getAvailableApptTime();
			long endRequiredTimeMs = startTime.getTimeInMillis() + requiredTimeMs;
			
			// if it ends in the time slot we're inspecting then we've got enough time
			long endTimeSlotMs = timeSlotStart.getTimeInMillis() + timeSlotDurationMs;
			if (endRequiredTimeMs <= endTimeSlotMs) return(true);

			// check if we've hit the end of the list of slots
			if (i + 1 >= allowedTimesFilteredByExistingAppointments.size()) return(false);

			// if the next time slot doesn't follow from the current ones end time, then there's a gap and it's no good.
			Calendar nextTimeSlotStart = allowedTimesFilteredByExistingAppointments.get(i + 1).getAvailableApptTime();
			long nextTimeSlotStartMs = nextTimeSlotStart.getTimeInMillis();
			if (endTimeSlotMs != nextTimeSlotStartMs) return(false);
		}

		return false;
	}
	
}
