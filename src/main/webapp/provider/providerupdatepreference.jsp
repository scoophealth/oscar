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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>
<%@ page import="java.sql.*, java.util.*, oscar.*" errorPage="errorpage.jsp"%>
<%@page import="org.oscarehr.common.model.ProviderPreference"%>
<%@page import="org.oscarehr.web.admin.ProviderPreferencesUIBean"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@ page import="org.oscarehr.common.dao.UserPropertyDAO" %>
<%@ page import="org.oscarehr.common.model.UserProperty" %>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script LANGUAGE="JavaScript">
    <!--
    function start(){
      this.focus();
    }
    //-->
</script>
</head>

<body>
<center>
<table border="0" cellspacing="0" cellpadding="0" width="90%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF"><bean:message	key="provider.providerupdatepreference.description" /></font></th>
	</tr>
</table>
<%
	String programId_forCME = request.getParameter("case_program_id");
	request.getSession().setAttribute("case_program_id",programId_forCME);
	
	String selected_site = (String) request.getParameter("site") ;
	if (selected_site != null) {
		session.setAttribute("site_selected", (selected_site.equals("none") ? null : selected_site) );	    
	}
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	String curUser_providerno = loggedInInfo.getLoggedInProviderNo();
	String ticklerforproviderno = request.getParameter("ticklerforproviderno");
	UserPropertyDAO propDao =(UserPropertyDAO)SpringUtils.getBean("UserPropertyDAO");
	UserProperty prop = propDao.getProp(curUser_providerno, UserProperty.PROVIDER_FOR_TICKLER_WARNING);
	if (prop == null) {
		prop = new UserProperty();
		prop.setProviderNo(curUser_providerno);
		prop.setName(UserProperty.PROVIDER_FOR_TICKLER_WARNING);
	}
	prop.setValue(ticklerforproviderno);
	propDao.saveProp(prop);
	
	ProviderPreference providerPreference=ProviderPreferencesUIBean.updateOrCreateProviderPreferences(request);

	//--- 
	session.setAttribute(SessionConstants.LOGGED_IN_PROVIDER_PREFERENCE, providerPreference);
	session.setAttribute("default_servicetype", providerPreference.getDefaultServiceType());
	session.setAttribute("newticklerwarningwindow", providerPreference.getNewTicklerWarningWindow());
	session.setAttribute("default_pmm", providerPreference.getDefaultCaisiPmm());
	session.setAttribute("caisiBillingPreferenceNotDelete",providerPreference.getDefaultDoNotDeleteBilling());
	session.setAttribute("defaultDxCode",providerPreference.getDefaultDxCode());
%>
<script LANGUAGE="JavaScript">
     self.opener.refresh1();
     self.close();
</script>
<p></p>
<hr width="90%"/>
<form><input type="button"
	value=<bean:message key="global.btnClose"/> onClick="self.close()">
</form>
</center>
</body>
</html:html>
