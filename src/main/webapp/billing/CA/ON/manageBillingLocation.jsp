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

<%
String user_no = (String) session.getAttribute("user");
String asstProvider_no = "";
String color ="";
String premiumFlag="";
String service_form="", service_name="";
%>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.net.*" errorPage="errorpage.jsp"%>
<%@ include file="../../../admin/dbconnection.jsp"%>

<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.model.ClinicLocation" %>
<%@page import="org.oscarehr.common.dao.ClinicLocationDao" %>
<%
	ClinicLocationDao clinicLocationDao = (ClinicLocationDao)SpringUtils.getBean("clinicLocationDao");
%>
<%
String clinicview = request.getParameter("billingform")==null?oscarVariables.getProperty("default_view"):request.getParameter("billingform");
String reportAction=request.getParameter("reportAction")==null?"":request.getParameter("reportAction");

if (request.getParameter("submit") != null && request.getParameter("submit").equals("Delete")) {
	clinicLocationDao.removeByClinicLocationNo(request.getParameter("location_no"));
}
%>

<html:html locale="true">
<head>
<title><bean:message key="admin.admin.btnAddBillingLocation" /></title>
<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">
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
function confirmthis(lno) {
  if(confirm("Are you sure that you want to delete the location " + lno + "?")) {
    return true;
  } else {
    return false;
  }
}
//-->
</script>
</head>

<body>
<h3><bean:message key="admin.admin.btnAddBillingLocation" /></h3>
<div class="container-fluid well">
<table>
	<tr>
		<td width="3%"></td>
		<td width="30%" align="left" valign="top">
		<form name="serviceform" method="post"
			action="dbManageBillingLocation.jsp"><B><bean:message
			key="billing.manageBillingLocation.msgCodeDescription" /></B> <br>
		<input style="width:40px" type="text" name="location1" size="4"> <input type="text" name="location1desc" size="30"> <br>
		<input style="width:40px" type="text" name="location2" size="4"> <input type="text" name="location2desc" size="30"> <br>
		<input style="width:40px" type="text" name="location3" size="4"> <input type="text" name="location3desc" size="30"> <br>
		<input style="width:40px" type="text" name="location4" size="4"> <input type="text" name="location4desc" size="30"> <br>
		<input style="width:40px" type="text" name="location5" size="4"> <input type="text" name="location5desc" size="30"> <br>
		<br>
		<input class="btn btn-primary" type="submit" name="action" value="<bean:message key="billing.manageBillingLocation.btnAdd"/>">
		<br>
		</p>
		</form>
		</td>

		<td width="37%" valign="top">

		<table class="table table-striped  table-condensed">
			<tr>
				<th width="6%"><bean:message
					key="billing.manageBillingLocation.msgClinicLocation" /></th>
				<th><bean:message
					key="billing.manageBillingLocation.msgDescription" /></th>
				<th>Action</th>
			</tr>

			<%

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
				<form name="serviceform" method="post"
					action="manageBillingLocation.jsp"
					onsubmit="return confirmthis(<%=clinicLocation.getClinicLocationNo()%>);">
				<td align="center"><%=clinicLocation.getClinicLocationNo()%></td>
				<td><%=clinicLocation.getClinicLocationName()%></td>
				<td align="center"><input class="btn" type="submit" name="submit"
					value="Delete" /> <input type="hidden" name="location_no"
					value="<%=clinicLocation.getClinicLocationNo()%>" /></td>
				</form>
			</tr>
			<%
	}
}
%>

		</table>

		</td>
	</tr>

</table>
</div>
</body>
</html:html>
