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

import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.apache.xmlbeans.XmlOptions;
import org.junit.Test;
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

public class AR2005XMLTest {

	protected SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");

	@Test
	public void generateXMLAndValidate() throws Exception {
		ARRecordSetDocument recordSetD = ARRecordSetDocument.Factory.newInstance();
		ARRecordSet recordSet = recordSetD.addNewARRecordSet();
		ARRecord arRecord = recordSet.addNewARRecord();
		AR1 ar1 = arRecord.addNewAR1();
		populateAr1(ar1);
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
			System.out.println(o);
		}
		recordSetD.save(new FileOutputStream("ar2005.xml"),opts);
	}

	void populateAr1(AR1 ar1)  {
		ar1.setDemographicNo(1);
		ar1.setProviderNo("999998");
		Calendar formCreatedDate = Calendar.getInstance();
		formCreatedDate.setTime(new Date());
		ar1.setFormCreated(formCreatedDate);
		Calendar formEditedDate = Calendar.getInstance();
		formEditedDate.setTime(new Date());
		ar1.setFormEdited(formEditedDate);
		ar1.setEpisodeId(1);
		ar1.setId(1);
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
		ar1.setComments("comments");
		setSignatures(ar1.addNewSignatures());
	}

	void populateInitialLaboratoryInvestigations(InitialLaboratoryInvestigations initialLaboratoryInvestigations)  {
		initialLaboratoryInvestigations.setHbResult("");
		//initialLaboratoryInvestigations.setHivResult(InitialLaboratoryInvestigations.HivResult.NEG);
		initialLaboratoryInvestigations.setHivResult(InitialLaboratoryInvestigations.HivResult.Enum.forString("NEG"));
		
		initialLaboratoryInvestigations.setHivCounsel(false);
		initialLaboratoryInvestigations.setMcvResult(0);
		initialLaboratoryInvestigations.setAboResult(InitialLaboratoryInvestigations.AboResult.A);
		initialLaboratoryInvestigations.setRhResult(InitialLaboratoryInvestigations.RhResult.POS);
		initialLaboratoryInvestigations.setPapResult("LSIL/?HSIL");
		try {
			initialLaboratoryInvestigations.setLastPapDate(this.createDate(dateFormatter.parse("2012/01/19")));
		}catch(ParseException e) {
			MiscUtils.getLogger().warn("error",e);
		}
		initialLaboratoryInvestigations.setAntibodyResult("");
		initialLaboratoryInvestigations.setGcResultGonorrhea(InitialLaboratoryInvestigations.GcResultGonorrhea.NEG);
		initialLaboratoryInvestigations.setGcResultChlamydia(InitialLaboratoryInvestigations.GcResultChlamydia.NEG);
		
		initialLaboratoryInvestigations.setRubellaResult("");
		initialLaboratoryInvestigations.setUrineResult("no sig growth");
		initialLaboratoryInvestigations.setHbsAgResult(InitialLaboratoryInvestigations.HbsAgResult.NDONE);
		initialLaboratoryInvestigations.setVdrlResult(InitialLaboratoryInvestigations.VdrlResult.UNK);
		initialLaboratoryInvestigations.setSickleCellResult(InitialLaboratoryInvestigations.SickleCellResult.NDONE);
		PrenatalGeneticScreeningType prenatalGeneticScreening = initialLaboratoryInvestigations.addNewPrenatalGenericScreening();
		populatePrenatalGeneticScreening(prenatalGeneticScreening);
	}

	void populatePrenatalGeneticScreening(PrenatalGeneticScreeningType prenatalGeneticScreening)  {
		prenatalGeneticScreening.setMSSIPSFTS("");
		prenatalGeneticScreening.setEDBCVS("");
		prenatalGeneticScreening.setMSAFP("");
		prenatalGeneticScreening.setDeclined(true);
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

	void populateCurrentPregnancy(CurrentPregnancyType currentPregnancy)  {
		currentPregnancy.setBleeding(getYesNoNullType(Boolean.FALSE));
		currentPregnancy.setNausea(getYesNoNullType(Boolean.FALSE));
		currentPregnancy.setSmoking(getYesNoNullType(Boolean.TRUE));
		currentPregnancy.setCigsPerDay(CurrentPregnancyType.CigsPerDay.OVER_20);
		currentPregnancy.setAlcoholDrugs(getYesNoNullType(Boolean.TRUE));
		currentPregnancy.setOccEnvRisks(getYesNoNullType(null));
		currentPregnancy.setDietaryRes(getYesNoNullType(Boolean.FALSE));
		currentPregnancy.setCalciumAdequate(getYesNoNullType(Boolean.TRUE));
		currentPregnancy.setFolate(getYesNoNullType(Boolean.TRUE));
	}

	void populateMedicalHistory(MedicalHistoryType medicalHistory)  {
		medicalHistory.setHypertension(getYesNoNullType(Boolean.FALSE));
		medicalHistory.setEndorince(getYesNoNullType(Boolean.FALSE));
		medicalHistory.setUrinaryTract(getYesNoNullType(Boolean.TRUE));
		medicalHistory.setCardiac(getYesNoNullType(Boolean.FALSE));
		medicalHistory.setLiver(getYesNoNullType(Boolean.FALSE));
		medicalHistory.setGynaecology(getYesNoNullType(Boolean.FALSE));
		medicalHistory.setHem(getYesNoNullType(Boolean.FALSE));
		medicalHistory.setSurgeries(getYesNoNullType(Boolean.TRUE));
		medicalHistory.setBloodTransfusion(getYesNoNullType(Boolean.FALSE));
		medicalHistory.setAnesthetics(getYesNoNullType(Boolean.FALSE));
		medicalHistory.setPsychiatry(getYesNoNullType(Boolean.TRUE));
		medicalHistory.setEpilepsy(getYesNoNullType(Boolean.FALSE));
		medicalHistory.setOther(getYesNoNullType(null));
		medicalHistory.setOtherDescr("");
	}

	void populateGenericHistory(GenericHistoryType geneticHistory)  {
		geneticHistory.setAtRisk(getYesNoNullType(Boolean.FALSE));
		geneticHistory.setDevelopmentalDelay(getYesNoNullType(Boolean.FALSE));
		geneticHistory.setCongenitalAnomolies(getYesNoNullType(Boolean.FALSE));
		geneticHistory.setChromosomalDisorders(getYesNoNullType(Boolean.FALSE));
		geneticHistory.setGeneticDisorders(getYesNoNullType(Boolean.FALSE));
	}

	void populateInfectiousDisease(InfectiousDiseaseType infectiousDiseases)  {
		infectiousDiseases.setVaricella(getYesNoNullType(Boolean.FALSE));
		infectiousDiseases.setStd(getYesNoNullType(Boolean.FALSE));
		infectiousDiseases.setTuberculosis(getYesNoNullType(Boolean.FALSE));
		infectiousDiseases.setOther(getYesNoNullType(null));
		infectiousDiseases.setOtherDescr("");
	}

	void populatePsychosocial(PsychosocialType psychosocial)  {
		psychosocial.setPoortSocialSupport(getYesNoNullType(Boolean.FALSE));
		psychosocial.setRelationshipProblems(getYesNoNullType(Boolean.FALSE));
		psychosocial.setEmotionalDepression(getYesNoNullType(Boolean.FALSE));
		psychosocial.setSubstanceAbuse(getYesNoNullType(Boolean.TRUE));
		psychosocial.setFamilyViolence(getYesNoNullType(Boolean.FALSE));
		psychosocial.setParentingConcerns(getYesNoNullType(Boolean.FALSE));
		psychosocial.setReligiousCultural(getYesNoNullType(null));
	}

	void populateFamilyHistory(FamilyHistoryType familyHistory)  {
		familyHistory.setAtRisk(getYesNoNullType(Boolean.FALSE));
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

	void populatePhysicalExamination(PhysicalExaminationType physicalExamination)  {
		physicalExamination.setHeight(156.5F);
		physicalExamination.setWeight(47.6F);
		physicalExamination.setBmi(19.4F);
		physicalExamination.setBp("110/80");

		physicalExamination.setThyroid(getNormalAbnormalNullType(Boolean.TRUE));
		physicalExamination.setChest(getNormalAbnormalNullType(Boolean.TRUE));
		physicalExamination.setBreasts(getNormalAbnormalNullType(Boolean.FALSE));
		physicalExamination.setCardiovascular(getNormalAbnormalNullType(Boolean.TRUE));
		physicalExamination.setAbdomen(getNormalAbnormalNullType(Boolean.TRUE));
		physicalExamination.setVaricosities(getNormalAbnormalNullType(Boolean.TRUE));
		physicalExamination.setExernalGenitals(getNormalAbnormalNullType(Boolean.TRUE));
		physicalExamination.setCervixVagina(getNormalAbnormalNullType(Boolean.TRUE));
		physicalExamination.setUterus(getNormalAbnormalNullType(Boolean.TRUE));
		physicalExamination.setUterusSize("23");
		physicalExamination.setAdnexa(getNormalAbnormalNullType(null));
		physicalExamination.setOther(getNormalAbnormalNullType(null));
		physicalExamination.setOtherDescr("");
	}



	void populateObstetricalHistory(ObstetricalHistory obstetricalHistory)  {
		ObstetricalHistoryItemList item1 = obstetricalHistory.addNewObsList();
		item1.setYear(2006);
		item1.setSex(ObstetricalHistoryItemList.Sex.M);
		item1.setGestAge(12);
		item1.setBirthWeight("");
		item1.setLengthOfLabour(0);
		item1.setPlaceOfBirth("Henderson");
		item1.setTypeOfDelivery(ObstetricalHistoryItemList.TypeOfDelivery.SVAG);
		item1.setComments("TA");

		item1 = obstetricalHistory.addNewObsList();
		item1.setYear(2007);
		item1.setSex(ObstetricalHistoryItemList.Sex.M);
		item1.setGestAge(37);
		item1.setBirthWeight("7lb");
		item1.setLengthOfLabour(15);
		item1.setPlaceOfBirth("SJH");
		item1.setTypeOfDelivery(ObstetricalHistoryItemList.TypeOfDelivery.SVAG);
		item1.setComments("no complications, \"Tristan\"");


	}

	Calendar createDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	void populatePregnancyHistory(PregnancyHistory pregnancyHistory)  {

		//TODO:null?
		pregnancyHistory.setLMP(null);
		pregnancyHistory.setLMPCertain(this.getYesNoNullType(Boolean.FALSE));
		pregnancyHistory.setMenCycle("30");
		pregnancyHistory.setMenCycleRegular(getYesNoNullType(null));
		pregnancyHistory.setContraceptiveType("nothing");
		pregnancyHistory.setContraceptiveLastUsed(createDate(oscar.MyDateFormat.getSysDate("2012-05-15")));
		pregnancyHistory.setMenstrualEDB(createDate(oscar.MyDateFormat.getSysDate("2012-05-03")));
		pregnancyHistory.setFinalEDB(createDate(oscar.MyDateFormat.getSysDate("2012-05-15")));
		DatingMethods datingMethods = pregnancyHistory.addNewDatingMethods();
		datingMethods.setDates(false);
		datingMethods.setT1US(false);
		datingMethods.setT2US(true);
		datingMethods.setArt(false);
		pregnancyHistory.setGravida(6);
		pregnancyHistory.setTerm(1);
		pregnancyHistory.setPremature(1);
		pregnancyHistory.setAbortuses(3);
		pregnancyHistory.setLiving(2);
	}


	void populatePractitionerInformation(PractitionerInformation practitionerInformation)  {
		getBirthAttendants(practitionerInformation.addNewBirthAttendants());
		practitionerInformation.setFamilyPhysician("Family Doc");
		getNewbornCare(practitionerInformation.addNewNewbornCare());
	}

	protected void getBirthAttendants(BirthAttendants birthAttendants)  {
		birthAttendants.setOBS(false);
		birthAttendants.setFP(false);
		birthAttendants.setMidwife(false);
		birthAttendants.setOther("maternity centre");
	}

	protected void getNewbornCare(NewbornCare newbornCare)  {
		newbornCare.setPed(false);
		newbornCare.setFP(false);
		newbornCare.setMidwife(false);
		newbornCare.setOther("maternity centre");
	}

	void populatePartnerInformation(PartnerInformation partnerInformation)  {
		partnerInformation.setLastName("Smith");
		partnerInformation.setFirstName("John");
		PartnerInformation.Occupation occ = partnerInformation.addNewOccupation();
		occ.setValue(PartnerInformation.Occupation.Value.OTHER);
		occ.setOther("programmer");
		partnerInformation.setOccupation(occ);
		partnerInformation.setAge(35);
		partnerInformation.setEducationLevel(PartnerInformation.EducationLevel.ED_001);
	}

	void populatePatientInformation(PatientInformation patientInformation)   {
		patientInformation.setLastName("Smith");
		patientInformation.setFirstName("Jane");
		patientInformation.setAddress("42 Main St.");
		patientInformation.setApt("4C");
		patientInformation.setCity("Toronto");
		patientInformation.setProvince(PatientInformation.Province.CA_ON);
		patientInformation.setPostalCode("M5W4A3");
		patientInformation.setHomePhone("555-555-5555");
		patientInformation.setWorkPhone("666-666-6666");
		patientInformation.setLanguage(PatientInformation.Language.ENG);
		Calendar cal = Calendar.getInstance();
		cal.setTime(oscar.MyDateFormat.getSysDate("1978-04-26"));
		patientInformation.setDob(cal);
		patientInformation.setAge(34);
		PatientInformation.Occupation occ = patientInformation.addNewOccupation();
		occ.setValue(PatientInformation.Occupation.Value.OCC_0005);
		occ.setOther("");
		patientInformation.setOccupation(occ);
		patientInformation.setLevelOfEducation(PatientInformation.LevelOfEducation.ED_001);
		PatientInformation.Hin hin = patientInformation.addNewHin();
		hin.setStringValue("2222222222");
		hin.setType(PatientInformation.Hin.Type.OHIP);		
		patientInformation.setFileNo("");
		patientInformation.setMaritalStatus(PatientInformation.MaritalStatus.MS_005);
		PatientInformation.EthnicBackground eb = patientInformation.addNewEthnicBackground();
		PatientInformation.EthnicBackground.Value v1 = eb.addNewValue();
		v1.setParent(EthnicBackground.Value.Parent.MATERNAL);
		v1.setStringValue("ANC001");
		PatientInformation.EthnicBackground.Value v2 = eb.addNewValue();
		v2.setParent(EthnicBackground.Value.Parent.PATERNAL);
		v2.setStringValue("ANC001");
		patientInformation.setEthnicBackground(eb);
		patientInformation.setAllergies("allergies");
		PatientInformation.Medications meds = patientInformation.addNewMedications();
		meds.setValueArray(new PatientInformation.Medications.Value.Enum[]{PatientInformation.Medications.Value.Enum.forInt(PatientInformation.Medications.Value.INT_MEP_001)});
		patientInformation.setMedications(meds);
	}


	/* ar2 */

	void populateAr2(AR2 ar2) {
		RiskFactorItemType riskFactorItem = ar2.addNewRiskFactorList();
		riskFactorItem.setRiskFactor("");
		riskFactorItem.setPlanOfManagement("");


		RecommendedImmunoprophylaxisType recommendedImmunoprophylaxis = ar2.addNewRecommendedImmunoprophylaxis();
		populateRecommendedImmunoprophylaxis(recommendedImmunoprophylaxis);

		SubsequentVisitItemType visit = ar2.addNewSubsequentVisitList();
		visit.setDate(createDate(oscar.MyDateFormat.getSysDate("2011-12-21")));
		visit.setGa("19w+1");
		visit.setWeight(52);
		visit.setBp("110/70");
		visit.setUrinePR("n");
		visit.setUrineGI("n");
		visit.setSFH("20");
		visit.setPresentationPosition("-");
		visit.setFHRFm("140");
		visit.setComments("1st visit");

		UltrasoundType ultrasound = ar2.addNewUltrasound();
		ultrasound.setDate(createDate(oscar.MyDateFormat.getSysDate("2012-01-10")));
		ultrasound.setGa("22w+0");
		ultrasound.setResults("12 d difference, plac.post/clear,normal anatomy");

		AdditionalLabInvestigationsType additionalLabInvestigations = ar2.addNewAdditionalLabInvestigations();
		populateAdditionalLabInvestigations(additionalLabInvestigations);

		DiscussionTopicsType discussionTopics = ar2.addNewDiscussionTopics();
		popuplateDiscussionTopics(discussionTopics);

		setSignatures(ar2.addNewSignatures());
	}

	void populateRecommendedImmunoprophylaxis(RecommendedImmunoprophylaxisType recommendedImmunoprophylaxis) {
		recommendedImmunoprophylaxis.setRhNegative(Boolean.FALSE);
		recommendedImmunoprophylaxis.setRhIgGiven(this.createDate(new Date()));
		recommendedImmunoprophylaxis.setRubella(Boolean.FALSE);
		recommendedImmunoprophylaxis.setNewbornHepIG(Boolean.FALSE);
		recommendedImmunoprophylaxis.setHepBVaccine(Boolean.FALSE);
	}

	void populateAdditionalLabInvestigations(AdditionalLabInvestigationsType additionalLabInvestigations) {
		additionalLabInvestigations.setHb("");
		additionalLabInvestigations.setBloodGroup(AdditionalLabInvestigationsType.BloodGroup.A);
		additionalLabInvestigations.setRh(AdditionalLabInvestigationsType.Rh.NEG);
		additionalLabInvestigations.setRepeatABS("");
		additionalLabInvestigations.setGCT("");
		additionalLabInvestigations.setGTT("");
		additionalLabInvestigations.setGBS(AdditionalLabInvestigationsType.GBS.UNK);

	}

	void setSignatures(SignatureType signatures) {
		signatures.setSignature("Marc Dumontier");
		signatures.setDate(createDate(new Date()));

		signatures.setSignature2("Blah");
		signatures.setDate2(createDate(new Date()));

	}

	void popuplateDiscussionTopics(DiscussionTopicsType discussionTopics) {
		discussionTopics.setExercise(Boolean.FALSE);
		discussionTopics.setWorkPlan(Boolean.FALSE);
		discussionTopics.setIntercourse(Boolean.FALSE);
		discussionTopics.setTravel(Boolean.FALSE);
		discussionTopics.setPrenatalClasses(Boolean.FALSE);
		discussionTopics.setBirthPlan(Boolean.FALSE);
		discussionTopics.setOnCallProviders(Boolean.FALSE);
		discussionTopics.setPretermLabour(Boolean.FALSE);
		discussionTopics.setPROM(Boolean.FALSE);
		discussionTopics.setAPH(Boolean.FALSE);
		discussionTopics.setFetalMovement(Boolean.FALSE);
		discussionTopics.setAdmissionTiming(Boolean.FALSE);
		discussionTopics.setPainManagement(Boolean.FALSE);
		discussionTopics.setLabourSupport(Boolean.FALSE);
		discussionTopics.setBreastFeeding(Boolean.FALSE);
		discussionTopics.setCircumcision(Boolean.FALSE);
		discussionTopics.setDischargePlanning(Boolean.FALSE);
		discussionTopics.setDepression(Boolean.FALSE);
		discussionTopics.setContraception(Boolean.FALSE);
		discussionTopics.setCarSeatSafety(Boolean.FALSE);
		discussionTopics.setPostpartumCare(Boolean.FALSE);
	}
}
