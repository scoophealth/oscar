/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */


package oscar.oscarLab.ca.all.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.util.MiscUtils;

/**
 * UI for managing current system settings
 *
 * @author dritan
 *
 */
public class HRMWebHandler extends DispatchAction {

	//private SettingsDAO settingsDAO = null;

	//public void setSystemSettingsDAO(SettingsDAO settingsDAO) {
	//	this.settingsDAO = settingsDAO;
	//}

	/**
	 * Define your own methods for calling by issuing an extra parameter in your forms. Pass
	 * "method=myCustomFormHandler" in the request to call myCustomFormHandler() ActionForward method.
	 */
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		return null;
	}

	public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		MiscUtils.getLogger().debug("System Settings Update");
		//DynaActionForm frm = (DynaActionForm) form;
		//Settings settings = (Settings) frm.get("settings");
		//Settings settings = new Settings(request.getParameter("groupName"), request.getParameter("keyField"), request
		//		.getParameter("valueField"));

		MiscUtils.getLogger().debug("settings : groupName" +request.getParameter("groupName")+"   keyField "+request.getParameter("keyField") +"   value  "+request.getParameter("valueField"));

		//settingsDAO.save(settings);

		return mapping.findForward("success");
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		/*
				String method = request.getParameter("method");
				if (method != null && method.equals("update")) {
					return update(mapping, form, request, response);
				}
		*/
		return mapping.findForward("success");
	}

	/**
	 * Show current settings for all or any given group - typed or selected from available.
	 */
	public ActionForward showSettings(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response)  {

		return mapping.findForward("success");
	}

}
