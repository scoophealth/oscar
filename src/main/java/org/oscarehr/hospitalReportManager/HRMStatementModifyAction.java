/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */
package org.oscarehr.hospitalReportManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.hospitalReportManager.dao.HRMProviderConfidentialityStatementDao;
import org.oscarehr.hospitalReportManager.model.HRMProviderConfidentialityStatement;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

public class HRMStatementModifyAction extends DispatchAction {

	HRMProviderConfidentialityStatementDao hrmProviderConfidentialityStatementDao = (HRMProviderConfidentialityStatementDao) SpringUtils.getBean("HRMProviderConfidentialityStatementDao");

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();
		String statement = request.getParameter("statement");
 
		HRMProviderConfidentialityStatement confStatement;
		try {
			confStatement = hrmProviderConfidentialityStatementDao.find(providerNo); 
			if (confStatement == null) confStatement = new HRMProviderConfidentialityStatement();
		} catch (Exception e) {
			// Not found
			confStatement = new HRMProviderConfidentialityStatement();
		}

		confStatement.setStatement(statement);
		confStatement.setId(providerNo);
		try {
			hrmProviderConfidentialityStatementDao.merge(confStatement);
			request.setAttribute("statementSuccess", true);
		} catch (Exception e) {
			// Not merged
			request.setAttribute("statementSuccess", false);
		}
		
		return mapping.findForward("success");
	}
}
