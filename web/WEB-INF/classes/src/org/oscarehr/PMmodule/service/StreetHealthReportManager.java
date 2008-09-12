package org.oscarehr.PMmodule.service;

import java.util.Date;
import java.util.List;

public interface StreetHealthReportManager {

	@SuppressWarnings("unchecked")
	public List getCohort(Date BeginDate, Date EndDate, int facilityId);
	
}
