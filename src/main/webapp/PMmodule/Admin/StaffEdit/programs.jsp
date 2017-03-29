
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

<%@ page import="org.oscarehr.PMmodule.model.*"%>
<%@ page import="org.oscarehr.PMmodule.web.formbean.*"%>
<%@ page import="java.util.*"%>
<script>
function assignTeam(id,selectBox) {
	var team_id = selectBox.options[selectBox.selectedIndex].value;
	document.staffManagerForm.elements['teamId'].value=team_id;	
	document.staffManagerForm.elements['program_provider.programId'].value=id;
	document.staffManagerForm.method.value='assign_team';
	document.staffManagerForm.submit();
}

function removeTeam(id,team_id) {
	document.staffManagerForm.elements['teamId'].value=team_id;	
	document.staffManagerForm.elements['program_provider.programId'].value=id;
	document.staffManagerForm.method.value='remove_team';
	document.staffManagerForm.submit();
}

function assignRole(id,selectBox) {
	var role_id = selectBox.options[selectBox.selectedIndex].value;
	document.staffManagerForm.elements['program_provider.roleId'].value=role_id;	
	document.staffManagerForm.elements['program_provider.programId'].value=id;
	document.staffManagerForm.method.value='assign_role';
	document.staffManagerForm.submit();
}

function remove_entry(id) {
	//alert('remove the following entry:' + id);
	document.staffManagerForm.elements['program_provider.id'].value=id;
	document.staffManagerForm.method.value='remove_entry';
	document.staffManagerForm.submit();
}
</script>
<html:hidden property="program_provider.roleId" />
<html:hidden property="program_provider.programId" />
<html:hidden property="program_provider.id" />
<input type="hidden" name="teamId" value="" />
<div class="tabs" id="tabs">
<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<th title="Programs">Programs</th>
	</tr>
</table>
</div>
<%
ProgramProvider pp = null;
%>
<!--  show current staff -->
<table class="simple" cellspacing="2" cellpadding="3">
	<tr>
		<th style="color:black">Assigned To</th>
		<th style="color:black">Program Name</th>
		<th style="color:black">Role</th>
		<th style="color:black">Team(s)</th>
	</tr>
	<c:forEach var="program" items="${all_programs}">
		<tr>
			<td>
			<%
				String checked = "";
				pp = null;
				List currentPrograms = (List) request.getAttribute("programs");
				StaffEditProgramContainer container = (StaffEditProgramContainer) pageContext.getAttribute("program");
				Program p = container.getProgram();
				for (int x = 0; x < currentPrograms.size(); x++) {
					ProgramProvider pp1 = (ProgramProvider) currentPrograms.get(x);
					if (pp1.getProgramId().longValue() == p.getId().intValue()) {
						checked = "checked";
						pp = pp1;
						break;
					}
				}
				pageContext.setAttribute("pp", pp);
				pageContext.setAttribute("checked", checked);
			%> <c:choose>
				<c:when test="${checked eq 'checked'}">
					<input type="checkbox" <%=checked%> onclick="remove_entry('<c:out value="${pp.id}"/>')" />
				</c:when>
				<c:otherwise>
					<input type="checkbox" <%=checked%> disabled />
				</c:otherwise>
			</c:choose></td>
			<td><a href="<html:rewrite action="/PMmodule/ProgramManager"/>?id=<c:out value="${program.program.id}"/>&view.tab=staff&method=edit"><c:out value="${program.program.name}" /></a></td>
			<td><select name="x" onchange="assignRole('<c:out value="${program.program.id}"/>',this);">
				<option value="0">&nbsp;</option>
				<c:forEach var="role" items="${roles}">
					<c:choose>
						<c:when test="${pp.roleId == role.id }">
							<option value="<c:out value="${role.id}"/>" selected><c:out value="${role.name}" /></option>
						</c:when>
						<c:otherwise>
							<option value="<c:out value="${role.id}"/>"><c:out value="${role.name}" /></option>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</select></td>
			<td>
			<table width="100%" cellspacing="2" cellpadding="2">
				<c:forEach var="team" items="${pp.teams}">
					<tr>
						<td><c:out value="${team.name }" /></td>
						<td><a href="javascript:void(0);" onclick="removeTeam('<c:out value="${pp.programId}"/>','<c:out value="${team.id}"/>');return false;">Remove</a>
					</tr>
				</c:forEach>
			</table>
			<select name="x" onchange="assignTeam('<c:out value="${pp.programId}"/>',this)">
				<option value="" SELECTED></option>
				<c:forEach var="team" items="${program.teamList}">
					<option value="<c:out value="${team.id}"/>"><c:out value="${team.name}" /></option>
				</c:forEach>
			</select></td>
	</c:forEach>
</table>
