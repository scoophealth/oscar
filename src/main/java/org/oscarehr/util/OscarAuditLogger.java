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

	public void log(LoggedInInfo loggedInInfo, String action, String content, String data) {
		try {
			OscarLog logItem = new OscarLog();
			logItem.setAction(action);
			logItem.setContent(content);
			logItem.setData(data);

			if (loggedInInfo!=null) logItem.setProviderNo(loggedInInfo.getLoggedInProviderNo());

			logDao.persist(logItem);

		} catch (Exception e) {
			MiscUtils.getLogger().error("Couldn't write log message", e);
		}
	}

	public void log(LoggedInInfo loggedInInfo, String action, String content, Integer demographicNo, String data) {
		try {
			OscarLog logItem = new OscarLog();
			logItem.setAction(action);
			logItem.setContent(content);
			logItem.setDemographicId(demographicNo);
			logItem.setData(data);

			if (loggedInInfo!=null) logItem.setProviderNo(loggedInInfo.getLoggedInProviderNo());

			logDao.persist(logItem);

		} catch (Exception e) {
			MiscUtils.getLogger().error("Couldn't write log message", e);
		}
	}

	public void log(LoggedInInfo loggedInInfo, String action, String content, String keyword, String ipAddress, Integer demographicNo, String data) {
		try {
			OscarLog logItem = new OscarLog();
			logItem.setAction(action);
			logItem.setContent(content);
			logItem.setContentId(keyword);
			logItem.setDemographicId(demographicNo);
			logItem.setData(data);
			logItem.setIp(ipAddress);

			if (loggedInInfo!=null) logItem.setProviderNo(loggedInInfo.getLoggedInProviderNo());

			logDao.persist(logItem);

		} catch (Exception e) {
			MiscUtils.getLogger().error("Couldn't write log message", e);
		}
	}
}
