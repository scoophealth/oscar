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

<%
  if(session.getValue("user") == null) response.sendRedirect("../login.jsp");
%>
<%@ page import="java.util.*" errorPage="errorpage.jsp"%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="receptionist.receptionistpreference.title" /></title>
<meta http-equiv="Cache-Control" content="no-cache">

<script language="javascript">
<!-- start javascript 
function setfocus() {
  this.focus();
  document.UPDATEPRE.start_hour.focus();
  document.UPDATEPRE.start_hour.select();
}

function upCaseCtrl(ctrl) {
	ctrl.value = ctrl.value.toUpperCase();
}
function checkTypeNum(typeIn) {
	var typeInOK = true;
	var i = 0;
	var length = typeIn.length;
	var ch;
	// walk through a string and find a number
	if (length>=1) {
	  while (i <  length) {
		ch = typeIn.substring(i, i+1);
		if (ch == ".") { i++; continue; }
		if ((ch < "0") || (ch > "9") ) {
			typeInOK = false;
			break;
		}
	    i++;
      }
	} else typeInOK = false;
	return typeInOK;
}
function checkTypeIn(obj) {
    if(!checkTypeNum(obj.value) ) {
	  alert ('<bean:message key="receptionist.receptionistpreference.msgTypeNumberField"/>');
	}
}
function checkTypeInAll() {
  var checkin = false;
  var s=0;
  var e=0;
  if(checkTypeNum(document.UPDATEPRE.start_hour.value) && checkTypeNum(document.UPDATEPRE.end_hour.value) && checkTypeNum(document.UPDATEPRE.every_min.value)) {
    s=eval(document.UPDATEPRE.start_hour.value);
    e=eval(document.UPDATEPRE.end_hour.value);
    if(e < 24 && s <e ) {
	    checkin = true;
	  } else {
	    alert ('<bean:message key="receptionist.receptionistpreference.msgSomethingWrongData"/>');
	  }
	} else {
      alert ('<bean:message key="receptionist.receptionistpreference.msgTypeNumberSomeFields"/>'); 
	}
	return checkin;
}

// stop javascript -->
</script>
</head>

<body background="../images/gray_bg.jpg" bgproperties="fixed"
	onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<FORM NAME="UPDATEPRE" METHOD="post" ACTION="receptionistcontrol.jsp"
	onSubmit="return(checkTypeInAll())">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF"><bean:message
			key="receptionist.receptionistpreference.description" /></font></th>
	</tr>
</table>


<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td width="100%">

		<table BORDER="0" CELLPADDING="0" CELLSPACING="1" WIDTH="100%"
			BGCOLOR="#C0C0C0">
			<tr valign="middle">
				<td width="25%" BGCOLOR="#C4D9E7" ALIGN="right"><font
					face="arial"><bean:message
					key="receptionist.preference.formStartHour" />:</font></td>
				<td width="25%" BGCOLOR="#C4D9E7" ALIGN="LEFT"><INPUT
					TYPE="TEXT" NAME="start_hour"
					VALUE='<%=request.getParameter("start_hour")%>' WIDTH="25"
					HEIGHT="20" border="0" hspace="2" size="10" maxlength="2"
					onBlur="checkTypeIn(this)"></td>
				<td width="28%" BGCOLOR="#C4D9E7" ALIGN="right"><font
					face="arial"><bean:message
					key="receptionist.preference.formEndHour" />:<font size='-2'>(<24)</font>
				:</font></td>
				<td width="22%" BGCOLOR="#C4D9E7" ALIGN="LEFT"><INPUT
					TYPE="TEXT" NAME="end_hour"
					VALUE='<%=request.getParameter("end_hour")%>'
					onBlur="checkTypeIn(this)" WIDTH="25" HEIGHT="20" border="0"
					hspace="2" size="10" maxlength="3"></td>
			</tr>
			<tr valign="middle">
				<td BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<div align="right"><font face="arial"><font
					face="arial"><bean:message
					key="receptionist.preference.formPeriod" />:</font></font></div>
				</td>
				<td BGCOLOR="#C4D9E7" ALIGN="LEFT"><INPUT TYPE="TEXT"
					NAME="every_min" VALUE='<%=request.getParameter("every_min")%>'
					WIDTH="25" HEIGHT="20" border="0" hspace="2" size="10"
					maxlength="3" onBlur="checkTypeIn(this)"></td>
				<td BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<div align="right"><font face="arial"> <a href=#
					onClick="popupPage(360,600,'receptionistcontrol.jsp?displaymode=displaymygroup&dboperation=searchmygroupall' )"><font
					size="-3"><bean:message
					key="receptionist.receptionistpreference.viewEdit" /></font><bean:message
					key="receptionist.receptionistpreference.btnGroupNo" /></a>:</font></div>
				</td>
				<td BGCOLOR="#C4D9E7" ALIGN="LEFT"><INPUT TYPE="TEXT"
					NAME="mygroup_no" VALUE='<%=request.getParameter("mygroup_no")%>'
					WIDTH="25" HEIGHT="20" border="0" hspace="2" size="10"
					maxlength="10"></td>
			</tr>
			<INPUT TYPE="hidden" NAME="provider_no"
				VALUE='<%=request.getParameter("provider_no")%>'>
			<INPUT TYPE="hidden" NAME="color_template" VALUE='deepblue'>
			<INPUT TYPE="hidden" NAME="dboperation" VALUE='updatepreference'>
			<INPUT TYPE="hidden" NAME="displaymode" VALUE='updatepreference'>

		</table>

		</td>
	</tr>
</table>

<table width="100%" BGCOLOR="#486ebd">
	<tr>
		<TD align="RIGHT" width="50%"><INPUT TYPE="submit"
			VALUE='<bean:message key="receptionist.receptionistpreference.btnSubmit"/>'
			SIZE="7"></TD>
		<TD></TD>
		<TD align="LEFT"><INPUT TYPE="RESET"
			VALUE='<bean:message key="receptionist.receptionistpreference.btnReset"/>'
			onClick="window.close();"></TD>
	</tr>
</TABLE>

</FORM>

<table width="100%" BGCOLOR="eeeeee">
	<tr>
		<TD align="center"><a href=#
			onClick="popupPage(230,600,'../provider/providerchangepassword.jsp');return false;"><bean:message
			key="receptionist.receptionistpreference.btnChangePassword" /></a></td>
	</tr>
</table>

</body>
</html:html>
