/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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


package oscar.log;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.common.dao.OscarLogDao;
import org.oscarehr.common.model.OscarLog;
import org.oscarehr.util.DeamonThreadFactory;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarDB.DBPreparedHandler;

public class LogAction {
	private static Logger logger = MiscUtils.getLogger();
	private static OscarLogDao oscarLogDao = (OscarLogDao) SpringUtils.getBean("oscarLogDao");
	private static ExecutorService executorService = Executors.newCachedThreadPool(new DeamonThreadFactory(LogAction.class.getSimpleName()+".executorService", Thread.MAX_PRIORITY));

	public static void addLogSynchronous(String action, String data)
	{
		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		OscarLog logEntry=new OscarLog();
		if (loggedInInfo.loggedInSecurity!=null) logEntry.setSecurityId(loggedInInfo.loggedInSecurity.getSecurityNo());
		if (loggedInInfo.loggedInProvider!=null) logEntry.setProviderNo(loggedInInfo.loggedInProvider.getProviderNo());
		logEntry.setAction(action);
		logEntry.setData(data);
		LogAction.addLogSynchronous(logEntry);		
	}
	
	/**
	 * This method will add a log entry asynchronously in a separate thread.
	 */
	public static void addLog(String provider_no, String action, String content, String data) {
		addLog(provider_no, action, content, null, null, null, data);
	}

	/**
	 * This method will add a log entry asynchronously in a separate thread.
	 */
	public static void addLog(String provider_no, String action, String content, String contentId, String ip) {
		addLog(provider_no, action, content, contentId, ip, null, null);
	}

	/**
	 * This method will add a log entry asynchronously in a separate thread.
	 */
	public static void addLog(String provider_no, String action, String content, String contentId, String ip, String demographicNo) {
		addLog(provider_no, action, content, contentId, ip, demographicNo, null);
	}

	/**
	 * This method will add a log entry asynchronously in a separate thread.
	 */
	public static void addLog(String provider_no, String action, String content, String contentId, String ip, String demographicNo, String data) {
		OscarLog oscarLog = new OscarLog();

		oscarLog.setProviderNo(provider_no);
		oscarLog.setAction(action);
		oscarLog.setContent(content);
		oscarLog.setContentId(contentId);
		oscarLog.setIp(ip);

		try {
			demographicNo=StringUtils.trimToNull(demographicNo);
			if (demographicNo != null) oscarLog.setDemographicId(Integer.parseInt(demographicNo));
		} catch (Exception e) {
			logger.error("Unexpected error", e);
		}

		oscarLog.setData(data);

		executorService.execute(new AddLogExecutorTask(oscarLog));
	}

	/**
	 * This method will add a log entry in the same thread and can participate in the same transaction if one exists.
	 */
	public static void addLogSynchronous(String provider_no, String action, String content, String contentId, String ip) {
		OscarLog oscarLog = new OscarLog();

		oscarLog.setProviderNo(provider_no);
		oscarLog.setAction(action);
		oscarLog.setContent(content);
		oscarLog.setContentId(contentId);
		oscarLog.setIp(ip);

		addLogSynchronous(oscarLog);
	}
	
	/**
	 * This method will add a log entry in the same thread and can participate in the same transaction if one exists.
	 */
	public static void addLogSynchronous(String provider_no, String action, String content, String contentId, Integer demographicNo) {
		OscarLog oscarLog = new OscarLog();

		oscarLog.setProviderNo(provider_no);
		oscarLog.setAction(action);
		oscarLog.setContent(content);
		oscarLog.setContentId(contentId);
		oscarLog.setDemographicId(demographicNo);

		addLogSynchronous(oscarLog);
	}

	/**
	 * This method will add a log entry in the same thread and can participate in the same transaction if one exists.
	 */
	public static void addLogSynchronous(String provider_no, String action, String content, String contentId, Integer demographicNo, String data) {
		OscarLog oscarLog = new OscarLog();

		oscarLog.setProviderNo(provider_no);
		oscarLog.setAction(action);
		oscarLog.setContent(content);
		oscarLog.setContentId(contentId);
		oscarLog.setDemographicId(demographicNo);
		oscarLog.setData(data);
		addLogSynchronous(oscarLog);
	}

	/**
	 * This method will add the log entry in the same thread and transaction as it's being called in. This method will not throw exceptions, it will log to the file / console / log4j logger if an error occurs.
	 */
	public static void addLogSynchronous(OscarLog oscarLog) {
		try {
			oscarLogDao.persist(oscarLog);
		} catch (Exception e) {
			logger.error("Error in logger.", e);
			logger.error("Error logging entry : " + oscarLog);
		}
	}

	public static boolean logAccess(String provider_no, String className, String method, String programId, String shelterId, String clientId, String queryStr, String sessionId, long timeSpan, String ex, int result) {
		boolean ret = false;
		DBPreparedHandler db = new DBPreparedHandler();
		String sql = "insert into access_log (Id,provider_no,ACTIONCLASS,METHOD,QUERYSTRING,PROGRAMID,SHELTERID,CLIENTID,TIMESPAN,EXCEPTION,RESULT, SESSIONID)";
		sql += " values(seq_log_id.nextval,'" + provider_no + "', '" + className + "','" + method + "',";
		sql += "'" + queryStr + "'," + programId + "," + shelterId + "," + clientId + "," + String.valueOf(timeSpan) + ",'" + ex + "'," + result + ",'" + sessionId + "')";
		try {
			db.queryExecuteUpdate(sql);
			ret = true;
		} catch (SQLException e) {
			logger.error("Error", e);
		}
		return ret;
	}
}
