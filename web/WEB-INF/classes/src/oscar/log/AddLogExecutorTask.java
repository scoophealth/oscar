package oscar.log;

import org.oscarehr.common.model.OscarLog;

public class AddLogExecutorTask implements Runnable {

	private OscarLog oscarLog;
	
	public AddLogExecutorTask(OscarLog oscarLog)
	{
		this.oscarLog=oscarLog;
	}
	
	public void run() {
		LogAction.addLogSynchronous(oscarLog);
	}
}
