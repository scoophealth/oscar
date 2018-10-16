<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.common.model.UserProperty"%>
<%@page import="org.oscarehr.common.dao.UserPropertyDAO"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="oscar.OscarProperties"%>
<%@page import="java.util.HashMap, oscar.log.*"
	errorPage="errorpage.jsp"%>
	
<%
	String message = null;    		
	String econsultUrl = OscarProperties.getInstance().getProperty("backendEconsultUrl");
	StringBuffer oscarUrl = request.getRequestURL();
	oscarUrl.setLength(oscarUrl.length() - request.getServletPath().length());
	String redirectURL = econsultUrl + "/SAML2/logout?oscarReturnURL=" + URLEncoder.encode(oscarUrl + "/logout.jsp","UTF-8");
	
			
	UserPropertyDAO userPropertyDAO = SpringUtils.getBean(UserPropertyDAO.class);
	UserProperty prop = userPropertyDAO.getProp(LoggedInInfo.getLoggedInInfoFromSession(request).getLoggedInProviderNo(), "clinicalConnectDisableLogoutWarning");
	if(prop != null && "true".equals(prop.getValue()) ) {
		response.sendRedirect(redirectURL);
		return;
	}
	
			
	if(request.getSession().getAttribute("CC_EHR_LOADED") == null) {
		if (request.getSession().getAttribute("oneIdEmail") != null && !request.getSession().getAttribute("oneIdEmail").equals("") ) {
			
			response.sendRedirect(redirectURL);
			return;
		 }  else {
			 response.sendRedirect(request.getContextPath() + "/logout.jsp");
		 }
	} else {
		message = "Please be aware that any browser windows that you were working on will remain open. To ensure patient privacy, close all browser windows containing confidential or private health information (PHI) after logging out of your EMR.";
		message = "Warning: Any browser windows that you opened during your session will remain open. To ensure patient privacy, close all browser windows containing confidential or private health information (PHI) after signing out.";
	}

%>
<html>
<head>
<meta http-equiv="refresh" content="10; url=<%=econsultUrl + "/SAML2/logout?oscarReturnURL=" + URLEncoder.encode(oscarUrl + "/logout.jsp", "UTF-8") %>">

</head>

<body>
<br/></br/><br/>
<center><img src="images/oscar_emr_logo.png"/></center>
<br/>

<div style="margin-left:auto;margin-right:auto;width:40em;font-size:15pt"><%=message %></div>
<br/>
<div style="margin-left:auto;margin-right:auto;width:40em;font-size:15pt;text-align:center">
CONFIRM SIGN OUT*
<br/>
<input type="button" value="SIGN OUT" onClick="window.location.href='<%=econsultUrl + "/SAML2/logout?oscarReturnURL=" + URLEncoder.encode(oscarUrl + "/logout.jsp", "UTF-8") %>'"/>
<input type="button" value="CANCEL" onClick="window.history.back();"/>
<br/>

<br/><br/>
<h6>* If not action is taken in 10 seconds, you will automatically be signed out. To disable this warning, to go Preferences -> ClinicalConnect settings.</h6>
</div>

</body>
</html>
