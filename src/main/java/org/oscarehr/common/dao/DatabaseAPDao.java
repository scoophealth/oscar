/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */
package org.oscarehr.common.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.oscarehr.common.model.OscarLog;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class DatabaseAPDao {

	@PersistenceContext
	protected EntityManager entityManager = null;

	public boolean executeUpdate(String sql, String apName, Integer demographicNo, String formId, String ip) {
		Query query = entityManager.createNativeQuery(sql);
		try {
			OscarLogDao oscarLogDao = (OscarLogDao) SpringUtils.getBean("oscarLogDao");
			OscarLog logEntry = new OscarLog();

			logEntry.setContent("DatabaseAP");
			logEntry.setContentId(formId);
			logEntry.setDemographicId(demographicNo);
			logEntry.setProviderNo(LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo());
			logEntry.setAction("edit");
			logEntry.setData("[apName=" + apName + "] " + sql);
			logEntry.setIp(ip);

			oscarLogDao.persist(logEntry);

			query.executeUpdate();

			return true;

		} catch (Exception e) {
			MiscUtils.getLogger().error("Couldn't perform DatabaseAP update. Query: " + sql, e);
		}

		return false;
	}

}
