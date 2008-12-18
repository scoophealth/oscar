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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@ page import="java.sql.*, java.util.*" errorPage="errorpage.jsp"%>
<%
  if(session.getValue("user") == null)
    response.sendRedirect("../logout.htm");
  //call the bean's queryResults() method to get the record data for updating
%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<meta http-equiv="Cache-Control" content="no-cache" />
<title><bean:message
	key="admin.preferenceupdatepreference.title" /></title>
<link rel="stylesheet" href="../web.css">
<script LANGUAGE="JavaScript">
    <!--
		function setfocus() {
		  document.updatearecord.start_hour.focus();
		  document.updatearecord.start_hour.select();
		}

    function onCancel() {
		document.location.href= "provideradmin.jsp";
	}


    //-->
    </script>
</head>

<body background="../images/gray_bg.jpg" bgproperties="fixed"
	onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">

		<th align="CENTER"><font face="Helvetica" color="#FFFFFF"><bean:message
			key="admin.preferenceupdatepreference.description" /></font></th>
	</tr>
</table>
<table cellspacing="0" cellpadding="2" width="100%" border="0">
	<form method="post" action="admincontrol.jsp" name="updatearecord">
	<%
  ResultSet rs = apptMainBean.queryResults(request.getParameter("keyword"), request.getParameter("dboperation"));
  if(rs==null) {
    out.println("<tr><td>failed</td></tr></form>");
  } else {
    while (rs.next()) {
    // the cursor of ResultSet only goes through once from top
%>
	
	<tr>
		<td width="40%" align="right"><bean:message
			key="admin.provider.formProviderNo" />:</td>
		<td>
		<% String provider_no = apptMainBean.getString(rs,"provider_no"); %> <%= provider_no %>
		<input type="hidden" name="provider_no" value="<%= provider_no %>">
		</td>
	</tr>
	<tr>
		<td>
		<div align="right"><bean:message
			key="admin.preference.formStartHour" />:</div>
		</td>
		<td><input type="text" index="3" name="start_hour"
			value="<%= apptMainBean.getString(rs,"start_hour") %>"></td>
	</tr>
	<tr>
		<td>
		<div align="right"><bean:message
			key="admin.preference.formEndHour" />:</div>
		</td>
		<td><input type="text" index="4" name="end_hour"
			value="<%= apptMainBean.getString(rs,"end_hour") %>"></td>
	</tr>
	<tr>
		<td align="right"><bean:message key="admin.preference.formPeriod" />
		(<bean:message key="admin.preference.inMin" />):</td>
		<td><input type="text" name="every_min"
			value="<%= apptMainBean.getString(rs,"every_min") %>"></td>
	</tr>
	<tr>
		<td align="right"><bean:message
			key="admin.preference.formGroupNo" />:</td>
		<td><input type="text" name="mygroup_no"
			value="<%= apptMainBean.getString(rs,"mygroup_no") %>"></td>
	</tr>
	<tr>
		<td align="right"><bean:message
			key="admin.preference.defaultForm" />:</td>
		<td><select name="default_servicetype">
			<option value="no">-- no --</option>
			<%  ResultSet rs1 = apptMainBean.queryResults("preference_list_servicetype");
    while (rs1.next()) { %>
			<option value="<%=rs1.getString("servicetype")%>"
				<%=rs1.getString("servicetype").equals(apptMainBean.getString(rs,"default_servicetype"))?"selected":""%>>
			<%=rs1.getString("servicetype_name")%></option>
			<%  } %>
		</select></td>
	</tr>
	<tr>
		<td colspan="2">
		<div align="center"><input type="hidden" name="color_template"
			value="deepblue"> <input type="hidden" name="preference_no"
			value="<%= apptMainBean.getString(rs,"preference_no")%>"> <input
			type="hidden" name="dboperation" value="preference_update_record">
		<input type="hidden" name="displaymode"
			value="Preference_Update_Record"> <input type="submit"
			name="subbutton"
			value="<bean:message key="admin.preferenceupdatepreference.btnSubmit"/>">
		<input type="button"
			value="<bean:message key="admin.preferenceupdatepreference.btnDelete"/>"
			onclick="window.location='admincontrol.jsp?keyword=<%=apptMainBean.getString(rs,"preference_no")%>&displaymode=Preference_Delete&dboperation=preference_delete'">
		</div>
		</td>
	</tr>
	<%
  }}
  apptMainBean.closePstmtConn();
%>
	</form>
</table>

<p></p>
<%@ include file="footerhtm.jsp"%></center>
</body>
</html:html>
