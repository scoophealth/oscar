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
  if(session.getValue("user") == null || !( ((String) session.getValue("userprofession")).equalsIgnoreCase("admin") ))
    response.sendRedirect("../logout.jsp");
  //instatiate/configure the main bean, forward the request to the output file
%>

<%@ page errorPage="errorpage.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%@ include file="dbconnection.jsp"%>

<%
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
    if(request.getParameter("search_mode").equals("search_providerno")) fieldname="provider_no";
    if(request.getParameter("search_mode").equals("search_preferenceno")) fieldname="preference_no";
    if(request.getParameter("search_mode").equals("search_username")) fieldname="user_name";
    if(request.getParameter("search_mode").equals("search_dob")) fieldname="year_of_birth "+regularexp+" ?"+" and month_of_birth "+regularexp+" ?"+" and date_of_birth ";
    if(request.getParameter("search_mode").equals("search_name")) {
      if(request.getParameter("keyword").indexOf(",")==-1)  fieldname="last_name";
      else if(request.getParameter("keyword").trim().indexOf(",")==(request.getParameter("keyword").trim().length()-1)) fieldname="last_name";
      else fieldname="last_name "+regularexp+" ?"+" and first_name ";
    }
  }
  //operation available to the client - dboperation
  String [][] dbQueries;
