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
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.casemgmt.dao.IssueDAO;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.IsPropertiesOn;
import org.oscarehr.common.dao.AllergyDao;
import org.oscarehr.common.dao.BillingreferralDao;
import org.oscarehr.common.dao.CaseManagementIssueNotesDao;
import org.oscarehr.common.dao.ClinicDAO;
import org.oscarehr.common.dao.ConsultationRequestExtDao;
import org.oscarehr.common.dao.DemographicContactDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DemographicExtDao;
import org.oscarehr.common.dao.DocumentResultsDao;
import org.oscarehr.common.dao.EFormGroupDao;
import org.oscarehr.common.dao.EFormValueDao;
import org.oscarehr.common.dao.MeasurementDao;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.dao.ProfessionalSpecialistDao;
import org.oscarehr.common.dao.SiteDao;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.Billingreferral;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DemographicContact;
import org.oscarehr.common.model.DemographicExt;
import org.oscarehr.common.model.Document;
import org.oscarehr.common.model.EFormGroup;
import org.oscarehr.common.model.EFormValue;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Site;
import org.oscarehr.common.service.PdfRecordPrinter;
import org.oscarehr.common.web.ContactAction;
import org.oscarehr.eyeform.MeasurementFormatter;
import org.oscarehr.eyeform.dao.EyeFormDao;
import org.oscarehr.eyeform.dao.EyeformConsultationReportDao;
import org.oscarehr.eyeform.dao.EyeformFollowUpDao;
import org.oscarehr.eyeform.dao.EyeformOcularProcedureDao;
import org.oscarehr.eyeform.dao.EyeformProcedureBookDao;
import org.oscarehr.eyeform.dao.EyeformSpecsHistoryDao;
import org.oscarehr.eyeform.dao.EyeformTestBookDao;
import org.oscarehr.eyeform.model.EyeForm;
import org.oscarehr.eyeform.model.EyeformConsultationReport;
import org.oscarehr.eyeform.model.EyeformFollowUp;
import org.oscarehr.eyeform.model.EyeformOcularProcedure;
import org.oscarehr.eyeform.model.EyeformProcedureBook;
import org.oscarehr.eyeform.model.EyeformSpecsHistory;
import org.oscarehr.eyeform.model.EyeformTestBook;
import org.oscarehr.eyeform.model.SatelliteClinic;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.beans.BeanUtils;

import oscar.OscarProperties;
import oscar.SxmlMisc;
import oscar.util.UtilDateUtilities;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfCopyFields;

public class EyeformAction extends DispatchAction {

	static Logger logger = MiscUtils.getLogger();
	static String[] cppIssues = {"CurrentHistory","PastOcularHistory","MedHistory","OMeds","OcularMedication","DiagnosticNotes","FamHistory"};

	CaseManagementManager cmm = null;
	OscarAppointmentDao appointmentDao = (OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");
	DemographicDao demographicDao= (DemographicDao)SpringUtils.getBean("demographicDao");
	ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
	CaseManagementNoteDAO caseManagementNoteDao = (CaseManagementNoteDAO)SpringUtils.getBean("CaseManagementNoteDAO");
	EyeformOcularProcedureDao ocularProcDao = SpringUtils.getBean(EyeformOcularProcedureDao.class);
	private EyeformSpecsHistoryDao specsHistoryDao = (EyeformSpecsHistoryDao)SpringUtils.getBean(EyeformSpecsHistoryDao.class);
	
	AllergyDao allergyDao = (AllergyDao)SpringUtils.getBean("allergyDao");
	EyeformFollowUpDao followUpDao = SpringUtils.getBean(EyeformFollowUpDao.class);
	protected EyeformProcedureBookDao procedureBookDao = SpringUtils.getBean(EyeformProcedureBookDao.class);
	
	EyeformTestBookDao testBookDao = SpringUtils.getBean(EyeformTestBookDao.class);
	EyeFormDao eyeFormDao = SpringUtils.getBean(EyeFormDao.class);
	
	MeasurementDao measurementDao = SpringUtils.getBean(MeasurementDao.class);
	ProfessionalSpecialistDao professionalSpecialistDao = (ProfessionalSpecialistDao) SpringUtils.getBean("professionalSpecialistDao");
	BillingreferralDao billingreferralDao = (BillingreferralDao) SpringUtils.getBean("billingreferralDao");
	ClinicDAO clinicDao = (ClinicDAO)SpringUtils.getBean("clinicDAO");
	SiteDao siteDao = (SiteDao)SpringUtils.getBean("siteDao");
	TicklerDAO ticklerDao = (TicklerDAO)SpringUtils.getBean("ticklerDAOT");
	CaseManagementIssueNotesDao caseManagementIssueNotesDao=(CaseManagementIssueNotesDao)SpringUtils.getBean("caseManagementIssueNotesDao");
	DemographicExtDao demographicExtDao = SpringUtils.getBean(DemographicExtDao.class);

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
			   } catch(NumberFormatException e){
				   //ignore
			   }
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
		   String cpp = request.getParameter("cpp");
		   boolean cppFromMeasurements=false;
		   if(cpp != null && cpp.equals("measurements")) {
			   cppFromMeasurements=true;
		   }

