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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.model.Provider;
import org.oscarehr.dashboard.display.beans.DashboardBean;
import org.oscarehr.managers.DashboardManager;
import org.oscarehr.managers.ProviderManager2;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import java.util.ArrayList;
import java.util.List;

public class DisplayDashboardAction extends DispatchAction {
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	private static DashboardManager dashboardManager = SpringUtils.getBean(DashboardManager.class);
	private ProviderManager2 providerManager = SpringUtils.getBean( ProviderManager2.class );
	private static Logger logger = MiscUtils.getLogger();
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		return null;
	}
	
	@SuppressWarnings("unused")
	public ActionForward getDashboard(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		
		if( ! securityInfoManager.hasPrivilege(loggedInInfo, "_dashboardDisplay", SecurityInfoManager.READ, null ) ) {	
			return mapping.findForward("unauthorized");
        }
        Boolean canChgDashboardUser = false;
		if( securityInfoManager.hasPrivilege(loggedInInfo, "_dashboardChgUser", SecurityInfoManager.READ, null ) ) {
			canChgDashboardUser = true;
		}

		String dashboardId = request.getParameter("dashboardId");
		int id = 0;
		if( dashboardId != null && ! dashboardId.isEmpty() ) {
			id = Integer.parseInt( dashboardId );
		}
		
		Provider preferredProvider = loggedInInfo.getLoggedInProvider();
		List<Provider> providers = new ArrayList<Provider>();

		if (canChgDashboardUser) {
			String requestedProviderNo = request.getParameter("providerNo");
			if (requestedProviderNo != null && !requestedProviderNo.isEmpty()) {
				logger.info("DashboardDisplay of provider_no " + requestedProviderNo + " requested by provider_no " + loggedInInfo.getLoggedInProviderNo());
				preferredProvider = providerManager.getProvider(loggedInInfo, requestedProviderNo);
				dashboardManager.setRequestedProviderNo(loggedInInfo, requestedProviderNo);
			} else if (dashboardManager.getRequestedProviderNo(loggedInInfo) != null) {
				preferredProvider = providerManager.getProvider(loggedInInfo, dashboardManager.getRequestedProviderNo(loggedInInfo));
			}
			providers = providerManager.getProviders(loggedInInfo, Boolean.TRUE);
		}

		request.setAttribute("preferredProvider", preferredProvider);
		request.setAttribute("providers", providers);

		DashboardBean dashboard = dashboardManager.getDashboard(loggedInInfo, id);

		request.setAttribute("dashboard", dashboard);

		return mapping.findForward("success");
	}
}
