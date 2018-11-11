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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.model.IndicatorTemplate;
import org.oscarehr.common.model.Provider;
import org.oscarehr.dashboard.display.beans.DrilldownBean;
import org.oscarehr.dashboard.handler.IndicatorTemplateHandler;
import org.oscarehr.managers.DashboardManager;
import org.oscarehr.managers.ProviderManager2;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.w3c.dom.NodeList;

public class DisplayDrilldownAction extends DispatchAction  {
	
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	private static DashboardManager dashboardManager = SpringUtils.getBean(DashboardManager.class);
	private ProviderManager2 providerManager = SpringUtils.getBean( ProviderManager2.class );
	private static Logger logger = MiscUtils.getLogger();
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		return null;
	}
	
	public ActionForward getDrilldown(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		
		if( ! securityInfoManager.hasPrivilege(loggedInInfo, "_dashboardDrilldown", SecurityInfoManager.READ, null ) ) {
			if (loggedInInfo != null && loggedInInfo.getLoggedInProvider() != null) {
				logger.info("Provider "+loggedInInfo.getLoggedInProvider().getProviderNo()+" does not have read permission on _dashboardDrilldown security object");
			}
			return mapping.findForward("unauthorized");
        }
		if (!securityInfoManager.hasPrivilege(loggedInInfo,
				"_dxresearch", SecurityInfoManager.WRITE, null)) {
			if (loggedInInfo != null && loggedInInfo.getLoggedInProvider() != null) {
				logger.info("Provider "+loggedInInfo.getLoggedInProvider().getProviderNo()+" does not have write permission on _dxresearch security object");
			}
			return mapping.findForward("unauthorized");
		}
		
		String indicatorTemplateId = request.getParameter("indicatorTemplateId");

		int id = 0;
		if( indicatorTemplateId != null && ! indicatorTemplateId.isEmpty() ) {
			id = Integer.parseInt( indicatorTemplateId );
		}
		
		String providerNo = null;
		if (dashboardManager.getRequestedProviderNo(loggedInInfo) != null) {
		    providerNo = dashboardManager.getRequestedProviderNo(loggedInInfo);
        }
		DrilldownBean drilldown;
		if (providerNo == null) {
			drilldown = dashboardManager.getDrilldownData(loggedInInfo, id, "null");
		} else {
			drilldown = dashboardManager.getDrilldownData(loggedInInfo, id, providerNo, "null");
		}
		
		// something must be returned. If not then something is very wrong.
		if ( drilldown == null ) {
			return mapping.findForward("error");
		}

		Provider preferredProvider = loggedInInfo.getLoggedInProvider();
		if (dashboardManager.getRequestedProviderNo(loggedInInfo) != null) {
			preferredProvider = providerManager.getProvider(loggedInInfo, dashboardManager.getRequestedProviderNo(loggedInInfo));
		}
		
		request.setAttribute("preferredProvider",  preferredProvider);
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
		
		String metricLabel = request.getParameter("sharedMetricSetLable");
		if(StringUtils.isBlank(metricLabel)) {
			metricLabel = "null";
		} 
		
		ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
		if(!providerDao.providerExists(providerNo)){
			return mapping.findForward("error");
		}
		
		List<IndicatorTemplate> sharedIndicatorTemplates = null;
		IndicatorTemplate indicatorTemplate = null;
		
		if(metricLabel.equals("null")) { //local pie opens drill down list for whole pie, should use shared xml file
			sharedIndicatorTemplates = dashboardManager.getIndicatorLibrary(LoggedInInfo.getLoggedInInfoFromSession(request), true);
			indicatorTemplate = findIndicatorTemplateBySharedMetricSetName(loggedInInfo,sharedIndicatorTemplates, metricSetName);

		} else { //common dashboard opens drill down list from pie slice, should use not-shared xml file for different metric label
			sharedIndicatorTemplates = dashboardManager.getIndicatorLibrary(LoggedInInfo.getLoggedInInfoFromSession(request), false);
			indicatorTemplate = findIndicatorTemplateBySharedMetricSetNameAndMatricLabel(loggedInInfo,sharedIndicatorTemplates, metricSetName, metricLabel);
		}
		
		if(indicatorTemplate == null) {
			return mapping.findForward("error");
		}
		
		
		DrilldownBean drilldown = null;
		
		if(metricLabel.equals("null")) { //local pie opens drill down list for whole pie, should use shared xml file
			drilldown = dashboardManager.getDrilldownData(loggedInInfo, indicatorTemplate.getId(),providerNo!=null?providerNo:null, metricLabel);
		} else { //common dashboard opens drill down list from pie slice, should use not-shared xml file for different metric label
			drilldown = dashboardManager.getDrilldownData(loggedInInfo, indicatorTemplate.getId(),providerNo!=null?providerNo:null, metricLabel);
		}
			
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
	

	protected IndicatorTemplate findIndicatorTemplateBySharedMetricSetNameAndMatricLabel(LoggedInInfo x, List<IndicatorTemplate> templates, String sharedMetricSetName, String metricLabel) {
		
		for(IndicatorTemplate template:templates) {
			IndicatorTemplateHandler ith = new IndicatorTemplateHandler(x, template.getTemplate().getBytes());
			
			String metricLabelFromTemplate = ith.getIndicatorTemplateXML().getMetricLabel();
			
			String metricSetName = null;
			NodeList nl = ith.getIndicatorTemplateDocument().getElementsByTagName("sharedMetricSetName");
			if (nl != null && nl.getLength() > 0) {
				metricSetName = nl.item(0).getTextContent();
			}
			
			if(sharedMetricSetName.equals(metricSetName)) {
				if(metricLabel.equals(metricLabelFromTemplate)) {
					return template;
				}				
			}
		}
		return null;
	}
	
}
