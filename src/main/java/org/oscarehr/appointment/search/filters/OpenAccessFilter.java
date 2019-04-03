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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.oscarehr.appointment.search.SearchConfig;
import org.oscarehr.appointment.search.TimeSlot;
import org.oscarehr.managers.DayWorkSchedule;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.ws.transfer_objects.CalendarScheduleCodePairTransfer;


public class OpenAccessFilter implements AvailableTimeSlotFilter{
	private static Logger logger = MiscUtils.getLogger();
	
	@Override
	public List<TimeSlot> filterAvailableTimeSlots(SearchConfig clinic,String mrp,String providerId, Long appointmentTypeId, DayWorkSchedule dayWorkScheduleTransfer, List<TimeSlot> currentlyAllowedTimeSlots, Calendar date,Map<String,String> params){
		ArrayList<TimeSlot> filteredResults = new ArrayList<TimeSlot>();
		for (TimeSlot entry : currentlyAllowedTimeSlots){
			if (isAllowedTime(dayWorkScheduleTransfer, entry.getAvailableApptTime(),params)){
				filteredResults.add(entry);
			}
		}
		return(filteredResults);
	}

	private boolean isAllowedTime(DayWorkSchedule dayWorkScheduleTransfer, Calendar timeSlot,Map<String,String> params){

		long currentTimeMillis = getCurrentTimeMillis(params);
		char[] openAccessCodes = {'o'};
		if(params != null && params.get("codes") != null){
			openAccessCodes = params.get("codes").toCharArray();
			Arrays.sort(openAccessCodes);
		}

		for (CalendarScheduleCodePairTransfer entry : CalendarScheduleCodePairTransfer.toTransfer(dayWorkScheduleTransfer.getTimeSlots())){
			// find the time slot type for this entry
			if (entry.getDate().equals(timeSlot)){
				char c = entry.getScheduleCode();
				if (Arrays.binarySearch(openAccessCodes,c) >= 0){
					return(entry.getDate().getTimeInMillis() < (currentTimeMillis + DateUtils.MILLIS_PER_DAY));
				}
			}
		}

		return(true);
	}
	
	//Used for testing to be able to adjust when "now" is
	public long getCurrentTimeMillis(Map<String,String> params){
		String nowDate = null;
		if(params != null && params.get("nowDate") != null){
			try{
				nowDate = params.get("nowDate");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				return sdf.parse(nowDate).getTime();
			}catch(Exception e){
				logger.error("Now Date "+nowDate+" is not parsing correctly. Needs to be in format yyyy-MM-dd HH:mm:ss ",e);
			}
		}
		return System.currentTimeMillis();
	}
}