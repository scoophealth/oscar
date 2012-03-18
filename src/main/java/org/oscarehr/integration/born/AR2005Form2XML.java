// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License.
// * This program is free software; you can redistribute it and/or
// * modify it under the terms of the GNU General Public License
// * as published by the Free Software Foundation; either version 2
// * of the License, or (at your option) any later version. *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
// * along with this program; if not, write to the Free Software
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
// *
// * <OSCAR TEAM>
// * This software was written for the
// * Department of Family Medicine
// * McMaster University
// * Hamilton
// * Ontario, Canada
// *
// -----------------------------------------------------------------------------------------------------------------------

package org.oscarehr.integration.born;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.apache.xmlbeans.XmlOptions;
import org.oscarehr.util.MiscUtils;
import org.oscarmcmaster.ar2005.AR1;
import org.oscarmcmaster.ar2005.AR2;
import org.oscarmcmaster.ar2005.ARRecord;
import org.oscarmcmaster.ar2005.ARRecordSet;
import org.oscarmcmaster.ar2005.ARRecordSetDocument;
import org.oscarmcmaster.ar2005.AdditionalLabInvestigationsType;
import org.oscarmcmaster.ar2005.BirthAttendants;
import org.oscarmcmaster.ar2005.CurrentPregnancyType;
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



public class AR2005Form2XML {

	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");

	public AR2005Form2XML() {

	}

	public void generateXML(ResultSet rs, OutputStream os) throws SQLException,IOException {

		ARRecordSetDocument recordSetD = ARRecordSetDocument.Factory.newInstance();
		ARRecordSet recordSet = recordSetD.addNewARRecordSet();

		while(rs.next()) {

			ARRecord arRecord = recordSet.addNewARRecord();
			AR1 ar1 = arRecord.addNewAR1();
			populateAr1(rs,ar1);
			AR2 ar2 = arRecord.addNewAR2();
			populateAr2(rs,ar2);

		}

		HashMap<String,String> suggestedPrefixes = new HashMap<String,String>();
		suggestedPrefixes.put("http://www.oscarmcmaster.org/AR2005", "");
		XmlOptions opts = new XmlOptions();
		opts.setSaveSuggestedPrefixes(suggestedPrefixes);
		opts.setSavePrettyPrint();

		recordSetD.save(os,opts);
	}

	void populateAr2(ResultSet rs, AR2 ar2) throws SQLException {
		for(int x=1;x<=7;x++) {
			RiskFactorItemType riskFactorItem = ar2.addNewRiskFactorList();
			riskFactorItem.setRiskFactor(rs.getString("c_riskFactors"+x));
			riskFactorItem.setPlanOfManagement(rs.getString("c_planManage"+x));
		}
		RecommendedImmunoprophylaxisType recommendedImmunoprophylaxis = ar2.addNewRecommendedImmunoprophylaxis();
		populateRecommendedImmunoprophylaxis(recommendedImmunoprophylaxis,rs);

		for(int page=2;page<=4;page++) {
			for(int x=1;x<=18;x++) {
				int pos = (page-2)*18 + x;
				SubsequentVisitItemType visit = ar2.addNewSubsequentVisitList();
				visit.setPage(page);
				visit.setPosition(x);
				Date date = rs.getDate("pg"+page+"_date"+pos);
				if(date != null) {
					visit.setDate(createDate(date));
				}
				visit.setGa(rs.getInt("pg"+page+"_gest"+pos));
				visit.setWeight(rs.getFloat("pg"+page+"_wt"+pos));
				visit.setBp(rs.getString("pg"+page+"_BP"+pos));
				visit.setUrinePR(rs.getString("pg"+page+"_urinePr"+pos));
				visit.setUrineGI(rs.getString("pg"+page+"_urineGl"+pos));
				visit.setSFH(rs.getString("pg"+page+"_ht"+pos));
				visit.setPresentationPosition(rs.getString("pg"+page+"_presn"+pos));
				visit.setFHRFm(rs.getString("pg"+page+"_FHR"+pos));
				visit.setComments(rs.getString("pg"+page+"_comments"+pos));
			}
		}

		for(int x=1;x<=4;x++) {
			UltrasoundType ultrasound = ar2.addNewUltrasound();
			Date date = rs.getDate("ar2_uDate"+x);
			if(date != null) {
				ultrasound.setDate(createDate(date));
			}
			ultrasound.setGa(rs.getInt("ar2_uGA"+x));
			ultrasound.setResults(rs.getString("ar2_uResults"+x));
		}

		AdditionalLabInvestigationsType additionalLabInvestigations = ar2.addNewAdditionalLabInvestigations();
		populateAdditionalLabInvestigations(additionalLabInvestigations, rs);

		DiscussionTopicsType discussionTopics = ar2.addNewDiscussionTopics();
		popuplateDiscussionTopics(discussionTopics,rs);

		setSignatures(ar2.addNewSignatures(),2,rs);
		setSignatures(ar2.addNewSignatures(),3,rs);
		setSignatures(ar2.addNewSignatures(),4,rs);
	}

