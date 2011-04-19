package org.oscarehr.provider.web;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

public class CppPreferencesAction extends DispatchAction {

	private Logger logger = MiscUtils.getLogger();
	
	@Override	   
	public ActionForward unspecified(ActionMapping mapping, ActionForm actionform, HttpServletRequest request, HttpServletResponse response) {		   
		return view(mapping, actionform, request, response);	   
	}
	   
	public ActionForward view(ActionMapping mapping, ActionForm actionform, HttpServletRequest request, HttpServletResponse response) {
		CppPreferencesUIBean bean = new CppPreferencesUIBean(LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo());
		bean.loadValues();
		request.setAttribute("bean", bean);
		return mapping.findForward("form");	   
	}
	
	public ActionForward save(ActionMapping mapping, ActionForm actionform, HttpServletRequest request, HttpServletResponse response) {
		@SuppressWarnings("unchecked")
		HashMap<String,String[]> parameters=new HashMap<String,String[]>(request.getParameterMap());
		CppPreferencesUIBean bean = new CppPreferencesUIBean(LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo());
		bean.deserializeParams(parameters);
		bean.saveValues();
		return view(mapping,actionform,request,response);
	}
}
