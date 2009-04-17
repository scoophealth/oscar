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
<%@ include file="/common/webAppContextAndSuperMgr.jsp"%>

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
	String content=SxmlMisc.createXmlDataString(request, "xml_");

	String[] param = new String[2];
	param[0]=request.getParameter("demographic_no");
	param[1]=content; 

	String[] param1 = new String[2];
	param1[0]=content;
	param1[1]=param[0];

	long numRecord=1, rowsAffected=0;
	List<Map> resultList = oscarSuperManager.find("providerDao", "search_demographicaccessorycount", new Object[] {param[0]});
	for (Map acc : resultList) {
		numRecord = (Long) acc.get("count(demographic_no)");
	}

    if (numRecord==0) {
      rowsAffected = oscarSuperManager.update("providerDao", "add_demographicaccessory", param);
    } else {
      rowsAffected = oscarSuperManager.update("providerDao", "update_demographicaccessory", param1);
    }
      
	if (rowsAffected == 1) {
%>
<p>
<h1>Successful Update of the demographic accessory record.</h1>
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
	} else {
%>
<p>
<h1>Sorry, Update has failed.</h1>
</p>
<%  
	}
%>
<p></p>
<hr width="90%"/>
<form><input type="button" value="Close this window"
	onClick="self.close()"></form>
</center>
</body>
</html>