
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
  if (session.getValue("user") == null)
    response.sendRedirect("../../../logout.jsp");
  String user_no;
  user_no = (String) session.getAttribute("user");
%>
<%@page import="java.util.*, java.sql.*, oscar.*, java.net.*"%>
<%@include file="../../../admin/dbconnection.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%@include file="dbBilling.jsp"%>
<%
  String search = "", search2 = "";
  search = request.getParameter("search");
  if (search.compareTo("") == 0) {
    search = "search_diagnostic_code";
  }
  String codeName = request.getParameter("name");
  //  int intCode = 0;
  //    intCode = codeName.indexOf(',');
  //    if (intCode == -1){
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Diagnostic Code Search</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-2">
<script LANGUAGE="JavaScript">
<!--
function CodeAttach(File2) {

      self.close();
      self.opener.document.serviceform.xml_diagnostic_detail.value = File2;
}
-->
</script>
</head>
<body bgcolor="#FFFFFF" text="#000000"
	onLoad="window.setTimeout('Refresh()',3000)" topmargin="0"
	leftmargin="0" rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP bgcolor="#CCCCFF"><font face="Helvetica"
			color="#000000">Diagnostic Code Search</font> <font
			face="Arial, Helvetica, sans-serif" color="#FF0000">(Maximum 3
		selections)</font></th>
	</tr>
</table>

<pre>
<%
  String coderange = request.getParameter("coderange");
  String codedesc = request.getParameter("codedesc");
  if (codedesc != null) {
    if (codedesc.compareTo("") == 0) {
      codeName = coderange;
      // search = "search_diagnostic_code";
    }
    else {
      codeName = codedesc;
      //   search = "search_diagnostic_text";
    }
  }
%>
</pre>
<h3><font face="Arial, Helvetica, sans-serif"> </font></h3>
<form name="codesearch" method=POST action="billingDigSearch.jsp">
<p><font face="Arial, Helvetica, sans-serif" size="2"> <b>Refine
Search</b> <br>
Code Range: <select name="coderange">
	<option value="0" selected>000-099</option>
	<option value="1">100-199</option>
	<option value="2">200-299</option>
	<option value="3">300-399</option>
	<option value="4">400-499</option>
	<option value="5">500-599</option>
	<option value="6">600-699</option>
	<option value="7">700-799</option>
	<option value="8">800-899</option>
	<option value="9">900-999</option>
</select> OR <br>
Description: <input type="text" name="codedesc" value="" size="30">
</font> <input type=submit name=search value="Search"></p>
</form>
<form name="diagcode" id="diagcode" method="post"
	action="billingDigUpdate.jsp">
