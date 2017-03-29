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
<!DOCTYPE html>

<%@ page errorPage="error.jsp"%>

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

<html:html>
<head>
<jsp:include page="head-includes.jsp" />
<style type="text/css">
/* limits table height */
.scrollable {
	/*
	display: block;
	height: 500px !important;
	max-height: 500px !important;
	overflow: auto;
	*/
}

/* needed to align elements in navbar */
select,button {
	margin: 0px !important;
}

.navbar {
	line-height: 50px;
	height: 50px;
	vertical-align: middle;
	margin-bottom: 50px;
}
</style>
<script language="javascript">

	function createNew() {
		window.location.href = "uploads.do";
		return false;
	}

	function downloadSelected(control) {
		return submitForm('download', control);
	}
	
	function submitSelected(control) {
		return submitForm('submit', control);
	}

	function deleteSelected(control) {
		if (!confirm("Please confirm that you would like to delete the selected messages")) {
			return false;
		}
		
		return submitForm('delete', control);
	}

	function updateSelected(resourceId, control) {
		if (control) {
			control.disabled = true;
		}
		window.location.href = "update.do?resourceId=" + resourceId;
		return false;
	}
	
	function getInfo(resourceId, control) {
		if (control) {
			control.disabled = true;
		}
		window.location.href = "info.do?resourceId=" + resourceId;
		return false;
	}
	
	function changeDisplay(control) {
		submitForm("changeDisplay", control);
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


<title>MCEDT: Index</title>

<html:base />

</head>

<body>
	<div class="container-fluid">
		<div class="row-fluid">

			<h2>MCEDT Resources</h2>

			<html:form action="/mcedt/mcedt" method="post" styleId="form">

				<jsp:include page="messages.jsp" />

				<input id="method" name="method" type="hidden" value="" />

				<div class="navbar">
					<div class="navbar-inner"
						style="vertical-align: middle !important;">

						Resource Type:
						<html:select property="resourceType" styleId="resourceType"
							styleClass="input-xxlarge">
							<html:option value=""> - All - </html:option>
							<c:forEach var="r" items="${mcedtTypeList.data}">
								<html:option value="${r.resourceType}">
									<c:out value="${r.resourceType}" /> -
										<c:out value="${r.access}" /> - 
										<c:out value="${r.descriptionEn}" />
								</html:option>
							</c:forEach>
						</html:select>

						Status:
						<html:select property="status" styleId="status">
							<html:option value=""> - All - </html:option>
							<c:forEach var="i"
								items="${mcedtResourceForm.resourceStatusValues}">
								<html:option value="${i}" />
							</c:forEach>
						</html:select>

						Page #:
						<html:select property="pageNo" styleId="pageNo">
							<c:forEach var="i" begin="1"
								end="${mcedtResourceForm.detail.resultSize}">
								<html:option value="${i}">
									<c:out value="${i}" />
								</html:option>
							</c:forEach>
						</html:select>

						<button type="button" class="btn" onclick="return changeDisplay();">Display</button>
					</div>
				</div>
				<!-- navbar -->

				<table class="table scrollable">
					<thead>
						<tr>
							<th></th>
							<th>ID</th>
							<th>Date</th>
							<th>Type</th>
							<th>Result</th>
							<th>Status</th>
							<th>Description</th>
							<th></th>
						</tr>
					</thead>
					<c:forEach var="r" items="${mcedtResourceForm.detail.data}">
						<tr>
							<td><input type="checkbox" value="${r.resourceID}"
								name="resourceId" /></td>
							<td><c:out value="${r.resourceID}" /></td>
							<td><fmt:formatDate value="${i:toDate(r.createTimestamp)}" />
							</td>
							<td><c:out value="${r.resourceType}" /></td>
							<td><c:out value="${r.result.code}" /> - <c:out
									value="${r.result.msg}" /></td>
							<td><c:out value="${r.status}" /></td>
							<td><c:out value="${r.description}" /></td>
							<td>
								<button class="btn"
									onclick="return updateSelected(${r.resourceID}, this)">Update</button>

								<button class="btn"
									onclick="return getInfo(${r.resourceID}, this)">Display
									Info</button>
							</td>
						</tr>
					</c:forEach>
				</table>

				<div>
					<button class="btn" onclick="return deleteSelected(this);">Delete
						Selected</button>
					<button class="btn" onclick="return submitSelected(this);">Submit
						Selected</button>
					<button class="btn" onclick="return downloadSelected();">Download
						Selected</button>
					<button class="btn" onclick="return createNew(this);">Create
						New Upload</button>
				</div>
			</html:form>
		</div>
		<!-- row -->
	</div>
	<!-- container -->
</body>
</html:html>