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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import org.oscarehr.common.dao.DxresearchDAO;
import org.oscarehr.common.dao.Hl7TextInfoDao;
import org.oscarehr.common.dao.Icd9Dao;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.dao.PartialDateDao;
import org.oscarehr.common.dao.PreventionDao;
import org.oscarehr.common.dao.PreventionExtDao;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.common.model.DataExport;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Dxresearch;
import org.oscarehr.common.model.Icd9;
import org.oscarehr.common.model.PartialDate;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.common.model.PreventionExt;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.oscarEncounter.oscarMeasurements.data.ImportExportMeasurements;
import oscar.oscarEncounter.oscarMeasurements.data.Measurements;
import oscar.oscarLab.ca.all.parsers.Factory;
import oscar.oscarLab.ca.all.parsers.MessageHandler;
import oscar.oscarLab.ca.on.CommonLabResultData;
import oscar.oscarLab.ca.on.LabResultData;
import oscar.oscarReport.data.DemographicSets;
import oscar.oscarRx.data.RxPrescriptionData;
import oscar.util.StringUtils;
import oscar.util.UtilDateUtilities;
import cdsDtCihiPhcvrs.DateFullOrPartial;
import cdsDtCihiPhcvrs.HealthCard;
import cdsDtCihiPhcvrs.PostalZipCode;
import cdsDtCihiPhcvrs.StandardCoding;
import cdsDtCihiPhcvrs.YnIndicator;
import cdsDtCihiPhcvrs.YnIndicatorAndBlank;
import cdscihiphcvrs.AllergiesAndAdverseReactionsDocument.AllergiesAndAdverseReactions;
import cdscihiphcvrs.AppointmentsDocument.Appointments;
import cdscihiphcvrs.CareElementsDocument.CareElements;
import cdscihiphcvrs.CiHiCdsDocument;
import cdscihiphcvrs.CiHiCdsDocument.CiHiCds;
import cdscihiphcvrs.DemographicsDocument.Demographics;
import cdscihiphcvrs.DemographicsDocument.Demographics.PersonStatusCode;
import cdscihiphcvrs.ExtractInformationDocument.ExtractInformation;
import cdscihiphcvrs.FamilyHistoryDocument.FamilyHistory;
import cdscihiphcvrs.ImmunizationsDocument.Immunizations;
import cdscihiphcvrs.LaboratoryResultsDocument.LaboratoryResults;
import cdscihiphcvrs.MedicationsAndTreatmentsDocument.MedicationsAndTreatments;
import cdscihiphcvrs.PatientRecordDocument.PatientRecord;
import cdscihiphcvrs.ProblemListDocument.ProblemList;
import cdscihiphcvrs.ProcedureDocument.Procedure;
import cdscihiphcvrs.RiskFactorsDocument.RiskFactors;

public class CihiExportPHC_VRSAction extends DispatchAction {
	private ClinicDAO clinicDAO;
	private DataExportDao dataExportDAO;
	private DemographicDao demographicDao;
	private OscarAppointmentDao oscarAppointmentDao;
	private IssueDAO issueDAO;
	private CaseManagementNoteDAO caseManagementNoteDAO;
	private CaseManagementNoteExtDAO caseManagementNoteExtDAO;
	private AllergyDao allergyDAO;
	private Hl7TextInfoDao hl7TextInfoDAO;
	private PreventionDao preventionDao;
	private DxresearchDAO dxresearchDAO;
	private Icd9Dao icd9Dao;
	private PreventionExtDao preventionExtDao = (PreventionExtDao)SpringUtils.getBean("preventionExtDao");

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

	public void setHl7TextInfoDAO(Hl7TextInfoDao hl7TextInfoDAO) {
	    this.hl7TextInfoDAO = hl7TextInfoDAO;
    }

	public Hl7TextInfoDao getHl7TextInfoDAO() {
	    return hl7TextInfoDAO;
    }

	public void setAllergyDao(AllergyDao allergyDAO) {
	    this.allergyDAO = allergyDAO;
    }

