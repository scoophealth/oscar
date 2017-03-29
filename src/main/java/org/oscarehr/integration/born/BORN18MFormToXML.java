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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.xmlbeans.XmlCalendar;
import org.apache.xmlbeans.XmlOptions;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.EFormDataDao;
import org.oscarehr.common.dao.EFormValueDao;
import org.oscarehr.common.dao.PreventionDao;
import org.oscarehr.common.dao.PreventionExtDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.EFormData;
import org.oscarehr.common.model.EFormValue;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.common.model.PreventionExt;
import org.oscarehr.common.model.Provider;
import org.oscarehr.sharingcenter.dao.ClinicInfoDao;
import org.oscarehr.sharingcenter.model.ClinicInfoDataObject;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.oscarPrevention.PreventionDisplayConfig;
import oscar.util.StringUtils;
import ca.bornontario.x18MEWBV.BORN18MEWBVBatch;
import ca.bornontario.x18MEWBV.BORN18MEWBVBatchDocument;
import ca.bornontario.x18MEWBV.CountryProvince;
import ca.bornontario.x18MEWBV.Gender;
import ca.bornontario.x18MEWBV.IMMUNIZATION;
import ca.bornontario.x18MEWBV.M18MARKERS;
import ca.bornontario.x18MEWBV.NDDS;
import ca.bornontario.x18MEWBV.PatientInfo;
import ca.bornontario.x18MEWBV.ProblemsAndPlans;
import ca.bornontario.x18MEWBV.RBR;
import ca.bornontario.x18MEWBV.RBRM18;
import ca.bornontario.x18MEWBV.YesNoUnknown;


public class BORN18MFormToXML {
	
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");
	private SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
	private SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	EFormValueDao eformValueDao = SpringUtils.getBean(EFormValueDao.class);
	EFormDataDao eformDataDao = SpringUtils.getBean(EFormDataDao.class);
	
	String demographicNo = null;
	HashMap<String, YesNoUnknown.Enum> nddsQ = new HashMap<String, YesNoUnknown.Enum>();
	HashMap<String, Integer> preventionType = new HashMap<String, Integer>();
	final String[] nddsQkey = {"identifypictures","usegestures","followdirections","make4consonants","point3bodyparts","say20words","holdcupdrink","eatfingerfood","helpwithdressing","walkupstairs","walkalone","squatpickstand","pushpullwalk","stack3blocks","showaffection","pointtoshow","lookwhentalk"};
	
	public BORN18MFormToXML(Integer demographicNo) {
		this.demographicNo = demographicNo.toString();
	}
	
	public boolean addXmlToStream(LoggedInInfo loggedInInfo, Writer os, XmlOptions opts, Integer rourkeFdid, Integer nddsFdid, Integer report18mFdid) throws IOException {
		return addXmlToStream(loggedInInfo, os,opts,rourkeFdid,nddsFdid, report18mFdid,false);
	}
	
	public boolean addXmlToStream(LoggedInInfo loggedInInfo, Writer os, XmlOptions opts, Integer rourkeFdid, Integer nddsFdid, Integer report18mFdid, boolean useClinicInfoForOrganizationId) throws IOException {
		if (rourkeFdid==null && nddsFdid==null && report18mFdid==null) return false;
	    
		BORN18MEWBVBatchDocument bornBatchDocument = ca.bornontario.x18MEWBV.BORN18MEWBVBatchDocument.Factory.newInstance();
		BORN18MEWBVBatch bornBatch = bornBatchDocument.addNewBORN18MEWBVBatch();
		PatientInfo patientInfo = bornBatch.addNewPatientInfo();

		propulatePatientInfo(patientInfo, rourkeFdid, useClinicInfoForOrganizationId);
		if (nddsFdid!=null) propulateNdds(patientInfo.addNewNDDS(), nddsFdid);
		if (rourkeFdid!=null) propulateRourke(loggedInInfo, patientInfo.addNewRBR(), rourkeFdid);
		if (report18mFdid!=null) propulateM18Markers(patientInfo.addNewM18MARKERS(), report18mFdid);

		//xml validation
		XmlOptions m_validationOptions = new XmlOptions();		
		ArrayList<Object> validationErrors = new ArrayList<Object>();
		m_validationOptions.setErrorListener(validationErrors);
		if(!bornBatchDocument.validate(m_validationOptions)) {
			MiscUtils.getLogger().warn("forms failed validation");
			for(Object o:validationErrors) {
				MiscUtils.getLogger().warn(o);
			}
		}
		bornBatchDocument.save(os,opts);
		return true;
	}
	
