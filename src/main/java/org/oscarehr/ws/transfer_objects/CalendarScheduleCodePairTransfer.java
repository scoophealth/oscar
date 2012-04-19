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


package org.oscarehr.ws.transfer_objects;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Map.Entry;

public final class CalendarScheduleCodePairTransfer {
	private Calendar date;
	private Character scheduleCode;


	public Calendar getDate() {
    	return (date);
    }

	public void setDate(Calendar date) {
    	this.date = date;
    }

	public Character getScheduleCode() {
    	return (scheduleCode);
    }

	public void setScheduleCode(Character scheduleCode) {
    	this.scheduleCode = scheduleCode;
    }

	public static CalendarScheduleCodePairTransfer toTransfer(Map.Entry<? extends Calendar, Character> entry) {
		if (entry==null) return(null);
		
		CalendarScheduleCodePairTransfer result = new CalendarScheduleCodePairTransfer();

		result.setDate(entry.getKey());
		result.setScheduleCode(entry.getValue());

		return (result);
	}

	public static CalendarScheduleCodePairTransfer[] toTransfer(Map<? extends Calendar, Character> map) {
		ArrayList<CalendarScheduleCodePairTransfer> result = new ArrayList<CalendarScheduleCodePairTransfer>();		
		for (Entry<? extends Calendar, Character> entry : map.entrySet())
		{
			result.add(toTransfer(entry));
		}
		
		return(result.toArray(new CalendarScheduleCodePairTransfer[0]));
	}
}
