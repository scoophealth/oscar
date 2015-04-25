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
package org.oscarehr.common.jobs;

import java.util.concurrent.ScheduledFuture;

import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.OscarJobDao;
import org.oscarehr.common.model.OscarJob;
import org.oscarehr.common.model.OscarJobType;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Security;
import org.oscarehr.util.SpringUtils;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

public class OscarJobUtils {


	
	public static boolean isJobTypeCurrentlyValid(OscarJobType oscarJobType) {
		
		if(oscarJobType.getClassName() == null) {
			return false;
		}
		
		try {
			Class clazz = Class.forName(oscarJobType.getClassName());
			for(Class i:clazz.getInterfaces()) {
				if(i.getName().equals("org.oscarehr.common.jobs.OscarRunnable")) {
					return true;
				}
			}
		} catch(Exception e) {
			//ignore
		}
		
		return false;
	}
	
	
	public static void initializeJobExecutionFramework() throws Exception {
		//SpringTaskScheduler
		OscarJobDao oscarJobDao = SpringUtils.getBean(OscarJobDao.class);
				
		
		for(OscarJob job:oscarJobDao.findAll(0,OscarJobDao.MAX_LIST_RETURN_SIZE)) {
			scheduleJob(job);	
		}

	}
	
	
	public static boolean scheduleJob(OscarJob job) throws Exception {
		//SpringTaskScheduler
		TaskScheduler taskScheduler = (TaskScheduler) SpringUtils.getBean("taskScheduler");
		OscarJobDao oscarJobDao = SpringUtils.getBean(OscarJobDao.class);
		ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
		
		ScheduledFuture<Object> future = OscarJobExecutingManager.getFutures().get(job.getId());
		if(future != null) {
			future.cancel(false);
		}
		
		if(!job.isEnabled()) {
			return false;
		}
		if(job.getCronExpression() == null) {
			return false;
		}
		if(job.getOscarJobType() == null || !job.getOscarJobType().isEnabled() || !OscarJobUtils.isJobTypeCurrentlyValid(job.getOscarJobType())) {
			return false;
		}
		
		CronTrigger trigger = new CronTrigger(job.getCronExpression());
	
		OscarRunnable oscarRunnableInstance = (OscarRunnable)Class.forName(job.getOscarJobType().getClassName()).newInstance();
		
		Security security = new Security();
		security.setSecurityNo(0);
		oscarRunnableInstance.setLoggedInSecurity(security);
		
		Provider provider = providerDao.getProvider(job.getProviderNo());
		if(provider == null) {
			return false;
		}
		oscarRunnableInstance.setLoggedInProvider(provider);
		
		ScheduledFuture<Object> schedulefuture= taskScheduler.schedule(oscarRunnableInstance, trigger );
		//cancel,isCancelled, isDone
		
		OscarJobExecutingManager.getFutures().put(job.getId(),schedulefuture);
		
		return true;
	}
	
}
