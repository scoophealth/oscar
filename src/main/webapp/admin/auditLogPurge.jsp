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

<%@page import="oscar.OscarProperties"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.ParseException"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.auditLogPurge" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin.auditLogPurge");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="oscar.OscarProperties"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page
	import="java.util.*,oscar.oscarDemographic.data.*,oscar.oscarPrevention.*,oscar.oscarProvider.data.*,oscar.util.*,oscar.oscarReport.data.*,oscar.oscarPrevention.pageUtil.*,oscar.oscarDemographic.pageUtil.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request" />

<%
	String msg = (String)request.getAttribute("msg");
%>

<html:html locale="true">
<head>
<title>Audit Log Purge Tool</title>

<link rel="stylesheet" href="<%=request.getContextPath() %>/css/bootstrap.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/font-awesome.min.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/bootstrap-responsive.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/js/jquery_css/smoothness/jquery-ui-1.10.2.custom.min.css" type="text/css">


<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.9.1.min.js"></script>
<script src="<%=request.getContextPath() %>/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-ui-1.10.2.custom.min.js"></script>



<SCRIPT LANGUAGE="JavaScript">
jQuery(document).ready(function(){
	jQuery("#dateBegin").datepicker();
	//jQuery("#dateBegin").datepicker("option","showAnim","blind");
	jQuery("#dateBegin").datepicker("option","dateFormat","yy-mm-dd");	
});

function submitForm() {
	
	if(jQuery("#dateBegin").val().length == 0) {
		alert('Please fill in a date');
		return false;
	}
	return confirm("Are you sure you want to continue?");
}

function resetForm() {
	jQuery("#dateBegin").val("");
}

</SCRIPT>

<style type="text/css">
	input[type="checkbox"] {
	    line-height: normal;
	    margin: 4px 4px 4px;
	}
</style>

</head>

<body>

<div class="container-fluid well">
<h3>Audit Log Purge Tool</h3>

<div class="span2">

</div><!--span2-->

<div class="span12">

<%
	String minDays = OscarProperties.getInstance().getProperty("log.purge.minDays", String.valueOf(365 * 10));
	Integer iMinDays = null;
	try {
		iMinDays = Integer.parseInt(minDays);
	} catch(NumberFormatException e) {
		
	}
	Calendar c = Calendar.getInstance();
	c.add(Calendar.DAY_OF_YEAR, -iMinDays);
	
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	String minDate = formatter.format(c.getTime());
	
	String outputDirectory =  OscarProperties.getInstance().getProperty("log.purge.outputdir");
	if(outputDirectory == null) {
		outputDirectory = OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
	}
	
%>

<%
	if(msg == null) {
%>
<form action="<%=request.getContextPath()%>/admin/AuditLogPurge.do" onsubmit="return submitForm();">
	
	<p>Welcome to the Audit Log Purge Tool.</p>
	<p>
		When run, this tool will backup all the log entries set to be purged using mysqldump onto the OSCAR server.
		The file can be found in <%=outputDirectory %>. 
	</p>
	<p>The admin of this system has set log.purge.minDays to <%=minDays%> meaning that you must choose a date below
	that is before <%=minDate%>.</p>
	<p>
		Please note that after this tool is run, you will have DELETED all audit log recorded prior to your chosen date.
	</p>
	
	Most Recent Day to purge: <input class="span4" type="text"  id="dateBegin" name="dateBegin" value=""/> 
	
	<br/>
	
	<input type="submit" value="Purge"/>&nbsp;&nbsp;<input type="button" value="Reset" onclick="return resetForm();"/>
	
</form>

<% } else { %>
	<br/>
	<p style="color:red"><%=msg %></p>

<% } %>
</div><!--span4-->

</div><!--container-->


</body>
</html:html>
