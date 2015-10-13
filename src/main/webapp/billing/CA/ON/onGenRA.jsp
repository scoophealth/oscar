<!DOCTYPE html>
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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%
    boolean isTeamBillingOnly=false;
    boolean isSiteAccessPrivacy=false;
    boolean isTeamAccessPrivacy=false; 
%>
<security:oscarSec objectName="_team_billing_only" roleName="<%=roleName$ %>" rights="r" reverse="false">
<% isTeamBillingOnly=true; %>
</security:oscarSec>
<security:oscarSec objectName="_site_access_privacy" roleName="<%=roleName$%>" rights="r" reverse="false">
	<%isSiteAccessPrivacy=true; %>
</security:oscarSec>
<security:oscarSec objectName="_team_access_privacy" roleName="<%=roleName$%>" rights="r" reverse="false">
	<%isTeamAccessPrivacy=true; %>
</security:oscarSec>

<%@ page import="java.io.*, java.sql.*, oscar.*, oscar.util.*, java.util.*" errorPage="errorpage.jsp"%>
<%@ page import="oscar.oscarBilling.ca.on.pageUtil.*"%>
<%@ page import="oscar.oscarBilling.ca.on.data.*"%>
<jsp:useBean id="documentBean" class="oscar.DocumentBean" scope="request" />

<%
JdbcBillingRAImpl dbObj = new JdbcBillingRAImpl();
Properties propRt = new Properties();

String nowDate = ""; 

String filepath="", filename = "", header="", headerCount="", total="", paymentdate="", payable="", totalStatus="", deposit=""; //request.getParameter("filename");
String transactiontype="", providerno="", specialty="", account="", patient_last="", patient_first="", provincecode="", newhin="", hin="", ver="", billtype="", location="";
String servicedate="", serviceno="", servicecode="", amountsubmit="", amountpay="", amountpaysign="", explain="", error="";
String proFirst="", proLast="", demoFirst="", demoLast="", apptDate="", apptTime="", checkAccount="", strcount="", strtCount="";
String balancefwd="",abf_ca="", abf_ad="",abf_re="",abf_de="";
String transaction="",trans_code="",cheque_indicator="", trans_date="",trans_amount="", trans_message="";
String message="", message_txt="";
String xml_ra="";

int accountno=0, totalsum=0, txFlag=0, recFlag=0, flag=0, payFlag=0, count=0, tCount=0, amountPaySum=0, amountSubmitSum=0;
String raNo = "";

ResultSet rslocal;
filename = documentBean.getFilename();

if(!filename.equals("")) {

	OscarProperties props = OscarProperties.getInstance();
	filepath = props.getProperty("DOCUMENT_DIR", "").trim(); //"/usr/local/OscarDocument/" + url +"/document/";
	dbObj.importRAFile(filepath + filename);	
} 
%>


<html>
<head>
<title><bean:message key="admin.admin.btnBillingReconciliation" /></title>
<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">

<script language="JavaScript">
<!--
var remote=null;

  
function rs(n,u,w,h,x) {
  args="width="+w+",height="+h+",resizable=yes,scrollbars=yes,status=0,top=60,left=30";
  remote=window.open(u,n,args);
  if (remote != null) {
    if (remote.opener == null)
      remote.opener = self;
  }
  if (x == 1) { return remote; }
}

var awnd=null;
function popPage(url) {
  awnd=rs('',url ,400,200,1);
  awnd.focus();
}

function checkReconcile(url){
  if(confirm("You are about to reconcile the file, are you sure?")) {
    location.href=url;
  } else{
    alert("You have cancel the action!");
  }
}
//-->
</SCRIPT>
</head>

<body>
<h3><bean:message key="admin.admin.btnBillingReconciliation" /></h3>

<div class="container-fluid well">
<button class="btn btn-primary pull-right" type='button' name='print' value='Print' onClick='window.print(); return false;'><i class="icon icon-print icon-white"></i>  Print</button><br/><br/>

<table class="table table-striped table-hover table-condensed">
<thead>
	<tr >
		<th>Read Date</th>
		<th>Payment Date</th>
		<th>Payable</th>
		<th>Records/Claims</th>
		<th>Total</th>
		<th>Action</th>
		<th>Status</th>
	</tr>
</thead>
<tbody>
	<% 

List aL;

if (isTeamBillingOnly || isTeamAccessPrivacy) {
	aL = dbObj.getTeamRahd("D", (String) session.getAttribute("user"));
}
else if (isSiteAccessPrivacy) {
	aL = dbObj.getSiteRahd("D", (String) session.getAttribute("user"));
}
else {
	aL =  dbObj.getAllRahd("D");
}

for(int i = 0; i < aL.size(); i++) {
	Properties pro = (Properties) aL.get(i);
	raNo = pro.getProperty("raheader_no");
	nowDate = pro.getProperty("readdate");
	paymentdate = pro.getProperty("paymentdate");
	payable = pro.getProperty("payable");
	strcount= pro.getProperty("claims");
	strtCount = pro.getProperty("records");
	total = pro.getProperty("totalamount");
	String status = pro.getProperty("status");
%>

	<tr <%=i%2==0?"class='myGreen'":"class='myIvory'"%>>
		<td><%=nowDate%></td>
		<td align="center"><%=paymentdate%></td>
		<td><%=payable%></td>
		<td align="center"><%=strcount%>/<%=strtCount%></td>
		<td align="right"><%=total%></td>
		<td align="center"><a
			href="../billing/CA/ON/onGenRAError.jsp?rano=<%=raNo%>&proNo="
			target="_blank">Error</a> | <a
			href="../billing/CA/ON/onGenRASummary.jsp?rano=<%=raNo%>&proNo="
			target="_blank">Summary</a>| <a
			href="../billing/CA/ON/genRADesc.jsp?rano=<%=raNo%>" target="_blank">Report
		</a></td>
		<td><%=status.compareTo("N")==0?"<a href=# onClick=\"checkReconcile('../billing/CA/ON/onGenRAsettle.jsp?rano=" + raNo +"')\">Settle</a> <a href=# onClick=\"checkReconcile('../billing/CA/ON/onGenRAsettle35.jsp?rano=" + raNo +"')\">S35</a>" : status.compareTo("S")==0?" <a href=# onClick=\"checkReconcile('../billing/CA/ON/onGenRAsettle35.jsp?rano=" + raNo +"')\">S35</a>":"Processed"%></td>
	</tr>
	<%
}
%>
</tbody>
</table>
</div>
</body>
</html>
