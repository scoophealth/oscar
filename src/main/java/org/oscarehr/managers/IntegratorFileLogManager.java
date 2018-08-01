/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.managers;

import java.util.Date;
import java.util.List;

import org.oscarehr.common.dao.IntegratorFileLogDao;
import org.oscarehr.common.model.IntegratorFileLog;
import org.oscarehr.util.LoggedInInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IntegratorFileLogManager {

	@Autowired
	IntegratorFileLogDao integratorFileLogDao;
	
	public void saveNewFileData(String filename, String checksum, Date lastDateUpdated, Date currentDate) {
		/*
		if(!securityInfoManager.hasPrivilege(loggedInInfo, "_con", privilege, null)) {
			throw new RuntimeException("Access Denied");
		}*/
		
		IntegratorFileLog item = new IntegratorFileLog();
		item.setFilename(filename);
		item.setChecksum(checksum);
		item.setLastDateUpdated(lastDateUpdated);
		item.setCurrentDate(currentDate);
		item.setDateCreated(new Date());
		
		integratorFileLogDao.persist(item);
	}
	
	public IntegratorFileLog getLastFileData() {
		
		return integratorFileLogDao.getLastFileData();
	}
	
	
	public List<IntegratorFileLog> getFileLogHistory(LoggedInInfo loggedInInfo) {
		return integratorFileLogDao.getFileLogHistory();
	}
	
	/**
	 * Used to compare with the Integrator logs.
	 * Gets all log entries that are coded with a final status.  
	 */
	@SuppressWarnings("unused")
	public List<IntegratorFileLog> getStatusNotCompleteOrError(LoggedInInfo loggedInInfo) {
		return integratorFileLogDao.findAllWithNoCompletedOrErrorIntegratorStatus();
	}
	
	public void updateIntegratorFileLog(LoggedInInfo loggedInInfo, IntegratorFileLog integratorFileLog) {
		integratorFileLogDao.merge(integratorFileLog);
	}
}
