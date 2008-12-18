<%--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * Yi Li
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
--%>

<%      
if(session.getAttribute("user") == null) response.sendRedirect("../logout.jsp");
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
<%@ page import="oscar.oscarBilling.ca.on.data.BillingONDataHelp"%>
<%@ include file="../../../admin/dbconnection.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%@ include file="dbBilling.jsp"%>

<%
String clinicview = request.getParameter("billingform")==null?oscarVariables.getProperty("default_view"):request.getParameter("billingform");
String reportAction=request.getParameter("reportAction")==null?"":request.getParameter("reportAction");

if (request.getParameter("submit") != null && request.getParameter("submit").equals("Delete")) {
	String	sql   = "delete from clinic_location where clinic_location_no='" + request.getParameter("location_no") + "'";
	System.out.println(sql);
	BillingONDataHelp dbObj = new BillingONDataHelp();
	if(!dbObj.updateDBRecord(sql)) {
		out.println("The action is failed!!!");
	}
}
%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="billing.manageBillingLocation.title" /></title>
<link rel="stylesheet" href="billingON.css">
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
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-2">
</head>

<body leftmargin="0" topmargin="5" rightmargin="0">

<table width="100%" border="0" cellspacing="0" cellpadding="0"
	cellspacing="2" cellpadding="2">
	<tr class="myDarkGreen">
		<td height="40" width="10%"></td>
		<td width="90%" align="left"><font color="#FFFFFF" size="+2">Billing
		Location</font></td>
	</tr>
</table>

<table width="100%" border="0" bgcolor="ivory">
	<tr>
		<td width="3%"></td>
		<td width="30%" align="left" valign="top">
		<form name="serviceform" method="post"
			action="dbManageBillingLocation.jsp"><B><bean:message
			key="billing.manageBillingLocation.msgCodeDescription" /></B> <br>
		<input type="text" name="location1" size="4"> <input
			type="text" name="location1desc" size="30"> <br>
		<input type="text" name="location2" size="4"> <input
			type="text" name="location2desc" size="30"> <br>
		<input type="text" name="location3" size="4"> <input
			type="text" name="location3desc" size="30"> <br>
		<input type="text" name="location4" size="4"> <input
			type="text" name="location4desc" size="30"> <br>
		<input type="text" name="location5" size="4"> <input
			type="text" name="location5desc" size="30"> <br>
		<br>
		<input type="submit" name="action" style="width: 100px;"
			value="<bean:message key="billing.manageBillingLocation.btnAdd"/>">
		<br>
		</p>
		</form>
		</td>

		<td width="37%" valign="top">

		<table width="100%" border="0" cellspacing="2" cellpadding="2">
			<tr class="myYellow">
				<th width="6%"><bean:message
					key="billing.manageBillingLocation.msgClinicLocation" /></th>
				<th><bean:message
					key="billing.manageBillingLocation.msgDescription" /></th>
				<th>Action</th>
			</tr>

			<% 
ResultSet rs=null ;
ResultSet rs2=null ;
String[] param =new String[1];
String[] param2 =new String[10];
String[] service_code = new String[45];

param[0] = "1";
rs = apptMainBean.queryResults(param, "search_clinic_location");
int rCount = 0;
boolean bodd=false;
String servicetype_name="";

if(rs==null) {
	out.println("failed!!!"); 
} else {
%>
			<% 
	while (rs.next()) {
		bodd=bodd?false:true; //for the color of rows
%>

			<tr <%=bodd? "class=\"myGreen\"":"bgcolor='ivory'"%>>
				<form name="serviceform" method="post"
					action="manageBillingLocation.jsp"
					onsubmit="return confirmthis(<%=rs.getString("clinic_location_no")%>);">
				<td align="center"><%=rs.getString("clinic_location_no")%></td>
				<td><%=rs.getString("clinic_location_name")%></td>
				<td align="center"><input type="submit" name="submit"
					value="Delete" /> <input type="hidden" name="location_no"
					value="<%=rs.getString("clinic_location_no")%>" /></td>
				</form>
			</tr>
			<%
	}
}     

apptMainBean.closePstmtConn();
%>

		</table>

		</td>
		<td width="20%">&nbsp;</td>
	</tr>

</table>
</body>
</html:html>
