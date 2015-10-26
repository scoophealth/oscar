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

<%@ page import="java.sql.*, java.util.*, oscar.MyDateFormat" errorPage="errorpage.jsp"%>

<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.dao.EncounterTemplateDao" %>
<%@page import="org.oscarehr.common.model.EncounterTemplate" %>
<%@page import="org.oscarehr.common.dao.EncounterDao" %>
<%@page import="org.oscarehr.common.model.Encounter" %>
<%@page import="oscar.util.ConversionUtils" %>
<%
	EncounterTemplateDao encounterTemplateDao = SpringUtils.getBean(EncounterTemplateDao.class);
    EncounterDao encounterDao = SpringUtils.getBean(EncounterDao.class);
%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Single Encounter</title>
<script LANGUAGE="JavaScript">
<!--
function start(){
  this.focus();
}

//-->
</script>
</head>
<body onload="start()" topmargin="0" leftmargin="0" rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#CCCCFF">
		<th align="CENTER">AN ENCOUNTER RECORD</th>
	</tr>
</table>
<%

   String content="";
   String encounterattachment="";
   String temp="";
   Encounter enc = encounterDao.find(Integer.parseInt(request.getParameter("encounter_no")));
   if(enc != null) {
     content = enc.getContent();
     encounterattachment = enc.getEncounterAttachment();
%>
<font size="-1"><%=ConversionUtils.toDateString(enc.getEncounterDate())%> <%=ConversionUtils.toTimeString(enc.getEncounterTime())%>
&nbsp;<font color="green"><%=enc.getSubject().equals("")?"Unknown":enc.getSubject()%></font></font>
<br>
<xml id="xml_list">
<encounter>
<%=content%>
</encounter>
</xml>
<%
   }     
%>
<table datasrc='#xml_list' width='100%' border='0' BGCOLOR="#EEEEFF">
	<tr>
		<td>Attachment: <%
   StringTokenizer st = new StringTokenizer(encounterattachment);
   while (st.hasMoreTokens()) {
   temp = st.nextToken(">").substring(1);
%> <a href=#
			onClick="popupPage(600,800, '<%=st.nextToken("<").substring(1)%>')">
		<%=temp%></a> <%
     st.nextToken(">");
   }
%>
		</td>
		<td align='right' width='20%' nowrap>
		<div datafld='xml_username'></div>
		</td>
	</tr>
</table>
<%
  if(request.getParameter("template")!=null && !(request.getParameter("template").equals(".")) ) {
	  
     for(EncounterTemplate template : encounterTemplateDao.findByName(request.getParameter("template"))) {
    	 out.println(template.getEncounterTemplateValue());
     }
     
	
  } else {
     out.println("<table datasrc='#xml_list' border='0'><tr><td><font color='blue'>Content:</font></td></tr><tr><td><div datafld='xml_content'></td></tr></table>");
  }
%>

<center><input type="button" value="Print Preview"
	onClick="popupPage(600,800, 'providerencounterprint.jsp?encounter_no=<%=request.getParameter("encounter_no")%>&demographic_no=<%=request.getParameter("demographic_no")%>&username=<%=request.getParameter("username")%>')">
<input type="button" value="Close this window" onClick="self.close()">
</center>
</body>
</html>
