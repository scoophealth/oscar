/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package oscar.ocan.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import org.apache.xerces.jaxp.datatype.DatatypeFactoryImpl;

import oscar.ocan.domain.client.AnswerGroup;
import oscar.ocan.domain.client.OCANClientSelfAssessment;
import oscar.ocan.domain.staff.CCommunityTreatmentOrder;
import oscar.ocan.domain.staff.CIfYesPleaseIndicateTheAreasWhereYouHaveConcernsCheckAllThatApply;
import oscar.ocan.domain.staff.CMedicalConditionsCheckAllThatApply;
import oscar.ocan.domain.staff.COtherContact;
import oscar.ocan.domain.staff.CPsychiatricHistory;
import oscar.ocan.domain.staff.CWhichOfTheFollowingDrugsHaveYouUsedCheckAllThatApply;
import oscar.ocan.domain.staff.OCANStaffAssessment;
import oscar.ocan.domain.submission.Action;
import oscar.ocan.domain.submission.ActionList;
import oscar.ocan.domain.submission.AddictionTypeList;
import oscar.ocan.domain.submission.AdditionalElements;
import oscar.ocan.domain.submission.ClientAddress;
import oscar.ocan.domain.submission.ClientCapacity;
import oscar.ocan.domain.submission.ClientContact;
import oscar.ocan.domain.submission.ClientID;
import oscar.ocan.domain.submission.ClientName;
import oscar.ocan.domain.submission.ClientOHIP;
import oscar.ocan.domain.submission.ClientRecord;
import oscar.ocan.domain.submission.ConcernAreaList;
import oscar.ocan.domain.submission.DiagnosticList;
import oscar.ocan.domain.submission.DiscrimExpList;
import oscar.ocan.domain.submission.DoctorContact;
import oscar.ocan.domain.submission.Domain;
import oscar.ocan.domain.submission.DrinkAlcohol;
import oscar.ocan.domain.submission.DrugUse;
import oscar.ocan.domain.submission.DrugUseList;
import oscar.ocan.domain.submission.FormalHelpNeed;
import oscar.ocan.domain.submission.FormalHelpRecvd;
import oscar.ocan.domain.submission.ImmigExpList;
import oscar.ocan.domain.submission.InformalHelpRecvd;
import oscar.ocan.domain.submission.LegalStatusList;
import oscar.ocan.domain.submission.MISFunction;
import oscar.ocan.domain.submission.MedicalConditionList;
import oscar.ocan.domain.submission.MedicationDetail;
import oscar.ocan.domain.submission.MedicationList;
import oscar.ocan.domain.submission.NeedRating;
import oscar.ocan.domain.submission.OCANDomains;
import oscar.ocan.domain.submission.OCANSubmissionFile;
import oscar.ocan.domain.submission.OCANSubmissionRecord;
import oscar.ocan.domain.submission.ObjectFactory;
import oscar.ocan.domain.submission.OrganizationRecord;
import oscar.ocan.domain.submission.OtherAgencyContact;
import oscar.ocan.domain.submission.OtherIllnessList;
import oscar.ocan.domain.submission.OtherPractitionerContact;
import oscar.ocan.domain.submission.PresentingIssue;
import oscar.ocan.domain.submission.PresentingIssueList;
import oscar.ocan.domain.submission.Program;
import oscar.ocan.domain.submission.PsychiatristContact;
import oscar.ocan.domain.submission.ReasonForAssessment;
import oscar.ocan.domain.submission.Referral;
import oscar.ocan.domain.submission.ReferralList;
import oscar.ocan.domain.submission.RiskUnemploymentList;
import oscar.ocan.domain.submission.SafetyToSelfRiskList;
import oscar.ocan.domain.submission.ServiceOrg;
import oscar.ocan.domain.submission.SideEffectsDetailList;
import oscar.ocan.domain.submission.SymptomList;
import oscar.ocan.domain.submission.TimeLivedInCanada;

public class OcanDataProcessor {

	private DateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

	private String serviceOrganizationNumber;
	private String submissionFileLocation;

	private ObjectFactory of = new ObjectFactory();;

	public class OcanProcess {
		public Date now;
		public GregorianCalendar yearAgo;
		public GregorianCalendar sixMonthsAgo;
		public GregorianCalendar oneMonthAgo;
		public String assessmentId;
		public OCANSubmissionFile file;
		public OcanProcess() {
			now = new Date();
			yearAgo = new GregorianCalendar(); yearAgo.roll(Calendar.YEAR, -1);
			sixMonthsAgo = new GregorianCalendar(); sixMonthsAgo.roll(Calendar.MONTH, -6);
			oneMonthAgo = new GregorianCalendar(); oneMonthAgo.roll(Calendar.MONTH, -1);
			assessmentId = new SimpleDateFormat("yyyyMM").format(now);
		}
	}

	public OcanProcess createOcanProcess() {
		OcanProcess process = new OcanProcess();
		process.file = of.createOCANSubmissionFile();
		process.file.setVersion("1.0.0");
		process.file.setID("OCAN" + process.assessmentId + serviceOrganizationNumber + "01.xml");
		//process.file.setTimestamp(XMLGregorianCalendarImpl.parse(FORMATTER.format(process.now) + "T00:00:00Z"));
		process.file.setTimestamp(new DatatypeFactoryImpl().newXMLGregorianCalendar(FORMATTER.format(process.now) + "T00:00:00Z"));

		return process;
	}
	
	public void finishOcanProcess(OcanProcess process) throws FileNotFoundException, JAXBException {
		JAXBElement<OCANSubmissionFile> jbx = wrap(null, "OCANSubmissionFile", process.file);
		marshal(jbx, submissionFileLocation + "/" + process.file.getID());
	}

	public OCANSubmissionRecord createOcanRecord(OcanProcess process, InputStream clientIs,
			InputStream staffIs, String orgClientId) throws ParseException, NumberFormatException, JAXBException {

		OCANClientSelfAssessment client = unmarshal(OCANClientSelfAssessment.class, clientIs);
		OCANStaffAssessment staff = unmarshal(OCANStaffAssessment.class, staffIs);

		return createSubmissionRecord(process, client, staff, orgClientId);
	}

