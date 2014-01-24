/**
 * Copyright (C) 2011-2012  PeaceWorks Technology Solutions
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


package org.oscarehr.oscarRx.erx;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.PropertyDao;
import org.oscarehr.common.model.Property;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;

/**
 * Task scheduler that calls the synchronisation process with the External Prescriber.
 */
public class ERxScheduler extends TimerTask {

	private static final String ERX_LAST_RUN = "ERxLastRun";
	private static final int ONE_DAY_MILLISEC = 86400000;
	private static final Logger logger = MiscUtils.getLogger();
	
	private static Long lastRun = null;
	private static boolean firstRun = true;

	@Override
	public void run() {
		List<Property> eRxLastRunList = ((PropertyDao) SpringUtils.getBean("propertyDao")).findByName(ERX_LAST_RUN);
		Property eRxLastRun = eRxLastRunList.isEmpty() ? null : eRxLastRunList.get(0);
		
		try {
				//Check if ERx is enabled
				boolean eRxEnabled = Boolean.valueOf(OscarProperties.getInstance().getProperty("util.erx.enabled"));
				//not set to run at all
				if (!eRxEnabled) return;
							
				logger.info("External Prescriber: Starting Synchronizer Task");
				
				int synchronizeInterval = ONE_DAY_MILLISEC;
//				int dayBack1stImport = 7;
				
				try {
					synchronizeInterval = Integer.parseInt(OscarProperties.getInstance().getProperty("util.erx.synchronize_interval"));
//					dayBack1stImport = Integer.parseInt(OscarProperties.getInstance().getProperty("util.erx.1st_import_day_back"));
				} catch (NumberFormatException e) {
					//do nothing
				}
				
				Date now = new Date();
				
			    if (!firstRun) {
			    	if (eRxLastRun != null) {
			    		try {
			    			lastRun = Long.parseLong(eRxLastRun.getValue());
			    		} catch (NumberFormatException nfex) {
			    			logger.error("Parse String to Long Error", nfex);
			    		}
			    	}
			    	if (lastRun == null) lastRun = new Date().getTime();
			    	
			    	if (lastRun + synchronizeInterval <= now.getTime()) {
			    		doPrescriptionSync();
				    }
				} else {
					logger.info("First Run after startup");
					
					if (lastRun == null) lastRun = (new Date().getTime()) - (ONE_DAY_MILLISEC * 7); //set back 1 week
//					if (lastRun == null) lastRun = (new Date().getTime()) - (ONE_DAY_MILLISEC * dayBack1stImport);
					doPrescriptionSync();
					ERxScheduler.firstRun = false;
					
					lastRun = now.getTime();
				}
				logger.debug("===== External Prescriber Synchronizer JOB RUNNING....");
				
			} catch (Exception e) {
				logger.error("External Prescriber: Synchronization Task ERROR", e);
			} finally {
				DbConnectionFilter.releaseAllThreadDbResources();
				saveLastRunToProperty(eRxLastRun);
			}
	}
	
	
	
	private void doPrescriptionSync() {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
		
		long daysBetween = (now.getTime() - lastRun) / ONE_DAY_MILLISEC;
		int daysBetweenInt = (int)daysBetween; //Don't need to check if daysBetween exceeds Integer.MAX_VALUE
		logger.debug("days between " + daysBetweenInt);
		
		//synchronize prescription for each day since last run that are are older than the day before 
		while(checkDaysDiffMoreThanOne(daysBetweenInt, now)) {
			Date syncDate = new Date(lastRun); //lastRun is at least yesterday
			logger.info("External Prescriber-: Synchronization started at :" + sdf.format(new Date()));				
			ERxScheduledSynchronizer.syncPrescriptions(syncDate);
			daysBetweenInt--;
			lastRun=syncDate.getTime() + ONE_DAY_MILLISEC; //add 24hrs to last run
			logger.info("External Prescriber-: Importing Rx dated on: " + ymd.format(syncDate));
		}
	}
	
	private boolean checkDaysDiffMoreThanOne(int daysBetweenInt, Date now) {
		if (daysBetweenInt > 1) return true;
		if (daysBetweenInt < 0) return false;
		
		//now & lastRun less than 24hrs but can still be different days
		SimpleDateFormat dayOnly = new SimpleDateFormat("d");
		String nowDay = dayOnly.format(now);
		String lastRunDay = dayOnly.format(new Date(lastRun));
		
		return !nowDay.equals(lastRunDay);
	}

	private void saveLastRunToProperty(Property eRxLastRun) {
		if (eRxLastRun == null) {
			eRxLastRun = new Property();
			eRxLastRun.setName(ERX_LAST_RUN);
		}
		eRxLastRun.setValue(String.valueOf(lastRun));
		((PropertyDao) SpringUtils.getBean("propertyDao")).merge(eRxLastRun);
	}
}
