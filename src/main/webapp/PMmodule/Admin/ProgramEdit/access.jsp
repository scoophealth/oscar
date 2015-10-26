
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

<script>
	function deleteAccess(id) {
		if(!confirm("Are you sure you want to delete the access entry?")) {
			return;
		}
		document.programManagerForm.elements['access.id'].value=id;
		document.programManagerForm.method.value='delete_access';
		document.programManagerForm.submit();
	}
</script>
<div class="tabs">
<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<th title="Programs">Access</th>
	</tr>
</table>
</div>
<display:table class="simple" cellspacing="2" cellpadding="3"
	id="access" name="accesses" export="false" pagesize="0"
	requestURI="/PMmodule/ProgramManager.do">
	<display:setProperty name="paging.banner.placement" value="bottom" />
	<display:setProperty name="basic.msg.empty_list"
		value="No access currently defined for this program." />
	<display:column sortable="false" title="">
		<a href="javascript:void(0);"
			onclick="deleteAccess('<c:out value="${access.id}"/>');return false;">
		Delete </a>
	</display:column>
	<display:column property="accessType.name" sortable="true" title="Name" />
	<display:column property="accessType.type" sortable="true" title="Type" />
	<display:column property="allRoles" sortable="true" title="All Roles" />
	<display:column sortable="true" title="Role(s)">
		<ul>
			<c:forEach var="role" items="${access.roles}">
				<li><c:out value="${role.name}" /></li>
			</c:forEach>
		</ul>
	</display:column>
</display:table>
<br />
<br />
<table width="100%" border="1" cellspacing="2" cellpadding="3">
	<tr class="b">
		<td width="20%">Name :</td>
		<td><html:select property="access.accessTypeId">
			<html:options collection="accessTypes" property="id"
				labelProperty="name" />
		</html:select> <html:hidden property="access.id" /> <html:hidden
			property="access.programId" /></td>
	</tr>
	<tr class="b">
		<td width="20%">All Roles:</td>
		<td><html:checkbox property="access.allRoles" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Roles:</td>
		<td><c:forEach var="role" items="${roles}">
			<input name="checked_role" value="<c:out value="${role.id}"/>"
				type="checkbox" />&nbsp;<c:out value="${role.name}" />
			<br />
		</c:forEach></td>
	</tr>
	<tr>
		<td colspan="2"><input type="button" value="Save"
			onclick="this.form.method.value='save_access';this.form.submit()" />
		<html:cancel /></td>
	</tr>
</table>
