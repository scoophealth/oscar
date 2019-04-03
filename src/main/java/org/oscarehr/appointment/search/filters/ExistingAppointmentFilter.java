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

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.appointment.search.SearchConfig;
import org.oscarehr.appointment.search.TimeSlot;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.managers.DayWorkSchedule;
//import org.oscarehr.oscar.ws.manager.ScheduleManager;
//import org.oscarehr.oscar.ws.utils.OscarServerWebServicesManager;
import org.oscarehr.managers.ScheduleManager;

//import org.oscarehr.ws.AppointmentTransfer;

//import org.oscarehr.ws.ScheduleWs;

public class ExistingAppointmentFilter implements AvailableTimeSlotFilter{
	
	private static Logger logger = MiscUtils.getLogger();
	ScheduleManager scheduleManager = SpringUtils.getBean(ScheduleManager.class);
	
	@Override
	public List<TimeSlot> filterAvailableTimeSlots(SearchConfig clinic,String mrp,String providerId, Long appointmentTypeId, DayWorkSchedule dayWorkScheduleTransfer, List<TimeSlot> currentlyAllowedTimeSlots, Calendar date,Map<String,String> params){
		ArrayList<TimeSlot> allowedTimesFilteredByExistingAppointments = new ArrayList<TimeSlot>();
		try{
			List<Appointment> existingAppointments = scheduleManager.getDayAppointments(null,providerId, date);
			for (TimeSlot startTime : currentlyAllowedTimeSlots){
				if (!isThisTakenByExistingAppointment(startTime, existingAppointments)){
					allowedTimesFilteredByExistingAppointments.add(startTime);
				}
			}
		}catch (Exception e){
			logger.error("Error getting existing appointments provider:"+providerId+ " could be oscar 12 will check it",e);
			
		}
		
		if(allowedTimesFilteredByExistingAppointments.size() == 0){
			//BookingLearningManager.recommendDayToBeSkipped(clinic, providerId, date, appointmentTypeId, this.getClass().getName());
		}
		
		return allowedTimesFilteredByExistingAppointments;
	}

	private static boolean isThisTakenByExistingAppointment(TimeSlot timeSlotStartTime, List<Appointment> existingAppointments){
		long timeSlotStartTimeMs = timeSlotStartTime.getAvailableApptTime().getTimeInMillis();

		for (Appointment appointment : existingAppointments){
			if (appointment.getStartTimeAsFullDate() == null || appointment.getEndTime() == null){
				return(true);
			}

			long appointmentStart = appointment.getStartTimeAsFullDate().getTime();
			long appointmentEnd = appointment.getEndTimeAsFullDate().getTime();
			boolean collide = false;
			if (timeSlotStartTimeMs >= appointmentStart && timeSlotStartTimeMs <= appointmentEnd) collide = true;
			if (collide) return(true);
		}
		return(false);
	}
}
