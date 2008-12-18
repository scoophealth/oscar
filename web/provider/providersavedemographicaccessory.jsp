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
  if(session.getValue("user") == null)  response.sendRedirect("../logout.jsp");
%>
<%@ page import="java.sql.*, java.util.*, java.net.*, oscar.*"
	errorPage="errorpage.jsp"%>
<jsp:useBean id="accsBean" class="oscar.AppointmentMainBean"
	scope="page" />
<%@ include file="../admin/dbconnection.jsp"%>
<% 
  String [][] dbQueries=new String[][] { 
    {"search_demographicaccessorycount", "select count(demographic_no) from demographicaccessory where demographic_no=?"},
    {"add_demographicaccessory", "insert into demographicaccessory values(?,?)"},
    {"update_demographicaccessory", "update demographicaccessory set content=? where demographic_no=?"},
  };
  String[][] responseTargets=new String[][] {  };
  accsBean.doConfigure(dbParams,dbQueries,responseTargets);
%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
</head>
<body>
<center>
<table border="0" cellspacing="0" cellpadding="0" width="90%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF">
		ADD/UPDATE AN ENCOUNTERDEMOACCS RECORD</font></th>
	</tr>
</table>
<%
  String content="";//default is not null temp=null, 
  content=SxmlMisc.createXmlDataString(request, "xml_");

    String[] param =new String[2];
	  param[0]=request.getParameter("demographic_no");
	  param[1]=content; 
    String[] param1 =new String[2];
	  param1[0]=content;
	  param1[1]=param[0];

    int numRecord=1, rowsAffected=0;
	  ResultSet rs = accsBean.queryResults(param[0], "search_demographicaccessorycount");
 	  while (rs.next()) { 
      numRecord=rs.getInt("count(demographic_no)");
    }

    if(numRecord==0) {
      rowsAffected = accsBean.queryExecuteUpdate(param,"add_demographicaccessory");
    } else {
      rowsAffected = accsBean.queryExecuteUpdate(param1,"update_demographicaccessory");
	    //System.out.println("ss  "+param1[0]+rowsAffected);
    }
      
  if (rowsAffected ==1) {
%>
<p>
<h1>Successful Updaten of an demographic acce Record.</h1>
</p>
<script LANGUAGE="JavaScript">
     	//self.history.go(-1);return false;//this.location.reload();	//self.opener.refresh();
function dunescape(s) {
  while(s.indexOf('+')>0) {
    s = s.replace('+', ' ');
  }
  return (unescape(s));
}
      self.close();
     	self.opener.document.encounter.xml_Problem_List.value = dunescape("<%=URLEncoder.encode(request.getParameter("xml_Problem_List"))%>");
     	self.opener.document.encounter.xml_Medication.value = dunescape("<%=URLEncoder.encode(request.getParameter("xml_Medication"))%>");
     	self.opener.document.encounter.xml_Alert.value = dunescape("<%=URLEncoder.encode(request.getParameter("xml_Alert"))%>");
     	self.opener.document.encounter.xml_Family_Social_History.value = dunescape("<%=URLEncoder.encode(request.getParameter("xml_Family_Social_History"))%>");
</script> <%
  }  else {
%>
<p>
<h1>Sorry, Update has failed.</h1>
</p>
<%  
  }
  accsBean.closePstmtConn();
%>
<p></p>
<hr width="90%"></hr>
<form><input type="button" value="Close this window"
	onClick="self.close()"></form>
</center>
</body>
</html>