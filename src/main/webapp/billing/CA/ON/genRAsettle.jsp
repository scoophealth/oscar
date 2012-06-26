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
<% 
    if(session.getValue("user") == null) response.sendRedirect("../../../logout.jsp");
%>

<%@ page
	import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, oscar.util.*, java.net.*,oscar.MyDateFormat"
	errorPage="errorpage.jsp"%>

<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%@ include file="dbBilling.jspf"%>

<% 
String raNo = "", flag="", plast="", pfirst="", pohipno="", proNo="";
String filepath="", filename = "", header="", headerCount="", total="", paymentdate="", payable="", totalStatus="", deposit=""; //request.getParameter("filename");
String transactiontype="", providerno="", specialty="", account="", patient_last="", patient_first="", provincecode="", hin="", ver="", billtype="", location="";
String servicedate="", serviceno="", servicecode="", amountsubmit="", amountpay="", amountpaysign="", explain="", error="";
String proFirst="", proLast="", demoFirst="", demoLast="", apptDate="", apptTime="", checkAccount="";
String errorAccount ="", eFlag="", noErrorAccount="";


raNo = request.getParameter("rano");
if (raNo == null || raNo.compareTo("") == 0) return;

ArrayList noErrorBill = new ArrayList();
ArrayList errorBill = new ArrayList();
String[] param0 = new String[2];
String[] param = new String[4];
param[0] = raNo;
param[1] = "I2";
param[2] = "35";
param[3] = proNo+"%";
//  rsdemo2 = null;
ResultSet rsdemo = apptMainBean.queryResults(param, "search_raerror35");
while (rsdemo.next()) {   
	account = rsdemo.getString("billing_no");
	errorBill.add((String) account);				  
}

account = "";
rsdemo = apptMainBean.queryResults(param, "search_ranoerror35");
while (rsdemo.next()) {   
	account = rsdemo.getString("billing_no");
	eFlag="1";
	for (int i=0; i< errorBill.size(); i++){
		errorAccount = (String) errorBill.get(i);
		if(errorAccount.compareTo(account)==0) {
			eFlag = "0";
			break;
		}
	}

	if(eFlag.compareTo("1")==0) noErrorBill.add(account);
}      

for (int j=0; j< noErrorBill.size();j++){
	noErrorAccount=(String) noErrorBill.get(j);
	//    ResultSet rsdemo2 = null;
	//    rsdemo2 = apptMainBean.queryResults(noErrorAccount, "search_bill_record");
	//      while (rsdemo2.next()) {   
	//    servicecode = rsdemo2.getString("service_code");
	//    amountsubmit = rsdemo2.getString("billing_amount");
	//    count = count + 1;
	//    }

	//    String[] param3 = new String[2];
	//                               param3[0] = raNo;
	//       param3[1] = noErrorAccount;
	//       
	//     ResultSet rsdemo3 = null;
	//         	           rsdemo3 = apptMainBean.queryResults(param3, "search_rabillno");
	//         	             while (rsdemo3.next()) {   
	//         	           serviceno = rsdemo3.getString("service_code");
	//         	           amountpay = rsdemo3.getString("amountpay");
	//   count1 = count1 +1 ;      	           
	//   }

	int recordAffected = apptMainBean.queryExecuteUpdate(noErrorAccount,"update_billhd");
}

String[] paramx = new String[2]; 
paramx[0] = "S";
paramx[1] = raNo;
int recordAffected1 = apptMainBean.queryExecuteUpdate(paramx,"update_rahd_status");
%>

<script LANGUAGE="JavaScript">
	self.close();
	self.opener.refresh();
</script>
