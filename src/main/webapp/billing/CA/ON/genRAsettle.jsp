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
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.model.RaHeader" %>
<%@page import="org.oscarehr.common.dao.RaHeaderDao" %>
<%@page import="org.oscarehr.common.model.Billing" %>
<%@page import="org.oscarehr.common.dao.BillingDao" %>
<%
	RaHeaderDao dao = SpringUtils.getBean(RaHeaderDao.class);
	BillingDao billingDao = SpringUtils.getBean(BillingDao.class);
%>

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


	int recordAffected = 0;
	for(Billing b:billingDao.findActive(Integer.parseInt(noErrorAccount))) {
          if(b != null) {
        	  b.setStatus("S");
        	  billingDao.merge(b);
        	  recordAffected++;
          }
      }
}

int recordAffected1 = 0;

RaHeader raHeader = dao.find(Integer.parseInt(raNo));
if(raHeader != null) {
	 raHeader.setStatus("S");
	 dao.merge(raHeader);
	recordAffected1++;
}
%>

<script LANGUAGE="JavaScript">
	self.close();
	self.opener.refresh();
</script>
