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
</script>
<%@ include file="/common/messages.jsp"%>
<div class="tabs" id="tabs">
<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<th title="Facilities">Facilities management</th>
	</tr>
</table>
</div>
<html:form action="/PMmodule/FacilityManager.do">
	<display:table class="simple" cellspacing="2" cellpadding="3"
		id="facility" name="facilities" export="false" pagesize="0"
		requestURI="/PMmodule/FacilityManager.do">
		<display:setProperty name="paging.banner.placement" value="bottom" />
		<display:setProperty name="paging.banner.item_name" value="agency" />
		<display:setProperty name="paging.banner.items_name"
			value="facilities" />
		<display:setProperty name="basic.msg.empty_list"
			value="No facilities found." />

		<display:column sortable="false" title="">
			<a
				href="<html:rewrite action="/PMmodule/FacilityManager.do"/>?method=view&id=<c:out value="${facility.id}" />">
			Details </a>
		</display:column>
		<display:column sortable="false" title="">
			<a
				href="<html:rewrite action="/PMmodule/FacilityManager.do"/>?method=edit&id=<c:out value="${facility.id}" />">
			Edit </a>
		</display:column>
		<display:column sortable="false" title="">
			<a
				onclick="return ConfirmDelete('<c:out value="${facility.name}"/>')"
				href="<html:rewrite action="/PMmodule/FacilityManager.do"/>?method=delete&id=<c:out value="${facility.id}"/>&name=<c:out value="${facility.name}"/>">
			Disable </a>
		</display:column>


		<display:column property="name" sortable="true" title="Name" />
		<display:column property="description" sortable="true"
			title="Description" />
		<display:column property="contactName" sortable="true"
			title="Contact name" />
		<display:column property="hic" sortable="true" title="HIC?" />
		<display:column sortable="false" title="">
			<a
				href="<html:rewrite action="/PMmodule/BedManager.do"/>?method=manage&facilityId=<c:out value="${facility.id}" />" />Manage
			Beds </a>
		</display:column>
	</display:table>
</html:form>
<div>
<p><a
	href="<html:rewrite action="/PMmodule/FacilityManager.do"/>?method=add">
Add new facility </a></p>
</div>
