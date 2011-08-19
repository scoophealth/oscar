package oscar.log;

import org.oscarehr.common.model.OscarLog;
import org.oscarehr.util.DbConnectionFilter;

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
		try
		{
			LogAction.addLogSynchronous(oscarLog);
		}
		finally
		{
			DbConnectionFilter.releaseAllThreadDbResources();
		}
	}
}