	private OCANSubmissionRecord createSubmissionRecord(OcanProcess process, OCANClientSelfAssessment client,
			OCANStaffAssessment staff, String orgClientId) throws ParseException, NumberFormatException {
		// Submission Record
		OCANSubmissionRecord r1 = of.createOCANSubmissionRecord();
		r1.setAssessmentID(process.assessmentId);
		r1.setAssessmentStatus("Complete");
		//r1.setStartDate(XMLGregorianCalendarImpl.parse(client.getCInstructions().getCStartDate()+"Z"));
		//r1.setCompletionDate(XMLGregorianCalendarImpl.parse(client.getCInstructions().getCStartDate()+"Z"));
		r1.setStartDate(new DatatypeFactoryImpl().newXMLGregorianCalendar(FORMATTER.format(client.getCInstructions().getCStartDate()) + "Z"));
		r1.setCompletionDate(new DatatypeFactoryImpl().newXMLGregorianCalendar(FORMATTER.format(client.getCInstructions().getCStartDate()) + "Z"));

		// Organization Record
		OrganizationRecord or1 = of.createOrganizationRecord();
		r1.setOrganizationRecord(or1);

		ServiceOrg so1 = of.createServiceOrg();
		so1.setName(staff.getCHeader().getCServiceOrganizationName());
		so1.setNumber(staff.getCHeader().getCServiceOrganizationNumber());
		or1.setServiceOrg(so1);

		Program p1 = of.createProgram();
		p1.setName(staff.getCHeader().getCProgramName());
		p1.setNumber(staff.getCHeader().getCProgramNumber());
		or1.setProgram(p1);

		MISFunction mf1 = of.createMISFunction();
		mf1.setValue(staff.getCHeader().getCFunctionMISFunctionalCentre());
		or1.setMISFunction(mf1);

		// Client Record
		ClientRecord cr = of.createClientRecord();
		r1.setClientRecord(cr);

		ClientID cid = of.createClientID();
		cid.setOrgClientID(orgClientId);
		cr.setClientID(cid);

		ClientName cn = of.createClientName();
		cn.setFirst("");
		cn.setLast("");
		cr.setClientName(cn);

		ClientAddress ca = of.createClientAddress();
		ca.setCity("");
		ca.setLine1("");
		ca.setLine2("");
		ca.setPostalCode("");
		ca.setProvince("");
		cr.setClientAddress(ca);

		ClientOHIP co = of.createClientOHIP();
		co.setNumber("");
		co.setVersion("");

		cr.setClientPhone("");
		cr.setClientOHIP(co);
		cr.setClientCulture("");

		ReasonForAssessment ra = of.createReasonForAssessment();
		if ("T".equals(staff.getCReasonForAssessmentSelectOne().getCInitialAssessment())) {
			ra.setValue("IA");
		} else if ("T".equals(staff.getCReasonForAssessmentSelectOne().getCReassessmentAt6Months())) {
			ra.setValue("RA");
		} else if ("T".equals(staff.getCReasonForAssessmentSelectOne().getCPriorToDischarge())) {
			ra.setValue("DIS");
		} else {
			ra.setValue("OTHR");
			ra.setOther(staff.getCReasonForAssessmentSelectOne().getCSpecifyOther());
		}
		cr.setReasonForAssessment(ra);

		ClientContact cc = of.createClientContact();
		cr.setClientContact(cc);

		DoctorContact dc = of.createDoctorContact();
		if ("T".equals(staff.getCDoctor().getCYes())) {
			dc.setDoctor("TRUE");
		} else if ("T".equals(staff.getCDoctor().getCNo())) {
			dc.setDoctor("FALSE");
		} else {
			dc.setDoctor("NA");
		}

		Date _dcls = FORMATTER.parse(staff.getCDoctor().getCLastSeen());
		if (_dcls.before(process.yearAgo.getTime())) {
			dc.setLastSeen("LS-13");
		} else if (_dcls.before(process.sixMonthsAgo.getTime())) {
			dc.setLastSeen("LS-12");
		} else if (_dcls.before(process.oneMonthAgo.getTime())) {
			dc.setLastSeen("LS-6");
		} else {
			dc.setLastSeen("LS-1");
		}
		cc.setDoctorContact(dc);

		PsychiatristContact pc = of.createPsychiatristContact();
		if ("T".equals(staff.getCPsychiatrist().getCYes())) {
			pc.setPsychiatrist("TRUE");
		} else if ("T".equals(staff.getCPsychiatrist().getCNo())) {
			pc.setPsychiatrist("FALSE");
		} else {
			pc.setPsychiatrist("NA");
		}

		Date _pcls = FORMATTER.parse(staff.getCPsychiatrist().getCLastSeen());
		if (_pcls.before(process.yearAgo.getTime())) {
			pc.setLastSeen("LS-13");
		} else if (_dcls.before(process.sixMonthsAgo.getTime())) {
			pc.setLastSeen("LS-12");
		} else if (_dcls.before(process.oneMonthAgo.getTime())) {
			pc.setLastSeen("LS-6");
		} else {
			pc.setLastSeen("LS-1");
		}
		cc.setPsychiatristContact(pc);

		Collection<COtherContact> otherContacts = staff.getCOtherContact();
		for (COtherContact oc : otherContacts) {
			OtherPractitionerContact oc1 = of.createOtherPractitionerContact();
			// dic item!
			oc1.setPractitionerType(oc.getCContactInformation());

			Date ocls = FORMATTER.parse(oc.getCLastSeen());
			if (ocls.before(process.yearAgo.getTime())) {
				oc1.setLastSeen("LS-13");
			} else if (ocls.before(process.sixMonthsAgo.getTime())) {
				oc1.setLastSeen("LS-12");
			} else if (ocls.before(process.oneMonthAgo.getTime())) {
				oc1.setLastSeen("LS-6");
			} else {
				oc1.setLastSeen("LS-1");
			}
			cc.getOtherPractitionerContact().add(oc1);
		}

		OtherAgencyContact oac = of.createOtherAgencyContact();

		Date oals = FORMATTER.parse(staff.getCOtherAgency().getCLastSeen());
		if (oals.before(process.yearAgo.getTime())) {
			oac.setLastSeen("LS-13");
		} else if (oals.before(process.sixMonthsAgo.getTime())) {
			oac.setLastSeen("LS-12");
		} else if (oals.before(process.oneMonthAgo.getTime())) {
			oac.setLastSeen("LS-6");
		} else {
			oac.setLastSeen("LS-1");
		}
		cc.setOtherAgencyContact(oac);

		// dic item!
		cr.setServiceRecipientLocation(staff.getCServiceInformation().getCServiceRecipientLocationCountyDistrictMunicipality());
		// dic item!
		cr.setServiceRecipientLHIN(Integer.parseInt(staff.getCServiceInformation().getCServiceRecipientLHIN()));
		// dic item!
		cr.setServiceDeliveryLHIN(Integer.parseInt(staff.getCServiceInformation().getCServiceDeliveryLHIN()));
		//cr.setClientDOB(XMLGregorianCalendarImpl.parse(staff.getCServiceInformation().getCDateOfBirthAge() + "Z"));
		cr.setClientDOB(new DatatypeFactoryImpl().newXMLGregorianCalendar(FORMATTER.format(staff.getCServiceInformation().getCDateOfBirthAge()) + "Z"));

		if ("T".equals(staff.getCGenderSelectOne().getCMale())) {
			cr.setGender("M");
		} else if ("T".equals(staff.getCGenderSelectOne().getCFemale())) {
			cr.setGender("F");
		} else if ("T".equals(staff.getCGenderSelectOne().getCOther())) {
			cr.setGender("OTH");
		} else if ("T".equals(staff.getCGenderSelectOne().getCUnknown())) {
			cr.setGender("UNK");
		} else {
			cr.setGender("CDA");
		}

		if ("T".equals(staff.getCMaritalStatusSelectOne().getCSingle())) {
			cr.setMaritalStatus("125681006");
		} else if ("T".equals(staff.getCMaritalStatusSelectOne().getCMarriedOrInCommonLawRelationship())) {
			cr.setMaritalStatus("87915002");
		} else if ("T".equals(staff.getCMaritalStatusSelectOne().getCPartnerOfSignificantOther())) {
			cr.setMaritalStatus("42120006");
		} else if ("T".equals(staff.getCMaritalStatusSelectOne().getCWiddowed())) {
			cr.setMaritalStatus("33553000");
		} else if ("T".equals(staff.getCMaritalStatusSelectOne().getCSeparated())) {
			cr.setMaritalStatus("13184001");
		} else if ("T".equals(staff.getCMaritalStatusSelectOne().getCDivorced())) {
			cr.setMaritalStatus("20295000");
		} else if ("T".equals(staff.getCMaritalStatusSelectOne().getCUnknown())) {
			cr.setMaritalStatus("261665006");
		} else {
			cr.setMaritalStatus("CDA");
		}


		ClientCapacity ccap = of.createClientCapacity();

		if ("T".equals(staff.getCClientCapacityStatusCheckAllThatApply().getCDoesTheClientHaveAPowerOfAttorneyForProperty().getCYes())) {
			ccap.setProperty("TRUE");
		} else if ("T".equals(staff.getCClientCapacityStatusCheckAllThatApply().getCDoesTheClientHaveAPowerOfAttorneyForProperty().getCNo())) {
			ccap.setProperty("FALSE");
		} else if ("T".equals(staff.getCClientCapacityStatusCheckAllThatApply().getCDoesTheClientHaveAPowerOfAttorneyForProperty().getCUnknown())) {
			ccap.setProperty("UNK");
		} else {
			ccap.setProperty("CDA");
		}

		if ("T".equals(staff.getCClientCapacityStatusCheckAllThatApply().getCDoesTheClientHaveAPowerOfAttorneyOrASubstituteDecisionMakerForPersonCare().getCYes())) {
			ccap.setPersonalCare("TRUE");
		} else if ("T".equals(staff.getCClientCapacityStatusCheckAllThatApply().getCDoesTheClientHaveAPowerOfAttorneyOrASubstituteDecisionMakerForPersonCare().getCNo())) {
			ccap.setPersonalCare("FALSE");
		} else if ("T".equals(staff.getCClientCapacityStatusCheckAllThatApply().getCDoesTheClientHaveAPowerOfAttorneyOrASubstituteDecisionMakerForPersonCare().getCUnknown())) {
			ccap.setPersonalCare("UNK");
		} else {
			ccap.setPersonalCare("CDA");
		}

		if ("T".equals(staff.getCClientCapacityStatusCheckAllThatApply().getCDoesTheClientHaveACourtAppointedGuardian().getCYes())) {
			ccap.setLegalGuardian("TRUE");
		} else if ("T".equals(staff.getCClientCapacityStatusCheckAllThatApply().getCDoesTheClientHaveACourtAppointedGuardian().getCNo())) {
			ccap.setLegalGuardian("FALSE");
		} else if ("T".equals(staff.getCClientCapacityStatusCheckAllThatApply().getCDoesTheClientHaveACourtAppointedGuardian().getCUnknown())) {
			ccap.setLegalGuardian("UNK");
		} else {
			ccap.setLegalGuardian("CDA");
		}

		cr.setClientCapacity(ccap);


		if ("T".equals(staff.getCWhoReferredYouToThisServiceSelectOne().getCGeneralHospital())) {
			cr.setReferralSource("018-01");
		} else if ("T".equals(staff.getCWhoReferredYouToThisServiceSelectOne().getCPsychiatricHospital())) {
			cr.setReferralSource("018-02");
		} else if ("T".equals(staff.getCWhoReferredYouToThisServiceSelectOne().getCOtherInstitution())) {
			cr.setReferralSource("018-03");
		} else if ("T".equals(staff.getCWhoReferredYouToThisServiceSelectOne().getCCommunityMentalHealthAndAddictionOrganization())) {
			cr.setReferralSource("018-04");
		} else if ("T".equals(staff.getCWhoReferredYouToThisServiceSelectOne().getCOtherCommunityAgencies())) {
			cr.setReferralSource("018-05");
		} else if ("T".equals(staff.getCWhoReferredYouToThisServiceSelectOne().getCFamilyPhysicians())) {
			cr.setReferralSource("018-06");
		} else if ("T".equals(staff.getCWhoReferredYouToThisServiceSelectOne().getCPsychiatrists())) {
			cr.setReferralSource("018-07");
		} else if ("T".equals(staff.getCWhoReferredYouToThisServiceSelectOne().getCMentalHealthWorker())) {
			cr.setReferralSource("018-08");
		} else if ("T".equals(staff.getCWhoReferredYouToThisServiceSelectOne().getCCriminalJusticeWorker())) {
			cr.setReferralSource("018-09");
		} else if ("T".equals(staff.getCWhoReferredYouToThisServiceSelectOne().getCSelfFamilyOrFriend())) {
			cr.setReferralSource("018-10");
		} else {
			cr.setReferralSource("018-11");
		}


		if ("T".equals(staff.getCAboriginalOriginSelectOne().getCAboriginal())) {
			cr.setAboriginalOrigin("011-01");
		} else if ("T".equals(staff.getCAboriginalOriginSelectOne().getCNonAboriginal())) {
			cr.setAboriginalOrigin("011-02");
		} else if ("T".equals(staff.getCAboriginalOriginSelectOne().getCUnknown())) {
			cr.setAboriginalOrigin("011-03");
		} else {
			cr.setAboriginalOrigin("011-04");
		}


		if ("T".equals(staff.getCCitizenshipStatusSelectOne().getCCanadianCitizen())) {
			cr.setCitizenshipStatus("CDN");
		} else if ("T".equals(staff.getCCitizenshipStatusSelectOne().getCPermanentResident())) {
			cr.setCitizenshipStatus("PR");
		} else if ("T".equals(staff.getCCitizenshipStatusSelectOne().getCTemporaryResident())) {
			cr.setCitizenshipStatus("TR");
		} else if ("T".equals(staff.getCCitizenshipStatusSelectOne().getCRefugee())) {
			cr.setCitizenshipStatus("REF");
		} else if ("T".equals(staff.getCCitizenshipStatusSelectOne().getCUnknown())) {
			cr.setCitizenshipStatus("UNK");
		} else {
			cr.setCitizenshipStatus("CDA");
		}


		TimeLivedInCanada tlc = of.createTimeLivedInCanada();
		String lic = staff.getCLengthOfTimeLivedInCanada().getCNumberOfYearsMonths();
		tlc.setYears(BigInteger.valueOf(Long.parseLong(lic.substring(0, lic.indexOf('/')))));
		tlc.setMonths(BigInteger.valueOf(Long.parseLong(lic.substring(lic.indexOf('/')+1))));
		cr.setTimeLivedInCanada(tlc);

		ImmigExpList iel = of.createImmigExpList();
		cr.setImmigExpList(iel);

		if ("T".equals(staff.getCDoYouHaveAnyIssuesWithYourImmigrationExperienceCheckAllThatApply().getCLackOfUnderstandingOfTheCanadianSystemResources())) {
			iel.getValue().add("1");
		} else if ("T".equals(staff.getCDoYouHaveAnyIssuesWithYourImmigrationExperienceCheckAllThatApply().getCApplyingPreviousWorkExperienceProfessionalQualifications())) {
			iel.getValue().add("2");
		} else if ("T".equals(staff.getCDoYouHaveAnyIssuesWithYourImmigrationExperienceCheckAllThatApply().getCSeparationFromFamilyMembersSignificantOthers())) {
			iel.getValue().add("3");
		} else if ("T".equals(staff.getCDoYouHaveAnyIssuesWithYourImmigrationExperienceCheckAllThatApply().getCFamilyLeftBehindInRefugeeCamp())) {
			iel.getValue().add("4");
		} else if ("T".equals(staff.getCDoYouHaveAnyIssuesWithYourImmigrationExperienceCheckAllThatApply().getCExperienceWithWarIncarcerationTorture())) {
			iel.getValue().add("5");
		} else if ("T".equals(staff.getCDoYouHaveAnyIssuesWithYourImmigrationExperienceCheckAllThatApply().getCRefugeeCamp())) {
			iel.getValue().add("6");
		} else if ("T".equals(staff.getCDoYouHaveAnyIssuesWithYourImmigrationExperienceCheckAllThatApply().getCExperienceWithOtherTrauma())) {
			iel.getValue().add("7");
		} else if (null != staff.getCDoYouHaveAnyIssuesWithYourImmigrationExperienceCheckAllThatApply().getCOther() &&
				staff.getCDoYouHaveAnyIssuesWithYourImmigrationExperienceCheckAllThatApply().getCOther().length() > 0) {
			iel.getValue().add("8");
			iel.setOtherImmigExp(staff.getCDoYouHaveAnyIssuesWithYourImmigrationExperienceCheckAllThatApply().getCOther());
		} else if ("T".equals(staff.getCDoYouHaveAnyIssuesWithYourImmigrationExperienceCheckAllThatApply().getCUnknown())) {
			iel.getValue().add("9");
		} else if ("T".equals(staff.getCDoYouHaveAnyIssuesWithYourImmigrationExperienceCheckAllThatApply().getCClientDeclinedToAnswer())) {
			iel.getValue().add("10");
		}


		DiscrimExpList del = of.createDiscrimExpList();
		cr.setDiscrimExpList(del);

		if ("T".equals(staff.getCExperienceOfDiscriminationCheckAllThatApply().getCDisability())) {
			del.getValue().add("21134002");
		} else if ("T".equals(staff.getCExperienceOfDiscriminationCheckAllThatApply().getCEthnicity())) {
			del.getValue().add("397731000");
		} else if ("T".equals(staff.getCExperienceOfDiscriminationCheckAllThatApply().getCGender())) {
			del.getValue().add("365873007");
		} else if ("T".equals(staff.getCExperienceOfDiscriminationCheckAllThatApply().getCImmigration())) {
			del.getValue().add("IMGR");
		} else if ("T".equals(staff.getCExperienceOfDiscriminationCheckAllThatApply().getCMentalIllness())) {
			del.getValue().add("74732009");
		} else if ("T".equals(staff.getCExperienceOfDiscriminationCheckAllThatApply().getCRace())) {
			del.getValue().add("415229000");
		} else if ("T".equals(staff.getCExperienceOfDiscriminationCheckAllThatApply().getCReligion())) {
			del.getValue().add("365577002");
		} else if ("T".equals(staff.getCExperienceOfDiscriminationCheckAllThatApply().getCSexualOrientation())) {
			del.getValue().add("365956009");
		} else if (null != staff.getCExperienceOfDiscriminationCheckAllThatApply().getCOther() &&
				staff.getCExperienceOfDiscriminationCheckAllThatApply().getCOther().length() > 0) {
			del.getValue().add("410515003");
			del.setOtherDiscrimExp(staff.getCExperienceOfDiscriminationCheckAllThatApply().getCOther());
		} else if ("T".equals(staff.getCExperienceOfDiscriminationCheckAllThatApply().getCUnknown())) {
			del.getValue().add("261665006");
		} else if ("T".equals(staff.getCExperienceOfDiscriminationCheckAllThatApply().getCClientDeclinedToAnswer())) {
			del.getValue().add("CDA");
		}


		// dic item!
		cr.setPrefLang(staff.getCServiceRecipientPreferredLanguage().getC());
		// dic item!
		cr.setServiceLang(staff.getCLanguageOfServiceProvision().getC());


		if ("T".equals(staff.getCDoYouHaveAnyLegalIssuesSelectOne().getCCivil())) {
			cr.setLegalIssues("Civil");
		} else if ("T".equals(staff.getCDoYouHaveAnyLegalIssuesSelectOne().getCCriminal())) {
			cr.setLegalIssues("Criminal");
		} else if ("T".equals(staff.getCDoYouHaveAnyLegalIssuesSelectOne().getCNone())) {
			cr.setLegalIssues("None");
		} else if ("T".equals(staff.getCDoYouHaveAnyLegalIssuesSelectOne().getCUnknown())) {
			cr.setLegalIssues("UNK");
		} else if ("T".equals(staff.getCDoYouHaveAnyLegalIssuesSelectOne().getCClientDeclinedToAnswer())) {
			cr.setLegalIssues("CDA");
		}


		LegalStatusList lsl = of.createLegalStatusList();
		cr.setLegalStatusList(lsl);

		if ("T".equals(staff.getCLegalStatusCheckAllThatApply().getCPreCharge().getCPreChargeDiversion())) {
			lsl.getLegalStatus().add("013-01");
		}
		if ("T".equals(staff.getCLegalStatusCheckAllThatApply().getCPreCharge().getCCourtDiversionProgram())) {
			lsl.getLegalStatus().add("013-02");
		}
		if ("T".equals(staff.getCLegalStatusCheckAllThatApply().getCPreTrial().getCAwaitingFitnessAssessment())) {
			lsl.getLegalStatus().add("013-03");
		}
		if ("T".equals(staff.getCLegalStatusCheckAllThatApply().getCPreTrial().getCAwaitingTrialWithOrWithoutBail())) {
			lsl.getLegalStatus().add("013-04");
		}
		if ("T".equals(staff.getCLegalStatusCheckAllThatApply().getCPreTrial().getCAwaitingCriminalResponsibilityAssessmentNCR())) {
			lsl.getLegalStatus().add("013-05");
		}
		if ("T".equals(staff.getCLegalStatusCheckAllThatApply().getCPreTrial().getCInCommunityOnOwnRecognizance())) {
			lsl.getLegalStatus().add("013-06");
		}
		if ("T".equals(staff.getCLegalStatusCheckAllThatApply().getCPreTrial().getCUnfitToStandTrial())) {
			lsl.getLegalStatus().add("013-07");
		}
		if ("T".equals(staff.getCLegalStatusCheckAllThatApply().getCOutcomes().getCChargesWithdrawn())) {
			lsl.getLegalStatus().add("013-08");
		}
		if ("T".equals(staff.getCLegalStatusCheckAllThatApply().getCOutcomes().getCStayOfProceedings())) {
			lsl.getLegalStatus().add("013-09");
		}
		if ("T".equals(staff.getCLegalStatusCheckAllThatApply().getCOutcomes().getCAwaitingSentence())) {
			lsl.getLegalStatus().add("013-10");
		}
		if ("T".equals(staff.getCLegalStatusCheckAllThatApply().getCOutcomes().getCNCR())) {
			lsl.getLegalStatus().add("013-11");
		}
		if ("T".equals(staff.getCLegalStatusCheckAllThatApply().getCOutcomes().getCConditionalDischarge())) {
			lsl.getLegalStatus().add("013-12");
		}
		if ("T".equals(staff.getCLegalStatusCheckAllThatApply().getCOutcomes().getCConditionalSentence())) {
			lsl.getLegalStatus().add("013-13");
		}
		if ("T".equals(staff.getCLegalStatusCheckAllThatApply().getCOutcomes().getCRestrainingOrder())) {
			lsl.getLegalStatus().add("013-14");
		}
		if ("T".equals(staff.getCLegalStatusCheckAllThatApply().getCOutcomes().getCPeaceBond())) {
			lsl.getLegalStatus().add("013-15");
		}
		if ("T".equals(staff.getCLegalStatusCheckAllThatApply().getCOutcomes().getCSuspendedSentence())) {
			lsl.getLegalStatus().add("013-16");
		}
		if ("T".equals(staff.getCLegalStatusCheckAllThatApply().getCCustodyStatus().getCORBDetainedCommunityAccess())) {
			lsl.getLegalStatus().add("013-17");
		}
		if ("T".equals(staff.getCLegalStatusCheckAllThatApply().getCCustodyStatus().getCORBConditionalDischarge())) {
			lsl.getLegalStatus().add("013-18");
		}
		if ("T".equals(staff.getCLegalStatusCheckAllThatApply().getCCustodyStatus().getCOnParole())) {
			lsl.getLegalStatus().add("013-19");
		}
		if ("T".equals(staff.getCLegalStatusCheckAllThatApply().getCCustodyStatus().getCOnProbation())) {
			lsl.getLegalStatus().add("013-20");
		}
		List<Serializable> _ols = staff.getCLegalStatusCheckAllThatApply().getCOther().getContent();
		for (Object obj : _ols) {
			try {
				JAXBElement<?> e = (JAXBElement<?>) obj;
				if ("T".equals(e.getValue().toString())) {
					if ("CNo_legal_problems__includes_absolute_discharge_and_time_served__end_of_custody_".equals(e.getName().toString())) {
						lsl.getLegalStatus().add("013-21");
					}
					if ("CUnknown".equals(e.getName().toString())) {
						lsl.getLegalStatus().add("013-24");
					}
					if ("CClient_declined_to_answer".equals(e.getName().toString())) {
						lsl.getLegalStatus().add("013-25");
					}
				}
			} catch (ClassCastException e) {
				continue;
			}
		}



		if ("T".equals(staff.getCExitDispositionSelectOneIfApplicable().getCCompletionWithoutReferral())) {
			cr.setExitDisposition("019-01");
		} else if ("T".equals(staff.getCExitDispositionSelectOneIfApplicable().getCCompletionWithReferral())) {
			cr.setExitDisposition("019-02");
		} else if ("T".equals(staff.getCExitDispositionSelectOneIfApplicable().getCSuicides())) {
			cr.setExitDisposition("019-03");
		} else if ("T".equals(staff.getCExitDispositionSelectOneIfApplicable().getCDeath())) {
			cr.setExitDisposition("019-04");
		} else if ("T".equals(staff.getCExitDispositionSelectOneIfApplicable().getCRelocation())) {
			cr.setExitDisposition("019-05");
		} else if ("T".equals(staff.getCExitDispositionSelectOneIfApplicable().getCWithdrawal())) {
			cr.setExitDisposition("019-06");
		}

		// end of Client Record


		OCANDomains ods = of.createOCANDomains();
		r1.setOCANDomains(ods);


		// 24 Domains
		Domain d1 = createCommonDomain(of, "01-accommodation",
			staff.getC1AccommodationWhatKindOfPlaceDoYouLiveInWhatSortOfPlaceIsIt().getC1DoesThePersonLackACurrentPlaceToStayIfRated0Or9SkipQuestions23AndPro(),
			client.getCQuestions().getC1AccommodationWhatKindOfPlaceDoYouLiveIn().getAnswerGroup(),
			staff.getC1AccommodationWhatKindOfPlaceDoYouLiveInWhatSortOfPlaceIsIt().getC2HowMuchHelpWithAccommodationDoesThePersonReceiveFromFriendsOrRelatives(),
			staff.getC1AccommodationWhatKindOfPlaceDoYouLiveInWhatSortOfPlaceIsIt().getC3AHowMuchHelpWithGettingEnoughToEatDoesThePersonReceiveFromLocalServices(),
			staff.getC1AccommodationWhatKindOfPlaceDoYouLiveInWhatSortOfPlaceIsIt().getC3BHowMuchHelpWithGettingEnoughToEatDoesThePersonNeedFromLocalServices());
		ods.getDomain().add(d1);

		if ("T".equals(staff.getC1AccommodationWhatKindOfPlaceDoYouLiveInWhatSortOfPlaceIsIt().getCWhereDoYouLiveSelectOne().getCApprovedHomesAndHomesForSpecialCare())) {
			d1.setResidenceType("024-01");
		} else if ("T".equals(staff.getC1AccommodationWhatKindOfPlaceDoYouLiveInWhatSortOfPlaceIsIt().getCWhereDoYouLiveSelectOne().getCCorrectionalProbationFacility())) {
			d1.setResidenceType("024-02");
		} else if ("T".equals(staff.getC1AccommodationWhatKindOfPlaceDoYouLiveInWhatSortOfPlaceIsIt().getCWhereDoYouLiveSelectOne().getCDomiciliaryHostel())) {
			d1.setResidenceType("024-03");
		} else if ("T".equals(staff.getC1AccommodationWhatKindOfPlaceDoYouLiveInWhatSortOfPlaceIsIt().getCWhereDoYouLiveSelectOne().getCGeneralHospital())) {
			d1.setResidenceType("024-04");
		} else if ("T".equals(staff.getC1AccommodationWhatKindOfPlaceDoYouLiveInWhatSortOfPlaceIsIt().getCWhereDoYouLiveSelectOne().getCPsychiatricHospital())) {
			d1.setResidenceType("024-05");
		} else if ("T".equals(staff.getC1AccommodationWhatKindOfPlaceDoYouLiveInWhatSortOfPlaceIsIt().getCWhereDoYouLiveSelectOne().getCOtherSpecialtyHospital())) {
			d1.setResidenceType("024-06");
		} else if ("T".equals(staff.getC1AccommodationWhatKindOfPlaceDoYouLiveInWhatSortOfPlaceIsIt().getCWhereDoYouLiveSelectOne().getCNoFixedAddress())) {
			d1.setResidenceType("024-07");
		} else if ("T".equals(staff.getC1AccommodationWhatKindOfPlaceDoYouLiveInWhatSortOfPlaceIsIt().getCWhereDoYouLiveSelectOne().getCHostelShelter())) {
			d1.setResidenceType("024-08");
		} else if ("T".equals(staff.getC1AccommodationWhatKindOfPlaceDoYouLiveInWhatSortOfPlaceIsIt().getCWhereDoYouLiveSelectOne().getCLongTermCareFacilityNursingHome())) {
			d1.setResidenceType("024-09");
		} else if ("T".equals(staff.getC1AccommodationWhatKindOfPlaceDoYouLiveInWhatSortOfPlaceIsIt().getCWhereDoYouLiveSelectOne().getCMunicipalNonProfitHousing())) {
			d1.setResidenceType("024-10");
		} else if ("T".equals(staff.getC1AccommodationWhatKindOfPlaceDoYouLiveInWhatSortOfPlaceIsIt().getCWhereDoYouLiveSelectOne().getCPrivateNonProfitHousing())) {
			d1.setResidenceType("024-11");
		} else if ("T".equals(staff.getC1AccommodationWhatKindOfPlaceDoYouLiveInWhatSortOfPlaceIsIt().getCWhereDoYouLiveSelectOne().getCPrivateHouseAptSROwnedMarketRent())) {
			d1.setResidenceType("024-12");
		} else if ("T".equals(staff.getC1AccommodationWhatKindOfPlaceDoYouLiveInWhatSortOfPlaceIsIt().getCWhereDoYouLiveSelectOne().getCPrivateHouseAptOtherSubsidized())) {
			d1.setResidenceType("024-13");
//		} else if ("T".equals(staff.getC1AccommodationWhatKindOfPlaceDoYouLiveInWhatSortOfPlaceIsIt().getCWhereDoYouLiveSelectOne().getCRetirementHome())) {
//			d1.setResidenceType("024-14");
		} else if ("T".equals(staff.getC1AccommodationWhatKindOfPlaceDoYouLiveInWhatSortOfPlaceIsIt().getCWhereDoYouLiveSelectOne().getCRoomingBoardingHouse())) {
			d1.setResidenceType("024-15");
		} else if ("T".equals(staff.getC1AccommodationWhatKindOfPlaceDoYouLiveInWhatSortOfPlaceIsIt().getCWhereDoYouLiveSelectOne().getCSupportiveHousingCongregateLiving())) {
			d1.setResidenceType("024-16");
		} else if ("T".equals(staff.getC1AccommodationWhatKindOfPlaceDoYouLiveInWhatSortOfPlaceIsIt().getCWhereDoYouLiveSelectOne().getCSupportiveHousingAssistedLiving())) {
			d1.setResidenceType("024-17");
		} else if ("T".equals(staff.getC1AccommodationWhatKindOfPlaceDoYouLiveInWhatSortOfPlaceIsIt().getCWhereDoYouLiveSelectOne().getCOther())) {
			d1.setResidenceType("024-18");
		} else if ("T".equals(staff.getC1AccommodationWhatKindOfPlaceDoYouLiveInWhatSortOfPlaceIsIt().getCWhereDoYouLiveSelectOne().getCUnknown())) {
			d1.setResidenceType("024-19");
		} else {
			d1.setResidenceType("024-20");
		}

		if ("T".equals(staff.getC1AccommodationWhatKindOfPlaceDoYouLiveInWhatSortOfPlaceIsIt().getCDoYouReceiveAnySupportSelectOne().getCIndependent())) {
			d1.setResidenceSupport("24A-01");
		} else if ("T".equals(staff.getC1AccommodationWhatKindOfPlaceDoYouLiveInWhatSortOfPlaceIsIt().getCDoYouReceiveAnySupportSelectOne().getCAssistedSupported())) {
			d1.setResidenceSupport("24A-02");
		} else if ("T".equals(staff.getC1AccommodationWhatKindOfPlaceDoYouLiveInWhatSortOfPlaceIsIt().getCDoYouReceiveAnySupportSelectOne().getCSupervisedNonFacility())) {
			d1.setResidenceSupport("24A-03");
		} else if ("T".equals(staff.getC1AccommodationWhatKindOfPlaceDoYouLiveInWhatSortOfPlaceIsIt().getCDoYouReceiveAnySupportSelectOne().getCSupervisedFacility())) {
			d1.setResidenceSupport("24A-04");
		} else if ("T".equals(staff.getC1AccommodationWhatKindOfPlaceDoYouLiveInWhatSortOfPlaceIsIt().getCDoYouReceiveAnySupportSelectOne().getCUnknown())) {
			d1.setResidenceSupport("24A-05");
		} else {
			d1.setResidenceSupport("24A-06");
		}

		if ("T".equals(staff.getC1AccommodationWhatKindOfPlaceDoYouLiveInWhatSortOfPlaceIsIt().getCDoYouLiveWithAnyoneSelectOne().getCSelf())) {
			d1.setLivingArrangementType("023-01");
		} else if ("T".equals(staff.getC1AccommodationWhatKindOfPlaceDoYouLiveInWhatSortOfPlaceIsIt().getCDoYouLiveWithAnyoneSelectOne().getCSpousePartner())) {
			d1.setLivingArrangementType("023-02");
		} else if ("T".equals(staff.getC1AccommodationWhatKindOfPlaceDoYouLiveInWhatSortOfPlaceIsIt().getCDoYouLiveWithAnyoneSelectOne().getCSpousePartnerAndOthers())) {
			d1.setLivingArrangementType("023-03");
		} else if ("T".equals(staff.getC1AccommodationWhatKindOfPlaceDoYouLiveInWhatSortOfPlaceIsIt().getCDoYouLiveWithAnyoneSelectOne().getCChildren())) {
			d1.setLivingArrangementType("023-04");
		} else if ("T".equals(staff.getC1AccommodationWhatKindOfPlaceDoYouLiveInWhatSortOfPlaceIsIt().getCDoYouLiveWithAnyoneSelectOne().getCParents())) {
			d1.setLivingArrangementType("023-05");
		} else if ("T".equals(staff.getC1AccommodationWhatKindOfPlaceDoYouLiveInWhatSortOfPlaceIsIt().getCDoYouLiveWithAnyoneSelectOne().getCRelatives())) {
			d1.setLivingArrangementType("023-06");
		} else if ("T".equals(staff.getC1AccommodationWhatKindOfPlaceDoYouLiveInWhatSortOfPlaceIsIt().getCDoYouLiveWithAnyoneSelectOne().getCNonRelatives())) {
			d1.setLivingArrangementType("023-07");
		} else if ("T".equals(staff.getC1AccommodationWhatKindOfPlaceDoYouLiveInWhatSortOfPlaceIsIt().getCDoYouLiveWithAnyoneSelectOne().getCUnknown())) {
			d1.setLivingArrangementType("023-08");
		} else {
			d1.setLivingArrangementType("023-09");
		}


		Domain d2 = createCommonDomain(of, "02-food",
			staff.getC2FoodWhatKindOfFoodFoYouEatAreYouAbleToPrepareYourOwnMealsAndDoYourOwnS().getC1DoesThePersonHaveDifficultyGettingEnoughToEatIfRated0Or9GoToTheNextDoma(),
			client.getCQuestions().getC2FoodDoYouGetEnoughToEat().getAnswerGroup(),
			staff.getC2FoodWhatKindOfFoodFoYouEatAreYouAbleToPrepareYourOwnMealsAndDoYourOwnS().getC2HowMuchHelpWithGettingEnoughToEatDoesThePersonReceiveFromFriendsOrRelatives(),
			staff.getC2FoodWhatKindOfFoodFoYouEatAreYouAbleToPrepareYourOwnMealsAndDoYourOwnS().getC3AHowMuchHelpWithGettingEnoughToEatDoesThePersonReceiveFromLocalServices(),
			staff.getC2FoodWhatKindOfFoodFoYouEatAreYouAbleToPrepareYourOwnMealsAndDoYourOwnS().getC3BHowMuchHelpWithGettingEnoughToEatDoesThePersonNeedFromLocalServices());
		ods.getDomain().add(d2);


		Domain d3 = createCommonDomain(of, "03-looking after the home",
			staff.getC3LookingAfterTheHomeAreYouAbleToLookAfterYourHomeDoesAnyoneHelpYou().getC1DoesThePersonHaveDifficultyLookingAfterTheHomeIfRated0Or9GoToTheNextDom(),
			client.getCQuestions().getC3LookingAfterTheHomeAreYouAbleToLookAfterYourHome().getAnswerGroup(),
			staff.getC3LookingAfterTheHomeAreYouAbleToLookAfterYourHomeDoesAnyoneHelpYou().getC2HowMuchHelpWithLookingAfterTheHomeDoesThePersonReceiveFromFriendsOrRelatives(),
			staff.getC3LookingAfterTheHomeAreYouAbleToLookAfterYourHomeDoesAnyoneHelpYou().getC3AHowMuchHelpWithLookingAfterTheHomeDoesThePersonReceiveFromLocalServices(),
			staff.getC3LookingAfterTheHomeAreYouAbleToLookAfterYourHomeDoesAnyoneHelpYou().getC3BHowMuchHelpWithLookingAfterTheHomeDoesThePersonNeedFromLocalServices());
		ods.getDomain().add(d3);


		Domain d4 = createCommonDomain(of, "04-self-care",
			staff.getC4SelfCareDoYouHaveProblemsKeepingCleanAndTidyDoYouEverNeedRemindingWhoBy().getC1DoesThePersonHaveDifficultyWitSelfCareIfRated0Or9GoToTheNextDomain(),
			client.getCQuestions().getC4SelfCareDoYouHaveProblemsKeepingCleanAndTidy().getAnswerGroup(),
			staff.getC4SelfCareDoYouHaveProblemsKeepingCleanAndTidyDoYouEverNeedRemindingWhoBy().getC2HowMuchHelpWithSelfCareDoesThePersonReceiveFromFriendsOrRelatives(),
			staff.getC4SelfCareDoYouHaveProblemsKeepingCleanAndTidyDoYouEverNeedRemindingWhoBy().getC3AHowMuchHelpWithSelfCareDoesThePersonReceiveFromLocalServices(),
			staff.getC4SelfCareDoYouHaveProblemsKeepingCleanAndTidyDoYouEverNeedRemindingWhoBy().getC3BHowMuchHelpWithSelfCareFoesThePersonNeedFromLocalServices());
		ods.getDomain().add(d4);


		Domain d5 = createCommonDomain(of, "05-daytime activities",
			staff.getC5DaytimeActivitiesHowDoYouSpendYourDayDoYouHaveEnoughToDo().getC1DoesThePersonHaveDifficultyWithRegularAppropriateDaytimeActivitiesIfRated0Or(),
			client.getCQuestions().getC5DaytimeActivitiesHowDoYouSpendYourDay().getAnswerGroup(),
			staff.getC5DaytimeActivitiesHowDoYouSpendYourDayDoYouHaveEnoughToDo().getC2HowMuchHelpDoesThePersonReceiveFromFriendsOrRelativesInFindingAndKeepingRegul(),
			staff.getC5DaytimeActivitiesHowDoYouSpendYourDayDoYouHaveEnoughToDo().getC3AHowMuchHelpDoesThePersonReceiveFromLocalServicesInFindingAndKeepingRegularAn(),
			staff.getC5DaytimeActivitiesHowDoYouSpendYourDayDoYouHaveEnoughToDo().getC3BHowMuchHelpDoesThePersonNeedFromLocalServicesInFindingAndKeepingRegularAndA());
		ods.getDomain().add(d5);

		if ("T".equals(staff.getC5DaytimeActivitiesHowDoYouSpendYourDayDoYouHaveEnoughToDo().getCWhatIsYourCurrentEmploymentStatusSelectOne().getCIndependentCompetitive())) {
			d5.setEmployStatus("224363007");
		} else if ("T".equals(staff.getC5DaytimeActivitiesHowDoYouSpendYourDayDoYouHaveEnoughToDo().getCWhatIsYourCurrentEmploymentStatusSelectOne().getCAssistedSupportive())) {
			d5.setEmployStatus("ES-1");
		} else if ("T".equals(staff.getC5DaytimeActivitiesHowDoYouSpendYourDayDoYouHaveEnoughToDo().getCWhatIsYourCurrentEmploymentStatusSelectOne().getCAlternativeBusinesses())) {
			d5.setEmployStatus("ES-2");
		} else if ("T".equals(staff.getC5DaytimeActivitiesHowDoYouSpendYourDayDoYouHaveEnoughToDo().getCWhatIsYourCurrentEmploymentStatusSelectOne().getCShelteredWorkshop())) {
			d5.setEmployStatus("224366004");
		} else if ("T".equals(staff.getC5DaytimeActivitiesHowDoYouSpendYourDayDoYouHaveEnoughToDo().getCWhatIsYourCurrentEmploymentStatusSelectOne().getCNonPaidWorkExperience())) {
			d5.setEmployStatus("276061003");
		} else if ("T".equals(staff.getC5DaytimeActivitiesHowDoYouSpendYourDayDoYouHaveEnoughToDo().getCWhatIsYourCurrentEmploymentStatusSelectOne().getCNoEmploymentOtherActivity())) {
			d5.setEmployStatus("ES-3");
		} else if ("T".equals(staff.getC5DaytimeActivitiesHowDoYouSpendYourDayDoYouHaveEnoughToDo().getCWhatIsYourCurrentEmploymentStatusSelectOne().getCCasualSporadic())) {
			d5.setEmployStatus("224364001");
		} else if ("T".equals(staff.getC5DaytimeActivitiesHowDoYouSpendYourDayDoYouHaveEnoughToDo().getCWhatIsYourCurrentEmploymentStatusSelectOne().getCNoEmploymentOfAnyKind())) {
			d5.setEmployStatus("73438004");
		} else if ("T".equals(staff.getC5DaytimeActivitiesHowDoYouSpendYourDayDoYouHaveEnoughToDo().getCWhatIsYourCurrentEmploymentStatusSelectOne().getCUnknown())) {
			d5.setEmployStatus("261665006");
		} else {
			d5.setEmployStatus("CDA");
		}

		if ("T".equals(staff.getC5DaytimeActivitiesHowDoYouSpendYourDayDoYouHaveEnoughToDo().getCAreYouCurrentlyInSchoolSelectOne().getCNotInSchool())) {
			d5.setEducationProgramStatus("224304004");
		} else if ("T".equals(staff.getC5DaytimeActivitiesHowDoYouSpendYourDayDoYouHaveEnoughToDo().getCAreYouCurrentlyInSchoolSelectOne().getCElementaryJuniorHighSchool())) {
			d5.setEducationProgramStatus("224306002");
		} else if ("T".equals(staff.getC5DaytimeActivitiesHowDoYouSpendYourDayDoYouHaveEnoughToDo().getCAreYouCurrentlyInSchoolSelectOne().getCSecondaryHighSchool())) {
			d5.setEducationProgramStatus("224308001");
		} else if ("T".equals(staff.getC5DaytimeActivitiesHowDoYouSpendYourDayDoYouHaveEnoughToDo().getCAreYouCurrentlyInSchoolSelectOne().getCTradeSchool())) {
			d5.setEducationProgramStatus("224860003");
		} else if ("T".equals(staff.getC5DaytimeActivitiesHowDoYouSpendYourDayDoYouHaveEnoughToDo().getCAreYouCurrentlyInSchoolSelectOne().getCVocationalTrainingSchool())) {
			d5.setEducationProgramStatus("54106008");
		} else if ("T".equals(staff.getC5DaytimeActivitiesHowDoYouSpendYourDayDoYouHaveEnoughToDo().getCAreYouCurrentlyInSchoolSelectOne().getCAdultEducation())) {
			d5.setEducationProgramStatus("161125007");
		} else if ("T".equals(staff.getC5DaytimeActivitiesHowDoYouSpendYourDayDoYouHaveEnoughToDo().getCAreYouCurrentlyInSchoolSelectOne().getCCommunityCollege())) {
			d5.setEducationProgramStatus("224870001");
		} else if ("T".equals(staff.getC5DaytimeActivitiesHowDoYouSpendYourDayDoYouHaveEnoughToDo().getCAreYouCurrentlyInSchoolSelectOne().getCUniversity())) {
			d5.setEducationProgramStatus("224871002");
		} else if ("T".equals(staff.getC5DaytimeActivitiesHowDoYouSpendYourDayDoYouHaveEnoughToDo().getCAreYouCurrentlyInSchoolSelectOne().getCOther())) {
			d5.setEducationProgramStatus("410515003");
		} else if ("T".equals(staff.getC5DaytimeActivitiesHowDoYouSpendYourDayDoYouHaveEnoughToDo().getCWhatIsYourCurrentEmploymentStatusSelectOne().getCUnknown())) {
			d5.setEducationProgramStatus("261665006");
		} else {
			d5.setEducationProgramStatus("CDA");
		}

		RiskUnemploymentList rul = of.createRiskUnemploymentList();
		d5.setRiskUnemploymentList(rul);

		if ("T".equals(staff.getC5DaytimeActivitiesHowDoYouSpendYourDayDoYouHaveEnoughToDo().getCAreYouAtRiskOfUnemploymentOrDisrupedEducationCheckAllThatApply().getCDifficultyInGettingToWorkSchoolOnTime())) {
			rul.getRiskUnemployment().add("UDER-01");
		}
		if ("T".equals(staff.getC5DaytimeActivitiesHowDoYouSpendYourDayDoYouHaveEnoughToDo().getCAreYouAtRiskOfUnemploymentOrDisrupedEducationCheckAllThatApply().getCProblemsDifficultyInWorkSchool())) {
			rul.getRiskUnemployment().add("UDER-02");
		}
		if ("T".equals(staff.getC5DaytimeActivitiesHowDoYouSpendYourDayDoYouHaveEnoughToDo().getCAreYouAtRiskOfUnemploymentOrDisrupedEducationCheckAllThatApply().getCLookingToQuitWorkSchool())) {
			rul.getRiskUnemployment().add("UDER-03");
		}
		if ("T".equals(staff.getC5DaytimeActivitiesHowDoYouSpendYourDayDoYouHaveEnoughToDo().getCAreYouAtRiskOfUnemploymentOrDisrupedEducationCheckAllThatApply().getCFrequentChangesInWorkSchool())) {
			rul.getRiskUnemployment().add("UDER-04");
		}
		if ("T".equals(staff.getC5DaytimeActivitiesHowDoYouSpendYourDayDoYouHaveEnoughToDo().getCAreYouAtRiskOfUnemploymentOrDisrupedEducationCheckAllThatApply().getCNoneOrNotApplicable())) {
			rul.getRiskUnemployment().add("UDER-05");
		}
		if ("T".equals(staff.getC5DaytimeActivitiesHowDoYouSpendYourDayDoYouHaveEnoughToDo().getCAreYouAtRiskOfUnemploymentOrDisrupedEducationCheckAllThatApply().getCUnknown())) {
			rul.getRiskUnemployment().add("UDER-06");
		}
		if ("T".equals(staff.getC5DaytimeActivitiesHowDoYouSpendYourDayDoYouHaveEnoughToDo().getCAreYouAtRiskOfUnemploymentOrDisrupedEducationCheckAllThatApply().getCClientDeclinedToAnswer())) {
			rul.getRiskUnemployment().add("UDER-07");
		}


		Domain d6 = createCommonDomain(of, "06-physical health",
			staff.getC6PhysicalHealthHowWellDoYouFeelPhysicallyAreYouGettingAnyTreatmentForPhysical().getC1DoesThePersonHaveAnyPhysicalDisabilityOrAnyPhysicalIllnessIfRated0Or9Ski(),
			client.getCQuestions().getC6PhysicalHealthHowWellDoYouFeelPhysically().getAnswerGroup(),
			staff.getC6PhysicalHealthHowWellDoYouFeelPhysicallyAreYouGettingAnyTreatmentForPhysical().getC2HowMuchHelpDoesThePersonReceiveFromFriendsOrRelativesForPhysicalHealthProblems(),
			staff.getC6PhysicalHealthHowWellDoYouFeelPhysicallyAreYouGettingAnyTreatmentForPhysical().getC3AHowMuchHelpDoesThePersonReceiveFromLocalServicesForPhysicalHealthProblems(),
			staff.getC6PhysicalHealthHowWellDoYouFeelPhysicallyAreYouGettingAnyTreatmentForPhysical().getC3BHowMuchHelpDoesThePersonNeedFromLocalServicesForPhysicalHealthProblems());
		ods.getDomain().add(d6);

		MedicalConditionList mcl = of.createMedicalConditionList();
		d6.setMedicalConditionList(mcl);

		CMedicalConditionsCheckAllThatApply medicalConditionsCheckAllThatApply = staff.getC6PhysicalHealthHowWellDoYouFeelPhysicallyAreYouGettingAnyTreatmentForPhysical().getCMedicalConditionsCheckAllThatApply();
		if ("T".equals(medicalConditionsCheckAllThatApply.getCAcquiredBrainInjuryABI())) {
			mcl.getMedicalCondition().add("127294003");
		}
		if ("T".equals(medicalConditionsCheckAllThatApply.getCArthritis())) {
			mcl.getMedicalCondition().add("3723001");
		}
		if ("T".equals(medicalConditionsCheckAllThatApply.getCAutism())) {
			mcl.getMedicalCondition().add("408856003");
		}

		mcl.setAutismDetail(null);
		List<Object> _mcl = medicalConditionsCheckAllThatApply.getCOtherOrCSpecifyOrCBreathingProblems();
		for (Object obj : _mcl) {
			try {
				JAXBElement<?> e = (JAXBElement<?>) obj;
				if ("CSpecify".equals(e.getName().toString())) {
					if (mcl.getAutismDetail() == null) {
						mcl.setAutismDetail(e.getValue().toString());
					} else {
						mcl.setOtherDetail(e.getValue().toString());
					}
				}
				if ("T".equals(e.getValue().toString())) {
					if ("CBreathing_problems".equals(e.getName().toString())) {
						mcl.getMedicalCondition().add("386813002");
					}
					if ("CCancer".equals(e.getName().toString())) {
						mcl.getMedicalCondition().add("363346000");
					}
					if ("CCirrhosis".equals(e.getName().toString())) {
						mcl.getMedicalCondition().add("19943007");
					}
					if ("CCommunicable_health_disease".equals(e.getName().toString())) {
						mcl.getMedicalCondition().add("191415002");//Communicable health disease
					}
					if ("CDiabetes_Type_1".equals(e.getName().toString())) {
						mcl.getMedicalCondition().add("46635009");//Diabetes Type 1
					}
					if ("CDiabetes_Type_2".equals(e.getName().toString())) {
						mcl.getMedicalCondition().add("44054006");//Diabetes Type 2
					}
					if ("CDiabetes_Type_3".equals(e.getName().toString())) {
						mcl.getMedicalCondition().add("359939009");//Diabetes Type 3
					}
//					if ("C".equals(e.getName().toString())) {
//						mcl.getMedicalCondition().add("73211009");//Diabetes Other
//					}
					if ("CEating_disorder".equals(e.getName().toString())) {
						mcl.getMedicalCondition().add("72366004");//Eating disorder
					}
					if ("CEpilepsy".equals(e.getName().toString())) {
						mcl.getMedicalCondition().add("84757009");//Epilepsy
					}
					if ("CHearing_Impairment".equals(e.getName().toString())) {
						mcl.getMedicalCondition().add("15188001");//Hearing impairment
					}
					if ("CHeart_Condition".equals(e.getName().toString())) {
						mcl.getMedicalCondition().add("301095005");//Heart condition
					}
					if ("CHepatitis_A".equals(e.getName().toString())) {
						mcl.getMedicalCondition().add("40468003");//Hepatitis A
					}
					if ("CHepatitis_B".equals(e.getName().toString())) {
						mcl.getMedicalCondition().add("66071002");//Hepatitis B
					}
					if ("CHepatitis_C".equals(e.getName().toString())) {
						mcl.getMedicalCondition().add("50711007");//Hepatitis C
					}
					if ("CHIV".equals(e.getName().toString())) {
						mcl.getMedicalCondition().add("86406008");//HIV
					}
					if ("CHigh_blood_pressure".equals(e.getName().toString())) {
						mcl.getMedicalCondition().add("38341003");//High blood pressure
					}
					if ("CHigh_cholesterol".equals(e.getName().toString())) {
						mcl.getMedicalCondition().add("13644009");//High cholesterol
					}
					if ("CIntellectual_disability".equals(e.getName().toString())) {
						mcl.getMedicalCondition().add("228156007");//Intellectual disability
					}
					if ("CLow_blood_pressure".equals(e.getName().toString())) {
						mcl.getMedicalCondition().add("45007003");//Low blood pressure
					}
					if ("CMRSA__C_Difficile".equals(e.getName().toString())) {
						mcl.getMedicalCondition().add("282028001");//MRSA, C Difficile
					}
					if ("CObesity".equals(e.getName().toString())) {
						mcl.getMedicalCondition().add("414916001");//Obesity
					}
					if ("COsteoporosis".equals(e.getName().toString())) {
						mcl.getMedicalCondition().add("64859006");//Osteoporosis
					}
					if ("CPregnancy".equals(e.getName().toString())) {
						mcl.getMedicalCondition().add("118185001");//Pregnancy
					}
					if ("CSeizure".equals(e.getName().toString())) {
						mcl.getMedicalCondition().add("128613002");//Seizure
					}
					if ("CSexually_Transmitted_Disease__STD_".equals(e.getName().toString())) {
						mcl.getMedicalCondition().add("8098009");//Sexually Transmitted Disease (STD)
					}
					if ("CSkin_conditions".equals(e.getName().toString())) {
						mcl.getMedicalCondition().add("95320005");//Skin conditions
					}
					if ("CStroke".equals(e.getName().toString())) {
						mcl.getMedicalCondition().add("230690007");//Stroke
					}
					if ("CThyroid".equals(e.getName().toString())) {
						mcl.getMedicalCondition().add("14304000");//Thyroid
					}
					if ("CVision_impairment".equals(e.getName().toString())) {
						mcl.getMedicalCondition().add("397540003");//Vision impairment
					}
					if ("COther".equals(e.getName().toString())) {
						mcl.getMedicalCondition().add("410515003");//Other
					}
				}
			} catch (ClassCastException e) {
				continue;
			}
		}
		if ("T".equals(medicalConditionsCheckAllThatApply.getCUnknown())) {
			mcl.getMedicalCondition().add("261665006");
		}
		if ("T".equals(medicalConditionsCheckAllThatApply.getCClientDeclinedToAnswer())) {
			mcl.getMedicalCondition().add("CDA");
		}


		if ("T".equals(staff.getC6PhysicalHealthHowWellDoYouFeelPhysicallyAreYouGettingAnyTreatmentForPhysical().getCDoYouHaveAnyConcernsAboutYourPhysicalHealth().getCYes())) {
			d6.setPhysicalHealthConcern("TRUE");
		} else if ("T".equals(staff.getC6PhysicalHealthHowWellDoYouFeelPhysicallyAreYouGettingAnyTreatmentForPhysical().getCDoYouHaveAnyConcernsAboutYourPhysicalHealth().getCNo())) {
			d6.setPhysicalHealthConcern("FALSE");
		} else if ("T".equals(staff.getC6PhysicalHealthHowWellDoYouFeelPhysicallyAreYouGettingAnyTreatmentForPhysical().getCDoYouHaveAnyConcernsAboutYourPhysicalHealth().getCUnknown())) {
			d6.setPhysicalHealthConcern("UNK");
		} else if ("T".equals(staff.getC6PhysicalHealthHowWellDoYouFeelPhysicallyAreYouGettingAnyTreatmentForPhysical().getCDoYouHaveAnyConcernsAboutYourPhysicalHealth().getCClientDeclinedToAnswer())) {
			d6.setPhysicalHealthConcern("CDA");
		}


		ConcernAreaList cal = of.createConcernAreaList();
		d6.setConcernAreaList(cal);

		CIfYesPleaseIndicateTheAreasWhereYouHaveConcernsCheckAllThatApply ifYesPleaseIndicateTheAreasWhereYouHaveConcernsCheckAllThatApply = staff.getC6PhysicalHealthHowWellDoYouFeelPhysicallyAreYouGettingAnyTreatmentForPhysical().getCIfYesPleaseIndicateTheAreasWhereYouHaveConcernsCheckAllThatApply();
		if ("T".equals(ifYesPleaseIndicateTheAreasWhereYouHaveConcernsCheckAllThatApply.getCHeadAndNeck())) {
			cal.getConcernArea().add("118254002");
		}
		if ("T".equals(ifYesPleaseIndicateTheAreasWhereYouHaveConcernsCheckAllThatApply.getCChest())) {
			cal.getConcernArea().add("279084009");
		}
		if ("T".equals(ifYesPleaseIndicateTheAreasWhereYouHaveConcernsCheckAllThatApply.getCAbdomen())) {
			cal.getConcernArea().add("119415007");
		}
		if ("T".equals(ifYesPleaseIndicateTheAreasWhereYouHaveConcernsCheckAllThatApply.getCExtremitiesArmsLegsHandsFeet())) {
			cal.getConcernArea().add("302293008");
		}
		if ("T".equals(ifYesPleaseIndicateTheAreasWhereYouHaveConcernsCheckAllThatApply.getCGenitalUrinary())) {
			cal.getConcernArea().add("300479008");
		}
		if ("T".equals(ifYesPleaseIndicateTheAreasWhereYouHaveConcernsCheckAllThatApply.getCSkin())) {
			cal.getConcernArea().add("106076001");
		}
		if ("T".equals(ifYesPleaseIndicateTheAreasWhereYouHaveConcernsCheckAllThatApply.getCJoints())) {
			cal.getConcernArea().add("118952005");
		}
		if ("T".equals(ifYesPleaseIndicateTheAreasWhereYouHaveConcernsCheckAllThatApply.getCMobility())) {
			cal.getConcernArea().add("365092005");
		}
		if ("T".equals(ifYesPleaseIndicateTheAreasWhereYouHaveConcernsCheckAllThatApply.getCNeurological())) {
			cal.getConcernArea().add("102957003");
		}
		if ("T".equals(ifYesPleaseIndicateTheAreasWhereYouHaveConcernsCheckAllThatApply.getCHearing())) {
			cal.getConcernArea().add("118230007");
		}
		if ("T".equals(ifYesPleaseIndicateTheAreasWhereYouHaveConcernsCheckAllThatApply.getCVision())) {
			cal.getConcernArea().add("118235002");
		}
		if ("T".equals(ifYesPleaseIndicateTheAreasWhereYouHaveConcernsCheckAllThatApply.getCOther())) {
			cal.getConcernArea().add("410515003");
		}

		cal.setOtherConcernArea(staff.getC6PhysicalHealthHowWellDoYouFeelPhysicallyAreYouGettingAnyTreatmentForPhysical().getCMedicationsAdditionalInformation().getC());


		MedicationList ml = of.createMedicationList();
		d6.setMedicationList(ml);

		List<JAXBElement<String>> medicationOrC3MedicationOrC4Medication = staff.getC6PhysicalHealthHowWellDoYouFeelPhysicallyAreYouGettingAnyTreatmentForPhysical().getCListOfAllCurrentMedicationsIncludingPrescribedAndAlternativeOverTheCounterMedicati().getC2MedicationOrC3MedicationOrC4Medication();
		String [] triad = new String[3];
		int placed = 0;
		for (JAXBElement<String> e : medicationOrC3MedicationOrC4Medication) {
			if ("CTaken_as_prescribed_".equals(e.getName().toString())) {
				triad[0] = e.getValue().toString();
				placed++;
			}
			if ("CHelp_is_provided_".equals(e.getName().toString())) {
				triad[1] = e.getValue().toString();
				placed++;
			}
			if ("CHelp_is_needed_".equals(e.getName().toString())) {
				triad[2] = e.getValue().toString();
				placed++;
			}
			if (placed == 3) {
				ml.getMedicationDetail().add(createMedicationDetail(of, triad[0], triad[1], triad[2]));
				placed = 0;
			}
		}


		if ("T".equals(staff.getC6PhysicalHealthHowWellDoYouFeelPhysicallyAreYouGettingAnyTreatmentForPhysical().getCReportsSideEffectsSelectOne().getCYes())) {
			d6.setSideEffects("TRUE");
		} else if ("T".equals(staff.getC6PhysicalHealthHowWellDoYouFeelPhysicallyAreYouGettingAnyTreatmentForPhysical().getCReportsSideEffectsSelectOne().getCNo())) {
			d6.setSideEffects("FALSE");
		} else if ("T".equals(staff.getC6PhysicalHealthHowWellDoYouFeelPhysicallyAreYouGettingAnyTreatmentForPhysical().getCReportsSideEffectsSelectOne().getCUnknown())) {
			d6.setSideEffects("UNK");
		} else if ("T".equals(staff.getC6PhysicalHealthHowWellDoYouFeelPhysicallyAreYouGettingAnyTreatmentForPhysical().getCReportsSideEffectsSelectOne().getCClientDeclinedToAnswer())) {
			d6.setSideEffects("CDA");
		}

		if ("T".equals(staff.getC6PhysicalHealthHowWellDoYouFeelPhysicallyAreYouGettingAnyTreatmentForPhysical().getCDoTheseSideEffectsAffectYourDailyLivingSelectOne().getCYes())) {
			d6.setDailyLivingAffected("TRUE");
		} else if ("T".equals(staff.getC6PhysicalHealthHowWellDoYouFeelPhysicallyAreYouGettingAnyTreatmentForPhysical().getCDoTheseSideEffectsAffectYourDailyLivingSelectOne().getCNo())) {
			d6.setDailyLivingAffected("FALSE");
		} else if ("T".equals(staff.getC6PhysicalHealthHowWellDoYouFeelPhysicallyAreYouGettingAnyTreatmentForPhysical().getCDoTheseSideEffectsAffectYourDailyLivingSelectOne().getCUnknown())) {
			d6.setDailyLivingAffected("UNK");
		} else if ("T".equals(staff.getC6PhysicalHealthHowWellDoYouFeelPhysicallyAreYouGettingAnyTreatmentForPhysical().getCDoTheseSideEffectsAffectYourDailyLivingSelectOne().getCClientDeclinedToAnswer())) {
			d6.setDailyLivingAffected("CDA");
		}


		SideEffectsDetailList sedl = of.createSideEffectsDetailList();
		d6.setSideEffectsDetailList(sedl);

		List<Object> sideEffectsDesc = staff.getC6PhysicalHealthHowWellDoYouFeelPhysicallyAreYouGettingAnyTreatmentForPhysical().getCDescriptionOfSideEffectsCheckAllThatApply().getContent();
		for (Object obj : sideEffectsDesc) {
			try {
				JAXBElement<?> e = (JAXBElement<?>) obj;
				if ("COther_".equals(e.getName().toString())) {
					sedl.setOtherSideEffectsDetail(e.getValue().toString());
				}
				if ("T".equals(e.getValue().toString())) {
					if ("CNone".equals(e.getName().toString())) {//None
						sedl.getSideEffectsDetail().add("410516002");
					}
					if ("Cblurred_dimmed_vision".equals(e.getName().toString())) {//Blurred/ dimmed vision
						sedl.getSideEffectsDetail().add("246636008");
					}
					if ("CChanges_in_appetite".equals(e.getName().toString())) {//Changes in appetite
						sedl.getSideEffectsDetail().add("249473004");
					}
					if ("CDizziness_spinning".equals(e.getName().toString())) {//Dizziness/ spinning
						sedl.getSideEffectsDetail().add("404640003");
					}
					if ("CDrowsiness_sedation".equals(e.getName().toString())) {//Drowsiness/ sedation
						sedl.getSideEffectsDetail().add("271782001");
					}
					if ("CDry_mouth".equals(e.getName().toString())) {//Dry mouth
						sedl.getSideEffectsDetail().add("87715008");
					}
					if ("CFatigue_weakness".equals(e.getName().toString())) {//Fatigue/ weakness
						sedl.getSideEffectsDetail().add("84229001");
					}
					if ("CFast_heart_beat".equals(e.getName().toString())) {//Fast heart beat
						sedl.getSideEffectsDetail().add("6285003");
					}
					if ("CGastrointestinal_distress".equals(e.getName().toString())) {//Gastrointestinal distress
						sedl.getSideEffectsDetail().add("53619000");
					}
					if ("CHeadache".equals(e.getName().toString())) {//Headache
						sedl.getSideEffectsDetail().add("25064002");
					}
					if ("CInsomnia".equals(e.getName().toString())) {//Insomnia
						sedl.getSideEffectsDetail().add("193462001");
					}
					if ("CMenstrual_changes".equals(e.getName().toString())) {//Menstrual changes
						sedl.getSideEffectsDetail().add("106002000");
					}
					if ("CMilky_discharge_from_breasts".equals(e.getName().toString())) {//Milky discharge from breasts
						sedl.getSideEffectsDetail().add("271941007");
					}
					if ("CMuscle_spasms".equals(e.getName().toString())) {//Muscle spasms
						sedl.getSideEffectsDetail().add("106030000");
					}
					if ("CNumbness_tingling".equals(e.getName().toString())) {//Numbness/ tingling
						sedl.getSideEffectsDetail().add("44077006");
					}
					if ("CRestlessness".equals(e.getName().toString())) {//Restlessness
						sedl.getSideEffectsDetail().add("24199005");
					}
					if ("CSexual_disturbance".equals(e.getName().toString())) {//Sexual disturbance
						sedl.getSideEffectsDetail().add("106142007");
					}
					if ("CTremors_rigidity_balance_problems".equals(e.getName().toString())) {//Tremors/ rigidity/ balance problems
						sedl.getSideEffectsDetail().add("26079004");
					}
					if ("CWeight_gain".equals(e.getName().toString())) {//Weight gain
						sedl.getSideEffectsDetail().add("8943002");
					}
					if ("COther_".equals(e.getName().toString())) {//Other
						sedl.getSideEffectsDetail().add("410515003");
					}
				}
			} catch (ClassCastException e) {
				continue;
			}
		}



		Domain d7 = createCommonDomain(of, "07-psychotic symptoms",
			staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getC1DoesThePersonHaveAnyPsychoticSymptoms(),
			client.getCQuestions().getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughts().getAnswerGroup(),
			staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getC2HowMuchHelpDoesThePersonReceiveFromFriendsOrRelativesForThesePsychoticSymptoms(),
			staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getC3AHowMuchHelpDoesThePersonReceiveFromLocalServicesForThesePsychoticSymptoms(),
			staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getC3BHowMuchHelpDoesThePersonNeedFromLocalServicesForThesePsychoticSymptoms());
		ods.getDomain().add(d7);

		List<Object> communityTreatmentOrderOrCPsychiatricHistory = staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getCCommunityTreatmentOrderOrCPsychiatricHistory();
		CPsychiatricHistory history = null;
		CCommunityTreatmentOrder treatment = null;
		for (Object obj : communityTreatmentOrderOrCPsychiatricHistory) {
			if (obj instanceof CPsychiatricHistory) {
				history = (CPsychiatricHistory) obj;
			} else if (obj instanceof CCommunityTreatmentOrder) {
				treatment = (CCommunityTreatmentOrder) obj;
			}
		}

		if ("Yes".equals(history.getCHaveYouBeenHospitalizedDueToYourMentalHealthDuringThePastTwoYears())) {
			d7.setHospitalizedPastTwoYears("TRUE");
			d7.setTotalAdmissions(history.getCIfYesTotalNumberOfAdmissionLastTwoYears());
			d7.setTotalHospitalDays(history.getCIfYesTotalNumberOfHospitalizationDaysLastTwoYears());
		} else if ("No".equals(history.getCHaveYouBeenHospitalizedDueToYourMentalHealthDuringThePastTwoYears())) {
			d7.setHospitalizedPastTwoYears("FALSE");
		} else if ("Unknown".equals(history.getCHaveYouBeenHospitalizedDueToYourMentalHealthDuringThePastTwoYears())) {
			d7.setHospitalizedPastTwoYears("UNK");
		} else if ("Client declined to answer".equals(history.getCHaveYouBeenHospitalizedDueToYourMentalHealthDuringThePastTwoYears())) {
			d7.setHospitalizedPastTwoYears("CDA");
		}

		if ("T".equals(treatment.getCIssuedCTO())) {
			d7.setCommunityTreatOrder("015-01");
		} else if ("T".equals(treatment.getCNoCTO())) {
			d7.setCommunityTreatOrder("015-02");
		} else if ("T".equals(treatment.getCUnknown())) {
			d7.setCommunityTreatOrder("015-03");
		} else if ("T".equals(treatment.getCClientDeclinedToAnswer())) {
			d7.setCommunityTreatOrder("015-04");
		}
		
		SymptomList sl = of.createSymptomList();
		d7.setSymptomList(sl);

		if ("T".equals(staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getCSymptomChecklist().getCNone())) {
			sl.getSymptom().add("410516002");
		}
		if ("T".equals(staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getCSymptomChecklist().getCAbnormalAffect())) {
			sl.getSymptom().add("416383008");
		}
		if ("T".equals(staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getCSymptomChecklist().getCAbnormalThoughtProcessForm())) {
			sl.getSymptom().add("78633001");
		}
		if ("T".equals(staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getCSymptomChecklist().getCAnger())) {
			sl.getSymptom().add("75408008");
		}
		if ("T".equals(staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getCSymptomChecklist().getCAnxiety())) {
			sl.getSymptom().add("48694002");
		}
		if ("T".equals(staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getCSymptomChecklist().getCCommandHallucinations())) {
			sl.getSymptom().add("78595002");
		}
		if ("T".equals(staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getCSymptomChecklist().getCCompulsiveBehaviour())) {
			sl.getSymptom().add("12479006");
		}
		if ("T".equals(staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getCSymptomChecklist().getCDecreasedEnergy())) {
			sl.getSymptom().add("248274002");
		}
		if ("T".equals(staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getCSymptomChecklist().getCDelusions())) {
			sl.getSymptom().add("2073000");
		}
		if ("T".equals(staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getCSymptomChecklist().getCEpisodesOfPanic())) {
			sl.getSymptom().add("225624000");
		}
		if ("T".equals(staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getCSymptomChecklist().getCFears())) {
			sl.getSymptom().add("1402001");
		}
		if ("T".equals(staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getCSymptomChecklist().getCGuiltShame())) {
			sl.getSymptom().add("7571003");
		}
		if ("T".equals(staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getCSymptomChecklist().getCHallucinations())) {
			sl.getSymptom().add("7011001");
		}
		if ("T".equals(staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getCSymptomChecklist().getCHopelessness())) {
			sl.getSymptom().add("307077003");
		}
		if ("T".equals(staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getCSymptomChecklist().getCHygiene())) {
			sl.getSymptom().add("410428008");
		}
		if ("T".equals(staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getCSymptomChecklist().getCInabilityTOExperienceJoyPleasure())) {
			sl.getSymptom().add("28669007");
		}
		if ("T".equals(staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getCSymptomChecklist().getCInflatedSelfWorth())) {
			sl.getSymptom().add("247783009");
		}
		if ("T".equals(staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getCSymptomChecklist().getCIntrusiveThoughts())) {
			sl.getSymptom().add("225445003");
		}
		if ("T".equals(staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getCSymptomChecklist().getCIrritability())) {
			sl.getSymptom().add("55929007");
		}
		if ("T".equals(staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getCSymptomChecklist().getCObsessiveThoughts())) {
			sl.getSymptom().add("67698009");
		}
		if ("T".equals(staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getCSymptomChecklist().getCPhobias())) {
			sl.getSymptom().add("386808001");
		}
		if ("T".equals(staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getCSymptomChecklist().getCPressuredSpeech())) {
			sl.getSymptom().add("53890003");
		}
		if ("T".equals(staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getCSymptomChecklist().getCRacingThoughts())) {
			sl.getSymptom().add("285303006");
		}
		if ("T".equals(staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getCSymptomChecklist().getCRapidMoodChanges())) {
			sl.getSymptom().add("18963009");
		}
		if ("T".equals(staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getCSymptomChecklist().getCRelivingTraumaticMemories())) {
			sl.getSymptom().add("285216009");
		}
		if ("T".equals(staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getCSymptomChecklist().getCSelfDeprecation())) {
			sl.getSymptom().add("247892001");
		}
		if ("T".equals(staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getCSymptomChecklist().getCSleepProblems())) {
			sl.getSymptom().add("301345002");
		}
		if ("T".equals(staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getCSymptomChecklist().getCTearfulness())) {
			sl.getSymptom().add("271951008");
		}
		if ("T".equals(staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getCSymptomChecklist().getCUnusualOrAbnormalPhysicalMovements())) {
			sl.getSymptom().add("225606002");
		}
		if ("T".equals(staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getCSymptomChecklist().getCUnknown())) {
			sl.getSymptom().add("261665006");
		}
		if ("T".equals(staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getCSymptomChecklist().getCOther())) {
			sl.getSymptom().add("410515003");
		}

		sl.setOtherSymptom(staff.getC7PsychoticSymptomsDoYouEverHearVoicesOrHaveProblemsWithYourThoughtsAreYouOn().getCSymptomChecklist().getCSpecify());



		Domain d8 = createCommonDomain(of, "08-condition and treatment",
			staff.getC8InformationOnConditionAndTreatmentHaveYouBeenGivenClearInformationAboutYourMed().getC1HasThePersonHadClearVerbalOrWrittenInformationAboutConditionAndTreatmentIfRa(),
			client.getCQuestions().getC8InformationOnConditionAndTreatmentHaveYouBeenGivenClearInformationAboutYourMed().getAnswerGroup(),
			staff.getC8InformationOnConditionAndTreatmentHaveYouBeenGivenClearInformationAboutYourMed().getC2HowMuchHelpDoesThePersonReceiveFromFriendsOrRelativesInObtainingSuchInformatio(),
			staff.getC8InformationOnConditionAndTreatmentHaveYouBeenGivenClearInformationAboutYourMed().getC3AHowMuchHelpDoesThePersonReceiveFromLocalServicesInObtainingSuchInformation(),
			staff.getC8InformationOnConditionAndTreatmentHaveYouBeenGivenClearInformationAboutYourMed().getC3BHowMuchHelpDoesThePersonNeedFromLocalServicesInObtainingSuchInformation());
		ods.getDomain().add(d8);

		DiagnosticList dl = of.createDiagnosticList();
		d8.setDiagnosticList(dl);

		if ("T".equals(staff.getC8InformationOnConditionAndTreatmentHaveYouBeenGivenClearInformationAboutYourMed().getCDiagnosticCategoriesCheckAllThatApply().getCAdjustmentDisorders())) {
			dl.getDiagnostic().add("17226007");
		}
		if ("T".equals(staff.getC8InformationOnConditionAndTreatmentHaveYouBeenGivenClearInformationAboutYourMed().getCDiagnosticCategoriesCheckAllThatApply().getCAnxietyDisorder())) {
			dl.getDiagnostic().add("197480006");
		}
		if ("T".equals(staff.getC8InformationOnConditionAndTreatmentHaveYouBeenGivenClearInformationAboutYourMed().getCDiagnosticCategoriesCheckAllThatApply().getCDeliriumDementiaAndAmnesticAndCognitiveDisorders())) {
			dl.getDiagnostic().add("2776000");
		}
		if ("T".equals(staff.getC8InformationOnConditionAndTreatmentHaveYouBeenGivenClearInformationAboutYourMed().getCDiagnosticCategoriesCheckAllThatApply().getCDisorderOfChildhoodAdolescence())) {
			dl.getDiagnostic().add("DCA");
		}
		if ("T".equals(staff.getC8InformationOnConditionAndTreatmentHaveYouBeenGivenClearInformationAboutYourMed().getCDiagnosticCategoriesCheckAllThatApply().getCDissociativeDisorders())) {
			dl.getDiagnostic().add("44376007");
		}
		if ("T".equals(staff.getC8InformationOnConditionAndTreatmentHaveYouBeenGivenClearInformationAboutYourMed().getCDiagnosticCategoriesCheckAllThatApply().getCEatingDisorders())) {
			dl.getDiagnostic().add("72366004");
		}
		if ("T".equals(staff.getC8InformationOnConditionAndTreatmentHaveYouBeenGivenClearInformationAboutYourMed().getCDiagnosticCategoriesCheckAllThatApply().getCFactitiousDisorders())) {
			dl.getDiagnostic().add("50705009");
		}
		if ("T".equals(staff.getC8InformationOnConditionAndTreatmentHaveYouBeenGivenClearInformationAboutYourMed().getCDiagnosticCategoriesCheckAllThatApply().getCImpulseControlDisordersNotElsewhereClassified())) {
			dl.getDiagnostic().add("66347000");
		}
		if ("T".equals(staff.getC8InformationOnConditionAndTreatmentHaveYouBeenGivenClearInformationAboutYourMed().getCDiagnosticCategoriesCheckAllThatApply().getCMentalDisordersDueToGeneralMedicalConditions())) {
			dl.getDiagnostic().add("MDGMC");
		}
		if ("T".equals(staff.getC8InformationOnConditionAndTreatmentHaveYouBeenGivenClearInformationAboutYourMed().getCDiagnosticCategoriesCheckAllThatApply().getCMoodDisorder())) {
			dl.getDiagnostic().add("46206005");
		}
		if ("T".equals(staff.getC8InformationOnConditionAndTreatmentHaveYouBeenGivenClearInformationAboutYourMed().getCDiagnosticCategoriesCheckAllThatApply().getCPersonalityDisorders())) {
			dl.getDiagnostic().add("33449004");
		}
		if ("T".equals(staff.getC8InformationOnConditionAndTreatmentHaveYouBeenGivenClearInformationAboutYourMed().getCDiagnosticCategoriesCheckAllThatApply().getCSchizopherniaAndOtherPsychoticDisorders())) {
			dl.getDiagnostic().add("58214004");
		}
		if ("T".equals(staff.getC8InformationOnConditionAndTreatmentHaveYouBeenGivenClearInformationAboutYourMed().getCDiagnosticCategoriesCheckAllThatApply().getCSexualAndGenderIdentityDisorders())) {
			dl.getDiagnostic().add("39898005");
		}
		if ("T".equals(staff.getC8InformationOnConditionAndTreatmentHaveYouBeenGivenClearInformationAboutYourMed().getCDiagnosticCategoriesCheckAllThatApply().getCSleepDisorders())) {
			dl.getDiagnostic().add("31297008");
		}
		if ("T".equals(staff.getC8InformationOnConditionAndTreatmentHaveYouBeenGivenClearInformationAboutYourMed().getCDiagnosticCategoriesCheckAllThatApply().getCSomatoformDisorders())) {
			dl.getDiagnostic().add("87858002");
		}
		if ("T".equals(staff.getC8InformationOnConditionAndTreatmentHaveYouBeenGivenClearInformationAboutYourMed().getCDiagnosticCategoriesCheckAllThatApply().getCSubstanceRelatedDisorders())) {
			dl.getDiagnostic().add("SRDDH");
		}
//		if ("T".equals(staff.getC8InformationOnConditionAndTreatmentHaveYouBeenGivenClearInformationAboutYourMed().getCDiagnosticCategoriesCheckAllThatApply().getCDevelopmentalHandicap())) {
//			dl.getDiagnostic().add("DH");
//		}
//		if ("T".equals(staff.getC8InformationOnConditionAndTreatmentHaveYouBeenGivenClearInformationAboutYourMed().getCDiagnosticCategoriesCheckAllThatApply().getCUnknown)) {
//			dl.getDiagnostic().add("261665006");
//		}
//		if ("T".equals(staff.getC8InformationOnConditionAndTreatmentHaveYouBeenGivenClearInformationAboutYourMed().getCDiagnosticCategoriesCheckAllThatApply().getCClientDeclinedToAnswer())) {
//			dl.getDiagnostic().add("CDA");
//		}

		OtherIllnessList oil = of.createOtherIllnessList();
		d8.setOtherIllnessList(oil);

		if ("T".equals(staff.getC8InformationOnConditionAndTreatmentHaveYouBeenGivenClearInformationAboutYourMed().getCOtherIllnessInformationCheckAllThatApply().getCConcurrentDisorderSubstanceAbuse())) {
			oil.getOtherIllness().add("016A-01");
		}
		if ("T".equals(staff.getC8InformationOnConditionAndTreatmentHaveYouBeenGivenClearInformationAboutYourMed().getCOtherIllnessInformationCheckAllThatApply().getCDualDiagnosisDevelopmentalDisability())) {
			oil.getOtherIllness().add("016A-02");
		}
		if ("T".equals(staff.getC8InformationOnConditionAndTreatmentHaveYouBeenGivenClearInformationAboutYourMed().getCOtherIllnessInformationCheckAllThatApply().getCOtherChronicIllnessesAndOrPhysicalDisabilities())) {
			oil.getOtherIllness().add("016A-03");
		}



		Domain d9 = createCommonDomain(of, "09-psychological distress",
			staff.getC9PsychologicalDistressHaveYouRecentlyFeltVerySadOrLowHaveYouFeltOverlyAnxious().getC1DoesThePersonSufferFromCurrentPsychologicalDistressIfRated0Or9GoToTheNext(),
			client.getCQuestions().getC9PsychologicalDistressHaveYouRecentlyFeltVerySadOrLow().getAnswerGroup(),
			staff.getC9PsychologicalDistressHaveYouRecentlyFeltVerySadOrLowHaveYouFeltOverlyAnxious().getC2HowMuchHelpDoesThePersonReceiveFromFriendsOrRelativesForThisDistress(),
			staff.getC9PsychologicalDistressHaveYouRecentlyFeltVerySadOrLowHaveYouFeltOverlyAnxious().getC3AHowMuchHelpDoesThePersonReceiveFromLocalServicesForThisDistress(),
			staff.getC9PsychologicalDistressHaveYouRecentlyFeltVerySadOrLowHaveYouFeltOverlyAnxious().getC3BHowMuchHelpDoesThisPersonNeedFromLocalServicesForThisDistress());
		ods.getDomain().add(d9);



		Domain d10 = createCommonDomain(of, "10-safety to self",
			staff.getC10SafetyToSelfDoYouEverHaveThoughtsOfHarmingYourselfOrActuallyHarmingYourself().getC1IsThePersonADangerToHimOrHerselfIfRated0Or9SkipQuestions23AndProceed(),
			client.getCQuestions().getC10SafetyToSelfDoYouEverHaveThoughtsOfHarmingYourself().getAnswerGroup(),
			staff.getC10SafetyToSelfDoYouEverHaveThoughtsOfHarmingYourselfOrActuallyHarmingYourself().getC2HowMuchHelpDoesThePersonReceiveFromFriendsOrRelativesToReduceTheRiskOfSelfH(),
			staff.getC10SafetyToSelfDoYouEverHaveThoughtsOfHarmingYourselfOrActuallyHarmingYourself().getC3AHowMuchHelpDoesThePersonReceiveFromLocalServicesToReduceTheRiskOfSelfHarm(),
			staff.getC10SafetyToSelfDoYouEverHaveThoughtsOfHarmingYourselfOrActuallyHarmingYourself().getC3BHowMuchHelpDoesThePersonNeedFromLocalServicesToReduceTheRiskOfSelfHarm());
		ods.getDomain().add(d10);

		if ("T".equals(staff.getC10SafetyToSelfDoYouEverHaveThoughtsOfHarmingYourselfOrActuallyHarmingYourself().getCHaveYouAttemptedSuicideInThePastSelectOne().getCYes())) {
			d10.setSuicideAttempt("TRUE");
		} else if ("T".equals(staff.getC10SafetyToSelfDoYouEverHaveThoughtsOfHarmingYourselfOrActuallyHarmingYourself().getCHaveYouAttemptedSuicideInThePastSelectOne().getCNo())) {
			d10.setSuicideAttempt("FALSE");
		} else if ("T".equals(staff.getC10SafetyToSelfDoYouEverHaveThoughtsOfHarmingYourselfOrActuallyHarmingYourself().getCHaveYouAttemptedSuicideInThePastSelectOne().getCUnknown())) {
			d10.setSuicideAttempt("UNK");
		} else if ("T".equals(staff.getC10SafetyToSelfDoYouEverHaveThoughtsOfHarmingYourselfOrActuallyHarmingYourself().getCHaveYouAttemptedSuicideInThePastSelectOne().getCClientDeclinedToAnswer())) {
			d10.setSuicideAttempt("CDA");
		}

		if ("T".equals(staff.getC10SafetyToSelfDoYouEverHaveThoughtsOfHarmingYourselfOrActuallyHarmingYourself().getCDoYouCurrentlyHaveSuicidalThoughtsSelectOne().getCYes())) {
			d10.setSuicideThoughts("TRUE");
		} else if ("T".equals(staff.getC10SafetyToSelfDoYouEverHaveThoughtsOfHarmingYourselfOrActuallyHarmingYourself().getCDoYouCurrentlyHaveSuicidalThoughtsSelectOne().getCNo())) {
			d10.setSuicideThoughts("FALSE");
		} else if ("T".equals(staff.getC10SafetyToSelfDoYouEverHaveThoughtsOfHarmingYourselfOrActuallyHarmingYourself().getCDoYouCurrentlyHaveSuicidalThoughtsSelectOne().getCUnknown())) {
			d10.setSuicideThoughts("UNK");
		} else if ("T".equals(staff.getC10SafetyToSelfDoYouEverHaveThoughtsOfHarmingYourselfOrActuallyHarmingYourself().getCDoYouCurrentlyHaveSuicidalThoughtsSelectOne().getCClientDeclinedToAnswer())) {
			d10.setSuicideThoughts("CDA");
		}

		if ("T".equals(staff.getC10SafetyToSelfDoYouEverHaveThoughtsOfHarmingYourselfOrActuallyHarmingYourself().getCDoYouHaveAnyConcernsFroYourOwnSafetySelectOne().getCYes())) {
			d10.setSafetyConcernSelf("TRUE");
		} else if ("T".equals(staff.getC10SafetyToSelfDoYouEverHaveThoughtsOfHarmingYourselfOrActuallyHarmingYourself().getCDoYouHaveAnyConcernsFroYourOwnSafetySelectOne().getCNo())) {
			d10.setSafetyConcernSelf("FALSE");
		} else if ("T".equals(staff.getC10SafetyToSelfDoYouEverHaveThoughtsOfHarmingYourselfOrActuallyHarmingYourself().getCDoYouHaveAnyConcernsFroYourOwnSafetySelectOne().getCUnknown())) {
			d10.setSafetyConcernSelf("UNK");
		} else if ("T".equals(staff.getC10SafetyToSelfDoYouEverHaveThoughtsOfHarmingYourselfOrActuallyHarmingYourself().getCDoYouHaveAnyConcernsFroYourOwnSafetySelectOne().getCClientDeclinedToAnswer())) {
			d10.setSafetyConcernSelf("CDA");
		}

		SafetyToSelfRiskList ssrl = of.createSafetyToSelfRiskList();
		d10.setSafetyToSelfRiskList(ssrl);

		if ("T".equals(staff.getC10SafetyToSelfDoYouEverHaveThoughtsOfHarmingYourselfOrActuallyHarmingYourself().getCRisksSelectAllThatApply().getCAbuseNeglect())) {
			ssrl.getSafetyToSelfRisk().add("225915006");
		}
		if ("T".equals(staff.getC10SafetyToSelfDoYouEverHaveThoughtsOfHarmingYourselfOrActuallyHarmingYourself().getCRisksSelectAllThatApply().getCAccidentalSelfHarm())) {
			ssrl.getSafetyToSelfRisk().add("242056005");
		}
		if ("T".equals(staff.getC10SafetyToSelfDoYouEverHaveThoughtsOfHarmingYourselfOrActuallyHarmingYourself().getCRisksSelectAllThatApply().getCDeliberateSelfHarm())) {
			ssrl.getSafetyToSelfRisk().add("401206008");
		}
		if ("T".equals(staff.getC10SafetyToSelfDoYouEverHaveThoughtsOfHarmingYourselfOrActuallyHarmingYourself().getCRisksSelectAllThatApply().getCExploitationRisk())) {
			ssrl.getSafetyToSelfRisk().add("417430008");
		}
		if (staff.getC10SafetyToSelfDoYouEverHaveThoughtsOfHarmingYourselfOrActuallyHarmingYourself().getCRisksSelectAllThatApply().getCOther()!=null &&
			staff.getC10SafetyToSelfDoYouEverHaveThoughtsOfHarmingYourselfOrActuallyHarmingYourself().getCRisksSelectAllThatApply().getCOther().length() > 0) {
			ssrl.getSafetyToSelfRisk().add("410515003");
			ssrl.setOtherSafetyToSelfRisk(staff.getC10SafetyToSelfDoYouEverHaveThoughtsOfHarmingYourselfOrActuallyHarmingYourself().getCRisksSelectAllThatApply().getCOther());
		}



		Domain d11 = createCommonDomain(of, "11-safety to others",
			staff.getC11SafetyToOthersDoYouThinkYouCouldBeADangerToOtherPeopleSSafetyDoYouEver().getC1IsThePersonACurrentOrPotentialRiskToOtherPeopleSSafetyIfRated0Or9GoTo(),
			client.getCQuestions().getC11SafetyToOthersDoYouThinkYouCouldBeADangerToOtherPeopleSSafety().getAnswerGroup(),
			staff.getC11SafetyToOthersDoYouThinkYouCouldBeADangerToOtherPeopleSSafetyDoYouEver().getC2HowMuchHelpDoesThePersonReceiveFromFriendsOrRelativesToReduceTheRiskThatHeO(),
			staff.getC11SafetyToOthersDoYouThinkYouCouldBeADangerToOtherPeopleSSafetyDoYouEver().getC3AHowMuchHelpDoesThePersonReceiveFromLocalServicesToReduceTheRiskThatHeOrShe(),
			staff.getC11SafetyToOthersDoYouThinkYouCouldBeADangerToOtherPeopleSSafetyDoYouEver().getC3BHowMuchHelpDoesThePersonNeedFromLocalServicesToReduceTheRiskThatHeOrSheMi());
		ods.getDomain().add(d11);



		Domain d12 = createCommonDomain(of, "12-alcohol",
			staff.getC12AlcoholDoesDrinkingCauseYouAnyProblemsDoYouWishYouCouldCutDownOnYourDrink().getC1DoesThePersonDrinkExcessivelyOrHaveAProblemControllingHisOrHerDrinkingIfRa(),
			client.getCQuestions().getC12AlcoholDoesDrinkingCauseYouAnyProblems().getAnswerGroup(),
			staff.getC12AlcoholDoesDrinkingCauseYouAnyProblemsDoYouWishYouCouldCutDownOnYourDrink().getC2HowMuchHelpDoesThePersonReceiveFromFriendsOrRelativesForThisDrinking(),
			staff.getC12AlcoholDoesDrinkingCauseYouAnyProblemsDoYouWishYouCouldCutDownOnYourDrink().getC3AHowMuchHelpDoesThePersonReceiveFromLocalServicesForThisDrinking(),
			staff.getC12AlcoholDoesDrinkingCauseYouAnyProblemsDoYouWishYouCouldCutDownOnYourDrink().getC3BHowMuchHelpDoesThePersonNeedFromLocalServicesForThisDrinking());
		ods.getDomain().add(d12);

		DrinkAlcohol da = of.createDrinkAlcohol();
		d12.setDrinkAlcohol(da);

		if (staff.getC12AlcoholDoesDrinkingCauseYouAnyProblemsDoYouWishYouCouldCutDownOnYourDrink().getCHowOftenDoYouDrinkAlcoholIENumberOfDrinks().getCDrinksDaily() != null &&
			staff.getC12AlcoholDoesDrinkingCauseYouAnyProblemsDoYouWishYouCouldCutDownOnYourDrink().getCHowOftenDoYouDrinkAlcoholIENumberOfDrinks().getCDrinksDaily().intValue() > 0) {
			da.setFrequency("1");
			da.setQuantity(staff.getC12AlcoholDoesDrinkingCauseYouAnyProblemsDoYouWishYouCouldCutDownOnYourDrink().getCHowOftenDoYouDrinkAlcoholIENumberOfDrinks().getCDrinksDaily());
		} else if (staff.getC12AlcoholDoesDrinkingCauseYouAnyProblemsDoYouWishYouCouldCutDownOnYourDrink().getCHowOftenDoYouDrinkAlcoholIENumberOfDrinks().getCDrinks23TimesWeekly() != null &&
			staff.getC12AlcoholDoesDrinkingCauseYouAnyProblemsDoYouWishYouCouldCutDownOnYourDrink().getCHowOftenDoYouDrinkAlcoholIENumberOfDrinks().getCDrinks23TimesWeekly().intValue() > 0) {
			da.setFrequency("2");
			da.setQuantity(staff.getC12AlcoholDoesDrinkingCauseYouAnyProblemsDoYouWishYouCouldCutDownOnYourDrink().getCHowOftenDoYouDrinkAlcoholIENumberOfDrinks().getCDrinks23TimesWeekly());
		} else if (staff.getC12AlcoholDoesDrinkingCauseYouAnyProblemsDoYouWishYouCouldCutDownOnYourDrink().getCHowOftenDoYouDrinkAlcoholIENumberOfDrinks().getCDrinksWeekly() != null &&
			staff.getC12AlcoholDoesDrinkingCauseYouAnyProblemsDoYouWishYouCouldCutDownOnYourDrink().getCHowOftenDoYouDrinkAlcoholIENumberOfDrinks().getCDrinksWeekly().intValue() > 0) {
			da.setFrequency("3");
			da.setQuantity(staff.getC12AlcoholDoesDrinkingCauseYouAnyProblemsDoYouWishYouCouldCutDownOnYourDrink().getCHowOftenDoYouDrinkAlcoholIENumberOfDrinks().getCDrinksWeekly());
		} else if (staff.getC12AlcoholDoesDrinkingCauseYouAnyProblemsDoYouWishYouCouldCutDownOnYourDrink().getCHowOftenDoYouDrinkAlcoholIENumberOfDrinks().getCDrinksMonthly() != null &&
			staff.getC12AlcoholDoesDrinkingCauseYouAnyProblemsDoYouWishYouCouldCutDownOnYourDrink().getCHowOftenDoYouDrinkAlcoholIENumberOfDrinks().getCDrinksMonthly().intValue() > 0) {
			da.setFrequency("4");
			da.setQuantity(staff.getC12AlcoholDoesDrinkingCauseYouAnyProblemsDoYouWishYouCouldCutDownOnYourDrink().getCHowOftenDoYouDrinkAlcoholIENumberOfDrinks().getCDrinksMonthly());
		}

		if ("T".equals(staff.getC12AlcoholDoesDrinkingCauseYouAnyProblemsDoYouWishYouCouldCutDownOnYourDrink().getCIndicateTheStageOfChangeClientIsAtOptionalSelectOne().getCPrecontemplation())) {
			d12.setStageOfChangeAlcohol("1");
		} else if ("T".equals(staff.getC12AlcoholDoesDrinkingCauseYouAnyProblemsDoYouWishYouCouldCutDownOnYourDrink().getCIndicateTheStageOfChangeClientIsAtOptionalSelectOne().getCContemplation())) {
			d12.setStageOfChangeAlcohol("2");
		} else if ("T".equals(staff.getC12AlcoholDoesDrinkingCauseYouAnyProblemsDoYouWishYouCouldCutDownOnYourDrink().getCIndicateTheStageOfChangeClientIsAtOptionalSelectOne().getCAction())) {
			d12.setStageOfChangeAlcohol("3");
		} else if ("T".equals(staff.getC12AlcoholDoesDrinkingCauseYouAnyProblemsDoYouWishYouCouldCutDownOnYourDrink().getCIndicateTheStageOfChangeClientIsAtOptionalSelectOne().getCMaintenance())) {
			d12.setStageOfChangeAlcohol("4");
		} else if ("T".equals(staff.getC12AlcoholDoesDrinkingCauseYouAnyProblemsDoYouWishYouCouldCutDownOnYourDrink().getCIndicateTheStageOfChangeClientIsAtOptionalSelectOne().getCRelapsePrevention())) {
			d12.setStageOfChangeAlcohol("5");
		}



		Domain d13 = createCommonDomain(of, "13-drugs",
			staff.getC13DrugsDoYouTakeDrugsThatArenTPrescribedAreThereAnyDrugsYouWouldFindHardTo().getC1DoesThePersonHaveProblemsWithDrugMisuseIfRated0Or9SkipQuestions23AndP(),
			client.getCQuestions().getC13DrugsDoYouTakeAnyDrugsThatArenTPrescribed().getAnswerGroup(),
			staff.getC13DrugsDoYouTakeDrugsThatArenTPrescribedAreThereAnyDrugsYouWouldFindHardTo().getC2HowMuchHelpWithDrugMisuseDoesThePersonReceiveFromFriendsOrRelatives(),
			staff.getC13DrugsDoYouTakeDrugsThatArenTPrescribedAreThereAnyDrugsYouWouldFindHardTo().getC3AHowMuchHelpWithDrugMisuseDoesThePersonReceiveFromLocalServices(),
			staff.getC13DrugsDoYouTakeDrugsThatArenTPrescribedAreThereAnyDrugsYouWouldFindHardTo().getC3BHowMuchHelpWithDrugMisuseDoesThePersonNeedFromLocalServices());
		ods.getDomain().add(d13);

		DrugUseList dul = of.createDrugUseList();
		d13.setDrugUseList(dul);

		CWhichOfTheFollowingDrugsHaveYouUsedCheckAllThatApply whichOfTheFollowing = staff.getC13DrugsDoYouTakeDrugsThatArenTPrescribedAreThereAnyDrugsYouWouldFindHardTo().getCWhichOfTheFollowingDrugsHaveYouUsedCheckAllThatApply();

		if ("T".equals(whichOfTheFollowing.getCHasTheSubstanceBeenInjected().getCPast6Months())) {
			dul.setInjected("5");
		} else if ("T".equals(whichOfTheFollowing.getCHasTheSubstanceBeenInjected().getCEver())) {
			dul.setInjected("6");
		}

		String frequency = null;
		if ("T".equals(whichOfTheFollowing.getCMarijuana().getCPast6Months())) {
			frequency = "5";
		} else if ("T".equals(whichOfTheFollowing.getCMarijuana().getCEver())) {
			frequency = "6";
		}
		if (frequency != null) {
			DrugUse du1 = of.createDrugUse();
			du1.setFrequency(frequency);
			du1.setName("398705004");
			dul.getDrugUse().add(du1);
		}

		frequency = null;
		if ("T".equals(whichOfTheFollowing.getCCocaineCrack().getCPast6Months())) {
			frequency = "5";
		} else if ("T".equals(whichOfTheFollowing.getCCocaineCrack().getCEver())) {
			frequency = "6";
		}
		if (frequency != null) {
			DrugUse du1 = of.createDrugUse();
			du1.setFrequency(frequency);
			du1.setName("288453002");
			dul.getDrugUse().add(du1);
		}

		frequency = null;
		if ("T".equals(whichOfTheFollowing.getCHallucinogensEGLSDPCP().getCPast6Months())) {
			frequency = "5";
		} else if ("T".equals(whichOfTheFollowing.getCHallucinogensEGLSDPCP().getCEver())) {
			frequency = "6";
		}
		if (frequency != null) {
			DrugUse du1 = of.createDrugUse();
			du1.setFrequency(frequency);
			du1.setName("229005006");
			dul.getDrugUse().add(du1);
		}

		frequency = null;
		if ("T".equals(whichOfTheFollowing.getCStimulantsEGAmphetamines().getCPast6Months())) {
			frequency = "5";
		} else if ("T".equals(whichOfTheFollowing.getCStimulantsEGAmphetamines().getCEver())) {
			frequency = "6";
		}
		if (frequency != null) {
			DrugUse du1 = of.createDrugUse();
			du1.setFrequency(frequency);
			du1.setName("226059005");
			dul.getDrugUse().add(du1);
		}

		frequency = null;
		if ("T".equals(whichOfTheFollowing.getCOpiatesEGHeroin().getCPast6Months())) {
			frequency = "5";
		} else if ("T".equals(whichOfTheFollowing.getCOpiatesEGHeroin().getCEver())) {
			frequency = "6";
		}
		if (frequency != null) {
			DrugUse du1 = of.createDrugUse();
			du1.setFrequency(frequency);
			du1.setName("226044004");
			dul.getDrugUse().add(du1);
		}

		frequency = null;
		if ("T".equals(whichOfTheFollowing.getCSedativesNotPrescribedOrNotTakenAsPrescribedEGValium().getCPast6Months())) {
			frequency = "5";
		} else if ("T".equals(whichOfTheFollowing.getCSedativesNotPrescribedOrNotTakenAsPrescribedEGValium().getCEver())) {
			frequency = "6";
		}
		if (frequency != null) {
			DrugUse du1 = of.createDrugUse();
			du1.setFrequency(frequency);
			du1.setName("372614000");
			dul.getDrugUse().add(du1);
		}

		frequency = null;
		if ("T".equals(whichOfTheFollowing.getCOverTheCounter().getCPast6Months())) {
			frequency = "5";
		} else if ("T".equals(whichOfTheFollowing.getCOverTheCounter().getCEver())) {
			frequency = "6";
		}
		if (frequency != null) {
			DrugUse du1 = of.createDrugUse();
			du1.setFrequency(frequency);
			du1.setName("80288002");
			dul.getDrugUse().add(du1);
		}

		frequency = null;
		if ("T".equals(whichOfTheFollowing.getCSolvents().getCPast6Months())) {
			frequency = "5";
		} else if ("T".equals(whichOfTheFollowing.getCSolvents().getCEver())) {
			frequency = "6";
		}
		if (frequency != null) {
			DrugUse du1 = of.createDrugUse();
			du1.setFrequency(frequency);
			du1.setName("61010005");
			dul.getDrugUse().add(du1);
		}

		frequency = null;
		List<Serializable> otherDrugs = whichOfTheFollowing.getCOther().getContent();
		for (Object obj : otherDrugs) {
			try {
				JAXBElement<?> e = (JAXBElement<?>) obj;
				if ("T".equals(e.getValue().toString())) {
					if ("CPast_6_months".equals(e.getName().toString())) {
						frequency = "5";
					}
					if ("CEver".equals(e.getName().toString())) {
						frequency = "6";
					}
				}
			} catch (ClassCastException e) {
				continue;
			}
		}
		if (frequency != null) {
			DrugUse du1 = of.createDrugUse();
			du1.setFrequency(frequency);
			du1.setName("410515003");
			dul.getDrugUse().add(du1);
		}


		if ("T".equals(staff.getC13DrugsDoYouTakeDrugsThatArenTPrescribedAreThereAnyDrugsYouWouldFindHardTo().getCIndicateTheStageOfChangeClientIsAtOptionalSelectOne().getCPrecontemplation())) {
			d13.setStageOfChangeDrugs("1");
		} else if ("T".equals(staff.getC13DrugsDoYouTakeDrugsThatArenTPrescribedAreThereAnyDrugsYouWouldFindHardTo().getCIndicateTheStageOfChangeClientIsAtOptionalSelectOne().getCContemplation())) {
			d13.setStageOfChangeDrugs("2");
		} else if ("T".equals(staff.getC13DrugsDoYouTakeDrugsThatArenTPrescribedAreThereAnyDrugsYouWouldFindHardTo().getCIndicateTheStageOfChangeClientIsAtOptionalSelectOne().getCAction())) {
			d13.setStageOfChangeDrugs("3");
		} else if ("T".equals(staff.getC13DrugsDoYouTakeDrugsThatArenTPrescribedAreThereAnyDrugsYouWouldFindHardTo().getCIndicateTheStageOfChangeClientIsAtOptionalSelectOne().getCMaintenance())) {
			d13.setStageOfChangeDrugs("4");
		} else if ("T".equals(staff.getC13DrugsDoYouTakeDrugsThatArenTPrescribedAreThereAnyDrugsYouWouldFindHardTo().getCIndicateTheStageOfChangeClientIsAtOptionalSelectOne().getCRelapsePrevention())) {
			d13.setStageOfChangeDrugs("5");
		}



		Domain d14 = createCommonDomain(of, "14-other addictions",
			staff.getC14OtherAddictionsDoYouHaveAnAddictionIsYourAddictionAProblem().getC1DoesThePersonHaveProblemsWithAddictionsIfRated0Or9GoToTheNextDomain(),
			client.getCQuestions().getCOtherAddictionsDoYouHaveAnyOtherAddictionsSuchAsGambling().getAnswerGroup(),
			staff.getC14OtherAddictionsDoYouHaveAnAddictionIsYourAddictionAProblem().getC2HowMuchHelpWithAddictionsDoesThePersonReceiveFromFriendsOrRelatives(),
			staff.getC14OtherAddictionsDoYouHaveAnAddictionIsYourAddictionAProblem().getC3AHowMuchHelpWithAddictionsDoesThePersonReceiveFromLocalServices(),
			staff.getC14OtherAddictionsDoYouHaveAnAddictionIsYourAddictionAProblem().getC3BHowMucHelpWithAddictionsDoesThePersonNeedFromLocalServices());
		ods.getDomain().add(d14);

		AddictionTypeList atl = of.createAddictionTypeList();
		d14.setAddictionTypeList(atl);

		if ("T".equals(staff.getC14OtherAddictionsDoYouHaveAnAddictionIsYourAddictionAProblem().getCTypeOfAddictionCheckAllThatApply().getCGambling())) {
			atl.getAddictionType().add("105523009");
		} else if ("T".equals(staff.getC14OtherAddictionsDoYouHaveAnAddictionIsYourAddictionAProblem().getCTypeOfAddictionCheckAllThatApply().getCNicotine())) {
			atl.getAddictionType().add("56294008");
		} else if (staff.getC14OtherAddictionsDoYouHaveAnAddictionIsYourAddictionAProblem().getCTypeOfAddictionCheckAllThatApply().getCOther() != null &&
				staff.getC14OtherAddictionsDoYouHaveAnAddictionIsYourAddictionAProblem().getCTypeOfAddictionCheckAllThatApply().getCOther().length() > 0) {
			atl.getAddictionType().add("410515003");
			atl.setOtherAddictionType(staff.getC14OtherAddictionsDoYouHaveAnAddictionIsYourAddictionAProblem().getCTypeOfAddictionCheckAllThatApply().getCOther());
		}


		if ("T".equals(staff.getC14OtherAddictionsDoYouHaveAnAddictionIsYourAddictionAProblem().getCIndicateTheStageOfChangeClientIsAtOptionalSelectOne().getCPrecontemplation())) {
			d14.setStageOfChangeAddictions("1");
		} else if ("T".equals(staff.getC14OtherAddictionsDoYouHaveAnAddictionIsYourAddictionAProblem().getCIndicateTheStageOfChangeClientIsAtOptionalSelectOne().getCContemplation())) {
			d14.setStageOfChangeAddictions("2");
		} else if ("T".equals(staff.getC14OtherAddictionsDoYouHaveAnAddictionIsYourAddictionAProblem().getCIndicateTheStageOfChangeClientIsAtOptionalSelectOne().getCAction())) {
			d14.setStageOfChangeAddictions("3");
		} else if ("T".equals(staff.getC14OtherAddictionsDoYouHaveAnAddictionIsYourAddictionAProblem().getCIndicateTheStageOfChangeClientIsAtOptionalSelectOne().getCMaintenance())) {
			d14.setStageOfChangeAddictions("4");
		} else if ("T".equals(staff.getC14OtherAddictionsDoYouHaveAnAddictionIsYourAddictionAProblem().getCIndicateTheStageOfChangeClientIsAtOptionalSelectOne().getCRelapsePrevention())) {
			d14.setStageOfChangeAddictions("5");
		}



		Domain d15 = createCommonDomain(of, "15-company",
			staff.getC15CompanyAreYouHappyWithYourSocialLifeDoYouWishYouHadMoreContactWithOthers().getC1DoesThePersonNeedHelpWithSocialContactIfRated0Or9SkipQuestions23AndPr(),
			client.getCQuestions().getC15CompanyAreYouHappyWithYourSocialLife().getAnswerGroup(),
			staff.getC15CompanyAreYouHappyWithYourSocialLifeDoYouWishYouHadMoreContactWithOthers().getC2HowMuchHelpWithAddictionsDoesThePersonReceiveFromFriendsOrRelatives(),
			staff.getC15CompanyAreYouHappyWithYourSocialLifeDoYouWishYouHadMoreContactWithOthers().getC3AHowMuchHelpDoesThePersonReceiveFromLocalServicesInOrganizingSocialContact(),
			staff.getC15CompanyAreYouHappyWithYourSocialLifeDoYouWishYouHadMoreContactWithOthers().getC3BHowMuchHelpDoesThePersonNeedFromLocalServicesInOrganizingSocialContact());
		ods.getDomain().add(d15);


		if ("T".equals(staff.getC15CompanyAreYouHappyWithYourSocialLifeDoYouWishYouHadMoreContactWithOthers().getCHaveThereBeenAnyChangesToYourSocialPatternsRecently().getCYes())) {
			d15.setChangedSocialPatterns("TRUE");
		} else if ("T".equals(staff.getC15CompanyAreYouHappyWithYourSocialLifeDoYouWishYouHadMoreContactWithOthers().getCHaveThereBeenAnyChangesToYourSocialPatternsRecently().getCNo())) {
			d15.setChangedSocialPatterns("FALSE");
		} else if ("T".equals(staff.getC15CompanyAreYouHappyWithYourSocialLifeDoYouWishYouHadMoreContactWithOthers().getCHaveThereBeenAnyChangesToYourSocialPatternsRecently().getCUnknown())) {
			d15.setChangedSocialPatterns("UNK");
		} else if ("T".equals(staff.getC15CompanyAreYouHappyWithYourSocialLifeDoYouWishYouHadMoreContactWithOthers().getCHaveThereBeenAnyChangesToYourSocialPatternsRecently().getCClientDeclinedToAnswer())) {
			d15.setChangedSocialPatterns("CDA");
		}



		Domain d16 = createCommonDomain(of, "16-intimate relationships",
			staff.getC16IntimateRelationshipsDoYouHaveAPartnerDoYouHaveProblemsInYourPartnershipMar().getC1DoesThisPersonHaveAnyDifficultyInFindingAPartnerOrInMaintainingACloseRelation(),
			client.getCQuestions().getC16IntimateRelationshipsDoYouHaveAPartner().getAnswerGroup(),
			staff.getC16IntimateRelationshipsDoYouHaveAPartnerDoYouHaveProblemsInYourPartnershipMar().getC2HowMuchHelpWithFormingAndMaintainingCloseRelationshipsDoesThePersonReceiveFrom(),
			staff.getC16IntimateRelationshipsDoYouHaveAPartnerDoYouHaveProblemsInYourPartnershipMar().getC3AHowMuchHelpWithFormingAndMaintainingCloseRelationshipsDoesThePersonReceiveFrom(),
			staff.getC16IntimateRelationshipsDoYouHaveAPartnerDoYouHaveProblemsInYourPartnershipMar().getC3BHowMuchHelpWithFormingAndMaintainingCloseRelationshipsDoesThePersonNeedFromLo());
		ods.getDomain().add(d16);



		Domain d17 = createCommonDomain(of, "17-sexual expression",
			staff.getC17SexualExpressionHowIsYourSexLife().getC1DoesThePersonHaveProblemsWithHisOrHerSexLifeIfRated0Or9GoToTheNextDom(),
			client.getCQuestions().getC17SexualExpressionHowIsYouSexLife().getAnswerGroup(),
			staff.getC17SexualExpressionHowIsYourSexLife().getC2HowMuchHelpWithProblemsInHisOrHerSexLifeDoesThePersonReceiveFromFriendsOrR(),
			staff.getC17SexualExpressionHowIsYourSexLife().getC3AHowMuchHelpWithProblemsInHisOrHerSexLifeDoesThePersonReceiveFromLocalServi(),
			staff.getC17SexualExpressionHowIsYourSexLife().getC3BHowMuchHelpWithProblemsInHisOrHerSexLifeDoesThePersonNeedFromLocalServices());
		ods.getDomain().add(d17);



		Domain d18 = createCommonDomain(of, "18-childcare",
			staff.getC18ChildCareDoYouHaveAnyChildrenUnder18DoYouHaveAnyDifficultInLookingAfterT().getC1DoesThePersonHaveDifficultyLookingAfterHisOrHerChildrenIfRated0Or9GoToT(),
			client.getCQuestions().getC18ChildCareDoYouHaveAnyChildrenUnder18().getAnswerGroup(),
			staff.getC18ChildCareDoYouHaveAnyChildrenUnder18DoYouHaveAnyDifficultInLookingAfterT().getC2HowMuchHelpWithLookingAfterTheChildrenDoesThePersonReceiveFromFriendsOrRelati(),
			staff.getC18ChildCareDoYouHaveAnyChildrenUnder18DoYouHaveAnyDifficultInLookingAfterT().getC3AHowMuchHelpWithLookingAfterTheChildrenDoesThePersonReceiveFromLocalServices(),
			staff.getC18ChildCareDoYouHaveAnyChildrenUnder18DoYouHaveAnyDifficultInLookingAfterT().getC3BHowMuchHelpWithLookingAfterTheChildrenDoesThePersonNeedFromLocalServices());
		ods.getDomain().add(d18);


		
		Domain d19 = createCommonDomain(of, "19-other dependents",
			staff.getC19OtherDependentsDoYouHaveAnyDependentsOtherThanChildrenUnder18SuchAsAnElder().getC1DoesThePersonHaveDifficultyLookingAfterOtherDependentsIfRates0Or9GoToThe(),
			client.getCQuestions().getC19OtherDependentsDoYouHaveAnyDependentsOtherThanChildrenUnder18SuchAsAnElder().getAnswerGroup(),
			staff.getC19OtherDependentsDoYouHaveAnyDependentsOtherThanChildrenUnder18SuchAsAnElder().getC2HowMuchHelpWithLookingAfterOtherDependentsDoesThePersonReceiveFromFriendsOrRe(),
			staff.getC19OtherDependentsDoYouHaveAnyDependentsOtherThanChildrenUnder18SuchAsAnElder().getC3AHowMuchHelpWithLookingAfterOtherDependentsDoesThePersonReceiveFromLocalServic(),
			staff.getC19OtherDependentsDoYouHaveAnyDependentsOtherThanChildrenUnder18SuchAsAnElder().getC3BHowMuchHelpWithLookingAfterOtherDependentsDoesThePersonNeedFromLocalServices());
		ods.getDomain().add(d19);


		
		Domain d20 = createCommonDomain(of, "20-basic education",
			staff.getC20BasicEducationDoYouHaveDifficultyInReadingWritingSpeakingOrUnderstandingEngl().getC1DoesThePersonLackBasicSkillsInNumeracyAndLiteracyIfRated0Or9SkipQuestions(),
			client.getCQuestions().getC20BasicEducationAnyDifficultyInReadingWritingOrUnderstandingEnglish().getAnswerGroup(),
			staff.getC20BasicEducationDoYouHaveDifficultyInReadingWritingSpeakingOrUnderstandingEngl().getC2HowMuchHelpWithNumeracyAndLiteracyDoesThePersonReceiveFromFriendsOrRelatives(),
			staff.getC20BasicEducationDoYouHaveDifficultyInReadingWritingSpeakingOrUnderstandingEngl().getC3AHowMuchHelpWithNumeracyAndLiteracyDoesThePersonReceiveFromLocalServices(),
			staff.getC20BasicEducationDoYouHaveDifficultyInReadingWritingSpeakingOrUnderstandingEngl().getC3BHowMuchHelpWithNumeracyAndLiteracyDoesThePersonNeedFromLocalServices());
		ods.getDomain().add(d20);

		if ("T".equals(staff.getC20BasicEducationDoYouHaveDifficultyInReadingWritingSpeakingOrUnderstandingEngl().getCWhatIsYourHighestLevelOfEducationSelectOne().getCNoFormalSchooling())) {
			d20.setHighestEducationLevel("224304004");
		} else if ("T".equals(staff.getC20BasicEducationDoYouHaveDifficultyInReadingWritingSpeakingOrUnderstandingEngl().getCWhatIsYourHighestLevelOfEducationSelectOne().getCSomeElementaryJuniorHighSchool())) {
			d20.setHighestEducationLevel("HLES-2");
		} else if ("T".equals(staff.getC20BasicEducationDoYouHaveDifficultyInReadingWritingSpeakingOrUnderstandingEngl().getCWhatIsYourHighestLevelOfEducationSelectOne().getCElementaryJuniorHighSchool())) {
			d20.setHighestEducationLevel("224306002");
		} else if ("T".equals(staff.getC20BasicEducationDoYouHaveDifficultyInReadingWritingSpeakingOrUnderstandingEngl().getCWhatIsYourHighestLevelOfEducationSelectOne().getCSomeSecondaryHighSchool())) {
			d20.setHighestEducationLevel("HLES-4");
		} else if ("T".equals(staff.getC20BasicEducationDoYouHaveDifficultyInReadingWritingSpeakingOrUnderstandingEngl().getCWhatIsYourHighestLevelOfEducationSelectOne().getCSecondaryHighSchool())) {
			d20.setHighestEducationLevel("224308001");
		} else if ("T".equals(staff.getC20BasicEducationDoYouHaveDifficultyInReadingWritingSpeakingOrUnderstandingEngl().getCWhatIsYourHighestLevelOfEducationSelectOne().getCSomeCollegeUniversity())) {
			d20.setHighestEducationLevel("HLES-6");
		} else if ("T".equals(staff.getC20BasicEducationDoYouHaveDifficultyInReadingWritingSpeakingOrUnderstandingEngl().getCWhatIsYourHighestLevelOfEducationSelectOne().getCCollegeUniversity())) {
			d20.setHighestEducationLevel("224871002");
		} else if ("T".equals(staff.getC20BasicEducationDoYouHaveDifficultyInReadingWritingSpeakingOrUnderstandingEngl().getCWhatIsYourHighestLevelOfEducationSelectOne().getCUnknown())) {
			d20.setHighestEducationLevel("261665006");
		} else if ("T".equals(staff.getC20BasicEducationDoYouHaveDifficultyInReadingWritingSpeakingOrUnderstandingEngl().getCWhatIsYourHighestLevelOfEducationSelectOne().getCClientDeclinedToAnswer())) {
			d20.setHighestEducationLevel("CDA");
		}



		Domain d21 = createCommonDomain(of, "21-telephone",
			staff.getC21TelephoneDoYouKnowHowToUseATelephoneIsItEasyToFindOneThatYouCanUse().getC1DoesThePersonHaveDifficultyInGettingAccessToOrUsingATelephoneIfRated0Or9(),
			client.getCQuestions().getC21TelephoneDoYouKnowHowToUseATelephone().getAnswerGroup(),
			staff.getC21TelephoneDoYouKnowHowToUseATelephoneIsItEasyToFindOneThatYouCanUse().getC2HowMuchHelpDoesThePersonReceiveFromFriendsOrRelativesToMakeTelephoneCalls(),
			staff.getC21TelephoneDoYouKnowHowToUseATelephoneIsItEasyToFindOneThatYouCanUse().getC3AHowMuchHelpDoesThePersonReceiveFromLocalServicesToMakeTelephoneCalls(),
			staff.getC21TelephoneDoYouKnowHowToUseATelephoneIsItEasyToFindOneThatYouCanUse().getC3BHowMuchHelpDoesThePersonNeedFromLocalServicesToMakeTelephoneCalls());
		ods.getDomain().add(d21);



		Domain d22 = createCommonDomain(of, "22-transport",
			staff.getC22TransportDoYouHaveAccessToTransportationDoYouHaveAccessToOtherAffordableTra().getC1DoesThePersonHaveAnyProblemsUsingPublicTransportIfRated0Or9GoToTheNextD(),
			client.getCQuestions().getC22TransportHowDoYouFindUsingTheBusStreetcarOrTrain().getAnswerGroup(),
			staff.getC22TransportDoYouHaveAccessToTransportationDoYouHaveAccessToOtherAffordableTra().getC2HowMuchHelpWithTravellingDoesThePersonReceiveFromFriendsOrRelatives(),
			staff.getC22TransportDoYouHaveAccessToTransportationDoYouHaveAccessToOtherAffordableTra().getC3AHowMuchHelpWithTravellingDoesThePersonReceiveFromLocalServices(),
			staff.getC22TransportDoYouHaveAccessToTransportationDoYouHaveAccessToOtherAffordableTra().getC3BHowMuchHelpWithTravellingDoesThePersonNeedFromLocalServices());
		ods.getDomain().add(d22);



		Domain d23 = createCommonDomain(of, "23-money",
			staff.getC23MoneyHowDoYouFindBudgetingYourMoneyDoYouManageToPayYourBills().getC1DoesThePersonHaveProblemsBudgetingHisOrHerMoneyIfRated0Or9SkipQuestions2(),
			client.getCQuestions().getC23MoneyHowDoYouFindBudgetingYourMoney().getAnswerGroup(),
			staff.getC23MoneyHowDoYouFindBudgetingYourMoneyDoYouManageToPayYourBills().getC2HowMuchHelpDoesThePersonReceiveFromFriendsOrRelativesInManagingHisOrHerMoney(),
			staff.getC23MoneyHowDoYouFindBudgetingYourMoneyDoYouManageToPayYourBills().getC3AHowMuchHelpDoesThePersonReceiveFromLocalServicesInManagingHisOrHerMoney(),
			staff.getC23MoneyHowDoYouFindBudgetingYourMoneyDoYouManageToPayYourBills().getC3BHowMuchHelpDoesThePersonNeedFromLocalServicesInManagingHisOrHerMoney());
		ods.getDomain().add(d23);

		if ("T".equals(staff.getC23MoneyHowDoYouFindBudgetingYourMoneyDoYouManageToPayYourBills().getCWhatIsYourPrimarySourceOfIncomeSelectOne().getCEmployment())) {
			d23.setSourceOfIncome("031-01");
		} else if ("T".equals(staff.getC23MoneyHowDoYouFindBudgetingYourMoneyDoYouManageToPayYourBills().getCWhatIsYourPrimarySourceOfIncomeSelectOne().getCEmploymentInsurance())) {
			d23.setSourceOfIncome("031-02");
		} else if ("T".equals(staff.getC23MoneyHowDoYouFindBudgetingYourMoneyDoYouManageToPayYourBills().getCWhatIsYourPrimarySourceOfIncomeSelectOne().getCPension())) {
			d23.setSourceOfIncome("031-03");
		} else if ("T".equals(staff.getC23MoneyHowDoYouFindBudgetingYourMoneyDoYouManageToPayYourBills().getCWhatIsYourPrimarySourceOfIncomeSelectOne().getCODSP())) {
			d23.setSourceOfIncome("031-04");
		} else if ("T".equals(staff.getC23MoneyHowDoYouFindBudgetingYourMoneyDoYouManageToPayYourBills().getCWhatIsYourPrimarySourceOfIncomeSelectOne().getCSocialAssistance())) {
			d23.setSourceOfIncome("031-05");
		} else if ("T".equals(staff.getC23MoneyHowDoYouFindBudgetingYourMoneyDoYouManageToPayYourBills().getCWhatIsYourPrimarySourceOfIncomeSelectOne().getCDisabilityAssistance())) {
			d23.setSourceOfIncome("031-06");
		} else if ("T".equals(staff.getC23MoneyHowDoYouFindBudgetingYourMoneyDoYouManageToPayYourBills().getCWhatIsYourPrimarySourceOfIncomeSelectOne().getCFamily())) {
			d23.setSourceOfIncome("031-07");
		} else if ("T".equals(staff.getC23MoneyHowDoYouFindBudgetingYourMoneyDoYouManageToPayYourBills().getCWhatIsYourPrimarySourceOfIncomeSelectOne().getCNoSourceOfIncome())) {
			d23.setSourceOfIncome("031-08");
		} else if ("T".equals(staff.getC23MoneyHowDoYouFindBudgetingYourMoneyDoYouManageToPayYourBills().getCWhatIsYourPrimarySourceOfIncomeSelectOne().getCOther())) {
			d23.setSourceOfIncome("031-09");
		} else if ("T".equals(staff.getC23MoneyHowDoYouFindBudgetingYourMoneyDoYouManageToPayYourBills().getCWhatIsYourPrimarySourceOfIncomeSelectOne().getCUnknown())) {
			d23.setSourceOfIncome("031-10");
		} else if ("T".equals(staff.getC23MoneyHowDoYouFindBudgetingYourMoneyDoYouManageToPayYourBills().getCWhatIsYourPrimarySourceOfIncomeSelectOne().getCClientDeclinedToAnswer())) {
			d23.setSourceOfIncome("031-11");
		}



		Domain d24 = createCommonDomain(of, "24-benefits",
			staff.getC24BenefitsAreYouSureThatYouAreGettingAllTheMoneyThatYouAreEntitledTo().getC1IsThePersonDefinitelyReceivingAllTheBenefitsThatHeOrSheIsEntitledToIfRated(),
			client.getCQuestions().getC24BenefitsAreYouGettingAllTheMoneyYouAreEntitledTo().getAnswerGroup(),
			staff.getC24BenefitsAreYouSureThatYouAreGettingAllTheMoneyThatYouAreEntitledTo().getC2HowMuchHelpDoesThePersonReceiveFromFriendsOrRelativesInObtainingTheFullBenefi(),
			staff.getC24BenefitsAreYouSureThatYouAreGettingAllTheMoneyThatYouAreEntitledTo().getC3AHowMuchHelpDoesThePersonReceiveFromLocalServicesInObtainingTheFullBenefitEnt(),
			staff.getC24BenefitsAreYouSureThatYouAreGettingAllTheMoneyThatYouAreEntitledTo().getC3BHowMuchHelpDoesThePersonNeedFromLocalServicesInObtainingFullBenefitEntitlemen());
		ods.getDomain().add(d24);
		// end of 24 Domains



		AdditionalElements ae = of.createAdditionalElements();
		r1.setAdditionalElements(ae);

//		ae.setClientHopesForFuture(client.getCQuestions().getCPleaseWriteAFewSentencesToAnswerTheFollowingQuestions().getCWhatAreYourHopesForTheFuture());
//		ae.setClientNeedToGetThere(client.getCQuestions().getCPleaseWriteAFewSentencesToAnswerTheFollowingQuestions().getCWhatDoYouThinkYouNeedInOrderToGetThere());
//		ae.setClientViewMentalHealth(client.getCQuestions().getCPleaseWriteAFewSentencesToAnswerTheFollowingQuestions().getCHowDoYouViewYourMentalHealth());
//		ae.setClientSpiritualityImportance(client.getCQuestions().getCPleaseWriteAFewSentencesToAnswerTheFollowingQuestions().getCIsSpiritualityAnImportantPartOfYourLife());
//		ae.setClientCultureHeritageImportance(client.getCQuestions().getCPleaseWriteAFewSentencesToAnswerTheFollowingQuestions().getCIsCultureHeritageAnImportantPartOfYourLife());

		ae.setClientHopesForFuture(staff.getCWhatAreYourHopesForTheFuture().getC());
		ae.setClientNeedToGetThere(staff.getCWhatDoYouThinkYouNeedInOrderToGetThere().getC());
		ae.setClientViewMentalHealth(staff.getCHowDoYouViewYourMentalHealth().getC());
		ae.setClientSpiritualityImportance(staff.getCIsSpiritualityAnImportantPartOfYourLife().getC());
		ae.setClientCultureHeritageImportance(staff.getCWhatCultureDoYouIdentifyWith().getC());


		PresentingIssueList pil = of.createPresentingIssueList();
		ae.setPresentingIssueList(pil);

		if ("T".equals(staff.getCPresentingIssues().getCThreatToOthersAttemptedSuicide())) {
			PresentingIssue pi1 = of.createPresentingIssue();
			pi1.setType("017-01");
			pil.getPresentingIssue().add(pi1);
		}
		if ("T".equals(staff.getCPresentingIssues().getCSpecificSymptomOfSeriousMentalIllness())) {
			PresentingIssue pi1 = of.createPresentingIssue();
			pi1.setType("017-02");
			pil.getPresentingIssue().add(pi1);
		}
		if ("T".equals(staff.getCPresentingIssues().getCPhysicalSexualAbuse())) {
			PresentingIssue pi1 = of.createPresentingIssue();
			pi1.setType("017-03");
			pil.getPresentingIssue().add(pi1);
		}
		if ("T".equals(staff.getCPresentingIssues().getCEducational())) {
			PresentingIssue pi1 = of.createPresentingIssue();
			pi1.setType("017-04");
			pil.getPresentingIssue().add(pi1);
		}
		if ("T".equals(staff.getCPresentingIssues().getCOccupationalEmploymentVocational())) {
			PresentingIssue pi1 = of.createPresentingIssue();
			pi1.setType("017-05");
			pil.getPresentingIssue().add(pi1);
		}
		if ("T".equals(staff.getCPresentingIssues().getCHousing())) {
			PresentingIssue pi1 = of.createPresentingIssue();
			pi1.setType("017-06");
			pil.getPresentingIssue().add(pi1);
		}
		if ("T".equals(staff.getCPresentingIssues().getCFinancial())) {
			PresentingIssue pi1 = of.createPresentingIssue();
			pi1.setType("017-07");
			pil.getPresentingIssue().add(pi1);
		}
		if ("T".equals(staff.getCPresentingIssues().getCLegal())) {
			PresentingIssue pi1 = of.createPresentingIssue();
			pi1.setType("017-08");
			pil.getPresentingIssue().add(pi1);
		}
		if ("T".equals(staff.getCPresentingIssues().getCProblemsWithRelationships())) {
			PresentingIssue pi1 = of.createPresentingIssue();
			pi1.setType("017-09");
			pil.getPresentingIssue().add(pi1);
		}
		if ("T".equals(staff.getCPresentingIssues().getCProblemsWithSubstanceAbuseAddictions())) {
			PresentingIssue pi1 = of.createPresentingIssue();
			pi1.setType("017-10");
			pil.getPresentingIssue().add(pi1);
		}
		if ("T".equals(staff.getCPresentingIssues().getCActivitiesOfDailyLiving())) {
			PresentingIssue pi1 = of.createPresentingIssue();
			pi1.setType("017-11");
			pil.getPresentingIssue().add(pi1);
		}
		if ("T".equals(staff.getCPresentingIssues().getCOther())) {
			PresentingIssue pi1 = of.createPresentingIssue();
			pi1.setType("017-12");
			pil.getPresentingIssue().add(pi1);
		}


		ActionList al = of.createActionList();
		ae.setActionList(al);

		List<JAXBElement<? extends Serializable>> actions = staff.getCSummaryOfActions().getCActionsOrCDomainOrCPriority();
		String [] duo = new String[2];
		placed = 0;
		for (JAXBElement<? extends Serializable> e : actions) {
			if ("CPriority_".equals(e.getName().toString())) {
				duo[0] = e.getValue().toString();
				placed++;
			}
			if ("CDomain_".equals(e.getName().toString())) {
				duo[1] = e.getValue().toString();
				placed++;
			}
			if (placed == 2) {
				Action a1 = of.createAction();
				// dic item!
				a1.setDomain(duo[0]);
				a1.setPriority(duo[1]);
				al.getAction().add(a1);
				placed = 0;
			}
		}


		ReferralList rl = of.createReferralList();
		ae.setReferralList(rl);

		List<JAXBElement<String>> referrals = staff.getCSummaryOfReferrals().getCSpecifyOrCActualReferralOrCOptimalReferral();
		String [] six = new String[6];
		placed = 0;
		for (JAXBElement<String> e : referrals) {
			if ("COptimal_Referral_".equals(e.getName().toString())) {
				six[0] = e.getValue().toString();
				placed++;
			}
			if ("CSpecify".equals(e.getName().toString())) {
				if (placed == 1) {
					six[1] = e.getValue().toString();
					placed++;
				}
				if (placed == 3) {
					six[3] = e.getValue().toString();
					placed++;
				}
			}
			if ("CActual_Referral_".equals(e.getName().toString())) {
				six[2] = e.getValue().toString();
				placed++;
			}
			if ("CReasons_for_Difference_".equals(e.getName().toString())) {
				six[4] = e.getValue().toString();
				placed++;
			}
			if ("CReferral_Status_".equals(e.getName().toString())) {
				six[5] = e.getValue().toString();
				placed++;
			}
			if (placed == 6) {
				// dic items (all 6)!
				Referral re1 = of.createReferral();
				re1.setOptimal(six[0]);
				re1.setSpecifyOptimal(six[1]);
				re1.setActual(six[2]);
				re1.setSpecifyActual(six[3]);
				re1.setDifferenceReason(six[4]);
				re1.setStatus(six[5]);
				rl.getReferral().add(re1);
				placed = 0;
			}
		}

		process.file.getOCANSubmissionRecord().add(r1);
		return r1;
	}

