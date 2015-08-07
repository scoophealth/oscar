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
package org.oscarehr.integration.born;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.xmlbeans.XmlCalendar;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlValidationError;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.EFormDao;
import org.oscarehr.common.dao.EFormDataDao;
import org.oscarehr.common.dao.EFormValueDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.EForm;
import org.oscarehr.common.model.EFormData;
import org.oscarehr.common.model.EFormValue;
import org.oscarehr.common.model.Provider;
import org.oscarehr.sharingcenter.dao.ClinicInfoDao;
import org.oscarehr.sharingcenter.model.ClinicInfoDataObject;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.util.StringUtils;
import ca.bornontario.wb.BORNWBBatch;
import ca.bornontario.wb.BORNWBBatchDocument;
import ca.bornontario.wb.CountryProvince;
import ca.bornontario.wb.FormVersion;
import ca.bornontario.wb.Gender;
import ca.bornontario.wb.NDDS;
import ca.bornontario.wb.NDDS04M;
import ca.bornontario.wb.NDDS06M;
import ca.bornontario.wb.NDDS09M;
import ca.bornontario.wb.NDDS12M;
import ca.bornontario.wb.NDDS15M;
import ca.bornontario.wb.NDDS18M;
import ca.bornontario.wb.NDDS1M2M;
import ca.bornontario.wb.NDDS2Y;
import ca.bornontario.wb.NDDS30M;
import ca.bornontario.wb.NDDS3Y;
import ca.bornontario.wb.NDDS4Y;
import ca.bornontario.wb.NDDS5Y;
import ca.bornontario.wb.NDDS6Y;
import ca.bornontario.wb.NDDSResponseCodes;
import ca.bornontario.wb.PatientInfo;
import ca.bornontario.wb.ProblemsAndPlans;
import ca.bornontario.wb.RBR;
import ca.bornontario.wb.RBRM01;
import ca.bornontario.wb.RBRM02;
import ca.bornontario.wb.RBRM02M04M06;
import ca.bornontario.wb.RBRM04;
import ca.bornontario.wb.RBRM06;
import ca.bornontario.wb.RBRM09;
import ca.bornontario.wb.RBRM09M1213;
import ca.bornontario.wb.RBRM09M1213M15;
import ca.bornontario.wb.RBRM1213;
import ca.bornontario.wb.RBRM15;
import ca.bornontario.wb.RBRM18;
import ca.bornontario.wb.RBRW01;
import ca.bornontario.wb.RBRW01W02M01;
import ca.bornontario.wb.RBRW02;
import ca.bornontario.wb.RBRY23;
import ca.bornontario.wb.RBRY23Y45;
import ca.bornontario.wb.RBRY45;
import ca.bornontario.wb.SUMRPTMARKERS;
import ca.bornontario.wb.YesNoUnknown;

/**
 * For KidConnect 0-5 project (BORN)
 * 
 * This class basically grabs all the forms for a demographic..and generates XML documents for that patient.
 * 
 * @author marc
 *
 */
public class BORNWbXmlGenerator {

	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");
	private SimpleDateFormat visitDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
	private SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	private DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	private ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	private EFormValueDao eformValueDao = SpringUtils.getBean(EFormValueDao.class);
	private EFormDataDao eformDataDao = SpringUtils.getBean(EFormDataDao.class);
	private EFormDao eformDao = SpringUtils.getBean(EFormDao.class);

	/**
	 * In the constructor a map is built of all the eforms we use to generate the XML.
	 * NDDS, Rourke, and Summary Report Markers
	 */
	private Map<String, EForm> eformMap = new HashMap<String, EForm>();

	/**
	 * holds the eform values for the latest eform data for patient.
	 */
	private Map<String, Map<String, EFormValue>> eformValuesMap = new HashMap<String, Map<String, EFormValue>>();

	/**
	 * holds the fdid reference for the latest eform data for each eform
	 */
	private Map<String, Integer> eformFdidMap = new HashMap<String, Integer>();

	private Integer demographicNo = null;
	
	public BORNWbXmlGenerator() {
		OscarProperties props = OscarProperties.getInstance();
		eformMap.put("RBR", eformDao.findByName(props.getProperty("born_eform_rourke", "Rourke Baby Record")));
		eformMap.put("SUMRPT", eformDao.findByName(props.getProperty("born_eform_sumrptmarkers", "Summary Report: Well Baby Visit")));
		eformMap.put("NDDS1M2M", eformDao.findByName(props.getProperty("born_eform_ndds1m2m", "NDDS 1&2 Months")));
		eformMap.put("NDDS4M", eformDao.findByName(props.getProperty("born_eform_ndds4m", "NDDS 4 Months")));
		eformMap.put("NDDS6M", eformDao.findByName(props.getProperty("born_eform_ndds6m", "NDDS 6 Months")));
		eformMap.put("NDDS9M", eformDao.findByName(props.getProperty("born_eform_ndds9m", "NDDS 9 Months")));
		eformMap.put("NDDS12M", eformDao.findByName(props.getProperty("born_eform_ndds12m", "NDDS 12 Months")));
		eformMap.put("NDDS15M", eformDao.findByName(props.getProperty("born_eform_ndds15m", "NDDS 15 Months")));
		eformMap.put("NDDS18M", eformDao.findByName(props.getProperty("born_eform_ndds18m", "NDDS 18 Months")));
		eformMap.put("NDDS24M", eformDao.findByName(props.getProperty("born_eform_ndds24m", "NDDS 24 Months")));
		eformMap.put("NDDS30M", eformDao.findByName(props.getProperty("born_eform_ndds30m", "NDDS 30 Months")));
		eformMap.put("NDDS3Y", eformDao.findByName(props.getProperty("born_eform_ndds3y", "NDDS 3 Years")));
		eformMap.put("NDDS4Y", eformDao.findByName(props.getProperty("born_eform_ndds4y", "NDDS 4 Years")));
		eformMap.put("NDDS5Y", eformDao.findByName(props.getProperty("born_eform_ndds5y", "NDDS 5 Years")));
		eformMap.put("NDDS6Y", eformDao.findByName(props.getProperty("born_eform_ndds6y", "NDDS 6 Years")));
	}

	public void init(Integer demographicNo) {
		this.demographicNo = demographicNo;
		/*
		 * for every eform, we initialize the latest fdid and eform value maps.
		 */
		for (String name : eformMap.keySet()) {
			EForm eform = eformMap.get(name);
			if (eform != null) {
				Integer fdid = getMaxFdid(eform.getId(), demographicNo);
				if (fdid != null) {
					eformFdidMap.put(name, fdid);
					List<EFormValue> values = eformValueDao.findByFormDataId(fdid);
					Map<String, EFormValue> valueMap = new HashMap<String, EFormValue>();
					for (EFormValue val : values) {
						valueMap.put(val.getVarName(), val);

					}
					eformValuesMap.put(name, valueMap);
					MiscUtils.getLogger().info("found eform values for " + eform.getFormName());
				}
			}
		}

	}

	/**
	 * Main call into the class to stream out the xml for a patient.
	 */
	public boolean addToStream(Writer os, XmlOptions opts, boolean useClinicInfoForOrganizationId) throws IOException {
		BORNWBBatchDocument bornBatchDocument = BORNWBBatchDocument.Factory.newInstance();
		BORNWBBatch bornBatch = bornBatchDocument.addNewBORNWBBatch();
		PatientInfo patientInfo = bornBatch.addNewPatientInfo();

		if (eformFdidMap.isEmpty()) {
			return false;
		}

		//populate patient info (comes from rourke and chart)
		populatePatientInfo(patientInfo, demographicNo, useClinicInfoForOrganizationId);

		//populate Rourke Baby Record data
		if (this.eformValuesMap.get("RBR") != null) {
			populateRBR(patientInfo.addNewRBR());
		}

		List<Integer> nddsFdids = new ArrayList<Integer>();
		for(String key: this.eformMap.keySet()) {
			if(key.startsWith("NDDS")) {
				Integer fdid = this.eformFdidMap.get(key);
				if(fdid != null) {
					nddsFdids.add(fdid);
				}
			}
		}
		
		if(!nddsFdids.isEmpty()) {
			//populate NDDS data from the 13 eforms (30 month one missing)
			populateNdds(patientInfo.addNewNDDS(),nddsFdids);
		}

		//populate summary report markers (eform doesn't exist yet)
		if (this.eformValuesMap.get("SUMRPT") != null) {
			populateSumRptMarkers(patientInfo.addNewSUMRPTMARKERS());
		}

		//business validation
		if (!isAgeLessThan7y(patientInfo)) {
			MiscUtils.getLogger().warn("Child is over 6years old..skipping");
			return false;
		}

		if (patientInfo.getGestationalAge() < 126 || patientInfo.getGestationalAge() > 315) {
			MiscUtils.getLogger().warn("gestational age not between 126 and 315.");
			return false;
		}

		if (!passBirthweightValidation(patientInfo)) {
			MiscUtils.getLogger().warn("failed birthweight validation");
			return false;
		}

		//TODO: xml validation - how can we report the problem better??
		XmlOptions m_validationOptions = new XmlOptions();
		ArrayList<Object> validationErrors = new ArrayList<Object>();
		m_validationOptions.setErrorListener(validationErrors);
		if (!bornBatchDocument.validate(m_validationOptions)) {
			MiscUtils.getLogger().warn("BORN WB validation failed");
			for (Object o : validationErrors) {
				XmlValidationError xve = (XmlValidationError) o;
				MiscUtils.getLogger().warn(o);
			}
		}

		//write to stream provided
		bornBatchDocument.save(os, opts);

		MiscUtils.getLogger().debug("Wrote out batch document for patient " + demographicNo);
		return true;
	}

	private void populateRBR(RBR rbr) {
		
		EFormData efd = eformDataDao.find(eformFdidMap.get("RBR"));
		if (efd != null) {
			Date date = new Date();
			date.setTime(efd.getFormDate().getTime() + efd.getFormTime().getTime());
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			rbr.setLastUpdateDate(cal);
		}

		rbr.setSetID(eformFdidMap.get("RBR"));
		rbr.setVersionID(1);

		if (hasValue("RBR", "visit_date_1w")) {
			populateRBRW01(rbr.addNewRBRW01());
		}
		if (hasValue("RBR", "visit_date_2w")) {
			populateRBRW02(rbr.addNewRBRW02());
		}
		if (hasValue("RBR", "visit_date_1w") || hasValue("RBR", "visit_date_2w") || hasValue("RBR", "visit_date_1m")) {
			populateRBRW01W02M01(rbr.addNewRBRW01W02M01());
		}
		if (hasValue("RBR", "visit_date_1m")) {
			populateRBRM01(rbr.addNewRBRM01());
		}

		if (hasValue("RBR", "visit_date_2m")) {
			populateRBRM02(rbr.addNewRBRM02());
		}
		if (hasValue("RBR", "visit_date_4m")) {
			populateRBRM04(rbr.addNewRBRM04());
		}
		if (hasValue("RBR", "visit_date_6m")) {
			populateRBRM06(rbr.addNewRBRM06());
		}
		if (hasValue("RBR", "visit_date_2m") || hasValue("RBR", "visit_date_4m") || hasValue("RBR", "visit_date_6m")) {
			populateRBRM02M04M06(rbr.addNewRBRM02M04M06());
		}

		if (hasValue("RBR", "visit_date_9m")) {
			populateRBRM09(rbr.addNewRBRM09());
		}
		if (hasValue("RBR", "visit_date_9m") || hasValue("RBR", "visit_date_12m")) {
			populateRBRM09M1213(rbr.addNewRBRM09M1213());
		}
		if (hasValue("RBR", "visit_date_12m")) {
			populateRBRM1213(rbr.addNewRBRM1213());
		}
		if (hasValue("RBR", "visit_date_15m")) {
			populateRBRM15(rbr.addNewRBRM15());
		}
		if (hasValue("RBR", "visit_date_9m") || hasValue("RBR", "visit_date_12m") || hasValue("RBR", "visit_date_15m")) {
			populateRBRM09M1213M15(rbr.addNewRBRM09M1213M15());
		}

		if (hasValue("RBR", "visit_date_18m")) {
			populateRBRM18(rbr.addNewRBRM18());
		}

		if (hasValue("RBR", "visit_date_2y")) {
			populateRBRY23(rbr.addNewRBRY23());
		}
		if (hasValue("RBR", "visit_date_2y") || hasValue("RBR", "visit_date_4y")) {
			populateRBRY23Y45(rbr.addNewRBRY23Y45());
		}
		if (hasValue("RBR", "visit_date_4y")) {
			populateRBRY45(rbr.addNewRBRY45());
		}

	}
	
	private Calendar generateNonTimestampDate(String strDate) {
		String[] parts = strDate.split("-");
		if(!(parts.length==3)) {
			return null;
		}
		//XmlDateTime xmlDateTime = XmlDateTime.Factory.newInstance();
		Calendar xmlCalendar = new XmlCalendar();
		xmlCalendar.set(Calendar.YEAR, Integer.parseInt(parts[0]));
		xmlCalendar.set(Calendar.MONTH, Integer.parseInt(parts[1])-1);
		xmlCalendar.set(Calendar.DATE, Integer.parseInt(parts[2]));
		//xmlDateTime.setCalendarValue(xmlCalendar);
		
		return xmlCalendar;
	}
	

