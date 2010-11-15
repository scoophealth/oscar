package org.oscarehr.common.model;

import java.util.Date;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.oscarehr.util.MiscUtils;

public class Stay {
	
	private static final Logger logger = MiscUtils.getLogger();

	private Interval interval;

	public Stay(Date admission, Date discharge, Date start, Date end) {
		DateTime admissionDateTime = admission.after(start) ? new DateTime(admission) : new DateTime(start);
		DateTime dischargeDateTime = (discharge != null) ? new DateTime(discharge) : new DateTime(end);
		
		try {
			interval = new Interval(admissionDateTime, dischargeDateTime);
		} catch (IllegalArgumentException e) {
			logger.error("admission: " + admission + " discharge: " + discharge, e);
			logger.error("admission datetime: " + admissionDateTime + " discharge datetime: " + dischargeDateTime);
			
			throw e;
		}
	}
	
	public Interval getInterval() {
		return interval;
	}

}