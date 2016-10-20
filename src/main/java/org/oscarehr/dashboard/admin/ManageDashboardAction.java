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

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
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
	
	private static Logger logger = MiscUtils.getLogger();
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
		String message = "";
	
		if( ! securityInfoManager.hasPrivilege(loggedInInfo, "_dashboardManager", SecurityInfoManager.WRITE, null ) ) {	
			return mapping.findForward("unauthorized");
        }

		FormFile formFile = (FormFile) form.getMultipartRequestHandler().getFileElements().get("indicatorTemplateFile");
		byte[] filebytes = null;
		JSONObject json = null;
		
		if( formFile != null ) {		
			try {
				filebytes = formFile.getFileData();
			} catch (Exception e) {
				json = new JSONObject();
				json.put("status", "error");
				json.put("message", e.getMessage() );
				MiscUtils.getLogger().error("Failed to transfer file. ", e);
			}
		}
		
		// TODO add a checksum: Uploaded templates will be checksum hashed, the hash will be stored in an 
		// indicatorTemplate column.  Every IndicatorTemplate checksum will be compared to the upload for duplicates.
		// The DashboarManager will contain a method. This class will return an error to the user.
		
		// TODO run the contained MySQL queries to check for syntax errors and whatever else may be broken. 
		// The DashboarManager will contain a method. This class will return an error to the user.
		
		if( filebytes != null ) {
			message = dashboardManager.importIndicatorTemplate( loggedInInfo, filebytes );
			json = JSONObject.fromObject(message);
		}

		Map<String, String> messageMap = new HashMap<String, String>();
		messageMap.put("status", json.getString("status") );
		messageMap.put("message", json.getString("message") );
		
		setRequest(loggedInInfo, request, messageMap);		

		return mapping.findForward("success");
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
		OutputStream outputStream = null;
		
		if( indicatorName == null || indicatorName.isEmpty() ) {
			indicatorName = "indicator_template-" + System.currentTimeMillis() + ".xml";
		} else {
			indicatorName = indicatorName + ".xml";
		}

		xmlTemplate = dashboardManager.exportIndicatorTemplate( loggedInInfo, Integer.parseInt( indicator ) );

		if( xmlTemplate != null ) {
			
			response.setContentType("text/xml");
	        response.setHeader("Content-disposition", "attachment; filename=" + indicatorName );
	        
	        try {
	        	outputStream = response.getOutputStream();
				outputStream.write( xmlTemplate.getBytes() );			
			} catch (Exception e) {
				MiscUtils.getLogger().error("File not found", e);
			} finally {
				if( outputStream != null ) {
					try {
						outputStream.flush();
						outputStream.close();
					} catch (IOException e) {
						logger.error("Failed to close output stream", e );
					}
				}
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
		
		// TODO check the current dashboard table for duplicates. 
		// strip out all punctuation and spaces, all names to lower case.
		// Method in DashboardManager, return message to user from this class. 
		
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
	
	private void setRequest(LoggedInInfo loggedInInfo, HttpServletRequest request) {
		setRequest(loggedInInfo, request, null);
	}
	
	/**
	 * Helper method to set a response object into the request.
	 */
	private void setRequest(LoggedInInfo loggedInInfo, HttpServletRequest request, Object message) {
		
		List<Dashboard> dashboards = dashboardManager.getDashboards(loggedInInfo);
		List<IndicatorTemplate> indicatorTemplates = dashboardManager.getIndicatorLibrary(loggedInInfo);
		
		if( dashboards != null ) {
			request.setAttribute("dashboards", dashboards);
		}
		
		if( indicatorTemplates != null ) {
			request.setAttribute("indicatorTemplates", indicatorTemplates);
		}
		
		if( message != null ) {
			request.setAttribute( "message", message );
		}
	}

}