	private void populateRBRW01(RBRW01 rbr) {
		rbr.setFormVersion(FormVersion.X_2014);

		//visit date
		EFormValue date = this.eformValuesMap.get("RBR").get("visit_date_1w");
		if (date != null && date.getVarValue() != null && !date.getVarValue().isEmpty()) {
			rbr.setVisitDate(generateNonTimestampDate(date.getVarValue()));
		}

		if (stringToBigDecimal(getRourkeStrValue("headcirc_1w")) != null) {
			rbr.setHeadCirc(stringToBigDecimal(getRourkeStrValue("headcirc_1w")));
		}
		if (stringToBigDecimal(getRourkeStrValue("height_1w")) != null) {
			rbr.setHeight(stringToBigDecimal(getRourkeStrValue("height_1w")));
		}

		Integer weight = null;
		if ((weight = getRourkeIntValue("weight_1w")) != null) {
			rbr.setWeight(weight);
		}

		
		rbr.setInvestigationsImmunizationRecordVaccines(getDiscussedConcernNotDiscussed("recordVaccines_1w"));

		rbr.setInvestigationsImmunizationHBsAgPosParentVaccine1(getDiscussedConcernNotDiscussed("hepbimmuneglobulin_1w"));
		rbr.setInvestigationsImmunizationHemoglobinopathyScreen(getDiscussedConcernNotDiscussed("hemoglobinopathy_1w"));
		rbr.setInvestigationsImmunizationNBS(getDiscussedConcernNotDiscussed("newbornscreen_1w"));
		rbr.setInvestigationsImmunizationUNHS(getDiscussedConcernNotDiscussed("unhs_1w"));

		
		rbr.setNutritionBreastfeeding(getDiscussedConcernNotDiscussed("breastfeeding_1w"));
		rbr.setNutritionFormulaFeeding(getDiscussedConcernNotDiscussed("formulafeeding_1w"));
		rbr.setNutritionStoolUrine(getDiscussedConcernNotDiscussed("stoolurine_1w"));
		rbr.setVitaminD400UIDay(getDiscussedConcernNotDiscussed("vitaminD_1w"));

		//the yes no stuff
		rbr.setNutritionBreastfeedingYesNo(getYesNoUnknown("breastfeeding_1w_yes_no"));
		rbr.setNutritionFormulaFeedingYesNo(getYesNoUnknown("formulafeeding_1w_yes_no"));
		rbr.setSupplementationWaterYesNo(getYesNoUnknown("supplementationWater_1w_yes_no"));
		rbr.setSupplementationOtherFluidsYesNo(getYesNoUnknown("supplementationOther_1w_yes_no"));
		
		if (getRourkeStrValue("parentConcerns1w") != null) {
			rbr.setParentalCaregiverConcerns(getRourkeStrValue("parentConcerns1w"));
		}

		rbr.setPhysicalExamEarsHearingScreening(getDiscussedConcernNotDiscussed("ears_1w"));
		rbr.setPhysicalExamEyes(getDiscussedConcernNotDiscussed("eyes_1w"));
		rbr.setPhysicalExamFemoralPulses(getDiscussedConcernNotDiscussed("femoralpulses_1w"));
		rbr.setPhysicalExamFontanelles(getDiscussedConcernNotDiscussed("fontanelles_1w"));
		rbr.setPhysicalExamHeart(getDiscussedConcernNotDiscussed("heartlungs_1w"));
		rbr.setPhysicalExamHips(getDiscussedConcernNotDiscussed("hips_1w"));
		rbr.setPhysicalExamMaleUrinary(getDiscussedConcernNotDiscussed("maleurinary_1w"));
		rbr.setPhysicalExamMuscleTone(getDiscussedConcernNotDiscussed("muscletone_1w"));
		rbr.setPhysicalExamPatencyOfAnus(getDiscussedConcernNotDiscussed("patencyofanus_1w"));
		rbr.setPhysicalExamSkin(getDiscussedConcernNotDiscussed("skin_1w"));
		rbr.setPhysicalExamTesticles(getDiscussedConcernNotDiscussed("testicles_1w"));
		rbr.setPhysicalExamTongueMobility(getDiscussedConcernNotDiscussed("tonguemobility_1w"));
		rbr.setPhysicalExamUmbilicus(getDiscussedConcernNotDiscussed("umbilicus_1w"));

		//signature
		if (getRourkeStrValue("signature1w") != null) {
			rbr.setSignature(getRourkeStrValue("signature1w"));
		}

		//problems and plans
		if (getRourkeStrValue("problemsPlans1w") != null) {
			rbr.setProblemsAndPlansOther(getRourkeStrValue("problemsPlans1w"));
		}
		EFormValue resource = this.eformValuesMap.get("RBR").get("problemsPlans1wRes");
		if (resource != null && resource.getVarValue() != null & !resource.getVarValue().isEmpty()) {
			for (String val : resource.getVarValue().split(",")) {
				ProblemsAndPlans pp = rbr.addNewProblemsAndPlans();
				pp.setStringValue(val);
			}
		}

	}

	private void populateRBRW02(RBRW02 rbr) {
		//visit date
		EFormValue date = this.eformValuesMap.get("RBR").get("visit_date_2w");
		if (date != null && date.getVarValue() != null && !date.getVarValue().isEmpty()) {
			rbr.setVisitDate(generateNonTimestampDate(date.getVarValue()));
		}

		rbr.setFormVersion(FormVersion.X_2014);

		if (stringToBigDecimal(getRourkeStrValue("headcirc_2w")) != null) {
			rbr.setHeadCirc(stringToBigDecimal(getRourkeStrValue("headcirc_2w")));
		}
		if (stringToBigDecimal(getRourkeStrValue("height_2w")) != null) {
			rbr.setHeight(stringToBigDecimal(getRourkeStrValue("height_2w")));
		}

		Integer weight = null;
		if ((weight = getRourkeIntValue("weight_2w")) != null) {
			rbr.setWeight(weight);
		}

		rbr.setInvestigationsImmunizationRecordVaccines(getDiscussedConcernNotDiscussed("recordVaccines_2w"));

		rbr.setNutritionBreastfeeding(getDiscussedConcernNotDiscussed("breastfeeding_2w"));
		rbr.setNutritionFormulaFeeding(getDiscussedConcernNotDiscussed("formulafeeding_2w"));
		rbr.setNutritionStoolUrine(getDiscussedConcernNotDiscussed("stoolurine_2w"));
		rbr.setVitaminD400UIDay(getDiscussedConcernNotDiscussed("vitaminD_2w"));

		//the yes no stuff
		rbr.setNutritionBreastfeedingYesNo(getYesNoUnknown("breastfeeding_2w_yes_no"));
		rbr.setNutritionFormulaFeedingYesNo(getYesNoUnknown("formulafeeding_2w_yes_no"));
		rbr.setSupplementationWaterYesNo(getYesNoUnknown("supplementationWater_2w_yes_no"));
		rbr.setSupplementationOtherFluidsYesNo(getYesNoUnknown("supplementationOther_2w_yes_no"));
		
				
		rbr.setDevelopmentNoParentCaregiverConcerns(getDiscussedConcernNotDiscussed("noconcerns_2w"));
		rbr.setDevelopmentSucksWellOnNipple(getDiscussedConcernNotDiscussed("suckswell_2w"));

		if (getRourkeStrValue("parentConcerns2w") != null) {
			rbr.setParentalCaregiverConcerns(getRourkeStrValue("parentConcerns2w"));
		}

		rbr.setPhysicalExamEarsHearingScreening(getDiscussedConcernNotDiscussed("ears_2w"));
		rbr.setPhysicalExamEyes(getDiscussedConcernNotDiscussed("eyes_2w"));
		rbr.setPhysicalExamFemoralPulses(getDiscussedConcernNotDiscussed("femoralpulses_2w"));
		rbr.setPhysicalExamFontanelles(getDiscussedConcernNotDiscussed("fontanelles_2w"));
		rbr.setPhysicalExamHeart(getDiscussedConcernNotDiscussed("heartlungs_2w"));
		rbr.setPhysicalExamHips(getDiscussedConcernNotDiscussed("hips_2w"));
		rbr.setPhysicalExamMaleUrinary(getDiscussedConcernNotDiscussed("maleurinary_2w"));
		rbr.setPhysicalExamMuscleTone(getDiscussedConcernNotDiscussed("muscletone_2w"));
		rbr.setPhysicalExamSkin(getDiscussedConcernNotDiscussed("skin_2w"));
		rbr.setPhysicalExamTesticles(getDiscussedConcernNotDiscussed("testicles_2w"));
		rbr.setPhysicalExamTongueMobility(getDiscussedConcernNotDiscussed("tonguemobility_2w"));
		rbr.setPhysicalExamUmbilicus(getDiscussedConcernNotDiscussed("umbilicus_2w"));

		//signature
		if (getRourkeStrValue("signature2w") != null) {
			rbr.setSignature(getRourkeStrValue("signature2w"));
		}

		//problems and plans
		if (getRourkeStrValue("problemsPlans2w") != null) {
			rbr.setProblemsAndPlansOther(getRourkeStrValue("problemsPlans2w"));
		}
		EFormValue resource = this.eformValuesMap.get("RBR").get("problemsPlans2wRes");
		if (resource != null && resource.getVarValue() != null & !resource.getVarValue().isEmpty()) {
			for (String val : resource.getVarValue().split(",")) {
				ProblemsAndPlans pp = rbr.addNewProblemsAndPlans();
				pp.setStringValue(val);
			}
		}

	}

	/*
	 * p1_education (education comments)
	 * 
	 * 
	 */
	private void populateRBRW01W02M01(RBRW01W02M01 rbr) {
		rbr.setEducationAdviceBehaviourCrying(getDiscussedConcernNotDiscussed("sleepcry_p1"));
		rbr.setEducationAdviceBehaviourFamilyConflicts(getDiscussedConcernNotDiscussed("familyconflict_p1"));
		rbr.setEducationAdviceBehaviourHealthySleepHabits(getDiscussedConcernNotDiscussed("sleephabits_p1"));
		rbr.setEducationAdviceBehaviourHighRiskInfants(getDiscussedConcernNotDiscussed("highrisk_p1"));
		rbr.setEducationAdviceBehaviourNightWaking(getDiscussedConcernNotDiscussed("nightwak_p1"));
		rbr.setEducationAdviceBehaviourParentalFatigue(getDiscussedConcernNotDiscussed("depression_p1"));
		rbr.setEducationAdviceBehaviourParentingBonding(getDiscussedConcernNotDiscussed("parenting_p1"));
		rbr.setEducationAdviceBehaviourSiblings(getDiscussedConcernNotDiscussed("siblings_p1"));
		rbr.setEducationAdviceBehaviourSoothabilityResponsiveness(getDiscussedConcernNotDiscussed("soothability_p1"));

		rbr.setEducationAdviceEnvironmentalHealthSecondHandSmoke(getDiscussedConcernNotDiscussed("secondhandsmoke_p1"));
		rbr.setEducationAdviceEnvironmentalHealthSunExposure(getDiscussedConcernNotDiscussed("sunexposure_p1"));
		rbr.setEducationAdviceInjuryPrevCarbonMonoxideSmokeDet(getDiscussedConcernNotDiscussed("smokedetectors_p1"));

		rbr.setEducationAdviceInjuryPrevCarSeat(getDiscussedConcernNotDiscussed("carseat_p1"));
		rbr.setEducationAdviceInjuryPrevChokingSafeToys(getDiscussedConcernNotDiscussed("choking_p1"));
		//rbr.setEducationAdviceInjuryPrevCribSafety(getDiscussedConcernNotDiscussed("cribsafety_p1")); DO NOT USE
		rbr.setEducationAdviceInjuryPrevFirearmSafety(getDiscussedConcernNotDiscussed("firearmsafety_p1"));
		rbr.setEducationAdviceInjuryPrevHotWater(getDiscussedConcernNotDiscussed("hotwater_p1"));
		rbr.setEducationAdviceInjuryPrevPacifierUse(getDiscussedConcernNotDiscussed("pacifier_p1"));
		rbr.setEducationAdviceInjuryPrevSafeSleep(getDiscussedConcernNotDiscussed("sleeppos_p1"));

		rbr.setEducationAdviceOtherComplementaryAlternMedicine(getDiscussedConcernNotDiscussed("complementarymedn_p1"));
		//rbr.setEducationAdviceOtherCounselPacifierUse(); //DO NOT USE
		rbr.setEducationAdviceOtherFeverAdvice(getDiscussedConcernNotDiscussed("feveradvice_p1"));
		rbr.setEducationAdviceOtherNoOTCCoughColdMeds(getDiscussedConcernNotDiscussed("nootcmedn_p1"));
		rbr.setEducationAdviceOtherTempControl(getDiscussedConcernNotDiscussed("temperaturectrl_p1"));
	}

	private void populateRBRM01(RBRM01 rbr) {
		rbr.setFormVersion(FormVersion.X_2014);

		//visit date
		EFormValue date = this.eformValuesMap.get("RBR").get("visit_date_1m");
		if (date != null && date.getVarValue() != null && !date.getVarValue().isEmpty()) {
			rbr.setVisitDate(generateNonTimestampDate(date.getVarValue()));
		}

		if (stringToBigDecimal(getRourkeStrValue("headcirc_1m")) != null) {
			rbr.setHeadCirc(stringToBigDecimal(getRourkeStrValue("headcirc_1m")));
		}
		if (stringToBigDecimal(getRourkeStrValue("height_1m")) != null) {
			rbr.setHeight(stringToBigDecimal(getRourkeStrValue("height_1m")));
		}
		Integer weight = null;
		if ((weight = getRourkeIntValue("weight_1m")) != null) {
			rbr.setWeight(weight);
		}

		rbr.setDevelopmentCalmsWhenComforted(getDiscussedConcernNotDiscussed("calmscomforted_1m"));
		rbr.setDevelopmentFocusesGaze(getDiscussedConcernNotDiscussed("focusesgaze_1m"));
		rbr.setDevelopmentNoParentCaregiverConcerns(getDiscussedConcernNotDiscussed("noconcerns_1m"));
		rbr.setDevelopmentStartlesLoudNoise(getDiscussedConcernNotDiscussed("startlestonoise_1m"));
		rbr.setDevelopmentSucksWellOnNipple(getDiscussedConcernNotDiscussed("suckswell_1m"));
		
		rbr.setInvestigationsImmunizationHBsAgPosParentVaccine2(getDiscussedConcernNotDiscussed("hepbimmuneglobulin_1m") );
		rbr.setInvestigationsImmunizationRecordVaccines(getDiscussedConcernNotDiscussed("recordVaccines_1m"));

		
		rbr.setNutritionBreastfeeding(getDiscussedConcernNotDiscussed("breastfeeding_1m"));
		rbr.setNutritionFormulaFeeding(getDiscussedConcernNotDiscussed("formulafeeding_1m"));
		rbr.setNutritionStoolUrine(getDiscussedConcernNotDiscussed("stoolurine_1m"));
		rbr.setVitaminD400UIDay(getDiscussedConcernNotDiscussed("vitaminD_1m"));

		//the yes no stuff
		rbr.setNutritionBreastfeedingYesNo(getYesNoUnknown("breastfeeding_1m_yes_no"));
		rbr.setNutritionFormulaFeedingYesNo(getYesNoUnknown("formulafeeding_1m_yes_no"));
		rbr.setSupplementationWaterYesNo(getYesNoUnknown("supplementationWater_1m_yes_no"));
		rbr.setSupplementationOtherFluidsYesNo(getYesNoUnknown("supplementationOther_1m_yes_no"));
		rbr.setSupplementationSolidsYesNo(getYesNoUnknown("supplementationSolid_1m_yes_no"));
				
		if (getRourkeStrValue("parentConcerns1m") != null) {
			rbr.setParentalCaregiverConcerns(getRourkeStrValue("parentConcerns1m"));
		}

		rbr.setPhysicalCornealLightReflex(getDiscussedConcernNotDiscussed("corneallightreflex_1m"));
		rbr.setPhysicalExamEyes(getDiscussedConcernNotDiscussed("eyes_1m"));
		rbr.setPhysicalExamHearingScreening(getDiscussedConcernNotDiscussed("hearing_1m"));
		rbr.setPhysicalExamFontanelles(getDiscussedConcernNotDiscussed("fontanelles_1m"));
		rbr.setPhysicalExamHeart(getDiscussedConcernNotDiscussed("heart_1m"));
		rbr.setPhysicalExamHips(getDiscussedConcernNotDiscussed("hips_1m"));
		rbr.setPhysicalExamMuscleTone(getDiscussedConcernNotDiscussed("muscletone_1m"));
		rbr.setPhysicalExamSkin(getDiscussedConcernNotDiscussed("skin_1m"));
		rbr.setPhysicalExamTongueMobility(getDiscussedConcernNotDiscussed("tonguemobility_1m"));

		//signature
		if (getRourkeStrValue("signature1m") != null) {
			rbr.setSignature(getRourkeStrValue("signature1m"));
		}

		//problems and plans
		if (getRourkeStrValue("problemsPlans1m") != null) {
			rbr.setProblemsAndPlansOther(getRourkeStrValue("problemsPlans1m"));
		}
		EFormValue resource = this.eformValuesMap.get("RBR").get("problemsPlans1mRes");
		if (resource != null && resource.getVarValue() != null & !resource.getVarValue().isEmpty()) {
			for (String val : resource.getVarValue().split(",")) {
				ProblemsAndPlans pp = rbr.addNewProblemsAndPlans();
				pp.setStringValue(val);
			}
		}

	}

