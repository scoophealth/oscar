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
  //operation available to the client - dboperation
  String orderby="", limit="", limit1="", limit2="";
  if(request.getParameter("orderby")!=null) orderby="order by "+request.getParameter("orderby");
  if(request.getParameter("limit1")!=null) limit1=request.getParameter("limit1")+", ";
  if(request.getParameter("limit2")!=null) {
    limit2=request.getParameter("limit2");
    limit="limit "+limit1+limit2;
  }
  
  
  String [][] dbQueries=new String[][] {
    {"search_provider_all_dt", "select * from provider where provider_type='doctor' and provider_no like ? order by last_name"},
    {"search_provider_dt", "select * from provider where provider_type='doctor' and ohip_no || null and provider_no like ? order by last_name"},
     {"search_provider_ohip_dt", "select * from provider where provider_type='doctor' and ohip_no like ? and ohip_no || null order by last_name"},
    {"search_demographic_details", "select * from demographic where demographic_no=?"},
      {"search_demographic_all", "select * from demographic where year_of_birth>?"},
    {"search_provider_name", "select * from provider where provider_no=?"},
    {"search_visit_location", "select clinic_location_name from clinic_location where clinic_location_no=?"},
   {"search_bill_location", "select * from clinic_location where clinic_no=1 and clinic_location_no=?"},    
    {"search_clinic_location", "select * from clinic_location where clinic_no=? order by clinic_location_no"},  	
	{"save_reportagesex", "insert into reportagesex values(?,?,?,?,?,?,?,?)"},
        {"delete_reportagesex_bydate", "delete from reportagesex where reportdate<=?"},
        {"count_reportagesex_roster", "select count(*) n from reportagesex where (status<>'OP' and status<>'IN' and status<>'DE') and roster=? and sex like ? and provider_no=? and age >= ? and age <=? and date_joined >=? and date_joined <=?"},
        {"count_reportagesex_noroster", "select count(*) n from reportagesex where (status<>'OP' and status<>'IN' and status<>'DE') and roster<>? and sex like? and provider_no=? and age >= ? and age <=? and date_joined >=? and date_joined <=?"},
        {"count_reportagesex", "select count(*) n from reportagesex where (status<>'OP' and status<>'IN' and status<>'DE') and sex like ? and provider_no=? and age >= ? and age <=? and date_joined >=? and date_joined <=?"},
        {"count_larrykain_clinic", "select count(*) n from billing where visittype='00' and clinic_ref_code=? and status<>'D' and billing_date >=? and billing_date <=?"},
	{"count_larrykain_hospital", "select count(*) n from billing where visittype<>'00' and (clinic_ref_code=? or clinic_ref_code=? or clinic_ref_code=? or clinic_ref_code=?) and status<>'D' and billing_date >=? and billing_date <=?"},
	{"count_larrykain_other","select count(*) n from billing where visittype<>'00' and status<>'D' and  (clinic_ref_code<>? and clinic_ref_code<>? and clinic_ref_code<>? and clinic_ref_code<>? and clinic_ref_code<>?) and billing_date >=? and billing_date<=?"},
	{"search_mygroup", "select distinct mygroup_no from mygroup where mygroup_no like ?"},
	{"search_mygroup_provider", "select p.last_name, p.first_name, p.provider_no, m.mygroup_no from provider p, mygroup m where m.provider_no=p.provider_no and m.mygroup_no=?"},
	{"delete_reportprovider_byaction", "delete from reportprovider where action=?"},
	{"save_reportprovider_byaction", "insert into reportprovider values(?,?,?,?)"},
	{"search_reportprovider_check", "select status from reportprovider where  provider_no=? and team=? and action=?"},
	{"search_reportprovider","select p.last_name, p.first_name, p.provider_no, r.team from provider p,reportprovider r where r.provider_no=p.provider_no and r.status<>'D' and r.action=? order by team"},
        {"count_visit", "select count(*) n from billing where status<>'D' and creator=? and visittype=? and billing_date>=? and billing_date<=?"},
  };
  
  //associate each operation with an output JSP file - displaymode
  String[][] responseTargets=new String[][] {
    {"day" , "appointmentprovideradminday.jsp"},
    {"month" , "appointmentprovideradminmonth.jsp"},
    {"addstatus" , "provideraddstatus.jsp"},
    {"updatepreference" , "providerupdatepreference.jsp"},
    {"displaymygroup" , "providerdisplaymygroup.jsp"},
    {"encounter" , "providerencounter.jsp"},
    {"prescribe" , "providerprescribe.jsp"},
    {"vary" , request.getParameter("displaymodevariable")==null?"":request.getParameter("displaymodevariable") },
    {"saveencounter" , "providersaveencounter.jsp"},
    {"savebill" , "providersavebill.jsp"},
    {"encounterhistory" , "providerencounterhistory.jsp"},
  };
  apptMainBean.doConfigure(dbParams,dbQueries,responseTargets);
%>