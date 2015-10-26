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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_demographic" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_demographic");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page import="java.sql.*, java.util.*, java.net.*, oscar.*"
	errorPage="errorpage.jsp"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.dao.DemographicAccessoryDao" %>
<%@page import="org.oscarehr.common.model.DemographicAccessory" %>
<%
	DemographicAccessoryDao demographicAccessoryDao = (DemographicAccessoryDao)SpringUtils.getBean("demographicAccessoryDao");
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
	String content=SxmlMisc.createXmlDataString(request, "xml_");

	String[] param = new String[2];
	param[0]=request.getParameter("demographic_no");
	param[1]=content;

	String[] param1 = new String[2];
	param1[0]=content;
	param1[1]=param[0];

	long numRecord=1, rowsAffected=0;
	numRecord = demographicAccessoryDao.findCount(Integer.parseInt(request.getParameter("demographic_no")));

    if (numRecord==0) {
      DemographicAccessory da = new DemographicAccessory();
      da.setContent(content);
      demographicAccessoryDao.persist(da);
    } else {
      DemographicAccessory da = demographicAccessoryDao.find(Integer.parseInt(param[0]));
      da.setContent(content);
      demographicAccessoryDao.merge(da);
    }

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
</script>

<p></p>
<hr width="90%"/>
<form><input type="button" value="Close this window"
	onClick="self.close()"></form>
</center>
</body>
</html>
