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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.oscarehr.common.dao.PreventionDao;
import org.oscarehr.common.dao.PreventionExtDao;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.common.model.PreventionExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.log.LogAction;
import oscar.oscarPrevention.PreventionDisplayConfig;
import oscar.util.StringUtils;

@Service
public class PreventionManager {
	@Autowired
	private PreventionDao preventionDao;
	@Autowired
	private PreventionExtDao preventionExtDao;
	
	private ArrayList<String> preventionTypeList = new ArrayList<String>();

	/**
	 * @deprecated 2014-05-20 remove after calling ws method is removed
	 */
	public List<Prevention> getPreventionsByIdStart(Boolean archived, Integer startIdInclusive, int itemsToReturn) {
		List<Prevention> results = preventionDao.findByIdStart(archived, startIdInclusive, itemsToReturn);

		//--- log action ---
		if (results.size()>0) {
			String resultIds=Prevention.getIdsAsStringList(results);
			LogAction.addLogSynchronous("PreventionManager.getPreventionsByIdStart", "ids returned=" + resultIds);
		}

		return (results);
	}
	
	public List<Prevention> getUpdatedAfterDate(Date updatedAfterThisDateInclusive, int itemsToReturn) {
		List<Prevention> results = preventionDao.findByUpdateDate(updatedAfterThisDateInclusive, itemsToReturn);

		LogAction.addLogSynchronous("PreventionManager.getUpdatedAfterDate", "updatedAfterThisDateInclusive=" + updatedAfterThisDateInclusive);

		return (results);
	}

	public Prevention getPrevention(Integer id)
	{
		Prevention result=preventionDao.find(id);
		
		//--- log action ---
		if (result != null) {
			LogAction.addLogSynchronous("PreventionManager.getPrevention", "id=" + id);
		}

		return(result);
	}

	public List<PreventionExt> getPreventionExtByPrevention(Integer preventionId)
	{
		List<PreventionExt> results=preventionExtDao.findByPreventionId(preventionId);
		
		//--- log action ---
		if (results.size()>0) {
			String resultIds=PreventionExt.getIdsAsStringList(results);
			LogAction.addLogSynchronous("PreventionManager.getPreventionExtByPrevention", "ids returned=" + resultIds);
		}

		return(results);
	}
	
	public ArrayList<String> getPreventionTypeList() {
		if (preventionTypeList.isEmpty()) {
			PreventionDisplayConfig pdc = PreventionDisplayConfig.getInstance();
			for (HashMap<String,String> prevTypeHash : pdc.getPreventions()) {
			    if (prevTypeHash != null && StringUtils.filled(prevTypeHash.get("name"))) {
			    	preventionTypeList.add(prevTypeHash.get("name").trim()); 
			    }
			}
		}
		return preventionTypeList;
	}
	
	public void addPreventionWithExts(Prevention prevention, HashMap<String, String> exts) {
		if (prevention == null) return;
		
		preventionDao.persist(prevention);
		if (exts != null) {
			for (String keyval : exts.keySet()) {
				if (StringUtils.filled(keyval) && StringUtils.filled(exts.get(keyval))) {
					PreventionExt preventionExt = new PreventionExt();
					preventionExt.setPreventionId(prevention.getId());
					preventionExt.setKeyval(keyval);
					preventionExt.setVal(exts.get(keyval));
					preventionExtDao.persist(preventionExt);
				}
			}
		}
	}
}
