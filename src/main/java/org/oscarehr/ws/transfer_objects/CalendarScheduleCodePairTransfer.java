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