	private static MedicationDetail createMedicationDetail(ObjectFactory of, String tap, String ihp, String ihn) {
		MedicationDetail md = of.createMedicationDetail();
		md.setTakenAsPrescribed("Yes".equals(tap)?"TRUE":("No".equals(tap)?"FALSE":"UNK"));
		md.setIsHelpProvided("Yes".equals(ihp)?"TRUE":("No".equals(ihp)?"FALSE":"UNK"));
		md.setIsHelpNeeded("Yes".equals(ihn)?"TRUE":("No".equals(ihn)?"FALSE":"UNK"));
		return md;
	}

	private static Domain createCommonDomain(ObjectFactory of, String name, Object _nrs, AnswerGroup _nrc, Object _ihr, Object _fhr, Object _fhn) {
		Domain d = of.createDomain();
		d.setName(name);

		NeedRating nr = of.createNeedRating();
		nr.setStaff(_nrs instanceof String ? Byte.valueOf((String)_nrs) : ((BigInteger)_nrs).byteValue());

		byte b_nrc = 0;
		if ("T".equals(_nrc.getCNoNeed())) {
			b_nrc = 1;
		} else if ("T".equals(_nrc.getCMetNeed())) {
			b_nrc = 2;
		} else if ("T".equals(_nrc.getCUnmetNeed())) {
			b_nrc = 9;
		} else {
			b_nrc = -1;
		}

		nr.setClient(b_nrc);
		d.setNeedRating(nr);

		InformalHelpRecvd ihr = of.createInformalHelpRecvd();
		ihr.setStaff(Byte.valueOf(_ihr instanceof String ? Byte.valueOf((String)_ihr) : ((BigInteger)_ihr).byteValue()));
		d.setInformalHelpRecvd(ihr);

		FormalHelpRecvd fhr = of.createFormalHelpRecvd();
		fhr.setStaff(Byte.valueOf(_fhr instanceof String ? Byte.valueOf((String)_fhr) : ((BigInteger)_fhr).byteValue()));
		d.setFormalHelpRecvd(fhr);

		FormalHelpNeed fhn = of.createFormalHelpNeed();
		fhn.setStaff(Byte.valueOf(_fhn instanceof String ? Byte.valueOf((String)_fhn) : ((BigInteger)_fhn).byteValue()));
		d.setFormalHelpNeed(fhn);

		return d;
	}

