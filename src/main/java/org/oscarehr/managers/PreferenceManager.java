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

import java.text.SimpleDateFormat;
import java.util.List;

import org.oscarehr.casemgmt.model.CaseManagementNoteExt;
import org.oscarehr.common.model.Property;
import org.oscarehr.util.LoggedInInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PreferenceManager {
	
	@Autowired
	private ProviderManager2 providerManager;
	
	public static final String SOCHX = "SocHistory";
	public static final String MEDHX = "MedHistory";
	public static final String CONCERNS = "Concerns";
	public static final String REMINDERS = "Reminders";
	
	//NEW - summary.item.custom.display
	public static final String CUSTOM_SUMMARY_ENABLE = "cpp.pref.enable";
	
	
	public static final String OLD_SOCIAL_HISTORY_POS = "cpp.social_hx.position";
	public static final String OLD_MEDICAL_HISTORY_POS = "cpp.medical_hx.position";
	public static final String OLD_ONGOING_CONCERNS_POS = "cpp.ongoing_concerns.position";
	public static final String OLD_REMINDERS_POS = "cpp.reminders.position";
	
	/*
	 * 
	 * if .position dosne't exist = on
	 * if .position exists and not equal "" = on
	 * ignore the R* position eg: R1I1 R1I2 R2I1 R2I2
	 * 
	 * so setting position in old ui will have not effect on the new ui
	 * and when setting position in new ui it will have no effect on the old ui
	 *  
	 * 
	 */
	public static final String SOC_HX_POS = "summary.item.social_hx.position";
	public static final String MED_HX_POS = "summary.item.med_hx.position";
	public static final String ONGOING_POS = "summary.item.ongoing_concerns.position";
	public static final String REMINDERS_POS = "summary.item.reminders.position";
	
	public static final String SOC_HX_START_DATE = "cpp.social_hx.start_date";
	public static final String SOC_HX_RES_DATE = "cpp.social_hx.res_date";
	public static final String MED_HX_START_DATE = "cpp.med_hx.start_date";
	public static final String MED_HX_RES_DATE = "cpp.med_hx.res_date";
	public static final String MED_HX_TREATMENT = "cpp.med_hx.treatment";
	public static final String MED_HX_PROCEDURE_DATE = "cpp.med_hx.procedure_date";
	public static final String ONGOING_START_DATE = "cpp.ongoing_concerns.start_date";
	public static final String ONGOING_RES_DATE = "cpp.ongoing_concerns.res_date";
	public static final String ONGOING_PROBLEM_STATUS = "cpp.ongoing_concerns.problem_status";
	public static final String REMINDERS_START_DATE = "cpp.reminders.start_date";
	public static final String REMINDERS_RES_DATE = "cpp.reminders.res_date";
		
	public static final String PREVENTION_POS = "summary.item.prevention.position";
	public static final String FAM_HX_POS = "summary.item.famhx.position";
	public static final String RISK_FACTORS_POS = "summary.item.riskfactors.position";
	public static final String ALLERGIES_POS = "summary.item.allergies.position";
	public static final String MEDS_POS = "summary.item.meds.position";
	public static final String OTHER_MEDS_POS = "summary.item.othermeds.position";
	public static final String ASSESSMENTS_POS = "summary.item.assessments.position";
	public static final String INCOMING_POS = "summary.item.incoming.position";
	public static final String DS_SUPPORT_POS = "summary.item.dssupport.position";
	public static final String DISEASES_POS = "summary.item.diseases.position";

	
	public boolean displaySummaryItem(LoggedInInfo loggedInInfo, String item){
		if(isCustomSummaryEnabled(loggedInInfo)){			
			List<Property> results = providerManager.getProviderProperties(loggedInInfo, loggedInInfo.getLoggedInProviderNo(), item);
			
			if(results.size()>0){
				String value = null;
				
				for(Property result:results){
					value = result.getValue();
				}
				
				if(value.isEmpty() || value.equals("off")){
					return false;	
				}
			}else{
				//check if the old cpp position property exist
				return isOldCppPosition(loggedInInfo, item);					
			}
		}
		
		return true;
	}
	
	private boolean isOldCppPosition(LoggedInInfo loggedInInfo, String property){
		if(property.equals(SOC_HX_POS)){
			return displaySummaryItem(loggedInInfo, OLD_SOCIAL_HISTORY_POS);
		}else if(property.equals(MED_HX_POS)){
			return displaySummaryItem(loggedInInfo, OLD_MEDICAL_HISTORY_POS);
		}else if(property.equals(ONGOING_POS)){
			return displaySummaryItem(loggedInInfo, OLD_ONGOING_CONCERNS_POS);
		}else if(property.equals(REMINDERS_POS)){
			return displaySummaryItem(loggedInInfo, OLD_REMINDERS_POS);
		}
		
		return true;
	}

	//TODO: look at appending the spaces 
	public String getCppExtsItem(LoggedInInfo loggedInInfo, List<CaseManagementNoteExt> noteExtList, String issueCode){	

		StringBuilder sb = new StringBuilder();
	
		if(issueCode.equals(CONCERNS)){
			if(isCustomCppItemOn(loggedInInfo, ONGOING_START_DATE)){
				sb.append("Start Date:" + getNoteExt("Start Date", noteExtList));
			}	
			
			if(isCustomCppItemOn(loggedInInfo, ONGOING_RES_DATE)){
				sb.append(" Resolution Date:" + getNoteExt("Resolution Date", noteExtList));
			}
			
			if(isCustomCppItemOn(loggedInInfo, ONGOING_PROBLEM_STATUS)){
				sb.append(" Problem Status:" + getNoteExt("Problem Status", noteExtList));
			}
			
		}else if(issueCode.equals(MEDHX)){
			if(isCustomCppItemOn(loggedInInfo, MED_HX_START_DATE)){
				sb.append("Start Date:" + getNoteExt("Start Date", noteExtList));
			}
			
			if(isCustomCppItemOn(loggedInInfo, MED_HX_RES_DATE)){
				sb.append(" Resolution Date:" + getNoteExt("Resolution Date", noteExtList));
			}
			
			if(isCustomCppItemOn(loggedInInfo, MED_HX_TREATMENT)){
				sb.append(" Treatment:" + getNoteExt("Treatment", noteExtList));
			}
			
			if(isCustomCppItemOn(loggedInInfo, MED_HX_PROCEDURE_DATE)){
				sb.append(" Procedure Date:" + getNoteExt("Procedure Date", noteExtList));
			}
			
		}else if(issueCode.equals(SOCHX)){
			if(isCustomCppItemOn(loggedInInfo, SOC_HX_START_DATE)){
				sb.append("Start Date:" + getNoteExt("Start Date", noteExtList));
			}
			
			if(isCustomCppItemOn(loggedInInfo, SOC_HX_RES_DATE)){
				sb.append(" Resolution Date:" + getNoteExt("Resolution Date", noteExtList));
			}
			
		}else if(issueCode.equals(REMINDERS)){
			if(isCustomCppItemOn(loggedInInfo, REMINDERS_START_DATE)){
				sb.append("Start Date:" + getNoteExt("Start Date", noteExtList));
			}
			
			if(isCustomCppItemOn(loggedInInfo, REMINDERS_RES_DATE)){
				sb.append(" Resolution Date:" + getNoteExt("Resolution Date", noteExtList));
			}
		}

		if (sb.length() > 0) {
			sb.insert(0, " (");
			sb.append(")");
		}
		
		return sb.toString();
	}
	
	static String getNoteExt(String key, List<CaseManagementNoteExt> lcme) {
		for (CaseManagementNoteExt cme : lcme) {
			if (cme.getKeyVal().equals(key)) {
				String val = null;

				if (key.contains(" Date")) {
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					val = formatter.format(cme.getDateValue());
				} else {
					val = cme.getValue();
				}
				return val;
			}
		}
		return "";
	}
	

	public boolean isCppItem(String issueCode){
		if(issueCode.equals(SOCHX) || issueCode.equals(MEDHX) || issueCode.equals(CONCERNS) || issueCode.equals(REMINDERS)){
			return true;
		}
		return false;
	}
	
	
	public boolean isCustomSummaryEnabled(LoggedInInfo loggedInInfo){
		List<Property> results = providerManager.getProviderProperties(loggedInInfo, loggedInInfo.getLoggedInProviderNo(), CUSTOM_SUMMARY_ENABLE);
		
		if(results.size()>0){
			String value = null;
			
			for(Property result:results){
				value = result.getValue();
			}
			
			if(value.equals("on")){
				return true;	
			}
		}
		
		return false;		
	}
	
	private boolean isCustomCppItemOn(LoggedInInfo loggedInInfo, String propertyName){
		List<Property> results = providerManager.getProviderProperties(loggedInInfo, loggedInInfo.getLoggedInProviderNo(), propertyName);
		
		if(results.size()>0){
			String value = null;
			
			for(Property result:results){
				value = result.getValue();
			}
			
			if(value.equals("on")){
				return true;	
			}
		}
		return false;
	}
	
	public String getProviderPreference(LoggedInInfo loggedInInfo, String propertyName){
		List<Property> results = providerManager.getProviderProperties(loggedInInfo, loggedInInfo.getLoggedInProviderNo(), propertyName);
		
		String value = null;
		
		if(results.size()>0){
			
			for(Property result:results){
				value = result.getValue();
			}

			return value;
		}
		
		return value;		
	}
	
}
