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
package org.oscarehr.integration.excelleris.com.colcamex.www.core;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.apache.log4j.Logger;
import static java.util.concurrent.TimeUnit.*;

/**
 * @author Dennis Warren Colcamex Resources
 * 
 * Major Contributors: 
 *  OSCARprn
 *  NERD
 *   
 * This community edition of Expedius is for use at your own risk, without warranty, and 
 * support. 
 * 
 */
public class PollTimer {
	
	// available services 
	
	public static int DEFAULT_POLL_INTERVAL = 7200;
	
	// polling frequencies
	private static final Integer INITIAL_DELAY = (60*30); // download after 10 minutes of start.

	private static Logger logger = Logger.getLogger("PollTimer");

	private static int pollFrequency;
	private static boolean hostStatus;

    private static ScheduledExecutorService scheduler;
    
	public static synchronized ScheduledExecutorService getScheduler() {
		return scheduler;
	}

	public static void setScheduler(ScheduledExecutorService scheduler) {
		PollTimer.scheduler = scheduler;
	}

	public static boolean isHostStatus() {
		return hostStatus;
	}

	public static void setHostStatus(boolean hostStatus) {
		PollTimer.hostStatus = hostStatus;
	}


	/**
	 * Returns the current run status of this scheduler.
	 * @return true if running.
	 */
	public static synchronized boolean isRunning() {

		if(scheduler != null) {
			return (! scheduler.isShutdown());
		}  
	
		return false;
	}

	public static synchronized int getPollFrequency() {
		return PollTimer.pollFrequency;
	}

	public static void setPollFrequency(int pollFrequency) {
		PollTimer.pollFrequency = pollFrequency;
	}

	/**
	 * Start a service
	 * 
	 * @param method
	 * @param interval
	 */
	public static void start(ServiceExecuter services, Integer interval) {		
		
		logger.debug("Poll interval(s): " + interval);

		if(interval != null && interval > DEFAULT_POLL_INTERVAL) {
			DEFAULT_POLL_INTERVAL = interval;
		}
		
		if(scheduler != null) {
			scheduler.shutdownNow();
		}

		scheduler = Executors.newScheduledThreadPool(2);

        if(interval > 0) {       	        	
			scheduler.scheduleWithFixedDelay(
					services, // action to trigger. 
					INITIAL_DELAY, //INITIAL_DELAY,
					INITIAL_DELAY, // trigger delay period
					SECONDS
			);
			
        }
		

		if(PollTimer.isRunning()) {
			Date startTime = new Date(System.currentTimeMillis());
			logger.info("Poll timer successfully started on "+startTime);
		}
	
	}

	/**
	 * If the scheduler is running this will shut it down.
	 */
	public static List<Runnable> stop() {
		
		logger.info("Poll timer stopped by user on " + new Date());
		return scheduler.shutdownNow();						
	}


	
	public static Calendar getTimestamp(Date time) {
		Calendar now = getTimestamp();
		now.setTime(time);
		return now;
	}
	
	public static Calendar getTimestamp() {
		Calendar now = Calendar.getInstance();
		now.setTimeInMillis(System.currentTimeMillis());
		return now;
	}
	
	
}