	public AllergyDao getAllergyDao() {
	    return allergyDAO;
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

	public DxresearchDAO getDxresearchDAO() {
		return dxresearchDAO;
	}

	public void setDxresearchDAO(DxresearchDAO dxresearchDAO) {
		this.dxresearchDAO = dxresearchDAO;
	}

	public Icd9Dao getIcd9Dao() {
		return icd9Dao;
	}

	public void setIcd9Dao(Icd9Dao icd9Dao) {
		this.icd9Dao = icd9Dao;
	}

	public ActionForward getFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		OscarProperties properties = OscarProperties.getInstance();
		String zipName = request.getParameter("zipFile");
		String dir = properties.getProperty("DOCUMENT_DIR");
		Util.downloadFile(zipName, dir, response);
		return null;

	}

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
		log.info("CIHI PHC VRS EXPORT IS DONE");
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
		 int numPatients = patientList.size();
		 log.info("Processing " + numPatients + " patients");
		 for( int idx = 0; idx < numPatients; ++idx ) {
			 demoNo = null;
			 cihicds = null;

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

				 if( !this.buildProvider(demo.getProvider(),cihicds.addNewProvider(), fileNamesMap) ) {
					 continue;
				 }
				 xmlMap.put(demo.getProviderNo(), cihiCdsDocument);
			 }
			 else {
				 cihiCdsDocument = xmlMap.get(demo.getProviderNo());
				 cihicds = cihiCdsDocument.getCiHiCds();
			 }

			 PatientRecord patientRecord = cihicds.addNewPatientRecord();

			 if( !this.buildPatientDemographic(demo, patientRecord.addNewDemographics())) {
				 continue;
			 }

			 log.info("Processing Demo " + idx + " of " + numPatients);
			 this.buildAppointment(demo, patientRecord);
			 this.buildFamilyHistory(demo, patientRecord);
			 this.buildOngoingProblems(demo, patientRecord);
			 this.buildRiskFactors(demo, patientRecord);
			 this.buildAllergies(demo, patientRecord);
			 this.buildCareElements(demo, patientRecord);
			

