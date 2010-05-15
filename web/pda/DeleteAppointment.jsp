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

<%@ page import="java.sql.*, java.util.*" errorPage="ErrorPage.jsp"%>
<jsp:useBean id="beanDBQuery" scope="session" class="bean.DBQuery" />
<jsp:useBean id="beanSwitchControl" scope="session"
	class="bean.SwitchControl" />

<html>
<!--Copyright (c) http://oscar.mcmaster.ca:8888/oscartest/copyright -->
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>DeleteAppointment</title>
</head>
<body>

<%
  if(session.getValue("user") == null)    response.sendRedirect("logout.jsp");

   String DayString = request.getParameter("DayString");
   String appointment_date = DayString ;

   int appointment_no = new Integer(request.getParameter("appointment_no")).intValue(); 
//***********************************************************
 

  int provider_no = new Integer((String) session.getAttribute("user")).intValue();


//if action is good, then give me the result
   int DeleteResult = beanDBQuery.deleteAppointment(appointment_no);
 
   if (DeleteResult ==1) {

// set the switch on
    beanSwitchControl.set_AppointmentToday_switch(1);

    response.sendRedirect("AppointmentToday.jsp?todayString="+appointment_date);


//if action is failed,
  } else {
%>
<br>
<br>
Sorry, deletion
<br>
has failed.
<%  
  }

// maybe use this to go to AppointmentToday, but not now

    String haveAppointmentOnThatDayOrNot = beanDBQuery.haveAppointmentOnThatDayOrNot(provider_no,DayString);

    int appointment_quantity = beanDBQuery.getAppointmentNo(haveAppointmentOnThatDayOrNot,provider_no);
 
%>
<br>
<br>
<a
	href="main.jsp?todayString=<%=haveAppointmentOnThatDayOrNot%>&apt_no=<%=appointment_quantity%>">Return</a>


</body>
</html>