	private void propulatePatientInfo(PatientInfo patientInfo, Integer rourkeFdid, boolean useClinicInfoForOrganizationId) {
		if (rourkeFdid!=null) propulatePatientInfoFromRourke(patientInfo, rourkeFdid);
		else patientInfo.setBirthWeight(0); //Birth weight is mandatory
		
		propulatePatientInfoFromDemographic(patientInfo);
		propulatePatientInfoFromPatientChart(patientInfo);
		
		patientInfo.setOrganizationID(OscarProperties.getInstance().getProperty("born18m_orgcode"));
	
		if(useClinicInfoForOrganizationId) {
			ClinicInfoDao clinicInfoDao = SpringUtils.getBean(ClinicInfoDao.class);
			ClinicInfoDataObject clinicInfo = clinicInfoDao.getClinic();
			patientInfo.setOrganizationID(clinicInfo.getFacilityName());
		} 
	}
	
	private void propulatePatientInfoFromRourke(PatientInfo patientInfo, Integer rourkeFdid) {
		//Mandatory field
		EFormValue value = eformValueDao.findByFormDataIdAndKey(rourkeFdid, "birth_wt");
		if (value!=null && StringUtils.filled(value.getVarValue())) {
			patientInfo.setBirthWeight(Integer.valueOf(value.getVarValue()));
		} else {
			patientInfo.setBirthWeight(0);
		}
		//end: Mantory field
		
		value = eformValueDao.findByFormDataIdAndKey(rourkeFdid, "head_circ");
		if (value!=null && StringUtils.filled(value.getVarValue())) {
			patientInfo.setBirthHeadCirc(stringToBigDecimal(value.getVarValue()));
		}
		
		value = eformValueDao.findByFormDataIdAndKey(rourkeFdid, "birth_length");
		if (value!=null && StringUtils.filled(value.getVarValue())) {
			patientInfo.setBirthLength(stringToBigDecimal(value.getVarValue()));
		}
		
		value = eformValueDao.findByFormDataIdAndKey(rourkeFdid, "discharge_wt");
		if (value!=null && StringUtils.filled(value.getVarValue())) {
			patientInfo.setDischargeWeight(Integer.valueOf(value.getVarValue()));
		}
		
		value = eformValueDao.findByFormDataIdAndKey(rourkeFdid, "gestational_age");
		if (value!=null && StringUtils.filled(value.getVarValue())) {
			patientInfo.setGestationalAge(Integer.valueOf(value.getVarValue()));
		}
	}
	
	private void propulatePatientInfoFromDemographic(PatientInfo patientInfo) {
		//Mandatory fields
		Demographic demographic = demographicDao.getDemographic(demographicNo);

		patientInfo.setUniqueVendorIDSequence(demographicNo);
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
		if (provider!=null && StringUtils.filled(provider.getPractitionerNo())) {
			patientInfo.setProviderNumber(demographic.getProviderNo());
			patientInfo.setCPSONumber(provider.getPractitionerNo());
		}
	}

	private void propulatePatientInfoFromPatientChart(PatientInfo patientInfo) {
		CaseManagementManager cmm = SpringUtils.getBean(CaseManagementManager.class);
		List<CaseManagementNote> lcmn = cmm.getNotes(demographicNo);
		String famHist=null, riskFactors=null;
		
		for (CaseManagementNote cmn : lcmn) {
			Set<CaseManagementIssue> sisu = cmn.getIssues();
			for (CaseManagementIssue isu : sisu) {
				if (isu.getIssue()==null) continue;
				if (!isu.getIssue().getType().equals("system")) continue;
				
				String _issue = isu.getIssue().getCode();
				if (_issue.equals("FamHistory")) {
					if (famHist==null) famHist = cmn.getNote();
					else famHist += "\n"+cmn.getNote();
					break;
				} else if (_issue.equals("RiskFactors")) {
					if (riskFactors==null) riskFactors = cmn.getNote();
					else riskFactors += "\n"+cmn.getNote();
					break;
				}
			}
		}
		if (famHist!=null) {
			if (famHist.length()>250) famHist = famHist.substring(0, 250);
			patientInfo.setFamilyHistory(famHist);
		}
		if (riskFactors!=null) {
			if (riskFactors.length()>250) riskFactors = riskFactors.substring(0, 250);
			patientInfo.setPastProblemsRiskFactor(riskFactors);
		}
	}
	
