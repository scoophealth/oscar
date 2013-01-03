/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.web;

import java.util.Calendar;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.RedirectingActionForward;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.BedDemographicManager;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.service.ProgramQueueManager;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.PMmodule.service.RoomDemographicManager;
import org.oscarehr.PMmodule.service.RoomManager;
import org.oscarehr.PMmodule.utility.Utility;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.SessionConstants;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.OscarProperties;

import com.quatro.common.KeyConstants;
import com.quatro.model.security.NoAccessException;
import com.quatro.service.security.SecurityManager;
import com.quatro.service.security.UserAccessManager;

/**
 * deprecated do not use this class anymore, there's no good reason for this classes existance. Action classes should generally speaking stand on their own without a common super class. If utilities are required, then a utility class should be made, not a super class.
 */
public abstract class BaseAction extends DispatchAction {
	
	protected static final String PARAM_START = "?";
	protected static final String PARAM_EQUALS = "=";
	protected static final String PARAM_AND = "&";
	
	public void addError(HttpServletRequest req, String message) {
		ActionMessages msgs = getErrors(req);
		msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", message));
		addErrors(req, msgs);
	}

	public void addMessage(HttpServletRequest req, String message) {
		ActionMessages msgs = getMessages(req);
		msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", message));
		addMessages(req, msgs);
	}

