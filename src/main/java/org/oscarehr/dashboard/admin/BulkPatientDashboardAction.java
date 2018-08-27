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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.oscarehr.dashboard.handler.DiseaseRegistryHandler;
import org.oscarehr.dashboard.handler.ExcludeDemographicHandler;
import org.oscarehr.dashboard.handler.MessageHandler;

public class BulkPatientDashboardAction extends DispatchAction {

	private static Logger logger = MiscUtils.getLogger();

	private ExcludeDemographicHandler excludeDemographicHandler = new ExcludeDemographicHandler();

	private DiseaseRegistryHandler diseaseRegistryHandler = new DiseaseRegistryHandler();

	private MessageHandler messageHandler = new MessageHandler();

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
			logger.error("Could not parse indicator id from: " + indicatorIdString);
			return null;
		}

		String indicatorName = excludeDemographicHandler.getDrilldownIdentifier(
				indicatorId);

		excludeDemographicHandler.excludeDemoIds(patientIdsJson, indicatorName);

		logger.info(
			"Excluded patients (" + patientIdsJson + ") from " + indicatorName
		);

		return null;
	}

	private String getICD9Code(HttpServletRequest request) {
		return request.getParameter("dxUpdateICD9Code");
	}

	public ActionForward addToDiseaseRegistry(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String providerNo = LoggedInInfo.getLoggedInInfoFromSession(request)
			.getLoggedInProviderNo();

		String icd9code = getICD9Code(request);

		String patientIdsJson = request.getParameter("patientIds");
		JSONArray patientIds = asJsonArray(patientIdsJson);

		for (int i = 0; i < patientIds.size(); ++i) {
			diseaseRegistryHandler.addToDiseaseRegistry(
				patientIds.getInt(i),
				icd9code,
				providerNo
			);
		}

		String subject = "Bulk addition to disease registry report.";
		String message = "Added code (" + icd9code +
			") to disease registry for patients (" + patientIdsJson + ")" +
			" with provider no (" + providerNo + ")";

		messageHandler.notifyProvider(subject, message, providerNo);

		logger.info(message);

		return null;
	}

	public ActionForward getICD9Description(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String icd9code = getICD9Code(request);
		String description = diseaseRegistryHandler.getDescription(icd9code);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("icd9code", icd9code);
		jsonObject.put("description", description);

		try {
			jsonObject.write(response.getWriter());
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (IOException e) {
			logger.error("Error generating JSON response", e);
			return mapping.findForward("error");
		}

		return null;
	}

	private static JSONArray asJsonArray(String jsonString) {
		if(jsonString == null || jsonString.isEmpty()) {
			return new JSONArray();
		}

		if(!jsonString.startsWith("[")) {
			jsonString = "[" + jsonString;
		}
		if(!jsonString.endsWith("]")) {
			jsonString = jsonString + "]";
		}

		JSONArray jsonArray = JSONArray.fromObject( jsonString );
		return jsonArray;
	}
}
