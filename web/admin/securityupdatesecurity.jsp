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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page  import="java.sql.*, java.util.*" errorPage="errorpage.jsp" %>
<%
  if(session.getValue("user") == null)  response.sendRedirect("../logout.jsp");
%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />

<html:html locale="true">
<head>
<meta http-equiv="Cache-Control" content="no-cache" />
<title><bean:message key="admin.securityupdatesecurity.title"/></title>
<link rel="stylesheet" href="../web.css">
<script LANGUAGE="JavaScript">
    <!--
		function setfocus() {
		  document.updatearecord.user_name.focus();
		  document.updatearecord.user_name.select();
		}
    function onCancel() {
		  document.location.href= "provideradmin.jsp";
   	}
    //-->
    </script>
</head>

<body   background="../images/gray_bg.jpg" bgproperties="fixed" onLoad="setfocus()"  topmargin="0" leftmargin="0" rightmargin="0">
<center>
    <table border="0" cellspacing="0" cellpadding="0" width="100%" >
      <tr bgcolor="#486ebd"> 
            
      <th align="CENTER"><font face="Helvetica" color="#FFFFFF"><bean:message key="admin.securityupdatesecurity.description"/></font></th>
      </tr>
    </table>
  <table cellspacing="0" cellpadding="2" width="100%" border="0">
    <form method="post" action="admincontrol.jsp" name="updatearecord" >
      <%
  ResultSet rs = apptMainBean.queryResults(request.getParameter("keyword"), request.getParameter("dboperation"));
  if(rs==null) {%>
    <tr><td><bean:message key="admin.securityupdatesecurity.msgFailed"/></td></tr></form>
<%
  } else {
    while (rs.next()) {
    // the cursor of ResultSet only goes through once from top
%>
      <tr> 
        <td width="50%" align="right"><bean:message key="admin.securityrecord.formUserName"/>: </td>
        <td> 
          <input type="text"  name="user_name" value="<%= rs.getString("user_name") %>">
        </td>
      </tr>
      <tr> 
        <td  align="right" nowrap><bean:message key="admin.securityrecord.formPassword"/><font size='-2'>(<=10char)</font>: 
        </td>
        <td> 
          <input type="text" name="password" value="<%= rs.getString("password") %>">
        </td>
      </tr>
      <tr> 
        <td> 
          <div align="right"><bean:message key="admin.securityrecord.formProviderNo"/>: </div>
        </td>
        <td> 
          <% String provider_no = rs.getString("provider_no"); %>
          <%= provider_no %> 
          <input type="hidden"  index="4" name="provider_no" value="<%= rs.getString("provider_no") %>">
        </td>
      </tr>
      <tr> 
        <td  align="right" nowrap><bean:message key="admin.securityrecord.formPIN"/>:</td>
        <td> 
          <input type="text" name="pin" value="<%= rs.getString("pin")==null?"":rs.getString("pin") %>">
        </td>
      </tr>
      <tr> 
        <td colspan="2" align="center"> 
            <input type="hidden" name="security_no" value="<%= rs.getString("security_no")%>">
            <input type="hidden" name="dboperation" value="security_update_record">
            <input type="hidden" name="displaymode" value="Security_Update_Record">
            <input type="submit" name="subbutton" value='<bean:message key="admin.securityupdatesecurity.btnSubmit"/>'>
            <input type="button" value="<bean:message key="admin.securityupdatesecurity.btnDelete"/>" onclick="window.location='admincontrol.jsp?keyword=<%=rs.getString("security_no")%>&displaymode=Security_Delete&dboperation=security_delete'">           
        </td>
      </tr>
      <%
  }}
  apptMainBean.closePstmtConn();
%>
    </form>
  </table>
  
  <p></p>
<%@ include file="footerhtm.jsp" %>
</center>
</body>
</html:html>
