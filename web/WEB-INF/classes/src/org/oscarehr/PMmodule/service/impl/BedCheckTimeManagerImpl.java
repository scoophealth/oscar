package org.oscarehr.PMmodule.service.impl;

import org.oscarehr.PMmodule.dao.BedCheckTimeDAO;
import org.oscarehr.PMmodule.model.BedCheckTime;
import org.oscarehr.PMmodule.service.BedCheckTimeManager;

public class BedCheckTimeManagerImpl implements BedCheckTimeManager {
	
	private BedCheckTimeDAO bedCheckTimeDAO;
	
	public void setBedCheckTimeDAO(BedCheckTimeDAO bedCheckTimeDAO) {
	    this.bedCheckTimeDAO = bedCheckTimeDAO;
    }

	/* (non-Javadoc)
	 * @see org.oscarehr.PMmodule.service.BedCheckTimeManager#addBedCheckTime(org.oscarehr.PMmodule.model.BedCheckTime)
	 */
	public void addBedCheckTime(BedCheckTime bedCheckTime) {
		if (!bedCheckTimeDAO.bedCheckTimeExists(bedCheckTime.getProgramId(), bedCheckTime.getTime())) {
			bedCheckTimeDAO.saveBedCheckTime(bedCheckTime);
		}
	}

	/* (non-Javadoc)
	 * @see org.oscarehr.PMmodule.service.BedCheckTimeManager#getBedCheckTimesByProgram(java.lang.Integer)
	 */
	public BedCheckTime[] getBedCheckTimesByProgram(Integer programId) {
		return bedCheckTimeDAO.getBedCheckTimes(programId);
	}

	/* (non-Javadoc)
	 * @see org.oscarehr.PMmodule.service.BedCheckTimeManager#removeBedCheckTime(java.lang.Integer)
	 */
	public void removeBedCheckTime(Integer bedCheckTimeId) {
		BedCheckTime bedCheckTime = bedCheckTimeDAO.getBedCheckTime(bedCheckTimeId);
		bedCheckTimeDAO.deleteBedCheckTime(bedCheckTime);
	}
	
}