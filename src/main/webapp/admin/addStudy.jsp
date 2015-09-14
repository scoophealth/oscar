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
<%@ page import="org.oscarehr.common.dao.StudyDao, org.oscarehr.common.model.Study" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.reporting" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.reporting");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Add Edit Study</title>

<style type="text/css">
BODY {
	font-family: Arial, Verdana, Tahoma, Helvetica, sans-serif;
	background-color: #EEEEFF;
	color: #003399;
	font-size: 10pt;
	text-align:center;	
}

table {

	margin-top: 20pt;
}

</style>
<script type="text/javascript">
function validateForm() {
	var ret = true;
	var msg = "";
	var name = document.getElementById("studyName");
	var desc = document.getElementById("studyDescription");
	
	if( name.value == null || name.value == "" ) {
		msg = "Please enter a name for the study\n";
		ret = false;
	}
	
	if( desc.value == null || desc.value == "" ) {
		msg += "Please enter a description for the study";
		ret = false;
	}
	
	if( !ret ) {
		alert(msg);
	}
	else {
		window.opener.reload();
		// window.close();
	}	

		
	return ret;
}
</script>

</head>
<body class="BODY" onload="document.forms[0].studyName.focus()">
<%
String studyId = request.getParameter("studyId");
Study study = null;

if( studyId == null ) {
    studyId = "";
}
else {
    StudyDao studyDao = (StudyDao)SpringUtils.getBean(StudyDao.class);    
    study = studyDao.find(Integer.parseInt(studyId));
}

%>

<form method="post" action="../study/ManageStudy.do">
<input type="hidden" name="studyId" value="<%=studyId%>"/>
<input type="hidden" name="method" value="saveUpdateStudy"/>
<center>
<table>
<tr>
	<td>Study Name<br><input type="text" id="studyName" name="studyName" value="<%=study == null ? "" : study.getStudyName()%>"/></td>
	<td>Description<br><input type="text" id="studyDescription" name="studyDescription" value="<%=study == null || study.getDescription() == null ? "" : study.getDescription()%>"/></td>
</tr>
<tr>
	<td>Form Name<br><input type="text" name="studyForm" value="<%=study == null || study.getFormName() == null ? "" : study.getFormName()%>"/></td>
	<td>Remote Server URL<br><input type="text" name="studyRemoteURL" value="<%=study == null || study.getRemoteServerUrl() == null ? "" : study.getRemoteServerUrl()%>"/></td>
</tr>
<tr>
	<td colspan="2" style="text-align:center;">Study Link<br><input type="text" name="studyLink" value="<%=study == null || study.getStudyLink() == null ? "" : study.getStudyLink()%>"/></td>
</tr>
<tr>
	<td colspan="2" style="text-align:center;"><input type="submit" value="Save" onclick="return validateForm();"/></td>
</tr>
</table>
</center>
</form>
<%if( !studyId.equals("") ) {%>
	<jsp:include page="listDemographics.jsp"></jsp:include>
<%} %>
</body>
</html>