		   Provider provider = LoggedInInfo.loggedInInfo.get().loggedInProvider;
		   ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
		   ConsultationRequestExtDao consultationRequestExtDao=(ConsultationRequestExtDao)SpringUtils.getBean("consultationRequestExtDao");
		 
		  
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

		   if(requestId>0) {
			   String tmp = consultationRequestExtDao.getConsultationRequestExtsByKey(requestId, "appNo");
        	   appNo = Integer.parseInt(tmp);
		   }
		   request.setAttribute("providerList", providerDao.getActiveProviders());
		   request.setAttribute("re_demoNo", demo);
/*
		   if(cppFromMeasurements) {
			   request.setAttribute("currentHistory",StringEscapeUtils.escapeJavaScript(getFormattedCppItemFromMeasurements("Current History:", "cpp_currentHis", Integer.parseInt(demo), appNo, false)));
			   request.setAttribute("pastOcularHistory",StringEscapeUtils.escapeJavaScript(getFormattedCppItemFromMeasurements("Past Ocular History:", "cpp_pastOcularHis", Integer.parseInt(demo), appNo, true)));
			   request.setAttribute("diagnosticNotes",StringEscapeUtils.escapeJavaScript(getFormattedCppItemFromMeasurements("Diagnostic Notes:", "cpp_diagnostics", Integer.parseInt(demo), appNo, true)));
			   request.setAttribute("medicalHistory",StringEscapeUtils.escapeJavaScript(getFormattedCppItemFromMeasurements("Medical History:", "cpp_medicalHis", Integer.parseInt(demo), appNo, true)));
			   request.setAttribute("familyHistory",StringEscapeUtils.escapeJavaScript(getFormattedCppItemFromMeasurements("Family History:", "cpp_familyHis", Integer.parseInt(demo), appNo, true)));
			   request.setAttribute("ocularMedication",StringEscapeUtils.escapeJavaScript(getFormattedCppItemFromMeasurements("Ocular Medications:", "cpp_ocularMeds", Integer.parseInt(demo), appNo, true)));

		   } else {*/
			   request.setAttribute("currentHistory",StringEscapeUtils.escapeJavaScript(getFormattedCppItem("Current History:", "CurrentHistory", Integer.parseInt(demo), appNo, false)));
			   request.setAttribute("pastOcularHistory",StringEscapeUtils.escapeJavaScript(getFormattedCppItem("Past Ocular History:", "PastOcularHistory", Integer.parseInt(demo), appNo, true)));
			   request.setAttribute("diagnosticNotes",StringEscapeUtils.escapeJavaScript(getFormattedCppItem("Diagnostic Notes:", "DiagnosticNotes", Integer.parseInt(demo), appNo, true)));
			   request.setAttribute("medicalHistory",StringEscapeUtils.escapeJavaScript(getFormattedCppItem("Medical History:", "MedHistory", Integer.parseInt(demo), appNo, true)));
			   request.setAttribute("familyHistory",StringEscapeUtils.escapeJavaScript(getFormattedCppItem("Family History:", "FamHistory", Integer.parseInt(demo), appNo, true)));
			   request.setAttribute("ocularMedication",StringEscapeUtils.escapeJavaScript(getFormattedCppItem("Ocular Medications:", "OcularMedication", Integer.parseInt(demo), appNo, true)));

			   IssueDAO issueDao = (IssueDAO)SpringUtils.getBean("IssueDAO");

			   String customCppIssues[] = OscarProperties.getInstance().getProperty("encounter.custom_cpp_issues", "").split(",");
			   for(String customCppIssue:customCppIssues) {
				   Issue i = issueDao.findIssueByCode(customCppIssue);
				   if(i != null) {
					   request.setAttribute(customCppIssue,StringEscapeUtils.escapeJavaScript(getFormattedCppItem(i.getDescription()+":", customCppIssue, Integer.parseInt(demo), appNo, true)));
				   }
			   }
		   //}

		   request.setAttribute("otherMeds",StringEscapeUtils.escapeJavaScript(getFormattedCppItem("Other Meds:", "OMeds", Integer.parseInt(demo), appNo, true)));


		   SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		   List<EyeformOcularProcedure> ocularProcs = ocularProcDao.getHistory(Integer.parseInt(demo), new Date(), "A");
		   StringBuilder ocularProc = new StringBuilder();
		   for(EyeformOcularProcedure op:ocularProcs) {
               ocularProc.append(sf.format(op.getDate()) + " ");
               ocularProc.append(op.getEye() + " ");
               ocularProc.append(op.getProcedureName() + " at " + op.getLocation());
               ocularProc.append(" by " + providerDao.getProvider(op.getDoctor()).getFormattedName());
               if (op.getProcedureNote() != null && !"".equalsIgnoreCase(op.getProcedureNote().trim()))
            	   ocularProc.append(". " + op.getProcedureNote() + "\n");
		   }
           String strOcularProcs = ocularProc.toString();
           if (strOcularProcs != null && !"".equalsIgnoreCase(strOcularProcs.trim()))
        	   strOcularProcs = "Past Ocular Procedures:\n" + strOcularProcs;
           else
        	   strOcularProcs = "";
           request.setAttribute("ocularProc", StringEscapeUtils.escapeJavaScript(strOcularProcs));


