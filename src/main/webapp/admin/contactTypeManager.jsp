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
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.oscarehr.managers.ProgramManager2"%>
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
<%@ page import="java.util.List" %>
<%@ page import="org.oscarehr.util.LoggedInInfo" %>
<%@ page import="org.oscarehr.PMmodule.model.Program" %>

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
<%
	ProgramManager2 programManager = SpringUtils.getBean(ProgramManager2.class);
%>
<script>

	function getProgramName(id) {
		switch(id) {
		
		<%
			List<Program> programs = programManager.getAllPrograms(LoggedInInfo.getLoggedInInfoFromSession(request));
			for(Program program: programs) {
				
		%>
		case "<%=program.getId()%>": 
			return "<%=StringEscapeUtils.escapeJavaScript(program.getName())%>";
			
			break;
		
			
			
		<% } %>
		}
		return id;
	}
	
	function assign() {
			$("#myForm").submit();
	}
	function addNew() {
		$("#myForm2").submit();
	}
	
	$(document).ready(function(){
		showList();
	});
	
	function showList() {
		$.ajax({
			url : '../demographic/Contact.do?method=listProgramContactTypes',
			type : 'GET',
			dataType : 'json',
			success : function(data) {
				//console.log(JSON.stringify(data));
				updateUI(data);
				
			},
			error : function(data) {
				alert('error:' + JSON.stringify(data));
			}
			
		});
	}
	
	function updateUI(dataArr) {
		
		$("#mappingTable tr").remove();
		
		
		$.each(dataArr, function (index, value) {
			//var programId = value;
			var val = '<tr>';
			val += '<td>' + getProgramName(index) + '</td>';
			val += '<td>';
			val += '<ul>';
			var types = value;
			for(var i=0;i<types.length;i++) {
				val += '<li>'+ '<b>' + types[i].id.category + "</b>:" + types[i].contactType.name + '</li>';
			}
			val += '</ul>';
			val += '</td></tr>';
			
			if(types.length>0)
				$("#mappingTable").append(val);
		});
		
	}
</script>
</head>

<body vlink="#0000FF" class="BodyStyle">
<h4>Contact Types Manager</h4>
<table id="mappingTable" name="mappingTable" class="table table-bordered table-striped table-hover table-condensed">
	<thead>
		<tr>
			<th>Program</th>
			<th>Types</th>
		</tr>
	</thead>
	<tbody></tbody>
</table>
<form action="contactTypeManagerAssign.jsp" id="myForm">
<input type="button" class="btn btn-primary" value="Assign Types to Program" onClick="assign()"/>	
</form>

<form action="contactTypeManagerAdd.jsp" id="myForm2">
<input type="button" class="btn btn-primary" value="Add new relationship type" onClick="addNew()"/>	
</form>

</body>
</html:html>