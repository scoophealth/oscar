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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.util.SpringUtils;

import oscar.log.LogAction;
import oscar.log.LogConst;

import com.quatro.common.KeyConstants;
import com.quatro.model.LookupCodeValue;
import com.quatro.service.LookupManager;
import com.quatro.service.security.SecurityManager;
import com.quatro.service.security.UserAccessManager;

public final class ShelterSelectionAction extends DispatchAction {

    private ProviderManager providerManager = (ProviderManager) SpringUtils.getBean("providerManager");
    private LookupManager lookupManager = (LookupManager) SpringUtils.getBean("lookupManager");
   
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	 String mthd =request.getParameter("method");
    	// initMenu(request);
         if(mthd!=null && mthd.equals("select")){
        	 select(mapping,form,request,response);
        	 return mapping.findForward("home");
         }
         
    	String providerNo=(String)request.getSession().getAttribute(KeyConstants.SESSION_KEY_PROVIDERNO);
    	List shelters=providerManager.getShelterIds(providerNo);
    	
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
             request.getSession().setAttribute(KeyConstants.SESSION_KEY_SHELTERID , shelterId);
             request.getSession().setAttribute(KeyConstants.SESSION_KEY_SHELTER, shelter);             
            // LogAction.addLog(strAuth[0], LogConst.LOGIN, LogConst.CON_LOGIN, "shelterId="+shelterId, ip);
         }
         else {
             request.getSession().setAttribute(KeyConstants.SESSION_KEY_SHELTERID, new Integer(0));
             request.getSession().setAttribute(KeyConstants.SESSION_KEY_SHELTER, new LookupCodeValue());
         }
         // initiate security manager
         UserAccessManager userAccessManager = (UserAccessManager) SpringUtils.getBean("userAccessManager");
         
         SecurityManager secManager = userAccessManager.getUserSecurityManager(providerNo,null,lookupManager);
         request.getSession().setAttribute(KeyConstants.SESSION_KEY_SECURITY_MANAGER, secManager);
        
         return mapping.findForward("home");
            	
    }
    public ActionForward select(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	
		String shelter =request.getParameter("shelterId");
		String providerNo = (String) request.getSession().getAttribute(KeyConstants.SESSION_KEY_PROVIDERNO);
		Integer shelterId = new Integer(shelter);
		request.getSession().setAttribute(KeyConstants.SESSION_KEY_SHELTERID,shelterId );
		LookupCodeValue shelterObj=lookupManager.GetLookupCode("SHL",shelter);
        request.getSession().setAttribute(KeyConstants.SESSION_KEY_SHELTER, shelterObj);
        
        // initiate security manager
        UserAccessManager userAccessManager = (UserAccessManager) SpringUtils.getBean("userAccessManager");
         
        SecurityManager secManager = userAccessManager.getUserSecurityManager(providerNo,shelterId,lookupManager);
        request.getSession().setAttribute(KeyConstants.SESSION_KEY_SECURITY_MANAGER, secManager);
        String ip = request.getRemoteAddr();
        LogAction.addLog(providerNo, LogConst.CON_LOGIN, LogConst.SHELTER_SELECTION, shelterId.toString(), ip);
        
        return mapping.findForward("home");
    }
    
	
}
