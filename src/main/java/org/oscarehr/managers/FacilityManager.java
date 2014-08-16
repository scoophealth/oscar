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

import org.oscarehr.common.dao.FacilityDao;
import org.oscarehr.common.model.Facility;
import org.oscarehr.util.LoggedInInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.log.LogAction;

@Service
public class FacilityManager {
	@Autowired
	private FacilityDao facilityDao;

	public Facility getDefaultFacility(LoggedInInfo loggedInInfo) {
		List<Facility> results = facilityDao.findAll(true);
		if (results.size() == 0) {
			return (null);
		} else {

			//--- log action ---
			LogAction.addLogSynchronous(loggedInInfo,"FacilityManager.getDefaultFacility", null);

			return (results.get(0));
		}
	}

	public List<Facility> getAllFacilities(LoggedInInfo loggedInInfo, Boolean active) {
		List<Facility> results = facilityDao.findAll(active);
		
		//--- log action ---
		LogAction.addLogSynchronous(loggedInInfo,"FacilityManager.getAllFacilities", null);

		return (results);
	}
}
