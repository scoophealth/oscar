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

package org.oscarehr.dashboard.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.oscarehr.common.model.Dashboard;
import org.oscarehr.common.model.IndicatorTemplate;
import org.oscarehr.managers.DashboardManager;
import org.oscarehr.managers.DashboardManager.ObjectName;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import net.sf.json.JSONObject;

public class ManageDashboardAction extends DispatchAction {
	
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	private static DashboardManager dashboardManager = SpringUtils.getBean(DashboardManager.class);
		
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		
		if( ! securityInfoManager.hasPrivilege(loggedInInfo, "_dashboardManager", SecurityInfoManager.WRITE, null ) ) {	
			return mapping.findForward("unauthorized");
        }
		
		setRequest(loggedInInfo, request);
				
		return mapping.findForward("success");
	}
	
	public ActionForward importTemplate(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, @SuppressWarnings("unused") HttpServletResponse response) {
		
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		ActionForward action = null;
		
		if( ! securityInfoManager.hasPrivilege(loggedInInfo, "_dashboardManager", SecurityInfoManager.WRITE, null ) ) {	
			return mapping.findForward("unauthorized");
        }

		FormFile formFile = (FormFile) form.getMultipartRequestHandler().getFileElements().get("indicatorTemplateFile");
		byte[] filebytes = null;
		
		if( formFile != null ) {		
			try {
				filebytes = formFile.getFileData();
			} catch (FileNotFoundException e) {
				MiscUtils.getLogger().error("Failed to transfer file.", e);
			} catch (IOException e) {
				MiscUtils.getLogger().error("Failed to transfer file.", e);
			}
		}
		
		if( filebytes != null && dashboardManager.importIndicatorTemplate(loggedInInfo, filebytes) ) {
			setRequest(loggedInInfo, request);
			action = mapping.findForward("success");
		} else {
			action = mapping.findForward("error");
		}

		return action;
	}
	
	
	@SuppressWarnings("unused")
	public ActionForward exportTemplate(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		
		if( ! securityInfoManager.hasPrivilege(loggedInInfo, "_dashboardManager", SecurityInfoManager.READ, null ) ) {
			MiscUtils.getLogger().warn("Unauthorized. Missing _dasboardManager object for user " 
					+ loggedInInfo.getLoggedInProviderNo() + " while downloading indicator template.");
			return mapping.findForward("unauthorized");
        }

		String indicator = request.getParameter("indicatorId");
		String indicatorName = request.getParameter("indicatorName");
		int indicatorId = 0;
		String xmlTemplate = null;
		File tempXmlFile = null;
		OutputStream out = null;
		FileInputStream in = null;
		
		if( indicatorName == null || indicatorName.isEmpty() ) {
			indicatorName = "indicator_template-" + System.currentTimeMillis() + ".xml";
		} else {
			indicatorName = indicatorName + ".xml";
		}
		
		if( indicator != null && ! indicator.isEmpty() ) {
			indicatorId = Integer.parseInt( indicator );
		}

		if( indicatorId > 0 ) {
			xmlTemplate = dashboardManager.exportIndicatorTemplate(loggedInInfo, indicatorId);
		}
		
		if( xmlTemplate != null ) {
	        response.setHeader("Content-disposition", "attachment; filename=" + indicatorName );
	        try {
				tempXmlFile = File.createTempFile("indicatorName", ".tmp");
				out = response.getOutputStream();
				in = new FileInputStream( tempXmlFile );
				byte[] buffer = new byte[4096];
				int length;
				while ( (length = in.read(buffer) ) > 0){
				    out.write(buffer, 0, length);
				}
				in.close();
				out.flush();
			} catch (FileNotFoundException e) {
				MiscUtils.getLogger().error("File not found", e);
			} catch (IOException e) {
				MiscUtils.getLogger().error("Error", e);
			}
		}
		
		return null;
	}
	
	@SuppressWarnings("unused")
	public ActionForward assignDashboard(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		
		if( ! securityInfoManager.hasPrivilege(loggedInInfo, "_dashboardManager", SecurityInfoManager.WRITE, null ) ) {	
			return mapping.findForward("unauthorized");
        }
		String dashboard = request.getParameter("indicatorDashboardId");
		String indicator = request.getParameter("indicatorId");
		int dashboardId = 0;
		int indicatorId = 0;
		
		JSONObject jsonObject = new JSONObject();
		
		if( dashboard != null && ! dashboard.isEmpty() ) {
			dashboardId = Integer.parseInt( dashboard );
		}
		
		if( indicator != null && ! indicator.isEmpty() ) {
			indicatorId = Integer.parseInt( indicator );
		}
		
		if( dashboardManager.assignIndicatorToDashboard(loggedInInfo, dashboardId, indicatorId) ) {
			jsonObject.put("success", "true");
		} else {
			jsonObject.put("success", "false");
		}
		
		try {
			jsonObject.write( response.getWriter() );
		} catch (IOException e) {
			 MiscUtils.getLogger().error("JSON response failed", e);
			 return mapping.findForward("error");
		}
		
		return null; 
	}
	

	@SuppressWarnings("unused")
	public ActionForward saveDashboard(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		ActionForward action = null;
		
		if( ! securityInfoManager.hasPrivilege(loggedInInfo, "_dashboardManager", SecurityInfoManager.WRITE, null ) ) {	
			return mapping.findForward("unauthorized");
        }

		String dashboardId = request.getParameter( "dashboardId" );
		String dashboardName = request.getParameter( "dashboardName" );
		String dashboardActive = request.getParameter( "dashboardActive" );
		String dashboardDescription = request.getParameter( "dashboardDescription" );
		Integer id = null;
		Boolean active = Boolean.FALSE;
		
		if( dashboardId != null && ! dashboardId.isEmpty()) {
			id = Integer.parseInt(dashboardId);
		}
		
		if( dashboardActive != null && ! dashboardActive.isEmpty()) {
			active = Boolean.TRUE;
		}
		
		Dashboard dashboard = new Dashboard();
		dashboard.setId( id );
		dashboard.setName( dashboardName );
		dashboard.setDescription( dashboardDescription );
		dashboard.setActive( active );
		dashboard.setCreator( loggedInInfo.getLoggedInProviderNo() );
		dashboard.setEdited( new Date(System.currentTimeMillis()) );
		dashboard.setLocked( Boolean.FALSE );

		if( dashboardManager.addDashboard(loggedInInfo, dashboard) ) {
			setRequest(loggedInInfo, request);
			action = mapping.findForward("success");
		} else {
			action = mapping.findForward("error");
		}

		return action;
	}
	
	@SuppressWarnings("unused")
	public ActionForward toggleActive(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		
		if( ! securityInfoManager.hasPrivilege(loggedInInfo, "_dashboardManager", SecurityInfoManager.WRITE, null ) ) {	
			return mapping.findForward("unauthorized");
        }
		
		String objectClassName = request.getParameter( "objectClassName" );
		String objectId = request.getParameter( "objectId" );
		String activeState = request.getParameter( "active" );
		Boolean active = Boolean.FALSE;
		int id = 0;
		
		if( objectId != null && ! objectId.isEmpty() ) {
			id = Integer.parseInt( objectId );
		}
		
		if( activeState != null ) {
			active = Boolean.parseBoolean( activeState );
		}
		
		if( id > 0 && objectClassName != null ) {
			dashboardManager.toggleStatus(loggedInInfo, id, ObjectName.valueOf( objectClassName ), active);
		}
		
		return null;
	}
	
	/**
	 * Helper method to set a response object into the request.
	 */
	private void setRequest(LoggedInInfo loggedInInfo, HttpServletRequest request) {
		
		List<Dashboard> dashboards = dashboardManager.getDashboards(loggedInInfo);
		List<IndicatorTemplate> indicatorTemplates = dashboardManager.getIndicatorLibrary(loggedInInfo);
		
		if( dashboards != null ) {
			request.setAttribute("dashboards", dashboards);
		}
		
		if( indicatorTemplates != null ) {
			request.setAttribute("indicatorTemplates", indicatorTemplates);
		}
	}

}
