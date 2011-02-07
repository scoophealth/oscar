package org.oscarehr.eyeform.web;

import java.text.SimpleDateFormat;
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
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.dao.ConsultationRequestExtDao;
import org.oscarehr.common.dao.ProfessionalSpecialistDao;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.Provider;
import org.oscarehr.eyeform.dao.EyeFormDao;
import org.oscarehr.eyeform.dao.FollowUpDao;
import org.oscarehr.eyeform.dao.OcularProcDao;
import org.oscarehr.eyeform.dao.ProcedureBookDao;
import org.oscarehr.eyeform.dao.SpecsHistoryDao;
import org.oscarehr.eyeform.dao.TestBookRecordDao;
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

public class EyeformAction extends DispatchAction {

	static Logger logger = MiscUtils.getLogger();
	
	CaseManagementManager cmm;
	  
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
		   
		   String specialProblem = "";
		   if(requestId>0) {
			   specialProblem = consultationRequestExtDao.getConsultationRequestExtsByKey(requestId, "specialProblem");			   
		   }
		   request.setAttribute("ext_specialProblem", specialProblem);
		   
		   request.setAttribute("currentHistory",getCppItemAsString(demo,"CurrentHistory","Current History:"));
		   request.setAttribute("ocularMedication",getCppItemAsString(demo,"OcularMedication","Current Medications:"));
		   request.setAttribute("pastOcularHistory",getCppItemAsString(demo,"PastOcularHistory","Past Ocular History:"));
		   request.setAttribute("diagnosticNotes",getCppItemAsString(demo,"DiagnosticNotes","Diagnostic Notes:"));

		   
		   SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		   List<OcularProc> ocularProcs = ocularProcDao.getHistory(Integer.parseInt(demo), new Date(), "A");
		   StringBuffer ocularProc = new StringBuffer();
		   for(OcularProc op:ocularProcs) {
               ocularProc.append(sf.format(op.getDate()) + " ");
               ocularProc.append(op.getEye() + " ");
               ocularProc
                               .append(op.getProcedureName() + " at " + op.getLocation());
               ocularProc.append(" by " + op.getDoctor());
               if (op.getProcedureNote() != null
                               && !"".equalsIgnoreCase(op.getProcedureNote().trim()))
                       ocularProc.append(". " + op.getProcedureNote() + "\n");
			   
		   }
           String strOcularProcs = ocularProc.toString();
           if (strOcularProcs != null && !"".equalsIgnoreCase(strOcularProcs.trim()))
        	   strOcularProcs = "Past Ocular Proc:\n" + ocularProcs + "\n";
           else
        	   strOcularProcs = "";
           
           strOcularProcs = StringEscapeUtils.escapeJavaScript(strOcularProcs);
           request.setAttribute("ocularProc", strOcularProcs);
		   
		   
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
      
           specsStr1 = StringEscapeUtils.escapeJavaScript(specsStr1);       
           request.setAttribute("specs", specsStr1);
           
           //impression
           logger.info("appNo="+appNo);
           if(requestId > 0) {
        	   //get the saved app no.
        	   String tmp = consultationRequestExtDao.getConsultationRequestExtsByKey(requestId, "appNo");
        	   appNo = Integer.parseInt(tmp);
        	   logger.info("updated appNo="+appNo);
           }
           
           
           
           //followUp
           FollowUpDao followUpDao = (FollowUpDao)SpringUtils.getBean("FollowUpDAO");
           List<FollowUp> followUps = followUpDao.getByAppointmentNo(appNo);
           String followup = "";
           for(FollowUp ef:followUps) {		   		
				if (ef.getTimespan() >0) {
					followup += "Follow up in " + ef.getTimespan() + " " + ef.getTimeframe() + "\n";
				}								
           }
           
           //get the checkboxes
           EyeFormDao dao = (EyeFormDao)SpringUtils.getBean("EyeFormDao");	   		
           EyeForm eyeform = dao.getByAppointmentNo(appNo);
           
           
			if (eyeform.getDischarge() != null
					&& eyeform.getDischarge().length()>0)
				followup += "Patient is discharged from my active care.\n";
	
			if (eyeform.getStat() != null
					&& eyeform.getStat().length()>0)
				followup += "Follow up as needed with me STAT or PRN if symptoms are worse.\n";
	
			if (eyeform.getOpt() != null && eyeform.getOpt().length() > 1)
				followup += "Routine eye care by an optometrist is recommended.\n";
                      
           
           followup = StringEscapeUtils.escapeJavaScript(followup);
           request.setAttribute("followup", followup);
           
           
           //test book
           TestBookRecordDao testBookDao = (TestBookRecordDao)SpringUtils.getBean("TestBookDAO");
           List<TestBookRecord> testBookRecords = testBookDao.getByAppointmentNo(appNo);
           
           //procedure book
           ProcedureBookDao procBookDao = (ProcedureBookDao)SpringUtils.getBean("ProcedureBookDAO");
           List<ProcedureBook> procBookRecords = procBookDao.getByAppointmentNo(appNo);
           
           //specs now
           
           
           //measurements
           MeasurementsDao measurementsDao = (MeasurementsDao) SpringUtils.getBean("measurementsDao");
           
           
           
		   
		   return mapping.findForward("conspecial");
	   }
			
	   private String getCppItemAsString(String demo, String issueCode, String text) {
		   if(cmm==null)
			   cmm=(CaseManagementManager) SpringUtils.getBean("caseManagementManager");
		   
		   Issue issue = cmm.getIssueInfoByCode("CurrentHistory");
		   if(issue ==null) {logger.warn("no issue for current history");return "";}
		   List<CaseManagementNote> notes = cmm.getCPP(demo, issue.getId(), null);
		   StringBuilder sb = new StringBuilder();		 
		   for(CaseManagementNote note:notes) {
			   sb.append(note.getNote()).append("\n");
		   }
		   logger.info(issueCode +":" + sb.toString());
		   
		   return text + "\n" + sb.toString();
	   }
	   
}
