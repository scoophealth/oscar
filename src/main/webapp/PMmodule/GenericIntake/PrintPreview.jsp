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
<%@ include file="/taglibs.jsp" %>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_pmm" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_pmm");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>
<%@ page import="org.oscarehr.PMmodule.model.Intake"%>
<%@ page
	import="org.oscarehr.PMmodule.web.formbean.GenericIntakeEditFormBean"%>
<%
GenericIntakeEditFormBean intakeEditForm = (GenericIntakeEditFormBean) session.getAttribute("genericIntakeEditForm");
Intake intake = intakeEditForm.getIntake();
%>
<html:html xhtml="true" locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Generic Intake Print</title>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<script type="text/javascript">
			<!--
			var djConfig = {
				isDebug: false,
				parseWidgets: false,
				searchIds: ["layoutContainer", "topPane", "clientPane", "bottomPane", "clientTable", "admissionsTable"]
			};
			// -->
		</script>
<script type="text/javascript"
	src="<html:rewrite page="/dojoAjax/dojo.js" />"></script>
<script type="text/javascript">
			<!--
			dojo.require("dojo.widget.*");
			dojo.require("dojo.validate.*");
			// -->
		</script>
<html:base />
</head>
<body>
<table class="intakeTable">
	<tr>
		<td><input type="button" value="Print" onclick="window.print()">
		</td>
		<td align="right"><input type="button" value="Close"
			onclick="window.close()" /></td>
	</tr>
	<tr>
		<td>
		<center>
		<h5><c:out value="${sessionScope.genericIntakeEditForm.title}" />
		</h5>
		</center>
		</td>
	</tr>
</table>
<table class="intakeTable">
	<tr>
		<td><label>First Name<br>
		<c:out value="${sessionScope.genericIntakeEditForm.client.firstName}" /></label></td>
		<td><label>Last Name<br>
		<c:out value="${sessionScope.genericIntakeEditForm.client.lastName}" /></label></td>
		<td><label>Gender<br>
		<c:out value="${sessionScope.genericIntakeEditForm.client.sexDesc}" /></label></td>
		<td><label>Birth Date<br>
		<c:out
			value="${sessionScope.genericIntakeEditForm.client.formattedDob}" /></label></td>
	</tr>
	<tr>
		<td><label>Health Card #<br>
		<c:out value="${sessionScope.genericIntakeEditForm.client.hin}" /></label></td>
		<td><label>Version<br>
		<c:out value="${sessionScope.genericIntakeEditForm.client.ver}" /></label></td>
	</tr>
	<tr>
		<td><label>Email<br>
		<c:out value="${sessionScope.genericIntakeEditForm.client.email}" /></label></td>
		<td><label>Phone #<br>
		<c:out value="${sessionScope.genericIntakeEditForm.client.phone}" /></label></td>
		<td><label>Secondary Phone #<br>
		<c:out value="${sessionScope.genericIntakeEditForm.client.phone2}" /></label></td>
	</tr>
	<tr>
		<td><label>Street<br>
		<c:out value="${sessionScope.genericIntakeEditForm.client.address}" /></label></td>
		<td><label>City<br>
		<c:out value="${sessionScope.genericIntakeEditForm.client.city}" /></label></td>
		<td><label>Province<br>
		<c:out value="${sessionScope.genericIntakeEditForm.client.province}" /></label></td>
		<td><label>Postal Code<br>
		<c:out value="${sessionScope.genericIntakeEditForm.client.postal}" /></label></td>
	</tr>
</table>

<caisi:intake base="<%=3%>" intake="<%=intake%>" />
<table>
	<tr>
		<td>&nbsp;</td>
	</tr>
</table>
</body>
</html:html>
