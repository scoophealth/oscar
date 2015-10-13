<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

--%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.billing,_admin" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin&type=_admin.billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*" errorPage="errorpage.jsp"%>

<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.RaHeader" %>
<%@ page import="org.oscarehr.common.dao.RaHeaderDao" %>
<%@ page import="org.oscarehr.common.model.Billing" %>
<%@ page import="org.oscarehr.common.dao.BillingDao" %>
<%@ page import="oscar.util.ConversionUtils" %>
<%
RaHeaderDao raHeaderDao = SpringUtils.getBean(RaHeaderDao.class);
BillingDao billingDao = SpringUtils.getBean(BillingDao.class);
%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<link rel="stylesheet" href="billing.css">
<title>Billing Reconcilliation</title>
</head>

<body bgcolor="#EBF4F5" text="#000000" leftmargin="0" topmargin="0"
	marginwidth="0" marginheight="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align='LEFT'><input type='button' name='print' value='Print'
			onClick='window.print()'></th>
		<th align='CENTER'><font face="Arial, Helvetica, sans-serif"
			color="#FFFFFF">Billing Reconcilliation </font></th>
		<th align='RIGHT'><input type='button' name='close' value='Close'
			onClick='window.close()'></th>
	</tr>
</table>
<%
String filepath="", filename = "", header="", headerCount="", total="", paymentdate="", payable="", totalStatus="", deposit=""; //request.getParameter("filename");
String transactiontype="", providerno="", specialty="", account="", patient_last="", patient_first="", provincecode="", hin="", ver="", billtype="", location="";
String servicedate="", serviceno="", servicecode="", amountsubmit="", amountpay="", amountpaysign="", explain="", error="";
String proFirst="", proLast="", demoFirst="", demoLast="", apptDate="", apptTime="", checkAccount="";
int accountno=0, totalsum=0, flag=0, payFlag=0, count = 0, tCount=0, amountPaySum=0, amountSubmitSum=0;






