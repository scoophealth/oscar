package org.oscarehr.ws.transfer_objects;

import java.util.Calendar;

public final class TimeBlock {
	private Calendar startTime;
	private Calendar endTime;

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

	@Override
	public String toString() {
		return ("startTime=" + (startTime == null ? null : startTime.getTime()) + ", endTime=" + (endTime == null ? null : endTime.getTime()));
	}
}
