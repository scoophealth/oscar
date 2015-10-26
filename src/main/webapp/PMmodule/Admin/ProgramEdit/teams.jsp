
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
function deleteTeam(id) {
	if(!confirm("Are you sure you want to delete the team entry?")) {
		return;
	}
	document.programManagerForm.elements['team.id'].value=id;
	document.programManagerForm.method.value='delete_team';
	document.programManagerForm.submit();
}

function editTeam(id) {
	document.programManagerForm.elements['team.id'].value=id;
	document.programManagerForm.method.value='edit_team';
	document.programManagerForm.submit();
}

function add_team(form) {
	if (form.elements['team.name'].value == '') {
		alert('You must choose a team name');
		return false;
	}
	
	form.elements['team.id'].value='0';
	form.method.value='save_team';
	form.submit();
}
</script>
<div class="tabs">
<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<th title="Programs">Team Management</th>
	</tr>
</table>
</div>
<!--  show current staff -->
<display:table class="simple" cellspacing="2" cellpadding="3" id="team" name="teams" export="false" pagesize="0" requestURI="/PMmodule/ProgramManager.do">
	<display:setProperty name="paging.banner.placement" value="bottom" />
	<display:setProperty name="basic.msg.empty_list" value="No teams are currently defined for this program." />
	<display:column sortable="false" title="">
		<a onclick="deleteTeam('<c:out value="${team.id}"/>');return false;" href="javascript:void(0);"> Delete </a>
	</display:column>
	<display:column property="name" sortable="true" title="Name" />
	<display:column sortable="true" title="Staff">
		<ul>
			<c:forEach var="provider" items="${team.providers}">
				<li><c:out value="${provider.provider.formattedName}" /> (<c:out value="${provider.role.name}" />)</li>
			</c:forEach>
		</ul>
	</display:column>
	<display:column sortable="true" title="Clients">
		<ul>
			<c:forEach var="admission" items="${team.admissions}">
				<li><c:out value="${admission.client.formattedName}" /></li>
			</c:forEach>
		</ul>
	</display:column>
</display:table>
<br />
<table width="100%" border="1" cellspacing="2" cellpadding="3">
	<html:hidden property="team.id" />
	<tr class="b">
		<td width="20%">Name:</td>
		<td><html:text property="team.name" size="50" maxlength="255"/></td>
	</tr>
	<tr>
		<td colspan="2"><input type="button" value="Save" onclick="add_team(this.form)" /> <html:cancel /></td>
	</tr>
</table>
