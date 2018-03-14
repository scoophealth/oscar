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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.oscarehr.common.dao.OscarLogDao;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.stereotype.Service;

import oscar.OscarProperties;
import oscar.log.LogAction;

@Service
public class AuditLogManager {

	Logger logger = MiscUtils.getLogger();
	
	String minDays = OscarProperties.getInstance().getProperty("log.purge.minDays", String.valueOf(365 * 10));
	String mysqldump = OscarProperties.getInstance().getProperty("log.purge.mysqldump");
	String outputDirectory =  OscarProperties.getInstance().getProperty("log.purge.outputdir");
	String daysFromNowToRemove =  OscarProperties.getInstance().getProperty("log.purge.daysfromnowtopurge");
	
	String user = OscarProperties.getInstance().getProperty("db_username");
	String password = OscarProperties.getInstance().getProperty("db_password");
	String dbName = OscarProperties.getInstance().getProperty("db_name").substring(0,  OscarProperties.getInstance().getProperty("db_name").indexOf("?"));
	
	
	public int purgeAuditLog(LoggedInInfo loggedInInfo, Date endDateToPurge) throws Exception {
	
		if(outputDirectory == null || outputDirectory.isEmpty()) {
			outputDirectory =  OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
		}
		if (mysqldump == null) {
			logger.warn("No mysqldump command has been defined. Please set log.purge.mysqldump in the properties file");
			throw new Exception("No mysqldump command has been defined. Please set log.purge.mysqldump in the properties file");
		}
		Integer iMinDays = null;
		try {
			iMinDays = Integer.parseInt(minDays);
		} catch (NumberFormatException e) {
			logger.warn("property log.purge.minDays should be set to a number");
			throw new Exception("property log.purge.minDays should be set to a number");
		}
		

		Calendar c = Calendar.getInstance();
		c.setTime(endDateToPurge); 
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		endDateToPurge = c.getTime();
		
		logger.info("Purge will be for all data BEFORE and INCLUDING " + endDateToPurge);

		int numDays = Days.daysBetween(new LocalDate(endDateToPurge.getTime()), new LocalDate(new Date().getTime())).getDays();
		if (numDays < iMinDays) {
			logger.warn("purge aborted because specified date is within " + numDays);
			throw new Exception("purge aborted because specified date is within " + numDays);
		}

		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat formatter3 = new SimpleDateFormat("yyyyMMddHHmmss");

		String filename = outputDirectory + "/OSCAR_AUDIR_LOG_PURGE_FILE_" + formatter3.format(endDateToPurge) + ".sql";
		
		String vars[] = new String[9];
		vars[0] = mysqldump;
		vars[1] = "--user=" + user;
		vars[2] = "--password=" + password;
		vars[3] = "-w";
		vars[4] = "dateTime < '" + formatter2.format(endDateToPurge) + "'";
		vars[5] = "-t";
		vars[6] = "--result-file=" + filename;
		vars[7] = dbName;
		vars[8] = "log";

		Integer exitValue = null;

		
		try {
			String s = null;
			
			Process p = Runtime.getRuntime().exec(vars);

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

			logger.info("Here is the standard output of the command:\n");
			while ((s = stdInput.readLine()) != null) {
				logger.info(s);
			}

			logger.info("Here is the standard error of the command (if any):\n");
			while ((s = stdError.readLine()) != null) {
				logger.info(s);
			}

			exitValue = p.waitFor();
		} catch (IOException e) {
			logger.error("Error running mysqldump command. Aborting!", e);
			throw new Exception(e);
		} catch (InterruptedException e) {
			logger.error("Error running mysqldump command. Aborting!", e);
			throw new Exception(e);
		}

		if (exitValue != 0) {
			logger.warn("Error running mysqldump command. Received an exit value of " + exitValue);
			throw new Exception("Error running mysqldump command. Received an exit value of " + exitValue);
		}

		logger.info("Backed up audit log which will be purged to " + filename);

		LogAction.addLogSynchronous(loggedInInfo, "AuditLogManager.purgeAuditLog", formatter2.format(endDateToPurge));

		OscarLogDao oscarLogDao = SpringUtils.getBean(OscarLogDao.class);
		int numRecordAffected = oscarLogDao.purgeLogEntries(endDateToPurge);

		logger.info("removed  " + numRecordAffected + " records");

		return numRecordAffected;
	}
}