<table width="600" border="1">
	<tr bgcolor="#CCCCFF">
		<td width="12%"><b> <font face="Arial, Helvetica, sans-serif"
			size="2">Code</font> </b></td>
		<td width="88%"><b> <font face="Arial, Helvetica, sans-serif"
			size="2">Description</font> </b></td>
	</tr>
	<%
 System.err.print("IN THE DIG SEARCH");
  ResultSet rslocal = null;
  ResultSet rslocal2 = null;
  String Dcode = "", DcodeDesc = "", Dcode2 = "", DcodeDesc2 = "";
  String codeName2 = "";
  String color = "";
  int Count = 0;
  int intCount = 0;
  String numCode = "";
  String textCode = "";
  String searchType = "";
  for (int i = 0; i < codeName.length(); i++) {
    String c = codeName.substring(i, i + 1);
    if (c.hashCode() >= 48 && c.hashCode() <= 58)
      numCode += c;
  }
  for (int j = 0; j < codeName.length(); j++) {
    String d = codeName.substring(j, j + 1);
    if (d.hashCode() < 48 || d.hashCode() > 58)
      textCode += d;
  }
  if (textCode.compareTo("") == -1 && textCode != null) {
    StringBuffer sBuffer = new StringBuffer(textCode);
    int k = textCode.indexOf(' ');
    sBuffer.deleteCharAt(k);
    sBuffer.insert(k, "");
    textCode = sBuffer.toString();
  }
  if (numCode.compareTo("") == 0) {
    if (textCode.compareTo("") == 0) {
      // search all case
      codeName = numCode;
      search = "search_diagnostic_code";
      searchType = "N";
    }
    else {
      //search text only
      codeName = "%" + textCode;
      search = "search_diagnostic_text";
      searchType = "N";
    }
  }
  else {
    if (textCode.compareTo("") == 0) {
      // search number only
      codeName = numCode;
      search = "search_diagnostic_code";
      searchType = "N";
    }
    else {
      //search both text and number only
      codeName = "%" + textCode;
      codeName2 = numCode;
      search = "search_diagnostic_text";
      search2 = "search_diagnostic_code";
      searchType = "BOTH";
    }
  }
  if (searchType.length() == 1) {
  // Retrieving Provider
    rslocal = null;
    rslocal = apptMainBean.queryResults(codeName + "%", search);
    while (rslocal.next()) {
      intCount = intCount + 1;
      Dcode = rslocal.getString("diagnostic_code");
      DcodeDesc = rslocal.getString("description");
      if (Count == 0) {
        Count = 1;
        color = "#FFFFFF";
      }
      else {
        Count = 0;
        color = "#EEEEFF";
      }
%>
	<tr bgcolor="<%=color%>">
		<td width="12%"><font face="Arial, Helvetica, sans-serif"
			size="2"> <a
			href="javascript:CodeAttach('<%=Dcode%>|<%=DcodeDesc%>')"><%=Dcode%>
		<a></font></td>
		<td width="88%"><font face="Arial, Helvetica, sans-serif"
			size="2"> <input type="text" name="<%=Dcode%>"
			value="<%=DcodeDesc%>" size="60"> <input type="submit"
			name="update" value="Update <%=Dcode%>"> </font></td>
	</tr>
	<%
  }
  } else {
    rslocal = null;
    rslocal = apptMainBean.queryResults(codeName + "%", search);
    while (rslocal.next()) {
      intCount = intCount + 1;
      Dcode = rslocal.getString("diagnostic_code");
      DcodeDesc = rslocal.getString("description");
      if (Count == 0) {
        Count = 1;
        color = "#FFFFFF";
      }
      else {
        Count = 0;
        color = "#F9E6F0";
      }
%>
	<tr bgcolor="<%=color%>">
		<td width="12%"><font face="Arial, Helvetica, sans-serif"
			size="2"> <a
			href="javascript:CodeAttach('<%=Dcode%>|<%=DcodeDesc%>')"><%=Dcode%>
		<a></font></td>
		<td width="88%"><font face="Arial, Helvetica, sans-serif"
			size="2"> <input type="text" name="<%=Dcode%>"
			value="<%=DcodeDesc%>" size="60"> <input type="submit"
			name="update" value="Update <%=Dcode%>"> </font></td>
	</tr>
	<%
  }
      rslocal2 = null;
  rslocal2 = apptMainBean.queryResults(codeName2 + "%", search2);
  while (rslocal2.next()) {
    intCount = intCount + 1;
    Dcode2 = rslocal2.getString("diagnostic_code");
    DcodeDesc2 = rslocal2.getString("description");
    if (Count == 0) {
      Count = 1;
      color = "#FFFFFF";
    }
    else {
      Count = 0;
      color = "#F9E6F0";
    }
%>
	<tr bgcolor="<%=color%>">
		<td width="12%"><font face="Arial, Helvetica, sans-serif"
			size="2"> <a
			href="javascript:CodeAttach('<%=Dcode2%>|<%=DcodeDesc2%>')"><%=Dcode2%>
		<a></font></td>
		<td width="88%"><font face="Arial, Helvetica, sans-serif"
			size="2"> <input type="text" name="<%=Dcode2%>"
			value="<%=DcodeDesc2%>" size="60"> <input type="submit"
			name="update" value="Update <%=Dcode2%>"> </font></td>
	</tr>
	<%
  }
      }
%>
	<%if (intCount == 0) {%>
	<tr bgcolor="<%=color%>">
		<td colspan="2"><font face="Arial, Helvetica, sans-serif"
			size="2"> No match found. <%// =i      %> </font></td>
	</tr>
	<%}%>
	<%if (intCount == 1) {%>
	<script LANGUAGE="JavaScript">
<!--
 CodeAttach('<%=Dcode%>|<%=DcodeDesc%>');
-->

</script>
	<%}%>
</table>
<form>
<p>&nbsp;</p>
<p>&nbsp;</p>
<h3>&nbsp;</h3>
</body>
</html>
