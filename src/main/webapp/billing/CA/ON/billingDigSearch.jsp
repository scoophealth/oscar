<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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

--%>
<%
  String user_no = (String) session.getAttribute("user");
%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.net.*" errorPage="errorpage.jsp"%>

<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.DiagnosticCode" %>
<%@ page import="org.oscarehr.common.dao.DiagnosticCodeDao" %>
<%
	DiagnosticCodeDao diagnosticCodeDao = SpringUtils.getBean(DiagnosticCodeDao.class);
%>
<% String search = "",search2 = "";
 search = request.getParameter("search");
 if (search.compareTo("") == 0){
 search = "search_diagnostic_code";
 }


   String codeName = request.getParameter("name");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="billing.billingDigSearch.title" /></title>
<script LANGUAGE="JavaScript">
<!--
function CodeAttach(File2) {
      if (self.opener.callChangeCodeDesc) self.opener.callChangeCodeDesc();

      <%if(request.getParameter("name2")!=null) {%>
      self.opener.<%=request.getParameter("name2")%> = File2.substring(0,3);
      <%} else {%>
      self.opener.document.forms[1].xml_diagnostic_detail.value = File2;
      <%}%>
      setTimeout("self.close();",100);
}
function setfocus() {
  this.focus();
  document.forms[0].codedesc.focus();
  document.forms[0].codedesc.select();
}
-->

</script>

</head>

<body bgcolor="#FFFFFF" text="#000000" onLoad="setfocus()" topmargin="0"
	leftmargin="0" rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align=center NOWRAP bgcolor="#CCCCFF"><font face="Helvetica"
			color="#000000"><bean:message
			key="billing.billingDigSearch.msgDiagnostic" /> </font><font
			face="Arial, Helvetica, sans-serif" color="#FF0000"><bean:message
			key="billing.billingDigSearch.msgMaxSelections" /></font></th>
	</tr>
</table>
<% String coderange=request.getParameter("coderange");
	String codedesc =request.getParameter("codedesc");
        if (codedesc != null){
        if (codedesc.compareTo("") == 0) {

   		codeName = coderange;
        } else {
           codeName =  codedesc;
   		}
   }
   %>

<form name="codesearch" id="codesearch" method="post"
	action="billingDigSearch.jsp">
<%if(request.getParameter("name2")!=null) {%>
<input type="hidden" name="name2"
	value="<%=request.getParameter("name2")%>" />
<%}%>
<p><font face="Arial, Helvetica, sans-serif" size="2"><b><bean:message
	key="billing.billingDigSearch.msgRefine" /></b><br>
<bean:message key="billing.billingDigSearch.msgCodeRange" />: <select
	name="coderange">
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
</select> <bean:message key="billing.billingDigSearch.msgOR" /> <br />
<bean:message key="billing.billingDigSearch.msgDescription" />: <input
	type="text" name="codedesc" value="" size="30" /></font> <input type="submit"
	name="search1"
	value="<bean:message key="billing.billingDigSearch.btnSearch"/>" /></p>
<input type="hidden" name="search"
	value="<bean:message key="billing.billingDigSearch.btnSearch"/>" />
</form>

<form name="diagcode" id="diagcode" method="post"
	action="billingDigUpdate.jsp">
<table width="600" border="1">

	<tr bgcolor="#CCCCFF">
		<td width="12%"><b><font face="Arial, Helvetica, sans-serif"
			size="2"><bean:message key="billing.billingDigSearch.formCode" /></font></b></td>
		<td width="88%"><b><font face="Arial, Helvetica, sans-serif"
			size="2"><bean:message
			key="billing.billingDigSearch.formDescription" /></font></b></td>
	</tr>


	<%  ResultSet rslocal = null;
      ResultSet rslocal2 = null;
        String Dcode="", DcodeDesc="", Dcode2="", DcodeDesc2="";
        String codeName2="";
    String color="";
 int Count = 0;
 int intCount = 0;
 String numCode="";
   String textCode="";
   String searchType="";
for(int i=0;i<codeName.length();i++)
 {
 String c = codeName.substring(i,i+1);
 if(c.hashCode()>=48 && c.hashCode()<=58)
 numCode += c;
 }
for(int j=0;j<codeName.length();j++)
 {
 String d = codeName.substring(j,j+1);
 if(d.hashCode()<48 || d.hashCode()>58)
 textCode += d;
 }
