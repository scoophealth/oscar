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

package org.oscarehr.match;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

/**
 * @author AnooshTech
 *
 */
public class MatchManagerScheduler {
	private Logger logger = MiscUtils.getLogger();
	private IMatchManager matchManager = new MatchManager();

	private static final int DELAY = 0;
	private static final int PERIOD = 30 * 60 * 1000;

	private ScheduledExecutorService MATCH_MANAGER_SCHEDULER = 
		new ScheduledThreadPoolExecutor(1, new ScheduledMatchManagerThreadFactory());

	public MatchManagerScheduler() {
		MATCH_MANAGER_SCHEDULER.scheduleWithFixedDelay(new MatchMgrSheduledTask(),
				DELAY, PERIOD, TimeUnit.MILLISECONDS);
	}

	private static class ScheduledMatchManagerThreadFactory implements ThreadFactory {
		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r, "SCHEDULED_MATCH_MANAGER_THREAD-1");
		}
	}

	private class MatchMgrSheduledTask implements Runnable {
		@Override
		public void run() {
			try {
				logger.info("Processing schedule MatchManagerTask..." + matchManager.processEvent(null, IMatchManager.Event.SCHEDULED_EVENT));
			} catch (MatchManagerException e) {
				logger.error("Error while processing scheduled MatchManager scheduled task..");
				logger.error("error",e);
			}
		}
	}
}
