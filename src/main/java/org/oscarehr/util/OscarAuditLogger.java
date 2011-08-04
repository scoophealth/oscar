package org.oscarehr.util;

import org.oscarehr.common.dao.OscarLogDao;
import org.oscarehr.common.model.OscarLog;

public class OscarAuditLogger {

	private static OscarAuditLogger instance = new OscarAuditLogger();
	private static OscarLogDao logDao = (OscarLogDao) SpringUtils.getBean("oscarLogDao");

	private OscarAuditLogger() {
		
	}
	
	public static OscarAuditLogger getInstance() {		
		return instance;
	}
	
	public void log(String action, String content, String data) {
		try {
			OscarLog logItem = new OscarLog();
			logItem.setAction(action);
			logItem.setContent(content);
			logItem.setData(data);

			if (LoggedInInfo.loggedInInfo.get().loggedInProvider != null)
				logItem.setProviderNo(LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo());
			
			logDao.persist(logItem);

		} catch (Exception e) {
			MiscUtils.getLogger().error("Couldn't write log message", e);
		}
	}
}
