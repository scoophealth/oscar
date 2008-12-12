<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_appointment"
	rights="r" reverse="<%=true%>">
	<%response.sendRedirect("../logout.jsp");%>
</security:oscarSec>
<%--
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
--%>
<%@ page errorPage="errorpage.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%@ include file="../admin/dbconnection.jsp"%>
<%
  //operation available to the client -- dboperation
  String [][] dbOperation=new String[][] {
    {"add_apptrecord", "insert into appointment (provider_no,appointment_date,start_time,end_time,name, notes,reason,location,resources,type, style,billing,status,createdatetime,creator, remarks, demographic_no) values(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?)" },
    {"search", "select * from appointment where appointment_no=?"},
    //{"update_apptrecord", "update appointment set demographic_no=?,appointment_date=?,start_time=?,end_time=?,name=?, notes=?,reason =?,location=?, resources=?, type=?,style=?,billing =?,status=?,createdatetime=?,creator=?,remarks=? where appointment_no=? "},
    {"update_apptrecord", "update appointment set demographic_no=?,appointment_date=?,start_time=?,end_time=?,name=?, notes=?,reason =?,location=?, resources=?, type=?,style=?,billing =?,status=?,updatedatetime=?,creator=?,remarks=? where appointment_no=? "},
    {"delete", "delete from appointment where appointment_no=?"},
    //{"cancel", "update appointment set status='C' where appointment_no=?"},
    //{"updatestatusc", "update appointment set status=?, creator=?, createdatetime=? where appointment_no=?"},
    {"updatestatusc", "update appointment set status=?, creator=?, updatedatetime=? where appointment_no=?"},
    {"updatestatus", "update appointment set status=? where appointment_no=?"},
    {"searchappointmentday", "select start_time,end_time,name,demographic_no,reason,notes,status from appointment where provider_no=? and appointment_date=? order by start_time"},
    {"searchapptweek", "select appointment_date, start_time,end_time, name from appointment where provider_no=? and appointment_date between ? and ? order by appointment_date,start_time"},
    {"searchappointmentmonth", "select start_time,end_time, name from appointment where provider_no=? and appointment_date between ? and ? order by appointment_date,start_time"},
    {"searchprovidername", "select last_name, first_name from provider where provider_no=?"},
    {"search_detail", "select * from demographic where demographic_no=?"},
    {"search_demographiccust_alert", "select cust3 from demographiccust where demographic_no = ? " },
    {"search_waitinglist", "select wl.listID, wln.name from waitingList wl, waitingListName wln where wl.demographic_no=? and wln.ID=wl.listID and wl.is_history ='N' order by wl.listID"},
  };

   //associate each operation with an output JSP file -- displaymode
   String[][] toFile=new String[][] {
     {"Add Appointment" , "appointmentaddarecord.jsp"},
     {"Group Appt" , "appointmentgrouprecords.jsp"},
     {"Group Action" ,  "appointmentgrouprecords.jsp"},            //"appointmentgroupaction.jsp"},
     {"Add Appt & PrintPreview" , "appointmentaddrecordprint.jsp"},
     {"TicklerSearch" , "../tickler/ticklerAdd.jsp"},
     {"Search " , "../demographic/demographiccontrol.jsp"},
     {"Search" , "appointmentsearchrecords.jsp"},
     {"edit" , "editappointment.jsp"},
     {"Update Appt" , "appointmentupdatearecord.jsp"},
     {"Delete Appt" , "appointmentdeletearecord.jsp"},
     {"Cut" , "appointmentcutrecord.jsp"},
     {"Copy" , "appointmentcopyrecord.jsp"},
   };
   apptMainBean.doConfigure(dbParams,dbOperation,toFile);
%>
<%
  apptMainBean.doCommand(request); //store request to a help class object Dict - function&params
  if(true) {
    out.clear();
    pageContext.forward(apptMainBean.whereTo()); //forward request&response to the target page
    return;
  }
%>
