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
<%@ page errorPage="error.jsp"%>

<!DOCTYPE html>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.oscar-emr.com/tags/integration" prefix="i"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
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

<head>
<jsp:include page="head-includes.jsp" />

<script language="javascript">
	function goBack(control) {
		if (control) {
			control.disabled = true;
		}
		window.location.href = "mcedt.do";
		return false;
	}
</script>
<title>MCEDT: Info</title>
</head>

<body>
	<div class="container-fluid">
		<div class="row-fluid">

			<h2>Resource Information</h2>

			<table class="table table-striped  table-condensed">
				<c:forEach var="d" items="${detail.data}">
					<tr>
						<td>ID</td>
						<td><c:out value="${d.resourceID}" /></td>
					</tr>
					<tr>
						<td>Created</td>
						<td><fmt:formatDate value="${i:toDate(d.createTimestamp)}" />
						</td>
					</tr>
					<tr>
						<td>Description</td>
						<td><c:out value="${d.description}" /></td>
					</tr>
					<tr>
						<td>Resource type</td>
						<td><c:out value="${d.resourceType}" /></td>
					</tr>
					<tr>
						<td>Modified</td>
						<td><fmt:formatDate value="${i:toDate(d.modifyTimestamp)}" />
						</td>
					</tr>
					<tr>
						<td>Result</td>
						<td><c:out value="${d.result.code}" /> - <c:out
								value="${d.result.msg}" /></td>
					</tr>
					<tr>
						<td>Status</td>
						<td><c:out value="${d.status}" /></td>
					</tr>
					<tr>
						<td></td>
						<td></td>
					</tr>
				</c:forEach>
			</table>

			<button onclick="return goBack(this);" class="btn btn-primary">Return</button>

		</div><!-- row -->
	</div><!-- container -->

</body>
</html>