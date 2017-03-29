<%--

    Copyright (c) 2014-2015. KAI Innovations Inc. All Rights Reserved.
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

<%@ page errorPage="../error.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.oscar-emr.com/tags/integration" prefix="i"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.billing,_billing" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin.billing&type=_billing");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page import="java.lang.*, java.util.*, java.text.*,java.sql.*, oscar.*"	%>
<%
	String mcedtUsername = (String)session.getAttribute("mcedtUsername");
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Update MCEDT Password</title>
	<script src="../js/jquery-1.7.1.min.js"></script>
	<link href="web/css/kai_mcedt.css" rel="stylesheet" type="text/css">
    <link href="http://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700,900" rel="stylesheet" type="text/css">
	
	<script type="text/javascript" charset="utf-8">

	window.onload = function() {
		
		var isSucess = <%=(String)session.getAttribute("isPassChange")%>;
		if(isSucess!=null && isSucess==true){
			alert('EDT Password has been changed successfully.');
			return gobacktoedthome();
			//return true;
		}				
		
	}

	function gobacktoedthome(control){
		return submitForm('cancel', control);
	}

	function onSub(control) {
	
	
		if (document.getElementById("password").value=="") {
			alert('<bean:message key="admin.securityrecord.formPassword" /> <bean:message key="admin.securityrecord.msgIsRequired"/>');
			setfocus('password');
			return false;
		}
		// if (document.getElementById("password").value != "*********" && !validatePassword(document.updatearecord.password.value)) {
		//	setfocus('password');
		//	return false;
		//}
		if (document.getElementById("password").value != document.getElementById("conPassword").value) {
			alert('<bean:message key="admin.securityrecord.msgPasswordNotConfirmed" />');
			setfocus('conPassword');
			return false;
		}		
		
		return changePass(control);
	}


		function changePass(control) {
			submitForm("changePassword", control);
		}
		
		function submitForm(methodType, control){
			if (control) {
				control.disabled = true;
			}
			
			var method = jQuery("#method");
			method.val(methodType);
			
			var form = jQuery("#form");
			form.submit();
			return true;
		}		
	</script>
	

</head>
<body>
	<!-- <div class="show">
   		<img class="logo" src="web/img/kai.png"/>
   </div> -->
	<div class="greyBox">    
		<div class="center">
			<div class="row" style="text-align: center;"> 
				<h1>Update MCEDT Password</h1>
			</div>
			
			<div class="center" style="width:45%">
				<%-- <html:form action="<%=request.getContextPath() %>/mcedt/kaichpass.do" method="POST"> --%>
				<html:form action="/mcedt/kaichpass" method="post" styleId="form">
					<input id="method" name="method" type="hidden" value="" />
					<table class="password">
						<tr>
							<td>User Name: </td>
							<td><input name="username" type="text" id="username" class="readonly" value="<%=mcedtUsername%>" readonly></td>
						</tr>
						<tr>
							<td>New Password: </td>
							 <td><input name="password" type="password" id="password" ></td>
						</tr>
						<tr>
							<td>Confirm Password: </td>
							<td><input name="conPassword" type="password" id="conPassword" ></td>
						</tr>
						<tr>
						<td colspan=2>
							<button type="button" class="noBorder greenBox flatLink font12 rightMargin5" onclick="return onSub();">Update Password</button>
							<button id="backedthome" class="noBorder blackBox flatLink font12" onclick="return gobacktoedthome();">Cancel</button>			
						</td>
						</tr>
					</table>
				</html:form>
			</div>
		</div>
	</div>
</body>
</html>