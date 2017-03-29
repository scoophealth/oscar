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
<%@page import="org.oscarehr.common.dao.EncounterDao" %>
<%@page import="org.oscarehr.common.model.Encounter" %>
<%@page import="oscar.util.ConversionUtils" %>

<%
	EncounterDao encounterDao = SpringUtils.getBean(EncounterDao.class);

%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<link rel="stylesheet" href="../web.css">
<script language="JavaScript">
<!--

    function start(){
      this.focus();
    }
    function closeit() {
    	//self.opener.refresh();
      self.close();
    }   
    //-->
</script>
</head>
<body onload="start()" topmargin="0" leftmargin="0" rightmargin="0">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF">
		ENCOUNTER HISTORY</font></th>
	</tr>
</table>
<table width="90%" border="0">
	<tr>
		<td width="95%">
<%
   List<Encounter> encs = encounterDao.findByDemographicNo(Integer.parseInt(request.getParameter("demographic_no")));

    for (Encounter enc : encs) {
%> &nbsp;<%=ConversionUtils.toDateString(enc.getEncounterDate())%> <%=ConversionUtils.toTimeString(enc.getEncounterTime())%><font
			color="yellow"> <%
     String historysubject = enc.getSubject()==null?"NULL":(enc.getSubject()).equals("")?"Unknown":enc.getSubject();
     StringTokenizer st=new StringTokenizer(historysubject,":");
     String strForm="", strTemplateURL="";
     while (st.hasMoreTokens()) {
       strForm = (new String(st.nextToken())).trim();
       break;
     }

     if(strForm.toLowerCase().compareTo("form")==0 && st.hasMoreTokens()) {
       strTemplateURL = "template" + (new String(st.nextToken())).trim().toLowerCase()+".jsp";
%> <a href=#
			onClick="popupPage(600,800,'providercontrol.jsp?encounter_no=<%=enc.getId()%>&demographic_no=<%=request.getParameter("demographic_no")%>&dboperation=search_encountersingle&displaymodevariable=<%=strTemplateURL%>&displaymode=vary&bNewForm=0')"><%=historysubject %>
		</a></font><br>
		<%
     } else if(strForm.compareTo("")!=0) {
%> <a href=#
			onClick="popupPage(400,600,'providercontrol.jsp?encounter_no=<%=enc.getId()%>&demographic_no=<%=request.getParameter("demographic_no")%>&template=<%=strForm%>&dboperation=search_encountersingle&displaymode=encountersingle')"><%=historysubject %>
		</a></font><br>
		<%
     }
   }     
%>
		</td>
	</tr>
</table>
<form><input type="button" value="Close this window"
	onClick="closeit()"></form>
</center>
</body>
</html>
