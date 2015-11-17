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
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.Security" %>
<%@ page import="org.oscarehr.util.LoggedInInfo" %>
<%@ page import="org.oscarehr.managers.SecurityManager" %>
<%@page import="org.oscarehr.common.dao.SecurityDao" %>


<%
	LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
	org.oscarehr.managers.SecurityManager securityManager = SpringUtils.getBean(org.oscarehr.managers.SecurityManager.class);
	
  String errormsg = "";
  if (request.getParameter("errormsg") != null) {
		   errormsg = request.getParameter("errormsg") ;
  }     
  
  String curUser_no = (String) session.getAttribute("user");
  Security s = securityManager.findByProviderNo(loggedInInfo, curUser_no);
  
  
  Integer BLocallockset = s.getBLocallockset();
  Integer BRemotelockset = 	s.getBRemotelockset();	  
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page
	import="java.lang.*, java.util.*, java.text.*,java.sql.*, oscar.*"
	errorPage="errorpage.jsp"%>

<%!
	OscarProperties op = OscarProperties.getInstance();
%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/checkPassword.js.jsp"></script>
<title><bean:message key="provider.providerchangepassword.title" /></title>
<script language="javascript">
function setfocus(el) {
	this.focus();
	document.updatepassword.elements[el].focus();
	document.updatepassword.elements[el].select();
}
function checkPwdPolicy() {
	
	var pwd1=document.updatepassword.mypassword.value;
	var pwd2=document.updatepassword.confirmpassword.value;

	var pin1 = null;
	var pin2 = null;
	
	var jsLocallockset = '<%=BLocallockset%>'; 
	var jsRemotelockset = '<%=BRemotelockset%>';
	
	if (jsLocallockset == '1' || jsRemotelockset == '1') {
		 pin1=document.updatepassword.newpin.value;
		 pin2=document.updatepassword.confirmpin.value;
	}
	
	if (pwd1 == '' && (pin1 == null || pin1 == '')) {
		alert ('<bean:message key="provider.providerchangepassword.msgPasswordAndPINBlank"/>');
		setfocus('mypassword');
		return false;
	}
	
	if (pwd1 != "") {
		if (document.updatepassword.oldpassword.value == "") {
			alert ('<bean:message key="provider.providerchangepassword.msgCurrPasswordError"/>');
			setfocus('oldpassword');
			return false;
		}
		if (!validatePassword(pwd1)) {
			setfocus('mypassword');
			return false;
		}
		if (pwd1 != pwd2) {
			alert ('<bean:message key="provider.providerchangepassword.msgPasswordConfirmError"/>');
			setfocus('confirmpassword');
			return false;
		}
    }

	if (jsLocallockset == '0' && jsRemotelockset == '0') {
		return true;
	}

	if (pin1 != "") {
		if (document.updatepassword.pin.value == "") {
			alert ('<bean:message key="provider.providerchangepassword.msgCurrPinError"/>');
			setfocus('pin');
			return false;
		}
		
		var pin_min_length = <%=op.getProperty("password_pin_min_length")%>;
		
		if (pin1.length < pin_min_length) {
			alert('<bean:message key="password.policy.violation.msgPinLengthError"/> ' +
					pin_min_length + ' <bean:message key="password.policy.violation.msgDigits"/>');
			return false;
		}
		
		if (pin1 != pin2) {
			alert ('<bean:message key="provider.providerchangepassword.msgPinConfirmError"/>');
			setfocus('confirmpin');
			return false;
		}
	}
	
	return true;
}
</script>
</head>

<body onLoad="setfocus('oldpassword')" topmargin="0" leftmargin="0" rightmargin="0">
<FORM NAME="updatepassword" METHOD="post"
	ACTION="providerupdatepassword.jsp" onSubmit="return(checkPwdPolicy())">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF"><bean:message
			key="provider.providerchangepassword.description" /></font></th>
	</tr>
</table>

<p><b><font color='red'><%=errormsg%></font></b>

