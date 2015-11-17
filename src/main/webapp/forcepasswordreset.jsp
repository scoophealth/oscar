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
<%@page import="org.oscarehr.common.dao.SecurityDao" %>
<%@page import="org.oscarehr.common.model.Security" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%
  //Make sure user has logged in first and username is in the session

  if(session.getAttribute("userName") == null )
    response.sendRedirect("../logout.jsp");
    		
   String errormsg = "";
   if (request.getParameter("errormsg") != null) {
	   errormsg = request.getParameter("errormsg") ;
   } 		
%>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@ page import="org.springframework.web.util.JavaScriptUtils"%>
<%@ page
	import="java.lang.*, java.util.*, java.text.*,java.sql.*, oscar.*"
	errorPage="errorpage.jsp"%>

<%
	OscarProperties op = OscarProperties.getInstance();
	String userName = (String)request.getSession().getAttribute("userName");
	SecurityDao securityDao = SpringUtils.getBean(SecurityDao.class);
	
	Security security = securityDao.findByUserName(userName).get(0);

	Integer BLocallockset = security.getBLocallockset();
	Integer BRemotelockset = security.getBRemotelockset();	  
	  
%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/checkPassword.js.jsp"></script>
<title><bean:message key="provider.providerchangepassword.title" /></title>
<script type="text/javascript">
function setfocus(el) {
	this.focus();
	document.forms[0].elements[el].focus();
	document.forms[0].elements[el].select();
}
function checkPwdPolicy() {
		
	if (document.forms[0].oldPassword.value == "") {
		alert ('<bean:message key="provider.providerchangepassword.msgOldPasswordError"/>');
		setfocus('oldPassword');
		return false;
	}
	var pwd1=document.forms[0].newPassword.value;
	var pwd2=document.forms[0].confirmPassword.value;
		
	var pin1 = null;
	var pin2 = null;
	
	var jsLocallockset = '<%=BLocallockset%>'; 
	var jsRemotelockset = '<%=BRemotelockset%>';
	
	if (jsLocallockset == '1' || jsRemotelockset == '1') {
		 pin1=document.updatepassword.newpin.value;
		 pin2=document.updatepassword.confirmpin.value;
	}
	
	
	if (!validatePassword(pwd1)) {
		setfocus('newPassword');
		return false;
	}
	if (pwd1 != pwd2) {
		alert ('<bean:message key="provider.providerchangepassword.msgPasswordConfirmError"/>');
		setfocus('confirmPassword');
		return false;
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

function validatePassword(pwd) {

	var password_min_length = <%=op.getProperty("password_min_length")%>;
	var password_min_groups = <%=op.getProperty("password_min_groups")%>;
	var password_group_lower_chars = "<%=JavaScriptUtils.javaScriptEscape(op.getProperty("password_group_lower_chars"))%>";
	var password_group_upper_chars = "<%=JavaScriptUtils.javaScriptEscape(op.getProperty("password_group_upper_chars"))%>";
	var password_group_digits = "<%=JavaScriptUtils.javaScriptEscape(op.getProperty("password_group_digits"))%>";
	var password_group_special = "<%=JavaScriptUtils.javaScriptEscape(op.getProperty("password_group_special"))%>";

	<%
	if (!Boolean.parseBoolean(op.getProperty("IGNORE_PASSWORD_REQUIREMENTS")))
	{
		%>
		if (pwd.length < password_min_length) {
			alert('<bean:message key="password.policy.violation.msgPasswordLengthError"/> ' +
				password_min_length + ' <bean:message key="password.policy.violation.msgSymbols"/>');
			return false;
		}

		var lower = false;
		var upper = false;
		var digits = false;
		var special = false;
	
		for (var i = 0; i < pwd.length; i++) {
			var s = pwd.charAt(i);
	
			if (!lower && password_group_lower_chars.indexOf(s) > -1) {
				lower = true;
			}
	
			if (!upper && password_group_upper_chars.indexOf(s) > -1) {
				upper = true;
			}
	
			if (!digits && password_group_digits.indexOf(s) > -1) {
				digits = true;
			}
	
			if (!special && password_group_special.indexOf(s) > -1) {
				special = true;
			}
		}
	
		var groups_used = parseInt(lower?1:0) + parseInt(upper?1:0) + parseInt(digits?1:0) + parseInt(special?1:0);
		if (groups_used < password_min_groups) {
			alert('<bean:message key="password.policy.violation.msgPasswordStrengthError"/> ' +
				password_min_groups + ' <bean:message key="password.policy.violation.msgPasswordGroups"/>');
			return false;
		}
		<%
	}
	%>

	return true;
}
</script>
</head>

<body onLoad="setfocus('oldPassword')" topmargin="0" leftmargin="0" rightmargin="0">
<html:form method="post" action="login" onsubmit="return checkPwdPolicy();">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF"><bean:message
			key="provider.providerchangepassword.description" /></font></th>
	</tr>
</table>

<table width="100%" border="0" cellpadding="2" bgcolor="#eeeeee">
	<tr>
		<td><font face="arial" size="2"><bean:message
			key="provider.providerchangepassword.msgInstructions" /> &nbsp; <b><bean:message
			key="provider.providerchangepassword.msgUpdate" /></b> <bean:message
			key="provider.providerchangepassword.msgClickButton" /></font></td>
	</tr>

</table>
<center>

<p><b><font color='red'><%=errormsg%></font></b>

<table border="0" width="100%" cellpadding="4" cellspacing="0">
	<tr>
		<td align="right" width="50%"><font face="arial"><bean:message
			key="provider.providerchangepassword.msgEnterOld" /> &nbsp; <b><bean:message
			key="provider.providerchangepassword.formOldPassword" />:</b></font></td>
		<td><input type=password name="oldPassword" value="" size=20
			maxlength=32></td>
	</tr>
	<tr>
		<td width="50%" align="right"><font face="arial"><bean:message
			key="provider.providerchangepassword.msgChooseNew" /> &nbsp; <b><bean:message
			key="provider.providerchangepassword.formNewPassword" />:</b></font></td>
		<td><input type=password name="newPassword" value="" size=20
			maxlength=32> <font size="-2">(<bean:message
			key="provider.providerchangepassword.msgAtLeast" />
			<%=op.getProperty("password_min_length")%> <bean:message
			key="provider.providerchangepassword.msgSymbols" />)</font></td>
	</tr>
	<tr>
		<td width="50%" align="right"><font face="arial"><bean:message
			key="provider.providerchangepassword.msgConfirm" /> &nbsp; <b><bean:message
			key="provider.providerchangepassword.formNewPassword" />:</b></font></td>
		<td><input type=password name="confirmPassword" value="" size=20
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
			<td><input type=password name="oldPin" value="" size=20
				maxlength=32></td>
		</tr>
			<tr>
			<td width="50%" align="right"><font face="arial"><bean:message
				key="provider.providerchangepassword.msgChooseNew" />&nbsp;<b><bean:message
				key="provider.providerchangepassword.newPIN" />:</b></font></td>
			<td><input type=password name="newPin" value="" size=20
				maxlength=32> <font size="-2">(<bean:message
				key="provider.providerchangepassword.msgAtLeast" />
				<%=op.getProperty("password_pin_min_length")%> <bean:message
				key="provider.providerchangepassword.msgSymbols" />)</font></td>
		</tr>
		<tr>
			<td width="50%" align="right"><font face="arial"><bean:message
				key="provider.providerchangepassword.msgConfirm" />&nbsp;<b><bean:message
				key="provider.providerchangepassword.newPIN" />:</b></font></td>
			<td><input type=password name="confirmPin" value="" size=20
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
			SIZE="7" onSubmit="return(checkPwdPolicy())"> </TD>			
	</tr>
</table>

    <input type=hidden name='forcedpasswordchange' value='true' />

</html:form>
</body>
</html:html>
