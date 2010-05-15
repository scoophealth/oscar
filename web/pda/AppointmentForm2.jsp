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


<%@ page errorPage="ErrorPage.jsp"%>
<%@ page import="java.sql.*"%>
<%@ page import="bean.*"%>

<jsp:useBean id="beanDBConnect" scope="session" class="bean.DBConnect" />
<jsp:useBean id="beanDBQuery" scope="session" class="bean.DBQuery" />
<jsp:useBean id="beanReturnPage" scope="session" class="bean.ReturnPage" />

<%

  if(session.getValue("user") == null)    response.sendRedirect("logout.jsp");

  int appointment_no = new Integer( request.getParameter("appointment_no").trim() ).intValue();
  String appointment_date = request.getParameter("appointment_date").trim();
  String start_time = request.getParameter("start_time").trim();
  String end_time = request.getParameter("end_time").trim();
  String name = request.getParameter("name").trim();
//*****************************************************************

 int provider_no = new Integer((String) session.getAttribute("user")).intValue();


  int demographic_no =0 ;
     if(request.getParameter("demographic_no")!=null){
        demographic_no = new Integer(request.getParameter("demographic_no").trim()).intValue();
     }
 
    
     ResultSet RS = beanDBQuery.getAppointmentDetail(appointment_no);
     RS.next();
%>


<html>
<!--Copyright (c) http://oscar.mcmaster.ca:8888/oscartest/copyright -->
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>AppointmentForm2</title>
</head>
<meta http-equiv="Cache-Control" content="no-cache">

<body>

<FORM NAME="EDITAPPT" METHOD="post" ACTION="EditAppointment.jsp">
<input type="hidden" name="DayString"
	value="<%=RS.getString("appointment_date")%>"> <input
	type="hidden" name="appointment_no" value="<%=appointment_no%>">
<table border="0" cellpadding="0" cellspacing="0" width="42%">
	<tr>
		<td><input type="submit" name="submitButton" value="Update"></td>
		<td><a
			href="Demographic_AppointmentForm.jsp?appointment_no=<%=appointment_no%>&demographic_no=<%=demographic_no%>">&nbsp;M&nbsp;</a>
		<a href="DeleteAppointment.jsp?appointment_no=<%=appointment_no%>">Del</a>
		<a href="GoAppointmentMonth.jsp">Month</a> <a
			href="AppointmentToday.jsp?todayString=<%=RS.getString("appointment_date")%>">Return</a>
		</td>
	</tr>

	<tr>
		<td>Date :</td>
		<td><input type="text" name="appointment_date"
			value="<%=appointment_date%>"></td>
	</tr>

	<tr>
		<td>Start Time :</td>
		<td><input type="text" name="start_time" value="<%=start_time%>"></td>
	</tr>
	<tr>
		<td>End Time :</td>
		<td><input type="text" name="end_time" value="<%=end_time%>"></td>
	</tr>

	<tr>
		<td>Name :</td>
		<td><input type="text" name="name" value="<%=name%>"></td>
	</tr>
	<tr>
		<td><input type="submit" name="submitButton" value="Search"></td>
		<td>
		<%
      if(request.getParameter("demographic_no")!=null){
          out.print("<input type=\"text\" name=\"demographic_no\" value=\""+demographic_no+"\">");
      }else{
          out.print("<input type=\"text\" name=\"demographic_no\">");
      }  

%>
		</td>
	</tr>
	<tr>
		<td colspan="2">Reason:<textarea rows="2" cols="25" name="reason"><%=RS.getString("reason")%></textarea></td>
	</tr>
	<tr>
		<td>Location:</td>
		<td><input type="text" name="location"
			value="<%=RS.getString("location")%>"></td>
	</tr>
	<tr>
		<td colspan="2">Notes: <textarea rows="2" cols="25" name="notes"><%=RS.getString("notes")%></textarea></td>
	</tr>
	<tr>
		<td>Last Creator :</td>
		<td><input type="text" name="creator"
			value="<%=beanDBQuery.getProvider(new Integer(RS.getString("provider_no")).intValue())%>"></td>
	</tr>
	<tr>
		<td>Status :</td>
		<td><input type="text" name="status"
			value="<%=RS.getString("status")%>"></td>
	</tr>
	<tr>
		<td>Type :</td>
		<td><input type="text" name="type"
			value="<%=RS.getString("type")%>"></td>
	</tr>
	<tr>
		<td>Chart No. :</td>
		<td><input type="text" name="chartno" value=""></td>
	</tr>
	<tr>
		<td>Resources :</td>
		<td><input type="text" name="resources"
			value="<%=RS.getString("resources")%>"></td>
	</tr>
	<tr>
		<td>Last Time :</td>
		<td><input type="text" name="createdatetime"
			value="<%=RS.getString("createdatetime")%>"></td>
	</tr>

</TABLE>
</FORM>
</body>
</html>


<%
     RS.close();
%>


</TABLE>

</FORM>
</body>
</html>