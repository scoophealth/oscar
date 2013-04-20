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

<%
    String curProvider_no = (String) session.getAttribute("user");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    
    boolean isSiteAccessPrivacy=false;
%>

<security:oscarSec objectName="_site_access_privacy" roleName="<%=roleName$%>" rights="r" reverse="false">
	<%isSiteAccessPrivacy=true; %>
</security:oscarSec>


<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="admin.providersearchrecordshtm.title" /></title>
<link rel="stylesheet" href="../web.css">
<script LANGUAGE="JavaScript">
    <!--

		function setfocus() {
		  document.searchprovider.keyword.focus();
		  document.searchprovider.keyword.select();
		}

    function onsub() {
      // make keyword lower case
      var keyword = document.searchprovider.keyword.value; 
      var keywordLowerCase = keyword.toLowerCase();
      document.searchprovider.keyword.value = keywordLowerCase;
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
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF"><bean:message
			key="admin.providersearchrecordshtm.description" /></font></th>
	</tr>
</table>

<table cellspacing="0" cellpadding="2" width="100%" border="0"
	BGCOLOR="#C4D9E7">

	<form method="post" action="providersearchresults.jsp" name="searchprovider"	onsubmit="return onsub()">
	<tr valign="top">
		<td rowspan="2" align="right" valign="middle"><font	face="Verdana" color="#0000FF">
			<b><i><bean:message key="admin.search.formSearchCriteria" /></i></b></font></td>
		<td nowrap><font size="1" face="Verdana" color="#0000FF">
			<input type="radio" checked name="search_mode" value="search_name" onclick="document.forms['searchprovider'].keyword.focus();">
			<bean:message key="admin.providersearch.formLastName" /></font></td>
		<td nowrap><font size="1" face="Verdana" color="#0000FF">
			<input type="radio" name="search_mode" value="search_providerno" onclick="document.forms['searchprovider'].keyword.focus();">
			<bean:message key="admin.providersearch.formNo" /></font></td>
		<td nowrap><font size="1" face="Verdana" color="#0000FF">
			<input type="checkbox" name="search_status" value="1">
			<bean:message key="admin.providersearch.formActiveStatus" /><br />
			<input type="checkbox" name="search_status" value="0">
			<bean:message key="admin.providersearch.formInactiveStatus" /> </font></td>
		<td valign="middle" rowspan="2" ALIGN="left"><input type="text"	NAME="keyword" SIZE="17" MAXLENGTH="100"> 
			<INPUT TYPE="hidden" NAME="orderby" VALUE="last_name"> 

			<INPUT TYPE="hidden" NAME="limit1" VALUE="0"> <INPUT TYPE="hidden" NAME="limit2" VALUE="10"> 
			
			<INPUT TYPE="SUBMIT" NAME="button" VALUE=<bean:message key="admin.search.btnSubmit"/> SIZE="17"></td>
	</tr>
	</form>
</table>

<hr width="100%" color="orange">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr>
		<td><a href="admin.jsp"> <img src="../images/leftarrow.gif"	border="0" width="25" height="20" align="absmiddle">
			<bean:message key="global.btnBack" /></a></td>
		<td align="right"><a href="../logout.jsp"><bean:message	key="global.btnLogout" />
			<img src="../images/rightarrow.gif" border="0" width="25" height="20" align="absmiddle"></a></td>
	</tr>
</table>

</center>
</body>
</html:html>
