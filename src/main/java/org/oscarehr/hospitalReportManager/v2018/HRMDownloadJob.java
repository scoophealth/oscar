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
package org.oscarehr.hospitalReportManager.v2018;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.jobs.OscarRunnable;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Security;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.hospitalReportManager.SFTPConnector;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;

public class HRMDownloadJob implements OscarRunnable {

	private Logger logger=MiscUtils.getLogger();
		
	UserPropertyDAO userPropertyDao = (UserPropertyDAO) SpringUtils.getBean("UserPropertyDAO");

	private Provider provider;
	private Security security;

	public void setLoggedInProvider(Provider provider) {
		this.provider = provider;
	}

	public void setLoggedInSecurity(Security security) {
		this.security = security;
	}
	
	private String getUserPropertyValueOrNull(String propName) {
		UserProperty up = userPropertyDao.getProp(propName);
		if (up != null && !StringUtils.isEmpty(up.getValue())) {
			return up.getValue();
		}
		return null;
	}
	
	@Override
	public void run() {
		
		LoggedInInfo x = new LoggedInInfo();
		x.setLoggedInProvider(provider);
		x.setLoggedInSecurity(security);

		
		try {
			logger.info("Starting HRM Job");

			String hostname = getUserPropertyValueOrNull("hrm_hostname");
			String port = getUserPropertyValueOrNull("hrm_port");
			String username = getUserPropertyValueOrNull("hrm_username");
			String remoteDir = getUserPropertyValueOrNull("hrm_location");
			String decryptionKey = getUserPropertyValueOrNull("hrm_decryption_key");
			String privateKeyFile = getUserPropertyValueOrNull("hrm_private_key_file");
			
			String privateKeyDirectory = OscarProperties.getInstance().getProperty("OMD_DIRECTORY");
			if (privateKeyDirectory == null) {
				privateKeyDirectory = OscarProperties.getInstance().getProperty("DOCUMENT_DIR") + ".." + File.separator + "hrm" + File.separator + "OMD" + File.separator;
			}
			
			
			SFTPConnector connector = new SFTPConnector(x, hostname,Integer.parseInt(port),username,privateKeyDirectory + privateKeyFile,"Automatic");
			SFTPConnector.setDecryptionKey(decryptionKey);
			connector.startAutoFetch(x,remoteDir);
				
			logger.info("===== HRM JOB DONE RUNNING....");
		} catch (Exception e) {
			logger.error("Error", e);
		} finally {
			DbConnectionFilter.releaseAllThreadDbResources();
		}
	}


	public void setConfig(String config) {
	}
	
}
