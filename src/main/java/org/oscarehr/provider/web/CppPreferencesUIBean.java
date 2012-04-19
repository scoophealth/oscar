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


package org.oscarehr.provider.web;

import java.util.HashMap;
import java.util.Map;

import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.util.SpringUtils;


public class CppPreferencesUIBean {
	public static final String SOCIAL_HISTORY_POS = "cpp.social_hx.position";
	public static final String MEDICAL_HISTORY_POS = "cpp.medical_hx.position";
	public static final String ONGOING_CONCERNS_POS = "cpp.ongoing_concerns.position";
	public static final String REMINDERS_POS = "cpp.reminders.position";
	
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
	
	public static final String ENABLE = "cpp.pref.enable";
	
	protected UserPropertyDAO userPropertyDao = (UserPropertyDAO)SpringUtils.getBean("UserPropertyDAO");
	
	private String providerNo;
	
	private String socialHxPosition = "R1I1";
	private String medicalHxPosition = "R1I2";
	private String ongoingConcernsPosition = "R2I1";
	private String remindersPosition = "R2I2";
	
	private String socialHxStartDate;
	private String socialHxResDate;
	private String medHxStartDate;
	private String medHxResDate;
	private String medHxTreatment;
	private String medHxProcedureDate;
	private String ongoingConcernsStartDate;
	private String ongoingConcernsResDate;
	private String ongoingConcernsProblemStatus;
	private String remindersStartDate;
	private String remindersResDate;
	private String enable;
	
	public Map<String,String> serialize() {
		Map<String,String> map = new HashMap<String,String>();
		map.put(SOCIAL_HISTORY_POS, getSocialHxPosition());
		map.put(MEDICAL_HISTORY_POS, getMedicalHxPosition());
		map.put(ONGOING_CONCERNS_POS, getOngoingConcernsPosition());
		map.put(REMINDERS_POS, getRemindersPosition());		
		map.put(SOC_HX_START_DATE,this.getSocialHxStartDate());
		map.put(SOC_HX_RES_DATE,this.getSocialHxResDate());
		map.put(MED_HX_START_DATE,this.getMedHxStartDate());
		map.put(MED_HX_RES_DATE,this.getMedHxResDate());
		map.put(MED_HX_TREATMENT,this.getMedHxTreatment());
		map.put(MED_HX_PROCEDURE_DATE,this.getMedHxProcedureDate());
		map.put(ONGOING_START_DATE,this.getOngoingConcernsStartDate());
		map.put(ONGOING_RES_DATE,this.getOngoingConcernsResDate());
		map.put(ONGOING_PROBLEM_STATUS,this.getOngoingConcernsProblemStatus());
		map.put(REMINDERS_START_DATE,this.getRemindersStartDate());
		map.put(REMINDERS_RES_DATE,this.getRemindersResDate());
		map.put(ENABLE,this.getEnable());
		return map;
	}
	
	public void deserializeParams(Map<String,String[]> map) {
		setSocialHxPosition(map.get(SOCIAL_HISTORY_POS)[0]);
		setMedicalHxPosition(map.get(MEDICAL_HISTORY_POS)[0]);
		setOngoingConcernsPosition(map.get(ONGOING_CONCERNS_POS)[0]);
		setRemindersPosition(map.get(REMINDERS_POS)[0]);
		if(map.get(SOC_HX_START_DATE)!=null)
			setSocialHxStartDate(map.get(SOC_HX_START_DATE)[0]);
		if(map.get(SOC_HX_RES_DATE)!=null)
			setSocialHxResDate(map.get(SOC_HX_RES_DATE)[0]);
		if(map.get(MED_HX_START_DATE)!=null)
			setMedHxStartDate(map.get(MED_HX_START_DATE)[0]);
		if(map.get(MED_HX_RES_DATE)!=null)
			setMedHxResDate(map.get(MED_HX_RES_DATE)[0]);
		if(map.get(MED_HX_TREATMENT)!=null)
			setMedHxTreatment(map.get(MED_HX_TREATMENT)[0]);
		if(map.get(MED_HX_PROCEDURE_DATE)!=null)
			setMedHxProcedureDate(map.get(MED_HX_PROCEDURE_DATE)[0]);
		if(map.get(ONGOING_START_DATE)!=null)
			setOngoingConcernsStartDate(map.get(ONGOING_START_DATE)[0]);
		if(map.get(ONGOING_RES_DATE)!=null)
			setOngoingConcernsResDate(map.get(ONGOING_RES_DATE)[0]);
		if(map.get(ONGOING_PROBLEM_STATUS)!=null)
			setOngoingConcernsProblemStatus(map.get(ONGOING_PROBLEM_STATUS)[0]);
		if(map.get(REMINDERS_START_DATE)!=null)
			setRemindersStartDate(map.get(REMINDERS_START_DATE)[0]);
		if(map.get(REMINDERS_RES_DATE)!=null)
			setRemindersResDate(map.get(REMINDERS_RES_DATE)[0]);
		if(map.get(ENABLE)!=null)
			setEnable(map.get(ENABLE)[0]);
	}
	
