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

<%
  
  String user_name = (String) session.getAttribute("userlastname")+","+ (String) session.getAttribute("userfirstname");
  String provider_no = request.getParameter("provider_no");
%>
<%@ page
	import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*"
	errorPage="../appointment/errorpage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<jsp:useBean id="scheduleMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<jsp:useBean id="scheduleDateBean" class="java.util.Hashtable"
	scope="session" />
<jsp:useBean id="scheduleRscheduleBean" class="oscar.RscheduleBean"
	scope="session" />
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="schedule.scheduledatesave.title" /></title>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">
</head>
<%
  String available = request.getParameter("available");
  String priority = "c";
  String reason = request.getParameter("reason");
  String hour = request.getParameter("hour");
  //save the record first, change holidaybean next
  int rowsAffected = 0;
  String[] param1 =new String[2];
  param1[0]=request.getParameter("date");
  param1[1]=provider_no;
  rowsAffected = scheduleMainBean.queryExecuteUpdate(param1,"delete_scheduledate");
  //add R schedule date if it is available
  if(request.getParameter("Submit")!=null && request.getParameter("Submit").equals(" Delete ")) {
    if(scheduleRscheduleBean.getDateAvail(request.getParameter("date"))) {
      String[] param3 =new String[8];
      param3[0]=request.getParameter("date");
      param3[1]=provider_no;
      param3[2]="1";
      param3[3]="b";
      param3[4]="";
      param3[5]=scheduleRscheduleBean.getDateAvailHour(request.getParameter("date"));
      param3[6]=user_name;      
      param3[7]=scheduleRscheduleBean.active;
      rowsAffected = scheduleMainBean.queryExecuteUpdate(param3,"add_scheduledate");
    }
  }
  scheduleDateBean.remove(request.getParameter("date") );
  
if(request.getParameter("Submit")!=null && request.getParameter("Submit").equals(" Save ")) {
  String[] param2 =new String[8];
  param2[0]=request.getParameter("date");
  param2[1]=provider_no;
  param2[2]=available;
  param2[3]=priority;
  param2[4]=reason;
  param2[5]=hour;
  param2[6]=user_name;
  param2[7]=scheduleRscheduleBean.active;
  rowsAffected = scheduleMainBean.queryExecuteUpdate(param2,"add_scheduledate");
  
  scheduleDateBean.put(request.getParameter("date"), new HScheduleDate(available, priority, reason, hour, user_name) );
}
%>

<script language="JavaScript">
<!--
  opener.location.reload();
  self.close();
//-->
</script>
<body>
</body>
</html:html>
