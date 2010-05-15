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
if(session.getAttribute("user") == null)    response.sendRedirect("../../../../logout.jsp");
String user_no="";
user_no = (String) session.getAttribute("user");
%>
<%@ page import="java.util.*, java.sql.*, oscar.*"
	errorPage="../../errorpage.jsp"%>
<%@ include file="../../../../admin/dbconnection.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<jsp:useBean id="SxmlMisc" class="oscar.SxmlMisc" scope="session" />
<%@ include file="dbINR.jsp"%>
<% 
String temp="";
String clinic_no= request.getParameter("clinic_no");
String clinic_ref_code = request.getParameter("xml_location");
String creator = request.getParameter("curUser");
String updatedate = request.getParameter("curDate"); 
String verCode = request.getParameter("verCode");

String demono="",demo_name="",demo_dob="",demo_hin="", billinginr_no="", provider_no="";
    String provider_ohip_no="", provider_rma_no="", diagnostic_code="", service_desc="",service_code="", billing_amount="";
    String billing_unit="";
    int colorCount = 0;
       String color="";
  int Count1 = 0;  

for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
		temp=e.nextElement().toString();
		if( temp.indexOf("inrbilling")==-1 ) continue; 
//////////////////////////////////////////////////////////////////////////////////////
// 	content+="<" +temp+ ">" +SxmlMisc.replaceHTMLContent(request.getParameter(temp))+ "</" +temp+ ">";
//////////////////////////////////////////////////////////////////////////////////////  	



   
       
       
     ResultSet rsdemo = null;
      rsdemo = apptMainBean.queryResults(temp.substring(10), "search_inrbilling_dt_billno");
      while(rsdemo.next()){ 
      
      demono = rsdemo.getString("demographic_no");
      demo_name = rsdemo.getString("demographic_name");
      demo_hin = rsdemo.getString("hin") + rsdemo.getString("ver");
      demo_dob = rsdemo.getString("year_of_birth") + rsdemo.getString("month_of_birth") + rsdemo.getString("date_of_birth");
      provider_no = rsdemo.getString("provider_no");
      provider_ohip_no = rsdemo.getString("provider_ohip_no");
      provider_rma_no = rsdemo.getString("provider_rma_no");
      diagnostic_code = rsdemo.getString("diagnostic_code");
      service_code = rsdemo.getString("service_code");
      service_desc = rsdemo.getString("service_desc");
      billing_amount = rsdemo.getString("billing_amount");
      billing_unit = rsdemo.getString("billing_unit");
      
      
      
	StringBuffer sotherBuffer = new StringBuffer(billing_amount);
	int f = billing_amount.indexOf('.');
	sotherBuffer.deleteCharAt(f);
	sotherBuffer.insert(f,"");
	billing_amount = sotherBuffer.toString();




  String[] param =new String[23]; 
	  param[0]=clinic_no;
	  param[1]=demono;
	  param[2]=provider_no;
	  param[3]="0";
	  param[4]="V03";
	  param[5]=demo_name;
	  param[6]=demo_hin;
	  param[7]=request.getParameter("curDate");
	  param[8]=MyDateFormat.getTimeXX_XX_XX("00:00");
	  param[9]=request.getParameter("xml_appointment_date");
	  param[10]=MyDateFormat.getTimeXX_XX_XX("00:00");
	  param[11]=clinic_ref_code;
	  param[12]="";
	  param[13]=billing_amount;
	  param[14]="O";
	  param[15]=demo_dob; 
	  param[16]=request.getParameter("xml_appointment_date"); 
	  param[17]="00"; 
	    param[18]=provider_ohip_no; 
	  param[19]=provider_rma_no; 
	  	  param[20]=""; 
	  	  	  param[21]=""; 
	  	  	  	  param[22]=creator; 
	  int rowsAffected = apptMainBean.queryExecuteUpdate(param,"save_bill");
	     
	    String billNo = null;
	    String[] param4 = new String[2];
	    param4[0] = demono;
	    param4[1] = "0";
	    rsdemo = null;
	     
	    rsdemo = apptMainBean.queryResults(param4, "search_billing_no_by_appt");
   while (rsdemo.next()) {   
   billNo = rsdemo.getString("billing_no");
   }
   
   int recordAffected=0;
         String[] param3 = new String[3];
                 param3[0] = "A";
                 param3[1] = request.getParameter("xml_appointment_date");
                 param3[2] = temp.substring(10);
          recordAffected = apptMainBean.queryExecuteUpdate(param3,"update_inrbilling_dt_billno");
	
   
   int recordCount = Integer.parseInt("1");
       for (int i=0;i<recordCount;i++){
       String[] param2 = new String[8];
       param2[0] = billNo;
       param2[1] = service_code;
       param2[2] = service_desc;
       param2[3] = billing_amount;
       param2[4] = diagnostic_code;
       param2[5] = request.getParameter("xml_appointment_date");
       param2[6] = "O";
        param2[7] = billing_unit;
       
       recordAffected = apptMainBean.queryExecuteUpdate(param2,"save_bill_record");
       
       
              }       
              
         
	}
} 
apptMainBean.closePstmtConn();
%>
<script LANGUAGE="JavaScript">
      self.close();
     // self.opener.refresh();
</script>