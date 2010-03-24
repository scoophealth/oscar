package org.oscarehr.web;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.XmlOptions;
import org.oscarehr.PMmodule.dao.AdmissionDao;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.common.dao.FacilityDao;
import org.oscarehr.common.dao.OcanClientFormDao;
import org.oscarehr.common.dao.OcanClientFormDataDao;
import org.oscarehr.common.dao.OcanStaffFormDao;
import org.oscarehr.common.dao.OcanStaffFormDataDao;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.OcanClientForm;
import org.oscarehr.common.model.OcanClientFormData;
import org.oscarehr.common.model.OcanStaffForm;
import org.oscarehr.common.model.OcanStaffFormData;
import org.oscarehr.ocan.OCANSubmissionFileDocument;
import org.oscarehr.ocan.ActionDocument.Action;
import org.oscarehr.ocan.ActionListDocument.ActionList;
import org.oscarehr.ocan.AddictionTypeListDocument.AddictionTypeList;
import org.oscarehr.ocan.AdditionalElementsDocument.AdditionalElements;
import org.oscarehr.ocan.ClientAddressDocument.ClientAddress;
import org.oscarehr.ocan.ClientCapacityDocument.ClientCapacity;
import org.oscarehr.ocan.ClientContactDocument.ClientContact;
import org.oscarehr.ocan.ClientIDDocument.ClientID;
import org.oscarehr.ocan.ClientNameDocument.ClientName;
import org.oscarehr.ocan.ClientOHIPDocument.ClientOHIP;
import org.oscarehr.ocan.ClientRecordDocument.ClientRecord;
import org.oscarehr.ocan.ConcernAreaListDocument.ConcernAreaList;
import org.oscarehr.ocan.DiagnosticListDocument.DiagnosticList;
import org.oscarehr.ocan.DiscrimExpListDocument.DiscrimExpList;
import org.oscarehr.ocan.DoctorContactDocument.DoctorContact;
import org.oscarehr.ocan.DomainDocument.Domain;
import org.oscarehr.ocan.DrinkAlcoholDocument.DrinkAlcohol;
import org.oscarehr.ocan.DrugUseDocument.DrugUse;
import org.oscarehr.ocan.DrugUseListDocument.DrugUseList;
import org.oscarehr.ocan.ExitDispositionDocument.ExitDisposition;
import org.oscarehr.ocan.FormalHelpNeedDocument.FormalHelpNeed;
import org.oscarehr.ocan.FormalHelpRecvdDocument.FormalHelpRecvd;
import org.oscarehr.ocan.ImmigExpListDocument.ImmigExpList;
import org.oscarehr.ocan.InformalHelpRecvdDocument.InformalHelpRecvd;
import org.oscarehr.ocan.LegalStatusDocument.LegalStatus;
import org.oscarehr.ocan.LegalStatusListDocument.LegalStatusList;
import org.oscarehr.ocan.MISFunctionDocument.MISFunction;
import org.oscarehr.ocan.MedicalConditionListDocument.MedicalConditionList;
import org.oscarehr.ocan.MedicationDetailDocument.MedicationDetail;
import org.oscarehr.ocan.MedicationListDocument.MedicationList;
import org.oscarehr.ocan.NeedRatingDocument.NeedRating;
import org.oscarehr.ocan.OCANDomainsDocument.OCANDomains;
import org.oscarehr.ocan.OCANSubmissionFileDocument.OCANSubmissionFile;
import org.oscarehr.ocan.OCANSubmissionRecordDocument.OCANSubmissionRecord;
import org.oscarehr.ocan.OrganizationRecordDocument.OrganizationRecord;
import org.oscarehr.ocan.OtherAgencyContactDocument.OtherAgencyContact;
import org.oscarehr.ocan.OtherIllnessListDocument.OtherIllnessList;
import org.oscarehr.ocan.OtherPractitionerContactDocument.OtherPractitionerContact;
import org.oscarehr.ocan.PresentingIssueDocument.PresentingIssue;
import org.oscarehr.ocan.PresentingIssueListDocument.PresentingIssueList;
import org.oscarehr.ocan.ProgramDocument.Program;
import org.oscarehr.ocan.PsychiatristContactDocument.PsychiatristContact;
import org.oscarehr.ocan.ReasonForAssessmentDocument.ReasonForAssessment;
import org.oscarehr.ocan.ReferralDocument.Referral;
import org.oscarehr.ocan.ReferralListDocument.ReferralList;
import org.oscarehr.ocan.RiskUnemploymentListDocument.RiskUnemploymentList;
import org.oscarehr.ocan.SafetyToSelfRiskListDocument.SafetyToSelfRiskList;
import org.oscarehr.ocan.ServiceDeliveryLHINDocument.ServiceDeliveryLHIN;
import org.oscarehr.ocan.ServiceOrgDocument.ServiceOrg;
import org.oscarehr.ocan.ServiceRecipientLHINDocument.ServiceRecipientLHIN;
import org.oscarehr.ocan.ServiceRecipientLocationDocument.ServiceRecipientLocation;
import org.oscarehr.ocan.SideEffectsDetailListDocument.SideEffectsDetailList;
import org.oscarehr.ocan.SymptomListDocument.SymptomList;
import org.oscarehr.ocan.TimeLivedInCanadaDocument.TimeLivedInCanada;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

public class OcanReportUIBean {

	static Log logger = LogFactory.getLog(OcanReportUIBean.class);

	private static OcanStaffFormDao ocanStaffFormDao = (OcanStaffFormDao) SpringUtils.getBean("ocanStaffFormDao");
	private static OcanStaffFormDataDao ocanStaffFormDataDao = (OcanStaffFormDataDao) SpringUtils.getBean("ocanStaffFormDataDao");	
	private static OcanClientFormDao ocanClientFormDao = (OcanClientFormDao) SpringUtils.getBean("ocanClientFormDao");
	private static OcanClientFormDataDao ocanClientFormDataDao = (OcanClientFormDataDao) SpringUtils.getBean("ocanClientFormDataDao");
	private static FacilityDao facilityDao = (FacilityDao)SpringUtils.getBean("facilityDao");
	private static AdmissionDao admissionDao = (AdmissionDao)SpringUtils.getBean("admissionDao");
	
