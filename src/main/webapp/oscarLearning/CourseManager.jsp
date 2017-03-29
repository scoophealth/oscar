<!DOCTYPE html>
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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>

<%@ page import="java.util.*,org.oscarehr.learning.web.CourseManagerAction,org.oscarehr.common.model.SecRole"%>


<%
	List<SecRole> roles = CourseManagerAction.getRoles();
	SecRole studentRole=null;
	SecRole moderatorRole=null;
	for(SecRole role:roles) {
		if(role.getName().equalsIgnoreCase("student")) {
			studentRole = role;
		}
		if(role.getName().equalsIgnoreCase("moderator")) {
			moderatorRole = role;
		}
		
	}
%>

<html:html locale="true">



<head>
<title><bean:message key="oscarLearning.courseManager.title" /></title>
<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">

<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.9.1.js"></script>

<script type="text/javascript">
$(document).ready(function() {
	$("#create_course_form").hide();	
});

$(document).ready(function() {
	$("#new_course_link").click(function() {newCourseEvent(); return false;});	
	$("#new_course_btn").click(function() {createGroup();});
});

$(document).ready(function() {
	updateCourseList();
});

function newCourseEvent() {
	$("#create_course_form").show()
	$("#messages").hide();
	$("#new_course_name").val("");
}

function updateCourseList() {
	$.ajax({
		type: "GET",
		url: "<%= request.getContextPath() %>/oscarLearning/CourseManager.do?method=getAllCourses",
		async: false,
		dataType: 'json',
		success: function(data) {
			populateCourseList(data);		
		}
	});		
}
function populateCourseList(data) {
	$("#course_list").children().remove();
	$("#course_list").append("<option value='0'>Select a Course</option>");
	 for(var x=0;x<data.length;x++) {		
		 	var name = data[x].name;
		 	var id = data[x].id;
	       	$('#course_list').append("<option value=\""+id+"\">"+name+"</option>");		       	  
      }
}

function createGroup() {

	$.ajax({
		type: "POST",
		data: {'name': $("#new_course_name").val() },
		dataType: 'text',
		url: "<%= request.getContextPath() %>/oscarLearning/CourseManager.do?method=createCourse",
		async: false,
		success: function(message) {
			$("#create_course_form").hide();
			$("#messages").html("<h4 style='color:blue'>"+message+"</h4><br/>");
			$("#messages").show();
			updateCourseList();
		},
		error: function() {
			alert('There was a problem processing your request.');
		}
	});	

			
}

function chooseCourse() {
	var courseId = $("#course_list").val();
	if(courseId == 0) {
		$("#course_details").html("");
	}
	
	$.ajax({
		type: "GET",
		url: "<%= request.getContextPath() %>/oscarLearning/CourseManager.do?method=getCourseDetails&id=" + courseId,
		async: false,
		dataType: 'json',
		success: function(data) {			
			updateCourseDetails(data);		
		}
	});			
}

function updateCourseDetails(data) {
	var detailDiv = $("#course_details");
	detailDiv.hide();
	var html = "<br/><form id=\"course_detail_form\"><input type=\"hidden\" name=\"courseId\" value=\""+$("#course_list").val()+"\"/><table border='1' width='40%'><tr><td></td><td>Provider Name</td><td>Role</td></tr>";
	detailDiv.html();	
	 for(var x=0;x<data.length;x++) {		
		 	var name = data[x].name;
		 	var id = data[x].id;
		 	var roleId = data[x].roleId;
		 	var checked="";
		 	if(data[x].checked == true) {
			 	checked="checked=\"checked\"";
		 	}
		 			 	
	       	html += "<tr><td><input type='checkbox' name=\"checked_"+data[x].providerNo+"\" id=\""+data[x].providerNo+"\" "+checked+"/></td><td>" + data[x].providerName + "</td><td>"+ generateRoleDropDown("role_" + data[x].providerNo,roleId) +"</td>";
   	}
	   	html += "</table>";
	   	html += "<input type=\"submit\" value=\"Save Changes\" id=\"save_course_details\"></form>";
	detailDiv.html(html);
	$("#course_detail_form").live('submit',submitCourseDetails);
	detailDiv.show();
}

function generateRoleDropDown(name,roleId) {
	var html = "<select name='" + name + "' id='" + name + "'><option value='0'>Choose One</option>";
	var studentSelected="";
	var moderatorSelected="";
	
	if(roleId == <%=studentRole.getId()%>)
		studentSelected="selected=\"selected\"";	
	if(roleId == <%=moderatorRole.getId()%>)
		moderatorSelected="selected=\"selected\"";
	
	//student
	html += "<option value=\"<%=studentRole.getId()%>\" "+studentSelected+"><%=studentRole.getName()%></option>";
	html += "<option value=\"<%=moderatorRole.getId()%>\" "+moderatorSelected+"><%=moderatorRole.getName()%></option>";	
	html += "</select>";

	return html;
	
}

function submitCourseDetails() {

	var validate = validateForm();
	if(!validate) {
		return false;
	}
	
	$.ajax({
		type: "POST",
		data: $("#course_detail_form").serialize(),
		dataType: 'text',
		url: "<%= request.getContextPath() %>/oscarLearning/CourseManager.do?method=saveCourseDetails",
		async: false,
		success: function(message) {
			alert('Changes Saved');	
		},
		error: function() {
			alert('There was a problem processing your request.');
		}
	});	
	
	return false;
}

function validateForm() {
	var passed=true;
	$("input[type='checkbox']").each(function() {		
		var checked = $(this).attr('checked');
		if(checked) {
			var providerNo = $(this).attr('id');		
			var roleId = $("#role_"+providerNo).val();
			if(roleId == 0) {
				alert('Please make sure that each user in the course is assigned a valid role');
				passed=false;
			}	
		}		
	});
	return passed;
	
}

</script>
</head>

<body>

<!--
removed for now since "course" provides no usefule results that I can see
<small><oscar:help keywords="course" key="app.top1"/></small>
-->

<h3><bean:message key="admin.admin.learning.manageCourses"/></h3>
<div class="container-fluid">

	<div class="well form-inline">				
	Current Courses:<br>
	<select id="course_list" onchange="chooseCourse()"><option value="0">Select a Course</option></select>
	<a href="#" id="new_course_link" class="btn">Create New Course</a>
	</div>		
			
	<div id="create_course_form" style="display:none" class="well form-inline">
		<h4>Create new Course</h4>

				Course Name: <br>
				<input id="new_course_name" name="new_course_name" type="text" size="40"/>
				<input id="new_course_btn" type="button" value="Create Course" class="btn" />
	
	</div>

	<div id="messages" style="display:none">				
	</div>

	<div id="course_details" style="display:none">

	</div>
			

</div><!--container-->
</body>


</html:html>
