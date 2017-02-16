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
	$(document).ready(function(){
		refresh();
		
		$( "#newDialog" ).dialog({
			autoOpen: false,
			height: 400,
			width: 780,
			modal: true,
			buttons: {
				"Save": function() {	
					if(validateSaveContactType()) {
						$.post('../demographic/Contact.do?method=saveContactType',$('#ctForm').serialize(),function(data){refresh();});
						$( this ).dialog( "close" );	
					}
					
				},
				Cancel: function() {
					$( this ).dialog( "close" );
				}
			},
			close: function() {
				
			}
		});
		
		$("#addBtn").bind('click',function(){
			$('#contactTypeId').val('0');
			$('#contactTypeName').val('');
			$('#contactTypeMale').prop('checked',false);
			$('#contactTypeFemale').prop('checked',false);
			$('#contactTypeActive').prop('checked',true);
			
			$('#newDialog').dialog('open');
		});		
		
	});
	
	function validateSaveContactType() {
		if($('#contactTypeName').val() == '') {
			alert('please enter a name');
			return false;
		}
		return true;
	}
	
	function saveContactType() {
		$('#contactTypeId').val();
		$('#contactTypeName').val();
		$('#contactTypeMale').prop('checked');
		$('#contactTypeFemale').prop('checked');
		$('#contactTypeActive').prop('checked');
		
	}
	function toggleActive(id,active) {
		
		var op = (active)?"archiveContactType":"restoreContactType";
		$.ajax({	
			url : '../demographic/Contact.do?method='+op+'&id='+id,
			type : 'GET',
			dataType : 'json',
			success : function(data) {
				refresh();
			},
			error : function(data) {
				alert('error:' + JSON.stringify(data));
			}
		});
	}
	
	function refresh() {
		$.ajax({
			

			url : '../demographic/Contact.do?method=listContactTypes',
			type : 'GET',
			dataType : 'json',
			success : function(data) {
				//fill the table	
				//alert(JSON.stringify(data));
				updateUI(data);
			},
			error : function(data) {
				alert('error:' + JSON.stringify(data));
			}
			
		});
	}
	
	function updateUI(dataArr) {
		$("#listings tr").remove();
		for(var x=0;x<dataArr.length;x++) {
			var row = '';
			if(dataArr[x].active) {
				row = '<tr>'+ '<td><input type="button" value="Remove" onClick="toggleActive('+dataArr[x].id+','+dataArr[x].active+')"/></td>' +'<td>' + dataArr[x].name + '</td>' + '<td>' + dataArr[x].active + '</td>' +'</tr>';
			} else {
				row = '<tr>'+ '<td><input type="button" value="Restore" onClick="toggleActive('+dataArr[x].id+','+dataArr[x].active+')"/></td>' +'<td>' + dataArr[x].name + '</td>' + '<td>' + dataArr[x].active + '</td>' +'</tr>';

			}
			$("#listings").append(row);
		}
	}
</script>
</head>

<body vlink="#0000FF" class="BodyStyle">
<h4>Add new relationship types</h4>
<br/>
<table id="listings" name="listings" class="table table-bordered table-striped table-hover table-condensed">
	<thead>
		<tr>
			<th style="width:50px"></th>
			<th>Name</th>
			<th>Active</th>
		</tr>
	</thead>
	<tbody>
	</tbody>
</table>

<button class="btn btn-primary" id="addBtn">Add</button>
<br/>

<a href="contactTypeManager.jsp">Go back to Manager</a>





<div id="newDialog" title="New relationship type">
	<p class="validateTips"></p>
	
	<form id="ctForm">
		<input type="hidden" name="contactTypeId" id="contactTypeId" value="0"/>
	
	<table cellpadding="2" cellspacing="2">
		<tr>
			<td><b>Name:</b></td>
			<td><input type="text" id="contactTypeName" name="contactTypeName"/></td>
		</tr>
		<tr>
			<td><b>Applies to males:</b></td>
			<td><input type="checkbox" id="contactTypeMale" name="contactTypeMale"/></td>
		</tr>
		<tr>
			<td><b>Applies to females:</b></td>
			<td><input type="checkbox" id="contactTypeFemale" name="contactTypeFemale"/></td>
		</tr>
		<tr>
			<td><b>Active:</b></td>
			<td><input type="checkbox" id="contactTypeActive" name="contactTypeActive"/></td>
		</tr>
	</table>
</form>
</div>


</body>
</html:html>
