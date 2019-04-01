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
<%@page import="org.oscarehr.common.model.FlowSheetUserCreated"%>
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


<script type="text/javascript" src="<%=request.getContextPath()%>/share/yui/js/yahoo-dom-event.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/share/yui/js/connection-min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/share/yui/js/animation-min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/share/yui/js/datasource-min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/share/yui/js/autocomplete-min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/demographicProviderAutocomplete.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/share/yui/css/fonts-min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/share/yui/css/autocomplete.css"/>
<link rel="stylesheet" type="text/css" media="all" href="<%=request.getContextPath()%>/share/css/demographicProviderAutocomplete.css"  />


<style>
.red{color:red}

</style>
<%
	String scope = request.getParameter("scope");	
%>
<script>

function updateScope() {
	if($("#scope").val() == "clinic" || $("#scope").val() == "" ) {
		$("#providerTR").hide();
		$("#patientTR").hide();
	}
	if($("#scope").val() == "provider") {
		$("#providerTR").show();
		$("#patientTR").hide();
	}
	if($("#scope").val() == "patient") {
		$("#providerTR").hide();
		$("#patientTR").show();
	}
}
$(document).ready(function(){
	updateScope();
	getSystemFlowsheets();
});

function getSystemFlowsheets() {
	jQuery.getJSON("<%=request.getContextPath()%>/admin/Flowsheet.do?method=listSystem", {},
    function(xml) {
		var arr = new Array();
		if(xml.results instanceof Array) {
			arr = xml.results;
		} else {
			arr[0] =xml.results;
		}
		
		$("#template").empty();
		$("#template").append("<option value=''>Select Below</option>");
		
		for(var i=0;i<arr.length;i++) {
			var fs = arr[i];
			$("#template").append("<option value='"+fs.name+"'>"+fs.displayName+"</option>");
		}
    });
}

function saveFlowsheet() {
    jQuery.post('<%=request.getContextPath()%>/admin/Flowsheet.do?method=addNewFlowsheet',
    		jQuery('#theForm').serialize(),
    		function(data){
    			location.href='<%=request.getContextPath()%>/oscarEncounter/oscarMeasurements/adminFlowsheet/FlowsheetManager.jsp';
    		});
}

function updateDetails() {
	var template = $("#template").val();
	
	if(template != '') {
		jQuery.getJSON("<%=request.getContextPath()%>/admin/Flowsheet.do?method=getTemplateDetails&template=" + template, {},
			    function(xml) {
	      $("#recommendationColour").val(xml.recommendationColour);
	      $("#warningColour").val(xml.warningColour);
	      $("#triggers").val(xml.dxTriggers);
		});
	}
}
</script>
</head>

<body vlink="#0000FF" class="BodyStyle">
<h2>Add New Custom Flowsheet</h2>
<br/>
<form id="theForm">
<table style="width:30%"  class="table table-bordered table-striped table-hover table-condensed">
 
<tr>
	<td><b>Name:</b></td>
	<td><input type="text" name="name" value=""/></td>
</tr>

<tr>
	<td><b>Template:</b></td>
	<td>
		<select name="template" id="template" onChange="updateDetails()">
			<option value="">Select Below</option>
		</select>
	</td>
</tr>
<tr>
	<td><b>Scope:</b></td>
	<td>
		<select name="scope" id="scope" onChange="updateScope()">
			<option value="">Select Below</option>
			<option value="<%=FlowSheetUserCreated.SCOPE_CLINIC %>" <%="clinic".equals(scope) ? "selected=\"selected\" " : "" %>>Clinic</option>
			<option value="<%=FlowSheetUserCreated.SCOPE_PROVIDER %>" <%="provider".equals(scope) ? "selected=\"selected\" " : "" %>>Provider</option>
			<option value="<%=FlowSheetUserCreated.SCOPE_PATIENT %>" <%="patient".equals(scope) ? "selected=\"selected\" " : "" %>>Patient</option>
		</select>
	</td>
