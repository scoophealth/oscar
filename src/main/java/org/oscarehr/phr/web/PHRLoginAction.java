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


package org.oscarehr.phr.web;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.ProviderPreferenceDao;
import org.oscarehr.common.model.ProviderPreference;
import org.oscarehr.phr.PHRAuthentication;
import org.oscarehr.phr.service.PHRService;
import org.oscarehr.util.EncryptionUtils;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.WebUtils;

public class PHRLoginAction extends DispatchAction
{
	private static Logger log = MiscUtils.getLogger();

	public PHRLoginAction()
	{
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		HttpSession session = request.getSession();

		String providerNo = (String)session.getAttribute("user");
		PHRAuthentication phrAuth = null;
		String forwardTo = request.getParameter("forwardto");

		ActionForward ar = new ActionForward(forwardTo);
		request.setAttribute("forwardToOnSuccess", request.getParameter("forwardToOnSuccess"));

		if (!PHRService.canAuthenticate(providerNo))
		{
			request.setAttribute("phrUserLoginErrorMsg", "You have not registered for MyOSCAR");
			request.setAttribute("phrTechLoginErrorMsg", "No MyOSCAR information in the database");
			return ar;
		}

		String myoscarPassword=request.getParameter("phrPassword");
		try
		{
			phrAuth = PHRService.authenticate(providerNo, myoscarPassword);

			if (phrAuth == null)
			{
				request.setAttribute("phrUserLoginErrorMsg", "Incorrect user/password");
				return ar;
			}
		}
		catch (Exception e)
		{
			MiscUtils.getLogger().error("Error", e);
			request.setAttribute("phrUserLoginErrorMsg", "Error contacting MyOSCAR server, please try again later");
			request.setAttribute("phrTechLoginErrorMsg", e.getMessage());
			return ar;
		}
		
		session.setAttribute(PHRAuthentication.SESSION_PHR_AUTH, phrAuth);

		boolean saveMyOscarPassword=WebUtils.isChecked(request, "saveMyOscarPassword");
		if (saveMyOscarPassword) saveMyOscarPassword(session, myoscarPassword);
				
		log.debug("Correct user/pass, auth success");
		return ar;
	}

	private void saveMyOscarPassword(HttpSession session, String myoscarPassword) {
		try {
	        SecretKeySpec key=EncryptionUtils.getDeterministicallyMangledPasswordSecretKeyFromSession(session);
	        byte[] encryptedMyOscarPassword=EncryptionUtils.encrypt(key, myoscarPassword.getBytes("UTF-8"));

	        LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
	        
	        ProviderPreferenceDao providerPreferenceDao=(ProviderPreferenceDao) SpringUtils.getBean("providerPreferenceDao");
	        ProviderPreference providerPreference=providerPreferenceDao.find(loggedInInfo.loggedInProvider.getProviderNo());
	        providerPreference.setEncryptedMyOscarPassword(encryptedMyOscarPassword);
	        providerPreferenceDao.merge(providerPreference);
        } catch (Exception e) {
	        log.error("Error saving myoscarPassword.", e);
        }	    
    }
}