	private void propulateNdds(NDDS ndds, Integer fdid) {
		for (String key : nddsQkey) {
			nddsQ.put(key, YesNoUnknown.U);
		}
		
		List<EFormValue> vars = eformValueDao.findByFormDataId(fdid);
		for (EFormValue var : vars) {
			String varName = var.getVarName();
			if (StringUtils.empty(varName)) continue;
			
			String varNameKey = varName.substring(0, varName.length()-1);
			String varNameYN = varName.substring(varName.length()-1);
			if (nddsQ.get(varNameKey)!=null) {
				if (varNameYN.equals("Y")) nddsQ.put(varNameKey, YesNoUnknown.Y);
				else if (varNameYN.equals("N")) nddsQ.put(varNameKey, YesNoUnknown.N);
			}
		}
		
		ndds.setNDDSQ01(nddsQ.get(nddsQkey[0]));
		ndds.setNDDSQ02(nddsQ.get(nddsQkey[1]));
		ndds.setNDDSQ03(nddsQ.get(nddsQkey[2]));
		ndds.setNDDSQ04(nddsQ.get(nddsQkey[3]));
		ndds.setNDDSQ05(nddsQ.get(nddsQkey[4]));
		ndds.setNDDSQ06(nddsQ.get(nddsQkey[5]));
		ndds.setNDDSQ07(nddsQ.get(nddsQkey[6]));
		ndds.setNDDSQ08(nddsQ.get(nddsQkey[7]));
		ndds.setNDDSQ09(nddsQ.get(nddsQkey[8]));
		ndds.setNDDSQ10(nddsQ.get(nddsQkey[9]));
		ndds.setNDDSQ11(nddsQ.get(nddsQkey[10]));
		ndds.setNDDSQ12(nddsQ.get(nddsQkey[11]));
		ndds.setNDDSQ13(nddsQ.get(nddsQkey[12]));
		ndds.setNDDSQ14(nddsQ.get(nddsQkey[13]));
		ndds.setNDDSQ15(nddsQ.get(nddsQkey[14]));
		ndds.setNDDSQ16(nddsQ.get(nddsQkey[15]));
		ndds.setNDDSQ17(nddsQ.get(nddsQkey[16]));
		
		ndds.setLastUpdateDateTime(formDateTimeToCal(fdid));
		ndds.setSetID(fdid);
		ndds.setVersionID(1);
	}
	
	private void propulateRourke(LoggedInInfo loggedInInfo, RBR rourke, Integer fdid) {
		RBRM18 rbrm18 = rourke.addNewRBRM18();
		
		propulateRourkeFromRourke18m(rbrm18, fdid);
		propulateRourkeFromNDDS(rbrm18);
		propulateRourkeFromImmunization(loggedInInfo, rourke);
		rourke.setLastUpdateDate(formDateTimeToCal(fdid));
		
		rourke.setSetID(fdid);
		rourke.setVersionID(1);
		
	}
	
