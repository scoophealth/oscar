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

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<meta http-equiv="Cache-Control" content="no-cache" />
<title>Add a Preference for a User</title>
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
        alert("You forgot to input a keyword!");
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

		<th align="CENTER"><font face="Helvetica" color="#FFFFFF">
		ADD A PREFERENCE FOR A USER</font></th>
	</tr>
</table>
<table cellspacing="0" cellpadding="2" width="90%" border="0">
	<form method="post" action="admincontrol.jsp" name="searchprovider"
		onsubmit="return onsub()">
	<tr>
		<td width="50%" align="right">Provider No.<font color="red">
		:</font></td>
		<td><input type="text" name="provider_no"></td>
	</tr>
	<tr>
		<td>
		<div align="right">Start Hour <font color="red">:</font></div>
		</td>
		<td><input type="text" name="start_hour" value="8"></td>
	</tr>
	<tr>
		<td>
		<div align="right">End Hour <font color="red">:</font></div>
		</td>
		<td><input type="text" name="end_hour" value="18"></td>
	</tr>
	<tr>
		<td>
		<div align="right">Period (<font size="-2">in min.</font>) <font
			color="red">:</font></div>
		</td>
		<td><input type="text" name="every_min" value="15"></td>
	</tr>
	<tr>
		<td>
		<div align="right">Group No <font color="red">:</font></div>
		</td>
		<td><input type="text" name="mygroup_no" value=""></td>
	</tr>
	<tr>
		<td colspan="2">
		<div align="center"><input type="hidden" name="color_template"
			value="deepblue"> <input type="hidden" name="dboperation"
			value="preference_add_record"> <input type="hidden"
			name="displaymode" value="Preference_Add_Record"> <input
			type="submit" name="subbutton" value="Add Record"></div>
		</td>
	</tr>
	</form>
</table>

<p></p>
<hr width="100%" color="orange">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr>
		<td><a href="admin.jsp"> <img src="../images/leftarrow.gif"
			border="0" width="25" height="20" align="absmiddle"> Back to
		Admin Page.</a></td>
		<td align="right"><a href="../logout.jsp">Log Out <img
			src="../images/rightarrow.gif" border="0" width="25" height="20"
			align="absmiddle"></a></td>
	</tr>
</table>

</center>
</body>
</html>