	void setSignatures(SignatureType signatures, int page, ResultSet rs) throws SQLException {
		signatures.setPage(page);
		signatures.setSignature(rs.getString("pg"+page+"_signature"));
		Date date = rs.getDate("pg"+page+"_formDate");
		if(date != null) {
			signatures.setDate(createDate(date));
		}
		signatures.setSignature2(rs.getString("pg"+page+"_signature2"));
		date = rs.getDate("pg"+page+"_formDate2");
		if(date != null) {
			signatures.setDate2(createDate(date));
		}
	}

	void popuplateDiscussionTopics(DiscussionTopicsType discussionTopics, ResultSet rs) throws SQLException {
		discussionTopics.setExercise(rs.getBoolean("ar2_exercise"));
		discussionTopics.setWorkPlan(rs.getBoolean("ar2_workPlan"));
		discussionTopics.setIntercourse(rs.getBoolean("ar2_intercourse"));
		discussionTopics.setTravel(rs.getBoolean("ar2_travel"));
		discussionTopics.setPrenatalClasses(rs.getBoolean("ar2_prenatal"));
		discussionTopics.setBirthPlan(rs.getBoolean("ar2_birth"));
		discussionTopics.setOnCallProviders(rs.getBoolean("ar2_oncall"));
		discussionTopics.setPretermLabour(rs.getBoolean("ar2_preterm"));
		discussionTopics.setPROM(rs.getBoolean("ar2_prom"));
		discussionTopics.setAPH(rs.getBoolean("ar2_aph"));
		discussionTopics.setFetalMovement(rs.getBoolean("ar2_fetal"));
		discussionTopics.setAdmissionTiming(rs.getBoolean("ar2_admission"));
		discussionTopics.setPainManagement(rs.getBoolean("ar2_pain"));
		discussionTopics.setLabourSupport(rs.getBoolean("ar2_labour"));
		discussionTopics.setBreastFeeding(rs.getBoolean("ar2_breast"));
		discussionTopics.setCircumcision(rs.getBoolean("ar2_circumcision"));
		discussionTopics.setDischargePlanning(rs.getBoolean("ar2_dischargePlan"));
		discussionTopics.setCarSeatSafety(rs.getBoolean("ar2_car"));
		discussionTopics.setDepression(rs.getBoolean("ar2_depression"));
		discussionTopics.setContraception(rs.getBoolean("ar2_contraception"));
		discussionTopics.setPostpartumCare(rs.getBoolean("ar2_postpartumCare"));
	}

	void populateAdditionalLabInvestigations(AdditionalLabInvestigationsType additionalLabInvestigations, ResultSet rs) throws SQLException {
		additionalLabInvestigations.setHb(rs.getString("ar2_hb"));
		additionalLabInvestigations.setBloodGroup(rs.getString("ar2_bloodGroup"));
		additionalLabInvestigations.setRh(rs.getString("ar2_rh"));
		additionalLabInvestigations.setRepeatABS(rs.getString("ar2_labABS"));
		additionalLabInvestigations.setGCT(rs.getString("ar2_lab1GCT"));
		additionalLabInvestigations.setGTT(rs.getString("ar2_lab2GTT"));
		additionalLabInvestigations.setGBS(rs.getString("ar2_strep"));

	}

