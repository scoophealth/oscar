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
  String demographic_no = request.getParameter("demographic_no"); 
  String deepColor = "#CCCCFF" , weakColor = "#EEEEFF" ;
%>  

<%@ page import = "java.sql.ResultSet" %> 
<jsp:useBean id="myFormBean" class="oscar.AppointmentMainBean" scope="page" />
<%@ include file="../admin/dbconnection.jsp" %>
<% 
  String [][] dbQueries=new String[][] { 
{"search_eform", "select * from eforms where status = 0 order by ?" }, 
  };
  myFormBean.doConfigure(dbParams,dbQueries);

  String query = "form_date";
  if (request.getParameter("query_eforms")!=null) { 
    if(request.getParameter("query_eforms").equals("a")) query =  "form_name" ;
    if(request.getParameter("query_eforms").equals("b")) query =  "subject"  ;
    if(request.getParameter("query_eforms").equals("c")) query =  "file_name"  ;
    if(request.getParameter("query_eforms").equals("d")) query =  "form_date DESC"  ;
    if(request.getParameter("query_eforms").equals("e")) query =  "form_time DESC"  ;
  }
	ResultSet RS = myFormBean.queryResults(query, "search_eform");
%>

<html>
<head>
<meta http-equiv="Cache-Control" content="no-cache" />
<title>MyForm</title>
<link rel="stylesheet" href="web.css">
<script language="javascript">
<!--
if (document.all || document.layers)
  window.resizeTo(790,580)
function checkHtml(){
  if(document.myForm.FileName.value==""){ 
    alert("Please choose a file first, then click Upload");
  } else {
    document.myForm.submit();
  } 
}
function newWindow(file,window) {
  msgWindow=open(file,window,'scrollbars=yes,width=760,height=520,screenX=0,screenY=0,top=0,left=10');
  if (msgWindow.opener == null) msgWindow.opener = self;
}
function returnMain(demographic_no) {
  top.location.href = "../demographic/demographiceditdemographic.jsp?demographic_no="+demographic_no;
}
//-->
</script>
</head>

<body topmargin="0" leftmargin="0" rightmargin="0">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="100%" >
  <tr bgcolor=<%=deepColor%> ><th><font face="Helvetica">My FORM</font></th></tr>
</table>

<table cellspacing="0" cellpadding="2" width="100%" border="0" BGCOLOR="<%=weakColor%>">
  <tr><td align='right'><a href="../demographic/demographiccontrol.jsp?demographic_no=<%=demographic_no%>&displaymode=edit&dboperation=search_detail">Back &nbsp;</a></td>
  </tr>
</table> 
   
<table border="0" cellspacing="0" cellpadding="0" width="90%">
  <tr><td>Form Library </td>
  <td align='right'></td></tr>
</table>

<table border="0" cellspacing="0" cellpadding="0" width="90%" >
  <tr>
    <td>
    <table border="1" cellspacing="0" cellpadding="1" width="100%">
      <tr bgcolor=<%=deepColor%> >
      <th><a href="MyForm.jsp?demographic_no=<%=demographic_no%>&query_eforms=a">Form Name</a></th>
      <th><a href="MyForm.jsp?demographic_no=<%=demographic_no%>&query_eforms=b">Subject</a></th>
      <th><a href="MyForm.jsp?demographic_no=<%=demographic_no%>&query_eforms=c">File</a></th>
      <th><a href="MyForm.jsp?demographic_no=<%=demographic_no%>&query_eforms=d">Form Date</a></th>
      <th><a href="MyForm.jsp?demographic_no=<%=demographic_no%>&query_eforms=e">Form Time</a></th> 
      </tr> 
<%
  String bgcolor = null;
  while (RS.next()){
    bgcolor = RS.getRow()%2==0?weakColor:"white" ;
    out.print("<tr bgcolor=\"" +bgcolor+ "\"><td width=130>");
    out.print("<a href=\"MakeMyForm.jsp?fid="+RS.getInt("fid")+"&form_name="+RS.getString("form_name")+ "&demographic_no="+demographic_no+"&subject="+RS.getString("subject")+"\">");          
        out.print(RS.getString("form_name"));
        out.print("</a></td><td width=190>");
        out.print("<a href=\"MakeMyForm.jsp?fid="+RS.getInt("fid")+"&form_name="+RS.getString("form_name")+ "&demographic_no="+demographic_no+"&subject="+RS.getString("subject")+"\">");       
        out.print(RS.getString("subject"));
        out.print("</a></td><td width=190>");
        out.print("<a href=\"MakeMyForm.jsp?fid="+RS.getInt("fid")+"&form_name="+RS.getString("form_name")+ "&demographic_no="+demographic_no+"&subject="+RS.getString("subject")+"\">");        
    out.print(RS.getString("file_name"));
    out.print("</a></td><td nowrap align='center'>");
    out.print(RS.getString("form_date")+"</td>");
    out.print("<td nowrap align='center'>");
    out.print(RS.getString("form_time")+"</td>");
    out.print("</tr>");
  }  
    myFormBean.closePstmtConn();
//RS.close();

%>               
 
         </table>
      </td>
    </tr>
  </table>

</center>

</body>
</html>

  