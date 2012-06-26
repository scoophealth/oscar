<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>
<%@ page import="java.sql.*, java.util.*, oscar.*, oscar.util.*, org.oscarehr.common.OtherIdManager"%>
<%@ page import="org.oscarehr.event.EventService"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ include file="/common/webAppContextAndSuperMgr.jsp"%>
<%@page import="org.oscarehr.common.dao.AppointmentArchiveDao" %>
<%@page import="org.oscarehr.common.dao.OscarAppointmentDao" %>
<%@page import="org.oscarehr.common.model.Appointment" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%
	AppointmentArchiveDao appointmentArchiveDao = (AppointmentArchiveDao)SpringUtils.getBean("appointmentArchiveDao");
	OscarAppointmentDao appointmentDao = (OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");
    String changedStatus = null;
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
</head>

<body background="../images/gray_bg.jpg" bgproperties="fixed">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="90%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF">
		<bean:message key="appointment.appointmentupdatearecord.msgMainLabel" /></font></th>
	</tr>
</table>
<%
  String updateuser = (String) session.getAttribute("user");

  int rowsAffected = 0;
  Appointment appt = appointmentDao.find(Integer.parseInt(request.getParameter("appointment_no")));
  appointmentArchiveDao.archiveAppointment(appt);
  
  //Did the appt status change ?
  if(!appt.getStatus().equals(request.getParameter("status"))){
	  changedStatus = request.getParameter("status");
  }
  
  if (request.getParameter("buttoncancel")!=null && (request.getParameter("buttoncancel").equals("Cancel Appt") || request.getParameter("buttoncancel").equals("No Show"))) {
	  String[] param = new String[3];
	  changedStatus = request.getParameter("buttoncancel").equals("Cancel Appt")?"C":"N";
	  param[0]=request.getParameter("buttoncancel").equals("Cancel Appt")?"C":"N";
	  param[1]=updateuser;  //request.getParameter("creator");
	  param[2]=request.getParameter("appointment_no");
	  rowsAffected = oscarSuperManager.update("appointmentDao", "updatestatusc", param);

  } else {
	  String[] param = new String[17];
	  if (request.getParameter("demographic_no")!=null && !(request.getParameter("demographic_no").equals(""))) {
		  param[0] = request.getParameter("demographic_no");
	  } else param[0]="0";
	  param[1]=request.getParameter("appointment_date");
	  param[2]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("start_time"));
	  param[3]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("end_time"));
	  param[4]=request.getParameter("keyword");
	  param[5]=request.getParameter("notes");
	  param[6]=request.getParameter("reason");
	  param[7]=request.getParameter("location");
	  param[8]=request.getParameter("resources");
	  param[9]=request.getParameter("type");
	  param[10]=request.getParameter("style");
	  param[11]=request.getParameter("billing");
	  param[12]=request.getParameter("status");
	  param[13]=updateuser;  //request.getParameter("creator");
	  param[14]=request.getParameter("remarks");
	  param[15]=(request.getParameter("urgency")!=null)?request.getParameter("urgency"):"";
	  param[16]=request.getParameter("appointment_no");
	  rowsAffected = oscarSuperManager.update("appointmentDao", request.getParameter("dboperation"), param);
  }
  if (rowsAffected == 1) {
%>
<p>
<h1><bean:message
	key="appointment.appointmentupdatearecord.msgUpdateSuccess" /></h1>

<script LANGUAGE="JavaScript">
	self.opener.refresh();
	self.close();
</script>
<%
	String apptNo = request.getParameter("appointment_no");
	String mcNumber = request.getParameter("appt_mc_number");
	OtherIdManager.saveIdAppointment(apptNo, "appt_mc_number", mcNumber);
	
	if(changedStatus != null){
		EventService eventService = SpringUtils.getBean(EventService.class); //updating an appt from the appt update screen delete doesn't work
		eventService.appointmentStatusChanged(this,apptNo.toString(), appt.getProviderNo(), changedStatus);
	}
	// End External Prescriber 
  } else {
%>
<p>
<h1><bean:message
	key="appointment.appointmentupdatearecord.msgUpdateFailure" /></h1>

<%
  }
%>
<p></p>
<hr width="90%"/>
<form>
<input type="button" value="<bean:message key="global.btnClose"/>" onClick="closeit()">
</form>
</center>
</body>
</html:html>
