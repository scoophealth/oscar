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
package oscar.oscarDemographic.pageUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.xmlbeans.XmlOptions;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ClinicalDocument;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementNoteExt;
import org.oscarehr.casemgmt.model.CaseManagementNoteLink;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.dao.DemographicArchiveDao;
import org.oscarehr.common.dao.DemographicContactDao;
import org.oscarehr.common.dao.DemographicExtDao;
import org.oscarehr.common.dao.Hl7TextInfoDao;
import org.oscarehr.common.dao.Hl7TextMessageDao;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.dao.PartialDateDao;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.DemographicArchive;
import org.oscarehr.common.model.DemographicContact;
import org.oscarehr.common.model.Hl7TextInfo;
import org.oscarehr.common.model.Hl7TextMessage;
import org.oscarehr.common.model.PartialDate;
import org.oscarehr.common.model.Provider;
import org.oscarehr.e2e.director.E2ECreator;
import org.oscarehr.e2e.util.EverestUtils;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentCommentDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentToDemographicDao;
import org.oscarehr.hospitalReportManager.model.HRMDocument;
import org.oscarehr.hospitalReportManager.model.HRMDocumentComment;
import org.oscarehr.hospitalReportManager.model.HRMDocumentToDemographic;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.sharingcenter.DocumentType;
import org.oscarehr.sharingcenter.dao.DemographicExportDao;
import org.oscarehr.sharingcenter.model.DemographicExport;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.WebUtils;

import cds.AlertsAndSpecialNeedsDocument.AlertsAndSpecialNeeds;
import cds.AllergiesAndAdverseReactionsDocument.AllergiesAndAdverseReactions;
import cds.AppointmentsDocument.Appointments;
import cds.CareElementsDocument.CareElements;
import cds.ClinicalNotesDocument.ClinicalNotes;
import cds.DemographicsDocument.Demographics;
import cds.FamilyHistoryDocument.FamilyHistory;
import cds.ImmunizationsDocument.Immunizations;
import cds.LaboratoryResultsDocument.LaboratoryResults;
import cds.MedicationsAndTreatmentsDocument.MedicationsAndTreatments;
import cds.OmdCdsDocument;
import cds.PastHealthDocument.PastHealth;
import cds.PatientRecordDocument.PatientRecord;
import cds.ProblemListDocument.ProblemList;
import cds.ReportsReceivedDocument.ReportsReceived;
import cds.RiskFactorsDocument.RiskFactors;
import oscar.OscarProperties;
import oscar.appt.ApptStatusData;
import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.oscarClinic.ClinicData;
import oscar.oscarDemographic.data.DemographicData;
import oscar.oscarDemographic.data.DemographicRelationship;
import oscar.oscarEncounter.oscarMeasurements.data.ImportExportMeasurements;
import oscar.oscarEncounter.oscarMeasurements.data.Measurements;
import oscar.oscarLab.ca.all.parsers.Factory;
import oscar.oscarLab.ca.all.parsers.MessageHandler;
import oscar.oscarLab.ca.all.upload.ProviderLabRouting;
import oscar.oscarPrevention.PreventionData;
import oscar.oscarProvider.data.ProviderData;
import oscar.oscarReport.data.DemographicSets;
import oscar.oscarReport.data.RptDemographicQueryBuilder;
import oscar.oscarReport.data.RptDemographicQueryLoader;
import oscar.oscarReport.pageUtil.RptDemographicReportForm;
import oscar.oscarRx.data.RxPatientData;
import oscar.oscarRx.data.RxPrescriptionData;
import oscar.util.ConversionUtils;
import oscar.util.StringUtils;
import oscar.util.UtilDateUtilities;

/**
 *
 * @author Ronnie Cheng
 */
public class DemographicExportAction4 extends Action {

