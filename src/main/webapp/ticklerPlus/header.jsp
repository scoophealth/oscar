<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>
<%-- Updated by Eugene Petruhin on 20 feb 2009 while fixing check_date() error --%>

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.PMmodule.caisi_integrator.ConformanceTestHelper"%>
<%@page import="java.util.Properties"%>
<%@page import="oscar.OscarProperties"%>
<%@ include file="/taglibs.jsp"%>
<%@ page
	import="org.oscarehr.PMmodule.model.*,org.springframework.context.*,org.springframework.web.context.support.*"%>
<%@ page import="java.util.Date"%>

<%@page import="org.oscarehr.common.model.Tickler" %>
<%@page import="org.oscarehr.common.model.TicklerComment" %>
<%@page import="org.oscarehr.common.model.TicklerUpdate" %>
<%@page import="org.oscarehr.common.model.CustomFilter" %>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    if(session.getAttribute("userrole") == null )  response.sendRedirect("logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");

   	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(session);
   		 
	if (ConformanceTestHelper.enableConformanceOnlyTestFeatures)
	{
		ConformanceTestHelper.populateLocalTicklerWithRemoteProviderMessageFollowUps(loggedInInfo);
	}
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_tasks"
	rights="r" reverse="<%=true%>">
	<%response.sendRedirect("noRights.html");%>
</security:oscarSec>

<%
	ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(application);
%>
<c:if test="${requestScope.from ne 'CaseMgmt'}">
	<html>
	<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
	<title>TicklerPlus</title>

	<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
	<html:base />

	<style type="text/css">
	table.legend{
	border:0;
	padding-top:0px;
	font-family:"Arial", Verdana;
	
	}
	
	table.legend td{
	font-size:10;
	text-align:left;
	padding-right:5px;
	
	}
	
	
	table.colour_codes{
	width:8px;
	height:10px;
	border:1px solid #999999;
	padding-right:0px;
	}
	
	
	</style>
	
	<!--[if IE]>
	<style type="text/css">
	
	table.legend{
	border:0;
	font-family:"Arial", Verdana;
	
	}
	
	table.legend td{
	font-size:12;
	text-align:left;
	padding-right:5px;
	}
	
	</style>
	<![endif]-->


	</head>
	<body>
</c:if>

<c:set var="ctx" value="${pageContext.request.contextPath}"
	scope="request" />
<script>
		// date picker variables
		var readOnly = false;
		var win = null;
		var timerId = 0;
		var isDateValid = true;
		var doOnBlur = true;
		var deferSubmit = false;

		function openBrWindow(theURL,winName,features) { 
		  window.open(theURL,winName,features);
		}
		
		function Check(e) {
			e.checked = true;
		}
		
		function Clear(e) {
			e.checked = false;
		}
		    
		function CheckAll(ml) {
			var len = ml.elements.length;
			for (var i = 0; i < len; i++) {
			    var e = ml.elements[i];
			    if (e.name == "checkbox") {
					Check(e);
			    }
			}
		}
		
		function ClearAll(ml) {
			var len = ml.elements.length;
			for (var i = 0; i < len; i++) {
			    var e = ml.elements[i];
			    if (e.name == "checkbox") {
				Clear(e);
			    }
			}
		}
</script>
<table border="0" cellspacing="0" cellpadding="1" width="100%"
	bgcolor="#CCCCFF">
	<tr class="subject">
		<th width="30%"></th>
		<th width="30%" align="center">TicklerPlus</th>
		<th width="30%"></th>
		<th width="10%"></th>
	</tr>
