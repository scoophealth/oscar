<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
  int fid =new Integer(request.getParameter("fid")).intValue(); 
  int demographic_no = new Integer(request.getParameter("demographic_no")).intValue();
  int provider_no =  new Integer((String) session.getValue("user")).intValue();
  String subject =  request.getParameter("subject") ; 
  String form_name = request.getParameter("form_name");
%>  
<%@ page import = "java.sql.ResultSet, oscar.eform.*"  errorPage="../errorpage.jsp"%>
<jsp:useBean id="beanMakeForm" class="oscar.eform.EfmMakeForm" scope="session"/> 
<jsp:useBean id="myFormBean" class="oscar.AppointmentMainBean" scope="page" />
<%@ include file="../admin/dbconnection.jsp" %>
<% 
  String [][] dbQueries=new String[][] { 
{"search_formhtml", "select form_html from eform where fid = ?" }, 
  };
  myFormBean.doConfigure(dbParams,dbQueries);
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
<meta http-equiv="Cache-Control" content="no-cache" />
<title>Make My Form</title>
</head>
<body>
<%
  ResultSet rs = myFormBean.queryResults(fid, "search_formhtml");
  if (rs.next()){ 
    String theStr = rs.getString("form_html");   
    EfmPrepData prepData = new EfmPrepData(provider_no, demographic_no, fid, form_name) ;

	out.clear();
    out.print(beanMakeForm.addContent(theStr, prepData.getMeta(), prepData.getValue(), prepData.getTagSymbol() ) );
  }else{
%> 
NO such file in database.
<%
  }
  myFormBean.closePstmtConn();
%>               
 
</body>
</html>