	void populateRecommendedImmunoprophylaxis(RecommendedImmunoprophylaxisType recommendedImmunoprophylaxis, ResultSet rs) throws SQLException {
		recommendedImmunoprophylaxis.setRhNegative(rs.getBoolean("ar2_rhNeg"));
		recommendedImmunoprophylaxis.setRhIgGiven(rs.getString("ar2_rhIg"));
		recommendedImmunoprophylaxis.setRubella(rs.getBoolean("ar2_rubella"));
		recommendedImmunoprophylaxis.setNewbornHepIG(rs.getBoolean("ar2_hepBIG"));
		recommendedImmunoprophylaxis.setHepBVaccine(rs.getBoolean("ar2_hepBVac"));
	}

	void populateAr1(ResultSet rs, AR1 ar1) throws SQLException {
		ar1.setDemographicNo(rs.getInt("demographic_no"));
		ar1.setProviderNo(rs.getString("provider_no"));
		Calendar formCreatedDate = Calendar.getInstance();
		formCreatedDate.setTime(rs.getDate("formCreated"));
		ar1.setFormCreated(formCreatedDate);
		Calendar formEditedDate = Calendar.getInstance();
		formEditedDate.setTime(rs.getTimestamp("formEdited"));
		ar1.setFormEdited(formEditedDate);
		ar1.setId(rs.getInt("ID"));
		PatientInformation patientInformation = ar1.addNewPatientInformation();
		PartnerInformation partnerInformation = ar1.addNewPartnerInformation();
		PractitionerInformation practitionerInformation = ar1.addNewPractitionerInformation();
		PregnancyHistory pregnancyHistory = ar1.addNewPregnancyHistory();
		ObstetricalHistory obstetricalHistory = ar1.addNewObstetricalHistory();
		MedicalHistoryAndPhysicalExam medicalHistoryAndPhysicalExam = ar1.addNewMedicalHistoryAndPhysicalExam();
		InitialLaboratoryInvestigations initialLaboratoryInvestigations = ar1.addNewInitialLaboratoryInvestigations();

		populatePatientInformation(patientInformation,rs);
		populatePartnerInformation(partnerInformation,rs);
		populatePractitionerInformation(practitionerInformation,rs);
		populatePregnancyHistory(pregnancyHistory,rs);
		populateObstetricalHistory(obstetricalHistory,rs);
		populateMedicalHistoryAndPhysicalExam(medicalHistoryAndPhysicalExam,rs);
		populateInitialLaboratoryInvestigations(initialLaboratoryInvestigations,rs);
		ar1.setComments(rs.getString("pg1_commentsAR1"));
		ar1.setSignature1(rs.getString("pg1_signature"));
		ar1.setSignatureDate1(createDate(rs.getDate("pg1_formDate")));
		ar1.setSignature2(rs.getString("pg1_signature2"));
		ar1.setSignatureDate2(createDate(rs.getDate("pg1_formDate2")));
	}

