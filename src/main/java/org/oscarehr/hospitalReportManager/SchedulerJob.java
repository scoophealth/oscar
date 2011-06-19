package org.oscarehr.hospitalReportManager;

import java.util.TimerTask;
import org.oscarehr.util.MiscUtils;

/**
 * A job can start many tasks
 * 
 * @author dritan
 * 
 */
public class SchedulerJob extends TimerTask {

	@Override
	public void run() {
		MiscUtils.getLogger().debug("===== JOB RUNNING....");
		SFTPConnector.startAutoFetch();

	}
}