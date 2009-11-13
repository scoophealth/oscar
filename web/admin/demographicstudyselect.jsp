<%@ page import="java.util.*, java.sql.*, oscar.*, oscar.util.*"
	errorPage="errorpage.jsp"%>
<%
    //this is a quick independent page to let you add studying patient.
    
    String demographic_no = request.getParameter("demographic_no")!=null ? request.getParameter("demographic_no") : "0";
    String curUser_no = (String) session.getAttribute("user");
    String strLimit1 = request.getParameter("limit1")!=null ? request.getParameter("limit1") : "0";
    String strLimit2 = request.getParameter("limit2")!=null ? request.getParameter("limit2") : "18";
    String deepColor = "#CCCCFF", weakColor = "#EEEEFF", rightColor = "gold" ;
%>

<jsp:useBean id="studyBean" class="oscar.AppointmentMainBean"
	scope="page" />
<%@ include file="../admin/dbconnection.jsp"%>
<% 
    String [][] dbQueries=new String[][] { 
        {"delete_demostudy", "delete from demographicstudy where demographic_no = ?" }, 
        {"save_demostudy", "insert into demographicstudy values (?,?,?,?)" }, 
        {"search_study", "select s.* from study s order by s.study_no" }, 
        {"search_demostudy", "select demographic_no from demographicstudy where demographic_no=? and study_no=? " }, 
	};
    studyBean.doConfigure(dbQueries);
%>

<%
    if (request.getParameter("submit")!=null && request.getParameter("submit").equals("Update")) {
		//if it is a array, need to have loop to insert the records for study_no
		String[] study_no = request.getParameterValues("study_no");
        String datetime = UtilDateUtilities.DateToString(UtilDateUtilities.now(), "yyyy-MM-dd HH:mm:ss");

		int rowsAffected = studyBean.queryExecuteUpdate(new String[]{demographic_no}, "delete_demostudy");
		if (study_no != null) {
			for (int i = 0; i < study_no.length; i++) {
				//System.out.println(i + " " + study_no[i]);
    			rowsAffected = studyBean.queryExecuteUpdate(new String[]{demographic_no, study_no[i], curUser_no, datetime}, "save_demostudy");
			}
		} 
        
		out.println("<script language='JavaScript'>window.close();opener.refreshstudy();</script>);");
    }
%>
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
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>PATIENT STUDY SEARCH RESULTS</title>
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
<table width="100%" border="0" bgcolor="#ffffff">
	<form method="post" name="study" action="demographicstudyselect.jsp">
	<input type="hidden" name="demographic_no" value="<%=demographic_no%>">
	<tr bgcolor="<%=deepColor%>">
		<TH width="5%"></TH>
		<TH width="10%">Study Name</TH>
		<TH width="15%">Description</TH>
	</tr>
	<%
    ResultSet rsdemo = null ;
    ResultSet rs = null ;
    int nItems=0;
    int ectsize=0;
    String datetime =null;
    String bgcolor = null;
  
    rsdemo = studyBean.queryResults("search_study");
    while (rsdemo.next()) { 
    	nItems++;
	    bgcolor = nItems%2==0?"#EEEEFF":"white";
        rs = studyBean.queryResults(new String[]{demographic_no,rsdemo.getString("s.study_no")}, "search_demostudy");
%>
	<tr bgcolor="<%=bgcolor%>">
		<td align='center'><input type="checkbox" name="study_no"
			value="<%=rsdemo.getString("s.study_no")%>"
			<%=demographic_no.equals(rs.next()?studyBean.getString(rs,"demographic_no"):"0") ? "checked" : "" %>></td>
		<td><%=studyBean.getString(rsdemo,"study_name")%></td>
		<td align="center"><%=studyBean.getString(rsdemo,"description")%></td>
	</tr>
	<%
  }
  studyBean.closePstmtConn();
%>
	<tr>
		<td>&nbsp;</td>
		<td></td>
		<td></td>
	</tr>
	<tr align="center" bgcolor="<%=weakColor%>">
		<td colspan='3'><input type="submit" name="submit" value="Update">
		<input type="button" name="button" value="Exit"
			onClick="window.close()"></td>
	</tr>
</table>

</form>
</CENTER>
</body>
</html>
