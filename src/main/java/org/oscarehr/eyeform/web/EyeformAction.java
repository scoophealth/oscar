package org.oscarehr.eyeform.web;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;
import org.caisi.dao.TicklerDAO;
import org.caisi.model.Tickler;
import org.oscarehr.PMmodule.dao.ClientDao;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.casemgmt.dao.AllergyDAO;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.casemgmt.model.Allergy;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.IsPropertiesOn;
import org.oscarehr.common.dao.BillingreferralDao;
import org.oscarehr.common.dao.ClinicDAO;
import org.oscarehr.common.dao.ConsultationRequestExtDao;
import org.oscarehr.common.dao.DocumentResultsDao;
import org.oscarehr.common.dao.EFormGroupDao;
import org.oscarehr.common.dao.EFormValueDao;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.dao.ProfessionalSpecialistDao;
import org.oscarehr.common.dao.SiteDao;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.Billingreferral;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DemographicExt;
import org.oscarehr.common.model.Document;
import org.oscarehr.common.model.EFormGroup;
import org.oscarehr.common.model.EFormValue;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Site;
import org.oscarehr.common.service.PdfRecordPrinter;
import org.oscarehr.eyeform.MeasurementFormatter;
import org.oscarehr.eyeform.dao.ConsultationReportDao;
import org.oscarehr.eyeform.dao.EyeFormDao;
import org.oscarehr.eyeform.dao.FollowUpDao;
import org.oscarehr.eyeform.dao.OcularProcDao;
import org.oscarehr.eyeform.dao.ProcedureBookDao;
import org.oscarehr.eyeform.dao.SpecsHistoryDao;
import org.oscarehr.eyeform.dao.TestBookRecordDao;
import org.oscarehr.eyeform.model.ConsultationReport;
import org.oscarehr.eyeform.model.EyeForm;
import org.oscarehr.eyeform.model.FollowUp;
import org.oscarehr.eyeform.model.OcularProc;
import org.oscarehr.eyeform.model.ProcedureBook;
import org.oscarehr.eyeform.model.SatelliteClinic;
import org.oscarehr.eyeform.model.SpecsHistory;
import org.oscarehr.eyeform.model.TestBookRecord;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.oscarEncounter.oscarMeasurements.dao.MeasurementsDao;
import oscar.oscarEncounter.oscarMeasurements.model.Measurements;
import oscar.util.UtilDateUtilities;

import com.lowagie.text.DocumentException;

public class EyeformAction extends DispatchAction {

	static Logger logger = MiscUtils.getLogger();
	static String[] cppIssues = {"CurrentHistory","PastOcularHistory","MedHistory","OMeds","OcularMedication","DiagnosticNotes","FamHistory"};
	