	private void populateRBRM02(RBRM02 rbr) {
		rbr.setFormVersion(FormVersion.X_2014);

		//visit date
		EFormValue date = this.eformValuesMap.get("RBR").get("visit_date_2m");
		if (date != null && date.getVarValue() != null && !date.getVarValue().isEmpty()) {
			rbr.setVisitDate(generateNonTimestampDate(date.getVarValue()));
		}

		if (stringToBigDecimal(getRourkeStrValue("headcirc_2m")) != null) {
			rbr.setHeadCirc(stringToBigDecimal(getRourkeStrValue("headcirc_2m")));
		}
		if (stringToBigDecimal(getRourkeStrValue("height_2m")) != null) {
			rbr.setHeight(stringToBigDecimal(getRourkeStrValue("height_2m")));
		}
		Integer weight = null;
		if ((weight = getRourkeIntValue("weight_2m")) != null) {
			rbr.setWeight(weight);
		}

		rbr.setDevelopmentComforted(getDiscussedConcernNotDiscussed("calmscomforted_2m"));
		rbr.setDevelopmentCoos(getDiscussedConcernNotDiscussed("coos_2m"));
		rbr.setDevelopmentFollowsMovementWithEyes(getDiscussedConcernNotDiscussed("followswitheyes_2m"));
		rbr.setDevelopmentLiftsHead(getDiscussedConcernNotDiscussed("liftsheadontummy_2m"));
		rbr.setDevelopmentNoParentCaregiverConcerns(getDiscussedConcernNotDiscussed("noconcerns_2m"));
		rbr.setDevelopmentSmiles(getDiscussedConcernNotDiscussed("smilesresponsively_2m"));
		rbr.setDevelopmentTwoOrMoreSucks(getDiscussedConcernNotDiscussed("sequencessucks_2m"));

		rbr.setInvestigationsImmunizationRecordVaccines(getDiscussedConcernNotDiscussed("recordVaccines_2m") );

		
		rbr.setNutritionBreastfeeding(getDiscussedConcernNotDiscussed("breastfeeding_2m"));
		rbr.setNutritionFormulaFeeding(getDiscussedConcernNotDiscussed("formulafeeding_2m"));
		rbr.setVitaminD400UIDay(getDiscussedConcernNotDiscussed("vitaminD_2m"));

		//the yes no stuff
		rbr.setNutritionBreastfeedingYesNo(getYesNoUnknown("breastfeeding_2m_yes_no"));
		rbr.setNutritionFormulaFeedingYesNo(getYesNoUnknown("formulafeeding_2m_yes_no"));
		rbr.setSupplementationWaterYesNo(getYesNoUnknown("supplementationWater_2m_yes_no"));
		rbr.setSupplementationOtherFluidsYesNo(getYesNoUnknown("supplementationOther_2m_yes_no"));
		rbr.setSupplementationSolidsYesNo(getYesNoUnknown("supplementationSolid_2m_yes_no"));
		
		if (getRourkeStrValue("parentConcerns2m") != null) {
			rbr.setParentalCaregiverConcerns(getRourkeStrValue("parentConcerns2m"));
		}

		rbr.setPhysicalCornealLightReflex(getDiscussedConcernNotDiscussed("corneallightreflex_2m"));
		rbr.setPhysicalExamEyes(getDiscussedConcernNotDiscussed("eyes_2m"));
		rbr.setPhysicalExamFontanelles(getDiscussedConcernNotDiscussed("fontanelles_2m"));
		rbr.setPhysicalExamHearingScreening(getDiscussedConcernNotDiscussed("hearing_2m"));
		rbr.setPhysicalExamHeart(getDiscussedConcernNotDiscussed("heart_2m"));
		rbr.setPhysicalExamHips(getDiscussedConcernNotDiscussed("hips_2m"));
		rbr.setPhysicalExamMuscleTone(getDiscussedConcernNotDiscussed("muscletone_2m"));

		//signature
		if (getRourkeStrValue("signature2m") != null) {
			rbr.setSignature(getRourkeStrValue("signature2m"));
		}

		//problems and plans
		if (getRourkeStrValue("problemsPlans2m") != null) {
			rbr.setProblemsAndPlansOther(getRourkeStrValue("problemsPlans2m"));
		}
		EFormValue resource = this.eformValuesMap.get("RBR").get("problemsPlans2mRes");
		if (resource != null && resource.getVarValue() != null & !resource.getVarValue().isEmpty()) {
			for (String val : resource.getVarValue().split(",")) {
				ProblemsAndPlans pp = rbr.addNewProblemsAndPlans();
				pp.setStringValue(val);
			}
		}

	}

	private void populateRBRM04(RBRM04 rbr) {
		rbr.setFormVersion(FormVersion.X_2014);

		//visit date
		EFormValue date = this.eformValuesMap.get("RBR").get("visit_date_4m");
		if (date != null && date.getVarValue() != null && !date.getVarValue().isEmpty()) {
			rbr.setVisitDate(generateNonTimestampDate(date.getVarValue()));
		}

		if (stringToBigDecimal(getRourkeStrValue("headcirc_4m")) != null) {
			rbr.setHeadCirc(stringToBigDecimal(getRourkeStrValue("headcirc_4m")));
		}
		if (stringToBigDecimal(getRourkeStrValue("height_4m")) != null) {
			rbr.setHeight(stringToBigDecimal(getRourkeStrValue("height_4m")));
		}
		Integer weight = null;
		if ((weight = getRourkeIntValue("weight_4m")) != null) {
			rbr.setWeight(weight);
		}

		rbr.setDevelopmentFollowsMovingToyPerson(getDiscussedConcernNotDiscussed("followswitheyes_4m"));
		rbr.setDevelopmentHoldsHead(getDiscussedConcernNotDiscussed("headsteady_4m"));
		rbr.setDevelopmentHoldsObject(getDiscussedConcernNotDiscussed("holdsobject_4m"));
		rbr.setDevelopmentLaughsSmiles(getDiscussedConcernNotDiscussed("smilesresponsively_4m"));
		rbr.setDevelopmentNoParentCaregiverConcerns(getDiscussedConcernNotDiscussed("noconcerns_4m"));
		rbr.setDevelopmentRespondsWithExcitement(getDiscussedConcernNotDiscussed("respondswithexcitement_4m"));

		rbr.setInvestigationsImmunizationRecordVaccines(getDiscussedConcernNotDiscussed("recordVaccines_4m"));

		
		rbr.setNutritionBreastfeeding(getDiscussedConcernNotDiscussed("breastfeeding_4m"));
		rbr.setNutritionFormulaFeeding(getDiscussedConcernNotDiscussed("formulafeeding_4m"));
		rbr.setNutritionIntroductionSolids(getDiscussedConcernNotDiscussed("discussSolids_4m"));
		rbr.setVitaminD400UIDay(getDiscussedConcernNotDiscussed("vitaminD_4m"));

		//the yes no stuff
		rbr.setNutritionBreastfeedingYesNo(getYesNoUnknown("breastfeeding_4m_yes_no"));
		rbr.setNutritionFormulaFeedingYesNo(getYesNoUnknown("formulafeeding_4m_yes_no"));
		rbr.setSupplementationWaterYesNo(getYesNoUnknown("supplementationWater_4m_yes_no"));
		rbr.setSupplementationOtherFluidsYesNo(getYesNoUnknown("supplementationOther_4m_yes_no"));
		rbr.setSupplementationSolidsYesNo(getYesNoUnknown("supplementationSolid_4m_yes_no"));

		
		if (getRourkeStrValue("parentConcerns4m") != null) {
			rbr.setParentalCaregiverConcerns(getRourkeStrValue("parentConcerns4m"));
		}

		rbr.setPhysicalCornealLightReflex(getDiscussedConcernNotDiscussed("corneallightreflex_4m"));
		rbr.setPhysicalExamAnteriorFontanelles(getDiscussedConcernNotDiscussed("anteriorfontanelles_4m"));
		rbr.setPhysicalExamEyes(getDiscussedConcernNotDiscussed("eyes_4m"));
		rbr.setPhysicalExamHearingScreening(getDiscussedConcernNotDiscussed("hearing_4m"));
		rbr.setPhysicalExamHips(getDiscussedConcernNotDiscussed("hips_4m"));
		rbr.setPhysicalExamMuscleTone(getDiscussedConcernNotDiscussed("muscletone_4m"));

		if (getRourkeStrValue("signature4m") != null) {
			rbr.setSignature(getRourkeStrValue("signature4m"));
		}

		//TODO:
		//problemsPlans4m not in XML??

	}

	private void populateRBRM06(RBRM06 rbr) {
		rbr.setFormVersion(FormVersion.X_2014);

		//visit date
		EFormValue date = this.eformValuesMap.get("RBR").get("visit_date_6m");
		if (date != null && date.getVarValue() != null && !date.getVarValue().isEmpty()) {
			rbr.setVisitDate(generateNonTimestampDate(date.getVarValue()));
		}

		if (stringToBigDecimal(getRourkeStrValue("headcirc_6m")) != null) {
			rbr.setHeadCirc(stringToBigDecimal(getRourkeStrValue("headcirc_6m")));
		}
		if (stringToBigDecimal(getRourkeStrValue("height_6m")) != null) {
			rbr.setHeight(stringToBigDecimal(getRourkeStrValue("height_6m")));
		}
		Integer weight = null;
		if ((weight = getRourkeIntValue("weight_6m")) != null) {
			rbr.setWeight(weight);
		}

		rbr.setDevelopmentMakesSounds(getDiscussedConcernNotDiscussed("soundswhiletalk_6m"));
		rbr.setDevelopmentNoParentCaregiverConcerns(getDiscussedConcernNotDiscussed("noconcerns_6m"));
		rbr.setDevelopmentReaches(getDiscussedConcernNotDiscussed("reachesobjects_6m"));
		rbr.setDevelopmentRolls(getDiscussedConcernNotDiscussed("rollstoside_6m"));
		rbr.setDevelopmentSits(getDiscussedConcernNotDiscussed("sitswithsupport_6m"));
		rbr.setDevelopmentTurnsHead(getDiscussedConcernNotDiscussed("turnstowardsounds_6m"));
		rbr.setDevelopmentVocalizes(getDiscussedConcernNotDiscussed("vocalizes_6m"));

		rbr.setInvestigationsImmunizationHBsAgPosParentVaccine3(getDiscussedConcernNotDiscussed("hepbimmuneglobulin_6m") );
		rbr.setInvestigationsImmunizationHemoglobin(getDiscussedConcernNotDiscussed( "hemoglobinRisk_6m") );
		rbr.setInvestigationsImmunizationInquireTBRiskFactors(getDiscussedConcernNotDiscussed( "tbriskfactors_6m"));

		rbr.setInvestigationsImmunizationRecordVaccines(getDiscussedConcernNotDiscussed("recordVaccines_6m"));

		
		rbr.setNutritionAvoidSweetJuicesLiquids(getDiscussedConcernNotDiscussed("avoidsweetened_6m"));
		rbr.setNutritionBreastfeeding(getDiscussedConcernNotDiscussed("breastfeeding_6m"));
		rbr.setNutritionChokingSafeFood(getDiscussedConcernNotDiscussed("chokingsafefood_6m"));
		rbr.setNutritionFormulaFeeding(getDiscussedConcernNotDiscussed("formulafeeding_6m"));
		rbr.setNutritionFruitsVegetablesMilk(getDiscussedConcernNotDiscussed("fruitsvegetables_6m"));
		rbr.setNutritionIronContainingFoods(getDiscussedConcernNotDiscussed("ironfoods_6m"));
		rbr.setNutritionNoBottlesInBed(getDiscussedConcernNotDiscussed("nobottleinbed_6m"));
		rbr.setNutritionNoHoney(getDiscussedConcernNotDiscussed("noeggnuthoney_6m"));
		rbr.setVitaminD400UIDay(getDiscussedConcernNotDiscussed("vitaminD_6m"));

		//the yes no stuff
		rbr.setNutritionBreastfeedingYesNo(getYesNoUnknown("breastfeeding_6m_yes_no"));
		rbr.setNutritionFormulaFeedingYesNo(getYesNoUnknown("formulafeeding_6m_yes_no"));
		
		
		if (getRourkeStrValue("parentConcerns6m") != null) {
			rbr.setParentalCaregiverConcerns(getRourkeStrValue("parentConcerns6m"));
		}

		rbr.setPhysicalCornealLightReflex(getDiscussedConcernNotDiscussed("corneallightreflex_6m"));
		rbr.setPhysicalExamAnteriorFontanelles(getDiscussedConcernNotDiscussed("anteriorfontanelles_6m"));
		rbr.setPhysicalExamEyes(getDiscussedConcernNotDiscussed("eyes_6m"));
		rbr.setPhysicalExamHearingScreening(getDiscussedConcernNotDiscussed("hearing_6m"));
		rbr.setPhysicalExamHips(getDiscussedConcernNotDiscussed("hips_6m"));
		rbr.setPhysicalExamMuscleTone(getDiscussedConcernNotDiscussed("muscletone_6m"));

		//signature
		if (getRourkeStrValue("signature6m") != null) {
			rbr.setSignature(getRourkeStrValue("signature6m"));
		}

		//problems and plans
		if (getRourkeStrValue("problemsPlans6m") != null) {
			rbr.setProblemsAndPlansOther(getRourkeStrValue("problemsPlans6m"));
		}
		EFormValue resource = this.eformValuesMap.get("RBR").get("problemsPlans6mRes");
		if (resource != null && resource.getVarValue() != null & !resource.getVarValue().isEmpty()) {
			for (String val : resource.getVarValue().split(",")) {
				ProblemsAndPlans pp = rbr.addNewProblemsAndPlans();
				pp.setStringValue(val);
			}
		}

	}

