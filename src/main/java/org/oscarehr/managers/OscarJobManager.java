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
package org.oscarehr.managers;

import java.util.ArrayList;
import java.util.List;

import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.common.dao.OscarJobDao;
import org.oscarehr.common.dao.OscarJobTypeDao;
import org.oscarehr.common.jobs.OscarJobUtils;
import org.oscarehr.common.model.OscarJob;
import org.oscarehr.common.model.OscarJobType;
import org.oscarehr.util.LoggedInInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.log.LogAction;

@Service
public class OscarJobManager {

	//private Logger logger = MiscUtils.getLogger();
	
	@Autowired
	private OscarJobDao oscarJobDao;
	
	@Autowired
	private OscarJobTypeDao oscarJobTypeDao;
	
	
	public void saveJob(LoggedInInfo loggedInInfo, OscarJob oscarJob) {
        oscarJobDao.persist(oscarJob);
     
        //--- log action ---
		LogAction.addLogSynchronous(loggedInInfo,"OscarJobManager.saveJob", "oscarJob.id="+oscarJob.getId());
    }
	
	public void updateJob(LoggedInInfo loggedInInfo, OscarJob oscarJob) {
        oscarJobDao.merge(oscarJob);
     
        //--- log action ---
		LogAction.addLogSynchronous(loggedInInfo,"OscarJobManager.updateJob", "oscarJob.id="+oscarJob.getId());
    }
	
	/*
	 * Make sure it's a valid class, and that it implements OscarRunnable
	 */
	public List<OscarJobType> getCurrentlyAvaliableJobTypes() {
		
		List<OscarJobType> validTypes = new ArrayList<OscarJobType>();
		
		for(OscarJobType oscarJobType : oscarJobTypeDao.findAll(0, AbstractDao.MAX_LIST_RETURN_SIZE)) {
			if(OscarJobUtils.isJobTypeCurrentlyValid(oscarJobType)) {
				validTypes.add(oscarJobType);
			}
		}
		return validTypes;
	}
	
	public List<OscarJobType> getAllJobTypes() {
		return  oscarJobTypeDao.findAll(0, AbstractDao.MAX_LIST_RETURN_SIZE);
	}
	
	
	public List<OscarJob> getAllJobs(LoggedInInfo loggedInInfo) {
		
		List<OscarJob> jobs = oscarJobDao.findAll(0, OscarJobDao.MAX_LIST_RETURN_SIZE);
		
		//--- log action ---
		LogAction.addLogSynchronous(loggedInInfo, "OscarJobManager.getAllJobs","");
	
		return jobs;
	}
	
	public OscarJob getJob(LoggedInInfo loggedInInfo, Integer id) {
		
		OscarJob job = oscarJobDao.find(id);
		
		if(job != null) {
			//--- log action ---
			LogAction.addLogSynchronous(loggedInInfo, "OscarJobManager.getJob","id="+id);
		}
	
		return job;
	}
	
	public OscarJobType getJobType(LoggedInInfo loggedInInfo, Integer id) {
		
		OscarJobType jobType = oscarJobTypeDao.find(id);
		
		if(jobType != null) {
			//--- log action ---
			LogAction.addLogSynchronous(loggedInInfo, "OscarJobManager.getJobType","id="+id);
		}
	
		return jobType;
	}
	
	public void saveJobType(LoggedInInfo loggedInInfo, OscarJobType oscarJob) {
        oscarJobTypeDao.persist(oscarJob);
     
        //--- log action ---
		LogAction.addLogSynchronous(loggedInInfo, "OscarJobManager.saveJobType", "oscarJobType.id="+oscarJob.getId());
    }
	
	public void updateJobType(LoggedInInfo loggedInInfo, OscarJobType oscarJob) {
        oscarJobTypeDao.merge(oscarJob);
     
        //--- log action ---
		LogAction.addLogSynchronous(loggedInInfo, "OscarJobManager.updateJobType", "oscarJobType.id="+oscarJob.getId());
    }
}