	public void deserialize(Map<String,String> map) {
		if(map.get(SOCIAL_HISTORY_POS)!=null)
			setSocialHxPosition(map.get(SOCIAL_HISTORY_POS));
		if(map.get(MEDICAL_HISTORY_POS)!=null)
			setMedicalHxPosition(map.get(MEDICAL_HISTORY_POS));
		if(map.get(ONGOING_CONCERNS_POS)!=null)
			setOngoingConcernsPosition(map.get(ONGOING_CONCERNS_POS));
		if(map.get(REMINDERS_POS)!=null)		
			setRemindersPosition(map.get(REMINDERS_POS));
		if(map.get(SOC_HX_START_DATE)!=null)
			setSocialHxStartDate(map.get(SOC_HX_START_DATE));
		if(map.get(SOC_HX_RES_DATE)!=null)
			setSocialHxResDate(map.get(SOC_HX_RES_DATE));
		if(map.get(MED_HX_START_DATE)!=null)
			setMedHxStartDate(map.get(MED_HX_START_DATE));
		if(map.get(MED_HX_RES_DATE)!=null)
			setMedHxResDate(map.get(MED_HX_RES_DATE));
		if(map.get(MED_HX_TREATMENT)!=null)
			setMedHxTreatment(map.get(MED_HX_TREATMENT));
		if(map.get(MED_HX_PROCEDURE_DATE)!=null)
			setMedHxProcedureDate(map.get(MED_HX_PROCEDURE_DATE));
		if(map.get(ONGOING_START_DATE)!=null)
			setOngoingConcernsStartDate(map.get(ONGOING_START_DATE));
		if(map.get(ONGOING_RES_DATE)!=null)
			setOngoingConcernsResDate(map.get(ONGOING_RES_DATE));
		if(map.get(ONGOING_PROBLEM_STATUS)!=null)
			setOngoingConcernsProblemStatus(map.get(ONGOING_PROBLEM_STATUS));
		if(map.get(REMINDERS_START_DATE)!=null)
			setRemindersStartDate(map.get(REMINDERS_START_DATE));
		if(map.get(REMINDERS_RES_DATE)!=null)
			setRemindersResDate(map.get(REMINDERS_RES_DATE));
		if(map.get(ENABLE)!=null)
			setEnable(map.get(ENABLE));
	}
	
	public CppPreferencesUIBean(String providerNo) {
		this.providerNo = providerNo;
	}
	
	public void loadValues() {
		Map<String,String> dbValues = userPropertyDao.getProviderPropertiesAsMap(providerNo);
		deserialize(dbValues);
	}
	
	public void saveValues() {
		Map<String,String> props = this.serialize();
		userPropertyDao.saveProperties(providerNo,props);
	}
	
