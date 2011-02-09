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
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
--%>
<%
  if(session.getAttribute("user") == null)
  {
	  response.sendRedirect("../logout.jsp");
	  return;
  }  
%>

<%@ page errorPage="errorpage.jsp" import="oscar.OscarProperties"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />

<%

  OscarProperties props = OscarProperties.getInstance();

  //operation available to the client -- dboperation
  //construct SQL expression
  String orderby="", limit="", limit1="", limit2="";
  if(request.getParameter("orderby")!=null) orderby="order by "+request.getParameter("orderby");
  if(request.getParameter("limit1")!=null) limit1=request.getParameter("limit1");
  if(request.getParameter("limit2")!=null) {
    limit2=request.getParameter("limit2");
    limit="limit "+limit2+" offset "+limit1;
  }

  String fieldname="", regularexp="like"; // exactly search is not required by users, e.g. regularexp="=";
  String strDbType = oscar.OscarProperties.getInstance().getProperty("db_type").trim();
  if (strDbType.trim().equalsIgnoreCase("mysql")) {
    regularexp = "regexp";
  } else if (strDbType.trim().equalsIgnoreCase("postgresql"))  {
    regularexp = "~*";
  }

  if(request.getParameter("search_mode")!=null) {
	  if(request.getParameter("keyword").indexOf("*")!=-1 || request.getParameter("keyword").indexOf("%")!=-1) regularexp="like";
    if(request.getParameter("search_mode").equals("search_address")) fieldname="address";
    if(request.getParameter("search_mode").equals("search_phone")) fieldname="phone";
    if(request.getParameter("search_mode").equals("search_hin")) fieldname="hin";
    if(request.getParameter("search_mode").equals("search_dob")) fieldname="year_of_birth "+regularexp+" ?"+" and month_of_birth "+regularexp+" ?"+" and date_of_birth ";
    if(request.getParameter("search_mode").equals("search_chart_no")) fieldname="chart_no";
    if(request.getParameter("search_mode").equals("search_name")) {
      if(request.getParameter("keyword").indexOf(",")==-1)  fieldname="lower(last_name)";
      else if(request.getParameter("keyword").trim().indexOf(",")==(request.getParameter("keyword").trim().length()-1)) fieldname="lower(last_name)";
      else fieldname="lower(last_name) "+regularexp+" ?"+" and lower(first_name) ";
    }
  }

  String ptstatusexp="";
  if(request.getParameter("ptstatus")!=null) {
	if(request.getParameter("ptstatus").equals("active")) {
		ptstatusexp=" and patient_status not in ("+props.getProperty("inactive_statuses", "'IN','DE','IC', 'ID', 'MO', 'FI'")+") ";	
	} 
	if(request.getParameter("ptstatus").equals("inactive"))  {
		ptstatusexp=" and patient_status in ("+props.getProperty("inactive_statuses", "'IN','DE','IC', 'ID', 'MO', 'FI'")+") ";
	}
  }
  else
      ptstatusexp=" and patient_status not in ("+props.getProperty("inactive_statuses", "'IN','DE','IC', 'ID', 'MO', 'FI'")+") ";   

  String domainRestriction="";
  if(request.getParameter("outofdomain")!=null && !request.getParameter("outofdomain").equals("true")) {
  	String curProvider_no = (String) session.getAttribute("user");
  	domainRestriction = "and demographic_no in (select client_id from admission where admission_status='current' and program_id in (select program_id from program_provider where provider_no='"+curProvider_no+"')) ";
  }

  
  String [][] dbQueries=new String[][] {
    {"search_titlename", "select *  from demographic where "+fieldname+" "+regularexp+" ? "+ptstatusexp+domainRestriction+orderby},
    {"search_titlename_mysql", "select *  from demographic where "+fieldname+" "+regularexp+" ? "+ptstatusexp+domainRestriction+orderby + " " + limit},
    {"add_apptrecord", "select demographic_no,first_name,last_name,roster_status,sex,chart_no,year_of_birth,month_of_birth,date_of_birth,provider_no from demographic where "+fieldname+ " "+regularexp+" ? " +ptstatusexp+domainRestriction+orderby},
    {"update_apptrecord", "select demographic_no,first_name,last_name,roster_status,sex,chart_no,year_of_birth,month_of_birth,date_of_birth,provider_no  from demographic where "+fieldname+ " "+regularexp+" ? " +ptstatusexp+domainRestriction+orderby + " "+limit},
    {"search_detail", "select * from demographic where demographic_no=?"},
    {"search_detail_ptbr", "select * from demographic d left outer join demographic_ptbr dptbr on dptbr.demographic_no = d.demographic_no where d.demographic_no=?"},
    {"update_record", "update demographic set last_name=?,first_name=?,address=?,city=?,province=?,postal=?,phone=?,phone2=?,email=?,pin=?,year_of_birth=?,month_of_birth=?,date_of_birth=?,hin=?,ver=?,roster_status=?,patient_status=?,chart_no=?,provider_no=?,sex=?,pcn_indicator=?,hc_type=?,family_doctor=?,country_of_origin=?,newsletter=?,sin=?,title=?,official_lang=?,spoken_lang=?,lastUpdateUser=?,lastUpdateDate=now(),date_joined=?,end_date=?,eff_date=?,hc_renew_date=?,roster_date=?  where demographic_no=?"},
    {"update_record_ptbr", "update demographic_ptbr set cpf=?,rg=?,chart_address=?,marriage_certificate=?,birth_certificate=?,marital_state=?,partner_name=?,father_name=?,mother_name=?,district=?,address_no=?,complementary_address=? where  demographic_no=?"},
    {"archive_record", "insert into demographicArchive (select * from demographic where demographic_no=?)" },
    {"add_record", "insert into demographic (last_name, first_name, address, city, province, postal, phone, phone2, email, pin, year_of_birth, month_of_birth, date_of_birth, hin, ver, roster_status, patient_status, date_joined, chart_no, provider_no, sex, end_date, eff_date, pcn_indicator, hc_type, hc_renew_date, family_doctor, country_of_origin, newsletter, sin, title, official_lang, spoken_lang, lastUpdateUser, lastUpdateDate) values(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,now())" },
    {"add_record_ptbr","insert into demographic_ptbr (demographic_no,cpf,rg,chart_address,marriage_certificate,birth_certificate,marital_state,partner_name,father_name,mother_name,district,address_no,complementary_address) values (?,?,?,?,?,?,?,?,?,?,?,?,?)" },
    {"search_provider", "select * from provider status='1' order by last_name"},
    {"search_provider_doc", "select * from provider where provider_type='doctor' and status='1' order by last_name"},
    {"search_provider_doc_with_ohip", "select * from provider where provider_type='doctor' and status='1' and ohip_no is not null and ohip_no !='' order by last_name"},
    {"search_demographicid", "select * from demographic where demographic_no=?"},
    {"search*", "select * from demographic "+ ptstatusexp+domainRestriction+orderby + " "+limit },
    {"search_lastfirstnamedob", "select demographic_no from demographic where last_name=? and first_name=? and year_of_birth=? and month_of_birth=? and date_of_birth=?"},
    {"search_demographiccust_alert", "select cust3 from demographiccust where demographic_no = ? " },
    {"search_demographiccust", "select * from demographiccust where demographic_no = ?" },
    {"search_demographic_ptbr","select * from demographic_ptbr where demographic_no = ?"},
    {"search_demoaddno", "select demographic_no from demographic where last_name=? and first_name =? and year_of_birth=? and month_of_birth=? and date_of_birth=? and hin=? and ver=?"},
    {"search_custrecordno", "select demographic_no from demographiccust  where demographic_no=?" },
    {"add_custrecord", "insert into demographiccust values(?,?,?,?,?, ?)" },
    {"update_custrecord", "update demographiccust set cust1=?,cust2=?,cust3=?,cust4=?,content=? where demographic_no=?" },
    {"appt_history", "select appointment_no, appointment_date, start_time, CONCAT(appointment_date,start_time) AS appttime, end_time, reason, appointment.status, provider.last_name, provider.first_name from appointment LEFT JOIN provider ON appointment.provider_no=provider.provider_no where appointment.demographic_no=? "+ orderby + " desc "},
    {"search_ptstatus", "select distinct patient_status from demographic where patient_status != '' and patient_status != 'AC' and patient_status != 'IN' and patient_status != 'DE' and patient_status != 'MO' and patient_status != 'FI'"},
    {"search_rsstatus", "select distinct roster_status from demographic where roster_status != '' and roster_status != 'RO' and roster_status != 'NR' and roster_status != 'TE' and roster_status != 'FS' "},
    {"search_waitingListPosition", "select max(position) as position from waitingList where listID=? AND is_history='N' "}, 
    {"add2WaitingList", "insert into waitingList (listID, demographic_no, note, position, onListSince, is_history) values(?,?,?,?,?,?)"}, 
    {"search_wlstatus", "select * from waitingList where demographic_no=? AND is_history='N' order by onListSince DESC"}, 
    {"search_waiting_list", "select * from waitingListName where group_no='" + session.getAttribute("groupno") +"' AND is_history='N' order by name"}, 
    {"search_demo_waiting_list", "select * from waitingList where demographic_no=? AND listID=?  AND is_history='N' "}, 
    {"search_future_appt", "select a.demographic_no, a.appointment_date from appointment a where a.appointment_date >= now() AND a.demographic_no=?"},
    {"search_hin", "select demographic_no, ver from demographic where hin=?"},
    {"add2caisi_admission", "insert into admission (client_id,program_id,provider_no,admission_date,admission_status,team_id,temporary_admission_flag) Values(?,?,?,?,'current',0,0)"},
    {"update_admission", "update admission set provider_no = ?, program_id = ? where client_id = ?"},
    {"search_program", "select id from program where name = ?"}
   };

	//associate each operation with an output JSP file -- displaymode
	String[][] responseTargets=new String[][] {
	  {"Add Record" , "demographicaddarecord.jsp"},
	  {"Search " , "demographicsearch2apptresults.jsp"},
	  {"Search" , "demographicsearchresults.jsp"},
	  {"edit" , "demographiceditdemographic.jsp"},
	  {"pdflabel" , "demographicpdflabel.jsp"},             
	  {"pdfaddresslabel" , "demographicpdfaddresslabel.jsp"},             
	  {"pdfchartlabel" , "demographicpdfchartlabel.jsp"},             
	  {"appt_history" , "demographicappthistory.jsp"},
	  {"Update Record" , "demographicupdatearecord.jsp"},
	
	  {"Delete" , "demographicdeletearecord.jsp"},
	  {"Save Record & Back to Appointment" , "demographicaddbacktoappt.jsp"},
	  {"Update Record & Back to Appointment" , "demographicupdatebacktoappt.jsp"},
	  {"Demographic ID" , "adddemographictoeditappt.jsp"},
	  {"Add Record & Back to Appointment" , "adddemographicbacktoappt.jsp"},
	  {"Update Record & Back to Appointment" , "updatedemographicbacktoappt.jsp"},
	  {"linkMsg2Demo" , "../oscarMessenger/msgSearchDemo.jsp"},             
	};
	apptMainBean.doConfigure( dbQueries,responseTargets);

   	apptMainBean.doCommand(request); //store request to a help class object Dict - function&params

	String pg=apptMainBean.whereTo();
	if (pg!=null)
	{
	   	pageContext.forward(pg); //forward request&response to the target page
		return;
	}
%>

