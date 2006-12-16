package org.oscarehr.PMmodule.dao;

import java.util.Date;

import org.oscarehr.PMmodule.model.BedCheckTime;

public interface BedCheckTimeDAO {

	/**
	 * @param programId
	 * @param date
	 * @return
	 */
	public boolean bedCheckTimeExists(Integer programId, Date time);

	/**
	 * @param id
	 * @return
	 */
	public BedCheckTime getBedCheckTime(Integer id);

	/**
	 * @param programId
	 * @return
	 */
	public BedCheckTime[] getBedCheckTimes(Integer programId);

	/**
	 * @param bedCheckTime
	 */
	public void saveBedCheckTime(BedCheckTime bedCheckTime);

	/**
	 * @param bedCheckTime
	 */
	public void deleteBedCheckTime(BedCheckTime bedCheckTime);

}