	public String getSocialHxPosition() {
    	return socialHxPosition;
    }
	public void setSocialHxPosition(String socialHxPosition) {
    	this.socialHxPosition = socialHxPosition;
    }
	public String getMedicalHxPosition() {
    	return medicalHxPosition;
    }
	public void setMedicalHxPosition(String medicalHxPosition) {
    	this.medicalHxPosition = medicalHxPosition;
    }
	public String getOngoingConcernsPosition() {
    	return ongoingConcernsPosition;
    }
	public void setOngoingConcernsPosition(String ongoingConcernsPosition) {
    	this.ongoingConcernsPosition = ongoingConcernsPosition;
    }
	public String getRemindersPosition() {
    	return remindersPosition;
    }
	public void setRemindersPosition(String remindersPosition) {
    	this.remindersPosition = remindersPosition;
    }
	
	public String getSocialHxStartDate() {
    	return socialHxStartDate;
    }

	public void setSocialHxStartDate(String socialHxStartDate) {
    	this.socialHxStartDate = socialHxStartDate;
    }

	public String getSocialHxResDate() {
    	return socialHxResDate;
    }

	public void setSocialHxResDate(String socialHxResDate) {
    	this.socialHxResDate = socialHxResDate;
    }

	public String getMedHxStartDate() {
    	return medHxStartDate;
    }

	public void setMedHxStartDate(String medHxStartDate) {
    	this.medHxStartDate = medHxStartDate;
    }

	public String getMedHxResDate() {
    	return medHxResDate;
    }

	public void setMedHxResDate(String medHxResDate) {
    	this.medHxResDate = medHxResDate;
    }

	public String getMedHxTreatment() {
    	return medHxTreatment;
    }

	public void setMedHxTreatment(String medHxTreatment) {
    	this.medHxTreatment = medHxTreatment;
    }

	public String getMedHxProcedureDate() {
    	return medHxProcedureDate;
    }

	public void setMedHxProcedureDate(String medHxProcedureDate) {
    	this.medHxProcedureDate = medHxProcedureDate;
    }

	public String getOngoingConcernsStartDate() {
    	return ongoingConcernsStartDate;
    }

	public void setOngoingConcernsStartDate(String ongoingConcernsStartDate) {
    	this.ongoingConcernsStartDate = ongoingConcernsStartDate;
    }

	public String getOngoingConcernsResDate() {
    	return ongoingConcernsResDate;
    }

	public void setOngoingConcernsResDate(String ongoingConcernsResDate) {
    	this.ongoingConcernsResDate = ongoingConcernsResDate;
    }

	public String getOngoingConcernsProblemStatus() {
    	return ongoingConcernsProblemStatus;
    }

	public void setOngoingConcernsProblemStatus(String ongoingConcernsProblemStatus) {
    	this.ongoingConcernsProblemStatus = ongoingConcernsProblemStatus;
    }

	public String getRemindersStartDate() {
    	return remindersStartDate;
    }

	public void setRemindersStartDate(String remindersStartDate) {
    	this.remindersStartDate = remindersStartDate;
    }

	public String getRemindersResDate() {
    	return remindersResDate;
    }

	public void setRemindersResDate(String remindersResDate) {
    	this.remindersResDate = remindersResDate;
    }

	public String getEnable() {
    	return enable;
    }

	public void setEnable(String enable) {
    	this.enable = enable;
    }

	public static String getPositionSelect(String currentValue) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(printOption("None","",currentValue));
		sb.append(printOption("Row 1, Column 1","R1I1",currentValue));
		sb.append(printOption("Row 1, Column 2","R1I2",currentValue));
		sb.append(printOption("Row 2, Column 1","R2I1",currentValue));
		sb.append(printOption("Row 2, Column 2","R2I2",currentValue));
		return sb.toString();
	}
	
	public static String printOption(String label, String value, String currentValue) {
		StringBuilder sb = new StringBuilder();
		String selected=new String();
		if(value.equals(currentValue)) {
			selected=" selected=\"selected\" ";
		}
		sb.append("<option value=\""+value+"\" "+selected+">"+label+"</option>\n");
		return sb.toString();
	}
	
	public static String getCheckbox(String label, String value, String currentValue) {
		String checked=new String();
		if(currentValue != null && currentValue.equals("on")) {
			checked=" checked=\"checked\" ";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<input type=\"checkbox\" name=\""+value+"\" "+checked+" />" + label);
		return sb.toString();
	}
	
}
