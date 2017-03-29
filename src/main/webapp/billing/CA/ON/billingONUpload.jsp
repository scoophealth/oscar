<!DOCTYPE html>
<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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

--%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.billing,_admin" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin&type=_admin.billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%   
OscarProperties props = OscarProperties.getInstance();
session.setAttribute("homepath", props.getProperty("project_home", ""));      
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="oscar.*" errorPage="errorpage.jsp"%>

<html>
<head>
<title><bean:message key="admin.admin.uploadMOHFile"/></title>
<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">

<script type="text/javascript">
function onSubmit(){
	var val1 = document.form1.file1.value;
	var n = val1.lastIndexOf('\\');
	val1 = val1.substring((n*1+1));
	if (val1.length>30) {
			alert("File name: "+val1+" is too long. Please rename file and upload again!");
			return false;
	}
	if (val1.substring(0,1) == "P" || val1.substring(0,1) == "S"){
		if (document.all){
			document.all.form1.action="../../../servlet/oscar.DocumentUploadServlet";
			document.all.form1.submit();
		}else{
			document.getElementById('form1').action="../../../servlet/oscar.DocumentUploadServlet";
			document.getElementById('form1').submit();
		}
	}else{
		if (document.all){
			document.all.form1.action="/<%=props.getProperty("project_home", "")%>/oscarBilling/DocumentErrorReportUpload.do";
			document.all.form1.submit();
		}else{
			document.getElementById('form1').action="/<%=props.getProperty("project_home", "")%>/oscarBilling/DocumentErrorReportUpload.do";
			document.getElementById('form1').submit();
		}
	}
	return false;
}
</SCRIPT>
</head>

<body>
<h3><bean:message key="admin.admin.uploadMOHFile"/></h3>
<div class="container-fluid well">
	<form id="form1" name="form1" method="post" action="" ENCTYPE="multipart/form-data" onsubmit="return onSubmit();">
		Select diskette<input style="margin-left:40px;" type="file" name="file1" value="" required>
		<input class="btn btn-primary" type="submit" name="Submit" value="Create Report">
	</form>
*Select a file downloaded from EDT.
</div>
</body>
</html>
