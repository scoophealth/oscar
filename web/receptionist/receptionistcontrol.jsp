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
<%@ page import="java.util.*" errorPage="errorpage.jsp"%>
<%
  if(session.getValue("user") == null || !((String) session.getValue("userprofession")).equalsIgnoreCase("receptionist"))
    response.sendRedirect("../logout.jsp");

  if(request.getParameter("year")==null && request.getParameter("month")==null && request.getParameter("day")==null && request.getParameter("displaymode")==null && request.getParameter("dboperation")==null) {
    GregorianCalendar now=new GregorianCalendar();
    int nowYear = now.get(Calendar.YEAR);
    int nowMonth = now.get(Calendar.MONTH)+1 ; //be care for the month +-1
    int nowDay = now.get(Calendar.DAY_OF_MONTH);
    response.sendRedirect("./receptionistcontrol.jsp?year="+nowYear+"&month="+(nowMonth)+"&day="+(nowDay)+"&view=0&displaymode=day&dboperation=searchappointmentday");
    return;
  }
%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%@ include file="../admin/dbconnection.jsp"%>

<%
String [][] dbOperation;
if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){
  dbOperation=new String[][] {
   {"search_tickler","select * from tickler where demographic_no=? and service_date<=? and status='A' order by service_date desc"},
  {"searchappointmentday", "select appointment_no, provider_no, start_time,end_time,name,demographic_no,reason,notes,status from appointment where provider_no=? and appointment_date=? order by start_time, status desc "}, 
    {"searchmygroupcount", "select count(provider_no) from mygroup where mygroup_no=? "}, 
    {"searchmygroupsubcount", "select count(provider_no) from mygroup where mygroup_no=? and vieworder like ?"}, 
    {"searchmygroupprovider", "select provider_no, last_name, first_name, vieworder from mygroup where mygroup_no=? order by vieworder, first_name"}, 
    {"searchmygroupsubprovider", "select provider_no, last_name, first_name from mygroup where mygroup_no=? and vieworder like ? order by vieworder, first_name"}, 
    {"searchmygroupall", "select * from mygroup order by mygroup_no"}, 
//    {"searchmygroupsub", "select * from mygroup order by ? and vieworder like ?"}, 
    {"searchmygroupno", "select mygroup_no from mygroup group by mygroup_no order by mygroup_no"}, 
    {"upgradegroupmember", "update mygroup set vieworder = ? where mygroup_no=? and provider_no=?"}, 
    {"deletegroupmember", "delete from mygroup where mygroup_no=? and provider_no=?"}, 
    {"savemygroup", "insert into mygroup (mygroup_no,provider_no,last_name,first_name) values(?,?,?,?)" },
    {"searchprovider", "select provider_no, last_name, first_name from provider where provider_type='doctor' and status='1' order by last_name"}, 
    {"searchallprovider", "select * from provider where status='1' order by last_name"}, 
    {"updateapptstatus", "update appointment set status=? where appointment_no=? "},
    {"updatepreference", "update preference set start_hour=?, end_hour=?, every_min=?, mygroup_no=?, color_template=?,new_tickler_warning_window=? where provider_no=? "},
    {"add_preference", "insert into preference (provider_no, start_hour, end_hour, every_min, mygroup_no, color_template, new_tickler_warning_window) values (?, ?, ?, ?, ?, ?, ?)"},

    {"search_scheduleholiday", "select * from scheduleholiday where sdate > ?" }, 
    {"search_scheduledate_datep", "select * from scheduledate where sdate between ? and ? order by sdate, reason" }, 
    {"search_scheduledate_singlep", "select * from scheduledate where sdate between ? and ? and provider_no=? order by sdate" }, 
    {"search_scheduledate_single", "select * from scheduledate where sdate=? and provider_no=?" }, 

    {"search_appttimecode", "select scheduledate.provider_no, scheduletemplate.timecode, scheduledate.sdate from scheduletemplate, scheduledate where scheduletemplate.name=scheduledate.hour and scheduledate.sdate=? and  scheduledate.provider_no=? and (scheduletemplate.provider_no=scheduledate.provider_no or scheduletemplate.provider_no='Public') order by scheduledate.sdate"}, 
    {"search_timecode", "select * from scheduletemplatecode order by code"}, 
    {"search_resource_baseurl", "select * from property where name = ?"}, 

    {"search_numgrpscheduledate", "select count(scheduledate.provider_no) from mygroup, scheduledate where mygroup_no = ? and scheduledate.sdate=? and mygroup.provider_no=scheduledate.provider_no and scheduledate.available = '1' "}, 
  };
}else{
  dbOperation=new String[][] {
   {"search_tickler","select * from tickler where demographic_no=? and service_date<=? and status='A' order by service_date desc"},
  {"searchappointmentday", "select appointment_no, provider_no, start_time,end_time,name,demographic_no,reason,notes,status from appointment where provider_no=? and appointment_date=? order by start_time, status desc "}, 
    {"searchmygroupcount", "select count(provider_no) from mygroup where mygroup_no=? "}, 
    {"searchmygroupsubcount", "select count(provider_no) from mygroup where mygroup_no=? and vieworder like ?"}, 
    {"searchmygroupprovider", "select provider_no, last_name, first_name, vieworder from mygroup where mygroup_no=? order by vieworder, first_name"}, 
    {"searchmygroupsubprovider", "select provider_no, last_name, first_name from mygroup where mygroup_no=? and vieworder like ? order by vieworder, first_name"}, 
    {"searchmygroupall", "select * from mygroup order by mygroup_no"}, 
//    {"searchmygroupsub", "select * from mygroup order by ? and vieworder like ?"}, 
    {"searchmygroupno", "select mygroup_no from mygroup group by mygroup_no order by mygroup_no"}, 
    {"upgradegroupmember", "update mygroup set vieworder = ? where mygroup_no=? and provider_no=?"}, 
    {"deletegroupmember", "delete from mygroup where mygroup_no=? and provider_no=?"}, 
    {"savemygroup", "insert into mygroup (mygroup_no,provider_no,last_name,first_name) values(?,?,?,?)" },
    {"searchprovider", "select provider_no, last_name, first_name from provider where provider_type='doctor' and status='1' order by last_name"}, 
    {"searchallprovider", "select * from provider where status='1' order by last_name"}, 
    {"updateapptstatus", "update appointment set status=? where appointment_no=? "},
    {"updatepreference", "update preference set start_hour=?, end_hour=?, every_min=?, mygroup_no=?, color_template=? where provider_no=? "},
    {"add_preference", "insert into preference (provider_no, start_hour, end_hour, every_min, mygroup_no, color_template) values (?, ?, ?, ?, ?, ?)"},

    {"search_scheduleholiday", "select * from scheduleholiday where sdate > ?" }, 
    {"search_scheduledate_datep", "select * from scheduledate where sdate between ? and ? order by sdate, reason" }, 
    {"search_scheduledate_singlep", "select * from scheduledate where sdate between ? and ? and provider_no=? order by sdate" }, 
    {"search_scheduledate_single", "select * from scheduledate where sdate=? and provider_no=?" }, 

    {"search_appttimecode", "select scheduledate.provider_no, scheduletemplate.timecode, scheduledate.sdate from scheduletemplate, scheduledate where scheduletemplate.name=scheduledate.hour and scheduledate.sdate=? and  scheduledate.provider_no=? and (scheduletemplate.provider_no=scheduledate.provider_no or scheduletemplate.provider_no='Public') order by scheduledate.sdate"}, 
    {"search_timecode", "select * from scheduletemplatecode order by code"}, 
    {"search_resource_baseurl", "select * from property where name = ?"}, 

    {"search_numgrpscheduledate", "select count(scheduledate.provider_no) from mygroup, scheduledate where mygroup_no = ? and scheduledate.sdate=? and mygroup.provider_no=scheduledate.provider_no and scheduledate.available = '1' "}, 
  };
}
  //associate each operation with an output JSP file -- displaymode
  String[][] toFile=new String[][] {
    {"day" , "appointmentreceptionistadminday.jsp"},
    {"month" , "appointmentreceptionistadminmonth.jsp"},
    {"addstatus" , "receptionistaddstatus.jsp"},
    {"updatepreference" , "receptionistupdatepreference.jsp"},
    {"displaymygroup" , "receptionistdisplaymygroup.jsp"},
    {"newgroup" , "receptionistnewgroup.jsp"},
    {"savemygroup" , "receptionistsavemygroup.jsp"},
  };
  apptMainBean.doConfigure(dbParams,dbOperation,toFile);
  apptMainBean.doCommand(request); //store request to a help class object Dict - function&params
  if(true) {
    out.clear();
    pageContext.forward(apptMainBean.whereTo()); //forward request&response to the target page
    return;
  }
%>
