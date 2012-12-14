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

<%@page import="java.util.*, java.sql.*, oscar.*, java.net.*"%>

<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.DiagnosticCode" %>
<%@ page import="org.oscarehr.common.dao.DiagnosticCodeDao" %>
<%
	DiagnosticCodeDao diagnosticCodeDao = SpringUtils.getBean(DiagnosticCodeDao.class);
%>
<%

  String search = "", search2 = "";
  search = request.getParameter("search");
  if (search.compareTo("") == 0) {
    search = "search_diagnostic_new_code";
  }
  String codeName = "", codeName1 = "", codeName2 = "";
  String xcodeName = "", xcodeName1 = "", xcodeName2 = "";
  codeName = request.getParameter("name");
  codeName1 = request.getParameter("name1");
  codeName2 = request.getParameter("name2");
  xcodeName = request.getParameter("name");
  xcodeName1 = request.getParameter("name1");
  xcodeName2 = request.getParameter("name2");
  String formName = request.getParameter("formName");
  String formElement = request.getParameter("formElement");
  if (formName == null || formElement == null) {
    formName = "";
    formElement = "";
  }
  String desc = "", desc1 = "", desc2 = "";
  if (codeName.compareTo("") == 0 || codeName == null) {
    codeName = " ";
    desc = " ";
  }
  else {
    codeName = codeName + "%";
    desc = "%" + codeName + "%";
  }
  if (codeName1.compareTo("") == 0 || codeName1 == null) {
    codeName1 = " ";
    desc1 = " ";
  }
  else {
    codeName1 = codeName1 + "%";
    desc1 = "%" + codeName1 + "%";
  }
  if (codeName2.compareTo("") == 0 || codeName2 == null) {
    codeName2 = " ";
    desc2 = " ";
  }
  else {
    codeName2 = codeName2 + "%";
    desc2 = "%" + codeName2 + "%";
  }
  String[] param = new String[6];
  param[0] = codeName;
  param[1] = codeName1;
  param[2] = codeName2;
  param[3] = desc;
  param[4] = desc1;
  param[5] = desc2;
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Diagnostic Code Search</title>
<script LANGUAGE="JavaScript">


<!--
function CodeAttach(File0) {
      self.close();
      self.opener.document.forms[0].xml_diagnostic_detail1.value = File0;
      self.opener.document.forms[0].xml_diagnostic_detail2.value ='';
      self.opener.document.forms[0].xml_diagnostic_detail3.value ='';
}
-->
</script>
</head>
<body bgcolor="#FFFFFF" text="#000000" topmargin="0" leftmargin="0"
	rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP bgcolor="#CCCCFF"><font face="Helvetica"
			color="#000000">Diagnostic Code Search ICD9</font> <font
			face="Arial, Helvetica, sans-serif" color="#FF0000">(Maximum 3
		selections)</font></th>
	</tr>
</table>
<form name="servicecode" id="servicecode" method="post"
	action="billingDigNewUpdate.jsp"><input type="hidden"
	name="formName" value="<%=formName%>" /> <input type="hidden"
	name="formElement" value="<%=formElement%>" />
<div style="height: 600; overflow: auto">
<table width="800" border="1">
	<tr bgcolor="#CCCCFF">
		<td><b> <font face="Arial, Helvetica, sans-serif" size="2">Code</font>
		</b></td>
		<td><b> <font face="Arial, Helvetica, sans-serif" size="2">Description</font>
		</b></td>
	</tr>
	<%
  ResultSet rslocal = null;
  ResultSet rslocal2 = null;
  String color = "";
  int Count = 0;
  int intCount = 0;
  String numCode = "";
  String textCode = "";
  String searchType = "";
  // Retrieving Provider
  String Dcode = "", DcodeDesc = "";

  List<DiagnosticCode> results = diagnosticCodeDao.newSearch(param[0],param[1],param[2],param[3],param[4],param[5]);
  for(DiagnosticCode result:results) {
	  intCount++;
	  Dcode = result.getDiagnosticCode();
	  DcodeDesc = result.getDescription();
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
		<td><font face="Arial, Helvetica, sans-serif" size="2"> <%if (Dcode.compareTo(xcodeName) == 0 || Dcode.compareTo(xcodeName1) == 0 || Dcode.compareTo(xcodeName2) == 0) {      %>
		<input type="checkbox" name="code_<%=Dcode%>" checked> <%} else {      %>
		<input type="checkbox" name="code_<%=Dcode%>"> <%}      %> <%=Dcode%>
		</font></td>
		<td><font face="Arial, Helvetica, sans-serif" size="2"> <input
			type="hidden" name="codedesc_<%=Dcode%>" value="<%=DcodeDesc%>">
		<input type="text" name="<%=Dcode%>" value="<%=DcodeDesc%>" size="80">
		<input type="submit" name="update" value="update <%=Dcode%>">
		</font></td>
	</tr>
	<%}%>
	<%if (intCount == 0) {%>
	<tr bgcolor="<%=color%>">
		<td colspan="2"><font face="Arial, Helvetica, sans-serif"
			size="2"> No match found. <%// =i      %> </font></td>
	</tr>
	<%}%>
	<%if (intCount == 1) {%>
	<script LANGUAGE="JavaScript">
<!--

 CodeAttach('<%=Dcode%>');
-->

</script>
	<%}%>
</table>
</div>
<input type="submit" name="update" value="Confirm"> <input
	type="button" name="cancel" value="Cancel"
	onclick="javascript:window.close()"></form>
</body>
</html>
