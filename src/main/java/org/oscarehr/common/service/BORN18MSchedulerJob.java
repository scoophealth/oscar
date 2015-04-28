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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimerTask;

import org.apache.cxf.common.util.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.common.dao.SecurityDao;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Security;
import org.oscarehr.integration.born.BORN18MConnector;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;

public class BORN18MSchedulerJob extends TimerTask {

	private static final OscarProperties oscarProperties = OscarProperties.getInstance();
	
	private static final String uploadHour = oscarProperties.getProperty("born18m_upload_hour", "01");
	private static final Logger logger = MiscUtils.getLogger();

	private ProviderManager providerManager = SpringUtils.getBean(ProviderManager.class);
	private SecurityDao securityDao = SpringUtils.getBean(SecurityDao.class);
	
	@Override
	public void run() {
		
		LoggedInInfo x = new LoggedInInfo();
		
		String jobProvider = OscarProperties.getInstance().getProperty("born18m_job_provider");
		if(StringUtils.isEmpty(jobProvider)) {
			logger.error("no born provider - set born18m_job_provider");
			return;
		}
		
		//load the provider record
		Provider p = providerManager.getProvider(jobProvider);
		if(p == null) {
			logger.error("cannot load born provider: " + jobProvider);
			return;
		}
		x.setLoggedInProvider(p);
		
		//load the first security record
		for(Security sec : securityDao.findByProviderNo(p.getProviderNo())) {
			x.setLoggedInSecurity(sec);
			break;
		}
		
    	Calendar cal = Calendar.getInstance();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH");
    	if (!sdf.format(cal.getTime()).equals(uploadHour)) return;
		
		logger.info("starting BORN18M upload job");
		
		BORN18MConnector c = new BORN18MConnector();
        c.updateBorn(x);
        
		logger.info("done BORN18M upload job");
	}
}
