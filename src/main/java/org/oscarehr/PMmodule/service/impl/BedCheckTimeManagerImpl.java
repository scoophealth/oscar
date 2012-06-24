/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.service.impl;

import org.oscarehr.PMmodule.dao.BedCheckTimeDAO;
import org.oscarehr.PMmodule.model.BedCheckTime;
import org.oscarehr.PMmodule.service.BedCheckTimeManager;
import org.springframework.transaction.annotation.Transactional;

@Transactional
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
