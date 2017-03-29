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

<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, oscar.util.*, java.net.*,oscar.MyDateFormat" errorPage="errorpage.jsp"%>
<%@ page import="oscar.oscarBilling.ca.on.pageUtil.*"%>

<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.model.RaHeader" %>
<%@page import="org.oscarehr.common.dao.RaHeaderDao" %>
<%@page import="org.oscarehr.common.model.RaDetail" %>
<%@page import="org.oscarehr.common.dao.RaDetailDao" %>
<%@page import="org.oscarehr.common.model.Billing" %>
<%@page import="org.oscarehr.common.dao.BillingDao" %>
<%
	RaHeaderDao dao = SpringUtils.getBean(RaHeaderDao.class);
	BillingDao billingDao = SpringUtils.getBean(BillingDao.class);
	RaDetailDao raDetailDao = SpringUtils.getBean(RaDetailDao.class);
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

for(RaDetail rad:raDetailDao.search_raerror35(Integer.parseInt(raNo),"I2","35",proNo+"%")) {
	account = String.valueOf(rad.getBillingNo());
	errorBill.add(account);	
}


account = "";
List<Integer> res = raDetailDao.search_ranoerror35(Integer.parseInt(raNo),"I2","35",proNo+"%");

for (Integer r:res) {   
	account = String.valueOf(r);
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

BillingRAPrep obj = new BillingRAPrep();
for (int j=0; j< noErrorBill.size();j++){
	noErrorAccount=(String) noErrorBill.get(j);
	obj.updateBillingStatus(noErrorAccount, "S");
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

