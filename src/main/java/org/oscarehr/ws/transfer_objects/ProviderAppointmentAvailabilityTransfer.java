package org.oscarehr.ws.transfer_objects;

import java.util.Calendar;

public class ProviderAppointmentAvailabilityTransfer {
	private String providerNo;
	private Calendar startTime;
	private Calendar endTime;
	private int timeSlotDurationMin;

	/**
	 * We specifically use a TimeBlock object because it contains no information other than time, this will prevent security concerns on data.
	 */
	private TimeBlock[] unavailableTimes;

	public String getProviderNo() {
		return (providerNo);
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

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

	public TimeBlock[] getUnavailableTimes() {
		return (unavailableTimes);
	}

	public void setUnavailableTimes(TimeBlock[] unavailableTimes) {
		this.unavailableTimes = unavailableTimes;
	}

}