	void populateInitialLaboratoryInvestigations(InitialLaboratoryInvestigations initialLaboratoryInvestigations,ResultSet rs) throws SQLException {
		initialLaboratoryInvestigations.setHbResult(rs.getString("pg1_labHb"));
		initialLaboratoryInvestigations.setHivResult(rs.getString("pg1_labHIV"));
		initialLaboratoryInvestigations.setHivCounsel(rs.getBoolean("pg1_labHIVCounsel"));
		initialLaboratoryInvestigations.setMcvResult(rs.getString("pg1_labMCV"));
		initialLaboratoryInvestigations.setAboResult(rs.getString("pg1_labABO"));
		initialLaboratoryInvestigations.setRhResult(rs.getString("pg1_labRh"));
		initialLaboratoryInvestigations.setPapResult(rs.getString("pg1_labLastPap"));
		try {
			initialLaboratoryInvestigations.setLastPapDate(this.createDate(dateFormatter.parse(rs.getString("pg1_labLastPapDate"))));
		}catch(ParseException e) {
			MiscUtils.getLogger().warn("error",e);
		}
		initialLaboratoryInvestigations.setAntibodyResult(rs.getString("pg1_labAntiScr"));
		initialLaboratoryInvestigations.setGcResult(rs.getString("pg1_labGC"));
		initialLaboratoryInvestigations.setRubellaResult(rs.getString("pg1_labRubella"));
		initialLaboratoryInvestigations.setUrineResult(rs.getString("pg1_labUrine"));
		initialLaboratoryInvestigations.setHbsAgResult(rs.getString("pg1_labHBsAg"));
		initialLaboratoryInvestigations.setVdrlResult(rs.getString("pg1_labVDRL"));
		initialLaboratoryInvestigations.setSickleCellResult(rs.getString("pg1_labSickle"));
		PrenatalGeneticScreeningType prenatalGeneticScreening = initialLaboratoryInvestigations.addNewPrenatalGenericScreening();
		populatePrenatalGeneticScreening(prenatalGeneticScreening,rs);
	}

	void populatePrenatalGeneticScreening(PrenatalGeneticScreeningType prenatalGeneticScreening, ResultSet rs) throws SQLException {
		prenatalGeneticScreening.setMSSIPSFTS(rs.getString("pg1_geneticA"));
		prenatalGeneticScreening.setEDBCVS(rs.getString("pg1_geneticB"));
		prenatalGeneticScreening.setMSAFP(rs.getString("pg1_geneticC"));
		prenatalGeneticScreening.setDeclined(rs.getBoolean("pg1_geneticD"));
	}

	void populateMedicalHistoryAndPhysicalExam(MedicalHistoryAndPhysicalExam medicalHistoryAndPhysicalExam, ResultSet rs) throws SQLException {
		CurrentPregnancyType currentPregnancy = medicalHistoryAndPhysicalExam.addNewCurrentPregnancy();
		MedicalHistoryType medicalHistory = medicalHistoryAndPhysicalExam.addNewMedicalHistory();
		GenericHistoryType genericHistory = medicalHistoryAndPhysicalExam.addNewGenericHistory();
		InfectiousDiseaseType infectiousDiseases = medicalHistoryAndPhysicalExam.addNewInfectiousDisease();
		PsychosocialType psychosocial  = medicalHistoryAndPhysicalExam.addNewPsychosocial();
		FamilyHistoryType familyHistory = medicalHistoryAndPhysicalExam.addNewFamilyHistory();
		PhysicalExaminationType physicalExamination = medicalHistoryAndPhysicalExam.addNewPhysicalExamination();

		populateCurrentPregnancy(currentPregnancy,rs);
		populateMedicalHistory(medicalHistory,rs);
		populateGenericHistory(genericHistory,rs);
		populateInfectiousDisease(infectiousDiseases,rs);
		populatePsychosocial(psychosocial,rs);
		populateFamilyHistory(familyHistory,rs);
		populatePhysicalExamination(physicalExamination,rs);
	}

	YesNoNullType getYesNoNullType(String yes, String no, ResultSet rs) throws SQLException {
		YesNoNullType result = YesNoNullType.Factory.newInstance();
		boolean yesVal = rs.getBoolean(yes);
		boolean noVal = rs.getBoolean(no);
		if(yesVal) {
			result.setYes(true);
		} else if(noVal) {
			result.setNo(true);
		} else {
			result.setNull(true);
		}
		return result;
	}

