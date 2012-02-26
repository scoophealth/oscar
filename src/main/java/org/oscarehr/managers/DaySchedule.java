package org.oscarehr.managers;

import java.util.Calendar;
import java.util.List;

import org.oscarehr.common.model.Appointment;

public final class DaySchedule {
	private Calendar startTime;
	private Calendar endTime;
	private int timeSlotDurationMin;
	private List<Appointment> appointments;

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

	public List<Appointment> getAppointments() {
		return (appointments);
	}

	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}
}
