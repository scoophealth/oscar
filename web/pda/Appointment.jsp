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
<%@ page import="bean.*"%>

<jsp:useBean id="beanDBConnect" scope="session" class="bean.DBConnect" />
<jsp:useBean id="beanDBQuery" scope="session" class="bean.DBQuery" />
<jsp:useBean id="beanReturnPage" scope="session" class="bean.ReturnPage" />

<HTML>
<!--Copyright (c) http://oscar.mcmaster.ca:8888/oscartest/copyright -->
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<meta http-equiv="Cache-Control" content="no-cache" />
<TITLE>Appointment</TITLE>
</HEAD>
<BODY>

<%  

    ResultSet RS = null;
    int appointment_no = 1;
    int apt_no = new Integer(request.getParameter("apt_no")).intValue();
    int provider_no = new Integer(request.getParameter("provider_no")).intValue();

    if (apt_no==1){
       appointment_no = new Integer(request.getParameter("appointment_no")).intValue();
    } 
    
    String appointment_date = new String(request.getParameter("appointment_date").trim());
    
%>
<table border="1">

	<%
 
// if only have one appointment, then display it

   if (apt_no==1){


      RS = beanDBQuery.getAppointmentSubDetail(provider_no,appointment_date);
          
	    while (RS.next()){
             String start_time = RS.getString("start_time");
             String end_time = RS.getString("end_time");
             String name = RS.getString("name");
             String reason = RS.getString("reason");
             String location = RS.getString("location");

 	    out.print("<tr><td>Date:"+appointment_date+"</td><td><a href=\"EditAppointmentForm.jsp?appointment_no="+appointment_no+"\">Edit</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"javascript:history.back()\">Return</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"logout.jsp\"<b>LogOut</b></a></td><td></td></tr>") ;

            String str = beanReturnPage.getAppointmentHtml(appointment_date,start_time,end_time,name,reason,location ) ;
            out.print( str);

	    }
	    RS.close();

// if have more than one appointment, then display birf message for user to choone 
	    
    } else{
	out.print("<tr><td>Date:"+appointment_date+"</td><td>&nbsp;&nbsp;&nbsp;Next</td><td></td></tr>");

      RS = beanDBQuery.getAppointmentSubDetail(provider_no,appointment_date);
          
	    while (RS.next()){
             int appointment_no_1 = new Integer(RS.getString("appointment_no")).intValue();
             String start_time = RS.getString("start_time");
             String end_time = RS.getString("end_time");
             String name = RS.getString("name");
             String reason = RS.getString("reason");
             String location = RS.getString("location");
 
 	     out.print("<tr><td><a href=\"AppointmentDetail.jsp?appointment_date="+appointment_date+"&appointment_no="+appointment_no_1+"&start_time="+start_time+"&end_time="+end_time+"&name="+name+"&reason="+reason+"&location="+location+"\" >");
               out.print(start_time);
	       out.print("</a>");
	       out.print("</td><td>");
  	     out.print("<a href=\"EditAppointmentForm.jsp?appointment_no="+appointment_no_1+"\" >");
               out.print(name);
	       out.print("</a>");
	     out.print("</td></tr>");
            
	    }
	    RS.close(); 	
 	    
    }
 	    
%>

</table>
</body>
</HTML>
