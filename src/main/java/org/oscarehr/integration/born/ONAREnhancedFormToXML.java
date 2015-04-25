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

import java.io.OutputStream;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import org.apache.cxf.common.util.StringUtils;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlOptions;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarmcmaster.ar2005.AR1;
import org.oscarmcmaster.ar2005.AR2;
import org.oscarmcmaster.ar2005.ARRecord;
import org.oscarmcmaster.ar2005.ARRecordDocument;
import org.oscarmcmaster.ar2005.ARRecordSet;
import org.oscarmcmaster.ar2005.ARRecordSetDocument;
import org.oscarmcmaster.ar2005.AdditionalLabInvestigationsType;
import org.oscarmcmaster.ar2005.BirthAttendants;
import org.oscarmcmaster.ar2005.CurrentPregnancyType;
import org.oscarmcmaster.ar2005.CustomLab;
import org.oscarmcmaster.ar2005.DatingMethods;
import org.oscarmcmaster.ar2005.DiscussionTopicsType;
import org.oscarmcmaster.ar2005.FamilyHistoryType;
import org.oscarmcmaster.ar2005.GenericHistoryType;
import org.oscarmcmaster.ar2005.InfectiousDiseaseType;
import org.oscarmcmaster.ar2005.InitialLaboratoryInvestigations;
import org.oscarmcmaster.ar2005.MedicalHistoryAndPhysicalExam;
import org.oscarmcmaster.ar2005.MedicalHistoryType;
import org.oscarmcmaster.ar2005.NewbornCare;
import org.oscarmcmaster.ar2005.NormalAbnormalNullType;
import org.oscarmcmaster.ar2005.ObstetricalHistory;
import org.oscarmcmaster.ar2005.ObstetricalHistoryItemList;
import org.oscarmcmaster.ar2005.PartnerInformation;
import org.oscarmcmaster.ar2005.PatientInformation;
import org.oscarmcmaster.ar2005.PatientInformation.EthnicBackground;
import org.oscarmcmaster.ar2005.PhysicalExaminationType;
import org.oscarmcmaster.ar2005.PractitionerInformation;
import org.oscarmcmaster.ar2005.PregnancyHistory;
import org.oscarmcmaster.ar2005.PrenatalGeneticScreeningType;
import org.oscarmcmaster.ar2005.PsychosocialType;
import org.oscarmcmaster.ar2005.RecommendedImmunoprophylaxisType;
import org.oscarmcmaster.ar2005.RiskFactorItemType;
import org.oscarmcmaster.ar2005.SignatureType;
import org.oscarmcmaster.ar2005.SubsequentVisitItemType;
import org.oscarmcmaster.ar2005.UltrasoundType;
import org.oscarmcmaster.ar2005.YesNoNullType;

import oscar.form.FrmRecord;
import oscar.form.FrmRecordFactory;

public class ONAREnhancedFormToXML {
	
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");
	private SimpleDateFormat dateFormatter2 = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat dateFormatter3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Logger logger = MiscUtils.getLogger();

	Properties props;
	String demographicNo;
	String providerNo;
	int formId;
	int episodeId;
	
	public void generateXMLAndValidate(LoggedInInfo loggedInInfo, OutputStream os, String providerNo,String demographicNo, int formId, int episodeId) throws Exception {
		this.demographicNo = demographicNo;
		this.providerNo = providerNo;
		this.formId = formId;
		this.episodeId = episodeId;
		FrmRecord rec = (new FrmRecordFactory()).factory("ONAREnhanced");
	    props = rec.getFormRecord(loggedInInfo, Integer.parseInt(demographicNo), formId);

		ARRecordSetDocument recordSetD = ARRecordSetDocument.Factory.newInstance();
		ARRecordSet recordSet = recordSetD.addNewARRecordSet();
		ARRecord arRecord = recordSet.addNewARRecord();
		AR1 ar1 = arRecord.addNewAR1();
		populateAr1(ar1,providerNo);
		AR2 ar2 = arRecord.addNewAR2();
		populateAr2(ar2);
		HashMap<String,String> suggestedPrefixes = new HashMap<String,String>();
		suggestedPrefixes.put("http://www.oscarmcmaster.org/AR2005", "");
		suggestedPrefixes.put("http://www.w3.org/2001/XMLSchema-instance","xsi");
		XmlOptions opts = new XmlOptions();
		opts.setSaveSuggestedPrefixes(suggestedPrefixes);
		opts.setSavePrettyPrint();
		ArrayList validationErrors = new ArrayList();
		XmlOptions m_validationOptions = new XmlOptions();
		m_validationOptions.setErrorListener(validationErrors);
		recordSetD.validate(m_validationOptions);
		for(Object o:validationErrors) {
			MiscUtils.getLogger().warn(o);
		}
		recordSetD.save(os,opts);
	}
	
	public boolean addXmlToStream(LoggedInInfo loggedInInfo, Writer os, XmlOptions opts, String providerNo,String demographicNo, int formId, int episodeId) throws Exception {
		this.demographicNo = demographicNo;
		this.providerNo = providerNo;
		this.formId = formId;
		this.episodeId = episodeId;	    
	    FrmRecord rec = (new FrmRecordFactory()).factory("ONAREnhanced");
	    props = rec.getFormRecord(loggedInInfo, Integer.parseInt(demographicNo), formId);	    
	    
	    if(StringUtils.isEmpty(props.getProperty("pg1_formDate")) || StringUtils.isEmpty(props.getProperty("pg2_formDate"))) {
	    	MiscUtils.getLogger().warn("skipping form since the signature dates are not both set");
	    	return false;
	    }
		//ARRecord arRecord = ARRecord.Factory.newInstance();
		ARRecordDocument arRecordDoc = ARRecordDocument.Factory.newInstance();
		ARRecord arRecord = arRecordDoc.addNewARRecord();
		AR1 ar1 = arRecord.addNewAR1();
		populateAr1(ar1,providerNo);
		AR2 ar2 = arRecord.addNewAR2();
		populateAr2(ar2);		
		XmlOptions m_validationOptions = new XmlOptions();		
		ArrayList validationErrors = new ArrayList();
		m_validationOptions.setErrorListener(validationErrors);
		if(arRecordDoc.validate(m_validationOptions)) {
			arRecordDoc.save(os,opts);
			return true;
		} else {
			MiscUtils.getLogger().warn("form failed validation:"+formId);
			for(Object o:validationErrors) {
				MiscUtils.getLogger().warn(o);
			}
		}
		return false;
	}
	
