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
<title>ADD/DELETE A TEMPLATE</title>
<link rel="stylesheet" href="../web.css">
<meta http-equiv="expires" content="Mon,12 May 1998 00:36:05 GMT">
<meta http-equiv="Pragma" content="no-cache">
<script language="JavaScript">
<!--

function setfocus() {
  this.focus();
  document.template.templatename.focus();
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
		A TEMPLATE</font></th>
	</tr>
</table>

<table width="100%" border="1" bgcolor="ivory">
	<form name="template" method="post" action="providercontrol.jsp">
	<tr>
		<td valign="top" width="20%" align="right">Template Name:</td>
		<td><input type="text" name="templatename" value=""
			style="width: 100%" maxlength="20"></td>
	</tr>
	<tr>
		<td valign="top" width="20%" align="right">Template Text:</td>
		<td><textarea name="templatetext" style="width: 100%" rows="8"></textarea>
		</td>
	</tr>
	<tr>
		<td colspan="2">
		<div align="center"><INPUT TYPE="hidden" NAME="dboperation"
			VALUE='add_template'> <INPUT TYPE="hidden" NAME="displaymode"
			VALUE='savetemplate'> <input type="submit" value=" Save "
			name="submit"> <input type="button" name="Button"
			value="Cancel" onClick="window.close()"></div>
		</td>
	</tr>
	</form>
</table>


<table width="100%" border="1" bgcolor="navy">
	<form name="deletetemplate" method="post" action="providercontrol.jsp">
	<tr>
		<td align="center"><font color="white">Template Name: </font> <select
			name="encountertemplate_name">
<%
  List<Map> resultList = oscarSuperManager.find("providerDao", "search_templatename", new Object[] {});
  for (Map template : resultList) {
%>
			<option value="<%=template.get("encountertemplate_name")%>"><%=template.get("encountertemplate_name")%></option>
<%
  }
%>
		</select> <input type="submit" value="Delete" name="submit"> <INPUT
			TYPE="hidden" NAME="displaymode" VALUE='savetemplate'></td>
	</tr>
	</form>
</table>


</body>
</html>
