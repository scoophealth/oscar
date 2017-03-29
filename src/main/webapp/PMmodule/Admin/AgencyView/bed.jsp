
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

<table width="100%" summary="View rooms, and beds">
	<tr>
		<td width="100%">
		<div class="tabs" id="viewRoomsHeader">
		<table cellpadding="3" cellspacing="0" border="0">
			<tr>
				<th>Rooms</th>
			</tr>
		</table>
		</div>
		<display:table class="simple" name="rooms" uid="room" pagesize="5"
			requestURI="/PMmodule/AgencyManager.do">
			<display:setProperty name="paging.banner.placement" value="bottom" />

			<display:column property="name" sortable="true" />
			<display:column property="floor" sortable="true" />
			<display:column property="roomTypeName" sortable="true" title="Type" />
			<display:column property="programName" sortable="true"
				title="Program" />
			<display:column property="active" sortable="true" />
		</display:table></td>
	</tr>
	<tr>
		<td><br />
		</td>
	</tr>
	<tr>
		<td width="100%">
		<div class="tabs" id="viewBedsHeader">
		<table cellpadding="3" cellspacing="0" border="0">
			<tr>
				<th>Beds</th>
			</tr>
		</table>
		</div>
		<display:table class="simple" name="beds" uid="bed" pagesize="10"
			requestURI="/PMmodule/AgencyManager.do">
			<display:setProperty name="paging.banner.placement" value="bottom" />

			<display:column property="name" sortable="true" />
			<display:column property="bedTypeName" sortable="true" title="Type" />
			<display:column property="roomName" sortable="true" title="Room" />
			<display:column property="teamName" sortable="true" title="Team" />
			<display:column property="reservationEnd" sortable="true"
				title="Reserved Until" />
			<display:column property="active" sortable="true" />
		</display:table></td>
	</tr>
</table>