	void populateCurrentPregnancy(CurrentPregnancyType currentPregnancy, ResultSet rs) throws SQLException {
		currentPregnancy.setBleeding(getYesNoNullType("pg1_cp1","pg1_cp1N",rs));
		currentPregnancy.setNausea(getYesNoNullType("pg1_cp2","pg1_cp2N",rs));
		currentPregnancy.setSmoking(getYesNoNullType("pg1_cp3","pg1_cp3N",rs));
		currentPregnancy.setCigsPerDay(rs.getInt("pg1_box3"));
		currentPregnancy.setAlcoholDrugs(getYesNoNullType("pg1_cp4","pg1_cp4N",rs));
		currentPregnancy.setOccEnvRisks(getYesNoNullType("pg1_cp8","pg1_cp8N",rs));
		currentPregnancy.setDietaryRes(getYesNoNullType("pg1_naDietRes","pg1_naDietBal",rs));
		currentPregnancy.setCalciumAdequate(getYesNoNullType("pg1_naMilk","pg1_naMilkN",rs));
		currentPregnancy.setFolate(getYesNoNullType("pg1_naFolic","pg1_naFolicN",rs));
	}

	void populateMedicalHistory(MedicalHistoryType medicalHistory, ResultSet rs) throws SQLException {
		medicalHistory.setHypertension(getYesNoNullType("pg1_yes9","pg1_no9",rs));
		medicalHistory.setEndorince(getYesNoNullType("pg1_yes10","pg1_no10",rs));
		medicalHistory.setUrinaryTract(getYesNoNullType("pg1_yes12","pg1_no12",rs));
		medicalHistory.setCardiac(getYesNoNullType("pg1_yes13","pg1_no13",rs));
		medicalHistory.setLiver(getYesNoNullType("pg1_yes14","pg1_no14",rs));
		medicalHistory.setGynaecology(getYesNoNullType("pg1_yes17","pg1_no17",rs));
		medicalHistory.setHem(getYesNoNullType("pg1_yes22","pg1_no22",rs));
		medicalHistory.setSurgeries(getYesNoNullType("pg1_yes20","pg1_no20",rs));
		medicalHistory.setBloodTransfusion(getYesNoNullType("pg1_bloodTranY","pg1_bloodTranN",rs));
		medicalHistory.setAnesthetics(getYesNoNullType("pg1_yes21","pg1_no21",rs));
		medicalHistory.setPsychiatry(getYesNoNullType("pg1_yes24","pg1_no24",rs));
		medicalHistory.setEpilepsy(getYesNoNullType("pg1_yes15","pg1_no15",rs));
		medicalHistory.setOther(getYesNoNullType("pg1_yes25","pg1_no25",rs));
		medicalHistory.setOtherDescr(rs.getString("pg1_box25"));
	}

	void populateGenericHistory(GenericHistoryType geneticHistory, ResultSet rs) throws SQLException {
		geneticHistory.setAtRisk(getYesNoNullType("pg1_yes27","pg1_no27",rs));
		geneticHistory.setDevelopmentalDelay(getYesNoNullType("pg1_yes31","pg1_no31",rs));
		geneticHistory.setCongenitalAnomolies(getYesNoNullType("pg1_yes32","pg1_no32",rs));
		geneticHistory.setChromosomalDisorders(getYesNoNullType("pg1_yes34","pg1_no34",rs));
		geneticHistory.setGeneticDisorders(getYesNoNullType("pg1_yes35","pg1_no35",rs));
	}

	void populateInfectiousDisease(InfectiousDiseaseType infectiousDiseases, ResultSet rs) throws SQLException {
		infectiousDiseases.setVaricella(getYesNoNullType("pg1_idt40","pg1_idt40N",rs));
		infectiousDiseases.setStd(getYesNoNullType("pg1_idt38","pg1_idt38N",rs));
		infectiousDiseases.setTuberculosis(getYesNoNullType("pg1_idt42","pg1_idt42N",rs));
		infectiousDiseases.setOther(getYesNoNullType("pg1_infectDisOtherY","pg1_infectDisOtherN",rs));
		infectiousDiseases.setOtherDescr(rs.getString("pg1_infectDisOther"));
	}

