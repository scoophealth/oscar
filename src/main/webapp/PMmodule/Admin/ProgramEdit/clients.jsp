
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

<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>
<script>
function assignTeam(id,selectBox) {
	var team_id = selectBox.options[selectBox.selectedIndex].value;
	document.programManagerForm.elements['admission.teamId'].value=team_id;
	document.programManagerForm.elements['admission.id'].value=id;
	document.programManagerForm.method.value='assign_team_client';
	document.programManagerForm.submit();
}

function assignStatus(id,selectBox) {
	var status_id = selectBox.options[selectBox.selectedIndex].value;
	document.programManagerForm.elements['admission.clientStatusId'].value=status_id;
	document.programManagerForm.elements['admission.id'].value=id;
	document.programManagerForm.method.value='assign_status_client';
	document.programManagerForm.submit();
}
</script>
<html:hidden property="admission.id" />
<html:hidden property="admission.teamId" />
<html:hidden property="admission.clientStatusId" />
<div class="tabs">
<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<th title="Programs">Clients</th>
	</tr>
</table>
</div>
<!--  show current clients -->
<display:table class="simple" cellspacing="2" cellpadding="3" id="admission" name="admissions" export="false" pagesize="0" requestURI="/PMmodule/ProgramManager.do">
	<display:setProperty name="paging.banner.placement" value="bottom" />
	<display:setProperty name="basic.msg.empty_list" value="No clients currently admitted to this program." />
	
	<display:column sortable="false" title="">
		<a href="javascript:void(0);" onclick="alert('Please discharge clients from the client manager');"> Discharge </a>
	</display:column>
	<display:column property="client.formattedName" sortable="true" title="Name" />
	<display:column property="admissionDate" sortable="true" title="Admission Date" />
	<caisi:isModuleLoad moduleName="pmm.refer.temporaryAdmission.enabled">
		<display:column property="temporaryAdmission" sortable="true" title="Temporary Admission" />
	</caisi:isModuleLoad>
	<display:column property="admissionNotes" sortable="true" title="Admission Notes" />
	<display:column property="teamName" sortable="true" title="Team" />
	<display:column sortable="false" title="" >
		<select name="x" onchange="assignTeam('<c:out value="${admission.id}"/>',this);">
			<option value="0">&nbsp;</option>
			<c:forEach var="team" items="${teams}">
				<c:choose>
					<c:when test="${team.id == admission.teamId}">
						<option value="<c:out value="${team.id}"/>" selected><c:out value="${team.name}" /></option>
					</c:when>
					<c:otherwise>
						<option value="<c:out value="${team.id}"/>"><c:out value="${team.name}" /></option>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</select>
	</display:column>
	<display:column sortable="false" title="Status" >
		<select name="y" onchange="assignStatus('<c:out value="${admission.id}"/>',this);">
			<option value="0">&nbsp;</option>
			<c:forEach var="status" items="${client_statuses}">
				<c:choose>
					<c:when test="${status.id == admission.clientStatusId}">
						<option value="<c:out value="${status.id}"/>" selected><c:out value="${status.name}" /></option>
					</c:when>
					<c:otherwise>
						<option value="<c:out value="${status.id}"/>"><c:out value="${status.name}" /></option>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</select>
	</display:column>
</display:table>
