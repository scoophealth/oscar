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
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_form" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page import="java.util.*, java.sql.*, oscar.*, oscar.util.*" errorPage="../../errorpage.jsp"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.model.Study" %>
<%@page import="org.oscarehr.common.dao.StudyDao" %>
<%@page import="org.oscarehr.common.model.DemographicStudy" %>
<%@page import="org.oscarehr.common.dao.DemographicStudyDao" %>
<%
	StudyDao studyDao = SpringUtils.getBean(StudyDao.class);
    DemographicStudyDao demographicStudyDao = SpringUtils.getBean(DemographicStudy.class);
%>

<%
    //this is a quick independent page to let you select study.
    
    String demographic_no = request.getParameter("demographic_no")!=null ? request.getParameter("demographic_no") : "0";
    String curUser_no = (String) session.getAttribute("user");
    String deepColor = "#CCCCFF", weakColor = "#EEEEFF", rightColor = "gold" ;
%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>PATIENT STUDY</title>
<link rel="stylesheet" href="../web.css">
<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
  //document.titlesearch.keyword.select();
}
//-->
</SCRIPT>
</head>
<body bgproperties="fixed" onLoad="setfocus()" topmargin="0"
	leftmargin="0" rightmargin="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="<%=deepColor%>">
		<th>PATIENT STUDY RECORDS</th>
	</tr>
</table>

<table width="100%" border="0">
	<tr>
		<td align="left"><%=request.getParameter("name")!=null?request.getParameter("name"):""%></td>
	</tr>
</table>

<CENTER>
<table width="70%" border="0" bgcolor="#ffffff">
	<form method="post" name="study" action="demographicstudyselect.jsp">
	<input type="hidden" name="demographic_no" value="<%=demographic_no%>">
	<tr bgcolor="<%=deepColor%>">
		<TH width="15%">Study No.</TH>
		<TH width="35%">Study Name</TH>
		<TH width="50%">Description</TH>
	</tr>
	<%

    int nItems=0;
    int ectsize=0;
    String datetime =null;
    String bgcolor = null;
  
    for(DemographicStudy ds : demographicStudyDao.findByDemographicNo(Integer.parseInt(request.getParameter("demographic_no")))) {
    	Study s = studyDao.find(ds.getId().getStudyNo());
    	if(s.getCurrent1() == 1) {
    		
    	nItems++;
	    bgcolor = nItems%2==0?"#EEEEFF":"white";
%>
	<tr bgcolor="<%=bgcolor%>">
		<td align="center"><%=s.getId()%></td>
		<td><a
			href="<%=s.getStudyLink()%>?demographic_no=<%=request.getParameter("demographic_no")%>&study_no=<%=s.getId()%>"><%=s.getStudyName()%></a></td>
		<td><%=s.getDescription()%></td>
	</tr>
	<%
	} }
%>
	<tr>
		<td>&nbsp;</td>
		<td></td>
		<td></td>
	</tr>
	<tr align="center" bgcolor="<%=weakColor%>">
		<td colspan='3'><input type="button" name="button" value=" Exit "
			onClick="window.close()"></td>
	</tr>
</table>

</form>
</CENTER>
</body>
</html>
