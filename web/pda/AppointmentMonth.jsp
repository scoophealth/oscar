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

<%@ page import="java.sql.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page errorPage="ErrorPage.jsp"%>
<%@ page import="bean.*"%>

<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<jsp:useBean id="beanDBConnect" scope="session" class="bean.DBConnect" />
<jsp:useBean id="beanFunctionGenerator" scope="session"
	class="bean.FunctionGenerator" />
<jsp:useBean id="beanDBQuery" scope="session" class="bean.DBQuery" />

<HTML>
<!--Copyright (c) http://oscar.mcmaster.ca:8888/oscartest/copyright -->
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<TITLE>AppointmentMonth</TITLE>
<meta http-equiv="Cache-Control" content="no-cache" />
</HEAD>

<%
  if(session.getValue("user") == null)    response.sendRedirect("logout.jsp");

// the only parameters are start_date and end_date
  String start_date = request.getParameter("start_date");
  String end_date = request.getParameter("end_date");
 
//********************************************************

  int provider_no = new Integer((String) session.getAttribute("user")).intValue();
 
  String [] theArrayA = new String[31];
  String [] theArrayB = new String[31];
  String [] theArrayC = new String[31];
 
    theArrayA = beanFunctionGenerator.getMonthCalendar(start_date,end_date);
    theArrayB = beanFunctionGenerator.getAppointmentInMonth(provider_no, theArrayA);
    theArrayC = beanDBQuery.getAppointmentNo(theArrayB,provider_no);

%>

<BODY>


Date
:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<a href="PreviousMonth.jsp?start_date=<%=start_date%>"><<</b></a>
&nbsp;&nbsp;&nbsp;&nbsp;
<a href="NextMonth.jsp?start_date=<%=start_date%>">>></b></a>
&nbsp;&nbsp;
<a href="Search.jsp">Search</a>
&nbsp;&nbsp;
<a href="logout.jsp" <b>LogOut</b></a>
<table border="1">

	<%
   for (int i=0;i<theArrayA.length;i++){
          out.print("<tr><td>");
//          out.print("<a href=\"AddAppointmentMonthForm.jsp?appointment_date="+theArrayA[i]+"&start_time=08:00:00\" >");
          out.print("<a href=\"AppointmentToday.jsp?todayString="+theArrayA[i]+"&apt_no="+theArrayC[i]+"\" >");
	  out.print(theArrayA[i]);
 	  out.print("</a>");
	  out.print("</td><td>&nbsp;&nbsp;");
//          out.print("<a href=\"AppointmentDay.jsp?appointment_date="+theArrayA[i]+"&apt_no="+theArrayC[i]+"\" >");
//          out.print("<a href=\"AppointmentToday.jsp?todayString="+theArrayA[i]+"&apt_no="+theArrayC[i]+"\" >");
	  out.print(theArrayC[i]);
	  out.print("</a>");
	  out.print("</td></tr>");
    }
 	    
%>

</table>
</body>
</HTML>
