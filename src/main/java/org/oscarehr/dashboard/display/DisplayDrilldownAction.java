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
package org.oscarehr.dashboard.display;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.model.IndicatorTemplate;
import org.oscarehr.dashboard.display.beans.DrilldownBean;
import org.oscarehr.dashboard.handler.IndicatorTemplateHandler;
import org.oscarehr.managers.DashboardManager;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import org.w3c.dom.NodeList;

public class DisplayDrilldownAction extends DispatchAction  {
	
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	private static DashboardManager dashboardManager = SpringUtils.getBean(DashboardManager.class);
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		return null;
	}
	
	public ActionForward getDrilldown(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		
		if( ! securityInfoManager.hasPrivilege(loggedInInfo, "_dashboardDrilldown", SecurityInfoManager.READ, null ) ) {	
			return mapping.findForward("unauthorized");
        }
		
		String indicatorTemplateId = request.getParameter("indicatorTemplateId");

		int id = 0;
		if( indicatorTemplateId != null && ! indicatorTemplateId.isEmpty() ) {
			id = Integer.parseInt( indicatorTemplateId );
		}
		
		DrilldownBean drilldown = dashboardManager.getDrilldownData(loggedInInfo, id);
		
		// something must be returned. If not then something is very wrong.
		if ( drilldown == null ) {
			return mapping.findForward("error");
		}

		request.setAttribute( "drilldown", drilldown );		
		return mapping.findForward("success");
	}
	
	
	public ActionForward getDrilldownBySharedMetricSetName(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		
		if( ! securityInfoManager.hasPrivilege(loggedInInfo, "_dashboardDrilldown", SecurityInfoManager.READ, null ) ) {	
			return mapping.findForward("unauthorized");
        }
		
		String metricSetName = request.getParameter("sharedMetricSetName");
		
		String providerNo = request.getParameter("providerNo");
		
		

		List<IndicatorTemplate> sharedIndicatorTemplates = dashboardManager.getIndicatorLibrary(LoggedInInfo.getLoggedInInfoFromSession(request));

		IndicatorTemplate indicatorTemplate = findIndicatorTemplateBySharedMetricSetName(loggedInInfo,sharedIndicatorTemplates, metricSetName);
		
		if(indicatorTemplate == null) {
			return mapping.findForward("error");
		}
		
		
		DrilldownBean drilldown = dashboardManager.getDrilldownData(loggedInInfo, indicatorTemplate.getId(),providerNo!=null?providerNo:null);
		
		// something must be returned. If not then something is very wrong.
		if ( drilldown == null ) {
			return mapping.findForward("error");
		}

		request.setAttribute( "drilldown", drilldown );		
		return mapping.findForward("success");
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
