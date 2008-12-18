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
<title><bean:message key="admin.preferenceaddarecord.title" /></title>
<link rel="stylesheet" href="../web.css">
<script LANGUAGE="JavaScript">
    <!--

		function setfocus() {
		  document.searchprovider.provider_no.focus();
		  document.searchprovider.provider_no.select();
		}

    function onsub() {
      if(document.searchprovider.provider_no.value=="" ||
	     document.searchprovider.start_hour.value=="" ||
		 document.searchprovider.end_hour.value=="" || 
		 document.searchprovider.every_min.value=="" || 
		 document.searchprovider.mygroup_no.value==""  
		) {
        alert("<bean:message key="global.msgInputKeyword"/>");
        return false;
      } else return true;
      // do nothing at the moment
      // check input data in the future 
    }
	function upCaseCtrl(ctrl) {
		ctrl.value = ctrl.value.toUpperCase();
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
			key="admin.preferenceaddarecord.description" /></font></th>
	</tr>
</table>
<table cellspacing="0" cellpadding="2" width="90%" border="0">
	<form method="post" action="admincontrol.jsp" name="searchprovider"
		onsubmit="return onsub()">
	<tr>
		<td width="40%" align="right"><bean:message
			key="admin.preference.formProviderNo" /><font color="red"> :</font></td>
		<td><input type="text" name="provider_no"></td>
	</tr>
	<tr>
		<td>
		<div align="right"><bean:message
			key="admin.preference.formStartHour" /><font color="red">:</font></div>
		</td>
		<td><input type="text" name="start_hour" value="8"></td>
	</tr>
	<tr>
		<td>
		<div align="right"><bean:message
			key="admin.preference.formEndHour" /><font color="red">:</font></div>
		</td>
		<td><input type="text" name="end_hour" value="18"></td>
	</tr>
	<tr>
		<td>
		<div align="right"><bean:message
			key="admin.preference.formPeriod" />(<font size="-2"><bean:message
			key="admin.preference.inMin" /></font>) <font color="red">:</font></div>
		</td>
		<td><input type="text" name="every_min" value="15"></td>
	</tr>
	<tr>
		<td>
		<div align="right"><bean:message
			key="admin.preference.formGroupNo" /><font color="red">:</font></div>
		</td>
		<td><input type="text" name="mygroup_no" value=""></td>
	</tr>
	<tr>
		<td align="right"><bean:message
			key="admin.preference.defaultForm" />:</td>
		<td><select name="default_servicetype">
			<option value="no">-- no --</option>
			<%  ResultSet rs1 = apptMainBean.queryResults("preference_list_servicetype");
    while (rs1.next()) { %>
			<option value="<%=rs1.getString("servicetype")%>"><%=rs1.getString("servicetype_name")%></option>
			<%  } %>
		</select></td>
	</tr>
	<tr>
		<td></td>
		<td><input type="hidden" name="new_tickler_warning_window"
			value="disable"></td>
	</tr>
	<tr>
		<td colspan="2">
		<div align="center"><input type="hidden" name="color_template"
			value="deepblue"> <input type="hidden" name="dboperation"
			value="preference_add_record"> <input type="hidden"
			name="displaymode" value="Preference_Add_Record"> <input
			type="submit" name="subbutton"
			value="<bean:message key="admin.preferenceaddarecord.btnSubmit"/>">
		</div>
		</td>
	</tr>
	</form>
</table>

<p></p>
<hr width="100%" color="orange">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr>
		<td><a href="admin.jsp"> <img src="../images/leftarrow.gif"
			border="0" width="25" height="20" align="absmiddle"><bean:message
			key="global.btnBack" /></a></td>
		<td align="right"><a href="../logout.jsp"><bean:message
			key="global.btnLogout" /><img src="../images/rightarrow.gif"
			border="0" width="25" height="20" align="absmiddle"></a></td>
	</tr>
</table>

</center>
</body>
</html:html>
