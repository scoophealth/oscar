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


package oscar.oscarEncounter.oscarConsultationRequest.pageUtil;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import oscar.OscarProperties;

public class EConsultAction extends Action {
   
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		OscarProperties oscarProperties = OscarProperties.getInstance();
		
		//Gets the request URL
		StringBuffer oscarUrl = request.getRequestURL();
		//Determines the initial length by subtracting the length of the servlet path from the full url's length
		Integer urlLength = oscarUrl.length() - request.getServletPath().length();

		//Sets the length of the URL, found by subtracting the length of the servlet path from the length of the full URL, that way it only gets up to the context path
		oscarUrl.setLength(urlLength);
		
		String backendEconsultUrl = oscarProperties.getProperty("backendEconsultUrl");
		String frontendEconsultUrl = oscarProperties.getProperty("frontendEconsultUrl");

		String oneIdEmail = request.getSession().getAttribute("oneIdEmail") != null ? request.getSession().getAttribute("oneIdEmail").toString() : "";
		String delegateOneIdEmail = request.getSession().getAttribute("delegateOneIdEmail") != null ? request.getSession().getAttribute("delegateOneIdEmail").toString() : "";
		String delegateEmailQueryString = "";
		String providerEmail = oneIdEmail;
		
		//If there is a delegateOneIdEmail then it is used as the normal oneId email and the current user is the delegate as they are delegating for that person
		if (!delegateOneIdEmail.equals("")) {
			delegateEmailQueryString = "&delegate_oneid_email=" + oneIdEmail;

			providerEmail = delegateOneIdEmail;
		}
		
		if (oneIdEmail != null && !oneIdEmail.equals("")) {
			response.sendRedirect(frontendEconsultUrl + "/?oneid_email=" + providerEmail + delegateEmailQueryString + "#!/dashboard");
		}
		else {
			response.sendRedirect(backendEconsultUrl + "/SAML2/login?oscarReturnURL=" + URLEncoder.encode(oscarUrl + "/econsultSSOLogin.do", "UTF-8") + "&loginStart=" + new Date().getTime() / 1000);
		}
		
		return  null;
	}
}