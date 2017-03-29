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

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"></script>
	<title>Manage Billing Payment Type</title>
</head>

<%
	List<BillingPaymentType> paymentTypeList = (List<BillingPaymentType>) request
			.getAttribute("paymentTypeList");
%>

<body>
	<table width="100%">
		<tbody>
			<tr bgcolor="#CCCCFF">
				<th>Manage Billing Payment Type</th>
			</tr>
		</tbody>
	</table>
	<p />
	<p />

	<table width="100%">
		<tbody>
			<tr bgcolor="#CCCCFF">
				<th>Id</th>
				<th>Type</th>
				<th colspan="2">Operation</th>
			</tr>
			<%
				int count = 0;
				String bgColor = "white";
				for (BillingPaymentType paymentType : paymentTypeList) {
					count++;
					if (count % 2 == 0) {
			%>
			<tr bgcolor="white">
				<%
					} else {
				%>
			
			<tr bgcolor="#EEEEFF">
				<%
					}
				%>
				<td><%=paymentType.getId()%></td>
				<td><%=paymentType.getPaymentType()%></td>
				<td>
				<a href="<%=request.getContextPath()%>/billing/CA/ON/editBillingPaymentType.jsp?id=<%=paymentType.getId()%>&type=<%=paymentType.getPaymentType()%>">Edit</a>
				</td>
				<td>
				<a href="#" paymentTypeId="<%=paymentType.getId()%>">Delete</a>
				</td>
			</tr>
			<%
				}
			%>
		</tbody>
	</table>
	<p />
	<hr />
	<center style="font-family: Verdana">
		<a
			href="<%=request.getContextPath()%>/billing/CA/ON/editBillingPaymentType.jsp">Create
			a new payment type</a>
	</center>
</body>
</html>

<script type="text/javascript">

jQuery(document).ready(function() {
	jQuery("tr td:nth-child(4)").on("click", "a", function(event) {
		jQuery.ajax({
			url: "<%=request.getContextPath()%>/billing/CA/ON/managePaymentType.do",
			type: "get",
			async: "false",
			timeout: 30000,
			dataType: "json",
			data: {method: "removeType", paymentTypeId: event.target.getAttribute("paymentTypeId")},
			success: function (data) {
				if (data == null) {
					alert("Error happened after getting response!");
				}
				if (parseInt(data.ret) == 0) {
					alert("Successed deleting the payment type!");
					location.href = "<%=request.getContextPath()%>/billing/CA/ON/managePaymentType.do?method=listAllType";
				} else {
					alert("Failed to delete the payment type, reason:" + data.reason);
				}
			},
			error: function() {
				alert("Error happened!!");
			}
		});
		return false;
	});
})

</script>

