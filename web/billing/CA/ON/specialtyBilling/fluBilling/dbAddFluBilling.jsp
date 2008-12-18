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

<% 
if(session.getAttribute("user") == null ) response.sendRedirect("../../../../../logout.jsp");
boolean billSaved = false;
String total = "0.00"; 
String content = "", rd="", rdohip="", hctype="";
String demoNo = request.getParameter("functionid");
rd = request.getParameter("rd").equals("null")?"":request.getParameter("rd");
rdohip = request.getParameter("rdohip").equals("null")?"000000":request.getParameter("rdohip");
hctype = request.getParameter("demo_hctype").equals("null")?"ON":request.getParameter("demo_hctype").equals("")?"ON":request.getParameter("demo_hctype");
content = content + "<rdohip>" + rdohip+"</rdohip>" + "<rd>" +  rd + "</rd>";
content = content + "<hctype>" + hctype+"</hctype>" + "<demosex>" + request.getParameter("demo_sex") + "</demosex>";
content = content + "<specialty>flu</specialty>";

String curUser_no,userfirstname,userlastname;
curUser_no = (String) session.getAttribute("user");
userfirstname = (String) session.getAttribute("userfirstname");
userlastname = (String) session.getAttribute("userlastname");
%>

<%@ page import="java.sql.*"%>
<%@ include file="../../../../../admin/dbconnection.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%@ include file="../../dbBilling.jsp"%>


<%
String billNo = null, svcDesc = null, svcPrice = null, sPrice = null ;
String[] param4 = new String[2];
param4[0] = request.getParameter("functionid");
param4[1] = request.getParameter("appointment_no");
ResultSet rsdemo = null;
String[] param5 = new String[1];
param5[0] = request.getParameter("svcCode"); 
rsdemo = apptMainBean.queryResults(param5, "search_servicecode_detail");
while(rsdemo.next()){
	svcDesc = rsdemo.getString("description");
	svcPrice = rsdemo.getString("value");
}

sPrice = svcPrice.substring(0,svcPrice.indexOf(".")) + svcPrice.substring(svcPrice.indexOf(".")+1);
String[] param =new String[23]; 
param[0]=request.getParameter("clinicNo");
param[1]=request.getParameter("functionid");
param[2]=request.getParameter("provider").substring(7);
param[3]=request.getParameter("appointment_no");
param[4]="V03G";
param[5]=request.getParameter("demo_name");
param[6]=request.getParameter("demo_hin");
param[7]=request.getParameter("docdate");
param[8]="00:00:00";
param[9]=request.getParameter("apptDate");
param[10]="00:00:00";
param[11]=request.getParameter("clinic_ref_code");
param[12]=content;
param[13]=svcPrice;
param[14]=request.getParameter("xml_billtype");
param[15]=request.getParameter("demo_dob"); 
param[16]="0000-00-00"; 
param[17]=request.getParameter("xml_visittype"); 
param[18]=request.getParameter("provider").substring(0,6); 
param[19]=""; 
param[20]=request.getParameter("apptProvider"); 
param[21]="0";
param[22]=request.getParameter("doccreator"); 
int rowsAffected = apptMainBean.queryExecuteUpdate(param,"save_bill");

rsdemo= apptMainBean.queryResults(param4, "search_billing_no_by_appt");
while (rsdemo.next()) {   
	billNo = rsdemo.getString("billing_no");
}

int recordAffected=0;
//int recordCount = Integer.parseInt(request.getParameter("record"));
String[] param2 = new String[8];
param2[0] = billNo;
param2[1] = request.getParameter("svcCode");
param2[2] = svcDesc;
param2[3] = sPrice;
param2[4] = request.getParameter("dxCode");
param2[5] = request.getParameter("apptDate");
param2[6] = request.getParameter("xml_billtype");
param2[7] = "1";
recordAffected = apptMainBean.queryExecuteUpdate(param2,"save_bill_record");

//	  int[] demo_no = new int[1]; demo_no[0]=Integer.parseInt(request.getParameter("demographic_no")); int rowsAffected = apptMainBean.queryExecuteUpdate(demo_no,param,request.getParameter("dboperation"));
  
if (rowsAffected ==1) {
	//apptMainBean.closePstmtConn();//change the status to billed {"updateapptstatus", "update appointment set status=? where appointment_no=? //provider_no=? and appointment_date=? and start_time=?"},
	String[] param1 =new String[2];
	param1[0]="B";
	param1[1]=request.getParameter("appointment_no");
//	  param1[1]=request.getParameter("apptProvider_no"); param1[2]=request.getParameter("appointment_date"); param1[3]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("start_time"));
//   rowsAffected = apptMainBean.queryExecuteUpdate(param1,"updateapptstatus");
	rsdemo = apptMainBean.queryResults(request.getParameter("functionid"), "search_billing_no");
	if (rsdemo.next()) {    
       billSaved = true;
   }
   apptMainBean.closePstmtConn();
   
   System.out.println(request.getParameter("goPrev"));
   if ( request.getParameter("goPrev") != null && request.getParameter("goPrev").equals("goPrev") && billSaved){ 
      response.sendRedirect("../../../../../oscarPrevention/AddPreventionData.jsp?prevention=Flu&demographic_no="+demoNo);
   }
}
%>



<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script LANGUAGE="JavaScript">
    <!--
    function start(){
      this.focus();
    }
    function closeit() {
    	//self.opener.refresh();
      //self.close();      
    }   
    //-->
</script>
</head>
<body onload="start()">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="90%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF">
		ADD A BILLING RECORD</font></th>
	</tr>
</table>
<%if (billSaved) { %>
<p>
<h1>Successful Addition of a billing Record.</h1>
</p>
<script LANGUAGE="JavaScript">             
              self.close();
              self.opener.refresh();              
        </script> <%}  else {%>
<p>
<h1>Sorry, addition has failed.</h1>
</p>
<%}%>
<p></p>
<hr width="90%"></hr>
<form><input type="button" value="Close this window"
	onClick="window.close()"></form>
</center>
</body>
</html>