	CaseManagementManager cmm = null;
	OscarAppointmentDao appointmentDao = (OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");
	ClientDao demographicDao= (ClientDao)SpringUtils.getBean("clientDao");
	ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
	CaseManagementNoteDAO caseManagementNoteDao = (CaseManagementNoteDAO)SpringUtils.getBean("CaseManagementNoteDAO");
	OcularProcDao ocularProcDao = (OcularProcDao)SpringUtils.getBean("OcularProcDAO");
	SpecsHistoryDao specsHistoryDao = (SpecsHistoryDao)SpringUtils.getBean("SpecsHistoryDAO");
	AllergyDAO allergyDao = (AllergyDAO)SpringUtils.getBean("AllergyDAO");
	FollowUpDao followUpDao = (FollowUpDao)SpringUtils.getBean("FollowUpDAO");
	ProcedureBookDao procedureBookDao = (ProcedureBookDao)SpringUtils.getBean("ProcedureBookDAO");
	TestBookRecordDao testBookDao = (TestBookRecordDao)SpringUtils.getBean("TestBookDAO");
	EyeFormDao eyeFormDao = (EyeFormDao)SpringUtils.getBean("EyeFormDao");	   		
	MeasurementsDao measurementsDao = (MeasurementsDao) SpringUtils.getBean("measurementsDao");
	BillingreferralDao brDao = (BillingreferralDao)SpringUtils.getBean("BillingreferralDAO");
	ClinicDAO clinicDao = (ClinicDAO)SpringUtils.getBean("clinicDAO");
	SiteDao siteDao = (SiteDao)SpringUtils.getBean("siteDao");
	TicklerDAO ticklerDao = (TicklerDAO)SpringUtils.getBean("ticklerDAOT");
	
	   public ActionForward getConReqCC(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		   String requestId = request.getParameter("requestId");
		   ConsultationRequestExtDao dao = (ConsultationRequestExtDao)SpringUtils.getBean("consultationRequestExtDao");
		   String cc = "";
		   if(requestId != null) {
			   try {
				   int reqId = Integer.parseInt(requestId);
				   String value = dao.getConsultationRequestExtsByKey(reqId,"cc");
				   if(value!=null)
					   cc = value;
			   } catch(NumberFormatException e){}
		   }
		   request.setAttribute("requestCc", cc);		   
		   ProfessionalSpecialistDao professionalSpecialistDao=(ProfessionalSpecialistDao)SpringUtils.getBean("professionalSpecialistDao");
		   List<ProfessionalSpecialist> psList = professionalSpecialistDao.findAll();
		   request.setAttribute("professionalSpecialists",psList);
	       return mapping.findForward("conreqcc");
	    }
	   
	   
	   public ActionForward specialConRequestHTML(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		   ConsultationRequestExtDao consultationRequestExtDao=(ConsultationRequestExtDao)SpringUtils.getBean("consultationRequestExtDao");
		   
		   String reqId = request.getParameter("requestId");
		   int requestId;
		   
		   if(reqId == null || reqId.length()==0 || reqId.equals("null")) {
			   requestId = 0;
		   } else {
			   requestId = Integer.parseInt(reqId);
		   }
		   
		   String specialProblem = "";
		   if(requestId>0) {
			   specialProblem = consultationRequestExtDao.getConsultationRequestExtsByKey(requestId, "specialProblem");			   
		   }
		   request.setAttribute("ext_specialProblem", specialProblem);
		   
		   return mapping.findForward("conspecialhtml");
	   }
	   
	   public ActionForward specialConRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		   String demo = request.getParameter("demographicNo");
		   String strAppNo = request.getParameter("appNo");
		   String reqId = request.getParameter("requestId");
		   
		   Provider provider = LoggedInInfo.loggedInInfo.get().loggedInProvider;
		   ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
		   ConsultationRequestExtDao consultationRequestExtDao=(ConsultationRequestExtDao)SpringUtils.getBean("consultationRequestExtDao");
		   OcularProcDao ocularProcDao = (OcularProcDao)SpringUtils.getBean("OcularProcDAO");
		   SpecsHistoryDao specsHistoryDao = (SpecsHistoryDao)SpringUtils.getBean("SpecsHistoryDAO");
		   
		   int appNo;		   
		   int requestId;
		   if(strAppNo == null || strAppNo.length()==0 || strAppNo.equals("null")) {
			   appNo = 0;
		   } else {
			   appNo = Integer.parseInt(strAppNo);
		   }
		   if(reqId == null || reqId.length()==0 || reqId.equals("null")) {
			   requestId = 0;
		   } else {
			   requestId = Integer.parseInt(reqId);
		   }
		   
		   request.setAttribute("providerList", providerDao.getActiveProviders());
		   request.setAttribute("re_demoNo", demo);
		   		   
		   
		   request.setAttribute("currentHistory",StringEscapeUtils.escapeJavaScript(getFormattedCppItem("Current History:", "CurrentHistory", Integer.parseInt(demo), appNo, false)));
		   request.setAttribute("ocularMedication",StringEscapeUtils.escapeJavaScript(getFormattedCppItem("Current Medications:", "OcularMedication", Integer.parseInt(demo), appNo, true)));
		   request.setAttribute("pastOcularHistory",StringEscapeUtils.escapeJavaScript(getFormattedCppItem("Past Ocular History:", "PastOcularHistory", Integer.parseInt(demo), appNo, true)));
		   request.setAttribute("diagnosticNotes",StringEscapeUtils.escapeJavaScript(getFormattedCppItem("Diagnostic Notes:", "DiagnosticNotes", Integer.parseInt(demo), appNo, true)));

		   
		   SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		   List<OcularProc> ocularProcs = ocularProcDao.getHistory(Integer.parseInt(demo), new Date(), "A");
		   StringBuilder ocularProc = new StringBuilder();
		   for(OcularProc op:ocularProcs) {
               ocularProc.append(sf.format(op.getDate()) + " ");
               ocularProc.append(op.getEye() + " ");
               ocularProc.append(op.getProcedureName() + " at " + op.getLocation());
               ocularProc.append(" by " + providerDao.getProvider(op.getDoctor()).getFormattedName());
               if (op.getProcedureNote() != null && !"".equalsIgnoreCase(op.getProcedureNote().trim()))                      
            	   ocularProc.append(". " + op.getProcedureNote() + "\n");			   		   
		   }
           String strOcularProcs = ocularProc.toString();
           if (strOcularProcs != null && !"".equalsIgnoreCase(strOcularProcs.trim()))
        	   strOcularProcs = "Past Ocular Proc:\n" + strOcularProcs + "\n";
           else
        	   strOcularProcs = "";           
           request.setAttribute("ocularProc", StringEscapeUtils.escapeJavaScript(strOcularProcs));
		   
		   
           List<SpecsHistory> specs = specsHistoryDao.getHistory(Integer.parseInt(demo), new Date(), null);
           StringBuilder specsStr = new StringBuilder();
           for(SpecsHistory spec:specs) {
        	   String specDate = sf.format(spec.getDate());
        	   String specDoctor = spec.getDoctor();
        	   String specType = spec.getType();
        	   specsStr.append(specDate + " ");

               StringBuilder data = new StringBuilder("");
               data.append("OD ");
               StringBuilder dataTemp = new StringBuilder("");
               dataTemp.append(spec.getOdSph() == null ? "" : spec.getOdSph());
               dataTemp.append(spec.getOdCyl() == null ? "" : spec.getOdCyl());
               if (spec.getOdAxis() != null
                               && spec.getOdAxis().trim().length() != 0)
                       dataTemp.append("x" + spec.getOdAxis());
               if (spec.getOdAdd() != null
                               && spec.getOdAdd().trim().length() != 0)
                       dataTemp.append("add" + spec.getOdAdd());
               specsStr.append(dataTemp.toString() + " " + spec.getType() + " "
                               + spec.getDoctor() + " ");
               data.append(dataTemp);

               String secHead = "\n      OS ";
               data.append(secHead);
               dataTemp = new StringBuilder("");
               dataTemp.append(spec.getOsSph() == null ? "" : spec.getOsSph());
               dataTemp.append(spec.getOsCyl() == null ? "" : spec.getOsCyl());
               if (spec.getOsAxis() != null
                       && spec.getOsAxis().trim().length() != 0)
                       dataTemp.append("x" + spec.getOsAxis());
               if (spec.getOsAdd() != null
                               && spec.getOsAdd().trim().length() != 0)
                       dataTemp.append("add" + spec.getOsAdd());
               specsStr.append(dataTemp.toString() + "\n");
               data.append(dataTemp);                             
           }
           String specsStr1 = "";
           if (specsStr != null && specs.size()>0)
               specsStr1  = "specs:\n" + specsStr.toString() + "\n";
           else
    	   		specsStr1 = "";
      
           request.setAttribute("specs", StringEscapeUtils.escapeJavaScript(specsStr1));
           
           //impression
           logger.info("appNo="+appNo);
           if(requestId > 0) {
        	   //get the saved app no.
        	   String tmp = consultationRequestExtDao.getConsultationRequestExtsByKey(requestId, "appNo");
        	   appNo = Integer.parseInt(tmp);        	   
           }
           String impression = getImpression(appNo);
           request.setAttribute("impression", StringEscapeUtils.escapeJavaScript(impression));
           
           
           
           
           //followUp
           FollowUpDao followUpDao = (FollowUpDao)SpringUtils.getBean("FollowUpDAO");
           List<FollowUp> followUps = followUpDao.getByAppointmentNo(appNo);
           StringBuilder followup = new StringBuilder();
           for(FollowUp ef:followUps) {		   		
				if (ef.getTimespan() >0) {
					followup.append((ef.getType().equals("followup")?"Follow Up":"Consult") + " in " + ef.getTimespan() + " " + ef.getTimeframe() + "\n");
				}								
           }
           
           //get the checkboxes
           EyeForm eyeform = eyeFormDao.getByAppointmentNo(appNo);           
           if(eyeform != null) {
	           if (eyeform.getDischarge() != null && eyeform.getDischarge().equals("true"))
					followup.append("Patient is discharged from my active care.\n");
	           if (eyeform.getStat() != null && eyeform.getStat().equals("true"))
					followup.append("Follow up as needed with me STAT or PRN if symptoms are worse.\n");				
	           if (eyeform.getOpt() != null && eyeform.getOpt().equals("true"))
					followup.append("Routine eye care by an optometrist is recommended.\n");
           }
                                 
           request.setAttribute("followup", StringEscapeUtils.escapeJavaScript(followup.toString()));
           
           
           //test book
           TestBookRecordDao testBookDao = (TestBookRecordDao)SpringUtils.getBean("TestBookDAO");
           List<TestBookRecord> testBookRecords = testBookDao.getByAppointmentNo(appNo);
           StringBuilder testbook = new StringBuilder();
           for(TestBookRecord tt:testBookRecords) {
        	   testbook.append(tt.getTestname());
   				testbook.append(" ");
   				testbook.append(tt.getEye());
   				testbook.append("\n");
           }
           if (testbook.length() > 0)			
        	   testbook.insert(0, "Diagnostic test booking:");
           request.setAttribute("testbooking", StringEscapeUtils.escapeJavaScript(testbook.toString()));

				
           //procedure book
           ProcedureBookDao procBookDao = (ProcedureBookDao)SpringUtils.getBean("ProcedureBookDAO");
           List<ProcedureBook> procBookRecords = procBookDao.getByAppointmentNo(appNo);          
           StringBuilder probook = new StringBuilder();
           for(ProcedureBook pp:procBookRecords) {
        	   probook.append(pp.getProcedureName());   			
        	   probook.append(" ");   			
        	   probook.append(pp.getEye());   			
        	   probook.append("\n");
           }
           if (probook.length() > 0)			
        	   probook.insert(0, "Procedure booking:");           				
           request.setAttribute("probooking", StringEscapeUtils.escapeJavaScript(probook.toString()));
		
           
           //specs now
           
           
           //measurements
           MeasurementsDao measurementsDao = (MeasurementsDao) SpringUtils.getBean("measurementsDao");                           
		   
		   return mapping.findForward("conspecial");
	   }
		
