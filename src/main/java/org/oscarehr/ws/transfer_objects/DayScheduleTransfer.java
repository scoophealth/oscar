package org.oscarehr.ws.transfer_objects;

import java.util.Calendar;

import org.oscarehr.managers.DaySchedule;

public final class DayScheduleTransfer {
	private Calendar startTime;
	private Calendar endTime;
	private int timeSlotDurationMin;
	private AppointmentTransfer[] appointments;

	public Calendar getStartTime() {
		return (startTime);
	}

	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}

	public Calendar getEndTime() {
		return (endTime);
	}

	public void setEndTime(Calendar endTime) {
		this.endTime = endTime;
	}

	public int getTimeSlotDurationMin() {
		return (timeSlotDurationMin);
	}

	public void setTimeSlotDurationMin(int timeSlotDurationMin) {
		this.timeSlotDurationMin = timeSlotDurationMin;
	}

	public AppointmentTransfer[] getAppointments() {
		return (appointments);
	}

	public void setAppointments(AppointmentTransfer[] appointments) {
		this.appointments = appointments;
	}

	public static DayScheduleTransfer toTransfer(DaySchedule daySchedule)
	{
		DayScheduleTransfer dayScheduleTransfer=new DayScheduleTransfer();
		
		dayScheduleTransfer.setStartTime(daySchedule.getStartTime());
		dayScheduleTransfer.setEndTime(daySchedule.getEndTime());
		dayScheduleTransfer.setTimeSlotDurationMin(daySchedule.getTimeSlotDurationMin());
		dayScheduleTransfer.setAppointments(AppointmentTransfer.toTransfer(daySchedule.getAppointments()));
		
		return(dayScheduleTransfer);
	}
}