	public static void writeXmlExportData(int year, int month, int increment, OutputStream out) {
		
		//get all submitted/completed forms where the completion date is in the year/month specified
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		
		List<OcanStaffForm> ocanStaffForms = ocanStaffFormDao.findLatestSignedOcanForms(loggedInInfo.currentFacility.getId(), "1.2", getStartDate(year,month), getEndDate(year,month));
		logger.info("# of staff forms found for time period = " + ocanStaffForms.size());
		
		OCANSubmissionFileDocument submissionFileDoc = OCANSubmissionFileDocument.Factory.newInstance();		
		OCANSubmissionFile submissionFile = OCANSubmissionFile.Factory.newInstance();
		submissionFile.setVersion(OCANSubmissionFile.Version.X_1_0_0);
		submissionFile.setID("File-" + ( (increment<10)?("0"+increment):(increment) ));
		submissionFile.setTimestamp(convertToOcanXmlDateTime(new Date()));
		List<OCANSubmissionRecord> submissionRecordList = new ArrayList<OCANSubmissionRecord>();
		for(OcanStaffForm staffForm:ocanStaffForms) {
			//check for a clientform
			OcanClientForm clientForm = ocanClientFormDao.findLatestSignedOcanForm(loggedInInfo.currentFacility.getId(),staffForm.getClientId(), "1.2", getStartDate(year,month), getEndDate(year,month));
			List<OcanClientFormData> clientFormData = null;
			if(clientForm != null) {
				clientFormData = ocanClientFormDataDao.findByForm(clientForm.getId());
			}			
			OCANSubmissionRecord submissionRecord = convertOcanForm(staffForm,ocanStaffFormDataDao.findByForm(staffForm.getId()),clientForm,clientFormData);
			submissionRecordList.add(submissionRecord);			
		}
		submissionFile.setOCANSubmissionRecordArray(submissionRecordList.toArray(new OCANSubmissionRecord[submissionRecordList.size()]));
		submissionFileDoc.setOCANSubmissionFile(submissionFile);
	/*	
		for(OcanStaffForm ocanStaffForm:ocanStaffForms) {
			List<OcanStaffFormData> formData = ocanStaffFormDataDao.findByForm(ocanStaffForm.getId());
			//convertOcanStaffForm(ocanStaffForm);
			try {
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("staff_form-"+ocanStaffForm.getId() + ".dat"));
				oos.writeObject(ocanStaffForm);
				oos.flush();
				oos.close();
				
				oos = new ObjectOutputStream(new FileOutputStream("staff_form_data-"+ocanStaffForm.getId() + ".dat"));
				oos.writeObject(formData);
				oos.flush();
				oos.close();
								
				
				OcanClientForm clientForm = ocanClientFormDao.findLatestSignedOcanForm(loggedInInfo.currentFacility.getId(),ocanStaffForm.getClientId(), "1.2", getStartDate(year,month), getEndDate(year,month));
				List<OcanClientFormData> clientFormData = null;
				if(clientForm != null) {
					clientFormData = ocanClientFormDataDao.findByForm(clientForm.getId());
					
					oos = new ObjectOutputStream(new FileOutputStream("client_form-"+clientForm.getId() + ".dat"));
					oos.writeObject(clientForm);
					oos.flush();
					oos.close();
					
					oos = new ObjectOutputStream(new FileOutputStream("client_form_data-"+clientForm.getId() + ".dat"));
					oos.writeObject(clientFormData);
					oos.flush();
					oos.close();
					
				}			
				
			}catch(IOException e) {e.printStackTrace();}
		}
		*/
		try {
			XmlOptions options = new XmlOptions();
			options.setUseDefaultNamespace();
			submissionFileDoc.save(out,options);
		}catch(IOException e) {
			logger.error(e);
		}
		
		
	}
	
	public static OCANSubmissionRecord convertOcanForm(OcanStaffForm ocanStaffForm, List<OcanStaffFormData> ocanStaffFormData, OcanClientForm ocanClientForm, List<OcanClientFormData> ocanClientFormData) {
		OCANSubmissionRecord ocanSubmissionRecord = OCANSubmissionRecord.Factory.newInstance();
		ocanSubmissionRecord.setAssessmentStatus(OCANSubmissionRecord.AssessmentStatus.COMPLETE);
		ocanSubmissionRecord.setAssessmentID(String.valueOf(ocanStaffForm.getId()));
		ocanSubmissionRecord.setStartDate(convertToOcanXmlDate(ocanStaffForm.getStartDate()));
		ocanSubmissionRecord.setCompletionDate(convertToOcanXmlDate(ocanStaffForm.getCompletionDate()));
		
		ocanSubmissionRecord.setOrganizationRecord(convertOrganizationRecord(ocanStaffForm,ocanStaffFormData));
		ocanSubmissionRecord.setClientRecord(convertClientRecord(ocanStaffForm,ocanStaffFormData));
		ocanSubmissionRecord.setOCANDomains(convertOCANDomains(ocanStaffForm,ocanStaffFormData, ocanClientForm, ocanClientFormData));
		ocanSubmissionRecord.setAdditionalElements(convertAdditionalElements(ocanStaffForm,ocanStaffFormData));
		return ocanSubmissionRecord;
	}
	