<table width="100%" border="0" cellpadding="2" bgcolor="#eeeeee">
	<tr>
		<td><font face="arial" size="2"><bean:message
			key="provider.providerchangepassword.msgInstructions" /> <b><bean:message
			key="provider.providerchangepassword.msgUpdate" /></b> <bean:message
			key="provider.providerchangepassword.msgClickButton" /></font></td>
	</tr>
</table>
<center>

<table border="0" width="100%" cellpadding="4" cellspacing="0">
	<tr>
		<td align="right" width="50%"><font face="arial"><bean:message
			key="provider.providerchangepassword.msgEnterOld" /> &nbsp;<b><bean:message
			key="provider.providerchangepassword.formCurrPassword" />:</b></font></td>
		<td><input type=password name="oldpassword" value="" size=20
			maxlength=32></td>
	</tr>
	<tr>
		<td width="50%" align="right"><font face="arial"><bean:message
			key="provider.providerchangepassword.msgChooseNew" /> &nbsp;<b><bean:message
			key="provider.providerchangepassword.formNewPassword" />:</b></font></td>
		<td><input type=password name="mypassword" value="" size=20
			maxlength=32> <font size="-2">(<bean:message
			key="provider.providerchangepassword.msgAtLeast" />
			<%=op.getProperty("password_min_length")%> <bean:message
			key="provider.providerchangepassword.msgSymbols" />)</font></td>
	</tr>
	<tr>
		<td width="50%" align="right"><font face="arial"><bean:message
			key="provider.providerchangepassword.msgConfirm" /> &nbsp;<b><bean:message
			key="provider.providerchangepassword.formNewPassword" />:</b></font></td>
		<td><input type=password name="confirmpassword" value="" size=20
			maxlength=32> <font size="-2">(<bean:message
			key="provider.providerchangepassword.msgAtLeast" />
			<%=op.getProperty("password_min_length")%> <bean:message
			key="provider.providerchangepassword.msgSymbols" />)</font></td>
	</tr>
	
	<% if ( BLocallockset != null && BRemotelockset != null && (BLocallockset.intValue() == 1 || BRemotelockset.intValue() == 1)) { %>
	
		<tr>
			<td align="right" width="50%"><font face="arial"><bean:message
				key="provider.providerchangepassword.msgEnterOld" />&nbsp;<b><bean:message
				key="provider.providerchangepassword.currentPIN" />:</b></font></td>
			<td><input type=password name="pin" value="" size=20
				maxlength=32></td>
		</tr>
			<tr>
			<td width="50%" align="right"><font face="arial"><bean:message
				key="provider.providerchangepassword.msgChooseNew" />&nbsp;<b><bean:message
				key="provider.providerchangepassword.newPIN" />:</b></font></td>
			<td><input type=password name="newpin" value="" size=20
				maxlength=32> <font size="-2">(<bean:message
				key="provider.providerchangepassword.msgAtLeast" />
				<%=op.getProperty("password_pin_min_length")%> <bean:message
				key="provider.providerchangepassword.msgSymbols" />)</font></td>
		</tr>
		<tr>
			<td width="50%" align="right"><font face="arial"><bean:message
				key="provider.providerchangepassword.msgConfirm" />&nbsp;<b><bean:message
				key="provider.providerchangepassword.newPIN" />:</b></font></td>
			<td><input type=password name="confirmpin" value="" size=20
				maxlength=32> <font size="-2">(<bean:message
				key="provider.providerchangepassword.msgAtLeast" />
				<%=op.getProperty("password_pin_min_length")%> <bean:message
				key="provider.providerchangepassword.msgSymbols" />)</font></td>
		</tr>
	<% } %>

</table>
</center>
<table width="100%" border="0" cellpadding="4" cellspacing="0"
	bgcolor="#486ebd">
	<tr>
		<TD align="center" width="50%"><INPUT TYPE="submit"
			VALUE='<bean:message key="provider.providerchangepassword.btnSubmit"/>'
			SIZE="7"> &nbsp;&nbsp;&nbsp; <INPUT TYPE="RESET"
			VALUE='<bean:message key="global.btnBack"/>'
			onClick="window.close();"></TD>
	</tr>
</table>
</form>
</body>
</html:html>