	public FrmRecord addXmlToStream2(LoggedInInfo loggedInInfo, Writer os, XmlOptions opts, String providerNo,String demographicNo, int formId, int episodeId) throws Exception {
		this.demographicNo = demographicNo;
		this.providerNo = providerNo;
		this.formId = formId;
		this.episodeId = episodeId;	    
	    FrmRecord rec = (new FrmRecordFactory()).factory("ONAREnhanced");
	    props = rec.getFormRecord(loggedInInfo, Integer.parseInt(demographicNo), formId);	    
		//ARRecord arRecord = ARRecord.Factory.newInstance();
		ARRecordDocument arRecordDoc = ARRecordDocument.Factory.newInstance();
		ARRecord arRecord = arRecordDoc.addNewARRecord();
		AR1 ar1 = arRecord.addNewAR1();
		populateAr1(ar1,providerNo);
		AR2 ar2 = arRecord.addNewAR2();
		populateAr2(ar2);		
		XmlOptions m_validationOptions = new XmlOptions();		
		ArrayList validationErrors = new ArrayList();
		m_validationOptions.setErrorListener(validationErrors);
		if(arRecordDoc.validate(m_validationOptions)) {
			arRecordDoc.save(os,opts);
			return rec;
		} else {
			MiscUtils.getLogger().warn("form failed validation:"+formId);
			for(Object o:validationErrors) {
				MiscUtils.getLogger().warn(o);
			}
		}
		return null;
	}

	void populateAr1(AR1 ar1, String providerNo)  {
		ar1.setDemographicNo(Integer.parseInt(demographicNo));
		ar1.setProviderNo(props.getProperty("provider_no"));
		ar1.setVersionID(formId);
		Calendar formCreatedDate = Calendar.getInstance();
		try {
			formCreatedDate.setTime(dateFormatter2.parse(props.getProperty("formCreated")));
		}catch(ParseException e) {}
		
		ar1.setFormCreated(formCreatedDate);
		Calendar formEditedDate = Calendar.getInstance();
		try {
			formEditedDate.setTime(dateFormatter3.parse(props.getProperty("formEdited")));
		}catch(ParseException e) {}
		ar1.setFormEdited(formEditedDate);
		ar1.setEpisodeId(episodeId);
		ar1.setId(formId);
		PatientInformation patientInformation = ar1.addNewPatientInformation();
		PartnerInformation partnerInformation = ar1.addNewPartnerInformation();
		PractitionerInformation practitionerInformation = ar1.addNewPractitionerInformation();
		PregnancyHistory pregnancyHistory = ar1.addNewPregnancyHistory();
		ObstetricalHistory obstetricalHistory = ar1.addNewObstetricalHistory();
		MedicalHistoryAndPhysicalExam medicalHistoryAndPhysicalExam = ar1.addNewMedicalHistoryAndPhysicalExam();
		InitialLaboratoryInvestigations initialLaboratoryInvestigations = ar1.addNewInitialLaboratoryInvestigations();

		populatePatientInformation(patientInformation);
		populatePartnerInformation(partnerInformation);
		populatePractitionerInformation(practitionerInformation);
		populatePregnancyHistory(pregnancyHistory);
		populateObstetricalHistory(obstetricalHistory);
		populateMedicalHistoryAndPhysicalExam(medicalHistoryAndPhysicalExam);
		populateInitialLaboratoryInvestigations(initialLaboratoryInvestigations);
		ar1.setComments(props.getProperty("pg1_commentsAR1", ""));
		ar1.setExtraComments(props.getProperty("pg1_comments2AR1", ""));
		setSignaturesAR1(ar1.addNewSignatures());
	}

	void populateInitialLaboratoryInvestigations(InitialLaboratoryInvestigations initialLaboratoryInvestigations)  {
		initialLaboratoryInvestigations.setHbResult(props.getProperty("pg1_labHb", ""));
		initialLaboratoryInvestigations.setHivResult(InitialLaboratoryInvestigations.HivResult.Enum.forString(props.getProperty("pg1_labHIV")));
		initialLaboratoryInvestigations.setHivCounsel(props.getProperty("pg1_labHIVCounsel", "").length()>0?true:false);
		if(props.getProperty("pg1_labMCV", "").length()>0)
			initialLaboratoryInvestigations.setMcvResult(Float.parseFloat(props.getProperty("pg1_labMCV", "0")));
		else {
			initialLaboratoryInvestigations.setMcvResult(0f);
		}
		initialLaboratoryInvestigations.setAboResult(InitialLaboratoryInvestigations.AboResult.Enum.forString(props.getProperty("pg1_labABO")));
		initialLaboratoryInvestigations.setRhResult(InitialLaboratoryInvestigations.RhResult.Enum.forString(props.getProperty("pg1_labRh")));
		initialLaboratoryInvestigations.setPapResult(props.getProperty("pg1_labLastPap", ""));
		try {
			if(props.getProperty("pg1_labLastPapDate", "").length()>0) {
				initialLaboratoryInvestigations.setLastPapDate(this.createDate(dateFormatter.parse(props.getProperty("pg1_labLastPapDate"))));
			} else {
				initialLaboratoryInvestigations.setLastPapDate(null);
			}
		}catch(ParseException e) {
			MiscUtils.getLogger().warn("error",e);
			initialLaboratoryInvestigations.setLastPapDate(null);
		}
		initialLaboratoryInvestigations.setAntibodyResult(props.getProperty("pg1_labAntiScr", ""));
		initialLaboratoryInvestigations.setGcResultGonorrhea(InitialLaboratoryInvestigations.GcResultGonorrhea.Enum.forString(props.getProperty("pg1_labGC", "")));
		initialLaboratoryInvestigations.setGcResultChlamydia(InitialLaboratoryInvestigations.GcResultChlamydia.Enum.forString(props.getProperty("pg1_labChlamydia", "")));	
		initialLaboratoryInvestigations.setRubellaResult(props.getProperty("pg1_labRubella", ""));
		initialLaboratoryInvestigations.setUrineResult(props.getProperty("pg1_labUrine", ""));
		initialLaboratoryInvestigations.setHbsAgResult(InitialLaboratoryInvestigations.HbsAgResult.Enum.forString(props.getProperty("pg1_labHBsAg", "")));
		initialLaboratoryInvestigations.setVdrlResult(InitialLaboratoryInvestigations.VdrlResult.Enum.forString(props.getProperty("pg1_labVDRL", "")));
		initialLaboratoryInvestigations.setSickleCellResult(InitialLaboratoryInvestigations.SickleCellResult.Enum.forString(props.getProperty("pg1_labSickle", "")));
		PrenatalGeneticScreeningType prenatalGeneticScreening = initialLaboratoryInvestigations.addNewPrenatalGenericScreening();
		populatePrenatalGeneticScreening(prenatalGeneticScreening);
		CustomLab cl1 = initialLaboratoryInvestigations.addNewCustomLab1();
		cl1.setLabel(props.getProperty("pg1_labCustom1Label", ""));
		cl1.setResult(props.getProperty("pg1_labCustom1Result", ""));
		CustomLab cl2 = initialLaboratoryInvestigations.addNewCustomLab2();
		cl2.setLabel(props.getProperty("pg1_labCustom2Label", ""));
		cl2.setResult(props.getProperty("pg1_labCustom2Result", ""));
	}

