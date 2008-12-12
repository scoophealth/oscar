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
<%@ page import="java.util.*"%>
<%@ page import="bean.*"%>
<jsp:useBean id="beanSwitchControl" scope="session"
	class="bean.SwitchControl" />
<jsp:useBean id="beanAboutDate" scope="session" class="bean.AboutDate" />
<%
// the only parameters are start_date 
 
   String todayString = request.getParameter("todayString");

//***********************************************************
 
    int in_year, in_month, in_date ;
 
     in_year = new Integer(todayString.substring(0,4)).intValue();
     in_month = new Integer(todayString.substring(5,7)).intValue();
     in_date = new Integer(todayString.substring(8)).intValue();

   
    GregorianCalendar now=new GregorianCalendar(in_year, in_month-1, in_date);
 
    todayString = beanAboutDate.getNextDay(in_year, in_month, in_date);

  
    response.sendRedirect("AppointmentToday.jsp?todayString="+todayString); 

%>