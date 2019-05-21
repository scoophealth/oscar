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
	
	public static final String PREVENTIONS_DSP = "cpp.preventions.display";
	public static final String DX_REGISTRY_DSP = "cpp.dx.display";
	public static final String FORMS_DSP = "cpp.forms.display";
	public static final String EFORMS_DSP = "cpp.eforms.display";
	public static final String DOCUMENTS_DSP = "cpp.docs.display";
	public static final String LABS_DSP = "cpp.labs.display";
	public static final String MEASUREMENTS_DSP = "cpp.measurements.display";
	public static final String CONSULTATIONS_DSP = "cpp.consultations.display";
	public static final String HRM_DSP = "cpp.hrm.display";
	
	public static final String ALLERGIES_DSP = "cpp.allergies.display";
	public static final String MEDICATIONS_DSP = "cpp.medications.display";
	public static final String OTHER_MEDS_DSP = "cpp.other_meds.display";
	public static final String RISK_FACTORS_DSP = "cpp.risk_factors.display";
	public static final String FAMILY_HISTORY_DSP = "cpp.family_hx.display";
	public static final String UNRESOLVED_ISSUES_DSP = "cpp.unresolved_issues.display";
	public static final String RESOLVED_ISSUES_DSP = "cpp.resolved_issues.display";
	public static final String EPISODES_DSP = "cpp.episodes.display";

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
	
	public static final String RISK_FACTORS_START_DATE = "cpp.risk_factors.start_date";
	public static final String RISK_FACTORS_RES_DATE = "cpp.risk_factors.res_date";
	public static final String OTHER_MEDS_START_DATE = "cpp.other_meds.start_date";
	public static final String OTHER_MEDS_RES_DATE = "cpp.other_meds.res_date";
	public static final String FAMILY_HISTORY_START_DATE = "cpp.family_history.start_date";
	public static final String FAMILY_HISTORY_RES_DATE = "cpp.family_history.res_date";
	public static final String FAMILY_HISTORY_TREATMENT = "cpp.family_history.treatment";
	public static final String FAMILY_HISTORY_RELATIONSHIP = "cpp.family_history.relationship";
	
	public static final String ALLERGY_START_DATE = "cpp.allergy.start_date";
	public static final String ALLERGY_SEVERITY = "cpp.allergy.severity";
	
	public static final String MEDICATION_START_DATE = "cpp.medication.start_date";
	public static final String MEDICATION_END_DATE = "cpp.medication.end_date";
	public static final String MEDICATION_QTY = "cpp.medication.qty";
	public static final String MEDICATION_REPEATS = "cpp.medication.repeats";
	public static final String MEDICATION_INSTRUCTIONS = "cpp.medication.instructions";
	
	
	public static final String ENABLE = "cpp.pref.enable";
	
	protected UserPropertyDAO userPropertyDao = (UserPropertyDAO)SpringUtils.getBean("UserPropertyDAO");
	
	private String providerNo;
	
	private String socialHxPosition = "R1I1";
	private String medicalHxPosition = "R1I2";
	private String ongoingConcernsPosition = "R2I1";
	private String remindersPosition = "R2I2";
	
	private String preventionsDisplay = "SHOW";
	private String dxRegistryDisplay = "SHOW";
	private String formsDisplay = "SHOW";
	private String eformsDisplay = "SHOW";
	private String documentsDisplay = "SHOW";
	private String labsDisplay = "SHOW";
	private String measurementsDisplay = "SHOW";
	private String consultationsDisplay = "SHOW";
	private String hrmDisplay = "SHOW";
	
	private String allergiesDisplay = "SHOW";
	private String medicationsDisplay = "SHOW";
	private String otherMedsDisplay = "SHOW";
	private String riskFactorsDisplay = "SHOW";
	private String familyHxDisplay = "SHOW";
	private String unresolvedIssuesDisplay = "SHOW";
	private String resolvedIssuesDisplay = "SHOW";
	private String episodesDisplay = "SHOW";
	
	private String socialHxStartDate = "";
	private String socialHxResDate = "";
	private String medHxStartDate = "";
	private String medHxResDate = "";
	private String medHxTreatment = "";
	private String medHxProcedureDate = "";
	private String ongoingConcernsStartDate = "";
	private String ongoingConcernsResDate = "";
	private String ongoingConcernsProblemStatus = "";
	private String remindersStartDate = "";
	private String remindersResDate = "";
	private String enable = "";
	
	private String riskFactorsStartDate = "";
	private String riskFactorsResDate = "";
	private String otherMedsStartDate = "";
	private String otherMedsResDate = "";
	private String familyHistoryStartDate = "";
	private String familyHistoryResDate = "";
	private String familyHistoryTreatment = "";
	private String familyHistoryRelationship = "";
	
	private String allergyStartDate = "";
	private String allergySeverity = "";
	
	private String medicationStartDate = "";
	private String medicationEndDate = "";
	private String medicationQty = "";
	private String medicationRepeats = "";
	private String medicationInstructions = "";
	
	public Map<String,String> serialize() {
		Map<String,String> map = new HashMap<String,String>();
		map.put(SOCIAL_HISTORY_POS, this.getSocialHxPosition());
		map.put(MEDICAL_HISTORY_POS, this.getMedicalHxPosition());
		map.put(ONGOING_CONCERNS_POS, this.getOngoingConcernsPosition());
		map.put(REMINDERS_POS, this.getRemindersPosition());
		
		map.put(PREVENTIONS_DSP, this.getPreventionsDisplay());
		map.put(DX_REGISTRY_DSP, this.getDxRegistryDisplay());
		map.put(FORMS_DSP, this.getFormsDisplay());
		map.put(EFORMS_DSP, this.getEformsDisplay());
		map.put(DOCUMENTS_DSP, this.getDocumentsDisplay());
		map.put(LABS_DSP, this.getLabsDisplay());
		map.put(MEASUREMENTS_DSP, this.getMeasurementsDisplay());
		map.put(CONSULTATIONS_DSP, this.getConsultationsDisplay());
		map.put(HRM_DSP, this.getHrmDisplay());
		
		map.put(ALLERGIES_DSP, this.getAllergiesDisplay());
		map.put(MEDICATIONS_DSP, this.getMedicationsDisplay());
		map.put(OTHER_MEDS_DSP, this.getOtherMedsDisplay());
		map.put(RISK_FACTORS_DSP, this.getRiskFactorsDisplay());
		map.put(FAMILY_HISTORY_DSP, this.getFamilyHxDisplay());
		map.put(UNRESOLVED_ISSUES_DSP, this.getUnresolvedIssuesDisplay());
		map.put(RESOLVED_ISSUES_DSP, this.getResolvedIssuesDisplay());
		map.put(EPISODES_DSP, this.getEpisodesDisplay());
		
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
		
		map.put(RISK_FACTORS_START_DATE,this.getRiskFactorsStartDate());
		map.put(RISK_FACTORS_RES_DATE,this.getRiskFactorsResDate());
		map.put(OTHER_MEDS_START_DATE,this.getOtherMedsStartDate());
		map.put(OTHER_MEDS_RES_DATE,this.getOtherMedsResDate());
		map.put(FAMILY_HISTORY_START_DATE,this.getFamilyHistoryStartDate());
		map.put(FAMILY_HISTORY_RES_DATE,this.getFamilyHistoryResDate());
		map.put(FAMILY_HISTORY_TREATMENT,this.getFamilyHistoryTreatment());
		map.put(FAMILY_HISTORY_RELATIONSHIP,this.getFamilyHistoryRelationship());
		
		map.put(ALLERGY_START_DATE, this.getAllergyStartDate());
		map.put(ALLERGY_SEVERITY, this.getAllergySeverity());
		
		map.put(MEDICATION_START_DATE, this.getMedicationStartDate());
		map.put(MEDICATION_END_DATE, this.getMedicationEndDate());
		map.put(MEDICATION_QTY, this.getMedicationQty());
		map.put(MEDICATION_REPEATS, this.getMedicationRepeats());
		map.put(MEDICATION_INSTRUCTIONS, this.getMedicationInstructions());
		return map;
	}
	
	public void deserializeParams(Map<String,String[]> map) {
		setSocialHxPosition(map.get(SOCIAL_HISTORY_POS)[0]);
		setMedicalHxPosition(map.get(MEDICAL_HISTORY_POS)[0]);
		setOngoingConcernsPosition(map.get(ONGOING_CONCERNS_POS)[0]);
		setRemindersPosition(map.get(REMINDERS_POS)[0]);
		
		setPreventionsDisplay(map.get(PREVENTIONS_DSP)[0]);
		setDxRegistryDisplay(map.get(DX_REGISTRY_DSP)[0]);
		setFormsDisplay(map.get(FORMS_DSP)[0]);
		setEformsDisplay(map.get(EFORMS_DSP)[0]);
		setDocumentsDisplay(map.get(DOCUMENTS_DSP)[0]);
		setLabsDisplay(map.get(LABS_DSP)[0]);
		setMeasurementsDisplay(map.get(MEASUREMENTS_DSP)[0]);
		setConsultationsDisplay(map.get(CONSULTATIONS_DSP)[0]);
		setHrmDisplay(map.get(HRM_DSP)[0]);
		
		setAllergiesDisplay(map.get(ALLERGIES_DSP)[0]);
		setMedicationsDisplay(map.get(MEDICATIONS_DSP)[0]);
		setOtherMedsDisplay(map.get(OTHER_MEDS_DSP)[0]);
		setRiskFactorsDisplay(map.get(RISK_FACTORS_DSP)[0]);
		setFamilyHxDisplay(map.get(FAMILY_HISTORY_DSP)[0]);
		setUnresolvedIssuesDisplay(map.get(UNRESOLVED_ISSUES_DSP)[0]);
		setResolvedIssuesDisplay(map.get(RESOLVED_ISSUES_DSP)[0]);
		setEpisodesDisplay(map.get(EPISODES_DSP)[0]);
		
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
		
		if(map.get(RISK_FACTORS_START_DATE)!=null)
			setRiskFactorsStartDate(map.get(RISK_FACTORS_START_DATE)[0]);
		if(map.get(RISK_FACTORS_RES_DATE)!=null)
			setRiskFactorsResDate(map.get(RISK_FACTORS_RES_DATE)[0]);
		
		if(map.get(OTHER_MEDS_START_DATE)!=null)
			setOtherMedsStartDate(map.get(OTHER_MEDS_START_DATE)[0]);
		if(map.get(OTHER_MEDS_RES_DATE)!=null)
			setOtherMedsResDate(map.get(OTHER_MEDS_RES_DATE)[0]);
		
		if(map.get(FAMILY_HISTORY_START_DATE)!=null)
			setFamilyHistoryStartDate(map.get(FAMILY_HISTORY_START_DATE)[0]);
		if(map.get(FAMILY_HISTORY_RES_DATE)!=null)
			setFamilyHistoryResDate(map.get(FAMILY_HISTORY_RES_DATE)[0]);
		if(map.get(FAMILY_HISTORY_TREATMENT)!=null)
			setFamilyHistoryTreatment(map.get(FAMILY_HISTORY_TREATMENT)[0]);
		if(map.get(FAMILY_HISTORY_RELATIONSHIP)!=null)
			setFamilyHistoryRelationship(map.get(FAMILY_HISTORY_RELATIONSHIP)[0]);
		
		if(map.get(ALLERGY_START_DATE) != null) {
			setAllergyStartDate(map.get(ALLERGY_START_DATE)[0]);
		}
		if(map.get(ALLERGY_SEVERITY) != null) {
			setAllergySeverity(map.get(ALLERGY_SEVERITY)[0]);
		}
		
		if(map.get(MEDICATION_START_DATE) != null) {
			setMedicationStartDate(map.get(MEDICATION_START_DATE)[0]);
		}
		if(map.get(MEDICATION_END_DATE) != null) {
			setMedicationEndDate(map.get(MEDICATION_END_DATE)[0]);
		}
		if(map.get(MEDICATION_QTY) != null) {
			setMedicationQty(map.get(MEDICATION_QTY)[0]);
		}
		if(map.get(MEDICATION_REPEATS) != null) {
			setMedicationRepeats(map.get(MEDICATION_REPEATS)[0]);
		}
		if(map.get(MEDICATION_INSTRUCTIONS) != null) {
			setMedicationInstructions(map.get(MEDICATION_INSTRUCTIONS)[0]);
		}
		
		
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Map<String,String> map = this.serialize();
		for(String key:map.keySet()) {
			String value = map.get(key);
			
			sb.append(key + "=" + value + "\n");
		}
		
		return sb.toString();
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
		
		if(map.get(PREVENTIONS_DSP)!=null)
			setPreventionsDisplay(map.get(PREVENTIONS_DSP));
		if(map.get(DX_REGISTRY_DSP)!=null)
			setDxRegistryDisplay(map.get(DX_REGISTRY_DSP));
		if(map.get(FORMS_DSP)!=null)
			setFormsDisplay(map.get(FORMS_DSP));
		if(map.get(EFORMS_DSP)!=null)
			setEformsDisplay(map.get(EFORMS_DSP));
		if(map.get(DOCUMENTS_DSP)!=null)
			setDocumentsDisplay(map.get(DOCUMENTS_DSP));
		if(map.get(LABS_DSP)!=null)
			setLabsDisplay(map.get(LABS_DSP));
		if(map.get(MEASUREMENTS_DSP)!=null)
			setMeasurementsDisplay(map.get(MEASUREMENTS_DSP));
		if(map.get(CONSULTATIONS_DSP)!=null)
			setConsultationsDisplay(map.get(CONSULTATIONS_DSP));
		if(map.get(HRM_DSP)!=null)
			setHrmDisplay(map.get(HRM_DSP));
		
		if(map.get(ALLERGIES_DSP)!=null)
			setAllergiesDisplay(map.get(ALLERGIES_DSP));
		if(map.get(MEDICATIONS_DSP)!=null)
			setMedicationsDisplay(map.get(MEDICATIONS_DSP));
		if(map.get(OTHER_MEDS_DSP)!=null)
			setOtherMedsDisplay(map.get(OTHER_MEDS_DSP));
		if(map.get(RISK_FACTORS_DSP)!=null)
			setRiskFactorsDisplay(map.get(RISK_FACTORS_DSP));
		if(map.get(FAMILY_HISTORY_DSP)!=null)
			setFamilyHxDisplay(map.get(FAMILY_HISTORY_DSP));
		if(map.get(UNRESOLVED_ISSUES_DSP)!=null)
			setUnresolvedIssuesDisplay(map.get(UNRESOLVED_ISSUES_DSP));
		if(map.get(RESOLVED_ISSUES_DSP)!=null)
			setResolvedIssuesDisplay(map.get(RESOLVED_ISSUES_DSP));
		if(map.get(EPISODES_DSP)!=null)
			setEpisodesDisplay(map.get(EPISODES_DSP));
		
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
		
		if(map.get(RISK_FACTORS_START_DATE)!=null)
			setRiskFactorsStartDate(map.get(RISK_FACTORS_START_DATE));
		if(map.get(RISK_FACTORS_RES_DATE)!=null)
			setRiskFactorsResDate(map.get(RISK_FACTORS_RES_DATE));
		
		if(map.get(OTHER_MEDS_START_DATE)!=null)
			setOtherMedsStartDate(map.get(OTHER_MEDS_START_DATE));
		if(map.get(OTHER_MEDS_RES_DATE)!=null)
			setOtherMedsResDate(map.get(OTHER_MEDS_RES_DATE));
		
		if(map.get(FAMILY_HISTORY_START_DATE)!=null)
			setFamilyHistoryStartDate(map.get(FAMILY_HISTORY_START_DATE));
		if(map.get(FAMILY_HISTORY_RES_DATE)!=null)
			setFamilyHistoryResDate(map.get(FAMILY_HISTORY_RES_DATE));
		if(map.get(FAMILY_HISTORY_TREATMENT)!=null)
			setFamilyHistoryTreatment(map.get(FAMILY_HISTORY_TREATMENT));
		if(map.get(FAMILY_HISTORY_RELATIONSHIP)!=null)
			setFamilyHistoryRelationship(map.get(FAMILY_HISTORY_RELATIONSHIP));
		
		if(map.get(ALLERGY_START_DATE)!=null)
			setAllergyStartDate(map.get(ALLERGY_START_DATE));
		if(map.get(ALLERGY_SEVERITY)!=null)
			setAllergySeverity(map.get(ALLERGY_SEVERITY));
		
		if(map.get(MEDICATION_START_DATE)!=null)
			setMedicationStartDate(map.get(MEDICATION_START_DATE));
		if(map.get(MEDICATION_END_DATE)!=null)
			setMedicationEndDate(map.get(MEDICATION_END_DATE));
		if(map.get(MEDICATION_QTY)!=null)
			setMedicationQty(map.get(MEDICATION_QTY));
		if(map.get(MEDICATION_REPEATS)!=null)
			setMedicationRepeats(map.get(MEDICATION_REPEATS));
		if(map.get(MEDICATION_INSTRUCTIONS)!=null)
			setMedicationInstructions(map.get(MEDICATION_INSTRUCTIONS));
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
	
	public String getPreventionsDisplay() {
		return preventionsDisplay;
	}
	public void setPreventionsDisplay(String preventionsDisplay) {
		this.preventionsDisplay = preventionsDisplay;
	}
	public String getDxRegistryDisplay() {
		return dxRegistryDisplay;
	}
	public void setDxRegistryDisplay(String dxRegistryDisplay) {
		this.dxRegistryDisplay = dxRegistryDisplay;
	}
	public String getFormsDisplay() {
		return formsDisplay;
	}
	public void setFormsDisplay(String formsDisplay) {
		this.formsDisplay = formsDisplay;
	}
	public String getEformsDisplay() {
		return eformsDisplay;
	}
	public void setEformsDisplay(String eformsDisplay) {
		this.eformsDisplay = eformsDisplay;
	}
	public String getDocumentsDisplay() {
		return documentsDisplay;
	}
	public void setDocumentsDisplay(String documentsDisplay) {
		this.documentsDisplay = documentsDisplay;
	}
	public String getLabsDisplay() {
		return labsDisplay;
	}
	public void setLabsDisplay(String labsDisplay) {
		this.labsDisplay = labsDisplay;
	}
	public String getMeasurementsDisplay() {
		return measurementsDisplay;
	}
	public void setMeasurementsDisplay(String measurementsDisplay) {
		this.measurementsDisplay = measurementsDisplay;
	}
	public String getConsultationsDisplay() {
		return consultationsDisplay;
	}
	public void setConsultationsDisplay(String consultationsDisplay) {
		this.consultationsDisplay = consultationsDisplay;
	}
	public String getHrmDisplay() {
		return hrmDisplay;
	}
	public void setHrmDisplay(String hrmDisplay) {
		this.hrmDisplay = hrmDisplay;
	}
	public String getAllergiesDisplay() {
    	return allergiesDisplay;
    }
	public void setAllergiesDisplay(String allergiesDisplay) {
    	this.allergiesDisplay = allergiesDisplay;
    }
	public String getMedicationsDisplay() {
    	return medicationsDisplay;
    }
	public void setMedicationsDisplay(String medicationsDisplay) {
    	this.medicationsDisplay = medicationsDisplay;
    }
	public String getOtherMedsDisplay() {
    	return otherMedsDisplay;
    }
	public void setOtherMedsDisplay(String otherMedsDisplay) {
    	this.otherMedsDisplay = otherMedsDisplay;
    }
	public String getRiskFactorsDisplay() {
    	return riskFactorsDisplay;
    }
	public void setRiskFactorsDisplay(String riskFactorsDisplay) {
    	this.riskFactorsDisplay = riskFactorsDisplay;
    }
	public String getFamilyHxDisplay() {
    	return familyHxDisplay;
    }
	public void setFamilyHxDisplay(String familyHxDisplay) {
    	this.familyHxDisplay = familyHxDisplay;
    }
	public String getUnresolvedIssuesDisplay() {
		return unresolvedIssuesDisplay;
	}
	public void setUnresolvedIssuesDisplay(String unresolvedIssuesDisplay) {
		this.unresolvedIssuesDisplay = unresolvedIssuesDisplay;
	}
	public String getResolvedIssuesDisplay() {
		return resolvedIssuesDisplay;
	}
	public void setResolvedIssuesDisplay(String resolvedIssuesDisplay) {
		this.resolvedIssuesDisplay = resolvedIssuesDisplay;
	}
	public String getEpisodesDisplay() {
		return episodesDisplay;
	}
	public void setEpisodesDisplay(String episodesDisplay) {
		this.episodesDisplay = episodesDisplay;
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
	
	public String getRiskFactorsStartDate() {
		return riskFactorsStartDate;
	}

	public void setRiskFactorsStartDate(String riskFactorsStartDate) {
		this.riskFactorsStartDate = riskFactorsStartDate;
	}

	public String getRiskFactorsResDate() {
		return riskFactorsResDate;
	}

	public void setRiskFactorsResDate(String riskFactorsResDate) {
		this.riskFactorsResDate = riskFactorsResDate;
	}
	
	public String getOtherMedsStartDate() {
		return otherMedsStartDate;
	}

	public void setOtherMedsStartDate(String otherMedsStartDate) {
		this.otherMedsStartDate = otherMedsStartDate;
	}

	public String getOtherMedsResDate() {
		return otherMedsResDate;
	}

	public void setOtherMedsResDate(String otherMedsResDate) {
		this.otherMedsResDate = otherMedsResDate;
	}

	public String getFamilyHistoryStartDate() {
		return familyHistoryStartDate;
	}

	public void setFamilyHistoryStartDate(String familyHistoryStartDate) {
		this.familyHistoryStartDate = familyHistoryStartDate;
	}

	public String getFamilyHistoryResDate() {
		return familyHistoryResDate;
	}

	public void setFamilyHistoryResDate(String familyHistoryResDate) {
		this.familyHistoryResDate = familyHistoryResDate;
	}

	public String getFamilyHistoryTreatment() {
		return familyHistoryTreatment;
	}

	public void setFamilyHistoryTreatment(String familyHistoryTreatment) {
		this.familyHistoryTreatment = familyHistoryTreatment;
	}

	public String getFamilyHistoryRelationship() {
		return familyHistoryRelationship;
	}

	public void setFamilyHistoryRelationship(String familyHistoryRelationship) {
		this.familyHistoryRelationship = familyHistoryRelationship;
	}
	
	public String getAllergyStartDate() {
		return allergyStartDate;
	}

	public void setAllergyStartDate(String allergyStartDate) {
		this.allergyStartDate = allergyStartDate;
	}

	public String getAllergySeverity() {
		return allergySeverity;
	}

	public void setAllergySeverity(String allergySeverity) {
		this.allergySeverity = allergySeverity;
	}

	public String getMedicationStartDate() {
		return medicationStartDate;
	}

	public void setMedicationStartDate(String medicationStartDate) {
		this.medicationStartDate = medicationStartDate;
	}

	public String getMedicationEndDate() {
		return medicationEndDate;
	}

	public void setMedicationEndDate(String medicationEndDate) {
		this.medicationEndDate = medicationEndDate;
	}

	public String getMedicationQty() {
		return medicationQty;
	}

	public void setMedicationQty(String medicationQty) {
		this.medicationQty = medicationQty;
	}

	public String getMedicationRepeats() {
		return medicationRepeats;
	}

	public void setMedicationRepeats(String medicationRepeats) {
		this.medicationRepeats = medicationRepeats;
	}

	public String getMedicationInstructions() {
		return medicationInstructions;
	}

	public void setMedicationInstructions(String medicationInstructions) {
		this.medicationInstructions = medicationInstructions;
	}

	public static String getPositionSelect(String currentValue) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(printOption("Hide","",currentValue));
		sb.append(printOption("Row 1, Column 1","R1I1",currentValue));
		sb.append(printOption("Row 1, Column 2","R1I2",currentValue));
		sb.append(printOption("Row 2, Column 1","R2I1",currentValue));
		sb.append(printOption("Row 2, Column 2","R2I2",currentValue));
		return sb.toString();
	}

	public static String getDisplaySelect(String currentValue) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(printOption("Hide","",currentValue));
		sb.append(printOption("Show","SHOW",currentValue));
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