	void populatePrenatalGeneticScreening(PrenatalGeneticScreeningType prenatalGeneticScreening)  {
		prenatalGeneticScreening.setMSSIPSFTS(props.getProperty("pg1_geneticA", ""));
		prenatalGeneticScreening.setEDBCVS(props.getProperty("pg1_geneticB", ""));
		prenatalGeneticScreening.setMSAFP(props.getProperty("pg1_geneticC", ""));
		prenatalGeneticScreening.setDeclined(props.getProperty("pg1_geneticD", "").length()>0?true:false);
		CustomLab cl1 = prenatalGeneticScreening.addNewCustomLab1();
		cl1.setLabel(props.getProperty("pg1_labCustom3Label", ""));
		cl1.setResult(props.getProperty("pg1_labCustom3Result", ""));
	}

	void populateMedicalHistoryAndPhysicalExam(MedicalHistoryAndPhysicalExam medicalHistoryAndPhysicalExam)  {
		CurrentPregnancyType currentPregnancy = medicalHistoryAndPhysicalExam.addNewCurrentPregnancy();
		MedicalHistoryType medicalHistory = medicalHistoryAndPhysicalExam.addNewMedicalHistory();
		GenericHistoryType genericHistory = medicalHistoryAndPhysicalExam.addNewGenericHistory();
		InfectiousDiseaseType infectiousDiseases = medicalHistoryAndPhysicalExam.addNewInfectiousDisease();
		PsychosocialType psychosocial  = medicalHistoryAndPhysicalExam.addNewPsychosocial();
		FamilyHistoryType familyHistory = medicalHistoryAndPhysicalExam.addNewFamilyHistory();
		PhysicalExaminationType physicalExamination = medicalHistoryAndPhysicalExam.addNewPhysicalExamination();

		populateCurrentPregnancy(currentPregnancy);
		populateMedicalHistory(medicalHistory);
		populateGenericHistory(genericHistory);
		populateInfectiousDisease(infectiousDiseases);
		populatePsychosocial(psychosocial);
		populateFamilyHistory(familyHistory);
		populatePhysicalExamination(physicalExamination);
	}

	YesNoNullType getYesNoNullType(Boolean b)  {
		YesNoNullType result = YesNoNullType.Factory.newInstance();
		if(b != null && b) {
			result.setYes(true);
		} else if(b != null && !b) {
			result.setNo(true);
		} else {
			result.setNull(true);
		}
		return result;
	}
	
	YesNoNullType getYesNoNullType(String yes, String no)  {
		YesNoNullType result = YesNoNullType.Factory.newInstance();
		boolean a = props.getProperty(yes, "").length()>0?true:false;
		boolean b = props.getProperty(no, "").length()>0?true:false;
		
		if(a) {
			result.setYes(true);
		} else if(b) {
			result.setNo(true);
		} else {
			result.setNull(true);
		}
		return result;
	}

	void populateCurrentPregnancy(CurrentPregnancyType currentPregnancy)  {
		currentPregnancy.setBleeding(getYesNoNullType("pg1_cp1","pg1_cp1N"));
		currentPregnancy.setNausea(getYesNoNullType("pg1_cp2","pg1_cp2N"));
		currentPregnancy.setSmoking(getYesNoNullType("pg1_cp3","pg1_cp3N"));
		if(props.getProperty("pg1_box3", "") != null)
			currentPregnancy.setCigsPerDay(CurrentPregnancyType.CigsPerDay.Enum.forString(props.getProperty("pg1_box3", "")));
		else
			currentPregnancy.setCigsPerDay(CurrentPregnancyType.CigsPerDay.X);
		currentPregnancy.setAlcoholDrugs(getYesNoNullType("pg1_cp4","pg1_cp4N"));
		currentPregnancy.setOccEnvRisks(getYesNoNullType("pg1_cp8","pg1_cp8N"));
		currentPregnancy.setDietaryRes(getYesNoNullType("pg1_naDiet","pg1_naDietN"));
		currentPregnancy.setCalciumAdequate(getYesNoNullType("pg1_naMilk","pg1_naMilkN"));
		currentPregnancy.setFolate(getYesNoNullType("pg1_naFolic","pg1_naFolicN"));
	}

