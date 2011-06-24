package org.oscarehr.olis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.oscarehr.olis.dao.OLISSystemPreferencesDao;
import org.oscarehr.olis.model.OLISSystemPreferences;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.scheduling.timer.ScheduledTimerTask;

/**
 * A job can start many tasks
 * 
 * @author Indivica
 * 
 */
public class OLISSchedulerJob extends TimerTask {
	
	private static final Logger logger = MiscUtils.getLogger();

	@Override
	public void run() {
		logger.info("starting OLIS poller job");
		//lets sync the db to the running spring bean.
		ScheduledTimerTask task = (ScheduledTimerTask)SpringUtils.getBean("olisScheduledPullTask");		
		OLISSystemPreferencesDao olisPrefDao = (OLISSystemPreferencesDao)SpringUtils.getBean("OLISSystemPreferencesDao");
        OLISSystemPreferences olisPrefs =  olisPrefDao.getPreferences();
        if(olisPrefs == null) {
        	//not set to run at all
        	logger.info("Don't need to run right now..no prefs");
        	return;
        }
        Date now = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMddHHmmssZ");
        Date startDate = null, endDate = null;
        try {
        	startDate = dateFormatter.parse(olisPrefs.getStartTime());
        	endDate = dateFormatter.parse(olisPrefs.getEndTime());
        }catch(ParseException e) {
        	logger.error("Error",e);
        	return;
        }
        logger.info("start date = "+ startDate);
        logger.info("end date = "+ endDate);
        
        if(now.before(startDate) || now.after(endDate)) {
        	logger.info("Don't need to run right now");
        	return;
        }
        
        if(olisPrefs.getLastRun() != null) {
	        //check to see if we are past last run + frequency interval
	        int freqMins = olisPrefs.getPollFrequency();
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(olisPrefs.getLastRun());
	        cal.add(Calendar.MINUTE, freqMins);
	        
	        if(cal.getTime().getTime() > now.getTime()) {
	        	logger.info("not yet time to run - last run @ " + olisPrefs.getLastRun() + " and freq is " + freqMins + " mins.");
	        	return;
	        }
        }
	       
		logger.info("===== OLIS JOB RUNNING....");
		olisPrefs.setLastRun(new Date());
		olisPrefDao.merge(olisPrefs);
		
		OLISPoller.startAutoFetch();
     
	}

}