	private static final Logger logger = MiscUtils.getLogger();
	private static final DemographicArchiveDao demoArchiveDao = (DemographicArchiveDao) SpringUtils.getBean("demographicArchiveDao");
	private static final DemographicContactDao contactDao = (DemographicContactDao) SpringUtils.getBean("demographicContactDao");
	private static final PartialDateDao partialDateDao = (PartialDateDao) SpringUtils.getBean("partialDateDao");
	private static final HRMDocumentToDemographicDao hrmDocToDemographicDao = (HRMDocumentToDemographicDao) SpringUtils.getBean("HRMDocumentToDemographicDao");
	private static final HRMDocumentDao hrmDocDao = (HRMDocumentDao) SpringUtils.getBean("HRMDocumentDao");
	private static final HRMDocumentCommentDao hrmDocCommentDao = (HRMDocumentCommentDao) SpringUtils.getBean("HRMDocumentCommentDao");
	private static final CaseManagementManager cmm = (CaseManagementManager) SpringUtils.getBean("caseManagementManager");
	private static final Hl7TextInfoDao hl7TxtInfoDao = (Hl7TextInfoDao)SpringUtils.getBean("hl7TextInfoDao");
	private static final Hl7TextMessageDao hl7TxtMssgDao = (Hl7TextMessageDao)SpringUtils.getBean("hl7TextMessageDao");
	private static final DemographicExtDao demographicExtDao = (DemographicExtDao) SpringUtils.getBean("demographicExtDao");
	private static final String PATIENTID = "Patient";
	private static final String ALERT = "Alert";
	private static final String ALLERGY = "Allergy";
	private static final String APPOINTMENT = "Appointment";
	private static final String CAREELEMENTS = "Care";
	private static final String CLINICALNOTE = "Clinical";
	private static final String PERSONALHISTORY = "Personal";
	private static final String FAMILYHISTORY = "Family";
	private static final String IMMUNIZATION = "Immunization";
	private static final String LABS = "Labs";
	private static final String MEDICATION = "Medication";
	private static final String PASTHEALTH = "Past";
	private static final String PROBLEMLIST = "Problem";
	private static final String REPORTBINARY = "Binary";
	private static final String REPORTTEXT = "Text";
	private static final String RISKFACTOR = "Risk";
	public static final int CMS4 = 0;
	public static final int E2E = 1;

	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
	Integer exportNo = 0;
	ArrayList<String> exportError = null;
	HashMap<String, Integer> entries = new HashMap<String, Integer>();
	OscarProperties oscarProperties = OscarProperties.getInstance();

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String strEditable = oscarProperties.getProperty("ENABLE_EDIT_APPT_STATUS");

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_demographic", "r", null)) {
			throw new SecurityException("missing required security object (_demographic)");
		}
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_demographicExport", "r", null)) {
			throw new SecurityException("missing required security object (_demographicExport)");
		}
		
		DemographicExportForm defrm = (DemographicExportForm)form;
		String demographicNo = defrm.getDemographicNo();
		String setName = defrm.getPatientSet();
		String pgpReady = defrm.getPgpReady();
		String templateOption = defrm.getTemplate();
		boolean exPersonalHistory = WebUtils.isChecked(request, "exPersonalHistory");
		boolean exFamilyHistory = WebUtils.isChecked(request, "exFamilyHistory");
		boolean exPastHealth = WebUtils.isChecked(request, "exPastHealth");
		boolean exProblemList = WebUtils.isChecked(request, "exProblemList");
		boolean exRiskFactors = WebUtils.isChecked(request, "exRiskFactors");
		boolean exAllergiesAndAdverseReactions = WebUtils.isChecked(request, "exAllergiesAndAdverseReactions");
		boolean exMedicationsAndTreatments = WebUtils.isChecked(request, "exMedicationsAndTreatments");
		boolean exImmunizations = WebUtils.isChecked(request, "exImmunizations");
		boolean exLaboratoryResults = WebUtils.isChecked(request, "exLaboratoryResults");
		boolean exAppointments = WebUtils.isChecked(request, "exAppointments");
		boolean exClinicalNotes = WebUtils.isChecked(request, "exClinicalNotes");
		boolean exReportsReceived = WebUtils.isChecked(request, "exReportsReceived");
		boolean exAlertsAndSpecialNeeds = WebUtils.isChecked(request, "exAlertsAndSpecialNeeds");
		boolean exCareElements = WebUtils.isChecked(request, "exCareElements");

		List<String> list = new ArrayList<String>();
		if (demographicNo==null) {
			list = new DemographicSets().getDemographicSet(setName);
		if (list.isEmpty()) {
			Date asofDate = new Date();
			RptDemographicReportForm frm = new RptDemographicReportForm ();
			frm.setSavedQuery(setName);
			RptDemographicQueryLoader demoL = new RptDemographicQueryLoader();
			frm = demoL.queryLoader(frm);
			frm.addDemoIfNotPresent();
			frm.setAsofDate(UtilDateUtilities.DateToString(asofDate));
			RptDemographicQueryBuilder demoQ = new RptDemographicQueryBuilder();
			ArrayList<ArrayList<String>> list2 = demoQ.buildQuery(loggedInInfo, frm,UtilDateUtilities.DateToString(asofDate));
			for (ArrayList<String> listDemo : list2) {
				list.add(listDemo.get(0));
			}
		}
	} else {
		list.add(demographicNo);
	}

	String ffwd = "fail";
	String tmpDir = oscarProperties.getProperty("TMP_DIR");
	
	// Sharing Center - holds the ID that will 'potentially' be exported.
	int documentExportId = 0;
	
	int template = 0;
	try {
		template = Integer.parseInt(templateOption);
	}
	catch(Exception e ) {
		MiscUtils.getLogger().error("Bad template Option");
	}
	
	switch(template) {
		case CMS4:
	if (!Util.checkDir(tmpDir)) {
		logger.debug("Error! Cannot write to TMP_DIR - Check oscar.properties or dir permissions.");
	} else {
		XmlOptions options = new XmlOptions();
		options.put( XmlOptions.SAVE_PRETTY_PRINT );
		options.put( XmlOptions.SAVE_PRETTY_PRINT_INDENT, 3 );
		options.put( XmlOptions.SAVE_AGGRESSIVE_NAMESPACES );

		HashMap<String,String> suggestedPrefix = new HashMap<String,String>();
		suggestedPrefix.put("cds_dt","cdsd");
		options.setSaveSuggestedPrefixes(suggestedPrefix);
		options.setSaveOuter();

		ArrayList<File> files = new ArrayList<File>();
		exportError = new ArrayList<String>();
		for (String demoNo : list) {
			if (StringUtils.empty(demoNo)) {
				exportError.add("Error! No Demographic Number");
				continue;
			}

			// DEMOGRAPHICS
			DemographicData d = new DemographicData();

			org.oscarehr.common.model.Demographic demographic = d.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), demoNo);

			if (demographic.getPatientStatus()!=null && demographic.getPatientStatus().equals("Contact-only")) continue;

			HashMap<String,String> demoExt = new HashMap<String,String>();
			demoExt.putAll(demographicExtDao.getAllValuesForDemo(Integer.parseInt(demoNo)));

			OmdCdsDocument omdCdsDoc = OmdCdsDocument.Factory.newInstance();
			OmdCdsDocument.OmdCds omdCds = omdCdsDoc.addNewOmdCds();
			PatientRecord patientRec = omdCds.addNewPatientRecord();
			Demographics demo = patientRec.addNewDemographics();

			demo.setUniqueVendorIdSequence(demoNo);
			entries.put(PATIENTID+exportNo, Integer.valueOf(demoNo));

			cdsDt.PersonNameStandard personName = demo.addNewNames();
			cdsDt.PersonNameStandard.LegalName legalName = personName.addNewLegalName();
			cdsDt.PersonNameStandard.LegalName.FirstName firstName = legalName.addNewFirstName();
			cdsDt.PersonNameStandard.LegalName.LastName  lastName  = legalName.addNewLastName();
			legalName.setNamePurpose(cdsDt.PersonNamePurposeCode.L);

			String name= StringUtils.noNull(demographic.getFirstName());
			if (StringUtils.filled(name)) {
				firstName.setPart(name);
				firstName.setPartType(cdsDt.PersonNamePartTypeCode.GIV);
//				firstName.setPartQualifier(cdsDt.PersonNamePartQualifierCode.BR);
			} else {
				exportError.add("Error! No First Name for Patient "+demoNo);
			}
			name = StringUtils.noNull(demographic.getLastName());
			if (StringUtils.filled(name)) {
				lastName.setPart(name);
				lastName.setPartType(cdsDt.PersonNamePartTypeCode.FAMC);
//				lastName.setPartQualifier(cdsDt.PersonNamePartQualifierCode.BR);
			} else {
				exportError.add("Error! No Last Name for Patient "+demoNo);
			}

			String title = demographic.getTitle();
			if (StringUtils.filled(title)) {
				if (title.equalsIgnoreCase("MISS")) personName.setNamePrefix(cdsDt.PersonNamePrefixCode.MISS);
				if (title.equalsIgnoreCase("MR")) personName.setNamePrefix(cdsDt.PersonNamePrefixCode.MR);
				if (title.equalsIgnoreCase("MRS")) personName.setNamePrefix(cdsDt.PersonNamePrefixCode.MRS);
				if (title.equalsIgnoreCase("MS")) personName.setNamePrefix(cdsDt.PersonNamePrefixCode.MS);
				if (title.equalsIgnoreCase("MSSR")) personName.setNamePrefix(cdsDt.PersonNamePrefixCode.MSSR);
				if (title.equalsIgnoreCase("PROF")) personName.setNamePrefix(cdsDt.PersonNamePrefixCode.PROF);
				if (title.equalsIgnoreCase("REEVE")) personName.setNamePrefix(cdsDt.PersonNamePrefixCode.REEVE);
				if (title.equalsIgnoreCase("REV")) personName.setNamePrefix(cdsDt.PersonNamePrefixCode.REV);
				if (title.equalsIgnoreCase("RT_HON")) personName.setNamePrefix(cdsDt.PersonNamePrefixCode.RT_HON);
				if (title.equalsIgnoreCase("SEN")) personName.setNamePrefix(cdsDt.PersonNamePrefixCode.SEN);
				if (title.equalsIgnoreCase("SGT")) personName.setNamePrefix(cdsDt.PersonNamePrefixCode.SGT);
				if (title.equalsIgnoreCase("SR")) personName.setNamePrefix(cdsDt.PersonNamePrefixCode.SR);
			}

			String lang = demographic.getOfficialLanguage();
			if (StringUtils.filled(lang)) {
				if (lang.equalsIgnoreCase("English"))	 demo.setPreferredOfficialLanguage(cdsDt.OfficialSpokenLanguageCode.ENG);
				else if (lang.equalsIgnoreCase("French")) demo.setPreferredOfficialLanguage(cdsDt.OfficialSpokenLanguageCode.FRE);
			} else {
				exportError.add("Error! No Preferred Official Language for Patient "+demoNo);
			}

			lang = demographic.getSpokenLanguage();
			if (StringUtils.filled(lang) && Util.convertLanguageToCode(lang) != null) {
				demo.setPreferredSpokenLanguage(Util.convertLanguageToCode(lang));
			}

			String sex = demographic.getSex();
			if (cdsDt.Gender.Enum.forString(sex)!=null) {
				demo.setGender(cdsDt.Gender.Enum.forString(sex));
			} else {
				exportError.add("Error! No Gender for Patient "+demoNo);
			}

			String sin = demographic.getSin();
			if (StringUtils.filled(sin) && sin.length()==9) {
				demo.setSIN(sin);
			}

			//Enrolment Status (Roster Status)
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Demographics.Enrolment enrolment;
			String rosterStatus = demographic.getRosterStatus();
			String rosterDate = "";
			if(demographic.getRosterDate() != null)
				rosterDate = formatter.format(demographic.getRosterDate());
			String rosterTermDate = "";
			if(demographic.getRosterTerminationDate() != null)
				rosterTermDate = formatter.format(demographic.getRosterTerminationDate());

			if (StringUtils.filled(rosterStatus)) {
				rosterStatus = rosterStatus.equalsIgnoreCase("RO") ? "1" : "0";
				enrolment = demo.addNewEnrolment();
				enrolment.setEnrollmentStatus(cdsDt.EnrollmentStatus.Enum.forString(rosterStatus));
				if (rosterStatus.equals("1")) {
					if (UtilDateUtilities.StringToDate(rosterDate)!=null) {
						enrolment.setEnrollmentDate(Util.calDate(rosterDate));
					}
				} else {
					if (UtilDateUtilities.StringToDate(rosterTermDate)!=null)
						enrolment.setEnrollmentTerminationDate(Util.calDate(rosterTermDate));
					String termReason = demographic.getRosterTerminationReason();
					if (StringUtils.filled(termReason))
						enrolment.setTerminationReason(cdsDt.TerminationReasonCode.Enum.forString(termReason));
				}
			}

			//Enrolment Status history
			List<DemographicArchive> DAs = demoArchiveDao.findRosterStatusHistoryByDemographicNo(Integer.valueOf(demoNo));
			String historyRS, historyRS1;
			Date historyRD, historyRD1, historyTD, historyTD1;
			for (int i=0; i<DAs.size(); i++) {
				historyRS = StringUtils.noNull(DAs.get(i).getRosterStatus());
				historyRS1 = i<DAs.size()-1 ? StringUtils.noNull(DAs.get(i+1).getRosterStatus()) : "-1";
				historyRS = historyRS.equalsIgnoreCase("RO") ? "1" : "0";
				historyRS1 = historyRS1.equalsIgnoreCase("RO") ? "1" : "0";

				historyRD = DAs.get(i).getRosterDate();
				historyRD1 = i<DAs.size()-1 ? DAs.get(i+1).getRosterDate() : null;
				historyTD = DAs.get(i).getRosterTerminationDate();
				historyTD1 = i<DAs.size()-1 ? DAs.get(i+1).getRosterTerminationDate() : null;

				if (i==0) { //check history info with current
					String rd = UtilDateUtilities.DateToString(historyRD);
					String td = UtilDateUtilities.DateToString(historyTD);
					if (historyRS.equals(rosterStatus) && rd.equals(rosterDate) && td.equals(rosterTermDate))
						continue;
				} else { //check history info with next
					if (historyRS.equals(historyRS1) &&
						UtilDateUtilities.nullSafeCompare(historyRD, historyRD1) == 0 &&
						UtilDateUtilities.nullSafeCompare(historyTD, historyTD1) == 0 )
						continue;
				}

				enrolment = demo.addNewEnrolment();
				enrolment.setEnrollmentStatus(cdsDt.EnrollmentStatus.Enum.forString(historyRS));
				if (historyRS.equals("1")) {
					if (historyRD!=null) enrolment.setEnrollmentDate(Util.calDate(historyRD));
				} else {
					if (historyTD!=null)
						enrolment.setEnrollmentTerminationDate(Util.calDate(historyTD));
					String termReason = DAs.get(i).getRosterTerminationReason();
					if (StringUtils.filled(termReason))
						enrolment.setTerminationReason(cdsDt.TerminationReasonCode.Enum.forString(termReason));
				}
			}

			//Person Status (Patient Status)
			String patientStatus = StringUtils.noNull(demographic.getPatientStatus());
			Demographics.PersonStatusCode personStatusCode = demo.addNewPersonStatusCode();
			if (StringUtils.empty(patientStatus)) {
				patientStatus = "";
				exportError.add("Error! No Person Status Code for Patient "+demoNo);
			}
			if (patientStatus.equalsIgnoreCase("AC")) personStatusCode.setPersonStatusAsEnum(cdsDt.PersonStatus.A);
			else if (patientStatus.equalsIgnoreCase("IN")) personStatusCode.setPersonStatusAsEnum(cdsDt.PersonStatus.I);
			else if (patientStatus.equalsIgnoreCase("DE")) personStatusCode.setPersonStatusAsEnum(cdsDt.PersonStatus.D);
			else {
				if ("MO".equalsIgnoreCase(patientStatus)) patientStatus = "Moved";
				else if ("FI".equalsIgnoreCase(patientStatus)) patientStatus = "Fired";
				personStatusCode.setPersonStatusAsPlainText(patientStatus);
			}
			String patientStatusDate = "";
			if(demographic.getPatientStatusDate() != null)
				patientStatusDate = formatter.format(demographic.getPatientStatusDate());
			if (StringUtils.filled(patientStatusDate)) demo.setPersonStatusDate(Util.calDate(patientStatusDate));

			//patient notes
			String demoNotes = d.getDemographicNotes(demoNo);
			if (StringUtils.filled(demoNotes)) demo.setNoteAboutPatient(demoNotes);

			String dob = StringUtils.noNull(DemographicData.getDob(demographic,"-"));
			demo.setDateOfBirth(Util.calDate(dob));
			if ("".equals(dob)) {
				exportError.add("Error! No Date Of Birth for Patient "+demoNo);
			} else if (UtilDateUtilities.StringToDate(dob)==null) {
				exportError.add("Not exporting invalid Date of Birth for Patient "+demoNo);
			}

			String chartNo = demographic.getChartNo();
			if (StringUtils.filled(chartNo)) demo.setChartNumber(chartNo);

			String email = demographic.getEmail();
			if (StringUtils.filled(email)) demo.setEmail(email);

			String providerNo = demographic.getProviderNo();
			if (StringUtils.filled(providerNo)) {
				Demographics.PrimaryPhysician pph = demo.addNewPrimaryPhysician();
				ProviderData prvd = new ProviderData(providerNo);
				if (StringUtils.noNull(prvd.getOhip_no()).length()<=6) pph.setOHIPPhysicianId(prvd.getOhip_no());
				Util.writeNameSimple(pph.addNewName(), prvd.getFirst_name(), prvd.getLast_name());
				String cpso = prvd.getPractitionerNo();
				if (cpso!=null && cpso.length()==5) pph.setPrimaryPhysicianCPSO(cpso);
			}

			if(StringUtils.filled(demographic.getSin())) {
				demo.setSIN(demographic.getSin());
			}

			if (StringUtils.filled(demographic.getHin())) {
				cdsDt.HealthCard healthCard = demo.addNewHealthCard();

				healthCard.setNumber(demographic.getHin());
				if (Util.setProvinceCode(demographic.getHcType())!=null) healthCard.setProvinceCode(Util.setProvinceCode(demographic.getHcType()));
				else healthCard.setProvinceCode(cdsDt.HealthCardProvinceCode.X_70); //Asked, unknown
				if (healthCard.getProvinceCode()==null) {
					exportError.add("Error! No Health Card Province Code for Patient "+demoNo);
				}
				if (StringUtils.filled(demographic.getVer())) healthCard.setVersion(demographic.getVer());
				String HCRenewDate = "";
				if(demographic.getHcRenewDate() != null)
					HCRenewDate = formatter.format(demographic.getHcRenewDate());
				if (UtilDateUtilities.StringToDate(HCRenewDate)!=null) {
					healthCard.setExpirydate(Util.calDate(HCRenewDate));
				}
			}
			if (StringUtils.filled(demographic.getAddress())) {
				cdsDt.Address addr = demo.addNewAddress();
				cdsDt.AddressStructured address = addr.addNewStructured();

				addr.setAddressType(cdsDt.AddressType.R);
				address.setLine1(demographic.getAddress());
				if (StringUtils.filled(demographic.getCity()) || StringUtils.filled(demographic.getProvince()) || StringUtils.filled(demographic.getPostal())) {
					address.setCity(StringUtils.noNull(demographic.getCity()));
					address.setCountrySubdivisionCode(Util.setCountrySubDivCode(demographic.getProvince()));
					address.addNewPostalZipCode().setPostalCode(StringUtils.noNull(demographic.getPostal()).replace(" ",""));
				}
			}

			boolean phoneExtTooLong = false;
			if (phoneNoValid(demographic.getPhone())) {
				phoneExtTooLong = addPhone(demographic.getPhone(), demoExt.get("hPhoneExt"), cdsDt.PhoneNumberType.R, demo.addNewPhoneNumber());
				if (phoneExtTooLong) {
					exportError.add("Home phone extension too long - trimmed for Patient "+demoNo);
				}
			}

			if (phoneNoValid(demographic.getPhone2())) {
				phoneExtTooLong = addPhone(demographic.getPhone2(), demoExt.get("wPhoneExt"), cdsDt.PhoneNumberType.W, demo.addNewPhoneNumber());
				if (phoneExtTooLong) {
					exportError.add("Work phone extension too long, export trimmed for Patient "+demoNo);
				}
			}

			if (phoneNoValid(demoExt.get("demo_cell"))) {
				addPhone(demoExt.get("demo_cell"), null, cdsDt.PhoneNumberType.C, demo.addNewPhoneNumber());
			}
			demoExt = null;

			if (oscarProperties.isPropertyActive("NEW_CONTACTS_UI")) {
				addDemographicContacts(loggedInInfo, demoNo, demo);
			} else {
				addDemographicRelationships(loggedInInfo, demoNo, demo);
			}

			List<CaseManagementNote> lcmn = cmm.getNotes(demoNo);

			//find all "header"; cms4 only
			List<CaseManagementNote> headers = new ArrayList<CaseManagementNote>();
			for (CaseManagementNote cmn : lcmn) {
				if (cmn.getNote()!=null && cmn.getNote().startsWith("imported.cms4.2011.06") && cmm.getLinkByNote(cmn.getId()).isEmpty())
					headers.add(cmn);
			}

			for (CaseManagementNote cmn : lcmn) {
				String famHist="", socHist="", medHist="", concerns="", reminders="", riskFactors="", encounter="", annotation="", summary="";
				Set<CaseManagementIssue> sisu = cmn.getIssues();
				boolean systemIssue = false;
				for (CaseManagementIssue isu : sisu) {
					String _issue = isu.getIssue()!=null ? isu.getIssue().getCode() : "";
					if (_issue.equals("SocHistory")) {
						systemIssue = true;
						socHist = cmn.getNote();
						break;
					} else if (_issue.equals("FamHistory")) {
						systemIssue = true;
						famHist = cmn.getNote();
						break;
					} else if (_issue.equals("MedHistory")) {
						systemIssue = true;
						medHist = cmn.getNote();
						break;
					} else if (_issue.equals("Concerns")) {
						systemIssue = true;
						concerns = cmn.getNote();
						break;
					} else if (_issue.equals("Reminders")) {
						systemIssue = true;
						reminders = cmn.getNote();
						break;
					} else if (_issue.equals("RiskFactors")) {
						systemIssue = true;
						riskFactors = cmn.getNote();
						break;
					} else continue;
				}
				if (!systemIssue && cmm.getLinkByNote(cmn.getId()).isEmpty()) { //this is not an annotation
						encounter = cmn.getNote();
						if (encounter.startsWith("imported.cms4.2011.06")) continue; //this is a "header", cms4 only
				}

				annotation = getNonDumpNote(CaseManagementNoteLink.CASEMGMTNOTE, cmn.getId(), null);
				List<CaseManagementNoteExt> cmeList = cmm.getExtByNote(cmn.getId());

				if (exPersonalHistory) {
					// PERSONAL HISTORY (SocHistory)
					if (StringUtils.filled(socHist)) {
						summary = Util.addSummary("Personal History", socHist);
						for (CaseManagementIssue isu : sisu) {
							String codeSystem = isu.getIssue().getType();
							if (!codeSystem.equals("system")) {
								summary = Util.addSummary(summary, "Diagnosis", isu.getIssue().getDescription());
							}
						}
						addOneEntry(PERSONALHISTORY);
						boolean bSTARTDATE=false, bRESOLUTIONDATE=false;
						for (CaseManagementNoteExt cme : cmeList) {
							if (cme.getKeyVal().equals(CaseManagementNoteExt.STARTDATE)) {
								if (bSTARTDATE) continue;
								if (cme.getDateValue()!=null) {
									summary = Util.addSummary(summary, CaseManagementNoteExt.STARTDATE, Util.readPartialDate(cme));
								}
								bSTARTDATE = true;
							} else if (cme.getKeyVal().equals(CaseManagementNoteExt.RESOLUTIONDATE)) {
								if (bRESOLUTIONDATE) continue;
								if (cme.getDateValue()!=null) {
									summary = Util.addSummary(summary, CaseManagementNoteExt.RESOLUTIONDATE, Util.readPartialDate(cme));
								}
								bRESOLUTIONDATE = true;
							}
						}
						summary = Util.addSummary(summary, "Notes", annotation);
						patientRec.addNewPersonalHistory().setCategorySummaryLine(summary);
					}
				}
				if (exFamilyHistory) {
					// FAMILY HISTORY (FamHistory)
					if (StringUtils.filled(famHist)) {
						FamilyHistory fHist = patientRec.addNewFamilyHistory();
						fHist.setProblemDiagnosisProcedureDescription(famHist);
						summary = Util.addSummary("Problem Description", famHist);

						boolean diagnosisAssigned = false;
						for (CaseManagementIssue isu : sisu) {
							String codeSystem = isu.getIssue().getType();
							if (!codeSystem.equals("system")) {
								if (diagnosisAssigned) {
									summary = Util.addSummary(summary, "Diagnosis", isu.getIssue().getDescription());
								} else {
									cdsDt.StandardCoding diagnosis = fHist.addNewDiagnosisProcedureCode();
									diagnosis.setStandardCodingSystem(codeSystem);
									String code = codeSystem.equalsIgnoreCase("icd9") ? Util.formatIcd9(isu.getIssue().getCode()) : isu.getIssue().getCode();
									diagnosis.setStandardCode(code);
									diagnosis.setStandardCodeDescription(isu.getIssue().getDescription());
									summary = Util.addSummary(summary, "Diagnosis", diagnosis.getStandardCodeDescription());
									diagnosisAssigned = true;
								}
							}
						}
						addOneEntry(FAMILYHISTORY);
						boolean bSTARTDATE=false, bTREATMENT=false, bAGEATONSET=false, bRELATIONSHIP=false, bLIFESTAGE=false;
						for (CaseManagementNoteExt cme : cmeList) {
							if (cme.getKeyVal().equals(CaseManagementNoteExt.STARTDATE)) {
								if (bSTARTDATE) continue;
								if (cme.getDateValue()!=null) {
									Util.putPartialDate(fHist.addNewStartDate(), cme);
									summary = Util.addSummary(summary, CaseManagementNoteExt.STARTDATE, Util.readPartialDate(cme));
								}
								bSTARTDATE = true;
							} else if (cme.getKeyVal().equals(CaseManagementNoteExt.TREATMENT)) {
								if (bTREATMENT) continue;
								if (StringUtils.filled(cme.getValue())) {
									fHist.setTreatment(cme.getValue());
									summary = Util.addSummary(summary, CaseManagementNoteExt.TREATMENT, cme.getValue());
								}
								bTREATMENT = true;
							} else if (cme.getKeyVal().equals(CaseManagementNoteExt.AGEATONSET)) {
								if (bAGEATONSET) continue;
								if (StringUtils.filled(cme.getValue())) {
									fHist.setAgeAtOnset(BigInteger.valueOf(Long.valueOf(cme.getValue())));
									summary = Util.addSummary(summary, CaseManagementNoteExt.AGEATONSET, cme.getValue());
								}
								bAGEATONSET = true;
							} else if (cme.getKeyVal().equals(CaseManagementNoteExt.RELATIONSHIP)) {
								if (bRELATIONSHIP) continue;
								if (StringUtils.filled(cme.getValue())) {
									fHist.setRelationship(cme.getValue());
									summary = Util.addSummary(summary, CaseManagementNoteExt.RELATIONSHIP, cme.getValue());
								}
								bRELATIONSHIP = true;
							} else if (cme.getKeyVal().equals(CaseManagementNoteExt.LIFESTAGE)) {
								if (bLIFESTAGE) continue;
								if ("NICTA".contains(cme.getValue()) && cme.getValue().length()==1) {
									fHist.setLifeStage(cdsDt.LifeStage.Enum.forString(cme.getValue()));
									summary = Util.addSummary(summary, CaseManagementNoteExt.LIFESTAGE, cme.getValue());
								}
								bLIFESTAGE = true;
							}
						}
						if (StringUtils.filled(annotation)) {
							fHist.setNotes(annotation);
							summary = Util.addSummary(summary, "Notes", annotation);
						}
						fHist.setCategorySummaryLine(summary);
					}
				}
				if (exPastHealth) {
					// PAST HEALTH (MedHistory)
					if (StringUtils.filled(medHist)) {
						PastHealth pHealth = patientRec.addNewPastHealth();
						summary = Util.addSummary("Problem Description", medHist);

						boolean diagnosisAssigned = false;
						for (CaseManagementIssue isu : sisu) {
							String codeSystem = isu.getIssue().getType();
							if (!codeSystem.equals("system")) {
								if (diagnosisAssigned) {
									summary = Util.addSummary(summary, "Diagnosis", isu.getIssue().getDescription());
								} else {
									cdsDt.StandardCoding diagnosis = pHealth.addNewDiagnosisProcedureCode();

									diagnosis.setStandardCodingSystem(codeSystem);
									String code = codeSystem.equalsIgnoreCase("icd9") ? Util.formatIcd9(isu.getIssue().getCode()) : isu.getIssue().getCode();
									diagnosis.setStandardCode(code);
									diagnosis.setStandardCodeDescription(isu.getIssue().getDescription());
									summary = Util.addSummary(summary, "Diagnosis", diagnosis.getStandardCodeDescription());
									diagnosisAssigned = true;
								}
							}
						}
						addOneEntry(PASTHEALTH);
						boolean bSTARTDATE=false, bRESOLUTIONDATE=false, bPROCEDUREDATE=false, bLIFESTAGE=false;
						for (CaseManagementNoteExt cme : cmeList) {
							if (cme.getKeyVal().equals(CaseManagementNoteExt.STARTDATE)) {
								if (bSTARTDATE) continue;
								if (cme.getDateValue()!=null) {
									Util.putPartialDate(pHealth.addNewOnsetOrEventDate(), cme);
									summary = Util.addSummary(summary, "Onset/Event Date", Util.readPartialDate(cme));
								}
								bSTARTDATE = true;
							} else if (cme.getKeyVal().equals(CaseManagementNoteExt.RESOLUTIONDATE)) {
								if (bRESOLUTIONDATE) continue;
								if (cme.getDateValue()!=null) {
									Util.putPartialDate(pHealth.addNewResolvedDate(), cme);
									summary = Util.addSummary(summary, "Resolved Date", Util.readPartialDate(cme));
								}
								bRESOLUTIONDATE = true;
							} else if (cme.getKeyVal().equals(CaseManagementNoteExt.PROCEDUREDATE)) {
								if (bPROCEDUREDATE) continue;
								if (cme.getDateValue()!=null) {
									Util.putPartialDate(pHealth.addNewProcedureDate(), cme);
									summary = Util.addSummary(summary, CaseManagementNoteExt.PROCEDUREDATE, Util.readPartialDate(cme));
								}
								bPROCEDUREDATE = true;
							} else if (cme.getKeyVal().equals(CaseManagementNoteExt.LIFESTAGE)) {
								if (bLIFESTAGE) continue;
									if ("NICTA".contains(cme.getValue()) && cme.getValue().length()==1) {
									pHealth.setLifeStage(cdsDt.LifeStage.Enum.forString(cme.getValue()));
									summary = Util.addSummary(summary, CaseManagementNoteExt.LIFESTAGE, cme.getValue());
								}
								bLIFESTAGE = true;
							}
						}
						pHealth.setPastHealthProblemDescriptionOrProcedures(medHist);
						if (StringUtils.filled(annotation)) {
							pHealth.setNotes(annotation);
							summary = Util.addSummary(summary, "Notes", annotation);
						}
						pHealth.setCategorySummaryLine(summary);
					}
				}
				if (exProblemList) {
					// PROBLEM LIST (Concerns)
					if (StringUtils.filled(concerns)) {
						ProblemList pList = patientRec.addNewProblemList();
						pList.setProblemDiagnosisDescription(concerns);
						summary = Util.addSummary("Problem Diagnosis", concerns);

						boolean diagnosisAssigned = false;
						for (CaseManagementIssue isu : sisu) {
							String codeSystem = isu.getIssue().getType();
							if (!codeSystem.equals("system")) {
								if (diagnosisAssigned) {
									summary = Util.addSummary(summary, "Diagnosis", isu.getIssue().getDescription());
								} else {
									cdsDt.StandardCoding diagnosis = pList.addNewDiagnosisCode();
									diagnosis.setStandardCodingSystem(codeSystem);
									String code = codeSystem.equalsIgnoreCase("icd9") ? Util.formatIcd9(isu.getIssue().getCode()) : isu.getIssue().getCode();
									diagnosis.setStandardCode(code);
									diagnosis.setStandardCodeDescription(isu.getIssue().getDescription());
									summary = Util.addSummary(summary, "Diagnosis", diagnosis.getStandardCodeDescription());
									diagnosisAssigned = true;
								}
							}
						}
						addOneEntry(PROBLEMLIST);
						boolean bPROBLEMDESC=false, bSTARTDATE=false, bRESOLUTIONDATE=false, bPROBLEMSTATUS=false, bLIFESTAGE=false;
						for (CaseManagementNoteExt cme : cmeList) {
							if (cme.getKeyVal().equals(CaseManagementNoteExt.PROBLEMDESC)) {
								if (bPROBLEMDESC) continue;
								if (StringUtils.filled(cme.getValue())) {
									pList.setProblemDescription(cme.getValue());
									summary = Util.addSummary(summary, "Problem Description", cme.getValue());
								}
								bPROBLEMDESC = true;
							} else if (cme.getKeyVal().equals(CaseManagementNoteExt.STARTDATE)) {
								if (bSTARTDATE) continue;
								Util.putPartialDate(pList.addNewOnsetDate(), cme);
								summary = Util.addSummary(summary, "Onset Date", Util.readPartialDate(cme));
								if (cme.getDateValue()==null) {
									exportError.add("Error! No Onset Date for Problem List for Patient "+demoNo);
								}
								bSTARTDATE = true;
							} else if (cme.getKeyVal().equals(CaseManagementNoteExt.RESOLUTIONDATE)) {
								if (bRESOLUTIONDATE) continue;
								if (cme.getDateValue()!=null) {
									Util.putPartialDate(pList.addNewResolutionDate(), cme);
									summary = Util.addSummary(summary, CaseManagementNoteExt.RESOLUTIONDATE, Util.readPartialDate(cme));
								}
								bRESOLUTIONDATE = true;
							} else if (cme.getKeyVal().equals(CaseManagementNoteExt.LIFESTAGE)) {
								if (bLIFESTAGE) continue;
								if ("NICTA".contains(cme.getValue()) && cme.getValue().length()==1) {
									pList.setLifeStage(cdsDt.LifeStage.Enum.forString(cme.getValue()));
									summary = Util.addSummary(summary, CaseManagementNoteExt.LIFESTAGE, cme.getValue());
								}
								bLIFESTAGE = true;
							} else if (cme.getKeyVal().equals(CaseManagementNoteExt.PROBLEMSTATUS)) {
								if (bPROBLEMSTATUS) continue;
								if (StringUtils.filled(cme.getValue())) {
									pList.setProblemStatus(cme.getValue());
									summary = Util.addSummary(summary, CaseManagementNoteExt.PROBLEMSTATUS, cme.getValue());
								}
								bPROBLEMSTATUS = true;
							}
						}

						if (StringUtils.filled(annotation)) {
							pList.setNotes(annotation);
							summary = Util.addSummary(summary, "Notes", annotation);
						}
						pList.setCategorySummaryLine(summary);
					}
				}
				if (exRiskFactors) {
					// RISK FACTORS
					if (StringUtils.filled(riskFactors)) {
						RiskFactors rFact = patientRec.addNewRiskFactors();

						if (riskFactors.length()>120) riskFactors = riskFactors.substring(0, 120);
						rFact.setRiskFactor(riskFactors);

						summary = Util.addSummary("Risk Factor", riskFactors);
						addOneEntry(RISKFACTOR);

						boolean bSTARTDATE=false, bRESOLUTIONDATE=false, bAGEATONSET=false, bEXPOSUREDETAIL=false, bLIFESTAGE=false;
						for (CaseManagementNoteExt cme : cmeList) {
							if (cme.getKeyVal().equals(CaseManagementNoteExt.STARTDATE)) {
								if (bSTARTDATE) continue;
								if (cme.getDateValue()!=null) {
									Util.putPartialDate(rFact.addNewStartDate(), cme);
									summary = Util.addSummary(summary, CaseManagementNoteExt.STARTDATE, Util.readPartialDate(cme));
								}
								bSTARTDATE = true;
							} else if (cme.getKeyVal().equals(CaseManagementNoteExt.RESOLUTIONDATE)) {
								if (bRESOLUTIONDATE) continue;
								if (cme.getDateValue()!=null) {
									Util.putPartialDate(rFact.addNewEndDate(), cme);
									summary = Util.addSummary(summary, "End Date", Util.readPartialDate(cme));
								}
								bRESOLUTIONDATE = true;
							} else if (cme.getKeyVal().equals(CaseManagementNoteExt.AGEATONSET)) {
								if (bAGEATONSET) continue;
								if (StringUtils.filled(cme.getValue())) {
									rFact.setAgeOfOnset(BigInteger.valueOf(Long.valueOf(cme.getValue())));
									summary = Util.addSummary(summary, CaseManagementNoteExt.AGEATONSET, cme.getValue());
								}
								bAGEATONSET = true;
							} else if (cme.getKeyVal().equals(CaseManagementNoteExt.LIFESTAGE)) {
								if (bLIFESTAGE) continue;
								if ("NICTA".contains(cme.getValue()) && cme.getValue().length()==1) {
									rFact.setLifeStage(cdsDt.LifeStage.Enum.forString(cme.getValue()));
									summary = Util.addSummary(summary, CaseManagementNoteExt.LIFESTAGE, cme.getValue());
								}
								bLIFESTAGE = true;
							} else if (cme.getKeyVal().equals(CaseManagementNoteExt.EXPOSUREDETAIL)) {
								if (bEXPOSUREDETAIL) continue;
								if (StringUtils.filled(cme.getValue())) {
									rFact.setExposureDetails(cme.getValue());
									summary = Util.addSummary(summary, CaseManagementNoteExt.EXPOSUREDETAIL, cme.getValue());
								}
								bEXPOSUREDETAIL = true;
							}
						}
						if (StringUtils.filled(annotation)) {
							rFact.setNotes(annotation);
							summary = Util.addSummary(summary, "Notes", annotation);
						}
						for (CaseManagementIssue isu : sisu) {
							String codeSystem = isu.getIssue().getType();
							if (!codeSystem.equals("system")) {
								summary = Util.addSummary(summary, "Diagnosis", isu.getIssue().getDescription());
							}
						}
						rFact.setCategorySummaryLine(summary);
					}
				}

				if (exClinicalNotes) {
					// CLINCAL NOTES
					if (StringUtils.filled(encounter)) {
						ClinicalNotes cNote = patientRec.addNewClinicalNotes();
						for (CaseManagementIssue isu : sisu) {
							String codeSystem = isu.getIssue().getType();
							if (!codeSystem.equals("system")) {
								encounter = Util.addLine(encounter, "Diagnosis: ", isu.getIssue().getDescription());
							}
						}
						cNote.setMyClinicalNotesContent(encounter);
						addOneEntry(CLINICALNOTE);

						Date createDate = cmn.getCreate_date();
						String uuid;
						for (CaseManagementNote header : headers) {
							uuid = header.getNote().substring("imported.cms4.2011.06".length());
							if (uuid.equals(cmn.getUuid())) {
								createDate = header.getCreate_date();
							}
						}

						//entered datetime
						if (createDate!=null) {
							cNote.addNewEnteredDateTime().setFullDateTime(Util.calDate(createDate));
						}

						//event datetime
						if (cmn.getObservation_date()!=null) {
							cNote.addNewEventDateTime().setFullDateTime(Util.calDate(cmn.getObservation_date()));
						}

						List<CaseManagementNote> cmn_same = cmm.getNotesByUUID(cmn.getUuid());
						for (CaseManagementNote cm_note : cmn_same) {

							//participating providers
							if (StringUtils.filled(cm_note.getProviderNo()) && !Util.isVerified(cm_note)) {
								//participant info
								ClinicalNotes.ParticipatingProviders pProvider = cNote.addNewParticipatingProviders();
								ProviderData prvd = new ProviderData(cm_note.getProviderNo());
								Util.writeNameSimple(pProvider.addNewName(), StringUtils.noNull(prvd.getFirst_name()), StringUtils.noNull(prvd.getLast_name()));
								if (StringUtils.noNull(prvd.getOhip_no()).length()<=6) pProvider.setOHIPPhysicianId(prvd.getOhip_no());

								//note created datetime
								cdsDt.DateTimeFullOrPartial noteCreatedDateTime = pProvider.addNewDateTimeNoteCreated();
								if (cmn.getUpdate_date()!=null) noteCreatedDateTime.setFullDateTime(Util.calDate(cm_note.getUpdate_date()));
								else noteCreatedDateTime.setFullDateTime(Util.calDate(new Date()));
							}

							//reviewing providers
							if (StringUtils.filled(cm_note.getSigning_provider_no()) && Util.isVerified(cm_note)) {
								//reviewer info
								ClinicalNotes.NoteReviewer noteReviewer = cNote.addNewNoteReviewer();
								ProviderData prvd = new ProviderData(cm_note.getSigning_provider_no());
								Util.writeNameSimple(noteReviewer.addNewName(), prvd.getFirst_name(), prvd.getLast_name());
								if (StringUtils.noNull(prvd.getOhip_no()).length()<=6) noteReviewer.setOHIPPhysicianId(prvd.getOhip_no());

								//note reviewed datetime
								cdsDt.DateTimeFullOrPartial noteReviewedDateTime = noteReviewer.addNewDateTimeNoteReviewed();
								if (cm_note.getUpdate_date()!=null) noteReviewedDateTime.setFullDateTime(Util.calDate(cm_note.getUpdate_date()));
								else noteReviewer.addNewDateTimeNoteReviewed().setFullDateTime(Util.calDate(new Date()));
							}
						}
					}
				}

				if (exAlertsAndSpecialNeeds) {
					// ALERTS AND SPECIAL NEEDS (Reminders)
					if (StringUtils.filled(reminders)) {
						AlertsAndSpecialNeeds alerts = patientRec.addNewAlertsAndSpecialNeeds();
						alerts.setAlertDescription(reminders);
						addOneEntry(ALERT);

						summary = Util.addSummary("Alert Description", reminders);
						boolean bSTARTDATE=false, bRESOLUTIONDATE=false;
						for (CaseManagementNoteExt cme : cmeList) {
							if (cme.getKeyVal().equals(CaseManagementNoteExt.STARTDATE)) {
								if (bSTARTDATE) continue;
								if (cme.getDateValue()!=null) {
									Util.putPartialDate(alerts.addNewDateActive(), cme);
									summary = Util.addSummary(summary, "Date Active", Util.readPartialDate(cme));
								}
								bSTARTDATE = true;
							} else if (cme.getKeyVal().equals(CaseManagementNoteExt.RESOLUTIONDATE)) {
								if (bRESOLUTIONDATE) continue;
								if (cme.getDateValue()!=null) {
									Util.putPartialDate(alerts.addNewEndDate(), cme);
									summary = Util.addSummary(summary, "End Date", Util.readPartialDate(cme));
								}
								bRESOLUTIONDATE = true;
							}
						}
						if (StringUtils.filled(annotation)) {
							alerts.setNotes(annotation);
							summary = Util.addSummary(summary, "Notes", annotation);
						}
						alerts.setCategorySummaryLine(summary);
					}
				}
			}

			if (exAllergiesAndAdverseReactions) {
				// ALLERGIES & ADVERSE REACTIONS
				Allergy[] allergies = RxPatientData.getPatient(loggedInInfo, demoNo).getActiveAllergies();
				String dateFormat = null, annotation = null;
				for (int j=0; j<allergies.length; j++) {
					AllergiesAndAdverseReactions alr = patientRec.addNewAllergiesAndAdverseReactions();
					Allergy allergy = allergies[j];
					String aSummary = "";
					addOneEntry(ALLERGY);

					String allergyDescription = allergy.getDescription();
					if (StringUtils.filled(allergyDescription)) {
						alr.setOffendingAgentDescription(allergyDescription);
						aSummary = Util.addSummary("Offending Agent Description", allergyDescription);
					}
					String regionalId = allergy.getRegionalIdentifier();
					if (StringUtils.filled(regionalId) && !regionalId.trim().equalsIgnoreCase("null")) {
						cdsDt.DrugCode drugCode = alr.addNewCode();
						drugCode.setCodeType("DIN");
						drugCode.setCodeValue(regionalId);
						aSummary = Util.addSummary(aSummary, "DIN", regionalId);
					}
					String typeCode = String.valueOf(allergy.getTypeCode());
					if (StringUtils.filled(typeCode)) {
						if (typeCode.equals("0")) {
							//alr.setReactionType(cdsDt.AdverseReactionType.AL);
							alr.setPropertyOfOffendingAgent(cdsDt.PropertyOfOffendingAgent.ND);
						} else {
							//alr.setReactionType(cdsDt.AdverseReactionType.AR);
							if (typeCode.equals("13")) {
								alr.setPropertyOfOffendingAgent(cdsDt.PropertyOfOffendingAgent.DR);
							} else {
								alr.setPropertyOfOffendingAgent(cdsDt.PropertyOfOffendingAgent.UK);
							}
						}
						aSummary = Util.addSummary(aSummary,"Property of Offending Agent",alr.getPropertyOfOffendingAgent().toString());
					}
					String allergyReaction = allergy.getReaction();
					if (StringUtils.filled(allergyReaction)) {
						alr.setReaction(allergyReaction);
						aSummary = Util.addSummary(aSummary, "Reaction", allergyReaction);
					}
					String severity = allergy.getSeverityOfReaction();
					if (StringUtils.filled(severity)) {
						if (severity.equals("1")) {
							alr.setSeverity(cdsDt.AdverseReactionSeverity.MI);
						} else if (severity.equals("2")) {
							alr.setSeverity(cdsDt.AdverseReactionSeverity.MO);
						} else if (severity.equals("3")) {
							alr.setSeverity(cdsDt.AdverseReactionSeverity.LT);
						}
						if (alr.getSeverity()!=null)
							aSummary = Util.addSummary(aSummary,"Adverse Reaction Severity",alr.getSeverity().toString());
					}
					if (allergy.getStartDate()!=null) {
						dateFormat = partialDateDao.getFormat(PartialDate.ALLERGIES, allergies[j].getAllergyId(), PartialDate.ALLERGIES_STARTDATE);
						Util.putPartialDate(alr.addNewStartDate(), allergy.getStartDate(), dateFormat);
						aSummary = Util.addSummary(aSummary,"Start Date",partialDateDao.getDatePartial(allergy.getStartDate(), dateFormat));
					}
					if (allergy.getLifeStage() != null && "NICTA".contains(allergy.getLifeStage()) && allergy.getLifeStage().length()==1) {
						alr.setLifeStage(cdsDt.LifeStage.Enum.forString(allergy.getLifeStage()));
						aSummary = Util.addSummary(aSummary,"Life Stage at Onset", allergy.getLifeStageDesc());
					}

					if (allergies[j].getEntryDate()!=null) {
						dateFormat = partialDateDao.getFormat(PartialDate.ALLERGIES, allergies[j].getAllergyId(), PartialDate.ALLERGIES_ENTRYDATE);
						Util.putPartialDate(alr.addNewRecordedDate(), allergies[j].getEntryDate(), dateFormat);
						aSummary = Util.addSummary(aSummary,"Recorded Date",partialDateDao.getDatePartial(allergies[j].getEntryDate(), dateFormat));
					}

					annotation = getNonDumpNote(CaseManagementNoteLink.ALLERGIES, (long)allergies[j].getAllergyId(), null);
					if (StringUtils.filled(annotation)) {
						alr.setNotes(annotation);
						aSummary = Util.addSummary(aSummary, "Notes", annotation);
					}

					if (StringUtils.empty(aSummary)) {
						exportError.add("Error! No Category Summary Line (Allergies & Adverse Reactions) for Patient "+demoNo+" ("+(j+1)+")");
					}
					alr.setCategorySummaryLine(aSummary);
				}
			}
			

			// IMMUNIZATIONS & PASTHEALTH (Preventive tests)
			ArrayList<Map<String,Object>> prevList = PreventionData.getPreventionData(loggedInInfo, Integer.valueOf(demoNo));
			String phSummary, imSummary;
			int cnt = 0;
			Map<String,Object> prevTypes = Util.getPreventionTypes(loggedInInfo);
			
			for (Map<String, Object> prevMap : prevList) {
				HashMap<String,Object> extraData = new HashMap<String,Object>();
				extraData.putAll(PreventionData.getPreventionById((String) prevMap.get("id")));

				String prevType = (String)prevMap.get("type");
				if (Util.isNonImmunizationPrevention(loggedInInfo, prevType, prevTypes)) {
					if (exPastHealth) {
						phSummary = null;
						PastHealth pHealth = patientRec.addNewPastHealth();
						
						String preventionDate = (String) prevMap.get("prevention_date");
						if (UtilDateUtilities.StringToDate(preventionDate)!=null) {
							pHealth.addNewProcedureDate().setFullDate(Util.calDate(preventionDate));
							phSummary = Util.addSummary(phSummary, "Date", preventionDate);
						}
						
						String description = prevType;
						phSummary = Util.addSummary("Procedure", prevType);
						
						String refused = (String) prevMap.get("refused");
						if (StringUtils.filled(refused) && !refused.equals("0")) {
							if (refused.equals("1")) refused = "Refused";
							if (refused.equals("2")) refused = "Ineligible";
							description = Util.addLine(description, refused);
						}
						
						String extra = (String)extraData.get("result");
						if (StringUtils.filled(extra)) {
							description = Util.addLine(description, "Result:", extra);
							phSummary = Util.addSummary(phSummary, "Result", extra);
						}
						extra = (String)extraData.get("reason");
						if (StringUtils.filled(extra)) {
							description = Util.addLine(description, "Reason:", extra);
							phSummary = Util.addSummary(phSummary, "Reason", extra);
						}
						pHealth.setPastHealthProblemDescriptionOrProcedures(description);
						
						extra = (String)extraData.get("comments");
						if (StringUtils.filled(extra)) {
							pHealth.setNotes(extra);
							phSummary = Util.addSummary(phSummary, "Comments", extra);
						}
						pHealth.setCategorySummaryLine(phSummary);
					}
				} else if (exImmunizations) {
					imSummary = null;
					cnt++;
					Immunizations immu = patientRec.addNewImmunizations();

					if (StringUtils.filled((String)extraData.get("manufacture"))) immu.setManufacturer((String)extraData.get("manufacture"));
					if (StringUtils.filled((String)extraData.get("lot"))) immu.setLotNumber((String)extraData.get("lot"));
					if (StringUtils.filled((String)extraData.get("route"))) immu.setRoute((String)extraData.get("route"));
					if (StringUtils.filled((String)extraData.get("location"))) immu.setSite((String)extraData.get("location"));
					if (StringUtils.filled((String)extraData.get("dose"))) immu.setDose((String)extraData.get("dose"));
					if (StringUtils.filled((String)extraData.get("comments"))) immu.setNotes((String)extraData.get("comments"));

					prevType = Util.getImmunizationType(loggedInInfo, prevType,prevTypes);
					if (cdsDt.ImmunizationType.Enum.forString(prevType)!=null) {
						immu.setImmunizationType(cdsDt.ImmunizationType.Enum.forString(prevType));
					} else {
						exportError.add("Error! No matching type for Immunization "+prevMap.get("type")+" for Patient "+demoNo+" ("+(cnt)+")");
					}

					if (StringUtils.filled((String)extraData.get("name"))) immu.setImmunizationName((String)extraData.get("name"));
					else
					{
						exportError.add("Error! No Name for Immunization "+prevType+" for Patient "+demoNo+" ("+(cnt)+")");
						if (StringUtils.filled(prevType)) {
							immu.setImmunizationName(prevType);
							imSummary = Util.addSummary("Immunization Name",prevType);
						}
						else immu.setImmunizationName("");
					}
					addOneEntry(IMMUNIZATION);

					String refused = (String) prevMap.get("refused");
					if (StringUtils.empty(refused)) {
						immu.addNewRefusedFlag();
						exportError.add("Error! No Refused Flag for Patient "+demoNo+" ("+(cnt)+")");
					} else {
						immu.addNewRefusedFlag().setBoolean(Util.convert10toboolean(refused));
						imSummary = Util.addSummary(imSummary, "Refused Flag", Util.convert10toboolean(refused)?"Y":"N");
					}

					String preventionDate = (String) prevMap.get("prevention_date");
					if (UtilDateUtilities.StringToDate(preventionDate)!=null) {
						immu.addNewDate().setFullDate(Util.calDate(preventionDate));
						imSummary = Util.addSummary(imSummary, "Date", preventionDate);
					}

					imSummary = Util.addSummary(imSummary, "Manufacturer", immu.getManufacturer());
					imSummary = Util.addSummary(imSummary, "Lot No", immu.getLotNumber());
					imSummary = Util.addSummary(imSummary, "Route", immu.getRoute());
					imSummary = Util.addSummary(imSummary, "Site", immu.getSite());
					imSummary = Util.addSummary(imSummary, "Dose", immu.getDose());
					imSummary = Util.addSummary(imSummary, "Notes", immu.getNotes());

					if (StringUtils.empty(imSummary)) {
						exportError.add("Error! No Category Summary Line (Immunization) for Patient "+demoNo+" ("+(cnt)+")");
					}
					immu.setCategorySummaryLine(StringUtils.noNull(imSummary));
				}
			}

			if (exMedicationsAndTreatments) {
				// MEDICATIONS & TREATMENTS
				RxPrescriptionData prescriptData = new RxPrescriptionData();
				RxPrescriptionData.Prescription[] arr = null;
				String annotation = null;
				arr = prescriptData.getPrescriptionsByPatient(Integer.parseInt(demoNo));
				for (int p = 0; p < arr.length; p++){
					MedicationsAndTreatments medi = patientRec.addNewMedicationsAndTreatments();
					String mSummary = "";
					if (arr[p].getWrittenDate()!=null) {
						String dateFormat = partialDateDao.getFormat(PartialDate.DRUGS, arr[p].getDrugId(), PartialDate.DRUGS_WRITTENDATE);
						Util.putPartialDate(medi.addNewPrescriptionWrittenDate(), arr[p].getWrittenDate(), dateFormat);
						mSummary = Util.addSummary("Prescription Written Date", partialDateDao.getDatePartial(arr[p].getWrittenDate(), dateFormat));
					}
					if (arr[p].getRxDate()!=null) {
						medi.addNewStartDate().setFullDate(Util.calDate(arr[p].getRxDate()));
						mSummary = Util.addSummary(mSummary,"Start Date",UtilDateUtilities.DateToString(arr[p].getRxDate(),"yyyy-MM-dd"));
					}
					String regionalId = arr[p].getRegionalIdentifier();
					if (StringUtils.filled(regionalId)) {
						medi.setDrugIdentificationNumber(regionalId);
						mSummary = Util.addSummary(mSummary, "DIN", regionalId);
					}
					String drugName = arr[p].getBrandName();
					if (StringUtils.filled(drugName)) {
						medi.setDrugName(drugName);
						mSummary = Util.addSummary(mSummary, "Drug Name", drugName);
					} else {
						drugName = arr[p].getCustomName();
						if (StringUtils.filled(drugName)) {
							medi.setDrugDescription(drugName);
							mSummary = Util.addSummary(mSummary, "Drug Description", drugName);
						} else {
							exportError.add("Error! No medication name for Patient "+demoNo+" ("+(p+1)+")");
						}
					}
					addOneEntry(MEDICATION);

					/* no need:
					DrugReasonDao drugReasonDao = (DrugReasonDao) SpringUtils.getBean("drugReasonDao");
					List<DrugReason> drugReasons = drugReasonDao.getReasonsForDrugID(arr[p].getDrugId(), true);
					if (drugReasons.size()>0 && StringUtils.filled(drugReasons.get(0).getCode()))
						medi.setProblemCode(drugReasons.get(0).getCode());
					 *
					 */

					if (StringUtils.filled(arr[p].getDosage())) {
						String[] strength = arr[p].getDosage().split(" ");

						cdsDt.DrugMeasure drugM = medi.addNewStrength();
						if (Util.leadingNum(strength[0]).equals(strength[0])) {//amount & unit separated by space
							drugM.setAmount(strength[0]);
							if (strength.length>1) drugM.setUnitOfMeasure(strength[1]);
							else drugM.setUnitOfMeasure("unit"); //UnitOfMeasure cannot be null

						} else {//amount & unit not separated, probably e.g. 50mg / 2tablet
							if (strength.length>1 && strength[1].equals("/")) {
								if (strength.length>2) {
									String unit1 = Util.leadingNum(strength[2]).equals("") ? "1" : Util.leadingNum(strength[2]);
									String unit2 = Util.trailingTxt(strength[2]).equals("") ? "unit" : Util.trailingTxt(strength[2]);

									drugM.setAmount(Util.leadingNum(strength[0])+"/"+unit1);
									drugM.setUnitOfMeasure(Util.trailingTxt(strength[0])+"/"+unit2);
								}
							} else {
								drugM.setAmount(Util.leadingNum(strength[0]));
								drugM.setUnitOfMeasure(Util.trailingTxt(strength[0]));
							}
						}
						mSummary = Util.addSummary(mSummary, "Strength", drugM.getAmount()+" "+drugM.getUnitOfMeasure());
					}

					String drugForm = arr[p].getDrugForm();
					if (StringUtils.filled(drugForm)) {
						medi.setForm(drugForm);
						mSummary = Util.addSummary(mSummary, "Form", drugForm);
					}

					//Process dosage export
					Float dosageValue = arr[p].getTakeMin();
					if (dosageValue==0) { //takemin=0, try takemax
						dosageValue = arr[p].getTakeMax();
					}
					String drugUnit = StringUtils.noNull(arr[p].getUnit());

					if (drugUnit.equalsIgnoreCase(getDosageUnit(arr[p].getDosage()))) {
						//drug unit should not be same as dosage unit
						//check drug form to see if it matches the following list

						if (StringUtils.containsIgnoreCase(drugForm, "capsule")) drugUnit = "capsule";
						else if (StringUtils.containsIgnoreCase(drugForm, "drop")) drugUnit = "drop";
						else if (StringUtils.containsIgnoreCase(drugForm, "dosing")) drugUnit = "dosing";
						else if (StringUtils.containsIgnoreCase(drugForm, "grobule")) drugUnit = "grobule";
						else if (StringUtils.containsIgnoreCase(drugForm, "granule")) drugUnit = "granule";
						else if (StringUtils.containsIgnoreCase(drugForm, "patch")) drugUnit = "patch";
						else if (StringUtils.containsIgnoreCase(drugForm, "pellet")) drugUnit = "pellet";
						else if (StringUtils.containsIgnoreCase(drugForm, "pill")) drugUnit = "pill";
						else if (StringUtils.containsIgnoreCase(drugForm, "tablet")) drugUnit = "tablet";

						if (drugUnit.equals(arr[p].getUnit())) {
							//drugUnit not changed by the above
							//export dosage as "take * dosageValue"
							dosageValue *= getDosageValue(arr[p].getDosage());
						}
					}

					//export dosage
					medi.setDosage(dosageValue.toString());
					if (StringUtils.filled(drugUnit)) medi.setDosageUnitOfMeasure(drugUnit);
					mSummary = Util.addSummary(mSummary, "Dosage", dosageValue+" "+drugUnit);

					if (StringUtils.filled(arr[p].getSpecialInstruction())) {
						medi.setPrescriptionInstructions(arr[p].getSpecialInstruction());
						mSummary = Util.addSummary(mSummary, "Prescription Instructions", arr[p].getSpecialInstruction());
					}

					if (StringUtils.filled(arr[p].getRoute())) {
						medi.setRoute(arr[p].getRoute());
						mSummary = Util.addSummary(mSummary, "Route", arr[p].getRoute());
					}
					if (StringUtils.filled(arr[p].getFreqDisplay())) {
						medi.setFrequency(arr[p].getFreqDisplay());
						mSummary = Util.addSummary(mSummary, "Frequency", arr[p].getFreqDisplay());
					}
					String duration = arr[p].getDuration();
					if (StringUtils.filled(duration)) {
						String durunit = StringUtils.noNull(arr[p].getDurationUnit());
						Integer fctr = 1;
						if (durunit.equals("W")) fctr = 7;
						else if (durunit.equals("M")) fctr = 30;

						if (NumberUtils.isDigits(duration)) {
							duration = String.valueOf(Integer.parseInt(duration)*fctr);
							medi.setDuration(duration);
							mSummary = Util.addSummary(mSummary, "Duration", duration+" Day(s)");
						}
					}
					if (StringUtils.filled(arr[p].getQuantity())) {
						medi.setQuantity(arr[p].getQuantity());
						mSummary = Util.addSummary(mSummary, "Quantity", arr[p].getQuantity());
					}
					/* no need:
					if (arr[p].getNosubs()) medi.setSubstitutionNotAllowed("Y");
					else medi.setSubstitutionNotAllowed("N");
					mSummary = Util.addSummary(mSummary, "Substitution not Allowed", arr[p].getNosubs()?"Yes":"No");
					 *
					 */

					if (StringUtils.filled(medi.getDrugName()) || StringUtils.filled(medi.getDrugIdentificationNumber())) {
						medi.setNumberOfRefills(String.valueOf(arr[p].getRepeat()));
						mSummary = Util.addSummary(mSummary, "Number of Refills", String.valueOf(arr[p].getRepeat()));
					}
					if (StringUtils.filled(arr[p].getETreatmentType())) {
						medi.setTreatmentType(arr[p].getETreatmentType());
						mSummary = Util.addSummary(mSummary, "Treatment Type", arr[p].getETreatmentType());
					}
					/* no need:
					if (StringUtils.filled(arr[p].getRxStatus())) {
						medi.setPrescriptionStatus(arr[p].getRxStatus());
						mSummary = Util.addSummary(mSummary, "Prescription Status", arr[p].getRxStatus());
					}
					/* no need:
					if (arr[p].getDispenseInterval()!=null) {
						medi.setDispenseInterval(String.valueOf(arr[p].getDispenseInterval()));
						mSummary = Util.addLine(mSummary, "Dispense Interval", arr[p].getDispenseInterval().toString());
					}
					 *
					 */
					if (arr[p].getRefillDuration()!=null) {
						medi.setRefillDuration(String.valueOf(arr[p].getRefillDuration()));
						mSummary = Util.addSummary(mSummary, "Refill Duration", arr[p].getRefillDuration().toString());
					}
					if (arr[p].getRefillQuantity()!=null) {
						medi.setRefillQuantity(String.valueOf(arr[p].getRefillQuantity()));
						mSummary = Util.addSummary(mSummary, "Refill Quantity", arr[p].getRefillQuantity().toString());
					}

					medi.addNewLongTermMedication().setBoolean(arr[p].getLongTerm());
					mSummary = Util.addSummary(mSummary, "Long Term Medication",arr[p].getLongTerm()?"Yes":"No");

					medi.addNewPastMedications().setBoolean(arr[p].getPastMed());
					mSummary = Util.addSummary(mSummary, "Past Medcation",arr[p].getPastMed()?"Yes":"No");

					cdsDt.YnIndicatorAndBlank pc = medi.addNewPatientCompliance();
					if (arr[p].getPatientCompliance()==null) {
						pc.setBlank(cdsDt.Blank.X);
					} else {
						String patientCompliance = arr[p].getPatientCompliance() ? "Yes" : "No";
						pc.setBoolean(arr[p].getPatientCompliance());
						mSummary = Util.addSummary(mSummary, "Patient Compliance", patientCompliance);
					}

					String outsideProviderName = arr[p].getOutsideProviderName();
					if (StringUtils.filled(outsideProviderName)) {
						MedicationsAndTreatments.PrescribedBy pcb = medi.addNewPrescribedBy();
						String ohip = arr[p].getOutsideProviderOhip();
						if (ohip!=null && ohip.trim().length()<=6)
							pcb.setOHIPPhysicianId(ohip.trim());
						Util.writeNameSimple(pcb.addNewName(), outsideProviderName);
						mSummary = Util.addSummary(mSummary, "Prescribed by", StringUtils.noNull(outsideProviderName));
					} else {
						String prescribeProvider = arr[p].getProviderNo();
						if (StringUtils.filled(prescribeProvider)) {
							MedicationsAndTreatments.PrescribedBy pcb = medi.addNewPrescribedBy();
							ProviderData prvd = new ProviderData(prescribeProvider);
							String ohip = prvd.getOhip_no();
							if (ohip!=null && ohip.trim().length()<=6)
								pcb.setOHIPPhysicianId(ohip.trim());
							Util.writeNameSimple(pcb.addNewName(), prvd.getFirst_name(), prvd.getLast_name());
							mSummary = Util.addSummary(mSummary, "Prescribed by", StringUtils.noNull(prvd.getFirst_name())+" "+StringUtils.noNull(prvd.getLast_name()));
						}
					}

					/* no need:
					data = arr[p].isNonAuthoritative() ? "Y" : "N";
					medi.setNonAuthoritativeIndicator(data);
					mSummary = Util.addSummary(mSummary, "Non-Authoritative", data);

					/* no need:
					medi.setPrescriptionIdentifier(String.valueOf(arr[p].getDrugId()));
					mSummary = Util.addSummary(mSummary, "Prescription Identifier", medi.getPrescriptionIdentifier());
					 *
					 */

					annotation = getNonDumpNote(CaseManagementNoteLink.DRUGS, (long)arr[p].getDrugId(), null);
					if (StringUtils.filled(annotation)) {
						medi.setNotes(annotation);
						mSummary = Util.addSummary(mSummary, "Notes", annotation);
					}

					if (StringUtils.empty(mSummary)) exportError.add("Error! No Category Summary Line (Medications & Treatments) for Patient "+demoNo+" ("+(p+1)+")");
					medi.setCategorySummaryLine(mSummary);
				}
				arr = null;
			}

			if (exLaboratoryResults) {
				// LABORATORY RESULTS
				
				//get lab readings from hl7 tables
				List<Object[]> infos = hl7TxtInfoDao.findByDemographicId(Integer.valueOf(demoNo));
				for (Object[] info : infos) {
					Hl7TextInfo hl7TxtInfo = (Hl7TextInfo)info[0];
					Hl7TextMessage hl7TextMessage = hl7TxtMssgDao.find(hl7TxtInfo.getLabNumber());
					if (hl7TextMessage==null) continue;
					
					String hl7Body = new String(Base64.decodeBase64(hl7TextMessage.getBase64EncodedeMessage()));
					if (!StringUtils.filled(hl7Body)) continue;
					
					MessageHandler h = Factory.getHandler(hl7TextMessage.getType(), hl7Body);
					if (h==null) continue;
					
					for (int i=0; i<h.getOBRCount(); i++) {
						for (int j=0; j<h.getOBXCount(i); j++) {
							String result = h.getOBXResult(i, j);
							String comments = null;
							for (int k=0; k<h.getOBXCommentCount(i, j); k++) {
								comments = Util.addLine(comments, h.getOBXComment(i, j, k));
							}
							
							if (StringUtils.filled(result) || StringUtils.filled(comments)) {
								HashMap<String,String> labMeaValues = new HashMap<String,String>();
								
								labMeaValues.put("identifier", h.getOBXIdentifier(i, j));
								labMeaValues.put("name", h.getOBXName(i, j));
								labMeaValues.put("labname", h.getPatientLocation());
								labMeaValues.put("datetime", h.getTimeStamp(i, j));
								labMeaValues.put("abnormal", h.getOBXAbnormalFlag(i, j));
								labMeaValues.put("unit", h.getOBXUnits(i, j));
								labMeaValues.put("accession", h.getAccessionNum());
								labMeaValues.put("range", h.getOBXReferenceRange(i, j));
								labMeaValues.put("request_datetime", h.getRequestDate(i));
								labMeaValues.put("olis_status", h.getOBXResultStatus(i, j));
								labMeaValues.put("lab_no", String.valueOf(hl7TxtInfo.getLabNumber()));
								labMeaValues.put("other_id", i+"-"+j);
								
								if (StringUtils.filled(result)) {
									labMeaValues.put("measureData", result);
									labMeaValues.put("comments", comments);
								} else {
									labMeaValues.put("measureData", comments);
								}
								
								LaboratoryResults labResults2 = patientRec.addNewLaboratoryResults();
								exportLabResult(labMeaValues, labResults2, demoNo);
							}
						}
					}
				}
				
				/*
				//get lab readings from measurements table
				List<LabMeasurements> labMeaList = ImportExportMeasurements.getLabMeasurements(demoNo);
				for (LabMeasurements labMea : labMeaList) {
					LaboratoryResults labResults = patientRec.addNewLaboratoryResults();
					exportLabResult(labMea, labResults, demoNo);
					
					String lab_no = labMea.getExtVal("lab_no");
					if (StringUtils.filled(lab_no)) {
						Hl7TextMessage hl7TextMessage = hl7TxtMssgDao.find(Integer.valueOf(lab_no));
						String hl7Body = new String(Base64.decodeBase64(hl7TextMessage.getBase64EncodedeMessage()));
						MessageHandler h = Factory.getHandler(hl7TextMessage.getType(), hl7Body);
						for (int i=0; i<h.getOBRCount(); i++) {
							for (int j=0; j<h.getOBXCount(i); j++) {
								if (StringUtils.filled(h.getOBXResult(i, j))) continue; //skip entries with result
								
								String commentAsResult = null;
								for (int k=0; k<h.getOBXCommentCount(i, j); k++) {
									commentAsResult = Util.addLine(commentAsResult, h.getOBXComment(i, j, k));
								}
								
								if (StringUtils.filled(commentAsResult)) {
									HashMap<String,String> labMeaValues = new HashMap<String,String>();
									
									labMeaValues.put("identifier", h.getOBXIdentifier(i, j));
									labMeaValues.put("name", h.getOBXName(i, j));
									labMeaValues.put("labname", h.getPatientLocation());
									labMeaValues.put("datetime", h.getTimeStamp(i, j));
									labMeaValues.put("abnormal", h.getOBXAbnormalFlag(i, j));
									labMeaValues.put("measureData", commentAsResult);
									labMeaValues.put("unit", h.getOBXUnits(i, j));
									labMeaValues.put("accession", h.getAccessionNum());
									labMeaValues.put("range", h.getOBXReferenceRange(i, j));
									labMeaValues.put("request_datetime", h.getRequestDate(i));
									labMeaValues.put("olis_status", h.getOBXResultStatus(i, j));
									labMeaValues.put("lab_no", lab_no);
									labMeaValues.put("other_id", i+"-"+j);
									
									LaboratoryResults labResults2 = patientRec.addNewLaboratoryResults();
									exportLabResult(labMeaValues, labResults2, demoNo);
								}
							}
						}
					}
				}
				*/
			}

			if (exAppointments) {
				// APPOINTMENTS
				OscarAppointmentDao appointmentDao = SpringUtils.getBean(OscarAppointmentDao.class);
				List<Object[]> results = appointmentDao.export_appt(Integer.parseInt(demoNo));
				Appointment ap = null;
				for (int j=0; j<results.size(); j++) {
					ap = (Appointment)results.get(j)[0];
					Provider p = (Provider)results.get(j)[1];
					
					Appointments aptm = patientRec.addNewAppointments();
					cdsDt.DateFullOrPartial apDate = aptm.addNewAppointmentDate();
					apDate.setFullDate(Util.calDate(ap.getAppointmentDate()));
					if (ap.getAppointmentDate()==null) {
						exportError.add("Error! No Appointment Date ("+j+") for Patient "+demoNo);
					}

					String startTime = ConversionUtils.toTimeString(ap.getStartTime());
					aptm.setAppointmentTime(Util.calDate(ap.getStartTime()));
					addOneEntry(APPOINTMENT);
					if (UtilDateUtilities.StringToDate(startTime,"HH:mm:ss")==null) {
						exportError.add("Error! No Appointment Time ("+(j+1)+") for Patient "+demoNo);
					}

					long dLong = (ap.getEndTime().getTime()-ap.getStartTime().getTime())/60000+1;
					BigInteger duration = BigInteger.valueOf(dLong); //duration in minutes
					aptm.setDuration(duration);

					if (StringUtils.filled(ap.getStatus())) {
						ApptStatusData asd = new ApptStatusData();
						asd.setApptStatus(ap.getStatus());
						String msg = null;
						if (strEditable!=null && strEditable.equalsIgnoreCase("yes"))
							msg = asd.getTitle();
						else
							msg = getResources(request).getMessage(asd.getTitle());

						if (StringUtils.filled(msg)) {
							aptm.setAppointmentStatus(msg);
						} else {
							exportError.add("Error! No matching message for appointment status code: " + ap.getStatus());
						}
					}
					if (StringUtils.filled(ap.getReason())) {
						aptm.setAppointmentPurpose(ap.getReason());
					}
					if (StringUtils.filled(p.getFirstName()) || StringUtils.filled(p.getLastName())) {
						Appointments.Provider prov = aptm.addNewProvider();

						if (StringUtils.noNull(p.getOhipNo()).length()<=6) prov.setOHIPPhysicianId(p.getOhipNo());
						Util.writeNameSimple(prov.addNewName(), p.getFirstName(), p.getLastName());
					}
					if (StringUtils.filled(ap.getNotes())) {
						aptm.setAppointmentNotes(ap.getNotes());
					}
				}
			}

			if (exReportsReceived) {
				// REPORTS RECEIVED
				ArrayList<EDoc> edoc_list = EDocUtil.listDemoDocs(loggedInInfo, demoNo);
				String annotation = null;
				for (int j=0; j<edoc_list.size(); j++) {
					EDoc edoc = edoc_list.get(j);

					File f = new File(edoc.getFilePath());
					if (!f.exists()) {
						exportError.add("Error! Document \""+f.getName()+"\" does not exist!");
					} else if (f.length()>Runtime.getRuntime().freeMemory()) {
						exportError.add("Error! Document \""+f.getName()+"\" too big to be exported. Not enough memory!");
					} else {
						ReportsReceived rpr = patientRec.addNewReportsReceived();
						rpr.setFormat(cdsDt.ReportFormat.TEXT);

						cdsDt.ReportContent rpc = rpr.addNewContent();
						InputStream in = new FileInputStream(f);
						byte[] b = new byte[(int)f.length()];

						int offset=0, numRead=0;
						while ((numRead=in.read(b,offset,b.length-offset)) >= 0
							   && offset < b.length) offset += numRead;

						if (offset < b.length) throw new IOException("Could not completely read file " + f.getName());
						in.close();
						if (edoc.getContentType()!=null && edoc.getContentType().startsWith("text")) {
							String str = new String(b);
							rpc.setTextContent(str);
							rpr.setFormat(cdsDt.ReportFormat.TEXT);
							addOneEntry(REPORTTEXT);
						} else {
							rpc.setMedia(b);
							rpr.setFormat(cdsDt.ReportFormat.BINARY);
							addOneEntry(REPORTBINARY);
						}

						String contentType = Util.mimeToExt(edoc.getContentType());
						if (StringUtils.empty(contentType)) contentType = cutExt(edoc.getFileName());
						if (StringUtils.empty(contentType)) exportError.add("Error! No File Extension&Version info for Document \""+edoc.getFileName()+"\"");
						rpr.setFileExtensionAndVersion(contentType);

						String docClass = edoc.getDocClass();
						if (cdsDt.ReportClass.Enum.forString(docClass)!=null) {
							rpr.setClass1(cdsDt.ReportClass.Enum.forString(docClass));
						} else {
							exportError.add("Error! No Class Type for Document \""+edoc.getFileName()+"\"");
							rpr.setClass1(cdsDt.ReportClass.OTHER_LETTER);
						}
						String docSubClass = edoc.getDocSubClass();
						if (StringUtils.filled(docSubClass)) {
							rpr.setSubClass(docSubClass);
						}
						String obsDateStr = edoc.getObservationDate();
						Date observationDate = UtilDateUtilities.StringToDate(obsDateStr, "yyyy/MM/dd");
						if (observationDate!=null) {
							rpr.addNewEventDateTime().setFullDate(Util.calDate(observationDate));
						} else {
							exportError.add("Not exporting invalid Event Date (Reports) for Patient "+demoNo+" ("+(j+1)+")");
						}
						String dateTimeStamp = edoc.getDateTimeStamp();
						if (UtilDateUtilities.StringToDate(dateTimeStamp, "yyyy-MM-dd HH:mm:ss")!=null) {
							rpr.addNewReceivedDateTime().setFullDateTime(Util.calDate(dateTimeStamp));
						} else {
							exportError.add("Not exporting invalid Received DateTime (Reports) for Patient "+demoNo+" ("+(j+1)+")");
						}
						String reviewDateTime = edoc.getReviewDateTime();
						if (UtilDateUtilities.StringToDate(reviewDateTime,"yyyy-MM-dd HH:mm:ss")!=null) {
							ReportsReceived.ReportReviewed reportReviewed = rpr.addNewReportReviewed();
							reportReviewed.addNewDateTimeReportReviewed().setFullDate(Util.calDate(reviewDateTime));
							Util.writeNameSimple(reportReviewed.addNewName(), edoc.getReviewerName());
							reviewDateTime = StringUtils.noNull(edoc.getReviewerOhip());
							if (reviewDateTime.length()<=6) reportReviewed.setReviewingOHIPPhysicianId(reviewDateTime);
						}
						Util.writeNameSimple(rpr.addNewSourceAuthorPhysician().addNewAuthorName(), edoc.getSource());

						if (StringUtils.filled(edoc.getSourceFacility())) rpr.setSourceFacility(edoc.getSourceFacility());

						if (edoc.getDocId()==null) continue;

						annotation = getNonDumpNote(CaseManagementNoteLink.DOCUMENT, Long.valueOf(edoc.getDocId()), null);
						if (StringUtils.filled(annotation)) rpr.setNotes(annotation);
					}
				}

				//HRM reports
				List<HRMDocumentToDemographic> hrmDocToDemographics = hrmDocToDemographicDao.findByDemographicNo(demoNo);
				for (HRMDocumentToDemographic hrmDocToDemographic : hrmDocToDemographics) {
					String hrmDocumentId = hrmDocToDemographic.getHrmDocumentId();
					List<HRMDocument> hrmDocs = hrmDocDao.findById(Integer.valueOf(hrmDocumentId));
					for (HRMDocument hrmDoc : hrmDocs) {
						String reportFile = hrmDoc.getReportFile();
						if (StringUtils.empty(reportFile)) continue;

						File hrmFile = new File(reportFile);
						if (!hrmFile.exists()) {
							exportError.add("Error! HRM report file '"+reportFile+"' not exists! HRM report not exported.");
							continue;
						}

						ReadHRMFile hrm = new ReadHRMFile(reportFile);
						for (int i=0; i<hrm.getReportsReceivedTotal(); i++) {
							ReportsReceived rpr = patientRec.addNewReportsReceived();

							//Message Unique ID
							if (hrm.getTransactionMessageUniqueID(i)!=null) rpr.setMessageUniqueID(hrm.getTransactionMessageUniqueID(i));

							HashMap<String,String> reportAuthor = hrm.getReportAuthorPhysician(i);
							HashMap<String,Object> reportContent = hrm.getReportContent(i);
							HashMap<String,String> reportStrings = hrm.getReportStrings(i);
							HashMap<String,Calendar> reportDates = hrm.getReportDates(i);

							if (reportAuthor!=null) {
								cdsDt.PersonNameSimple author = rpr.addNewSourceAuthorPhysician().addNewAuthorName();
								if (StringUtils.filled(reportAuthor.get("firstname"))) author.setFirstName(reportAuthor.get("firstname"));
								if (StringUtils.filled(reportAuthor.get("lastname"))) author.setLastName(reportAuthor.get("lastname"));
							}

							if (reportContent!=null) {
								if (reportContent!=null) {
									if (reportContent.get("textcontent")!=null) {
										cdsDt.ReportContent content = rpr.addNewContent();
										content.setTextContent((String)reportContent.get("textcontent"));
									} else if (reportContent.get("media")!=null) {
										cdsDt.ReportContent content = rpr.addNewContent();
										content.setMedia((byte[])reportContent.get("media"));
									}
								}
							}

							String reviewerId = null;
							Calendar reviewDate = null;

							if (reportStrings!=null) {
								//Format
								if (reportStrings.get("format")!=null) {
									if (reportStrings.get("format").equalsIgnoreCase("Text")) {
										rpr.setFormat(cdsDt.ReportFormat.TEXT);
									} else {
										rpr.setFormat(cdsDt.ReportFormat.BINARY);
									}
								} else {
									if (rpr.getContent().getMedia()!=null) rpr.setFormat(cdsDt.ReportFormat.BINARY);
									else rpr.setFormat(cdsDt.ReportFormat.TEXT);
									exportError.add("Error! No Format for HRM report! Patient "+demoNo+" ("+(i+1)+")");
								}

								//Class
								if (reportStrings.get("class")!=null) {
									rpr.setClass1(cdsDt.ReportClass.Enum.forString(formatHrmEnum(reportStrings.get("class"))));

								} else {
									rpr.setClass1(cdsDt.ReportClass.OTHER_LETTER);
									exportError.add("Error! No Class for HRM report! Export as 'Other Letter'. Patient "+demoNo+" ("+(i+1)+")");
								}

								//Media
								if (reportStrings.get("media")!=null) {
									rpr.setMedia(cdsDt.ReportMedia.Enum.forString(formatHrmEnum(reportStrings.get("media"))));
								}

								//Subclass
								if (reportStrings.get("subclass")!=null) {
									rpr.setSubClass(reportStrings.get("subclass"));
								}

								//File extension & version
								if (reportStrings.get("fileextension&version")!=null) {
									rpr.setFileExtensionAndVersion(reportStrings.get("fileextension&version"));
								}

								//HRM Result Status
								if (reportStrings.get("resultstatus")!=null) {
									rpr.setHRMResultStatus(reportStrings.get("resultstatus"));
								}

								//Sending Facility ID
								if (reportStrings.get("sendingfacility")!=null) {
									rpr.setSendingFacilityId(reportStrings.get("sendingfacility"));
								}

								//Sending Facility Report Number
								if (reportStrings.get("sendingfacilityreportnumber")!=null) {
									rpr.setSendingFacilityReport(reportStrings.get("sendingfacilityreportnumber"));
								}

								//Reviewing OHIP Physician ID
								reviewerId = reportStrings.get("reviewingohipphysicianid");
							}

							//report dates
							if (reportDates!=null) {
								if (reportDates.get("eventdatetime")!=null) {
									rpr.addNewEventDateTime().setFullDateTime(reportDates.get("eventdatetime"));
								}
								if (reportDates.get("receiveddatetime")!=null) {
									rpr.addNewReceivedDateTime().setFullDateTime(reportDates.get("receiveddatetime"));
								}
								reviewDate = reportDates.get("revieweddatetime");
							}

							//Source Facility
							if (hrmDoc.getSourceFacility()!=null) {
								rpr.setSourceFacility(hrmDoc.getSourceFacility());
							}

							//reviewing info
							if (reviewerId!=null && reviewDate!=null) {
								ReportsReceived.ReportReviewed reviewed = rpr.addNewReportReviewed();
								reviewed.addNewName();
								reviewed.setReviewingOHIPPhysicianId(reviewerId);
								reviewed.addNewDateTimeReportReviewed().setFullDate(reviewDate);
							}

							//Notes
							List<HRMDocumentComment> comments = hrmDocCommentDao.getCommentsForDocument(Integer.parseInt(hrmDocumentId));
							String notes = null;
							for (HRMDocumentComment comment : comments) {
								notes = Util.addLine(notes, comment.getComment());
							}
							if (StringUtils.filled(notes)) rpr.setNotes(notes);

							//OBR Content
							for (int j=0; j<hrm.getReportOBRContentTotal(i); j++) {
								HashMap<String,String> obrStrings = hrm.getReportOBRStrings(i, j);
								Calendar obrObservationDateTime = hrm.getReportOBRObservationDateTime(i, j);

								ReportsReceived.OBRContent obrContent = rpr.addNewOBRContent();

								if (obrStrings!=null) {

									//Accompanying Description
									if (obrStrings.get("accompanyingdescription")!=null) {
										obrContent.setAccompanyingDescription(obrStrings.get("accompanyingdescription"));
									}

									//Accompanying Mnemonic
									if (obrStrings.get("accompanyingmnemonic")!=null) {
										obrContent.setAccompanyingMnemonic(obrStrings.get("accompanyingmnemonic"));
									}

									//Accompanying Subclass
									if (obrStrings.get("accompanyingsubclass")!=null) {
										obrContent.setAccompanyingSubClass(obrStrings.get("accompanyingsubclass"));
									}
								}

								//OBR Observation Datetime
								if (obrObservationDateTime!=null) {
									obrContent.addNewObservationDateTime().setFullDateTime(obrObservationDateTime);
								}
							}
						}
					}
				}
			}

			if (exCareElements) {
				//CARE ELEMENTS
				List<Measurements> measList = ImportExportMeasurements.getMeasurements(demoNo);
				CareElements careElm = null;
				if (measList.size()>0) careElm = patientRec.addNewCareElements();
				for (Measurements meas : measList) {
					if (meas.getType().equals("HT")) { //Height in cm
						cdsDt.Height height = careElm.addNewHeight();
						height.setDate(Util.calDate(meas.getDateObserved()));
						if (meas.getDateObserved()==null) {
							exportError.add("Error! No Date for Height (id="+meas.getId()+") for Patient "+demoNo);
						}
						height.setHeight(meas.getDataField());
						height.setHeightUnit(cdsDt.Height.HeightUnit.CM);
						addOneEntry(CAREELEMENTS);
					} else if (meas.getType().equals("WT") && meas.getMeasuringInstruction().equalsIgnoreCase("in kg")) { //Weight in kg
						cdsDt.Weight weight = careElm.addNewWeight();
						weight.setDate(Util.calDate(meas.getDateObserved()));
						if (meas.getDateObserved()==null) {
							exportError.add("Error! No Date for Weight (id="+meas.getId()+") for Patient "+demoNo);
						}
						weight.setWeight(meas.getDataField());
						weight.setWeightUnit(cdsDt.Weight.WeightUnit.KG);
						addOneEntry(CAREELEMENTS);
					} else if (meas.getType().equals("WAIS") || meas.getType().equals("WC")) { //Waist Circumference in cm
						cdsDt.WaistCircumference waist = careElm.addNewWaistCircumference();
						waist.setDate(Util.calDate(meas.getDateObserved()));
						if (meas.getDateObserved()==null) {
							exportError.add("Error! No Date for Waist Circumference (id="+meas.getId()+") for Patient "+demoNo);
						}
						waist.setWaistCircumference(meas.getDataField());
						waist.setWaistCircumferenceUnit(cdsDt.WaistCircumference.WaistCircumferenceUnit.CM);
						addOneEntry(CAREELEMENTS);
					} else if (meas.getType().equals("BP")) { //Blood Pressure
						cdsDt.BloodPressure bloodp = careElm.addNewBloodPressure();
						bloodp.setDate(Util.calDate(meas.getDateObserved()));
						if (meas.getDateObserved()==null) {
							exportError.add("Error! No Date for Blood Pressure (id="+meas.getId()+") for Patient "+demoNo);
						}
						String[] sdbp = meas.getDataField().split("/");
						bloodp.setSystolicBP(sdbp[0]);
						bloodp.setDiastolicBP(sdbp[1]);
						bloodp.setBPUnit(cdsDt.BloodPressure.BPUnit.MM_HG);
						addOneEntry(CAREELEMENTS);
					} else if (meas.getType().equals("POSK")) { //Packs of Cigarettes per day
						cdsDt.SmokingPacks smokp = careElm.addNewSmokingPacks();
						smokp.setDate(Util.calDate(meas.getDateObserved()));
						if (meas.getDateObserved()==null) {
							exportError.add("Error! No Date for Smoking Packs (id="+meas.getId()+") for Patient "+demoNo);
						}
						try {
							smokp.setPerDay(new BigDecimal(meas.getDataField()));
						} catch (Exception e) {
							exportError.add("Error! Smoking Packs data null/invalid (id="+meas.getId()+") for Patient "+demoNo);
						}
						addOneEntry(CAREELEMENTS);
					} else if (meas.getType().equals("SKST")) { //Smoking Status
						cdsDt.SmokingStatus smoks = careElm.addNewSmokingStatus();
						smoks.setDate(Util.calDate(meas.getDateObserved()));
						if (meas.getDateObserved()==null) {
							exportError.add("Error! No Date for Smoking Status (id="+meas.getId()+") for Patient "+demoNo);
						}
						smoks.setStatus(Util.yn(meas.getDataField()));
						addOneEntry(CAREELEMENTS);
					} else if (meas.getType().equals("SMBG")) { //Self Monitoring Blood Glucose
						cdsDt.SelfMonitoringBloodGlucose bloodg = careElm.addNewSelfMonitoringBloodGlucose();
						bloodg.setDate(Util.calDate(meas.getDateObserved()));
						if (meas.getDateObserved()==null) {
							exportError.add("Error! No Date for Self-monitoring Blood Glucose (id="+meas.getId()+") for Patient "+demoNo);
						}
						bloodg.setSelfMonitoring(Util.yn(meas.getDataField()));
						addOneEntry(CAREELEMENTS);
					} else if (meas.getType().equals("DMME")) { //Diabetes Education
						cdsDt.DiabetesEducationalSelfManagement des = careElm.addNewDiabetesEducationalSelfManagement();
						des.setDate(Util.calDate(meas.getDateObserved()));
						if (meas.getDateObserved()==null) {
							exportError.add("Error! No Date for Diabetes Educational Self-management (id="+meas.getId()+") for Patient "+demoNo);
						}
						des.setEducationalTrainingPerformed(Util.yn(meas.getDataField()));
						addOneEntry(CAREELEMENTS);
					} else if (meas.getType().equals("SMCD")) { //Self Management Challenges
						cdsDt.DiabetesSelfManagementChallenges dsc = careElm.addNewDiabetesSelfManagementChallenges();
						dsc.setCodeValue(cdsDt.DiabetesSelfManagementChallenges.CodeValue.X_44941_3);
						dsc.setDate(Util.calDate(meas.getDateObserved()));
						if (meas.getDateObserved()==null) {
							exportError.add("Error! No Date for Diabetes Self-management Challenges (id="+meas.getId()+") for Patient "+demoNo);
						}
						dsc.setChallengesIdentified(Util.yn(meas.getDataField()));
						addOneEntry(CAREELEMENTS);
					} else if (meas.getType().equals("MCCN")) { //Motivation Counseling Completed Nutrition
						cdsDt.DiabetesMotivationalCounselling dmc = careElm.addNewDiabetesMotivationalCounselling();
						dmc.setDate(Util.calDate(meas.getDateObserved()));
						if (meas.getDateObserved()==null) {
							exportError.add("Error! No Date for Diabetes Motivational Counselling on Nutrition (id="+meas.getId()+") for Patient "+demoNo);
						}
						dmc.setCounsellingPerformed(cdsDt.DiabetesMotivationalCounselling.CounsellingPerformed.NUTRITION);
						if (Util.yn(meas.getDataField())==cdsDt.YnIndicatorsimple.N) {
							exportError.add("Patient "+demoNo+" didn't do Diabetes Counselling (Nutrition) on "+UtilDateUtilities.DateToString(meas.getDateObserved(),"yyyy-MM-dd"));
						}
						addOneEntry(CAREELEMENTS);
					} else if (meas.getType().equals("MCCE")) { //Motivation Counseling Completed Exercise
						cdsDt.DiabetesMotivationalCounselling dmc = careElm.addNewDiabetesMotivationalCounselling();
						dmc.setDate(Util.calDate(meas.getDateObserved()));
						if (meas.getDateObserved()==null) {
							exportError.add("Error! No Date for Diabetes Motivational Counselling on Exercise (id="+meas.getId()+") for Patient "+demoNo);
						}
						dmc.setCounsellingPerformed(cdsDt.DiabetesMotivationalCounselling.CounsellingPerformed.EXERCISE);
						if (Util.yn(meas.getDataField())==cdsDt.YnIndicatorsimple.N) {
							exportError.add("Patient "+demoNo+" didn't do Diabetes Counselling (Exercise) on "+UtilDateUtilities.DateToString(meas.getDateObserved(),"yyyy-MM-dd"));
						}
						addOneEntry(CAREELEMENTS);
					} else if (meas.getType().equals("MCCS")) { //Motivation Counseling Completed Smoking Cessation
						cdsDt.DiabetesMotivationalCounselling dmc = careElm.addNewDiabetesMotivationalCounselling();
						dmc.setDate(Util.calDate(meas.getDateObserved()));
						if (meas.getDateObserved()==null) {
							exportError.add("Error! No Date for Diabetes Motivational Counselling on Smoking Cessation (id="+meas.getId()+") for Patient "+demoNo);
						}
						dmc.setCounsellingPerformed(cdsDt.DiabetesMotivationalCounselling.CounsellingPerformed.SMOKING_CESSATION);
						if (Util.yn(meas.getDataField())==cdsDt.YnIndicatorsimple.N) {
							exportError.add("Patient "+demoNo+" didn't do Diabetes Counselling (Smoking Cessation) on "+UtilDateUtilities.DateToString(meas.getDateObserved(),"yyyy-MM-dd"));
						}
						addOneEntry(CAREELEMENTS);
					} else if (meas.getType().equals("MCCO")) { //Motivation Counseling Completed Other
						cdsDt.DiabetesMotivationalCounselling dmc = careElm.addNewDiabetesMotivationalCounselling();
						dmc.setDate(Util.calDate(meas.getDateObserved()));
						if (meas.getDateObserved()==null) {
							exportError.add("Error! No Date for Diabetes Motivational Counselling on Other Matters (id="+meas.getId()+") for Patient "+demoNo);
						}
						dmc.setCounsellingPerformed(cdsDt.DiabetesMotivationalCounselling.CounsellingPerformed.OTHER);
						if (Util.yn(meas.getDataField())==cdsDt.YnIndicatorsimple.N) {
							exportError.add("Patient "+demoNo+" didn't do Diabetes Counselling (Other) on "+UtilDateUtilities.DateToString(meas.getDateObserved(),"yyyy-MM-dd"));
						}
						addOneEntry(CAREELEMENTS);
					} else if (meas.getType().equals("EYEE")) { //Dilated Eye Exam
						cdsDt.DiabetesComplicationScreening dcs = careElm.addNewDiabetesComplicationsScreening();
						dcs.setDate(Util.calDate(meas.getDateObserved()));
						if (meas.getDateObserved()==null) {
							exportError.add("Error! No Date for Diabetes Complication Screening on Eye Exam (id="+meas.getId()+") for Patient "+demoNo);
						}
						dcs.setExamCode(cdsDt.DiabetesComplicationScreening.ExamCode.X_32468_1);
						if (Util.yn(meas.getDataField())==cdsDt.YnIndicatorsimple.N) {
							exportError.add("Patient "+demoNo+" didn't do Diabetes Complications Screening (Retinal Exam) on "+UtilDateUtilities.DateToString(meas.getDateObserved(),"yyyy-MM-dd"));
						}
						addOneEntry(CAREELEMENTS);
					} else if (meas.getType().equals("FTE")) { //Foot Exam
						cdsDt.DiabetesComplicationScreening dcs = careElm.addNewDiabetesComplicationsScreening();
						dcs.setDate(Util.calDate(meas.getDateObserved()));
						if (meas.getDateObserved()==null) {
							exportError.add("Error! No Date for Diabetes Complication Screening on Foot Exam (id="+meas.getId()+") for Patient "+demoNo);
						}
						dcs.setExamCode(cdsDt.DiabetesComplicationScreening.ExamCode.X_11397_7);
						if (Util.yn(meas.getDataField())==cdsDt.YnIndicatorsimple.N) {
							exportError.add("Patient "+demoNo+" didn't do Diabetes Complications Screening (Foot Exam) on "+UtilDateUtilities.DateToString(meas.getDateObserved(),"yyyy-MM-dd"));
						}
						addOneEntry(CAREELEMENTS);
					} else if (meas.getType().equals("FTLS")) { // Foot Exam Test Loss of Sensation (Neurological Exam)
						cdsDt.DiabetesComplicationScreening dcs = careElm.addNewDiabetesComplicationsScreening();
						dcs.setDate(Util.calDate(meas.getDateObserved()));
						if (meas.getDateObserved()==null) {
							exportError.add("Error! No Date for Diabetes Complication Screening on Neurological Exam (id="+meas.getId()+") for Patient "+demoNo);
						}
						dcs.setExamCode(cdsDt.DiabetesComplicationScreening.ExamCode.NEUROLOGICAL_EXAM);
						if (Util.yn(meas.getDataField())==cdsDt.YnIndicatorsimple.N) {
							exportError.add("Patient "+demoNo+" didn't do Diabetes Complications Screening (Neurological Exam) on "+UtilDateUtilities.DateToString(meas.getDateObserved(),"yyyy-MM-dd"));
						}
						addOneEntry(CAREELEMENTS);
					} else if (meas.getType().equals("CGSD")) { //Collaborative Goal Setting
						cdsDt.DiabetesSelfManagementCollaborative dsco = careElm.addNewDiabetesSelfManagementCollaborative();
						dsco.setDate(Util.calDate(meas.getDateObserved()));
						if (meas.getDateObserved()==null) {
							exportError.add("Error! No Date for Diabetes Self-management Collaborative Goal Setting (id="+meas.getId()+") for Patient "+demoNo);
						}
						dsco.setCodeValue(cdsDt.DiabetesSelfManagementCollaborative.CodeValue.X_44943_9);
						dsco.setDocumentedGoals(meas.getDataField());
						addOneEntry(CAREELEMENTS);
					} else if (meas.getType().equals("HYPE")) { //Hypoglycemic Episodes
						cdsDt.HypoglycemicEpisodes he = careElm.addNewHypoglycemicEpisodes();
						he.setDate(Util.calDate(meas.getDateObserved()));
						if (meas.getDateObserved()==null) {
							exportError.add("Error! No Date for Hypoglycemic Episodes (id="+meas.getId()+") for Patient "+demoNo);
						}
						he.setNumOfReportedEpisodes(new BigInteger(meas.getDataField().trim()));
						addOneEntry(CAREELEMENTS);
					}
				}
			}
			exportNo++;


			//export file to temp directory
			try{
				File directory = new File(tmpDir);
				if(!directory.exists()){
					throw new Exception("Temporary Export Directory does not exist!");
				}

				//Standard format for xml exported file : PatientFN_PatientLN_PatientUniqueID_DOB (DOB: ddmmyyyy)
				String expFile = demographic.getFirstName()+"_"+demographic.getLastName();
				expFile += "_"+demoNo;
				expFile += "_"+demographic.getDateOfBirth()+demographic.getMonthOfBirth()+demographic.getYearOfBirth();
				files.add(new File(directory, expFile+".xml"));
			}catch(Exception e){
				logger.error("Error", e);
			}
			try {
					omdCdsDoc.save(files.get(files.size()-1), options);
			} catch (IOException ex) {logger.error("Error", ex);
					throw new Exception("Cannot write .xml file(s) to export directory.\n Please check directory permissions.");
		}
	}

	//create ReadMe.txt & ExportEvent.log
		files.add(makeReadMe(files));
		files.add(makeExportLog(files.get(0).getParentFile()));

	//zip all export files
		String zipName = files.get(0).getName().replace(".xml", ".zip");
	if (setName!=null) zipName = "export_"+setName.replace(" ","")+"_"+UtilDateUtilities.getToday("yyyyMMddHHmmss")+".zip";
