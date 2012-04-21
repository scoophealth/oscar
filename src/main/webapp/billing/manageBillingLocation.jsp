<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<%

String user_no = (String) session.getAttribute("user");
String asstProvider_no = "";
String color ="";
String premiumFlag="";
String service_form="", service_name="";
%>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.net.*"
	errorPage="errorpage.jsp"%>
<%@ include file="../admin/dbconnection.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%@ include file="dbBilling.jspf"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.dao.ClinicLocationDao" %>
<%@page import="org.oscarehr.common.model.ClinicLocation" %>
<%
	ClinicLocationDao clinicLocationDao = (ClinicLocationDao)SpringUtils.getBean("clinicLocationDao");
%>
<%
String clinicview = request.getParameter("billingform")==null?oscarVariables.getProperty("default_view"):request.getParameter("billingform");
String reportAction=request.getParameter("reportAction")==null?"":request.getParameter("reportAction");
%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="billing.manageBillingLocation.title" /></title>
<link rel="stylesheet" href="billing.css">

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<script language="JavaScript">
<!--

function selectprovider(s) {
  if(self.location.href.lastIndexOf("&providerview=") > 0 ) a = self.location.href.substring(0,self.location.href.lastIndexOf("&providerview="));
  else a = self.location.href;
	self.location.href = a + "&providerview=" +s.options[s.selectedIndex].value ;
}
function openBrWindow(theURL,winName,features) {
  window.open(theURL,winName,features);
}
function setfocus() {
  this.focus();
  document.ADDAPPT.keyword.focus();
  document.ADDAPPT.keyword.select();
}

function valid(form){
if (validateServiceType(form)){
form.action = "dbManageBillingform_add.jsp"
form.submit()}

else{}
}
function validateServiceType() {
  if (document.servicetypeform.typeid.value == "MFP") {
alert("<bean:message key="billing.manageBillingLocation.msgServiceTypeExists"/>");
	return false;
 }
 else{
 return true;
}

}
function refresh() {
  var u = self.location.href;
  if(u.lastIndexOf("view=1") > 0) {
    self.location.href = u.substring(0,u.lastIndexOf("view=1")) + "view=0" + u.substring(eval(u.lastIndexOf("view=1")+6));
  } else {
    history.go(0);
  }
}
//-->
</script>
</head>

<body leftmargin="0" topmargin="5" rightmargin="0">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="#000000">
		<td height="40" width="10%"></td>
		<td width="90%" align="left">
		<p><font face="Verdana, Arial, Helvetica, sans-serif"
			color="#FFFFFF"><b><font
			face="Arial, Helvetica, sans-serif" size="4">oscar<font
			size="3"><bean:message
			key="billing.manageBillingLocation.msgBilling" /></font></font></b></font></p>
		</td>
	</tr>
</table>

<table width="100%" border="0" bgcolor="#EEEEFF">
	<tr>
		<td width="27%" align="left">
		<form name="serviceform" method="post"
			action="dbManageBillingLocation.jsp">
		<p><bean:message
			key="billing.manageBillingLocation.msgCodeDescription" /></p>
		<input type="text" name="location1" size="10"> <input
			type="text" name="location1desc" size="30"> <br>
		<input type="text" name="location2" size="10"> <input
			type="text" name="location2desc" size="30"> <br>
		<input type="text" name="location3" size="10"> <input
			type="text" name="location3desc" size="30"> <br>
		<input type="text" name="location4" size="10"> <input
			type="text" name="location4desc" size="30"> <br>
		<input type="text" name="location5" size="10"> <input
			type="text" name="location5desc" size="30"> <br>
		<br>
		<input type="submit" name="action"
			value="<bean:message key="billing.manageBillingLocation.btnAdd"/>">
		<br>
		</p>
		</form>
		</td>

		<td width="39%" valign="top">

		<table width="90%" border="0" cellspacing="2" cellpadding="2">
			<tr>
				<td><bean:message
					key="billing.manageBillingLocation.msgClinicLocation" /></td>
				<td><bean:message
					key="billing.manageBillingLocation.msgDescription" /></td>
			</tr>

			<%
ResultSet rs2=null ;
String[] param2 =new String[10];
String[] service_code = new String[45];

List<ClinicLocation> clinicLocations = clinicLocationDao.findByClinicNo(1);
int rCount = 0;
boolean bodd=false;
String servicetype_name="";

if(clinicLocations.size()==0) {
	out.println("failed!!!");
} else {
%>
			<%
	for (ClinicLocation clinicLocation:clinicLocations) {
		bodd=bodd?false:true; //for the color of rows
%>

			<tr>
				<td><%=clinicLocation.getClinicLocationNo()%></td>
				<td><%=clinicLocation.getClinicLocationName()%></td>
			</tr>
			<%
	}
}
%>

		</table>

		</td>
		<td width="34%">&nbsp;</td>
	</tr>

</table>
</body>
</html:html>
