<%@ page
	import="java.util.*, java.sql.*, java.net.*, oscar.*, oscar.util.*"
	errorPage="errorpage.jsp"%>
<%
    //this is a quick independent page to let you add studying patient.
    
    String deepColor = "#CCCCFF", weakColor = "#EEEEFF", rightColor = "gold" ;
%>

<jsp:useBean id="studyBean" class="oscar.AppointmentMainBean"
	scope="page" />
<%@ include file="../admin/dbconnection.jsp"%>
<% 
    String [][] dbQueries=new String[][] { 
        {"search_echart", "select * from eChart order by timeStamp desc" }, 
    };
    studyBean.doConfigure(dbQueries);
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
		<th>SEARCH FOR PATIENT STUDY RECORDS</th>
	</tr>
</table>

<CENTER>
<table width="100%" border="0" bgcolor="#ffffff">
	<tr bgcolor="<%=deepColor%>">
		<TH width="10%">eChartId</TH>
		<TH width="10%">timeStamp</TH>
		<TH width="10%">demographicNo</TH>
		<TH width="3%">providerNo</TH>
		<TH width="10%">encounter</TH>
	</tr>
	<%
    ResultSet rsdemo = null ;
    int nItems=0;
    int ectsize=0;
    String datetime =null;
    String bgcolor = null;
  
    rsdemo = studyBean.queryResults("search_echart");
    while (rsdemo.next()) { 
    	nItems++;
	    bgcolor = nItems%2==0?"#EEEEFF":"white";
		if (rsdemo.getString("encounter")!=null && rsdemo.getString("encounter").length()>32*1024) {
%>
	<tr bgcolor="<%=bgcolor%>">
		<td align="center"><%=rsdemo.getString("eChartId")%></td>
		<td><%=rsdemo.getString("timeStamp")%></td>
		<td><a href=#
			onClick="popupPage(600,700, '../demographic/demographiccontrol.jsp?demographic_no=<%=rsdemo.getString("demographicNo")%>&displaymode=edit&dboperation=search_detail')"><%=rsdemo.getString("demographicNo")%></a></td>
		<td align="center"><%=rsdemo.getString("providerNo")%></td>
		<td align="center"><%=rsdemo.getString("encounter").length()%></td>
	</tr>
	<%
		}
  }
  studyBean.closePstmtConn();
%>

</table>
<br>

</CENTER>
</body>
</html>