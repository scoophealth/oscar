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

<%@ page import="java.sql.*, java.util.*, oscar.MyDateFormat,org.oscarehr.event.EventService"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@page import="org.oscarehr.common.dao.AppointmentArchiveDao" %>
<%@page import="org.oscarehr.common.dao.OscarAppointmentDao" %>
<%@page import="org.oscarehr.common.model.Appointment" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%
	AppointmentArchiveDao appointmentArchiveDao = (AppointmentArchiveDao)SpringUtils.getBean("appointmentArchiveDao");
	OscarAppointmentDao appointmentDao = (OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");
%>
<%
  //if action is good, then give me the result
    String[] param =new String[3];
    param[0]=request.getParameter("status")+ request.getParameter("statusch");
    param[1]=(String)session.getAttribute("user");
    param[2]=request.getParameter("appointment_no");
    Appointment appt = appointmentDao.find(Integer.parseInt(request.getParameter("appointment_no")));
    appointmentArchiveDao.archiveAppointment(appt);
    int rowsAffected=0;
    if(appt != null) {
  	  appt.setStatus(request.getParameter("status") + request.getParameter("statusch"));
  	  appt.setLastUpdateUser((String)session.getAttribute("user"));
  	  appointmentDao.merge(appt);
  	  rowsAffected=1;
    }
    
    EventService eventService = SpringUtils.getBean(EventService.class);//This is when the icon is clicked in the appt screen
	eventService.appointmentStatusChanged(this,request.getParameter("appointment_no"), request.getParameter("provider_no"), request.getParameter("statusch"));
    
    if (rowsAffected == 1) {//add_record
      int view=0;
      if(request.getParameter("view")!=null) view=Integer.parseInt(request.getParameter("view")); //0-multiple views, 1-single view
      String strView=(view==0)?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") );
      String strViewAll=request.getParameter("viewall")==null?"0":(request.getParameter("viewall")) ;
      String displaypage="providercontrol.jsp?year="+request.getParameter("year")+"&month="+request.getParameter("month")+"&day="+request.getParameter("day") +"&view="+  strView  +"&displaymode=day&dboperation=searchappointmentday" +"&viewall=" +strViewAll+"&x="+request.getParameter("x")+"&y="+request.getParameter("y");
    if (request.getParameter("viewWeek") != null) {
       displaypage += "&provider_no=" + request.getParameter("provider_no");
    }
    if(true) {
      out.clear();
    	response.sendRedirect(displaypage);
      //pageContext.forward(displaypage); //forward request&response to the target page
      return;
    }
  } else {
%>
<p>
<h1><bean:message key="AddProviderStatus.msgAddFailure" /></h1>

<%
  }
%>
