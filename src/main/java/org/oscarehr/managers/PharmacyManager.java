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

import org.oscarehr.common.dao.DemographicPharmacyDao;
import org.oscarehr.common.model.DemographicPharmacy;
import org.oscarehr.util.LoggedInInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.log.LogAction;

/**
 * Will provide access to pharmacy data.
 * 
 * Future Use: Add privacy, security, and consent profiles
 * 
 *
 */
@Service
public class PharmacyManager {

	//private Logger logger=MiscUtils.getLogger();

	@Autowired
	private DemographicPharmacyDao demographicPharmacyDao;

	public List<DemographicPharmacy> getPharmacies(LoggedInInfo loggedInInfo, Integer demographicId) {
		List<DemographicPharmacy> result =  demographicPharmacyDao.findAllByDemographicId(demographicId);
		
		if(result != null) {
			for(DemographicPharmacy item:result) {
		    	//--- log action ---
				LogAction.addLogSynchronous(loggedInInfo, "PharmacyManager.getPharmacies", "pharmacyId="+item.getPharmacyId());
			}
	    }
	    
	    return result;
	}

	public DemographicPharmacy addPharmacy(LoggedInInfo loggedInInfo, Integer demographicId, Integer pharmacyId, Integer preferredOrder) {
		DemographicPharmacy result =  demographicPharmacyDao.addPharmacyToDemographic(demographicId, pharmacyId, preferredOrder);
		
		if(result != null) {
	    	//--- log action ---
			LogAction.addLogSynchronous(loggedInInfo, "PharmacyManager.addPharmacy", "demographicNo="+demographicId+ ",pharmacyId="+pharmacyId);
	    }
	    
	    return result;
	}

	public void removePharmacy(LoggedInInfo loggedInInfo, Integer demographicId, Integer pharmacyId) {
		DemographicPharmacy pharmacy = demographicPharmacyDao.find(pharmacyId);
		if (pharmacy == null) {
			throw new IllegalArgumentException("Unable to locate pharmacy association with id " + pharmacyId);
		}
		
		if (pharmacy.getDemographicNo() != demographicId) {
			throw new IllegalArgumentException("Pharmacy association with id " + pharmacyId + " does't belong to demographic record with ID " + demographicId);
		}
		
		pharmacy.setStatus("0");
		demographicPharmacyDao.saveEntity(pharmacy);
		
		//--- log action ---
		LogAction.addLogSynchronous(loggedInInfo, "PharmacyManager.removePharmacy", "demographicNo="+demographicId + ",pharmacyId="+pharmacyId);	
	}

}