			 this.buildProcedure(demo, patientRecord);
			 this.buildLaboratoryResults(loggedInInfo, demo, patientRecord);
			 this.buildMedications(demo, patientRecord);
			 this.buildImmunizations(demo, patientRecord);
		 }


		 return this.makeFiles(xmlMap, fileNamesMap, tmpDir);
	}

	private void buildExtractInformation(DynaValidatorForm frm, ExtractInformation info, Calendar now) {
		info.setRunDate(now);
		info.setExtractType((String) frm.get("extractType"));
		info.setOrganizationName(frm.getString("orgName"));
		info.setContactLastName(frm.getString("contactLName"));
		info.setContactFirstName(frm.getString("contactFName"));
		info.setContactPhoneNumber(Util.onlyNum(frm.getString("contactPhone")));
		info.setContactEmail(frm.getString("contactEmail"));
		info.setContactUserName(frm.getString("contactUserName"));
		info.setEMRVendorID(frm.getString("vendorId"));
		info.setEMRVendorBusinessName(frm.getString("vendorBusinessName"));
		info.setEMRVendorCommonName(frm.getString("vendorCommonName"));
		info.setEMRSoftwareName(frm.getString("vendorSoftware"));
		info.setEMRSoftwareCommonName(frm.getString("vendorSoftwareCommonName"));
		info.setEMRSoftwareVersionNumber(frm.getString("vendorSoftwareVer"));

                Date installDate = UtilDateUtilities.StringToDate(frm.getString("installDate"),"yyyy-MM-dd hh:mm aa");
                if (installDate==null) installDate = new Date();

                Calendar installCalendar = Calendar.getInstance();
                installCalendar.setTime(installDate);
                info.setEMRSoftwareVersionDate(installCalendar);
	}

	private boolean buildProvider(Provider provider, cdscihiphcvrs.ProviderDocument.Provider xmlProvider, Map<String,String> fileNamesMap) {
                if (provider==null || xmlProvider==null || fileNamesMap==null) return false;


        String seed = provider.getProviderNo() + provider.getOhipNo();
		String strHash = this.buildHash(seed);

		if( strHash.equals("")) {
			return false;
		}

		log.info("Seed: " + seed + " Provider: " + strHash);
		xmlProvider.setPrimaryPhysicianLastName(strHash);
		xmlProvider.setPrimaryPhysicianFirstName(strHash);
		xmlProvider.setPrimaryPhysicianCPSO(strHash);

		String filename = strHash + ".xml";
		fileNamesMap.put(provider.getProviderNo(), filename);

		return true;
	}

	private String buildHash(String seed) {
		MessageDigest digest;
        byte[] hash;
        StringBuilder strHash = new StringBuilder();

        try {
			digest = MessageDigest.getInstance("MD5");
			digest.update(seed.getBytes());
			hash = digest.digest();
			for( int i = 0; i < hash.length; ++i ) {
                strHash.append(Integer.toHexString((hash[i] >>> 4) & 0x0F));
                strHash.append(Integer.toHexString(hash[i] & 0x0F));
            }

		} catch (NoSuchAlgorithmException e) {
			log.error("Cannot use md5 for provider", e);
		}

		return strHash.toString();
	}

	private boolean buildPatientDemographic(Demographic demo, Demographics xmlDemographics) {
        String seed = demo.getHin();

        if( StringUtils.empty(seed) ) {
        	seed = demo.getDemographicNo().toString() + demo.getFirstName() + demo.getLastName();
        }


        String strHash = this.buildHash(seed);

        log.info("Demo Seed: " + seed + " HASH: " + strHash);

		if( strHash.equals("")) {
			return false;
		}

		HealthCard healthCard = xmlDemographics.addNewHealthCard();
		healthCard.setNumber(strHash);

		Calendar dob  = demo.getBirthDay();
		xmlDemographics.setDateOfBirth(dob);

		String sex = demo.getSex();
		if( !"F".equalsIgnoreCase(sex) && !"M".equalsIgnoreCase(sex)) {
			sex = "U";
		}
		cdsDtCihiPhcvrs.Gender.Enum enumDemo = cdsDtCihiPhcvrs.Gender.Enum.forString(sex);
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
		if( "AC".equalsIgnoreCase(status)) personStatusCode.setPersonStatusAsEnum(cdsDtCihiPhcvrs.PersonStatus.A);
		else if( "IN".equalsIgnoreCase(status)) personStatusCode.setPersonStatusAsEnum(cdsDtCihiPhcvrs.PersonStatus.I);
                else if( "DE".equalsIgnoreCase(status)) personStatusCode.setPersonStatusAsEnum(cdsDtCihiPhcvrs.PersonStatus.D);
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

                return true;
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
			//empty
		}

	}


    private void buildFamilyHistory(Demographic demo, PatientRecord patientRecord ) {
		List<Issue> issueList = issueDAO.findIssueByCode(new String[] {"FamHistory","MedHistory"});
		String[] famHistory = new String[issueList.size()];
		for( int idx = 0; idx < famHistory.length; ++idx ) {
			famHistory[idx] = issueList.get(idx).getId().toString();
		}

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
                	this.putPartialDate(familyHistory.addNewStartDate(), startDate, startDateFormat);
                }

                if( !"".equalsIgnoreCase(age) ) {
                	try {
                		familyHistory.setAgeAtOnset(BigInteger.valueOf(Long.parseLong(age)));
                	}catch(NumberFormatException e) {
                		//empty
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

	private void putPartialDate(cdsDtCihiPhcvrs.DateFullOrPartial dfp, CaseManagementNoteExt cme) {
    	putPartialDate(dfp, cme.getDateValue(), cme.getValue());
    }

	private void putPartialDate(cdsDtCihiPhcvrs.DateFullOrPartial dfp, Date dateValue, Integer tableName, Integer tableId, Integer fieldName) {
    	PartialDate pd = partialDateDao.getPartialDate(tableName, tableId, fieldName);
    	if( pd != null ) {
    		putPartialDate(dfp, dateValue, pd.getFormat());
    	}
    }

	private void putPartialDate(cdsDtCihiPhcvrs.DateFullOrPartial dfp, Date dateValue, String format) {
        if (dateValue!=null) {
            if (PartialDate.YEARONLY.equals(format)) dfp.setYearOnly(Util.calDate(dateValue));
            else if (PartialDate.YEARMONTH.equals(format)) dfp.setYearMonth(Util.calDate(dateValue));
            else dfp.setFullDate(Util.calDate(dateValue));
        }
    }


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
                	this.putPartialDate(problemList.addNewOnsetDate(), startDate, startDateFormat);
                }

                if( endDate != null ) {
                	this.putPartialDate(problemList.addNewResolutionDate(), endDate, endDateFormat);
                }

                //if( !hasIssue ) {  //Commenting out another one
                    problemList.setProblemDiagnosisDescription(caseManagementNote.getNote());
                //}


            }
        }

		this.buildOngoingproblemsDiseaseRegistry(demo, patientRecord);
    }

	private void buildOngoingproblemsDiseaseRegistry(Demographic demo, PatientRecord patientRecord) {
		List<Dxresearch> dxResearchList = dxresearchDAO.getDxResearchItemsByPatient(demo.getDemographicNo());
		String description;
		String dateFormat = "yyyy-MM-dd";
		Date date;
		Character status;

		for( Dxresearch dxResearch : dxResearchList ) {
			status = dxResearch.getStatus();
			if( status.compareTo('D') == 0 ) {
				continue;
			}
			ProblemList problemList = patientRecord.addNewProblemList();
			StandardCoding standardCoding = problemList.addNewDiagnosisCode();
			standardCoding.setStandardCodingSystem(dxResearch.getCodingSystem());
			standardCoding.setStandardCode(dxResearch.getDxresearchCode());

			if( "icd9".equalsIgnoreCase(dxResearch.getCodingSystem()) ) {
				List<Icd9>icd9Code = icd9Dao.getIcd9Code(standardCoding.getStandardCode());
				if( !icd9Code.isEmpty() ) {
					description = icd9Code.get(0).getDescription();
					standardCoding.setStandardCodeDescription(description);
				}
			}

			date = dxResearch.getStartDate();
			if( date != null ) {
				this.putPartialDate(problemList.addNewOnsetDate(), date, dateFormat);
			}

			if( status.compareTo('C') == 0 ) {
				date = dxResearch.getUpdateDate();
				if( date != null ) {
					this.putPartialDate(problemList.addNewResolutionDate(), date, dateFormat);
				}
			}
		}
	}

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
            	this.putPartialDate(riskFactors.addNewStartDate(), startDate, startDateFormat);
            }

            if( endDate != null ) {
            	this.putPartialDate(riskFactors.addNewEndDate(), endDate, endDateFormat);
            }

            riskFactors.setRiskFactor(caseManagementNote.getNote());
		}
	}

	private void buildAllergies(Demographic demo, PatientRecord patientRecord) {
		String[] severity = new String[] {"MI","MO","LT","NO"};
		List<Allergy> allergies = allergyDAO.findAllergies(demo.getDemographicNo());
		int index;

		Date date;
        for( Allergy allergy: allergies ) {
            	AllergiesAndAdverseReactions xmlAllergies = patientRecord.addNewAllergiesAndAdverseReactions();
                Integer typeCode = allergy.getTypeCode();
                if (typeCode == null) {
                    xmlAllergies.setPropertyOfOffendingAgent(cdsDtCihiPhcvrs.PropertyOfOffendingAgent.UK);
                } else {
                    if (typeCode == 13) xmlAllergies.setPropertyOfOffendingAgent(cdsDtCihiPhcvrs.PropertyOfOffendingAgent.DR);
                    else if(typeCode == 0) xmlAllergies.setPropertyOfOffendingAgent(cdsDtCihiPhcvrs.PropertyOfOffendingAgent.ND);
                    else xmlAllergies.setPropertyOfOffendingAgent(cdsDtCihiPhcvrs.PropertyOfOffendingAgent.UK);
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
        	xmlAllergies.setSeverity(cdsDtCihiPhcvrs.AdverseReactionSeverity.Enum.forString(severity[index]));
        	date = allergy.getStartDate();
        	if( date != null ) {
        		this.putPartialDate(xmlAllergies.addNewStartDate(), date, PartialDate.ALLERGIES, (int)allergy.getId(), PartialDate.ALLERGIES_STARTDATE);
        	}
        }
	}



	@SuppressWarnings("unchecked")
    private void buildCareElements(Demographic demo, PatientRecord patientRecord)  {
		List<Measurements> measList = ImportExportMeasurements.getMeasurements(demo.getDemographicNo().toString());
		CareElements careElements = patientRecord.addNewCareElements();
		Calendar cal = Calendar.getInstance();
		Date date = null;
		for (Measurements measurement : measList) {
			if( measurement.getType().equalsIgnoreCase("BP")) {
				String[] sysdiabBp = measurement.getDataField().split("/");
				if( sysdiabBp.length == 2 ) {
					cdsDtCihiPhcvrs.BloodPressure bloodpressure = careElements.addNewBloodPressure();
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
				cdsDtCihiPhcvrs.Height height = careElements.addNewHeight();
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
				cdsDtCihiPhcvrs.Weight weight = careElements.addNewWeight();
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
				cdsDtCihiPhcvrs.WaistCircumference waist = careElements.addNewWaistCircumference();
				waist.setWaistCircumference(measurement.getDataField());
				waist.setWaistCircumferenceUnit("cm");
				date = measurement.getDateObserved();
				if( date == null ) {
					date = measurement.getDateEntered();
				}
				cal.setTime(date);
				waist.setDate(cal);
			}
			else if( StringUtils.filled(measurement.getType()) ) {
				cdsDtCihiPhcvrs.Observation observation = careElements.addNewObservation();
				observation.setObservationName(measurement.getType());
				observation.setObservationData(measurement.getDataField());
				observation.setObservationUnit(measurement.getMeasuringInstruction());
				date = measurement.getDateObserved();
				if( date == null ) {
					date = measurement.getDateEntered();
				}
				cal.setTime(date);
				observation.setDate(cal);
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
	                            this.putPartialDate(procedure.addNewProcedureDate(), caseManagementNoteExt);
	                        } else
	                        if( keyval.equals(CaseManagementNoteExt.STARTDATE)) {
	                            if (problemlist==null) problemlist = patientRecord.addNewProblemList();
	                            this.putPartialDate(problemlist.addNewOnsetDate(), caseManagementNoteExt);
	                        } else
	                        if( keyval.equals(CaseManagementNoteExt.RESOLUTIONDATE)) {
	                            if (problemlist==null) problemlist = patientRecord.addNewProblemList();
	                            this.putPartialDate(problemlist.addNewResolutionDate(), caseManagementNoteExt);
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

	private void buildLaboratoryResults(LoggedInInfo loggedInInfo, Demographic demo, PatientRecord patientRecord) {

		CommonLabResultData comLab = new CommonLabResultData();
		ArrayList<LabResultData> labdocs = comLab.populateLabsData(loggedInInfo, demo.getProviderNo(), String.valueOf(demo.getDemographicNo()), demo.getFirstName(), demo.getLastName(), demo.getHin(), "", "");
		MessageHandler handler;
		int i;
		int j;
		int k;
		int OBRCount;
		int obxCount;
		String obxName, identifier, obxResult;
		Date dateTime;

		log.info("Building lab results for " + demo.getDemographicNo() + " " + demo.getFormattedName());
		for( LabResultData labData : labdocs ) {
			try {
				handler = Factory.getHandler(labData.segmentID);

				ArrayList<String> headers = handler.getHeaders();
				OBRCount = handler.getOBRCount();
	            for(i=0;i<headers.size();i++){
	            	for ( j=0; j < OBRCount; j++){
	            		obxCount = handler.getOBXCount(j);

	                    for (k=0; k < obxCount; k++){

	                    	if( handler.getOBXResultStatus(j, k) == null || handler.getObservationHeader(j, k) == null ) {
	                    		continue;
	                    	}

	                    	if ( !handler.getOBXResultStatus(j, k).equals("DNS") /*&& !obxName.equals("")*/ && handler.getObservationHeader(j, k).equals(headers.get(i))){ // <<--  DNS only needed for MDS messages
	                    		LaboratoryResults labResults = patientRecord.addNewLaboratoryResults();
	                        	obxName = handler.getOBXName(j, k);

	                            identifier = "";

	                            if( StringUtils.filled(obxName)) {
	                            	labResults.setTestNameReportedByLab(obxName);
	                            }


	                            if(handler.getOBXValueType(j,k) != null &&  handler.getOBXValueType(j,k).equalsIgnoreCase("NAR")) {
	                            	obxResult = handler.getOBXResult( j, k);
	                            	if( StringUtils.filled(obxResult)) {
	                            		LaboratoryResults.Result result = labResults.addNewResult();
	                            		result.setValue(obxResult);
	                            	}

	                            }else if(handler.getOBXValueType(j,k) != null &&  handler.getOBXValueType(j,k).equalsIgnoreCase("FT")){
	                                String[] dividedString  =divideStringAtFirstNewline(handler.getOBXResult( j, k));
	                                obxResult = dividedString[0];
	                                identifier = handler.getOBXIdentifier(j, k);
	                            }
	                            else {
	                            	obxResult = handler.getOBXResult( j, k);
	                            	identifier = handler.getOBXIdentifier(j,k);
	                            }

	                            if( StringUtils.filled(identifier)) {
	                            	labResults.setLabTestCode(identifier);
	                            }

	                            LaboratoryResults.Result result = labResults.addNewResult();
	                    		result.setValue(obxResult);
	                    		result.setUnitOfMeasure(handler.getOBXUnits(j, k));
	                    		String range = handler.getOBXReferenceRange(j, k);
	                    		if( StringUtils.filled(range)) {
	                    			LaboratoryResults.ReferenceRange refRange = labResults.addNewReferenceRange();
	                    			String rangeLimits[] = range.split("-");
	                    			if( rangeLimits.length == 2 ) {
	                    				refRange.setLowLimit(rangeLimits[0]);
	                        			refRange.setHighLimit(rangeLimits[1]);
	                    			}
	                    			else {
	                    				refRange.setLowLimit(range);
	                    				refRange.setHighLimit(range);
	                    			}
	                    		}

	                    		dateTime = UtilDateUtilities.StringToDate(handler.getTimeStamp(j, k),"yyyy-MM-dd HH:mm:ss");

	                			if (dateTime==null) {
	                            	dateTime = UtilDateUtilities.StringToDate(handler.getServiceDate(),"yyyy-MM-dd");
	                            }

	                    		cdsDtCihiPhcvrs.DateFullOrDateTime collDate = labResults.addNewCollectionDateTime();
	                            if (dateTime!=null) {
	                            	collDate.setFullDate(Util.calDate(dateTime));
	                            }
	                            else {
	                            	collDate.setFullDate(Util.calDate("0001-01-01"));
	                            }
	                        }
	                    }
	            	}
	            }
			}catch( Exception e ) {
				log.error("Skipping Lab " + labData.segmentID);
			}
		}

	}

	private String[] divideStringAtFirstNewline(String s){
        int i = s.indexOf("<br />");
        String[] ret  = new String[2];
        if(i == -1){
               ret[0] = new String(s);
               ret[1] = null;
            }else{
               ret[0] = s.substring(0,i);
               ret[1] = s.substring(i+6);
            }
        return ret;
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
	        	this.putPartialDate(medications.addNewPrescriptionWrittenDate(), writtenDate, dateFormat);
			}

			if (StringUtils.filled(pa[p].getDosage())) {
            	strength = pa[p].getDosage().split(" ");

            	cdsDtCihiPhcvrs.DrugMeasure drugM = medications.addNewStrength();
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
	        	patientCompliance.setBlank(cdsDtCihiPhcvrs.Blank.X);
	        } else {
	        	patientCompliance.setBoolean(pa[p].getPatientCompliance());
	        }
		}

	}

    private void buildImmunizations(Demographic demo, PatientRecord patientRecord) {
    	List<Prevention> preventionsList = getPreventionDao().findNotDeletedByDemographicId(demo.getDemographicNo());

         for( Prevention prevention: preventionsList ) {

             List<PreventionExt> listPreventionExt = preventionExtDao.findByPreventionId(prevention.getId());

        	 Immunizations immunizations = patientRecord.addNewImmunizations();

        	 for( PreventionExt preventionExt : listPreventionExt ) {

	        	 if (preventionExt.getkeyval().equalsIgnoreCase("name") &&  StringUtils.filled(preventionExt.getVal())) {
	        		immunizations.setImmunizationName(preventionExt.getVal());
	        	 }else{
	        	    immunizations.setImmunizationName(prevention.getPreventionType());
	        	 }

	        	 if (preventionExt.getkeyval().equalsIgnoreCase("lot") &&  StringUtils.filled(preventionExt.getVal())) {
	        		 immunizations.setLotNumber(preventionExt.getVal());
	        	 }

	         }

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
