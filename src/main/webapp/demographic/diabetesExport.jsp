<!DOCTYPE html>
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
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_demographic,_demographicExport" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_demographic");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page
	import="oscar.oscarDemographic.data.*,java.util.*,oscar.oscarProvider.data.*,oscar.util.*,oscar.oscarReport.data.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>

<%
  if(session.getAttribute("user") == null) response.sendRedirect("../logout.jsp");
  String demographic_no = request.getParameter("demographic_no");

  DemographicSets  ds = new DemographicSets();
  List<String> sets = ds.getDemographicSets();

  RptSearchData searchData  = new RptSearchData();
  ArrayList queryArray = searchData.getQueryTypes();

  String userRole = (String)session.getAttribute("userrole");
%>

<html:html locale="true">

<head>
<!--I18n-->
<title>Reporting of Diabetes</title>

<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">

<script type="text/javascript" src="../share/calendar/calendar.js"></script>
<script type="text/javascript"
	src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
<script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>

<SCRIPT LANGUAGE="JavaScript">

function checkSelect(slct) {
    if (slct==-1) {
	alert("Please select a Patient Set");
	return false;
    }
    else return true;
}

function checkAll() {
    var frm = document.forms.DiabetesExportForm;
    if (!checkSelect(frm.patientSet.value)) return false;
    if (frm.startDate.value=="") {
	alert("Please enter a valid start date");
	return false;
    }
    if (frm.endDate.value=="") {
	alert("Please enter a valid end date");
	return false;
    }
    return true;
}
</SCRIPT>

</head>

<body>
<div class="container-fluid well">
<%
if (!userRole.toLowerCase().contains("admin")) { %>
<p>
<h2>Sorry! Only administrators can report diabetes.</h2>
</p>
<%
} else {
%>

<h3>Reporting of Diabetes <small><oscar:help keywords="export" key="app.top1"/></small></h3>

<html:form action="/demographic/DiabetesExport" method="get"
	onsubmit="return checkAll();">

<table><tr>
	<% if (demographic_no!= null) { %>
		<td><input type="hidden" name="demographicNo" value="<%=demographic_no%>"/>
		Reporting :
	<%} else {%>
	    Patient Set: <html:select property="patientSet">
			<html:option value="-1">--Select Set--</html:option>
	<%
	    for (int i=0; i<sets.size(); i++) {
		String setName = sets.get(i);
	%>
		<html:option value="<%=setName%>"><%=setName%></html:option>
	    <%}%>
	    </html:select>
    <%}%>
    </td></tr><tr>
	<td align="right" width=73>Start Date: </td>
	<td><html:text property="startDate" size="10" /> (YYYY-MM-DD)</td>
    </tr><tr>
	<td align="right">End Date: </td>
	<td><html:text property="endDate" size="10" /> (YYYY-MM-DD)</td>
</tr></table>
<p><input class="btn btn-primary" type="submit" value="Export" />
</html:form>

<%}%>
</div>
</body>
</html:html>
<%!

%>
