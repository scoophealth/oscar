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
<%@ page
	import="java.util.*, java.sql.*, java.net.*, oscar.*, oscar.util.*"
	errorPage="errorpage.jsp"%>
<jsp:useBean id="studyBean" class="oscar.AppointmentMainBean"
	scope="page" />

<%
    //this is a quick independent page to let you add studying patient.
    if(session.getAttribute("user") == null) response.sendRedirect("../logout.jsp");
	if (request.getParameter("form") != null) {  response.sendRedirect("demographicstudysearchresults.jsp?search_mode="+request.getParameter("search_mode")+"&keyword="+request.getParameter("keyword") +"&limit1=0&limit2=10");
	}

    String strLimit1 = request.getParameter("limit1")!=null ? request.getParameter("limit1") : "0";
    String strLimit2 = request.getParameter("limit2")!=null ? request.getParameter("limit2") : "18";
    String deepColor = "#CCCCFF", weakColor = "#EEEEFF", rightColor = "gold" ;

	//nameValue is a part of sql string
	String nameValue = null;
    if (request.getParameter("search_mode")!=null && request.getParameter("search_mode").equals("name")) {
		String last_name="", first_name="";
		String name = request.getParameter("keyword").trim();
		if (name.indexOf(",") > 0) {last_name = name.substring(0, name.indexOf(",")).trim(); first_name = name.substring(name.indexOf(",")+1).trim();}
		else {last_name = name; }
	    nameValue = "last_name like '" + UtilMisc.charEscape(last_name, '\'') + "%' and first_name like '" + UtilMisc.charEscape(first_name, '\'') + "%'";
	} else if(request.getParameter("search_mode")!=null && request.getParameter("search_mode").equals("search_dob")) {
		String name = request.getParameter("keyword").trim();
	  	nameValue = "year_of_birth = " + name.substring(0,4);
	  	nameValue += " and month_of_birth = " + name.substring(5,7);
	  	nameValue += " and date_of_birth = " + name.substring(8,10);
	} else {
	    nameValue = request.getParameter("search_mode") + "='" + UtilMisc.charEscape(request.getParameter("keyword"), '\'') +"'";
	}

    String [][] dbQueries=new String[][] { 
        {"search_demo", "select d.*, s.study_no from demographic d LEFT JOIN demographicstudy s ON d.demographic_no = s.demographic_no where " + nameValue + " group by d.demographic_no order by last_name limit ? offset ?" }, 
    };
    studyBean.doConfigure(dbQueries);
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>PATIENT STUDY SEARCH RESULTS</title>
<link rel="stylesheet" href="../web.css">
<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
  document.titlesearch.keyword.select();
}
function checkTypeIn() {
  var dob = document.titlesearch.keyword;
  if(document.titlesearch.search_mode[2].checked) {
    if(dob.value.length==8) {
      dob.value = dob.value.substring(0, 4)+"-"+dob.value.substring(4, 6)+"-"+dob.value.substring(6, 8);
    }
    if(dob.value.length!=10) {
      alert("You have a wrong DOB input!!!");
    }
  }
} 

