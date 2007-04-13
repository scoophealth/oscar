package org.oscarehr.common.model;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Interval;

public class Stay {

	private Interval interval;

	public Stay(Date admission, Date discharge, Date start, Date end) {
		DateTime admissionDateTine = admission.after(start) ? new DateTime(admission) : new DateTime(start);
		DateTime dischargeDateTime = (discharge != null) ? new DateTime(discharge) : new DateTime(end);
		
		interval = new Interval(admissionDateTine, dischargeDateTime);
	}
	
	public Interval getInterval() {
		return interval;
	}

}