	void populateMedicalHistory(MedicalHistoryType medicalHistory)  {
		medicalHistory.setHypertension(getYesNoNullType("pg1_yes9","pg1_no9"));
		medicalHistory.setEndorince(getYesNoNullType("pg1_yes10","pg1_no10"));
		medicalHistory.setUrinaryTract(getYesNoNullType("pg1_yes12","pg1_no12"));
		medicalHistory.setCardiac(getYesNoNullType("pg1_yes13","pg1_no13"));
		medicalHistory.setLiver(getYesNoNullType("pg1_yes14","pg1_no14"));
		medicalHistory.setGynaecology(getYesNoNullType("pg1_yes17","pg1_no17"));
		medicalHistory.setHem(getYesNoNullType("pg1_yes22","pg1_no22"));
		medicalHistory.setSurgeries(getYesNoNullType("pg1_yes20","pg1_no20"));
		medicalHistory.setBloodTransfusion(getYesNoNullType("pg1_bloodTranY","pg1_bloodTranN"));
		medicalHistory.setAnesthetics(getYesNoNullType("pg1_yes21","pg1_no21"));
		medicalHistory.setPsychiatry(getYesNoNullType("pg1_yes24","pg1_no24"));
		medicalHistory.setEpilepsy(getYesNoNullType("pg1_yes15","pg1_no15"));
		medicalHistory.setOther(getYesNoNullType("pg1_yes25","pg1_no25"));
		medicalHistory.setOtherDescr(props.getProperty("pg1_box25", ""));
	}

	void populateGenericHistory(GenericHistoryType geneticHistory)  {
		geneticHistory.setAtRisk(getYesNoNullType("pg1_yes27","pg1_no27"));
		geneticHistory.setDevelopmentalDelay(getYesNoNullType("pg1_yes31","pg1_no31"));
		geneticHistory.setCongenitalAnomolies(getYesNoNullType("pg1_yes32","pg1_no32"));
		geneticHistory.setChromosomalDisorders(getYesNoNullType("pg1_yes34","pg1_no34"));
		geneticHistory.setGeneticDisorders(getYesNoNullType("pg1_yes35","pg1_no35"));
	}

	void populateInfectiousDisease(InfectiousDiseaseType infectiousDiseases)  {
		infectiousDiseases.setVaricella(getYesNoNullType("pg1_idt40","pg1_idt40N"));
		infectiousDiseases.setStd(getYesNoNullType("pg1_idt38","pg1_idt38N"));
		infectiousDiseases.setTuberculosis(getYesNoNullType("pg1_idt42","pg1_idt42N"));
		infectiousDiseases.setOther(getYesNoNullType("pg1_infectDisOtherY","pg1_infectDisOtherN"));
		infectiousDiseases.setOtherDescr(props.getProperty("pg1_infectDisOther", ""));
	}

	void populatePsychosocial(PsychosocialType psychosocial)  {
		psychosocial.setPoortSocialSupport(getYesNoNullType("pg1_pdt43","pg1_pdt43N"));
		psychosocial.setRelationshipProblems(getYesNoNullType("pg1_pdt44","pg1_pdt44N"));
		psychosocial.setEmotionalDepression(getYesNoNullType("pg1_pdt45","pg1_pdt45N"));
		psychosocial.setSubstanceAbuse(getYesNoNullType("pg1_pdt46","pg1_pdt46N"));
		psychosocial.setFamilyViolence(getYesNoNullType("pg1_pdt47","pg1_pdt47N"));
		psychosocial.setParentingConcerns(getYesNoNullType("pg1_pdt48","pg1_pdt48N"));
		psychosocial.setReligiousCultural(getYesNoNullType("pg1_reliCultY","pg1_reliCultN"));
	}

	void populateFamilyHistory(FamilyHistoryType familyHistory)  {
		familyHistory.setAtRisk(getYesNoNullType("pg1_fhRiskY","pg1_fhRiskN"));
	}

	NormalAbnormalNullType getNormalAbnormalNullType(Boolean b)  {
		NormalAbnormalNullType result = NormalAbnormalNullType.Factory.newInstance();

		if(b != null && b) {
			result.setNormal(true);
		} else if(b != null && !b) {
			result.setAbnormal(true);
		} else {
			result.setNull(true);
		}
		return result;
	}
	
	NormalAbnormalNullType getNormalAbnormalNullType(String normal, String abn)  {
		NormalAbnormalNullType result = NormalAbnormalNullType.Factory.newInstance();

		boolean n = props.getProperty(normal, "").length()>0?true:false;
		boolean a = props.getProperty(abn, "").length()>0?true:false;

		if(n) {
			result.setNormal(true);
		} else if(a) {
			result.setAbnormal(true);
		} else {
			result.setNull(true);
		}
		return result;
	}

	void populatePhysicalExamination(PhysicalExaminationType physicalExamination)  {
		if(props.getProperty("pg1_ht", "").length()>0)
			physicalExamination.setHeight(Float.valueOf(props.getProperty("pg1_ht")));
		else
			physicalExamination.setNilHeight();
		if(props.getProperty("pg1_wt", "").length()>0)
			physicalExamination.setWeight(Float.valueOf(props.getProperty("pg1_wt")));
		else
			physicalExamination.setNilWeight();
		if(props.getProperty("c_bmi", "").length()>0)
			physicalExamination.setBmi(Float.valueOf(props.getProperty("c_bmi")));
		else
			physicalExamination.setNilBmi();
		physicalExamination.setBp(props.getProperty("pg1_BP",""));

		physicalExamination.setThyroid(getNormalAbnormalNullType("pg1_thyroid","pg1_thyroidA"));
		physicalExamination.setChest(getNormalAbnormalNullType("pg1_chest","pg1_chestA"));
		physicalExamination.setBreasts(getNormalAbnormalNullType("pg1_breasts","pg1_breastsA"));
		physicalExamination.setCardiovascular(getNormalAbnormalNullType("pg1_cardio","pg1_cardioA"));
		physicalExamination.setAbdomen(getNormalAbnormalNullType("pg1_abdomen","pg1_abdomenA"));
		physicalExamination.setVaricosities(getNormalAbnormalNullType("pg1_vari","pg1_variA"));
		physicalExamination.setExernalGenitals(getNormalAbnormalNullType("pg1_extGen","pg1_extGenA"));
		physicalExamination.setCervixVagina(getNormalAbnormalNullType("pg1_cervix","pg1_cervixA"));
		physicalExamination.setUterus(getNormalAbnormalNullType("pg1_uterus","pg1_uterusA"));
		physicalExamination.setUterusSize(props.getProperty("pg1_uterusBox", ""));
		physicalExamination.setAdnexa(getNormalAbnormalNullType("pg1_adnexa","pg1_adnexaA"));
		physicalExamination.setOther(getNormalAbnormalNullType("pg1_pExOther","pg1_pExOtherA"));
		physicalExamination.setOtherDescr(props.getProperty("pg1_pExOtherDesc", ""));
	}



