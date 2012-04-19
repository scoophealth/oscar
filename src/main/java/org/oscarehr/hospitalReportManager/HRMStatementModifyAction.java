/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
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
		String providerNo = LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo();
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
