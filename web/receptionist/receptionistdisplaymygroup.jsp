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
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
-->

<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
%>
<%@ page import="java.util.*,java.sql.*" errorPage="../provider/errorpage.jsp" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />

<html>
<head><title> My Group</title></head>
      <meta http-equiv="expires" content="Mon,12 May 1998 00:36:05 GMT">
      <meta http-equiv="Pragma" content="no-cache">

<script language="javascript">
<!--
function setfocus() {
  this.focus();
}
// stop -->
</script>

<body bgproperties="fixed"  onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<FORM NAME = "UPDATEPRE" METHOD="post" ACTION="receptionistcontrol.jsp">
<table border=0 cellspacing=0 cellpadding=0 width="100%" >
  <tr bgcolor="#CCCCFF"> 
      <th align=CENTER NOWRAP>MY GROUP</th>
  </tr>
</table>

<center>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr><td width="100%">
  
          <table BORDER="0" CELLPADDING="0" CELLSPACING="1" WIDTH="100%" BGCOLOR="#C0C0C0">
            <tr BGCOLOR="#CCFFFF"  ALIGN="center"> 
              <th colspan="2">  Group No.</th>
              <th>Provider's Name </th>
              <th>Order</th>
          </tr>
<%
   ResultSet rsgroup = null;
   boolean bNewNo=false;
   String oldNo="";
   rsgroup = apptMainBean.queryResults("mygroup_no", "searchmygroupall");
   while (rsgroup.next()) { 
     bNewNo=bNewNo?false:true;
%>
          <tr BGCOLOR="<%=bNewNo?"white":"#EEEEFF"%>">
            <td width="10%" align="center"><input type="checkbox" name="<%=rsgroup.getString("mygroup_no")+rsgroup.getString("provider_no")%>" value="<%=rsgroup.getString("mygroup_no")%>">
            </td>
            <td ALIGN="center"> <%=rsgroup.getString("mygroup_no")%></td>
            <td> &nbsp; <%=rsgroup.getString("last_name")+", "+rsgroup.getString("first_name")%> </td>
            <td ALIGN="center"><INPUT TYPE="text" name="__vieworder<%=rsgroup.getString("mygroup_no")+rsgroup.getString("provider_no")%>" VALUE="<%=rsgroup.getString("vieworder")==null?"":rsgroup.getString("vieworder")%>" SIZE="3" maxlength="2"> </td>
          </tr>
<%
   }
   apptMainBean.closePstmtConn();
%>
              <INPUT TYPE="hidden" NAME="displaymode" VALUE='newgroup'>

        </table>
	
	</td></tr>
</table>
</center>

<table width="100%">
  <tr bgcolor="#CCCCFF">
    <TD align="center" >
      <INPUT TYPE="submit" name="submit" VALUE="Update" SIZE="7">
      <INPUT TYPE="submit" name="submit" VALUE="Delete" SIZE="7">
      <INPUT TYPE="submit" name="submit" VALUE="New Group/Add a Member" SIZE="7">
      <INPUT TYPE = "RESET" VALUE = " Exit " onClick="window.close();"></TD>
  </tr>
</TABLE>

</FORM>

</body>
</html>