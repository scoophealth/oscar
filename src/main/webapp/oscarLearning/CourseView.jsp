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

<%@page import="org.oscarehr.util.LoggedInInfo"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@page import="java.util.*,org.oscarehr.learning.web.CourseManagerAction,org.oscarehr.common.model.SecRole,org.oscarehr.PMmodule.model.Program"%>


<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	//get list of courses for the drop down
	List<Program> courses = org.oscarehr.learning.web.CourseManagerAction.getCoursesByModerator(loggedInInfo.getLoggedInProviderNo());
%>
<html:html locale="true">



<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery.js"></script>

<script>

function popupOscarRx(vheight,vwidth,varpage) {
	var page = varpage;
	windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
	var popup=window.open(varpage, "<bean:message key="global.oscarRx"/>_appt", windowprops);
	if (popup != null) {
		if (popup.opener == null) {
			popup.opener = self;
		}
		popup.focus();
	}
}


function changeCourse() {
	var courseId = $("#course_id").val();
	var viewType = $("#view_type").val();

	if(courseId == 0) {
		$("#course_details").html("");
	}

	var method;
	if(viewType == 0) {
		method="getCourseProviders"
	} else {
		method="getCourseStudents";
	}
	
	$.ajax({
		type: "GET",
		url: "<%= request.getContextPath() %>/oscarLearning/CourseManager.do?method="+method+"&id=" + courseId,
		async: false,
		dataType: 'json',
		success: function(data) {	
			if(viewType == 0)		
				updateCourseDetails(data);
			else
				updateCourseStudents(data);		
		}
	});	
}

function updateCourseDetails(data) {
	var detailDiv = $("#course_details");
	detailDiv.hide();
	var html = "<br/><table border='1' width='40%'><tr><td>Provider Name</td></tr>";
	detailDiv.html();	
	 for(var x=0;x<data.length;x++) {		
		 	var name = data[x].name;
		 	var id = data[x].id;
		 	var roleId = data[x].roleId;
		 			 			 	
	       	html += "<tr><td>" + data[x].providerName + "</td></tr>";
   	}
	   	html += "</table>";	   
	detailDiv.html(html);
	
	detailDiv.show();
}

function updateCourseStudents(data) {
	var detailDiv = $("#course_details");
	detailDiv.hide();
	var html = "<br/><table border='1' width='40%'><tr><td>Patient Name</td><td>Student Name</td></tr>";
	detailDiv.html();	
	 for(var x=0;x<data.length;x++) {		
		 	var name = data[x].name;
		 	var eURL = "../oscarEncounter/IncomingEncounter.do?demographicNo="+ data[x].demographicNo;
		 	var mURL = "../demographic/demographiccontrol.jsp?demographic_no="+data[x].demographicNo+"&displaymode=edit&dboperation=search_detail";
		 	var rxURL = "../oscarRx/choosePatient.do?providerNo="+<%=session.getAttribute("user")%>+"&demographicNo=" + data[x].demographicNo;	 			 	
		 
	       	html += "<tr><td><a href=\"#\">" + data[x].name + "</a>&nbsp;<a href=\""+eURL+"\">E</a>&nbsp;<a href=\""+mURL+"\">M</a>&nbsp;<a href=\"#\" onclick=\"popupOscarRx(700,1027,"+rxURL+")\">Rx</a></td><td>" + data[x].providerName +"</td></tr>";
   	}
	   	html += "</table>";

	detailDiv.html(html);
	
	detailDiv.show();
}

</script>
<html:base />
<title><bean:message key="oscarLearning.courseView.title" />
</title>
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css">



<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

</head>

<body class="BodyStyle" vlink="#0000FF">
<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn"><bean:message
			key="oscarLearning.courseView.msgManager" /></td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td></td>
				<td>&nbsp;</td>
				<td style="text-align: right"><oscar:help keywords="course" key="app.top1"/> | <a
					href="javascript:popupStart(300,400,'About.jsp')"><bean:message
					key="global.about" /></a> | <a
					href="javascript:popupStart(300,400,'License.jsp')"><bean:message
					key="global.license" /></a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top">&nbsp;</td>
		<td class="MainTableRightColumn">
			<table>
			<tr>
			<td>Choose Course:</td> 
			<Td><select name="course_id" id="course_id">
				<%for(Program p:courses) { %>
					<option value="<%=p.getId()%>"><%=p.getName() %></option>
				<%} %>
			</select></Td>
			</tr>
			<Tr>
			<td>Choose View Type:</td> 
			<td><select name="view_type" id="view_type">				
					<option value="0">Students</option>
					<option value="1">Patients</option>						
			</select></td>
			</Tr>
			</table>
			
			<Br/>
			<input type="button" value="display" onclick="changeCourse()"/>
			<br/>
			
			<br/><br/><br/>
			<div id="course_details">
					
			</div>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>
       
</body>


</html:html>
