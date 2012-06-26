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
    if(session.getAttribute("user") == null) response.sendRedirect("../../../logout.jsp");
%>

<%@ page
	import="java.math.*, java.util.*, java.io.*, java.sql.*, java.net.*, oscar.*, oscar.util.*, oscar.MyDateFormat"
	errorPage="errorpage.jsp"%>
<%@ page import="oscar.oscarBilling.ca.on.pageUtil.*"%>

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

raNo = request.getParameter("rano") != null ? request.getParameter("rano") : "";
if (raNo.compareTo("") == 0)  return;

ArrayList noErrorBill = new ArrayList();
ArrayList errorBill = new ArrayList();
ArrayList errorBillNoQ = new ArrayList();

String[] param0 = new String[2];
param0[0] = raNo;
param0[1] = proNo+"%";
String[] param = new String[4];
param[0] = raNo;
param[1] = "I2";
param[2] = "35";
param[3] = proNo+"%";

ResultSet rsdemo = apptMainBean.queryResults(param, "search_raerror35");
while (rsdemo.next()) {   
	account = rsdemo.getString("billing_no");
	errorBill.add((String) account);
	if(!rsdemo.getString("service_code").matches("Q011A|Q020A|Q130A|Q131A|Q132A|Q133A|Q140A|Q141A|Q142A")) {
		errorBillNoQ.add((String) account);
	}
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

// settle Qcodes
account = "";
rsdemo = apptMainBean.queryResults(param0, "search_ranoerrorQ");
while (rsdemo.next()) {   
	account = rsdemo.getString("billing_no");
	eFlag="1";
	for (int i=0; i< errorBillNoQ.size(); i++){
		errorAccount = (String) errorBillNoQ.get(i);
		if(errorAccount.compareTo(account)==0) {
			eFlag = "0";
			break;
		}
	}

	if(eFlag.compareTo("1")==0) noErrorBill.add(account);
}      

BillingRAPrep obj = new BillingRAPrep();
for (int j=0; j< noErrorBill.size();j++){
	noErrorAccount=(String) noErrorBill.get(j);
	obj.updateBillingStatus(noErrorAccount, "S");
	//int recordAffected = apptMainBean.queryExecuteUpdate(noErrorAccount,"update_billhd");
}

String[] paramx = new String[2]; 
paramx[0] = "F";
paramx[1] = raNo;

int recordAffected1 = apptMainBean.queryExecuteUpdate(paramx,"update_rahd_status");
%>

<script LANGUAGE="JavaScript">
self.close();
self.opener.refresh();
</script>
