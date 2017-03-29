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
<%-- This JSP is the page that allows users to add templates by XML --%>


<%@ page
	import="oscar.oscarReport.reportByTemplate.*, java.sql.*, org.apache.commons.lang.StringUtils"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>

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

<link rel="stylesheet" type="text/css" href="../../js/jquery_css/smoothness/jquery-ui-1.10.2.custom.min.css"/>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="../../jquery-ui-1.10.2.custom.min.js"></script>
<script>
$(function() {
    $( document ).tooltip();
  });
</script>

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
		<td class="MainTableLeftColumn" valign="top" width="160px;"><jsp:include
			page="listTemplates.jsp" /></td>
		<%// can be three options: add/edit/upload
            // add --> no relevant request parameters/attributes
            // edit --> templateid in request parameter or attribute
            // upload --> templateXML in request parameter and no templateid
          ReportManager rm = new ReportManager();
            String action = "add";
            // try templateid from attribute and parameter - check to see if we are editing or adding
            String templateid = "";
            String templatexml = "";
            if ((request.getAttribute("templateid") != null) && (!((String) request.getAttribute("templateid")).equals(""))) {
                templateid = (String) request.getAttribute("templateid");
                templatexml = rm.getTemplateXml(templateid);
                action = "edit";
            }
            else if ((request.getParameter("templateid") != null) && (!request.getParameter("templateid").equals(""))) {
                templateid = request.getParameter("templateid");
                templatexml = rm.getTemplateXml(templateid);
                action = "edit";
            }
            //open the XML editing textarea or no?
            String openText = "0";
            if (request.getAttribute("opentext") != null) {
                openText = (String) request.getAttribute("opentext");
            } else if (request.getParameter("opentext") != null) {
                openText = request.getParameter("opentext");
            }
            // try templatexml in attribute, check to see if we are uploading
            if (request.getAttribute("templatexml") != null) {
                templatexml = (String) request.getAttribute("templatexml");
            }
            String message = (String) request.getAttribute("message");
            if (message == null) message = "";
          %>
		<td class="MainTableRightColumn" valign="top">
		<div class="reportTitle"><%=StringUtils.capitalize(action)%>
		Template</div>
		<%if (message.toLowerCase().indexOf("success") != -1) {%>
		<div class="green"><%=message%></div>
		<%} else {%>
		<div class="warning"><%=message%></div>
		<%}%> <html:form action="/oscarReport/reportByTemplate/uploadTemplates"
			enctype="multipart/form-data">
                  Upload Templates: <input type="file"
				name="templateFile">
				<span title="<bean:message key="global.uploadWarningBody"/>" style="vertical-align:middle;font-family:arial;font-size:20px;font-weight:bold;color:#ABABAB;cursor:pointer"><img border="0" src="../../images/icon_alertsml.gif"/></span></span>
        
			<input type="hidden" name="action" value="<%=action%>">
			<input type="hidden" name="opentext" value="<%=openText%>">
			<input type="hidden" name="templateid" value="<%=templateid%>">
			<input type="submit"
				value="Upload & <%=StringUtils.capitalize(action)%>">
		</html:form>
		<div id="editXml"
			style="<%if (openText.equals("0")) {%>display: none;<%}%>"><html:form
			action="/oscarReport/reportByTemplate/addEditTemplatesAction">
                     OR:
                     <textarea name="xmltext"
				style="width: 100%; height: 400px;"><%=templatexml%></textarea>
			<br />
			<input type="hidden" name="action" value="<%=action%>">
			<input type="hidden" name="opentext" value="<%=openText%>">
			<input type="hidden" name="templateid" value="<%=templateid%>">
			<br />
			<input type="button" onclick="this.form.submit()" value="<%if (action.equals("edit")){out.print("Save");}else{ out.print(StringUtils.capitalize(action));}%>">
			
			<%if (action.equals("edit")) {%> 
			<input type="button" name="done" value="Done" onclick="document.location='reportConfiguration.jsp?templateid=<%=templateid%>'">
			<%} else {%> 
			<input type="button" name="cancel" value="Cancel" onclick="document.location='homePage.jsp'"> 
			<%}%>
		</html:form></div>

		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>

		<td class="MainTableBottomRowRightColumn">&nbsp;</td>
	</tr>
</table>
</html:html>
