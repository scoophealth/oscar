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

<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="org.oscarehr.common.model.BillingPaymentType"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<style type="text/css">
body {
	font-size: 18px;
	font-family: Verdana;
}
</style>

<%
	Boolean isModify = false;
	String id = request.getParameter("id");
	String type = request.getParameter("type");
	String titleStr = "Create Billing Payment Type";
	String method = "createType";
	if (id != null && type != null) {
		isModify = true;
		titleStr = "Modify Billing Payment Type";
		method = "editType";
	} else {
		type = "";
	}
%>


<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><%=titleStr%></title>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript">

function check() {
	if (document.getElementById("paymentType").value.length < 1) {
		alert("Payment type can not be empty!");
		return false;
	}
	return true;
}

function createType() {
	if (!check()) {
		return;
	}
	$.ajax({
		type:"GET",
		async: true,
		data: {paymentType:document.getElementById("paymentType").value},
		url:"<%=request.getContextPath()%>/billing/CA/ON/managePaymentType.do?method=<%=method%>",
		dataType: "json",
		success: function(ret){
			if (!ret) {
				alert("Failed to create new payment type!");
			} else if (ret.ret=="1") {
				alert(ret.reason);
			} else {
				alert("Success");
				history.back();
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown){
			if (textStatus) {
				alert(JSON.toString(textStatus));
			} else if (errorThrown) {
				alert(JSON.toString(errorThrown));
			} else {
				alert("Unknown error happened!");
			}
		}
	});
}

function saveType() {
	if (!check()) {
                return;
        }
	$.ajax({
		type:"GET",
		async: true,
		data: {id:"<%=id%>",oldPaymentType:"<%=type%>", paymentType:document.getElementById("paymentType").value},
		url:"<%=request.getContextPath()%>/billing/CA/ON/managePaymentType.do?method=<%=method%>",
		dataType: "json",
		success : function(ret) {
			if (!ret) {
				alert("Failed to create new payment type!");
			} else if (ret.ret == "1") {
				alert(ret.reason);
			} else {
				alert("Success");
				history.back();
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if (textStatus) {
				alert(JSON.toString(textStatus));
			} else if (errorThrown) {
				alert(JSON.toString(errorThrown));
			} else {
				alert("Unknown error happened!");
			}
		}
	});
}
</script>

</head>

<body>
	<table width="100%">
		<tbody>
			<tr bgcolor="#CCCCFF">
				<th><%=titleStr%></th>
			</tr>
		</tbody>
	</table>
	<p />
	<p />

	<center>
		<input id="paymentType" name="paymentType" type="text"
			value="<%=type%>" placeholder="Please input a new payment type"
			size="38" />
		<%
			if (isModify) {
		%>
		<input name="save" type="button" onclick="saveType()" value="save" />
		<%
			} else {
		%>
		<input name="create" type="button" onclick="createType()"
			value="create" />
		<%}%>
		<input name="back" type="button" onclick="history.back();" value="back" />
	</center>
</body>
</html>
