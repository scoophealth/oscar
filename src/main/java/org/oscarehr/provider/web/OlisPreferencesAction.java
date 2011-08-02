package org.oscarehr.provider.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class OlisPreferencesAction extends DispatchAction {

	private Logger logger = MiscUtils.getLogger();
	private UserPropertyDAO dao = (UserPropertyDAO)SpringUtils.getBean("UserPropertyDAO");
	
	@Override	   
	public ActionForward unspecified(ActionMapping mapping, ActionForm actionform, HttpServletRequest request, HttpServletResponse response) {		   
		return view(mapping, actionform, request, response);	   
	}
	   
	public ActionForward view(ActionMapping mapping, ActionForm actionform, HttpServletRequest request, HttpServletResponse response) {
		String providerNo = LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo();		
		UserProperty prop = dao.getProp(providerNo, "olis_reportingLab");
		if(prop != null)
			request.setAttribute("reportingLaboratory", prop.getValue());

		prop = dao.getProp(providerNo, "olis_exreportingLab");
		if(prop != null)
			request.setAttribute("excludeReportingLaboratory", prop.getValue());

		return mapping.findForward("form");	   
	}
	
	public ActionForward save(ActionMapping mapping, ActionForm actionform, HttpServletRequest request, HttpServletResponse response) {
		String reportingLab = request.getParameter("reportingLaboratory");
		String excludeReportingLab = request.getParameter("excludeReportingLaboratory");
		String providerNo = LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo();
		
		if(reportingLab != null ) {
			UserProperty prop = dao.getProp(providerNo, "olis_reportingLab");
			if(prop == null) {
				prop = new UserProperty();
				prop.setName("olis_reportingLab");
				prop.setProviderNo(providerNo);				
			}
			prop.setValue(reportingLab);
			dao.saveProp(prop);
		}
		
		if(excludeReportingLab != null ) {
			UserProperty prop = dao.getProp(providerNo, "olis_exreportingLab");
			if(prop == null) {
				prop = new UserProperty();
				prop.setName("olis_exreportingLab");
				prop.setProviderNo(providerNo);				
			}
			prop.setValue(excludeReportingLab);
			dao.saveProp(prop);
		}
		
		return view(mapping,actionform,request,response);
	}
}
