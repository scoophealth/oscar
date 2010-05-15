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
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
-->

<%
  if(session.getValue("user") == null)
    response.sendRedirect("../login.htm");
  //String curProvider_no;
  //curProvider_no = (String) session.getAttribute("user");
  //curProvider_no =  request.getParameter("provider_no");
  
  //display the main provider page
  //includeing the provider name and a month calendar
  String strLimit1="0";
  String strLimit2="18";
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
<title>PATIENT SEARCH RESULTS - demographicsearchresults</title>
<link rel="stylesheet" href="../web.css">
<script language="JavaScript">
<!--
function setfocus() {
  document.titlesearch.keyword.focus();
  document.titlesearch.keyword.select();
}
//-->
</SCRIPT>
<!--base target="pt_srch_main"-->
</head>
<body background="../images/gray_bg.jpg" bgproperties="fixed"
	onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF">SEARCH
		FOR PATIENT RECORDS</font></th>
	</tr>
</table>

<%@ include file="zdemographicfulltitlesearch.htm"%>
<table width="95%" border="0">
	<tr>
		<td align="left"><i>Results based on keyword(s)</i> : <%=request.getParameter("keyword")%></td>
	</tr>
</table>
<hr>
<CENTER>
<table width="100%" border="2" bgcolor="#ffffff">
	<tr bgcolor="silver">
		<TH align="center" width="20%"><b><a
			href="admincontrol.jsp?keyword=<%=request.getParameter("keyword")%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=demographic_no&limit1=0&limit2=<%=strLimit2%>">DEMOGP'
		NO</a></b></font></TH>
		<TH align="center" width="20%"><b><a
			href="admincontrol.jsp?keyword=<%=request.getParameter("keyword")%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=last_name&limit1=0&limit2=<%=strLimit2%>">LAST
		NAME</a> </b></font></TH>
		<TH align="center" width="20%"><b><a
			href="admincontrol.jsp?keyword=<%=request.getParameter("keyword")%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=first_name&limit1=0&limit2=<%=strLimit2%>">FIRST
		NAME</a> </b></font></TH>
		<TH align="center" width="10%"><b><a
			href="admincontrol.jsp?keyword=<%=request.getParameter("keyword")%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=year_of_birth,month_of_birth,date_of_birth&limit1=0&limit2=<%=strLimit2%>">AGE</a></b></font></TH>
		<TH align="center" width="10%"><b><a
			href="admincontrol.jsp?keyword=<%=request.getParameter("keyword")%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=roster_status&limit1=0&limit2=<%=strLimit2%>">ROSTER
		STATUS</a></b></font></TH>
		<TH align="center" width="10%"><b><a
			href="admincontrol.jsp?keyword=<%=request.getParameter("keyword")%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=sex&limit1=0&limit2=<%=strLimit2%>">SEX</a></B></font></TH>
		<TH align="center" width="10%"><b><a
			href="admincontrol.jsp?keyword=<%=request.getParameter("keyword")%>&displaymode=<%=request.getParameter("displaymode")%>&search_mode=<%=request.getParameter("search_mode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=year_of_birth,month_of_birth,date_of_birth&limit1=0&limit2=<%=strLimit2%>">DOB(yy/mm/dd)</a></B></Font></TH>
	</tr>
	<%
	GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  int age=0;
  ResultSet rs=null ;
    
  String dboperation = request.getParameter("dboperation");
  String keyword=request.getParameter("keyword").trim();
  //keyword.replace('*', '%').trim();
  if(request.getParameter("search_mode").equals("search_name")) {
      keyword=request.getParameter("keyword")+"%";
      if(keyword.indexOf(",")==-1)  rs = apptMainBean.queryResults(keyword, dboperation) ; //lastname
      else if(keyword.indexOf(",")==(keyword.length()-1))  rs = apptMainBean.queryResults(keyword.substring(0,(keyword.length()-1)), dboperation);//lastname
      else { //lastname,firstname
    		String[] param =new String[2];
    		int index = keyword.indexOf(",");
	  		param[0]=keyword.substring(0,index).trim()+"%";//(",");
	  		param[1]=keyword.substring(index+1).trim()+"%";
	  		//System.out.println("from -------- :"+ param[0]+ ": next :"+param[1]);
    		rs = apptMainBean.queryResults(param, dboperation);
   		}
  } else if(request.getParameter("search_mode").equals("search_dob")) {
    		String[] param =new String[3];
	  		param[0]=""+MyDateFormat.getYearFromStandardDate(keyword)+"%";//(",");
	  		param[1]=""+MyDateFormat.getMonthFromStandardDate(keyword)+"%";
	  		param[2]=""+MyDateFormat.getDayFromStandardDate(keyword)+"%";  
	      //System.out.println("1111111111111111111 "+param[0]+param[1]+param[2]);
    		rs = apptMainBean.queryResults(param, dboperation);
  } else {
    keyword=request.getParameter("keyword")+"%";
    rs = apptMainBean.queryResults(keyword, dboperation);
  }
 
  boolean bodd=false;
  int nItems=0;
  
  if(rs==null) {
    out.println("failed!!!");
  } else {
    while (rs.next()) {
      bodd=bodd?false:true; //for the color of rows
      nItems++; //to calculate if it is the end of records

     if(!(rs.getString("month_of_birth").equals(""))) {//   ||rs.getString("year_of_birth")||rs.getString("date_of_birth")) {
    	if(curMonth>Integer.parseInt(rs.getString("month_of_birth"))) {
    		age=curYear-Integer.parseInt(rs.getString("year_of_birth"));
    	} else {
    		if(curMonth==Integer.parseInt(rs.getString("month_of_birth")) &&
    			curDay>Integer.parseInt(rs.getString("date_of_birth"))) {
    			age=curYear-Integer.parseInt(rs.getString("year_of_birth"));
    		} else {
    			age=curYear-Integer.parseInt(rs.getString("year_of_birth"))-1; 
    		}
    	}	
     }	
   
%>
	<tr bgcolor="<%=bodd?"ivory":"white"%>">
		<td width="20%" align="center" height="25"><a
			href="admincontrol.jsp?demographic_no=<%=rs.getString("demographic_no")%>&displaymode=Demographic_Edit&dboperation=demographic_search_detail"><%=rs.getString("demographic_no")%></a></td>
		<td align="center" width="20%" height="25"><%=rs.getString("last_name")%></td>
		<td align="center" width="20%" height="25"><%=rs.getString("first_name")%></td>
		<td align="center" width="10%" height="25"><%=age%></td>
		<td align="center" width="10%" height="25"><%=rs.getString("roster_status")%></td>
		<td align="center" width="10%" height="25"><%=rs.getString("sex")%></td>
		<td align="center" width="10%" height="25"><%=rs.getString("year_of_birth")+"-"+rs.getString("month_of_birth")+"-"+rs.getString("date_of_birth")%></td>
	</tr>
	<%
    }
  }
  apptMainBean.closePstmtConn();
%>

</table>
<br>
<%
  int nLastPage=0,nNextPage=0;
  nNextPage=Integer.parseInt(strLimit2)+Integer.parseInt(strLimit1);
  nLastPage=Integer.parseInt(strLimit1)-Integer.parseInt(strLimit2);
  if(nLastPage>=0) {
%> <a
	href="admincontrol.jsp?keyword=<%=request.getParameter("keyword")%>&search_mode=<%=request.getParameter("search_mode")%>&displaymode=<%=request.getParameter("displaymode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nLastPage%>&limit2=<%=strLimit2%>">Last
Page</a> | <%
  }
  if(nItems==Integer.parseInt(strLimit2)) {
%> <a
	href="admincontrol.jsp?keyword=<%=request.getParameter("keyword")%>&search_mode=<%=request.getParameter("search_mode")%>&displaymode=<%=request.getParameter("displaymode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nNextPage%>&limit2=<%=strLimit2%>">
Next Page</a> <%
}
%>
<p>Please select by clicking on the patient's demographic id for
details.</p>
</center>
<%@ include file="footer.htm"%>
</body>
</html>