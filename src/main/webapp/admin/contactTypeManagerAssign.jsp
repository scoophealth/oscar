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
	objectName="_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_admin");%>
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
<%@ page import="org.oscarehr.managers.ProgramManager2" %>
<%@ page import="org.oscarehr.util.LoggedInInfo" %>
<%@ page import="java.util.List" %>
<%@ page import="org.oscarehr.PMmodule.model.Program" %>
<%@ page import="java.util.Collections" %>

<%
	ProgramManager2 programManager = SpringUtils.getBean(ProgramManager2.class);
	LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
	
	List<Program> programs = programManager.getAllPrograms(loggedInInfo);
	Collections.sort(programs,Program.NameComparator);
%>

<html:html locale="true">
<head>
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

<script>
	function addNew() {
			$("#myForm").submit();
	}
	
	function setViewMode() {
		setProgram();	
	}
	
	function save() {
		//find all the checked boxes, and basicaslly re-stave the program contact types for this program
		//pct_<contactTypeId>
		
		//alert($("#pctForm").serialize());
		
		$.ajax({	
			url : '../demographic/Contact.do?method=saveProgramContactTypes',
			type : 'POST',
			data: $("#pctForm").serialize(),
			dataType : 'json',
			success : function(data) {
				//alert(data);
				setViewMode();
			
			},
			error : function(data) {
				alert('error:' + JSON.stringify(data));
			}
	});
		
	}
	
	function setEditMode() {
		//alert('edit');
		var contactTypes = null;
		
		//grab all possible contact types
		$.ajax({	
				url : '../demographic/Contact.do?method=listContactTypes',
				type : 'GET',
				dataType : 'json',
				async: false,
				success : function(data) {
				//	showProgramData(data);
				contactTypes = data;
				
				},
				error : function(data) {
					alert('error:' + JSON.stringify(data));
				}
		});
		
		var programId = $('#programId').find(":selected").val();
		
		//grab checked contact types
		$.ajax({	
				url : '../demographic/Contact.do?method=getContactTypeDataForProgram&programId='+programId,
				type : 'GET',
				dataType : 'json',
				success : function(data) {
					//alert('data=' + JSON.stringify(data));
					//alert('contactTypes=' + JSON.stringify(contactTypes));
					
					//redraw table in edit mode
					showProgramEditData(contactTypes,data);
					//draw save button and exit buttons
				
				},
				error : function(data) {
					alert('error:' + JSON.stringify(data));
				}
		});
		
	}
	
	function setProgram() {
		var programId = $('#programId').find(":selected").val();
		
		if(programId != 0) {
			//load table with data, and show it
			$.ajax({	
				url : '../demographic/Contact.do?method=getContactTypeDataForProgram&programId='+programId,
				type : 'GET',
				dataType : 'json',
				success : function(data) {
					//console.log(JSON.stringify(data));
					showProgramData(data);
				},
				error : function(data) {
					alert('error:' + JSON.stringify(data));
				}
			});
		} else {
			
		}
	}
	
	function hasIt(contactType,theList) {

		for(var x=0;x<theList.length;x++) {
			if(theList[x].contactType.id == contactType.id) {
				return true;
			}
		}
		return false;
	}
	
	function showProgramEditData(contactTypes,data) {
		//(JSON.stringify(contactTypes));
		var tblData = '';
		
		tblData += '<input type="button" value="View" onClick="setViewMode()" /><br/>';
		
		tblData += '<table>';
		tblData += '<thead><tr><th>Personal</th><th>Health Care Provider</th><th>Other</th></tr></thead>';
			
		var personals = [];
		var healthproviders =  [];
		var others =  [];
		
		for(var x=0;x<data.length;x++) {
			//console.log(JSON.stringify(data[x]));
			if(data[x].id.category == 'Personal') {
				personals.push(data[x]);
			}
			if(data[x].id.category == 'Health Care Provider') {
				healthproviders.push(data[x]);
			}
			if(data[x].id.category == 'Other') {
				others.push(data[x]);
			}
		}
		
		tblData += '<tbody>';
		tblData += '<tr>';
			
		
		tblData += '<td><ul>';
		for(var x=0;x<contactTypes.length;x++) {
			var checked="";
			if(hasIt(contactTypes[x],personals)) {
				checked=' checked="checked" ';
			}
			
			
			var disabled = "";
			if(contactTypes[x].active != 1) {
				disabled = "&nbsp;<span style='color:red'>(Inactive)</span>";
			}
			tblData += '<li><input type="checkbox" '+checked+'  name="pct_0_'+contactTypes[x].id+'"/>'  + contactTypes[x].name +  disabled + '</li>';
		}
		tblData += '</ul></td>';
		
		tblData += '<td><ul>';
		for(var x=0;x<contactTypes.length;x++) {
			var checked="";
			if(hasIt(contactTypes[x],healthproviders)) {
				checked=' checked="checked" ';
			}
			var disabled = "";
			if(contactTypes[x].active != 1) {
				disabled = "&nbsp;<span style='color:red'>(Inactive)</span>";
			}
			tblData += '<li><input type="checkbox" '+checked+' name="pct_1_'+contactTypes[x].id+'">'  + contactTypes[x].name + disabled +'</li>';
		}
		tblData += '</ul></td>';
		
		tblData += '<td><ul>';
		for(var x=0;x<contactTypes.length;x++) {
			var checked="";
			if(hasIt(contactTypes[x],others)) {
				checked=' checked="checked" ';
			}
			var disabled="";
			
			if(contactTypes[x].active != 1) {
				disabled = "&nbsp;<span style='color:red'>(Inactive)</span>";
			}
			tblData += '<li><input type="checkbox" '+checked+'  name="pct_2_'+contactTypes[x].id+'"/>'  + contactTypes[x].name + disabled + '</li>';
		}
		tblData += '</ul></td>';
		
		tblData += '</tr>';
		
		tblData += '</tbody>';
		tblData += '</table>';
		
		tblData += '<br/>';
		
		tblData += '<input type="button" value="Save" onClick="save()" />';
		$("#dataDiv").html("");
		$("#dataDiv").append(tblData);
	}
	
	function showProgramData(data) {
		//console.log(JSON.stringify(data));
		var tblData = '';
		
		tblData += '<input type="button" value="Modify" onClick="setEditMode()" /><br/>';
		
		tblData += '<table style="width:40%">';
		tblData += '<thead><tr><th>Personal</th><th>Health Care Provider</th><th>Other</th></tr></thead>';
			
		var personals = [];
		var healthproviders =  [];
		var others =  [];
		
		for(var x=0;x<data.length;x++) {
			if(data[x].id.category == 'Personal') {
				personals.push(data[x]);
			}
			if(data[x].id.category == 'Health Care Provider') {
				healthproviders.push(data[x]);
			}
			if(data[x].id.category == 'Other') {
				others.push(data[x]);
			}
		}
		
		tblData += '<tbody>';
		tblData += '<tr>';
			
		
		tblData += '<td valign="top"><ul>';
		for(var x=0;x<personals.length;x++) {
			var disabled = (personals[x].contactType.active)?"":"&nbsp;<span style='color:red'>(Inactive)</span>";
			tblData += '<li>' + personals[x].contactType.name + disabled + '</li>';
		}
		tblData += '</ul></td>';
		
		tblData += '<td valign="top"><ul>';
		for(var x=0;x<healthproviders.length;x++) {
			var disabled = (healthproviders[x].contactType.active)?"":"&nbsp;<span style='color:red'>(Inactive)</span>";
			tblData += '<li>' + healthproviders[x].contactType.name + disabled+ '</li>';
		}
		tblData += '</ul></td>';
		
		tblData += '<td><ul>';
		for(var x=0;x<others.length;x++) {
			var disabled = (others[x].contactType.active)?"":"&nbsp;<span style='color:red'>(Inactive)</span>";
			tblData += '<li>' + others[x].contactType.name + disabled+ '</li>';
		}
		tblData += '</ul></td>';
		
		tblData += '</tr>';
		
		tblData += '</tbody>';
		tblData += '</table>';
		
		$("#dataDiv").html("");
		$("#dataDiv").append(tblData);
	}
	
	
	
</script>
</head>

<h4>Assign relationship types to a program</h4>
<body vlink="#0000FF" class="BodyStyle">
<br/>
<form name="pctForm" id="pctForm">

Select a Program:
<select onchange="setProgram()" id="programId" name="programId">
	<option value="0"></option>
	<%
		for(Program p:programs) {
		%><option value="<%=p.getId()%>"><%=p.getName() %></option><%
		}
	%>
</select>
<br/>

<div id="dataDiv">
</div>
</form>

<a href="contactTypeManager.jsp">Go back to Manager</a>
</body>
</html:html>