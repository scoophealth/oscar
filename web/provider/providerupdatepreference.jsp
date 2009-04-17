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
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>

<%@ page import="java.sql.*, java.util.*, oscar.*"
	errorPage="errorpage.jsp"%>
<%@ include file="/common/webAppContextAndSuperMgr.jsp"%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script LANGUAGE="JavaScript">
    <!--
    function start(){
      this.focus();
    }
    //-->
</script>
</head>

<body background="../images/gray_bg.jpg" bgproperties="fixed">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="90%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF"><bean:message
			key="provider.providerupdatepreference.description" /></font></th>
	</tr>
</table>
<%
String[] param=null;
String op = request.getParameter("dboperation"); // updatepreference
if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){
  param = new String[9];
  param[6]=request.getParameter("new_tickler_warning_window");
  if (param[6] == null) {
	param[6] = (String) session.getAttribute("newticklerwarningwindow");
  }
  param[7]=request.getParameter("default_pmm");
  if (param[7] == null) {
	  param[7] = session.getAttribute("default_pmm")==null?"disabled":(String) session.getAttribute("default_pmm");
  }
  param[8]=request.getParameter("provider_no");
  op += "_newtickler";
} else {
  param = new String[7];
  param[6]=request.getParameter("provider_no");
}

param[0]=request.getParameter("start_hour");
param[1]=request.getParameter("end_hour");
param[2]=request.getParameter("every_min");
param[3]=request.getParameter("mygroup_no");
param[4]=request.getParameter("default_servicetype");
param[5]=request.getParameter("color_template");

int rowsAffected = oscarSuperManager.update("providerDao", op, param);
if (rowsAffected >= 1) { //Successful Update of a Preference Record.
    session.setAttribute("starthour", param[0]);
    session.setAttribute("endhour", param[1]);
    session.setAttribute("everymin", param[2]);
    session.setAttribute("groupno", param[3]);
    session.setAttribute("default_servicetype", param[4]);
  if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){
    session.setAttribute("newticklerwarningwindow", param[6]);
    session.setAttribute("default_pmm", param[7]);
  }
%>
<script LANGUAGE="JavaScript">
     self.opener.refresh1();
     self.close();
</script>
<%  
} else {
	//now try to add the new preference record
	op = "add_preference";
	param[0]=request.getParameter("provider_no");
	param[1]=request.getParameter("start_hour");
	param[2]=request.getParameter("end_hour");
	param[3]=request.getParameter("every_min");
	param[4]=request.getParameter("mygroup_no");
	param[5]=request.getParameter("default_servicetype");
	param[6]=request.getParameter("color_template");
  if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){
	param[7]=request.getParameter("new_tickler_warning_window");
	if (param[7] == null) {
		param[7] = (String) session.getAttribute("newticklerwarningwindow");
	}
	param[8]=request.getParameter("default_pmm");
	if (param[8] == null) {
		param[8] = session.getAttribute("default_pmm")==null?"disabled":(String) session.getAttribute("default_pmm");
	}
	op += "_newtickler";
  }
  rowsAffected = oscarSuperManager.update("providerDao", op, param);
  if (rowsAffected == 1) { //Successful add of a Preference Record.
    session.setAttribute("starthour", param[1]);
    session.setAttribute("endhour", param[2]);
    session.setAttribute("everymin", param[3]);
    session.setAttribute("groupno", param[4]);
    session.setAttribute("default_servicetype", param[5]);
  if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){
    session.setAttribute("newticklerwarningwindow", param[7]);
    session.setAttribute("default_pmm", param[8]);
  }
%>
<script LANGUAGE="JavaScript">
     self.opener.refresh1();
     self.close();
</script>
<%  
  } else {
%>
<p>
<h1><bean:message
	key="provider.providerupdatepreference.msgUpdateFailure" /></h1>
</p>
<%  
  }
}
%>
<p></p>
<hr width="90%"/>
<form><input type="button"
	value=<bean:message key="global.btnClose"/> onClick="self.close()">
</form>
</center>
</body>
</html:html>
