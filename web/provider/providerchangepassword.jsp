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
  if(session.getValue("user") == null )
    response.sendRedirect("../logout.jsp");
%>
<%@ page import="java.lang.*, java.util.*, java.text.*,java.sql.*, oscar.*" errorPage="errorpage.jsp" %>

<html>
<head><title>CHANGE PASSWORD</title></head>
<meta http-equiv="Cache-Control" content="no-cache" >

<script language="javascript">
<!-- start javascript 
function setfocus() {
  this.focus();
  document.updatepassword.oldpassword.focus();
  document.updatepassword.oldpassword.select();
}
function checkPwdLength() {
	var typeInOK = false;
  var len1=document.updatepassword.mypassword.value.length;
  var len2=document.updatepassword.confirmpassword.value.length;
  if(len1==len2 && len1>5) typeInOK=true;
	if(!typeInOK) alert ("The password must be at least 6 characters \nand the confirm password should be the same as the new password.");

	return typeInOK;
}

// stop javascript -->
</script>

<body  background="../images/gray_bg.jpg" bgproperties="fixed"  onLoad="setfocus()" topmargin="0"leftmargin="0" rightmargin="0">
<FORM NAME = "updatepassword" METHOD="post" ACTION="providerupdatepassword.jsp" onSubmit="return(checkPwdLength())">
<table border=0 cellspacing=0 cellpadding=0 width="100%" >
  <tr bgcolor="#486ebd"> 
      <th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF">CHANGE YOUR PASSWORD</font></th>
  </tr>
</table>

<table width="100%" border="0" cellpadding="2" bgcolor="#eeeeee">
<tr>
      <td><font face="arial" size="2">Enter your old password and then choose 
        your new password. Click <b>Update</b> button when you're done.</font></td>
    </tr>
</table>
<center>

<table border="0"  width="100%" cellpadding="4" cellspacing="0">
<tr>
<td align="right" width="50%"><font face="arial">Enter your <b>Old&nbsp;Password:</b></font></td>
<td><input type=password name="oldpassword" value="" size=20 maxlength=10></td>
</tr>
<tr>
<td width="50%" align="right"><font face="arial">Choose a <b>New Password:</b></font></td>
<td><input type=password name="mypassword" value="" size=20 maxlength=10>
          <font size="-2">(at least 6 chars)</font></td>
</tr>
<tr>
<td width="50%" align="right"><font face="arial">Confirm your <b>New Password:</b></font></td>
<td><input type=password name="confirmpassword" value="" size=20 maxlength=10>
          <font size="-2">(at least 6 chars)</font></td>
</tr>
</table>
</center>
<table width="100%" border="0" cellpadding="4" cellspacing="0"  BGCOLOR="#486ebd">
  <tr>
    <TD align="center" width="50%">
    <INPUT TYPE="submit" VALUE="Update" SIZE="7"> &nbsp;&nbsp;&nbsp;
    <INPUT TYPE = "RESET" VALUE = " Cancel " onClick="window.close();"></TD>
  </tr>
</TABLE>
</form>
</body>
</html>