function popupPage1(varpage) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height=660,width=1000,location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=50,screenY=50,top=0,left=0";
  var popup=window.open(page, "studydemo", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self; 
    }
  }
}
function refreshstudy() {
  history.go(0);
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

<table BORDER="0" CELLPADDING="1" CELLSPACING="0" WIDTH="100%"
	BGCOLOR="<%=weakColor%>">
	<form method="post" name="titlesearch"
		action="demographicstudysearchresults.jsp?form=1"
		onSubmit="checkTypeIn()">
	<tr valign="top">
		<td rowspan="2" ALIGN="right" valign="middle"><font
			face="Verdana" color="#0000FF"><b><i>Search </i></b></font></td>

		<td width="10%" nowrap><font size="1" face="Verdana"
			color="#0000FF"> <input type="radio" checked
			name="search_mode" value="name"> Name </font></td>
		<td nowrap><font size="1" face="Verdana" color="#0000FF">
		<input type="radio" name="search_mode" value="phone"> Phone</font></td>
		<td nowrap><font size="1" face="Verdana" color="#0000FF">
		<input type="radio" name="search_mode" value="search_dob">
		DOB(yyyymmdd)</font></td>
		<td valign="middle" rowspan="2" ALIGN="left"><input type="text"
			NAME="keyword" SIZE="17" MAXLENGTH="100"> <input
			type="submit" name="submit" value=" GO "></td>
		<INPUT TYPE="hidden" NAME="dboperation" VALUE="search_titlename">
		<INPUT TYPE="hidden" NAME="limit1" VALUE="0">
		<INPUT TYPE="hidden" NAME="limit2" VALUE="10">
	</tr>
	<tr>

		<td nowrap><font size="1" face="Verdana" color="#0000FF">
		<input type="radio" name="search_mode" value="address">
		Address </font></td>
		<td nowrap><font size="1" face="Verdana" color="#0000FF">
		<input type="radio" name="search_mode" value="hin"> HIN</font></td>
		<td nowrap><font size="1" face="Verdana" color="#0000FF">
		<input type="radio" name="search_mode" value="chart_no"> Chart
		No</font></td>
	</tr>
	</form>
</table>

<table width="100%" border="0">
	<tr>
		<td align="left"><i>Results based on keyword(s)</i> : <%=request.getParameter("keyword")!=null?request.getParameter("keyword"):""%></td>
	</tr>
</table>

<CENTER>
<table width="100%" border="0" bgcolor="#ffffff">
	<tr bgcolor="<%=deepColor%>">
		<TH width="10%">Demo' No</TH>
		<TH width="10%">Last Name</TH>
		<TH width="10%">First Name</TH>
		<TH width="3%">Sex</TH>
		<TH width="10%">DOB</TH>
		<TH width="10%">Chart No</TH>
		<TH width="5%">Ros' Sta</TH>
		<TH width="5%">Pat' Sta</TH>
		<TH width="5%">Action</TH>
	</tr>
	<%
    ResultSet rsdemo = null ;
    int nItems=0;
    int ectsize=0;
    String datetime =null;
    String bgcolor = null;
  
    int[] itemp1 = new int[2];
    itemp1[1] = Integer.parseInt(strLimit1);
    itemp1[0] = Integer.parseInt(strLimit2);

    rsdemo = studyBean.queryResults(itemp1, "search_demo");
    while (rsdemo.next()) { 
    	nItems++;
	    bgcolor = nItems%2==0?"#EEEEFF":"white";
        bgcolor = rsdemo.getString("s.study_no")!=null ? rightColor: bgcolor;
%>
	<tr bgcolor="<%=bgcolor%>">
		<td align="center"><a href=#
			onclick="popupPage1('../demographic/demographiccontrol.jsp?demographic_no=<%=rsdemo.getString("d.demographic_no")%>&displaymode=edit&dboperation=search_detail');return false;"><%=rsdemo.getString("d.demographic_no")%></a></td>
		<td><%=rsdemo.getString("d.last_name")%></td>
		<td><%=rsdemo.getString("d.first_name")%></td>
		<td align="center"><%=rsdemo.getString("d.sex")%></td>
		<td align="center"><%=rsdemo.getString("d.year_of_birth") +"-"+ rsdemo.getString("d.month_of_birth") +"-"+ rsdemo.getString("d.date_of_birth")%></td>
		<td align="center"><%=rsdemo.getString("d.chart_no")%></td>
		<td align="center"><%=rsdemo.getString("d.roster_status")%></td>
		<td align="center"><%=rsdemo.getString("d.patient_status")%></td>
		<td align="center"><a href=#
			onClick="popupPage(380, 500, 'demographicstudyselect.jsp?demographic_no=<%=rsdemo.getString("d.demographic_no")%>&name=<%= URLEncoder.encode(rsdemo.getString("d.last_name")+", "+rsdemo.getString("d.first_name"))%> ')"><%=bgcolor.equals(rightColor) ? "Edit": "Join In"%></td>
	</tr>
	<%
  }
%>

</table>
<br>

<%
  int nLastPage=0,nNextPage=0;
  nNextPage=Integer.parseInt(strLimit2)+Integer.parseInt(strLimit1);
  nLastPage=Integer.parseInt(strLimit1)-Integer.parseInt(strLimit2);
  if(nLastPage>=0) {
%> <a
	href="demographicstudysearchresults.jsp?search_mode=<%=request.getParameter("search_mode")%>&keyword=<%=request.getParameter("keyword")%>&limit1=<%=nLastPage%>&limit2=<%=strLimit2%>">Last
Page</a> | <%
  }
  if(nItems==Integer.parseInt(strLimit2)) {
%> <a
	href="demographicstudysearchresults.jsp?search_mode=<%=request.getParameter("search_mode")%>&keyword=<%=request.getParameter("keyword")%>&limit1=<%=nNextPage%>&limit2=<%=strLimit2%>">
Next Page</a> <%
  }
%>
</CENTER>
</body>
</html>
