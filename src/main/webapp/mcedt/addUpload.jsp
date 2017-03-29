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

<script language="javascript">
	function cancel(control) {
		if (control) {
			control.disabled = true;
		}
		window.location.href = "uploads.do";
		return false;
	}

	function create(control) {
		if (control) {
			control.disabled = true;
		}
		var form = jQuery("#form");
		form.submit();
		return true;
	}
</script>


<title>MCEDT: Upload</title>

<html:base />
</head>

<body>
	<div class="container-fluid">
		<div class="row-fluid">
		
			<h2>Upload Details</h2>

			<html:form action="/mcedt/addUpload.do" method="post" styleId="form"
				enctype="multipart/form-data">
		
				<jsp:include page="messages.jsp" />

				<input id="method" name="method" type="hidden" value="" />

				<div class="form-group">
					<label class="control-label" for="resourceType">Resource
						Type</label>
						
					<html:select property="resourceType" styleId="resourceType" styleClass="input-xxlarge">
						<c:forEach var="r" items="${mcedtTypeList.data}">
							<html:option value="${r.resourceType}">
								<c:out value="${r.resourceType}" /> -
										<c:out value="${r.access}" /> - 
										<c:out value="${r.descriptionEn}" />
							</html:option>
						</c:forEach>
					</html:select>
					<label class="control-label" for="description">Description:</label>
					<html:text styleId="description" property="description" value="" />

					<label class="control-label" for="content">File Upload</label>
					<html:file property="content" styleId="content" />

					<div style="margin-top: 1em;">
						<button class="btn" onclick="return create();">Create</button>
						<button class="btn" onclick="return cancel();">Cancel</button>
					</div>
			</html:form>
		</div>
	</div>
</body>
</html:html>