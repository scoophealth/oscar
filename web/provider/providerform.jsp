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

<%@ page import="java.util.*, java.sql.*, oscar.*"
	errorPage="errorpage.jsp"%>
<%@ include file="/common/webAppContextAndSuperMgr.jsp"%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>ADD/DELETE A FORM</title>
<link rel="stylesheet" href="../web.css">
<meta http-equiv="expires" content="Mon,12 May 1998 00:36:05 GMT">
<meta http-equiv="Pragma" content="no-cache">
<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
  document.form.formname.focus();
  //document.template.templatename.select();
}
//-->
</SCRIPT>
</head>
<body background="../images/gray_bg.jpg" bgproperties="fixed"
	onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF">ADD/DELETE
		A FORM</font></th>
	</tr>
</table>

<table width="100%" border="1" bgcolor="ivory">
	<form name="form" method="post" action="providercontrol.jsp">
	<tr>
		<td valign="top" width="30%" align="right">Name (shown):</td>
		<td><input type="text" name="formname" value=""
			style="width: 100%"></td>
	</tr>
	<tr>
		<td valign="top" width="30%" align="right">Form Name:</td>
		<td><input type="text" name="formfilename" style="width: 100%"></td>
	</tr>
	<tr>
		<td valign="top" width="30%" align="right">Form Table:</td>
		<td><input type="text" name="formtable" style="width: 100%"></td>
	</tr>
	<tr>
		<td colspan="2">
		<div align="center"><INPUT TYPE="hidden" NAME="dboperation"
			VALUE='add_encounterform'> <INPUT TYPE="hidden"
			NAME="displaymode" VALUE='save_encounterform'> <input
			type="submit" value=" Save " name="submit"> <input
			type="button" name="Button" value="Cancel" onClick="window.close()">
		</div>
		</td>
	</tr>
	</form>
</table>


<table width="100%" border="1" bgcolor="navy">
	<form name="deleteform" method="post" action="providercontrol.jsp">
	<tr>
		<td align="center"><font color="white">Form Name: </font> <select
			name="encounterform_name">
<%
  List<Map> resultList = oscarSuperManager.find("providerDao", "search_encounterformname", new Object[] {});
  for (Map form : resultList) {
%>
			<option value="<%=form.get("form_name")%>"><%=form.get("form_name")%></option>
<%
  }
%>
		</select> <input type="submit" value="Delete" name="submit"> <INPUT
			TYPE="hidden" NAME="displaymode" VALUE='save_encounterform'>
		</td>
	</tr>
	</form>
</table>


</body>
</html>
