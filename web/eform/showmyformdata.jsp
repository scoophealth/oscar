<%--  
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
--%>
<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
  int fdid = Integer.parseInt(request.getParameter("fdid")) ; 
  String deepColor = "#CCCCFF" , weakColor = "#EEEEFF" ;
%>  

<%@ page import = "java.sql.ResultSet, java.net.*" errorPage="../errorpage.jsp"%> 
<jsp:useBean id="beanMakeForm" scope="session" class="oscar.eform.EfmMakeForm" />
<jsp:useBean id="myFormBean" class="oscar.AppointmentMainBean" scope="page" />
<%@ include file="../admin/dbconnection.jsp" %>
<% 
  String [][] dbQueries=new String[][] { 
{"search_eformdata", "select * from eform_data where fdid= ?" }, 
  };
  myFormBean.doConfigure(dbParams,dbQueries);
%>
 
<%
  ResultSet rs = myFormBean.queryResults(fdid, "search_eformdata");

  if (rs.next()){ 
    int demographic_no = rs.getInt("demographic_no");
    int status = rs.getInt("status");
    String form_name = rs.getString("form_name");
    String subject = rs.getString("subject");
    String form_date = rs.getString("form_date");
    String form_time = rs.getString("form_time");
    String form_provider = rs.getString("form_provider");
    String form_data = rs.getString("form_data");

    String [] meta = new String[]{"action="};
    String [] value = new String[] {"\"editmyform.jsp?fdid="+fdid+"&form_name="+URLEncoder.encode(form_name,"UTF-8")+"&demographic_no="+demographic_no+"\"  "  };
    String [][] tag = new String[][] { {"form", ""} };
    form_data = beanMakeForm.addContent(form_data, meta, value, tag) ;
    out.clear();
    out.print(form_data); 
  }else{
%> 
NO such file in database.
<%
  }
  myFormBean.closePstmtConn();
%>               
 
</body>
</html>