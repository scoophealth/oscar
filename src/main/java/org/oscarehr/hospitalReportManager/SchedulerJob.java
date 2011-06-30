package org.oscarehr.hospitalReportManager;

import java.util.Date;
import java.util.TimerTask;

import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

/**
 * A job can start many tasks
 * 
 * @author dritan
 * 
 */
public class SchedulerJob extends TimerTask {

	private static long lastRun = new Date().getTime();
	private static boolean firstRun = false;
	
	@Override
	public void run() {
		UserPropertyDAO userPropertyDao = (UserPropertyDAO) SpringUtils.getBean("UserPropertyDAO");

		UserProperty hrmInterval = userPropertyDao.getProp("hrm_interval");

		Integer intervalTime = 1800;

		try {
			if (hrmInterval != null && hrmInterval.getValue() != null && hrmInterval.getValue().trim().length() > 0) {
				intervalTime = Integer.parseInt(hrmInterval.getValue()) * 60;
			} 
		} catch (Exception e) {
			intervalTime = 1800;
		}
		
		if (!firstRun) {
			if (lastRun + intervalTime >= new Date().getTime()) {
				// Run now
				SFTPConnector.startAutoFetch(false);
				SchedulerJob.lastRun = new Date().getTime();
			}
		} else {
			SFTPConnector.startAutoFetch(false);
			SchedulerJob.firstRun = false;
		}
		
		MiscUtils.getLogger().debug("===== HRM JOB RUNNING....");
		SFTPConnector.startAutoFetch(false);

	}
}