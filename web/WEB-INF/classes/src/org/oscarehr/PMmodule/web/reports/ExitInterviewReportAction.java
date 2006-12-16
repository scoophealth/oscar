package org.oscarehr.PMmodule.web.reports;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.model.ConsentInterview;
import org.oscarehr.PMmodule.service.ConsentManager;

import com.Ostermiller.util.CSVPrinter;

public class ExitInterviewReportAction extends DispatchAction {

	private ConsentManager consentManager;
	
	public void setConsentManager(ConsentManager mgr) {
		this.consentManager = mgr;
	}
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return form(mapping,form,request,response);
	}
	
	public ActionForward form(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("interviews", consentManager.getConsentInterviews());
		return mapping.findForward("form");
	}
	
	public ActionForward export(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		List interviews = consentManager.getConsentInterviews();
		
		try {
			response.setContentType("text/x-csv");
			response.addHeader("Content-Disposition", "attachement; filename=exit-interview-data.csv");
			
			CSVPrinter printer = new CSVPrinter(response.getOutputStream());
			printer.setAutoFlush(true);
			printer.println(new String[] {"Language","Language-Other","Language (Read)","Language (Read)-Other","Education","Review","Review-Comments","Pressure","Pressure-Comments","Information","Information-Comments","Followup","Followup-Comments","Comments","Comments-Comments"});
			
			for(Iterator iter = interviews.iterator();iter.hasNext();) {
				ConsentInterview interview = (ConsentInterview)iter.next();
				String data[] = new String[15];
				data[0] = interview.getLanguage();
				data[1] = interview.getLanguageOther();
				data[2] = interview.getLanguageRead();
				data[3] = interview.getLanguageReadOther();
				data[4] = interview.getEducation();
				data[5] = interview.getReview();
				data[6] = interview.getReviewOther();
				data[7] = interview.getPressure();
				data[8] = interview.getPressureOther();
				data[9] = interview.getInformation();
				data[10] = interview.getInformationOther();
				data[11] = interview.getFollowup();
				data[12] = interview.getFollowupOther();
				data[13] = interview.getComments();
				data[14] = interview.getCommentsOther();
				printer.println(data);
			}
			
			printer.close();
		}catch(Exception e) {
			log.error(e);
		}		
		
		return null;
		
	}
	
}