           List<EyeformSpecsHistory> specs = specsHistoryDao.getAllPreviousAndCurrent(Integer.parseInt(demo), appNo);
           StringBuilder specsStr = new StringBuilder();
           for(EyeformSpecsHistory spec:specs) {
        	   String specDate = sf.format(spec.getDate());
        	   specsStr.append(specDate + " ");

               StringBuilder data = new StringBuilder("");
               data.append(" OD ");
               StringBuilder dataTemp = new StringBuilder("");
               dataTemp.append(spec.getOdSph() == null ? "" : spec.getOdSph());
               dataTemp.append(spec.getOdCyl() == null ? "" : spec.getOdCyl());
               if (spec.getOdAxis() != null
                               && spec.getOdAxis().trim().length() != 0)
                       dataTemp.append("x" + spec.getOdAxis());
               if (spec.getOdAdd() != null && spec.getOdAdd().trim().length() != 0)
                       dataTemp.append(" add " + spec.getOdAdd());
               if(spec.getOdPrism() != null && spec.getOdPrism().length()>0) {
            	   dataTemp.append(" prism " + spec.getOdPrism());
               }
               specsStr.append(dataTemp.toString());
               specsStr.append("\n           ");
               data.append(dataTemp);

               String secHead = "\n      OS ";
               data.append(secHead);
               dataTemp = new StringBuilder("");
               dataTemp.append(spec.getOsSph() == null ? "" : spec.getOsSph());
               dataTemp.append(spec.getOsCyl() == null ? "" : spec.getOsCyl());
               if (spec.getOsAxis() != null && spec.getOsAxis().trim().length() != 0)
            	   dataTemp.append("x" + spec.getOsAxis());
               if (spec.getOsAdd() != null && spec.getOsAdd().trim().length() != 0)
            	   dataTemp.append(" add " + spec.getOsAdd());

               if(spec.getOsPrism() != null && spec.getOsPrism().length()>0) {
            	   dataTemp.append(" prism " + spec.getOsPrism());
               }

               specsStr.append(dataTemp.toString() + "\n");
               data.append(dataTemp);
           }
           String specsStr1 = "";
           if (specsStr != null && specs.size()>0)
               specsStr1  = "Spectacles:\n" + specsStr.toString();
           else
    	   		specsStr1 = "";

           request.setAttribute("specs", StringEscapeUtils.escapeJavaScript(specsStr1));

           //impression
           //logger.info("appNo="+appNo);
           if(requestId > 0) {
        	   //get the saved app no.
        	   String tmp = consultationRequestExtDao.getConsultationRequestExtsByKey(requestId, "appNo");
        	   appNo = Integer.parseInt(tmp);
        	   request.setAttribute("appNo",appNo);
           }
           String impression = getImpression(appNo);
           request.setAttribute("impression", StringEscapeUtils.escapeJavaScript("Impression:" + "\n" + impression));




           //followUp
           List<EyeformFollowUp> followUps = followUpDao.getByAppointmentNo(appNo);
           StringBuilder followup = new StringBuilder();
           for(EyeformFollowUp ef:followUps) {
				if (ef.getTimespan() >0) {
					followup.append((ef.getType().equals("followup")?"Follow Up":"Consult") + " in " + ef.getTimespan() + " " + ef.getTimeframe());
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
           List<EyeformTestBook> testBookRecords = testBookDao.getByAppointmentNo(appNo);
           StringBuilder testbook = new StringBuilder();
           for(EyeformTestBook tt:testBookRecords) {
        	   testbook.append(tt.getTestname());
   				testbook.append(" ");
   				testbook.append(tt.getEye());
   				testbook.append("\n");
           }
           if (testbook.length() > 0)
        	   testbook.insert(0, "Diagnostic test booking:");
           request.setAttribute("testbooking", StringEscapeUtils.escapeJavaScript(testbook.toString()));


           //procedure book
           List<EyeformProcedureBook> procBookRecords = procedureBookDao.getByAppointmentNo(appNo);
           StringBuilder probook = new StringBuilder();
           for(EyeformProcedureBook pp:procBookRecords) {
        	   probook.append(pp.getProcedureName());
        	   probook.append(" ");
        	   probook.append(pp.getEye());
        	   probook.append("\n");
           }
           if (probook.length() > 0)
        	   probook.insert(0, "Procedure booking:");
           request.setAttribute("probooking", StringEscapeUtils.escapeJavaScript(probook.toString()));

           //measurements
           if(requestId > 0) {
        	   String tmp = consultationRequestExtDao.getConsultationRequestExtsByKey(requestId, "specialProblem");
        	   request.setAttribute("specialProblem", StringEscapeUtils.escapeJavaScript(tmp));
           } else {
        	   request.setAttribute("specialProblem", "");
           }

		   return mapping.findForward("conspecial");
	   }

	   public String getFormattedCppItemFromMeasurements(String header, String measurementType, int demographicNo, int appointmentNo, boolean includePrevious) {
		  Measurement measurement = measurementDao.findLatestByAppointmentNoAndType(demographicNo,measurementType);
		  if(measurement == null) {
			  return new String();
		  }
		  if(!includePrevious) {
			  if(measurement.getAppointmentNo() != appointmentNo) {
				  return new String();
			  }
		  }

		  StringBuilder sb = new StringBuilder();
		  sb.append("\n");
		  sb.append(measurement.getDataField());

		  return header + sb.toString();
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
				   sb.append("\n");
				   sb.append(note.getNote());
			   }
			   return header + sb.toString();
		   }
		   return new String();
	   }