	public void addMessage(HttpServletRequest req, String key, String val) {
		ActionMessages msgs = getMessages(req);
		msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(key, val));
		addMessages(req, msgs);
	}

	public ApplicationContext getAppContext() {
		return WebApplicationContextUtils.getWebApplicationContext(getServlet().getServletContext());
	}

	public CaseManagementManager getCaseManagementManager() {
		return (CaseManagementManager)getAppContext().getBean("caseManagementManager");
	}

	public AdmissionManager getAdmissionManager() {
		return (AdmissionManager) getAppContext().getBean("admissionManager");
	}

	public ClientManager getClientManager() {
		return (ClientManager) getAppContext().getBean("clientManager");
	}

	public ProgramManager getProgramManager() {
		return (ProgramManager) getAppContext().getBean("programManager");
	}
	public UserAccessManager getUserAccessManager() {
		return (UserAccessManager) getAppContext().getBean("userAccessManager");
	}

	public ProgramQueueManager getProgramQueueManager() {
		return (ProgramQueueManager) getAppContext().getBean("programQueueManager");
	}

	public RoomManager getRoomManager() {
		return (RoomManager) getAppContext().getBean("roomManager");
	}
	
	public RoomDemographicManager getRoomDemographicManager() {
		return (RoomDemographicManager) getAppContext().getBean("roomDemographicManager");
	}
	
	public BedDemographicManager getBedDemographicManager() {
		return (BedDemographicManager) getAppContext().getBean("bedDemographicManager");
	}

	public ProviderManager getProviderManager() {
		return (ProviderManager) getAppContext().getBean("providerManager");
	}

	protected String getProviderNo(HttpServletRequest request) {
		return ((Provider) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PROVIDER)).getProviderNo();
	}

    protected Provider getProvider(HttpServletRequest request) {
        return ((Provider) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PROVIDER));
    }

	protected SecurityManager getSecurityManager(HttpServletRequest request)
	{
		return (SecurityManager) request.getSession()
		.getAttribute(KeyConstants.SESSION_KEY_SECURITY_MANAGER);
	}
	protected String getParameter(HttpServletRequest request, String parameterName) {
		return request.getParameter(parameterName);
	}
	
	protected Object getSessionAttribute(HttpServletRequest request, String attributeName) {
		Object attribute = request.getSession().getAttribute(attributeName);
		
		if (attribute != null) {
			request.getSession().removeAttribute(attributeName);
		}
		
		return attribute;
	}
	
	protected ActionForward createRedirectForward(ActionMapping mapping, String forwardName, StringBuffer parameters) {
		ActionForward forward = mapping.findForward(forwardName);
		StringBuilder path = new StringBuilder(forward.getPath());
		path.append(parameters);
		
		return new RedirectingActionForward(path.toString());
	}

	protected void setMenu(HttpServletRequest request,String currentMenu) throws NoAccessException {
		/*
		  isPageChangedFlag appeared?
		*/
		
		if (request.getAttribute("pageChanged") == null) {
			if(request.getParameter("pageChanged")!= null) request.setAttribute("pageChanged", request.getParameter("pageChanged"));
		}
		String lastMenu = (String) request.getSession().getAttribute("currMenu");
		if (lastMenu == null) {
			initMenu(request);
		}
		else
		{
			request.getSession().setAttribute(lastMenu, KeyConstants.ACCESS_VIEW);
		}
		// check home page access
		if(!currentMenu.equals(KeyConstants.MENU_HOME))
		{
			if (request.getSession().getAttribute(currentMenu).equals(KeyConstants.ACCESS_NULL))
			{
				throw new NoAccessException();
			}
		}
		String scrollPosition = request.getParameter("scrollPosition");
		if(null != scrollPosition) {
			request.setAttribute("scrPos", scrollPosition);
		}
		else
		{
			request.setAttribute("scrPos", "0");
		}
	}

	private void initMenu(HttpServletRequest request)
	{
		SecurityManager sec = getSecurityManager(request);
		if (sec==null) return;		
		//Client Management
		if (sec.GetAccess(KeyConstants.FUN_CLIENT, "").compareTo(KeyConstants.ACCESS_READ) >= 0) {
			request.getSession().setAttribute(KeyConstants.MENU_CLIENT, KeyConstants.ACCESS_VIEW);
		} else
			request.getSession().setAttribute(KeyConstants.MENU_CLIENT, KeyConstants.ACCESS_NULL);
	
		//Program
		if (sec.GetAccess(KeyConstants.FUN_PROGRAM, "").compareTo(KeyConstants.ACCESS_READ) >= 0) {
			request.getSession().setAttribute(KeyConstants.MENU_PROGRAM, KeyConstants.ACCESS_VIEW);
		} else
			request.getSession().setAttribute(KeyConstants.MENU_PROGRAM, KeyConstants.ACCESS_NULL);

		//Facility Management
		if (sec.GetAccess(KeyConstants.FUN_FACILITY, "").compareTo(KeyConstants.ACCESS_READ) >= 0) {
			request.getSession().setAttribute(KeyConstants.MENU_FACILITY, KeyConstants.ACCESS_VIEW);
		} else
			request.getSession().setAttribute(KeyConstants.MENU_FACILITY, KeyConstants.ACCESS_NULL);

		//Report Runner
		if (sec.GetAccess(KeyConstants.FUN_REPORTS, "").compareTo(KeyConstants.ACCESS_READ) >= 0) {
			request.getSession().setAttribute(KeyConstants.MENU_REPORT, KeyConstants.ACCESS_VIEW);
		} else
			request.getSession().setAttribute(KeyConstants.MENU_REPORT, KeyConstants.ACCESS_NULL);

		//System Admin
		if (OscarProperties.getInstance().isAdminOptionOn() && sec.GetAccess("_admin", "").compareTo(KeyConstants.ACCESS_READ) >= 0) {
			request.getSession().setAttribute(KeyConstants.MENU_ADMIN, KeyConstants.ACCESS_VIEW);
		} else
			request.getSession().setAttribute(KeyConstants.MENU_ADMIN, KeyConstants.ACCESS_NULL);
		request.getSession().setAttribute(KeyConstants.MENU_HOME, KeyConstants.ACCESS_VIEW);
		request.getSession().setAttribute(KeyConstants.MENU_TASK, KeyConstants.ACCESS_VIEW);
	}
	
	protected ActionForward createRedirectForward(ActionMapping mapping,
			String forwardName, StringBuilder parameters) {
		ActionForward forward = mapping.findForward(forwardName);
		StringBuilder path = new StringBuilder(forward.getPath());
		path.append(parameters);

		return new RedirectingActionForward(path.toString());
	}
	protected ActionForward dispatchMethod(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response, String name) throws Exception
	{
		/*
		String tokenP = request.getParameter("token");
		if ( name!= null && (name.indexOf("save")>=0 || name.indexOf("login")>=0)) {
			String tokenS = (String) request.getSession().getAttribute("token"); 
			if(Utility.isNotNullOrEmptyStr(tokenS)) {
				if (!Utility.isNotNullOrEmptyStr(tokenP)) throw new Exception("Sorry this page cannot be displayed.");
				if(!tokenS.equals(tokenP))   throw new Exception("Sorry this page cannot be displayed.");
			}
		}
		*/
		Calendar startDt = Calendar.getInstance();
		try {
			ActionForward fwd =  super.dispatchMethod(mapping, form, request, response, name);
			if(fwd != null && fwd.getName() != null && fwd.getName().equals("failure")) throw new NoAccessException();
	        response.setHeader("Expires", "-1");
	        response.setHeader("Cache-Control",
	        	"must-revalidate, post-check=0, pre-check=0");
	        response.setHeader("Pragma", "private");
	        
	        if (request.getAttribute("notoken") == null)
	        {
	        	request.getSession().setAttribute("token", String.valueOf(Calendar.getInstance().getTimeInMillis()));
	        }
	        // do a access log
	        Calendar endDt = Calendar.getInstance();
	        long timeSpan = endDt.getTimeInMillis() - startDt.getTimeInMillis();
	        log(timeSpan,null,name, 1, request);
	        return fwd;
		}
		catch (Exception ex)
		{
	        Calendar endDt = Calendar.getInstance();
	        long timeSpan = endDt.getTimeInMillis()-startDt.getTimeInMillis();
			log(timeSpan, ex.toString(),name,0, request);
			throw ex;
		}
	}
	
	private void log(long timeSpan, String ExName,String method, int result, HttpServletRequest request)
	{
		String auditMode = oscar.OscarProperties.getInstance().getProperty("audit_mode");
        if (auditMode == null || !auditMode.equals("on")) return;
        HttpSession session = request.getSession();
        String providerNo = (String) session.getAttribute(KeyConstants.SESSION_KEY_PROVIDERNO);
        String className = this.toString();
        if(method == null) method = "unspecified";
        if (request.getParameter("tab") != null) method = method + "(" + request.getParameter("tab") + ")";
		if(method.equals("unspecified") && request.getParameter("method") != null) method = method + "(" + request.getParameter("method") + ")";
        Integer programId = null;
        try {
        	programId = (Integer) request.getAttribute("programId");
        }
        catch(Exception ex)
        {
            if(request.getAttribute("programId") != null) programId = Integer.valueOf((String) request.getAttribute("programId"));
        }
        if(programId == null) programId = new Integer(0);
        String clientId = "";
        try {
        	clientId = (String) request.getAttribute("clientId");
        }
        catch(Exception ex)
        {
        	if (request.getAttribute("clientId") != null)
        	clientId = ((Integer) request.getAttribute("clientId")).toString();
        }
        Integer shelterId = (Integer) session.getAttribute(KeyConstants.SESSION_KEY_SHELTERID);
        if(shelterId == null) shelterId = new Integer(0);
        String sessionId = request.getSession().getId();
        String queryString = request.getRequestURI()  + '?' + request.getQueryString();
        if(clientId == null) 
        {
            HashMap actionParam = (HashMap) request.getAttribute("actionParam");
            if(actionParam!=null){
            	clientId = (String) actionParam.get("clientId"); 
            }
        }
       
	}
	protected String getClientId(HttpServletRequest request){
		String clientId=request.getParameter("demoNo");
		if(Utility.isNotNullOrEmptyStr(clientId)) clientId=request.getParameter("clientId");
		return clientId;
	}

	protected Integer getParameterAsInteger(HttpServletRequest request, String name, Integer defaultVal) {
		String param = request.getParameter(name);
		if(!(param==null || param.equals("null") || param.equals(""))) {
			return Integer.valueOf(param);
		}
		return defaultVal;
	}
	
	protected Boolean getParameterAsBoolean(HttpServletRequest request, String name, Boolean defaultVal) {
		String param = request.getParameter(name);
		if(param != null) {
			return Boolean.valueOf(param);
		}
		return defaultVal;
	}
	
	protected Boolean getParameterAsBoolean(HttpServletRequest request, String name) {
		return getParameterAsBoolean(request,name,false);
	}
}