</tr>
<tr style="display:none" id="providerTR">
	<td><b>Provider:</b></td>
	<td>
		<input type="text" name="providerAC" id="providerAC"/>
		<div id="provider_choices" class="autocomplete"></div>
	</td>
</tr>
<tr style="display:none" id="patientTR">
	<td><b>Patient:</b></td>
	<td>
		<input type="text" name="demographicAC" id="demographicAC"/>
		<div id="demographic_choices" class="autocomplete"></div>
	</td>
</tr>
<tr>
	<td><b>Triggers:</b></td>
	<td><input type="text" name="triggers" id="triggers" value=""/> (codeSystem:code[,codeSystem:code]+)</td>
</tr>
<tr>
	<td><b>Recommendation Colour:</b></td>
	<td><input type="text" name="recommendationColour" id="recommendationColour" value=""/> </td>
</tr>
<tr>
	<td><b>Warning Colour:</b></td>
	<td><input type="text" name="warningColour" id="warningColour" value=""/></td>
</tr>
<tr>
	<td colspan="2">
		<input type="button" class="btn btn-primary" value="Save" onClick="saveFlowsheet();"/>
	</td>
</tr>
</table>
<input type="hidden" name="providerNo" id="providerNo"  value=""/>
<input type="hidden" name="demographicNo" id="demographicNo" value=""/>
</form>

<script>

YAHOO.example.BasicRemote = function() {
    var url = "<%= request.getContextPath() %>/demographic/SearchDemographic.do";
    var oDS = new YAHOO.util.XHRDataSource(url,{connMethodPost:true,connXhrMode:'ignoreStaleResponses'});
    oDS.responseType = YAHOO.util.XHRDataSource.TYPE_JSON;
    oDS.responseSchema = {
   		resultsList : "results",
        fields : ["formattedName","fomattedDob","demographicNo","status"]
    };
    oDS.maxCacheEntries = 0;
    var oAC = new YAHOO.widget.AutoComplete("demographicAC", "demographic_choices", oDS);
    oAC.queryMatchSubset = true;
    oAC.minQueryLength = 3;
    oAC.maxResultsDisplayed = 25;
    oAC.formatResult = resultFormatter2;
    oAC.queryMatchContains = true;
    oAC.itemSelectEvent.subscribe(function(type, args) {
       var oData=args[2];
       var demographicNo = args[2][2];
       var demographicName = args[2][0];
       $("#demographicNo").val(demographicNo);
       $("#demographicAC").val(demographicName);
    });
    return {
        oDS: oDS,
        oAC: oAC
    };
}();


YAHOO.example.BasicRemote = function() {
    var url = "<%= request.getContextPath() %>/provider/SearchProvider.do";
    var oDS = new YAHOO.util.XHRDataSource(url,{connMethodPost:true,connXhrMode:'ignoreStaleResponses'});
    oDS.responseType = YAHOO.util.XHRDataSource.TYPE_JSON;
    oDS.responseSchema = {
        resultsList : "results",
        fields : ["providerNo","firstName","lastName"]
    };
    oDS.maxCacheEntries = 0;
    var oAC = new YAHOO.widget.AutoComplete("providerAC", "provider_choices", oDS);
    oAC.queryMatchSubset = true;
    oAC.minQueryLength = 3;
    oAC.maxResultsDisplayed = 25;
    oAC.formatResult = resultFormatter3;
    oAC.queryMatchContains = true;
    oAC.itemSelectEvent.subscribe(function(type, args) {
       var oData=args[2];
       var providerNo = args[2][0];
       var providerName = args[2][2] + "," + args[2][1];
       $("#providerNo").val(providerNo);
       $("#providerAC").val(providerName);
    });
    return {
        oDS: oDS,
        oAC: oAC
    };
}();

</script>
</body>
</html:html>
