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
<%@ include file="/common/webAppContextAndSuperMgr.jsp"%>

<%@ page import="java.sql.*, java.util.*, oscar.*"
	errorPage="errorpage.jsp"%>


<%@page import="org.oscarehr.common.model.ProviderPreference"%>
<%@page import="org.oscarehr.web.admin.ProviderPreferencesUIBean"%><html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script LANGUAGE="JavaScript">
<!--
function start(){
  this.focus();
}
function closeit() {
  self.opener.refresh1();
  close();
}   
//-->
</script>
</head>

<body>
<center>
<table border="0" cellspacing="0" cellpadding="0" width="90%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF"><bean:message
			key="receptionist.receptionistupdatepreference.description" /></font></th>
	</tr>
</table>
<%
	ProviderPreference providerPreference=ProviderPreferencesUIBean.updateOrCreateProviderPreferences(request);

	//--- 
	session.setAttribute(SessionConstants.LOGGED_IN_PROVIDER_PREFERENCE, providerPreference);
	session.setAttribute("default_servicetype", providerPreference.getDefaultServiceType());
	session.setAttribute("newticklerwarningwindow", providerPreference.getNewTicklerWarningWindow());
	session.setAttribute("default_pmm", providerPreference.getDefaultCaisiPmm());

%>
<script LANGUAGE="JavaScript">
	self.opener.refresh1();
	self.close();
</script>
<p></p>
<hr width="90%"/>
<form><input type="button"
	value='<bean:message key="receptionist.receptionistupdatepreference.btnClose"/>'
	onClick="closeit()"></form>
</center>
</body>
</html:html>
