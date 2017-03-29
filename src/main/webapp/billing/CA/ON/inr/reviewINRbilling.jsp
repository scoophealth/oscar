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
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../../securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>



<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.BillingService" %>
<%@ page import="org.oscarehr.common.dao.BillingServiceDao" %>
<%
	BillingServiceDao billingServiceDao= SpringUtils.getBean(BillingServiceDao.class);
%>


<%

GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  
  String nowDate = String.valueOf(curYear)+"/"+String.valueOf(curMonth) + "/" + String.valueOf(curDay)+ " " +now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE) + ":"+now.get(Calendar.SECOND);

String demoid="", module_id="", doctype="", docdesc="", docxml="", doccreator="", docdate="", docfilename="";
String demo_name="", demo_dob="", demo_hin="", provider="", provider_no="", provider_rma_no="", provider_ohip_no="", service_desc="", service_code="", service_amount="", service_unit="", diag_code="", errorCode="", total = "";
//module = request.getParameter("function");
demo_name= request.getParameter("demo_name");
demo_dob = request.getParameter("demo_dob");
demo_hin = request.getParameter("demo_hin");
provider = request.getParameter("provider");
if (provider.trim().length() <= 0) { errorCode = "Please select a provider.<br>"; } else{
provider_ohip_no = provider.substring(0,6);
provider_no = provider.substring(7);
}

demoid = request.getParameter("functionid");

service_code = request.getParameter("xml_other1");
if (service_code.trim().compareTo("") == 0){
errorCode = errorCode + "Please input a service code.<br>"; }else{
service_code = service_code.substring(0,5);


for(BillingService bs:billingServiceDao.getBillingCodeAttr(service_code)) {
	service_desc = bs.getDescription();
	service_code = bs.getServiceCode();
	service_amount = bs.getValue();
}

 
}
diag_code = request.getParameter("xml_diagnostic_detail");

if (diag_code.trim().compareTo("") == 0){
errorCode = errorCode + "Please input a diagnostic code.<br>";


}else{
diag_code = diag_code.substring(0,3);
String numCode = "";
for(int i=0;i<diag_code.length();i++)
 {
 String c = diag_code.substring(i,i+1);
 if(c.hashCode()>=48 && c.hashCode()<=58)
 numCode += c;
 
 }
 
 
 if (numCode.length() < 3) {
 // diagnostic_code = "000|Other code";
 diag_code="000";
 errorCode = errorCode + "Please input a diagnostic code.<br>";
 }
 
 
 
} 




doccreator = request.getParameter("doccreator");
docdate = request.getParameter("docdate");
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>INR BILLING</title>
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
function ScriptAttach() {
  f0 = escape(document.serviceform.xml_diagnostic_detail.value);
  f1 = document.serviceform.xml_dig_search1.value;
 // f2 = escape(document.serviceform.elements["File2Data"].value);
 // fname = escape(document.Compose.elements["FName"].value);
  awnd=rs('att','../billingDigSearch.jsp?name='+f0 + '&search=' + f1,600,600,1);
  awnd.focus();
}



function OtherScriptAttach() {
  t0 = escape(document.serviceform.xml_other1.value);
 // t1 = escape(document.serviceform.xml_other2.value);
 // t2 = escape(document.serviceform.xml_other3.value);
 // f1 = document.serviceform.xml_dig_search1.value;
 // f2 = escape(document.serviceform.elements["File2Data"].value);
 // fname = escape(document.Compose.elements["FName"].value);
  awnd=rs('att','../billingCodeSearch.jsp?name='+t0 + '&name1=' + "" + '&name2=' + "" + '&search=',600,600,1);
  awnd.focus();
}
//-->
</script>
<link rel="stylesheet" href="../../web.css" />
</head>

<body onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF">REVIEW
		INR BILLING</font></th>
	</tr>
</table>
<%
if (errorCode.compareTo("") != 0) {
%>

<form><%=errorCode%> <input type=button name=back
	onClick="javascript:history.go(-1);return false;"></form>
<%} else {%>
<table BORDER="0" CELLPADDING="1" CELLSPACING="0" WIDTH="100%"
	BGCOLOR="#C4D9E7">
	<FORM NAME="serviceform" ACTION="dbINRbilling.jsp" METHOD="POST">
	<tr valign="top">
		<td rowspan="2" ALIGN="right" valign="middle">
		<div align="center">
		<p>&nbsp;</p>
		<table width="80%" border="1" cellspacing="0" cellpadding="0">
			<tr>
				<td colspan="3"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"></font></td>
			</tr>
			<tr>
				<td width="29%"><font face="Arial, Helvetica, sans-serif"
					color="#000000" size="1">Demographic Name </font></td>
				<td width="50%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"> <input
					type="hidden" name="demoid" value="<%=demoid%> " size="20">
				<input type="text" name="demo_name" value="<%=demo_name%> "
					size="20" readonly> </font></td>
				<td rowspan="9" width="21%" valign="middle">
				<p><br>
				</p>
				</td>
			</tr>
			<tr>
				<td width="29%"><font size="1"
					face="Arial, Helvetica, sans-serif">Demographic HIN</font></td>
				<td width="50%"><input type="text" name="demo_hin"
					value="<%=demo_hin%> " size="20" readonly></td>
			</tr>
			<tr>
				<td width="29%"><font size="1"
					face="Arial, Helvetica, sans-serif">Demographic DOB</font></td>
				<td width="50%"><input type="text" name="demo_dob"
					value="<%=demo_dob%> " size="20" readonly></td>
			</tr>
			<tr>
				<td width="29%"><font face="Arial, Helvetica, sans-serif"
					color="#000000" size="1">Service Code </font></td>
				<td width="50%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"> <input
					type="text" name="service_code" size="10" value="<%=service_code%>"
					readonly> <input type="text" name="service_amount"
					size="10" value="<%=service_amount%>" readonly> <input
					type="hidden" name="service_unit" value="1"> </font></td>
			</tr>
			<tr>
				<td width="29%"><font size="1"
					face="Arial, Helvetica, sans-serif">Diagnostic Code</font></td>
				<td width="50%"><input type="text" name="diag_code" size="20"
					value="<%=diag_code%>" readonly></td>
			</tr>
			<tr>
				<td width="29%"><font face="Arial, Helvetica, sans-serif"
					color="#000000" size="1">Create Date</font></td>
				<td width="50%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"> <input
					type="text" name="docdate" readonly value="<%=nowDate%>" size="20">
				</font></td>
			</tr>
			<tr>
				<td width="29%"><font
					face="Verdana, Arial, Helvetica, sans-serif" color="#0000FF"
					size="1"><b><i> <input type="SUBMIT" value="Submit"
					name="SUBMIT"> </i></b></font></td>
				<td width="50%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"> <input
					type="hidden" name="doccreator"
					value="<%=request.getParameter("doccreator")%>" size="20">
				<input type="hidden" name="orderby" value="updatedatetime desc"
					size="20"> <input type="hidden" name="provider_no"
					value="<%=provider_no%>"> <input type="hidden"
					name="provider_ohip_no" value="<%=provider_ohip_no%>"> <input
					type="hidden" name="provider_rma_no" value="<%=provider_rma_no%>">
				<input type="hidden" name="service_desc" value="<%=service_desc%>">
				</font></td>
			</tr>
		</table>
		<p><font face="Verdana" color="#0000FF"><b><i> </i></b></font> <br>
		</p>
		</div>
		</td>
	</tr>
	</form>
</table>
<% 
}
%>