	private void populateRBRM02M04M06(RBRM02M04M06 rbr) {

		rbr.setEducationAdviceBehaviourChildCareReturnToWork(getDiscussedConcernNotDiscussed("childcare_p2"));
		rbr.setEducationAdviceBehaviourCrying(getDiscussedConcernNotDiscussed("sleepcry_p2"));
		rbr.setEducationAdviceBehaviourFamilyActiveLiving(getDiscussedConcernNotDiscussed("familyActiveLiving_p2"));
		rbr.setEducationAdviceBehaviourFamilyConflicts(getDiscussedConcernNotDiscussed("familyconflict_p2"));

		rbr.setEducationAdviceBehaviourHealthySleepHabits(getDiscussedConcernNotDiscussed("sleephabits_p2"));

		rbr.setEducationAdviceBehaviourHighRiskInfants(getDiscussedConcernNotDiscussed("highrisk_p2"));
		rbr.setEducationAdviceBehaviourNightWaking(getDiscussedConcernNotDiscussed("nightwak_p2"));
		rbr.setEducationAdviceBehaviourParentalFatigue(getDiscussedConcernNotDiscussed("depression_p2"));
		rbr.setEducationAdviceBehaviourParentingBonding(getDiscussedConcernNotDiscussed("parenting_p2"));
		rbr.setEducationAdviceBehaviourSiblings(getDiscussedConcernNotDiscussed("siblings_p2"));
		rbr.setEducationAdviceBehaviourSoothabilityResponsiveness(getDiscussedConcernNotDiscussed("soothability_p2"));

		rbr.setEducationAdviceEnvironmentalHealthSecondHandSmoke(getDiscussedConcernNotDiscussed("secondhandsmoke_p2"));
		rbr.setEducationAdviceEnvironmentPesticideExposure(getDiscussedConcernNotDiscussed("pesticide_p2"));
		rbr.setEducationAdviceEnvironmentSunExposure(getDiscussedConcernNotDiscussed("sunexposure_p2"));

		rbr.setEducationAdviceInjuryPrevCarbonMonoxideSmokeDet(getDiscussedConcernNotDiscussed("smokedetectors_p2"));
		rbr.setEducationAdviceInjuryPrevCarSeat(getDiscussedConcernNotDiscussed("carseat_p2"));
		rbr.setEducationAdviceInjuryPrevChokingSafeToys(getDiscussedConcernNotDiscussed("choking_p2"));
		rbr.setEducationAdviceInjuryPrevElectricPlugsCords(getDiscussedConcernNotDiscussed("electricplugs_p2"));
		rbr.setEducationAdviceInjuryPrevFalls(getDiscussedConcernNotDiscussed("falls_p2"));
		rbr.setEducationAdviceInjuryPrevFirearmSafety(getDiscussedConcernNotDiscussed("firearmsafety_p2"));
		rbr.setEducationAdviceInjuryPrevHotWaterBathSafety(getDiscussedConcernNotDiscussed("hotwater_p2"));
		rbr.setEducationAdviceInjuryPrevPoisons(getDiscussedConcernNotDiscussed("poisons_p2"));
		rbr.setEducationAdviceInjuryPrevSafeSleep(getDiscussedConcernNotDiscussed("sleeppos_p2"));

		rbr.setEducationAdviceOtherComplementaryAlternMedicine(getDiscussedConcernNotDiscussed("complementarymedn_p2"));
		rbr.setEducationAdviceOtherEncourageReading(getDiscussedConcernNotDiscussed("reading_p2"));
		rbr.setEducationAdviceOtherFeverAdvice(getDiscussedConcernNotDiscussed("feveradvice_p2"));
		rbr.setEducationAdviceOtherNoOTCCoughColdMeds(getDiscussedConcernNotDiscussed("nootcmedn_p2"));
		rbr.setEducationAdviceInjuryPrevPacifierUse(getDiscussedConcernNotDiscussed("pacifier_p2"));
		rbr.setEducationAdviceOtherTeethingDentalCleaningFluoride(getDiscussedConcernNotDiscussed("dental_p2"));
		rbr.setEducationAdviceOtherTempControl(getDiscussedConcernNotDiscussed("temperaturectrl_p2"));
	}

	/*
	 * anemiascreening_9m
	 * 		
			//hepbimmuneglobulin_9m?
	 */
	private void populateRBRM09(RBRM09 rbr) {
		rbr.setFormVersion(FormVersion.X_2014);

		//visit date
		EFormValue date = this.eformValuesMap.get("RBR").get("visit_date_9m");
		if (date != null && date.getVarValue() != null && !date.getVarValue().isEmpty()) {
			rbr.setVisitDate(generateNonTimestampDate(date.getVarValue()));
		}

		if (stringToBigDecimal(getRourkeStrValue("headcirc_9m")) != null) {
			rbr.setHeadCirc(stringToBigDecimal(getRourkeStrValue("headcirc_9m")));
		}
		if (stringToBigDecimal(getRourkeStrValue("height_9m")) != null) {
			rbr.setHeight(stringToBigDecimal(getRourkeStrValue("height_9m")));
		}
		Integer weight = null;
		if ((weight = getRourkeIntValue("weight_9m")) != null) {
			rbr.setWeight(weight);
		}

		rbr.setDevelopmentBabbles(getDiscussedConcernNotDiscussed("babbles_9m"));
		rbr.setDevelopmentCriesForAttention(getDiscussedConcernNotDiscussed("criesforattention_9m"));
		rbr.setDevelopmentLooksForHiddenObjects(getDiscussedConcernNotDiscussed("looksforhidden_9m"));
		rbr.setDevelopmentMakesSoundsToGetAttention(getDiscussedConcernNotDiscussed("soundsgetattention_9m"));
		rbr.setDevelopmentNoParentCaregiverConcerns(getDiscussedConcernNotDiscussed("noconcerns_9m"));
		rbr.setDevelopmentOpposesThumbsaAndFingersWithGrasps(getDiscussedConcernNotDiscussed("opposesthumbfingers_9m"));
		rbr.setDevelopmentPlaySocialGames(getDiscussedConcernNotDiscussed("socialgames_9m"));
		rbr.setDevelopmentRespondsDiffToDiffPeople(getDiscussedConcernNotDiscussed("respondsdiffpeople_9m"));
		rbr.setDevelopmentSitsWithoutSupport(getDiscussedConcernNotDiscussed("sitsnosupport_9m"));
		rbr.setDevelopmentStandsWithSupport(getDiscussedConcernNotDiscussed("standswithsupport_9m"));

		rbr.setInvestigationsImmunizationRecordVaccines(getDiscussedConcernNotDiscussed("recordVaccines_9m"));

		
		rbr.setNutritionAvoidSweetJuicesLiquids(getDiscussedConcernNotDiscussed("avoidsweetened_9m"));
		rbr.setNutritionBreastfeeding(getDiscussedConcernNotDiscussed("breastfeeding_9m"));
		rbr.setNutritionCerealMeatFruitsVegetables(getDiscussedConcernNotDiscussed("cerealmeatfruits_9m"));
		rbr.setNutritionChokingSafeFood(getDiscussedConcernNotDiscussed("chokingsafefood_9m"));
		rbr.setNutritionCowMilkProducts(getDiscussedConcernNotDiscussed("cowsmilkproduct_9m"));
		rbr.setNutritionFormulaFeeding(getDiscussedConcernNotDiscussed("formulafeeding_9m"));
		rbr.setNutritionIronEncourageChangeBottleToCup(getDiscussedConcernNotDiscussed("bottletocup_9m"));
		rbr.setNutritionNoBottlesInBed(getDiscussedConcernNotDiscussed("nobottleinbed_9m"));
		rbr.setNutritionNoHoney(getDiscussedConcernNotDiscussed("noeggnuthoney_9m"));

		//the yes no stuff
		rbr.setNutritionBreastfeedingYesNo(getYesNoUnknown("breastfeeding_9m_yes_no"));
		rbr.setNutritionFormulaFeedingYesNo(getYesNoUnknown("formulafeeding_9m_yes_no"));

		
		if (getRourkeStrValue("parentConcerns9m") != null) {
			rbr.setParentalCaregiverConcerns(getRourkeStrValue("parentConcerns9m"));
		}

		rbr.setPhysicalCornealLightReflex(getDiscussedConcernNotDiscussed("corneallightreflex_9m"));
		rbr.setPhysicalExamAnteriorFontanelles(getDiscussedConcernNotDiscussed("anteriorfontanelles_9m"));
		rbr.setPhysicalExamEyes(getDiscussedConcernNotDiscussed("eyes_9m"));
		rbr.setPhysicalExamHearingScreening(getDiscussedConcernNotDiscussed("hearing_9m"));
		rbr.setPhysicalExamHips(getDiscussedConcernNotDiscussed("hips_9m"));

		//signature
		if (getRourkeStrValue("signature9m") != null) {
			rbr.setSignature(getRourkeStrValue("signature9m"));
		}

		//problems and plans
		if (getRourkeStrValue("problemsPlans9m") != null) {
			rbr.setProblemsAndPlansOther(getRourkeStrValue("problemsPlans9m"));
		}
		EFormValue resource = this.eformValuesMap.get("RBR").get("problemsPlans9mRes");
		if (resource != null && resource.getVarValue() != null & !resource.getVarValue().isEmpty()) {
			for (String val : resource.getVarValue().split(",")) {
				ProblemsAndPlans pp = rbr.addNewProblemsAndPlans();
				pp.setStringValue(val);
			}
		}
	}

	/*
	 * 
	 * anemiascreening_9m
	 * 
	 */
	private void populateRBRM1213(RBRM1213 rbr) {
		rbr.setFormVersion(FormVersion.X_2014);

		//visit date
		EFormValue date = this.eformValuesMap.get("RBR").get("visit_date_12m");
		if (date != null && date.getVarValue() != null && !date.getVarValue().isEmpty()) {
			rbr.setVisitDate(generateNonTimestampDate(date.getVarValue()));
		}

		if (stringToBigDecimal(getRourkeStrValue("headcirc_12m")) != null) {
			rbr.setHeadCirc(stringToBigDecimal(getRourkeStrValue("headcirc_12m")));
		}
		if (stringToBigDecimal(getRourkeStrValue("height_12m")) != null) {
			rbr.setHeight(stringToBigDecimal(getRourkeStrValue("height_12m")));
		}
		Integer weight = null;
		if ((weight = getRourkeIntValue("weight_12m")) != null) {
			rbr.setWeight(weight);
		}

		rbr.setDevelopmentCrawls(getDiscussedConcernNotDiscussed("crawls_12m"));
		rbr.setDevelopmentFollowsYourGazeToReferenceObjects(getDiscussedConcernNotDiscussed("followsgazeobject_12m"));
		rbr.setDevelopmentMakesOneConsonantVowelComb(getDiscussedConcernNotDiscussed("consonantvowel_12m"));
		rbr.setDevelopmentNoParentCaregiverConcerns(getDiscussedConcernNotDiscussed("noconcerns_12m"));
		rbr.setDevelopmentPullsToStandWalks(getDiscussedConcernNotDiscussed("pullstostand_12m"));
		rbr.setDevelopmentRespondsToOwnName(getDiscussedConcernNotDiscussed("respondstoname_12m"));
		rbr.setDevelopmentSaysThreeWords(getDiscussedConcernNotDiscussed("words3ormore_12m"));
		rbr.setDevelopmentShowsDistressWhenSeparated(getDiscussedConcernNotDiscussed("distressseparated_12m"));
		rbr.setDevelopmentUnderstandsSimpleRequests(getDiscussedConcernNotDiscussed("understandsrequests_12m"));

		rbr.setInvestigationsImmunizationRecordVaccines(getDiscussedConcernNotDiscussed("recordVaccines_9m"));
		
		rbr.setNutritionAppetiteReduced(getDiscussedConcernNotDiscussed("appetitereduced_12m"));
		rbr.setNutritionAvoidSweetJuicesLiquids(getDiscussedConcernNotDiscussed("avoidsweetened_12m"));
		rbr.setNutritionBreastfeeding(getDiscussedConcernNotDiscussed("breastfeeding_12m"));
		rbr.setNutritionChokingSafeFood(getDiscussedConcernNotDiscussed("chokingsafefood_12m"));
		rbr.setNutritionHomogenizedMilk(getDiscussedConcernNotDiscussed("formulafeeding_12m"));
		rbr.setNutritionIronPromoteCup(getDiscussedConcernNotDiscussed("bottletocup_12m"));
		rbr.setNutritionVegeterianDiets(getDiscussedConcernNotDiscussed("vegetariandiets_12m"));

		//the yes no stuff
		rbr.setNutritionBreastfeedingYesNo(getYesNoUnknown("breastfeeding_12m_yes_no"));

		
		if (getRourkeStrValue("parentConcerns12m") != null) {
			rbr.setParentalCaregiverConcerns(getRourkeStrValue("parentConcerns12m"));
		}

		rbr.setPhysicalCornealLightReflex(getDiscussedConcernNotDiscussed("corneallightreflex_12m"));
		rbr.setPhysicalExamAnteriorFontanelles(getDiscussedConcernNotDiscussed("anteriorfontanelles_12m"));
		rbr.setPhysicalExamEyes(getDiscussedConcernNotDiscussed("eyes_12m"));
		rbr.setPhysicalExamHearingScreening(getDiscussedConcernNotDiscussed("hearing_12m"));
		rbr.setPhysicalExamHips(getDiscussedConcernNotDiscussed("hips_12m"));
		rbr.setPhysicalExamSnoringTonsil(getDiscussedConcernNotDiscussed("snoring_12m"));
		rbr.setPhysicalExamTeeth(getDiscussedConcernNotDiscussed("teeth_12m"));

		//signature
		if (getRourkeStrValue("signature12m") != null) {
			rbr.setSignature(getRourkeStrValue("signature12m"));
		}

		//problems and plans
		if (getRourkeStrValue("problemsPlans12m") != null) {
			rbr.setProblemsAndPlansOther(getRourkeStrValue("problemsPlans12m"));
		}
		EFormValue resource = this.eformValuesMap.get("RBR").get("problemsPlans12mRes");
		if (resource != null && resource.getVarValue() != null & !resource.getVarValue().isEmpty()) {
			for (String val : resource.getVarValue().split(",")) {
				ProblemsAndPlans pp = rbr.addNewProblemsAndPlans();
				pp.setStringValue(val);
			}
		}
	}

	private void populateRBRM09M1213(RBRM09M1213 rbr) {
		rbr.setInvestigationsImmunizationHBsAgPosMotherCheckAntibodies(getDiscussedConcernNotDiscussed("hepbimmuneglobulin_9m") );
		rbr.setInvestigationsImmunizationHBsAgPosParentVaccine3(getDiscussedConcernNotDiscussed("hemoglobinRisk_9m") );
	}

