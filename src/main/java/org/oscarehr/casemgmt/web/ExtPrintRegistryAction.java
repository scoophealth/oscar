package org.oscarehr.casemgmt.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.casemgmt.util.ExtPrintRegistry;
import org.oscarehr.util.MiscUtils;

public class ExtPrintRegistryAction extends DispatchAction {

	private static Logger logger = MiscUtils.getLogger();
	
	public ActionForward register(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String name = request.getParameter("name");
		String bean = request.getParameter("bean");
		
		ExtPrintRegistry.addEntry(name, bean);
		
		logger.info("ext print registry added " + name + ":" + bean);
		return null;
	}
	
}