	private void propulateRourkeFromRourke18m(RBRM18 rbrm18, Integer fdid) {
		List<EFormValue> vars = eformValueDao.findByFormDataId(fdid);
		for (EFormValue var : vars) {
			String name = var.getVarName();
			String value = var.getVarValue();
			if (StringUtils.empty(name)) continue;
			if (StringUtils.empty(value)) continue;

			if (name.equals("visit_date_18m")) {
				Date visitDate = stringToDate(value);
				if (visitDate!=null) rbrm18.setVisitDate(dateToCal(visitDate));
			}
			else if (name.equals("height_18m")) rbrm18.setHeight(stringToBigDecimal(value)); 
			else if (name.equals("weight_18m")) rbrm18.setWeight(Integer.valueOf(value));
			else if (name.equals("headcirc_18m")) rbrm18.setHeadCirc(stringToBigDecimal(value));
			
			else if (name.equals("getattentionshow_18m_o")) rbrm18.setDevelopmentCommSkillsGetsAttention(1);
			else if (name.equals("getattentionshow_18m_x")) rbrm18.setDevelopmentCommSkillsGetsAttention(2);
			else if (name.equals("imitatesspeech_18m_o")) rbrm18.setDevelopmentCommSkillsImitatesSpeech(1);
			else if (name.equals("imitatesspeech_18m_x")) rbrm18.setDevelopmentCommSkillsImitatesSpeech(2);
			else if (name.equals("looksfortoy_18m_o")) rbrm18.setDevelopmentCommSkillsLooksForToys(1);
			else if (name.equals("looksfortoy_18m_x")) rbrm18.setDevelopmentCommSkillsLooksForToys(2);
			else if (name.equals("pointsbodyparts_18m_o")) rbrm18.setDevelopmentCommSkillsPointsToBodyParts(1);
			else if (name.equals("pointsbodyparts_18m_x")) rbrm18.setDevelopmentCommSkillsPointsToBodyParts(2);
			else if (name.equals("pointstowants_18m_o")) rbrm18.setDevelopmentCommSkillsPointsToWants(1);
			else if (name.equals("pointstowants_18m_x")) rbrm18.setDevelopmentCommSkillsPointsToWants(2);
			else if (name.equals("consonants4_18m_o")) rbrm18.setDevelopmentCommSkillsProduces4Cons(1);
			else if (name.equals("consonants4_18m_x")) rbrm18.setDevelopmentCommSkillsProduces4Cons(2);
			else if (name.equals("respondstoname_18m_o")) rbrm18.setDevelopmentCommSkillsRespondWhenCalled(1);
			else if (name.equals("respondstoname_18m_x")) rbrm18.setDevelopmentCommSkillsRespondWhenCalled(2);
			else if (name.equals("words20ormore_18m_o")) rbrm18.setDevelopmentCommSkillsSays20Words(1);
			else if (name.equals("words20ormore_18m_x")) rbrm18.setDevelopmentCommSkillsSays20Words(2);
			else if (name.equals("spoonfeedsself_18m_o")) rbrm18.setDevelopmentMotorSkillsFeedsSelf(1);
			else if (name.equals("spoonfeedsself_18m_x")) rbrm18.setDevelopmentMotorSkillsFeedsSelf(2);
			else if (name.equals("walksalone_18m_o")) rbrm18.setDevelopmentMotorSkillsWalksAlone(1);
			else if (name.equals("walksalone_18m_x")) rbrm18.setDevelopmentMotorSkillsWalksAlone(2);
			else if (name.equals("removeshatsocks_18m_o")) rbrm18.setAdaptiveSkillsRemovesHat(1);
			else if (name.equals("removeshatsocks_18m_x")) rbrm18.setAdaptiveSkillsRemovesHat(2);
			else if (name.equals("noconcerns_18m_o")) rbrm18.setAdaptiveMotorSkillsNoParentConcerns(1);
			else if (name.equals("noconcerns_18m_x")) rbrm18.setAdaptiveMotorSkillsNoParentConcerns(2);
			else if (name.equals("comesforcomfort_18m_o")) rbrm18.setDevelopmentSocialEmotionalComesForComfort(1);
			else if (name.equals("comesforcomfort_18m_x")) rbrm18.setDevelopmentSocialEmotionalComesForComfort(2);
			else if (name.equals("easytosoothe_18m_o")) rbrm18.setDevelopmentSocialEmotionalEasyToSoothe(1);
			else if (name.equals("easytosoothe_18m_x")) rbrm18.setDevelopmentSocialEmotionalEasyToSoothe(2);
			else if (name.equals("interestedinchildren_18m_o")) rbrm18.setDevelopmentSocialEmotionalInterestedInChildren(1);
			else if (name.equals("interestedinchildren_18m_x")) rbrm18.setDevelopmentSocialEmotionalInterestedInChildren(2);
			else if (name.equals("behaviourmanageable_18m_o")) rbrm18.setDevelopmentSocialEmotionalManagableBehaviour(1);
			else if (name.equals("behaviourmanageable_18m_x")) rbrm18.setDevelopmentSocialEmotionalManagableBehaviour(2);
			else if (name.equals("disciplineparenting_18m_o")) rbrm18.setEducationAdviceBehaviourDisciplineParentingSkills(1);
			else if (name.equals("disciplineparenting_18m_x")) rbrm18.setEducationAdviceBehaviourDisciplineParentingSkills(2);
			else if (name.equals("sleephabits_18m_o")) rbrm18.setEducationAdviceBehaviourHealthySleepHabits(1);
			else if (name.equals("sleephabits_18m_x")) rbrm18.setEducationAdviceBehaviourHealthySleepHabits(2);
			else if (name.equals("pcinteraction_18m_o")) rbrm18.setEducationAdviceBehaviourParentChildInteraction(1);
			else if (name.equals("pcinteraction_18m_x")) rbrm18.setEducationAdviceBehaviourParentChildInteraction(2);
			else if (name.equals("highrisk_18m_o")) rbrm18.setEducationAdviceFamilyHighRiskChildren(1);
			else if (name.equals("highrisk_18m_x")) rbrm18.setEducationAdviceFamilyHighRiskChildren(2);
			else if (name.equals("familyActiveLiving_18m_o")) rbrm18.setEducationAdviceFamilyHealthyActiveLiving(1);
			else if (name.equals("familyActiveLiving_18m_x")) rbrm18.setEducationAdviceFamilyHealthyActiveLiving(2);
			else if (name.equals("depression_18m_o")) rbrm18.setEducationAdviceFamilyParentalFatigueStress(1);
			else if (name.equals("depression_18m_x")) rbrm18.setEducationAdviceFamilyParentalFatigueStress(2);
			else if (name.equals("bathsafety_18m_o")) rbrm18.setEducationAdviceInjuryPrevBathSafety(1);
			else if (name.equals("bathsafety_18m_x")) rbrm18.setEducationAdviceInjuryPrevBathSafety(2);
			else if (name.equals("carseat_18m_o")) rbrm18.setEducationAdviceInjuryPrevCarSeat(1);
			else if (name.equals("carseat_18m_x")) rbrm18.setEducationAdviceInjuryPrevCarSeat(2);
			else if (name.equals("choking_18m_o")) rbrm18.setEducationAdviceInjuryPrevChokingSafeToys(1);
			else if (name.equals("choking_18m_x")) rbrm18.setEducationAdviceInjuryPrevChokingSafeToys(2);
			else if (name.equals("falls_18m_o")) rbrm18.setEducationAdviceInjuryPrevFalls(1);
			else if (name.equals("falls_18m_x")) rbrm18.setEducationAdviceInjuryPrevFalls(2);
			else if (name.equals("weanpacifier_18m_o")) rbrm18.setEducationAdviceInjuryPrevWeanFromPacifier(1);
			else if (name.equals("weanpacifier_18m_x")) rbrm18.setEducationAdviceInjuryPrevWeanFromPacifier(2);
			else if (name.equals("dental_18m_o")) rbrm18.setEducationAdviceOtherDentalCare(1);
			else if (name.equals("dental_18m_x")) rbrm18.setEducationAdviceOtherDentalCare(2);
			else if (name.equals("reading_18m_o")) rbrm18.setEducationAdviceFamilyEncourageReading(1);
			else if (name.equals("reading_18m_x")) rbrm18.setEducationAdviceFamilyEncourageReading(2);
			else if (name.equals("socializing_18m_o")) rbrm18.setEducationAdviceFamilySocializing(1);
			else if (name.equals("socializing_18m_x")) rbrm18.setEducationAdviceFamilySocializing(2);
			else if (name.equals("toiletlearning_18m_o")) rbrm18.setEducationAdviceOtherToiletLearning(1);
			else if (name.equals("toiletlearning_18m_x")) rbrm18.setEducationAdviceOtherToiletLearning(2);
			else if (name.equals("secondhandsmoke_18m_o")) rbrm18.setEducationAdviceEnvironmentalHealthSecondHandSmoke(1);
			else if (name.equals("secondhandsmoke_18m_x")) rbrm18.setEducationAdviceEnvironmentalHealthSecondHandSmoke(2);
			else if (name.equals("serumlead_18m_o")) rbrm18.setEducationAdviceEnvironmentalHealthSerumLead(1);
			else if (name.equals("serumlead_18m_x")) rbrm18.setEducationAdviceEnvironmentalHealthSerumLead(2);
			else if (name.equals("sunexposure_18m_o")) rbrm18.setEducationAdviceEnvironmentalHealthSunExposure(1);
			else if (name.equals("sunexposure_18m_x")) rbrm18.setEducationAdviceEnvironmentalHealthSunExposure(2);
			else if (name.equals("pesticide_18m_o")) rbrm18.setEducationAdviceEnvironmentalHealthPesticideExposure(1);
			else if (name.equals("pesticide_18m_x")) rbrm18.setEducationAdviceEnvironmentalHealthPesticideExposure(2);
			else if (name.equals("avoidsweetened_18m_o")) rbrm18.setNutritionAvoidSweetJuicesLiquids(1);
			else if (name.equals("avoidsweetened_18m_x")) rbrm18.setNutritionAvoidSweetJuicesLiquids(2);
			else if (name.equals("breastfeeding_18m_o")) rbrm18.setNutritionBreastfeeding(1);
			else if (name.equals("breastfeeding_18m_x")) rbrm18.setNutritionBreastfeeding(2);
			else if (name.equals("formulafeeding_18m_o")) rbrm18.setNutritionHomogenizedMilk(1);
			else if (name.equals("formulafeeding_18m_x")) rbrm18.setNutritionHomogenizedMilk(2);
			else if (name.equals("nobottle_18m_o")) rbrm18.setNutritionNoBottles(1);
			else if (name.equals("nobottle_18m_x")) rbrm18.setNutritionNoBottles(2);
	
			else if (name.equals("corneallightreflex_18m_o")) rbrm18.setPhysicalCornealLightReflex(1);
			else if (name.equals("corneallightreflex_18m_x")) rbrm18.setPhysicalCornealLightReflex(2);
			else if (name.equals("anteriorfontanelles_18m_o")) rbrm18.setPhysicalExamAnteriorFontanelles(1);
			else if (name.equals("anteriorfontanelles_18m_x")) rbrm18.setPhysicalExamAnteriorFontanelles(2);
			else if (name.equals("eyes_18m_o")) rbrm18.setPhysicalExamEyes(1);
			else if (name.equals("eyes_18m_x")) rbrm18.setPhysicalExamEyes(2);
			else if (name.equals("hearing_18m_o")) rbrm18.setPhysicalExamHearingScreening(1);
			else if (name.equals("hearing_18m_x")) rbrm18.setPhysicalExamHearingScreening(2);
			else if (name.equals("snoring_18m_o")) rbrm18.setPhysicalExamSnoringTonsil(1);
			else if (name.equals("snoring_18m_x")) rbrm18.setPhysicalExamSnoringTonsil(2);
			else if (name.equals("teeth_18m_o")) rbrm18.setPhysicalExamTeeth(1);
			else if (name.equals("teeth_18m_x")) rbrm18.setPhysicalExamTeeth(2);
			
			else if (name.equals("parentConcerns18m")) rbrm18.setParentalCaregiverConcerns(value);
			else if (name.equals("problemsPlans18m")) rbrm18.setProblemsAndPlansOther(value);
			else if (name.equals("problemsPlans18mRes")) {
				String[] resources = value.split(",");
				for (String res : resources) {
					if (StringUtils.filled(res)) {
						ProblemsAndPlans pp = rbrm18.addNewProblemsAndPlans();
						pp.setStringValue(res);
					}
				}
			}
			else if (name.equals("subject")) rbrm18.setSignature18M(value);
			
		}
	}
	