	private void populateRBRM15(RBRM15 rbr) {
		rbr.setFormVersion(FormVersion.X_2014);

		//visit date
		EFormValue date = this.eformValuesMap.get("RBR").get("visit_date_15m");
		if (date != null && date.getVarValue() != null && !date.getVarValue().isEmpty()) {
			rbr.setVisitDate(generateNonTimestampDate(date.getVarValue()));
		}

		if (stringToBigDecimal(getRourkeStrValue("headcirc_15m")) != null) {
			rbr.setHeadCirc(stringToBigDecimal(getRourkeStrValue("headcirc_15m")));
		}
		if (stringToBigDecimal(getRourkeStrValue("height_15m")) != null) {
			rbr.setHeight(stringToBigDecimal(getRourkeStrValue("height_15m")));
		}
		Integer weight = null;
		if ((weight = getRourkeIntValue("weight_15m")) != null) {
			rbr.setWeight(weight);
		}

		rbr.setDevelopmentCrawlsFewStairsSteps(getDiscussedConcernNotDiscussed("crawlsup_15m"));
		rbr.setDevelopmentNoParentCaregiverConcerns(getDiscussedConcernNotDiscussed("noconcerns_15m"));
		rbr.setDevelopmentPicksUpEatsFingerFood(getDiscussedConcernNotDiscussed("eatsfingerfoods_15m"));
		rbr.setDevelopmentSaysFiveWords(getDiscussedConcernNotDiscussed("words5ormore_15m"));
		rbr.setDevelopmentShowsFearStangePeople(getDiscussedConcernNotDiscussed("fearofstrange_15m"));
		rbr.setDevelopmentTriesSquat(getDiscussedConcernNotDiscussed("squattopick_15m"));
		rbr.setDevelopmentWalkSidewaysHolding(getDiscussedConcernNotDiscussed("walkssideways_15m"));

		rbr.setInvestigationsImmunizationRecordVaccines(getDiscussedConcernNotDiscussed("recordVaccines_15m"));
		
		rbr.setNutritionAvoidSweetJuicesLiquids(getDiscussedConcernNotDiscussed("avoidsweetened_15m"));
		rbr.setNutritionBreastfeeding(getDiscussedConcernNotDiscussed("breastfeeding_15m"));
		rbr.setNutritionChokingSafeFood(getDiscussedConcernNotDiscussed("chokingsafefood_15m"));
		rbr.setNutritionHomogenizedMilk(getDiscussedConcernNotDiscussed("formulafeeding_15m"));
		rbr.setNutritionIronPromoteCup(getDiscussedConcernNotDiscussed("bottletocup_15m"));
		rbr.setNutritionVegeterianDiets(getDiscussedConcernNotDiscussed("vegetariandiets_15m"));

		//the yes no stuff
		rbr.setNutritionBreastfeedingYesNo(getYesNoUnknown("breastfeeding_15m_yes_no"));

		
		if (getRourkeStrValue("parentConcerns15m") != null) {
			rbr.setParentalCaregiverConcerns(getRourkeStrValue("parentConcerns15m"));
		}

		rbr.setPhysicalCornealLightReflex(getDiscussedConcernNotDiscussed("corneallightreflex_15m"));
		rbr.setPhysicalExamAnteriorFontanelles(getDiscussedConcernNotDiscussed("anteriorfontanelles_15m"));
		rbr.setPhysicalExamEyes(getDiscussedConcernNotDiscussed("eyes_15m"));
		rbr.setPhysicalExamHearingScreening(getDiscussedConcernNotDiscussed("hearing_15m"));
		rbr.setPhysicalExamHips(getDiscussedConcernNotDiscussed("hips_15m"));
		rbr.setPhysicalExamSnoringTonsil(getDiscussedConcernNotDiscussed("snoring_15m"));
		rbr.setPhysicalExamTeeth(getDiscussedConcernNotDiscussed("teeth_15m"));

		//signature
		if (getRourkeStrValue("signature15m") != null) {
			rbr.setSignature(getRourkeStrValue("signature15m"));
		}

		//problems and plans
		if (getRourkeStrValue("problemsPlans15m") != null) {
			rbr.setProblemsAndPlansOther(getRourkeStrValue("problemsPlans15m"));
		}
		EFormValue resource = this.eformValuesMap.get("RBR").get("problemsPlans15mRes");
		if (resource != null && resource.getVarValue() != null & !resource.getVarValue().isEmpty()) {
			for (String val : resource.getVarValue().split(",")) {
				ProblemsAndPlans pp = rbr.addNewProblemsAndPlans();
				pp.setStringValue(val);
			}
		}
	}

	private void populateRBRM09M1213M15(RBRM09M1213M15 rbr) {
		rbr.setEducationAdviceBehaviourNightWaking(getDiscussedConcernNotDiscussed("nightwak_p3"));
		rbr.setEducationAdviceBehaviourChildCareReturnToWork(getDiscussedConcernNotDiscussed("childcare_p3"));
		rbr.setEducationAdviceBehaviourCrying(getDiscussedConcernNotDiscussed("sleepcry_p3"));
		rbr.setEducationAdviceBehaviourFamilyActiveLiving(getDiscussedConcernNotDiscussed("activehealthy_p3"));
		rbr.setEducationAdviceBehaviourFamilyConflicts(getDiscussedConcernNotDiscussed("familyconflict_p3"));
		rbr.setEducationAdviceBehaviourFamilyPesticideExposure(getDiscussedConcernNotDiscussed("pesticide_p3"));
		rbr.setEducationAdviceBehaviourHealthySleepHabits(getDiscussedConcernNotDiscussed("sleephabits_p3"));
		rbr.setEducationAdviceBehaviourHighRiskInfants(getDiscussedConcernNotDiscussed("highrisk_p3"));
		rbr.setEducationAdviceBehaviourParentalFatigue(getDiscussedConcernNotDiscussed("depression_p3"));
		rbr.setEducationAdviceBehaviourParentingBonding(getDiscussedConcernNotDiscussed("parenting_p3"));
		rbr.setEducationAdviceBehaviourSiblings(getDiscussedConcernNotDiscussed("siblings_p3"));
		rbr.setEducationAdviceBehaviourSoothabilityResponsiveness(getDiscussedConcernNotDiscussed("soothability_p3"));

		rbr.setEducationAdviceEnvironmentalHealthSecondHandSmoke(getDiscussedConcernNotDiscussed("secondhandsmoke_p3"));
		rbr.setEducationAdviceEnvironmentalHealthSerumLead(getDiscussedConcernNotDiscussed("serumlead_p3"));
		rbr.setEducationAdviceEnvironmentalHealthSunExposure(getDiscussedConcernNotDiscussed("sunexposure_p3"));

		rbr.setEducationAdviceInjuryPrevCarbonMonoxideSmokeDet(getDiscussedConcernNotDiscussed("smokedetectors_p3"));
		rbr.setEducationAdviceInjuryPrevCarSeat(getDiscussedConcernNotDiscussed("carseat_p3"));
		rbr.setEducationAdviceInjuryPrevChokingSafeToys(getDiscussedConcernNotDiscussed("choking_p3"));
		rbr.setEducationAdviceInjuryPrevElectricPlugsCords(getDiscussedConcernNotDiscussed("electricplugs_p3"));
		rbr.setEducationAdviceInjuryPrevFalls(getDiscussedConcernNotDiscussed("falls_p3"));
		rbr.setEducationAdviceInjuryPrevFirearmSafety(getDiscussedConcernNotDiscussed("firearmsafety_p3"));
		rbr.setEducationAdviceInjuryPrevHotWaterBathSafety(getDiscussedConcernNotDiscussed("hotwater_p3"));
		rbr.setEducationAdviceInjuryPrevPacifierUse(getDiscussedConcernNotDiscussed("pacifier_p3"));
		rbr.setEducationAdviceInjuryPrevPoisons(getDiscussedConcernNotDiscussed("poisons_p3"));

		rbr.setEducationAdviceOtherComplementaryAlternMedicine(getDiscussedConcernNotDiscussed("complementarymedn_p3"));
		rbr.setEducationAdviceOtherEncourageReading(getDiscussedConcernNotDiscussed("reading_p3"));
		rbr.setEducationAdviceOtherFeverAdvice(getDiscussedConcernNotDiscussed("feveradvice_p3"));
		rbr.setEducationAdviceOtherFootwear(getDiscussedConcernNotDiscussed("footwear_p3"));
		rbr.setEducationAdviceOtherNoOTCCoughColdMeds(getDiscussedConcernNotDiscussed("nootcmedn_p3"));
		rbr.setEducationAdviceOtherTeethingDentalCleaningFluoride(getDiscussedConcernNotDiscussed("dental_p3"));
	}

	private void populateRBRM18(RBRM18 rbr) {
		rbr.setFormVersion(FormVersion.X_2014);

		//visit date
		EFormValue date = this.eformValuesMap.get("RBR").get("visit_date_18m");
		if (date != null && date.getVarValue() != null && !date.getVarValue().isEmpty()) {
			rbr.setVisitDate(generateNonTimestampDate(date.getVarValue()));
		}

		if (stringToBigDecimal(getRourkeStrValue("headcirc_18m")) != null) {
			rbr.setHeadCirc(stringToBigDecimal(getRourkeStrValue("headcirc_18m")));
		}
		if (stringToBigDecimal(getRourkeStrValue("height_18m")) != null) {
			rbr.setHeight(stringToBigDecimal(getRourkeStrValue("height_18m")));
		}
		Integer weight = null;
		if ((weight = getRourkeIntValue("weight_18m")) != null) {
			rbr.setWeight(weight);
		}

		String nddsNotAttained = getRourkeStrValue("nddsNotAttained");
		if(nddsNotAttained != null && nddsNotAttained.length()>0) {
			for(String item:nddsNotAttained.split(",")) {
				String val = item.length()==1?"0"+item:item;
				try {
					BeanUtils.setProperty(rbr, "developmentNDDSNotAttained18M"+val, item);
				} catch (Exception e) {
					MiscUtils.getLogger().warn("error setting developmentNDDSNotAttained18M"+val, e);
				}
			}
		}

		rbr.setAdaptiveMotorSkillsNoParentConcerns(getDiscussedConcernNotDiscussed("noconcerns_18m"));
		rbr.setAdaptiveSkillsRemovesHat(getDiscussedConcernNotDiscussed("removeshatsocks_18m"));
		rbr.setDevelopmentCommSkillsGetsAttention(getDiscussedConcernNotDiscussed("getattentionshow_18m"));
		rbr.setDevelopmentCommSkillsImitatesSpeech(getDiscussedConcernNotDiscussed("imitatesspeech_18m"));
		rbr.setDevelopmentCommSkillsLooksForToys(getDiscussedConcernNotDiscussed("looksfortoy_18m"));
		rbr.setDevelopmentCommSkillsPointsToBodyParts(getDiscussedConcernNotDiscussed("pointsbodyparts_18m"));
		rbr.setDevelopmentCommSkillsPointsToWants(getDiscussedConcernNotDiscussed("pointstowants_18m"));
		rbr.setDevelopmentCommSkillsProduces4Cons(getDiscussedConcernNotDiscussed("consonants4_18m"));
		rbr.setDevelopmentCommSkillsRespondWhenCalled(getDiscussedConcernNotDiscussed("respondstoname_18m"));
		rbr.setDevelopmentCommSkillsSays20Words(getDiscussedConcernNotDiscussed("words20ormore_18m"));
		rbr.setDevelopmentMotorSkillsFeedsSelf(getDiscussedConcernNotDiscussed("spoonfeedsself_18m"));
		rbr.setDevelopmentMotorSkillsWalksAlone(getDiscussedConcernNotDiscussed("walksalone_18m"));
		rbr.setDevelopmentSocialEmotionalComesForComfort(getDiscussedConcernNotDiscussed("comesforcomfort_18m"));
		rbr.setDevelopmentSocialEmotionalEasyToSoothe(getDiscussedConcernNotDiscussed("easytosoothe_18m"));
		rbr.setDevelopmentSocialEmotionalInterestedInChildren(getDiscussedConcernNotDiscussed("interestedinchildren_18m"));
		rbr.setDevelopmentSocialEmotionalManagableBehaviour(getDiscussedConcernNotDiscussed("behaviourmanageable_18m"));

		rbr.setEducationAdviceBehaviourDisciplineParentingSkills(getDiscussedConcernNotDiscussed("disciplineparenting_18m"));
		rbr.setEducationAdviceBehaviourHealthySleepHabits(getDiscussedConcernNotDiscussed("sleephabits_18m"));
		rbr.setEducationAdviceBehaviourParentChildInteraction(getDiscussedConcernNotDiscussed("pcinteraction_18m"));

		rbr.setEducationAdviceEnvironmentalHealthPesticideExposure(getDiscussedConcernNotDiscussed("pesticide_18m"));
		rbr.setEducationAdviceEnvironmentalHealthSecondHandSmoke(getDiscussedConcernNotDiscussed("secondhandsmoke_18m"));
		rbr.setEducationAdviceEnvironmentalHealthSerumLead(getDiscussedConcernNotDiscussed("serumlead_18m"));
		rbr.setEducationAdviceEnvironmentalHealthSunExposure(getDiscussedConcernNotDiscussed("sunexposure_18m"));

		rbr.setEducationAdviceFamilyEncourageReading(getDiscussedConcernNotDiscussed("reading_18m"));
		rbr.setEducationAdviceFamilyHealthyActiveLiving(getDiscussedConcernNotDiscussed("familyActiveLiving_18m"));
		rbr.setEducationAdviceFamilyHighRiskChildren(getDiscussedConcernNotDiscussed("highrisk_18m"));
		rbr.setEducationAdviceFamilyParentalFatigueStress(getDiscussedConcernNotDiscussed("depression_18m"));
		rbr.setEducationAdviceFamilySocializing(getDiscussedConcernNotDiscussed("socializing_18m"));
		rbr.setEducationAdviceInjuryPrevBathSafety(getDiscussedConcernNotDiscussed("bathsafety_18m"));
		rbr.setEducationAdviceInjuryPrevCarSeat(getDiscussedConcernNotDiscussed("carseat_18m"));
		rbr.setEducationAdviceInjuryPrevChokingSafeToys(getDiscussedConcernNotDiscussed("choking_18m"));
		rbr.setEducationAdviceInjuryPrevFalls(getDiscussedConcernNotDiscussed("falls_18m"));
		rbr.setEducationAdviceInjuryPrevWeanFromPacifier(getDiscussedConcernNotDiscussed("weanpacifier_18m"));
		rbr.setEducationAdviceOtherDentalCare(getDiscussedConcernNotDiscussed("dental_18m"));
		rbr.setEducationAdviceFamilySocializing(getDiscussedConcernNotDiscussed("socializing_18m"));
		rbr.setEducationAdviceOtherToiletLearning(getDiscussedConcernNotDiscussed("toiletlearning_18m"));

		rbr.setInvestigationsImmunizationRecordVaccines(getDiscussedConcernNotDiscussed("recordVaccines_18m"));
		
		rbr.setNutritionAvoidSweetJuicesLiquids(getDiscussedConcernNotDiscussed("avoidsweetened_18m"));
		rbr.setNutritionBreastfeeding(getDiscussedConcernNotDiscussed("breastfeeding_18m"));
		rbr.setNutritionHomogenizedMilk(getDiscussedConcernNotDiscussed("formulafeeding_18m"));
		rbr.setNutritionNoBottles(getDiscussedConcernNotDiscussed("nobottle_18m"));

		//the yes no stuff
		rbr.setNutritionBreastfeedingYesNo(getYesNoUnknown("breastfeeding_18m_yes_no"));

		
		if (getRourkeStrValue("parentConcerns18m") != null) {
			rbr.setParentalCaregiverConcerns(getRourkeStrValue("parentConcerns18m"));
		}

		rbr.setPhysicalCornealLightReflex(getDiscussedConcernNotDiscussed("corneallightreflex_18m"));
		rbr.setPhysicalExamAnteriorFontanelles(getDiscussedConcernNotDiscussed("anteriorfontanelles_18m"));
		rbr.setPhysicalExamEyes(getDiscussedConcernNotDiscussed("eyes_18m"));
		rbr.setPhysicalExamHearingScreening(getDiscussedConcernNotDiscussed("hearing_18m"));
		rbr.setPhysicalExamSnoringTonsil(getDiscussedConcernNotDiscussed("snoring_18m"));
		rbr.setPhysicalExamTeeth(getDiscussedConcernNotDiscussed("teeth_18m"));

		//signature
		if (getRourkeStrValue("signature18m") != null) {
			rbr.setSignature(getRourkeStrValue("signature18m"));
		}

		//problems and plans
		if (getRourkeStrValue("problemsPlans18m") != null) {
			rbr.setProblemsAndPlansOther(getRourkeStrValue("problemsPlans18m"));
		}
		EFormValue resource = this.eformValuesMap.get("RBR").get("problemsPlans18mRes");
		if (resource != null && resource.getVarValue() != null & !resource.getVarValue().isEmpty()) {
			for (String val : resource.getVarValue().split(",")) {
				ProblemsAndPlans pp = rbr.addNewProblemsAndPlans();
				pp.setStringValue(val);
			}
		}
	}

