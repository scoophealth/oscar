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

<%@page import="org.oscarehr.util.SessionConstants"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>
<%@ page import="java.sql.*, java.util.*" errorPage="errorpage.jsp"%>
<%@ page import="oscar.login.DBHelp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />

<%@page import="org.oscarehr.common.model.ProviderPreference"%>
<%@page import="org.oscarehr.web.admin.ProviderPreferencesUIBean"%>
<%
    if(session.getAttribute("user") == null ) response.sendRedirect("../logout.jsp");
    String curProvider_no = (String) session.getAttribute("user");

    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    
    boolean isSiteAccessPrivacy=false;
%>


<security:oscarSec objectName="_site_access_privacy" roleName="<%=roleName$%>" rights="r" reverse="false">
	<%isSiteAccessPrivacy=true; %>
</security:oscarSec>
	
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="admin.preferenceaddpreference.title" /></title>
<link rel="stylesheet" href="../web.css">
</head>
<body background="../images/gray_bg.jpg" bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF"><bean:message key="admin.preferenceaddpreference.description" /></font></th>
	</tr>
</table>
<%
boolean isAccessPass = true;

if (isSiteAccessPrivacy) {
	//multisite : check provider no
	String provider_no = request.getParameter("provider_no");
	String sqlStr = "select COUNT(*) as cou from providersite where provider_no = " + provider_no 
		+ " and site_id in (select site_id from providersite where provider_no = " + curProvider_no + ") ";
	DBHelp dbObj = new DBHelp();
	ResultSet rs = dbObj.searchDBRecord(sqlStr);
	
    if(rs.next()){          
          int ret = rs.getInt("cou");
          if(ret >= 1){
        	  isAccessPass = true;
          }
          else {
        	  isAccessPass = false;
          }
    }
    rs.close();
}

if (!isSiteAccessPrivacy || isAccessPass) {
	ProviderPreference providerPreference=ProviderPreferencesUIBean.updateOrCreateProviderPreferences(request);

	//---
	session.setAttribute(SessionConstants.LOGGED_IN_PROVIDER_PREFERENCE, providerPreference);
	session.setAttribute("default_servicetype", providerPreference.getDefaultServiceType());
	session.setAttribute("newticklerwarningwindow", providerPreference.getNewTicklerWarningWindow());
	session.setAttribute("default_pmm", providerPreference.getDefaultCaisiPmm());
%>
<h1><bean:message key="admin.preferenceaddarecord.msgSuccessful" />
</h1>
	<%  
} 
else {
	%>
	<h1><bean:message key="admin.preferenceaddpreference.msgAccessFailure" />
<%	
}
%> <!-- footer -->
<hr width="100%" color="navy">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr>
		<td><a href="admin.jsp"> <img src="../images/leftarrow.gif"
			border="0" width="25" height="20" align="absmiddle"><bean:message
			key="global.btnBack" /></a></td>
		<td align="right"><a href="../logout.jsp"><bean:message
			key="global.btnLogout" /><img src="../images/rightarrow.gif"
			border="0" width="25" height="20" align="absmiddle"></a></td>
	</tr>
</table>

</center>
</body>
</html:html>