	private void propulateRourkeFromNDDS(RBRM18 rbrm18) {
		if (YesNoUnknown.N.equals(nddsQ.get(nddsQkey[0]))) rbrm18.setDevelopmentNDDSNotAttained18M01(1);
		if (YesNoUnknown.N.equals(nddsQ.get(nddsQkey[1]))) rbrm18.setDevelopmentNDDSNotAttained18M01(2);
		if (YesNoUnknown.N.equals(nddsQ.get(nddsQkey[2]))) rbrm18.setDevelopmentNDDSNotAttained18M01(3);
		if (YesNoUnknown.N.equals(nddsQ.get(nddsQkey[3]))) rbrm18.setDevelopmentNDDSNotAttained18M01(4);
		if (YesNoUnknown.N.equals(nddsQ.get(nddsQkey[4]))) rbrm18.setDevelopmentNDDSNotAttained18M01(5);
		if (YesNoUnknown.N.equals(nddsQ.get(nddsQkey[5]))) rbrm18.setDevelopmentNDDSNotAttained18M01(6);
		if (YesNoUnknown.N.equals(nddsQ.get(nddsQkey[6]))) rbrm18.setDevelopmentNDDSNotAttained18M01(7);
		if (YesNoUnknown.N.equals(nddsQ.get(nddsQkey[7]))) rbrm18.setDevelopmentNDDSNotAttained18M01(8);
		if (YesNoUnknown.N.equals(nddsQ.get(nddsQkey[8]))) rbrm18.setDevelopmentNDDSNotAttained18M01(9);
		if (YesNoUnknown.N.equals(nddsQ.get(nddsQkey[9]))) rbrm18.setDevelopmentNDDSNotAttained18M01(10);
		if (YesNoUnknown.N.equals(nddsQ.get(nddsQkey[10]))) rbrm18.setDevelopmentNDDSNotAttained18M01(11);
		if (YesNoUnknown.N.equals(nddsQ.get(nddsQkey[11]))) rbrm18.setDevelopmentNDDSNotAttained18M01(12);
		if (YesNoUnknown.N.equals(nddsQ.get(nddsQkey[12]))) rbrm18.setDevelopmentNDDSNotAttained18M01(13);
		if (YesNoUnknown.N.equals(nddsQ.get(nddsQkey[13]))) rbrm18.setDevelopmentNDDSNotAttained18M01(14);
		if (YesNoUnknown.N.equals(nddsQ.get(nddsQkey[14]))) rbrm18.setDevelopmentNDDSNotAttained18M01(15);
		if (YesNoUnknown.N.equals(nddsQ.get(nddsQkey[15]))) rbrm18.setDevelopmentNDDSNotAttained18M01(16);
		if (YesNoUnknown.N.equals(nddsQ.get(nddsQkey[16]))) rbrm18.setDevelopmentNDDSNotAttained18M01(17);
	}
	
