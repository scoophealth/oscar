<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>
<%@page contentType="text/html"%>
<%@ include file="/casemgmt/taglibs.jsp"%>
<%@page import="org.oscarehr.common.model.UserProperty" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>

<%
String curUser_no;
curUser_no = (String) session.getAttribute("user");
String tite = (String) request.getAttribute("provider.title");
%>

<%
	String roleName$ = (String)session.getAttribute("userrole") + "," + (String)session.getAttribute("user");
%>

<html:html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>

<title>Reboot Confirmation</title></head>

<body>	

	<security:oscarSec roleName="<%=roleName$%>"
		objectName="_admin,_admin.misc" rights="r" reverse="<%=false%>">
		
	<%
	if (request.getParameter("subbutton") != null && request.getParameter("subbutton").equals("REBOOT OSCAR")) {
	%>
	
	<form method="post" action="<%=request.getContextPath() %>/admin/oscarStatus.do">
		Restarting Oscar will take several minutes and all unsaved data will be lost. <br>
		Are you sure you want to restart Oscar? <br>
		<input type="hidden" name="method" value="rebootOscar" />
		<input type="submit" name="submit" value="Reboot Oscar" />
	</form>
	
	<%} else if (request.getParameter("subbutton") != null && request.getParameter("subbutton").equals("REBOOT SERVER")) {%>
	
	<form method="post" action="<%=request.getContextPath() %>/admin/oscarStatus.do">
		Rebooting the server will take approximately 10 to 15 minutes to complete. All unsaved data will be lost. <br>
		Are you sure you want to reboot the server? <br>
		<input type="hidden" name="method" value="rebootServer" />
		<input type="submit" name="submit" value="Reboot Server" />
	</form>
	
	<%} %>
	
	</security:oscarSec>
	
	<security:oscarSec roleName="<%=roleName$%>"
		objectName="_admin,_admin.misc" rights="r" reverse="<%=true%>">
		
		You don't have access!
	</security:oscarSec>
</body>

</html:html>
