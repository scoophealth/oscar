package org.oscarehr.ws.transfer_objects;

import org.oscarehr.managers.DayWorkSchedule;

public final class DayWorkScheduleTransfer {
	private boolean isHoliday;
	private Integer timeSlotDurationMin;
	private KeyValueEntryTransfer[] timeSlots;

	public boolean isHoliday() {
		return (isHoliday);
	}

	public void setHoliday(boolean isHoliday) {
		this.isHoliday = isHoliday;
	}

	public Integer getTimeSlotDurationMin() {
		return (timeSlotDurationMin);
	}

	public void setTimeSlotDurationMin(Integer timeSlotDurationMin) {
		this.timeSlotDurationMin = timeSlotDurationMin;
	}

	public KeyValueEntryTransfer[] getTimeSlots() {
		return (timeSlots);
	}

	public void setTimeSlots(KeyValueEntryTransfer[] timeSlots) {
		this.timeSlots = timeSlots;
	}

	public static DayWorkScheduleTransfer toTransfer(DayWorkSchedule dayWorkSchedule)
	{
		DayWorkScheduleTransfer dayWorkScheduleTransfer=new DayWorkScheduleTransfer();
		
		dayWorkScheduleTransfer.setHoliday(dayWorkSchedule.isHoliday());
		dayWorkScheduleTransfer.setTimeSlotDurationMin(dayWorkSchedule.getTimeSlotDurationMin());
		dayWorkScheduleTransfer.setTimeSlots(KeyValueEntryTransfer.toTransfer(dayWorkSchedule.getTimeSlots()));
		
		return(dayWorkScheduleTransfer);
	}
}