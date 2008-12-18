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
  if(session.getValue("user") == null)    response.sendRedirect("logout.jsp");

    int appointment_no = new Integer(request.getParameter("appointment_no")).intValue();
    
    String appointment_date = new String(request.getParameter("appointment_date").trim());
    String start_time = new String(request.getParameter("start_time").trim());
    String end_time = new String(request.getParameter("end_time").trim());
    String name = new String(request.getParameter("name").trim());
    String reason = new String(request.getParameter("reason").trim());
    String location = new String(request.getParameter("location").trim());
    
%>
<table border="1">
	<%
 	    out.print("<tr><td>Date:"+appointment_date+"</td><td><a href=\"EditAppointmentForm.jsp?appointment_no="+appointment_no+"\">Edit</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"javascript:history.back()\">Return</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"logout.jsp\"<b>LogOut</b></a></td><td></td></tr>") ;
 
            String str = beanReturnPage.getAppointmentHtml(appointment_date,start_time,end_time,name,reason,location ) ;
            out.print( str);
 	    
%>
</table>
</body>
</HTML>
