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
<%@ page errorPage="ErrorPage.jsp"%>
<%@ page import="bean.*"%>

<jsp:useBean id="beanAboutDate" scope="session" class="bean.AboutDate" />

<HTML>
<!--Copyright (c) http://oscar.mcmaster.ca:8888/oscartest/copyright -->
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<TITLE>PreviousMonth</TITLE>
<meta content="no-cache" />
</HEAD>

<%
//  System.out.print("PreviousMonth--------------");

// the only parameters are start_date 
 
  String start_date = request.getParameter("start_date");

//***********************************************************
 
    start_date = beanAboutDate.getPreviousMonth_start_date(start_date);


    int in_year, in_month, in_date ;
      in_year = new Integer(start_date.substring(0,4)).intValue();
      in_month = new Integer(start_date.substring(5,7)).intValue();
      in_date = new Integer(start_date.substring(8)).intValue();
      
    GregorianCalendar now=new GregorianCalendar(in_year, in_month-1, in_date);

    int maxIndex=now.getActualMaximum(Calendar.DAY_OF_MONTH); 
 
  String end_date = start_date.substring(0,8) + new Integer(maxIndex).toString();
 
//System.out.print("end_date = new Integer(maxIndex).intValue(----------"+end_date);
   response.sendRedirect("AppointmentMonth.jsp?start_date="+start_date+"&end_date="+end_date); 

%>

</HTML>