	private <T> JAXBElement<T> wrap(String ns, String tag, T o) {
		QName qtag = new QName(ns, tag);
		Class<?> clazz = o.getClass();
		@SuppressWarnings("unchecked")
		JAXBElement<T> jbe = new JAXBElement(qtag, clazz, o);
		return jbe;
	}

	@SuppressWarnings("unchecked")
	private <T> T unmarshal(Class<T> docClass, InputStream inputStream)
	throws JAXBException {
		String packageName = docClass.getPackage().getName();
		JAXBContext jc = JAXBContext.newInstance( packageName );
		Unmarshaller u = jc.createUnmarshaller();
		return (T) u.unmarshal( inputStream );
	}

	private void marshal(Object doc, String name) throws FileNotFoundException, JAXBException {
		/* Class<T> clazz = document.getValue().getClass(); */
		JAXBContext jc = JAXBContext.newInstance("oscar.ocan.domain.submission"
		/* clazz.getPackage().getName()	 */);
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		FileOutputStream os = new FileOutputStream(name);
		m.marshal(doc, os);
		try {
			os.close();
		} catch (Exception ignored) {}
	}

	public void setServiceOrganizationNumber(String serviceOrganizationNumber) {
		this.serviceOrganizationNumber = serviceOrganizationNumber;
	}

	public void setSubmissionFileLocation(String submissionFileLocation) {
		this.submissionFileLocation = submissionFileLocation;
	}
}
