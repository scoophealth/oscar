/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package org.oscarehr.eyeform;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.oscarTickler.TicklerCreator;


public class EyeformSendTicklerAction extends DispatchAction {

	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);

		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_demographic", "w", null)) {
        	throw new SecurityException("missing required security object (_demographic)");
        }
		
		String followUp = request.getParameter("followUp");
		String procedure = request.getParameter("procedure");
		String diagnostics = request.getParameter("diagnostics");
		String demographicNo = request.getParameter("demographic_no");

		String customMessage = request.getParameter("message");
		String toProviderNo = request.getParameter("toProvider");

		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("demographicNo", demographicNo);
		hashMap.put("toProviderNo", toProviderNo);

		TicklerCreator tc = new TicklerCreator();
		String message = "";

		if (followUp != null && followUp.trim().length() > 0) {
			message = "";
			message += "Book " + request.getParameter("followUp_type");
			message += " with " + request.getParameter("followUp_doc");
			message += " in " + request.getParameter("followUp_time") + " " + request.getParameter("followUp_timeType") + ". ";
			message += "Urgency: " + request.getParameter("followUp_urgency") + "; ";
			message += "Comment: " + (request.getParameter("followUp_comment") != null && request.getParameter("followUp_comment").trim().length() > 0 ? request.getParameter("followUp_comment") : "(none)");

			hashMap.put("followUp", message);
			tc.createTickler(loggedInInfo, demographicNo, toProviderNo, message);
		}

		if (procedure != null && procedure.trim().length() > 0) {
			message = "";
			message += "Book procedure " + request.getParameter("procedure_procedure");
			message += " for eye " + request.getParameter("procedure_eye");
			message += " at location: " + request.getParameter("procedure_location") + ". ";
			message += "Urgency: " + request.getParameter("procedure_urgency") + "; ";
			message += "Comment: " + (request.getParameter("procedure_comment") != null && request.getParameter("procedure_comment").trim().length() > 0 ? request.getParameter("procedure_comment") : "(none)");

			hashMap.put("procedure", message);
			tc.createTickler(loggedInInfo, demographicNo, toProviderNo, message);
		}

		if (diagnostics != null && diagnostics.trim().length() > 0) {
			message = "";
			message += "Book diagnostics " + request.getParameter("diagnostics_name");
			message += " for eye " + request.getParameter("diagnostics_eye") + ". ";
			message += "Urgency: " + request.getParameter("diagnostics_urgency") + "; ";
			message += "Comment: " + (request.getParameter("diagnostics_comment") != null && request.getParameter("diagnostics_comment").trim().length() > 0 ? request.getParameter("diagnostics_comment") : "(none)");

			hashMap.put("diagnostics", message);
			tc.createTickler(loggedInInfo, demographicNo, toProviderNo, message);
		}

		if (toProviderNo != null && customMessage != null && customMessage.trim().length() > 0) {
			tc.createTickler(loggedInInfo, demographicNo, toProviderNo, customMessage);

			hashMap.put("custom", customMessage);
		}

		JSONObject json = JSONObject.fromObject(hashMap);
		response.getOutputStream().write(json.toString().getBytes());

		return null;
	}

}