	public static AdditionalElements convertAdditionalElements(OcanStaffForm ocanStaffForm, List<OcanStaffFormData> ocanStaffFormData) {
		AdditionalElements additionalElements = AdditionalElements.Factory.newInstance();
		additionalElements.setClientHopesForFuture("");
		additionalElements.setClientNeedToGetThere("");
		additionalElements.setClientViewMentalHealth("");
		additionalElements.setClientSpiritualityImportance("");
		additionalElements.setClientCultureHeritageImportance("");

		PresentingIssueList presentingIssueList = getPresentingIssueList(ocanStaffFormData);
		additionalElements.setPresentingIssueList(presentingIssueList);
		
		ActionList actionList = ActionList.Factory.newInstance();
		List<Action> actions = new ArrayList<Action>();
		
		String strActionCount = getStaffAnswer("summary_of_actions_count",ocanStaffFormData);
		if(strActionCount.equals("")) {strActionCount="0";}
		int actionCount = Integer.valueOf(strActionCount);
		for(int x=0;x<actionCount;x++) {
			int index = x+1;
			Action action = Action.Factory.newInstance();
			action.setDomain(getDomainName(Integer.valueOf(getStaffAnswer(index+"_summary_of_actions_domain",ocanStaffFormData))));
			action.setPriority(String.valueOf(index));
			actions.add(action);
		}
		actionList.setActionArray(actions.toArray(new Action[actions.size()]));
		additionalElements.setActionList(actionList);
		
		
		ReferralList referralList = ReferralList.Factory.newInstance();
		List<Referral> referrals = new ArrayList<Referral>();
		
		String strReferralCount = getStaffAnswer("referrals_count",ocanStaffFormData);
		int referralCount = 0;
		try {referralCount = Integer.valueOf(strReferralCount);}catch(NumberFormatException e){}
		
		for(int x=0;x<referralCount;x++) {
			int index = x+1;
			Referral referral = Referral.Factory.newInstance();
			referral.setOptimal(getStaffAnswer(index+"_summary_of_referral_optimal",ocanStaffFormData));
			referral.setSpecifyOptimal(getStaffAnswer(index+"_summary_of_referral_optimal_spec",ocanStaffFormData));
			referral.setActual(getStaffAnswer(index+"_summary_of_referral_actual",ocanStaffFormData));
			referral.setDifferenceReason(getStaffAnswer(index+"_summary_of_referral_diff",ocanStaffFormData));
			referral.setStatus(getStaffAnswer(index+"_summary_of_referral_status",ocanStaffFormData));
			referrals.add(referral);
		}
		referralList.setReferralArray(referrals.toArray(new Referral[referrals.size()]));
		additionalElements.setReferralList(referralList);
		
		
		return additionalElements;
	}
	
	public static PresentingIssueList getPresentingIssueList(List<OcanStaffFormData> ocanStaffFormData) {
		PresentingIssueList presentingIssueList = PresentingIssueList.Factory.newInstance();
		List<String> answers = getMultipleStaffAnswer("presenting_issues",ocanStaffFormData);
		List<PresentingIssue> piList = new ArrayList<PresentingIssue>();
		for(String answer:answers) {
			PresentingIssue pi = PresentingIssue.Factory.newInstance();
			pi.setType(answer);
			piList.add(pi);
		}
		presentingIssueList.setPresentingIssueArray(piList.toArray(new PresentingIssue[piList.size()]));		
		return presentingIssueList;
	}
	
	public static OCANDomains convertOCANDomains(OcanStaffForm ocanStaffForm, List<OcanStaffFormData> ocanStaffFormData, OcanClientForm ocanClientForm, List<OcanClientFormData> ocanClientFormData) {
		OCANDomains ocanDomains = OCANDomains.Factory.newInstance();
		
		List<Domain> domainList = new ArrayList<Domain>();
		
		for(int x=0;x<24;x++) {
			domainList.add(convertOCANDomain(x+1,ocanStaffForm,ocanStaffFormData,ocanClientForm, ocanClientFormData));
			ocanDomains.setDomainArray((Domain[])domainList.toArray(new Domain[domainList.size()]));
		}
		
		return ocanDomains;
	}

