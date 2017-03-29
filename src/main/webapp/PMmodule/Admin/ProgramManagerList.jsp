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
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.pmm" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin.pmm");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>
<script>
	function ConfirmDelete(name)
	{
		if(confirm("Are you sure you want to delete " + name + " ?")) {
			return true;
		}
		return false;
	}
	function submitForm(method)
	{
		document.programManagerForm.method.value=method;
		document.programManagerForm.submit()
	}
</script>
<%@ include file="/common/messages.jsp"%>
<div class="tabs" id="tabs">
<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<th title="Programs">Programs</th>
	</tr>
</table>
</div>
<html:form action="/PMmodule/ProgramManager.do">
	<table class="simple" cellspacing="2" cellpadding="3" width="100%">
		<thead>
			<tr>
				<th>Status</th>
				<th>Type</th>
				<th>Facility</th>
				<th>&nbsp;</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td><html:select property="searchStatus">
					<html:option value="Any" />
					<html:option value="active" />
					<html:option value="inactive" />
				</html:select></td>
				<td><html:select property="searchType">
					<html:option value="Any" />
					<html:option value="Bed" />
					<html:option value="Service" />
					<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="false">
						<html:option value="External" />
						<html:option value="community">Community</html:option>
					</caisi:isModuleLoad>
				</html:select></td>
				<td><html-el:select property="searchFacilityId">
					<html-el:option value="0">Any</html-el:option>
					<c:forEach var="facility" items="${facilities}">
						<html-el:option value="${facility.id}">
							<c:out value="${facility.name}" />
						</html-el:option>
					</c:forEach>
				</html-el:select></td>
				<td><input type="button" name="search" value="Search"
					onclick="javascript:submitForm('list')" ; /></td>
			</tr>
			<tr>
				<td colspan="4">&nbsp;</td>
			</tr>
		</tbody>
	</table>
</html:form>
<display:table class="simple" cellspacing="2" cellpadding="3"
	id="program" name="programs" export="false" pagesize="0"
	requestURI="/PMmodule/ProgramManager.do">
	<display:setProperty name="paging.banner.placement" value="bottom" />
	<display:setProperty name="paging.banner.item_name" value="program" />
	<display:setProperty name="paging.banner.items_name" value="programs" />
	<display:setProperty name="basic.msg.empty_list"
		value="No programs found." />

	<display:column sortable="false" title="">
		<a
			onclick="return ConfirmDelete('<c:out value="${program.nameJs}"/>')"
			href="<html:rewrite action="/PMmodule/ProgramManager.do"/>?method=delete&id=<c:out value="${program.id}"/>&name=<c:out value="${program.name}"/>">
		Delete </a>
	</display:column>

	<c:choose>
		<c:when test="${program.programStatus=='active'}">
			<display:column sortable="false" title="">
				<a
					href="<html:rewrite action="/PMmodule/ProgramManager.do"/>?method=edit&id=<c:out value="${program.id}" />">
				Edit </a>
			</display:column>
		</c:when>
		<c:otherwise>
			<display:column sortable="false" title="">
 		Edit 
	</display:column>
		</c:otherwise>
	</c:choose>

	<display:column sortable="true" title="Name">
		<a
			href="<html:rewrite action="/PMmodule/ProgramManagerView.do"/>?id=<c:out value="${program.id}" />">
		<c:out value="${program.name}" /> </a>
	</display:column>
	<display:column property="description" sortable="true"
		title="Description" />
	<display:column property="type" sortable="true" title="Type" />
	<display:column property="programStatus" sortable="true" title="Status" />
	<display:column property="location" sortable="true" title="Location" />
	<display:column sortable="true" title="Participation">
		<c:out value="${program.numOfMembers}" />/<c:out
			value="${program.maxAllowed}" />&nbsp;(<c:out
			value="${program.queueSize}" /> waiting)
	</display:column>
</display:table>
