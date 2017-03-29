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
  String userRole = (String)session.getAttribute("userrole");
  String status = (String)request.getAttribute("status");
  EFormDao efd = (EFormDao) SpringUtils.getBean("EFormDao");
%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.eform" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin.eform");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<html:html locale="true">

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>

<title>Rich Text Letter Settings</title>
<script src="../share/javascript/Oscar.js"></script>


</head>

<body>

<% if (!userRole.toLowerCase().contains("admin")) { %>
<p>
<h2>Sorry! Only administrators can change these settings..</h2>
</p>
<% } else { %>

<div class="well">

<h3>Rich Text Letter</h3>

<% if (status != null) { %>
Rich Text Letter is <%= status %>.
<% } else { %>
<form action="<%=request.getContextPath()%>/eform/IndivicaRichTextLetterSettings.do" method="post" class="form-inline" id="rtlConfigForm">
				
	<label for="indivica_rtl_enabled"><input type="checkbox" name="indivica_rtl_enabled" id="indivica_rtl_enabled" <%= efd.isIndivicaRTLEnabled() ? "checked" : "" %>/> Check to use Rich Text Letter</label>
	<br/><br/>
	
	<input type="submit" class="btn btn-primary" value="Save"/> 
</form>

</div>


<%}%>

<%}%>

<script>
registerFormSubmit('rtlConfigForm', 'dynamic-content');

</script>
</body>
</html:html>
<%!

%>
