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
//  String demographic_no = request.getParameter("demographic_no"); 
  int fid =new Integer(request.getParameter("fid")).intValue(); 
  int demographic_no = new Integer(request.getParameter("demographic_no")).intValue();
  String subject =  request.getParameter("subject") ; 
  String form_name = request.getParameter("form_name");
%>  
<%@ page import = "java.sql.ResultSet" %>
<jsp:useBean id="beanUtility" scope="session" class="bean.Utility" />
<jsp:useBean id="beanMakeForm" class="bean.MakeForm" scope="session"/> 

<jsp:useBean id="myFormBean" class="oscar.AppointmentMainBean" scope="page" />
<%@ include file="../admin/dbconnection.jsp" %>
<% 
  String [][] dbQueries=new String[][] { 
{"search_formhtml", "select form_html from eforms where fid = ?" }, 
  };
  String[][] responseTargets=new String[][] {  };
  myFormBean.doConfigure(dbParams,dbQueries,responseTargets);
%>

<html>
<head>
<meta http-equiv="Cache-Control" content="no-cache" />
<title>MakeMyForm</title>
</head>
<body>

<%
  session.setAttribute("zt_fid",request.getParameter("fid"));
  session.setAttribute("zt_demographic_no",request.getParameter("demographic_no"));
  session.setAttribute("form_name",form_name);

	ResultSet RS = myFormBean.queryResults(fid, "search_formhtml");
//  String query = "select form_html from eforms where fid= "+fid ;
//  ResultSet RS =  beanDBConnect.executeQuery(query);

  if (RS.next()){ 
    String theStr = RS.getString("form_html");
// theStr = beanUtility.moveQuote(theStr) ;
//**********Add data from database
//out.print(beanMakeForm.addContent(demographic_no,theStr,subject));
    out.print(beanMakeForm.addContent(demographic_no,theStr,fid));

    myFormBean.closePstmtConn();
//    RS.close();
  }else{
%> 
NO such file in database.
<%
  }
%>               
 
</body>
</html>

  
