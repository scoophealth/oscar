<%--

    Copyright (c) 2001-2002. Andromedia. All Rights Reserved.
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

    This software was written for
    Andromedia, to be provided as
    part of the OSCAR McMaster
    EMR System

--%>
<%
  	if (session.getAttribute("user") == null){
		response.sendRedirect("../../logout.jsp");
	}
%>

<%@page import="oscar.oscarDB.DBHandler"%><html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>OSCAR Providers</title>
<link rel="stylesheet" href="../../../../share/css/oscar.css">
</head>
<%
	String form=request.getParameter("form"),field=request.getParameter("field");
%>
<script language="JavaScript">
function posttoText(index){
   self.close();
   opener.document.<%=form%>.<%=field%>.value = index;
	opener.focus();		
}
</script>
<body bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0"
	bgcolor="#D3D3D3">
	<tr>
		<td height="40" width="25"></td>
		<td width="90%" align="left">
		<p><font face="Verdana" color="#4D4D4D"><b><font
			size="4">oscar<font size="3">Providers</font></font></b></font></p>
		</td>
	</tr>
</table>
<br>
<table width="100%" border="0" cellspacing="5" cellpadding="0">
	<tr bgcolor="#D4D4D4">
		<td>Link</td>
		<td>Provider</td>
	</tr>
	<%
	boolean color = false;
	java.sql.ResultSet rs = DBHandler.GetSQL("SELECT first_name, last_name, ohip_no  FROM provider WHERE ohip_no<> '' ORDER BY first_name, last_name");
	while (rs.next())
	{
%>
	<tr <%=((color) ? "bgcolor=\"#F6F6F6\"" : "")%> align="left"
		valign="top">
		<td class="SmallerText"><a href=#
			onClick="posttoText('<%=oscar.Misc.getString(rs,"ohip_no")%>');"><%=oscar.Misc.getString(rs,"ohip_no")%></a>
		</td>
		<td class="SmallerText">Dr. <%=oscar.Misc.getString(rs,"first_name")%> <%=oscar.Misc.getString(rs,"last_name")%>
		</td>
	</tr>
	<%
		color = !(color);
	}
%>
	<tr bgcolor="#D4D4D4">
		<td colspan="5">&nbsp</td>
	</tr>
</table>
</body>
</html>
