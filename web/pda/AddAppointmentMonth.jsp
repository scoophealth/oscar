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

<%@ page errorPage="ErrorPage.jsp"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.*"%>
<%@ page import="bean.*"%>

<jsp:useBean id="beanDBConnect" scope="session" class="bean.DBConnect" />
<jsp:useBean id="beanDBQuery" scope="session" class="bean.DBQuery" />
<jsp:useBean id="beanSwitchControl" scope="session"
	class="bean.SwitchControl" />
<jsp:useBean id="beanFunctionGenerator" scope="session"
	class="bean.FunctionGenerator" />
<html>
<!--Copyright (c) http://oscar.mcmaster.ca:8888/oscartest/copyright -->
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>AddAppointmentMonth</title>
</head>
<body>


<%  
 
        int creator = new Integer((String) session.getAttribute("user")).intValue();
        int provider_no = new Integer((String) session.getAttribute("user")).intValue();

        String appointment_date = request.getParameter("appointment_date").trim();
        String start_time = request.getParameter("start_time").trim();
        String end_time = request.getParameter("end_time").trim();
        String name = request.getParameter("name").trim();
        String reason = request.getParameter("reason").trim();
        String location = request.getParameter("location").trim();
//        String creator = request.getParameter("creator").trim();
        String status = request.getParameter("status").trim();
        String type = request.getParameter("type").trim();
        String notes = request.getParameter("notes").trim();
        String resources = request.getParameter("resources").trim();
        String createdatetime = request.getParameter("createdatetime").trim();

// if user click "Updat" button
if (request.getParameter("submitButton")!=null &&
               request.getParameter("submitButton").equals("Update")){



//      String queryString = update appointment set demographic_no=?,end_time=?,name=?, notes=?,reason =?,location=?, resources=?, type=?,style=?,billing =?,status=?,createdatetime=?,creator=?,remarks=? where appointment_no=? "}, //provider_no=? and appointment_date=? and start_time=?";     
        String queryString = "insert into appointment "
                  +" (appointment_date,provider_no,start_time,end_time,name,reason,location,status,type,notes,resources ,creator,createdatetime) values  "
                  +" ('"+appointment_date+"',"+provider_no+",'"+start_time+"','"+end_time+"','"+name+"','"+reason+"','"+location+"','"+status+"','"+type+"','"+notes+"','"+resources+"',"+creator+",'"+createdatetime+"' )";
                      
        

//if action is good, then give me the result
 
   if (beanDBConnect.executeUpdate(queryString)) {

%>
<br>
<br>
Successful insert
<br>
an appointment Record.
<%  
  } else {
%>
<br>
<br>
Sorry, insert
<br>
has failed.
<%  
  }

// set the switch on
    beanSwitchControl.set_AppointmentToday_switch(1);
 

%>
<br>
<br>
<a href="AppointmentToday.jsp?todayString=<%=appointment_date%>">Return</a>
<%


// if user click "Search" button
}else{
     
//  response.sendRedirect("AddAppointmentMonthSearch.jsp?appointment_date="+appointment_date+"&start_time="+start_time+"&end_time="+end_time+"&name="+name+"&back_to=AddAppointmentMonthForm2");

 
    String gotoJsp = "AddAppointmentMonthSearch.jsp?name="+name+"&appointment_date="+appointment_date+"&start_time="+start_time+"&end_time="+end_time;
           gotoJsp = beanFunctionGenerator.getHttpHeaderString(gotoJsp);    
 
    response.sendRedirect(gotoJsp);   
}
%>
</body>
</html>