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
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>
<%@ page  import="java.sql.*, java.util.*, oscar.*" errorPage="errorpage.jsp" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />

<html:html locale="true">
<head>
<script LANGUAGE="JavaScript">
<!--
function start(){
  this.focus();
}
function closeit() {
  self.opener.refresh();
  close();
}   
//-->
</script>
</head>

<body >
<center>
    <table border="0" cellspacing="0" cellpadding="0" width="90%" >
      <tr bgcolor="#486ebd"> 
            <th align="CENTER"><font face="Helvetica" color="#FFFFFF"><bean:message key="receptionist.receptionistupdatepreference.description"/></font></th>
      </tr>
    </table>
<%
String[] param =null;
if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){
	  param =new String[7];
}else{
		param =new String[6];
}
	  param[0]=request.getParameter("start_hour");
	  param[1]=request.getParameter("end_hour");
	  param[2]=request.getParameter("every_min");
	  param[3]=request.getParameter("mygroup_no");
	  param[4]=request.getParameter("color_template");
	  param[5]=request.getParameter("provider_no");
if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){
	  param[6]=request.getParameter("new_tickler_warning_window");
}
  int rowsAffected = apptMainBean.queryExecuteUpdate(param, request.getParameter("dboperation"));
  if (rowsAffected >=1) { //Successful Update of a Preference Record.
      session.setAttribute("starthour", param[0]);
      session.setAttribute("endhour", param[1]);
      session.setAttribute("everymin", param[2]);
      session.setAttribute("groupno", param[3]); 
if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){
      session.setAttribute("newticklerwarningwindow", param[6]);
}
%>
<script LANGUAGE="JavaScript">
     	self.opener.refresh1();
      self.close();
</script>
<%  
  } else {
  //now try to add the new preference record
	param[0]=request.getParameter("provider_no");
	param[1]=request.getParameter("start_hour");
	param[2]=request.getParameter("end_hour");
	param[3]=request.getParameter("every_min");
	param[4]=request.getParameter("mygroup_no");
	param[5]=request.getParameter("color_template");
if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){
	param[6]=request.getParameter("new_tickler_warning_window");
}	
  rowsAffected = apptMainBean.queryExecuteUpdate(param, "add_preference");
  if (rowsAffected ==1) { //Successful add a Preference Record.
    session.setAttribute("starthour", param[1]);
    session.setAttribute("endhour", param[2]);
    session.setAttribute("everymin", param[3]);
    session.setAttribute("groupno", param[4]);
 if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){
    session.setAttribute("newticklerwarningwindow", param[6]);
}
%>
<script LANGUAGE="JavaScript">
     	self.opener.refresh1();
      self.close();
</script>
<%  
  } else {
%>
  <p><h1><bean:message key="receptionist.receptionistupdatepreference.msgUpdateFailure"/></h1></p>
<%} 
  }
  apptMainBean.closePstmtConn();
%>
  <p></p>
  <hr width="90%"></hr>
  <form>
    <input type="button" value='<bean:message key="receptionist.receptionistupdatepreference.btnClose"/>' onClick="closeit()">
  </form>
</center>
</body>
</html:html>
