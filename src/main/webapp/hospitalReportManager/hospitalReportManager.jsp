<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%-- This JSP is the first page you see when you enter 'report by template' --%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>

<%@ page import="org.oscarehr.util.SpringUtils"%>
<%@ page import="java.util.*"%>
<%@ page import="org.oscarehr.hospitalReportManager.SFTPConnector, org.oscarehr.hospitalReportManager.dao.HRMProviderConfidentialityStatementDao" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Hospital Report Manager</title>
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css">

<script type="text/javascript" language="JavaScript"
	src="../share/javascript/prototype.js"></script>
<script type="text/javascript" language="JavaScript"
	src="../share/javascript/Oscar.js"></script>
	
<script type="text/javascript">
function runFetch() {
	window.location = "<%=request.getContextPath() %>/hospitalReportManager/hospitalReportManager.jsp?fetch=true";
}
</script>

<style type="text/css">
table.outline {
	margin-top: 50px;
	border-bottom: 1pt solid #888888;
	border-left: 1pt solid #888888;
	border-top: 1pt solid #888888;
	border-right: 1pt solid #888888;
}

table.grid {
	border-bottom: 1pt solid #888888;
	border-left: 1pt solid #888888;
	border-top: 1pt solid #888888;
	border-right: 1pt solid #888888;
}

td.gridTitles {
	border-bottom: 2pt solid #888888;
	font-weight: bold;
	text-align: center;
}

td.gridTitlesWOBottom {
	font-weight: bold;
	text-align: center;
}

td.middleGrid {
	border-left: 1pt solid #888888;
	border-right: 1pt solid #888888;
	text-align: center;
}

label {
	float: left;
	width: 120px;
	font-weight: bold;
}

label.checkbox {
	float: left;
	width: 116px;
	font-weight: bold;
}

label.fields {
	float: left;
	width: 80px;
	font-weight: bold;
}

span.labelLook {
	font-weight: bold;
}

input,textarea,select { //
	margin-bottom: 5px;
}

textarea {
	width: 450px;
	height: 100px;
}

.boxes {
	width: 1em;
}

#submitbutton {
	margin-left: 120px;
	margin-top: 5px;
	width: 90px;
}

br {
	clear: left;
}
</style>
</head>

<body vlink="#0000FF" class="BodyStyle">

<% if (request.getParameter("fetch") != null && request.getParameter("fetch").equalsIgnoreCase("true"))
		SFTPConnector.startAutoFetch();
%>
<table class="MainTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar" style="width: 100%;">
			<tr>
				<td>Hospital Report Manager</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top" width="160px;">
		&nbsp;</td>
		<td class="MainTableRightColumn" valign="top">
		HRM Status: <%=SFTPConnector.isFetchRunning() ? "Fetching data from HRM" : "Idle" %><br />
		<% if (!SFTPConnector.isFetchRunning()) { %>
			<input type="button" onClick="runFetch()" value="Fetch New Data from HRM" />
		<% } else { %>
			Please wait until the current fetch task completes before requesting another data fetch.
		<% } %>
		</td>
	</tr>
	<tr>
       	<td class="MainTableLeftColumn" valign="top" width="160px;">&nbsp;</td>
        <td class="MainTableRightColumn" valign="top">
        <% if (request.getAttribute("success") != null) { %>
                <%=((Boolean)request.getAttribute("success")) ? "Successful" : "Error" %>
        <% } %>
        <form enctype="multipart/form-data" action="<%=request.getContextPath() %>/hospitalReportManager/UploadLab.do" method="post">
	    Upload an HRM report from your computer: <input type="file" name="importFile" /> <input type="submit" name="submit" value="Upload" />
        </form>
        </td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top" width="160px;">&nbsp;</td>
		<td class="MainTableRightColumn" valign="top">
			<%
			HRMProviderConfidentialityStatementDao hrmProviderConfidentialityStatementDao = (HRMProviderConfidentialityStatementDao) SpringUtils.getBean("HRMProviderConfidentialityStatementDao");
			String statement = hrmProviderConfidentialityStatementDao.getConfidentialityStatementForProvider(LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo());
			%>
			<form action="<%=request.getContextPath() %>/hospitalReportManager/Statement.do" method="post">
			Provider Confidentiality Statement:<br />
			<textarea name="statement"><%=statement %></textarea><br />
			<input type="submit" name="submit" value="Save Statement" /> <% if (request.getAttribute("statementSuccess") != null && (Boolean) request.getAttribute("statementSuccess")) { %>Success<% } else if (request.getAttribute("statementSuccess") != null && !((Boolean) request.getAttribute("statementSuccess")))  { %>Error<% } %>
			</form>

			<br />
			<input type="button" value="I don't want to receive any more HRM outtage messages for this outtage instance" onclick="window.location='disable_msg_action.jsp'" />
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>

		<td class="MainTableBottomRowRightColumn">&nbsp;</td>
	</tr>
</table>



</html:html>
