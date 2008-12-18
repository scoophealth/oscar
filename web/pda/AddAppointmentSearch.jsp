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

<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page errorPage="ErrorPage.jsp"%>

<jsp:useBean id="beanDBConnect" scope="session" class="bean.DBConnect" />
<jsp:useBean id="beanDBQuery" scope="session" class="bean.DBQuery" />
<jsp:useBean id="beanSwitchControl" scope="session"
	class="bean.SwitchControl" />
<jsp:useBean id="beanFunctionGenerator" scope="session"
	class="bean.FunctionGenerator" />

<% 
  if(session.getValue("user") == null)    response.sendRedirect("logout.jsp");
 
  String appointment_date = request.getParameter("appointment_date").trim();
  String start_time = request.getParameter("start_time").trim();
  String end_time = request.getParameter("end_time").trim();
 
//*****************************************************************  

  String queryString = "";
  String name = ""; 
      name = request.getParameter("name").trim();

      ResultSet rs = beanDBQuery.getSearchName(name);

%>


<html>
<!--Copyright (c) http://oscar.mcmaster.ca:8888/oscartest/copyright -->
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>AddAppointmentSearch</title>
<meta http-equiv="Cache-Control" content="no-cache">

</head>
<body>
<table border="0">
	<%
 // if has search result
if( rs.next()){

  rs.beforeFirst();
   
  while (rs.next()){

//  out.print("<tr><td><a href=\"AddAppointmentForm2.jsp?demographic_no="+rs.getInt("demographic_no")+"&appointment_date="+appointment_date+"&start_time="+start_time+"&end_time="+end_time+"&name="+rs.getString("last_name")+","+rs.getString("first_name")+"\">Get</a></td>" );
//  out.print("<tr><td><a href=\"AppointmentForm2.jsp?appointment_no="+appointment_no+"&demographic_no="+rs.getInt("demographic_no")+"&appointment_date="+appointment_date+"&start_time="+start_time+"&end_time="+end_time+"&name="+rs.getString("last_name")+","+rs.getString("first_name")+"\">Get</a></td>" );
  out.print("<tr><td><a href=\"AddAppointmentForm2.jsp?demographic_no="+rs.getInt("demographic_no")+"&appointment_date="+appointment_date+"&start_time="+start_time+"&end_time="+end_time+"&name="+rs.getString("last_name")+","+rs.getString("first_name")+"\">" );
  out.print(rs.getString("last_name")+"," );
  out.print(rs.getString("first_name")+"</td>" );
  out.print("</a>" );
//  out.print("<td>"+rs.getString("year_of_birth")+"</td> ");

  out.print("<td>"+beanFunctionGenerator.getAge(rs.getString("year_of_birth"),rs.getString("month_of_birth"))+"</td> ");
  out.print("<td>"+rs.getString("sex")+"</td> ");
  out.print("</td></tr>");
  }

  rs.close();


// if has NO search result
}else{
  
//response.sendRedirect("AddAppointmentForm2.jsp?appointment_date="+appointment_date+"&start_time="+start_time+"&end_time="+end_time+"&name="+name);
 
    String gotoJsp = "AddAppointmentForm2.jsp?appointment_date="+appointment_date+"&start_time="+start_time+"&end_time="+end_time+"&name="+name;
           gotoJsp = beanFunctionGenerator.getHttpHeaderString(gotoJsp);    

  
    response.sendRedirect(gotoJsp);
 

}

%>
</table>
</body>
</html>