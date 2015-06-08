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

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;
import org.apache.xmlbeans.XmlOptions;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.casemgmt.dao.CaseManagementNoteExtDAO;
import org.oscarehr.casemgmt.dao.IssueDAO;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementNoteExt;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.common.dao.AllergyDao;
import org.oscarehr.common.dao.ClinicDAO;
import org.oscarehr.common.dao.DataExportDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.Hl7TextInfoDao;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.dao.PartialDateDao;
import org.oscarehr.common.dao.PreventionDao;
import org.oscarehr.common.dao.PreventionExtDao;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.common.model.DataExport;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.PartialDate;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.oscarEncounter.oscarMeasurements.data.ImportExportMeasurements;
import oscar.oscarEncounter.oscarMeasurements.data.LabMeasurements;
import oscar.oscarEncounter.oscarMeasurements.data.Measurements;
import oscar.oscarReport.data.DemographicSets;
import oscar.oscarRx.data.RxPrescriptionData;
import oscar.util.StringUtils;
import oscar.util.UtilDateUtilities;
import cdsDtCihi.DateFullOrPartial;
import cdsDtCihi.HealthCard;
import cdsDtCihi.PostalZipCode;
import cdsDtCihi.StandardCoding;
import cdsDtCihi.YnIndicator;
import cdsDtCihi.YnIndicatorAndBlank;
import cdscihi.AllergiesAndAdverseReactionsDocument.AllergiesAndAdverseReactions;
import cdscihi.AppointmentsDocument.Appointments;
import cdscihi.CareElementsDocument.CareElements;
import cdscihi.CiHiCdsDocument;
import cdscihi.CiHiCdsDocument.CiHiCds;
import cdscihi.DemographicsDocument.Demographics;
import cdscihi.DemographicsDocument.Demographics.PersonStatusCode;
import cdscihi.ExtractInformationDocument.ExtractInformation;
import cdscihi.FamilyHistoryDocument.FamilyHistory;
import cdscihi.ImmunizationsDocument.Immunizations;
import cdscihi.LaboratoryResultsDocument.LaboratoryResults;
import cdscihi.MedicationsAndTreatmentsDocument.MedicationsAndTreatments;
import cdscihi.PatientRecordDocument.PatientRecord;
import cdscihi.ProblemListDocument.ProblemList;
import cdscihi.ProcedureDocument.Procedure;
import cdscihi.RiskFactorsDocument.RiskFactors;

public class CihiExportAction extends DispatchAction {
	private ClinicDAO clinicDAO;
	private DataExportDao dataExportDAO;
	private DemographicDao demographicDao;
	private OscarAppointmentDao oscarAppointmentDao;
	private IssueDAO issueDAO;
	private CaseManagementNoteDAO caseManagementNoteDAO;
	private CaseManagementNoteExtDAO caseManagementNoteExtDAO;
	private Hl7TextInfoDao hl7TextInfoDAO;
	private PreventionDao preventionDao;
	private PreventionExtDao preventionExtDao;

	private Logger log = MiscUtils.getLogger();

	private static final PartialDateDao partialDateDao = (PartialDateDao) SpringUtils.getBean("partialDateDao");

	public void setDemographicDao(DemographicDao demographicDao) {
	    this.demographicDao = demographicDao;
    }

	public DemographicDao getDemographicDao() {
	    return demographicDao;
    }

	public void setPreventionDao(PreventionDao preventionDao) {
	    this.preventionDao = preventionDao;
    }

	public PreventionDao getPreventionDao() {
	    return preventionDao;
    }

	public void setPreventionExtDao(PreventionExtDao preventionExtDao) {
	    this.preventionExtDao = preventionExtDao;
    }

	public PreventionExtDao getPreventionExtDao() {
	    return preventionExtDao;
    }

	public void setHl7TextInfoDAO(Hl7TextInfoDao hl7TextInfoDAO) {
	    this.hl7TextInfoDAO = hl7TextInfoDAO;
    }

	public Hl7TextInfoDao getHl7TextInfoDAO() {
	    return hl7TextInfoDAO;
    }

	public void setCaseManagementNoteExtDAO(CaseManagementNoteExtDAO caseManagementNoteExtDAO) {
	    this.caseManagementNoteExtDAO = caseManagementNoteExtDAO;
    }

	public CaseManagementNoteExtDAO getCaseManagementNoteExtDAO() {
	    return caseManagementNoteExtDAO;
    }

	public void setCaseManagementNoteDAO(CaseManagementNoteDAO caseManagementNoteDAO) {
	    this.caseManagementNoteDAO = caseManagementNoteDAO;
    }

	public CaseManagementNoteDAO getCaseManagementNoteDAO() {
	    return caseManagementNoteDAO;
    }

	public void setIssueDAO(IssueDAO issueDAO) {
	    this.issueDAO = issueDAO;
    }

	public IssueDAO getIssueDAO() {
	    return issueDAO;
    }

	public void setOscarAppointmentDao(OscarAppointmentDao appointmentDao) {
	    this.oscarAppointmentDao = appointmentDao;
    }

	public OscarAppointmentDao getOscarAppointmentDao() {
	    return oscarAppointmentDao;
    }

	public DataExportDao getDataExportDAO() {
    	return dataExportDAO;
    }

	public void setDataExportDAO(DataExportDao dataExportDAO) {
    	this.dataExportDAO = dataExportDAO;
    }

	public ClinicDAO getClinicDAO() {
    	return clinicDAO;
    }

	public void setClinicDAO(ClinicDAO clinicDAO) {
    	this.clinicDAO = clinicDAO;
    }

