package org.oscarehr.PMmodule.web.reports;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.model.AdmissionSearchBean;
import org.oscarehr.PMmodule.model.ClientReferral;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.web.formbean.ActivityReportFormBean;

public class ActivityReportAction extends DispatchAction {
	private static Log log = LogFactory.getLog(ActivityReportAction.class);
	private  final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	
	private ProgramManager programManager;
	private AdmissionManager admissionManager;
	private ClientManager clientManager;
	
	public void setProgramManager(ProgramManager mgr) {
		this.programManager = mgr;
	}
	
	public void setAdmissionManager(AdmissionManager mgr) {
		this.admissionManager = mgr;
	}
	
	public void setClientManager(ClientManager mgr) {
		this.clientManager = mgr;
	}
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return form(mapping,form,request,response);
	}
	
	public ActionForward form(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("programs",programManager.getProgramsByAgencyId("0"));
		
		return mapping.findForward("form");
	}
	
	public ActionForward generate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm reportForm = (DynaActionForm)form;
		ActivityReportFormBean formBean = (ActivityReportFormBean)reportForm.get("form");
		
		//# of new admissions
		AdmissionSearchBean searchBean = new AdmissionSearchBean();
		searchBean.setProgramId(new Long(formBean.getProgramId()));
		try {
			searchBean.setStartDate(formatter.parse(formBean.getStartDate()));
			searchBean.setEndDate(formatter.parse(formBean.getEndDate()));
		}catch(Exception e) {
			log.error(e);
		}
		
		Map summaryMap = new LinkedHashMap();
		
		List programs = programManager.getProgramsByAgencyId("0");
		for(Iterator iter=programs.iterator();iter.hasNext();) {
			Program p = (Program)iter.next();
			searchBean.setProgramId(new Long(p.getId().intValue()));
			List admissions = admissionManager.search(searchBean);
			int totalAdmissions = admissions.size();
			
			ClientReferral cr = new ClientReferral();
			cr.setProgramId(new Long(p.getId().longValue()));
			List referrals = clientManager.searchReferrals(cr);
			int totalReferrals = referrals.size();
			
			Long[] values = {new Long(totalAdmissions),new Long(totalReferrals)};
			summaryMap.put(p.getName(), values);
		}
		request.setAttribute("summary", summaryMap);
		
		return mapping.findForward("report");
	}
}