	   public String getFormattedCppItem(String header, String issueCode, int demographicNo, int appointmentNo, boolean includePrevious) {
		   Collection<CaseManagementNote> notes = null;
		   if(!includePrevious) {
			    notes = filterNotesByAppointment(caseManagementNoteDao.findNotesByDemographicAndIssueCode(demographicNo, new String[] {issueCode}),appointmentNo);			   
		   } else {
			   notes = filterNotesByPreviousOrCurrentAppointment(caseManagementNoteDao.findNotesByDemographicAndIssueCode(demographicNo, new String[] {issueCode}),appointmentNo);								
		   }
		   if(notes.size()>0) {
			   StringBuilder sb = new StringBuilder();		 
			   for(CaseManagementNote note:notes) {
				   sb.append(note.getNote()).append("\n");
			   }
			   return header + "\n" + sb.toString();
		   }
		   return new String();
	   }
	   
	   private String getCppItemAsString(String demo, String issueCode, String text) {
		   if(cmm==null)
			   cmm=(CaseManagementManager) SpringUtils.getBean("caseManagementManager");
		   
		   Issue issue = cmm.getIssueInfoByCode(issueCode);
		   if(issue ==null) {logger.warn("no issue for current history");return "";}
		   List<CaseManagementNote> notes = cmm.getCPP(demo, issue.getId(), null);
		   StringBuilder sb = new StringBuilder();		 
		   for(CaseManagementNote note:notes) {
			   sb.append(note.getNote()).append("\n");
		   }
		   logger.info(issueCode +":" + sb.toString());
		   
		   return text + "\n" + sb.toString();
	   }
	   
	   private String getImpression(int appointmentNo) {
		   List<CaseManagementNote> notes = caseManagementNoteDao.getMostRecentNotesByAppointmentNo(appointmentNo);
		   notes = filterOutCpp(notes);		
		   if(notes.size()>0) {
			   StringBuilder sb = new StringBuilder();		 
			   for(CaseManagementNote note:notes) {
				   sb.append(note.getNote()).append("\n");
			   }
			   return "Impression:" + "\n" + sb.toString();
		   }
		   return new String();
	   }
	   
	   public ActionForward print(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		   response.setContentType("application/pdf"); // octet-stream
			response.setHeader("Content-Disposition", "attachment; filename=\"Encounter-" + UtilDateUtilities.getToday("yyyy-MM-dd.hh.mm.ss") + ".pdf\"");
			doPrint(request, response.getOutputStream());
			return null;
	   }
	   
		
	   public void doPrint(HttpServletRequest request, OutputStream os) throws IOException, DocumentException {
			String ids[] = request.getParameter("apptNos").split(",");
			String providerNo = LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo();

			PdfRecordPrinter printer = new PdfRecordPrinter(request, os);
			
			
			//loop through each visit..concatenate into 1 PDF			
			for(int x=0;x<ids.length;x++) {
				if(x>0) {
					printer.setNewPage(true);
				}
				int appointmentNo = Integer.parseInt(ids[x]);
				Appointment appointment = appointmentDao.find(appointmentNo);
				Demographic demographic = demographicDao.getClientByDemographicNo(appointment.getDemographicNo());				
				printer.setDemographic(demographic);
				printer.setAppointment(appointment);
				printer.printDocHeaderFooter();
					
				//get cpp items by appointmentNo (current history,past ocular hx,
				//medical hx, ocular meds, other meds, diagnostic notes			
				printCppItem(printer,"Current History","CurrentHistory",demographic.getDemographicNo(), appointmentNo, false);
				printCppItem(printer,"Past Ocular History","PastOcularHistory",demographic.getDemographicNo(), appointmentNo, true);
				printCppItem(printer,"Medical History","MedHistory",demographic.getDemographicNo(), appointmentNo, true);
				printCppItem(printer,"Family History","FamHistory",demographic.getDemographicNo(), appointmentNo, true);				
				printCppItem(printer,"Ocular Medication","OcularMedication",demographic.getDemographicNo(), appointmentNo, true);
				printCppItem(printer,"Other Meds","OMeds",demographic.getDemographicNo(), appointmentNo, true);
				printCppItem(printer,"Diagnostic Notes","DiagnosticNotes",demographic.getDemographicNo(), appointmentNo, false);
				printer.setNewPage(true);
				
				//ocular procs
				List<OcularProc> ocularProcs = ocularProcDao.getAllPreviousAndCurrent(demographic.getDemographicNo(),appointmentNo);
				printer.printOcularProcedures(ocularProcs);
				
				//specs history
				List<SpecsHistory> specsHistory = specsHistoryDao.getAllPreviousAndCurrent(demographic.getDemographicNo(),appointmentNo);
				printer.printSpecsHistory(specsHistory);
				
				//allergies
				List<Allergy> allergies = allergyDao.getAllergies(String.valueOf(demographic.getDemographicNo()));
				printer.printAllergies(allergies);
				
				//rx
				printer.printRx(String.valueOf(demographic.getDemographicNo()));
				
				//measurements
				List<Measurements> measurements = measurementsDao.getMeasurementsByAppointment(appointmentNo);
				MeasurementFormatter formatter = new MeasurementFormatter(measurements);
				printer.printEyeformMeasurements(formatter);
				
				//impression
				logger.info("printing notes for appt " + appointmentNo);
				List<CaseManagementNote> notes = caseManagementNoteDao.getMostRecentNotesByAppointmentNo(appointmentNo);
				logger.info("found "  + notes.size());
				notes = filterOutCpp(notes);			
				logger.info("filtered down to " +  notes.size());
				printer.printNotes(notes);
				
				//plan - followups/consults, procedures booked, tests booked, checkboxes
				List<FollowUp> followUps = followUpDao.getByAppointmentNo(appointmentNo);
				List<ProcedureBook> procedureBooks = procedureBookDao.getByAppointmentNo(appointmentNo);
				List<TestBookRecord> testBooks = testBookDao.getByAppointmentNo(appointmentNo);
				EyeForm eyeform = eyeFormDao.getByAppointmentNo(appointmentNo);
		        printer.printEyeformPlan(followUps, procedureBooks, testBooks,eyeform);
				
		        //photos
		        DocumentResultsDao documentDao = (DocumentResultsDao)SpringUtils.getBean("documentResultsDao");
		        List<Document> documents = documentDao.getPhotosByAppointmentNo(appointmentNo);
		        if(documents.size()>0) {
		        	String servletUrl  = request.getRequestURL().toString();
		        	String url = servletUrl.substring(0,servletUrl.indexOf(request.getContextPath())+request.getContextPath().length());
		        	printer.printPhotos(url,documents);
		        }
		        
		        //diagrams
		        EFormValueDao eFormValueDao = (EFormValueDao) SpringUtils.getBean("EFormValueDao");
		        EFormGroupDao eFormGroupDao = (EFormGroupDao) SpringUtils.getBean("EFormGroupDao");
		        List<EFormGroup> groupForms = eFormGroupDao.getByGroupName("Eye form");
		        List<EFormValue> values = eFormValueDao.findByApptNo(appointmentNo);
		        List<EFormValue> diagrams = new ArrayList<EFormValue>();
		        for(EFormValue value:values) {
		        	int formId = value.getFormId();
		        	boolean include=false;
		        	for(EFormGroup group:groupForms) {
		        		if(group.getFormId() == formId) {
		        			include=true;
		        			break;
		        		}
		        	}
		        	if(include)
		        		diagrams.add(value);
		        }
		        
		        printer.printDiagrams(diagrams);
		       
			}
			
			printer.finish();							
					   
	   }
	   
