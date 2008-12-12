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


<%@ page errorPage="errorpage.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%@ include file="../admin/dbconnection.jsp"%>

<%
  //operation available to the client -- dboperation
  //construct SQL expression
  String orderby="", limit="", limit1="", limit2="";
  if(request.getParameter("orderby")!=null) orderby="order by "+request.getParameter("orderby");
  if(request.getParameter("limit1")!=null) limit1=request.getParameter("limit1")+", ";
  if(request.getParameter("limit2")!=null) {
    limit2=request.getParameter("limit2");
    limit="limit "+limit1+limit2;
  }

  String fieldname="", regularexp="like"; // exactly search is not required by users, e.g. regularexp="=";
  if(request.getParameter("search_mode")!=null) {
	  if(request.getParameter("keyword").indexOf("*")!=-1 || request.getParameter("keyword").indexOf("%")!=-1) regularexp="like";
    if(request.getParameter("search_mode").equals("search_address")) fieldname="address";
    if(request.getParameter("search_mode").equals("search_phone")) fieldname="phone";
    if(request.getParameter("search_mode").equals("search_hin")) fieldname="hin";
    if(request.getParameter("search_mode").equals("search_dob")) fieldname="year_of_birth "+regularexp+" ?"+" and month_of_birth "+regularexp+" ?"+" and date_of_birth ";
    if(request.getParameter("search_mode").equals("search_chart_no")) fieldname="chart_no";
    if(request.getParameter("search_mode").equals("search_name")) {
      if(request.getParameter("keyword").indexOf(",")==-1)  fieldname="last_name";
      else if(request.getParameter("keyword").trim().indexOf(",")==(request.getParameter("keyword").trim().length()-1)) fieldname="last_name";
      else fieldname="last_name "+regularexp+" ?"+" and first_name ";
    }
  }
       
  String [][] dbQueries=new String[][] {
    {"search_titlename", "select demographic_no,first_name,last_name,roster_status,sex,chart_no,year_of_birth,month_of_birth,date_of_birth  from demographic where "+fieldname+ " "+regularexp+" ? " +orderby + " "+limit},  
    {"add_apptrecord", "select demographic_no,first_name,last_name,roster_status,sex,chart_no,year_of_birth,month_of_birth,date_of_birth  from demographic where "+fieldname+ " "+regularexp+" ? " +orderby + " "+limit},  
    {"update_apptrecord", "select demographic_no,first_name,last_name,roster_status,sex,chart_no,year_of_birth,month_of_birth,date_of_birth  from demographic where "+fieldname+ " "+regularexp+" ? " +orderby + " "+limit},  
    {"search_detail", "select * from demographic where demographic_no=?"},
    {"update_record", "update demographic set last_name=?,first_name =?,address=?, city=?,province=?,postal=?,phone =?,phone2=?, year_of_birth=?,month_of_birth=?,date_of_birth=?,hin=?,ver=?, roster_status=?, patient_status=?, date_joined=?,  chart_no=?,provider_no=?,sex=? , end_date=?,eff_date=?, pcn_indicator=?,hc_type=? ,hc_renew_date=?, family_doctor=? where  demographic_no=?"},
    {"add_record", "insert into demographic values('\\N',?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?)" }, //26-1 demographic_no auto_increment
  
    {"search_demographicid", "select demographic_no,first_name,last_name,roster_status,sex,year_of_birth,month_of_birth,date_of_birth from demographic where demographic_no=?"},
    {"search*", "select * from demographic "+ orderby + " "+limit }, 
    {"search_lastfirstnamedob", "select demographic_no from demographic where last_name=? and first_name=? and year_of_birth=? and month_of_birth=? and date_of_birth=?"},
//    {"delete", "delete from demographic where demographic_no=?"},
    {"appt_history", "select appointment_date, start_time, end_time, provider.last_name, provider.first_name from appointment, provider where appointment.demographic_no=? and provider.provider_no=appointment.provider_no "+ orderby + " desc "+limit },
   };
   
   //associate each operation with an output JSP file -- displaymode
   String[][] responseTargets=new String[][] {
     {"Add Record" , "demographicaddarecord.jsp"},
     {"Search " , "demographicsearch2apptresults.jsp"},
     {"Search" , "demographicsearchresults.jsp"},
     {"edit" , "demographiceditdemographic.jsp"},
     {"appt_history" , "demographicappthistory.jsp"},
     {"Update Record" , "demographicupdatearecord.jsp"},

     {"Add Demographic" , "adddemographictoappt.jsp"},
     {" Search" , "demographicsearch2editapptresults.jsp"},
     {"Delete" , "demographicdeletearecord.jsp"},
     {"Save Record & Back to Appointment" , "demographicaddbacktoappt.jsp"},
     {"Update Record & Back to Appointment" , "demographicupdatebacktoappt.jsp"},
     {"Demographic ID" , "adddemographictoeditappt.jsp"},
     {"Add Record & Back to Appointment" , "adddemographicbacktoappt.jsp"},
     {"Update Record & Back to Appointment" , "updatedemographicbacktoappt.jsp"},
   };
   apptMainBean.doConfigure(dbParams,dbQueries,responseTargets);
%>
<%
  apptMainBean.doCommand(request); //store request to a help class object Dict - function&params
  if(true) {
    out.clear();
    //System.out.println("appointmentcontrol.jsp: -------- "+apptMainBean.whereTo());
    pageContext.forward(apptMainBean.whereTo()); //forward request&response to the target page
    return;
  }
%>
