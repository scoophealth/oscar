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
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Billing Reconcilliation</title>
<script language="JavaScript">
<!--


//-->

function onSubmit(){   
   if ( document.form1.file1.value == "!" ){
      alert("You must select a file before creating a report");
      return false;
   }
   return true;
}
</SCRIPT>
</head>

<body bgcolor="#FFFFFF" text="#000000" onLoad="setfocus()">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align='CENTER'><font face="Arial, Helvetica, sans-serif"
			color="#FFFFFF"> Billing Reconcilliation </font></th>
	</tr>
</table>

<p>
<table width="400" border="0">
	<form name="form1" method="post"
		action="../../../servlet/oscar.DocumentUploadServlet"
		ENCTYPE="multipart/form-data" onsubmit="return onSubmit();">
	<tr>
		<td width="181"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Select diskette </font></b></td>
		<td width="209"><font face="Arial, Helvetica, sans-serif"
			size="2"> <input type="file" name="file1" value=""></font></td>
	</tr>
	<tr>
		<td width="181"><input type="submit" name="Submit"
			value="Create Report"></td>
		<td width="209">&nbsp;</td>
	</tr>
	</form>
</table>

<p><font face="Arial, Helvetica, sans-serif" size="2">*
Select a file first or click button 'Create Report' directly.</font></p>

</body>
</html>
