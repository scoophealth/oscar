package org.oscarehr.threads;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.managers.WaitListManager;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;

public class WaitListEmailThread extends TimerTask
{
	private static final Logger logger = MiscUtils.getLogger();

	private static Timer timer = new Timer("WaitListEmailThread Timer", true);

	private static TimerTask timerTask = null;

	public static synchronized void startTaskIfEnabled() {
		boolean enableEmailNotifications=Boolean.parseBoolean(OscarProperties.getInstance().getProperty("enable_wait_list_email_notifications"));
		long waitListNotificationPeriod=Long.parseLong(OscarProperties.getInstance().getProperty("wait_list_email_notification_period"));

		logger.info("WaitListEmailThread enabled=" + enableEmailNotifications+", period="+waitListNotificationPeriod);

		if (!enableEmailNotifications) return;
		
		if (timerTask == null) {
			timerTask = new WaitListEmailThread();
			// run 4 times per period so if it's behind it can slowly catch up
			timer.schedule(timerTask, 10000, waitListNotificationPeriod/4);
		} else {
			logger.error("Start was called twice on this timer task object.", new Exception());
		}
	}

	public static synchronized void stopTask() {
		if (timerTask != null) {
			timerTask.cancel();
			timerTask = null;

			logger.info("WaitListEmailThread has been unscheduled.");
		}
	}

	@Override
    public void run() {
		logger.debug("WaitListEmailThread start");
		
		try
		{
			notifyProgramAdmissions();
		}
		catch (Exception e)
		{
			logger.error("Unexpected Error", e);
		}

		logger.debug("WaitListEmailThread end");
    }

	private void notifyProgramAdmissions() {
		String programIdsString=StringUtils.trimToNull(OscarProperties.getInstance().getProperty("wait_list_email_notification_program_ids"));
		
		if (programIdsString==null) return;
		
		String[] programIdsStringSplit=programIdsString.split(",");
	    
		ProgramDao programDao=(ProgramDao) SpringUtils.getBean("programDao");
		WaitListManager waitListManager=(WaitListManager) SpringUtils.getBean("waitListManager");
		
		for (String programIdString : programIdsStringSplit)
		{
			try {
	            Integer programId=new Integer(programIdString);
	            Program program=programDao.getProgram(programId);
	            waitListManager.checkAndSendIntervalNotification(program);
            } catch (Exception e) {
	         logger.error("Unexpected error processing programId="+programIdString, e);
            }
		}
    }	
}