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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.oscarehr.common.dao.PreventionDao;
import org.oscarehr.common.dao.PreventionExtDao;
import org.oscarehr.common.dao.PropertyDao;
import org.oscarehr.common.interfaces.Immunization.ImmunizationProperty;
import org.oscarehr.common.model.ConsentType;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.common.model.PreventionExt;
import org.oscarehr.common.model.Property;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
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
	@Autowired
	private PropertyDao propertyDao;
	@Autowired
	private PatientConsentManager patientConsentManager;
	@Autowired
	private SecurityInfoManager securityInfoManager;
		
	private static final String HIDE_PREVENTION_ITEM = "hide_prevention_item";

	private ArrayList<String> preventionTypeList = new ArrayList<String>();

	public List<Prevention> getUpdatedAfterDate(LoggedInInfo loggedInInfo, Date updatedAfterThisDateExclusive, int itemsToReturn) {
		List<Prevention> results = preventionDao.findByUpdateDate(updatedAfterThisDateExclusive, itemsToReturn);
		patientConsentManager.filterProviderSpecificConsent(loggedInInfo, results);
		LogAction.addLogSynchronous(loggedInInfo, "PreventionManager.getUpdatedAfterDate", "updatedAfterThisDateExclusive=" + updatedAfterThisDateExclusive);

		return (results);
	}

	public List<Prevention> getByDemographicIdUpdatedAfterDate(LoggedInInfo loggedInInfo, Integer demographicId, Date updatedAfterThisDateExclusive) {
		List<Prevention> results = new ArrayList<Prevention>();
		ConsentType consentType = patientConsentManager.getProviderSpecificConsent(loggedInInfo);
		if (patientConsentManager.hasPatientConsented(demographicId, consentType)) {
			results = preventionDao.findByDemographicIdAfterDatetimeExclusive(demographicId, updatedAfterThisDateExclusive);
			LogAction.addLogSynchronous(loggedInInfo, "PreventionManager.getByDemographicIdUpdatedAfterDate", "demographicId="+demographicId+" updatedAfterThisDateExclusive=" + updatedAfterThisDateExclusive);
		}
		return (results);
	}

	public Prevention getPrevention(LoggedInInfo loggedInInfo, Integer id) {
		Prevention result = preventionDao.find(id);

		//--- log action ---
		if (result != null) {
			LogAction.addLogSynchronous(loggedInInfo, "PreventionManager.getPrevention", "id=" + id);
		}

		return (result);
	}

	public List<PreventionExt> getPreventionExtByPrevention(LoggedInInfo loggedInInfo, Integer preventionId) {
		List<PreventionExt> results = preventionExtDao.findByPreventionId(preventionId);

		LogAction.addLogSynchronous(loggedInInfo, "PreventionManager.getPreventionExtByPrevention", "preventionId=" + preventionId);

		return (results);
	}

	public ArrayList<String> getPreventionTypeList() {
		if (preventionTypeList.isEmpty()) {
			PreventionDisplayConfig pdc = PreventionDisplayConfig.getInstance();
			for (HashMap<String, String> prevTypeHash : pdc.getPreventions()) {
				if (prevTypeHash != null && StringUtils.filled(prevTypeHash.get("name"))) {
					preventionTypeList.add(prevTypeHash.get("name").trim());
				}
			}
		}
		return preventionTypeList;
	}
	
	public ArrayList<HashMap<String,String>> getPreventionTypeDescList() {
		PreventionDisplayConfig pdc = PreventionDisplayConfig.getInstance();
		ArrayList<HashMap<String,String>> preventionTypeDescList = pdc.getPreventions();
		
		return preventionTypeDescList;
	}
	
	public boolean isHidePrevItemExist() {
		List<Property> props = propertyDao.findByName(HIDE_PREVENTION_ITEM);
		if(props.size()>0){
			return true;
		}		
		return false;
	}
	
	public boolean hideItem(String item) {
		String itemsToRemove = null;
		Property p = propertyDao.checkByName(HIDE_PREVENTION_ITEM);
		
		if(p!=null && p.getValue()!=null){
			itemsToRemove = p.getValue();
			List<String> items = Arrays.asList(itemsToRemove.split("\\s*,\\s*"));
			for(String i:items){
				if(i.equals(item)){
					return true;
				}
			}
		}
		return false;
	}
	
	public static String getCustomPreventionItems() {
		String itemsToRemove = "";
		PropertyDao propertyDao = (PropertyDao)SpringUtils.getBean("propertyDao");
		Property p = propertyDao.checkByName(HIDE_PREVENTION_ITEM);
		if(p!=null && p.getValue()!=null){
		itemsToRemove = p.getValue();
		}
		return itemsToRemove;
	}
	
	public void addCustomPreventionItems(String items){
		boolean propertyExists = isHidePrevItemExist();
		if(propertyExists){
			Property p = propertyDao.checkByName(HIDE_PREVENTION_ITEM);
			p.setValue(items);
			propertyDao.merge(p);
		}else{
			Property x = new Property();
			x.setName("hide_prevention_item");
			x.setValue(items);
			propertyDao.persist(x);
		}
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

	/**
	 * programId is ignored for now as oscar doesn't support it yet.
	 */
	public List<Prevention> getPreventionsByProgramProviderDemographicDate(LoggedInInfo loggedInInfo, Integer programId, String providerNo, Integer demographicId, Calendar updatedAfterThisDateExclusive, int itemsToReturn) {
		List<Prevention> results = preventionDao.findByProviderDemographicLastUpdateDate(providerNo, demographicId, updatedAfterThisDateExclusive.getTime(), itemsToReturn);

		LogAction.addLogSynchronous(loggedInInfo, "PreventionManager.getUpdatedAfterDate", "programId=" + programId + ", providerNo=" + providerNo + ", demographicId=" + demographicId + ", updatedAfterThisDateExclusive=" + updatedAfterThisDateExclusive.getTime());

		return (results);
	}
	
	public List<Prevention> getPreventionsByDemographicNo(LoggedInInfo loggedInInfo, Integer demographicNo) {
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_prevention", SecurityInfoManager.READ, demographicNo)) {
			throw new RuntimeException("missing required security object (_prevention)");
		}
		
		List<Prevention> results = preventionDao.findUniqueByDemographicId(demographicNo);
		
		LogAction.addLogSynchronous(loggedInInfo, "PreventionManager.getPreventionsByDemographicNo", "demographicNo=" + demographicNo);

		return (results);
	}
	
	public List<Prevention> getImmunizationsByDemographic(LoggedInInfo loggedInInfo, Integer demographicNo) {

		List<Prevention> results = getPreventionsByDemographicNo(loggedInInfo, demographicNo);
		
		// the ImmunizationInterface should really be located in Oscar/Common.
		List<Prevention> immunizations = new ArrayList<Prevention>();
		
		if(results == null) {
			results = Collections.emptyList();
		}
		
		for(Prevention prevention : results) {
			
			// this sets the PreventionExt values for use in the Immunization interface.
			prevention.setPreventionExtendedProperties();
			
			if(prevention.isImmunization() || prevention.getImmunizationProperty(ImmunizationProperty.dose) != null) {
				// this splits out a new Immunizations list of preventions.
				immunizations.add(prevention);
			}
		}
		
		LogAction.addLogSynchronous(loggedInInfo, "PreventionManager.getImmunizationsByDemographic", "demographicNo=" + demographicNo);

		return immunizations;
	}
	
}