	private void propulateRourkeFromImmunization(LoggedInInfo loggedInInfo, RBR rourke) {
		PreventionDao preventionDao = SpringUtils.getBean(PreventionDao.class);
		PreventionExtDao preventionExtDao = SpringUtils.getBean(PreventionExtDao.class);
		List<String> immunizationPreventionTypes = new ArrayList<String>();

		//Prepare list of prevention types which are immunizations
        PreventionDisplayConfig pdc = PreventionDisplayConfig.getInstance(loggedInInfo);
        ArrayList<HashMap<String,String>> prevList = pdc.getPreventions(loggedInInfo);
        for (int k =0 ; k < prevList.size(); k++){
            HashMap<String,String> a = new HashMap<String,String>();
            a.putAll(prevList.get(k));
            if (a != null && a.get("layout") != null && a.get("layout").equals("injection")){
            	if (a.get("name") != null) immunizationPreventionTypes.add(a.get("name"));
            }
        }
        
        //Map list of prevention types to Vaccine Name in spec
        if (preventionType.isEmpty()) {
        	preventionType.put("Rot", 1);
        	preventionType.put("DTaP-IPV-Hib", 2);
        	preventionType.put("Pneu-C", 3);
        	preventionType.put("MenC-C", 4);
        	preventionType.put("Men-C-ACWY", 5);
        	preventionType.put("HepB", 6);
        	preventionType.put("MMR", 7);
        	preventionType.put("MMRV", 7);
        	preventionType.put("VZ", 8);
        	preventionType.put("DTaP-IPV", 9);
        	preventionType.put("HPV", 10);
        	preventionType.put("HPV Vaccine", 10);
        	preventionType.put("dTap", 11);
        	preventionType.put("Flu", 12);
        }
		
		List<Prevention> preventions = preventionDao.findByDemographicId(Integer.valueOf(demographicNo));
		for (Prevention prevention : preventions) {
			if (!immunizationPreventionTypes.contains(prevention.getPreventionType())) continue;
			
			IMMUNIZATION imm = rourke.addNewIMMUNIZATION();
			imm.setDateGiven(dateToCal(prevention.getPreventionDate()));
		
			if (preventionType.get(prevention.getPreventionType())!=null) {
				imm.setVaccineName(preventionType.get(prevention.getPreventionType()));
			} else {
				imm.setVaccineName(13);
				imm.setVaccineNameOther(prevention.getPreventionType());
			}
			
			List<PreventionExt> preventionExts = preventionExtDao.findByPreventionId(prevention.getId());
			for (PreventionExt preventionExt : preventionExts) {
				String key = preventionExt.getkeyval();
				String val = preventionExt.getVal();
				if (StringUtils.empty(key)) continue;
				if (StringUtils.empty(val)) continue;
				
				if (key.equals("name")) {
					if (imm.getVaccineNameOther()==null) imm.setVaccineNameOther(val);
				}
				else if (key.equals("lot")) imm.setLotNumber(val);
				else if (key.equals("comments")) imm.setComments(val);
			}
		}
	}
	