	void populateObstetricalHistory(ObstetricalHistory obstetricalHistory)  {
		int length = Integer.parseInt(props.getProperty("obxhx_num","0"));
		for(int x=0;x<length;x++) {
			int n = x+1;
			
			//skip empty line
			if(props.getProperty("pg1_year"+n,"").length()==0 && props.getProperty("pg1_sex"+n,"").length()==0 && props.getProperty("pg1_oh_gest"+n,"").length()==0
					&& props.getProperty("pg1_weight"+n,"").length()==0 && props.getProperty("pg1_length"+n,"").length()==0 && props.getProperty("pg1_place"+n,"").length()==0 
					&& props.getProperty("pg1_svb"+n,"").length()==0 && props.getProperty("pg1_cs"+n,"").length()==0 && props.getProperty("pg1_ass"+n,"").length()==0 && 
					props.getProperty("pg1_oh_comments"+n,"").length()==0 ) {
				continue;
			}
			
			if(StringUtils.isEmpty(props.getProperty("pg1_sex"+n))) {
				continue;
			}
			
			ObstetricalHistoryItemList item1 = obstetricalHistory.addNewObsList();
			item1.setYear(props.getProperty("pg1_year"+n,"0").length()>0?Integer.parseInt(props.getProperty("pg1_year"+n, "0")):0);
			item1.setSex(ObstetricalHistoryItemList.Sex.Enum.forString(props.getProperty("pg1_sex"+n).toUpperCase()));
			item1.setGestAge(props.getProperty("pg1_oh_gest"+n,"0").length()>0?Integer.parseInt(props.getProperty("pg1_oh_gest"+n, "0")):0);
			item1.setBirthWeight(props.getProperty("pg1_weight"+n,""));
			item1.setLengthOfLabour(props.getProperty("pg1_length"+n,"0").length()>0?Float.parseFloat(props.getProperty("pg1_length"+n, "0")):0);
			item1.setPlaceOfBirth(props.getProperty("pg1_place"+n,"")!=null?props.getProperty("pg1_place"+n,""):"");
			if(props.getProperty("pg1_svb"+n,"").length()>0)
				item1.setTypeOfDelivery(ObstetricalHistoryItemList.TypeOfDelivery.SVAG);
			else if(props.getProperty("pg1_cs"+n,"").length()>0)
				item1.setTypeOfDelivery(ObstetricalHistoryItemList.TypeOfDelivery.CS);
			else if(props.getProperty("pg1_ass"+n,"").length()>0)
				item1.setTypeOfDelivery(ObstetricalHistoryItemList.TypeOfDelivery.AVAG);
			else 
				item1.setTypeOfDelivery(ObstetricalHistoryItemList.TypeOfDelivery.UN);
			
			item1.setComments(props.getProperty("pg1_oh_comments"+n,"")!=null?props.getProperty("pg1_oh_comments"+n,""):"");
		}
	}

	Calendar createDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	void populatePregnancyHistory(PregnancyHistory pregnancyHistory)  {

		//TODO:null?
		if(props.getProperty("pg1_menLMP", "").length()>0){
			try {
				pregnancyHistory.setLMP(createDate(dateFormatter.parse(props.getProperty("pg1_menLMP"))));
			}catch(ParseException e) {}
		} else {
			pregnancyHistory.setLMP(null);
		}
		pregnancyHistory.setLMPCertain(this.getYesNoNullType("pg1_psCertY","pg1_psCertN"));
		pregnancyHistory.setMenCycle(props.getProperty("pg1_menCycle", ""));
		pregnancyHistory.setMenCycleRegular(getYesNoNullType("pg1_menReg","pg1_menRegN"));
		pregnancyHistory.setContraceptiveType(props.getProperty("pg1_contracep", ""));
		if(props.getProperty("pg1_lastUsed", "").length()>0) {
			try {
				pregnancyHistory.setContraceptiveLastUsed(createDate(dateFormatter.parse(props.getProperty("pg1_lastUsed"))));
			}catch(ParseException e) {}			
		} else {
			pregnancyHistory.setContraceptiveLastUsed(null);
		}
		if(props.getProperty("pg1_menEDB", "").length()>0) {
			try {
				pregnancyHistory.setMenstrualEDB(createDate(dateFormatter.parse(props.getProperty("pg1_menEDB"))));
			}catch(ParseException e){}
		} else {
			pregnancyHistory.setMenstrualEDB(null);
		}
		if(props.getProperty("c_finalEDB", "").length()>0) {
			try {
				pregnancyHistory.setFinalEDB(createDate(dateFormatter.parse(props.getProperty("c_finalEDB"))));
			}catch(ParseException e) {}
		} else {
			pregnancyHistory.setFinalEDB(null);
		}
		DatingMethods datingMethods = pregnancyHistory.addNewDatingMethods();
		datingMethods.setDates(props.getProperty("pg1_edbByDate", "").length()>0?true:false);
		datingMethods.setT1US(props.getProperty("pg1_edbByT1", "").length()>0?true:false);
		datingMethods.setT2US(props.getProperty("pg1_edbByT2", "").length()>0?true:false);
		datingMethods.setArt(props.getProperty("pg1_edbByART", "").length()>0?true:false);
		if(props.getProperty("c_gravida", "").length()>0)
			pregnancyHistory.setGravida(Integer.parseInt(props.getProperty("c_gravida", "")));	
		else
			pregnancyHistory.setGravida(999);
		if(props.getProperty("c_term", "").length()>0)
			pregnancyHistory.setTerm(Integer.parseInt(props.getProperty("c_term", "")));
		else
			pregnancyHistory.setTerm(999);
		if(props.getProperty("c_prem", "").length()>0)
			pregnancyHistory.setPremature(Integer.parseInt(props.getProperty("c_prem", "")));
		else
			pregnancyHistory.setPremature(999);
		if(props.getProperty("c_abort", "").length()>0)
			pregnancyHistory.setAbortuses(Integer.parseInt(props.getProperty("c_abort", "")));
		else 
			pregnancyHistory.setAbortuses(999);
		if(props.getProperty("c_living", "").length()>0)
			pregnancyHistory.setLiving(Integer.parseInt(props.getProperty("c_living", "")));
		else 
			pregnancyHistory.setLiving(999);
	}