	public static Domain convertOCANDomain(int domainNumber,OcanStaffForm ocanStaffForm, List<OcanStaffFormData> ocanStaffFormData, OcanClientForm ocanClientForm, List<OcanClientFormData> ocanClientFormData) {
		Domain domain = Domain.Factory.newInstance();
		domain.setName(getDomainName(domainNumber));
		
		NeedRating needRating = convertNeedRating(domainNumber,ocanStaffForm,ocanStaffFormData, ocanClientForm, ocanClientFormData);
		domain.setNeedRating(needRating);
		if(needRating.getStaff() != 0 && needRating.getStaff() != 9) {
			//2,3a,3b
			domain.setInformalHelpRecvd(convertInformalHelpRecvd(domainNumber,ocanStaffForm,ocanStaffFormData));
			domain.setFormalHelpRecvd(convertFormalHelpRecvd(domainNumber,ocanStaffForm,ocanStaffFormData));
			domain.setFormalHelpNeed(convertFormalHelpNeed(domainNumber,ocanStaffForm,ocanStaffFormData));			
		}
		
		switch(domainNumber) {
		
		case 1:
			domain.setResidenceType(getStaffAnswer("1_where_live",ocanStaffFormData));
			domain.setResidenceSupport(getStaffAnswer("1_any_support",ocanStaffFormData));
			domain.setLivingArrangementType(getStaffAnswer("1_live_with_anyone",ocanStaffFormData));			
			break;
		
		case 5:
			domain.setEmployStatus(getStaffAnswer("5_current_employment_status",ocanStaffFormData));
			domain.setEducationProgramStatus(getStaffAnswer("5_education_program_status",ocanStaffFormData));
			RiskUnemploymentList riskUnemploymentList = getRiskUnemploymentList(ocanStaffFormData);
			if(riskUnemploymentList.getRiskUnemploymentList().size()>0) {
				domain.setRiskUnemploymentList(riskUnemploymentList);
			}			
			break;
		
		case 6:
			MedicalConditionList medicalConditionList = getMedicalConditionList(ocanStaffFormData);
			domain.setMedicalConditionList(medicalConditionList);				

			domain.setPhysicalHealthConcern(getStaffAnswer("6_physical_health_concerns",ocanStaffFormData));
			
			ConcernAreaList concernAreaList = getConcernAreaList(ocanStaffFormData);
			if(concernAreaList.getConcernAreaList().size()>0) {
				domain.setConcernAreaList(concernAreaList);
			}
			
			MedicationList medicationList = getMedicationList(ocanStaffFormData);
			if(medicationList.getMedicationDetailList().size()>0) {
				domain.setMedicationList(medicationList);
			}
			domain.setSideEffects(getStaffAnswer("se_reported",ocanStaffFormData));
			domain.setDailyLivingAffected(getStaffAnswer("se_affects",ocanStaffFormData));
			
			SideEffectsDetailList sideEffectsDetailList = getSideEffectsDetailList(ocanStaffFormData);
			if(sideEffectsDetailList.getSideEffectsDetailList().size()>0) {
				domain.setSideEffectsDetailList(sideEffectsDetailList);
			}			
			break;
			
		case 7:
			domain.setHospitalizedPastTwoYears(getStaffAnswer("hospitalized_mental_illness",ocanStaffFormData));
			String totalAdmissions = getStaffAnswer("hospitalized_mental_illness_admissions",ocanStaffFormData);
			if(totalAdmissions!=null&&totalAdmissions.length()>0) {
				domain.setTotalAdmissions(new BigInteger(totalAdmissions));
			}
			String totalHospitalDays = getStaffAnswer("hospitalized_mental_illness_days",ocanStaffFormData);
			if(totalHospitalDays!=null&&totalHospitalDays.length()>0) {
				domain.setTotalHospitalDays(new BigInteger(totalHospitalDays));
			}
			domain.setCommunityTreatOrder(getStaffAnswer("community_treatment_orders",ocanStaffFormData));
		
			SymptomList symptomList = getSymptomList(ocanStaffFormData);
			if(symptomList.getSymptomList().size()>0) {
				domain.setSymptomList(symptomList);
			}			
			break;
		
		case 8:
			DiagnosticList diagnosticList = getDiagnosticList(ocanStaffFormData);
			if(diagnosticList.getDiagnosticList().size()>0) {
				domain.setDiagnosticList(diagnosticList);
			}	
			
			OtherIllnessList otherIllnessList = getOtherIllnessList(ocanStaffFormData);
			if(otherIllnessList.getOtherIllnessList().size()>0) {
				domain.setOtherIllnessList(otherIllnessList);
			}
			
			break;
			
		case 10:
			
			domain.setSuicideAttempt(getStaffAnswer("suicide_past",ocanStaffFormData));
			domain.setSuicideThoughts(getStaffAnswer("suicidal_thoughts",ocanStaffFormData));
			domain.setSafetyConcernSelf(getStaffAnswer("safety_concerns",ocanStaffFormData));

			SafetyToSelfRiskList safetyToSelfRiskList = getSafetyToSelfRiskList(ocanStaffFormData);
			if(safetyToSelfRiskList.getSafetyToSelfRiskList().size()>0) {
				domain.setSafetyToSelfRiskList(safetyToSelfRiskList);
			}
			break;
			
		case 12:			
			domain.setDrinkAlcohol(getDrinkAlcohol(ocanStaffFormData));
			domain.setStageOfChangeAlcohol(getStaffAnswer("state_of_change_alcohol",ocanStaffFormData));
			break;
		
		case 13:
			DrugUseList drugUseList = getDrugUseList(ocanStaffFormData);
			domain.setDrugUseList(drugUseList);
			domain.setStageOfChangeDrugs(getStaffAnswer("state_of_change_addiction",ocanStaffFormData));
			
			break;
			
		case 14:
			
			AddictionTypeList addictionTypeList = getAddictionTypeList(ocanStaffFormData);
			if(addictionTypeList.getAddictionTypeList().size()>0) {
				domain.setAddictionTypeList(addictionTypeList);
			}
			domain.setStageOfChangeAddictions(getStaffAnswer("14_state_of_change",ocanStaffFormData));
			
			break;
			
		case 15:
			domain.setChangedSocialPatterns(getStaffAnswer("social_patterns",ocanStaffFormData));
			break;
			
		case 20:
			domain.setHighestEducationLevel(getStaffAnswer("level_of_education",ocanStaffFormData));
			break;
			
		case 23:
			domain.setSourceOfIncome(getStaffAnswer("income_source_type",ocanStaffFormData));
			break;
		}
		
		return domain;
	}
	
	
	public static NeedRating convertNeedRating(int domainNumber,OcanStaffForm ocanStaffForm, List<OcanStaffFormData> ocanStaffFormData, OcanClientForm ocanClientForm, List<OcanClientFormData> ocanClientFormData) {
		NeedRating needRating = NeedRating.Factory.newInstance();
		String staffAnswer = getStaffAnswer(domainNumber+"_1",ocanStaffFormData);
		needRating.setStaff(Byte.valueOf(staffAnswer));
		if(ocanClientForm != null) {
			String clientAnswer = getClientAnswer(domainNumber+"_1",ocanClientFormData);
			if(clientAnswer.length()>0)
				needRating.setClient(Byte.valueOf(clientAnswer));
		}
		return needRating;
	}

	public static InformalHelpRecvd convertInformalHelpRecvd(int domainNumber,OcanStaffForm ocanStaffForm, List<OcanStaffFormData> ocanStaffFormData) {
		InformalHelpRecvd informalHelpRecvd = InformalHelpRecvd.Factory.newInstance();
		String staffAnswer = getStaffAnswer(domainNumber+"_2",ocanStaffFormData);
		informalHelpRecvd.setStaff(Byte.valueOf(staffAnswer));		
		return informalHelpRecvd;
	}
	
	public static FormalHelpRecvd convertFormalHelpRecvd(int domainNumber,OcanStaffForm ocanStaffForm, List<OcanStaffFormData> ocanStaffFormData) {
		FormalHelpRecvd formalHelpRecvd = FormalHelpRecvd.Factory.newInstance();
		String staffAnswer = getStaffAnswer(domainNumber+"_3a",ocanStaffFormData);
		formalHelpRecvd.setStaff(Byte.valueOf(staffAnswer));		
		return formalHelpRecvd;
	}
	
	public static FormalHelpNeed convertFormalHelpNeed(int domainNumber,OcanStaffForm ocanStaffForm, List<OcanStaffFormData> ocanStaffFormData) {
		FormalHelpNeed formalHelpNeed = FormalHelpNeed.Factory.newInstance();
		String staffAnswer = getStaffAnswer(domainNumber+"_3b",ocanStaffFormData);
		formalHelpNeed.setStaff(Byte.valueOf(staffAnswer));		
		return formalHelpNeed;
	}

	public static RiskUnemploymentList getRiskUnemploymentList(List<OcanStaffFormData> ocanStaffFormData) {
		RiskUnemploymentList riskUnemploymentList = RiskUnemploymentList.Factory.newInstance();
		List<String> answers = getMultipleStaffAnswer("5_unemployment_risk",ocanStaffFormData);
		riskUnemploymentList.setRiskUnemploymentArray(answers.toArray(new String[answers.size()]));		
		return riskUnemploymentList;
	}
	
	public static MedicalConditionList getMedicalConditionList(List<OcanStaffFormData> ocanStaffFormData) {
		MedicalConditionList medicalConditionList = MedicalConditionList.Factory.newInstance();
		List<String> answers = getMultipleStaffAnswer("6_medical_conditions",ocanStaffFormData);
		medicalConditionList.setMedicalConditionArray(answers.toArray(new String[answers.size()]));
		medicalConditionList.setAutismDetail(getStaffAnswer("6_medical_conditions_autism",ocanStaffFormData));
		medicalConditionList.setOtherDetail(getStaffAnswer("6_medical_conditions_other",ocanStaffFormData));
		return medicalConditionList;
	}
	
