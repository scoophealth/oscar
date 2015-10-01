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
import java.util.List;

import org.oscarehr.common.dao.DrugProductDao;
import org.oscarehr.common.dao.DrugProductTemplateDao;
import org.oscarehr.common.dao.ProductLocationDao;
import org.oscarehr.common.model.DrugProduct;
import org.oscarehr.common.model.DrugProductTemplate;
import org.oscarehr.common.model.ProductLocation;
import org.oscarehr.util.LoggedInInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.log.LogAction;

@Service
public class DrugProductManager {

	@Autowired
	private DrugProductDao drugProductDao;
	
	@Autowired
	private ProductLocationDao productLocationDao;
	
	@Autowired
	private DrugProductTemplateDao drugProductTemplateDao;
	
	
	public void saveDrugProduct(LoggedInInfo loggedInInfo, DrugProduct drugProduct) {
		drugProduct.setLastUpdateUser(loggedInInfo.getLoggedInProviderNo());
		drugProduct.setDateCreated(new Date());
		drugProduct.setLastUpdateDate(drugProduct.getDateCreated());
		drugProductDao.persist(drugProduct);
		
		//--- log action ---
		LogAction.addLogSynchronous(loggedInInfo,"DrugProductManager.saveDrugProduct", "id="+drugProduct.getId());
		
	}
	
	public void updateDrugProduct(LoggedInInfo loggedInInfo, DrugProduct drugProduct) {
		drugProduct.setLastUpdateUser(loggedInInfo.getLoggedInProviderNo());
		drugProduct.setLastUpdateDate(new Date());
		
		drugProductDao.merge(drugProduct);
		
		//--- log action ---
		LogAction.addLogSynchronous(loggedInInfo,"DrugProductManager.saveDrugProduct", "id="+drugProduct.getId());
		
	}
	
	public DrugProduct getDrugProduct(LoggedInInfo loggedInInfo, Integer id) {
		DrugProduct result = drugProductDao.find(id);
		
		if(result != null) {
			//--- log action ---
			LogAction.addLogSynchronous(loggedInInfo,"DrugProductManager.getDrugProduct", "id="+result.getId());	
		}
		
		return result;
	}
	
	public List<DrugProduct> getAllDrugProducts(LoggedInInfo loggedInInfo, Integer offset, Integer limit) {
		List<DrugProduct> results = drugProductDao.findAll(offset, limit);
		
		//--- log action ---
		if (results.size()>0) {
			String resultIds=DrugProduct.getIdsAsStringList(results);
			LogAction.addLogSynchronous(loggedInInfo,"DrugProductManager.getAllDrugProducts", "ids returned=" + resultIds);
		}
		
		return results;
	}
	
	public List<DrugProduct> getAllDrugProductsByName(LoggedInInfo loggedInInfo, Integer offset, Integer limit, String productName) {
		List<DrugProduct> results = drugProductDao.findByName(offset,limit,productName);
		
		//--- log action ---
		if (results.size()>0) {
			String resultIds=DrugProduct.getIdsAsStringList(results);
			LogAction.addLogSynchronous(loggedInInfo,"DrugProductManager.getAllDrugProductsByName", "ids returned=" + resultIds);
		}
		
		return results;
	}
	
	public List<DrugProduct> getAllDrugProductsByNameAndLot(LoggedInInfo loggedInInfo, Integer offset, Integer limit, String productName, String lotNumber, Integer location,  boolean availableOnly) {
		List<DrugProduct> results = drugProductDao.findByNameAndLot(offset,limit,productName, lotNumber, location, availableOnly);
		
		//--- log action ---
		if (results.size()>0) {
			String resultIds=DrugProduct.getIdsAsStringList(results);
			LogAction.addLogSynchronous(loggedInInfo,"DrugProductManager.getAllDrugProductsByName", "ids returned=" + resultIds);
		}
		
		return results;
	}
	
	public Integer getAllDrugProductsByNameAndLotCount(LoggedInInfo loggedInInfo, String productName, String lotNumber, Integer location,  boolean availableOnly) {
		Integer result = drugProductDao.findByNameAndLotCount(productName, lotNumber, location, availableOnly);
		
		LogAction.addLogSynchronous(loggedInInfo,"DrugProductManager.getAllDrugProductsByNameAndLotCount","");
		
		
		return result;
	}
	
	public List<DrugProduct> getAllDrugProductsGroupedByCode(LoggedInInfo loggedInInfo, Integer offset, Integer limit) {
		List<DrugProduct> results = drugProductDao.findAll(offset, limit);
		
		//--- log action ---
		if (results.size()>0) {
			String resultIds=DrugProduct.getIdsAsStringList(results);
			LogAction.addLogSynchronous(loggedInInfo,"DrugProductManager.getAllDrugProducts", "ids returned=" + resultIds);
		}
		
		return results;
	}
	
	public List<String> findUniqueDrugProductNames(LoggedInInfo loggedInInfo) {
		List<String> results = drugProductDao.findUniqueDrugProductNames();
		
		//--- log action ---
		if (results.size()>0) {
			LogAction.addLogSynchronous(loggedInInfo,"DrugProductManager.getUniqueDrugProductNames","");
		}
		
		return results;
	}
	
	public List<String> findUniqueDrugProductLotsByName(LoggedInInfo loggedInInfo, String productName) {
		if(productName == null || "".equals(productName)) {
			return new ArrayList<String>();
		}
		List<String> results = drugProductDao.findUniqueDrugProductLotsByName(productName);
		
		//--- log action ---
		if (results.size()>0) {
			LogAction.addLogSynchronous(loggedInInfo,"DrugProductManager.getUniqueDrugProductNames","");
		}
		
		return results;
	}
	
	
	public void deleteDrugProduct(LoggedInInfo loggedInInfo, Integer drugProductId) {
		DrugProduct drugProduct = drugProductDao.find(drugProductId);
		if(drugProduct != null && drugProduct.getDispensingEvent() != null) { 
			throw new RuntimeException("Cannot delete a dispensed drug");
		}
		
		drugProductDao.remove(drugProductId);
		
		//--- log action ---
		LogAction.addLogSynchronous(loggedInInfo,"DrugProductManager.deleteDrugProduct", "id="+drugProductId);	
	}
	
	public List<ProductLocation> getProductLocations() {
		return productLocationDao.findAll(0, ProductLocationDao.MAX_LIST_RETURN_SIZE);
	}
	
	public List<DrugProductTemplate> getDrugProductTemplates() {
		return drugProductTemplateDao.findAll(0, DrugProductTemplateDao.MAX_LIST_RETURN_SIZE);
	}
}