	private void propulateM18Markers(M18MARKERS m18Markers, Integer fdid) {
		List<EFormValue> vars = eformValueDao.findByFormDataId(fdid);
		
		for (EFormValue var : vars) {
			String name = var.getVarName();
			String value = var.getVarValue();
			if (StringUtils.empty(name)) continue;
			if (StringUtils.empty(value)) continue;
			
			if (name.equals("fsa")) m18Markers.setFSA(value);
			else if (name.equals("Premature")) m18Markers.setPremature(YesNoUnknown.Y);
			else if (name.equals("HighRisk")) m18Markers.setHighRisk(YesNoUnknown.Y);
			else if (name.equals("NoConcerns")) m18Markers.setNoConcerns(YesNoUnknown.Y);
			else if (name.equals("SecondHandSmokeInUtero")) m18Markers.setSecondHandSmokeExposureInUtero(YesNoUnknown.Y);
			else if (name.equals("SecondHandSmokeSinceBirth")) m18Markers.setSecondHandSmokeExposureSinceBirth(YesNoUnknown.Y);
			else if (name.equals("SecondHandSmokeNoExposure")) m18Markers.setSecondHandSmokeExposureNoExposure(YesNoUnknown.Y);
			else if (name.equals("SubstanceAbuseInUteroYes")) m18Markers.setSubstanceAbuseInUteroYes(YesNoUnknown.Y);
			else if (name.equals("SubstanceAbuseInUteroNo")) m18Markers.setSubstanceAbuseInUteroNo(YesNoUnknown.Y);
			else if (name.equals("SubstanceAbuseAlcohol")) m18Markers.setSubstanceAbuseAlcohol(YesNoUnknown.Y);
			else if (name.equals("SubstanceAbuseDrugs")) m18Markers.setSubstanceAbuseDrugs(YesNoUnknown.Y);
			else if (name.equals("MoreThanOneDevAreaAffectedYes")) m18Markers.setMoreThanOneDevAreaAffectedYes(YesNoUnknown.Y);
			else if (name.equals("MoreThanOneDevAreaAffectedNo")) m18Markers.setMoreThanOneDevAreaAffectedNo(YesNoUnknown.Y);
			else if (name.equals("NeedForAddAssessmentYes")) m18Markers.setNeedForAddAssessmentYes(YesNoUnknown.Y);
			else if (name.equals("NeedForAddAssessmentNo")) m18Markers.setNeedForAddAssessmentNo(YesNoUnknown.Y);
			else if (name.equals("APGAR1")) m18Markers.setAPGAR1(Integer.parseInt(value));
			else if (name.equals("APGAR5")) m18Markers.setAPGAR5(Integer.parseInt(value));
		}
		
		m18Markers.setLastUpdateDateTime(formDateTimeToCal(fdid));
		m18Markers.setSetID(fdid);
		m18Markers.setVersionID(1);
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
		String date =  new SimpleDateFormat("yyyy-MM-dd").format(inDate);
		String time = timeFormatter.format(inDate);
		try {
			XmlCalendar x = new XmlCalendar(date+"T"+time);
			return x;
		} catch (Exception ex) {
			XmlCalendar x = new XmlCalendar("0001-01-01T00:00:00");
			return x;
		}
    }
	
	private Date stringToDate(String date) {
		
		Date newDate = null;
		try {
	         newDate = dateFormatter.parse(date);
        } catch (ParseException e) {
	 
        }
		
		return newDate;
	}
	
	private BigDecimal stringToBigDecimal(String n) {
		return (new BigDecimal(n));
	}
}