	private void populateRBRY23(RBRY23 rbr) {
		rbr.setFormVersion(FormVersion.X_2014);

		//visit date
		EFormValue date = this.eformValuesMap.get("RBR").get("visit_date_2y");
		if (date != null && date.getVarValue() != null && !date.getVarValue().isEmpty()) {
			rbr.setVisitDate(generateNonTimestampDate(date.getVarValue()));
		}

		if (stringToBigDecimal(getRourkeStrValue("headcirc_2y")) != null) {
			rbr.setHeadCirc(stringToBigDecimal(getRourkeStrValue("headcirc_2y")));
		}
		if (stringToBigDecimal(getRourkeStrValue("height_2y")) != null) {
			rbr.setHeight(stringToBigDecimal(getRourkeStrValue("height_2y")));
		}
		Integer weight = null;
		if ((weight = getRourkeIntValue("weight_2y")) != null) {
			rbr.setWeight(weight);
		}

		rbr.setDevelopment2YCombines2Words(getDiscussedConcernNotDiscussed("multiplewords_2y"));
		rbr.setDevelopment2YDevelopNewSkills(getDiscussedConcernNotDiscussed("developnewskills_2y"));
		rbr.setDevelopment2YNoParentCaregiverConcerns(getDiscussedConcernNotDiscussed("noconcerns_2y"));
		rbr.setDevelopment2YOneTwoStepDirection(getDiscussedConcernNotDiscussed("understandsdirections_2y"));
		rbr.setDevelopment2YPutsObjectsSmallCont(getDiscussedConcernNotDiscussed("putsintocontainer_2y"));
		rbr.setDevelopment2YTriesToRun(getDiscussedConcernNotDiscussed("triestorun_2y"));
		rbr.setDevelopment2YUseToysForPretendPlay(getDiscussedConcernNotDiscussed("pretendplay_2y"));
		rbr.setDevelopment2YWalksBackward(getDiscussedConcernNotDiscussed("walksbackward_2y"));

		rbr.setDevelopment3YMakeBelieveGames(getDiscussedConcernNotDiscussed("pretendplay_3y"));
		rbr.setDevelopment3YMusic(getDiscussedConcernNotDiscussed("listenstomusic_3y"));
		rbr.setDevelopment3YNoParentCaregiverConcerns(getDiscussedConcernNotDiscussed("noconcerns_3y"));
		rbr.setDevelopment3YSentences5Words(getDiscussedConcernNotDiscussed("multiplewords_3y"));
		rbr.setDevelopment3YSharing(getDiscussedConcernNotDiscussed("shares_3y"));
		rbr.setDevelopment3YTurnsPages(getDiscussedConcernNotDiscussed("turn1page_3y"));
		rbr.setDevelopment3YTwistsLids(getDiscussedConcernNotDiscussed("twistslidsoff_3y"));
		rbr.setDevelopment3YTwoThreeStepDirections(getDiscussedConcernNotDiscussed("understandsdirections_3y"));
		rbr.setDevelopment3YWalksUpStairs(getDiscussedConcernNotDiscussed("walksupstairs_3y"));
		
		rbr.setInvestigationsImmunizationRecordVaccines(getDiscussedConcernNotDiscussed("recordVaccines_2y"));
		
		rbr.setNutritionAvoidSweetenedLiquids(getDiscussedConcernNotDiscussed("avoidSweetLiquids_2y"));
		rbr.setNutritionBreastfeeding(getDiscussedConcernNotDiscussed("breastfeeding_2y"));
		rbr.setNutritionCanadaFoodGuide(getDiscussedConcernNotDiscussed("foodguide_2y"));
		rbr.setNutritionMilk(getDiscussedConcernNotDiscussed("formulafeeding_2y"));
		rbr.setNutritionTransitionToLowerFatDiet(getDiscussedConcernNotDiscussed("lowerfatdiet_2y"));
		rbr.setNutritionVegeterianDiets(getDiscussedConcernNotDiscussed("vegetariandiets_2y"));

		//the yes no stuff
		rbr.setNutritionBreastfeedingYesNo(getYesNoUnknown("breastfeeding_2y_yes_no"));

		
		if (getRourkeStrValue("parentConcerns2y") != null) {
			rbr.setParentalCaregiverConcerns(getRourkeStrValue("parentConcerns2y"));
		}

		rbr.setPhysicalCornealLightReflex(getDiscussedConcernNotDiscussed("corneallightreflex_2y"));
		rbr.setPhysicalExamBloodPressure(getDiscussedConcernNotDiscussed("bloodpressure_2y"));
		rbr.setPhysicalExamEyes(getDiscussedConcernNotDiscussed("eyes_2y"));
		rbr.setPhysicalExamHearingScreening(getDiscussedConcernNotDiscussed("hearing_2y"));
		rbr.setPhysicalExamSnoringTonsil(getDiscussedConcernNotDiscussed("snoring_2y"));
		rbr.setPhysicalExamTeeth(getDiscussedConcernNotDiscussed("teeth_2y"));

		//signature
		if (getRourkeStrValue("signature2y") != null) {
			rbr.setSignature(getRourkeStrValue("signature2y"));
		}

		//problems and plans
		if (getRourkeStrValue("problemsPlans2y") != null) {
			rbr.setProblemsAndPlansOther(getRourkeStrValue("problemsPlans2y"));
		}
		EFormValue resource = this.eformValuesMap.get("RBR").get("problemsPlans2yRes");
		if (resource != null && resource.getVarValue() != null & !resource.getVarValue().isEmpty()) {
			for (String val : resource.getVarValue().split(",")) {
				ProblemsAndPlans pp = rbr.addNewProblemsAndPlans();
				pp.setStringValue(val);
			}
		}
	}

	/*
	 * 
	 * 
	 */
	private void populateRBRY45(RBRY45 rbr) {
		rbr.setFormVersion(FormVersion.X_2014);

		//visit date
		EFormValue date = this.eformValuesMap.get("RBR").get("visit_date_4y");
		if (date != null && date.getVarValue() != null && !date.getVarValue().isEmpty()) {
			rbr.setVisitDate(generateNonTimestampDate(date.getVarValue()));
		}

		if (stringToBigDecimal(getRourkeStrValue("headcirc_4y")) != null) {
			rbr.setHeadCirc(stringToBigDecimal(getRourkeStrValue("headcirc_4y")));
		}
		if (stringToBigDecimal(getRourkeStrValue("height_4y")) != null) {
			rbr.setHeight(stringToBigDecimal(getRourkeStrValue("height_4y")));
		}
		Integer weight = null;
		if ((weight = getRourkeIntValue("weight_4y")) != null) {
			rbr.setWeight(weight);
		}

		rbr.setDevelopment4YAskAnswerLotsOfQuestions(getDiscussedConcernNotDiscussed("asksanswersquestions_4y"));
		rbr.setDevelopment4YNoParentCaregiverConcerns(getDiscussedConcernNotDiscussed("noconcerns_4y"));
		rbr.setDevelopment4YTriesToComfort(getDiscussedConcernNotDiscussed("comfortsomeoneupset_4y"));
		rbr.setDevelopment4YUnderstandsThreePartDirections(getDiscussedConcernNotDiscussed("understandsdirections_4y"));
		rbr.setDevelopment4YUndoesButtonsZippers(getDiscussedConcernNotDiscussed("undoesbuttons_4y"));
		rbr.setDevelopment4YWalksUpDownStairs(getDiscussedConcernNotDiscussed("walksstairs_4y"));

		rbr.setDevelopment5YCooperates(getDiscussedConcernNotDiscussed("cooperateswithadult_5y"));
		rbr.setDevelopment5YCountsOutLoudOnFingers(getDiscussedConcernNotDiscussed("counts_5y"));
		rbr.setDevelopment5YDressesUndresses(getDiscussedConcernNotDiscussed("dresseswithlittlehelp_5y"));
		rbr.setDevelopment5YHops(getDiscussedConcernNotDiscussed("hopson1foot_5y"));
		rbr.setDevelopment5YNoParentCaregiverConcerns(getDiscussedConcernNotDiscussed("noconcerns_5y"));
		rbr.setDevelopment5YRetells(getDiscussedConcernNotDiscussed("retellsstory_5y"));
		rbr.setDevelopment5YSeparates(getDiscussedConcernNotDiscussed("partseasilyfromparent_5y"));
		rbr.setDevelopment5YSpeaksClearly(getDiscussedConcernNotDiscussed("speaksadultlike_5y"));
		rbr.setDevelopment5YThrowsAndCatchesBall(getDiscussedConcernNotDiscussed("throwscatchesball_5y"));

		rbr.setInvestigationsImmunizationRecordVaccines(getDiscussedConcernNotDiscussed("recordVaccines_4y"));

		rbr.setNutritionAvoidSweetenedLiquids(getDiscussedConcernNotDiscussed("avoidsweeteded_4y"));
		rbr.setNutritionCanadaFoodGuide(getDiscussedConcernNotDiscussed("foodguide_4y"));
		rbr.setNutritionMilk(getDiscussedConcernNotDiscussed("formulafeeding_4y"));
		rbr.setNutritionVegeterianDiets(getDiscussedConcernNotDiscussed("vegetariandiets_4y"));

		if (getRourkeStrValue("parentConcerns4y") != null) {
			rbr.setParentalCaregiverConcerns(getRourkeStrValue("parentConcerns4y"));
		}

		rbr.setPhysicalCornealLightReflex(getDiscussedConcernNotDiscussed("corneallightreflex_4y"));
		rbr.setPhysicalExamBloodPressure(getDiscussedConcernNotDiscussed("bloodpressure_4y"));
		rbr.setPhysicalExamEyes(getDiscussedConcernNotDiscussed("eyes_4y"));
		rbr.setPhysicalExamHearingScreening(getDiscussedConcernNotDiscussed("hearing_4y"));
		rbr.setPhysicalExamSnoringTonsil(getDiscussedConcernNotDiscussed("snoring_4y"));
		rbr.setPhysicalExamTeeth(getDiscussedConcernNotDiscussed("teeth_4y"));

		//signature
		if (getRourkeStrValue("signature4y") != null) {
			rbr.setSignature(getRourkeStrValue("signature4y"));
		}

		//problems and plans
		if (getRourkeStrValue("problemsPlans4y") != null) {
			rbr.setProblemsAndPlansOther(getRourkeStrValue("problemsPlans4y"));
		}
		EFormValue resource = this.eformValuesMap.get("RBR").get("problemsPlans4yRes");
		if (resource != null && resource.getVarValue() != null & !resource.getVarValue().isEmpty()) {
			for (String val : resource.getVarValue().split(",")) {
				ProblemsAndPlans pp = rbr.addNewProblemsAndPlans();
				pp.setStringValue(val);
			}
		}

	}

	private void populateRBRY23Y45(RBRY23Y45 rbr) {

		rbr.setEducationAdviceBehaviourDisciplineParentingSkills(getDiscussedConcernNotDiscussed("disciplineparenting_2y"));
		rbr.setEducationAdviceBehaviourFamilyConflict(getDiscussedConcernNotDiscussed("familyconflict_2y"));
		rbr.setEducationAdviceBehaviourHighRiskChildren(getDiscussedConcernNotDiscussed("highrisk_2y"));
		rbr.setEducationAdviceBehaviourParentalFatigue(getDiscussedConcernNotDiscussed("depression_2y"));
		rbr.setEducationAdviceBehaviourParentChildInteraction(getDiscussedConcernNotDiscussed("pcinteraction_2y"));
		rbr.setEducationAdviceBehaviourSiblings(getDiscussedConcernNotDiscussed("siblings_2y"));

		rbr.setEducationAdviceEnvironmentalHealthSecondHandSmoke(getDiscussedConcernNotDiscussed("secondhandsmoke_2y"));
		rbr.setEducationAdviceEnvironmentalPesticideExposure(getDiscussedConcernNotDiscussed("pesticide_2y"));
		rbr.setEducationAdviceEnvironmentalSerumLead(getDiscussedConcernNotDiscussed("serumlead_2y"));
		rbr.setEducationAdviceEnvironmentalSunExposure(getDiscussedConcernNotDiscussed("sunexposure_2y"));

		rbr.setEducationAdviceFamilyActiveLiving(getDiscussedConcernNotDiscussed("activehealthy_2y"));
		rbr.setEducationAdviceFamilyChildCare(getDiscussedConcernNotDiscussed("childcare_2y"));
		rbr.setEducationAdviceFamilyEncourageReading(getDiscussedConcernNotDiscussed("reading_2y"));
		rbr.setEducationAdviceFamilyHealthySleepHabits(getDiscussedConcernNotDiscussed("sleephabits_2y"));
		rbr.setEducationAdviceFamilySocializing(getDiscussedConcernNotDiscussed("socializing_2y"));

		rbr.setEducationAdviceInjuryPrevBikeHelmet(getDiscussedConcernNotDiscussed("bikehelmets_2y"));
		rbr.setEducationAdviceInjuryPrevCarbonMonoxideSmokeDet(getDiscussedConcernNotDiscussed("smokedetectors_2y"));
		rbr.setEducationAdviceInjuryPrevCarSeat(getDiscussedConcernNotDiscussed("carseat_2y"));
		rbr.setEducationAdviceInjuryPrevFalls(getDiscussedConcernNotDiscussed("falls_2y"));
		rbr.setEducationAdviceInjuryPrevFirearmSafety(getDiscussedConcernNotDiscussed("firearmsafety_2y"));
		rbr.setEducationAdviceInjuryPrevMatches(getDiscussedConcernNotDiscussed("matches_2y"));
		rbr.setEducationAdviceInjuryPrevWaterSafety(getDiscussedConcernNotDiscussed("watersafety_2y"));

		rbr.setEducationAdviceOtherComplementaryAltMedicine(getDiscussedConcernNotDiscussed("complementarymedn_2y"));
		rbr.setEducationAdviceOtherDentalCare(getDiscussedConcernNotDiscussed("dental_2y"));
		rbr.setEducationAdviceOtherNoOTCCoughColdMeds(getDiscussedConcernNotDiscussed("nootcmedn_2y"));
		rbr.setEducationAdviceOtherNoPacifiers(getDiscussedConcernNotDiscussed("nopacifier_2y"));
		rbr.setEducationAdviceOtherToiletTraining(getDiscussedConcernNotDiscussed("toiletlearning_2y"));
	}

