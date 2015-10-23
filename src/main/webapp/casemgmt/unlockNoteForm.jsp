
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



<%@ include file="/casemgmt/taglibs.jsp"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_casemgmt.notes" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_casemgmt.notes");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Case Management</title>
<c:set var="ctx" value="${pageContext.request.contextPath}"
	scope="request" />
<link rel="stylesheet" href="<c:out value="${ctx}"/>/css/casemgmt.css"
	type="text/css">
</head>

<body>
<c:if
	test="${not empty requestScope.success and requestScope.success == false}">
	<h4 style="color: red">Error unlocking note.</h4>
</c:if>
<h2>Please enter password to unlock this note.</h2>
<h5 style="color: red">This note will only be unlocked for the
duration of your session. To permanently unlock, click on Edit Note, and
remove the password.</h5>
<nested:form action="/CaseManagementView">
	<input type="hidden" name="method" value="do_unlock" />
	<html:hidden property="noteId" />
	<table>
		<tr>
			<td class="fieldTitle">Password:</td>
			<td class="fieldValue"><html:password property="password" /></td>
		</tr>
		<tr>
			<td class="fieldValue" colspan="2"><input type="button"
				value="Unlock"
				onclick="this.form.method.value='do_unlock';this.form.submit();">
			</td>
		</tr>
	</table>
</nested:form>
</body>

</html>
