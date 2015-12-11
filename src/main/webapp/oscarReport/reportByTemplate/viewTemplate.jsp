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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%--This JSP is the 'view template XML' jsp from the report configuraiton screen--%>


<%@ page
	import="oscar.oscarReport.reportByTemplate.*, org.apache.commons.lang.StringEscapeUtils"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting,_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_report&type=_admin.reporting&type=_admin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Report by Template</title>
<link rel="stylesheet" type="text/css"
	href="../../share/css/OscarStandardLayout.css">
<link rel="stylesheet" type="text/css" href="reportByTemplate.css">
<style type="text/css" media="print">
.MainTableTopRow,.MainTableLeftColumn,.noprint,.showhidequery,.sqlBorderDiv,.MainTableBottomRow
	{
	display: none;
}

.xmlBorderDiv {
	border: 0px;
}

.MainTableRightColumn {
	border: 0;
}
</style>
</head>

<body vlink="#0000FF" class="BodyStyle">

<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn"><bean:message
			key="oscarReport.CDMReport.msgReport" /></td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar" style="width: 100%;">
			<tr>
				<td>Report by Template</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<%String templateid = request.getParameter("templateid");
        if (templateid == null) templateid = (String) request.getAttribute("templateid");
        if (templateid == null) { %>
		<jsp:forward page="homePage.jsp" />
		<%}
        ReportObject curreport = (new ReportManager()).getReportTemplateNoParam(templateid);
        String xml = (new ReportManager()).getTemplateXml(templateid);%>
		<td class="MainTableLeftColumn" valign="top" width="160px;"><jsp:include
			page="listTemplates.jsp">
			<jsp:param name="templateviewid"
				value="<%=curreport.getTemplateId()%>" />
		</jsp:include></td>
		<td class="MainTableRightColumn" valign="top">
		<div class="reportTitle"><%=curreport.getTitle()%></div>
		<div class="reportDescription"><%=curreport.getDescription()%></div>
		<div class="xmlBorderDiv"><pre wrap="on"
			style="font-size: 11px;"><%=StringEscapeUtils.escapeHtml(xml)%></pre>
		</div>
		
		<div class="noprint" style="clear: left; float: left; margin-top: 15px;">
			<input type="button" value="Back" onclick="javascript: history.go(-1);return false;"> 
			<input type="button" value="Print" onclick="javascript: window.print();">
			<input type="button" value="Edit Template" onclick="document.location='addEditTemplate.jsp?templateid=<%=templateid%>&opentext=1'">
			<a href="exportTemplateAction.do?templateid=<%=templateid%>&name=<%=curreport.getTitle()%>" class="link">Export Template to K2A</a>
		</div>
		</td>
	</tr>
	<tr class="MainTableBottomRow">
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>

		<td class="MainTableBottomRowRightColumn">&nbsp;</td>
	</tr>
</table>
</html:html>