	   public void printCppItem(PdfRecordPrinter printer, String header, String issueCode, int demographicNo, int appointmentNo, boolean includePrevious) throws DocumentException,IOException {
		   Collection<CaseManagementNote> notes = null;
		   if(!includePrevious) {
			    notes = filterNotesByAppointment(caseManagementNoteDao.findNotesByDemographicAndIssueCode(demographicNo, new String[] {issueCode}),appointmentNo);			   
		   } else {
			   notes = filterNotesByPreviousOrCurrentAppointment(caseManagementNoteDao.findNotesByDemographicAndIssueCode(demographicNo, new String[] {issueCode}),appointmentNo);								
		   }
		   if(notes.size()>0) {
			   printer.printCPPItem(header, notes);
			   printer.printBlankLine();
		   }
	   }

	   public Collection<CaseManagementNote> filterNotesByAppointment(Collection<CaseManagementNote> notes, int appointmentNo) {
		   List<CaseManagementNote> filteredNotes = new ArrayList<CaseManagementNote>();
		   for(CaseManagementNote note:notes) {
			   if(note.getAppointmentNo() == appointmentNo) {
				   filteredNotes.add(note);
			   }
		   }
		   return filteredNotes;
	   }
	   
	   public Collection<CaseManagementNote> filterNotesByPreviousOrCurrentAppointment(Collection<CaseManagementNote> notes, int appointmentNo) {
		   List<CaseManagementNote> filteredNotes = new ArrayList<CaseManagementNote>();
		   for(CaseManagementNote note:notes) {
			   if(note.getAppointmentNo() <= appointmentNo) {
				   filteredNotes.add(note);
			   }
		   }
		   return filteredNotes;
	   }
	   
	   public List<CaseManagementNote> filterOutCpp(Collection<CaseManagementNote> notes) {
		   List<CaseManagementNote> filteredNotes = new ArrayList<CaseManagementNote>();
		   for(CaseManagementNote note:notes) {
			   boolean skip=false;
			 for(CaseManagementIssue issue:note.getIssues()) {
				 for(int x=0;x<cppIssues.length;x++) {
					 if(issue.getIssue().getCode().equals(cppIssues[x])) {
						 skip=true;
					 }
				 }
			 }
			 if(!skip) {
				 filteredNotes.add(note);
			 }
		   }
		   return filteredNotes;
	   }
	   
	   public ActionForward prepareConReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		   String demoNo = request.getParameter("demographicNo");		
		   String appointmentNo = request.getParameter("appNo");
			
		   Integer demographicNo = new Integer(demoNo);		
		   Integer appNo = new Integer(0);			
		   if (appointmentNo != null && appointmentNo.trim().length() > 0)
				appNo = new Integer(appointmentNo);			
		   
		   		  
		   Provider provider = LoggedInInfo.loggedInInfo.get().loggedInProvider;				  
		   Demographic demographic = demographicDao.getClientByDemographicNo(demographicNo);
						
		   request.setAttribute("demographicNo", demoNo);		   
		   request.setAttribute("demographicName", demographic.getFormattedName());
		   
			//demographic_ext 
			String famName = new String();
			
			DemographicExt famExt = demographicDao.getDemographicExt(demographic.getDemographicNo(),"Family_Doctor");
			if(famExt != null) {
				famName = famExt.getValue();
			}
			request.setAttribute("famName", famName);
		
			ConsultationReport cp = new ConsultationReport();
			String refNo = null;
			String referraldoc = new String();
			
			if (!"saved".equalsIgnoreCase((String) request.getAttribute("savedflag"))
					&& "new".equalsIgnoreCase(request.getParameter("flag"))) {
				
				cp.setDemographicNo(demographicNo);
				cp.setProviderNo(provider.getProviderNo());				
				cp.setAppointmentNo(appNo);
				cp.setDate(new Date());
				cp.setReason(demographic.getFormattedName() + " ");
				cp.setUrgency("Non-urgent");
				cp.setStatus("Incomplete");
				request.setAttribute("newFlag", "true");
			} else {
				String cpId = request.getParameter("conReportNo");
				if ("saved".equalsIgnoreCase((String) request.getAttribute("savedflag"))) {
					cpId = (String) request.getAttribute("cpId");
				}
				ConsultationReportDao crDao = (ConsultationReportDao)SpringUtils.getBean("consultationReportDao");
				cp = crDao.find(new Integer(cpId));
				request.setAttribute("newFlag", "false");
				appNo = cp.getAppointmentNo();

				Billingreferral refdoc = brDao.getById(cp.getReferralId());
				if(refdoc != null) {
					referraldoc = refdoc.getLastName() + "," + refdoc.getFirstName();
					request.setAttribute("referral_doc_name", referraldoc);
					cp.setReferralNo(refdoc.getReferralNo());
					refNo = refdoc.getReferralNo();
				}
			}
			
			request.setAttribute("providerName",providerDao.getProvider(cp.getProviderNo()).getFormattedName());
			 
			DynaValidatorForm crForm = (DynaValidatorForm) form;	         
			crForm.set("cp", cp);
	         
			//loades latest eyeform
			
			if ("".equalsIgnoreCase(refNo)) {
				String referal = demographic.getFamilyDoctor();

				if (referal != null && !"".equals(referal.trim())) {
					Integer ref = getRefId(referal);
					cp.setReferralId(ref);
					refNo = getRefNo(referal);
					
					List refList = brDao.getBillingreferral(getRefNo(referal));
					if(refList.size()>0) {
						Billingreferral tmp = ((Billingreferral)refList.get(0));
						referraldoc = tmp.getLastName() + "," + tmp.getFirstName();
						request.setAttribute("referral_doc_name", referraldoc);
						cp.setReferralNo(tmp.getReferralNo());
					}					
				}
			}
			