	public ActionForward getFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		OscarProperties properties = OscarProperties.getInstance();
		String zipName = request.getParameter("zipFile");
		String dir = properties.getProperty("DOCUMENT_DIR");
		Util.downloadFile(zipName, dir, response);
		return null;

	}

	@SuppressWarnings("rawtypes")
    @Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		OscarProperties properties = OscarProperties.getInstance();
		Clinic clinic = clinicDAO.getClinic();
		List<DataExport> dataExportList = dataExportDAO.findAllByType(DataExportDao.CIHI_OMD4);
		dataExportList.addAll(dataExportDAO.findAllByType(DataExportDao.CIHI_PHC_VRS));
		Collections.sort(dataExportList);

		DynaValidatorForm frm = (DynaValidatorForm)form;
		String patientSet = frm.getString("patientSet");

		if( patientSet == null || "".equals(patientSet) || patientSet.equals("-1")) {
			frm.set("orgName", clinic.getClinicName());
			frm.set("vendorId", properties.getProperty("vendorId", ""));
			frm.set("vendorBusinessName", properties.getProperty("vendorBusinessName", ""));
			frm.set("vendorCommonName", properties.getProperty("vendorCommonName", ""));
			frm.set("vendorSoftware", properties.getProperty("softwareName", ""));
			frm.set("vendorSoftwareCommonName", properties.getProperty("softwareCommonName", ""));
			frm.set("vendorSoftwareVer", properties.getProperty("version", ""));
			frm.set("installDate", properties.getProperty("buildDateTime", ""));

			if( dataExportList.size() > 0 ) {
				DataExport dataExport = dataExportList.get(dataExportList.size()-1);
				frm.set("contactLName",dataExport.getContactLName());
				frm.set("contactFName", dataExport.getContactFName());
				frm.set("contactPhone", dataExport.getContactPhone());
				frm.set("contactEmail", dataExport.getContactEmail());
				frm.set("contactUserName", dataExport.getUser());
			}
			request.setAttribute("dataExportList", dataExportList);
			return mapping.findForward("display");
		}


		//Create export files
	    String tmpDir = properties.getProperty("TMP_DIR");
	    if (!Util.checkDir(tmpDir)) {
	    	tmpDir = System.getProperty("java.io.tmpdir");
	        MiscUtils.getLogger().error("Error! Cannot write to TMP_DIR - Check oscar.properties or dir permissions. Using " + tmpDir);
	    }
	    tmpDir = Util.fixDirName(tmpDir);


	    //grab patient list from set
	    DemographicSets demoSets = new DemographicSets();
		List<String> patientList = demoSets.getDemographicSet(frm.getString("patientSet"));

		//make all xml files, zip them and save to document directory
		String filename = this.make(LoggedInInfo.getLoggedInInfoFromSession(request), frm, patientList, tmpDir);

		//we got this far so save entry to db
		DataExport dataExport = new DataExport();
		dataExport.setContactLName(frm.getString("contactLName"));
		dataExport.setContactFName(frm.getString("contactFName"));
		dataExport.setContactPhone(frm.getString("contactPhone"));
		dataExport.setContactEmail(frm.getString("contactEmail"));
		dataExport.setUser(frm.getString("contactUserName"));
		dataExport.setType(frm.getString("extractType"));
		Timestamp runDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
		dataExport.setDaterun(runDate);
		dataExport.setFile(filename);
		dataExportDAO.persist(dataExport);

		dataExportList.add(dataExport);
		request.setAttribute("dataExportList", dataExportList);
		return mapping.findForward("display");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
    private String make(LoggedInInfo loggedInInfo, DynaValidatorForm frm, List patientList, String tmpDir) throws Exception {
		 HashMap<String,CiHiCdsDocument> xmlMap = new HashMap<String,CiHiCdsDocument>();
		 HashMap<String,String> fileNamesMap = new HashMap<String,String>();
		 String demoNo;
		 Demographic demo;
		 Calendar now = Calendar.getInstance();
		 CiHiCds cihicds;
		 CiHiCdsDocument cihiCdsDocument;

		 for( int idx = 0; idx < patientList.size(); ++idx ) {
			 demoNo = null;
			 Object obj = patientList.get(idx);
			 if (obj instanceof String) {
				demoNo = (String)obj;
			 } else {
				ArrayList<String> l2 = (ArrayList<String>)obj;
				demoNo = l2.get(0);
			 }

			 demo = getDemographicDao().getDemographic(demoNo);
			 if( demo == null ) {
				 continue;
			 }

			 if( !xmlMap.containsKey(demo.getProviderNo())) {
				 cihiCdsDocument = CiHiCdsDocument.Factory.newInstance();
				 cihicds =  cihiCdsDocument.addNewCiHiCds();
				 this.buildExtractInformation(frm, cihicds.addNewExtractInformation(), now);
				 this.buildProvider(demo.getProvider(),cihicds.addNewProvider(), fileNamesMap);
				 xmlMap.put(demo.getProviderNo(), cihiCdsDocument);
			 }
			 else {
				 cihiCdsDocument = xmlMap.get(demo.getProviderNo());
				 cihicds = cihiCdsDocument.getCiHiCds();
			 }

			 PatientRecord patientRecord = cihicds.addNewPatientRecord();
			 this.buildPatientDemographic(demo, patientRecord.addNewDemographics());
			 this.buildAppointment(demo, patientRecord);
			 this.buildFamilyHistory(demo, patientRecord);
			 this.buildOngoingProblems(demo, patientRecord);
			 this.buildRiskFactors(demo, patientRecord);
			 this.buildAllergies(demo, patientRecord);
			 
			 this.buildCareElements(demo, patientRecord);
			 
			 this.buildProcedure(demo, patientRecord);
			 this.buildLaboratoryResults(demo, patientRecord);
			 this.buildMedications(demo, patientRecord);
			 this.buildImmunizations(loggedInInfo, demo, patientRecord);
		 }


		 return this.makeFiles(xmlMap, fileNamesMap, tmpDir);
	}

	private void buildExtractInformation(DynaValidatorForm frm, ExtractInformation info, Calendar now) {
		info.setRunDate(now);
		info.setExtractType((String) frm.get("extractType"));
		info.setOrganizationName(frm.getString("orgName"));
		info.setContactLastName(frm.getString("contactLName"));
		info.setContactFirstName(frm.getString("contactFName"));
		info.setContactEmail(frm.getString("contactEmail"));
		info.setContactUserName(frm.getString("contactUserName"));
		info.setEMRVendorID(frm.getString("vendorId"));
		info.setEMRVendorBusinessName(frm.getString("vendorBusinessName"));
		info.setEMRVendorCommonName(frm.getString("vendorCommonName"));
		info.setEMRSoftwareName(frm.getString("vendorSoftware"));
		info.setEMRSoftwareCommonName(frm.getString("vendorSoftwareCommonName"));
		info.setEMRSoftwareVersionNumber(frm.getString("vendorSoftwareVer"));

		String contactPhoneNumber = Util.onlyNum(frm.getString("contactPhone"));
		if (contactPhoneNumber.length()>12) contactPhoneNumber = contactPhoneNumber.substring(0,12);
		info.setContactPhoneNumber(contactPhoneNumber);

		Date installDate = UtilDateUtilities.StringToDate(frm.getString("installDate"),"yyyy-MM-dd hh:mm aa");
		if (installDate==null) installDate = new Date();

		Calendar installCalendar = Calendar.getInstance();
		installCalendar.setTime(installDate);
		info.setEMRSoftwareVersionDate(installCalendar);
	}

	private void buildProvider(Provider provider, cdscihi.ProviderDocument.Provider xmlProvider, Map<String,String> fileNamesMap) {
                if (provider==null || xmlProvider==null || fileNamesMap==null) return;

		xmlProvider.setPrimaryPhysicianLastName(provider.getLastName());
		xmlProvider.setPrimaryPhysicianFirstName(provider.getFirstName());
		String cpso = StringUtils.noNull(provider.getPractitionerNo());
		xmlProvider.setPrimaryPhysicianCPSO(cpso);

		String filename = provider.getFirstName() + "_" + provider.getLastName() + "_" + cpso + ".xml";
		fileNamesMap.put(provider.getProviderNo(), filename);
	}

	private void buildPatientDemographic(Demographic demo, Demographics xmlDemographics) {
		HealthCard healthCard = xmlDemographics.addNewHealthCard();
		healthCard.setNumber(demo.getHin());
                if (StringUtils.empty(healthCard.getNumber())) healthCard.setNumber("0");
		healthCard.setType("HCN");

		healthCard.setJurisdiction(cdsDtCihi.HealthCardProvinceCode.CA_ON);

		Calendar dob  = demo.getBirthDay();
		xmlDemographics.setDateOfBirth(dob);

		String sex = demo.getSex();
		if( !"F".equalsIgnoreCase(sex) && !"M".equalsIgnoreCase(sex)) {
			sex = "U";
		}
		cdsDtCihi.Gender.Enum enumDemo = cdsDtCihi.Gender.Enum.forString(sex);
		xmlDemographics.setGender(enumDemo);

		String spokenLanguage = demo.getSpokenLanguage();
		if (StringUtils.filled(spokenLanguage) && Util.convertLanguageToCode(spokenLanguage)!=null){
			xmlDemographics.setPreferredSpokenLanguage(Util.convertLanguageToCode(spokenLanguage));
		}

		Date statusDate = demo.getPatientStatusDate();
		Calendar statusCal = Calendar.getInstance();
		if( statusDate != null ) {
			statusCal.setTime(statusDate);
		}

                PersonStatusCode personStatusCode = xmlDemographics.addNewPersonStatusCode();
		String status = demo.getPatientStatus();
		if( "AC".equalsIgnoreCase(status)) personStatusCode.setPersonStatusAsEnum(cdsDtCihi.PersonStatus.A);
		else if( "IN".equalsIgnoreCase(status)) personStatusCode.setPersonStatusAsEnum(cdsDtCihi.PersonStatus.I);
                else if( "DE".equalsIgnoreCase(status)) personStatusCode.setPersonStatusAsEnum(cdsDtCihi.PersonStatus.D);
                else {
                    if ("MO".equalsIgnoreCase(status)) status = "Moved";
                    else if ("FI".equalsIgnoreCase(status)) status = "Fired";
                    personStatusCode.setPersonStatusAsPlainText(status);
                }
                if( statusDate != null ) {
                        xmlDemographics.setPersonStatusDate(statusCal);
		}

                Date rosterDate = demo.getRosterDate();
                Calendar rosterCal = Calendar.getInstance();
                if( rosterDate != null ) {
                        rosterCal.setTime(rosterDate);
                        xmlDemographics.setEnrollmentDate(rosterCal);
                }

                Date rosterTermDate = demo.getRosterTerminationDate();
                Calendar rosterTermCal = Calendar.getInstance();
                if( rosterTermDate != null ) {
                        rosterTermCal.setTime(rosterTermDate);
                        xmlDemographics.setEnrollmentTerminationDate(rosterTermCal);
                }

                if (StringUtils.filled(demo.getPostal())) {
                    PostalZipCode postalZipCode = xmlDemographics.addNewPostalAddress();
                    postalZipCode.setPostalCode(StringUtils.noNull(demo.getPostal()).replace(" ",""));
                }
	}

	private void buildAppointment(Demographic demo, PatientRecord patientRecord) {
		//some weird values for appointment time preventing query from executing so just skip bad records
		try {
			List<Appointment> appointmentList = oscarAppointmentDao.getAppointmentHistory(demo.getDemographicNo());

			Calendar cal = Calendar.getInstance();
			Calendar startTime = Calendar.getInstance();
			Date startDate;

			for( Appointment appt: appointmentList) {
				if( appt.getAppointmentDate() == null ) {
					continue;
				}
				Appointments appointments = patientRecord.addNewAppointments();
	            if (StringUtils.filled(appt.getReason())) appointments.setAppointmentPurpose(appt.getReason());

				DateFullOrPartial dateFullorPartial = appointments.addNewAppointmentDate();
				cal.setTime(appt.getAppointmentDate());

				startDate = appt.getStartTime();
				startTime.setTime(startDate);
				cal.set(Calendar.HOUR_OF_DAY, startTime.get(Calendar.HOUR_OF_DAY));
				cal.set(Calendar.MINUTE, startTime.get(Calendar.MINUTE));
				dateFullorPartial.setFullDate(cal);
			}
		} catch( Exception e ) {
			MiscUtils.getLogger().error("Error",e);
		}

	}

	@SuppressWarnings("unchecked")
    private void buildFamilyHistory(Demographic demo, PatientRecord patientRecord ) {
		List<Issue> issueList = issueDAO.findIssueByCode(new String[] {"FamHistory"});
		String[] famHistory = new String[] {issueList.get(0).getId().toString()};

		String intervention;
	    String relationship;
	    String age;
	    Date startDate;
	    String startDateFormat;
	    List<CaseManagementNote> notesList = getCaseManagementNoteDAO().getActiveNotesByDemographic(demo.getDemographicNo().toString(), famHistory);
		for( CaseManagementNote caseManagementNote: notesList) {

			intervention = "";
			relationship = "";
			age = "";
			startDateFormat = null;
			startDate = null;
			List<CaseManagementNoteExt> caseManagementNoteExtList = getCaseManagementNoteExtDAO().getExtByNote(caseManagementNote.getId());
            String keyval;
            for( CaseManagementNoteExt caseManagementNoteExt: caseManagementNoteExtList ) {
                keyval = caseManagementNoteExt.getKeyVal();
                if( keyval.equals(CaseManagementNoteExt.TREATMENT)) {
                    intervention = caseManagementNoteExt.getValue();
                }
                else if( keyval.equals(CaseManagementNoteExt.RELATIONSHIP)) {
                    relationship = caseManagementNoteExt.getValue();
                }
                else if( keyval.equals(CaseManagementNoteExt.AGEATONSET)) {
                    age = caseManagementNoteExt.getValue();
                }
                else if( keyval.equals(CaseManagementNoteExt.STARTDATE)) {
                    startDate = caseManagementNoteExt.getDateValue();
                    startDateFormat = caseManagementNoteExt.getValue();
                }
            }

            Set<CaseManagementIssue> noteIssueList = caseManagementNote.getIssues();
            if( noteIssueList != null && noteIssueList.size() > 0 ) {
                Iterator<CaseManagementIssue> i = noteIssueList.iterator();
                CaseManagementIssue cIssue;
                FamilyHistory familyHistory = patientRecord.addNewFamilyHistory();
                while ( i.hasNext() ) {
                	cIssue = i.next();
                    if (cIssue.getIssue().getType().equals("system")) continue;

                	StandardCoding standardCoding = familyHistory.addNewDiagnosisProcedureCode();
                	standardCoding.setStandardCodingSystem(cIssue.getIssue().getType());
                    String code = cIssue.getIssue().getType().equalsIgnoreCase("icd9") ? Util.formatIcd9(cIssue.getIssue().getCode()) : cIssue.getIssue().getCode();
                	standardCoding.setStandardCode(code);
                	standardCoding.setStandardCodeDescription(cIssue.getIssue().getDescription());
                    break;
                }
                if( startDate != null ) {
                	Util.putPartialDate(familyHistory.addNewStartDate(), startDate, startDateFormat);
                }

                if( !"".equalsIgnoreCase(age) ) {
                	try {
                		familyHistory.setAgeAtOnset(BigInteger.valueOf(Long.parseLong(age)));
                	}catch(NumberFormatException e) {
                		//ignore
                	}
                }

                //if( !hasIssue ) {  //Commenting this out.  I don't see why it's not there if it has an issue
                    familyHistory.setProblemDiagnosisProcedureDescription(caseManagementNote.getNote());
                //}

                if( !"".equalsIgnoreCase(intervention)) {
                    familyHistory.setTreatment(intervention);
                }

                if( !"".equalsIgnoreCase(relationship)) {
                    familyHistory.setRelationship(relationship);
                }
            }

        }
    }

	@SuppressWarnings("unchecked")
    private void buildOngoingProblems(Demographic demo, PatientRecord patientRecord) {
		List<Issue> issueList = issueDAO.findIssueByCode(new String[] {"Concerns"});
		String[] onGoingConcerns = new String[] {issueList.get(0).getId().toString()};

		Date startDate;
	    Date endDate;
	    String startDateFormat;
	    String endDateFormat;
	    List<CaseManagementNote> notesList = getCaseManagementNoteDAO().getActiveNotesByDemographic(demo.getDemographicNo().toString(), onGoingConcerns);
		for( CaseManagementNote caseManagementNote: notesList) {

			startDate = null;
			endDate = null;
		    startDateFormat = null;
		    endDateFormat = null;
			List<CaseManagementNoteExt> caseManagementNoteExtList = getCaseManagementNoteExtDAO().getExtByNote(caseManagementNote.getId());
            String keyval;
            for( CaseManagementNoteExt caseManagementNoteExt: caseManagementNoteExtList ) {
                keyval = caseManagementNoteExt.getKeyVal();

                if( keyval.equals(CaseManagementNoteExt.STARTDATE)) {
                    startDate = caseManagementNoteExt.getDateValue();
                    startDateFormat = caseManagementNoteExt.getValue();
                }
                else if( keyval.equals(CaseManagementNoteExt.RESOLUTIONDATE)) {
                    endDate = caseManagementNoteExt.getDateValue();
                    endDateFormat = caseManagementNoteExt.getValue();
                }
            }

            Set<CaseManagementIssue> noteIssueList = caseManagementNote.getIssues();
            if( noteIssueList != null && noteIssueList.size() > 0 ) {
                Iterator<CaseManagementIssue> i = noteIssueList.iterator();
                CaseManagementIssue cIssue;
                ProblemList problemList = patientRecord.addNewProblemList();
                while ( i.hasNext() ) {
                	cIssue = i.next();
                    if (cIssue.getIssue().getType().equals("system")) continue;

                	StandardCoding standardCoding = problemList.addNewDiagnosisCode();
                	standardCoding.setStandardCodingSystem(cIssue.getIssue().getType());
                    String code = cIssue.getIssue().getType().equalsIgnoreCase("icd9") ? Util.formatIcd9(cIssue.getIssue().getCode()) : cIssue.getIssue().getCode();
                	standardCoding.setStandardCode(code);
                	standardCoding.setStandardCodeDescription(cIssue.getIssue().getDescription());
                    break;
                }

                if( startDate != null ) {
                	Util.putPartialDate(problemList.addNewOnsetDate(), startDate, startDateFormat);
                }

                if( endDate != null ) {
                	Util.putPartialDate(problemList.addNewResolutionDate(), endDate, endDateFormat);
                }

                //if( !hasIssue ) {  //Commenting out another one
                    problemList.setProblemDiagnosisDescription(caseManagementNote.getNote());
                //}


            }
        }
    }

	@SuppressWarnings("unchecked")
    private void buildRiskFactors(Demographic demo, PatientRecord patientRecord) {
		List<Issue> issueList = issueDAO.findIssueByCode(new String[] {"RiskFactors"});
		String[] riskFactor = new String[] {issueList.get(0).getId().toString()};

		Date startDate;
	    Date endDate;
	    String startDateFormat;
	    String endDateFormat;

		List<CaseManagementNote> notesList = getCaseManagementNoteDAO().getActiveNotesByDemographic(demo.getDemographicNo().toString(), riskFactor);
		for( CaseManagementNote caseManagementNote: notesList) {

			startDate = null;
			endDate = null;
		    startDateFormat = null;
		    endDateFormat = null;

			List<CaseManagementNoteExt> caseManagementNoteExtList = getCaseManagementNoteExtDAO().getExtByNote(caseManagementNote.getId());
            String keyval;
            for( CaseManagementNoteExt caseManagementNoteExt: caseManagementNoteExtList ) {
                keyval = caseManagementNoteExt.getKeyVal();

                if( keyval.equals(CaseManagementNoteExt.STARTDATE)) {
                    startDate = caseManagementNoteExt.getDateValue();
                    startDateFormat = caseManagementNoteExt.getValue();
                }
                else if( keyval.equals(CaseManagementNoteExt.RESOLUTIONDATE)) {
                    endDate = caseManagementNoteExt.getDateValue();
                    endDateFormat = caseManagementNoteExt.getValue();
                }
            }

            RiskFactors riskFactors = patientRecord.addNewRiskFactors();


            if( startDate != null ) {
            	Util.putPartialDate(riskFactors.addNewStartDate(), startDate, startDateFormat);
            }

            if( endDate != null ) {
            	Util.putPartialDate(riskFactors.addNewEndDate(), endDate, endDateFormat);
            }

            String riskFactorContent = caseManagementNote.getNote();
            if (riskFactorContent.length()>120) riskFactorContent = riskFactorContent.substring(0, 120);
            riskFactors.setRiskFactor(riskFactorContent);
		}
	}

	private void buildAllergies(Demographic demo, PatientRecord patientRecord) {
		String[] severity = new String[] {"MI","MO","LT","NO"};
		AllergyDao allergyDao=(AllergyDao)SpringUtils.getBean("allergyDao");
		List<Allergy> allergies = allergyDao.findActiveAllergies(demo.getDemographicNo());
		int index;
		Date date;
        for( Allergy allergy: allergies ) {
            	AllergiesAndAdverseReactions xmlAllergies = patientRecord.addNewAllergiesAndAdverseReactions();
                Integer typeCode = allergy.getTypeCode();
                if (typeCode == null) {
                    xmlAllergies.setPropertyOfOffendingAgent(cdsDtCihi.PropertyOfOffendingAgent.UK);
                } else {
                    if (typeCode == 13) xmlAllergies.setPropertyOfOffendingAgent(cdsDtCihi.PropertyOfOffendingAgent.DR);
                    else if(typeCode == 0) xmlAllergies.setPropertyOfOffendingAgent(cdsDtCihi.PropertyOfOffendingAgent.ND);
                    else xmlAllergies.setPropertyOfOffendingAgent(cdsDtCihi.PropertyOfOffendingAgent.UK);
                }
        	xmlAllergies.setOffendingAgentDescription(allergy.getDescription());
        	try {
                index = Integer.parseInt(allergy.getSeverityOfReaction());
            }catch(Exception e ) {
                index = 4;
            }
        	if( index > 0 ) {
        		index -= 1;
        	}
        	xmlAllergies.setSeverity(cdsDtCihi.AdverseReactionSeverity.Enum.forString(severity[index]));
        	date = allergy.getStartDate();
        	if( date != null ) {
        		Util.putPartialDate(xmlAllergies.addNewStartDate(), date, PartialDate.ALLERGIES, allergy.getAllergyId(), PartialDate.ALLERGIES_STARTDATE);
        	}
        }
	}

	@SuppressWarnings("unchecked")
    private void buildCareElements(Demographic demo, PatientRecord patientRecord) {
		List<Measurements> measList = ImportExportMeasurements.getMeasurements(demo.getDemographicNo().toString());
		CareElements careElements = patientRecord.addNewCareElements();
		Calendar cal = Calendar.getInstance();
		Date date = null;
		for (Measurements measurement : measList) {
			if( measurement.getType().equalsIgnoreCase("BP")) {
				String[] sysdiabBp = measurement.getDataField().split("/");
				if( sysdiabBp.length == 2 ) {
					cdsDtCihi.BloodPressure bloodpressure = careElements.addNewBloodPressure();
					bloodpressure.setSystolicBP(sysdiabBp[0]);
					bloodpressure.setDiastolicBP(sysdiabBp[1]);
					date = measurement.getDateObserved();
					if( date == null ) {
						date = measurement.getDateEntered();
					}
					cal.setTime(date);
					bloodpressure.setDate(cal);
				}
			}
			else if( measurement.getType().equalsIgnoreCase("HT")) {
				cdsDtCihi.Height height = careElements.addNewHeight();
				height.setHeight(measurement.getDataField());
				height.setHeightUnit("cm");
				date = measurement.getDateObserved();
				if( date == null ) {
					date = measurement.getDateEntered();
				}
				cal.setTime(date);
				height.setDate(cal);
			}
			else if( measurement.getType().equalsIgnoreCase("WT")) {
				cdsDtCihi.Weight weight = careElements.addNewWeight();
				weight.setWeight(measurement.getDataField());
				weight.setWeightUnit("Kg");
				date = measurement.getDateObserved();
				if( date == null ) {
					date = measurement.getDateEntered();
				}
				cal.setTime(date);
				weight.setDate(cal);
			}
			else if( measurement.getType().equalsIgnoreCase("WAIS") || measurement.getType().equalsIgnoreCase("WC") ) {
				cdsDtCihi.WaistCircumference waist = careElements.addNewWaistCircumference();
				waist.setWaistCircumference(measurement.getDataField());
				waist.setWaistCircumferenceUnit("cm");
				date = measurement.getDateObserved();
				if( date == null ) {
					date = measurement.getDateEntered();
				}
				cal.setTime(date);
				waist.setDate(cal);
			}
		}
	}

