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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
</head>
<body>
<%@ page import="oscar.*,java.text.*, java.util.*"%>
<jsp:useBean id="billing" scope="session" class="oscar.BillingBean" />
<jsp:useBean id="billingItem" scope="page" class="oscar.BillingItemBean" />
<jsp:useBean id="billingDataBean" class="oscar.BillingDataBean"
	scope="session" />
<jsp:useBean id="billingPatientDataBean"
	class="oscar.BillingPatientDataBean" scope="session" />
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%@ include file="../../../admin/dbconnection.jsp"%>
<%@ include file="dbBilling.jsp"%>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF">Billing
		Correction Successfully Done</font></th>
	</tr>
</table>
<%
 
 try {
 
 // BillingDataBean billingDataBean = new BillingDataBean();
 java.lang.String _p0_0 = billingDataBean.getUpdate_date(); //throws an exception if empty
 java.lang.String _p0_1 = billingDataBean.getBilling_no(); //throws an exception if empty
 java.lang.String _p0_2 = billingDataBean.getHin(); //throws an exception if empty
 java.lang.String _p0_3 = billingDataBean.getVisittype(); //throws an exception if empty
 java.lang.String _p0_4 = billingDataBean.getVisitdate(); //throws an exception if empty
 java.lang.String _p0_5 = billingDataBean.getStatus(); //throws an exception if empty
 java.lang.String _p0_6 = billingDataBean.getDob(); //throws an exception if empty
 java.lang.String _p0_7 = billingDataBean.getProviderNo(); //throws an exception if empty
 java.lang.String _p0_8 = billingDataBean.getClinic_ref_code(); //throws an exception if empty
 java.lang.String _p0_9 = billingDataBean.getBilling_date(); //throws an exception if empty
 java.lang.String _p0_10 = billingPatientDataBean.getDemoname(); //throws an exception if empty
 java.lang.String _p0_11 = billingPatientDataBean.getAddress(); //throws an exception if empty
 java.lang.String _p0_12 = billingPatientDataBean.getProvince(); //throws an exception if empty
 java.lang.String _p0_13 = billingPatientDataBean.getCity(); //throws an exception if empty
 java.lang.String _p0_14 = billingPatientDataBean.getPostal(); //throws an exception if empty
 java.lang.String _p0_15 = billingPatientDataBean.getSex(); //throws an exception if empty
  java.lang.String _p0_16 = billingDataBean.getContent(); //throws an exception if empty
  java.lang.String _p0_17 = "";
   java.lang.String _p0_18 = billingDataBean.getTotal(); //throws an exception if empty
   java.lang.String _p0_19 = "";
  
  GregorianCalendar now=new GregorianCalendar();
   String[] param =new String[3];
	  param[0]=(String) session.getAttribute("user");
	  param[1]= _p0_16;
	  param[2]= now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH)+ " " + now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE);;
 int rowsAffected = apptMainBean.queryExecuteUpdate(param,"archive_bill");
 int rowsAffected2 = apptMainBean.queryExecuteUpdate(_p0_1,"delete_bill_detail");
  
  
  String[] param2 =new String[11];
	  param2[0]=_p0_2;
	  param2[1]=_p0_6;
	  param2[2]=_p0_3;
	  param2[3]=_p0_4;
	  param2[4]=_p0_8;
	  param2[5]=_p0_7;
	  param2[6]=_p0_5;
	  param2[7]=now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH);
	  param2[8] = _p0_18;
	  param2[9]=_p0_16;
	  param2[10]=_p0_1;
	 
	  int rowsAffected3 = apptMainBean.queryExecuteUpdate(param2,"update_bill_header");
  
 %>

<%
    int recordAffected = 0;
    ListIterator it	=	billing.getBillingItems().listIterator();
 
	while (it.hasNext()) {
    billingItem = (BillingItemBean)it.next();
    
     String[] param3 = new String[8];
       param3[0] = _p0_1;
       param3[1] = billingItem.getService_code();
       param3[2] = billingItem.getDesc();
       param3[3] = billingItem.getService_value();
       param3[4] = billingItem.getDiag_code();
       param3[5] = _p0_9;
       param3[6] = _p0_5;
           param3[7] = billingItem.getQuantity();
       recordAffected = apptMainBean.queryExecuteUpdate(param3,"save_bill_record");  
      
   %>




<%
  }
   apptMainBean.closePstmtConn();
  %>

<%
  }
   catch (java.lang.ArrayIndexOutOfBoundsException _e0) {
 }%>

<form action="billingCorrection.jsp"><input type="hidden"
	name="billing_no" value=""> <input type="submit"
	value="Correct Another One" name="submit"> <input type="button"
	value="Successful - Close this window" onClick="window.close()">
</form>
</body>
</html>