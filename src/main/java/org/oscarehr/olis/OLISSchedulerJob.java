/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */
package org.oscarehr.olis;

import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.SecurityDao;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.olis.dao.OLISProviderPreferencesDao;
import org.oscarehr.olis.dao.OLISSystemPreferencesDao;
import org.oscarehr.olis.model.OLISProviderPreferences;
import org.oscarehr.olis.model.OLISSystemPreferences;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.LoggedInInfo;
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
		OLISSystemPreferencesDao olisPrefDao = (OLISSystemPreferencesDao) SpringUtils.getBean("OLISSystemPreferencesDao");
		OLISProviderPreferencesDao olisProviderPreferencesDao = (OLISProviderPreferencesDao) SpringUtils.getBean("OLISProviderPreferencesDao");
		UserPropertyDAO userPropertyDAO = SpringUtils.getBean(UserPropertyDAO.class);
		
		try {
			logger.info("starting OLIS poller job");
			OLISSystemPreferences olisPrefs = olisPrefDao.getPreferences();
			if (olisPrefs == null) {
				// not set to run at all
				logger.info("OLISPoller - Cannot run. No entry in OLISSystemPreferences table");
				return;
			}
			Date now = new Date();
			
/*
 * SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMddHHmmssZ");
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
*/
			olisPrefs.setLastRun(new Date());
			olisPrefDao.merge(olisPrefs);

			LoggedInInfo loggedInInfo = new LoggedInInfo();
			ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
			SecurityDao securityDao = SpringUtils.getBean(SecurityDao.class);
			
			loggedInInfo.setLoggedInProvider(providerDao.getProvider("999998"));
			loggedInInfo.setLoggedInSecurity(securityDao.getByProviderNo("999998"));
			
			
			//get list of all providers to poll Z04 for
			//check if we need to poll based on provider level preference, then clinic level preference.
			
			for(Provider p : providerDao.getActiveProviders()) {
				//only providers setup for OLIS
				String officialLastName  = userPropertyDAO.getStringValue(p.getProviderNo(),UserProperty.OFFICIAL_LAST_NAME);
				if(StringUtils.isEmpty(officialLastName)) {
					continue;
				}

				logger.info("OLIS: Will poll for " + p.getFormattedName());
				
				OLISProviderPreferences olisProviderPreferences = olisProviderPreferencesDao.findById(p.getProviderNo());
				Date lastRunDate = null;
				if(olisProviderPreferences != null && olisProviderPreferences.getLastRun() != null) {
					lastRunDate = olisProviderPreferences.getLastRun();
				}
				
				//determine polling frequency to use for this provider
				Integer pollingFrequency = olisPrefs.getPollFrequency();
				String providerPolling  = userPropertyDAO.getStringValue(p.getProviderNo(),"olis_polling_frequency");
				if(providerPolling != null) {
					pollingFrequency = Integer.parseInt(providerPolling);
				}
				
				logger.info("Last Run: " + lastRunDate + ", polling frequency is " + pollingFrequency + " minutes");
				
				if(lastRunDate == null) {
					OLISPollingUtil.doZ04PollOnBehalfOf(loggedInInfo, p.getProviderNo());
				} else {
					
					//do we need to run?
					Calendar cal = Calendar.getInstance();
					cal.setTime(lastRunDate);
					cal.add(Calendar.MINUTE, pollingFrequency);
					
					if(cal.getTime().before(now)) {
						OLISPollingUtil.doZ04PollOnBehalfOf(loggedInInfo, p.getProviderNo());
					} else {
						logger.warn("skipping. Not time yet");
					}
				}
	
			}
			
		} catch (Exception e) {
			logger.error("error", e);
		} finally {
			DbConnectionFilter.releaseAllThreadDbResources();
		}
	}

}