	void populatePsychosocial(PsychosocialType psychosocial, ResultSet rs) throws SQLException {
		psychosocial.setPoortSocialSupport(getYesNoNullType("pg1_pdt43","pg1_pdt43N",rs));
		psychosocial.setRelationshipProblems(getYesNoNullType("pg1_pdt44","pg1_pdt44N",rs));
		psychosocial.setEmotionalDepression(getYesNoNullType("pg1_pdt45","pg1_pdt45N",rs));
		psychosocial.setSubstanceAbuse(getYesNoNullType("pg1_pdt46","pg1_pdt46N",rs));
		psychosocial.setFamilyViolence(getYesNoNullType("pg1_pdt47","pg1_pdt47N",rs));
		psychosocial.setParentingConcerns(getYesNoNullType("pg1_pdt48","pg1_pdt48N",rs));
		psychosocial.setReligiousCultural(getYesNoNullType("pg1_reliCultY","pg1_reliCultN",rs));
	}

	void populateFamilyHistory(FamilyHistoryType familyHistory, ResultSet rs) throws SQLException {
		familyHistory.setAtRisk(getYesNoNullType("pg1_fhRiskY","pg1_fhRiskN",rs));
	}

	NormalAbnormalNullType getNormalAbnormalNullType(String normal, String abnormal, ResultSet rs) throws SQLException {
		NormalAbnormalNullType result = NormalAbnormalNullType.Factory.newInstance();
		boolean normalVal = rs.getBoolean(normal);
		boolean abnormalVal = rs.getBoolean(abnormal);
		if(normalVal) {
			result.setNormal(true);
		} else if(abnormalVal) {
			result.setAbnormal(true);
		} else {
			result.setNull(true);
		}
		return result;
	}

	void populatePhysicalExamination(PhysicalExaminationType physicalExamination, ResultSet rs) throws SQLException {
		physicalExamination.setHeight(rs.getString("pg1_ht"));
		physicalExamination.setWeight(rs.getString("pg1_wt"));
		physicalExamination.setBmi(rs.getString("c_bmi"));
		physicalExamination.setBp(rs.getString("pg1_BP"));

		physicalExamination.setThyroid(getNormalAbnormalNullType("pg1_thyroid","pg1_thyroidA",rs));
		physicalExamination.setChest(getNormalAbnormalNullType("pg1_chest","pg1_chestA",rs));
		physicalExamination.setBreasts(getNormalAbnormalNullType("pg1_breasts","pg1_breastsA",rs));
		physicalExamination.setCardiovascular(getNormalAbnormalNullType("pg1_cardio","pg1_cardioA",rs));
		physicalExamination.setAbdomen(getNormalAbnormalNullType("pg1_abdomen","pg1_abdomenA",rs));
		physicalExamination.setVaricosities(getNormalAbnormalNullType("pg1_vari","pg1_variA",rs));
		physicalExamination.setExernalGenitals(getNormalAbnormalNullType("pg1_extGen","pg1_extGenA",rs));
		physicalExamination.setCervixVagina(getNormalAbnormalNullType("pg1_cervix","pg1_cervixA",rs));
		physicalExamination.setUterus(getNormalAbnormalNullType("pg1_uterus","pg1_uterusA",rs));
		physicalExamination.setUterusSize(rs.getString("pg1_uterusBox"));
		physicalExamination.setThyroid(getNormalAbnormalNullType("pg1_adnexa","pg1_adnexaA",rs));
		physicalExamination.setThyroid(getNormalAbnormalNullType("pg1_pExOtherN","pg1_pExOtherA",rs));
		physicalExamination.setOtherDescr(rs.getString("pg1_pExOther"));
	}



