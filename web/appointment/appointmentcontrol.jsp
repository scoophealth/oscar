<%
  if(session.getValue("user") == null || !(((String) session.getValue("userprofession")).equalsIgnoreCase("doctor") || ((String) session.getValue("userprofession")).equalsIgnoreCase("receptionist")))
    response.sendRedirect("../logout.jsp");
%>

<%@ page errorPage="errorpage.jsp" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />
<%@ include file="../admin/dbconnection.jsp" %>  
<%
  //operation available to the client -- dboperation
  String [][] dbOperation=new String[][] {
    {"add_apptrecord", "insert into appointment (provider_no,appointment_date,start_time,end_time,name, notes,reason,location,resources,type, style,billing,status,createdatetime,creator, remarks, demographic_no) values(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?)" },
    {"search", "select * from appointment where appointment_no=?"}, 
    {"update_apptrecord", "update appointment set demographic_no=?,appointment_date=?,start_time=?,end_time=?,name=?, notes=?,reason =?,location=?, resources=?, type=?,style=?,billing =?,status=?,createdatetime=?,creator=?,remarks=? where appointment_no=? "}, 
    {"delete", "delete from appointment where appointment_no=?"}, 
    //{"cancel", "update appointment set status='C' where appointment_no=?"}, 
    {"updatestatusc", "update appointment set status=?, creator=?, createdatetime=? where appointment_no=?"}, 
    {"updatestatus", "update appointment set status=? where appointment_no=?"}, 
    {"searchappointmentday", "select start_time,end_time,name,demographic_no,reason,notes,status from appointment where provider_no=? and appointment_date=? order by start_time"}, 
    {"searchapptweek", "select appointment_date, start_time,end_time, name from appointment where provider_no=? and appointment_date between ? and ? order by appointment_date,start_time"}, 
    {"searchappointmentmonth", "select start_time,end_time, name from appointment where provider_no=? and appointment_date between ? and ? order by appointment_date,start_time"}, 
    {"searchprovidername", "select last_name, first_name from provider where provider_no=?"}, 
    {"search_detail", "select * from demographic where demographic_no=?"},
    {"search_demographiccust_alert", "select cust3 from demographiccust where demographic_no = ? " }, 
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