	public static ConcernAreaList getConcernAreaList(List<OcanStaffFormData> ocanStaffFormData) {
		ConcernAreaList concernAreaList = ConcernAreaList.Factory.newInstance();
		List<String> answers = getMultipleStaffAnswer("6_physical_health_details_other",ocanStaffFormData);
		concernAreaList.setConcernAreaArray(answers.toArray(new String[answers.size()]));		
		return concernAreaList;
	}
	
	public static MedicationList getMedicationList(List<OcanStaffFormData> ocanStaffFormData) {
		MedicationList medicationList = MedicationList.Factory.newInstance();
		
		int numberOfMedications = Integer.valueOf(getStaffAnswer("medications_count",ocanStaffFormData));
		List<MedicationDetail> medicationDetailList = new ArrayList<MedicationDetail>();
		for(int x=0;x<numberOfMedications;x++) {
			MedicationDetail medicationDetail = getMedicationDetail(x+1,ocanStaffFormData);
			medicationDetailList.add(medicationDetail);
		}
		medicationList.setMedicationDetailArray(medicationDetailList.toArray(new MedicationDetail[medicationDetailList.size()]));		
		return medicationList;
	}
	
	public static MedicationDetail getMedicationDetail(int index, List<OcanStaffFormData> ocanStaffFormData) {
		MedicationDetail medicationDetail = MedicationDetail.Factory.newInstance();
		medicationDetail.setIsHelpNeeded(getStaffAnswer("medication_"+index+"_help_needed",ocanStaffFormData));
		medicationDetail.setIsHelpProvided(getStaffAnswer("medication_"+index+"_help_provided",ocanStaffFormData));
		medicationDetail.setTakenAsPrescribed(getStaffAnswer("medication_"+index+"_taken_as_prescribed",ocanStaffFormData));
		return medicationDetail;
	}
	
	public static SideEffectsDetailList getSideEffectsDetailList(List<OcanStaffFormData> ocanStaffFormData) {
		SideEffectsDetailList sideEffectsDetailList = SideEffectsDetailList.Factory.newInstance();
		List<String> answers = getMultipleStaffAnswer("se_description",ocanStaffFormData);
		sideEffectsDetailList.setSideEffectsDetailArray(answers.toArray(new String[answers.size()]));
		sideEffectsDetailList.setOtherSideEffectsDetail(getStaffAnswer("se_description_other",ocanStaffFormData));
		return sideEffectsDetailList;
	}
	
	public static SymptomList getSymptomList(List<OcanStaffFormData> ocanStaffFormData) {
		SymptomList symptomList = SymptomList.Factory.newInstance();
		List<String> answers = getMultipleStaffAnswer("symptom_checklist",ocanStaffFormData);
		symptomList.setSymptomArray(answers.toArray(new String[answers.size()]));
		symptomList.setOtherSymptom(getStaffAnswer("symptom_checklist_other",ocanStaffFormData));
		return symptomList;
	}
	
	public static DiagnosticList getDiagnosticList(List<OcanStaffFormData> ocanStaffFormData) {
		DiagnosticList diagnosticList = DiagnosticList.Factory.newInstance();
		List<String> answers = getMultipleStaffAnswer("diagnostic_categories",ocanStaffFormData);
		diagnosticList.setDiagnosticArray(answers.toArray(new String[answers.size()]));
		return diagnosticList;
	}
	
	public static OtherIllnessList getOtherIllnessList(List<OcanStaffFormData> ocanStaffFormData) {
		OtherIllnessList otherIllnessList = OtherIllnessList.Factory.newInstance();
		List<String> answers = getMultipleStaffAnswer("other_illness",ocanStaffFormData);
		otherIllnessList.setOtherIllnessArray(answers.toArray(new String[answers.size()]));
		return otherIllnessList;
	}
	
	public static SafetyToSelfRiskList getSafetyToSelfRiskList(List<OcanStaffFormData> ocanStaffFormData) {
		SafetyToSelfRiskList safetyToSelfRiskList = SafetyToSelfRiskList.Factory.newInstance();
		List<String> answers = getMultipleStaffAnswer("risks",ocanStaffFormData);
		safetyToSelfRiskList.setSafetyToSelfRiskArray(answers.toArray(new String[answers.size()]));
		safetyToSelfRiskList.setOtherSafetyToSelfRisk(getStaffAnswer("risks_other",ocanStaffFormData));
		return safetyToSelfRiskList;
	}
	
	public static DrinkAlcohol getDrinkAlcohol(List<OcanStaffFormData> ocanStaffFormData) {
		DrinkAlcohol drinkAlcohol = DrinkAlcohol.Factory.newInstance();
		String quantity = getStaffAnswer("num_drinks",ocanStaffFormData);
		if(quantity!=null&&quantity.length()>0) {
			drinkAlcohol.setQuantity(new BigInteger(quantity));
		}
		drinkAlcohol.setFrequency(getStaffAnswer("frequency_alcohol",ocanStaffFormData));
		return drinkAlcohol;
	}
	
	public static DrugUseList getDrugUseList(List<OcanStaffFormData> ocanStaffFormData) {
		DrugUseList drugUseList = DrugUseList.Factory.newInstance();
		List<DrugUse> drugs = new ArrayList<DrugUse>();
		List<String> drugList = getDrugList();
		
		for(String drug:drugList) {
			String mnth = getStaffAnswer(drug+"_freq_6months",ocanStaffFormData);
			String ever = getStaffAnswer(drug+"_freq_ever",ocanStaffFormData);
			if(mnth.length()>0 || ever.length()>0) {
				//we have a winner
				DrugUse drugUse = DrugUse.Factory.newInstance();
				drugUse.setName(drug);
				if(mnth.length()>0) {
					drugUse.setFrequency("5");
				} else {
					drugUse.setFrequency("6");
				}
				drugs.add(drugUse);
			}
		}
		
		if(getStaffAnswer("drug_injection_freq_6months",ocanStaffFormData).length()>0) {
			drugUseList.setInjected("5");
		} else if(getStaffAnswer("drug_injection_freq_ever",ocanStaffFormData).length()>0) {
			drugUseList.setInjected("6");
		} else {
			drugUseList.setInjected("");
		}
		drugUseList.setDrugUseArray(drugs.toArray(new DrugUse[drugs.size()]));
		return drugUseList;
	}
	
	
	public static AddictionTypeList getAddictionTypeList(List<OcanStaffFormData> ocanStaffFormData) {
		AddictionTypeList addictionTypeList = AddictionTypeList.Factory.newInstance();
		List<String> answers = getMultipleStaffAnswer("addiction_type",ocanStaffFormData);
		addictionTypeList.setAddictionTypeArray(answers.toArray(new String[answers.size()]));
		addictionTypeList.setOtherAddictionType(getStaffAnswer("addiction_type_other",ocanStaffFormData));
		return addictionTypeList;
	}
	
