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

	<jsp:include page="../head-includes.jsp" />

	<link href="<%= request.getContextPath() %>/mcedt/web/css/kai_mcedt.css" rel="stylesheet" type="text/css">
    <link href="http://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700,900" rel="stylesheet" type="text/css">

	<script language="javascript">
		function cancel(control) {
			if (control) {
				control.disabled = true;
			}
			window.location.href = '<%= request.getContextPath() %>/mcedt/kaimcedt.do?tab=upload';
			return false;
		}
	
		function create(control) {
			if (control) {
				control.disabled = true;
			}
			var method = jQuery("#methodAddUpload");
			method.val('addUpload');
			var form = jQuery("#formAddUpload");
			form.submit();
			return true;
		}
	</script>
	
	
	<title>MCEDT: Upload</title>

<html:base />
</head>

<body>
    <div class="greyBox">    
		<div class="center">
			<h1>Upload Details</h1>
		
			<html:form action="/mcedt/addUpload.do" method="post" styleId="formAddUpload"
				enctype="multipart/form-data">
				
				<jsp:include page="../messages.jsp" />
		
				<input id="methodAddUpload" name="method" type="hidden" value="" />
		      	<label for="addUploadFile">Upload your file</label>
		      	<input type="file" name="addUploadFile" id="addUploadFile" style="width:95%; margin-top:5px;"/>
		      	<div class="row topMargin30">
			      	<button class="noBorder greenBox flatLink font12 rightMargin5" onclick="return create(this);">Create</button>
					<button class="noBorder blackBox flatLink font12" onclick="return cancel();">Cancel</button>
				</div>
			</html:form>
		</div>
	</div>
</body>
</html:html>
