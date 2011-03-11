package org.oscarehr.eyeform.web;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.dao.ClientDao;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.casemgmt.dao.AllergyDAO;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.casemgmt.model.Allergy;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.dao.BillingreferralDao;
import org.oscarehr.common.dao.ConsultationRequestExtDao;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.dao.ProfessionalSpecialistDao;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.Billingreferral;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.Provider;
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
import org.oscarehr.eyeform.model.SpecsHistory;
import org.oscarehr.eyeform.model.TestBookRecord;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarEncounter.oscarMeasurements.dao.MeasurementsDao;
import oscar.oscarEncounter.oscarMeasurements.model.Measurements;
import oscar.util.UtilDateUtilities;

import com.lowagie.text.DocumentException;

public class EyeformAction extends DispatchAction {

	static Logger logger = MiscUtils.getLogger();
	static String[] cppIssues = {"CurrentHistory","PastOcularHistory","MedHistory","OMeds","OcularMedication","DiagnosticNotes"};
	
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
		   StringBuffer ocularProc = new StringBuffer();
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

               StringBuffer data = new StringBuffer("");
               data.append("OD ");
               StringBuffer dataTemp = new StringBuffer("");
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
               dataTemp = new StringBuffer("");
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
				List<CaseManagementNote> notes = caseManagementNoteDao.getMostRecentNotesByAppointmentNo(appointmentNo);
				notes = filterOutCpp(notes);				
				printer.printNotes(notes);
				
				//plan - followups/consults, procedures booked, tests booked, checkboxes
				List<FollowUp> followUps = followUpDao.getByAppointmentNo(appointmentNo);
				List<ProcedureBook> procedureBooks = procedureBookDao.getByAppointmentNo(appointmentNo);
				List<TestBookRecord> testBooks = testBookDao.getByAppointmentNo(appointmentNo);
				EyeForm eyeform = eyeFormDao.getByAppointmentNo(appointmentNo);
		        printer.printEyeformPlan(followUps, procedureBooks, testBooks,eyeform);
				
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
			
		   Integer appNo = new Integer(0);
			if (appointmentNo != null && appointmentNo.trim().length() > 0)
				appNo = new Integer(appointmentNo);
			request.setAttribute("demographicNo", demoNo);
			Integer demographicNo = new Integer(demoNo);
			Date now = new Date();
			String providerNo = (String) request.getSession().getAttribute("user");
			String provider = providerDao.getProviderName(providerNo);
			Demographic demo = demographicDao.getClientByDemographicNo(demographicNo);
			String demoName = demo.getFormattedName();
			//demographic_ext Family_Doctor
			String famName = new String();
/*
			for(int x=0;x<demo.getExtras().length;x++) {
				if(demo.getExtras()[x].getKey().equals("Family_Doctor")) {
					famName = demo.getExtras()[x].getValue();
				}
			}
*/			
			ConsultationReport cp = new ConsultationReport();
			String refNo = null;
			String referraldoc = new String();
			
			if (!"saved".equalsIgnoreCase((String) request.getAttribute("savedflag"))
					&& "new".equalsIgnoreCase(request.getParameter("flag"))) {
				
				cp.setDemographicNo(demographicNo);
				cp.setProviderNo(provider);				
				cp.setAppointmentNo(appNo);
				cp.setDate(now);
				String reason = demoName + " ";
				cp.setReason(reason);
				cp.setUrgency("Non-urgent");
				cp.setStatus("Incomplete");
				request.setAttribute("newFlag", "true");
			} else {
				String cpId = request.getParameter("conReportNo");
				if ("saved".equalsIgnoreCase((String) request.getAttribute("savedflag")))
					cpId = (String) request.getAttribute("cpId");
				ConsultationReportDao crDao = (ConsultationReportDao)SpringUtils.getBean("consultationReportDao");
				cp = crDao.find(new Integer(cpId));
				request.setAttribute("newFlag", "false");
				appNo = cp.getAppointmentNo();

				BillingreferralDao brDao = (BillingreferralDao)SpringUtils.getBean("BillingreferralDao");
				Billingreferral refdoc = brDao.getByReferralNo(String.valueOf(cp.getReferralId()));
				referraldoc = refdoc.getLastName() + "," + refdoc.getFirstName();
				refNo = refdoc.getReferralNo();
			}

			//loades latest eyeform
			
			if ("".equalsIgnoreCase(refNo)) {
				String referal = demo.getFamilyDoctor();

				if (referal != null && !"".equals(referal.trim())) {
					Integer ref = getRefId(referal);
					cp.setReferralId(ref);
					refNo = getRefNo(referal);
					
					BillingreferralDao brDao = (BillingreferralDao)SpringUtils.getBean("BillingreferralDao");
					List refList = brDao.getBillingreferral(getRefNo(referal));
					if(refList.size()>0) {
						Billingreferral tmp = ((Billingreferral)refList.get(0));
						referraldoc = tmp.getLastName() + "," + tmp.getFirstName();
					}					
				}
			}
			
			
			return mapping.findForward("conReport");
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
					BillingreferralDao brDao = (BillingreferralDao)SpringUtils.getBean("BillingreferralDao");
					List refList = brDao.getBillingreferral(ref.trim());
					if(refList.size()>0)
						refNo = ((Billingreferral)refList.get(0)).getBillingreferralNo();					
				}
			}
			return refNo;
		}
}