	public static OrganizationRecord convertOrganizationRecord(OcanStaffForm ocanStaffForm, List<OcanStaffFormData> ocanStaffFormData) {
		OrganizationRecord organizationRecord = OrganizationRecord.Factory.newInstance();
		organizationRecord.setServiceOrg(convertServiceOrg(ocanStaffForm));		
		organizationRecord.setProgram(convertProgram(ocanStaffForm,ocanStaffFormData));
		organizationRecord.setMISFunction(convertMISFunction(ocanStaffForm,ocanStaffFormData));
		return organizationRecord;
	}
		
	public static ServiceOrg convertServiceOrg(OcanStaffForm ocanStaffForm) {
		Facility facility = facilityDao.find(ocanStaffForm.getFacilityId());
		ServiceOrg serviceOrg = ServiceOrg.Factory.newInstance();
		serviceOrg.setName(facility.getName());
		serviceOrg.setNumber(facility.getOcanServiceOrgNumber());
		return serviceOrg;
	}
	
	public static Program convertProgram(OcanStaffForm ocanStaffForm, List<OcanStaffFormData> ocanStaffFormData) {
		Admission admission = admissionDao.getAdmission(ocanStaffForm.getAdmissionId());		
		Program program = Program.Factory.newInstance();
		program.setName(admission.getProgramName());
		program.setNumber(String.valueOf(admission.getProgramId()));
		return program;
	}
	
	public static MISFunction convertMISFunction(OcanStaffForm ocanStaffForm, List<OcanStaffFormData> ocanStaffFormData) {
		MISFunction misFunction = MISFunction.Factory.newInstance();
		misFunction.setValue(getStaffAnswer("function",ocanStaffFormData));		
		return misFunction;
	}
		
	
	public static ClientRecord convertClientRecord(OcanStaffForm ocanStaffForm, List<OcanStaffFormData> ocanStaffFormData) {
		ClientRecord clientRecord = ClientRecord.Factory.newInstance();
		clientRecord.setClientID(convertClientID(ocanStaffForm,ocanStaffFormData));
		
		//START OF PHI
		clientRecord.setClientName(convertClientName(ocanStaffForm,ocanStaffFormData));
		clientRecord.setClientAddress(convertClientAddress(ocanStaffForm,ocanStaffFormData));
		//clientRecord.setClientPhone(ocanStaffForm.getPhoneNumber());
		clientRecord.setClientPhone("");
		clientRecord.setClientOHIP(convertClientOHIP(ocanStaffForm,ocanStaffFormData));
		clientRecord.setClientCulture("");
		
		//END OF PHI
		
		clientRecord.setReasonForAssessment(convertReasonForAssessment(ocanStaffForm,ocanStaffFormData));
		clientRecord.setClientContact(convertClientContact(ocanStaffForm,ocanStaffFormData));
		clientRecord.setServiceRecipientLocation(getStaffAnswer("service_recipient_location",ocanStaffFormData));
		clientRecord.setServiceRecipientLHIN(Integer.valueOf(getStaffAnswer("service_recipient_lhin",ocanStaffFormData)));
		clientRecord.setServiceDeliveryLHIN(Integer.valueOf(getStaffAnswer("service_recipient_lhin",ocanStaffFormData)));
		clientRecord.setClientDOB(ocanStaffForm.getDateOfBirth() + "Z");
		clientRecord.setGender(getStaffAnswer("gender",ocanStaffFormData));
		clientRecord.setMaritalStatus(getStaffAnswer("marital_status",ocanStaffFormData));
		clientRecord.setClientCapacity(convertClientCapacity(ocanStaffForm,ocanStaffFormData));
		clientRecord.setReferralSource(getStaffAnswer("source_of_referral",ocanStaffFormData));
		clientRecord.setAboriginalOrigin(getStaffAnswer("aboriginal",ocanStaffFormData));
		clientRecord.setCitizenshipStatus(getStaffAnswer("citizenship_status",ocanStaffFormData));
		clientRecord.setTimeLivedInCanada(convertTimeLivedInCanada(ocanStaffForm,ocanStaffFormData));
	
		List<String> immigrationExpAnswers = getMultipleStaffAnswer("immigration_issues",ocanStaffFormData);
		ImmigExpList immigExpList = null; 		
		for(String answer:immigrationExpAnswers) {
			if(immigExpList==null) {
				immigExpList = ImmigExpList.Factory.newInstance();
			}
			immigExpList.addValue(answer);
		}
		if(immigExpList!=null) {
			clientRecord.setImmigExpList(immigExpList);
		}
		
		List<String> discriminationExpAnswers = getMultipleStaffAnswer("discrimination",ocanStaffFormData);
		DiscrimExpList discrimExpList = null; 		
		for(String answer:discriminationExpAnswers) {
			if(discrimExpList==null) {
				discrimExpList = DiscrimExpList.Factory.newInstance();
			}
			discrimExpList.addValue(answer);
		}
		if(discrimExpList!=null) {
			clientRecord.setDiscrimExpList(discrimExpList);
		}
		
		
		clientRecord.setPrefLang(getStaffAnswer("preferred_language",ocanStaffFormData));
		clientRecord.setServiceLang(getStaffAnswer("language_service_provision",ocanStaffFormData));
		clientRecord.setLegalIssues(getStaffAnswer("legal_issues",ocanStaffFormData));
		
		List<String> legalStatusAnswers = getMultipleStaffAnswer("legal_status",ocanStaffFormData);
		LegalStatusList legalStatusList = null; 		
		for(String answer:legalStatusAnswers) {
			if(legalStatusList==null) {
				legalStatusList = LegalStatusList.Factory.newInstance();
			}
			legalStatusList.addLegalStatus(answer);
		}
		if(legalStatusList!=null) {
			clientRecord.setLegalStatusList(legalStatusList);
		}
		
		clientRecord.setExitDisposition(getStaffAnswer("exit_disposition",ocanStaffFormData));
		
		return clientRecord;
	}

