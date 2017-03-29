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
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../../securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>



<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat"%>
<%@page import="org.oscarehr.billing.CA.model.BillingInr" %>
<%@page import="org.oscarehr.common.model.Demographic" %>
<%@page import="org.oscarehr.common.dao.DemographicDao" %>
<%@page import="org.oscarehr.billing.CA.dao.BillingInrDao" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="oscar.util.ConversionUtils" %>
<%
	DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
%>


<%

GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  
  String nowDate = String.valueOf(curYear)+"/"+String.valueOf(curMonth) + "/" + String.valueOf(curDay)+ " " +now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE) + ":"+now.get(Calendar.SECOND);

String demoid="", module_id="", doctype="", docdesc="", docxml="", doccreator="", docdate="", docfilename="";
String demo_name="", demo_dob="", demo_hin="", provider="", provider_no="", provider_rma_no="", provider_ohip_no="", service_desc="", service_code="", service_amount="", service_unit="", diag_code="", errorCode="", total = "";
String billinginr_no="";
//module = request.getParameter("function");
demo_name= request.getParameter("demo_name");
String demono = request.getParameter("demono");
billinginr_no = request.getParameter("billinginr_no");
provider = request.getParameter("provider_name");


Demographic d = demographicDao.getDemographicById(Integer.parseInt(demono));
if(d != null) {
	demo_dob = MyDateFormat.getStandardDate(Integer.parseInt(d.getYearOfBirth()),Integer.parseInt(d.getMonthOfBirth()),Integer.parseInt(d.getDateOfBirth()));
	 demo_hin = d.getHin() + d.getVer().toUpperCase();	
}

%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>UPDATE INR BILLING</title>
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

function onUnbilled() {
  if(confirm("You are about to delete the inr billing record, are you sure?")) {
     serviceform.inraction.value="delete";
     serviceform.inraction.type="submit";
    serviceform.submit();
  }else{
    serviceform.inraction.value="no_delete";
    window.close()
    }
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
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF">UPDATE
		INR BILLING</font></th>
	</tr>
</table>

<table BORDER="0" CELLPADDING="1" CELLSPACING="0" WIDTH="100%"
	BGCOLOR="#C4D9E7">
	<FORM NAME="serviceform" ACTION="dbUpdateINRbilling.jsp" METHOD="POST">
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
					type="hidden" name="demono" value="<%=demono%> " size="20">
				<input type="hidden" name="billinginr_no" value="<%=billinginr_no%>">
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
					type="text" name="service_code" size="10"
					value="<%=request.getParameter("servicecode")%>"> <input
					type="hidden" name="service_unit" value="1"> </font></td>
			</tr>
			<tr>
				<td width="29%"><font size="1"
					face="Arial, Helvetica, sans-serif">Diagnostic Code</font></td>
				<td width="50%"><input type="text" name="diag_code" size="20"
					value="<%=request.getParameter("dxcode")%>"></td>
			</tr>
			<tr>
				<td width="29%"><font
					face="Verdana, Arial, Helvetica, sans-serif" color="#0000FF"
					size="1"><b><i> <input type="SUBMIT" value="update"
					name="inraction"> </i></b></font></td>
				<td width="50%"><font
					face="Verdana, Arial, Helvetica, sans-serif" size="1"> <input
					type="SUBMIT" value="delete" name="inraction"> </font></td>
			</tr>
		</table>
		<p><font face="Verdana" color="#0000FF"><b><i> </i></b></font> <br>
		</p>
		</div>
		</td>
	</tr>
	</form>
</table>
</body>
</html>