	private void populateNdds(NDDS ndds, List<Integer> nddsFdids) {

		if (this.eformValuesMap.get("NDDS1M2M") != null) {
			populateNDDS1M2M(ndds.addNewNDDS1M2M());
		}
		if (this.eformValuesMap.get("NDDS4M") != null) {
			populateNDDS04M(ndds.addNewNDDS04M());
		}
		if (this.eformValuesMap.get("NDDS6M") != null) {
			populateNDDS06M(ndds.addNewNDDS06M());
		}
		if (this.eformValuesMap.get("NDDS9M") != null) {
			populateNDDS09M(ndds.addNewNDDS09M());
		}
		if (this.eformValuesMap.get("NDDS12M") != null) {
			populateNDDS12M(ndds.addNewNDDS12M());
		}
		if (this.eformValuesMap.get("NDDS15M") != null) {
			populateNDDS15M(ndds.addNewNDDS15M());
		}
		if (this.eformValuesMap.get("NDDS18M") != null) {
			populateNDDS18M(ndds.addNewNDDS18M());
		}

		if (this.eformValuesMap.get("NDDS24M") != null) {
			populateNDDS2Y(ndds.addNewNDDS2Y());
		}

		if(this.eformValuesMap.get("NDDS30M") != null) {
			populateNDDS30M(ndds.addNewNDDS30M());
		}

		if (this.eformValuesMap.get("NDDS3Y") != null) {
			populateNDDS3Y(ndds.addNewNDDS3Y());
		}
		if (this.eformValuesMap.get("NDDS4Y") != null) {
			populateNDDS4Y(ndds.addNewNDDS4Y());
		}
		if (this.eformValuesMap.get("NDDS5Y") != null) {
			populateNDDS5Y(ndds.addNewNDDS5Y());
		}
		if (this.eformValuesMap.get("NDDS6Y") != null) {
			populateNDDS6Y(ndds.addNewNDDS6Y());
		}

		
		
		Date d = eformDataDao.getLatestFormDateAndTimeForEforms(nddsFdids);
		if(d != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			ndds.setLastUpdateDateTime(cal);
		}
		
		//String id = BORNWbXmlGenerator.generateHash(nddsFdids);
		
		ndds.setSetID(1);
		ndds.setVersionID(1);
	}

	/*
	 * M1Q1_Yes
	 * M2Q9_No
	 * M2Q7_Not_Sure
	 * M2Q10_Sometimes
	 * M1Q4_Have_Not_Tried
	 * 
	 */
	private NDDSResponseCodes.Enum getNDDSAnswer(String name, String key) {
		if (this.eformValuesMap.get(name).get(key + "_Yes") != null) {
			return NDDSResponseCodes.Y;
		}
		if (this.eformValuesMap.get(name).get(key + "_No") != null) {
			return NDDSResponseCodes.N;
		}
		if (this.eformValuesMap.get(name).get(key + "_Not_Sure") != null) {
			return NDDSResponseCodes.NS;
		}
		if (this.eformValuesMap.get(name).get(key + "_Sometimes") != null) {
			return NDDSResponseCodes.S;
		}
		if (this.eformValuesMap.get(name).get(key + "_Have_Not_Tried") != null) {
			return NDDSResponseCodes.HT;
		}
		return null;
	}

	private void setNDDSAnswer(String formName, String formKey, String xmlName, Object ndds) {
		NDDSResponseCodes.Enum resp = null;
		if ((resp = getNDDSAnswer(formName, formKey)) != null) {
			try {
				BeanUtils.setProperty(ndds, xmlName, resp);
			} catch (Exception e) {
				MiscUtils.getLogger().warn("error setting XML value from NDDS", e);
			}
		}
	}

	private void setNDDSAnswers(String formName, Object ndds, int numQuestions) {
		for (int x = 1; x <= numQuestions; x++) {
			String idx = String.valueOf(x);
			idx = (idx.length() == 1) ? "0" + idx : idx;
			setNDDSAnswer(formName, "q" + x, "NDDSQ" + idx, ndds);
		}
	}

	private void populateNDDS1M2M(NDDS1M2M ndds) {
		ndds.setFormVersion(FormVersion.X_2011);

		setNDDSCompletionDateTime("NDDS1M2M", ndds);

		setNDDSAnswer("NDDS1M2M", "M1Q1", "NDDSQ01", ndds);
		setNDDSAnswer("NDDS1M2M", "M1Q2", "NDDSQ02", ndds);
		setNDDSAnswer("NDDS1M2M", "M1Q3", "NDDSQ03", ndds);
		setNDDSAnswer("NDDS1M2M", "M1Q4", "NDDSQ04", ndds);

		setNDDSAnswer("NDDS1M2M", "M2Q1", "NDDSQ05", ndds);
		setNDDSAnswer("NDDS1M2M", "M2Q2", "NDDSQ06", ndds);
		setNDDSAnswer("NDDS1M2M", "M2Q3", "NDDSQ07", ndds);
		setNDDSAnswer("NDDS1M2M", "M2Q4", "NDDSQ08", ndds);
		setNDDSAnswer("NDDS1M2M", "M2Q5", "NDDSQ09", ndds);
		setNDDSAnswer("NDDS1M2M", "M2Q6", "NDDSQ10", ndds);
		setNDDSAnswer("NDDS1M2M", "M2Q7", "NDDSQ11", ndds);
		setNDDSAnswer("NDDS1M2M", "M2Q8", "NDDSQ12", ndds);
		setNDDSAnswer("NDDS1M2M", "M2Q9", "NDDSQ13", ndds);
		setNDDSAnswer("NDDS1M2M", "M2Q10", "NDDSQ14", ndds);
		setNDDSAnswer("NDDS1M2M", "M2Q11", "NDDSQ15", ndds);
		setNDDSAnswer("NDDS1M2M", "M2Q12", "NDDSQ16", ndds);
		setNDDSAnswer("NDDS1M2M", "M2Q13", "NDDSQ17", ndds);
	}

	private void setNDDSCompletionDateTime(String formName, Object ndds) {
		EFormValue date = this.eformValuesMap.get(formName).get("today");
		if (date != null && date.getVarValue() != null && !date.getVarValue().isEmpty()) {
			try {
				Date x = visitDateFormatter.parse(date.getVarValue());
				Calendar c = Calendar.getInstance();
				c.setTime(x);

				BeanUtils.setProperty(ndds, "NDDSCompletionDateTime", c);
			} catch (Exception e) {
				MiscUtils.getLogger().warn("invalid date " + date.getVarValue());
			}
		}
	}

	private void populateNDDS04M(NDDS04M ndds) {
		ndds.setFormVersion(FormVersion.X_2011);
		setNDDSCompletionDateTime("NDDS4M", ndds);
		setNDDSAnswers("NDDS4M", ndds, 12);
	}

	private void populateNDDS06M(NDDS06M ndds) {
		ndds.setFormVersion(FormVersion.X_2011);
		setNDDSCompletionDateTime("NDDS6M", ndds);
		setNDDSAnswers("NDDS6M", ndds, 14);
	}

	private void populateNDDS09M(NDDS09M ndds) {
		ndds.setFormVersion(FormVersion.X_2011);
		setNDDSCompletionDateTime("NDDS9M", ndds);
		setNDDSAnswers("NDDS9M", ndds, 15);
	}

	private void populateNDDS12M(NDDS12M ndds) {
		ndds.setFormVersion(FormVersion.X_2011);

		setNDDSCompletionDateTime("NDDS12M", ndds);
		setNDDSAnswers("NDDS12M", ndds, 16);

	}

	private void populateNDDS15M(NDDS15M ndds) {
		ndds.setFormVersion(FormVersion.X_2011);
		setNDDSCompletionDateTime("NDDS15M", ndds);
		setNDDSAnswers("NDDS15M", ndds, 16);

	}

	private void populateNDDS18M(NDDS18M ndds) {
		ndds.setFormVersion(FormVersion.X_2011);
		setNDDSCompletionDateTime("NDDS18M", ndds);
		setNDDSAnswers("NDDS18M", ndds, 17);

	}
	
	private void populateNDDS30M(NDDS30M ndds) {
		ndds.setFormVersion(FormVersion.X_2011);
		setNDDSCompletionDateTime("NDDS30M", ndds);
		setNDDSAnswers("NDDS30M", ndds, 16);
	}

	private void populateNDDS2Y(NDDS2Y ndds) {
		ndds.setFormVersion(FormVersion.X_2011);
		setNDDSCompletionDateTime("NDDS24M", ndds);
		setNDDSAnswers("NDDS24M", ndds, 17);

	}
	
	private void populateNDDS3Y(NDDS3Y ndds) {
		ndds.setFormVersion(FormVersion.X_2011);
		setNDDSCompletionDateTime("NDDS3Y", ndds);
		setNDDSAnswers("NDDS3Y", ndds, 17);
	}

	private void populateNDDS4Y(NDDS4Y ndds) {
		ndds.setFormVersion(FormVersion.X_2011);
		setNDDSCompletionDateTime("NDDS4Y", ndds);
		setNDDSAnswers("NDDS4Y", ndds, 20);
	}

	private void populateNDDS5Y(NDDS5Y ndds) {
		ndds.setFormVersion(FormVersion.X_2011);
		setNDDSCompletionDateTime("NDDS5Y", ndds);
		setNDDSAnswers("NDDS5Y", ndds, 22);
	}

	private void populateNDDS6Y(NDDS6Y ndds) {
		ndds.setFormVersion(FormVersion.X_2011);
		setNDDSCompletionDateTime("NDDS6Y", ndds);
		setNDDSAnswers("NDDS6Y", ndds, 22);
	}

	
   protected ca.bornontario.wb.YesNoUnknown.Enum getSumRptAnswer(String key) {
	   String val = getValueFromEForm("SUMRPT", key);
		if(val == null) {
			return ca.bornontario.wb.YesNoUnknown.N;
		}
		if(val != null && "on".equals(val)) {
			return ca.bornontario.wb.YesNoUnknown.Y;
		}
		return ca.bornontario.wb.YesNoUnknown.U;
   }

	
	private void populateSumRptMarkers(SUMRPTMARKERS markers) {

		markers.setSetID(this.eformFdidMap.get("SUMRPT"));
		markers.setVersionID(1);
		markers.setLastUpdateDateTime(formDateTimeToCal(eformFdidMap.get("SUMRPT")));
		markers.setNoConcerns(getSumRptAnswer("NoConcerns"));
		Integer apgar1 = getIntFromEForm("SUMRPT", "APGAR1");
		if(apgar1 != null) {
		        markers.setAPGAR1(apgar1);
		}
		Integer apgar5 = getIntFromEForm("SUMRPT", "APGAR5");
		if(apgar5 != null) {
		        markers.setAPGAR5(apgar5);
		}

		markers.setFSA(getValueFromEForm("SUMRPT", "fsa"));
		if(getValueFromEForm("RPTSUM", "MoreThanOneDevAreaAffectedYes") != null) {
			markers.setMoreThanOneDevAreaAffected(ca.bornontario.wb.YesNoUnknown.Y);
		} else if(getValueFromEForm("RPTSUM", "MoreThanOneDevAreaAffectedNo") != null) {
			markers.setMoreThanOneDevAreaAffected(ca.bornontario.wb.YesNoUnknown.N);
		} else {
			markers.setMoreThanOneDevAreaAffected(ca.bornontario.wb.YesNoUnknown.U);
		}
				
		markers.setPremature(getSumRptAnswer("Premature"));
		markers.setPremature(getSumRptAnswer("HighRisk"));

		markers.setSecondHandSmokeExposureInUtero(getSumRptAnswer("SecondHandSmokeInUtero"));
		markers.setSecondHandSmokeExposureSinceBirth(getSumRptAnswer("SecondHandSmokeSinceBirth"));
		markers.setSecondHandSmokeExposureNoExposure(getSumRptAnswer("SecondHandSmokeNoExposure"));
		
		if(getValueFromEForm("RPTSUM", "SubstanceAbuseInUteroYes") != null) {
			markers.setSubstanceAbuseInUtero(ca.bornontario.wb.YesNoUnknown.Y);
		}
		else if(getValueFromEForm("RPTSUM", "SubstanceAbuseInUteroNo") != null) {
	        markers.setSubstanceAbuseInUtero(ca.bornontario.wb.YesNoUnknown.N);
		} else {
	        markers.setSubstanceAbuseInUtero(ca.bornontario.wb.YesNoUnknown.U);
		}
			
		if(getValueFromEForm("RPTSUM", "SubstanceAbuseInUteroYes") != null) {
	        markers.setSubstanceAbuseAlcohol(getSumRptAnswer("SubstanceAbuseAlcohol"));
	        markers.setSubstanceAbuseDrugs(getSumRptAnswer("SubstanceAbuseDrugs"));
		}
	
		if(getValueFromEForm("RPTSUM", "NeedForAddAssessmentYes") != null) {
			markers.setNeedForAddAssessment(ca.bornontario.wb.YesNoUnknown.Y);
		}
		else if(getValueFromEForm("RPTSUM", "NeedForAddAssessmentNo") != null) {
	        markers.setNeedForAddAssessment(ca.bornontario.wb.YesNoUnknown.N);
		} else {
			markers.setNeedForAddAssessment(ca.bornontario.wb.YesNoUnknown.U);
		}
			
	}

	/*
	 * Populates from rourke, master record, and chart
	 */
	private void populatePatientInfo(PatientInfo patientInfo, Integer demographicNo, boolean useClinicInfoForOrganizationId) {
		patientInfo.setBirthWeight(0);

		populatePatientInfoFromRourke(patientInfo);
		populatePatientInfoFromDemographic(patientInfo, demographicNo);
		populatePatientInfoFromPatientChart(patientInfo, demographicNo);

		patientInfo.setOrganizationID(OscarProperties.getInstance().getProperty("born_orgcode"));

		if (useClinicInfoForOrganizationId) {
			ClinicInfoDao clinicInfoDao = SpringUtils.getBean(ClinicInfoDao.class);
			ClinicInfoDataObject clinicInfo = clinicInfoDao.getClinic();
			patientInfo.setOrganizationID(clinicInfo.getFacilityName());
		}
	}

