/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
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

package org.oscarehr.olis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.oscarehr.olis.dao.OLISSystemPreferencesDao;
import org.oscarehr.olis.model.OLISSystemPreferences;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

/**
 * A job can start many tasks
 * 
 * @author Indivica
 */
public class OLISSchedulerJob extends TimerTask {

	private static final Logger logger = MiscUtils.getLogger();

	@Override
	public void run() {
		try {
			logger.info("starting OLIS poller job");
			OLISSystemPreferencesDao olisPrefDao = (OLISSystemPreferencesDao) SpringUtils.getBean("OLISSystemPreferencesDao");
			OLISSystemPreferences olisPrefs = olisPrefDao.getPreferences();
			if (olisPrefs == null) {
				// not set to run at all
				logger.info("Don't need to run right now..no prefs");
				return;
			}
			Date now = new Date();
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMddHHmmssZ");
			Date startDate = null, endDate = null;
			try {
				if (olisPrefs.getStartTime() != null && olisPrefs.getStartTime().trim().length() > 0) startDate = dateFormatter.parse(olisPrefs.getStartTime());

				if (olisPrefs.getEndTime() != null && olisPrefs.getEndTime().trim().length() > 0) endDate = dateFormatter.parse(olisPrefs.getEndTime());
			} catch (ParseException e) {
				logger.error("Error", e);
			}
			logger.info("start date = " + startDate);
			logger.info("end date = " + endDate);

			if ((startDate != null && now.before(startDate)) || (endDate != null && now.after(endDate))) {
				logger.info("Don't need to run right now");
				return;
			}

			if (olisPrefs.getLastRun() != null) {
				// check to see if we are past last run + frequency interval
				int freqMins = olisPrefs.getPollFrequency();
				Calendar cal = Calendar.getInstance();
				cal.setTime(olisPrefs.getLastRun());
				cal.add(Calendar.MINUTE, freqMins);

				if (cal.getTime().getTime() > now.getTime()) {
					logger.info("not yet time to run - last run @ " + olisPrefs.getLastRun() + " and freq is " + freqMins + " mins.");
					return;
				}
			}

			logger.info("===== OLIS JOB RUNNING....");
			olisPrefs.setLastRun(new Date());
			olisPrefDao.merge(olisPrefs);

			OLISPoller.startAutoFetch();
		} catch (Exception e) {
			logger.error("error", e);
		} finally {
			DbConnectionFilter.releaseAllThreadDbResources();
		}
	}

}