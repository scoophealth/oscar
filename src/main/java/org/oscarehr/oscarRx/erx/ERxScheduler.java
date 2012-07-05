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
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;

/**
 * Task scheduler that calls the synchronisation process with the External Prescriber.
 */
public class ERxScheduler extends TimerTask {

	private static final Logger logger = MiscUtils.getLogger();
	
	private static long lastRun = new Date().getTime();
	private static boolean firstRun = true;

	@Override
	public void run() {
		try {
				//Check if ERx is enabled
				boolean eRxEnabled = Boolean.valueOf(OscarProperties.getInstance().getProperty("util.erx.enabled"));
				//not set to run at all
				if (!eRxEnabled) return;
							
				logger.info("External Prescriber: Starting Synchronizer Task");
				
				int synchronizeInterval = 86400000;
				
				try
				{
					synchronizeInterval = Integer.parseInt(OscarProperties.getInstance().getProperty("util.erx.synchronize_interval"));
				} catch (Exception e) {
					synchronizeInterval = 86400000;
				}
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
				
				Date now = new Date();
				
			    if (!firstRun) {
			    	if (lastRun + synchronizeInterval <= now.getTime())
				    {
			    		long daysBetween = (now.getTime() - lastRun) / 86400000;
			    		int daysBetweenInt = (int)daysBetween; //Don't need to check if daysBetween exceeds Integer.MAX_VALUE
			    		logger.debug("days between "+daysBetweenInt);
			    		//synchronize prescription for each day since last run that are are older than the day before 
			    		while(daysBetweenInt > 1)
			    		{
			    			Date syncDate = new Date(lastRun + 86400000); //add 24hrs to last run
			    			logger.info("External Prescriber-: Synchronization started at :" + sdf.format(new Date()));				
							ERxScheduledSynchronizer.syncPrescriptions(syncDate);
							daysBetweenInt--;
							lastRun=syncDate.getTime();
							logger.info("External Prescriber-: Importing Rx dated on: " + ymd.format(syncDate));
			    		}
			    		
			    		logger.info("External Prescriber: Synchronization started at :" + sdf.format(now));
			    		logger.info("External Prescriber: Importing Rx dated on: " + ymd.format(now));
						ERxScheduledSynchronizer.syncPrescriptions(now);				    	
				    }
				    
				} else {
					logger.info("External Prescriber*: Synchronization started at :" + sdf.format(now));
		    		logger.info("External Prescriber*: Importing Rx dated on: " + ymd.format(now));
					ERxScheduledSynchronizer.syncPrescriptions(now);
					ERxScheduler.firstRun = false;
				}

				lastRun = new Date().getTime();	    			    
				logger.debug("===== External Prescriber Synchronizer JOB RUNNING....");
				
			} catch (Exception e) {
				logger.error("External Prescriber: Synchronization Task ERROR", e);
			} finally {
				DbConnectionFilter.releaseAllThreadDbResources();
			}
	}

}
