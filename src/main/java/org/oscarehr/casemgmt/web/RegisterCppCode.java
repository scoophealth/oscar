package org.oscarehr.casemgmt.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.util.CppUtils;
import org.oscarehr.util.MiscUtils;

public class RegisterCppCode extends DispatchAction {
	
	static Logger logger = MiscUtils.getLogger();
	
	
	public ActionForward register(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String code = request.getParameter("code");
		String[] codes = code.split(",");
		for(String c:codes) {
			if(!exists(c)) {
				logger.info("adding " + c + " to cpp codes");
				CppUtils.addCppCode(c);
			}
		}
		return null;
	}
	
	private boolean exists(String c) {
		for(String s:CppUtils.cppCodes) {
			if(s.equals(c))
				return true;
		}
		return false;
	}
}
