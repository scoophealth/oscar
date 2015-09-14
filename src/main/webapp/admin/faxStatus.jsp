<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.apache.commons.collections.iterators.ArrayListIterator"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html"%>
<%@ include file="/casemgmt/taglibs.jsp"%>
<%@page import="org.oscarehr.common.model.UserProperty" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>

<%
  String curUser_no;
  curUser_no = (String) session.getAttribute("user");
   String tite = (String) request.getAttribute("provider.title");

	String roleName$ = (String)session.getAttribute("userrole") + "," + (String)session.getAttribute("user");
	boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.fax" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.fax");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<c:set var="ctx" value="${pageContext.request.contextPath}"
	scope="request" />
<html:html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/jquery/jquery-1.4.2.js"></script>

<html:base />
<meta http-equiv="Content-Type" content="text/html;">
<title><bean:message key="admin.faxStatus.faxStatus" /></title>

<link rel="stylesheet" type="text/css"
	href="../oscarEncounter/encounterStyles.css">

</head>

<body class="BodyStyle" vlink="#0000FF">

<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn"><bean:message key="admin.faxStatus.fax" /></td>
		<td style="color: white" class="MainTableTopRowRightColumn"><bean:message key="admin.faxStatus.faxStatus" /></td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn">&nbsp;</td>
		<td class="MainTableRightColumn">
		
		<% if (request.getAttribute("restartText") != null) { %>
			<div style="margin-bottom: 20px;">
			<em>Fax server is restarting.</em>
			<div style="colour: black; border: 1px solid black; background-color: lightgrey; padding: 5px; margin: 10px;">
				<pre><%=request.getAttribute("restartText") %></pre>
			</div>
			</div>
		<% } %>
		
		<% if (request.getAttribute("clearTempDirText") != null) { %>
			<div style="margin-bottom: 20px;">
			<em>Fax backlog is being cleared.</em>
			<div style="colour: black; border: 1px solid black; background-color: lightgrey; padding: 5px; margin: 10px;">
				<pre><%=request.getAttribute("clearTempDirText") %></pre>
			</div>
			</div>
		<% } %>
		
		<% if (request.getAttribute("statusText") != null) { %>
			<div>
			<strong>Fax Server Status</strong>
			<div style="colour: black; border: 1px solid black; background-color: lightgrey; padding: 5px; margin: 10px;">
				<pre><%=request.getAttribute("statusText") %></pre>
			</div>
			</div>
		<% } %>
		
		<security:oscarSec roleName="<%=roleName$%>"
		objectName="_admin,_admin.misc" rights="r" reverse="<%=false%>">
		
		<div style="margin-top: 10px;">
			If you are not receiving or are unable to send faxes, click on the button below to restart the fax server.
			<form method="post" action="<%=request.getContextPath() %>/admin/faxStatus.do">
				<input type="hidden" name="method" value="restartFax" />
				<input type="submit" name="submit" value="Restart Fax Server" />
			</form>
		</div>
		
		<div style="margin-top: 10px;">
			If there has been a backlog generated of previous faxes that were unable to be sent due to an error, click on the button below to clear those faxes.
			<form method="post" action="<%=request.getContextPath() %>/admin/faxStatus.do">
				<input type="hidden" name="method" value="clearTempDir" />
				<input type="submit" name="submit" value="Clear Fax Backlog" />
			</form>
		</div>
		
		</security:oscarSec>
	</tr>
</table>

</body>
</html:html>