			request.setAttribute("reason", cp.getReason());			
			request.setAttribute("currentHistory",StringEscapeUtils.escapeJavaScript(getFormattedCppItem("Current History:", "CurrentHistory", demographic.getDemographicNo(), appNo, false)));			   
			request.setAttribute("pastOcularHistory",StringEscapeUtils.escapeJavaScript(getFormattedCppItem("Past Ocular History:", "PastOcularHistory", demographic.getDemographicNo(), appNo, true))); 		   
			request.setAttribute("medHistory",StringEscapeUtils.escapeJavaScript(getFormattedCppItem("Medical History:", "MedHistory", demographic.getDemographicNo(), appNo, true))); 		   
			request.setAttribute("famHistory",StringEscapeUtils.escapeJavaScript(getFormattedCppItem("Family History:", "FamHistory", demographic.getDemographicNo(), appNo, true))); 		   
			request.setAttribute("ocularMedication",StringEscapeUtils.escapeJavaScript(getFormattedCppItem("Current Medications:", "OcularMedication", demographic.getDemographicNo(), appNo, true)));
			request.setAttribute("otherMeds",StringEscapeUtils.escapeJavaScript(getFormattedCppItem("Other Medications:", "OMeds", demographic.getDemographicNo(), appNo, true))); 		   
			request.setAttribute("diagnosticNotes",StringEscapeUtils.escapeJavaScript(getFormattedCppItem("Diagnostic Notes:", "DiagnosticNotes", demographic.getDemographicNo() , appNo, true)));
			
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			   List<OcularProc> ocularProcs = ocularProcDao.getHistory(demographic.getDemographicNo(), new Date(), "A");
			   StringBuilder ocularProc = new StringBuilder();
			   for(OcularProc op:ocularProcs) {
	               ocularProc.append(sf.format(op.getDate()) + " ");
	               ocularProc.append(op.getEye() + " ");
	               ocularProc.append(op.getProcedureName() + " at " + op.getLocation());
	               ocularProc.append(" by " + providerDao.getProvider(op.getDoctor()).getFormattedName());
	               if (op.getProcedureNote() != null && !"".equalsIgnoreCase(op.getProcedureNote().trim()))                      
	            	   ocularProc.append(". " + op.getProcedureNote() + "\n");			   		   
			   }
	           String strOcularProcs = ocularProc.toString();
	           if (strOcularProcs != null && !"".equalsIgnoreCase(strOcularProcs.trim()))
	        	   strOcularProcs = "Past Ocular Proc:\n" + strOcularProcs + "\n";
	           else
	        	   strOcularProcs = "";           
	           request.setAttribute("ocularProc", StringEscapeUtils.escapeJavaScript(strOcularProcs));
			   	         				          	         
	           //impression	           
	           String impression = getImpression(appNo);
	           request.setAttribute("impression", StringEscapeUtils.escapeJavaScript(impression));
	           
	           //followUp
	           FollowUpDao followUpDao = (FollowUpDao)SpringUtils.getBean("FollowUpDAO");
	           List<FollowUp> followUps = followUpDao.getByAppointmentNo(appNo);
	           StringBuilder followup = new StringBuilder();
	           for(FollowUp ef:followUps) {		   		
					if (ef.getTimespan() >0) {
						followup.append((ef.getType().equals("followup")?"Follow Up":"Consult") + " in " + ef.getTimespan() + " " + ef.getTimeframe() + "\n");
					}								
	           }
	           
	           //get the checkboxes
	           EyeForm eyeform = eyeFormDao.getByAppointmentNo(appNo);           
	           if (eyeform.getDischarge() != null && eyeform.getDischarge().equals("true"))
					followup.append("Patient is discharged from my active care.\n");
	           if (eyeform.getStat() != null && eyeform.getStat().equals("true"))
					followup.append("Follow up as needed with me STAT or PRN if symptoms are worse.\n");				
	           if (eyeform.getOpt() != null && eyeform.getOpt().equals("true"))
					followup.append("Routine eye care by an optometrist is recommended.\n");
	                                 
	           request.setAttribute("followup", StringEscapeUtils.escapeJavaScript(followup.toString()));
	           
	           
	           //test book
	           TestBookRecordDao testBookDao = (TestBookRecordDao)SpringUtils.getBean("TestBookDAO");
	           List<TestBookRecord> testBookRecords = testBookDao.getByAppointmentNo(appNo);
	           StringBuilder testbook = new StringBuilder();
	           for(TestBookRecord tt:testBookRecords) {
	        	   testbook.append(tt.getTestname());
	   				testbook.append(" ");
	   				testbook.append(tt.getEye());
	   				testbook.append("\n");
	           }
	           if (testbook.length() > 0)			
	        	   testbook.insert(0, "Diagnostic test booking:");
	           request.setAttribute("testbooking", StringEscapeUtils.escapeJavaScript(testbook.toString()));

					
	           //procedure book
	           ProcedureBookDao procBookDao = (ProcedureBookDao)SpringUtils.getBean("ProcedureBookDAO");
	           List<ProcedureBook> procBookRecords = procBookDao.getByAppointmentNo(appNo);          
	           StringBuilder probook = new StringBuilder();
	           for(ProcedureBook pp:procBookRecords) {
	        	   probook.append(pp.getProcedureName());   			
	        	   probook.append(" ");   			
	        	   probook.append(pp.getEye());   			
	        	   probook.append("\n");
	           }
	           if (probook.length() > 0)			
	        	   probook.insert(0, "Procedure booking:");           				
	           request.setAttribute("probooking", StringEscapeUtils.escapeJavaScript(probook.toString()));
				           				         			
