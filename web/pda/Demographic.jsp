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
<jsp:useBean id="beanFunctionGenerator" scope="session"
	class="bean.FunctionGenerator" />

<% 
//  if(session.getValue("user") == null)    response.sendRedirect("logout.htm");
 
        String demographic_no = request.getParameter("demographic_no").trim();
        String keyword = request.getParameter("keyword").trim();
        String button = request.getParameter("button").trim();

//*****************************************************************  
 
  String queryString = "";
        queryString = "select * from demographic where demographic_no="+demographic_no;
        ResultSet rs = beanDBConnect.executeQuery(queryString);
 
%>

<html>
<!--Copyright (c) http://oscar.mcmaster.ca:8888/oscartest/copyright -->
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Demographic</title>
<meta http-equiv="Cache-Control" content="no-cache">

</head>
<body>

<%

 // if has search result
if( rs.next()){
  out.print("<table border=\"1\" width=\"38%\">");
  out.print("<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"ProblimList.jsp?demographic_no="+demographic_no+"&keyword="+keyword+"&button="+button+"\">CPP</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"SearchIt.jsp?keyword="+keyword+"&button="+button+"\">Return</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"logout.jsp\">LogOut</a></td></tr>");
  out.print("</table>");

  out.print("<table border=\"0\" width=\"38%\">");
  out.print("<tr><td width=\"8%\">LastName</td><td><input type=\"text\" width=\"6\" value=\""+rs.getString("last_name")+"\"></td></tr>" );
  out.print("<tr><td width=\"8%\">FirstName</td><td><input type=\"text\" width=\"6\" value=\""+rs.getString("first_name")+"\"></td></tr>" );
  out.print("<tr><td width=\"8%\">Address</td><td><input type=\"text\" width=\"6\" value=\""+rs.getString("address")+"\"></td></tr>" );
  out.print("<tr><td width=\"8%\">City</td><td><input type=\"text\" width=\"6\" value=\""+rs.getString("city")+"\"></td></tr>" );
  out.print("<tr><td width=\"8%\">Province</td><td><input type=\"text\" width=\"6\" value=\""+rs.getString("province")+"\"></td></tr>" );
  out.print("<tr><td width=\"8%\">Postal</td><td><input type=\"text\" width=\"6\" value=\""+rs.getString("postal")+"\"></td></tr>" );
  out.print("<tr><td width=\"8%\">Phone(h)</td><td><input type=\"text\" width=\"6\" value=\""+rs.getString("phone")+"\"></td></tr>" );
  out.print("<tr><td width=\"8%\">Phone(o)</td><td><input type=\"text\" width=\"6\" value=\""+rs.getString("phone2")+"\"></td></tr>" );
  out.print("<tr><td width=\"8%\">DOB</td><td><input type=\"text\" width=\"6\" value=\""+rs.getString("year_of_birth")+"-"+rs.getString("month_of_birth")+"-"+rs.getString("date_of_birth")+"\"></td></tr>" );
  out.print("<tr><td width=\"8%\">Age</td><td><input type=\"text\" width=\"6\" value=\""+beanFunctionGenerator.getAge(rs.getString("year_of_birth"),rs.getString("month_of_birth"))+"\"></td></tr>" );
  out.print("<tr><td width=\"8%\">HIN</td><td><input type=\"text\" width=\"6\" value=\""+rs.getString("hin")+"\"></td></tr>" );
  out.print("<tr><td width=\"8%\">Ver</td><td><input type=\"text\" width=\"6\" value=\""+rs.getString("ver")+"\"></td></tr>" );
  out.print("<tr><td width=\"8%\">Roster S.</td><td><input type=\"text\" width=\"6\" value=\""+rs.getString("roster_status")+"\"></td></tr>" );
  out.print("<tr><td width=\"8%\">Patient S.</td><td><input type=\"text\" width=\"6\" value=\""+rs.getString("patient_status")+"\"></td></tr>" );
  out.print("<tr><td width=\"8%\">DateJoined</td><td><input type=\"text\" width=\"6\" value=\""+rs.getString("date_joined")+"\"></td></tr>" );
  out.print("<tr><td width=\"8%\">HC Type</td><td><input type=\"text\" width=\"6\" value=\""+rs.getString("hc_type")+"\"></td></tr>" );
  out.print("<tr><td width=\"8%\">MCDoctor</td><td><input type=\"text\" width=\"6\" value=\"???\"></td></tr>" );
  out.print("<tr><td width=\"8%\">FMDoctor</td><td><input type=\"text\" width=\"6\" value=\""+rs.getString("family_doctor")+"\"></td></tr>" );
  out.print("<tr><td width=\"8%\">EndDate</td><td><input type=\"text\" width=\"6\" value=\""+rs.getString("end_date")+"\"></td></tr>" );
  out.print("<tr><td width=\"8%\">EFFDate</td><td><input type=\"text\" width=\"6\" value=\""+rs.getString("eff_date")+"\"></td></tr>" );
  out.print("<tr><td width=\"8%\">PCN</td><td><input type=\"text\" width=\"6\" value=\""+rs.getString("pcn_indicator")+"\"></td></tr>" );
  out.print("<tr><td width=\"8%\">ChartNo</td><td><input type=\"text\" width=\"6\" value=\""+rs.getString("chart_no")+"\"></td></tr>" );
  out.print("<tr><td width=\"8%\">RenewDate</td><td><input type=\"text\" width=\"6\" value=\""+rs.getString("hc_renew_date")+"\"></td></tr>" );
  out.print("<tr><td width=\"8%\">Sex</td><td><input type=\"text\" width=\"6\" value=\""+rs.getString("sex")+"\"></td></tr>" );

 
  rs.close();


// if has NO  result
}else{
   out.print("<table border=\"1\" width=\"38%\">");
   out.print("<tr><td>No record</td></tr>");
   out.print("<tr><td><a href=\"SearchIt.jsp?keyword="+keyword+"&button="+button+"\">Return</a>&nbsp;<a href=\"logout.jsp\">LogOut</a></td></tr>");
}

%>
</table>
</body>
</html>