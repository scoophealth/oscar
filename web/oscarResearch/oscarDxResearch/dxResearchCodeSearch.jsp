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
  if(session.getValue("user") == null)
    response.sendRedirect("../../logout.jsp");
  String user_no;
  user_no = (String) session.getAttribute("user");
%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.net.*" errorPage="../errorpage.jsp" %>
<%@ include file="../../admin/dbconnection.jsp" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />
<%@ include file="dbDxResearch.jsp" %>
<% String search = "",search2 = "";
 search = request.getParameter("search"); 
 if (search.compareTo("") == 0){
 search = "search_research_code"; 
 }
 
    String codeName= "",codeName1 = "", codeName2 = "",codeName3 = "", codeName4 = "";
   String xcodeName= "",xcodeName1 = "", xcodeName2 = "",xcodeName3 = "", xcodeName4 = "";
 
  codeName = request.getParameter("name");
   codeName1= request.getParameter("name1");
   codeName2 = request.getParameter("name2");
   codeName3= request.getParameter("name3");
      codeName4 = request.getParameter("name4");
      xcodeName = request.getParameter("name");
      xcodeName1= request.getParameter("name1");
      xcodeName2 = request.getParameter("name2");
      xcodeName3= request.getParameter("name3");
      xcodeName4 = request.getParameter("name4");
   String desc = "", desc1 = "", desc2 = "", desc3 = "", desc4 = "";
   
 if (codeName.compareTo("") == 0 || codeName == null){
 codeName = " ";
 desc = " ";
 }
 else{
codeName = codeName + "%";
desc = "%" + codeName + "%";
}
  if (codeName1.compareTo("") == 0 || codeName1 == null){
  codeName1 = " ";
  desc1 = " "; 
  }
  else{
 codeName1 = codeName1 + "%"; 
 desc1 = "%" + codeName1 + "%";
}
 if (codeName2.compareTo("") == 0 || codeName2 == null){
 codeName2 = " ";
 desc2 = " ";
 }
 else{
codeName2 = codeName2 + "%";
desc2 = "%" +codeName2 + "%";
}


 if (codeName3.compareTo("") == 0 || codeName2 == null){
 codeName3 = " ";
 desc3 = " ";
 }
 else{
codeName3 = codeName3 + "%";
desc3 = "%"+codeName3 + "%";
}


 if (codeName4.compareTo("") == 0 || codeName4 == null){
 codeName4 = " ";
 desc4 = " ";
 }
 else{
codeName4 = codeName4 + "%";
desc4 = "%"+codeName4 + "%";
}

 String[] param =new String[10];
 param[0] = codeName;
 param[1] = codeName1;
 param[2] = codeName2;
 param[3] = codeName3;
 param[4] = codeName4;
 param[5] = desc;
 param[6] = desc1;
 param[7] = desc2;
 param[8] = desc3;
 param[9] = desc4;
%>
<html>
<head>
<title>Research Code Search</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-2">
<script LANGUAGE="JavaScript">
<!--
function CodeAttach(File0) {
      
      self.close();
  self.opener.document.serviceform.xml_research1.value = File0;
      self.opener.document.serviceform.xml_research2.value = '';
      self.opener.document.serviceform.xml_research3.value = '';
     self.opener.document.serviceform.xml_research4.value = '';
      self.opener.document.serviceform.xml_research5.value = '';

}

-->
</script>

</head>

<body bgcolor="#FFFFFF" text="#000000" >



<h3><font face="Arial, Helvetica, sans-serif">Research (ICHPPC) Code Search </font></h3>
<form name="servicecode" id="servicecode" method="post" action="dxResearchCodeUpdate.jsp">
<table width="600" border="1">
 
  <tr bgcolor="#FFBC9B"> 
    <td width="12%"><b><font face="Arial, Helvetica, sans-serif" size="2">Code</font></b></td>
    <td width="88%"><b><font face="Arial, Helvetica, sans-serif" size="2">Description</font></b></td>
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
 rslocal = null;
  rslocal = apptMainBean.queryResults(param, search);
 while(rslocal.next()){
 intCount = intCount + 1;
 Dcode = rslocal.getString("ichppccode");
  DcodeDesc = rslocal.getString("description");
 if (Count == 0){
 Count = 1;
 color = "#FFFFFF";
 } else {
 Count = 0;
 color="#F9E6F0";
 }
 %>
  
  <tr bgcolor="<%=color%>"> 
    <td width="12%"><font face="Arial, Helvetica, sans-serif" size="2"><input type="checkbox" name="code_<%=Dcode%>" <%=Dcode.equals(xcodeName)?"checked":Dcode.equals(xcodeName1)?"checked":Dcode.equals(xcodeName2)?"checked":Dcode.equals(xcodeName3)?"checked":Dcode.equals(xcodeName4)?"checked":""%>><%=Dcode%></font></td>
    <td width="88%"><font face="Arial, Helvetica, sans-serif" size="2"><%=DcodeDesc%></font></td>
  </tr>
  <% 
  }
  %>
  
  <%  if (intCount == 0 ) { %>
  <tr bgcolor="<%=color%>"> 
    <td colspan="2"><font face="Arial, Helvetica, sans-serif" size="2">No match found. <%// =i%></font></td>
    
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
<input type="submit" name="submit" value="Confirm"><input type="button" name="cancel" value="Cancel" onclick="javascript:window.close()">
<form>
<p></p>
<p>&nbsp;</p>
<h3>&nbsp;</h3>
</body>
</html>
