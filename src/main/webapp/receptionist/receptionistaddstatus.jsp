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

<%@ page import="java.sql.*, java.util.*, oscar.MyDateFormat"
	errorPage="errorpage.jsp"%>
<%@ include file="/common/webAppContextAndSuperMgr.jsp"%>
<%@page import="org.oscarehr.common.dao.AppointmentArchiveDao" %>
<%@page import="org.oscarehr.common.dao.OscarAppointmentDao" %>
<%@page import="org.oscarehr.common.model.Appointment" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%
	AppointmentArchiveDao appointmentArchiveDao = (AppointmentArchiveDao)SpringUtils.getBean("appointmentArchiveDao");
	OscarAppointmentDao appointmentDao = (OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");
%>

<%
  String[] param = new String[3];
  param[0] = request.getParameter("status") + request.getParameter("statusch");
  param[1] = (String)session.getAttribute("user");
  param[2] = request.getParameter("appointment_no");
  Appointment appt = appointmentDao.find(Integer.parseInt(request.getParameter("appointment_no")));
  appointmentArchiveDao.archiveAppointment(appt);
  int rowsAffected = oscarSuperManager.update("receptionistDao", request.getParameter("dboperation"), param);
  if (rowsAffected == 1) {
    int view=0;
    if(request.getParameter("view")!=null) view=Integer.parseInt(request.getParameter("view")); //0-multiple views, 1-single view
    String strView=(view==0)?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") );
    String strViewAll=request.getParameter("viewall")==null?"0":(request.getParameter("viewall")) ;
    String displaypage="receptionistcontrol.jsp?year="+request.getParameter("year")+"&month="+request.getParameter("month")+"&day="+request.getParameter("day") +"&view="+  strView  +"&displaymode=day&dboperation=searchappointmentday" +"&viewall=" +strViewAll;
    if(true) {
      out.clear(); //needed
      response.sendRedirect(displaypage);
      return;
    }
  } else {
%>
<p>
<h1>Sorry, addition has failed.</h1>
</p>
<%
  }
%>
