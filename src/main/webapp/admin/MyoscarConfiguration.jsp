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
<%-- This JSP is the first page you see when you enter 'report by template' --%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>"
	objectName="_admin,_admin.misc" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.misc");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>


<%@ page import="java.util.*,oscar.oscarReport.reportByTemplate.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html-el" prefix="html-el" %>

<%@ page import="oscar.oscarEncounter.oscarMeasurements.MeasurementTemplateFlowSheetConfig" %>
<%@ page import="oscar.oscarEncounter.oscarMeasurements.MeasurementFlowSheet" %>
<%@ page import="org.oscarehr.common.model.Flowsheet" %>
<%@ page import="org.oscarehr.common.dao.FlowsheetDao" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>

<html:html locale="true">
<head>
	<title>PHR Account Creation Config</title>
	<link href="<%=request.getContextPath() %>/css/bootstrap.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath() %>/css/datepicker.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath() %>/css/DT_bootstrap.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath() %>/css/bootstrap-responsive.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" href="<%=request.getContextPath() %>/css/font-awesome.min.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/cupertino/jquery-ui-1.8.18.custom.css">
	
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-ui-1.8.18.custom.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap-datepicker.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.validate.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.dataTables.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/DT_bootstrap.js"></script>   
	<script type="text/javascript" language="JavaScript" src="<%= request.getContextPath() %>/share/javascript/Oscar.js"></script>
	
	<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
	<script type="text/javascript" language="JavaScript" src="../share/javascript/prototype.js"></script>
</head>

<body vlink="#0000FF" class="BodyStyle">
<h4>PHR Account Creation Config</h4>

<html-el:form action="/phr/UserManagement" styleId="registrationForm" method="POST">
 	<html-el:hidden property="method" value="setRegistrationLetterData"/>
	<fieldset>
		<div class="control-group">
			<label class="control-label">Offset for name/address</label>
			<div class="controls">
				<input name="nameOffset" type="text" size="3"/> (default 150) <br/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Upper Text</label>
			<div class="controls">
				<textarea name="upperText=" disabled>Not yet implemented</textarea>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Lower Text</label>
			<div class="controls">
				<textarea name="lowerText" disabled>Not yet implemented</textarea>
			</div>
		</div>
		<div class="control-group">
			<input type="submit" class="btn btn-primary" value="Save"/>&nbsp;
			<input type="button" class="btn" value="Test Output" disabled/>
		</div>
	</fieldset>
</html-el:form>
</html:html>
