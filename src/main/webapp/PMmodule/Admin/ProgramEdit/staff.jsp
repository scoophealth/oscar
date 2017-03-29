
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

<style>
.b th {color:black;}
</style>
<script>
	function search_provider(name) {
		var url = '<html:rewrite action="/PMmodule/ProviderSearch.do"/>';
			url += '?q=' + name;
			url += '&formName=programManagerForm';
			url += '&formElementId=provider.providerNo';
			url += '&formElementName=providerName';
		
		var name = document.programManagerForm.elements['providerName'].value;
		
		window.open(url, 'provider_search','width=500, height=400, scrollbars=yes');
	}

	function deleteProvider(id) {
		if(!confirm("Are you sure you want to delete the staff entry?")) {
			return;
		}
		document.programManagerForm.elements['provider.id'].value=id;
		document.programManagerForm.method.value='delete_provider';
		document.programManagerForm.submit();
	}
	
	function editProvider(id) {
		document.programManagerForm.elements['provider.id'].value=id;
		document.programManagerForm.method.value='edit_provider';
		document.programManagerForm.submit();
	}

	function assignTeam(id,selectBox) {
		var team_id = selectBox.options[selectBox.selectedIndex].value;
		if(team_id == '') { return;}
		document.programManagerForm.elements['teamId'].value=team_id;	
		document.programManagerForm.elements['provider.id'].value=id;
		document.programManagerForm.method.value='assign_team';
		document.programManagerForm.submit();
	}
	
	function removeTeam(id,team_id) {
		if(!confirm("Are you sure you want to delete the team entry?")) {
			return;
		}
		document.programManagerForm.elements['teamId'].value=team_id;	
		document.programManagerForm.elements['provider.id'].value=id;
		document.programManagerForm.method.value='remove_team';
		document.programManagerForm.submit();
	}
	
	function assignRole(id,selectBox) {
		var role_id = selectBox.options[selectBox.selectedIndex].value;
		document.programManagerForm.elements['provider.roleId'].value=role_id;	
		document.programManagerForm.elements['provider.id'].value=id;
		document.programManagerForm.method.value='assign_role';
		document.programManagerForm.submit();
	}
	
	function add_provider(form) {
		if(form.elements['provider.providerNo'].value == 0) {
			alert('You must choose a provider');
			return false;
		}
		form.method.value='save_provider';
		form.elements['provider.id'].value=0;
		form.submit();
	}
</script>
<input type="hidden" name="teamId" value="0" />
<div class="tabs">
<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<th title="Programs">Staff</th>
	</tr>
</table>
</div>
<!--  show current staff -->
<display:table class="simple" cellspacing="2" cellpadding="3" id="provider" name="providers" export="false" pagesize="0" requestURI="/PMmodule/ProgramManager.do">
	<display:setProperty name="paging.banner.placement" value="bottom" />
	<display:setProperty name="basic.msg.empty_list" value="No staff currently assigned to this program." />
	<display:column sortable="false" title="">
		<a href="javascript:void(0);" onclick="deleteProvider('<c:out value="${provider.id}"/>');return false;"> Delete </a>
	</display:column>
	<display:column property="provider.formattedName" sortable="true" title="Name" />
	<display:column property="provider.phone" sortable="true" title="Phone" />
	<display:column sortable="false" title="Team">
		<table width="100%" cellspacing="2" cellpadding="2">
			<c:forEach var="team" items="${provider.teams}">
				<tr>
					<td><c:out value="${team.name }" /></td>
					<td><a href="javascript:void(0);" onclick="removeTeam('<c:out value="${provider.id}"/>','<c:out value="${team.id}"/>');return false;">Remove</a>
				</tr>
			</c:forEach>
		</table>
		<select name="x" onchange="assignTeam('<c:out value="${provider.id}"/>',this)">
			<option value="" SELECTED></option>
			<c:forEach var="team" items="${teams}">
				<option value="<c:out value="${team.id}"/>"><c:out value="${team.name}" /></option>
			</c:forEach>
		</select>
	</display:column>
	<display:column sortable="false" title="Role">
		<select name="x" onchange="assignRole('<c:out value="${provider.id}"/>',this);">
			<option value="0">&nbsp;</option>
			<c:forEach var="role" items="${roles}">
				<c:choose>
					<c:when test="${role.id == provider.roleId}">
						<option value="<c:out value="${role.id}"/>" selected><c:out value="${role.name}" /></option>
					</c:when>
					<c:otherwise>
						<option value="<c:out value="${role.id}"/>"><c:out value="${role.name}" /></option>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</select>
	</display:column>
</display:table>
<br />
<table width="100%" border="1" cellspacing="2" cellpadding="3">
	<tr class="b">
		<td width="20%">Provider:</td>
		<td>
			<html:hidden property="provider.id" />
			<html:hidden property="provider.providerNo" />
			<%
 			String providerName = (String) request.getAttribute("providerName");
			if (providerName == null) {
				providerName = "";
			}
			%>
			<input type="text" name="providerName" size="30" value="<%=providerName%>" />
			<input type="button" value="Search" onclick="search_provider(this.form.providerName.value);" />
		</td>
	</tr>
	<tr class="b">
		<td width="20%">Role:</td>
		<td>
			<html:select property="provider.roleId">
				<html:options collection="roles" property="id" labelProperty="name" />
			</html:select>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<input type="button" value="Save" onclick="add_provider(this.form)" />
			<html:cancel />
		</td>
	</tr>
</table>
