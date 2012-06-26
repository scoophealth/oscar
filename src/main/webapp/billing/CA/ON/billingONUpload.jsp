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
<%   
if(session.getAttribute("user") == null) response.sendRedirect("../../../logout.jsp");
OscarProperties props = OscarProperties.getInstance();
session.setAttribute("homepath", props.getProperty("project_home", ""));      
%>
<%@ page import="oscar.*" errorPage="errorpage.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Upload MOH Files</title>
<link rel="stylesheet" type="text/css" href="billingON.css" />

<script type="text/javascript">
<!--


//-->

function onSubmit(){
	var val1 = document.form1.file1.value;
	var n = val1.lastIndexOf('\\');
	val1 = val1.substring((n*1+1));
	//alert(val1);
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

<body bgcolor="#FFFFFF" text="#000000" onLoad="setfocus()">
<table border="0" cellspacing="0" cellpadding="0" width="100%"
	class="myDarkGreen">
	<tr>
		<th><font color="#FFFFFF"> Upload MOH Files </font></th>
	</tr>
</table>

<p>
<table width="400" border="0">
	<form id="form1" name="form1" method="post" action=""
		ENCTYPE="multipart/form-data" onsubmit="return onSubmit();">
	<tr>
		<td width="181"><b>Select diskette</b></td>
		<td width="209"><input type="file" name="file1" value=""></td>
	</tr>
	<tr>
		<td width="181"><input type="submit" name="Submit"
			value="Create Report"></td>
		<td width="209">&nbsp;</td>
	</tr>
	</form>
</table>

<p><font face="Arial, Helvetica, sans-serif" size="2">*
Select a file downloaded from EDT.</font></p>

</body>
</html>
