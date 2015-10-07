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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting,_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_report&type=_admin.reporting&type=_admin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<% java.util.Properties oscarVariables = oscar.OscarProperties.getInstance(); %>
<%   
  if(session.getValue("user") == null)
    response.sendRedirect("../../../logout.jsp");
  String user_no;
  user_no = (String) session.getAttribute("user");
           String docdownload = oscarVariables.getProperty("project_home") ;;
           session.setAttribute("homepath", docdownload);      

%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Teleplan Billing Report Generator</title>
</head>

<body bgcolor="#FFFFFF" text="#000000">
<p><font face="Arial, Helvetica, sans-serif" size="2"><b>Teleplan
Billing Report Generator</b></font></p>
<form name="form1" method="post"
	action="../../../servlet/oscar.DocumentTeleplanReportUploadServlet"
	ENCTYPE="multipart/form-data"><font
	face="Arial, Helvetica, sans-serif" size="2"> </font>
<table width="400" border="0">
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
</table>

<p><font face="Arial, Helvetica, sans-serif" size="2"> </font></p>
<p>&nbsp;</p>
</form>
</body>
</html>
