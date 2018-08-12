/**
 * Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
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
 * Department of Computer Science
 * LeadLab
 * University of Victoria
 * Victoria, Canada
 */

package org.oscarehr.dashboard.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import org.oscarehr.dashboard.handler.ExcludeDemographicHandler;

public class BulkPatientDashboardAction extends DispatchAction {
	private ExcludeDemographicHandler excludeDemographicHandler = new ExcludeDemographicHandler();

	public ActionForward excludePatients(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {

		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		excludeDemographicHandler.setLoggedinInfo(loggedInInfo);

		String patientIdsJson = request.getParameter("patientIds");
		String indicatorIdString = request.getParameter("indicatorId");

		int indicatorId;
		try {
			indicatorId = Integer.parseInt(indicatorIdString);
		} catch (NumberFormatException exception) {
			MiscUtils.getLogger().
				error("Could not parse indicator id from: " + indicatorIdString);
			return null;
		}

		String indicatorName = excludeDemographicHandler.getDrilldownIdentifier(
				indicatorId);

		excludeDemographicHandler.excludeDemoIds(patientIdsJson, indicatorName);

		MiscUtils.getLogger().info(
			"Excluded patients (" + patientIdsJson + ") from " + indicatorName
		);

		return null;
	}
}
