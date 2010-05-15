<!--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
-->

<%
  if(session.getValue("user") == null)
    response.sendRedirect("../login.htm");
%>
<%@ page import="java.util.*,java.sql.*"
	errorPage="../provider/errorpage.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>My Group</title>
</head>
<meta http-equiv="expires" content="Mon,12 May 1998 00:36:05 GMT">
<meta http-equiv="Pragma" content="no-cache">

<script language="javascript">
<!-- start javascript ---- check to see if it is really empty in database

//function upCaseCtrl(ctrl) {
//	ctrl.value = ctrl.value.toUpperCase();
//}

// stop javascript -->
</script>

<body background="../images/gray_bg.jpg" bgproperties="fixed"
	onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<FORM NAME="UPDATEPRE" METHOD="post" ACTION="admincontrol.jsp">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF">MY
		GROUP</font></th>
	</tr>
</table>

<center>
<table border="0" cellpadding="0" cellspacing="0" width="80%">
	<tr>
		<td width="100%">

		<table BORDER="0" CELLPADDING="0" CELLSPACING="1" WIDTH="100%"
			BGCOLOR="#C0C0C0">
			<tr BGCOLOR="#CCFFFF">
				<td ALIGN="center" colspan="2"><font face="arial"> Group
				No.</font></td>
				<td ALIGN="center"><font face="arial"> Provider's Name</font></td>
			</tr>
			<%
   ResultSet rsgroup = null;
   boolean bNewNo=false;
   String oldNo="";
   rsgroup = apptMainBean.queryResults("mygroup_no", "searchmygroupall");
   while (rsgroup.next()) { 
     if(!(rsgroup.getString("mygroup_no").equals(oldNo)) ) {
       bNewNo=bNewNo?false:true; oldNo=rsgroup.getString("mygroup_no");
       //System.out.println(oldNo);
     }
%>
			<tr BGCOLOR="<%=bNewNo?"white":"ivory"%>">
				<td width="10%" align="center"><input type="checkbox"
					name="<%=rsgroup.getString("mygroup_no")+rsgroup.getString("provider_no")%>"
					value="<%=rsgroup.getString("mygroup_no")%>"></td>
				<td ALIGN="center"><font face="arial"> <%=rsgroup.getString("mygroup_no")%></font></td>
				<td ALIGN="center"><font face="arial"> <%=rsgroup.getString("last_name")+","+rsgroup.getString("first_name")%></font>
				</td>
			</tr>
			<%
   }
   apptMainBean.closePstmtConn();
%>
			<INPUT TYPE="hidden" NAME="displaymode" VALUE='newgroup'>

		</table>

		</td>
	</tr>
</table>
</center>

<table width="100%" BGCOLOR="#486ebd">
	<tr>
		<TD align="center"><INPUT TYPE="submit" name="submit"
			VALUE="Delete" SIZE="7"> <INPUT TYPE="submit" name="submit"
			VALUE="New Group/Add a Member" SIZE="7"> <INPUT TYPE="RESET"
			VALUE=" Exit " onClick="window.close();"></TD>
	</tr>
</TABLE>

</FORM>

</body>
</html>