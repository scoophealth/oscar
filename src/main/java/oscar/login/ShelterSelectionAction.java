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

package oscar.login;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.PMmodule.web.BaseAction;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.log.LogAction;
import oscar.log.LogConst;

import com.quatro.common.KeyConstants;
import com.quatro.model.LookupCodeValue;
import com.quatro.service.LookupManager;
import com.quatro.service.security.SecurityManager;
import com.quatro.service.security.UserAccessManager;

public final class ShelterSelectionAction extends BaseAction {

    private ProviderManager providerManager = (ProviderManager) SpringUtils.getBean("providerManager");
    private LookupManager lookupManager = (LookupManager) SpringUtils.getBean("lookupManager");
    private static final Logger _logger = Logger.getLogger(LoginAction.class);
    private static final String LOG_PRE = "Login!@#$: ";

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	 String mthd =request.getParameter("method");
    	// initMenu(request);
         if(mthd!=null && mthd.equals("select")){
        	 select(mapping,form,request,response);
        	 return mapping.findForward("home");
         }
         
    	String providerNo=(String)request.getSession(true).getAttribute(KeyConstants.SESSION_KEY_PROVIDERNO);
    	List shelters=providerManager.getShelterIds(providerNo);
    	ActionMessages messages = new ActionMessages();
      
         if (shelters.size() > 1) {
         	String shlts = String.valueOf(shelters.get(0));
         	for(int i=1; i<shelters.size(); i++)
         	{
         		shlts += "," + String.valueOf(shelters.get(i));
         	}
         	List facilityCodes = lookupManager.LoadCodeList("SHL", false, shlts, null);
         	request.setAttribute("shelters", facilityCodes);
	        response.setHeader("Expires", "-1");
	        response.setHeader("Cache-Control",
	        	"must-revalidate, post-check=0, pre-check=0");
	        response.setHeader("Pragma", "private");

             return(mapping.findForward("shelterSelection"));
         }
         else if (shelters.size() == 1) {
             Integer shelterId = (Integer) shelters.get(0);
             LookupCodeValue shelter=lookupManager.GetLookupCode("SHL",String.valueOf(shelterId));
             request.getSession(true).setAttribute(KeyConstants.SESSION_KEY_SHELTERID , shelterId);
             request.getSession(true).setAttribute(KeyConstants.SESSION_KEY_SHELTER, shelter);             
            // LogAction.addLog(strAuth[0], LogConst.LOGIN, LogConst.CON_LOGIN, "shelterId="+shelterId, ip);
         }
         else {
             request.getSession(true).setAttribute(KeyConstants.SESSION_KEY_SHELTERID, new Integer(0));
             request.getSession(true).setAttribute(KeyConstants.SESSION_KEY_SHELTER, new LookupCodeValue());
         }
         // initiate security manager
         UserAccessManager userAccessManager = (UserAccessManager) getAppContext().getBean("userAccessManager");
         
         SecurityManager secManager = userAccessManager.getUserSecurityManager(providerNo,null,lookupManager);
         request.getSession(true).setAttribute(KeyConstants.SESSION_KEY_SECURITY_MANAGER, secManager);
        
         return mapping.findForward("home");
            	
    }
    public ActionForward select(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	
		String shelter =request.getParameter("shelterId");
		String providerNo = (String) request.getSession().getAttribute(KeyConstants.SESSION_KEY_PROVIDERNO);
		Integer shelterId = new Integer(shelter);
		request.getSession(true).setAttribute(KeyConstants.SESSION_KEY_SHELTERID,shelterId );
		LookupCodeValue shelterObj=lookupManager.GetLookupCode("SHL",shelter);
        request.getSession(true).setAttribute(KeyConstants.SESSION_KEY_SHELTER, shelterObj);
        
        // initiate security manager
        UserAccessManager userAccessManager = (UserAccessManager) getAppContext().getBean("userAccessManager");
         
        SecurityManager secManager = userAccessManager.getUserSecurityManager(providerNo,shelterId,lookupManager);
        request.getSession(true).setAttribute(KeyConstants.SESSION_KEY_SECURITY_MANAGER, secManager);
        String ip = request.getRemoteAddr();
        LogAction.addLog(providerNo, LogConst.CON_LOGIN, LogConst.SHELTER_SELECTION, shelterId.toString(), ip);
        
        return mapping.findForward("home");
    }
    private void initMenu(HttpServletRequest request)
	{
		SecurityManager sec = getSecurityManager(request);
		if (sec==null) return;
		//Client Management
		if (sec.GetAccess(KeyConstants.FUN_CLIENT, "").compareTo("r") >= 0) {
			request.getSession(true).setAttribute(KeyConstants.MENU_CLIENT, KeyConstants.ACCESS_VIEW);
		} else
			request.getSession(true).setAttribute(KeyConstants.MENU_CLIENT, KeyConstants.ACCESS_NULL);
	
		//Program
		if (sec.GetAccess(KeyConstants.FUN_PROGRAM, "").compareTo("r") >= 0) {
			request.getSession(true).setAttribute(KeyConstants.MENU_PROGRAM, KeyConstants.ACCESS_VIEW);
		} else
			request.getSession(true).setAttribute(KeyConstants.MENU_PROGRAM, KeyConstants.ACCESS_NULL);

		//Facility Management
		if (sec.GetAccess(KeyConstants.FUN_FACILITY, "").compareTo("r") >= 0) {
			request.getSession(true).setAttribute(KeyConstants.MENU_FACILITY, KeyConstants.ACCESS_VIEW);
		} else
			request.getSession(true).setAttribute(KeyConstants.MENU_FACILITY, KeyConstants.ACCESS_NULL);

		//Report Runner
		if (sec.GetAccess(KeyConstants.FUN_REPORTS, "").compareTo("r") >= 0) {
			request.getSession(true).setAttribute(KeyConstants.MENU_REPORT, KeyConstants.ACCESS_VIEW);
		} else
			request.getSession(true).setAttribute(KeyConstants.MENU_REPORT, KeyConstants.ACCESS_NULL);

		//System Admin
		if (OscarProperties.getInstance().isAdminOptionOn() && sec.GetAccess("_admin", "").compareTo("r") >= 0) {
			request.getSession(true).setAttribute(KeyConstants.MENU_ADMIN, KeyConstants.ACCESS_VIEW);
		} else
			request.getSession(true).setAttribute(KeyConstants.MENU_ADMIN, KeyConstants.ACCESS_NULL);
		request.getSession(true).setAttribute(KeyConstants.MENU_HOME, KeyConstants.ACCESS_VIEW);
		request.getSession(true).setAttribute(KeyConstants.MENU_TASK, KeyConstants.ACCESS_VIEW);
	}
	
}