filename = "PB0177.001";
//request.getRealPath("/download");
filepath = "/usr/local/tomcat/webapps/oscar_mch/download/";
FileInputStream file = new FileInputStream(filepath + filename);
InputStreamReader reader = new InputStreamReader(file);
BufferedReader input = new BufferedReader(reader);
String nextline;
while ((nextline=input.readLine())!=null){
%>
<% 
	header = nextline.substring(0,1);
   if (header.compareTo("H") == 0) {
   headerCount = nextline.substring(2,3);
   if (headerCount.compareTo("1") == 0){
   paymentdate = nextline.substring(21,29);
   payable = nextline.substring(29,59);
   total = nextline.substring(59,68);
   totalStatus = nextline.substring(68,69);
   deposit = nextline.substring(69,77);


   totalsum = Integer.parseInt(total);
   total = String.valueOf(totalsum);
   total = total.substring(0, total.length()-2) + "." + total.substring(total.length()-2);

          
              String raNo = "";
	  	   for(RaHeader rh:raHeaderDao.findCurrentByFilenamePaymentDate(filename,paymentdate)) {
	  	    	raNo = rh.getId().toString();
	  	    }
	  	    
	  	  
           if (raNo.compareTo("") == 0 || raNo == null){

        	   RaHeader raHeader = new RaHeader();
        	   raHeader.setFilename(filename);
        	   raHeader.setPaymentDate(paymentdate);
        	   raHeader.setTotalAmount(total);
        	   raHeader.setStatus("N");
        	   raHeaderDao.persist(raHeader);



         for(RaHeader rh:raHeaderDao.findCurrentByFilenamePaymentDate(filename,paymentdate)) {
   	  	    	raNo = rh.getId().toString();
   	  	    }
        }


   }

      if (headerCount.compareTo("4") == 0){
      transactiontype = nextline.substring(14,15);
      providerno = nextline.substring(15,21);
      specialty = nextline.substring(21,23);
      account = nextline.substring(23,31);
      patient_last = nextline.substring(31,45);
      patient_first = nextline.substring(45,50);
      hin = nextline.substring(52,64);
      ver = nextline.substring(64,66);
      billtype = nextline.substring(66,69);
      location = nextline.substring(69,73);

     count = count + 1;
      accountno= Integer.parseInt(account);
      account = String.valueOf(accountno);
            // proFirst = "";  proLast = ""; demoFirst =""; demoLast = "";  apptDate = "";   apptTime = "";

      for(Object[] res : billingDao.search_bill_generic(Integer.parseInt(account))) {
       proFirst = (String)res[3]; 
       proLast = (String)res[2];
       demoFirst = (String)res[1];
       demoLast = (String)res[0];
       apptDate = ConversionUtils.toDateString((java.util.Date)res[5]);
       apptTime = ConversionUtils.toTimeString((java.util.Date)res[6]);

        }
%>

<%
   }

   if (headerCount.compareTo("5") == 0){
      transactiontype = nextline.substring(14,15);
      servicedate = nextline.substring(15,23);
      serviceno= nextline.substring(23,25);
      servicecode = nextline.substring(25,30);
      amountsubmit = nextline.substring(31,37);
      amountpay = nextline.substring(37,43);
      amountpaysign = nextline.substring(43,44);
      explain = nextline.substring(44,46);
      payFlag = 0;
      error = "";
      tCount = tCount +1;
      amountPaySum = Integer.parseInt(amountpay );
      amountpay  = String.valueOf(amountPaySum );
      if (amountpay.compareTo("0") == 0){
      amountpay = "000";}
      amountpay = amountpay.substring(0, amountpay.length()-2) + "." + amountpay.substring(amountpay.length()-2);
       amountSubmitSum = Integer.parseInt(amountsubmit);
        amountsubmit  = String.valueOf(amountSubmitSum );
           if (amountsubmit.compareTo("0") == 0){
      amountsubmit = "000";}
       amountsubmit =amountsubmit.substring(0, amountsubmit.length()-2) + "." + amountsubmit.substring(amountsubmit.length()-2);
      if (explain.compareTo("  ")==0 || explain.compareTo("")==0){
      payFlag = 0;
      }
      else{
      if (explain.compareTo("I2")==0) {
      payFlag = 0;
      }
      else{
      payFlag = 1;
      error = explain;
      }
      }
      if(checkAccount.compareTo(account) == 0){
      flag=0;       %>

<%
      }
      else {
      flag = 1;
      checkAccount = account;
      %>

<%
      }

   }


   }
%>




<%



}


%>



<table width="100%" border="1" cellspacing="0" cellpadding="0"
	bgcolor="#EFEFEF">
	<form>
	<tr>
		<td width="12%" height="16">File Name:</td>
		<td width="33%" height="16"><input type="text" name="textfield2"
			value="<%=filename%>" style="width: 100%"></td>
		<td width="16%" height="16">Payment Date:</td>
		<td width="35%" height="16"><input type="text" name="textfield"
			value="<%=paymentdate%>" style="width: 100%"></td>
	</tr>
	<tr>
		<td width="12%">Payable To:</td>
		<td width="33%"><input type="text" name="textfield3"
			value="<%=payable%>" style="width: 100%"></td>
		<td width="16%">Total:</td>
		<td width="35%"><input type="text" name="textfield4"
			value="<%=total%>" style="width: 100%"></td>
	</tr>
	<tr>
		<td width="12%" height="16">Total Record:</td>
		<td width="33%" height="16"><input type="text" name="textfield5"
			value="<%=count%>" style="width: 100%"></td>
		<td width="16%" height="16">Total Transaction:</td>
		<td width="35%" height="16"><input type="text" name="textfield6"
			value="<%=tCount%>" style="width: 100%"></td>
	</tr>
	</form>
</table>

</body>
</html>
