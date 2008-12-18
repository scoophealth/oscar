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

<%@ page import="java.sql.*, java.util.*" errorPage="errorpage.jsp"%>
<%
  if(session.getValue("user") == null)
    response.sendRedirect("../login.htm");
  //call the bean's queryResults() method to get the record data for updating
%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<meta http-equiv="Cache-Control" content="no-cache" />
<title>OSCAR Project</title>
<link rel="stylesheet" href="../web.css">
<script LANGUAGE="JavaScript">
    <!--
		function setfocus() {
		  document.updatearecord.last_name.focus();
		  document.updatearecord.last_name.select();
		}

    //-->
    </script>
</head>

<body background="../images/gray_bg.jpg" bgproperties="fixed"
	onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF">
		UPDATE A PROVIDER RECORD</font></th>
	</tr>
</table>
<form method="post" action="admincontrol.jsp" name="updatearecord">
<%
  ResultSet rs = apptMainBean.queryResults(request.getParameter("keyword"), request.getParameter("dboperation"));
  if(rs==null) {
    out.println("failed");
  } else {
    while (rs.next()) {
    // the cursor of ResultSet only goes through once from top
%> <xml id="xml_list"> <providercomments> <%=rs.getString("comments")%>
</providercomments> </xml>
<table cellspacing="0" cellpadding="2" width="100%" border="0"
	datasrc='#xml_list'>

	<tr>
		<td width="50%" align="right">Provider No.:</td>
		<td>
		<% String provider_no = rs.getString("provider_no"); %><%= provider_no %>
		<input type="hidden" name="provider_no" value="<%= provider_no %>">
		<input type="hidden" name="dboperation" value="provider_update_record"></td>
	</tr>
	<tr>
		<td>
		<div align="right">Last Name:</div>
		</td>
		<td><input type="text" index="3" name="last_name"
			value="<%= rs.getString("last_name") %>"></td>
	</tr>
	<tr>
		<td>
		<div align="right">First Name:</div>
		</td>
		<td><input type="text" index="4" name="first_name"
			value="<%= rs.getString("first_name") %>"></td>
	</tr>
	<tr>
		<td align="right">Type (receptionist/doctor/admin):</td>
		<td><input type="text" name="provider_type"
			value="<%= rs.getString("provider_type") %>"></td>
	</tr>
	<tr>
		<td align="right">Specialty:</td>
		<td><input type="text" name="specialty"
			value="<%= rs.getString("specialty") %>"></td>
	</tr>
	<tr>
		<td align="right">Team:</td>
		<td><input type="text" name="team"
			value="<%= rs.getString("team") %>"></td>
	</tr>
	<tr>
		<td align="right">Sex:</td>
		<td><input type="text" name="sex"
			value="<%= rs.getString("sex") %>"></td>
	</tr>
	<tr>
		<td align="right">DOB:</td>
		<td><input type="text" name="dob"
			value="<%= rs.getString("dob") %>"></td>
	</tr>
	<tr>
		<td align="right">Address:</td>
		<td><input type="text" name="address"
			value="<%= rs.getString("address") %>" size="40"></td>
	</tr>
	<tr>
		<td align="right">Phone:</td>
		<td><input type="text" name="phone"
			value="<%= rs.getString("phone") %>"></td>
	</tr>
	<tr>
		<td align="right">Pager:</td>
		<td><input type="text" name="xml_p_pager" value=""
			datafld='xml_p_pager'></td>
	</tr>
	<tr>
		<td align="right">Cell:</td>
		<td><input type="text" name="xml_p_cell" value=""
			datafld='xml_p_cell'></td>
	</tr>
	<tr>
		<td align="right">Other Phone:</td>
		<td><input type="text" name="xml_p_phone2" value=""
			datafld='xml_p_phone2'></td>
	</tr>
	<tr>
		<td align="right">Fax:</td>
		<td><input type="text" name="xml_p_fax" value=""
			datafld='xml_p_fax'></td>
	</tr>
	<tr>
		<td align="right">OHIP NO:</td>
		<td><input type="text" name="ohip_no"
			value="<%= rs.getString("ohip_no") %>"></td>
	</tr>
	<tr>
		<td align="right">RMA NO:</td>
		<td><input type="text" name="rma_no"
			value="<%= rs.getString("rma_no") %>"></td>
	</tr>
	<tr>
		<td align="right">Billing No.:</td>
		<td><input type="text" name="billing_no"
			value="<%= rs.getString("billing_no") %>"></td>
	</tr>
	<tr>
		<td align="right">HSO NO:</td>
		<td><input type="text" name="hso_no"
			value="<%= rs.getString("hso_no") %>"></td>
	</tr>
	<tr>
		<td align="right">Status:</td>
		<td><input type="text" name="status"
			value="<%= rs.getString("status") %>"></td>
	</tr>
	<tr>
		<td align="right">Specialty Code:</td>
		<td><input type="text" name="xml_p_specialty_code" value=""
			datafld='xml_p_specialty_code'></td>
	</tr>
	<tr>
		<td align="right">Billing Group No.:</td>
		<td><input type="text" name="xml_p_billinggroup_no" value=""
			datafld='xml_p_billinggroup_no'></td>
	</tr>
	<tr>
		<td align="right">SLP USERNAME:</td>
		<td><input type="text" name="xml_p_slpusername" value=""
			datafld='xml_p_slpusername'></td>
	</tr>
	<tr>
		<td align="right">SLP PASSWORD:</td>
		<td><input type="text" name="xml_p_slppassword" value=""
			datafld='xml_p_slppassword'></td>
	</tr>
	<tr>
		<td colspan="2">
		<div align="center"><input type="hidden" name="displaymode"
			value="Provider_Update_Record"> <input type="submit"
			name="subbutton" value="Update Record"> <a
			href='admincontrol.jsp?keyword=<%=rs.getString("provider_no")%>&displaymode=Provider_Delete&dboperation=provider_delete'>
		<img src="../images/buttondelete.gif" width="73" height="28"
			border="0" align="absmiddle" alt="Delete the Record"></a> <!--input type="button" name="Button" value="Cancel" onClick="onCancel()"-->
		</div>
		</td>
	</tr>
</table>
<%
  }}
  apptMainBean.closePstmtConn();
%>
</form>

<p></p>
<%@ include file="footer.htm"%></center>
</body>
</html>
