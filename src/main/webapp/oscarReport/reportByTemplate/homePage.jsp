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
<%-- This JSP is the first page you see when you enter 'report by template' --%>


<%@ page import="java.util.*,oscar.oscarReport.reportByTemplate.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
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
<script type="text/javascript" language="JavaScript"
	src="../../share/javascript/prototype.js"></script>
<script type="text/javascript" language="JavaScript"
	src="../../share/javascript/Oscar.js"></script>
</head>

<body vlink="#0000FF" class="BodyStyle">

<table class="MainTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn"><bean:message
			key="oscarReport.CDMReport.msgReport" /></td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar" style="width: 100%;">
			<tr>
				<td>Report By Template</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top" width="160px;">
		<a href="javascript:void(0)" onclick="newWindow('<%=request.getContextPath()%>/oscarReport/reportByTemplate/k2aTemplates.jsp','templates')" title="<bean:message key='oscarReport.oscarReportByTemplate.msgK2ATemplate' />"><bean:message key="oscarReport.oscarReportByTemplate.msgDownloadFromK2A" /></a>
		<jsp:include
			page="listTemplates.jsp">
			<jsp:param name="templateviewid" value="" />
		</jsp:include>
		</td>
		<td class="MainTableRightColumn" valign="top">
		<%ArrayList templates = (new ReportManager()).getReportTemplatesNoParam(roleName$);
            String templateViewId = request.getParameter("templateviewid");
            if (templateViewId == null) templateViewId = "";
            %>
		<div class="titleHP">Report By Template</div>
		Select a template:
		<div class="templatelistdivHP">
		<ul class="templatelistHP">
			<%
				class CustomComparator implements Comparator<ReportObject>{
                	public int compare(ReportObject r1, ReportObject r2){
                    	return r1.getTitle().compareTo(r2.getTitle());
            		}
    			}
				Collections.sort(templates,new CustomComparator());
			for (int i=0; i<templates.size(); i++) {
                    ReportObject curReport = (ReportObject) templates.get(i);%>
			<li><%=String.valueOf(i+1)%>. <a
				href="reportConfiguration.jsp?templateid=<%=curReport.getTemplateId()%>"><%=curReport.getTitle()%></a></li>
			<div class="templateDescriptionHP"><%=curReport.getDescription()%>
			</div>
			<% } %>
		</ul>
		<%if (templates.isEmpty()) {%>
		<div class="warning">No templates in the database, please create
		a template file by clicking on "Edit Templates"</div>
		<%}%>
		</div>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>

		<td class="MainTableBottomRowRightColumn">&nbsp;</td>
	</tr>
</table>
</html:html>
