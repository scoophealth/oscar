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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.billing,_admin" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin&type=_admin.billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<% 
  String user_no = (String) session.getAttribute("user");
%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.net.*" errorPage="../errorpage.jsp"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.Ichppccode" %>
<%@ page import="org.oscarehr.common.dao.IchppccodeDao" %>

<%
	IchppccodeDao ichppccodeDao = SpringUtils.getBean(IchppccodeDao.class);
%>
<% String search = "",search2 = "";
 search = request.getParameter("search"); 
 if (search.compareTo("") == 0){
 search = "search_research_code";
 }
 
    String codeName= "",codeName1 = "", codeName2 = "";
   codeName = request.getParameter("name");
   codeName1= request.getParameter("name1");
   codeName2 = request.getParameter("name2");
   String desc = "", desc1 = "", desc2 = "";
   
 if (codeName.compareTo("") == 0 || codeName == null){
 codeName = " ";
 desc = " ";
 }
 else{
codeName = codeName + "%";
desc = codeName + "%";
}
  if (codeName1.compareTo("") == 0 || codeName1 == null){
  codeName1 = " ";
  desc1 = " "; 
  }
  else{
 codeName1 = codeName1 + "%"; 
 desc1 = codeName1 + "%";
}
 if (codeName2.compareTo("") == 0 || codeName2 == null){
 codeName2 = " ";
 desc2 = " ";
 }
 else{
codeName2 = codeName2 + "%";
desc2 = codeName2 + "%";
}


%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Research Code Search</title>
<script LANGUAGE="JavaScript">
<!--
function CodeAttach(File0) {
      
      self.close();
  self.opener.document.serviceform.xml_research1.value = File0;
      self.opener.document.serviceform.xml_research2.value = '';
      self.opener.document.serviceform.xml_research3.value = '';
}
-->
</script>

</head>

<body bgcolor="#FFFFFF" text="#000000">



<h3><font face="Arial, Helvetica, sans-serif">Research
(ICHPPC) Code Search <font face="Arial, Helvetica, sans-serif"
	color="#FF0000">(Maximum 3 selections)</font></font></h3>
<form name="servicecode" id="servicecode" method="post"
	action="billingResearchCodeUpdate.jsp">
<table width="600" border="1">

	<tr bgcolor="#FFBC9B">
		<td width="12%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Code</font></b></td>
		<td width="88%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Description</font></b></td>
	</tr>


	<%  ResultSet rslocal = null;  
      ResultSet rslocal2 = null;
     
     
    String color="";
 int Count = 0;
 int intCount = 0;
 String numCode="";
   String textCode="";
   String searchType="";
// Retrieving Provider

String Dcode="", DcodeDesc="";

for(Ichppccode i :ichppccodeDao.search_research_code(codeName, codeName1, codeName2, desc, desc1, desc2)) {
 intCount = intCount + 1;
 Dcode = i.getId();
  DcodeDesc = i.getDescription();
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
			size="2"><input type="checkbox" name="code_<%=Dcode%>"><%=Dcode%></font></td>
		<td width="88%"><font face="Arial, Helvetica, sans-serif"
			size="2"><%=DcodeDesc%></font></td>
	</tr>
	<% 
  }
  %>

	<%  if (intCount == 0 ) { %>
	<tr bgcolor="<%=color%>">
		<td colspan="2"><font face="Arial, Helvetica, sans-serif"
			size="2">No match found. <%// =i%>
		</font></td>

	</tr>
	<%  }%>

	<% if (intCount == 1) { %>
	<script LANGUAGE="JavaScript">
<!--
 CodeAttach('<%=Dcode%>'); 
-->

</script>
	<% } %>
</table>
<input type="submit" name="submit" value="Confirm"><input
	type="button" name="cancel" value="Cancel"
	onclick="javascript:window.close()">
<form>
<p></p>
<p>&nbsp;</p>
<h3>&nbsp;</h3>
</body>
</html>