	void populatePractitionerInformation(PractitionerInformation practitionerInformation)  {
		getBirthAttendants(practitionerInformation.addNewBirthAttendants());
		practitionerInformation.setFamilyPhysician(props.getProperty("c_famPhys", ""));
		getNewbornCare(practitionerInformation.addNewNewbornCare());
	}

	private void getBirthAttendants(BirthAttendants birthAttendants)  {
		birthAttendants.setOBS( props.getProperty("pg1_baObs", "").length()>0?true:false);
		birthAttendants.setFP(props.getProperty("pg1_baFP", "").length()>0?true:false);
		birthAttendants.setMidwife(props.getProperty("pg1_baMidwife", "").length()>0?true:false);
		birthAttendants.setOther(props.getProperty("c_ba", ""));
	}

	private void getNewbornCare(NewbornCare newbornCare)  {
		newbornCare.setPed(props.getProperty("pg1_ncPed", "").length()>0?true:false);
		newbornCare.setFP(props.getProperty("pg1_ncFP", "").length()>0?true:false);
		newbornCare.setMidwife(props.getProperty("pg1_ncMidwife", "").length()>0?true:false);
		newbornCare.setOther(props.getProperty("c_nc", ""));
	}

	void populatePartnerInformation(PartnerInformation partnerInformation)  {
		partnerInformation.setLastName(props.getProperty("c_partnerLastName", ""));
		partnerInformation.setFirstName(props.getProperty("c_partnerFirstName", ""));
		PartnerInformation.Occupation occ = partnerInformation.addNewOccupation();
		occ.setValue(PartnerInformation.Occupation.Value.OTHER);
		occ.setOther(props.getProperty("pg1_partnerOccupation", ""));
		partnerInformation.setOccupation(occ);
		partnerInformation.setAge(props.getProperty("pg1_partnerAge", "").length()>0?Integer.parseInt(props.getProperty("pg1_partnerAge", "")):0);
		partnerInformation.setEducationLevel(PartnerInformation.EducationLevel.Enum.forString(props.getProperty("pg1_partnerEduLevel")));		
	}

	String convertProvince(String p) {
		if(p.equals("AB")) {return "CA-AB";}
		if(p.equals("BC")) {return "CA-BC";}
		if(p.equals("MB")) {return "CA-MB";}
		if(p.equals("NB")) {return "CA-NB";}
		if(p.equals("NL")) {return "CA-NL";}
		if(p.equals("NT")) {return "CA-NT";}
		if(p.equals("NS")) {return "CA-NS";}
		if(p.equals("NU")) {return "CA-NU";}
		if(p.equals("ON")) {return "CA-ON";}
		if(p.equals("PE")) {return "CA-PE";}
		if(p.equals("QC")) {return "CA-QC";}
		if(p.equals("SK")) {return "CA-SK";}
		if(p.equals("YT")) {return "CA-YT";}
		if(p.startsWith("US")) {return "USA";}
		if(p.equals("OT")) {return "OUTC";}
		return null;
	}
	void populatePatientInformation(PatientInformation patientInformation)   {
		patientInformation.setLastName(props.getProperty("c_lastName", ""));
		patientInformation.setFirstName(props.getProperty("c_firstName", "") );
		patientInformation.setAddress(props.getProperty("c_address", ""));
		patientInformation.setApt(props.getProperty("c_apt", ""));
		patientInformation.setCity(props.getProperty("c_city", ""));
		patientInformation.setProvince(PatientInformation.Province.Enum.forString(convertProvince(props.getProperty("c_province", ""))));		
		patientInformation.setPostalCode(props.getProperty("c_postal", ""));
		patientInformation.setHomePhone(props.getProperty("pg1_homePhone", "").replaceAll("\\-", ""));
		patientInformation.setWorkPhone(props.getProperty("pg1_workPhone", "").replaceAll("\\-", ""));
		patientInformation.setLanguage(PatientInformation.Language.Enum.forString(props.getProperty("pg1_language")));
		if(props.getProperty("pg1_dateOfBirth", "").length()>0) {
			try {
				patientInformation.setDob(createDate(dateFormatter.parse(props.getProperty("pg1_dateOfBirth"))));
			}catch(ParseException e) {}
		}		
		patientInformation.setAge(props.getProperty("pg1_age", "").length()>0?Integer.parseInt(props.getProperty("pg1_age")):0);
		PatientInformation.Occupation occ = patientInformation.addNewOccupation();
		occ.setValue(PatientInformation.Occupation.Value.OTHER);
		occ.setOther(props.getProperty("pg1_occupation", ""));
		patientInformation.setOccupation(occ);		
		patientInformation.setLevelOfEducation(PatientInformation.LevelOfEducation.Enum.forString((props.getProperty("pg1_eduLevel"))));
		PatientInformation.Hin hin = patientInformation.addNewHin();
		hin.setStringValue(props.getProperty("c_hin", ""));
		hin.setType(PatientInformation.Hin.Type.Enum.forString(props.getProperty("c_hinType")));		
		patientInformation.setFileNo(props.getProperty("c_fileNo", ""));
		String maritalStatus = props.getProperty("pg1_maritalStatus").toUpperCase();
		if(maritalStatus.equals("M") || maritalStatus.equals("CL")) {
			patientInformation.setMaritalStatus(PatientInformation.MaritalStatus.Enum.forString("MS005"));
		}
		else if(maritalStatus.equals("S") ) {
			patientInformation.setMaritalStatus(PatientInformation.MaritalStatus.Enum.forString("MS015"));
		} else if(maritalStatus.equals("DS") ) {
			patientInformation.setMaritalStatus(PatientInformation.MaritalStatus.Enum.forString("MS010"));
		} else {
			patientInformation.setMaritalStatus(PatientInformation.MaritalStatus.Enum.forString("UN"));
		}

		PatientInformation.EthnicBackground eb = patientInformation.addNewEthnicBackground();
		PatientInformation.EthnicBackground.Value v1 = eb.addNewValue();
		v1.setParent(EthnicBackground.Value.Parent.MATERNAL);
		v1.setStringValue(props.getProperty("pg1_ethnicBgMother", ""));
		PatientInformation.EthnicBackground.Value v2 = eb.addNewValue();
		v2.setParent(EthnicBackground.Value.Parent.PATERNAL);
		v2.setStringValue(props.getProperty("pg1_ethnicBgFather", ""));
		patientInformation.setEthnicBackground(eb);
		patientInformation.setAllergies(props.getProperty("c_allergies", ""));
		PatientInformation.Medications meds = patientInformation.addNewMedications();
		meds.setValueArray(new PatientInformation.Medications.Value.Enum[]{PatientInformation.Medications.Value.OTHER});
		meds.setOtherArray(new String[]{props.getProperty("c_meds", "")});
		patientInformation.setMedications(meds);
	}


