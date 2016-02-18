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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.soap.SOAPElement;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;
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
import org.oscarehr.common.dao.FaxClientLogDao;
import org.oscarehr.common.dao.MeasurementDao;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.dao.OscarCommLocationsDao;
import org.oscarehr.common.dao.ProfessionalSpecialistDao;
import org.oscarehr.common.dao.SiteDao;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DemographicContact;
import org.oscarehr.common.model.DemographicExt;
import org.oscarehr.common.model.Document;
import org.oscarehr.common.model.EFormGroup;
import org.oscarehr.common.model.EFormValue;
import org.oscarehr.common.model.FaxClientLog;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.common.model.OscarCommLocations;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Site;
import org.oscarehr.common.model.Tickler;
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
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.managers.TicklerManager;
import org.oscarehr.util.EncounterUtil;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.beans.BeanUtils;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfCopyFields;

import oscar.OscarProperties;
import oscar.SxmlMisc;
import oscar.oscarFax.client.OSCARFAXClient;
import oscar.oscarFax.client.OSCARFAXSOAPMessage;
import oscar.util.UtilDateUtilities;

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
	CaseManagementIssueNotesDao caseManagementIssueNotesDao=(CaseManagementIssueNotesDao)SpringUtils.getBean("caseManagementIssueNotesDao");
	DemographicExtDao demographicExtDao = SpringUtils.getBean(DemographicExtDao.class);
	TicklerManager ticklerManager = SpringUtils.getBean(TicklerManager.class);
	
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
		
		ProviderDao providerDao = (ProviderDao) SpringUtils
				.getBean("providerDao");
		ConsultationRequestExtDao consultationRequestExtDao = (ConsultationRequestExtDao) SpringUtils
				.getBean("consultationRequestExtDao");
		
		int appNo;
		int requestId;
		if (strAppNo == null || strAppNo.length() == 0
				|| strAppNo.equals("null")) {
			appNo = 0;
		} else {
			appNo = Integer.parseInt(strAppNo);
		}
		if (reqId == null || reqId.length() == 0 || reqId.equals("null")) {
			requestId = 0;
		} else {
			requestId = Integer.parseInt(reqId);
		}

		if (requestId > 0) {
			String tmp = consultationRequestExtDao
					.getConsultationRequestExtsByKey(requestId, "appNo");
			appNo = Integer.parseInt(tmp);
		}
		request.setAttribute("providerList", providerDao.getActiveProviders());
		request.setAttribute("re_demoNo", demo);

		String whichEyeForm = OscarProperties.getInstance().getProperty(
				"cme_js", "");
		Boolean includeCPPForPrevAppts = null;
		String eyeform_onlyPrintCurrentVisit = OscarProperties.getInstance()
				.getProperty("eyeform_onlyPrintCurrentVisit");
		if (eyeform_onlyPrintCurrentVisit != null
				&& eyeform_onlyPrintCurrentVisit.trim().length() > 0) {
			eyeform_onlyPrintCurrentVisit = eyeform_onlyPrintCurrentVisit
					.trim();
			boolean onlyPrintCurrentApptRecordsFromEncounter = Boolean
					.parseBoolean(eyeform_onlyPrintCurrentVisit);

			// if only print current appt.. then includeCPPForPrevAppts = false
			if (onlyPrintCurrentApptRecordsFromEncounter)
				includeCPPForPrevAppts = false;
			else
				includeCPPForPrevAppts = true;
		}
		if (includeCPPForPrevAppts != null) {
			if (whichEyeForm != null
					&& whichEyeForm.equalsIgnoreCase("eyeform_DrJinapriya")) {
				request.setAttribute("currentHistory", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem("Subjective:",
								"CurrentHistory", Integer.parseInt(demo),
								appNo, includeCPPForPrevAppts)));
				request.setAttribute("pastOcularHistory", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Past Ocular History:", "PastOcularHistory",
								Integer.parseInt(demo), appNo,
								includeCPPForPrevAppts)));
				request.setAttribute("medHistory", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Medical History:", "MedHistory",
								Integer.parseInt(demo), appNo,
								includeCPPForPrevAppts)));
				request.setAttribute("famHistory", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Ocular Diagnoses:", "FamHistory",
								Integer.parseInt(demo), appNo,
								includeCPPForPrevAppts)));
				request.setAttribute("diagnosticNotes", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem("Objective:",
								"DiagnosticNotes", Integer.parseInt(demo),
								appNo, includeCPPForPrevAppts)));
				request.setAttribute("ocularMedication", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Drops Administered This Visit:",
								"OcularMedication", Integer.parseInt(demo),
								appNo, includeCPPForPrevAppts)));
				request.setAttribute("otherMeds", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem("Systemic Meds:",
								"OMeds", Integer.parseInt(demo), appNo,
								includeCPPForPrevAppts)));
			} else {
				request.setAttribute("currentHistory", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Current History:", "CurrentHistory",
								Integer.parseInt(demo), appNo,
								includeCPPForPrevAppts)));
				request.setAttribute("pastOcularHistory", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Past Ocular History:", "PastOcularHistory",
								Integer.parseInt(demo), appNo,
								includeCPPForPrevAppts)));
				request.setAttribute("diagnosticNotes", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Diagnostic Notes:", "DiagnosticNotes",
								Integer.parseInt(demo), appNo,
								includeCPPForPrevAppts)));
				request.setAttribute("medHistory", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Medical History:", "MedHistory",
								Integer.parseInt(demo), appNo,
								includeCPPForPrevAppts)));
				request.setAttribute("famHistory", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Family History:", "FamHistory",
								Integer.parseInt(demo), appNo,
								includeCPPForPrevAppts)));
				request.setAttribute("ocularMedication", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem("Ocular Meds:",
								"OcularMedication", Integer.parseInt(demo),
								appNo, includeCPPForPrevAppts)));
				request.setAttribute("otherMeds", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem("Other Meds:",
								"OMeds", Integer.parseInt(demo), appNo,
								includeCPPForPrevAppts)));
			}
		} else {
			if (whichEyeForm != null
					&& whichEyeForm.equalsIgnoreCase("eyeform_DrJinapriya")) {
				request.setAttribute("currentHistory", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem("Subjective:",
								"CurrentHistory", Integer.parseInt(demo),
								appNo, false)));
				request.setAttribute("pastOcularHistory", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Past Ocular History:", "PastOcularHistory",
								Integer.parseInt(demo), appNo, true)));
				request.setAttribute("medHistory", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Medical History:", "MedHistory",
								Integer.parseInt(demo), appNo, true)));
				request.setAttribute("famHistory", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Ocular Diagnoses:", "FamHistory",
								Integer.parseInt(demo), appNo, true)));
				request.setAttribute("diagnosticNotes", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem("Objective:",
								"DiagnosticNotes", Integer.parseInt(demo),
								appNo, true)));
				request.setAttribute("ocularMedication", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Drops Administered This Visit:",
								"OcularMedication", Integer.parseInt(demo),
								appNo, true)));
				request.setAttribute("otherMeds", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem("Systemic Meds:",
								"OMeds", Integer.parseInt(demo), appNo, true)));
			} else {
				request.setAttribute("currentHistory", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Current History:", "CurrentHistory",
								Integer.parseInt(demo), appNo, false)));
				request.setAttribute("pastOcularHistory", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Past Ocular History:", "PastOcularHistory",
								Integer.parseInt(demo), appNo, true)));
				request.setAttribute("diagnosticNotes", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Diagnostic Notes:", "DiagnosticNotes",
								Integer.parseInt(demo), appNo, true)));
				request.setAttribute("medHistory", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Medical History:", "MedHistory",
								Integer.parseInt(demo), appNo, true)));
				request.setAttribute("famHistory", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Family History:", "FamHistory",
								Integer.parseInt(demo), appNo, true)));
				request.setAttribute("ocularMedication", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem("Ocular Meds:",
								"OcularMedication", Integer.parseInt(demo),
								appNo, true)));
				request.setAttribute("otherMeds", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem("Other Meds:",
								"OMeds", Integer.parseInt(demo), appNo, true)));
			}
		}
		IssueDAO issueDao = (IssueDAO) SpringUtils.getBean("IssueDAO");

		String customCppIssues[] = OscarProperties.getInstance()
				.getProperty("encounter.custom_cpp_issues", "").split(",");
		for (String customCppIssue : customCppIssues) {
			if (includeCPPForPrevAppts != null) {
				if (customCppIssue != null
						&& customCppIssue
								.equalsIgnoreCase("GlaucomaRiskFactors")) { // For
																			// Dr.Jinapriya,
																			// replace
																			// PatientLog
																			// with
																			// GlaucomaRiskFactors,
																			// but
																			// still
																			// use
																			// PatientLog
																			// issue.
					Issue i = issueDao.findIssueByCode("PatientLog");
					if (i != null) {
						request.setAttribute(customCppIssue, StringEscapeUtils
								.escapeJavaScript(getFormattedCppItem(
										"Glaucoma Risk Factors" + ":",
										"PatientLog", Integer.parseInt(demo),
										appNo, includeCPPForPrevAppts)));
					}
				} else { // ="Misc"
					Issue i = issueDao.findIssueByCode(customCppIssue);
					if (i != null) {
						request.setAttribute(customCppIssue, StringEscapeUtils
								.escapeJavaScript(getFormattedCppItem(
										i.getDescription() + ":",
										customCppIssue, Integer.parseInt(demo),
										appNo, includeCPPForPrevAppts)));
					}
				}
			} else {
				if (customCppIssue != null
						&& customCppIssue
								.equalsIgnoreCase("GlaucomaRiskFactors")) { // For
																			// Dr.Jinapriya,
																			// replace
																			// PatientLog
																			// with
																			// GlaucomaRiskFactors,
																			// but
																			// still
																			// use
																			// PatientLog
																			// issue.
					Issue i = issueDao.findIssueByCode("PatientLog");
					if (i != null) {
						request.setAttribute(customCppIssue, StringEscapeUtils
								.escapeJavaScript(getFormattedCppItem(
										"Glaucoma Risk Factors" + ":",
										"PatientLog", Integer.parseInt(demo),
										appNo, true)));
					}
				} else { // ="Misc"
					Issue i = issueDao.findIssueByCode(customCppIssue);
					if (i != null) {
						request.setAttribute(customCppIssue, StringEscapeUtils
								.escapeJavaScript(getFormattedCppItem(
										i.getDescription() + ":",
										customCppIssue, Integer.parseInt(demo),
										appNo, true)));
					}
				}
			}
		}

		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		List<EyeformOcularProcedure> ocularProcs;
		if (includeCPPForPrevAppts != null && !includeCPPForPrevAppts) {
			ocularProcs = ocularProcDao.getByAppointmentNo(appNo);
		} else if (includeCPPForPrevAppts != null && includeCPPForPrevAppts) {
			ocularProcs = ocularProcDao.getHistory(Integer.parseInt(demo),
					new Date(), "A");
		} else {
			ocularProcs = ocularProcDao.getHistory(Integer.parseInt(demo),
					new Date(), "A");
		}

		StringBuilder ocularProc = new StringBuilder();
		for (EyeformOcularProcedure op : ocularProcs) {
			ocularProc.append(sf.format(op.getDate()) + " ");
			ocularProc.append(op.getEye() + " ");
			ocularProc
					.append(op.getProcedureName() + " at " + op.getLocation());
			ocularProc.append(" by "
					+ providerDao.getProvider(op.getDoctor())
							.getFormattedName());
			if (op.getProcedureNote() != null
					&& !"".equalsIgnoreCase(op.getProcedureNote().trim()))
				ocularProc.append(". " + op.getProcedureNote() + "\n");
		}
		String strOcularProcs = ocularProc.toString();
		if (strOcularProcs != null
				&& !"".equalsIgnoreCase(strOcularProcs.trim()))
			strOcularProcs = "Past Ocular Procedures:\n" + strOcularProcs;
		else
			strOcularProcs = "";
		request.setAttribute("ocularProc",
				StringEscapeUtils.escapeJavaScript(strOcularProcs));

		List<EyeformSpecsHistory> specs;
		if (includeCPPForPrevAppts == null || includeCPPForPrevAppts == true)
			specs = specsHistoryDao.getAllPreviousAndCurrent(
					Integer.parseInt(demo), appNo);
		else
			specs = specsHistoryDao
					.getAllCurrent(Integer.parseInt(demo), appNo);

		StringBuilder specsStr = new StringBuilder();
		for (EyeformSpecsHistory spec : specs) {
			String specDate = spec.getDate() == null ? "" : sf.format(spec
					.getDate());
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
		if (requestId > 0) {
			// get the saved app no.
			String tmp = consultationRequestExtDao
					.getConsultationRequestExtsByKey(requestId, "appNo");
			appNo = Integer.parseInt(tmp);
			request.setAttribute("appNo", appNo);
		}
		String impression = " ";
		if (appNo != 0) {
			impression = getImpression(appNo);
		}
		request.setAttribute(
				"impression",
				StringEscapeUtils.escapeJavaScript("Impression:" + "\n"
						+ impression));

           //followUp
		
           List<EyeformFollowUp> followUps = followUpDao.getByAppointmentNo(appNo);
           StringBuilder followup = new StringBuilder();
           for(EyeformFollowUp ef:followUps) {
			// if (ef.getTimespan() >0) {
			if (!ef.getTimespan().equals("0") || !ef.getTimespan().equals("")) {
				followup.append((ef.getType().equals("followup") ? "Follow Up"
						: "Consult")
						+ " in "
						+ ef.getTimespan()
						+ " "
						+ ef.getTimeframe());
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
		
		List<EyeformTestBook> testBookRecords = testBookDao
				.getByAppointmentNo(appNo);
		StringBuilder testbook = new StringBuilder();
		for (EyeformTestBook tt : testBookRecords) {
			testbook.append(tt.getTestname());
			testbook.append(" ");
			testbook.append(tt.getEye());
			testbook.append("\n");
		}
		if (testbook.length() > 0)
			testbook.insert(0, "Diagnostic test booking:");
		request.setAttribute("testbooking",
				StringEscapeUtils.escapeJavaScript(testbook.toString()));

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
		
		if (requestId > 0) {
			String tmp = consultationRequestExtDao
					.getConsultationRequestExtsByKey(requestId,
							"specialProblem");
			if ((whichEyeForm != null)
					&& ((whichEyeForm.equals("eyeform3"))
							|| ("eyeform3.1".equals(whichEyeForm)) || ("eyeform3.2"
								.equals(whichEyeForm)))) {
				tmp = tmp.replaceAll("\n", "<br>");
				tmp = tmp.replaceAll("\r", "");
				tmp = tmp.replaceAll("\t", "");
				request.setAttribute("specialProblem", tmp);
				HttpSession session = request.getSession();
				session.setAttribute("specialProblem", tmp);
			} else {
				request.setAttribute("specialProblem",
						StringEscapeUtils.escapeJavaScript(tmp));
			}
           } else {
        	   request.setAttribute("specialProblem", "");
           }

		   return mapping.findForward("conspecial");
	   }

	   public String getFormattedCppItemFromMeasurements(String header, String measurementType, int demographicNo, int appointmentNo, boolean includePrevious) {
			
		Measurement measurement = measurementDao.getLatestMeasurementByDemographicNoAndType(demographicNo,measurementType);
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
				// sb.append("\n");
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
		notes = filterOutBillingDocEformForm(notes);
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

	public ActionForward print(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		response.setContentType("application/pdf"); // octet-stream
		response.setHeader(
				"Content-Disposition",
				"attachment; filename=\"Encounter-"
						+ UtilDateUtilities.getToday("yyyy-MM-dd.hh.mm.ss")
						+ ".pdf\"");
		String ids[] = request.getParameter("apptNos").split(",");
		if (ids.length == 1 && ids[0].equals("0"))
			return null;
		doPrint(request, response.getOutputStream());
		return null;
	}


	   public void doPrint(HttpServletRequest request, OutputStream os) throws IOException, DocumentException {
			String ids[] = request.getParameter("apptNos").split(",");
		//String providerNo = LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo();
			String providerNo = LoggedInInfo.getLoggedInInfoFromSession(request).getLoggedInProviderNo();
			String cpp = request.getParameter("cpp");
			boolean cppFromMeasurements=false;
			if(cpp != null && cpp.equals("measurements")) {
				cppFromMeasurements=true;
			}
			String[] customCppIssues =OscarProperties.getInstance().getProperty("encounter.custom_cpp_issues","").split(",");

			PdfCopyFields finalDoc = new PdfCopyFields(os);
			finalDoc.getWriter().setStrictImageSequence(true);
		PdfRecordPrinter printer = new PdfRecordPrinter(request, os);
		int pageNum = 0;
			//loop through each visit..concatenate into 1 PDF
			for(int x=0;x<ids.length;x++) {

			if (!StringUtils.isBlank(ids[x])) {

				int appointmentNo = Integer.parseInt(ids[x]);
				Appointment appointment = appointmentDao.find(appointmentNo);
				if (appointment == null)
					continue;
				else
					pageNum++;

				if (pageNum > 1) {
					printer.setNewPage(true);
				}

				Demographic demographic = demographicDao
						.getClientByDemographicNo(appointment
								.getDemographicNo());
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
				Boolean includeCPPForPrevAppts = null;

				OscarProperties oscarProperties = OscarProperties.getInstance();
				String eyeform_onlyPrintCurrentVisit = oscarProperties
						.getProperty("eyeform_onlyPrintCurrentVisit");

				// if eyeform_onlyPrintCurrentVisit is not set at all then it
				// should work as previously..
				if (eyeform_onlyPrintCurrentVisit != null
						&& eyeform_onlyPrintCurrentVisit.trim().length() > 0) {
					eyeform_onlyPrintCurrentVisit = eyeform_onlyPrintCurrentVisit
							.trim();
					boolean onlyPrintCurrentApptRecordsFromEncounter = Boolean
							.parseBoolean(eyeform_onlyPrintCurrentVisit);

					// if only print current appt.. then includeCPPForPrevAppts
					// = false
					if (onlyPrintCurrentApptRecordsFromEncounter)
						includeCPPForPrevAppts = false;
					else
						includeCPPForPrevAppts = true;
				}
				String whichEyeForm = OscarProperties.getInstance()
						.getProperty("cme_js", "");

				IssueDAO issueDao = (IssueDAO) SpringUtils.getBean("IssueDAO");
				for (String customCppIssue : customCppIssues) {
					// Don't need to print out patient log
					if (customCppIssue != null
							&& (customCppIssue.equalsIgnoreCase("PatientLog") || customCppIssue
									.equalsIgnoreCase("GlaucomaRiskFactors")))
						continue;
					if (includeCPPForPrevAppts != null) {
						if (customCppIssue != null
								&& customCppIssue
										.equalsIgnoreCase("GlaucomaRiskFactors")) { // For
																					// Dr.Jinapriya,
																					// replace
																					// PatientLog
																					// with
																					// GlaucomaRiskFactors,
																					// but
																					// still
																					// use
																					// PatientLog
																					// issue.
							Issue i = issueDao.findIssueByCode("PatientLog");
							if (i != null) {
								printCppItem(printer, "Glaucoma Risk Factors",
										"PatientLog",
										demographic.getDemographicNo(),
										appointmentNo, includeCPPForPrevAppts);
							}
						} else { // ="PatientLog", or Misc
							Issue issue = issueDao
									.findIssueByCode(customCppIssue);
							if (issue != null) {
								printCppItem(printer, issue.getDescription(),
										customCppIssue,
										demographic.getDemographicNo(),
										appointmentNo, includeCPPForPrevAppts);
							}
						}
					} else {
						if (customCppIssue != null
								&& customCppIssue
										.equalsIgnoreCase("GlaucomaRiskFactors")) { // For
																					// Dr.Jinapriya,
																					// replace
																					// PatientLog
																					// with
																					// GlaucomaRiskFactors,
																					// but
																					// still
																					// use
																					// PatientLog
																					// issue.
							Issue i = issueDao.findIssueByCode("PatientLog");
							if (i != null) {
								printCppItem(printer, "Glaucoma Risk Factors",
										"PatientLog",
										demographic.getDemographicNo(),
										appointmentNo, true);
							}
						} else { // ="PatientLog", or Misc
							Issue issue = issueDao
									.findIssueByCode(customCppIssue);
							if (issue != null) {
								printCppItem(printer, issue.getDescription(),
										customCppIssue,
										demographic.getDemographicNo(),
										appointmentNo, true);
							}
						}
					}
				}

				if (includeCPPForPrevAppts != null) {
					if (whichEyeForm != null
							&& whichEyeForm
									.equalsIgnoreCase("eyeform_DrJinapriya")) {
						printCppItem(printer, "Subjective", "CurrentHistory",
								demographic.getDemographicNo(), appointmentNo,
								includeCPPForPrevAppts);
						printCppItem(printer, "Past Ocular History",
								"PastOcularHistory",
								demographic.getDemographicNo(), appointmentNo,
								includeCPPForPrevAppts);
						printCppItem(printer, "Medical History", "MedHistory",
								demographic.getDemographicNo(), appointmentNo,
								includeCPPForPrevAppts);
						printCppItem(printer, "Ocular Diagnoses", "FamHistory",
								demographic.getDemographicNo(), appointmentNo,
								includeCPPForPrevAppts);
						printCppItem(printer, "Objective", "DiagnosticNotes",
								demographic.getDemographicNo(), appointmentNo,
								includeCPPForPrevAppts);
						printCppItem(printer, "Drops Administered This Visit",
								"OcularMedication",
								demographic.getDemographicNo(), appointmentNo,
								includeCPPForPrevAppts);
						printCppItem(printer, "Systemic Meds", "OMeds",
								demographic.getDemographicNo(), appointmentNo,
								includeCPPForPrevAppts);
					} else {
						printCppItem(printer, "Current History",
								"CurrentHistory",
								demographic.getDemographicNo(), appointmentNo,
								includeCPPForPrevAppts);
						printCppItem(printer, "Past Ocular History",
								"PastOcularHistory",
								demographic.getDemographicNo(), appointmentNo,
								includeCPPForPrevAppts);
						printCppItem(printer, "Medical History", "MedHistory",
								demographic.getDemographicNo(), appointmentNo,
								includeCPPForPrevAppts);
						printCppItem(printer, "Family History", "FamHistory",
								demographic.getDemographicNo(), appointmentNo,
								includeCPPForPrevAppts);
						printCppItem(printer, "Diagnostic Notes",
								"DiagnosticNotes",
								demographic.getDemographicNo(), appointmentNo,
								includeCPPForPrevAppts);
						printCppItem(printer, "Ocular Medications",
								"OcularMedication",
								demographic.getDemographicNo(), appointmentNo,
								includeCPPForPrevAppts);
						printCppItem(printer, "Other Medications", "OMeds",
								demographic.getDemographicNo(), appointmentNo,
								includeCPPForPrevAppts);
					}
				} else {
					if (whichEyeForm != null
							&& whichEyeForm
									.equalsIgnoreCase("eyeform_DrJinapriya")) {
						printCppItem(printer, "Subjective", "CurrentHistory",
								demographic.getDemographicNo(), appointmentNo,
								false);
						printCppItem(printer, "Past Ocular History",
								"PastOcularHistory",
								demographic.getDemographicNo(), appointmentNo,
								true);
						printCppItem(printer, "Medical History", "MedHistory",
								demographic.getDemographicNo(), appointmentNo,
								true);
						printCppItem(printer, "Ocular Diagnoses", "FamHistory",
								demographic.getDemographicNo(), appointmentNo,
								true);
						printCppItem(printer, "Objective", "DiagnosticNotes",
								demographic.getDemographicNo(), appointmentNo,
								false);
						printCppItem(printer, "Drops Administered This Visit",
								"OcularMedication",
								demographic.getDemographicNo(), appointmentNo,
								true);
						printCppItem(printer, "Systemic Meds", "OMeds",
								demographic.getDemographicNo(), appointmentNo,
								true);

					} else {
						// work as it is
						printCppItem(printer, "Current History",
								"CurrentHistory",
								demographic.getDemographicNo(), appointmentNo,
								false);
						printCppItem(printer, "Past Ocular History",
								"PastOcularHistory",
								demographic.getDemographicNo(), appointmentNo,
								true);
						printCppItem(printer, "Medical History", "MedHistory",
								demographic.getDemographicNo(), appointmentNo,
								true);
						printCppItem(printer, "Family History", "FamHistory",
								demographic.getDemographicNo(), appointmentNo,
								true);
						printCppItem(printer, "Diagnostic Notes",
								"DiagnosticNotes",
								demographic.getDemographicNo(), appointmentNo,
								false);
						printCppItem(printer, "Ocular Medications",
								"OcularMedication",
								demographic.getDemographicNo(), appointmentNo,
								true);
						printCppItem(printer, "Other Medications", "OMeds",
								demographic.getDemographicNo(), appointmentNo,
								true);

					}

				}

				printer.setNewPage(true);

				// ocular procs
				List<EyeformOcularProcedure> ocularProcs = null;
				if (includeCPPForPrevAppts == null
						|| includeCPPForPrevAppts == true)
					ocularProcs = ocularProcDao.getAllPreviousAndCurrent(
							demographic.getDemographicNo(), appointmentNo);
				else
					ocularProcs = ocularProcDao.getAllCurrent(
							demographic.getDemographicNo(), appointmentNo);
				if (ocularProcs.size() > 0) {
					printer.printOcularProcedures(ocularProcs);
				}

				//specs history
				if ((whichEyeForm != null)
						&& ((whichEyeForm.equals("eyeform3"))
								|| ("eyeform3.1".equals(whichEyeForm)) || ("eyeform3.2"
									.equals(whichEyeForm)))) {

				} else {
					List<EyeformSpecsHistory> specsHistory;
					if (includeCPPForPrevAppts == null
							|| includeCPPForPrevAppts == true)
						specsHistory = specsHistoryDao
								.getAllPreviousAndCurrent(
										demographic.getDemographicNo(),
										appointmentNo);
					else
						specsHistory = specsHistoryDao.getAllCurrent(
								demographic.getDemographicNo(), appointmentNo);
					if (specsHistory.size() > 0) {
						printer.printSpecsHistory(specsHistory);
					}
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
					if ((whichEyeForm != null)
							&& ((whichEyeForm.equals("eyeform3"))
									|| ("eyeform3.1".equals(whichEyeForm)) || ("eyeform3.2"
										.equals(whichEyeForm)))) {
						MeasurementFormatter formatter = new MeasurementFormatter(
								measurements);
						printer.printEyeformMeasurements(formatter,
								appointmentNo);
					} else {
						MeasurementFormatter formatter = new MeasurementFormatter(
								measurements);
						printer.printEyeformMeasurements(formatter);
					}
				}

				//impression
				//let's filter out custom cpp notes, as they will already have been
				//printed out in CPP section
				List<CaseManagementNote> filteredNotes = new ArrayList<CaseManagementNote>();
				for(CaseManagementNote note:notes) {
					boolean okToAdd=true;
					for(String i:customCppIssues) {
						if (i.equalsIgnoreCase("GlaucomaRiskFactors")) { // Dr.Jinapriy's
																			// GlaucomaRiskFactors=PatientLog
																			// in
																			// eyeform2
																			// from
																			// Dr.Eric
																			// Tam
							if (containsIssue(note.getId().intValue(),
									"PatientLog")) {
								okToAdd = false;
								break;
							}
						} else {
							if (containsIssue(note.getId().intValue(), i)) {
								okToAdd = false;
								break;
							}
						}
					}
					if (okToAdd)
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

				}
			} //end of loop

		if (pageNum == 0) {
			printer.setNewPage(true);
		}
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

	public void printCppItemFromMeasurements(PdfRecordPrinter printer,
			String header, String measurementType, int demographicNo,
			int appointmentNo, boolean includePrevious)
			throws DocumentException {
		Measurement measurement = measurementDao
				.getLatestMeasurementByDemographicNoAndType(demographicNo,
						measurementType);
		if (measurement == null) {
			return;
		}
		if (!includePrevious) {
			if (measurement.getAppointmentNo() != appointmentNo) {
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

	public List<CaseManagementNote> filterOutCpp(
			Collection<CaseManagementNote> notes) {
		List<CaseManagementNote> filteredNotes = new ArrayList<CaseManagementNote>();
		for (CaseManagementNote note : notes) {
			boolean skip = false;
			for (CaseManagementIssue issue : note.getIssues()) {
				for (int x = 0; x < cppIssues.length; x++) {
					if (issue.getIssue().getCode().equals(cppIssues[x])) {
						skip = true;
					}
				}
			}
			if (!skip) {
				filteredNotes.add(note);
			}
		}
		return filteredNotes;
	}

	public List<CaseManagementNote> filterOutBillingDocEformForm(
			Collection<CaseManagementNote> notes) {
		List<CaseManagementNote> filteredNotes = new ArrayList<CaseManagementNote>();
		for (CaseManagementNote note : notes) {
			boolean skip = true;
			if (note.getEncounter_type() != null
					&& EncounterUtil.EncounterType.FACE_TO_FACE_WITH_CLIENT
							.getOldDbValue().equals(note.getEncounter_type()))
				skip = false;
			if (!skip) {
				filteredNotes.add(note);
			}
		}
		return filteredNotes;
	}

	public ActionForward prepareConReport(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String demoNo = request.getParameter("demographicNo");
		String appointmentNo = request.getParameter("appNo");
		String cpp = request.getParameter("cpp");
		boolean cppFromMeasurements = false;
		if (cpp != null && cpp.equals("measurements")) {
			cppFromMeasurements = true;
		}

		Integer demographicNo = new Integer(demoNo);
		Integer appNo = new Integer(0);
		if (appointmentNo != null && appointmentNo.trim().length() > 0
				&& !appointmentNo.equalsIgnoreCase("null"))
			appNo = new Integer(appointmentNo);

		Provider provider = LoggedInInfo.getLoggedInInfoFromSession(request).getLoggedInProvider();
		Demographic demographic = demographicDao
				.getClientByDemographicNo(demographicNo);

		request.setAttribute("demographicNo", demoNo);
		request.setAttribute("demographicName", demographic.getFormattedName());

		// demographic_ext
		String famName = new String();

		DemographicExt famExt = demographicExtDao.getDemographicExt(
				demographic.getDemographicNo(), "Family_Doctor");
		if (famExt != null) {
			famName = famExt.getValue();
		}
		request.setAttribute("famName", famName);

		EyeformConsultationReport cp = new EyeformConsultationReport();
		String refNo = null;
		String referraldoc = new String();

		// referralNo and referral_doc_name
		String famXml = demographic.getFamilyDoctor();
		if (famXml != null && famXml.length() > 0) {
			refNo = SxmlMisc.getXmlContent(famXml, "rdohip");
			referraldoc = SxmlMisc.getXmlContent(famXml, "rd");
			request.setAttribute("referral_doc_name", referraldoc);
			cp.setReferralNo(refNo);
			List<ProfessionalSpecialist> specList = professionalSpecialistDao.findByReferralNo(refNo);
			if (specList != null && specList.size() > 0) {
				cp.setReferralFax(specList.get(0).getFaxNumber());
			}
		}

		DemographicContactDao demographicContactDao = (DemographicContactDao) SpringUtils
				.getBean("demographicContactDao");
		List<DemographicContact> contacts = demographicContactDao
				.findByDemographicNoAndCategory(demographicNo, "professional");
		contacts = ContactAction.fillContactNames(contacts);
		request.setAttribute("contacts", contacts);

		if (!"saved".equalsIgnoreCase((String) request
				.getAttribute("savedflag"))
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
			if ("saved".equalsIgnoreCase((String) request
					.getAttribute("savedflag"))) {
				cpId = (String) request.getAttribute("cpId");
			}
			EyeformConsultationReportDao crDao = (EyeformConsultationReportDao) SpringUtils.getBean("eyeformConsultationReportDao");
			cp = crDao.find(new Integer(cpId));
			request.setAttribute("newFlag", "false");
			appNo = cp.getAppointmentNo();

			ProfessionalSpecialist specialist = professionalSpecialistDao
					.find(cp.getReferralId());
			if (specialist != null) {
				referraldoc = specialist.getLastName() + ","
						+ specialist.getFirstName();
				request.setAttribute("referral_doc_name", referraldoc);
				request.setAttribute("referral_id", specialist.getId());
				cp.setReferralNo(specialist.getReferralNo());
				cp.setReferralFax(specialist.getFaxNumber());
				refNo = specialist.getReferralNo();
			}
		}

		request.setAttribute("providerName",
				providerDao.getProvider(cp.getProviderNo()).getFormattedName());
			DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
			contacts = ContactAction.fillContactNames(demographicManager.getDemographicContacts(LoggedInInfo.getLoggedInInfoFromSession(request), demographicNo, "professional"));
			request.setAttribute("contacts", contacts);


		if (IsPropertiesOn.isMultisitesEnable()) {
			List<Site> sites = siteDao
					.getActiveSitesByProviderNo((String) request.getSession()
							.getAttribute("user"));
			request.setAttribute("sites", sites);

			Integer appt_no = cp.getAppointmentNo();
			Site defaultSite = null;
			if (cp.getSiteId() == null) {
				String location = null;
				if (appt_no != null) {
					Appointment appt = appointmentDao.find(appt_no);
					if (appt != null) {
						location = appt.getLocation();
						for (int i = 0; i < sites.size(); i++) {
							Site s = sites.get(i);
							if (s.getName().equals(location)) {
								defaultSite = s;
								cp.setSiteId(defaultSite.getSiteId());
								break;
							}
						}
					}
				}
			} else {
				for (int i = 0; i < sites.size(); i++) {
					Site s = sites.get(i);
					if (s.getId() == cp.getSiteId()) {
						defaultSite = s;
						break;
					}
				}
			}

			// ArrayList<SatelliteClinic> clinicArr = new
			// ArrayList<SatelliteClinic>();

		}

		DynaValidatorForm crForm = (DynaValidatorForm) form;
		crForm.set("cp", cp);
		request.setAttribute("cp", cp);

		// loades latest eyeform

		if ("".equalsIgnoreCase(refNo)) {
			String referral = demographic.getFamilyDoctor();

			if (referral != null && !"".equals(referral.trim())) {
				Integer ref = getRefId(referral);
				cp.setReferralId(ref);
				refNo = getRefNo(referral);

				List<ProfessionalSpecialist> refList = professionalSpecialistDao
						.findByReferralNo(refNo);
				if (refList != null && refList.size() > 0) {
					ProfessionalSpecialist refSpecialist = refList.get(0);
					referraldoc = refSpecialist.getLastName() + ","
							+ refSpecialist.getFirstName();
					request.setAttribute("referral_doc_name", referraldoc);
					cp.setReferralNo(refSpecialist.getReferralNo());
					cp.setReferralFax(refSpecialist.getFaxNumber());
				}
			}
		}

		request.setAttribute("reason", cp.getReason());

		String whichEyeForm = OscarProperties.getInstance().getProperty(
				"cme_js", "");

		if (("eyeform3".equals(whichEyeForm))
				|| ("eyeform3.1".equals(whichEyeForm))
				|| ("eyeform3.2".equals(whichEyeForm))) {
			if (cp.getExamination() != null) {
				String examination = cp.getExamination();
				examination = examination.replaceAll("\n", "<br>");
				examination = examination.replaceAll("\r", "");
				examination = examination.replaceAll("\t", "");
				request.setAttribute("old_examination", examination);
			}
		}

		Boolean includeCPPForPrevAppts = null;
		String eyeform_onlyPrintCurrentVisit = OscarProperties.getInstance()
				.getProperty("eyeform_onlyPrintCurrentVisit");
		if (eyeform_onlyPrintCurrentVisit != null
				&& eyeform_onlyPrintCurrentVisit.trim().length() > 0) {
			eyeform_onlyPrintCurrentVisit = eyeform_onlyPrintCurrentVisit
					.trim();
			boolean onlyPrintCurrentApptRecordsFromEncounter = Boolean
					.parseBoolean(eyeform_onlyPrintCurrentVisit);

			// if only print current appt.. then includeCPPForPrevAppts = false
			if (onlyPrintCurrentApptRecordsFromEncounter)
				includeCPPForPrevAppts = false;
			else
				includeCPPForPrevAppts = true;
		}
		if (includeCPPForPrevAppts != null) {
			if (whichEyeForm != null
					&& whichEyeForm.equalsIgnoreCase("eyeform_DrJinapriya")) {
				request.setAttribute("currentHistory", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem("Subjective:",
								"CurrentHistory",
								demographic.getDemographicNo(), appNo,
								includeCPPForPrevAppts)));
				request.setAttribute("pastOcularHistory", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Past Ocular History:", "PastOcularHistory",
								demographic.getDemographicNo(), appNo,
								includeCPPForPrevAppts)));
				request.setAttribute("medHistory", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Medical History:", "MedHistory",
								demographic.getDemographicNo(), appNo,
								includeCPPForPrevAppts)));
				request.setAttribute("famHistory", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Ocular Diagnoses:", "FamHistory",
								demographic.getDemographicNo(), appNo,
								includeCPPForPrevAppts)));
				request.setAttribute("diagnosticNotes", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem("Objective:",
								"DiagnosticNotes",
								demographic.getDemographicNo(), appNo,
								includeCPPForPrevAppts)));
				request.setAttribute("ocularMedication", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Drops Administered This Visit:",
								"OcularMedication",
								demographic.getDemographicNo(), appNo,
								includeCPPForPrevAppts)));
				request.setAttribute("otherMeds", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem("Systemic Meds:",
								"OMeds", demographic.getDemographicNo(), appNo,
								includeCPPForPrevAppts)));
			} else {

				request.setAttribute("currentHistory", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Current History:", "CurrentHistory",
								demographic.getDemographicNo(), appNo,
								includeCPPForPrevAppts)));
				request.setAttribute("pastOcularHistory", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Past Ocular History:", "PastOcularHistory",
								demographic.getDemographicNo(), appNo,
								includeCPPForPrevAppts)));
				request.setAttribute("medHistory", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Medical History:", "MedHistory",
								demographic.getDemographicNo(), appNo,
								includeCPPForPrevAppts)));
				request.setAttribute("famHistory", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Family History:", "FamHistory",
								demographic.getDemographicNo(), appNo,
								includeCPPForPrevAppts)));
				request.setAttribute("diagnosticNotes", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Diagnostic Notes:", "DiagnosticNotes",
								demographic.getDemographicNo(), appNo,
								includeCPPForPrevAppts)));
				// request.setAttribute("ocularMedication",StringEscapeUtils.escapeJavaScript(getFormattedCppItem("Current Medications:",
				// "OcularMedication", demographic.getDemographicNo(), appNo,
				// includeCPPForPrevAppts)));
				request.setAttribute("ocularMedication", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem("Ocular Meds:",
								"OcularMedication",
								demographic.getDemographicNo(), appNo, true)));
				request.setAttribute("otherMeds", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Other Medications:", "OMeds",
								demographic.getDemographicNo(), appNo,
								includeCPPForPrevAppts)));
			}
		} else {

			if (whichEyeForm != null
					&& whichEyeForm.equalsIgnoreCase("eyeform_DrJinapriya")) {
				request.setAttribute("currentHistory", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem("Subjective:",
								"CurrentHistory",
								demographic.getDemographicNo(), appNo, false)));
				request.setAttribute("pastOcularHistory", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Past Ocular History:", "PastOcularHistory",
								demographic.getDemographicNo(), appNo, true)));
				request.setAttribute("medHistory", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Medical History:", "MedHistory",
								demographic.getDemographicNo(), appNo, true)));
				request.setAttribute("famHistory", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Ocular Diagnoses:", "FamHistory",
								demographic.getDemographicNo(), appNo, true)));
				request.setAttribute("diagnosticNotes", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem("Objective:",
								"DiagnosticNotes",
								demographic.getDemographicNo(), appNo, true)));
				request.setAttribute("ocularMedication", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Drops Administered This Visit:",
								"OcularMedication",
								demographic.getDemographicNo(), appNo, true)));
				request.setAttribute("otherMeds", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem("Systemic Meds:",
								"OMeds", demographic.getDemographicNo(), appNo,
								true)));
			} else {

				request.setAttribute("currentHistory", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Current History:", "CurrentHistory",
								demographic.getDemographicNo(), appNo, false)));
				request.setAttribute("pastOcularHistory", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Past Ocular History:", "PastOcularHistory",
								demographic.getDemographicNo(), appNo, true)));
				request.setAttribute("medHistory", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Medical History:", "MedHistory",
								demographic.getDemographicNo(), appNo, true)));
				request.setAttribute("famHistory", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Family History:", "FamHistory",
								demographic.getDemographicNo(), appNo, true)));
				request.setAttribute("diagnosticNotes", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Diagnostic Notes:", "DiagnosticNotes",
								demographic.getDemographicNo(), appNo, true)));
				// request.setAttribute("ocularMedication",StringEscapeUtils.escapeJavaScript(getFormattedCppItem("Current Medications:",
				// "OcularMedication", demographic.getDemographicNo(), appNo,
				// true)));
				request.setAttribute("ocularMedication", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem("Ocular Meds:",
								"OcularMedication",
								demographic.getDemographicNo(), appNo, true)));
				request.setAttribute("otherMeds", StringEscapeUtils
						.escapeJavaScript(getFormattedCppItem(
								"Other Medications:", "OMeds",
								demographic.getDemographicNo(), appNo, true)));
			}
		}

		IssueDAO issueDao = (IssueDAO) SpringUtils.getBean("IssueDAO");
		String customCppIssues[] = OscarProperties.getInstance()
				.getProperty("encounter.custom_cpp_issues", "").split(",");
		for (String customCppIssue : customCppIssues) {
			if (includeCPPForPrevAppts != null) {
				if (customCppIssue != null
						&& customCppIssue
								.equalsIgnoreCase("GlaucomaRiskFactors")) { // For
																			// Dr.Jinapriya,
																			// replace
																			// PatientLog
																			// with
																			// GlaucomaRiskFactors,
																			// but
																			// still
																			// use
																			// PatientLog
																			// issue.
					Issue i = issueDao.findIssueByCode("PatientLog");
					if (i != null) {
						request.setAttribute(customCppIssue, StringEscapeUtils
								.escapeJavaScript(getFormattedCppItem(
										"Glaucoma Risk Factors" + ":",
										"PatientLog",
										demographic.getDemographicNo(), appNo,
										includeCPPForPrevAppts)));
					}
				} else { // ="PatientLog"
					Issue i = issueDao.findIssueByCode(customCppIssue);
					if (i != null) {
						request.setAttribute(customCppIssue, StringEscapeUtils
								.escapeJavaScript(getFormattedCppItem(
										i.getDescription() + ":",
										customCppIssue,
										demographic.getDemographicNo(), appNo,
										includeCPPForPrevAppts)));
					}
				}
			} else {
				if (customCppIssue != null
						&& customCppIssue
								.equalsIgnoreCase("GlaucomaRiskFactors")) { // For
																			// Dr.Jinapriya,
																			// replace
																			// PatientLog
																			// with
																			// GlaucomaRiskFactors,
																			// but
																			// still
																			// use
																			// PatientLog
																			// issue.
					Issue i = issueDao.findIssueByCode("PatientLog");
					if (i != null) {
						request.setAttribute(customCppIssue, StringEscapeUtils
								.escapeJavaScript(getFormattedCppItem(
										"Glaucoma Risk Factors" + ":",
										"PatientLog",
										demographic.getDemographicNo(), appNo,
										true)));
					}
				} else { // ="PatientLog"
					Issue i = issueDao.findIssueByCode(customCppIssue);
					if (i != null) {
						request.setAttribute(customCppIssue, StringEscapeUtils
								.escapeJavaScript(getFormattedCppItem(
										i.getDescription() + ":",
										customCppIssue,
										demographic.getDemographicNo(), appNo,
										true)));
					}
				}
			}
		}

		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		List<EyeformOcularProcedure> ocularProcs;
		String strOcularProcs = "";
		if (appNo != null && appNo != 0) {
			if (includeCPPForPrevAppts != null && !includeCPPForPrevAppts) {
				ocularProcs = ocularProcDao.getByAppointmentNo(appNo);
			} else if (includeCPPForPrevAppts != null && includeCPPForPrevAppts) {
				ocularProcs = ocularProcDao.getHistory(
						demographic.getDemographicNo(), new Date(), "A");
			} else {
				ocularProcs = ocularProcDao.getHistory(
						demographic.getDemographicNo(), new Date(), "A");
			}

			StringBuilder ocularProc = new StringBuilder();
			for (EyeformOcularProcedure op : ocularProcs) {
				ocularProc.append(sf.format(op.getDate()) + " ");
				ocularProc.append(op.getEye() + " ");
				ocularProc.append(op.getProcedureName() + " at "
						+ op.getLocation());
				ocularProc.append(" by "
						+ providerDao.getProvider(op.getDoctor())
								.getFormattedName());
				if (op.getProcedureNote() != null
						&& !"".equalsIgnoreCase(op.getProcedureNote().trim()))
					ocularProc.append(". " + op.getProcedureNote() + "\n");
			}

			strOcularProcs = ocularProc.toString();
			if (strOcularProcs != null
					&& !"".equalsIgnoreCase(strOcularProcs.trim()))
				strOcularProcs = "Past Ocular Procedures:\n" + strOcularProcs
						+ "\n";
			else
				strOcularProcs = "";
		}
		request.setAttribute("ocularProc",
				StringEscapeUtils.escapeJavaScript(strOcularProcs));

		List<EyeformSpecsHistory> specs;
		String specsStr1 = "";

		if (appNo != null && appNo != 0) {
			if (includeCPPForPrevAppts != null && !includeCPPForPrevAppts) {
				// specs = specsHistoryDao.getByAppointmentNo(appNo); //the old
				// version does not have appNo in specHistory, so all apptNo are
				// 0.
				specs = specsHistoryDao.getAllCurrent(
						demographic.getDemographicNo(), appNo);
			} else if (includeCPPForPrevAppts != null && includeCPPForPrevAppts) {
				specs = specsHistoryDao.getAllPreviousAndCurrent(
						demographic.getDemographicNo(), appNo);
			} else {
				specs = specsHistoryDao.getAllPreviousAndCurrent(
						demographic.getDemographicNo(), appNo);
			}

			StringBuilder specsStr = new StringBuilder();
			for (EyeformSpecsHistory spec : specs) {
				String specDate = spec.getDate() == null ? "" : sf.format(spec
						.getDate());
				specsStr.append(specDate + " ");

				StringBuilder data = new StringBuilder("");
				data.append(" OD ");
				StringBuilder dataTemp = new StringBuilder("");
				dataTemp.append(spec.getOdSph() == null ? "" : spec.getOdSph());
				dataTemp.append(spec.getOdCyl() == null ? "" : spec.getOdCyl());
				if (spec.getOdAxis() != null
						&& spec.getOdAxis().trim().length() != 0)
					dataTemp.append("x" + spec.getOdAxis());
				if (spec.getOdAdd() != null
						&& spec.getOdAdd().trim().length() != 0)
					dataTemp.append(" add " + spec.getOdAdd());
				if (spec.getOdPrism() != null && spec.getOdPrism().length() > 0) {
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
				if (spec.getOsAxis() != null
						&& spec.getOsAxis().trim().length() != 0)
					dataTemp.append("x" + spec.getOsAxis());
				if (spec.getOsAdd() != null
						&& spec.getOsAdd().trim().length() != 0)
					dataTemp.append(" add " + spec.getOsAdd());

				if (spec.getOsPrism() != null && spec.getOsPrism().length() > 0) {
					dataTemp.append(" prism " + spec.getOsPrism());
				}

				specsStr.append(dataTemp.toString() + "\n");
				data.append(dataTemp);
			}

			if (specsStr != null && specs.size() > 0)
				specsStr1 = "Spectacles:\n" + specsStr.toString();
			else
				specsStr1 = "";
		}
		request.setAttribute("specs",
				StringEscapeUtils.escapeJavaScript(specsStr1));

		// impression
		String impression = "";
		if (appNo != null && appNo != 0)
			impression = getImpression(appNo);
		impression = impression.trim();
		request.setAttribute("impression",
				StringEscapeUtils.escapeJavaScript(impression));

		// followUp
		StringBuilder followup = new StringBuilder();
		if (appNo != null && appNo != 0) {
			List<EyeformFollowUp> followUps = followUpDao
					.getByAppointmentNo(appNo);
			for (EyeformFollowUp ef : followUps) {
				// if (ef.getTimespan() >0) {
				if (!ef.getTimespan().equals("0")
						|| !ef.getTimespan().equals("")) {
					followup.append((ef.getType().equals("followup") ? "Follow Up"
							: "Consult")
							+ " in "
							+ ef.getTimespan()
							+ " "
							+ ef.getTimeframe());
				}
			}

			// get the checkboxes
			EyeForm eyeform = eyeFormDao.getByAppointmentNo(appNo);
			if (eyeform != null) {
				if (eyeform.getDischarge() != null
						&& eyeform.getDischarge().equals("true"))
					followup.append("Patient is discharged from my active care.\n");
				if (eyeform.getStat() != null
						&& eyeform.getStat().equals("true"))
					followup.append("Follow up as needed with me STAT or PRN if symptoms are worse.\n");
				if (eyeform.getOpt() != null && eyeform.getOpt().equals("true"))
					followup.append("Routine eye care by an optometrist is recommended.\n");

			}
		}
		request.setAttribute("followup",
				StringEscapeUtils.escapeJavaScript(followup.toString()));

		// test book
		StringBuilder testbook = new StringBuilder();
		EyeformTestBookDao testBookDao = (EyeformTestBookDao) SpringUtils
				.getBean("eyeformTestBookDao");
		if (appNo != null && appNo != 0) {
			List<EyeformTestBook> testBookRecords = testBookDao
					.getByAppointmentNo(appNo);
			for (EyeformTestBook tt : testBookRecords) {
				testbook.append(tt.getTestname());
				testbook.append(" ");
				testbook.append(tt.getEye());
				testbook.append("\n");
			}
		}

		if (testbook.length() > 0)
			testbook.insert(0, "Diagnostic test booking:");
		request.setAttribute("testbooking",
				StringEscapeUtils.escapeJavaScript(testbook.toString()));

		// procedure book
		StringBuilder probook = new StringBuilder();
		EyeformProcedureBookDao procedureBookDao = (EyeformProcedureBookDao) SpringUtils
				.getBean("eyeformProcedureBookDao");
		if (appNo != null && appNo != 0) {
			List<EyeformProcedureBook> procBookRecords = procedureBookDao
					.getByAppointmentNo(appNo);
			for (EyeformProcedureBook pp : procBookRecords) {
				probook.append(pp.getProcedureName());
				probook.append(" ");
				probook.append(pp.getEye());
				probook.append("\n");
			}
		}
		if (probook.length() > 0)
			probook.insert(0, "Procedure booking:");

		request.setAttribute("probooking",
				StringEscapeUtils.escapeJavaScript(probook.toString()));

		// for crm
		/* do not remove!!! please contact victor.weng@gmail.com if questions */
		crForm.set("isRefOnline", "false");
		String onlineMark = "$ref[";
		if (cp.getAppointmentNo() != 0) {
			Appointment appoint = appointmentDao.find(cp.getAppointmentNo());
			// Appointment appoint =
			// getEyeFormMgr().getAppointment(cp.getAppointmentNo(),
			// demographicNo);
			if (appoint != null
					&& appoint.getResources() != null
					&& appoint.getResources().toLowerCase().indexOf(onlineMark) >= 0)

				crForm.set("isRefOnline", "true");
		}

		return mapping.findForward("conReport");
	}

	public ActionForward saveConRequest(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("saveConRequest");
		EyeformConsultationReportDao dao = (EyeformConsultationReportDao) SpringUtils
				.getBean("eyeformConsultationReportDao");

		DynaValidatorForm crForm = (DynaValidatorForm) form;
		EyeformConsultationReport cp = (EyeformConsultationReport) crForm
				.get("cp");
		EyeformConsultationReport consultReport = null;

		oscar.OscarProperties props1 = oscar.OscarProperties.getInstance();
		String eyeform = props1.getProperty("cme_js");
		String examination = "";
		if (("eyeform3".equals(eyeform)) || ("eyeform3.1".equals(eyeform))
				|| ("eyeform3.2".equals(eyeform))) {
			HttpSession session = request.getSession();
			examination = (String) session.getAttribute("examination");
			if (examination == null) {
				examination = "";
			}
			if (cp.getExamination().trim().length() > 0) {
				if (!examination.contains(cp.getExamination())) {
					examination = cp.getExamination();
				}
			}
			
		}
		cp.setExamination(examination);

		// Integer id=cp.getId();
		Integer id = null;
		if (request.getParameter("cp.id") != null
				&& request.getParameter("cp.id").trim().length() > 0)
			id = Integer.parseInt(request.getParameter("cp.id").trim());
		else
			id = cp.getId();

		if (id != null && id.intValue() > 0) {
			consultReport = dao.find(id);
		} else {
			consultReport = new EyeformConsultationReport();

		}
		// BeanUtils.copyProperties(cp, consultReport, new
		// String[]{"id","demographic","provider"});
		BeanUtils.copyProperties(cp, consultReport, new String[] { "id" });

		
		ProfessionalSpecialist professionalSpecialist = null;
		if (cp.getReferralId() > 0) {
			professionalSpecialist = professionalSpecialistDao.find(cp.getReferralId());
		} else
			professionalSpecialist = professionalSpecialistDao
					.getByReferralNo(cp.getReferralNo());

		if (professionalSpecialist != null)
			consultReport.setReferralId(professionalSpecialist.getId());

		consultReport.setDate(new Date());

		dao.merge(consultReport);

		request.setAttribute("cpId", consultReport.getId().toString());
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

	public ActionForward printConRequest(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.debug("printConreport");
		EyeformConsultationReportDao dao = (EyeformConsultationReportDao) SpringUtils
				.getBean("eyeformConsultationReportDao");
		DynaValidatorForm crForm = (DynaValidatorForm) form;

		EyeformConsultationReport cp = (EyeformConsultationReport) crForm
				.get("cp");
		Demographic demographic = demographicDao.getClientByDemographicNo(cp
				.getDemographicNo());
		request.setAttribute("demographic", demographic);
		Appointment appointment = this.appointmentDao.find(cp
				.getAppointmentNo());

		EyeformConsultationReport consultReport = null;

		// String id = request.getParameter("cp.id");

		oscar.OscarProperties props1 = oscar.OscarProperties.getInstance();
		String eyeform = props1.getProperty("cme_js");
		String examination = "";
		if (("eyeform3".equals(eyeform)) || ("eyeform3.1".equals(eyeform))
				|| ("eyeform3.2".equals(eyeform))) {
			HttpSession session = request.getSession();
			examination = (String) session.getAttribute("examination");
			if (examination != null) {
				if (cp.getExamination() != null) {
					if (cp.getExamination().trim().length() > 0) {
						if (!examination.contains(cp.getExamination())) {
							examination = cp.getExamination();
							if (examination.contains("<br>")) {
								examination = examination.replaceAll("\n", "");
								examination = examination.replaceAll("<br>","\n");
							}
						}
					}
				}

				examination = examination.replaceAll("\r", "");
				examination = examination.replaceAll("\t", "");
				// examination = examination.replaceAll("<table>", "");
				// examination = examination.replaceAll("</table>", "");
				// examination = examination.replaceAll("<tbody>", "");
				// examination = examination.replaceAll("</tbody>", "");
				// examination = examination.replaceAll("<tr>", "");
				// examination = examination.replaceAll("</tr>", "");
				// examination = examination.replaceAll("<td>", " ");
				// examination = examination.replaceAll("</td>", "");
				cp.setExamination(examination);
			} else {
				if (cp.getExamination() == null) {
					examination = "";
					cp.setExamination(examination);
				} else {
					examination = cp.getExamination();
					examination = examination.replaceAll("\r", "");
					examination = examination.replaceAll("\t", "");
					examination = examination.replaceAll("<br>", "");
					// examination = examination.replaceAll("<table>", "");
					// examination = examination.replaceAll("</table>", "");
					// examination = examination.replaceAll("<tbody>", "");
					// examination = examination.replaceAll("</tbody>", "");
					// examination = examination.replaceAll("<tr>", "");
					// examination = examination.replaceAll("</tr>", "");
					// examination = examination.replaceAll("<td>", " ");
					// examination = examination.replaceAll("</td>", "");
					cp.setExamination(examination);
				}
			}
		}

		Integer id = null;
		if (request.getParameter("cp.id") != null
				&& request.getParameter("cp.id").trim().length() > 0)
			id = Integer.parseInt(request.getParameter("cp.id").trim());
		else
			id = cp.getId();

		if (id != null && id.intValue() > 0) {
			consultReport = dao.find(id);
			cp.setDate(consultReport.getDate());
		} else {
			consultReport = new EyeformConsultationReport();

		}
		// BeanUtils.copyProperties(cp, consultReport, new
		BeanUtils.copyProperties(cp, consultReport, new String[] { "id" });

		ProfessionalSpecialist professionalSpecialist = null;
		if (cp.getReferralId() > 0) {
			professionalSpecialist = professionalSpecialistDao.find(cp.getReferralId());
		} else
			professionalSpecialist = professionalSpecialistDao
					.getByReferralNo(cp.getReferralNo());
		if (professionalSpecialist != null)
			consultReport.setReferralId(professionalSpecialist.getId());
		if (cp.getDate() == null) {
			consultReport.setDate(new Date());
		}
		/*
		 * if(cp.getId() != null && cp.getId()>0) { dao.merge(cp); } else {
		 * dao.persist(cp); }
		 */
		dao.merge(consultReport);

			cp.setCc(divycc(cp.getCc()));
			cp.setClinicalInfo(divy(wrap(cp.getClinicalInfo(),80)));
			cp.setClinicalInfo(cp.getClinicalInfo().replaceAll("\\s", "&nbsp;"));
			cp.setConcurrentProblems(divy(wrap(cp.getConcurrentProblems(),80)));
			cp.setCurrentMeds(wrap(cp.getCurrentMeds(),80));
		if (("eyeform3".equals(eyeform)) || ("eyeform3.1".equals(eyeform))
				|| ("eyeform3.2".equals(eyeform))) {
			// cp.setExamination(divy((wrap(examination,80))));
			cp.setExamination(divy(examination));
		} else {
			cp.setExamination(divy(wrap(cp.getExamination(), 80)));
		}
		cp.setExamination(cp.getExamination().replaceAll("\n", ""));
		// cp.setImpression(divy(wrap(cp.getImpression(),80)));
		cp.setImpression(divy(cp.getImpression()));
			cp.setAllergies(divy(wrap(cp.getAllergies(),80)));
			cp.setPlan(divy(wrap(cp.getPlan(),80)));

			SimpleDateFormat sf = new SimpleDateFormat("MM/dd/yyyy");
		if (cp.getDate() != null)
			request.setAttribute("date", sf.format(cp.getDate()));
		else
			request.setAttribute("date", sf.format(new Date()));

		// Billingreferral ref =
			request.setAttribute("refer", professionalSpecialist);

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
			Site s = siteDao.getById(cp.getSiteId());
			clinic = new Clinic();
			clinic.setClinicName(s.getName());
			clinic.setClinicAddress(s.getAddress());
			clinic.setClinicCity(s.getCity());
			clinic.setClinicProvince(s.getProvince());
			clinic.setClinicPostal(s.getPostal());
			clinic.setClinicPhone(s.getPhone());
			clinic.setClinicFax(s.getFax());

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

		if (appointment != null) {
			Provider apptProvider = providerDao.getProvider(appointment
					.getProviderNo());
			request.setAttribute("appointmentDoctor",
					apptProvider.getFormattedName());
			
			String specialty_apptDoc = new String();			
			if (apptProvider != null)
				specialty_apptDoc = apptProvider.getSpecialty();
			if (specialty_apptDoc != null && !"".equalsIgnoreCase(specialty_apptDoc.trim())) {				
				specialty_apptDoc = ", " + specialty_apptDoc.trim();
			} else
				specialty_apptDoc = new String();
			request.setAttribute("specialty_apptDoc", specialty_apptDoc);
		}

		return mapping.findForward("printReport");
	}

	public ActionForward faxConRequest(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.debug("printConreport");
		EyeformConsultationReportDao dao = (EyeformConsultationReportDao) SpringUtils
				.getBean("eyeformConsultationReportDao");
		DynaValidatorForm crForm = (DynaValidatorForm) form;

		EyeformConsultationReport cp = (EyeformConsultationReport) crForm
				.get("cp");
		Demographic demographic = demographicDao.getClientByDemographicNo(cp
				.getDemographicNo());
		request.setAttribute("demographic", demographic);
		Appointment appointment = this.appointmentDao.find(cp
				.getAppointmentNo());

		EyeformConsultationReport consultReport = null;

		// String id = request.getParameter("cp.id");

		oscar.OscarProperties props1 = oscar.OscarProperties.getInstance();
		String eyeform = props1.getProperty("cme_js");
		String examination = "";
		if (("eyeform3".equals(eyeform)) || ("eyeform3.1".equals(eyeform))
				|| ("eyeform3.2".equals(eyeform))) {
			HttpSession session = request.getSession();
			examination = (String) session.getAttribute("examination");
			if (examination != null) {
				if (cp.getExamination() != null) {
					if (cp.getExamination().length() > 0) {
						if (!examination.contains(cp.getExamination())) {
							examination = cp.getExamination();
							if (examination.contains("<br>")) {
								examination = examination.replaceAll("\n", "");
								examination = examination.replaceAll("<br>",
										"\n");
							}
						}
					}
				}

				examination = examination.replaceAll("\r", "");
				examination = examination.replaceAll("\t", "");
				// examination = examination.replaceAll("<table>", "");
				// examination = examination.replaceAll("</table>", "");
				// examination = examination.replaceAll("<tbody>", "");
				// examination = examination.replaceAll("</tbody>", "");
				// examination = examination.replaceAll("<tr>", "");
				// examination = examination.replaceAll("</tr>", "");
				// examination = examination.replaceAll("<td>", " ");
				// examination = examination.replaceAll("</td>", "");
				cp.setExamination(examination);
			} else {
				if (cp.getExamination() == null) {
					examination = "";
					cp.setExamination(examination);
				} else {
					examination = cp.getExamination();
					examination = examination.replaceAll("\r", "");
					examination = examination.replaceAll("\t", "");
					examination = examination.replaceAll("<br>", "");
					// examination = examination.replaceAll("<table>", "");
					// examination = examination.replaceAll("</table>", "");
					// examination = examination.replaceAll("<tbody>", "");
					// examination = examination.replaceAll("</tbody>", "");
					// examination = examination.replaceAll("<tr>", "");
					// examination = examination.replaceAll("</tr>", "");
					// examination = examination.replaceAll("<td>", " ");
					// examination = examination.replaceAll("</td>", "");
					cp.setExamination(examination);
				}
			}
		}

		Integer id = null;
		if (request.getParameter("cp.id") != null
				&& request.getParameter("cp.id").trim().length() > 0)
			id = Integer.parseInt(request.getParameter("cp.id").trim());
		else
			id = cp.getId();

		if (id != null && id.intValue() > 0) {
			consultReport = dao.find(id);
			cp.setDate(consultReport.getDate());
		} else {
			consultReport = new EyeformConsultationReport();

		}
		// BeanUtils.copyProperties(cp, consultReport, new
		// String[]{"id","demographic","provider"});
		BeanUtils.copyProperties(cp, consultReport, new String[] { "id" });

		ProfessionalSpecialist professionalSpecialist = null;
		if (cp.getReferralId() > 0) {
			professionalSpecialist = professionalSpecialistDao.find(cp.getReferralId());
		} else
			professionalSpecialist = professionalSpecialistDao
					.getByReferralNo(cp.getReferralNo());
		if (professionalSpecialist != null)
			consultReport.setReferralId(professionalSpecialist.getId());
		if (cp.getDate() == null) {
			consultReport.setDate(new Date());
		}
		/*
		 * if(cp.getId() != null && cp.getId()>0) { dao.merge(cp); } else {
		 * dao.persist(cp); }
		 */
		dao.merge(consultReport);

		cp.setCc(divycc(cp.getCc()));
		cp.setClinicalInfo(divy(wrap(cp.getClinicalInfo(), 80)));
		cp.setClinicalInfo(cp.getClinicalInfo().replaceAll("\\s", "&nbsp;"));
		cp.setConcurrentProblems(divy(wrap(cp.getConcurrentProblems(), 80)));
		cp.setCurrentMeds(wrap(cp.getCurrentMeds(), 80));
		if (("eyeform3".equals(eyeform)) || ("eyeform3.1".equals(eyeform))
				|| ("eyeform3.2".equals(eyeform))) {
			// cp.setExamination(divy((wrap(examination,80))));
			cp.setExamination(divy(examination));
		} else {
			cp.setExamination(divy(wrap(cp.getExamination(), 80)));
		}
		cp.setExamination(cp.getExamination().replaceAll("\n", ""));
		// cp.setImpression(divy(wrap(cp.getImpression(),80)));
		cp.setImpression(divy(cp.getImpression()));
		cp.setAllergies(divy(wrap(cp.getAllergies(), 80)));
		cp.setPlan(divy(wrap(cp.getPlan(), 80)));

		SimpleDateFormat sf = new SimpleDateFormat("MM/dd/yyyy");
		if (cp.getDate() != null)
			request.setAttribute("date", sf.format(cp.getDate()));
		else
			request.setAttribute("date", sf.format(new Date()));

		// Billingreferral ref =
		// billingreferralDao.getByReferralNo(String.valueOf(cp.getReferralId()));
		request.setAttribute("refer", professionalSpecialist);

		request.setAttribute("cp", cp);

		Provider internalProvider = null;
		if (demographic.getProviderNo() != null
				&& !demographic.getProviderNo().equalsIgnoreCase("null")
				&& demographic.getProviderNo().length() > 0) {

			internalProvider = providerDao.getProvider(demographic
					.getProviderNo());
			if (internalProvider != null) {
				request.setAttribute("internalDrName",
						internalProvider.getFirstName() + " "
								+ internalProvider.getLastName());
			} else {
				// request.setAttribute("internalDrName", );
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
			Site s = siteDao.getById(cp.getSiteId());
			clinic = new Clinic();
			clinic.setClinicName(s.getName());
			clinic.setClinicAddress(s.getAddress());
			clinic.setClinicCity(s.getCity());
			clinic.setClinicProvince(s.getProvince());
			clinic.setClinicPostal(s.getPostal());
			clinic.setClinicPhone(s.getPhone());
			clinic.setClinicFax(s.getFax());
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
		request.setAttribute("appointDate",
				(appointment != null ? appointment.getAppointmentDate() : ""));

		if (appointment != null) {
			Provider apptProvider = providerDao.getProvider(appointment
					.getProviderNo());
			request.setAttribute("appointmentDoctor",
					apptProvider.getFormattedName());
			
			String specialty_apptDoc = new String();			
			if (apptProvider != null)
				specialty_apptDoc = apptProvider.getSpecialty();
			if (specialty_apptDoc != null && !"".equalsIgnoreCase(specialty_apptDoc.trim())) {				
				specialty_apptDoc = ", " + specialty_apptDoc.trim();
			} else
				specialty_apptDoc = new String();
			request.setAttribute("specialty_apptDoc", specialty_apptDoc);
		}

		boolean faxflag = props.isConsultationFaxEnabled();
		faxflag = false; // force to execute the if branch
		if (!faxflag) {
			try {
				com.itextpdf.text.Document document = new com.itextpdf.text.Document();
				document.top(50f);
				PdfPTable table = new PdfPTable(1);
				PdfPTable table1 = new PdfPTable(1);
				float[] widths2 = { 0.5f, 0.5f };
				PdfPTable table2 = new PdfPTable(widths2);
				float[] widths3 = { 0.3f, 0.7f };
				PdfPTable table3 = new PdfPTable(widths3);
				PdfPTable table4 = new PdfPTable(widths3);
				PdfPTable table5 = new PdfPTable(1);
				PdfPTable table7 = new PdfPTable(1);
				Object eyeformCReport = request.getAttribute("cp");
				EyeformConsultationReport eyeformConsultationReport = (EyeformConsultationReport) eyeformCReport;
				int number = eyeformConsultationReport.getReferralId() == 0 ? 0
						: eyeformConsultationReport.getReferralId();

				String nowtime = System.currentTimeMillis() + "";
				PdfWriter.getInstance(
						document,
						new FileOutputStream(System
								.getProperty("java.io.tmpdir")
								+ "/CRF-"
								+ number + "." + nowtime + ".pdf"));
				document.open();
				
				
				Object str1 = request.getAttribute("mdstring") == null ? ""
						: request.getAttribute("mdstring");
				/*
				Object str2 = request.getAttribute("internalDrName") == null ? ""
						: request.getAttribute("internalDrName");
				Object str3 = request.getAttribute("specialty") == null ? ""
						: request.getAttribute("specialty");
				*/
				Object str2 = request.getAttribute("appointmentDoctor") == null ? ""
						: request.getAttribute("appointmentDoctor");
				Object str3 = request.getAttribute("specialty_apptDoc") == null ? ""
						: request.getAttribute("specialty_apptDoc");
				
				PdfPCell cell = new PdfPCell(new Paragraph((String) str1
						+ (String) str2 + (String) str3, FontFactory.getFont(
						FontFactory.COURIER, 9, Font.BOLD)));					

						
				cell.setColspan(1);
				cell.setBorderWidth(0f);
				cell.setPaddingLeft(30f);
				table1.addCell(cell);

				Clinic clinic1 = (Clinic) request.getAttribute("clinic");
				String clinicname = clinic1.getClinicName() == null ? ""
						: clinic1.getClinicName();
				PdfPCell cell2 = new PdfPCell(new Paragraph(clinicname,
						FontFactory.getFont(FontFactory.COURIER, 9, Font.BOLD)));
				cell2.setColspan(1);
				cell2.setBorderWidth(0f);
				cell2.setPaddingLeft(30f);
				table1.addCell(cell2);

				String cliniAddre = clinic1.getClinicAddress() == null ? ""
						: clinic1.getClinicAddress();
				String clinicCity = clinic1.getClinicCity() == null ? ""
						: clinic1.getClinicCity();
				String clinicProvince = clinic1.getClinicProvince() == null ? ""
						: clinic1.getClinicProvince();
				String clinicPostal = clinic1.getClinicPostal() == null ? ""
						: clinic1.getClinicPostal();
				PdfPCell cell3 = new PdfPCell(new Paragraph(cliniAddre + ", "
						+ clinicCity + ", " + clinicProvince + ", "
						+ clinicPostal, FontFactory.getFont(
						FontFactory.COURIER, 7)));
				cell3.setColspan(1);
				cell3.setBorderWidth(0f);
				cell3.setPaddingLeft(30f);
				table1.addCell(cell3);

				boolean isMultiSites = props.getBooleanProperty("multisites",
						"on");
				String clicnicphone = clinic1.getClinicPhone() == null ? ""
						: clinic1.getClinicPhone();
				String ClinicFax = clinic1.getClinicFax() == null ? ""
						: clinic1.getClinicFax();

				if (!isMultiSites) {
					PdfPCell cell4 = new PdfPCell(new Paragraph(
							"Tel:   " + clicnicphone + "      " + "Fax:   "
									+ ClinicFax + "     URL: "
									+ props.getProperty("clinicurl", ""),
							FontFactory.getFont(FontFactory.COURIER, 7)));
					cell4.setColspan(1);
					cell4.setBorderWidth(0f);
					cell4.setPaddingLeft(30f);
					table1.addCell(cell4);

				} else {
					Clinic tmpCli = (Clinic) request.getAttribute("clinic");
					if (tmpCli != null) {
						SiteDao siteDao = (SiteDao) SpringUtils
								.getBean(SiteDao.class);
						Site site = siteDao.findByName(tmpCli.getClinicName());
						PdfPCell cell4 = new PdfPCell(new Paragraph("Tel:"
								+ clicnicphone + "       Fax:" + ClinicFax
								+ "   URL: " + site.getSiteUrl() == null ? ""
								: site.getSiteUrl(), FontFactory.getFont(
								FontFactory.COURIER, 7)));
						cell4.setColspan(1);
						cell4.setBorderWidth(0f);
						cell4.setPaddingLeft(30f);

					} else {

						PdfPCell cell4 = new PdfPCell(new Paragraph("Tel:"
								+ clicnicphone + "        Fax:" + ClinicFax
								+ "   URL: "
								+ props.getProperty("clinicurl", ""),
								FontFactory.getFont(FontFactory.COURIER, 7)));
						cell4.setColspan(1);
						cell4.setBorderWidth(0f);
						cell4.setPaddingLeft(30f);
						table1.addCell(cell4);

					}
				}

				PdfPCell cell5 = new PdfPCell(new Paragraph(
						"Consultation Report", FontFactory.getFont(
								FontFactory.COURIER, 9, Font.BOLD)));
				cell5.setColspan(1);
				cell5.setBorderWidth(0f);
				cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell5.setMinimumHeight(40f);
				table7.addCell(cell5);
				table7.setWidthPercentage(98);
				PdfPCell cell80 = new PdfPCell(table7);
				cell80.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell80.setBorderWidthBottom(0f);
				cell80.setBorderWidthTop(0f);
				// table1.setHorizontalAlignment(Element.ALIGN_LEFT);
				table1.setWidthPercentage(98);
				PdfPCell cell90 = new PdfPCell(table1);
				cell90.setBorderWidthBottom(0f);
				table.addCell(cell90);
				table.addCell(cell80);

				String date = (String) request.getAttribute("date") == null ? ""
						: (String) request.getAttribute("date");
				PdfPCell cell6 = new PdfPCell(new Paragraph("Date:",
						FontFactory.getFont(FontFactory.COURIER, 8, Font.BOLD)));
				cell6.setBorderWidth(0f);
				table3.addCell(cell6);
				PdfPCell cell7 = new PdfPCell(new Paragraph(date,
						FontFactory.getFont(FontFactory.COURIER, 7)));
				cell7.setBorderWidthLeft(0f);
				cell7.setBorderWidthRight(0f);
				cell7.setBorderWidthTop(0f);
				table3.addCell(cell7);

				Serializable refer1 = (Serializable) request
						.getAttribute("refer") == null ? ""
						: (Serializable) request.getAttribute("refer");
				ProfessionalSpecialist refer = (ProfessionalSpecialist) refer1;
				String lname = refer.getLastName() == null ? "" : refer
						.getLastName();
				String fname = refer.getFirstName() == null ? "" : refer
						.getFirstName();
				PdfPCell cell8 = new PdfPCell(new Paragraph("To:",
						FontFactory.getFont(FontFactory.COURIER, 8, Font.BOLD)));
				cell8.setBorderWidth(0f);
				table3.addCell(cell8);
				PdfPCell cell9 = new PdfPCell(new Paragraph(lname + ", "
						+ fname, FontFactory.getFont(FontFactory.COURIER, 7)));
				cell9.setBorderWidthLeft(0f);
				cell9.setBorderWidthRight(0f);
				cell9.setBorderWidthTop(0f);
				table3.addCell(cell9);

				String raddress = refer.getStreetAddress() == null ? "" : refer
						.getStreetAddress();

				PdfPCell cell10 = new PdfPCell(new Paragraph("Address:",
						FontFactory.getFont(FontFactory.COURIER, 8, Font.BOLD)));
				cell10.setBorderWidth(0f);
				table3.addCell(cell10);
				PdfPCell cell11 = new PdfPCell(new Paragraph(raddress,
						FontFactory.getFont(FontFactory.COURIER, 7)));
				// cell11.setBorderWidth(0f);
				cell11.setBorderWidthLeft(0f);
				cell11.setBorderWidthRight(0f);
				cell11.setBorderWidthTop(0f);
				table3.addCell(cell11);

				String rphone = refer.getPhoneNumber() == null ? "" : refer
						.getPhoneNumber();
				PdfPCell cell12 = new PdfPCell(new Paragraph("Phone:",
						FontFactory.getFont(FontFactory.COURIER, 8, Font.BOLD)));
				cell12.setBorderWidth(0f);
				table3.addCell(cell12);
				PdfPCell cell13 = new PdfPCell(new Paragraph(rphone,
						FontFactory.getFont(FontFactory.COURIER, 7)));
				cell13.setBorderWidthLeft(0f);
				cell13.setBorderWidthRight(0f);
				cell13.setBorderWidthTop(0f);
				table3.addCell(cell13);

				String rfax = refer.getFaxNumber() == null ? "" : refer
						.getFaxNumber();
				PdfPCell cell14 = new PdfPCell(new Paragraph("Fax:",
						FontFactory.getFont(FontFactory.COURIER, 8, Font.BOLD)));
				cell14.setBorderWidth(0f);
				table3.addCell(cell14);
				PdfPCell cell15 = new PdfPCell(new Paragraph(rfax,
						FontFactory.getFont(FontFactory.COURIER, 7)));
				cell15.setBorderWidthLeft(0f);
				cell15.setBorderWidthRight(0f);
				cell15.setBorderWidthTop(0f);
				table3.addCell(cell15);

				FileOutputStream fos = new FileOutputStream(
						System.getProperty("java.io.tmpdir") + "/CRF-" + number
								+ "." + nowtime + ".txt");
				java.io.PrintWriter pw = new java.io.PrintWriter(fos);
				rfax = rfax.replaceAll(" ", "");
				pw.println(rfax);
				pw.close();
				fos.close();

				PdfPCell cell16 = new PdfPCell(new Paragraph("CC:",
						FontFactory.getFont(FontFactory.COURIER, 8, Font.BOLD)));
				cell16.setBorderWidth(0f);
				table3.addCell(cell16);
				PdfPCell cell17 = new PdfPCell(new Paragraph(cp.getCc(),
						FontFactory.getFont(FontFactory.COURIER, 7)));
				cell17.setBorderWidthLeft(0f);
				cell17.setBorderWidthRight(0f);
				cell17.setBorderWidthTop(0f);
				table3.addCell(cell17);

//				PdfPCell cell18 = new PdfPCell(new Paragraph("Re:",
//						FontFactory.getFont(FontFactory.COURIER, 8, Font.BOLD)));
//				cell18.setBorderWidth(0f);
//				table3.addCell(cell18);
				
				
				PdfPCell cell19 = new PdfPCell(new Paragraph(cp.getReason(),
						FontFactory.getFont(FontFactory.COURIER, 7)));
				cell19.setBorderWidthLeft(0f);
				cell19.setBorderWidthRight(0f);
				cell19.setBorderWidthTop(0f);
				table3.addCell(cell19);
				table3.setHorizontalAlignment(Element.ALIGN_LEFT);
				table3.setWidthPercentage(100);
				// document.add(new Paragraph("Re:"+cp.getReason()));
				table2.addCell(table3);

				Serializable demographic1 = (Serializable) request
						.getAttribute("demographic") == null ? ""
						: (Serializable) request.getAttribute("demographic");
				Demographic demographic2 = (Demographic) demographic1;
				String dlname = demographic2.getLastName() == null ? ""
						: demographic2.getLastName();
				String dfname = demographic2.getFirstName() == null ? ""
						: demographic2.getFirstName();

				PdfPCell cell20 = new PdfPCell(new Paragraph("Patient:",
						FontFactory.getFont(FontFactory.COURIER, 8, Font.BOLD)));
				cell20.setBorderWidth(0f);
				//cell20.setMinimumHeight(20f);
				table4.addCell(cell20);
				PdfPCell cell21 = new PdfPCell(new Paragraph(dlname + ","
						+ dfname, FontFactory.getFont(FontFactory.COURIER, 7)));
				cell21.setBorderWidthLeft(0f);
				cell21.setBorderWidthRight(0f);
				cell21.setBorderWidthTop(0f);
				//cell21.setMinimumHeight(20f);
				table4.addCell(cell21);

				String address = demographic.getAddress() == null ? ""
						: demographic.getAddress();
				if (!address.equals("")) {
					address += ",";
				}
				String city = demographic.getCity() == null ? "" : demographic
						.getCity();
				if (!city.equals("")) {
					city += ",";
				}
				String province = demographic.getProvince() == null ? ""
						: demographic.getProvince();
				if (!province.equals("")) {
					province += ",";
				}
				String postal = demographic.getPostal() == null ? ""
						: demographic.getPostal();

				PdfPCell cell22 = new PdfPCell(new Paragraph("Address:",
						FontFactory.getFont(FontFactory.COURIER, 8, Font.BOLD)));
				cell22.setBorderWidth(0f);
				cell22.setMinimumHeight(20f);
				table4.addCell(cell22);
				PdfPCell cell23 = new PdfPCell(new Paragraph(address + city
						+ province + postal, FontFactory.getFont(
						FontFactory.COURIER, 7)));
				cell23.setBorderWidthLeft(0f);
				cell23.setBorderWidthRight(0f);
				cell23.setBorderWidthTop(0f);
				cell23.setMinimumHeight(20f);
				table4.addCell(cell23);

				String phone = demographic.getPhone() == null ? ""
						: demographic.getPhone();

				PdfPCell cell24 = new PdfPCell(new Paragraph("Phone:",
						FontFactory.getFont(FontFactory.COURIER, 8, Font.BOLD)));
				cell24.setBorderWidth(0f);
				//cell24.setMinimumHeight(20f);
				table4.addCell(cell24);
				PdfPCell cell25 = new PdfPCell(new Paragraph(phone,
						FontFactory.getFont(FontFactory.COURIER, 7)));
				cell25.setBorderWidthLeft(0f);
				cell25.setBorderWidthRight(0f);
				cell25.setBorderWidthTop(0f);
				//cell25.setMinimumHeight(20f);
				table4.addCell(cell25);

				String yearOfBirth = demographic.getYearOfBirth() == null ? ""
						: demographic.getYearOfBirth();
				String monthOfBirth = demographic.getMonthOfBirth() == null ? ""
						: demographic.getMonthOfBirth();
				String dateOfBirth = demographic.getDateOfBirth() == null ? ""
						: demographic.getDateOfBirth();
				PdfPCell cell26 = new PdfPCell(new Paragraph("DOB:",
						FontFactory.getFont(FontFactory.COURIER, 8, Font.BOLD)));
				cell26.setBorderWidth(0f);
				//cell26.setMinimumHeight(20f);
				table4.addCell(cell26);
				PdfPCell cell27 = new PdfPCell(new Paragraph(yearOfBirth + "/"
						+ monthOfBirth + "/" + dateOfBirth + " (y/m/d)",
						FontFactory.getFont(FontFactory.COURIER, 7)));
				cell27.setBorderWidthLeft(0f);
				cell27.setBorderWidthRight(0f);
				cell27.setBorderWidthTop(0f);
				//cell27.setMinimumHeight(20f);
				table4.addCell(cell27);

				String hin = demographic.getHin() == null ? "" : demographic
						.getHin();
				String ver = demographic.getVer() == null ? "" : demographic
						.getVer();
				PdfPCell cell28 = new PdfPCell(new Paragraph("OHIP #",
						FontFactory.getFont(FontFactory.COURIER, 8, Font.BOLD)));
				cell28.setBorderWidth(0f);
				table4.addCell(cell28);
				PdfPCell cell29 = new PdfPCell(new Paragraph(hin + " " + ver,
						FontFactory.getFont(FontFactory.COURIER, 7)));
				cell29.setBorderWidthLeft(0f);
				cell29.setBorderWidthRight(0f);
				cell29.setBorderWidthTop(0f);
				table4.addCell(cell29);

				table4.setHorizontalAlignment(Element.ALIGN_LEFT);
				table4.setWidthPercentage(100);
				table4.setHorizontalAlignment(Element.ALIGN_CENTER);
				table2.addCell(table4);
				table2.setHorizontalAlignment(Element.ALIGN_LEFT);
				table2.setWidthPercentage(100);
				PdfPCell cell60 = new PdfPCell(table2);
				cell60.setBorderWidthBottom(0f);
				cell60.setBorderWidthTop(0f);
				cell60.setPadding(0f);
				cell60.setPaddingLeft(3f);
				cell60.setPaddingRight(3f);
				table.addCell(cell60);

				String rlname = refer.getLastName() == null ? "" : refer
						.getLastName();
				PdfPCell cell30 = new PdfPCell(new Paragraph("Dear Dr."
						+ rlname, FontFactory.getFont(FontFactory.COURIER, 8,
						Font.BOLD)));
				cell30.setBorderWidth(0f);
				table5.addCell(cell30);

				String letter = "";

				int eyeformCReportvalue = eyeformConsultationReport
						.getGreeting();
				if (eyeformCReportvalue == 1) {
					String age = demographic.getAge() == null ? ""
							: demographic.getAge();
					letter += "I had the pleasure of seeing " + age
							+ " year old " + dlname + "," + dfname;
					Object appointdate = request.getAttribute("appointDate");
					if (appointdate != null) {
						letter += " on " + appointdate;
					}
					letter += " on your kind referral.";
					PdfPCell cell31 = new PdfPCell(new Paragraph(letter,
							FontFactory.getFont(FontFactory.COURIER, 7)));
					cell31.setBorderWidth(0f);
					table5.addCell(cell31);

				}
				if (eyeformCReportvalue == 2) {
					String age = demographic.getAge() == null ? ""
							: demographic.getAge();
					letter += "This is a report on my most recent assessment of "
							+ age + " year old " + dlname + "," + dfname;
					Object appointdate = request.getAttribute("appointDate");
					if (appointdate != null) {
						letter += " on " + appointdate;
					}
					letter += ".";
					PdfPCell cell31 = new PdfPCell(new Paragraph(letter,
							FontFactory.getFont(FontFactory.COURIER, 7)));
					cell31.setBorderWidth(0f);
					table5.addCell(cell31);

				}

				String ClinicalInfo = eyeformConsultationReport
						.getClinicalInfo();
				if (!ClinicalInfo.equals("")) {
					PdfPCell cell32 = new PdfPCell(new Paragraph(
							"Current Clinical Information:",
							FontFactory.getFont(FontFactory.COURIER, 8,
									Font.BOLD)));
					cell32.setBorderWidth(0f);
					table5.addCell(cell32);
					String clinicalinfo = cp.getClinicalInfo() == null ? ""
							: cp.getClinicalInfo();

					String str = clinicalinfo.replaceAll("&nbsp;", " ");
					String testhuan = "<br>";
					char buf[] = str.toCharArray();
					int numhuan = 0;
					for (int i = 0; i < buf.length; i++) {
						if ("<".equalsIgnoreCase(buf[i] + "")) {
							numhuan++;
						}
					}
					for (int i = 0; i < numhuan; i++) {
						int s = str.indexOf("<");
						int e = str.indexOf(">");
						if (s != -1 && e != -1) {

							if (testhuan.equalsIgnoreCase(str.substring(s,
									e + 1))) {
								str = str.replaceAll(str.substring(s, e + 1),
										"\n");
								continue;
							}
							str = str.substring(0, s) + str.substring(e + 1);

						}
						if (s == -1 || e == -1) {
							break;
						}
					}

					PdfPCell cell33 = new PdfPCell(new Paragraph(str,
							FontFactory.getFont(FontFactory.COURIER, 7)));
					cell33.setBorderWidth(0f);
					table5.addCell(cell33);

				}

				String Allergies = eyeformConsultationReport.getAllergies();
				if (!Allergies.equals("")) {
					PdfPCell cell34 = new PdfPCell(new Paragraph(
							"Allergies and Medications:", FontFactory.getFont(
									FontFactory.COURIER, 8, Font.BOLD)));
					cell34.setBorderWidth(0f);
					table5.addCell(cell34);
					String allergies = cp.getAllergies() == null ? "" : cp
							.getAllergies();

					String str = allergies;
					String testhuan = "<br>";
					char buf[] = str.toCharArray();
					int numhuan = 0;
					for (int i = 0; i < buf.length; i++) {
						if ("<".equalsIgnoreCase(buf[i] + "")) {
							numhuan++;
						}
					}
					for (int i = 0; i < numhuan; i++) {
						int s = str.indexOf("<");
						int e = str.indexOf(">");
						if (s != -1 && e != -1) {

							if (testhuan.equalsIgnoreCase(str.substring(s,
									e + 1))) {
								str = str.replaceAll(str.substring(s, e + 1),
										"\n");
								continue;
							}
							str = str.substring(0, s) + str.substring(e + 1);

						}
						if (s == -1 || e == -1) {
							break;
						}
					}

					PdfPCell cell35 = new PdfPCell(new Paragraph(str,
							FontFactory.getFont(FontFactory.COURIER, 7)));
					cell35.setBorderWidth(0f);
					table5.addCell(cell35);

				}

				String Examination = eyeformConsultationReport.getExamination();
				if (!Examination.equals("")) {
					PdfPCell cell36 = new PdfPCell(new Paragraph(
							"Examination:", FontFactory.getFont(
									FontFactory.COURIER, 8, Font.BOLD)));
					cell36.setBorderWidth(0f);
					table5.addCell(cell36);
					String str = cp.getExamination();
					String testhuan = "<br>";
					char buf[] = str.toCharArray();
					int numhuan = 0;
					for (int i = 0; i < buf.length; i++) {
						if ("<".equalsIgnoreCase(buf[i] + "")) {
							numhuan++;
						}
					}
					for (int i = 0; i < numhuan; i++) {
						int s = str.indexOf("<");
						int e = str.indexOf(">");
						if (s != -1 && e != -1) {

							if (testhuan.equalsIgnoreCase(str.substring(s,
									e + 1))) {
								str = str.replaceAll(str.substring(s, e + 1),
										"\n");
								continue;
							}
							str = str.substring(0, s) + str.substring(e + 1);

						}
						if (s == -1 || e == -1) {
							break;
						}
					}

					PdfPCell cell37 = new PdfPCell(new Paragraph(str,
							FontFactory.getFont(FontFactory.COURIER, 7)));
					cell37.setBorderWidth(0f);
					table5.addCell(cell37);

				}

				String Impression = eyeformConsultationReport.getImpression();
				if (!Impression.equals("")) {
					PdfPCell cell38 = new PdfPCell(new Paragraph(
							"Impression/Plan:", FontFactory.getFont(
									FontFactory.COURIER, 8, Font.BOLD)));
					cell38.setBorderWidth(0f);
					table5.addCell(cell38);
					String impression = cp.getImpression() == null ? "" : cp
							.getImpression();

					String str = impression;
					String testhuan = "<br>";
					char buf[] = str.toCharArray();
					int numhuan = 0;
					for (int i = 0; i < buf.length; i++) {
						if ("<".equalsIgnoreCase(buf[i] + "")) {
							numhuan++;
						}
					}
					for (int i = 0; i < numhuan; i++) {
						int s = str.indexOf("<");
						int e = str.indexOf(">");
						if (s != -1 && e != -1) {

							if (testhuan.equalsIgnoreCase(str.substring(s,
									e + 1))) {
								str = str.replaceAll(str.substring(s, e + 1),
										"\n");
								continue;
							}
							str = str.substring(0, s) + str.substring(e + 1);

						}
						if (s == -1 || e == -1) {
							break;
						}
					}

					PdfPCell cell39 = new PdfPCell(new Paragraph(str,
							FontFactory.getFont(FontFactory.COURIER, 7)));
					cell39.setBorderWidth(0f);
					table5.addCell(cell39);

				}

				table5.setHorizontalAlignment(Element.ALIGN_LEFT);
				table5.setWidthPercentage(98);
				PdfPCell cell100 = new PdfPCell(table5);
				cell100.setBorderWidthTop(0f);
				cell100.setBorderWidthBottom(0f);
				table.addCell(cell100);

				PdfPCell cell40 = new PdfPCell(
						new Paragraph(
								"Thank you for allowing me to participate in the care of this patient.\nBest regards,\n\n\n",
								FontFactory.getFont(FontFactory.COURIER, 7)));
				cell40.setBorderWidthBottom(0f);
				//table6.addCell(cell40);
				table.addCell(cell40);
				
				PdfPCell cellTmp = new PdfPCell(new Paragraph("", FontFactory.getFont(FontFactory.COURIER, 7)));
				cellTmp.setBorderWidthBottom(0f);
				cellTmp.setBorderWidthTop(0f);
				//table6.addCell(cellTmp);
				table.addCell(cellTmp);
				
				String flag = request.getAttribute("providerflag") == null ? ""
						: (String) request.getAttribute("providerflag");
				if (flag.equals("false")) {
					PdfPCell cell42 = new PdfPCell(new Paragraph(
							"Associated with:" + "cp.provider",
							FontFactory.getFont(FontFactory.COURIER, 7)));
					cell42.setBorderWidthTop(0f);
					cell42.setBorderWidthBottom(0f);
					//table6.addCell(cell42);
					table.addCell(cell42);

				}

				String appointmentDoctor = (String) request
						.getAttribute("appointmentDoctor") == null ? ""
						: (String) request.getAttribute("appointmentDoctor");
				PdfPCell cell43 = new PdfPCell(new Paragraph(appointmentDoctor,
						FontFactory.getFont(FontFactory.COURIER, 7)));
				cell43.setBorderWidthTop(0f);
				cell43.setBorderWidthBottom(0f);
				//table6.addCell(cell43);
				table.addCell(cell43);

				//table6.setHorizontalAlignment(Element.ALIGN_LEFT);
				//table6.setWidthPercentage(98);
				//table.addCell(table6);
				
				cellTmp = new PdfPCell(new Paragraph("", FontFactory.getFont(FontFactory.COURIER, 7)));
				//cellTmp.setBorderWidth(0f);
				//cellTmp.setMinimumHeight(1f);
				cellTmp.setBorderWidthBottom(0f);
				//table6.addCell(cellTmp);
				table.addCell(cellTmp);

				cellTmp = new PdfPCell(new Paragraph("This information is direct in confidence solely to " +
						"the person named above and may not otherwise be distributed, copied or disclosed. " +
						"Therefore, this information should be considered strictly confidential.  " +
						"If you have received this telecopy in error, please notify us immediately by " +
						"telephone. Thank you for your assistance.", FontFactory.getFont(FontFactory.COURIER, 7)));
				cellTmp.setBorderWidthTop(0f);
				table.addCell(cellTmp);
				
				table.setWidthPercentage(80);
				table.setHorizontalAlignment(Element.ALIGN_LEFT);
				document.add(table);
				document.close();

			} catch (Throwable e) {
				MiscUtils.getLogger().error("Error", e);
			}
			
			return null;
			
		} else {
			try {
				String SIMPLE_SAMPLE_URI = props.getProperty("faxURI",
						"https://67.69.12.117:14043/OSCARFaxWebService");
				javax.xml.messaging.URLEndpoint endpoint = new javax.xml.messaging.URLEndpoint(SIMPLE_SAMPLE_URI);
				OSCARFAXClient OSFc = new OSCARFAXClient();
				OSCARFAXSOAPMessage OFSm = OSFc.createOSCARFAXSOAPMessage();
				String curUser_no = (String) request.getSession().getAttribute(
						"user");
				OFSm.setSendingProvider(curUser_no);
				String locationId = getLocationId();
				OFSm.setLocationId(locationId);
				String identifier = props.getProperty("faxIdentifier",
						"zwf4t%8*9@s");
				OFSm.setIdentifier(identifier);
				OFSm.setFaxType(OFSm.consultation);
				OFSm.setCoverSheet(true);
				OFSm.setComments("");

				java.util.Calendar calender = java.util.Calendar.getInstance();
				String day = Integer.toString(calender
						.get(java.util.Calendar.DAY_OF_MONTH));
				String mon = Integer.toString(calender
						.get(java.util.Calendar.MONTH) + 1);
				String year = Integer.toString(calender
						.get(java.util.Calendar.YEAR));
				String hourOfDay = Integer.toString(calender
						.get(java.util.Calendar.HOUR_OF_DAY));
				String minute = Integer.toString(calender
						.get(java.util.Calendar.MINUTE));
				String formattedDate = year + "/" + mon + "/" + day + "  "
						+ hourOfDay + ":" + minute;
				OFSm.setDateOfSending(formattedDate);

				SOAPElement payloadEle = OFSm.getPayLoad();
				SOAPElement conPacket;
				conPacket = payloadEle.addChildElement("conPacket");
				Object eyeformCReport = request.getAttribute("cp");
				EyeformConsultationReport eyeformConsultationReport = (EyeformConsultationReport) eyeformCReport;
				int number = eyeformConsultationReport.getReferralId() == 0 ? 0
						: eyeformConsultationReport.getReferralId();
				
				String nowtime = System.currentTimeMillis() + "";
				Object str1 = request.getAttribute("mdstring") == null ? ""
						: request.getAttribute("mdstring");
				/* Object str2 = request.getAttribute("internalDrName") == null ? ""
						: request.getAttribute("internalDrName");
				Object str3 = request.getAttribute("specialty") == null ? ""
						: request.getAttribute("specialty");
				*/
				Object str2 = request.getAttribute("appointmentDoctor") == null ? ""
						: request.getAttribute("appointmentDoctor");
				Object str3 = request.getAttribute("specialty_apptDoc") == null ? ""
						: request.getAttribute("specialty_apptDoc");
				
				OFSm.setFrom((String) str1 + (String) str2 + (String) str3);
				conPacket.addChildElement("from").addTextNode(
						replaceIllegalCharacters((String) str1 + (String) str2
								+ (String) str3));
				
				
				Clinic clinic1 = (Clinic) request.getAttribute("clinic");
				String clinicname = clinic1.getClinicName() == null ? ""
						: clinic1.getClinicName();

				conPacket.addChildElement("clinicName").addTextNode(
						replaceIllegalCharacters(clinicname));
				String cliniAddre = clinic1.getClinicAddress() == null ? ""
						: clinic1.getClinicAddress();
				String clinicCity = clinic1.getClinicCity() == null ? ""
						: clinic1.getClinicCity();
				String clinicProvince = clinic1.getClinicProvince() == null ? ""
						: clinic1.getClinicProvince();
				String clinicPostal = clinic1.getClinicPostal() == null ? ""
						: clinic1.getClinicPostal();

				conPacket.addChildElement("clinicAddress").addTextNode(
						replaceIllegalCharacters(cliniAddre + ", " + clinicCity
								+ ", " + clinicProvince + ", " + clinicPostal));

				boolean isMultiSites = props.getBooleanProperty("multisites",
						"on");
				String clicnicphone = clinic1.getClinicPhone() == null ? ""
						: clinic1.getClinicPhone();
				String ClinicFax = clinic1.getClinicFax() == null ? ""
						: clinic1.getClinicFax();

				if (!isMultiSites) {

					OFSm.setSendersFax(ClinicFax);
					OFSm.setSendersPhone(clicnicphone);
					conPacket.addChildElement("clinicTelephone").addTextNode(
							replaceIllegalCharacters(clicnicphone));
					conPacket.addChildElement("clinicFax").addTextNode(
							replaceIllegalCharacters(ClinicFax));
				} else {
					Clinic tmpCli = (Clinic) request.getAttribute("clinic");
					if (tmpCli != null) {
						SiteDao siteDao = (SiteDao) SpringUtils
								.getBean(SiteDao.class);
						Site site = siteDao.findByName(tmpCli.getClinicName());

						OFSm.setSendersFax(ClinicFax);
						OFSm.setSendersPhone(clicnicphone);
						conPacket.addChildElement("clinicTelephone")
								.addTextNode(
										replaceIllegalCharacters(clicnicphone));
						conPacket.addChildElement("clinicFax").addTextNode(
								replaceIllegalCharacters(ClinicFax));
					} else {

						OFSm.setSendersFax(ClinicFax);
						OFSm.setSendersPhone(clicnicphone);
						conPacket.addChildElement("clinicTelephone")
								.addTextNode(
										replaceIllegalCharacters(clicnicphone));
						conPacket.addChildElement("clinicFax").addTextNode(
								replaceIllegalCharacters(ClinicFax));
					}
				}

				String date = (String) request.getAttribute("date") == null ? ""
						: (String) request.getAttribute("date");

				conPacket.addChildElement("consultDate").addTextNode(
						replaceIllegalCharacters(date));

				Serializable refer1 = (Serializable) request
						.getAttribute("refer") == null ? ""
						: (Serializable) request.getAttribute("refer");
				ProfessionalSpecialist refer = (ProfessionalSpecialist) refer1;
				String lname = refer.getLastName() == null ? "" : refer
						.getLastName();
				String fname = refer.getFirstName() == null ? "" : refer
						.getFirstName();

				OFSm.setRecipient(lname + ", " + fname);
				conPacket.addChildElement("consultantName").addTextNode(
						replaceIllegalCharacters(lname + ", " + fname));
				String raddress = refer.getStreetAddress() == null ? "" : refer
						.getStreetAddress();

				conPacket.addChildElement("consultantAddress").addTextNode(
						replaceIllegalCharactersAmps(raddress));
				String rphone = refer.getPhoneNumber() == null ? "" : refer
						.getPhoneNumber();

				conPacket.addChildElement("consultantPhone").addTextNode(
						replaceIllegalCharacters(rphone));

				String rfax = refer.getFaxNumber() == null ? "" : refer
						.getFaxNumber();

				OFSm.setRecipientFaxNumber(rfax);
				conPacket.addChildElement("consultantFax").addTextNode(
						replaceIllegalCharacters(rfax));

				conPacket.addChildElement("CC").addTextNode(
						replaceIllegalCharacters(cp.getCc()));

				conPacket.addChildElement("Re").addTextNode(
						replaceIllegalCharacters(cp.getReason()));

				Serializable demographic1 = (Serializable) request
						.getAttribute("demographic") == null ? ""
						: (Serializable) request.getAttribute("demographic");
				Demographic demographic2 = (Demographic) demographic1;
				String dlname = demographic2.getLastName() == null ? ""
						: demographic2.getLastName();
				String dfname = demographic2.getFirstName() == null ? ""
						: demographic2.getFirstName();

				conPacket.addChildElement("patientName").addTextNode(
						replaceIllegalCharacters(dlname + "," + dfname));

				String address = demographic.getAddress() == null ? ""
						: demographic.getAddress();
				if (!address.equals("")) {
					address += ",";
				}
				String city = demographic.getCity() == null ? "" : demographic
						.getCity();
				if (!city.equals("")) {
					city += ",";
				}
				String province = demographic.getProvince() == null ? ""
						: demographic.getProvince();
				if (!province.equals("")) {
					province += ",";
				}
				String postal = demographic.getPostal() == null ? ""
						: demographic.getPostal();

				conPacket.addChildElement("consultantAddress").addTextNode(
						replaceIllegalCharactersAmps(address + city + province
								+ postal));

				String phone = demographic.getPhone() == null ? ""
						: demographic.getPhone();

				conPacket.addChildElement("patientTelephone").addTextNode(
						replaceIllegalCharacters(phone));

				String yearOfBirth = demographic.getYearOfBirth() == null ? ""
						: demographic.getYearOfBirth();
				String monthOfBirth = demographic.getMonthOfBirth() == null ? ""
						: demographic.getMonthOfBirth();
				String dateOfBirth = demographic.getDateOfBirth() == null ? ""
						: demographic.getDateOfBirth();

				conPacket.addChildElement("patientBirthdate").addTextNode(
						replaceIllegalCharacters(yearOfBirth + "/"
								+ monthOfBirth + "/" + dateOfBirth
								+ "\n(y/m/d)"));

				String hin = demographic.getHin() == null ? "" : demographic
						.getHin();
				String ver = demographic.getVer() == null ? "" : demographic
						.getVer();

				conPacket.addChildElement("healthCardNo").addTextNode(
						replaceIllegalCharacters(hin + " " + ver));

				String rlname = refer.getLastName() == null ? "" : refer
						.getLastName();

				String letter = "";

				int eyeformCReportvalue = eyeformConsultationReport
						.getGreeting();
				if (eyeformCReportvalue == 1) {
					String age = demographic.getAge() == null ? ""
							: demographic.getAge();
					letter += "I had the pleasure of seeing " + age
							+ " year old " + dlname + "," + dfname;
					Object appointdate = request.getAttribute("appointDate");
					if (appointdate != null) {
						letter += " on " + appointdate;
					}
					letter += " on your kind referral.";

					conPacket.addChildElement("letterContext").addTextNode(
							"Dear Dr." + rlname + "\n" + letter);
				}
				if (eyeformCReportvalue == 2) {
					String age = demographic.getAge() == null ? ""
							: demographic.getAge();
					letter += "This is a report on my most recent assessment of "
							+ age + " year old " + dlname + "," + dfname;
					Object appointdate = request.getAttribute("appointDate");
					if (appointdate != null) {
						letter += " on " + appointdate;
					}
					letter += ".";

					conPacket.addChildElement("letterContext").addTextNode(
							replaceIllegalCharacters("Dear Dr." + rlname + "\n"
									+ letter));
				}

				String ClinicalInfo = eyeformConsultationReport
						.getClinicalInfo();
				if (!ClinicalInfo.equals("")) {

					String clinicalinfo = cp.getClinicalInfo() == null ? ""
							: cp.getClinicalInfo();

					String str = clinicalinfo;
					String testhuan = "<br>";
					char buf[] = str.toCharArray();
					int numhuan = 0;
					for (int i = 0; i < buf.length; i++) {
						if ("<".equalsIgnoreCase(buf[i] + "")) {
							numhuan++;
						}
					}
					for (int i = 0; i < numhuan; i++) {
						int s = str.indexOf("<");
						int e = str.indexOf(">");
						if (s != -1 && e != -1) {

							if (testhuan.equalsIgnoreCase(str.substring(s,
									e + 1))) {
								str = str.replaceAll(str.substring(s, e + 1),
										"\n");
								continue;
							}
							str = str.substring(0, s) + str.substring(e + 1);

						}
						if (s == -1 || e == -1) {
							break;
						}
					}

					conPacket.addChildElement("pertinentClinicalInformation")
							.addTextNode(replaceIllegalCharacters(str));
				}

				String Allergies = eyeformConsultationReport.getAllergies();
				if (!Allergies.equals("")) {

					String allergies = cp.getAllergies() == null ? "" : cp
							.getAllergies();

					String str = allergies;
					String testhuan = "<br>";
					char buf[] = str.toCharArray();
					int numhuan = 0;
					for (int i = 0; i < buf.length; i++) {
						if ("<".equalsIgnoreCase(buf[i] + "")) {
							numhuan++;
						}
					}
					for (int i = 0; i < numhuan; i++) {
						int s = str.indexOf("<");
						int e = str.indexOf(">");
						if (s != -1 && e != -1) {

							if (testhuan.equalsIgnoreCase(str.substring(s,
									e + 1))) {
								str = str.replaceAll(str.substring(s, e + 1),
										"\n");
								continue;
							}
							str = str.substring(0, s) + str.substring(e + 1);

						}
						if (s == -1 || e == -1) {
							break;
						}
					}

					conPacket.addChildElement("AllergiesAndMedications")
							.addTextNode(replaceIllegalCharacters(str));
				}

				String Examination = eyeformConsultationReport.getExamination();
				if (!Examination.equals("")) {

					String str = cp.getExamination();
					String testhuan = "<br>";
					char buf[] = str.toCharArray();
					int numhuan = 0;
					for (int i = 0; i < buf.length; i++) {
						if ("<".equalsIgnoreCase(buf[i] + "")) {
							numhuan++;
						}
					}
					for (int i = 0; i < numhuan; i++) {
						int s = str.indexOf("<");
						int e = str.indexOf(">");
						if (s != -1 && e != -1) {

							if (testhuan.equalsIgnoreCase(str.substring(s,
									e + 1))) {
								str = str.replaceAll(str.substring(s, e + 1),
										"\n");
								continue;
							}
							str = str.substring(0, s) + str.substring(e + 1);

						}
						if (s == -1 || e == -1) {
							break;
						}
					}

					conPacket.addChildElement("Examination").addTextNode(
							replaceIllegalCharacters(str));
				}

				String Impression = eyeformConsultationReport.getImpression();
				if (!Impression.equals("")) {

					String impression = cp.getImpression() == null ? "" : cp
							.getImpression();

					String str = impression;
					String testhuan = "<br>";
					char buf[] = str.toCharArray();
					int numhuan = 0;
					for (int i = 0; i < buf.length; i++) {
						if ("<".equalsIgnoreCase(buf[i] + "")) {
							numhuan++;
						}
					}
					for (int i = 0; i < numhuan; i++) {
						int s = str.indexOf("<");
						int e = str.indexOf(">");
						if (s != -1 && e != -1) {

							if (testhuan.equalsIgnoreCase(str.substring(s,
									e + 1))) {
								str = str.replaceAll(str.substring(s, e + 1),
										"\n");
								continue;
							}
							str = str.substring(0, s) + str.substring(e + 1);

						}
						if (s == -1 || e == -1) {
							break;
						}
					}

					conPacket.addChildElement("ImpressionPlan").addTextNode(
							replaceIllegalCharacters(str));
				}

				conPacket
						.addChildElement("thank")
						.addTextNode(
								replaceIllegalCharacters("Thank you for allowing me to participate in the care of this patient.\n Best regards,"));
				String flag = request.getAttribute("providerflag") == null ? ""
						: (String) request.getAttribute("providerflag");
				if (flag.equals("false")) {

					conPacket.addChildElement("associatedWith").addTextNode(
							replaceIllegalCharacters("cp.provider"));
				}

				String appointmentDoctor = (String) request
						.getAttribute("appointmentDoctor") == null ? ""
						: (String) request.getAttribute("appointmentDoctor");

				conPacket.addChildElement("appointmentDoctor").addTextNode(
						replaceIllegalCharacters(appointmentDoctor));

				OFSm.save();
				FaxClientLog faxClientLog = new FaxClientLog();
				faxClientLog.setProviderNo(curUser_no);
				faxClientLog.setStartTime(new Date());
				FaxClientLogDao faxDao = (FaxClientLogDao) SpringUtils
						.getBean("faxClientLogDao");
				// faxDao.persist(faxClientLog);
				boolean reply = OSFc.sendMessage(OFSm, endpoint);

				try {
					if (reply) {
						MiscUtils.getLogger()
								.debug("Job Id " + OSFc.getJobId());
						request.setAttribute("jobId", OSFc.getJobId());
						MiscUtils.getLogger().debug(
								"Request Id " + OSFc.getRequestId());
						request.setAttribute("requestId", OSFc.getRequestId());
						faxClientLog.setRequestId(OSFc.getRequestId());
						faxClientLog.setFaxId(OSFc.getJobId());
						// faxDao.merge(faxClientLog);
					} else {
						MiscUtils.getLogger().debug(
								"Error Message " + OSFc.getErrorMessage());
						request.setAttribute("oscarFaxError",
								OSFc.getErrorMessage());
						faxClientLog.setResult(OSFc.getErrorMessage());
						faxClientLog.setEndTime(new Date());
						// faxDao.merge(faxClientLog);
					}
				} catch (Exception e4) {
					MiscUtils.getLogger().error("Error", e4);
					MiscUtils.getLogger().debug(
							"Fax Service has Returned a Fatal Error ");
					request.setAttribute(
							"oscarFaxError",
							"Fax Service Is currently not available, please contact your Oscar Fax Administrator");
					faxClientLog.setResult("FAX SERVICE RETURNED NULL");
					faxClientLog.setEndTime(new Date());
					// faxDao.merge(faxClientLog);
				}

			} catch (Throwable e) {
				MiscUtils.getLogger().error("Error", e);
			}

			MiscUtils.getLogger().debug("Client Has Finished Running");
			
			return mapping.findForward("faxWS");
		}
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
			
			LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
			
			String demoNo = request.getParameter("demographicNo");
			String docFlag = request.getParameter("docFlag");
			String bsurl = request.getContextPath();
			if ("true".equalsIgnoreCase(docFlag))
				sendDocTickler(loggedInInfo, "REP", demoNo, (String) request.getSession().getAttribute("user"), bsurl);

			response.getWriter().println("alert('tickler sent');");
			return null;
		}

		public void sendDocTickler(LoggedInInfo loggedInInfo, String flag, String demoNo, String providerNo, String bsurl) {
			Tickler tkl = new Tickler();
			Date now = new Date();

			tkl.setCreator(providerNo);
			tkl.setDemographicNo(Integer.valueOf(demoNo));
			tkl.setPriorityAsString("Normal");
			tkl.setServiceDate(now);
			tkl.setStatusAsChar('A');
			tkl.setTaskAssignedTo(providerNo);
			tkl.setUpdateDate(now);

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
			ticklerManager.addTickler(loggedInInfo, tkl);
		}

		public void sendFrontTickler(LoggedInInfo loggedInInfo, String flag, String demoNo, String providerNo, String creator, String bsurl) {
			Tickler tkl = new Tickler();
			tkl.setCreator(creator);
			tkl.setDemographicNo(Integer.parseInt(demoNo));
			tkl.setTaskAssignedTo(providerNo);
			
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
			ticklerManager.addTickler(loggedInInfo, tkl);
		}

	public String wrap(String in, int len) {
		if (in == null)
			in = "";
		// in=in.trim();
		if (in.length() < len) {
			if (in.length() > 1 && !in.startsWith("  ")) {
				in = in.trim();
			}
			return in;
		}
		if (in.substring(0, len).contains("\n")) {
			String x = in.substring(0, in.indexOf("\n"));
			if (x.length() > 1 && !x.startsWith("  ")) {
				x = x.trim();
			}
			return x + "\n" + wrap(in.substring(in.indexOf("\n") + 1), len);
		}
		int place = Math.max(
				Math.max(in.lastIndexOf(" ", len), in.lastIndexOf("\t", len)),
				in.lastIndexOf("-", len));
		return in.substring(0, place).trim() + "\n"
				+ wrap(in.substring(place), len);
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
		oscar.OscarProperties props1 = oscar.OscarProperties.getInstance();
		String eyeform = props1.getProperty("cme_js");
		if (("eyeform3".equals(eyeform)) || ("eyeform3.1".equals(eyeform))
				|| ("eyeform3.2".equals(eyeform))) {
			sections.add(new LabelValueBean("GLASSES HISTORY",
					"GLASSES HISTORY"));
			sections.add(new LabelValueBean("VISION ASSESSMENT",
					"VISION ASSESSMENT"));
			sections.add(new LabelValueBean("VISION MEASUREMENT",
					"VISION MEASUREMENT"));
			sections.add(new LabelValueBean("STEREO VISION", "STEREO VISION"));
			sections.add(new LabelValueBean("INTRAOCULAR PRESSURE",
					"INTRAOCULAR PRESSURE"));
			sections.add(new LabelValueBean("REFRACTIVE", "REFRACTIVE"));
			sections.add(new LabelValueBean("OTHER EXAM", "OTHER EXAM"));
			sections.add(new LabelValueBean("DUCTION/DIPLOPIA TESTING",
					"DUCTION/DIPLOPIA TESTING"));
			sections.add(new LabelValueBean("DEVIATION MEASUREMENT",
					"DEVIATION MEASUREMENT"));
			sections.add(new LabelValueBean("EXTERNAL/ORBIT", "EXTERNAL/ORBIT"));
			sections.add(new LabelValueBean("EYELID/NASOLACRIMAL DUCT",
					"EYELID/NASOLACRIMAL DUCT"));
			sections.add(new LabelValueBean("EYELID MEASUREMENT",
					"EYELID MEASUREMENT"));
			sections.add(new LabelValueBean("ANTERIOR SEGMENT",
					"ANTERIOR SEGMENT"));
			sections.add(new LabelValueBean("POSTERIOR SEGMENT",
					"POSTERIOR SEGMENT"));
		} else {
			sections.add(new LabelValueBean("VISION ASSESSMENT",
					"VISION ASSESSMENT"));
			sections.add(new LabelValueBean("MANIFEST VISION",
					"MANIFEST VISION"));
			sections.add(new LabelValueBean("INTRAOCULAR PRESSURE",
					"INTRAOCULAR PRESSURE"));
			sections.add(new LabelValueBean("OTHER EXAM", "OTHER EXAM"));
			sections.add(new LabelValueBean("EOM/STEREO", "EOM/STEREO"));
			sections.add(new LabelValueBean("ANTERIOR SEGMENT",
					"ANTERIOR SEGMENT"));
			sections.add(new LabelValueBean("POSTERIOR SEGMENT",
					"POSTERIOR SEGMENT"));
			sections.add(new LabelValueBean("EXTERNAL/ORBIT", "EXTERNAL/ORBIT"));
			sections.add(new LabelValueBean("NASOLACRIMAL DUCT",
					"NASOLACRIMAL DUCT"));
			sections.add(new LabelValueBean("EYELID MEASUREMENT",
					"EYELID MEASUREMENT"));
		}
		return sections;
	}

	public static List<LabelValueBean> getMeasurementHeaders() {
		List<LabelValueBean> sections = new ArrayList<LabelValueBean>();
		oscar.OscarProperties props1 = oscar.OscarProperties.getInstance();
		String eyeform = props1.getProperty("cme_js");
		if (("eyeform3".equals(eyeform)) || ("eyeform3.1".equals(eyeform))
				|| ("eyeform3.2".equals(eyeform))) {
			sections.add(new LabelValueBean("Glasses Rx", "Glasses Rx"));
			sections.add(new LabelValueBean("Distance vision (sc)",
					"Distance vision (sc)"));
			sections.add(new LabelValueBean("Distance vision (cc)",
					"Distance vision (cc)"));
			sections.add(new LabelValueBean("Distance vision (ph)",
					"Distance vision (ph)"));
			sections.add(new LabelValueBean("Intermediate vision (sc)",
					"Intermediate vision (sc)"));
			sections.add(new LabelValueBean("Intermediate vision (cc)",
					"Intermediate vision (cc)"));
			sections.add(new LabelValueBean("Near vision (sc)",
					"Near vision (sc)"));
			sections.add(new LabelValueBean("Near vision (cc)",
					"Near vision (cc)"));
			sections.add(new LabelValueBean("Fly test", "Fly test"));
			sections.add(new LabelValueBean("Stereo-acuity", "Stereo-acuity"));
			sections.add(new LabelValueBean("Keratometry", "Keratometry"));
			sections.add(new LabelValueBean("Auto-refraction",
					"Auto-refraction"));
			sections.add(new LabelValueBean("Manifest distance",
					"Manifest distance"));
			sections.add(new LabelValueBean("Manifest near", "Manifest near"));
			sections.add(new LabelValueBean("Cycloplegic refraction",
					"Cycloplegic refraction"));
			sections.add(new LabelValueBean("NCT", "NCT"));
			sections.add(new LabelValueBean("Applanation", "Applanation"));
			sections.add(new LabelValueBean("Central corneal thickness",
					"Central corneal thickness"));
			sections.add(new LabelValueBean("Dominance", "Dominance"));
			sections.add(new LabelValueBean("Mesopic pupil size",
					"Mesopic pupil size"));
			sections.add(new LabelValueBean("Angle Kappa", "Angle Kappa"));
			sections.add(new LabelValueBean("Colour vision", "Colour vision"));
			sections.add(new LabelValueBean("Pupil", "Pupil"));
			sections.add(new LabelValueBean("Amsler grid", "Amsler grid"));
			sections.add(new LabelValueBean("Potential acuity meter",
					"Potential acuity meter"));
			sections.add(new LabelValueBean("Confrontation fields",
					"Confrontation fields"));
			sections.add(new LabelValueBean("Maddox rod", "Maddox rod"));
			sections.add(new LabelValueBean("Bagolini test", "Bagolini test"));
			sections.add(new LabelValueBean("Worth 4 Dot (distance)",
					"Worth 4 Dot (distance)"));
			sections.add(new LabelValueBean("Worth 4 Dot (near)",
					"Worth 4 Dot (near)"));
			sections.add(new LabelValueBean("DUCTION/DIPLOPIA TESTING",
					"DUCTION/DIPLOPIA TESTING"));
			sections.add(new LabelValueBean("Primary gaze", "Primary gaze"));
			sections.add(new LabelValueBean("Up gaze", "Up gaze"));
			sections.add(new LabelValueBean("Down gaze", "Down gaze"));
			sections.add(new LabelValueBean("Right gaze", "Right gaze"));
			sections.add(new LabelValueBean("Left gaze", "Left gaze"));
			sections.add(new LabelValueBean("Right head tilt",
					"Right head tilt"));
			sections.add(new LabelValueBean("Left head tilt", "Left head tilt"));
			sections.add(new LabelValueBean("Near", "Near"));
			sections.add(new LabelValueBean("Near with +3D add",
					"Near with +3D add"));
			sections.add(new LabelValueBean("Far distance", "Far distance"));
			sections.add(new LabelValueBean("Face", "Face"));
			sections.add(new LabelValueBean("Retropulsion", "Retropulsion"));
			sections.add(new LabelValueBean("Hertel", "Hertel"));
			sections.add(new LabelValueBean("Upper lid", "Upper lid"));
			sections.add(new LabelValueBean("Lower lid", "Lower lid"));
			sections.add(new LabelValueBean("Lacrimal lake", "Lacrimal lake"));
			sections.add(new LabelValueBean("Lacrimal irrigation",
					"Lacrimal irrigation"));
			sections.add(new LabelValueBean("Punctum", "Punctum"));
			sections.add(new LabelValueBean("Nasolacrimal duct",
					"Nasolacrimal duct"));
			sections.add(new LabelValueBean("Dye disappearance",
					"Dye disappearance"));
			sections.add(new LabelValueBean("Margin reflex distance",
					"Margin reflex distance"));
			sections.add(new LabelValueBean("Inferior scleral show",
					"Inferior scleral show"));
			sections.add(new LabelValueBean("Levator function",
					"Levator function"));
			sections.add(new LabelValueBean("Lagophthalmos", "Lagophthalmos"));
			sections.add(new LabelValueBean("Blink reflex", "Blink reflex"));
			sections.add(new LabelValueBean("Cranial Nerve VII function",
					"Cranial Nerve VII function"));
			sections.add(new LabelValueBean("Bell's phenomenon",
					"Bells phenomenon"));
			sections.add(new LabelValueBean("Schirmer test", "Schirmer test"));
			sections.add(new LabelValueBean("Cornea", "Cornea"));
			sections.add(new LabelValueBean("Conjunctiva/Sclera",
					"Conjunctiva/Sclera"));
			sections.add(new LabelValueBean("Anterior chamber",
					"Anterior chamber"));
			sections.add(new LabelValueBean("Angle", "Angle"));
			sections.add(new LabelValueBean("Iris", "Iris"));
			sections.add(new LabelValueBean("Lens", "Lens"));
			sections.add(new LabelValueBean("Optic disc", "Optic disc"));
			sections.add(new LabelValueBean("C/D ratio", "C/D ratio"));
			sections.add(new LabelValueBean("Macula", "Macula"));
			sections.add(new LabelValueBean("Retina", "Retina"));
			sections.add(new LabelValueBean("Vitreous", "Vitreous"));
		} else {
			sections.add(new LabelValueBean("Auto-refraction",
					"Auto-refraction"));
			sections.add(new LabelValueBean("Keratometry", "Keratometry"));
			sections.add(new LabelValueBean("Distance vision (sc)",
					"Distance vision (sc)"));
			sections.add(new LabelValueBean("Distance vision (cc)",
					"Distance vision (cc)"));
			sections.add(new LabelValueBean("Distance vision (ph)",
					"Distance vision (ph)"));
			sections.add(new LabelValueBean("Near vision (sc)",
					"Near vision (sc)"));
			sections.add(new LabelValueBean("Near vision (cc)",
					"Near vision (cc)"));

			sections.add(new LabelValueBean("Manifest distance",
					"Manifest distance"));
			sections.add(new LabelValueBean("Manifest near", "Manifest near"));
			sections.add(new LabelValueBean("Cycloplegic refraction",
					"Cycloplegic refraction"));
			// sections.add(new
			// LabelValueBean("Best corrected distance vision","Best corrected distance vision"));

			sections.add(new LabelValueBean("NCT", "NCT"));
			sections.add(new LabelValueBean("Applanation", "Applanation"));
			sections.add(new LabelValueBean("Central corneal thickness",
					"Central corneal thickness"));

			sections.add(new LabelValueBean("Colour vision", "Colour vision"));
			sections.add(new LabelValueBean("Pupil", "Pupil"));
			sections.add(new LabelValueBean("Amsler grid", "Amsler grid"));
			sections.add(new LabelValueBean("Potential acuity meter",
					"Potential acuity meter"));
			sections.add(new LabelValueBean("Confrontation fields",
					"Confrontation fields"));
			// sections.add(new LabelValueBean("Maddox rod","Maddox rod"));
			// sections.add(new
			// LabelValueBean("Bagolini test","Bagolini test"));
			// sections.add(new
			// LabelValueBean("Worth 4 dot (distance)","Worth 4 dot (distance)"));
			// sections.add(new
			// LabelValueBean("Worth 4 dot (near)","Worth 4 dot (near)"));
			sections.add(new LabelValueBean("EOM", "EOM"));

			sections.add(new LabelValueBean("Cornea", "Cornea"));
			sections.add(new LabelValueBean("Conjunctiva/Sclera",
					"Conjunctiva/Sclera"));
			sections.add(new LabelValueBean("Anterior chamber",
					"Anterior chamber"));
			sections.add(new LabelValueBean("Angle", "Angle"));
			sections.add(new LabelValueBean("Iris", "Iris"));
			sections.add(new LabelValueBean("Lens", "Lens"));

			sections.add(new LabelValueBean("Optic disc", "Optic disc"));
			sections.add(new LabelValueBean("C/D ratio", "C/D ratio"));
			sections.add(new LabelValueBean("Macula", "Macula"));
			sections.add(new LabelValueBean("Retina", "Retina"));
			sections.add(new LabelValueBean("Vitreous", "Vitreous"));

			sections.add(new LabelValueBean("Face", "Face"));
			sections.add(new LabelValueBean("Upper lid", "Upper lid"));
			sections.add(new LabelValueBean("Lower lid", "Lower lid"));
			sections.add(new LabelValueBean("Punctum", "Punctum"));
			sections.add(new LabelValueBean("Lacrimal lake", "Lacrimal lake"));
			// sections.add(new
			// LabelValueBean("Schirmer test","Schirmer test"));
			sections.add(new LabelValueBean("Retropulsion", "Retropulsion"));
			sections.add(new LabelValueBean("Hertel", "Hertel"));

			sections.add(new LabelValueBean("Lacrimal irrigation",
					"Lacrimal irrigation"));
			sections.add(new LabelValueBean("Nasolacrimal duct",
					"Nasolacrimal duct"));
			sections.add(new LabelValueBean("Dye disappearance",
					"Dye disappearance"));

			sections.add(new LabelValueBean("Margin reflex distance",
					"Margin reflex distance"));
			sections.add(new LabelValueBean("Inferior scleral show",
					"Inferior scleral show"));
			sections.add(new LabelValueBean("Levator function",
					"Levator function"));
			sections.add(new LabelValueBean("Lagophthalmos", "Lagophthalmos"));
			sections.add(new LabelValueBean("Blink reflex", "Blink reflex"));
			sections.add(new LabelValueBean("Cranial nerve VII function",
					"Cranial nerve VII function"));
			sections.add(new LabelValueBean("Bell's phenomenon",
					"Bells phenomenon"));
		}
		return sections;
	}

	public ActionForward getMeasurementText(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String[] values = request.getParameterValues(request
				.getParameter("name"));
		String appointmentNo = request.getParameter("appointmentNo");
		StringBuilder exam = new StringBuilder();
		Map<String, Boolean> headerMap = new HashMap<String, Boolean>();
		for (int x = 0; x < values.length; x++) {
			headerMap.put(values[x], true);
		}

		if (!StringUtils.isBlank(appointmentNo)
				&& !appointmentNo.equalsIgnoreCase("null")) {
			List<Measurement> measurements = measurementDao.getMeasurementsByAppointment2(Integer.parseInt(appointmentNo));
			MeasurementFormatter formatter = new MeasurementFormatter(measurements);

			String tmp = null;
			oscar.OscarProperties props1 = oscar.OscarProperties.getInstance();
			String eyeform = props1.getProperty("cme_js");
			if (("eyeform3".equals(eyeform)) || ("eyeform3.1".equals(eyeform)) || ("eyeform3.2".equals(eyeform))) {
				tmp = formatter.getGlasseshistory(headerMap,Integer.parseInt(appointmentNo));
				exam.append(tmp);

				tmp = formatter.getVisionAssessment(headerMap);
				if (exam.length() > 0 && tmp.length() > 0) {
					exam.append("\n\n");
				}
				exam.append(tmp);

				tmp = formatter.getManifestVision(headerMap);
				if (exam.length() > 0 && tmp.length() > 0) {
					exam.append("\n\n");
				}
				exam.append(tmp);

				tmp = formatter.getStereoVision(headerMap);
				if (exam.length() > 0 && tmp.length() > 0) {
					exam.append("\n\n");
				}
				exam.append(tmp);

				tmp = formatter.getRactive(headerMap);
				if (exam.length() > 0 && tmp.length() > 0) {
					exam.append("\n\n");
				}
				exam.append(tmp);

				tmp = formatter.getIntraocularPressure(headerMap);
				if (exam.length() > 0 && tmp.length() > 0) {
					exam.append("\n\n");
				}
				exam.append(tmp);

				tmp = formatter.getOtherExam(headerMap);
				if (exam.length() > 0 && tmp.length() > 0) {
					exam.append("\n\n");
				}
				exam.append(tmp);

				tmp = formatter.getDuctionTesting(headerMap);
				if (exam.length() > 0 && tmp.length() > 0) {
					exam.append("\n\n");
				}
				exam.append(tmp);

				tmp = formatter.getDeviationMeasurement(headerMap);
				if (exam.length() > 0 && tmp.length() > 0) {
					exam.append("\n\n");
				}
				exam.append(tmp);

				tmp = formatter.getExternalOrbit(headerMap);
				if (exam.length() > 0 && tmp.length() > 0) {
					exam.append("\n\n");
				}
				exam.append(tmp);

				tmp = formatter.getEyelidDuct(headerMap);
				if (exam.length() > 0 && tmp.length() > 0) {
					exam.append("\n\n");
				}
				exam.append(tmp);

				tmp = formatter.getEyelidMeasurement(headerMap);
				if (exam.length() > 0 && tmp.length() > 0) {
					exam.append("\n\n");
				}
				exam.append(tmp);

				tmp = formatter.getAnteriorSegment(headerMap);
				if (exam.length() > 0 && tmp.length() > 0) {
					exam.append("\n\n");
				}
				exam.append(tmp);

				tmp = formatter.getPosteriorSegment(headerMap);
				if (exam.length() > 0 && tmp.length() > 0) {
					exam.append("\n\n");
				}
				exam.append(tmp);

				String new_exam = "";
				// new_exam = divy(wrap(exam.toString(),150));

				new_exam = divy(exam.toString());
				new_exam = new_exam.replaceAll("\n", "");
				// new_exam = new_exam.replaceAll(" ", "&nbsp;");

				HttpSession session = request.getSession();
				session.setAttribute("examination", exam.toString());

				// response.getWriter().println(exam.toString());
				response.getWriter().println(new_exam);
			} else {
				exam.append(formatter.getVisionAssessment(headerMap));

				tmp = formatter.getManifestVision(headerMap);
				if (exam.length() > 0 && tmp.length() > 0) {
					exam.append("\n\n");
				}
				exam.append(tmp);
				tmp = formatter.getIntraocularPressure(headerMap);
				if (exam.length() > 0 && tmp.length() > 0) {
					exam.append("\n\n");
				}
				exam.append(tmp);

				tmp = formatter.getOtherExam(headerMap);
				if (exam.length() > 0 && tmp.length() > 0) {
					exam.append("\n\n");
				}
				exam.append(tmp);

				tmp = formatter.getEOMStereo(headerMap);
				if (exam.length() > 0 && tmp.length() > 0) {
					exam.append("\n\n");
				}
				exam.append(tmp);

				tmp = formatter.getAnteriorSegment(headerMap);
				if (exam.length() > 0 && tmp.length() > 0) {
					exam.append("\n\n");
				}
				exam.append(tmp);

				tmp = formatter.getPosteriorSegment(headerMap);
				if (exam.length() > 0 && tmp.length() > 0) {
					exam.append("\n\n");
				}
				exam.append(tmp);

				tmp = formatter.getExternalOrbit(headerMap);
				if (exam.length() > 0 && tmp.length() > 0) {
					exam.append("\n\n");
				}
				exam.append(tmp);

				tmp = formatter.getNasalacrimalDuct(headerMap);
				if (exam.length() > 0 && tmp.length() > 0) {
					exam.append("\n\n");
				}
				exam.append(tmp);

				tmp = formatter.getEyelidMeasurement(headerMap);
				if (exam.length() > 0 && tmp.length() > 0) {
					exam.append("\n\n");
				}
				exam.append(tmp);

				response.getWriter().println(exam.toString());

			}
		}
		return null;
	}

		public static List<Provider> getActiveProviders() {
			ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
			return providerDao.getActiveProviders();
		}

		public ActionForward specialReqTickler(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
			log.debug("specialReqTickler");

			LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);

			String demoNo = request.getParameter("demographicNo");
			String docFlag = request.getParameter("docFlag");
			String frontFlag = request.getParameter("frontFlag");
			String providerNo = request.getParameter("providerNo");
			String bsurl = request.getContextPath();
			String user = loggedInInfo.getLoggedInProviderNo();
			if ("true".equalsIgnoreCase(docFlag))
				sendDocTickler(loggedInInfo,"REQ", demoNo,user, bsurl);
			if ("true".equalsIgnoreCase(frontFlag))
				sendFrontTickler(loggedInInfo,"REQ", demoNo, providerNo,user, bsurl);
			response.getWriter().println("alert('tickler sent');");
			return null;
		}

	String getLocationId() {
		OscarCommLocationsDao dao = (OscarCommLocationsDao) SpringUtils.getBean("oscarCommLocationDao");
		List<OscarCommLocations> list = dao.findByCurrent1(1);
		if(list.size()>0) {
			return String.valueOf(list.get(0).getId());
		} else {
			return "";
		}		
	}

	String replaceIllegalCharacters(String str) {
		return str.replaceAll("&", "&amp;").replaceAll(">", "&gt;")
				.replaceAll("<", "&lt;");
	}

	String replaceIllegalCharactersAmps(String str) {
		return str.replaceAll("&", "&amp;");
	}
}
