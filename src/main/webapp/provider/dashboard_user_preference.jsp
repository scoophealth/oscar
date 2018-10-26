<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>
<%@ include file="/casemgmt/taglibs.jsp"%>
<%@page import="java.util.*" %>
<%@page import="org.apache.log4j.Logger" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.model.Provider,org.oscarehr.PMmodule.dao.ProviderDao"%>
<%
Logger logger = org.oscarehr.util.MiscUtils.getLogger();
//Get list of providers           
String curProviderNo = (String) session.getAttribute("user"); 
ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
Provider provider = providerDao.getProvider(curProviderNo);

logger.info("user: " + curProviderNo);
List<Provider> providerList = null;
providerList = providerDao.getBillableProviders();
// for (Provider p: providerList) {
//      logger.info("provider: " + p.getFullName());
// }
%>

<%
//   String curUser_no;
//   curUser_no = (String) session.getAttribute("user");

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request" />
<html:html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<html:base />
<title><bean:message key="provider.dashboardUserPrefs" /></title>

<link rel="stylesheet" type="text/css" href="../oscarEncounter/encounterStyles.css">
<link rel="stylesheet" type="text/css" media="all" href="<c:out value="${ctx}"/>/share/calendar/calendar.css" title="win2k-cold-1">
<script src="<c:out value="${ctx}"/>/share/javascript/prototype.js"	type="text/javascript"></script>
<script src="<c:out value="${ctx}"/>/share/javascript/scriptaculous.js"	type="text/javascript"></script>
<script src="<c:out value="${ctx}"/>/js/jquery.js"></script>
<script>
	jQuery.noConflict();
</script>


</head>

<body class="BodyStyle" vlink="#0000FF">

<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn"><bean:message key="provider.setNoteStaleDate.msgPrefs" /></td>
		<td style="color: white" class="MainTableTopRowRightColumn"><bean:message key="provider.dashboardUserPrefs" /></td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn">&nbsp;</td>
		<td class="MainTableRightColumn">
			<!-- form starts here -->
			<form action="<c:out value="${ctx}"/>/provider/DashboardUserPreference.do?method=save" method="post">
			<table width="100%" border="1">
			<tr>
				<th width="20%">Default Dashboard User</th>
				<td colspan="3">
					<%
						String val1 = (String)request.getAttribute("dashboardUser");
						if(val1 == null) val1 = curProviderNo;
					%>
					<select id="dashboardUser" name="dashboardUser">
					<option value="" <%=(val1.equals("")?"selected=\"selected\"":"") %>></option>
					<%for(Provider p: providerList) {%>
						<option value="<%=p.getProviderNo()%>"<%=(val1.equals(p.getProviderNo())?"selected=\"selected\"":"") %>><%=p.getFullName()%></option>
						<%}%>
					</select>
				</td>
			</tr>
			<tr>
				<th width="20%">Share With Ontario Common Dashboard</th>
				<td width="30%">
					<%
						val1 = (String)request.getAttribute("shareDashboard");
						if(val1 == null) val1 = "false";
					%>
					<select id="shareDashboard" name="shareDashboard">
						<option value="" <%=(val1.equals("")?"selected=\"selected\"":"") %>></option>
						<option value="true" <%=(val1.equals("YES")?"selected=\"selected\"":"") %>>YES</option>
						<option value="false" <%=(val1.equals("NO")?"selected=\"selected\"":"") %>>NO</option>
					</select>
			</td>
			</tr>
								
			
		</table>
			<input type="submit" value="Save Changes"/>
			</form>
			<!-- end of form -->
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>
</body>
</html:html>