	/* ar2 */

	void populateAr2(AR2 ar2) {
		String rfLengthStr = props.getProperty("rf_num", "0");
		if(rfLengthStr.length()==0) {rfLengthStr="0";}
		int rfLength = Integer.parseInt(rfLengthStr);
		for(int x=0;x<rfLength;x++) {
			int y = x+1;
			if(props.getProperty("c_riskFactors"+y, "").length()==0 && props.getProperty("c_planManage"+y, "").length() == 0) {
				continue;
			}
			RiskFactorItemType riskFactorItem = ar2.addNewRiskFactorList();			
			riskFactorItem.setRiskFactor(props.getProperty("c_riskFactors"+y, ""));
			riskFactorItem.setPlanOfManagement(props.getProperty("c_planManage"+y, ""));
		}		

		RecommendedImmunoprophylaxisType recommendedImmunoprophylaxis = ar2.addNewRecommendedImmunoprophylaxis();
		populateRecommendedImmunoprophylaxis(recommendedImmunoprophylaxis);

		String svLengthStr = props.getProperty("sv_num", "0");
		if(svLengthStr.length()==0) {svLengthStr="0";}
		int svLength = Integer.parseInt(svLengthStr);
		for(int x=0;x<svLength;x++) {
			int y = x+1;
			if(props.getProperty("pg2_year"+y,"").length()==0 && props.getProperty("pg2_gest"+y,"").length()==0 && props.getProperty("pg2_wt"+y,"").length()==0
					&& props.getProperty("pg2_BP"+y,"").length()==0 && props.getProperty("pg2_urinePr"+y,"").length()==0/* && props.getProperty("pg2_urineGl"+y,"").length()==0*/ 
					&& props.getProperty("pg2_presn1"+y,"").length()==0 && props.getProperty("pg2_FHR"+y,"").length()==0 && props.getProperty("pg2_comments"+y,"").length()==0 ) {
				continue;
			}
			SubsequentVisitItemType visit = ar2.addNewSubsequentVisitList();
			if(props.getProperty("pg2_date"+y, "").length()>0) {
				try {
					visit.setDate(createDate(dateFormatter.parse((props.getProperty("pg2_date"+y)))));
				}catch(ParseException e){}
			} else {
				visit.setDate(null);
			}
			visit.setGa(props.getProperty("pg2_gest"+y, ""));
			visit.setWeight(props.getProperty("pg2_wt"+y, "").length()>0?Float.parseFloat(props.getProperty("pg2_wt"+y, "")):0);
			visit.setBp(props.getProperty("pg2_BP"+y, ""));
			visit.setUrinePR(props.getProperty("pg2_urinePr"+y, ""));
			/*visit.setUrineGI(props.getProperty("pg2_urineGl"+y, ""));*/
			visit.setUrineGI("");
			visit.setSFH(props.getProperty("pg2_ht"+y, ""));
			visit.setPresentationPosition(props.getProperty("pg2_presn"+y, ""));
			visit.setFHRFm(props.getProperty("pg2_FHR"+y, ""));
			visit.setComments(props.getProperty("pg2_comments"+y, ""));
		}
		
		
		String usLengthStr = props.getProperty("us_num", "0");
		if(usLengthStr.length()==0) {usLengthStr="0";}
		int usLength = Integer.parseInt(usLengthStr);
		for(int x=0;x<usLength;x++) {
			int y = x+1;
			if(props.getProperty("ar2_uDate"+y).length()==0 && props.getProperty("ar2_uGA"+y).length()==0 && props.getProperty("ar2_uResults"+y).length()==0) {
				continue;
			}			
			UltrasoundType ultrasound = ar2.addNewUltrasound();
			if(props.getProperty("ar2_uDate"+y).length()>0) {
				try {
					ultrasound.setDate(createDate(dateFormatter.parse(props.getProperty("ar2_uDate"+y))));
				}catch(ParseException e){
					logger.warn("can't parse date " + props.getProperty("ar2_uDate"+y));
				}
			} else {
				ultrasound.setDate(null);
			}
			ultrasound.setGa(props.getProperty("ar2_uGA"+y));
			ultrasound.setResults(props.getProperty("ar2_uResults"+y)!=null?props.getProperty("ar2_uResults"+y):"");
		}

		AdditionalLabInvestigationsType additionalLabInvestigations = ar2.addNewAdditionalLabInvestigations();
		populateAdditionalLabInvestigations(additionalLabInvestigations);

		DiscussionTopicsType discussionTopics = ar2.addNewDiscussionTopics();
		popuplateDiscussionTopics(discussionTopics);

		setSignatures(ar2.addNewSignatures());
	}

	void populateRecommendedImmunoprophylaxis(RecommendedImmunoprophylaxisType recommendedImmunoprophylaxis) {
		recommendedImmunoprophylaxis.setRhNegative(props.getProperty("ar2_rhNeg", "").length()>0?true:false);
		if(props.getProperty("ar2_rhIG", "").length()>0) {
			try {
				recommendedImmunoprophylaxis.setRhIgGiven(this.createDate(dateFormatter.parse(props.getProperty("ar2_rhIG"))));
			}catch(ParseException e) {}
		} else {
			recommendedImmunoprophylaxis.setRhIgGiven(null);
		}
		recommendedImmunoprophylaxis.setRubella(props.getProperty("ar2_rubella", "").length()>0?true:false);
		recommendedImmunoprophylaxis.setNewbornHepIG(props.getProperty("ar2_hepBIG", "").length()>0?true:false);
		recommendedImmunoprophylaxis.setHepBVaccine(props.getProperty("ar2_hepBVac", "").length()>0?true:false);
	}

