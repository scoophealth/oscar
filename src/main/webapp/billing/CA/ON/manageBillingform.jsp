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
      String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed2=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_admin.billing,_admin" rights="w" reverse="<%=true%>">
	<%authed2=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin&type=_admin.billing");%>
</security:oscarSec>
<%
if(!authed2) {
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
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.CtlBillingService" %>
<%@ page import="org.oscarehr.common.dao.CtlBillingServiceDao" %>
<%@ page import="org.oscarehr.common.model.CtlDiagCode" %>
<%@ page import="org.oscarehr.common.dao.CtlDiagCodeDao" %>
<%@ page import="org.oscarehr.common.model.CtlBillingServicePremium" %>
<%@ page import="org.oscarehr.common.dao.CtlBillingServicePremiumDao" %>
<%
	CtlBillingServiceDao ctlBillingServiceDao = SpringUtils.getBean(CtlBillingServiceDao.class);
	CtlDiagCodeDao ctlDiagCodeDao = SpringUtils.getBean(CtlDiagCodeDao.class);
	CtlBillingServicePremiumDao ctlBillingServicePremiumDao = SpringUtils.getBean(CtlBillingServicePremiumDao.class);
	
%>


<%
String clinicview = request.getParameter("billingform")==null?oscarVariables.getProperty("default_view"):request.getParameter("billingform");
String reportAction=request.getParameter("reportAction")==null?"":request.getParameter("reportAction");
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="billing.manageBillingform.title" /></title>
<link rel="stylesheet" href="billing.css">

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<script type="text/javascript"
	src="../../../share/javascript/prototype.js"></script>
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
		form.action = "dbManageBillingform_add.jsp";
		form.submit();
	}
}

function validateServiceType() {
	if (document.servicetypeform.typeid.value == "MFP") {
		alert("<bean:message key="billing.manageBillingform.msgIDExists"/>");
		return false;
	}
	
	if (document.servicetypeform.typeid.value == '') {
		alert("<bean:message key="billing.manageBillingform.btnManage.msgRequiredField"/>");
		return false;
	}
	return true;
}

function refresh() {
  var u = self.location.href;
  if(u.lastIndexOf("view=1") > 0) {
    self.location.href = u.substring(0,u.lastIndexOf("view=1")) + "view=0" + u.substring(eval(u.lastIndexOf("view=1")+6));
  } else {
    history.go(0);
  }
}

function manageType(stype,stype_name) {
    url  = "manageBillingform_billtype.jsp";
    pars = "type_id=" + stype + "&type_name=" + stype_name;
    
    var myAjax = new Ajax.Updater("manage_type", url, {method:"get", parameters:pars});
    showManageType(true);
}

function onUnbilled(url) {
  if(confirm("<bean:message key="billing.manageBillingform.msgDeleteBillingConfirm"/>")) {
    popupPage(700,720, url);
  }
}

function showManageType(cmd) {
    var p = $("manage_type");
    if (cmd) p.show();
    else p.hide();
}

function manageBillType(id,oldtype,newtype) {
    url = "dbManageBillingform_billtype.jsp";
    pars = "?servicetype="+id+"&billtype_old="+oldtype+"&billtype="+newtype;
    popupPage(700,720,url+pars);
}
//-->
</script>
</head>

<body leftmargin="0" topmargin="5" rightmargin="0"
	onload="showManageType(false);">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="#000000">
		<td height="40" width="10%"></td>
		<td width="90%" align="left">
		<p><font face="Verdana, Arial, Helvetica, sans-serif"
			color="#FFFFFF"><b><font
			face="Arial, Helvetica, sans-serif" size="4">oscar<font
			size="3"><bean:message
			key="billing.manageBillingform.msgBilling" /></font></font></b></font></p>
		</td>
	</tr>
</table>

<table width="100%" border="0" bgcolor="#EEEEFF">
	<form name="serviceform" method="post" action="manageBillingform.jsp">
	<tr>
		<td width="30%" align="right"><font size="2" color="#333333"
			face="Verdana, Arial, Helvetica, sans-serif"> <input
			type="radio" name="reportAction" value="servicecode"
			<%=reportAction.equals("servicecode")?"checked":""%>> <bean:message
			key="billing.manageBillingform.formServiceCode" /> <input
			type="radio" name="reportAction" value="dxcode"
			<%=reportAction.equals("dxcode")?"checked":""%>> <bean:message
			key="billing.manageBillingform.formDxCode" /></font></td>
		<td width="50%" align="center"><font
			face="Verdana, Arial, Helvetica, sans-serif" size="2" color="#333333"><b><bean:message
			key="billing.manageBillingform.formSelectForm" /></b></font> <select
			name="billingform">
			<option value="000" <%=clinicview.equals("000")?"selected":""%>><bean:message
				key="billing.manageBillingform.formAddDelete" /></option>
			<option value="***" <%=clinicview.equals("***")?"selected":""%>><bean:message
				key="billing.manageBillingform.formManagePremium" /></option>

<% 
String serviceType="";
String serviceTypeName="";
List<Object[]> billingServices = ctlBillingServiceDao.findServiceTypes();

for(Object[] billingService:billingServices){
	serviceType = String.valueOf(billingService[0]);
	serviceTypeName = String.valueOf(billingService[1]);
%>
			<option value="<%=serviceType%>"
				<%=clinicview.equals(serviceType)?"selected":""%>><%=serviceTypeName%></option>
<%
}      
%>
		</select></td>
		<td width="40%"><font color="#333333" size="2"
			face="Verdana, Arial, Helvetica, sans-serif"> <input
			type="submit" name="Submit" style=""
			value="<bean:message key="billing.manageBillingform.btnManage"/>">
		</font></td>
	</tr>
	</form>
</table>

<%
if (clinicview.compareTo("000") == 0) { %>
<%@ include file="manageBillingform_add.jspf"%>
<%} else if (clinicview.compareTo("***") == 0) { %>
<%@ include file="manageBillingform_premium.jspf"%>
<%} else if (reportAction.compareTo("") == 0 || reportAction == null){ %>
<p>&nbsp;</p>
<%} else if (reportAction.compareTo("servicecode") == 0) {  %>
<%@ include file="manageBillingform_service.jspf"%>
<%} else if (reportAction.compareTo("dxcode") == 0) { %>
<%@ include file="manageBillingform_dx.jspf"%>
<%
}
%>


</body>
</html:html>