/*
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
*/
	   private String getImpression(int appointmentNo) {
		   List<CaseManagementNote> notes = caseManagementNoteDao.getMostRecentNotesByAppointmentNo(appointmentNo);
		   notes = filterOutCpp(notes);
		   if(notes.size()>0) {
			   StringBuilder sb = new StringBuilder();
			   for(CaseManagementNote note:notes) {
				   sb.append(note.getNote()).append("\n");
			   }
			   //return "Impression:" + "\n" + sb.toString();
			   return sb.toString();
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

			String cpp = request.getParameter("cpp");
			boolean cppFromMeasurements=false;
			if(cpp != null && cpp.equals("measurements")) {
				cppFromMeasurements=true;
			}
			String[] customCppIssues =OscarProperties.getInstance().getProperty("encounter.custom_cpp_issues","").split(",");

			PdfCopyFields finalDoc = new PdfCopyFields(os);
			finalDoc.getWriter().setStrictImageSequence(true);
			PdfRecordPrinter printer = new PdfRecordPrinter(os);
                        printer.start();
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

				//need to get notes first to set the signing provider
				List<CaseManagementNote> notes = caseManagementNoteDao.getMostRecentNotesByAppointmentNo(appointmentNo);
				notes = filterOutCpp(notes);
				if(notes.size()>0) {
					String tmp = notes.get(0).getSigning_provider_no();
					if(tmp != null && tmp.length()>0) {
						Provider signingProvider = providerDao.getProvider(tmp);
						if(signingProvider != null) {
							printer.setSigningProvider(signingProvider.getFormattedName());
						}
					}
				}



				printer.printDocHeaderFooter();

				//get cpp items by appointmentNo (current history,past ocular hx,
				//medical hx, ocular meds, other meds, diagnostic notes
/*
				if(cppFromMeasurements) {
					printCppItemFromMeasurements(printer,"Current History","cpp_currentHis",demographic.getDemographicNo(), appointmentNo, false);
					printCppItemFromMeasurements(printer,"Past Ocular History","cpp_pastOcularHis",demographic.getDemographicNo(), appointmentNo, true);
					printCppItemFromMeasurements(printer,"Medical History","cpp_medicalHis",demographic.getDemographicNo(), appointmentNo, true);
					printCppItemFromMeasurements(printer,"Family History","cpp_familyHis",demographic.getDemographicNo(), appointmentNo, true);
					printCppItemFromMeasurements(printer,"Diagnostic Notes","cpp_diagnostics",demographic.getDemographicNo(), appointmentNo, false);
					printCppItemFromMeasurements(printer,"Ocular Medication","cpp_ocularMeds",demographic.getDemographicNo(), appointmentNo, true);

				} else {
*/
				IssueDAO issueDao = (IssueDAO)SpringUtils.getBean("IssueDAO");

					printCppItem(printer,"Current History","CurrentHistory",demographic.getDemographicNo(), appointmentNo, false);
					printCppItem(printer,"Past Ocular History","PastOcularHistory",demographic.getDemographicNo(), appointmentNo, true);
					printCppItem(printer,"Medical History","MedHistory",demographic.getDemographicNo(), appointmentNo, true);
					printCppItem(printer,"Family History","FamHistory",demographic.getDemographicNo(), appointmentNo, true);
					printCppItem(printer,"Diagnostic Notes","DiagnosticNotes",demographic.getDemographicNo(), appointmentNo, false);
					printCppItem(printer,"Ocular Medications","OcularMedication",demographic.getDemographicNo(), appointmentNo, true);

					for(String customCppIssue:customCppIssues) {
						Issue issue = issueDao.findIssueByCode(customCppIssue);
						if(issue != null) {
							printCppItem(printer,issue.getDescription(),customCppIssue,demographic.getDemographicNo(), appointmentNo, true);
						}
					}
//				}
				printCppItem(printer,"Other Medications","OMeds",demographic.getDemographicNo(), appointmentNo, true);

				printer.setNewPage(true);

				//ocular procs
				List<EyeformOcularProcedure> ocularProcs = ocularProcDao.getAllPreviousAndCurrent(demographic.getDemographicNo(),appointmentNo);
				if(ocularProcs.size()>0) {
					printer.printOcularProcedures(ocularProcs);
				}

				//specs history
				List<EyeformSpecsHistory> specsHistory = specsHistoryDao.getAllPreviousAndCurrent(demographic.getDemographicNo(),appointmentNo);
				if(specsHistory.size()>0) {
					printer.printSpecsHistory(specsHistory);
				}

				//allergies
				List<Allergy> allergies = allergyDao.findAllergies(demographic.getDemographicNo());
				if(allergies.size()>0) {
					printer.printAllergies(allergies);
				}

				//rx
				printer.printRx(String.valueOf(demographic.getDemographicNo()));

				//measurements
				List<Measurement> measurements = measurementDao.findByAppointmentNo(appointmentNo);
				if(measurements.size()>0) {
/*
					if(cppFromMeasurements) {
						if(getNumMeasurementsWithoutCpp(measurements)>0) {
							MeasurementFormatter formatter = new MeasurementFormatter(measurements);
							printer.printEyeformMeasurements(formatter);
						}
					} else {
*/
						MeasurementFormatter formatter = new MeasurementFormatter(measurements);
						printer.printEyeformMeasurements(formatter);
//					}
				}

				//impression
				//let's filter out custom cpp notes, as they will already have been
				//printed out in CPP section
				List<CaseManagementNote> filteredNotes = new ArrayList<CaseManagementNote>();
				for(CaseManagementNote note:notes) {
					boolean okToAdd=true;
					for(String i:customCppIssues) {
						if(containsIssue(note.getId().intValue(),i)) {
							okToAdd=false;
							break;
						}
					}
					if(okToAdd)
						filteredNotes.add(note);
				}
				if(filteredNotes.size()>0) {
					printer.printNotes(filteredNotes);
				}

				

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
		        if(diagrams.size()>0) {
		        	printer.printDiagrams(diagrams);
		        }


			} //end of loop

			printer.finish();

	   }

	   private boolean containsIssue(Integer noteId, String issueCode) {
			List<CaseManagementIssue> caseManagementIssues=caseManagementIssueNotesDao.getNoteIssues(noteId);
			for (CaseManagementIssue caseManagementIssue : caseManagementIssues) {
				if (caseManagementIssue.getIssue().getCode().equals(issueCode)) {
						return(true);
				}
			}
			return false;
		}


	   public int getNumMeasurementsWithoutCpp(List<Measurement> measurements) {
		   List<Measurement> filtered = new ArrayList<Measurement>();
		   for(Measurement m:measurements) {
			   if(m.getType().startsWith("cpp_")) {
				   continue;
			   }
			   filtered.add(m);
		   }
		   return filtered.size();
	   }

	   public void printCppItem(PdfRecordPrinter printer, String header, String issueCode, int demographicNo, int appointmentNo, boolean includePrevious) throws DocumentException {
		   Collection<CaseManagementNote> notes = null;
		   if(!includePrevious) {
			    notes = filterNotesByAppointment(caseManagementNoteDao.findNotesByDemographicAndIssueCodeInEyeform(demographicNo, new String[] {issueCode}),appointmentNo);
		   } else {
			   notes = filterNotesByPreviousOrCurrentAppointment(caseManagementNoteDao.findNotesByDemographicAndIssueCodeInEyeform(demographicNo, new String[] {issueCode}),appointmentNo);
		   }
		   if(notes.size()>0) {
			   printer.printCPPItem(header, notes);
			   printer.printBlankLine();
		   }
	   }

	   public void printCppItemFromMeasurements(PdfRecordPrinter printer, String header, String measurementType, int demographicNo, int appointmentNo, boolean includePrevious) throws DocumentException {
			  Measurement measurement = measurementDao.findLatestByDemographicNoAndType(demographicNo,measurementType);
			  if(measurement == null) {
				  return;
			  }
			  if(!includePrevious) {
				  if(measurement.getAppointmentNo() != appointmentNo) {
					  return;
				  }
			  }

			  printer.printCPPItem(header, measurement);
			  printer.printBlankLine();

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

	   public List<Measurement> filterMeasurementsByAppointment(List<Measurement> measurements, int appointmentNo) {
		   List<Measurement> filteredMeasurements = new ArrayList<Measurement>();
		   for(Measurement measurement:measurements) {
			   if(measurement.getAppointmentNo() == appointmentNo) {
				   filteredMeasurements.add(measurement);
			   }
		   }
		   return filteredMeasurements;
	   }

	   public List<Measurement> filterMeasurementsByPreviousOrCurrentAppointment(List<Measurement> measurements, int appointmentNo) {
		   List<Measurement> filteredMeasurements = new ArrayList<Measurement>();
		   for(Measurement measurement:measurements) {
			   if(measurement.getAppointmentNo() <= appointmentNo) {
				   filteredMeasurements.add(measurement);
			   }
		   }
		   return filteredMeasurements;
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
		   String cpp = request.getParameter("cpp");
		   boolean cppFromMeasurements=false;
		   if(cpp != null && cpp.equals("measurements")) {
			   cppFromMeasurements=true;
		   }

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

			DemographicExt famExt = demographicExtDao.getDemographicExt(demographic.getDemographicNo(),"Family_Doctor");
			if(famExt != null) {
				famName = famExt.getValue();
			}
			request.setAttribute("famName", famName);

			EyeformConsultationReport cp = new EyeformConsultationReport();
			String refNo = null;
			String referraldoc = new String();

			//referralNo and referral_doc_name
			String famXml = demographic.getFamilyDoctor();
			if(famXml != null && famXml.length()>0) {
				refNo = SxmlMisc.getXmlContent(famXml,"rdohip");
				referraldoc = SxmlMisc.getXmlContent(famXml,"rd");
				request.setAttribute("referral_doc_name", referraldoc);
				cp.setReferralNo(refNo);
			}

			DemographicContactDao demographicContactDao = (DemographicContactDao)SpringUtils.getBean("demographicContactDao");
			List<DemographicContact> contacts = demographicContactDao.findByDemographicNoAndCategory(demographicNo, "professional");
			contacts = ContactAction.fillContactNames(contacts);
			request.setAttribute("contacts", contacts);


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
				EyeformConsultationReportDao crDao = SpringUtils.getBean(EyeformConsultationReportDao.class);

				cp = crDao.find(new Integer(cpId));
				request.setAttribute("newFlag", "false");
				appNo = cp.getAppointmentNo();

				ProfessionalSpecialist specialist = professionalSpecialistDao.find(cp.getReferralId());
				if(specialist != null) {
					referraldoc = specialist.getLastName() + "," + specialist.getFirstName();
					request.setAttribute("referral_doc_name", referraldoc);
					cp.setReferralNo(specialist.getReferralNo());
					refNo = specialist.getReferralNo();
				}
			}

			request.setAttribute("providerName",providerDao.getProvider(cp.getProviderNo()).getFormattedName());

			DynaValidatorForm crForm = (DynaValidatorForm) form;
			crForm.set("cp", cp);

			//loades latest eyeform

			if ("".equalsIgnoreCase(refNo)) {
				String referral = demographic.getFamilyDoctor();

				if (referral != null && !"".equals(referral.trim())) {
					Integer ref = getRefId(referral);
					cp.setReferralId(ref);
					refNo = getRefNo(referral);

					List<ProfessionalSpecialist> refList = professionalSpecialistDao.findByReferralNo(refNo);
					if(refList!=null && refList.size()>0) {
						ProfessionalSpecialist refSpecialist = refList.get(0);
						referraldoc = refSpecialist.getLastName() + "," + refSpecialist.getFirstName();
						request.setAttribute("referral_doc_name", referraldoc);
						cp.setReferralNo(refSpecialist.getReferralNo());
					}
				}
			}

			request.setAttribute("reason", cp.getReason());

/*			if(cppFromMeasurements) {
				request.setAttribute("currentHistory",StringEscapeUtils.escapeJavaScript(getFormattedCppItemFromMeasurements("Current History:", "cpp_currentHis", demographic.getDemographicNo(), appNo, false)));
				request.setAttribute("pastOcularHistory",StringEscapeUtils.escapeJavaScript(getFormattedCppItemFromMeasurements("Past Ocular History:", "cpp_pastOcularHis", demographic.getDemographicNo(), appNo, true)));
			   request.setAttribute("diagnosticNotes",StringEscapeUtils.escapeJavaScript(getFormattedCppItemFromMeasurements("Diagnostic Notes:", "cpp_diagnostics", demographic.getDemographicNo(), appNo, true)));
			   request.setAttribute("medHistory",StringEscapeUtils.escapeJavaScript(getFormattedCppItemFromMeasurements("Medical History:", "cpp_medicalHis", demographic.getDemographicNo(), appNo, true)));
			   request.setAttribute("famHistory",StringEscapeUtils.escapeJavaScript(getFormattedCppItemFromMeasurements("Family History:", "cpp_familyHis", demographic.getDemographicNo(), appNo, true)));
			   request.setAttribute("ocularMedication",StringEscapeUtils.escapeJavaScript(getFormattedCppItemFromMeasurements("Current Medications:", "cpp_ocularMeds", demographic.getDemographicNo(), appNo, true)));

			} else {*/
				request.setAttribute("currentHistory",StringEscapeUtils.escapeJavaScript(getFormattedCppItem("Current History:", "CurrentHistory", demographic.getDemographicNo(), appNo, false)));
				request.setAttribute("pastOcularHistory",StringEscapeUtils.escapeJavaScript(getFormattedCppItem("Past Ocular History:", "PastOcularHistory", demographic.getDemographicNo(), appNo, true)));
				request.setAttribute("medHistory",StringEscapeUtils.escapeJavaScript(getFormattedCppItem("Medical History:", "MedHistory", demographic.getDemographicNo(), appNo, true)));
				request.setAttribute("famHistory",StringEscapeUtils.escapeJavaScript(getFormattedCppItem("Family History:", "FamHistory", demographic.getDemographicNo(), appNo, true)));
				request.setAttribute("diagnosticNotes",StringEscapeUtils.escapeJavaScript(getFormattedCppItem("Diagnostic Notes:", "DiagnosticNotes", demographic.getDemographicNo() , appNo, true)));
				request.setAttribute("ocularMedication",StringEscapeUtils.escapeJavaScript(getFormattedCppItem("Current Medications:", "OcularMedication", demographic.getDemographicNo(), appNo, true)));

			//}

			request.setAttribute("otherMeds",StringEscapeUtils.escapeJavaScript(getFormattedCppItem("Other Medications:", "OMeds", demographic.getDemographicNo(), appNo, true)));

			IssueDAO issueDao = (IssueDAO)SpringUtils.getBean("IssueDAO");
			String customCppIssues[] = OscarProperties.getInstance().getProperty("encounter.custom_cpp_issues", "").split(",");
			for(String customCppIssue:customCppIssues) {
				Issue i = issueDao.findIssueByCode(customCppIssue);
				if(i != null) {
					request.setAttribute(customCppIssue,StringEscapeUtils.escapeJavaScript(getFormattedCppItem(i.getDescription()+":", customCppIssue, demographic.getDemographicNo(), appNo, true)));
				}
			}


			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			   List<EyeformOcularProcedure> ocularProcs = ocularProcDao.getHistory(demographic.getDemographicNo(), new Date(), "A");
			   StringBuilder ocularProc = new StringBuilder();
			   for(EyeformOcularProcedure op:ocularProcs) {
	               ocularProc.append(sf.format(op.getDate()) + " ");
	               ocularProc.append(op.getEye() + " ");
	               ocularProc.append(op.getProcedureName() + " at " + op.getLocation());
	               ocularProc.append(" by " + providerDao.getProvider(op.getDoctor()).getFormattedName());
	               if (op.getProcedureNote() != null && !"".equalsIgnoreCase(op.getProcedureNote().trim()))
	            	   ocularProc.append(". " + op.getProcedureNote() + "\n");
			   }
	           String strOcularProcs = ocularProc.toString();
	           if (strOcularProcs != null && !"".equalsIgnoreCase(strOcularProcs.trim()))
	        	   strOcularProcs = "Past Ocular Procedures:\n" + strOcularProcs + "\n";
	           else
	        	   strOcularProcs = "";
	           request.setAttribute("ocularProc", StringEscapeUtils.escapeJavaScript(strOcularProcs));

	           List<EyeformSpecsHistory> specs = specsHistoryDao.getAllPreviousAndCurrent(demographic.getDemographicNo(),appNo);
	           StringBuilder specsStr = new StringBuilder();
	           for(EyeformSpecsHistory spec:specs) {
	        	   String specDate = sf.format(spec.getDate());
	        	   specsStr.append(specDate + " ");

	               StringBuilder data = new StringBuilder("");
	               data.append(" OD ");
	               StringBuilder dataTemp = new StringBuilder("");
	               dataTemp.append(spec.getOdSph() == null ? "" : spec.getOdSph());
	               dataTemp.append(spec.getOdCyl() == null ? "" : spec.getOdCyl());
	               if (spec.getOdAxis() != null && spec.getOdAxis().trim().length() != 0)
	                       dataTemp.append("x" + spec.getOdAxis());
	               if (spec.getOdAdd() != null && spec.getOdAdd().trim().length() != 0)
	                       dataTemp.append(" add " + spec.getOdAdd());
	               if(spec.getOdPrism() != null && spec.getOdPrism().length()>0) {
	            	   dataTemp.append(" prism " + spec.getOdPrism());
	               }
	               specsStr.append(dataTemp.toString());
	               specsStr.append("\n           ");
	               data.append(dataTemp);

	               String secHead = "\n      OS ";
	               data.append(secHead);
	               dataTemp = new StringBuilder("");
	               dataTemp.append(spec.getOsSph() == null ? "" : spec.getOsSph());
	               dataTemp.append(spec.getOsCyl() == null ? "" : spec.getOsCyl());
	               if (spec.getOsAxis() != null && spec.getOsAxis().trim().length() != 0)
	            	   dataTemp.append("x" + spec.getOsAxis());
	               if (spec.getOsAdd() != null && spec.getOsAdd().trim().length() != 0)
	            	   dataTemp.append(" add " + spec.getOsAdd());

	               if(spec.getOsPrism() != null && spec.getOsPrism().length()>0) {
	            	   dataTemp.append(" prism " + spec.getOsPrism());
	               }

	               specsStr.append(dataTemp.toString() + "\n");
	               data.append(dataTemp);
	           }
	           String specsStr1 = "";
	           if (specsStr != null && specs.size()>0)
	               specsStr1  = "Spectacles:\n" + specsStr.toString();
	           else
	    	   		specsStr1 = "";

	           request.setAttribute("specs", StringEscapeUtils.escapeJavaScript(specsStr1));

	           //impression
	           String impression = getImpression(appNo);
	           request.setAttribute("impression", StringEscapeUtils.escapeJavaScript(impression));

	           //followUp
	           EyeformFollowUpDao followUpDao = SpringUtils.getBean(EyeformFollowUpDao.class);
	           List<EyeformFollowUp> followUps = followUpDao.getByAppointmentNo(appNo);
	           StringBuilder followup = new StringBuilder();
	           for(EyeformFollowUp ef:followUps) {
					if (ef.getTimespan() >0) {
						followup.append((ef.getType().equals("followup")?"Follow Up":"Consult") + " in " + ef.getTimespan() + " " + ef.getTimeframe());
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
	           List<EyeformTestBook> testBookRecords = testBookDao.getByAppointmentNo(appNo);
	           StringBuilder testbook = new StringBuilder();
	           for(EyeformTestBook tt:testBookRecords) {
	        	   testbook.append(tt.getTestname());
	   				testbook.append(" ");
	   				testbook.append(tt.getEye());
	   				testbook.append("\n");
	           }
	           if (testbook.length() > 0)
	        	   testbook.insert(0, "Diagnostic test booking:");
	           request.setAttribute("testbooking", StringEscapeUtils.escapeJavaScript(testbook.toString()));


	           //procedure book
	           List<EyeformProcedureBook> procBookRecords = procedureBookDao.getByAppointmentNo(appNo);
	           StringBuilder probook = new StringBuilder();
	           for(EyeformProcedureBook pp:procBookRecords) {
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
			EyeformConsultationReportDao dao = SpringUtils.getBean(EyeformConsultationReportDao.class);


                        DynaValidatorForm crForm = (DynaValidatorForm) form;
                        EyeformConsultationReport cp = (EyeformConsultationReport) crForm.get("cp");
                        EyeformConsultationReport consultReport = null;
                        String id = request.getParameter("cp.id");
                        if(id != null && id.length()>0) {
                                consultReport = dao.find(Integer.parseInt(id));
                        } else {
                                consultReport = new EyeformConsultationReport();
                        }
                        BeanUtils.copyProperties(cp, consultReport, new String[]{"id","demographic","provider"});

			ProfessionalSpecialist professionalSpecialist = professionalSpecialistDao.getByReferralNo(cp.getReferralNo());
			if (professionalSpecialist != null)
				cp.setReferralId(professionalSpecialist.getId());

			cp.setDate(new Date());

			if(cp.getId() != null && cp.getId()>0) {
				dao.merge(cp);
			} else {
				dao.persist(cp);
			}
			request.setAttribute("cpId", cp.getId().toString());
			request.setAttribute("savedflag", "saved");
			//return prepareConReport(mapping, form, request, response);
			request.setAttribute("parentAjaxId", "conReport");
			return mapping.findForward("success");
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
					ProfessionalSpecialist professionalSpecialist = professionalSpecialistDao.getByReferralNo(ref.trim());
					if(professionalSpecialist != null)
						refNo = professionalSpecialist.getId();
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
			EyeformConsultationReportDao dao = SpringUtils.getBean(EyeformConsultationReportDao.class);

                        DynaValidatorForm crForm = (DynaValidatorForm) form;
                        EyeformConsultationReport cp = (EyeformConsultationReport) crForm.get("cp");
                        Demographic demographic = demographicDao.getClientByDemographicNo(cp.getDemographicNo());
                        request.setAttribute("demographic",demographic);
                        Appointment appointment = this.appointmentDao.find(cp.getAppointmentNo());
                        EyeformConsultationReport consultReport = null;
                        String id = request.getParameter("cp.id");
                        if(id != null && id.length()>0) {
                                consultReport = dao.find(Integer.parseInt(id));
                        } else {
                                consultReport = new EyeformConsultationReport();
                        }
                        BeanUtils.copyProperties(cp, consultReport, new String[]{"id","demographic","provider"});

			ProfessionalSpecialist professionalSpecialist = professionalSpecialistDao.getByReferralNo(cp.getReferralNo());

			if (professionalSpecialist != null)
				cp.setReferralId(professionalSpecialist.getId());
			if(cp.getDate()==null){
				cp.setDate(new Date());
			}
			if(cp.getId() != null && cp.getId()>0) {
				dao.merge(cp);
			} else {
				dao.persist(cp);
			}

			cp.setCc(divycc(cp.getCc()));
			cp.setClinicalInfo(divy(wrap(cp.getClinicalInfo(),80)));
			cp.setClinicalInfo(cp.getClinicalInfo().replaceAll("\\s", "&nbsp;"));
			cp.setConcurrentProblems(divy(wrap(cp.getConcurrentProblems(),80)));
			cp.setCurrentMeds(wrap(cp.getCurrentMeds(),80));
			cp.setExamination(divy(wrap(cp.getExamination(),80)));
			cp.setExamination(cp.getExamination().replaceAll("\n", ""));
			cp.setImpression(divy(wrap(cp.getImpression(),80)));
			cp.setAllergies(divy(wrap(cp.getAllergies(),80)));
			cp.setPlan(divy(wrap(cp.getPlan(),80)));

			SimpleDateFormat sf = new SimpleDateFormat("MM/dd/yyyy");
			request.setAttribute("date", sf.format(new Date()));

			Billingreferral ref = billingreferralDao.getByReferralNo(String.valueOf(cp.getReferralId()));
			request.setAttribute("refer", ref);
		//	request.setAttribute("refer", professionalSpecialist);

			request.setAttribute("cp", cp);

			Provider internalProvider = null;
			if(demographic.getProviderNo()!=null && !demographic.getProviderNo().equalsIgnoreCase("null") && demographic.getProviderNo().length()>0) {

				internalProvider = providerDao.getProvider(demographic.getProviderNo());
				if(internalProvider != null) {
					request.setAttribute("internalDrName", internalProvider.getFirstName() + " " + internalProvider.getLastName());
				} else {
//				request.setAttribute("internalDrName", );
				}
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
				Integer appt_no= cp.getAppointmentNo();	
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
			request.setAttribute("appointDate", (appointment!=null?appointment.getAppointmentDate(): "") );

			if(appointment!=null) {
				Provider apptProvider = providerDao.getProvider(appointment.getProviderNo());
				request.setAttribute("appointmentDoctor", apptProvider.getFormattedName());
			}

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

		public String wrap(String in,int len) {
			if(in==null)
				in="";
			//in=in.trim();
			if(in.length()<len) {
				if(in.length()>1 && !in.startsWith("  ")) {
					in=in.trim();
				}
				return in;
			}
			if(in.substring(0, len).contains("\n")) {
				String x = in.substring(0, in.indexOf("\n"));
				if(x.length()>1 && !x.startsWith("  ")) {
					x=x.trim();
				}
				return x + "\n" + wrap(in.substring(in.indexOf("\n") + 1), len);
			}
			int place=Math.max(Math.max(in.lastIndexOf(" ",len),in.lastIndexOf("\t",len)),in.lastIndexOf("-",len));
			return in.substring(0,place).trim()+"\n"+wrap(in.substring(place),len);
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

			List<Measurement> measurements = measurementDao.findByAppointmentNo(Integer.parseInt(appointmentNo));
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
			exam.append(tmp);

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
