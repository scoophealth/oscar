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
<%@ page import="java.util.*, java.sql.*, oscar.*, oscar.util.*"
	errorPage="../../errorpage.jsp"%>
<%
    //this is a quick independent page to let you select study.
    
    String demographic_no = request.getParameter("demographic_no")!=null ? request.getParameter("demographic_no") : "0";
    String curUser_no = (String) session.getAttribute("user");
    String deepColor = "#CCCCFF", weakColor = "#EEEEFF", rightColor = "gold" ;
%>

<jsp:useBean id="studyBean" class="oscar.AppointmentMainBean"
	scope="page" />

<% 
    String [][] dbQueries=new String[][] { 
        {"search_study", "select s.* from study s order by ? " }, 
        {"search_demostudy", "select d.demographic_no, s.* from demographicstudy d left join study s on d.study_no=s.study_no where d.demographic_no=? and s.current1=1 order by d.study_no" }, 
	};
    studyBean.doConfigure(dbQueries);
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
    ResultSet rs = null ;
    int nItems=0;
    int ectsize=0;
    String datetime =null;
    String bgcolor = null;
  
    rs = studyBean.queryResults(new String[]{request.getParameter("demographic_no")}, "search_demostudy");
    while (rs.next()) { 
    	nItems++;
	    bgcolor = nItems%2==0?"#EEEEFF":"white";
%>
	<tr bgcolor="<%=bgcolor%>">
		<td align="center"><%=studyBean.getString(rs,"s.study_no")%></td>
		<td><a
			href="<%=studyBean.getString(rs,"s.study_link")%>?demographic_no=<%=request.getParameter("demographic_no")%>&study_no=<%=studyBean.getString(rs,"s.study_no")%>"><%=studyBean.getString(rs,"s.study_name")%></a></td>
		<td><%=studyBean.getString(rs,"s.description")%></td>
	</tr>
	<%
	}
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
