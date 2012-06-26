<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>
<%@ page import="oscar.OscarProperties"%>
<%@ page import="org.springframework.web.util.JavaScriptUtils"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<%@ page contentType="text/javascript"%>

<%!
	OscarProperties op = OscarProperties.getInstance();
%>

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

function validatePin(pin) {

	var password_pin_min_length = <%=op.getProperty("password_pin_min_length")%>;
	var password_group_digits = "<%=JavaScriptUtils.javaScriptEscape(op.getProperty("password_group_digits"))%>";

	if (pin.length < password_pin_min_length) {
		alert('<bean:message key="password.policy.violation.msgPinLengthError"/> ' +
			password_pin_min_length + ' <bean:message key="password.policy.violation.msgDigits"/>');
		return false;
	}

	for (var i = 0; i < pin.length; i++) {
		var s = pin.charAt(i);

		if (password_group_digits.indexOf(s) == -1) {
			alert('<bean:message key="password.policy.violation.msgPinGroups"/>');
			return false;
		}
	}

	return true;
}