	public static ClientID convertClientID(OcanStaffForm ocanStaffForm, List<OcanStaffFormData> ocanStaffFormData) {
		ClientID clientId = ClientID.Factory.newInstance();		
		clientId.setOrgClientID(String.valueOf(ocanStaffForm.getClientId()));
		return clientId;
	}
	
	public static ClientName convertClientName(OcanStaffForm ocanStaffForm, List<OcanStaffFormData> ocanStaffFormData) {
		ClientName clientName = ClientName.Factory.newInstance();
	//	clientName.setLast(ocanStaffForm.getLastName());
		//clientName.setFirst(ocanStaffForm.getFirstName());
		clientName.setLast("");
		clientName.setFirst("");
		return clientName;
	}
	
	public static ClientAddress convertClientAddress(OcanStaffForm ocanStaffForm, List<OcanStaffFormData> ocanStaffFormData) {
		ClientAddress clientAddress = ClientAddress.Factory.newInstance();
		/*
		clientAddress.setLine1(ocanStaffForm.getAddressLine1());
		clientAddress.setLine2(ocanStaffForm.getAddressLine2());
		clientAddress.setCity(ocanStaffForm.getCity());
		clientAddress.setProvince(ocanStaffForm.getProvince());
		clientAddress.setPostalCode(ocanStaffForm.getPostalCode());
		*/
		clientAddress.setLine1("");
		clientAddress.setLine2("");
		clientAddress.setCity("");
		clientAddress.setProvince("");
		clientAddress.setPostalCode("");
		return clientAddress;
	}
	
	
	public static ClientOHIP convertClientOHIP(OcanStaffForm ocanStaffForm, List<OcanStaffFormData> ocanStaffFormData) {
		ClientOHIP clientOhip = ClientOHIP.Factory.newInstance();	
		//clientOhip.setNumber(ocanStaffForm.getHcNumber());
		//clientOhip.setVersion(ocanStaffForm.getHcVersion());
		clientOhip.setNumber("");
		clientOhip.setVersion("");
		return clientOhip;
	}
	
	
	public static ReasonForAssessment convertReasonForAssessment(OcanStaffForm ocanStaffForm, List<OcanStaffFormData> ocanStaffFormData) {
		ReasonForAssessment reasonForAssessment = ReasonForAssessment.Factory.newInstance();
		String answer = ocanStaffForm.getReasonForAssessment();
		reasonForAssessment.setValue(answer);		
		reasonForAssessment.setOther(getStaffAnswer("reason_for_assessment_other",ocanStaffFormData));		
		return reasonForAssessment;
	}
	
	public static ClientContact convertClientContact(OcanStaffForm ocanStaffForm, List<OcanStaffFormData> ocanStaffFormData) {
		ClientContact clientContact = ClientContact.Factory.newInstance();	
		clientContact.setDoctorContact(convertDoctorContact(ocanStaffForm,ocanStaffFormData));
		clientContact.setPsychiatristContact(convertPsychiatristContact(ocanStaffForm,ocanStaffFormData));
		List<OtherPractitionerContact> otherPractitionerList = convertOtherPractitionerContact(ocanStaffForm,ocanStaffFormData);
		clientContact.setOtherPractitionerContactArray(otherPractitionerList.toArray(new OtherPractitionerContact[otherPractitionerList.size()]));
		clientContact.setOtherAgencyContact(convertOtherAgencyContact(ocanStaffForm,ocanStaffFormData));
		
		return clientContact;
	}
	
	public static DoctorContact convertDoctorContact(OcanStaffForm ocanStaffForm, List<OcanStaffFormData> ocanStaffFormData) {
		DoctorContact doctorContact = DoctorContact.Factory.newInstance();
		doctorContact.setDoctor(getStaffAnswer("doctor",ocanStaffFormData));
		doctorContact.setLastSeen(getStaffAnswer("doctor_last_seen",ocanStaffFormData));		
		return doctorContact;
	}
	
	public static PsychiatristContact convertPsychiatristContact(OcanStaffForm ocanStaffForm, List<OcanStaffFormData> ocanStaffFormData) {
		PsychiatristContact psychiatristContact = PsychiatristContact.Factory.newInstance();
		psychiatristContact.setPsychiatrist(getStaffAnswer("psychiatrist",ocanStaffFormData));
		psychiatristContact.setLastSeen(getStaffAnswer("psychiatrist_last_seen",ocanStaffFormData));		
		return psychiatristContact;
	}
	
	public static List<OtherPractitionerContact> convertOtherPractitionerContact(OcanStaffForm ocanStaffForm, List<OcanStaffFormData> ocanStaffFormData) {
		List<OtherPractitionerContact> list = new ArrayList<OtherPractitionerContact>();
		for(int x=0;x<3;x++) {
			int index = x+1;
			String contact = getStaffAnswer(index+"_other_contact",ocanStaffFormData);
			if(contact.length()>0) {
				OtherPractitionerContact otherPractitionerContact = OtherPractitionerContact.Factory.newInstance();
				otherPractitionerContact.setPractitionerType(contact);
				otherPractitionerContact.setLastSeen(getStaffAnswer(index+"_other_contact_last_seen",ocanStaffFormData));
				list.add(otherPractitionerContact);
			}
		}
		return list;
	}
	
	public static OtherAgencyContact convertOtherAgencyContact(OcanStaffForm ocanStaffForm, List<OcanStaffFormData> ocanStaffFormData) {
		OtherAgencyContact otherAgencyContact = OtherAgencyContact.Factory.newInstance();
		otherAgencyContact.setLastSeen(getStaffAnswer("other_agency_last_seen",ocanStaffFormData));		
		return otherAgencyContact;
	}
	
	public static ServiceRecipientLocation convertServiceRecipientLocationt(OcanStaffForm ocanStaffForm, List<OcanStaffFormData> ocanStaffFormData) {
		ServiceRecipientLocation serviceRecipientLocation = ServiceRecipientLocation.Factory.newInstance();		
		return serviceRecipientLocation;
	}
	
	public static ServiceRecipientLHIN convertServiceRecipientLHIN(OcanStaffForm ocanStaffForm, List<OcanStaffFormData> ocanStaffFormData) {
		ServiceRecipientLHIN serviceRecipientLhin = ServiceRecipientLHIN.Factory.newInstance();		
		return serviceRecipientLhin;
	}
	
