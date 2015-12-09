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
<%--This JSP is to configure the report before it is run, this is where the user fills in all the param--%>



<%@ page import="java.util.*,oscar.oscarReport.reportByTemplate.*"%>
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
<script type="text/javascript" src="<%=request.getContextPath()%>/library/angular.min.js"></script>
<title>Report by Template</title>
<link rel="stylesheet" type="text/css"
	href="../../share/css/OscarStandardLayout.css">
<link rel="stylesheet" type="text/css" href="reportByTemplate.css">
<script type="text/javascript" language="JavaScript"
	src="../../share/javascript/prototype.js"></script>
<script type="text/javascript" language="JavaScript"
	src="../../share/javascript/Oscar.js"></script>
<link rel="stylesheet" type="text/css" media="all"
	href="../../share/calendar/calendar.css" title="win2k-cold-1" />
<script type="text/javascript" src="../../share/calendar/calendar.js"></script>
<script type="text/javascript"
	src="../../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
<script type="text/javascript"
	src="../../share/calendar/calendar-setup.js"></script>
<script type="text/javascript" language="JavaScript">
    function checkform(formobj) {
        if (!validDateFieldsByClass('datefield', formobj)) {
            alert("Invalid Date: Must be in the format YYYY/MM/DD");
            return false;
        }
        return true;
    }
</script>
</head>

<body vlink="#0000FF" class="BodyStyle">

<table class="MainTable" ng-controller="reportConfiguration">
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
	<%String templateid = request.getParameter("templateid");
      if (templateid == null) templateid = (String) request.getAttribute("templateid");
      if (templateid == null) { %>
	<jsp:forward page="homePage.jsp" />
	<%}%>
	<tr>
		<td class="MainTableLeftColumn" valign="top" width="160px;"><jsp:include
			page="listTemplates.jsp">
			<jsp:param name="templateviewid" value="<%=templateid%>" />
		</jsp:include></td>
		<td class="MainTableRightColumn" valign="top">
		<%ReportObject curreport = (new ReportManager()).getReportTemplate(templateid);
		  		 String xml = (new ReportManager()).getTemplateXml(templateid);
                 ArrayList parameters = curreport.getParameters();
                 int step = 0;
                 if (request.getAttribute("errormsg") != null) {
                    String errormsg = (String) request.getAttribute("errormsg");%>
		<div class="warning"><%=errormsg%></div>
		<%}%>
		<div class="reportTitle"
			<%if (curreport.getTitle().indexOf("Error") != -1) {%>
			style="color: red;" <%}%>><%=curreport.getTitle()%></div>
		<div class="reportDescription"><%=curreport.getDescription()%></div>
		<html:form action="/oscarReport/reportByTemplate/GenerateReportAction"
			onsubmit="return checkform(this);">
			<input type="hidden" name="templateId"
				value="<%=curreport.getTemplateId()%>">
			<input type="hidden" name="type" value="<%=curreport.getType()%>">
			<div class="configDiv">
			<table class="configTable">
				<%for (int i=0; i<parameters.size(); i++) {
                             step++;
                             Parameter curparam = (Parameter) parameters.get(i);
                     %>
				<tr>
					<th class="stepRC">Step <%=step%>:</th>
					<td class="descriptionRC" style="max-width: 550px"><%=curparam.getParamDescription()%>
					</td>
					<td id="enclosingCol<%=i%>"><%-- If LIST field --%> <%if (curparam.getParamType().equals(curparam.LIST)) {%>
					<select name="<%=curparam.getParamId()%>">
						<%ArrayList paramChoices = curparam.getParamChoices();
                                         for (int i2=0; i2<paramChoices.size(); i2++) { 
                                         Choice curchoice = (Choice) paramChoices.get(i2);%>
						<option value="<%=curchoice.getChoiceId()%>"><%=curchoice.getChoiceText()%></option>
						<%}%>
					</select> <%--If TEXT field --%> <% } else if (curparam.getParamType().equals(curparam.TEXT)) {%>
					<input type="text" size="20" name="<%=curparam.getParamId()%>">
					<%--If DATE field --%> <% } else if (curparam.getParamType().equals(curparam.DATE)) {%>
					<input type="text" class="datefield" id="datefield<%=i%>"
						name="<%=curparam.getParamId()%>"><a id="obsdate<%=i%>"><img
						title="Calendar" src="../../images/cal.gif" alt="Calendar"
						border="0" /></a> <script type="text/javascript">
                                    Calendar.setup( { inputField : "datefield<%=i%>", ifFormat : "%Y-%m-%d", showsTime :false, button : "obsdate<%=i%>", singleClick : true, step : 1 } );
                                 </script> <%--If CHECK field --%> <% } else if (curparam.getParamType().equals(curparam.CHECK)) {%>
					<input type="hidden" name="<%=curparam.getParamId()%>:check"
						value=""> <input type="checkbox" name="mastercheck"
						onclick="checkAll(this, 'enclosingCol<%=i%>', 'checkclass<%=i%>')"><br />
					<%ArrayList paramChoices = curparam.getParamChoices();
                                     for (int i2=0; i2<paramChoices.size(); i2++) {
                                         Choice curchoice = (Choice) paramChoices.get(i2);%>
					<input type="checkbox" name="<%=curparam.getParamId()%>"
						class="checkclass<%=i%>" value="<%=curchoice.getChoiceId()%>">
					<%=curchoice.getChoiceText()%><br />
					<%}%> <% } else if (curparam.getParamType().equals(curparam.TEXTLIST)) {%>
					<input type="text" size="20" name="<%=curparam.getParamId()%>:list">
					<font style="font-size: 10px;">(Comma Separated)</font> <% }%>
					</td>
				</tr>
				<%} %>
				<tr>
					<th>Step <%=step+1%>:</th>
					<td>Generate Query</td>
					<td><input type="submit" name="submitButton" value="Run Query"></td>
				</tr>
			</table>
			<div><a
				href="viewTemplate.jsp?templateid=<%=curreport.getTemplateId()%>"
				class="link">View Template XML</a> <a
				href="addEditTemplate.jsp?templateid=<%=curreport.getTemplateId()%>&opentext=1"
				class="link">Edit Template</a> <a
				href="addEditTemplatesAction.do?templateid=<%=curreport.getTemplateId()%>&action=delete"
				onclick="return confirm('Are you sure you want to delete this report template?')"
				class="link">Delete Template</a> <a
				href="exportTemplateAction.do?templateid=<%=templateid%>&name=<%=curreport.getTitle()%>"
				class="link">Export Template to K2A</a>
			</div>
			<%  if (request.getAttribute("message") != null) {
				String message = (String) request.getAttribute("message"); %>
			<%=message%>
			<%}%>
			</div>
		</html:form></td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>

		<td class="MainTableBottomRowRightColumn">&nbsp;</td>
	</tr>
</table>
</html:html>
