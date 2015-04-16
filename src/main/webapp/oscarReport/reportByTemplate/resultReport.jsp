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
<%--This JSP displays the result of the report query--%>
<%
  if(session.getValue("user") == null) response.sendRedirect("../../logout.jsp");
  String roleName$ = (String)session.getAttribute("userrole") + "," + (String)session.getAttribute("user");
%>

<%@ page
	import="java.util.*,oscar.oscarReport.reportByTemplate.*,java.sql.*, org.apache.commons.lang.StringEscapeUtils"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>

<security:oscarSec roleName="<%=roleName$%>"
	objectName="_admin,_report"	rights="r" reverse="<%=true%>">
	<%
		response.sendRedirect("../logout.jsp");
	%>
</security:oscarSec>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Report by Template</title>
<link rel="stylesheet" type="text/css"
	href="../../share/css/OscarStandardLayout.css">
<link rel="stylesheet" type="text/css" href="reportByTemplate.css">
<script type="text/javascript" language="JavaScript"
	src="../../share/javascript/prototype.js"></script>
<script type="text/javascript" language="JavaScript"
	src="../../share/javascript/Oscar.js"></script>
<script type="text/javascript" language="javascript">
function clearSession(){
    new Ajax.Request('clearSession.jsp','{asynchronous:true}');

}
</script>
<style type="text/css" media="print">
.MainTableTopRow,.MainTableLeftColumn,.noprint,.showhidequery,.sqlBorderDiv,.MainTableBottomRow
	{
	display: none;
}

.MainTableRightColumn {
	border: 0;
}
</style>
</head>

<body vlink="#0000FF" class="BodyStyle" onunload="clearSession();">

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
		<%ReportObjectGeneric curreport = (ReportObjectGeneric) request.getAttribute("reportobject");
            String sql = (String) request.getAttribute("sql");
            String csv = (String) request.getAttribute("csv");
            if (csv == null) csv = "";
          %>
		<td class="MainTableLeftColumn" valign="top" width="160px;"><jsp:include
			page="listTemplates.jsp">
			<jsp:param name="templateviewid"
				value="<%=curreport.getTemplateId()%>" />
		</jsp:include></td>
		<td class="MainTableRightColumn" valign="top">
		<div class="reportTitle"><%=curreport.getTitle()%></div>
		<div class="reportDescription"><%=curreport.getDescription()%></div>
		<a href="#" style="font-size: 10px; text-decoration: none;"
			class="showhidequery" onclick="showHideItem('sqlDiv')">Hide/Show
		Query</a>
		<div class="sqlBorderDiv" id="sqlDiv" style="display: none;"><b>Query:</b><br />
		<code style="font-size: 11px;"><%=org.apache.commons.lang.StringEscapeUtils.escapeHtml(sql)%></code>
		</div>
		<div class="reportBorderDiv">
		<%String rsHtml = (String) request.getAttribute("resultsethtml");
                     out.println(rsHtml);
                   %>
		</div>
		<div class="noprint"
			style="clear: left; float: left; margin-top: 15px;"><html:form
			action="/oscarReport/reportByTemplate/generateOutFilesAction">
			<input type="hidden" name="csv"
				value="<%=StringEscapeUtils.escapeHtml(csv)%>">
			<input type="button" value="<-- Back"
				onclick="javascript: history.go(-1);return false;">
			<input type="button" value="Print"
				onclick="javascript: window.print();">
			<input type="submit" name="getCSV" value="Export to CSV">
			<input type="submit" name="getXLS" value="Export to XLS">
		</html:form></div>
		</td>
	</tr>
	<tr class="MainTableBottomRow">
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>

		<td class="MainTableBottomRowRightColumn">&nbsp;</td>
	</tr>
</table>
</html:html>