if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){
    dbQueries=new String[][] {
    {"provider_add_record", "insert into provider values(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?)" },
    {"provider_search_titlename", "select provider_no,first_name,last_name,specialty,sex,team,phone from provider where "+fieldname+ " "+regularexp+" ? " +orderby + " "+limit}, 
    {"provider_search_detail", "select * from provider where provider_no=?"},
    {"provider_delete", "delete from provider where provider_no=? and provider_no!='super'"},
    {"provider_update_record", "update provider set last_name=?,first_name=?, provider_type=?, specialty=?,team=?,sex =?,dob=?, address=?,phone=?,ohip_no =?,rma_no=?,billing_no=?,hso_no=?,status=?, comments=? where provider_no=? and provider_no!='super'"},
    {"demographic_search_titlename", "select demographic_no,first_name,last_name,roster_status,sex,year_of_birth,month_of_birth,date_of_birth  from demographic where "+fieldname+ " "+regularexp+" ? " +orderby + " "+limit},  
    {"demographic_search_detail", "select * from demographic where demographic_no=?"},
    {"demographic_update_record", "update demographic set last_name=?,first_name =?,address=?, city=?,province=?,postal=?,phone =?,phone2=?, year_of_birth=?,month_of_birth=?,date_of_birth=?,hin=?,ver=?, roster_status=?, patient_status=?, date_joined=?,  chart_no=?,provider_no=?,sex=? , end_date=?,eff_date=?, pcn_indicator=?,hc_type=? ,hc_renew_date=?, family_doctor=? where  demographic_no=?"},
    {"demographic_add_record", "insert into demographic values('\\N',?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?)" }, //26-1 demographic_no auto_increment
    {"demographic_delete", "delete from demographic where demographic_no=?"},
    {"search_lastfirstnamedob", "select demographic_no from demographic where last_name=? and first_name=? and year_of_birth=? and month_of_birth=? and date_of_birth=?"},
    {"security_add_record", "insert into security (user_name,password,provider_no) values(?,?,?)" },
    {"security_search_titlename", "select * from security where "+fieldname+ " "+regularexp+" ? " +orderby + " "+limit}, 
    {"security_search_detail", "select * from security where security_no=?"},
    {"security_delete", "delete from security where security_no=? and provider_no!='super'"},
    {"security_update_record", "update security set user_name=?,password=?,provider_no=? where security_no=?" },
    {"preference_add_record", "insert into preference (provider_no,start_hour,end_hour,every_min,mygroup_no,color_template,new_tickler_warning_window) values(?,?,?,?,?,?,?)" },
    {"preference_search_titlename", "select * from preference where "+fieldname+ " "+regularexp+" ? " +orderby + " "+limit}, 
    {"preference_search_detail", "select * from preference where preference_no=?"},
    {"preference_delete", "delete from preference where preference_no=?"},
    {"preference_update_record", "update preference set provider_no=?,start_hour=?,end_hour=?,every_min=?,mygroup_no=?,color_template=?,new_tickler_warning_window=? where preference_no=?" },
    {"preference_addupdate_record", "update preference set start_hour=?, end_hour=?, every_min=?, mygroup_no=?, color_template=?,new_tickler_warning_window=? where provider_no=? "},

    {"searchmygroupcount", "select count(provider_no) from mygroup where mygroup_no=? "}, 
    {"searchmygroupprovider", "select provider_no, last_name, first_name from mygroup where mygroup_no=? "}, 
    {"searchmygroupall", "select * from mygroup order by ?"}, 
    {"deletegroupmember", "delete from mygroup where mygroup_no=? and provider_no=?"}, 
    {"savemygroup", "insert into mygroup (mygroup_no,provider_no,last_name,first_name) values(?,?,?,?)" },
    {"searchprovider", "select provider_no, last_name, first_name from provider where provider_type='doctor' order by ?"}, 

  };
}else {
  dbQueries=new String[][] {
    {"provider_add_record", "insert into provider values(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?)" },
    {"provider_search_titlename", "select provider_no,first_name,last_name,specialty,sex,team,phone from provider where "+fieldname+ " "+regularexp+" ? " +orderby + " "+limit}, 
    {"provider_search_detail", "select * from provider where provider_no=?"},
    {"provider_delete", "delete from provider where provider_no=? and provider_no!='super'"},
    {"provider_update_record", "update provider set last_name=?,first_name=?, provider_type=?, specialty=?,team=?,sex =?,dob=?, address=?,phone=?,ohip_no =?,rma_no=?,billing_no=?,hso_no=?,status=?, comments=? where provider_no=? and provider_no!='super'"},
    {"demographic_search_titlename", "select demographic_no,first_name,last_name,roster_status,sex,year_of_birth,month_of_birth,date_of_birth  from demographic where "+fieldname+ " "+regularexp+" ? " +orderby + " "+limit},  
    {"demographic_search_detail", "select * from demographic where demographic_no=?"},
    {"demographic_update_record", "update demographic set last_name=?,first_name =?,address=?, city=?,province=?,postal=?,phone =?,phone2=?, year_of_birth=?,month_of_birth=?,date_of_birth=?,hin=?,ver=?, roster_status=?, patient_status=?, date_joined=?,  chart_no=?,provider_no=?,sex=? , end_date=?,eff_date=?, pcn_indicator=?,hc_type=? ,hc_renew_date=?, family_doctor=? where  demographic_no=?"},
    {"demographic_add_record", "insert into demographic values('\\N',?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?)" }, //26-1 demographic_no auto_increment
    {"demographic_delete", "delete from demographic where demographic_no=?"},
    {"search_lastfirstnamedob", "select demographic_no from demographic where last_name=? and first_name=? and year_of_birth=? and month_of_birth=? and date_of_birth=?"},
    {"security_add_record", "insert into security (user_name,password,provider_no) values(?,?,?)" },
    {"security_search_titlename", "select * from security where "+fieldname+ " "+regularexp+" ? " +orderby + " "+limit}, 
    {"security_search_detail", "select * from security where security_no=?"},
    {"security_delete", "delete from security where security_no=? and provider_no!='super'"},
    {"security_update_record", "update security set user_name=?,password=?,provider_no=? where security_no=?" },
    {"preference_add_record", "insert into preference (provider_no,start_hour,end_hour,every_min,mygroup_no,color_template) values(?,?,?,?,?,?)" },
    {"preference_search_titlename", "select * from preference where "+fieldname+ " "+regularexp+" ? " +orderby + " "+limit}, 
    {"preference_search_detail", "select * from preference where preference_no=?"},
    {"preference_delete", "delete from preference where preference_no=?"},
    {"preference_update_record", "update preference set provider_no=?,start_hour=?,end_hour=?,every_min=?,mygroup_no=?,color_template=? where preference_no=?" },
    {"preference_addupdate_record", "update preference set start_hour=?, end_hour=?, every_min=?, mygroup_no=?, color_template=? where provider_no=? "},

    {"searchmygroupcount", "select count(provider_no) from mygroup where mygroup_no=? "}, 
    {"searchmygroupprovider", "select provider_no, last_name, first_name from mygroup where mygroup_no=? "}, 
    {"searchmygroupall", "select * from mygroup order by ?"}, 
    {"deletegroupmember", "delete from mygroup where mygroup_no=? and provider_no=?"}, 
    {"savemygroup", "insert into mygroup (mygroup_no,provider_no,last_name,first_name) values(?,?,?,?)" },
    {"searchprovider", "select provider_no, last_name, first_name from provider where provider_type='doctor' order by ?"}, 

  };
}
   
  //associate each operation with an output JSP file - displaymode
  String[][] responseTargets=new String[][] {
    {"Provider_Add_Record" , "provideraddarecord.jsp"},
    {"Provider_Search" , "providersearchresults.jsp"},
    {"Provider_Update" , "providerupdateprovider.jsp"},
    {"Provider_Delete" , "providerdelete.jsp"},
    {"Provider_Update_Record" , "providerupdate.jsp"},
    {"Demographic_Search" , "demographicsearchresults.jsp"},
    {"Demographic_Add_Record" , "demographicaddarecord.jsp"},
    {"Demographic_Edit" , "demographiceditdemographic.jsp"},
    {"Demographic_Update" , "demographicupdatearecord.jsp"},
    {"Demographic_Delete" , "demographicdeletearecord.jsp"},
    {"Security_Add_Record" , "securityaddsecurity.jsp"},
    {"Security_Search" , "securitysearchresults.jsp"},
    {"Security_Update" , "securityupdatesecurity.jsp"},
    {"Security_Delete" , "securitydelete.jsp"},
    {"Security_Update_Record" , "securityupdate.jsp"},
    {"Preference_Add_Record" , "preferenceaddpreference.jsp"},
    {"Preference_Search" , "preferencesearchresults.jsp"},
    {"Preference_Update" , "preferenceupdatepreference.jsp"},
    {"Preference_Delete" , "preferencedelete.jsp"},
    {"Preference_Update_Record" , "preferenceupdate.jsp"},
   
    {"displaymygroup" , "admindisplaymygroup.jsp"},
    {"newgroup" , "adminnewgroup.jsp"},
    {"savemygroup" , "adminsavemygroup.jsp"},
  };
  apptMainBean.doConfigure(dbParams,dbQueries,responseTargets);
  apptMainBean.doCommand(request); //store request to a help class object Dict - function&params
  if(true) {
    out.clear();
    pageContext.forward(apptMainBean.whereTo()); //forward request&response to the target page
    return;
  }
%>
