<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>
<%@ page import="java.util.*,oscar.oscarDemographic.data.*,oscar.oscarPrevention.*,oscar.oscarProvider.data.*,oscar.util.*,oscar.oscarReport.data.*,oscar.oscarPrevention.pageUtil.*,oscar.oscarDemographic.pageUtil.*"%>
<%@ page import="org.oscarehr.util.SpringUtils,org.oscarehr.common.dao.EFormDao" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
  if(session.getAttribute("user") == null) response.sendRedirect("../logout.jsp");
  String userRole = (String)session.getAttribute("userrole");
  String status = (String)request.getAttribute("status");
  EFormDao efd = (EFormDao) SpringUtils.getBean("EFormDao");
%>

<html:html locale="true">

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>

<title>Rich Text Letter Settings</title>
<script src="../share/javascript/Oscar.js"></script>
<link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css">
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<body class="BodyStyle" vlink="#0000FF">
<% if (!userRole.toLowerCase().contains("admin")) { %>
<p>
<h2>Sorry! Only administrators can change these settings..</h2>
</p>
<% } else { %>

<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td style="max-width:200px;" class="MainTableTopRowLeftColumn">
		Rich Text Letter</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td>Settings</td>						
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top" nowrap="nowrap">
		   
		</td>
		<td valign="top" class="MainTableRightColumn">
			<% if (status != null) { %>
			Rich Text Letter is <%= status %>.
			<% } else { %>
			<form action="IndivicaRichTextLetterSettings.do" method="post">
							
				<input type="checkbox" name="indivica_rtl_enabled" id="indivica_rtl_enabled" <%= efd.isIndivicaRTLEnabled() ? "checked" : "" %>/> <label for="indivica_rtl_enabled">Check to use Rich Text Letter</label>
				<br/><input type="submit" value="submit"/> 
			</form>
			<% } %>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>
		<td class="MainTableBottomRowRightColumn" valign="top">&nbsp;</td>
	</tr>
</table>


<%}%>
</body>
</html:html>
<%!

%>
