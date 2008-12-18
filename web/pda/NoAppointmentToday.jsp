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
<%@ page import="java.util.*"%>
<jsp:useBean id="beanDBConnect" scope="session" class="bean.DBConnect" />
<jsp:useBean id="beanDBQuery" scope="session" class="bean.DBQuery" />
<jsp:useBean id="beanReturnPage" scope="session" class="bean.ReturnPage" />
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<jsp:useBean id="beanSwitchControl" scope="session"
	class="bean.SwitchControl" />
<jsp:useBean id="beanAboutDate" scope="session" class="bean.AboutDate" />

<HTML>
<!--Copyright (c) http://oscar.mcmaster.ca:8888/oscartest/copyright -->
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<meta http-equiv="Cache-Control" content="no-cache" />
<TITLE>NoAppointmentToday</TITLE>
</HEAD>
<BODY>
<%  

// need only the todayString 

    String todayString = request.getParameter("todayString").trim();
//*********************************************************************************

  int provider_no = new Integer((String) session.getAttribute("user")).intValue();

  GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);

  String curMonthString = curMonth>9?(new Integer(curMonth).toString()):"0"+(new Integer(curMonth).toString());

%>
<table border="0">
	<tr>
		<form method="post" action="GoAppointmentToday.jsp">
		<td colspan="2"><a href="Search.jsp">Search</a>&nbsp;&nbsp; <INPUT
			TYPE="submit" NAME="submit" VALUE="GoTo">&nbsp;&nbsp; <INPUT
			TYPE="text" NAME="year" VALUE="<%=curYear%>" WIDTH="4" HEIGHT="10"
			border="0" size="4" maxlength="4">- <INPUT TYPE="text"
			NAME="month" VALUE="<%=curMonthString%>" WIDTH="2" HEIGHT="10"
			border="0" size="2" maxlength="2">- <INPUT TYPE="text"
			NAME="day" VALUE="<%=curDay%>" WIDTH="2" HEIGHT="10" border="0"
			size="2" maxlength="2"> <INPUT TYPE="hidden"
			NAME="displaymode" VALUE="day"> <INPUT TYPE="hidden"
			NAME="dboperation" VALUE="searchappointmentday"></td>
		</form>
	</tr>
	<%
	out.print("<tr><td>Date:"+todayString+"</td><td><a href=\"PreviousDay.jsp?todayString="+todayString+"\"><<</a>&nbsp;&nbsp;<a href=\"NextDay.jsp?todayString="+todayString+"\">>></a>&nbsp;&nbsp;<a href=\"GoAppointmentMonth.jsp\">Month</a>&nbsp;&nbsp;<a href=\"logout.jsp\">LogOut</a></td><td></td></tr>");
%>
</table>
<table border="0">
	<% 	    
        
//prepare for the time table (00:15 step)

	  int iCols=0, iRows=0, iS=0,iE=0; //for each starting hour, how many events
          int startHour=8, endHour=18, hourCursor=0 ,hourCursorN=0;
          String am_pm=null;
          boolean bFirstTimeRs=true;

    			String[] param =new String[2];
  				param[0]=new Integer(provider_no).toString();
	 				param[1]=curYear+"-"+curMonth+"-"+curDay;//e.g."2001-02-12";

// am :pm changing  
          for(int ih=startHour; ih<=endHour; ih++) {
            hourCursorN = ih;  // this is only for compare time value, not for display
            hourCursor =  ih>12?ih-12:ih;
            am_pm = ih<12?"am":"pm";

            String hourCursorNString = ih>9?new Integer(ih).toString():"0"+new Integer(ih).toString();

                 String minute[] = {":00",":15",":30",":45"};
                 int flag = 0;
                 for (int i=0; i<4; i++){               

%>
	<tr>
		<td><a
			href="AddAppointmentForm.jsp?appointment_date=<%=todayString%>&start_time=<%=hourCursorNString+minute[i]+":00"%>">
		<%=(hourCursor+minute[i]+am_pm)%> &nbsp;</a></td>
		<%           
                      out.println("<td>");

                      out.println("</td></tr>");

                 }
          }
//    			apptMainBean.closePstmtConn();

// set back the switch
    beanSwitchControl.set_AppointmentToday_switch(0);

 
%>
	
</table>
</body>
</HTML>
