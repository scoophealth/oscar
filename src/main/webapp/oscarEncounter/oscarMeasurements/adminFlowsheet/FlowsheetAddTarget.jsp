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
<%@page import="org.oscarehr.common.dao.DemographicDao"%>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao"%>
<%@page import="org.oscarehr.common.dao.FlowSheetUserCreatedDao"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.util.LoggedInInfo" %>
<%@ page import="org.oscarehr.common.model.Provider" %>

<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	Provider provider = loggedInInfo.getLoggedInProvider();
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Flowsheet Editor</title>
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

<style>
.red{color:red}

</style>
<%
	String flowsheetId = request.getParameter("flowsheetId");
	String measurementType = request.getParameter("measurementType");
%>
<script>
$(document).ready(function(){
	loadIndicators();
});

function loadIndicators() {
	jQuery.getJSON("<%=request.getContextPath()%>/admin/Flowsheet.do?method=getIndicators&flowsheetId=<%=flowsheetId%>", {},
		    function(xml) {
				var arr = new Array();
				if(xml.indicators instanceof Array) {
					arr = xml.indicators;
				} else {
					arr[0] =xml.indicators;
				}
				
				for(var i=0;i<arr.length;i++) {
					$('#indicator').append($('<option>', {
					    value: arr[i].key,
					    text: arr[i].key
					}));		
				}
				
	});
}


function saveItem() {
	jQuery.post('<%=request.getContextPath()%>/admin/Flowsheet.do?method=saveFlowsheetItemTarget',
   		jQuery('#theForm').serialize(),
   		function(data){
   			location.href='<%=request.getContextPath()%>/oscarEncounter/oscarMeasurements/adminFlowsheet/FlowsheetItemEditor.jsp?flowsheetId=<%=flowsheetId %>&measurementType=<%=measurementType%>';
   	});
}

</script>
</head>

<!-- 
<rule indicationColor="LOW">
	<condition type="getDataAsDouble" param="WAIS" value="&gt;102" />
	<condition type="isMale"  value="true" />
</rule>
 -->
<body vlink="#0000FF" class="BodyStyle">
<h2>Flowsheet Target Editor</h2>
<br/>
<form name="theForm" id="theForm">
<input type="hidden" name="flowsheetId" value="<%=flowsheetId %>"/>
<input type="hidden" name="measurementType" value="<%=measurementType %>"/>

<table style="width:20%">
<tr>
	<td><b>Indicator:</b></td>
	<td>
		<select name="indicator" id="indicator">
		</select>
	</td>
</tr>
<tr>
	<td><b>Type:</b></td>
	<td>
		<select name="type" id="type">
			<option value="">Select Below</option>
			<option value="getDataAsDouble">getDataAsDouble</option>
			<option value="isMale">isMale</option>
			<option value="isFemale">isFemale</option>
			<option value="isDataEqualTo">isDataEqualTo</option>
			<option value="patientAge">patientAge</option>
		</select>
	</td>
</tr>
<tr>
	<td><b>Parameter:</b></td>
	<td><input type="text" id="param" name="param"/></td>
</tr>
<tr>
	<td><b>Value:</b></td>
	<td><input type="text" id="value" name="value"/></td>
</tr>
<tr>
	<td colspan="2">
		<input type="button" value="Save" onClick="saveItem()"/>
	</td>
</tr>
</table>
</form>

</body>
</html:html>