//	if (setName!=null) zipName = "export_"+setName.replace(" ","")+"_"+UtilDateUtilities.getToday("yyyyMMddHHmmss")+".pgp";
	if (!Util.zipFiles(files, zipName, tmpDir)) {
			logger.debug("Error! Failed to zip export files");
	}

		if (pgpReady.equals("Yes")) {
			//PGP encrypt zip file
			PGPEncrypt pgp = new PGPEncrypt();
			if (pgp.encrypt(zipName, tmpDir)) {
				
				// Sharing Center - Skip download if sharing with affinity domain
				if (request.getParameter("SendToAffinityDomain") == null) {
					Util.downloadFile(zipName+".pgp", tmpDir, response);
					Util.cleanFile(zipName+".pgp", tmpDir);
					ffwd = "success";
				} else {
					// Sharing Center - Change the forward (redirect) to the affinity domain export page
					ffwd = "sendToAffinityDomain";
				}
				
			} else {
				request.getSession().setAttribute("pgp_ready", "No");
			}
		} else {
			logger.info("Warning: PGP Encryption NOT available - unencrypted file exported!");
			
			// Sharing Center - Skip download if sharing with affinity domain
			if (request.getParameter("SendToAffinityDomain") == null) {
				Util.downloadFile(zipName, tmpDir, response);
				ffwd = "success";
			} else {
				// Sharing Center - Change the forward (redirect) to the affinity domain export page
				ffwd = "sendToAffinityDomain";
			}
			
		}
		
		// Sharing Center - Store the exported data for later retrieval while sending to the affinity domain
		if (ffwd.equalsIgnoreCase("sendToAffinityDomain")) {
			String exportFile = Util.fixDirName(tmpDir) + zipName;
			
			DemographicExportDao demographicExportDao = SpringUtils.getBean(DemographicExportDao.class);
			DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
			
			DemographicExport demographicExport = new DemographicExport();
			byte[] data = FileUtils.readFileToByteArray(new File(exportFile));
			demographicExport.setDocument(data);
			demographicExport.setDemographic(demographicManager.getDemographic(loggedInInfo, demographicNo));
			demographicExport.setDocumentType(DocumentType.CDS.name());
			
			DemographicExport export = demographicExportDao.saveEntity(demographicExport);
			documentExportId = export.getId();
		}

		//Remove zip & export files from temp dir
		Util.cleanFile(zipName, tmpDir);
		Util.cleanFiles(files);
	}
			break;
		case E2E:
			if (!Util.checkDir(tmpDir)) {
				logger.debug("Error! Cannot write to TMP_DIR - Check oscar.properties or dir permissions.");
			} else {
				ArrayList<File> files = new ArrayList<File>();
				StringBuilder exportLog = new StringBuilder();
				for (String demoNo : list) {
					if (StringUtils.empty(demoNo)) {
						String msg = "Error! No Demographic Number";
						logger.error(msg);
						exportLog.append(msg);
						continue;
					}

					// Populate Clinical Document
					ClinicalDocument clinicalDocument = E2ECreator.createEmrConversionDocument(Integer.parseInt(demoNo));
					if(clinicalDocument == null) {
						String msg = "[Demo ".concat(demoNo).concat("] Not active or failed to populate");
						logger.info(msg);
						exportLog.append(msg);
						continue;
					}

					// Output Clinical Document as String
					String output = EverestUtils.generateDocumentToString(clinicalDocument, true);

					//export file to temp directory
					try {
						File directory = new File(tmpDir);
						if(!directory.exists()){
							throw new Exception("Temporary Export Directory does not exist!");
						}

						//Standard format for xml exported file : Demographic_PatientUniqueID
						String expFile = "Demographic_".concat(demoNo);
						files.add(new File(directory, expFile+".xml"));
					} catch(Exception e) {
						logger.error("Error", e);
					}
					BufferedWriter out = null;
					try {
						out = new BufferedWriter(new FileWriter(files.get(files.size()-1)));
						out.write(output);
					} catch (IOException e) {
						logger.error("Error", e);
						throw new Exception("Cannot write .xml file(s) to export directory.\nPlease check directory permissions.");
					} finally {
						try {
							out.close();
						} catch(Exception e) {
							//ignore
						}
					}
				}

				// Create Export Log
				try {
					File exportLogFile = new File(files.get(0).getParentFile(), "ExportEvent.log");
					BufferedWriter out = new BufferedWriter(new FileWriter(exportLogFile));
					String pidRange = "Patient ID Range: ".concat(list.get(0));
					pidRange = pidRange.concat("-").concat(list.get(list.size()-1));

					out.write(pidRange.concat(System.getProperty("line.separator")));
					out.write(System.getProperty("line.separator"));
					if(exportLog.toString().length() == 0) {
						out.write("Export contains no errors".concat(System.getProperty("line.separator")));
					} else {
						out.write(exportLog.toString());
					}
					out.close();

					files.add(exportLogFile);
				} catch (IOException e) {
					logger.error("Error", e);
					throw new Exception("Cannot write .xml file(s) to export directory.\nPlease check directory permissions.");
				}

				// Zip all export files
				String zipName = files.get(0).getName().replace(".xml", ".zip");
				if (setName!=null) zipName = "export_"+setName.replace(" ","")+"_"+UtilDateUtilities.getToday("yyyyMMddHHmmss")+".zip";
				//	if (setName!=null) zipName = "export_"+setName.replace(" ","")+"_"+UtilDateUtilities.getToday("yyyyMMddHHmmss")+".pgp";
				if (!Util.zipFiles(files, zipName, tmpDir)) {
					logger.debug("Error! Failed to zip export files");
				}

				// Apply PGP if installed
				if (pgpReady.equals("Yes")) {
					//PGP encrypt zip file
					PGPEncrypt pgp = new PGPEncrypt();
					if (pgp.encrypt(zipName, tmpDir)) {
						Util.downloadFile(zipName+".pgp", tmpDir, response);
						Util.cleanFile(zipName+".pgp", tmpDir);
						ffwd = "success";
					} else {
						request.getSession().setAttribute("pgp_ready", "No");
					}
				} else {
					logger.info("Warning: PGP Encryption NOT available - unencrypted file exported!");
					Util.downloadFile(zipName, tmpDir, response);
					ffwd = "success";
				}

				// Remove zip & export files from temp dir
				Util.cleanFile(zipName, tmpDir);
				Util.cleanFiles(files);
			}
			break;
		default:
			break;
	}

	if (ffwd.equalsIgnoreCase("sendToAffinityDomain")) {
		return new ActionForward(mapping.findForward("sendToAffinityDomain").getPath() + "?affinityDomain=" + request.getParameter("affinityDomain") + "&demoId=" + demographicNo + "&docNo=" + documentExportId + "&type=" + DocumentType.CDS.name(), false);
	} else {
		return mapping.findForward(ffwd);
	}
}

	File makeReadMe(ArrayList<File> fs) throws IOException {
	File readMe = new File(fs.get(0).getParentFile(), "ReadMe.txt");
	BufferedWriter out = new BufferedWriter(new FileWriter(readMe));
	out.write("Physician Group					: ");
	out.write(new ClinicData().getClinicName());
	out.newLine();
	out.write("CMS Vendor, Product & Version	  : ");
	String vendor = oscarProperties.getProperty("Vendor_Product");
	if (StringUtils.empty(vendor)) {
		exportError.add("Error! Vendor_Product not defined in oscar.properties");
	} else {
		out.write(vendor);
	}
	out.newLine();
	out.write("Application Support Contact		: ");
	String support = oscarProperties.getProperty("Support_Contact");
	if (StringUtils.empty(support)) {
		exportError.add("Error! Support_Contact not defined in oscar.properties");
	} else {
		out.write(support);
	}
	out.newLine();
	out.write("Date and Time stamp				: ");
	out.write(UtilDateUtilities.getToday("yyyy-MM-dd hh:mm:ss aa"));
	out.newLine();
	out.write("Total patients files extracted	 : ");
	out.write(String.valueOf(fs.size()));
	out.newLine();
	out.write("Number of errors				   : ");
	out.write(String.valueOf(exportError.size()));
	if (exportError.size()>0) out.write(" (See ExportEvent.log for detail)");
	out.newLine();
	out.write("Patient ID range				   : ");
	out.write(getIDInExportFilename(fs.get(0).getName()));
	out.write("-");
	out.write(getIDInExportFilename(fs.get(fs.size()-1).getName()));
	out.newLine();
	out.close();

	return readMe;
	}

	File makeExportLog(File dir) throws IOException {
			String[][] keyword = new String[2][16];
			keyword[0][0] = PATIENTID;
			keyword[1][0] = "ID";
			keyword[0][1] = " "+PERSONALHISTORY;
			keyword[1][1] = " History";
			keyword[0][2] = " "+FAMILYHISTORY;
			keyword[1][2] = " History";
			keyword[0][3] = " "+PASTHEALTH;
			keyword[1][3] = " Health";
			keyword[0][4] = " "+PROBLEMLIST;
			keyword[1][4] = " List";
			keyword[0][5] = " "+RISKFACTOR;
			keyword[1][5] = " Factor";
			keyword[0][6] = " "+ALLERGY;
			keyword[0][7] = " "+MEDICATION;
			keyword[0][8] = " "+IMMUNIZATION;
			keyword[0][9] = " "+LABS;
			keyword[0][10] = " "+APPOINTMENT;
			keyword[0][11] = " "+CLINICALNOTE;
			keyword[1][11] = " Note";
			keyword[0][12] = "	Report	";
			keyword[1][12] = " "+REPORTTEXT;
			keyword[1][13] = " "+REPORTBINARY;
			keyword[0][14] = " "+CAREELEMENTS;
			keyword[1][14] = " Elements";
			keyword[0][15] = " "+ALERT;

			for (int i=0; i<keyword[0].length; i++) {
				if (keyword[0][i].contains("Report")) {
					keyword[0][i+1] = "Report2";
					i++;
					continue;
				}
				if (keyword[1][i]==null) keyword[1][i] = " ";
				if (keyword[0][i].length()>keyword[1][i].length()) keyword[1][i] = fillUp(keyword[1][i], ' ', keyword[0][i].length());
				if (keyword[0][i].length()<keyword[1][i].length()) keyword[0][i] = fillUp(keyword[0][i], ' ', keyword[1][i].length());
			}

			File exportLog = new File(dir, "ExportEvent.log");
			BufferedWriter out = new BufferedWriter(new FileWriter(exportLog));
			int tableWidth = 0;
			for (int i=0; i<keyword.length; i++) {
				for (int j=0; j<keyword[i].length; j++) {
					out.write(keyword[i][j]+" |");
					if (keyword[i][j].trim().equals("Report")) j++;
					if (i==1) tableWidth += keyword[i][j].length()+2;
				}
				out.newLine();
			}
			out.write(fillUp("",'-',tableWidth)); out.newLine();

			//general log data
			if (exportNo==0) exportNo = 1;
			for (int i=0; i<exportNo; i++) {

				for (int j=0; j<keyword[0].length; j++) {
					String category = keyword[0][j].trim();
					if (category.contains("Report")) category = keyword[1][j].trim();
					Integer occurs = entries.get(category+i);
					if (occurs==null) occurs = 0;
					out.write(fillUp(occurs.toString(), ' ', keyword[1][j].length()));
					out.write(" |");
				}
				out.newLine();
				out.write(fillUp("",'-',tableWidth)); out.newLine();
			}
			out.newLine();
			out.newLine();
			out.newLine();

			//error log
			out.write("Errors/Notes");
			out.newLine();
			out.write(fillUp("",'-',tableWidth)); out.newLine();

			//write any error that has occurred
			if (exportError.size()>0) {
				out.write(exportError.get(0));
				out.newLine();
				for (int j=1; j<exportError.size(); j++) {
					out.write("	 ");
					out.write(exportError.get(j));
					out.newLine();
				}
				out.write(fillUp("",'-',tableWidth)); out.newLine();
			}
			out.write(fillUp("",'-',tableWidth)); out.newLine();

			out.close();
			exportNo = 0;
			return exportLog;
	}





	//------------------------------------------------------------

	private String getIDInExportFilename(String filename) {
		if (filename==null) return null;

		//PatientFN_PatientLN_PatientUniqueID_DOB
		String[] sects = filename.split("_");
		if (sects.length==4) return sects[2];

		return null;
	}

	private String cutExt(String filename) {
	if (StringUtils.empty(filename)) return "";
	String[] parts = filename.split(".");
	if (parts.length>1) return "."+parts[parts.length-1];
	else return "";
	}

	private String fillUp(String tobefilled, char c, int size) {
	if (size>=tobefilled.length()) {
		int fill = size-tobefilled.length();
		for (int i=0; i<fill; i++) tobefilled += c;
	}
	return tobefilled;
	}

	private void addOneEntry(String category) {
		if (StringUtils.empty(category)) return;

		Integer n = entries.get(category+exportNo);
		n = n==null ? 1 : n+1;
		entries.put(category+exportNo, n);
	}

	private String getNonDumpNote(Integer tableName, Long tableId, String otherId) {
		String note = null;

		List<CaseManagementNoteLink> cmll;
		if (StringUtils.empty(otherId))
			cmll = cmm.getLinkByTableIdDesc(tableName, tableId);
		else
			cmll = cmm.getLinkByTableIdDesc(tableName, tableId, otherId);

		for (CaseManagementNoteLink cml : cmll) {
			CaseManagementNote n = cmm.getNote(cml.getNoteId().toString());
			if (n.getNote()!=null && !n.getNote().startsWith("imported.cms4.2011.06")) {//not from dumpsite
				note = n.getNote();
				break;
			}
		}
		return note;
	}

	private String formatHrmEnum(String hrmEnum) {
		if (StringUtils.empty(hrmEnum)) return null;

		hrmEnum = hrmEnum.replace("_", " ");
		hrmEnum = hrmEnum.toLowerCase();

		String hrmEnumF = hrmEnum.substring(0,1).toUpperCase();
		for (int i=1; i<hrmEnum.length(); i++) {
			hrmEnumF += hrmEnum.substring(i,i+1);
			if (hrmEnum.substring(i,i+1).equals(" ") && (i+1)<hrmEnum.length()) {
				hrmEnumF += hrmEnum.substring(i+1, i+2).toUpperCase();
				i++;
			}
		}

		if (hrmEnumF.equals("Medical Records Report")) hrmEnumF = "Medical Record Report"; //HRM & CDS class names not match
		return hrmEnumF;
	}

	private void addDemographicContacts(LoggedInInfo loggedInInfo, String demoNo, Demographics demo) {
		List<DemographicContact> demoContacts = contactDao.findByDemographicNo(Integer.valueOf(demoNo));
		DemographicContact demoContact;

		//create a list of contactIds
		String[] contactId = new String[demoContacts.size()];
		for (int j=0; j<demoContacts.size(); j++) {
			demoContact = demoContacts.get(j);
			if (demoContact!=null) contactId[j] = demoContact.getContactId();
		}

	LoopContacts:
		for (int j=0; j<demoContacts.size(); j++) {
			if (StringUtils.filled(contactId[j])) {
				//if this contact is a duplicate, skip
				//multiple contact purposes are processed later
				for (int k=0; k<j; k++) {
					if (contactId[j].equals(contactId[k])) {
						continue LoopContacts;
					}
				}
				demoContact = demoContacts.get(j);
				if (demoContact==null) continue;

				Demographics.Contact contact = demo.addNewContact();

				String ec=null, sdm=null, rel=null, contactNote=null;
				//process multiple contact purposes
				for (int k=j; k<demoContacts.size(); k++) {
					demoContact = demoContacts.get(k);
					if (demoContact==null) continue;
					if (!contactId[j].equals(demoContact.getContactId())) continue;

					rel = demoContact.getRole();
					if (StringUtils.empty(ec)) ec = demoContact.getEc();
					if (StringUtils.empty(sdm)) sdm = demoContact.getSdm();

					fillContactPurpose(rel, contact);
					contactNote = Util.addLine(contactNote, demoContact.getNote());

				}
				if ("true".equalsIgnoreCase(ec)) {
					contact.addNewContactPurpose().setPurposeAsEnum(cdsDt.PurposeEnumOrPlainText.PurposeAsEnum.EC);
				}
				if ("true".equalsIgnoreCase(sdm)) {
					contact.addNewContactPurpose().setPurposeAsEnum(cdsDt.PurposeEnumOrPlainText.PurposeAsEnum.SDM);
				}
				if (StringUtils.filled(contactNote)) contact.setNote(contactNote);

				fillContactInfo(loggedInInfo, contact, contactId[j], demoNo, j);
			}
		}
	}


	private void addDemographicRelationships(LoggedInInfo loggedInInfo, String demoNo, Demographics demo) {
		DemographicRelationship demographicRelationship = new DemographicRelationship();
		List<Map<String,String>> demographicRelationships = demographicRelationship.getDemographicRelationships(demoNo);
		Map<String,String> demoRel;

		//create a list of contactIds
		String[] contactId = new String[demographicRelationships.size()];
		for (int j=0; j<demographicRelationships.size(); j++) {
			demoRel = demographicRelationships.get(j);
			if (demoRel!=null) contactId[j] = demoRel.get("demographic_no");
		}

	LoopContacts:
		for (int j=0; j<demographicRelationships.size(); j++) {
			if (StringUtils.filled(contactId[j])) {
				//if this contact is a duplicate, skip
				//multiple contact purposes are processed later
				for (int k=0; k<j; k++) {
					if (contactId[j].equals(contactId[k])) {
						continue LoopContacts;
					}
				}
				demoRel = demographicRelationships.get(j);
				if (demoRel==null) continue;

				Demographics.Contact contact = demo.addNewContact();

				String ec=null, sdm=null, rel=null, contactNote=null;
				//process multiple contact purposes
				for (int k=j; k<demographicRelationships.size(); k++) {
					demoRel = demographicRelationships.get(k);
					if (demoRel==null) continue;
					if (!contactId[j].equals(demoRel.get("demographic_no"))) continue;

					rel = demoRel.get("relation");
					if (StringUtils.empty(ec) || "0".equals(ec)) ec = demoRel.get("emergency_contact");
					if (StringUtils.empty(sdm) || "0".equals(sdm)) sdm = demoRel.get("sub_decision_maker");

					fillContactPurpose(rel, contact);
					contactNote = Util.addLine(contactNote, demoRel.get("notes"));

				}
				if ("1".equals(ec)) {
					contact.addNewContactPurpose().setPurposeAsEnum(cdsDt.PurposeEnumOrPlainText.PurposeAsEnum.EC);
				}
				if ("1".equals(sdm)) {
					contact.addNewContactPurpose().setPurposeAsEnum(cdsDt.PurposeEnumOrPlainText.PurposeAsEnum.SDM);
				}
				if (StringUtils.filled(contactNote)) contact.setNote(contactNote);

				fillContactInfo(loggedInInfo, contact, contactId[j], demoNo, j);
			}
		}
	}

	private void fillContactInfo(LoggedInInfo loggedInInfo, Demographics.Contact contact, String contactId, String demoNo, int index) {

		org.oscarehr.common.model.Demographic relDemo = new DemographicData().getDemographic(loggedInInfo, contactId);
		HashMap<String,String> relDemoExt = new HashMap<String,String>();
		relDemoExt.putAll(demographicExtDao.getAllValuesForDemo(Integer.parseInt(contactId)));

		Util.writeNameSimple(contact.addNewName(), relDemo.getFirstName(), relDemo.getLastName());
		if (StringUtils.empty(relDemo.getFirstName())) {
			exportError.add("Error! No First Name for contact ("+index+") for Patient "+demoNo);
		}
		if (StringUtils.empty(relDemo.getLastName())) {
			exportError.add("Error! No Last Name for contact ("+index+") for Patient "+demoNo);
		}

		if (StringUtils.filled(relDemo.getEmail())) contact.setEmailAddress(relDemo.getEmail());

		boolean phoneExtTooLong = false;
		if (phoneNoValid(relDemo.getPhone())) {
			phoneExtTooLong = addPhone(relDemo.getPhone(), relDemoExt.get("hPhoneExt"), cdsDt.PhoneNumberType.R, contact.addNewPhoneNumber());
			if (phoneExtTooLong) {
				exportError.add("Home phone extension too long, export trimmed for contact ("+(index+1)+") of Patient "+demoNo);
			}
		}

		if (phoneNoValid(relDemo.getPhone2())) {
			phoneExtTooLong = addPhone(relDemo.getPhone2(), relDemoExt.get("wPhoneExt"), cdsDt.PhoneNumberType.W, contact.addNewPhoneNumber());
			if (phoneExtTooLong) {
				exportError.add("Work phone extension too long, export trimmed for contact ("+(index+1)+") of Patient "+demoNo);
			}
		}

		if (phoneNoValid(relDemoExt.get("demo_cell"))) {
			addPhone(relDemoExt.get("demo_cell"), null, cdsDt.PhoneNumberType.C, contact.addNewPhoneNumber());
		}
	}

	private void fillContactPurpose(String rel, Demographics.Contact contact) {
		if (StringUtils.filled(rel)) {
			cdsDt.PurposeEnumOrPlainText contactPurpose = contact.addNewContactPurpose();
			if (rel.equals("Next of Kin")) contactPurpose.setPurposeAsEnum(cdsDt.PurposeEnumOrPlainText.PurposeAsEnum.NK);
			else if (rel.equals("Administrative Staff")) contactPurpose.setPurposeAsEnum(cdsDt.PurposeEnumOrPlainText.PurposeAsEnum.AS);
			else if (rel.equals("Care Giver")) contactPurpose.setPurposeAsEnum(cdsDt.PurposeEnumOrPlainText.PurposeAsEnum.CG);
			else if (rel.equals("Power of Attorney")) contactPurpose.setPurposeAsEnum(cdsDt.PurposeEnumOrPlainText.PurposeAsEnum.PA);
			else if (rel.equals("Insurance")) contactPurpose.setPurposeAsEnum(cdsDt.PurposeEnumOrPlainText.PurposeAsEnum.IN);
			else if (rel.equals("Guarantor")) contactPurpose.setPurposeAsEnum(cdsDt.PurposeEnumOrPlainText.PurposeAsEnum.GT);
			else contactPurpose.setPurposeAsPlainText(rel);
		}
	}

	private boolean phoneNoValid(String phoneNo) {
		phoneNo = Util.onlyNum(phoneNo);
		if (StringUtils.filled(phoneNo) && phoneNo.length()>=7)
			return true;
		else
			return false;
	}

	private boolean addPhone(String phoneNo, String phoneExt, cdsDt.PhoneNumberType.Enum phoneNoType, cdsDt.PhoneNumber cdsDtPhoneNumber) {
		boolean extensionTooLong = false;

		cdsDtPhoneNumber.setPhoneNumber(Util.onlyNum(phoneNo));
		cdsDtPhoneNumber.setPhoneNumberType(phoneNoType);
		if (phoneExt!=null) {
			if (phoneExt.length()>5) {
				phoneExt = phoneExt.substring(0,5);
				extensionTooLong = true;
			}
			cdsDtPhoneNumber.setExtension(phoneExt);
		}
		return extensionTooLong;
	}

	/*
	private void exportLabResult(LabMeasurements labMea, LaboratoryResults labResults, String demoNo) {
		HashMap<String,String> labMeaValues = new HashMap<String,String>();
		
		labMeaValues.put("identifier", labMea.getExtVal("identifier"));
		labMeaValues.put("name_internal", labMea.getExtVal("name_internal"));
		labMeaValues.put("name", labMea.getExtVal("name"));
		labMeaValues.put("labname", labMea.getExtVal("labname"));
		labMeaValues.put("datetime", labMea.getExtVal("datetime"));
		labMeaValues.put("abnormal", labMea.getExtVal("abnormal"));
		labMeaValues.put("measureData", labMea.getMeasure().getDataField());
		labMeaValues.put("unit", labMea.getExtVal("unit"));
		labMeaValues.put("accession", labMea.getExtVal("accession"));
		labMeaValues.put("comments", labMea.getExtVal("comments"));
		labMeaValues.put("range", labMea.getExtVal("range"));
		labMeaValues.put("minimum", labMea.getExtVal("minimum"));
		labMeaValues.put("maximum", labMea.getExtVal("maximum"));
		labMeaValues.put("request_datetime", labMea.getExtVal("request_datetime"));
		labMeaValues.put("olis_status", labMea.getExtVal("olis_status"));
		labMeaValues.put("lab_no", labMea.getExtVal("lab_no"));
		labMeaValues.put("other_id", labMea.getExtVal("other_id"));
		
		exportLabResult(labMeaValues, labResults, demoNo);
	}
	*/
	
	private void exportLabResult(HashMap<String,String> labMea, LaboratoryResults labResults, String demoNo) {

		//lab test code, test name, test name reported by lab
		if (StringUtils.filled(labMea.get("identifier"))) labResults.setLabTestCode(labMea.get("identifier"));
		if (StringUtils.filled(labMea.get("name_internal"))) labResults.setTestName(labMea.get("name_internal"));
		if (StringUtils.filled(labMea.get("name"))) labResults.setTestNameReportedByLab(labMea.get("name"));

		//laboratory name
		labResults.setLaboratoryName(StringUtils.noNull(labMea.get("labname")));
		addOneEntry(LABS);
		if (StringUtils.empty(labResults.getLaboratoryName())) {
			exportError.add("Error! No Laboratory Name for Lab Test "+labResults.getLabTestCode()+" for Patient "+demoNo);
		}

		// lab collection datetime
		cdsDt.DateTimeFullOrPartial collDate = labResults.addNewCollectionDateTime();
		String sDateTime = labMea.get("datetime");
		if (StringUtils.filled(sDateTime)) {
			collDate.setFullDateTime(Util.calDate(sDateTime));
		} else {
			exportError.add("Error! No Collection Datetime for Lab Test "+labResults.getLabTestCode()+" for Patient "+demoNo);
			collDate.setFullDate(Util.calDate("0001-01-01"));
		}

		//lab normal/abnormal flag
		labResults.setResultNormalAbnormalFlag(cdsDt.ResultNormalAbnormalFlag.U);
		String abnormalFlag = StringUtils.noNull(labMea.get("abnormal"));
		if (abnormalFlag.equals("A") || abnormalFlag.equals("L")) labResults.setResultNormalAbnormalFlag(cdsDt.ResultNormalAbnormalFlag.Y);
		if (abnormalFlag.equals("N")) labResults.setResultNormalAbnormalFlag(cdsDt.ResultNormalAbnormalFlag.N);

		//lab unit of measure
		String measureData = StringUtils.noNull(labMea.get("measureData"));
		if (StringUtils.filled(measureData)) {
			LaboratoryResults.Result result = labResults.addNewResult();
			if (measureData.length()>120) {
				measureData = measureData.substring(0, 120);
				exportError.add("Error! Result text length > 120 - truncated; Lab Test "+labResults.getLabTestCode()+" for Patient "+demoNo);
			}
			result.setValue(measureData);
			measureData = labMea.get("unit");
			if (StringUtils.filled(measureData)) result.setUnitOfMeasure(measureData);
		}

		//lab accession number
		String accessionNo = StringUtils.noNull(labMea.get("accession"));
		if (StringUtils.filled(accessionNo)) {
			labResults.setAccessionNumber(accessionNo);
		}

		//notes from lab
		String labComments = StringUtils.noNull(labMea.get("comments"));
		if (StringUtils.filled(labComments)) {
			labResults.setNotesFromLab(Util.replaceTags(labComments));
		}

		//lab reference range
		String range = StringUtils.noNull(labMea.get("range"));
		String min = StringUtils.noNull(labMea.get("minimum"));
		String max = StringUtils.noNull(labMea.get("maximum"));
		LaboratoryResults.ReferenceRange refRange = labResults.addNewReferenceRange();
		if (StringUtils.filled(range)) refRange.setReferenceRangeText(range);
		else {
			if (StringUtils.filled(min)) refRange.setLowLimit(min);
			if (StringUtils.filled(max)) refRange.setHighLimit(max);
		}

		//lab requisition datetime
		
		String reqDate = labMea.get("request_datetime");
		if (StringUtils.filled(reqDate)) labResults.addNewLabRequisitionDateTime().setFullDateTime(Util.calDate(reqDate));

		//OLIS test result status
		String olis_status = labMea.get("olis_status");
		if (StringUtils.filled(olis_status)) labResults.setOLISTestResultStatus(olis_status);

		Integer labTable = CaseManagementNoteLink.LABTEST;
		String lab_no = labMea.get("lab_no");
		if (StringUtils.empty(lab_no)) {
			lab_no = labMea.get("lab_ppid");
			labTable = CaseManagementNoteLink.LABTEST2;
		}
		if (StringUtils.filled(lab_no)) {

			//lab annotation
			String other_id = StringUtils.noNull(labMea.get("other_id"));
			String annotation = getNonDumpNote(labTable, Long.valueOf(lab_no), other_id);
			if (StringUtils.filled(annotation)) labResults.setPhysiciansNotes(annotation);

//		  String info = labRoutingInfo.get("comment"); <--for whole report, may refer to >1 lab results


			//lab reviewer
			HashMap<String,Object> labRoutingInfo = new HashMap<String,Object>();
			if (labTable.equals(CaseManagementNoteLink.LABTEST))
				labRoutingInfo.putAll(ProviderLabRouting.getInfo(lab_no, "HL7"));
			else
				labRoutingInfo.putAll(ProviderLabRouting.getInfo(lab_no, "CML"));

			String timestamp = labRoutingInfo.get("timestamp").toString();
			if (UtilDateUtilities.StringToDate(timestamp,"yyyy-MM-dd HH:mm:ss")!=null) {
				LaboratoryResults.ResultReviewer reviewer = labResults.addNewResultReviewer();
				reviewer.addNewDateTimeResultReviewed().setFullDateTime(Util.calDate(timestamp));

				//reviewer name
				cdsDt.PersonNameSimple reviewerName = reviewer.addNewName();
				String lab_provider_no = (String)labRoutingInfo.get("provider_no");
				if (!"0".equals(lab_provider_no)) {
					ProviderData pvd = new ProviderData(lab_provider_no);
					Util.writeNameSimple(reviewerName, pvd.getFirst_name(), pvd.getLast_name());
					if (StringUtils.noNull(pvd.getOhip_no()).length()<=6) reviewer.setOHIPPhysicianId(pvd.getOhip_no());
				}
			}
		}
	}
	
	private Float getDosageValue(String dosage) {
		String[] dosageBreak = getDosageMultiple1st(dosage).split(" ");

		if (NumberUtils.isNumber(dosageBreak[0])) return Float.parseFloat(dosageBreak[0]);
		else return 0f;
	}

	private String getDosageUnit(String dosage) {
		String[] dosageBreak = getDosageMultiple1st(dosage).split(" ");

		if (dosageBreak.length==2) return dosageBreak[1].trim();
		else return null;
	}

	private String getDosageMultiple1st(String dosage) {
		if (StringUtils.empty(dosage)) return "";

		String[] dosageMultiple = dosage.split("/");
		return dosageMultiple[0].trim();
	}
}
