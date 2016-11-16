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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.oscarehr.common.dao.AllergyDao;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.util.LoggedInInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.log.LogAction;

@Service
public class AllergyManager {
	@Autowired
	private AllergyDao allergyDao;
	@Autowired
	SecurityInfoManager securityInfoManager;

	public Allergy getAllergy(LoggedInInfo loggedInInfo, Integer id) {
		Allergy result = allergyDao.find(id);

		//--- log action ---
		if (result != null) {
			LogAction.addLogSynchronous( loggedInInfo, "AllergyManager.getAllergy", "id=" + id);
		}

		return (result);
	}

	public List<Allergy> getActiveAllergies( LoggedInInfo loggedInInfo, Integer demographicNo ) {
		if(demographicNo == null) {
			return null;
		}
		
		if (! securityInfoManager.hasPrivilege(loggedInInfo, "_allergy", SecurityInfoManager.READ, demographicNo) ) {
			throw new RuntimeException("Access Denied");
		}
		
		List<Allergy> results = allergyDao.findActiveAllergiesOrderByDescription(demographicNo);
		
		//--- log action ---
		if (results!=null && results.size()>0) {
			LogAction.addLogSynchronous(loggedInInfo, "AllergyManager.getActiveAllergies", "demographicNo=" + demographicNo);
		}

		return(results);
	}
	
	public List<Allergy> getUpdatedAfterDate(LoggedInInfo loggedInInfo, Date updatedAfterThisDateInclusive, int itemsToReturn) {
		List<Allergy> results = allergyDao.findByUpdateDate(updatedAfterThisDateInclusive, itemsToReturn);

		LogAction.addLogSynchronous(loggedInInfo, "AllergyManager.getUpdatedAfterDate", "updatedAfterThisDateInclusive=" + updatedAfterThisDateInclusive);

		return (results);
	}

	/**
	 * At this time, not all criteria maybe available in oscar but the method signature is what is "should" be
	 * and hopefully can be refactored as data becomes available.
	 */
	public List<Allergy> getAllergiesByProgramProviderDemographicDate(LoggedInInfo loggedInInfo, Integer programId, String providerNo, Integer demographicId, Calendar updatedAfterThisDateInclusive, int itemsToReturn) {
		List<Allergy> results = allergyDao.findByProviderDemographicLastUpdateDate(providerNo, demographicId, updatedAfterThisDateInclusive.getTime(), itemsToReturn);

		LogAction.addLogSynchronous(loggedInInfo, "AllergyManager.getUpdatedAfterDate", "programId=" + programId + ", providerNo=" + providerNo + ", demographicId=" + demographicId + ", updatedAfterThisDateInclusive=" + updatedAfterThisDateInclusive.getTime());

		return (results);
	}
	
}
