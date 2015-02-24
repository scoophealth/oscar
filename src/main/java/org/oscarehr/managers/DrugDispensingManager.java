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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.dao.DrugDispensingDao;
import org.oscarehr.common.dao.DrugProductDao;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.DrugDispensing;
import org.oscarehr.common.model.DrugProduct;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DrugDispensingManager {

	@Autowired
	private DrugDispensingDao drugDispensingDao;
	
	@Autowired
	private DrugProductDao drugProductDao;
	
	@Autowired
	private DrugDao drugDao;
	
	
	/**
	 * Return the dispensing status as a string.
	 * 
	 * there's some extra code in here to be used later to get info about doses remaining/available/dispensed
	 * @param drugId
	 * @return
	 */
	public String getStatus(Integer drugId) {
		
		Drug drug = drugDao.find(drugId);
		
		if(drug == null) {
			return null;
		}
		
		Integer totalDosesAvailable = null;
		String strTotalDosesAvailable = "<Unknown>";
		
		try {
			int quantity = Integer.parseInt(drug.getQuantity());
			totalDosesAvailable  = quantity + (quantity * drug.getRepeat());
			strTotalDosesAvailable = totalDosesAvailable.toString();
		}catch(NumberFormatException e){
			MiscUtils.getLogger().error("Error",e);
			return null;
		}
		
		List<DrugDispensing> dispensingEvents = drugDispensingDao.findByDrugId(drugId);
		
		Map<Integer,Integer> productAmounts = new HashMap<Integer,Integer>();
		
		for(DrugDispensing dd:dispensingEvents) {
			int totalAmountForDD = 0;
				
			List<DrugProduct> dps = drugProductDao.findByDispensingId(dd.getId());
			for(int x=0;x<dps.size();x++) {
				DrugProduct dp = dps.get(x);
				totalAmountForDD += dp.getAmount();
			}
			
			productAmounts.put(dd.getId(),totalAmountForDD);
		}
		
		int totalDosesDispensed = 0;
		int totalDispensingEvents = dispensingEvents.size();
		int totalQuantitiesDispensed = 0;
		
		for(DrugDispensing dd:dispensingEvents) {
			totalDosesDispensed += productAmounts.get(dd.getId());
			totalQuantitiesDispensed += dd.getQuantity();
		}
		
		Integer totalDosesRemaining = (totalDosesAvailable==null)?null:new Integer(totalDosesAvailable-totalDosesDispensed);
		String strTotalDosesRemaining = (totalDosesRemaining==null)?"<Unknown>":String.valueOf(totalDosesRemaining);
		
		String status = null;
		if(totalDosesRemaining != null && totalDosesRemaining > 0) {
			status = "Active";
		} else {
				status="Filled";
		}
		if(status.equals("Active") && drug.isExpired()) {
			status="Expired";	
		}
		
		return status;
	}
}
