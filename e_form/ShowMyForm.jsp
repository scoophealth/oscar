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
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
  int demographic_no = Integer.parseInt(request.getParameter("demographic_no")); 
  String deepColor = "#CCCCFF" , weakColor = "#EEEEFF" ;
%>  

<%@ page import = "java.sql.ResultSet" %> 
<jsp:useBean id="myFormBean" class="oscar.AppointmentMainBean" scope="page" />
<%@ include file="../admin/dbconnection.jsp" %>
<% 
  String [][] dbQueries=new String[][] { 
{"search_eformdatadefault", "select * from eforms_data where status = 0 and demographic_no = ? order by form_date DESC" }, 
{"search_eformdataname", "select * from eforms_data where status = 0 and demographic_no = ? order by form_name" }, 
{"search_eformdatasubject", "select * from eforms_data where status = 0 and demographic_no = ? order by subject" }, 
{"search_eformdatadate", "select * from eforms_data where status = 0 and demographic_no = ? order by form_date" }, 
  };
  myFormBean.doConfigure(dbParams,dbQueries);
%>
 
<%  
  String query = dbQueries[0][0];
  if (request.getParameter("query_eforms")!=null) { 
    if(request.getParameter("query_eforms").equals("a")) query =  dbQueries[1][0]  ;
    if(request.getParameter("query_eforms").equals("b")) query =  dbQueries[2][0]  ;
    if(request.getParameter("query_eforms").equals("c")) query =  dbQueries[3][0]  ;
  }
	ResultSet RS = myFormBean.queryResults(demographic_no, query);
%>

<html>
<SCRIPT LANGUAGE="JavaScript">
<!--
if (document.all || document.layers)  window.resizeTo(790,580);
function newWindow(file,window) {
  msgWindow=open(file,window,'scrollbars=yes,width=760,height=520,screenX=0,screenY=0,top=0,left=10');
  if (msgWindow.opener == null) msgWindow.opener = self;
} 
//-->
</SCRIPT>
<head>
<meta http-equiv="Cache-Control" content="no-cache" />
<title>ShowMyForm</title>
<link rel="stylesheet" href="web.css">
</head>

<body topmargin="0" leftmargin="0" rightmargin="0">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="100%" >
  <tr bgcolor=<%=deepColor%> ><th><font face="Helvetica">My FORM</font></th></tr>
</table>

<table cellspacing="0" cellpadding="2" width="100%" border="0" BGCOLOR="<%=weakColor%>">
  <tr><td align='right'><a href="MyForm.jsp?demographic_no=<%=demographic_no%>" > Add E-Form </a>
  | <a href="../demographic/demographiccontrol.jsp?demographic_no=<%=demographic_no%>&displaymode=edit&dboperation=search_detail">Back &nbsp;</a></td>
  </tr>
</table> 

<table border="0" cellspacing="0" cellpadding="0" width="90%">
  <tr><td>Form Library </td>
  <td align='right'><a href="CallDeletedFormData.jsp?demographic_no=<%=demographic_no%>"> 
    List Deleted Forms </a></td></tr>
</table>
  
<table border="0" cellspacing="0" cellpadding="0" width="90%" >
  <tr>
    <td>
    <table border="1" cellspacing="0" cellpadding="0" width="100%">
      <tr bgcolor=<%=deepColor%> ><th><a href="ShowMyForm.jsp?demographic_no=<%=demographic_no%>&query_eforms=a">Form Name</a></th>
      <th><a href="ShowMyForm.jsp?demographic_no=<%=demographic_no%>&query_eforms=b">Subject</a></th>
      <th><a href="ShowMyForm.jsp?demographic_no=<%=demographic_no%>&query_eforms=c">Form Date</a></th>
      <th>Form Time</th> 
      <th>Action</th> 
      </tr> 
<%
  boolean bodd = true ;
  if(RS.next()) {
    RS.beforeFirst();
    while (RS.next()) {
      bodd = bodd?false:true;
      out.print("<tr bgcolor=\"" + (bodd?"#EEEEFF":"white") +"\"><td width=120><a href=\"JavaScript:");
      out.print("newWindow('ShowMyFormData.jsp?fdid="+RS.getInt("fdid")+"','_blank')\">");        
      out.print(RS.getString("form_name"));
      out.print("</a></td>");
      out.print("<td width=200><a href=\"JavaScript:");
      out.print("newWindow('ShowMyFormData.jsp?fdid="+RS.getInt("fdid")+"','_blank')\">");        
      out.print(RS.getString("subject"));
      out.print("</a></td>");
      out.print("<td width=100 align='center'><a href=\"JavaScript:");
      out.print("newWindow('ShowMyFormData.jsp?fdid="+RS.getInt("fdid")+"','_blank')\">");        
      out.print(RS.getString("form_date"));
      out.print("</a></td><td width=130 align='center'>");
      out.print(RS.getString("form_time"));
      out.print("</td><td width=60 align='center'><a href=DeleteFormData.jsp?fdid="+RS.getInt("fdid")+"&demographic_no="+demographic_no+">");
      out.print("Delete");
      out.print("</a></td></tr>");
    }  
    myFormBean.closePstmtConn();
//    RS.close();
  }else {
    out.print("<tr><td align='center' colspan='5'>No data!</td></tr>");
  }
%>               
 
      </table>
      </td>
    </tr>
  </table>

</center>

</body>
</html>

  