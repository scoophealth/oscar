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

import java.util.Date;
import java.util.List;

import org.oscarehr.common.dao.IntegratorProgressDao;
import org.oscarehr.common.dao.IntegratorProgressItemDao;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.IntegratorProgress;
import org.oscarehr.common.model.IntegratorProgressItem;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IntegratorPushManager {

	public static final String DISABLE_INTEGRATOR_PUSH_PROP = "DisableIntegratorPushes";
	public static final String INTEGRATOR_PAUSE_FULL_PUSH = "PauseIntegratorFullPush";

	@Autowired
	private IntegratorProgressDao integratorProgressDao;
	
	@Autowired
	private IntegratorProgressItemDao integratorProgressItemDao;
	
	
	public IntegratorProgress createNewPush(List<Integer> demographicIds) {
		IntegratorProgress ip = new IntegratorProgress();
		ip.setDateCreated(new Date());
		ip.setStatus(IntegratorProgress.STATUS_RUNNING);
		
		integratorProgressDao.persist(ip);
		
		for(Integer id:demographicIds) {
			IntegratorProgressItem ipi = new IntegratorProgressItem(ip.getId(),id);
			integratorProgressItemDao.persist(ipi);
		}
		
		return ip;
	}
	
	public void updateItem(IntegratorProgress ip, Integer demographicNo) {
		IntegratorProgressItem ipi = integratorProgressItemDao.find(ip.getId(),demographicNo);
		if(ipi != null) {
			ipi.setStatus(IntegratorProgressItem.STATUS_COMPLETED);
			integratorProgressItemDao.merge(ipi);
		}
	}
	
	public void completePush(IntegratorProgress ip, boolean verify) {
		ip.setStatus(IntegratorProgress.STATUS_COMPLETED);
		integratorProgressDao.merge(ip);
		
		if(verify) {
			//TODO: check that all the items are complete...want
			//to see this in action a bit so see how to handle
			//problematic records that don't send for example.
		}
	}
	
	public void setError(IntegratorProgress ip, Exception e) {
		ip.setStatus(IntegratorProgress.STATUS_ERROR);
		ip.setErrorMessage(e.getMessage());
		integratorProgressDao.merge(ip);
		
	}
	
	public IntegratorProgress getCurrentlyRunning() {
		List<IntegratorProgress> runningOnes = integratorProgressDao.findRunning();
		if(runningOnes.isEmpty()) {
			return null;
		}
		if(runningOnes.size()>1) {
			MiscUtils.getLogger().error("You have more than 1 entry in the IntegratorProgress table that has status 'running'. Should have 0 or 1");
			return null;
		}
		return runningOnes.get(0);
	}
	
	public List<Integer> findOutstandingDemographicNos(IntegratorProgress ip) {
		List<Integer> results = integratorProgressItemDao.findOutstandingDemographicNos(ip.getId());
		
		return results;
	}
	
	public List<IntegratorProgress> findAll() {
		List<IntegratorProgress> results = null;
		results = integratorProgressDao.findRunning();
		results.addAll(integratorProgressDao.findCompleted());
		return results;
	}
	
	public int getTotalItemsForProgress(Integer ipId) {
		return integratorProgressItemDao.findTotalDemographicNos(ipId);
	}
	
	public int getTotalOutstandingItemsForProgress(Integer ipId) {
		return integratorProgressItemDao.findTotalOutstandingDemographicNos(ipId);
	}
	
	public boolean isPauseFlagSet() {
		UserPropertyDAO userPropertyDao = SpringUtils.getBean(UserPropertyDAO.class);
		
		UserProperty up = userPropertyDao.getProp(INTEGRATOR_PAUSE_FULL_PUSH);
		if(up == null) {
			return false;
		}
		if(up.getValue().equals("true")) {
			return true;
		}
		return false;
	}
	
	public void togglePause(boolean pause) {
		UserPropertyDAO userPropertyDao = SpringUtils.getBean(UserPropertyDAO.class);
		
		UserProperty up = userPropertyDao.getProp(INTEGRATOR_PAUSE_FULL_PUSH);
		if(up == null) {
			up = new UserProperty();
			up.setName(INTEGRATOR_PAUSE_FULL_PUSH);
			up.setValue(new Boolean(pause).toString());
			userPropertyDao.persist(up);
		} else {
			up.setValue(new Boolean(pause).toString());
			userPropertyDao.merge(up);
		}
	}
}