/*
	private void buildProcedure2(Demographic demo, PatientRecord patientRecord) {
            OscarProperties properties = OscarProperties.getInstance();
            Calendar cal = Calendar.getInstance();
            Date procedureDate;
            boolean hasIssue;
            String code;
            List<CaseManagementNote> notesList = getCaseManagementNoteDAO().getNotesByDemographic(demo.getDemographicNo().toString());
            for( CaseManagementNote caseManagementNote: notesList) {

                Procedure procedure = patientRecord.addNewProcedure();
                hasIssue = false;
                Set<CaseManagementIssue> noteIssueList = caseManagementNote.getIssues();
                if( noteIssueList != null && noteIssueList.size() > 0 ) {
                    Iterator<CaseManagementIssue> i = noteIssueList.iterator();
                    CaseManagementIssue cIssue;
                    hasIssue = true;
                    while ( i.hasNext() ) {
                        cIssue = i.next();
                        if (cIssue.getIssue().getType().equals("system")) continue;

                        StandardCoding procedureCode = procedure.addNewProcedureCode();
                        procedureCode.setStandardCodingSystem(properties.getProperty("dxResearch_coding_sys","icd9"));
                        procedureCode.setStandardCode(cIssue.getIssue().getCode());
                        procedureCode.setStandardCodeDescription(cIssue.getIssue().getDescription());
                        break;
                    }
                }

                //if( !hasIssue ) {
                    String note = caseManagementNote.getNote();
                    if (note!=null && note.length()>250)
                        procedure.setProcedureInterventionDescription(caseManagementNote.getNote().substring(0, 249));//Description only allow 250 characters
                //}

                procedureDate = caseManagementNote.getObservation_date();
                cal.setTime(procedureDate);
                DateFullOrPartial dateFullOrPartial = procedure.addNewProcedureDate();
                dateFullOrPartial.setFullDate(cal);
            }
	}
 *
 */

	@SuppressWarnings("unchecked")
    private void buildProcedure(Demographic demo, PatientRecord patientRecord) {
		List<Issue> issueList = issueDAO.findIssueByCode(new String[] {"MedHistory"});
		String[] medhistory = new String[] {issueList.get(0).getId().toString()};

		Procedure procedure;
		ProblemList problemlist;

		List<CaseManagementNote> notesList = getCaseManagementNoteDAO().getActiveNotesByDemographic(demo.getDemographicNo().toString(), medhistory);
		for( CaseManagementNote caseManagementNote: notesList) {

                    procedure = null;
                    problemlist = null;

                    List<CaseManagementNoteExt> caseManagementNoteExtList = getCaseManagementNoteExtDAO().getExtByNote(caseManagementNote.getId());
                    String keyval;
                    for( CaseManagementNoteExt caseManagementNoteExt: caseManagementNoteExtList ) {
                        if (procedure!=null && procedure.getProcedureDate()!=null) break;
                        if (problemlist!=null && problemlist.getOnsetDate()!=null && problemlist.getResolutionDate()!=null) break;

                        keyval = caseManagementNoteExt.getKeyVal();

                        if( caseManagementNoteExt.getDateValue() != null ) {
	                        if( keyval.equals(CaseManagementNoteExt.PROCEDUREDATE)) {
	                            procedure = patientRecord.addNewProcedure();
	                            Util.putPartialDate(procedure.addNewProcedureDate(), caseManagementNoteExt);
	                        } else
	                        if( keyval.equals(CaseManagementNoteExt.STARTDATE)) {
	                            if (problemlist==null) problemlist = patientRecord.addNewProblemList();
	                            Util.putPartialDate(problemlist.addNewOnsetDate(), caseManagementNoteExt);
	                        } else
	                        if( keyval.equals(CaseManagementNoteExt.RESOLUTIONDATE)) {
	                            if (problemlist==null) problemlist = patientRecord.addNewProblemList();
	                            Util.putPartialDate(problemlist.addNewResolutionDate(), caseManagementNoteExt);
	                        }
                        }
                    }
                    if (procedure==null && problemlist==null) continue;

                    Set<CaseManagementIssue> noteIssueList = caseManagementNote.getIssues();
                    if( noteIssueList != null && noteIssueList.size() > 0 ) {
                        Iterator<CaseManagementIssue> i = noteIssueList.iterator();
                        CaseManagementIssue cIssue;

                        while ( i.hasNext() ) {
                            cIssue = i.next();
                            if (cIssue.getIssue().getType().equals("system")) continue;

                            String code = cIssue.getIssue().getType().equalsIgnoreCase("icd9") ? Util.formatIcd9(cIssue.getIssue().getCode()) : cIssue.getIssue().getCode();
                            if (procedure!=null) {
                                StandardCoding procedureCode = procedure.addNewProcedureCode();
                                procedureCode.setStandardCodingSystem(cIssue.getIssue().getType());
                                procedureCode.setStandardCode(code);
                                procedureCode.setStandardCodeDescription(cIssue.getIssue().getDescription());
                                break;
                            }
                            if (problemlist!=null) {
                                StandardCoding diagnosisCode = problemlist.addNewDiagnosisCode();
                                diagnosisCode.setStandardCodingSystem(cIssue.getIssue().getType());
                                diagnosisCode.setStandardCode(code);
                                diagnosisCode.setStandardCodeDescription(cIssue.getIssue().getDescription());
                                break;
                            }
                        }
                    }
                    if (procedure!=null) procedure.setProcedureInterventionDescription(caseManagementNote.getNote());
                    if (problemlist!=null) problemlist.setProblemDiagnosisDescription(caseManagementNote.getNote());
		}
	}

	private void buildLaboratoryResults(Demographic demo, PatientRecord patientRecord) {
				log.debug("Building lab results for " + demo.getDemographicNo() + " " + demo.getFormattedName());
                List<LabMeasurements> labMeaList = ImportExportMeasurements.getLabMeasurements(demo.getDemographicNo().toString());
                for (LabMeasurements labMea : labMeaList) {
                	String data = StringUtils.noNull(labMea.getExtVal("identifier"));
                	log.debug("Measurement search for identifier '" + data + "'");

                    Date dateTime = UtilDateUtilities.StringToDate(labMea.getExtVal("datetime"),"yyyy-MM-dd HH:mm:ss");
                    if (dateTime==null) {
                    	dateTime = UtilDateUtilities.StringToDate(labMea.getExtVal("datetime"),"yyyy-MM-dd");
                    	if (dateTime==null) {
                    		log.debug("dateTime is null...continuing");
                    	}
                    }

                    LaboratoryResults labResults = patientRecord.addNewLaboratoryResults();

                    if (StringUtils.filled(data)) labResults.setLabTestCode(data);

                    cdsDtCihi.DateFullOrDateTime collDate = labResults.addNewCollectionDateTime();
                    if (dateTime!=null) collDate.setFullDate(Util.calDate(dateTime));
                    else collDate.setFullDate(Util.calDate("0001-01-01"));

                    data = labMea.getExtVal("name");

                    if (StringUtils.filled(data)) {
                    	log.debug("Adding " + data);
                    	labResults.setTestNameReportedByLab(data);
                    }

                    data = labMea.getMeasure().getDataField();
                    if (StringUtils.filled(data)) {
                        LaboratoryResults.Result result = labResults.addNewResult();
                        result.setValue(data);
                        data = labMea.getExtVal("unit");
                        if (StringUtils.filled(data)) result.setUnitOfMeasure(data);
                    }

                    String min = labMea.getExtVal("minimum");
                    String max = labMea.getExtVal("maximum");
                    LaboratoryResults.ReferenceRange refRange = labResults.addNewReferenceRange();
                    if (StringUtils.filled(min)) refRange.setLowLimit(min);
                    if (StringUtils.filled(max)) refRange.setHighLimit(max);
		}
	}


    private void buildMedications(Demographic demo, PatientRecord patientRecord) {
		MedicationsAndTreatments medications;
		RxPrescriptionData.Prescription[] pa = new RxPrescriptionData().getPrescriptionsByPatient(Integer.parseInt(demo.getDemographicNo().toString()));
		String drugname;
		String customname;
		String dosage;
		String[] strength;
		int sep;

		for(int p = 0; p<pa.length; ++p) {
			drugname = pa[p].getBrandName();
			customname = pa[p].getCustomName();
			if (StringUtils.empty(drugname) && StringUtils.empty(customname)) continue;

			medications = patientRecord.addNewMedicationsAndTreatments();

			if (StringUtils.filled(drugname)) medications.setDrugName(drugname);
			else medications.setDrugDescription(customname);

			Date writtenDate = pa[p].getWrittenDate();
			if (writtenDate!=null) {
	        	String dateFormat = partialDateDao.getFormat(PartialDate.DRUGS, pa[p].getDrugId(), PartialDate.DRUGS_WRITTENDATE);
	        	Util.putPartialDate(medications.addNewPrescriptionWrittenDate(), writtenDate, dateFormat);
			}

			if (StringUtils.filled(pa[p].getDosage())) {
            	strength = pa[p].getDosage().split(" ");

            	cdsDtCihi.DrugMeasure drugM = medications.addNewStrength();
            	if (Util.leadingNum(strength[0]).equals(strength[0])) {//amount & unit separated by space
            		drugM.setAmount(strength[0]);
            		if (strength.length>1) drugM.setUnitOfMeasure(strength[1]);
            		else drugM.setUnitOfMeasure("unit"); //UnitOfMeasure cannot be null

            	} else {//amount & unit not separated, probably e.g. 50mg / 2tablet
            		if (strength.length>1 && strength[1].equals("/")) {
            			if (strength.length>2) {
            				String unit1 = Util.leadingNum(strength[2]).equals("") ? "1" : Util.leadingNum(strength[2]);
            				String unit2 = Util.trailingTxt(strength[2]).equals("") ? "unit" : Util.trailingTxt(strength[2]);

                    		drugM.setAmount(Util.leadingNum(strength[0])+"/"+Util.leadingNum(strength[2]));
                    		drugM.setUnitOfMeasure(Util.trailingTxt(strength[0])+"/"+unit2);
            			}
            		} else {
                		drugM.setAmount(Util.leadingNum(strength[0]));
                		drugM.setUnitOfMeasure(Util.trailingTxt(strength[0]));
            		}
            	}
			}

	        dosage = StringUtils.noNull(pa[p].getDosageDisplay());
	        medications.setDosage(dosage);
	        medications.setDosageUnitOfMeasure(StringUtils.noNull(pa[p].getUnit()));
	        medications.setForm(StringUtils.noNull(pa[p].getDrugForm()));
	        medications.setFrequency(StringUtils.noNull(pa[p].getFreqDisplay()));
	        medications.setRoute(StringUtils.noNull(pa[p].getRoute()));
	        medications.setNumberOfRefills(String.valueOf(pa[p].getRepeat()));

	        YnIndicatorAndBlank patientCompliance = medications.addNewPatientCompliance();
	        if (pa[p].getPatientCompliance()==null) {
	        	patientCompliance.setBlank(cdsDtCihi.Blank.X);
	        } else {
	        	patientCompliance.setBoolean(pa[p].getPatientCompliance());
	        }
		}

	}

    private void buildImmunizations(LoggedInInfo loggedInInfo, Demographic demo, PatientRecord patientRecord) {
    	HashMap<String,String> preventionMap;
    	List<Prevention> preventionsList = getPreventionDao().findNotDeletedByDemographicId(demo.getDemographicNo());

    	Map<String,Object> prevTypes = Util.getPreventionTypes(loggedInInfo);
    	
         for( Prevention prevention: preventionsList ) {
             preventionMap = getPreventionExtDao().getPreventionExt(prevention.getId());

        	 Immunizations immunizations = patientRecord.addNewImmunizations();

        	 if (StringUtils.filled(preventionMap.get("name"))) {
        		immunizations.setImmunizationName(preventionMap.get("name"));
        	 }else{
        		String preventionType = Util.getImmunizationType(loggedInInfo, prevention.getPreventionType(),prevTypes);
        		if (cdsDt.ImmunizationType.Enum.forString(preventionType)!=null) {
        			immunizations.setImmunizationName(preventionType);
        		} else {
        			immunizations.setImmunizationName("");
        		}
        	 }

        	 if (StringUtils.filled(preventionMap.get("lot")))
        		 immunizations.setLotNumber(preventionMap.get("lot"));

        	 if (prevention.getPreventionDate()!=null) {
        		 DateFullOrPartial dateFullorPartial = immunizations.addNewDate();
        		 dateFullorPartial.setFullDate(Util.calDate(prevention.getPreventionDate()));
        	 }

        	 YnIndicator refusedIndicator = immunizations.addNewRefusedFlag();
             if( prevention.isRefused() ) {
            	 refusedIndicator.setBoolean(true);
             }
             else {
                 refusedIndicator.setBoolean(false);
             }
         }
    }

    private String makeFiles(Map<String,CiHiCdsDocument> xmlMap, Map<String,String> fileNamesMap, String tmpDir) throws Exception {
    	XmlOptions options = new XmlOptions();
    	options.put( XmlOptions.SAVE_PRETTY_PRINT );
    	options.put( XmlOptions.SAVE_PRETTY_PRINT_INDENT, 3 );
    	options.put( XmlOptions.SAVE_AGGRESSIVE_NAMESPACES );

        HashMap<String,String> suggestedPrefix = new HashMap<String,String>();
        suggestedPrefix.put("cds_dt","cdsd");
        options.setSaveSuggestedPrefixes(suggestedPrefix);

    	options.setSaveOuter();

    	ArrayList<File> files = new ArrayList<File>();
    	int idx = 0;
    	CiHiCdsDocument cihiDoc;
    	Set<String>xmlKeys = xmlMap.keySet();

    	//Save each file
    	for( String key: xmlKeys) {
                if (fileNamesMap.get(key)==null) continue;

    		cihiDoc = xmlMap.get(key);
    		files.add(new File(tmpDir, fileNamesMap.get(key)));

    		try {
    			cihiDoc.save(files.get(idx), options);
    		}
    		catch( IOException e ) {
    			MiscUtils.getLogger().error("Cannot write .xml file(s) to export directory " + tmpDir + ".\nPlease check directory permissions.", e);
    		}
    		++idx;
    	} //end for

    	//Zip export files
        String zipName = "omd_cihi_export-"+UtilDateUtilities.getToday("yyyy-MM-dd.HH.mm.ss")+".zip";
        if (!Util.zipFiles(files, zipName, tmpDir)) {
        	MiscUtils.getLogger().error("Error! Failed zipping export files");
        }

        //copy zip to document directory
        File zipFile = new File(tmpDir,zipName);
        OscarProperties properties = OscarProperties.getInstance();
        File destDir = new File(properties.getProperty("DOCUMENT_DIR"));
        org.apache.commons.io.FileUtils.copyFileToDirectory(zipFile, destDir);

        //Remove zip & export files from temp dir
        Util.cleanFile(zipName, tmpDir);
        Util.cleanFiles(files);

        return zipName;
    }
}