	void populateObstetricalHistory(ObstetricalHistory obstetricalHistory, ResultSet rs) throws SQLException {
		for(int x=1;x<7;x++) {
			ObstetricalHistoryItemList item = obstetricalHistory.addNewObsList();
			item.setYear(rs.getString("pg1_year"+x));
			String sex = rs.getString("pg1_sex"+x);
			if(sex.equalsIgnoreCase("m")) {
				item.setSex(ObstetricalHistoryItemList.Sex.M);
			} else if(sex.equalsIgnoreCase("f")) {
				item.setSex(ObstetricalHistoryItemList.Sex.F);
			} else {
				item.setSex(null);
			}
			item.setGestAge(rs.getInt("pg1_oh_gest"+x));
			item.setBirthWeight(rs.getString("pg1_weight"+x));
			item.setLengthOfLabour(rs.getString("pg1_length"+x));
			item.setPlaceOfBirth(rs.getString("pg1_place"+x));

			if(rs.getBoolean("pg1_svb1")) {
				item.setTypeOfDelivery(ObstetricalHistoryItemList.TypeOfDelivery.SVB);
			} else if(rs.getBoolean("pg1_cs1")){
				item.setTypeOfDelivery(ObstetricalHistoryItemList.TypeOfDelivery.CS);
			} else if(rs.getBoolean("pg1_ass1")) {
				item.setTypeOfDelivery(ObstetricalHistoryItemList.TypeOfDelivery.ASSD);
			} else {
				item.setTypeOfDelivery(null);
			}
			item.setComments(rs.getString("pg1_oh_comments"+x));
		}

	}

	ObstetricalHistoryItemList.Sex.Enum getObstetricalSex(String field) {
		//if(field )
		return null;
	}

	Calendar createDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	void populatePregnancyHistory(PregnancyHistory pregnancyHistory, ResultSet rs) throws SQLException {

		pregnancyHistory.setLMP(createDate(rs.getDate("pg1_menLMP")));
		pregnancyHistory.setLMPCertain(getLMPCertain(rs));
		pregnancyHistory.setMenCycle(rs.getString("pg1_menCycle"));
		pregnancyHistory.setMenCycleRegular(getMenCycleRegular(rs));
		pregnancyHistory.setContraceptiveType(rs.getString("pg1_contracep"));
		pregnancyHistory.setContraceptiveLastUsed(createDate(rs.getDate("pg1_lastUsed")));
		pregnancyHistory.setMenstrualEDB(createDate(rs.getDate("pg1_menEDB")));
		pregnancyHistory.setFinalEDB(createDate(rs.getDate("c_finalEDB")));
		getDatingMethod(pregnancyHistory.addNewDatingMethods(),rs);
		pregnancyHistory.setGravida(rs.getInt("c_gravida"));
		pregnancyHistory.setTerm(rs.getInt("c_term"));
		pregnancyHistory.setPremature(rs.getInt("c_prem"));
		pregnancyHistory.setAbortuses(rs.getInt("c_abort"));
		pregnancyHistory.setLiving(rs.getInt("c_living"));
	}

	void getDatingMethod(DatingMethods datingMethods,ResultSet rs) throws SQLException {
		datingMethods.setDates(rs.getBoolean("pg1_edbByDate"));
		datingMethods.setT1US(rs.getBoolean("pg1_edbByT1"));
		datingMethods.setT2US(rs.getBoolean("pg1_edbByT2"));
		datingMethods.setArt(rs.getBoolean("pg1_edbByART"));
	}

	YesNoNullType getLMPCertain(ResultSet rs) throws SQLException {
		boolean yes = rs.getBoolean("pg1_psCertY");
		boolean no = rs.getBoolean("pg1_psCertN");
		YesNoNullType result = YesNoNullType.Factory.newInstance();
		if(yes) {
			result.setYes(true);
		} else if(no) {
			result.setNo(true);
		} else {
			result.setNull(true);
		}
		return result;
	}

	YesNoNullType getMenCycleRegular(ResultSet rs) throws SQLException {
		boolean yes = rs.getBoolean("pg1_menReg");
		boolean no = rs.getBoolean("pg1_menRegN");
		YesNoNullType result = YesNoNullType.Factory.newInstance();
		if(yes) {
			result.setYes(true);
		} else if(no) {
			result.setNo(true);
		} else {
			result.setNull(true);
		}
		return result;
	}