	           return mapping.findForward("conReport");
	   }
	   
		public ActionForward saveConRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
			log.info("saveConRequest");

			DynaValidatorForm crForm = (DynaValidatorForm) form;
			ConsultationReport cp = (ConsultationReport) crForm.get("cp");
			String id = request.getParameter("cp.id");
			if(id != null && id.length()>0) {
				cp.setId(Integer.parseInt(id));
			}
			@SuppressWarnings("unchecked")
			List<Billingreferral> brs = brDao.getBillingreferral(cp.getReferralNo());
			cp.setReferralId(brs.get(0).getBillingreferralNo());
			if(cp.getDate()==null){
				cp.setDate(new Date());
			}
			ConsultationReportDao dao = (ConsultationReportDao)SpringUtils.getBean("consultationReportDao");
			if(cp.getId() != null && cp.getId()>0) {
				dao.merge(cp);
			} else {
				dao.persist(cp);
			}
			request.setAttribute("cpId", cp.getId().toString());
			request.setAttribute("savedflag", "saved");
			return prepareConReport(mapping, form, request, response);
		}
		
		public String getRefNo(String referal) {
			if (referal == null)
				return "";
			int start = referal.indexOf("<rdohip>");
			int end = referal.indexOf("</rdohip>");
			String ref = new String();

			if (start >= 0 && end >= 0) {
				String subreferal = referal.substring(start + 8, end);
				if (!"".equalsIgnoreCase(subreferal.trim())) {
					ref = subreferal;

				}
			}
			return ref;
		}
		public Integer getRefId(String referal) {
			int start = referal.indexOf("<rdohip>");
			int end = referal.indexOf("</rdohip>");
			String ref = new String();
			Integer refNo = new Integer(0);
			if (start >= 0 && end >= 0) {
				String subreferal = referal.substring(start + 8, end);
				if (!"".equalsIgnoreCase(subreferal.trim())) {
					ref = subreferal;
					List refList = brDao.getBillingreferral(ref.trim());
					if(refList.size()>0)
						refNo = ((Billingreferral)refList.get(0)).getBillingreferralNo();					
				}
			}
			return refNo;
		}
		
		public static String getField(HttpServletRequest request, String name) {
			String val = request.getParameter(name);
			if(val == null) {
				val = (String)request.getAttribute(name);
			}
			return val;
		}
		
		
		
		public ActionForward printConRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
			log.debug("printConreport");
			DynaValidatorForm crForm = (DynaValidatorForm) form;
			ConsultationReport cp = (ConsultationReport) crForm.get("cp");
			Demographic demographic = demographicDao.getClientByDemographicNo(cp.getDemographicNo());			
			request.setAttribute("demographic",demographic);
			
			String id = request.getParameter("cp.id");
			if(id != null && id.length()>0) {
				cp.setId(Integer.parseInt(id));
			}
			@SuppressWarnings("unchecked")
			List<Billingreferral> brs = brDao.getBillingreferral(cp.getReferralNo());
			cp.setReferralId(brs.get(0).getBillingreferralNo());
			if(cp.getDate()==null){
				cp.setDate(new Date());
			}
			ConsultationReportDao dao = (ConsultationReportDao)SpringUtils.getBean("consultationReportDao");
			if(cp.getId() != null && cp.getId()>0) {
				dao.merge(cp);
			} else {
				dao.persist(cp);
			}
			
			cp.setCc(divycc(cp.getCc()));
			cp.setClinicalInfo(divy(cp.getClinicalInfo()));
			cp.setConcurrentProblems(divy(cp.getConcurrentProblems()));
			cp.setCurrentMeds(divy(cp.getCurrentMeds()));
			cp.setExamination(dive(cp.getExamination()));
			cp.setImpression(divy(cp.getImpression()));
			cp.setAllergies(divy(cp.getAllergies()));
			cp.setPlan(divy(cp.getPlan()));
			
			SimpleDateFormat sf = new SimpleDateFormat("MM/dd/yyyy");
			request.setAttribute("date", sf.format(new Date()));
			
			String referralNo = cp.getReferralNo();
			Billingreferral br = brDao.getByReferralNo(referralNo);
			request.setAttribute("refer", br);
						
			request.setAttribute("cp", cp);		
			
			
			Provider internalProvider = providerDao.getProvider(demographic.getProviderNo());
			if(internalProvider != null) {
				request.setAttribute("internalDrName", internalProvider.getFirstName() + " " + internalProvider.getLastName());						
			} else {
//				request.setAttribute("internalDrName", );									
			}
			
			String specialty = new String();
			String mdStr = new String();
			if (internalProvider != null)				
				specialty = internalProvider.getSpecialty();
			if (specialty != null && !"".equalsIgnoreCase(specialty.trim())) {
				if ("MD".equalsIgnoreCase(specialty.substring(0, 2)))
					mdStr = "Dr.";
				specialty = ", " + specialty.trim();
			} else
				specialty = new String();
			request.setAttribute("specialty", specialty);
			
			Clinic clinic = clinicDao.getClinic();
			// prepare the satellite clinic address
			OscarProperties props = OscarProperties.getInstance();
			String sateliteFlag = "false";

			if (IsPropertiesOn.isMultisitesEnable()) {				
				Integer appt_no= (Integer) crForm.get("apptNo");
				String location = null;
				if (appt_no != null) {
					Appointment appt = appointmentDao.find(appt_no);
					if (appt != null)
						location = appt.getLocation();
				}

				List<Site> sites = siteDao.getActiveSitesByProviderNo(internalProvider.getProviderNo());

				ArrayList<SatelliteClinic> clinicArr = new ArrayList<SatelliteClinic>();
				Site defaultSite = null;
				for (int i = 0; i < sites.size(); i++) {
					Site s = sites.get(i);
					SatelliteClinic sc = new SatelliteClinic();
					sc.setClinicId(s.getSiteId());
					sc.setClinicName(s.getName());
					sc.setClinicAddress(s.getAddress());
					sc.setClinicCity(s.getCity());
					sc.setClinicProvince(s.getProvince());
					sc.setClinicPostal(s.getPostal());
					sc.setClinicPhone(s.getPhone());
					sc.setClinicFax(s.getFax());
					clinicArr.add(sc);
					if (s.getName().equals(location))
						defaultSite = s;
				}

				sateliteFlag = "true";
				request.setAttribute("clinicArr", clinicArr);
				if (defaultSite != null)
					request.setAttribute("sateliteId", defaultSite.getSiteId().toString());

			} else {
				if (props.getProperty("clinicSatelliteName") != null) {
					ArrayList<SatelliteClinic> clinicArr = getSateliteClinics(props);
					if (clinicArr.size() > 0) {
						sateliteFlag = "true";
						request.setAttribute("clinicArr", clinicArr);
						SatelliteClinic sc = clinicArr.get(0);
						clinic.setClinicName(sc.getClinicName());
						clinic.setClinicAddress(sc.getClinicAddress());
						clinic.setClinicCity(sc.getClinicCity());
						clinic.setClinicProvince(sc.getClinicProvince());
						clinic.setClinicPostal(sc.getClinicPostal());
						clinic.setClinicPhone(sc.getClinicPhone());
						clinic.setClinicFax(sc.getClinicFax());
					}
				}
			}

			request.setAttribute("sateliteFlag", sateliteFlag);
			request.setAttribute("clinic", clinic);
			
			return mapping.findForward("printReport");
		}
		
		public ArrayList<SatelliteClinic> getSateliteClinics(OscarProperties props) {
			ArrayList<SatelliteClinic> clinicArr = new ArrayList<SatelliteClinic>();
			String[] temp0 = props.getProperty("clinicSatelliteName", "").split(
					"\\|");
			String[] temp1 = props.getProperty("clinicSatelliteAddress", "").split(
					"\\|");
			String[] temp2 = props.getProperty("clinicSatelliteCity", "").split(
					"\\|");
			String[] temp3 = props.getProperty("clinicSatelliteProvince", "")
					.split("\\|");
			String[] temp4 = props.getProperty("clinicSatellitePostal", "").split(
					"\\|");
			String[] temp5 = props.getProperty("clinicSatellitePhone", "").split(
					"\\|");
			String[] temp6 = props.getProperty("clinicSatelliteFax", "").split(
					"\\|");
			for (int i = 0; i < temp0.length; i++) {
				SatelliteClinic sc = new SatelliteClinic();
				sc.setClinicId(new Integer(i));
				sc.setClinicName(temp0[i]);
				sc.setClinicAddress(temp1[i]);
				sc.setClinicCity(temp2[i]);
				sc.setClinicProvince(temp3[i]);
				sc.setClinicPostal(temp4[i]);
				sc.setClinicPhone(temp5[i]);
				sc.setClinicFax(temp6[i]);
				clinicArr.add(sc);
			}

			return clinicArr;
		}
		
		public ActionForward specialRepTickler(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
			log.debug("specialRepTickler");

			String demoNo = request.getParameter("demographicNo");
			String docFlag = request.getParameter("docFlag");
			String bsurl = request.getContextPath();
			if ("true".equalsIgnoreCase(docFlag))
				sendDocTickler("REP", demoNo, (String) request.getSession().getAttribute("user"), bsurl);

			response.getWriter().println("alert('tickler sent');");
			return null;
		}
		
		public void sendDocTickler(String flag, String demoNo, String providerNo, String bsurl) {
			Tickler tkl = new Tickler();
			Date now = new Date();

			tkl.setCreator(providerNo);
			tkl.setDemographic_no(demoNo);
			tkl.setPriority("Normal");
			tkl.setService_date(now);
			tkl.setStatus('A');
			tkl.setTask_assigned_to(providerNo);
			tkl.setUpdate_date(now);
			
			StringBuilder mes = new StringBuilder();
			mes.append("Remember to <a href=\"javascript:void(0)\" onclick=\"window.open(\'");
			String[] slist = bsurl.trim().split("/");
			mes.append("/");
			mes.append(slist[1]);
			if ("REQ".equalsIgnoreCase(flag)) {
				mes.append("/oscarEncounter/oscarConsultationRequest/DisplayDemographicConsultationRequests.jsp?de=");
				mes.append(demoNo);
				mes.append("\',\'conRequest\',\'height=700,width=700,scrollbars=yes,menubars=no,toolbars=no,resizable=yes\');\">");
				mes.append("complete the consultation request.");
			} else if ("REP".equalsIgnoreCase(flag)) {
				mes.append("/eyeform/ConsultationReportList.do?method=list&cr.demographicNo=");
				mes.append(demoNo);
				mes.append("\',\'conReport\',\'height=700,width=700,scrollbars=yes,menubars=no,toolbars=no,resizable=yes\');\">");
				mes.append("complete the consultation report.");
			}
			mes.append("</a>");
			tkl.setMessage(mes.toString());
			ticklerDao.saveTickler(tkl);
		}
		
		public void sendFrontTickler(String flag, String demoNo, String providerNo, String creator, String bsurl) {
			Tickler tkl = new Tickler();
			Date now = new Date();
			tkl.setCreator(creator);
			tkl.setDemographic_no(demoNo);
			tkl.setPriority("Normal");
			tkl.setService_date(now);
			tkl.setStatus('A');
			tkl.setTask_assigned_to(providerNo);
			tkl.setUpdate_date(now);
			StringBuilder mes = new StringBuilder();
			mes.append("Arrange <a href=\"javascript:void(0)\" onclick=\"window.open(\'");
			String[] slist = bsurl.trim().split("/");
			mes.append("/");
			mes.append(slist[1]);
			if ("REQ".equalsIgnoreCase(flag)) {
				mes.append("/oscarEncounter/oscarConsultationRequest/DisplayDemographicConsultationRequests.jsp?de=");
				mes.append(demoNo);
				mes.append("\',\'conRequest\',\'height=700,width=700,scrollbars=yes,menubars=no,toolbars=no,resizable=yes\');\">");
				mes.append("consultation request.");
			}
			mes.append("</a>");
			tkl.setMessage(mes.toString());
			ticklerDao.saveTickler(tkl);
		}
		
		public String divy(String str) {
			StringBuilder sb = new StringBuilder();
			sb.append(str);
			int j = 0;
			int i = 0;
			while (i < sb.length()) {
				if (sb.charAt(i) == '\n') {
					sb.insert(i, "<BR>");
					i = i + 4;
				}

				i++;
			}
			return sb.toString();

		}

		public String dive(String str) {
			// add "\n" to string
			StringBuilder stringBuffer = new StringBuilder();
			stringBuffer.append(str);
			int j = 0;
			int i = 0;
			while (i < stringBuffer.length()) {
				if (stringBuffer.charAt(i) == '\n') {
					j = 0;
				}
				i++;
				if (j > 75) {
					stringBuffer.insert(i, "\n");
					i++;
					j = 0;
				}

				j++;
			}
			return stringBuffer.toString();
		}

		public String divycc(String str) {
			StringBuilder stringBuffer = new StringBuilder();
			stringBuffer.append(str);
			int j = 0;
			int i = 0;
			while (i < stringBuffer.length()) {

				if (stringBuffer.charAt(i) == ';') {
					j++;
					if (j % 2 == 0) {
						stringBuffer.insert(i + 1, "<BR>");
						i = i + 4;
					}
				}
				i++;
			}
			return stringBuffer.toString();
		}

		public static List<LabelValueBean> getMeasurementSections() {
	           List<LabelValueBean> sections = new ArrayList<LabelValueBean>();
	           sections.add(new LabelValueBean("VISION ASSESSMENT","VISION ASSESSMENT"));
	           sections.add(new LabelValueBean("MANIFEST VISION","MANIFEST VISION"));
	           sections.add(new LabelValueBean("INTRAOCULAR PRESSURE","INTRAOCULAR PRESSURE"));
	           sections.add(new LabelValueBean("OTHER EXAM","OTHER EXAM"));
	           sections.add(new LabelValueBean("EOM/STEREO","EOM/STEREO"));
	           sections.add(new LabelValueBean("ANTERIOR SEGMENT","ANTERIOR SEGMENT"));
	           sections.add(new LabelValueBean("POSTERIOR SEGMENT","POSTERIOR SEGMENT"));
	           sections.add(new LabelValueBean("EXTERNAL/ORBIT","EXTERNAL/ORBIT"));
	           sections.add(new LabelValueBean("NASOLACRIMAL DUCT","NASOLACRIMAL DUCT"));
	           sections.add(new LabelValueBean("EYELID MEASUREMENT","EYELID MEASUREMENT"));
	           return sections;	           
		}
		
		public static List<LabelValueBean> getMeasurementHeaders() {
	           List<LabelValueBean> sections = new ArrayList<LabelValueBean>();
	           sections.add(new LabelValueBean("Auto-refraction","Auto-refraction"));
	           sections.add(new LabelValueBean("Keratometry","Keratometry"));
	           sections.add(new LabelValueBean("Distance vision (sc)","Distance vision (sc)"));
	           sections.add(new LabelValueBean("Distance vision (cc)","Distance vision (cc)"));
	           sections.add(new LabelValueBean("Distance vision (ph)","Distance vision (ph)"));
	           sections.add(new LabelValueBean("Near vision (sc)","Near vision (sc)"));
	           sections.add(new LabelValueBean("Near vision (cc)","Near vision (cc)"));
	           
	           sections.add(new LabelValueBean("Manifest distance","Manifest distance"));
	           sections.add(new LabelValueBean("Manifest near","Manifest near"));
	           sections.add(new LabelValueBean("Cycloplegic refraction","Cycloplegic refraction"));
	          // sections.add(new LabelValueBean("Best corrected distance vision","Best corrected distance vision"));
	           
	           sections.add(new LabelValueBean("NCT","NCT"));
	           sections.add(new LabelValueBean("Applanation","Applanation"));
	           sections.add(new LabelValueBean("Central corneal thickness","Central corneal thickness"));
	           
	           sections.add(new LabelValueBean("Colour vision","Colour vision"));
	           sections.add(new LabelValueBean("Pupil","Pupil"));
	           sections.add(new LabelValueBean("Amsler grid","Amsler grid"));
	           sections.add(new LabelValueBean("Potential acuity meter","Potential acuity meter"));
	           sections.add(new LabelValueBean("Confrontation fields","Confrontation fields"));
	           //sections.add(new LabelValueBean("Maddox rod","Maddox rod"));
	           //sections.add(new LabelValueBean("Bagolini test","Bagolini test"));
	           //sections.add(new LabelValueBean("Worth 4 dot (distance)","Worth 4 dot (distance)"));
	          // sections.add(new LabelValueBean("Worth 4 dot (near)","Worth 4 dot (near)"));
	           sections.add(new LabelValueBean("EOM","EOM"));
	           
	           sections.add(new LabelValueBean("Cornea","Cornea"));
	           sections.add(new LabelValueBean("Conjunctiva/Sclera","Conjunctiva/Sclera"));
	           sections.add(new LabelValueBean("Anterior chamber","Anterior chamber"));
	           sections.add(new LabelValueBean("Angle","Angle"));
	           sections.add(new LabelValueBean("Iris","Iris"));
	           sections.add(new LabelValueBean("Lens","Lens"));
	           
	           sections.add(new LabelValueBean("Optic disc","Optic disc"));
	           sections.add(new LabelValueBean("C/D ratio","C/D ratio"));
	           sections.add(new LabelValueBean("Macula","Macula"));
	           sections.add(new LabelValueBean("Retina","Retina"));
	           sections.add(new LabelValueBean("Vitreous","Vitreous"));
	           
	           sections.add(new LabelValueBean("Face","Face"));
	           sections.add(new LabelValueBean("Upper lid","Upper lid"));
	           sections.add(new LabelValueBean("Lower lid","Lower lid"));
	           sections.add(new LabelValueBean("Punctum","Punctum"));
	           sections.add(new LabelValueBean("Lacrimal lake","Lacrimal lake"));	           
	           //sections.add(new LabelValueBean("Schirmer test","Schirmer test"));
	           sections.add(new LabelValueBean("Retropulsion","Retropulsion"));
	           sections.add(new LabelValueBean("Hertel","Hertel"));
	           
	           sections.add(new LabelValueBean("Lacrimal irrigation","Lacrimal irrigation"));
	           sections.add(new LabelValueBean("Nasolacrimal duct","Nasolacrimal duct"));
	           sections.add(new LabelValueBean("Dye disappearance","Dye disappearance"));
	           
	           sections.add(new LabelValueBean("Margin reflex distance","Margin reflex distance"));
	           sections.add(new LabelValueBean("Inferior scleral show","Inferior scleral show"));
	           sections.add(new LabelValueBean("Levator function","Levator function"));
	           sections.add(new LabelValueBean("Lagophthalmos","Lagophthalmos"));
	           sections.add(new LabelValueBean("Blink reflex","Blink reflex"));
	           sections.add(new LabelValueBean("Cranial nerve VII function","Cranial nerve VII function"));
	           sections.add(new LabelValueBean("Bell's phenomenon","Bells phenomenon"));	           	           	           	           	           
	           
	           return sections;	           
		}
		
		public ActionForward getMeasurementText(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {			
			String[] values = request.getParameterValues(request.getParameter("name"));
			String appointmentNo = request.getParameter("appointmentNo");
			StringBuilder exam = new StringBuilder();
			Map<String,Boolean> headerMap = new HashMap<String,Boolean>();			
			for(int x=0;x<values.length;x++) {
				headerMap.put(values[x],true);
			}
			
			List<Measurements> measurements = measurementsDao.getMeasurementsByAppointment(Integer.parseInt(appointmentNo));
			MeasurementFormatter formatter = new MeasurementFormatter(measurements);
			exam.append(formatter.getVisionAssessment(headerMap));
			String tmp = null;
			tmp = formatter.getManifestVision(headerMap);
			if(exam.length()>0 && tmp.length()>0 ){
				exam.append("\n\n");				
			}
			exam.append(tmp);
			tmp = formatter.getIntraocularPressure(headerMap);
			if(exam.length()>0 && tmp.length()>0 ){
				exam.append("\n\n");				
			}
			exam.append(tmp);
			
			tmp = formatter.getOtherExam(headerMap);
			if(exam.length()>0 && tmp.length()>0 ){
				exam.append("\n\n");				
			}
			exam.append(tmp);
			
			tmp = formatter.getEOMStereo(headerMap);
			if(exam.length()>0 && tmp.length()>0 ){
				exam.append("\n\n");				
			}
			exam.append(tmp);
			
			tmp = formatter.getAnteriorSegment(headerMap);
			if(exam.length()>0 && tmp.length()>0 ){
				exam.append("\n\n");				
			}
			exam.append(tmp);
			
			tmp = formatter.getPosteriorSegment(headerMap);
			if(exam.length()>0 && tmp.length()>0 ){
				exam.append("\n\n");				
			}
			exam.append(tmp);
			
			tmp = formatter.getExternalOrbit(headerMap);
			if(exam.length()>0 && tmp.length()>0 ){
				exam.append("\n\n");				
			}
			
			tmp = formatter.getNasalacrimalDuct(headerMap);
			if(exam.length()>0 && tmp.length()>0 ){
				exam.append("\n\n");				
			}
			exam.append(tmp);
			
			tmp = formatter.getEyelidMeasurement(headerMap);
			if(exam.length()>0 && tmp.length()>0 ){
				exam.append("\n\n");				
			}
			exam.append(tmp);
			
			response.getWriter().println(exam.toString());
			
			
			return null;
		}
		
		public static List<Provider> getActiveProviders() {
			ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
			return providerDao.getActiveProviders();
		}
		
		public ActionForward specialReqTickler(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
			log.debug("specialReqTickler");

			String demoNo = request.getParameter("demographicNo");
			String docFlag = request.getParameter("docFlag");
			String frontFlag = request.getParameter("frontFlag");
			String providerNo = request.getParameter("providerNo");
			String bsurl = request.getContextPath();
			String user = LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo();
			if ("true".equalsIgnoreCase(docFlag))
				sendDocTickler("REQ", demoNo,user, bsurl);
			if ("true".equalsIgnoreCase(frontFlag))
				sendFrontTickler("REQ", demoNo, providerNo,user, bsurl);
			response.getWriter().println("alert('tickler sent');");
			return null;
		}
		
		
			
}
