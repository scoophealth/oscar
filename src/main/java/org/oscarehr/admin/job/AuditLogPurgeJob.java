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
package org.oscarehr.admin.job;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.oscarehr.common.jobs.OscarRunnable;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Security;
import org.oscarehr.managers.AuditLogManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;

/*
 *  a) determine which rows to be purged 
 b) backup rows to be purged using mysqldump and saving that output file directly on the server (somewhere under DOCUMENT_DIR) 
 c) log it's own action to the Log 
 c) permanently remove identified rows from log table. 
 
 */
public class AuditLogPurgeJob implements OscarRunnable {

	private Provider provider;
	private Security security;
	Logger logger = MiscUtils.getLogger();

	
	
	@Override
	public void run() {
		AuditLogManager manager = SpringUtils.getBean(AuditLogManager.class);
		
		logger.info("AuditLogPurgeJob running as " + provider.getFormattedName() + " login is " + security.getUserName());

		LoggedInInfo loggedInInfo = new LoggedInInfo();
		loggedInInfo.setLoggedInProvider(provider);
		loggedInInfo.setLoggedInSecurity(security);

		String daysFromNowToRemove =  OscarProperties.getInstance().getProperty("log.purge.daysfromnowtopurge");
		
		
		Integer daysRemove = null;
		if(daysFromNowToRemove != null) {
			try {
				daysRemove = Integer.parseInt(daysFromNowToRemove);
			} catch(NumberFormatException e) {
				logger.warn("Invalid value in log.purge.daysfromnowtopurge");
				return;
			}
		}
		if(daysRemove == null) {
			logger.warn("Aborting. You need to set log.purge.daysfromnowtopurge if you want this job to work");
			return;
		}
		
		Date endDateToPurge = null;
		
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, -daysRemove);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		endDateToPurge = c.getTime();
		
		logger.info("Calling AuditLogManager.purgeAuditLog with date " + endDateToPurge);
		
		try {
			int numRecordsDeleted = manager.purgeAuditLog(loggedInInfo, endDateToPurge);
		}catch(Exception e) {
			logger.error("Error", e);
		}
		
		logger.info("Job complete");

	}

	@Override
	public void setLoggedInProvider(Provider provider) {
		this.provider = provider;
	}

	@Override
	public void setLoggedInSecurity(Security security) {
		this.security = security;
	}

}