if (textCode.compareTo("") == -1 && textCode != null){
StringBuffer sBuffer = new StringBuffer(textCode);
int k = textCode.indexOf(' ');
sBuffer.deleteCharAt(k);
sBuffer.insert(k,"");
textCode = sBuffer.toString();
 }
 if (numCode.compareTo("")==0){
    if(textCode.compareTo("")==0){
    // search all case
        codeName = numCode;
        search = "search_diagnostic_code";
        searchType= "N";
        }
        else{
    //search text only
         codeName = "%" + textCode;
         search = "search_diagnostic_text";
         searchType="N";
         }
   }else{

    if(textCode.compareTo("")==0){
    // search number only
        codeName = numCode;
        search = "search_diagnostic_code";
        searchType= "N";
        }
        else{
    //search both text and number only
         codeName = "%" + textCode;
         codeName2 = numCode;
         search = "search_diagnostic_text";
         search2 = "search_diagnostic_code";
         searchType="BOTH";
         }
   }

 List<DiagnosticCode> results = null;

         if (searchType.length() == 1) {

// Retrieving Provider

	if("search_diagnostic_code".equals(search)) {
		results=diagnosticCodeDao.searchCode(codeName+"%");
	} else if("search_diagnostic_text".equals(search)) {
		results=diagnosticCodeDao.searchText(codeName+"%");
	}
	for(DiagnosticCode result:results) {
		intCount++;
		Dcode = result.getDiagnosticCode();
		DcodeDesc = result.getDescription().trim();
		if (Count == 0){
			Count = 1;
			color = "#FFFFFF";
		} else {
			Count = 0;
			color="#EEEEFF";
		}
 %>

	<tr bgcolor="<%=color%>">
		<td width="12%"><font face="Arial, Helvetica, sans-serif"
			size="2"><a
			href="javascript:CodeAttach('<%=Dcode%>|<%=DcodeDesc%>')"><%=Dcode%><a></font></td>
		<td width="88%"><font face="Arial, Helvetica, sans-serif"
			size="2"><input type="text" name="<%=Dcode%>"
			value="<%=DcodeDesc%>" size="60"><input type="submit"
			name="update"
			value="<bean:message key="billing.billingDigSearch.btnUpdate"/> <%=Dcode%>"></font></td>
	</tr>
	<%
  } //end of while looop
  } else { //both

	  results=diagnosticCodeDao.searchText(codeName+"%");
  	  for(DiagnosticCode result:results) {
  		  intCount++;
  		  Dcode = result.getDiagnosticCode();
  		  DcodeDesc = result.getDescription().trim();
  		  if (Count == 0){
  			 Count = 1;
  			 color = "#FFFFFF";
  		  } else {
  			 Count = 0;
  			 color="#F9E6F0";
  		  }

 %>

	<tr bgcolor="<%=color%>">
		<td width="12%"><font face="Arial, Helvetica, sans-serif"
			size="2"><a
			href="javascript:CodeAttach('<%=Dcode%>|<%=DcodeDesc%>')"><%=Dcode%><a></font></td>
		<td width="88%"><font face="Arial, Helvetica, sans-serif"
			size="2"><input type="text" name="<%=Dcode%>"
			value="<%=DcodeDesc%>" size="60"><input type="submit"
			name="update"
			value="<bean:message key="billing.billingDigSearch.btnUpdate"/> <%=Dcode%>"></font></td>
	</tr>
	<%
  }


	  results=diagnosticCodeDao.searchCode(codeName2+"%");
  	  for(DiagnosticCode result:results) {
  		  intCount++;
  		  Dcode2 = result.getDiagnosticCode();
  		  DcodeDesc2 = result.getDescription().trim();
  		  if (Count == 0){
  			 Count = 1;
  			 color = "#FFFFFF";
  		  } else {
  			 Count = 0;
  			 color="#F9E6F0";
  		  }
 %>

	<tr bgcolor="<%=color%>">
		<td width="12%"><font face="Arial, Helvetica, sans-serif"
			size="2"><a
			href="javascript:CodeAttach('<%=Dcode2%>|<%=DcodeDesc2%>')"><%=Dcode2%><a></font></td>
		<td width="88%"><font face="Arial, Helvetica, sans-serif"
			size="2"><input type="text" name="<%=Dcode2%>"
			value="<%=DcodeDesc2%>" size="60"><input type="submit"
			name="update"
			value="<bean:message key="billing.billingDigSearch.btnUpdate"/> <%=Dcode2%>"></font></td>
	</tr>
	<%
  }
  }
  %>

	<%  if (intCount == 0 ) { %>
	<tr bgcolor="<%=color%>">
		<td colspan="2"><font face="Arial, Helvetica, sans-serif"
			size="2"><bean:message
			key="billing.billingDigSearch.msgNoMatch" />. <%// =i%></font></td>

	</tr>
	<%  }%>

	<% if (intCount == 1) { %>
	<script LANGUAGE="JavaScript">
<!--
 CodeAttach('<%=Dcode%>|<%=DcodeDesc%>');
-->

</script>
	<% } %>
</table>
</form>
<p>&nbsp;</p>
<p>&nbsp;</p>
<h3>&nbsp;</h3>
</body>
</html:html>