	public static ServiceDeliveryLHIN convertServiceDeliveryLHIN(OcanStaffForm ocanStaffForm, List<OcanStaffFormData> ocanStaffFormData) {
		ServiceDeliveryLHIN serviceDeliveryLhin = ServiceDeliveryLHIN.Factory.newInstance();		
		return serviceDeliveryLhin;
	}
	
	
	public static ClientCapacity convertClientCapacity(OcanStaffForm ocanStaffForm, List<OcanStaffFormData> ocanStaffFormData) {
		ClientCapacity clientCapacity = ClientCapacity.Factory.newInstance();
		clientCapacity.setPersonalCare(getStaffAnswer("power_attorney_personal_care",ocanStaffFormData));
		clientCapacity.setProperty(getStaffAnswer("power_attorney_property",ocanStaffFormData));
		clientCapacity.setLegalGuardian(getStaffAnswer("court_appointed_guardian",ocanStaffFormData));
		return clientCapacity;
	}
	

	public static TimeLivedInCanada convertTimeLivedInCanada(OcanStaffForm ocanStaffForm, List<OcanStaffFormData> ocanStaffFormData) {
		TimeLivedInCanada timeLivedInCanada = TimeLivedInCanada.Factory.newInstance();
		String years = getStaffAnswer("years_in_canada",ocanStaffFormData);
		String months = getStaffAnswer("months_in_canada",ocanStaffFormData);
		if(years!=null && years.length()>0) {
			timeLivedInCanada.setYears(new BigInteger(years));	
		}
		if(months!=null && months.length()>0) {
			timeLivedInCanada.setMonths(new BigInteger(months));
			
		}
		return timeLivedInCanada;
	}
	
	
	public static DiscrimExpList convertDiscrimExpList(OcanStaffForm ocanStaffForm, List<OcanStaffFormData> ocanStaffFormData) {
		DiscrimExpList discrimExpList = DiscrimExpList.Factory.newInstance();		
		return discrimExpList;
	}
	

	public static LegalStatusList convertLegalStatusList(OcanStaffForm ocanStaffForm, List<OcanStaffFormData> ocanStaffFormData) {
		LegalStatusList legalStatusList = LegalStatusList.Factory.newInstance();		
		return legalStatusList;
	}
	
	public static LegalStatus convertLegalStatus(OcanStaffForm ocanStaffForm, List<OcanStaffFormData> ocanStaffFormData) {
		LegalStatus legalStatus = LegalStatus.Factory.newInstance();		
		return legalStatus;
	}
	
	public static ExitDisposition convertExitDisposition(OcanStaffForm ocanStaffForm, List<OcanStaffFormData> ocanStaffFormData) {
		ExitDisposition exitDisposition = ExitDisposition.Factory.newInstance();		
		return exitDisposition;
	}
	
	
	
	
	
	
	public static String getFilename(int year, int month, int increment) {
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
	
		return "OCAN" +  year + ( (month<10)?("0"+month):(month) )+ loggedInInfo.loggedInInfo.get().currentFacility.getOcanServiceOrgNumber() +  ( (increment<10)?("0"+increment):(increment) ) + ".xml";
	}
	
	private static Date getStartDate(int year, int month) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month-1);
		c.set(Calendar.DAY_OF_MONTH, 1);
		return c.getTime();
	}
	
	private static Date getEndDate(int year, int month) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month-1);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DATE));
		c.set(Calendar.HOUR_OF_DAY,23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		return c.getTime();
	}
	
	public static String convertToOcanXmlDateTime(Date date) {
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatter2 = new SimpleDateFormat("HH-mm-ss");
		
		return formatter1.format(date) + "T" + formatter2.format(date) + "Z";
	}
	
	public static String convertToOcanXmlDate(Date date) {
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
		
		return formatter1.format(date) + "Z";
	}
	
	static String getStaffAnswer(String question,List<OcanStaffFormData> ocanStaffFormData) {
		for(OcanStaffFormData item:ocanStaffFormData) {
			//logger.info("looking for " +  question + " - " + item.getQuestion());
			if(item.getQuestion().equals(question)) {
				return item.getAnswer();
			}
		}
		return "";
	}
	
	static String getClientAnswer(String question,List<OcanClientFormData> ocanClientFormData) {
		for(OcanClientFormData item:ocanClientFormData) {
			//logger.info("looking for " +  question + " - " + item.getQuestion());
			if(item.getQuestion().equals(question)) {
				return item.getAnswer();
			}
		}
		return "";
	}
	
	static List<String> getMultipleStaffAnswer(String question,List<OcanStaffFormData> ocanStaffFormData) {
		List<String> answers = new ArrayList<String>();
		for(OcanStaffFormData item:ocanStaffFormData) {
			//logger.info("looking for " +  question + " - " + item.getQuestion());
			if(item.getQuestion().equals(question)) {
				answers.add(item.getAnswer());
			}
		}
		return answers;
	}
	
	static String getDomainName(int domainNumber) {

		switch(domainNumber) {
		case 1: return "01-accommodation";
		case 2: return "02-food";
		case 3: return "03-looking after the home";
		case 4: return "04-self-care";
		case 5: return "05-daytime activities";
		case 6: return "06-physical health";
		case 7: return "07-psychotic symptoms";
		case 8: return "08-condition and treatment";
		case 9: return "09-psychological distress";
		case 10: return "10-safety to self";
		case 11: return "11-safety to others";
		case 12: return "12-alcohol";
		case 13: return "13-drugs";
		case 14: return "14-other addictions";
		case 15: return "15-company";
		case 16: return "16-intimate relationships";
		case 17: return "17-sexual expression";
		case 18: return "18-childcare";
		case 19: return "19-other dependents";
		case 20: return "20-basic education";
		case 21: return "21-telephone";
		case 22: return "22-transport";
		case 23: return "23-money";
		case 24: return "24-benefits";
		}
		return "";
	
	}
	
	static List<String> getDrugList() {
		List<String> result = new ArrayList<String>();
		result.add("398705004");
		result.add("288453002");
		result.add("229005006");
		result.add("226059005");
		result.add("226044004");
		result.add("372614000");
		result.add("80288002");
		result.add("61010005");
		result.add("410515003");		
		return result;
	}
}

