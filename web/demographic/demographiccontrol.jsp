<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
%>

<%@ page errorPage="errorpage.jsp" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />
<%@ include file="../admin/dbconnection.jsp" %>  

<%
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
    {"search_titlename", "select *  from demographic where "+fieldname+ " "+regularexp+" ? " +orderby + " "+limit},  
    {"add_apptrecord", "select demographic_no,first_name,last_name,roster_status,sex,chart_no,year_of_birth,month_of_birth,date_of_birth,provider_no from demographic where "+fieldname+ " "+regularexp+" ? " +orderby + " "+limit},  
    {"update_apptrecord", "select demographic_no,first_name,last_name,roster_status,sex,chart_no,year_of_birth,month_of_birth,date_of_birth,provider_no  from demographic where "+fieldname+ " "+regularexp+" ? " +orderby + " "+limit},  
    {"search_detail", "select * from demographic where demographic_no=?"},
    {"search_detail_ptbr", "select * from demographic d left outer join demographic_ptbr dptbr on dptbr.demographic_no = d.demographic_no where d.demographic_no=?"},
    {"update_record", "update demographic set last_name=?,first_name =?,address=?, city=?,province=?,postal=?,phone =?,phone2=?,email=?,pin=?, year_of_birth=?,month_of_birth=?,date_of_birth=?,hin=?,ver=?, roster_status=?, patient_status=?, date_joined=?,  chart_no=?,provider_no=?,sex=? , end_date=?,eff_date=?, pcn_indicator=?,hc_type=? ,hc_renew_date=?, family_doctor=? where  demographic_no=?"},
    {"update_record_ptbr", "update demographic_ptbr set cpf=?,rg=?,chart_address=?,marriage_certificate=?,birth_certificate=?,marital_state=?,partner_name=?,father_name=?,mother_name=?,district=?,address_no=?,complementary_address=? where  demographic_no=?"},
    {"add_record", "insert into demographic (last_name, first_name, address, city, province, postal, phone, phone2, email, pin, year_of_birth, month_of_birth, date_of_birth, hin, ver, roster_status, patient_status, date_joined, chart_no, provider_no, sex, end_date, eff_date, pcn_indicator, hc_type, hc_renew_date, family_doctor) values(?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,'today',?, ?,?,?,?,?, ?,?,?)" }, 
    {"add_record_ptbr","insert into demographic_ptbr (demographic_no,cpf,rg,chart_address,marriage_certificate,birth_certificate,marital_state,partner_name,father_name,mother_name,district,address_no,complementary_address) values (?,?,?,?,?,?,?,?,?,?,?,?,?)" }, 
    {"search_provider", "select * from provider order by last_name"},
    {"search_demographicid", "select * from demographic where demographic_no=?"},
    {"search*", "select * from demographic "+ orderby + " "+limit }, 
    {"search_lastfirstnamedob", "select demographic_no from demographic where last_name=? and first_name=? and year_of_birth=? and month_of_birth=? and date_of_birth=?"},
    {"search_demographiccust_alert", "select cust3 from demographiccust where demographic_no = ? " }, 
    {"search_demographiccust", "select * from demographiccust where demographic_no = ?" },
    {"search_demographic_ptbr","select * from demographic_ptbr where demographic_no = ?"},
    {"search_demoaddno", "select demographic_no from demographic where last_name=? and first_name =? and year_of_birth=? and month_of_birth=? and date_of_birth=? and hin=? and ver=?"},
    {"search_custrecordno", "select demographic_no from demographiccust  where demographic_no=?" }, 
    {"add_custrecord", "insert into demographiccust values(?,?,?,?,?, ?)" }, 
    {"update_custrecord", "update demographiccust set cust1=?,cust2=?,cust3=?,content=? where demographic_no=?" }, 
    {"appt_history", "select appointment_no, appointment_date, start_time, end_time, reason, appointment.status, provider.last_name, provider.first_name from appointment LEFT JOIN provider ON appointment.provider_no=provider.provider_no where appointment.demographic_no=? "+ orderby + " desc "+limit },
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
