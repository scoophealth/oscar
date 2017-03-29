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
<%@ include file="/taglibs.jsp"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.pmm" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin.pmm");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<div class="tabs" id="tabs">
<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<th title="Programs">Access</th>
	</tr>
</table>
</div>

<html:form action="/PMmodule/Admin/DefaultRoleAccess">
	<input type="hidden" name="method" value="save" />
	<html:hidden property="form.id" />

	<table width="100%" border="1" cellspacing="2" cellpadding="3"
		class="b">
		<tr class="b">
			<td width="20%">Role:</td>
			<td><html:select property="form.roleId">
				<html:options collection="roles" property="id" labelProperty="name" />
			</html:select></td>
		</tr>

		<tr class="b">
			<td width="20%">Access Type:</td>
			<td><html:select property="form.accessTypeId">
				<html:options collection="access_types" property="id"
					labelProperty="name" />
			</html:select></td>
		</tr>

		<tr>
			<td colspan="2"><html:submit value="Save" /> <input
				type="button" value="Cancel"
				onclick="location.href='<html:rewrite action="/PMmodule/Admin/DefaultRoleAccess"/>'" />
			</td>
		</tr>
	</table>
</html:form>