	void populatePractitionerInformation(PractitionerInformation practitionerInformation, ResultSet rs) throws SQLException {
		getBirthAttendants(practitionerInformation.addNewBirthAttendants(),rs);
		practitionerInformation.setFamilyPhysician(rs.getString("c_famPhys"));
		getNewbornCare(practitionerInformation.addNewNewbornCare(),rs);
	}

	private void getBirthAttendants(BirthAttendants birthAttendants, ResultSet rs) throws SQLException {
		birthAttendants.setOBS(rs.getBoolean("pg1_baObs"));
		birthAttendants.setFP(rs.getBoolean("pg1_baFP"));
		birthAttendants.setMidwife(rs.getBoolean("pg1_baMidwife"));
		birthAttendants.setOther(rs.getString("c_ba"));
	}

	private void getNewbornCare(NewbornCare newbornCare, ResultSet rs) throws SQLException {
		newbornCare.setPed(rs.getBoolean("pg1_ncPed"));
		newbornCare.setFP(rs.getBoolean("pg1_ncFP"));
		newbornCare.setMidwife(rs.getBoolean("pg1_ncMidwife"));
		newbornCare.setOther(rs.getString("c_nc"));
	}

	void populatePartnerInformation(PartnerInformation partnerInformation, ResultSet rs) throws SQLException {
		partnerInformation.setLastName(rs.getString("c_partnerLastName"));
		partnerInformation.setFirstName(rs.getString("c_partnerFirstName"));
		partnerInformation.setOccupation(rs.getString("pg1_partnerOccupation"));
		partnerInformation.setAge(rs.getInt("pg1_partnerAge"));
		partnerInformation.setEducationLevel(rs.getString("pg1_partnerEduLevel"));
	}

	void populatePatientInformation(PatientInformation patientInformation, ResultSet rs) throws SQLException  {
		patientInformation.setLastName(rs.getString("c_lastName"));
		patientInformation.setFirstName(rs.getString("c_firstName"));
		patientInformation.setAddress(rs.getString("c_address"));
		patientInformation.setApt(rs.getString("c_apt"));
		patientInformation.setCity(rs.getString("c_city"));
		patientInformation.setProvince(rs.getString("c_province"));
		patientInformation.setPostalCode(rs.getString("c_postal"));
		patientInformation.setHomePhone(rs.getString("pg1_homePhone"));
		patientInformation.setWorkPhone(rs.getString("pg1_workPhone"));
		patientInformation.setLanguage(rs.getString("pg1_language"));
		Calendar cal = Calendar.getInstance();
		cal.setTime(rs.getDate("pg1_dateOfBirth"));
		patientInformation.setDob(cal);
		patientInformation.setAge(rs.getInt("pg1_age"));
		patientInformation.setOccupation(rs.getString("pg1_occupation"));
		patientInformation.setLevelOfEducation(rs.getString("pg1_eduLevel"));
		patientInformation.setHin(rs.getString("c_hin"));
		patientInformation.setFileNo(rs.getString("c_fileNo"));
		patientInformation.setMaritalStatus(getMaritalStatus(rs));
		patientInformation.setEthnicBackground(rs.getString("pg1_ethnicBg"));
		patientInformation.setAllergies(rs.getString("c_allergies"));
		patientInformation.setMedications(rs.getString("c_meds"));
	}

	private PatientInformation.MaritalStatus.Enum getMaritalStatus(ResultSet rs) throws SQLException {
		boolean s = rs.getBoolean("pg1_msSingle");
		boolean cl = rs.getBoolean("pg1_msCommonLaw");
		boolean m = rs.getBoolean("pg1_msMarried");

		if(s) {
			return PatientInformation.MaritalStatus.S;
		} else if(cl) {
			return PatientInformation.MaritalStatus.CL;
		} else if(m) {
			return PatientInformation.MaritalStatus.M;
		}
		return PatientInformation.MaritalStatus.UNKNOWN;
	}
	public static void main(String... arg) {

	}
}
