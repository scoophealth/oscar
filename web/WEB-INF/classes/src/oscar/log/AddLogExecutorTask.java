package oscar.log;

import org.oscarehr.common.model.OscarLog;

/**
 * No one should be calling / using this class except the LogAction.java class.
 */
class AddLogExecutorTask implements Runnable {

	private OscarLog oscarLog;
	
	public AddLogExecutorTask(OscarLog oscarLog)
	{
		this.oscarLog=oscarLog;
	}
	
	public void run() {
		LogAction.addLogSynchronous(oscarLog);
	}
}
