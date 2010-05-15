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
  if(session.getValue("user") == null)
    response.sendRedirect("../logout.htm");
  String curUser_no,userfirstname,userlastname;
  curUser_no = (String) session.getAttribute("user");
//  mygroupno = (String) session.getAttribute("groupno");  
  userfirstname = (String) session.getAttribute("userfirstname");
  userlastname = (String) session.getAttribute("userlastname");
%>
<%@ page import="java.sql.*, java.util.*,java.net.*, oscar.MyDateFormat"
	errorPage="errorpage.jsp"%>
<%@ include file="../../../admin/dbconnection.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%@ include file="dbBilling.jsp"%>
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
		DELETE A BILLING RECORD</font></th>
	</tr>
</table>
<%
   String billCode = request.getParameter("billCode");
   if (billCode.substring(0,1).compareTo("B") == 0) {
   %>
<p>
<h1>Sorry, cannot delete billed items.</h1>
</p>
<form><input type="button" value="Back to previous page"
	onClick="history.go(-1);return false;"></form>
<% }
   else{
     
   
  int rowsAffected=0;
  // int recordCount = Integer.parseInt(request.getParameter("record"));
 //      for (int i=0;i<recordCount;i++){
 //     String[] param2 = new String[7];
   //    param2[0] = billNo;
   //    param2[1] = request.getParameter("billrec"+i);
   //    param2[2] = request.getParameter("billrecdesc"+i);
   //    param2[3] = request.getParameter("pricerec"+i);
   //    param2[4] = request.getParameter("diagcode");
   //    param2[5] = request.getParameter("appointment_date");
   //    param2[6] = request.getParameter("billtype");
       rowsAffected = apptMainBean.queryExecuteUpdate(request.getParameter("billing_no"),"delete_bill");
       
       
       //       }

//	  int[] demo_no = new int[1]; demo_no[0]=Integer.parseInt(request.getParameter("demographic_no")); int rowsAffected = apptMainBean.queryExecuteUpdate(demo_no,param,request.getParameter("dboperation"));
  
  if (rowsAffected ==1) {
    //apptMainBean.closePstmtConn();//change the status to billed {"updateapptstatus", "update appointment set status=? where appointment_no=? //provider_no=? and appointment_date=? and start_time=?"},
   String[] param1 =new String[2];
	  param1[0]="P";
	  param1[1]=request.getParameter("appointment_no");
//	  param1[1]=request.getParameter("apptProvider_no"); param1[2]=request.getParameter("appointment_date"); param1[3]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("start_time"));
   rowsAffected = apptMainBean.queryExecuteUpdate(param1,"updateapptstatus");
// rsdemo = null;
 //  rsdemo = apptMainBean.queryResults(request.getParameter("demographic_no"), "search_billing_no");
 //  while (rsdemo.next()) {    
%>
<p>
<h1>Successful Addition of a billing Record.</h1>
</p>
<script LANGUAGE="JavaScript">
      self.close();
      //self.top.location = 'providercontrol.jsp?appointment_no=<%// =request.getParameter("appointment_no")%>&demographic_no=<%// =Integer.parseInt(request.getParameter("demographic_no"))%>&curProvider_no=<%// =curUser_no%>&username=<%// = userfirstname+" "+userlastname %>&appointment_date=<%// =request.getParameter("appointment_date")%>&start_time=<%// =request.getParameter("start_time")%>&status=B&displaymode=encounter&dboperation=search_demograph&template=';
    //  self.opener.document.encounter.encounterattachment.value +="<billing>../billing/billingOB2.jsp?billing_no=<%// =rsdemo.getString("billing_no")%>^dboperation=search_bill^hotclick=0</billing>"; //providercontrol.jsp?billing_no=<%// =rsdemo.getString("billing_no")%>^displaymode=vary^displaymodevariable=<%// =URLEncoder.encode("../billing/")%>billing<%// =request.getParameter("billing_name")%>.jsp^dboperation=search_bill^hotclick=0</billing>";
     // self.opener.document.encounter.attachmentdisplay.value +="Billing "; //:<%=request.getParameter("billing_name")%> ";
     	self.opener.refresh();
</script> <%
  //  break; //get only one billing_no
  //  }//end of while
 }  else {
%>
<p>
<h1>Sorry, addition has failed.</h1>
</p>
<%  
  }
  apptMainBean.closePstmtConn();
  }
%>
<p></p>
<hr width="90%"></hr>
<form><input type="button" value="Close this window"
	onClick="window.close()"></form>
</center>
</body>
</html>