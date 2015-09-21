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
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ include file="/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>

<%@page import="java.util.*"%>
<%@page import="org.oscarehr.common.model.Episode" %>
<%@page import="org.oscarehr.common.dao.EpisodeDao" %>
<%@page import="org.oscarehr.util.SpringUtils" %>

<%

%>
<html:html locale="true">
<head>
<script src="<%=request.getContextPath() %>/js/jquery-1.7.1.min.js" type="text/javascript"></script>

<title>Addition of Pregnancy</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/share/css/OscarStandardLayout.css">

<style>
body
{
	text-align: center;
}

div#demo
{
	margin-left: auto;
	margin-right: auto;
	width: 90%;
	text-align: left;
}
</style>
<script>
<%
	if(request.getAttribute("error") == null) {
		%>
			$(document).ready(function(){
				window.opener.reloadNav('pregnancy');
				window.opener.reloadNav('forms');
				window.close();
			});
		<%
	}
%>
</script>
</head>

<body>

<Br/>
<h2 style="text-align:center">Pregnancy Management</h2>
<br/>
<%
	if(request.getAttribute("error") != null) {
%>
	<h2 style="color:red"><%=request.getAttribute("error") %></h2>
<% 
	return;
	} %>
success

</html:html>
