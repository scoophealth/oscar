<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="admin.securitysearchrecordshtm.title" /></title>
<link rel="stylesheet" href="../web.css">
<script LANGUAGE="JavaScript">
<!--

	function setfocus() {
	  document.searchprovider.keyword.focus();
	  document.searchprovider.keyword.select();
	}

  function onsub() {
    // check input data in the future 
  }
	function upCaseCtrl(ctrl) {
		ctrl.value = ctrl.value.toUpperCase();
	}

//-->
    </script>
</head>

<body onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">

		<th align="CENTER"><font face="Helvetica" color="#FFFFFF">
		<bean:message key="admin.securitysearchrecordshtm.description" /></font></th>
	</tr>
</table>

<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    
    boolean isSiteAccessPrivacy=false;
%>

<security:oscarSec objectName="_site_access_privacy" roleName="<%=roleName$%>" rights="r" reverse="false">
	<%isSiteAccessPrivacy=true; %>
</security:oscarSec>

<table cellspacing="0" cellpadding="2" width="100%" border="0"
	BGCOLOR="#C4D9E7">

	<form method="post" action="securitysearchresults.jsp" name="searchprovider"
		onsubmit="return onsub()">
	<tr valign="top">
		<td rowspan="2" align="right" valign="middle"><font
			face="Verdana" color="#0000FF"><b><i><bean:message
			key="admin.securitysearchrecordshtm.msgCriteria" /></i></b></font></td>
		<td nowrap><font size="1" face="Verdana" color="#0000FF">
		<input type="radio" name="search_mode" value="search_username">
		<bean:message key="admin.securityrecord.formUserName" /></font></td>

		<td nowrap><font size="1" face="Verdana" color="#0000FF">
		<input type="radio" checked name="search_mode"
			value="search_providerno"> <bean:message
			key="admin.securityrecord.formProviderNo" /></font></td>
		<td valign="middle" rowspan="2" ALIGN="left"><input type="text"
			NAME="keyword" SIZE="17" MAXLENGTH="100"> <INPUT
			TYPE="hidden" NAME="orderby" VALUE="user_name"> 

		<INPUT TYPE="hidden" NAME="limit1" VALUE="0"> <INPUT
			TYPE="hidden" NAME="limit2" VALUE="10"><INPUT
			TYPE="SUBMIT" NAME="button"
			VALUE="<bean:message key="admin.securitysearchrecordshtm.btnSearch"/>"
			SIZE="17"></td>
	</tr>
	<tr>
		<td nowrap><font size="1" face="Verdana" color="#0000FF"><bean:message
			key="admin.securitysearchrecordshtm.reserved" /> </font></td>
		<td nowrap><font size="1" face="Verdana" color="#0000FF">
		</font></td>

	</tr>
	</form>
</table>


<hr width="100%" color="orange">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr>
		<td><a href="admin.jsp"> <img src="../images/leftarrow.gif"
			border="0" width="25" height="20" align="absmiddle"> <bean:message
			key="admin.securitysearchrecordshtm.btnBack" /></a></td>
		<td align="right"><a href="../logout.jsp"><bean:message
			key="admin.securitysearchrecordshtm.btnLogOut" /> <img
			src="../images/rightarrow.gif" border="0" width="25" height="20"
			align="absmiddle"></a></td>
	</tr>
</table>

</center>
</body>
</html:html>
