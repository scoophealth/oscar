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
<title>OSCAR Jobs</title>
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
	String id = request.getParameter("id");	
%>
<script>
$(document).ready(function(){
	loadFlowsheet();
	loadTypes();
	loadPreventionTypes();
});

function editItem(flowsheetId, measurementType) {
	location.href='<%=request.getContextPath()%>/oscarEncounter/oscarMeasurements/adminFlowsheet/FlowsheetItemEditor.jsp?flowsheetId='  + flowsheetId + '&measurementType=' + measurementType;
}
function removeItem(id) {
	  jQuery.post('<%=request.getContextPath()%>/admin/Flowsheet.do?method=removeItem',{flowsheetId: <%=id%>, id:id},
	    		function(data){
		  			loadFlowsheet();
	    		});
}
function sortItem(id,direction) {
	alert('sort ' + direction);
}

function loadFlowsheet() {
	jQuery.getJSON("<%=request.getContextPath()%>/admin/Flowsheet.do?method=getFlowsheet&id=<%=id%>", {},
    function(xml) {
		$("#itemTable tbody").empty();
		$("#name").html(xml.name);
		$("#template").html(xml.template);
		$("#createdBy").html(xml.createdBy);
		$("#createdDate").html(xml.createdDate);
		$("#dxCodeTriggers").html(xml.dxCodeTriggers);
		$("#recommendationColour").html(xml.recommendationColour);
		$("#warningColour").html(xml.warningColour);
		
		
		for(var x=0;x<xml.items.length;x++) {
			var i = xml.items[x];
			var type = i.measurementType;
			if(i.measurementType === undefined) {
				type = i.preventionType;
			}
			var measuringInst = i.measuringInstruction;
			if(measuringInst === undefined) {
				measuringInst = "";
			}
			var validation = i.validation;
			if(i.validation === undefined) {
				validation = "";
			}
			$("#itemTable tbody").append("<tr><td><a href=\"javascript:void(0)\" onClick=\"removeItem('"+type+"')\"><img src=\"<%=request.getContextPath()%>/images/icons/101.png\" border=\"0\"/></a>&nbsp;<a href=\"javascript:void(0)\" onClick=\"editItem(<%=id%>,'"+type+"')\"><img src=\"<%=request.getContextPath()%>/images/edit.png\" border=\"0\"/></a>&nbsp;<a href=\"javascript:void(0)\" onClick=\"sortItem('"+type+"','up')\"><img src=\"<%=request.getContextPath()%>/images/icon_up_sort_arrow.png\" border=\"0\"/></a>&nbsp;<a href=\"javascript:void(0)\" onClick=\"sortItem('"+type+"','down')\"><img src=\"<%=request.getContextPath()%>/images/icon_down_sort_arrow.png\" border=\"0\"/></a></td><td>"+type+"</td><td>"+i.displayName+"</td><td>"+i.guideline+"</td><td>"+i.graphable+"</td><td>"+measuringInst+"</td><td>"+validation+"</td></tr>");
		}
    });
}

function loadTypes() {
	jQuery.getJSON("<%=request.getContextPath()%>/admin/Flowsheet.do?method=getMeasurementTypes", {},
    function(xml) {
		var arr = new Array();
		if(xml.results instanceof Array) {
			arr = xml.results;
		} else {
			arr[0] =xml.results;
		}
		
		for(var i=0;i<arr.length;i++) {
			jQuery('#types').append("<option value="+arr[i].id +">"+arr[i].displayName+"</option>");
		}
    });
}


function loadPreventionTypes() {
	jQuery.getJSON("<%=request.getContextPath()%>/admin/Flowsheet.do?method=getPreventionTypes", {},
    function(xml) {
		var arr = new Array();
		if(xml.results instanceof Array) {
			arr = xml.results;
		} else {
			arr[0] =xml.results;
		}
		
		for(var i=0;i<arr.length;i++) {
			jQuery('#preventionTypes').append("<option value="+arr[i].id +">"+arr[i].displayName+"</option>");
		}
    });
}


function addMeasurement() {
	var typeId = $("#types").val();
	
    $.post('<%=request.getContextPath()%>/admin/Flowsheet.do?method=addMeasurement',{flowsheetId:<%=id%>,measurementTypeId:typeId},function(data){
        loadFlowsheet();
	});
}

function addPrevention() {
	var typeId = $("#preventionTypes").val();
	
    $.post('<%=request.getContextPath()%>/admin/Flowsheet.do?method=addPrevention',{flowsheetId:<%=id%>,preventionType:typeId},function(data){
        loadFlowsheet();
	});
}


</script>
</head>

<body vlink="#0000FF" class="BodyStyle">
<h2>Flowsheet Editor</h2>
<br/>

<table style="width:20%">
<tr>
	<td><b>Name:</b></td>
	<td><span id="name"></span></td>
</tr>
<tr>
	<td><b>Template:</b></td>
	<td><span id="template"></span></td>
</tr>
<tr>
	<td><b>Created By:</b></td>
	<td><span id="createdBy"></span></td>
</tr>
<tr>
	<td><b>Date Created:</b></td>
	<td><span id="createdDate"></span></td>
</tr>
<tr>
	<td><b>Triggers:</b></td>
	<td><span id="dxCodeTriggers"></span></td>
</tr>
<tr>
	<td><b>Recommendation Colour:</b></td>
	<td><span id="recommendationColour"></span></td>
</tr>
<tr>
	<td><b>Warning Colour:</b></td>
	<td><span id="warningColour"></span></td>
</tr>
</table>
<br/>

Add new measurement type to flowsheet :

<select id="types" onChange="addMeasurement()">
	<option value="0">Select Below</option>
</select>
 
&nbsp;&nbsp;&nbsp;

<select id="preventionTypes" onChange="addPrevention()">
	<option value="0">Select Below</option>
</select>

<br/>

<table id="itemTable" name="itemTable" class="table table-bordered table-striped table-hover table-condensed">
	<thead>
		<tr>
			<th></th>
			<th>Type</th>
			<th>Display Name</th>
			<th>Guideline</th>
			<th>Graphable</th>
			<th>Measuring Instruction</th>
			<th>Validation</th>
		</tr>
	</thead>
	<tbody>
	</tbody>
</table>

</body>
</html:html>
