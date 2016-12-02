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
package org.oscarehr.integration.dashboard;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.model.IndicatorTemplate;
import org.oscarehr.common.model.Provider;
import org.oscarehr.dashboard.handler.IndicatorTemplateHandler;
import org.oscarehr.managers.DashboardManager;
import org.oscarehr.managers.ProviderManager2;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.w3c.dom.NodeList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class OutcomesDashboardAction extends DispatchAction {

	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	private DashboardManager dashboardManager = SpringUtils.getBean(DashboardManager.class);
	
	ProviderManager2 providerManager = SpringUtils.getBean(ProviderManager2.class);
	
	Logger logger = MiscUtils.getLogger();
	
	public ActionForward refreshIndicators(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_dashboardDisplay", "r", null)) {
			throw new SecurityException("missing required security object (_dashboarDisplay)");
		}
		
		List<IndicatorTemplate> sharedIndicatorTemplates = dashboardManager.getIndicatorLibrary(LoggedInInfo.getLoggedInInfoFromSession(request));

		String data = request.getParameter("data");

		byte[] b = Base64.decodeBase64(data);

		JSONObject jsonObject = JSONObject.fromObject(new String(b));

		String username = jsonObject.getString("username");
		JSONArray arr = jsonObject.getJSONArray("queryList");
		for(int x=0;x<arr.size();x++) {
			String metricSetName = arr.getString(x);
			
			//need to find the right indicator!
			Provider provider = providerManager.getProvider(LoggedInInfo.getLoggedInInfoFromSession(request), username);
			IndicatorTemplate indicatorTemplate = findIndicatorTemplateBySharedMetricSetName(LoggedInInfo.getLoggedInInfoFromSession(request),sharedIndicatorTemplates, metricSetName);
			
			if(indicatorTemplate != null) {
				OutcomesDashboardUtils.sendProviderIndicatorData(LoggedInInfo.getLoggedInInfoFromSession(request), provider, indicatorTemplate);
			} else {
				logger.warn("shared indicator not found:" + metricSetName);
			}
		}
		
		JSONObject o = new JSONObject();

		o.write(response.getWriter());

		return null;
	}

	protected IndicatorTemplate findIndicatorTemplateBySharedMetricSetName(LoggedInInfo x, List<IndicatorTemplate> templates, String sharedMetricSetName) {
		
		for(IndicatorTemplate template:templates) {
			IndicatorTemplateHandler ith = new IndicatorTemplateHandler(x, template.getTemplate().getBytes());

			String metricSetName = null;
			NodeList nl = ith.getIndicatorTemplateDocument().getElementsByTagName("sharedMetricSetName");
			if (nl != null && nl.getLength() > 0) {
				metricSetName = nl.item(0).getTextContent();
			}
			
			if(sharedMetricSetName.equals(metricSetName)) {
				return template;
			}
		}
		return null;
	}
}
