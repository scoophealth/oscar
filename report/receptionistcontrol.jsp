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

<%
  if(session.getValue("user") == null || !((String) session.getValue("userprofession")).equalsIgnoreCase("receptionist"))
    response.sendRedirect("../logout.jsp");
  //instatiate/configure the main bean, forward the request to the output file
%>

<%@ page errorPage="../errorpage.jsp" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />
<%@ include file="../admin/dbconnection.jsp" %>  

<%
  //operation available to the client -- dboperation
  String [][] dbQueries=new String[][] {
    {"searchappointmentday", "select appointment_no, provider_no, start_time,end_time,name,demographic_no,reason,notes,status from appointment where provider_no=? and appointment_date=? order by start_time, status desc "}, 
    {"searchmygroupcount", "select count(provider_no) from mygroup where mygroup_no=? "}, 
    {"searchmygroupprovider", "select provider_no, last_name, first_name from mygroup where mygroup_no=? order by first_name"}, 
    {"searchmygroupall", "select * from mygroup order by ?"}, 
    {"searchmygroupno", "select * from mygroup group by mygroup_no order by ?"}, 
    {"deletegroupmember", "delete from mygroup where mygroup_no=? and provider_no=?"}, 
    {"savemygroup", "insert into mygroup (mygroup_no,provider_no,last_name,first_name) values(?,?,?,?)" },
    {"searchprovider", "select provider_no, last_name, first_name from provider where provider_type='doctor' order by ?"}, 
    {"searchallprovider", "select * from provider order by ?"}, 
    {"updateapptstatus", "update appointment set status=? where appointment_no=? "},
    {"updatepreference", "update preference set start_hour=?, end_hour=?, every_min=?, mygroup_no=?, color_template=? where provider_no=? "},
    {"add_preference", "insert into preference values ('\\N',?, ?, ?, ?, ?, ?)"},

    {"search_scheduleholiday", "select * from scheduleholiday where sdate > ?" }, 
    {"search_scheduledate_datep", "select * from scheduledate where sdate between ? and ? order by sdate, reason" }, 
    {"search_scheduledate_singlep", "select * from scheduledate where sdate between ? and ? and provider_no=? order by sdate" }, 
    {"search_scheduledate_single", "select * from scheduledate where sdate=? and provider_no=?" }, 

    {"search_appttimecode", "select scheduledate.provider_no, scheduletemplate.timecode, scheduledate.sdate from scheduletemplate, scheduledate where scheduletemplate.name=scheduledate.hour and scheduledate.sdate=? and  scheduledate.provider_no=? and (scheduletemplate.provider_no=scheduledate.provider_no or scheduletemplate.provider_no='Public') order by scheduledate.sdate"}, 
    {"search_timecode", "select * from scheduletemplatecode order by ?"}, 
    {"search_resource_baseurl", "select * from property where name = ?"}, 
  };
   
  //associate each operation with an output JSP file -- displaymode
  String[][] responseTargets=new String[][] {
    {"day" , "appointmentreceptionistadminday.jsp"},
    {"month" , "appointmentreceptionistadminmonth.jsp"},
    {"addstatus" , "receptionistaddstatus.jsp"},
    {"updatepreference" , "receptionistupdatepreference.jsp"},
    {"displaymygroup" , "receptionistdisplaymygroup.jsp"},
    {"newgroup" , "receptionistnewgroup.jsp"},
    {"savemygroup" , "receptionistsavemygroup.jsp"},
  };
  apptMainBean.doConfigure(dbParams,dbQueries,responseTargets);
  apptMainBean.doCommand(request); //store request to a help class object Dict - function&params
  if(true) {
    out.clear();
    pageContext.forward(apptMainBean.whereTo()); //forward request&response to the target page
    return;
  }
%>
