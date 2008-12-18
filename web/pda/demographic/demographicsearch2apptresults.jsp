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
  if(session.getValue("user") == null)    response.sendRedirect("../logout.htm");
  // String curProvider_no = (String) session.getAttribute("user");
  String curProvider_no = request.getParameter("provider_no");
  String strLimit1="0";
  String strLimit2="10";
  if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");
  if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");
%>

<%@ page import="java.util.*, java.sql.*, oscar.*"
	errorPage="errorpage.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>PATIENT SEARCH RESULTS (demographicsearch2apptresults)</title>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">
<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
  document.titlesearch.keyword.focus();
  document.titlesearch.keyword.select();
}
//-->
</SCRIPT>
</head>
<body background="../images/gray_bg.jpg" bgproperties="fixed"
	onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF">PATIENT'S
		RECORD</font></th>
	</tr>
</table>

<table border="0" cellpadding="1" cellspacing="0" width="100%"
	bgcolor="#C4D9E7">
	<form method="post" name="titlesearch"
		action="../demographic/demographiccontrol.jsp"><%--@ include file="zdemographictitlesearch.htm"--%>
	<tr valign="top">
		<td rowspan="2" ALIGN="right" valign="middle"><font
			face="Verdana" color="#0000FF"><b><i>Search </i></b></font></td>

		<td width="10%" nowrap><font size="1" face="Verdana"
			color="#0000FF"> <input type="radio" checked
			name="search_mode" value="search_name"> Name </font></td>
		<td nowrap><font size="1" face="Verdana" color="#0000FF">
		<input type="radio" name="search_mode" value="search_phone">
		Phone</font></td>
		<td nowrap><font size="1" face="Verdana" color="#0000FF">
		<input type="radio" name="search_mode" value="search_dob"> DOB</font></td>
		<td valign="middle" rowspan="2" ALIGN="left"><input type="text"
			NAME="keyword" SIZE="17" MAXLENGTH="100"> <INPUT
			TYPE="hidden" NAME="orderby" VALUE="last_name"> <INPUT
			TYPE="hidden" NAME="dboperation" VALUE="search_titlename"> <INPUT
			TYPE="hidden" NAME="limit1" VALUE="0"> <INPUT TYPE="hidden"
			NAME="limit2" VALUE="5"> <input type="hidden"
			name="displaymode" value="Search "> <input type="SUBMIT"
			name="displaymode" value="Search " size="17">
	</tr>
	<tr>

		<td nowrap><font size="1" face="Verdana" color="#0000FF">
		<input type="radio" name="search_mode" value="search_address">
		Address </font></td>
		<td nowrap><font size="1" face="Verdana" color="#0000FF">
		<input type="radio" name="search_mode" value="search_hin"> HIN</font></td>
		<td></td>
	</tr>
	<%
  String temp=null;
	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
		temp=e.nextElement().toString();
		if(temp.equals("keyword") || temp.equals("dboperation") ||temp.equals("displaymode") ||temp.equals("search_mode") ||temp.equals("chart_no")) continue;
  	out.println("<input type='hidden' name='"+temp+"' value='"+request.getParameter(temp)+"'>");
  }
	%>
	</form>
</table>

<table width="95%" border="0">
	<tr>
		<td align="left"><font size="-1"><i>Results based on
		keyword(s)</i> : <%=request.getParameter("keyword")%></font></td>
	</tr>
</table>


<script language="JavaScript">
<!--
var fullname="";
function addName(lastname, firstname, chartno) {
  fullname=lastname+","+firstname;
  //document.write("<input type='hidden' name='bFirstDisp' value='false'>");
  //alert("<input type='hidden' name='name' value='"+ lastname+", "+firstname+"'>");
  document.addform.action="<%=request.getParameter("originalpage")%>?name="+fullname+"&chart_no="+chartno+"&bFirstDisp=false";  //+"\"" ;
  document.addform.submit(); // 
  //return;
}
//-->
</SCRIPT>

