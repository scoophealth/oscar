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

import java.util.List;

import org.oscarehr.common.dao.PreventionDao;
import org.oscarehr.common.dao.PreventionExtDao;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.common.model.PreventionExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.log.LogAction;

@Service
public class PreventionManager {
	@Autowired
	private PreventionDao preventionDao;

	@Autowired
	private PreventionExtDao preventionExtDao;

	public List<Prevention> getPreventionsByIdStart(Boolean archived, Integer startIdInclusive, int itemsToReturn) {
		List<Prevention> results = preventionDao.findByIdStart(archived, startIdInclusive, itemsToReturn);

		//--- log action ---
		if (results.size()>0) {
			String resultIds=Prevention.getIdsAsStringList(results);
			LogAction.addLogSynchronous("PreventionManager.getPreventionsByIdStart", "ids returned=" + resultIds);
		}

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
}
