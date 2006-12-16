package org.oscarehr.PMmodule.service;

import org.oscarehr.PMmodule.model.BedCheckTime;

public interface BedCheckTimeManager {

	/**
	 * @param programId
	 * @return
	 */
	public BedCheckTime[] getBedCheckTimesByProgram(Integer programId);

	/**
	 * @param bedCheckTime
	 */
	public void addBedCheckTime(BedCheckTime bedCheckTime);

	/**
	 * @param bedCheckTimeId
	 */
	public void removeBedCheckTime(Integer bedCheckTimeId);
	
}
