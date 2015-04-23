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
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>

<%@ page import="java.util.*, oscar.oscarReport.reportByTemplate.*"%>
<%
  if(session.getValue("user") == null) response.sendRedirect("../../logout.jsp");
  String roleName$ = (String)session.getAttribute("userrole") + "," + (String)session.getAttribute("user");
ArrayList templates = (new ReportManager()).getReportTemplatesNoParam();
String templateViewId = request.getParameter("templateviewid");
if (templateViewId == null) templateViewId = "";
%>

<security:oscarSec roleName="<%=roleName$%>"
	objectName="_admin,_report"	rights="r" reverse="<%=true%>">
	<%
		response.sendRedirect("../logout.jsp");
	%>
</security:oscarSec>

<div class="templatelist">
<div class="templatelistHeader">Select a template:</div>
<ul class="templatelist">
	<li><a href="homePage.jsp"><b>Main Page</b></a> <%for (int i=0; i<templates.size(); i++) {
                String selected = "";
                ReportObject curReport = (ReportObject) templates.get(i);
                String templateId = curReport.getTemplateId();
                String templateTitle = curReport.getTitle();
                String selectedTemplate = "";
                if (templateId.equals(templateViewId)) selectedTemplate = "selectedTemplate";%>
	
	<li class="<%=selectedTemplate%>"><%=String.valueOf(i+1)%>. <a
		href="reportConfiguration.jsp?templateid=<%=templateId%>"><%=templateTitle%></a></li>
	<% } %>
</ul>
<a href="addEditTemplate.jsp" style="color: #226d55; font-size: 10px;">Add
Template</a></div>
</form>