	void populateAdditionalLabInvestigations(AdditionalLabInvestigationsType additionalLabInvestigations) {
		additionalLabInvestigations.setHb(props.getProperty("ar2_hb", ""));
		additionalLabInvestigations.setBloodGroup(AdditionalLabInvestigationsType.BloodGroup.Enum.forString(props.getProperty("ar2_bloodGroup")));
		if(props.getProperty("ar2_bloodGroup").equals("NDONE")) {
			additionalLabInvestigations.setBloodGroup(AdditionalLabInvestigationsType.BloodGroup.ND);			
		}
		additionalLabInvestigations.setRh(AdditionalLabInvestigationsType.Rh.Enum.forString(props.getProperty("ar2_rh")));		
		additionalLabInvestigations.setRepeatABS(props.getProperty("ar2_labABS", ""));
		additionalLabInvestigations.setGCT(props.getProperty("ar2_lab1GCT", ""));
		additionalLabInvestigations.setGTT(props.getProperty("ar2_lab2GTT", ""));
		additionalLabInvestigations.setGBS(AdditionalLabInvestigationsType.GBS.Enum.forString(props.getProperty("ar2_strep")));
		CustomLab cl1 = additionalLabInvestigations.addNewCustomLab1();
		cl1.setLabel(props.getProperty("ar2_labCustom1Label", ""));
		cl1.setResult(props.getProperty("ar2_labCustom1Result", ""));
		CustomLab cl2 = additionalLabInvestigations.addNewCustomLab2();
		cl2.setLabel(props.getProperty("ar2_labCustom2Label", ""));
		cl2.setResult(props.getProperty("ar2_labCustom2Result", ""));
		CustomLab cl3 = additionalLabInvestigations.addNewCustomLab3();
		cl3.setLabel(props.getProperty("ar2_labCustom3Label", ""));
		cl3.setResult(props.getProperty("ar2_labCustom3Result", ""));
		CustomLab cl4 = additionalLabInvestigations.addNewCustomLab4();
		cl4.setLabel(props.getProperty("ar2_labCustom4Label", ""));
		cl4.setResult(props.getProperty("ar2_labCustom4Result", ""));
	}
	
	void setSignaturesAR1(SignatureType signatures) {
		signatures.setSignature(props.getProperty("pg1_signature", ""));
		if(props.getProperty("pg1_formDate", "").length()>0) {
			try {
				signatures.setDate(createDate(dateFormatter.parse((props.getProperty("pg1_formDate")))));
			}catch(ParseException e) {}
		} else {
			//signatures.setDate(null);
		}
		
		if(props.getProperty("pg1_signature2", "").length()>0){ 
			signatures.setSignature2(props.getProperty("pg1_signature2", ""));
			if(props.getProperty("pg1_formDate2", "").length()>0) {
				try {
					signatures.setDate2(createDate(dateFormatter.parse((props.getProperty("pg1_formDate2")))));
				}catch(ParseException e) {}
			} else {
				//signatures.setDate2(null);
			}
		}
	}
	
	void setSignatures(SignatureType signatures) {
		signatures.setSignature(props.getProperty("pg2_signature", ""));
		if(props.getProperty("pg2_formDate", "").length()>0) {
			try {
				signatures.setDate(createDate(dateFormatter.parse((props.getProperty("pg2_formDate")))));
			}catch(ParseException e) {}
		} else {
			//signatures.setDate(null);
		}
		
		if(props.getProperty("pg2_signature2", "").length()>0) {
			signatures.setSignature2(props.getProperty("pg2_signature2", ""));
			if(props.getProperty("pg2_formDate2", "").length()>0) {
				try {
					signatures.setDate2(createDate(dateFormatter.parse((props.getProperty("pg2_formDate2")))));
				}catch(ParseException e) {}
			} else {
				//signatures.setDate2(null);
			}
		}

	}

	void popuplateDiscussionTopics(DiscussionTopicsType discussionTopics) {
		discussionTopics.setExercise(props.getProperty("ar2_exercise", "").length()>0?true:false);
		discussionTopics.setWorkPlan(props.getProperty("ar2_workPlan", "").length()>0?true:false);
		discussionTopics.setIntercourse(props.getProperty("ar2_intercourse", "").length()>0?true:false);
		discussionTopics.setTravel(props.getProperty("ar2_travel", "").length()>0?true:false);
		discussionTopics.setPrenatalClasses(props.getProperty("ar2_prenatal", "").length()>0?true:false);
		discussionTopics.setBirthPlan(props.getProperty("ar2_birth", "").length()>0?true:false);
		discussionTopics.setOnCallProviders(props.getProperty("ar2_onCall", "").length()>0?true:false);
		discussionTopics.setPretermLabour(props.getProperty("ar2_preterm", "").length()>0?true:false);
		discussionTopics.setPROM(props.getProperty("ar2_prom", "").length()>0?true:false);
		discussionTopics.setAPH(props.getProperty("ar2_aph", "").length()>0?true:false);
		discussionTopics.setFetalMovement(props.getProperty("ar2_fetal", "").length()>0?true:false);
		discussionTopics.setAdmissionTiming(props.getProperty("ar2_admission", "").length()>0?true:false);
		discussionTopics.setPainManagement(props.getProperty("ar2_pain", "").length()>0?true:false);
		discussionTopics.setLabourSupport(props.getProperty("ar2_labour", "").length()>0?true:false);
		discussionTopics.setBreastFeeding(props.getProperty("ar2_breast", "").length()>0?true:false);
		discussionTopics.setCircumcision(props.getProperty("ar2_circumcision", "").length()>0?true:false);
		discussionTopics.setDischargePlanning(props.getProperty("ar2_dischargePlan", "").length()>0?true:false);
		discussionTopics.setDepression(props.getProperty("ar2_depression", "").length()>0?true:false);
		discussionTopics.setContraception(props.getProperty("ar2_contraception", "").length()>0?true:false);
		discussionTopics.setCarSeatSafety(props.getProperty("ar2_car", "").length()>0?true:false);
		discussionTopics.setPostpartumCare(props.getProperty("ar2_postpartumCare", "").length()>0?true:false);
	}
}
