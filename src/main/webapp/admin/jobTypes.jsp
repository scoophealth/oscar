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
<title>Manage REST Clients (OAuth)</title>
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

	function editJobType(jobTypeId) {
		jQuery.getJSON("../ws/rs/jobs/jobType/"+jobTypeId, {},
        function(xml) {
			if(xml.types) {
				var job;
				if(xml.types instanceof Array) {
					job = xml.types[0];
				} else {
					job = xml.types;
				}
				
				$('#jobTypeName').val(job.name);
				$('#jobTypeDescription').val(job.description);
				$('#jobTypeClassName').val(job.className);
				$('#jobTypeEnabled').prop('checked',job.enabled);
				$('#jobTypeId').val(job.id);
			}
        });
		$('#new-jobtype').dialog('open');
	}
	
	function addNewJobType() {	
		$('#jobTypeId').val('0');
		$('#jobTypeName').val('');
		$('#jobTypeDescription').val('');
		$('#jobTypeClassName').val('');
		$('#jobTypeEnabled').prop('checked',true);
		$('#new-jobtype').dialog('open');
	}
	
	function clearJobs() {
		$("#jobTypeTable tbody tr").remove();
	}
	
	function listJobs() {
		return getJobTypes();
	}
	
	function getJobTypes() {
		jQuery.getJSON("../ws/rs/jobs/types/all",{async:false},
        function(xml) {
			if(xml.types) {
				var arr = new Array();
				if(xml.types instanceof Array) {
					arr = xml.types;
				} else {
					arr[0] = xml.types;
				}
				
				for(var i=0;i<arr.length;i++) {
					var job = arr[i];
					var html = '<tr>';
					
					html += '<td><u><a href="javascript:void(0);" onclick="editJobType('+job.id+');">'+job.name+'</a></u></td>';
					html += '<td>'+job.description+'</td>';
					html += '<td>'+job.className+'</td>';
					html += '<td>'+job.currentlyValid + '</td>';
					html += '<td>'+job.enabled+'</td>';
					html += '<td>'+new Date(job.updated)+'</td>';
					html += '</tr>';
				
					jQuery('#jobTypeTable tbody').append(html);
				}
				
			}
			
        });
	}
	
	$(document).ready(function(){
		listJobs();
		
		$( "#new-jobtype" ).dialog({
			autoOpen: false,
			height: 525,
			width: 620,
			modal: true,
			buttons: {
				"Save Job Type": function() {	
					$.post('../ws/rs/jobs/saveJobType',$('#jobTypeForm').serialize(),function(data){clearJobs();listJobs();});
					$( this ).dialog( "close" );	
					
				},
				Cancel: function() {
					$( this ).dialog( "close" );
				}
			},
			close: function() {
				
			}
		});
	
	});
</script>
</head>

<body vlink="#0000FF" class="BodyStyle">
<h4>Manage Job Types</h4>
<table id="jobTypeTable" name="jobTypeTable" class="table table-bordered table-striped table-hover table-condensed">
	<thead>
		<tr>
			<th>Name</th>
			<th>Description</th>
			<th>JAVA Class Name</th>
			<th>Valid Class</th>
			<th>Enabled</th>
			<th>Updated</th>
		</tr>
	</thead>
	<tbody>
	</tbody>
</table>
<input type="button" class="btn btn-primary" value="Add New" onClick="addNewJobType()"/>	


<div id="new-jobtype" title="OSCAR Job Type Editor">
	<p class="validateTips"></p>
	
	<form id="jobTypeForm">
		<input type="hidden" name="jobType.id" id="jobTypeId" value="0"/>
		<fieldset>
			<div class="control-group">
				<label class="control-label" for="jobTypeName">Name:*</label>
				<div class="controls">
					<input type="text" name="jobType.name" id="jobTypeName" value=""/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="jobTypeDescription">Description:</label>
				<div class="controls">
					<textarea rows="5" name="jobType.description" id="jobTypeDescription"></textarea>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="jobTypeClassName">JAVA Class Name:</label>
				<div class="controls">
					<input type="text" name="jobType.className" id="jobTypeClassName" value=""/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="jobTypeEnabled">Enabled: <input type="checkbox" name="jobType.enabled" id="jobTypeEnabled" /></label>
				<div class="controls">
					
				</div>
			</div>
			
		</fieldset>
	</form>
</div>


</body>
</html:html>
