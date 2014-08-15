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

import org.oscarehr.common.dao.LookupListDao;
import org.oscarehr.common.dao.LookupListItemDao;
import org.oscarehr.common.model.LookupList;
import org.oscarehr.common.model.LookupListItem;
import org.oscarehr.util.LoggedInInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.log.LogAction;

@Service
public class LookupListManager {

	@Autowired
	private LookupListDao lookupListDao;
	@Autowired
	private LookupListItemDao lookupListItemDao;
	
	
	public List<LookupList> findAllActiveLookupLists(LoggedInInfo loggedInInfo) {
		List<LookupList> results = lookupListDao.findAllActive();
		
		//--- log action ---
		if (results.size()>0) {
			String resultIds=LookupList.getIdsAsStringList(results);
			LogAction.addLogSynchronous(loggedInInfo, "LookupListManager.findAllLookupLists", "ids returned=" + resultIds);
		}

		return (results);
	}
	
	public LookupList findLookupListById(LoggedInInfo loggedInInfo, int id) {
		LookupList result = lookupListDao.find(id);
		
		//--- log action ---
		if (result != null) {
			LogAction.addLogSynchronous(loggedInInfo, "LookupListManager.findLookupListById", "id returned=" + result.getId());
		}

		return (result);
		
	}
	
	public LookupList findLookupListByName(LoggedInInfo loggedInInfo, String name) {
		LookupList result = lookupListDao.findByName(name);
		
		//--- log action ---
		if (result != null) {
			LogAction.addLogSynchronous(loggedInInfo, "LookupListManager.findLookupListByName", "id returned=" + result.getId());
		}

		return (result);
		
	}
	
	public LookupList addLookupList(LoggedInInfo loggedInInfo, LookupList lookupList) {
		lookupListDao.persist(lookupList);
		LogAction.addLogSynchronous(loggedInInfo, "LookupListManager.addLookupList", "id=" + lookupList.getId());
		

		return (lookupList);
		
	}
	
	public LookupListItem addLookupListItem(LoggedInInfo loggedInInfo, LookupListItem lookupListItem) {
		lookupListItemDao.persist(lookupListItem);
		LogAction.addLogSynchronous(loggedInInfo, "LookupListManager.addLookupListItem", "id=" + lookupListItem.getId());
		

		return (lookupListItem);
		
	}
	
}
