package oscar.oscarDemographic.pageUtil;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;
import org.apache.xmlbeans.XmlOptions;


import oscar.oscarEncounter.oscarMeasurements.data.ImportExportMeasurements;
import oscar.oscarEncounter.oscarMeasurements.data.Measurements;
import oscar.oscarLab.ca.all.parsers.MessageHandler;
import oscar.oscarLab.ca.all.parsers.Factory;
import oscar.oscarReport.data.DemographicSets;
import oscar.oscarRx.data.RxPrescriptionData;
import oscar.OscarProperties;
import oscar.util.StringUtils;
import oscar.util.UtilDateUtilities;

import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.casemgmt.dao.CaseManagementNoteExtDAO;
import org.oscarehr.casemgmt.dao.IssueDAO;
import org.oscarehr.casemgmt.dao.PrescriptionDAO;

import org.oscarehr.common.model.Allergy;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementNoteExt;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.common.model.*;
import org.oscarehr.common.dao.*;
import org.oscarehr.util.MiscUtils;

import cdsDtCihi.DateFullOrDateTime;
import cdsDtCihi.DateFullOrPartial;
import cdsDtCihi.DrugMeasure;
import cdsDtCihi.HealthCard;
import cdsDtCihi.PostalZipCode;
import cdsDtCihi.StandardCoding;
import cdsDtCihi.YnIndicator;
import cdsDtCihi.YnIndicatorAndBlank;
import cdscihi.*;
import cdscihi.AllergiesAndAdverseReactionsDocument.AllergiesAndAdverseReactions;
import cdscihi.AppointmentsDocument.Appointments;
import cdscihi.CareElementsDocument.CareElements;
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
	private AllergyDAO allergyDAO;
	private Hl7TextInfoDao hl7TextInfoDAO;
	private PrescriptionDAO prescriptionDao;
	private PreventionDao preventionDao;
	
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
	
	public void setPrescriptionDao(PrescriptionDAO prescriptionDao) {
	    this.prescriptionDao = prescriptionDao;
    }

	public PrescriptionDAO getPrescriptionDao() {
	    return prescriptionDao;
    }

	public void setHl7TextInfoDAO(Hl7TextInfoDao hl7TextInfoDAO) {
	    this.hl7TextInfoDAO = hl7TextInfoDAO;
    }

	public Hl7TextInfoDao getHl7TextInfoDAO() {
	    return hl7TextInfoDAO;
    }
	
	public void setAllergyDAO(AllergyDAO allergyDAO) {
	    this.allergyDAO = allergyDAO;
    }

	public AllergyDAO getAllergyDAO() {
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
	
	public ActionForward getFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
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
		List<DataExport> dataExportList = dataExportDAO.findAll();
		
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
			frm.set("installDate", properties.getProperty("versionDate", ""));
			
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
	    	tmpDir = System.clearProperty("java.io.tmpdir");
	        MiscUtils.getLogger().error("Error! Cannot write to TMP_DIR - Check oscar.properties or dir permissions. Using " + tmpDir);
	    }
	    tmpDir = Util.fixDirName(tmpDir);
	    
	    
	    //grab patient list from set
	    DemographicSets demoSets = new DemographicSets(); 
		ArrayList patientList = demoSets.getDemographicSet(frm.getString("patientSet"));
		
		//make all xml files, zip them and save to document directory
		String filename = this.make(frm, patientList, tmpDir);		
		
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
    private String make(DynaValidatorForm frm, List patientList, String tmpDir) throws Exception {		 
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
			 try {
				 this.buildCareElements(demo, patientRecord);
			 }
			 catch(SQLException e) {
				 MiscUtils.getLogger().error("Build Care Elements DB Failed", e);
			 }
			 
			 this.buildProcedure(demo, patientRecord);
			 this.buildLaboratoryResults(demo, patientRecord);
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
		info.setContactPhoneNumber(frm.getString("contactPhone"));
		info.setContactEmail(frm.getString("contactEmail"));
		info.setContactUserName(frm.getString("contactUserName"));
		info.setEMRVendorID(frm.getString("vendorId"));
		info.setEMRVendorBusinessName(frm.getString("vendorBusinessName"));
		info.setEMRVendorCommonName(frm.getString("vendorCommonName"));
		info.setEMRSoftwareName(frm.getString("vendorSoftware"));
		info.setEMRSoftwareCommonName(frm.getString("vendorSoftwareCommonName"));
		info.setEMRSoftwareVersionNumber(frm.getString("vendorSoftwareVer"));
		Date installDate = UtilDateUtilities.StringToDate(frm.getString("installDate"));
		Calendar installCalendar = Calendar.getInstance();
		installCalendar.setTime(installDate);
		info.setEMRSoftwareVersionDate(installCalendar);
	}
	
	private void buildProvider(Provider provider, cdscihi.ProviderDocument.Provider xmlProvider, Map<String,String> fileNamesMap) {
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
					
		Date endDate = demo.getEndDate();
		Calendar endCal = Calendar.getInstance();
		if( endDate != null ) {
			endCal.setTime(endDate);
		}
		
		String status = demo.getPatientStatus();
		if( "AC".equalsIgnoreCase(status)) {
			status = "A";
		}
		else if( "IN".equalsIgnoreCase(status)) {
			status = "I";
			xmlDemographics.setEnrollmentTerminationDate(endCal);
		}
		else if( "DE".equalsIgnoreCase(status)) {
			status = "D";
			if( endDate != null ) {
				xmlDemographics.setPersonStatusDate(endCal);
			}
		}
		else if( "TE".equalsIgnoreCase(status)) {
			if( endDate != null ) {
				xmlDemographics.setEnrollmentTerminationDate(endCal);
			}
		}
		
		PersonStatusCode personStatusCode = xmlDemographics.addNewPersonStatusCode();
		personStatusCode.setPersonStatusAsPlainText(status);
				
		Date joinDate = demo.getDateJoined();
		if( joinDate != null ) {
			Calendar joinedCal = Calendar.getInstance();
			joinedCal.setTime(joinDate);
			xmlDemographics.setEnrollmentDate(joinedCal);
		}
		
		PostalZipCode postalZipCode = xmlDemographics.addNewPostalAddress();
		postalZipCode.setPostalCode(demo.getPostal());
		
	}
	
	private void buildAppointment(Demographic demo, PatientRecord patientRecord) {
		List<Appointment> appointmentList = oscarAppointmentDao.getAppointmentHistory(demo.getDemographicNo());
		
		Calendar cal = Calendar.getInstance();
		
		for( Appointment appt: appointmentList) {
			Appointments appointments = patientRecord.addNewAppointments();
			appointments.setAppointmentPurpose(appt.getReason());
			DateFullOrPartial dateFullorPartial = appointments.addNewAppointmentDate();			
			cal.setTime(appt.getAppointmentDate());			
			dateFullorPartial.setFullDate(cal);
		}
	}
	
	@SuppressWarnings("unchecked")
    private void buildFamilyHistory(Demographic demo, PatientRecord patientRecord ) {
		List<Issue> issueList = issueDAO.findIssueByCode(new String[] {"FamHistory"});
		String[] famHistory = new String[] {issueList.get(0).getId().toString()};
		
		Calendar cal = Calendar.getInstance();
		
	    String intervention;
	    String relationship;
	    String age;
	    Date startDate;	    
	    boolean hasIssue;
	    OscarProperties properties = OscarProperties.getInstance();
	    
		List<CaseManagementNote> notesList = getCaseManagementNoteDAO().getActiveNotesByDemographic(demo.getDemographicNo().toString(), famHistory);
		for( CaseManagementNote caseManagementNote: notesList) {
			
			intervention = "";
			relationship = "";
			age = "";
			startDate = null;				
			hasIssue = false;
			
			List<CaseManagementNoteExt> caseManagementNoteExtList = getCaseManagementNoteExtDAO().getExtByNote(caseManagementNote.getId());
            String keyval;
            for( CaseManagementNoteExt caseManagementNoteExt: caseManagementNoteExtList ) {
                keyval = caseManagementNoteExt.getKeyVal();
                if( keyval.equals("Treatment")) {
                    intervention = caseManagementNoteExt.getValue();
                }
                else if( keyval.equals("Relationship")) {
                    relationship = caseManagementNoteExt.getValue();
                }
                else if( keyval.equals("Age at Onset")) {
                    age = caseManagementNoteExt.getValue();
                }
                else if( keyval.equals("Start Date")) {
                    startDate = caseManagementNoteExt.getDateValue();
                }                
            }
            
            FamilyHistory familyHistory = patientRecord.addNewFamilyHistory();
            
            Set<CaseManagementIssue> noteIssueList = caseManagementNote.getIssues();
            if( noteIssueList != null && noteIssueList.size() > 0 ) {
                Iterator<CaseManagementIssue> i = noteIssueList.iterator();
                CaseManagementIssue cIssue;                
                hasIssue = true;
                while( i.hasNext() ) {
                	cIssue = i.next();
                	StandardCoding standardCoding = familyHistory.addNewDiagnosisProcedureCode();
                	standardCoding.setStandardCode(cIssue.getIssue().getCode());
                	standardCoding.setStandardCodingSystem(properties.getProperty("dxResearch_coding_sys","icd9"));
                	standardCoding.setStandardCodeDescription(cIssue.getIssue().getDescription());
                }

            }
                                    
            if( startDate != null ) {
            	DateFullOrPartial dateFullOrPartial = familyHistory.addNewStartDate();
            	cal.setTime(startDate);
            	dateFullOrPartial.setFullDate(cal);
            }
            
            if( !"".equalsIgnoreCase(age) ) {
            	familyHistory.setAgeAtOnset(BigInteger.valueOf(Long.parseLong(age)));
            }
            
            if( !hasIssue ) {
            	familyHistory.setProblemDiagnosisProcedureDescription(caseManagementNote.getNote());
            }
            
            if( !"".equalsIgnoreCase(intervention)) {
            	familyHistory.setTreatment(intervention);
            }
            
            if( !"".equalsIgnoreCase(relationship)) {
            	familyHistory.setRelationship(relationship);
            }
		}
	}
	
	@SuppressWarnings("unchecked")
    private void buildOngoingProblems(Demographic demo, PatientRecord patientRecord) {
		List<Issue> issueList = issueDAO.findIssueByCode(new String[] {"Concerns"});
		String[] onGoingConcerns = new String[] {issueList.get(0).getId().toString()};
		
		Calendar cal = Calendar.getInstance();
			   
	    Date startDate;
	    Date endDate;
	    boolean hasIssue;
	    OscarProperties properties = OscarProperties.getInstance();

		List<CaseManagementNote> notesList = getCaseManagementNoteDAO().getActiveNotesByDemographic(demo.getDemographicNo().toString(), onGoingConcerns);
		for( CaseManagementNote caseManagementNote: notesList) {
			
			startDate = null;
			endDate = null;
			hasIssue = false;
			
			List<CaseManagementNoteExt> caseManagementNoteExtList = getCaseManagementNoteExtDAO().getExtByNote(caseManagementNote.getId());
            String keyval;
            for( CaseManagementNoteExt caseManagementNoteExt: caseManagementNoteExtList ) {
                keyval = caseManagementNoteExt.getKeyVal();
                
                if( keyval.equals("Start Date")) {
                    startDate = caseManagementNoteExt.getDateValue();
                }
                else if( keyval.equals("Resolution Date")) {
                    endDate = caseManagementNoteExt.getDateValue();
                }
            }
            
            ProblemList problemList = patientRecord.addNewProblemList();
            
            Set<CaseManagementIssue> noteIssueList = caseManagementNote.getIssues();
            if( noteIssueList != null && noteIssueList.size() > 0 ) {
                Iterator<CaseManagementIssue> i = noteIssueList.iterator();
                CaseManagementIssue cIssue;                
                hasIssue = true;
                while( i.hasNext() ) {
                	cIssue = i.next();
                	StandardCoding standardCoding = problemList.addNewDiagnosisCode();
                	standardCoding.setStandardCode(cIssue.getIssue().getCode());
                	standardCoding.setStandardCodingSystem(properties.getProperty("dxResearch_coding_sys","icd9"));
                	standardCoding.setStandardCodeDescription(cIssue.getIssue().getDescription());
                }

            }
                                    
            if( startDate != null ) {
            	DateFullOrPartial dateFullOrPartial = problemList.addNewOnsetDate();
            	cal.setTime(startDate);
            	dateFullOrPartial.setFullDate(cal);
            }
            
            if( endDate != null ) {
            	DateFullOrPartial dateFullOrPartial = problemList.addNewResolutionDate();
            	cal.setTime(endDate);
            	dateFullOrPartial.setFullDate(cal);
            }
                        
            if( !hasIssue ) {
            	problemList.setProblemDiagnosisDescription(caseManagementNote.getNote());
            }
                        
		}
	}
	
	@SuppressWarnings("unchecked")
    private void buildRiskFactors(Demographic demo, PatientRecord patientRecord) {
		List<Issue> issueList = issueDAO.findIssueByCode(new String[] {"RiskFactors"});
		String[] riskFactor = new String[] {issueList.get(0).getId().toString()};
		
		Calendar cal = Calendar.getInstance();			    
	    Date startDate;
	    Date endDate;
	    
		List<CaseManagementNote> notesList = getCaseManagementNoteDAO().getActiveNotesByDemographic(demo.getDemographicNo().toString(), riskFactor);
		for( CaseManagementNote caseManagementNote: notesList) {
			
			startDate = null;
			endDate = null;
			
			List<CaseManagementNoteExt> caseManagementNoteExtList = getCaseManagementNoteExtDAO().getExtByNote(caseManagementNote.getId());
            String keyval;
            for( CaseManagementNoteExt caseManagementNoteExt: caseManagementNoteExtList ) {
                keyval = caseManagementNoteExt.getKeyVal();
                
                if( keyval.equals("Start Date")) {
                    startDate = caseManagementNoteExt.getDateValue();
                }
                else if( keyval.equals("Resolution Date")) {
                    endDate = caseManagementNoteExt.getDateValue();
                }
            }
            
            RiskFactors riskFactors = patientRecord.addNewRiskFactors();
                        
                                    
            if( startDate != null ) {
            	DateFullOrPartial dateFullOrPartial = riskFactors.addNewStartDate();
            	cal.setTime(startDate);
            	dateFullOrPartial.setFullDate(cal);
            }
            
            if( endDate != null ) {
            	DateFullOrPartial dateFullOrPartial = riskFactors.addNewEndDate();
            	cal.setTime(endDate);
            	dateFullOrPartial.setFullDate(cal);
            }
                                    
            riskFactors.setRiskFactor(caseManagementNote.getNote());                                    
		}
	}
	
	private void buildAllergies(Demographic demo, PatientRecord patientRecord) {
		String[] severity = new String[] {"MI","MO","LT","NO"};
		List<Allergy> allergies = allergyDAO.getAllergies(demo.getDemographicNo().toString());
		int index;
		Calendar cal = Calendar.getInstance();
		Date date;
        for( Allergy allergy: allergies ) {
        	AllergiesAndAdverseReactions xmlAllergies = patientRecord.addNewAllergiesAndAdverseReactions();
        	xmlAllergies.setPropertyOfOffendingAgent(cdsDtCihi.PropertyOfOffendingAgent.DR);
        	xmlAllergies.setOffendingAgentDescription(allergy.getDescription());
        	try {
                index = Integer.parseInt(allergy.getSeverityOfReaction());
            }catch(Exception e ) {
                index = 4;
            }
        	xmlAllergies.setSeverity(cdsDtCihi.AdverseReactionSeverity.Enum.forString(severity[index-1]));
        	date = allergy.getStartDate();
        	if( date != null ) {
        		DateFullOrPartial dateFullOrPartial = xmlAllergies.addNewStartDate();        	
        		cal.setTime(date);
        		dateFullOrPartial.setFullDate(cal);
        	}
        	
        }
	}
	
	@SuppressWarnings("unchecked")
    private void buildCareElements(Demographic demo, PatientRecord patientRecord) throws SQLException {
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
	
	private void buildProcedure(Demographic demo, PatientRecord patientRecord) {
		OscarProperties properties = OscarProperties.getInstance();
		Calendar cal = Calendar.getInstance();
		Date procedureDate;
		boolean hasIssue;
		String code;
		Procedure procedure = patientRecord.addNewProcedure();
		List<CaseManagementNote> notesList = getCaseManagementNoteDAO().getNotesByDemographic(demo.getDemographicNo().toString());
		for( CaseManagementNote caseManagementNote: notesList) {
			
			hasIssue = false;
			Set<CaseManagementIssue> noteIssueList = caseManagementNote.getIssues();
            if( noteIssueList != null && noteIssueList.size() > 0 ) {
                Iterator<CaseManagementIssue> i = noteIssueList.iterator();
                CaseManagementIssue cIssue;                
                hasIssue = true;
                while( i.hasNext() ) {                	
                	cIssue = i.next();
                	code = cIssue.getIssue().getCode();
                	if( code.equalsIgnoreCase("Concerns") || code.equalsIgnoreCase("OMeds") || code.equalsIgnoreCase("SocHistory") || code.equalsIgnoreCase("MedHistory")
                			|| code.equalsIgnoreCase("Reminders") || code.equalsIgnoreCase("FamHistory")) {
                		continue;
                	}
                	StandardCoding procedureCode = procedure.addNewProcedureCode();
                	procedureCode.setStandardCodingSystem(properties.getProperty("dxResearch_coding_sys","icd9"));
                	procedureCode.setStandardCode(cIssue.getIssue().getCode());
                	procedureCode.setStandardCodeDescription(cIssue.getIssue().getDescription());
                }
            }
            
            if( !hasIssue ) {
            	procedure.setProcedureInterventionDescription(caseManagementNote.getNote());
            }
            
            procedureDate = caseManagementNote.getObservation_date();
            cal.setTime(procedureDate);
            DateFullOrPartial dateFullOrPartial = procedure.addNewProcedureDate();
            dateFullOrPartial.setFullDate(cal);
		}
	}
	
	private void buildLaboratoryResults(Demographic demo, PatientRecord patientRecord) {		
		String dateStr;		
		MessageHandler handler;
		LaboratoryResults labResults = patientRecord.addNewLaboratoryResults();
		List<Hl7TextInfo> hl7TextInfoList = this.getHl7TextInfoDAO().findByHealthCardNo(demo.getHin());		
		for( Hl7TextInfo hl7TextInfo: hl7TextInfoList ) {
			DateFullOrDateTime dateFullOrTime = labResults.addNewCollectionDateTime();
			handler = Factory.getHandler("" + hl7TextInfo.getLabNumber());
			dateStr = handler.getServiceDate();
			dateFullOrTime.setFullDateTime(Util.calDate(dateStr));				
		}
	}
	
	
    private void buildMedications(Demographic demo, PatientRecord patientRecord) {
		MedicationsAndTreatments medications;
		RxPrescriptionData.Prescription[] pa = new RxPrescriptionData().getUniquePrescriptionsByPatient(Integer.parseInt(demo.getDemographicNo().toString()));
		String drugname;
		String dosage;
		String strength;
		int sep;
		
		for(int p = 0; p<pa.length; ++p) {
			medications = patientRecord.addNewMedicationsAndTreatments();
			
			drugname = StringUtils.noNull(pa[p].getDrugName());			
			medications.setDrugName(drugname);
			DateFullOrPartial dateFullorPartial = medications.addNewPrescriptionWrittenDate();
			dateFullorPartial.setFullDate(Util.calDate(pa[p].getRxDate()));
						
	        if( StringUtils.filled(pa[p].getDosage()) ) {
	        	strength = pa[p].getDosage();
	        	sep = strength.indexOf("/");
	        	strength = sep<0 ? strength : strength.substring(0,sep);
	        	if( sep > 0 ) {
	        		strength.substring(0,sep);
	        	}
	        	DrugMeasure drugMeasure = medications.addNewStrength();
	        	drugMeasure.setAmount(strength.substring(0,strength.indexOf(" ")));
	        	drugMeasure.setUnitOfMeasure(strength.substring(strength.indexOf(" ")+1));
	        }
	        
	        dosage = StringUtils.noNull(pa[p].getDosageDisplay());
	        medications.setDosage(dosage);
	        medications.setDosageUnitOfMeasure(StringUtils.noNull(pa[p].getUnit()));
	        medications.setForm(StringUtils.noNull(pa[p].getDrugForm()));
	        medications.setFrequency(StringUtils.noNull(pa[p].getFreqDisplay()));
	        medications.setRoute(StringUtils.noNull(pa[p].getRoute()));
	        medications.setNumberOfRefills(String.valueOf(pa[p].getRepeat()));
	        
	        YnIndicatorAndBlank patientCompliance = medications.addNewPatientCompliance();
	        if (pa[p].getPatientCompliance()==0) {
	        	patientCompliance.setBlank(cdsDtCihi.Blank.X);	        	
	        }
	        else {
	        	patientCompliance.setBoolean(pa[p].getPatientCompliance()==1);
	        }
		}
		
	}
	    
    private void buildImmunizations(Demographic demo, PatientRecord patientRecord) {
    	HashMap<String,String> preventionMap;    	
    	List<Prevention> preventionsList = getPreventionDao().findNotDeletedByDemographicId(demo.getDemographicNo());
    	 
         for( Prevention prevention: preventionsList ) {
             preventionMap = getPreventionDao().getPreventionExt(prevention.getId());
             if( preventionMap.containsKey("lot") ) {
            	 Immunizations immunizations = patientRecord.addNewImmunizations();
            	 immunizations.setImmunizationName(prevention.getPreventionType());
            	 DateFullOrPartial dateFullorPartial = immunizations.addNewDate();
            	 dateFullorPartial.setFullDate(Util.calDate(prevention.getPreventionDate()));
            	 immunizations.setLotNumber(preventionMap.get("lot"));
            	 
            	 YnIndicator refusedIndicator = immunizations.addNewRefusedFlag();
                 if( prevention.isRefused() ) {
                	 refusedIndicator.setBoolean(true);
                 }
                 else {
                     refusedIndicator.setBoolean(false);
                 }                 

             }
         }
    }	
    
    private String makeFiles(Map<String,CiHiCdsDocument> xmlMap, Map<String,String> fileNamesMap, String tmpDir) throws Exception {
    	XmlOptions options = new XmlOptions();
    	options.put( XmlOptions.SAVE_PRETTY_PRINT );
    	options.put( XmlOptions.SAVE_PRETTY_PRINT_INDENT, 3 );
    	options.put( XmlOptions.SAVE_AGGRESSIVE_NAMESPACES );
    	options.setSaveOuter();
    	
    	ArrayList<File> files = new ArrayList<File>();
    	int idx = 0;
    	CiHiCdsDocument cihiDoc;
    	Set<String>xmlKeys = xmlMap.keySet();
    	
    	//Save each file
    	for( String key: xmlKeys) {    		
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
