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


package org.oscarehr.managers;

import java.util.Calendar;
import java.util.Map.Entry;
import java.util.TreeMap;

public final class DayWorkSchedule {
	private boolean isHoliday;

	/**
	 * is null if there's no schedule for the given day. 
	 */
	private Integer timeSlotDurationMin;
	
	/**
	 * This treemap holds and orders the start of the time slot and the time code for that slot, i.e. scheduleTemplateCode
	 */
	private TreeMap<Calendar,Character> timeSlots=new TreeMap<Calendar,Character>();

	public Integer getTimeSlotDurationMin() {
    	return (timeSlotDurationMin);
    }

	public void setTimeSlotDurationMin(Integer timeSlotDurationMin) {
    	this.timeSlotDurationMin = timeSlotDurationMin;
    }

	public boolean isHoliday() {
    	return (isHoliday);
    }

	public void setHoliday(boolean isHoliday) {
    	this.isHoliday = isHoliday;
    }

	public TreeMap<Calendar, Character> getTimeSlots() {
    	return (timeSlots);
    }

	public void setTimeSlots(TreeMap<Calendar, Character> timeSlots) {
    	this.timeSlots = timeSlots;
    }
	
	@Override
	public String toString()
	{
		StringBuilder sb=new StringBuilder();
		
		sb.append("isHoliday=");
		sb.append(isHoliday);
		sb.append(", timeSlotDurationMin=");
		sb.append(timeSlotDurationMin);
		sb.append(", timeBlocks=(");
		for (Entry<Calendar, Character> entry : timeSlots.entrySet())
		{
			sb.append('[');
			sb.append(entry.getKey().getTime());
			sb.append('=');
			sb.append(entry.getValue());
			sb.append(']');
		}
		sb.append(")");
		
		return(sb.toString());
	}
}