<CENTER>
<table width="100%" border="1" cellpadding="0" cellspacing="1"
	bgcolor="#ffffff">
	<form method="post" name="addform"
		action="../appointment/addappointment.jsp">
	<tr bgcolor="#339999">
		<TH align="center" width="20%"><b>DEMOGP' ID</b></TH>
		<TH align="center" width="20%"><b>LAST NAME </b></TH>
		<TH align="center" width="20%"><b>FIRST NAME </b></TH>
		<TH align="center" width="10%"><b>AGE</b></TH>
		<TH align="center" width="10%"><b>ROSTER STATUS</b></TH>
		<TH align="center" width="10%"><b>SEX</B></TH>
		<TH align="center" width="10%"><b>DOB(yy/mm/dd)</B></TH>
	</tr>

	<%@ include file="../demographic/zdemographicsearchresult.jsp"%>

	<tr bgcolor="<%=bodd?"ivory":"white"%>" align="center">
		<td><input type="submit" name="demographic_no"
			value="<%=rs.getString("demographic_no")%>"
			onClick="addName('<%=rs.getString("last_name")%>','<%=rs.getString("first_name")%>','<%=rs.getString("chart_no")%>')"></td>
		<td><%=rs.getString("last_name")%></td>
		<td><%=rs.getString("first_name")%></td>
		<td><%=age%></td>
		<td><%=rs.getString("roster_status")%></td>
		<td><%=rs.getString("sex")%></td>
		<td><%=rs.getString("year_of_birth")+"-"+rs.getString("month_of_birth")+"-"+rs.getString("date_of_birth")%></td>
	</tr>
	<%
    }
  }
%>
	<%
  //String temp=null;
	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
		temp=e.nextElement().toString();
		if(temp.equals("keyword") || temp.equals("dboperation") ||temp.equals("displaymode")||temp.equals("submit") ||temp.equals("chart_no")) continue;
  	out.println("<input type='hidden' name='"+temp+"' value='"+request.getParameter(temp)+"'>");
         //System.out.println();
  }
  
  //should close the pipe connected to the database here!!!
	%>
	</form>
	<%	
  if(nItems==1) { //if there is only one search result, it should be added to the appoint page directly.
	%>
	<script language="JavaScript">
<!--
<%
  out.println("fullname=\"" +(rs.getString("last_name")+ ","+ rs.getString("first_name")) +"\";");
%>
  document.addform.action="<%=request.getParameter("originalpage")%>?name="+fullname+"&bFirstDisp=false&demographic_no="+ <%=rs.getString("demographic_no")%>; // +  "\""  ;
  document.addform.submit();  
//-->
</SCRIPT>
	<%
  }
%>

</table>
<%
  int nLastPage=0,nNextPage=0;
  nNextPage=Integer.parseInt(strLimit2)+Integer.parseInt(strLimit1);
  nLastPage=Integer.parseInt(strLimit1)-Integer.parseInt(strLimit2);
%> <script language="JavaScript">
<!--
function last() {
  document.nextform.action="../demographic/demographiccontrol.jsp?keyword=<%=request.getParameter("keyword")%>&search_mode=<%=request.getParameter("search_mode")%>&displaymode=<%=request.getParameter("displaymode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nLastPage%>&limit2=<%=strLimit2%>" ;
  //document.nextform.submit();  
}
function next() {
  document.nextform.action="../demographic/demographiccontrol.jsp?keyword=<%=request.getParameter("keyword")%>&search_mode=<%=request.getParameter("search_mode")%>&displaymode=<%=request.getParameter("displaymode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nNextPage%>&limit2=<%=strLimit2%>" ;
  //document.nextform.submit();  
}
//-->
</SCRIPT>

<form method="post" name="nextform"
	action="../demographic/demographiccontrol.jsp">
<%
  if(nLastPage>=0) {
%> <input type="submit" name="submit" value="Last Page" onClick="last()">
<%
  }
  if(nItems==Integer.parseInt(strLimit2)) {
%> <input type="submit" name="submit" value="Next Page" onClick="next()">
<%
}
%> <%
	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
		temp=e.nextElement().toString();
		if(temp.equals("keyword") || temp.equals("dboperation") ||temp.equals("displaymode") ||temp.equals("submit")  ||temp.equals("chart_no")) continue;
  	out.println("<input type='hidden' name='"+temp+"' value='"+request.getParameter(temp)+"'>");
         //System.out.println();
  }
	%>
</form>

Please select by clicking on the demographic no button.</center>
</body>
</html>