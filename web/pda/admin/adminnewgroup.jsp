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
  if(session.getValue("user") == null)  response.sendRedirect("../logout.jsp");
%>
<%@ page import="java.util.*,java.sql.*"
	errorPage="../provider/errorpage.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>New Group</title>
</head>
<meta http-equiv="expires" content="Mon,12 May 1998 00:36:05 GMT">
<meta http-equiv="Pragma" content="no-cache">

<script language="javascript">
<!-- start javascript ---- check to see if it is really empty in database
function setfocus() {
  this.focus();
  document.UPDATEPRE.mygroup_no.focus();
  document.UPDATEPRE.mygroup_no.select();
}
function checkForm() {
	if (UPDATEPRE.mygroup_no.value == "") {
		alert("No Group No.!");
		UPDATEPRE.mygroup_no.focus();
		return false;
	}
	return true;
}

// stop javascript -->
</script>

<body background="../images/gray_bg.jpg" bgproperties="fixed"
	onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<%
  if(request.getParameter("submit").equals("Delete") ) { //delete the group member 
    int rowsAffected=0;
    String[] param =new String[2];
    StringBuffer strbuf=new StringBuffer();

  	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
	    strbuf=new StringBuffer(e.nextElement().toString());
  		if( strbuf.toString().indexOf("displaymode")!=-1 || strbuf.toString().indexOf("submit")!=-1) continue;
      param[0]=request.getParameter(strbuf.toString());
	    param[1]=strbuf.toString().substring( strbuf.toString().indexOf(param[0])+1 ); 
	    //System.out.println("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq      " + param[0]+ "     " + param[1]);
  
      rowsAffected = apptMainBean.queryExecuteUpdate(param,"deletegroupmember");
    }
    out.println("<script language='JavaScript'>self.close();</script>");
  }
%>


<FORM NAME="UPDATEPRE" METHOD="post" ACTION="admincontrol.jsp"
	onSubmit="return checkForm();">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF">NEW
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
				<td ALIGN="center"><font face="arial"> Group No.</font></td>
				<td ALIGN="center"><font face="arial"> </font> <input
					type="text" name="mygroup_no" size="3" maxlength="2"> <font
					size="-2">(Max. 2 char.)</font></td>
			</tr>
			<%
   ResultSet rsgroup = null;
   int i=0;
   rsgroup = apptMainBean.queryResults("last_name", "searchprovider");
   while (rsgroup.next()) { 
     i++;
%>
			<tr BGCOLOR="#C4D9E7">
				<td><font face="arial"> &nbsp;<%=rsgroup.getString("last_name")%>,
				<%=rsgroup.getString("first_name")%></font></td>
				<td ALIGN="center"><font face="arial"> </font> <input
					type="checkbox" name="data<%=i%>" value="<%=i%>"> <input
					type="hidden" name="provider_no<%=i%>"
					value="<%=rsgroup.getString("provider_no")%>"> <INPUT
					TYPE="hidden" NAME="last_name<%=i%>"
					VALUE='<%=rsgroup.getString("last_name")%>'> <INPUT
					TYPE="hidden" NAME="first_name<%=i%>"
					VALUE='<%=rsgroup.getString("first_name")%>'></td>
			</tr>
			<%
   }
   apptMainBean.closePstmtConn();
%>
			<INPUT TYPE="hidden" NAME="dboperation" VALUE='savemygroup'>
			<INPUT TYPE="hidden" NAME="displaymode" VALUE='savemygroup'>

		</table>

		</td>
	</tr>
</table>
</center>

<table width="100%" BGCOLOR="#486ebd">
	<tr>
		<TD align="center"><input type="submit" name="Submit"
			value=" Save "> <INPUT TYPE="RESET" VALUE=" Exit "
			onClick="window.close();"></TD>
	</tr>
</TABLE>

</FORM>

<div align="center"><font size="1" face="Verdana" color="#0000FF"><B></B></font></div>

</body>
</html>