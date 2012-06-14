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

<%
  if(session.getValue("user") == null)
    response.sendRedirect("../login.htm");
  String curProvider_no;
  curProvider_no = (String) session.getAttribute("user");
  
  //display the main provider page
  //includeing the provider name and a month calendar
%>
<%@ page import="java.util.*, java.sql.*, oscar.*"
	errorPage="../appointment/errorpage.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />


<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>PATIENT SEARCH INFO</title>
<link rel="stylesheet" href="../web.css" />
<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
  document.titlesearch.keyword.focus();
  document.titlesearch.keyword.select();
}
function goback(){
 history.go(-1);return false;
}
//-->
</script>
</head>
<!--base target="pt_srch_main"-->
<body background="../images/gray_bg.jpg" bgproperties="fixed"
	onLoad="setfocus()">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF">PATIENT'S
		DETAIL RECORD</font></th>
	</tr>
</table>

<table border="0" cellpadding="1" cellspacing="0" width="100%"
	bgcolor="#C4D9E7">
	<form method="post" name="titlesearch" action="../cpp/cppcontrol.jsp">
	<%--@ include file="zcpptitlesearch.htm"--%>
	<tr valign="top">
		<td rowspan="2" ALIGN="right" valign="middle"><font
			face="Verdana" color="#0000FF"><b><i>Search </i></b></font></td>

		<td width="10%" nowrap><font size="1" face="Verdana"
			color="#0000FF"> 
<%
    String searchMode = request.getParameter("search_mode");
    if (searchMode == null || searchMode.isEmpty()) {
        searchMode = OscarProperties.getInstance().getProperty("default_search_mode","search_name");
    }
%>
                    <input type="radio" checked
			name="search_mode" value="<%=searchMode%>"> Name </font></td>
		<td nowrap><font size="1" face="Verdana" color="#0000FF">
		<input type="radio" name="search_mode" value="search_phone">
		Phone</font></td>
		<td nowrap><font size="1" face="Verdana" color="#0000FF">
		<input type="radio" name="search_mode" value="search_dob"> DOB</font></td>
		<td valign="middle" rowspan="2" ALIGN="left"><input type="text"
			NAME="keyword" SIZE="17" MAXLENGTH="100"> <INPUT
			TYPE="hidden" NAME="orderby" VALUE="cpp_id"> <INPUT
			TYPE="hidden" NAME="dboperation" VALUE="search_titlename"> <INPUT
			TYPE="hidden" NAME="limit1" VALUE="0"> <INPUT TYPE="hidden"
			NAME="limit2" VALUE="10"> <input type="hidden"
			name="displaymode" value=" Search"> <input type="SUBMIT"
			name="displaymode" value=" Search" size="17">
	</tr>
	<tr>

		<td nowrap><font size="1" face="Verdana" color="#0000FF">
		<input type="radio" name="search_mode" value="search_address">
		Address </font></td>
		<td nowrap><font size="1" face="Verdana" color="#0000FF">
		<input type="radio" name="search_mode" value="search_hin"> HIN</font></td>
		<td></td>
	</tr>
	<%
  String temp=null;
	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
		temp=e.nextElement().toString();
		if(temp.equals("cpp_id") || temp.equals("dboperation") ||temp.equals("displaymode")) continue;
  	out.println("<input type='hidden' name='"+temp+"' value='"+request.getParameter(temp)+"'>");
  }
	%>
	</form>
</table>


</body>
</html>
