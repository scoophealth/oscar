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
package org.oscarehr.common.service;

import java.util.List;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.caisi.dao.ProviderDAO;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.SecurityDao;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Security;
import org.oscarehr.integration.born.ONAREnhancedBornConnector;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;

public class BORNSchedulerJob extends TimerTask {

	private static final Logger logger = MiscUtils.getLogger();


	@Override
	public void run() {
		try {
			
			String providerNo = OscarProperties.getInstance().getProperty("born_scheduler_job_run_as_provider");
			if(providerNo == null) {
				return;
			}
			
			ProviderDAO providerDao = SpringUtils.getBean(ProviderDao.class);
			Provider provider = providerDao.getProvider(providerNo);
			
			if(provider == null) {
				return;
			}
			
			SecurityDao securityDao = SpringUtils.getBean(SecurityDao.class);
			List<Security> securityList = securityDao.findByProviderNo(providerNo);
			
			if(securityList.isEmpty()) {
				return;
			}
			
			LoggedInInfo x = new LoggedInInfo();
			x.setLoggedInProvider(provider);
			x.setLoggedInSecurity(securityList.get(0));
			
			logger.info("starting BORN upload job");
			
			ONAREnhancedBornConnector c = new ONAREnhancedBornConnector();
			c.updateBorn(x);
			
			logger.info("done BORN upload job");
			
		}catch(Exception e ) {
			logger.error("Error",e);	
		} finally {
			DbConnectionFilter.releaseAllThreadDbResources();
		}
	}
}