	private void populatePatientInfoFromRourke(PatientInfo patientInfo) {

		//need a map with all the latest forms for patient..by name
		patientInfo.setBirthWeight(0);
		Map<String, EFormValue> values = eformValuesMap.get("RBR");
		if (values != null) {
			EFormValue value = values.get("birth_wt");
			if (value != null && StringUtils.filled(value.getVarValue())) {
				patientInfo.setBirthWeight(Integer.valueOf(value.getVarValue()));
			}

			value = values.get("head_circ");
			if (value != null && StringUtils.filled(value.getVarValue())) {
				patientInfo.setBirthHeadCirc(stringToBigDecimal(value.getVarValue()));
			}

			value = values.get("birth_length");
			if (value != null && StringUtils.filled(value.getVarValue())) {
				patientInfo.setBirthLength(stringToBigDecimal(value.getVarValue()));
			}

			value = values.get("discharge_wt");
			if (value != null && StringUtils.filled(value.getVarValue())) {
				patientInfo.setDischargeWeight(Integer.valueOf(value.getVarValue()));
			}

			value = values.get("gestational_age");
			if (value != null && StringUtils.filled(value.getVarValue())) {
				patientInfo.setGestationalAge(Integer.valueOf(value.getVarValue()));
			}
		}

	}

	private void populatePatientInfoFromDemographic(PatientInfo patientInfo, Integer demographicNo) {
		//Mandatory fields
		Demographic demographic = demographicDao.getDemographicById(demographicNo);

		patientInfo.setUniqueVendorIDSequence(demographicNo.toString());
		patientInfo.setFirstName(demographic.getFirstName());
		patientInfo.setLastName(demographic.getLastName());
		patientInfo.setDOB(new XmlCalendar(demographic.getBirthDayAsString()));

		if ("M".equals(demographic.getSex())) patientInfo.setGender(Gender.M);
		else if ("F".equals(demographic.getSex())) patientInfo.setGender(Gender.F);
		else patientInfo.setGender(Gender.U);

		patientInfo.setChartNumber(String.valueOf(demographic.getDemographicNo()));
		
		if (StringUtils.filled(demographic.getHin())) patientInfo.setHealthCardNum(demographic.getHin());
		else patientInfo.setHealthCardNum("0");

		patientInfo.setHealthCardType(0);
		if (StringUtils.filled(demographic.getHcType())) {
			if (demographic.getHcType().equals("ON")) patientInfo.setHealthCardType(1);
			if (demographic.getHcType().equals("QC")) patientInfo.setHealthCardType(2);
		}

		//end: Mandatory fields

		if (StringUtils.filled(demographic.getAddress())) patientInfo.setResidentAddressLine1(demographic.getAddress());
		if (StringUtils.filled(demographic.getCity())) patientInfo.setResidentCity(demographic.getCity());
		if (StringUtils.filled(demographic.getProvince())) {
			String province = demographic.getProvince();
			if (province.equals("AB")) patientInfo.setResidentCountryProvince(CountryProvince.CA_AB);
			else if (province.equals("BC")) patientInfo.setResidentCountryProvince(CountryProvince.CA_BC);
			else if (province.equals("MB")) patientInfo.setResidentCountryProvince(CountryProvince.CA_MB);
			else if (province.equals("NB")) patientInfo.setResidentCountryProvince(CountryProvince.CA_NB);
			else if (province.equals("NL")) patientInfo.setResidentCountryProvince(CountryProvince.CA_NL);
			else if (province.equals("NT")) patientInfo.setResidentCountryProvince(CountryProvince.CA_NT);
			else if (province.equals("NS")) patientInfo.setResidentCountryProvince(CountryProvince.CA_NS);
			else if (province.equals("NU")) patientInfo.setResidentCountryProvince(CountryProvince.CA_NU);
			else if (province.equals("ON")) patientInfo.setResidentCountryProvince(CountryProvince.CA_ON);
			else if (province.equals("PE")) patientInfo.setResidentCountryProvince(CountryProvince.CA_PE);
			else if (province.equals("QC")) patientInfo.setResidentCountryProvince(CountryProvince.CA_QC);
			else if (province.equals("SK")) patientInfo.setResidentCountryProvince(CountryProvince.CA_SK);
			else if (province.equals("YT")) patientInfo.setResidentCountryProvince(CountryProvince.CA_YT);
			else if (province.startsWith("US")) patientInfo.setResidentCountryProvince(CountryProvince.USA);
			else patientInfo.setResidentCountryProvince(CountryProvince.UNKN);
		}
		if (StringUtils.filled(demographic.getPostal())) {
			patientInfo.setResidentPostalCode(demographic.getPostal().replace(" ", ""));
		}

		Provider provider = providerDao.getProvider(demographic.getProviderNo());
		if (provider != null && StringUtils.filled(provider.getPractitionerNo())) {
			patientInfo.setProviderNumber(demographic.getProviderNo());
			patientInfo.setCPSONumber(provider.getPractitionerNo());
		}
	}

	private void populatePatientInfoFromPatientChart(PatientInfo patientInfo, Integer demographicNo) {
		CaseManagementManager cmm = SpringUtils.getBean(CaseManagementManager.class);
		List<CaseManagementNote> lcmn = cmm.getNotes(demographicNo.toString());
		String famHist = null, riskFactors = null;

		for (CaseManagementNote cmn : lcmn) {
			Set<CaseManagementIssue> sisu = cmn.getIssues();
			for (CaseManagementIssue isu : sisu) {
				if (isu.getIssue() == null) continue;
				if (!isu.getIssue().getType().equals("system")) continue;

				String _issue = isu.getIssue().getCode();
				if (_issue.equals("FamHistory")) {
					if (famHist == null) famHist = cmn.getNote();
					else famHist += "\n" + cmn.getNote();
					break;
				} else if (_issue.equals("RiskFactors")) {
					if (riskFactors == null) riskFactors = cmn.getNote();
					else riskFactors += "\n" + cmn.getNote();
					break;
				}
			}
		}
		if (famHist != null) {
			if (famHist.length() > 250) famHist = famHist.substring(0, 250);
			patientInfo.setFamilyHistory(famHist);
		}
		if (riskFactors != null) {
			if (riskFactors.length() > 250) riskFactors = riskFactors.substring(0, 250);
			patientInfo.setPastProblemsRiskFactor(riskFactors);
		}
	}

	private Calendar formDateTimeToCal(Integer fdid) {
		EFormData eformData = eformDataDao.find(fdid);

		String formDate = dateFormatter.format(eformData.getFormDate());
		String formTime = timeFormatter.format(eformData.getFormTime());
		String dateTime = formDate + " " + formTime;
		Date formDateTime = null;
		try {
			formDateTime = dateTimeFormatter.parse(dateTime);
		} catch (ParseException e) {

		}

		return dateToCal(formDateTime);
	}

	private Calendar dateToCal(Date inDate) {
		String date = new SimpleDateFormat("yyyy-MM-dd").format(inDate);
		String time = timeFormatter.format(inDate);
		try {
			XmlCalendar x = new XmlCalendar(date + "T" + time);
			return x;
		} catch (Exception ex) {
			XmlCalendar x = new XmlCalendar("0001-01-01T00:00:00");
			return x;
		}
	}

	private BigDecimal stringToBigDecimal(String n) {
		if (n != null) return (new BigDecimal(n));
		return null;
	}

	private Integer getMaxFdid(Integer fid, Integer demographicNo) {
		Integer fdid = eformDataDao.getLatestFdid(fid, demographicNo);

		return fdid;
	}

	private boolean isAgeLessThan7y(PatientInfo patientInfo) {
		
		
		LocalDate date1 = new LocalDate(LocalDate.fromCalendarFields(patientInfo.getDOB()));
        LocalDate date2 = new LocalDate(new java.util.Date());
        PeriodType monthDay = PeriodType.months();
        Period difference = new Period(date1, date2, monthDay);
        int months = difference.getMonths();
        
        if(months >= ((12*6)+6)) {
        	return false;
        }
        return true;
        
     
	}

	/*
	private boolean checkUploadedToBorn(Integer fdid) {
		EFormValue value = eformValueDao.findByFormDataIdAndKey(fdid, UPLOADED_TO_BORN);
		return (value!=null && value.getVarValue().equals(VALUE_YES));
	}
	*/

	private boolean passBirthweightValidation(PatientInfo patientInfo) {
		int ga = patientInfo.getGestationalAge();
		int wks = (int) Math.ceil((double)ga / 7);
		if (wks >= 20 && wks <= 22) {
			if (patientInfo.getBirthWeight() >= 100 && patientInfo.getBirthWeight() <= 800) {
				return true;
			}
		} else if (wks == 23) {
			if (patientInfo.getBirthWeight() >= 200 && patientInfo.getBirthWeight() <= 900) {
				return true;
			}
		} else if (wks == 24) {
			if (patientInfo.getBirthWeight() >= 250 && patientInfo.getBirthWeight() <= 1050) {
				return true;
			}
		} else if (wks == 25) {
			if (patientInfo.getBirthWeight() >= 300 && patientInfo.getBirthWeight() <= 1200) {
				return true;
			}
		} else if (wks == 26) {
			if (patientInfo.getBirthWeight() >= 450 && patientInfo.getBirthWeight() <= 1550) {
				return true;
			}
		} else if (wks == 27) {
			if (patientInfo.getBirthWeight() >= 500 && patientInfo.getBirthWeight() <= 1800) {
				return true;
			}
		} else if (wks == 28) {
			if (patientInfo.getBirthWeight() >= 550 && patientInfo.getBirthWeight() <= 2100) {
				return true;
			}
		} else if (wks == 29) {
			if (patientInfo.getBirthWeight() >= 600 && patientInfo.getBirthWeight() <= 2400) {
				return true;
			}
		} else if (wks == 30) {
			if (patientInfo.getBirthWeight() >= 600 && patientInfo.getBirthWeight() <= 2700) {
				return true;
			}
		} else if (wks == 31) {
			if (patientInfo.getBirthWeight() >= 650 && patientInfo.getBirthWeight() <= 2900) {
				return true;
			}
		} else if (wks == 32) {
			if (patientInfo.getBirthWeight() >= 700 && patientInfo.getBirthWeight() <= 3200) {
				return true;
			}
		} else if (wks == 33) {
			if (patientInfo.getBirthWeight() >= 800 && patientInfo.getBirthWeight() <= 3600) {
				return true;
			}
		} else if (wks == 34) {
			if (patientInfo.getBirthWeight() >= 900 && patientInfo.getBirthWeight() <= 3900) {
				return true;
			}
		} else if (wks == 35) {
			if (patientInfo.getBirthWeight() >= 1000 && patientInfo.getBirthWeight() <= 4200) {
				return true;
			}
		} else if (wks == 36) {
			if (patientInfo.getBirthWeight() >= 1200 && patientInfo.getBirthWeight() <= 4500) {
				return true;
			}
		} else if (wks == 37) {
			if (patientInfo.getBirthWeight() >= 1500 && patientInfo.getBirthWeight() <= 5100) {
				return true;
			}
		} else if (wks == 38) {
			if (patientInfo.getBirthWeight() >= 1600 && patientInfo.getBirthWeight() <= 5100) {
				return true;
			}
		} else if (wks == 39) {
			if (patientInfo.getBirthWeight() >= 1800 && patientInfo.getBirthWeight() <= 5400) {
				return true;
			}
		} else if (wks == 40) {
			if (patientInfo.getBirthWeight() >= 1900 && patientInfo.getBirthWeight() <= 5500) {
				return true;
			}
		} else if (wks == 41) {
			if (patientInfo.getBirthWeight() >= 2100 && patientInfo.getBirthWeight() <= 5700) {
				return true;
			}
		} else if (wks == 42) {
			if (patientInfo.getBirthWeight() >= 2400 && patientInfo.getBirthWeight() <= 6300) {
				return true;
			}
		} else if (wks >= 43) {
			if (patientInfo.getBirthWeight() >= 2600 && patientInfo.getBirthWeight() <= 6500) {
				return true;
			}
		}
		return false;
	}
	
	
	private boolean hasValue(String name, String key) {
		Map<String, EFormValue> m = this.eformValuesMap.get(name);
		if (m != null) {
			EFormValue val = m.get(key);
			if (val != null && val.getVarValue() != null && !val.getVarValue().isEmpty()) {
				return true;
			}
		}
		return false;
	}

	/*
	 * 0	Not discussed
	 * 1	Discussed and no concerns
	 * 2	Concerns
	 */
	private int getDiscussedConcernNotDiscussed(String key) {
		Map<String, EFormValue> m = this.eformValuesMap.get("RBR");
		if (m.get(key + "_o") != null && "on".equals(m.get(key + "_o").getVarValue())) {
			return 1;
		}
		if (m.get(key + "_x") != null && "on".equals(m.get(key + "_x").getVarValue())) {
			return 2;
		}
		return 0;

	}
	
	private YesNoUnknown.Enum getYesNoUnknown(String key) {
		Map<String, EFormValue> m = this.eformValuesMap.get("RBR");
		if (m.get(key) != null && "Yes".equals(m.get(key).getVarValue())) {
			return YesNoUnknown.Y;
		}
		if (m.get(key) != null && "No".equals(m.get(key).getVarValue())) {
			return YesNoUnknown.N;
		}
		return YesNoUnknown.U;
	}

	private String getValueFromEForm(String name, String key) {
		Map<String, EFormValue> m = this.eformValuesMap.get(name);
		if (m != null) {
			return (m.get(key) != null ? m.get(key).getVarValue() : null);
		}
		return null;
	}
	
	protected Integer getIntFromEForm(String name, String key) {
		Map<String, EFormValue> m = this.eformValuesMap.get(name);
		if (m != null) {
			String v =  (m.get(key) != null ? m.get(key).getVarValue() : null);
			if(v!=null) {
				try {
					int iv = Integer.parseInt(v);
					return iv;
				}catch(NumberFormatException e) {
					return null;
				}
			}
		}
		return null;
	}

	private String getRourkeStrValue(String key) {
		return getValueFromEForm("RBR", key);
	}

	private Integer getRourkeIntValue(String key) {
		String str = getRourkeStrValue(key);
		if(str == null) {
			MiscUtils.getLogger().info("couldn't find value for integer key " +  key + ":" + str);
			return null;
		}
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			MiscUtils.getLogger().warn("error parsing expected integer: " + str);
		}
		return null;
	}

	public Map<String, Integer> getEformFdidMap() {
		return eformFdidMap;
	}

	
	public Map<String, EForm> getEformMap() {
		return eformMap;
	}

	public static String generateHash(Collection<Integer> ids) {
		long idToHash = 0;
		for (Integer id : ids) {
			idToHash += id.intValue();
		}

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(String.valueOf(idToHash).getBytes());
	
			byte byteData[] = md.digest();
	
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
	
			return sb.toString();
		}catch(NoSuchAlgorithmException e) {
			MiscUtils.getLogger().error("Error",e);
		}
		return null;
	}

}
