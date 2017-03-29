
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

<%@ page import="org.oscarehr.PMmodule.web.formbean.*"%>
<html:form action="/PMmodule/AgencyManager">
	<table width="100%" summary="Create and edit rooms, and beds">
		<tr>
			<td width="80%">
			<div class="tabs">
			<table cellpadding="3" cellspacing="0" border="0">
				<tr>
					<th>Rooms</th>
				</tr>
			</table>
			</div>
			<display:table class="simple"
				name="sessionScope.agencyManagerForm.rooms" uid="room"
				requestURI="/PMmodule/AgencyManager.do" summary="Edit rooms">
				<display:column title="Name" sortable="true">
					<input type="text"
						name="rooms[<c:out value="${room_rowNum - 1}" />].name"
						value="<c:out value="${room.name}" />" />
				</display:column>
				<display:column title="Floor" sortable="true">
					<input type="text"
						name="rooms[<c:out value="${room_rowNum - 1}" />].floor"
						value="<c:out value="${room.floor}" />" />
				</display:column>
				<display:column title="Type">
					<select
						name="rooms[<c:out value="${room_rowNum - 1}" />].roomTypeId">
						<c:forEach var="roomType"
							items="${agencyManagerForm.map.roomTypes}">
							<c:choose>
								<c:when test="${roomType.id == room.roomTypeId}">
									<option value="<c:out value="${roomType.id}"/>"
										selected="selected"><c:out value="${roomType.name}" />
									</option>
								</c:when>
								<c:otherwise>
									<option value="<c:out value="${roomType.id}"/>"><c:out
										value="${roomType.name}" /></option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</display:column>
				<display:column title="Program">
					<select
						name="rooms[<c:out value="${room_rowNum - 1}" />].programId">
						<c:forEach var="program" items="${agencyManagerForm.map.programs}">
							<c:choose>
								<c:when test="${program.id == room.programId}">
									<option value="<c:out value="${program.id}"/>"
										selected="selected"><c:out value="${program.name}" />
									</option>
								</c:when>
								<c:otherwise>
									<option value="<c:out value="${program.id}"/>"><c:out
										value="${program.name}" /></option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</display:column>
				<display:column title="Active" sortable="true">
					<c:choose>
						<c:when test="${room.active}">
							<input type="checkbox"
								name="rooms[<c:out value="${room_rowNum - 1}" />].active"
								checked="checked" />
						</c:when>
						<c:otherwise>
							<input type="checkbox"
								name="rooms[<c:out value="${room_rowNum - 1}" />].active" />
						</c:otherwise>
					</c:choose>
				</display:column>
			</display:table></td>
			<td width="20%"><br />
			<html:submit onclick="agencyManagerForm.method.value='saveRooms';">Save Rooms</html:submit>
			</td>
		</tr>
		<tr>
			<td><html:text property="numRooms" /> <html:submit
				onclick="agencyManagerForm.method.value='addRooms';">Add Rooms</html:submit>
			</td>
		</tr>
		<tr>
			<td><br />
			</td>
		</tr>
		<tr>
			<td width="80%">
			<div class="tabs">
			<table cellpadding="3" cellspacing="0" border="0">
				<tr>
					<th>Beds</th>
				</tr>
			</table>
			</div>
			<display:table class="simple"
				name="sessionScope.agencyManagerForm.beds" uid="bed"
				requestURI="/PMmodule/AgencyManager.do" summary="Edit beds">
				<display:column title="Name" sortable="true">
					<input type="text"
						name="beds[<c:out value="${bed_rowNum - 1}" />].name"
						value="<c:out value="${bed.name}" />" />
				</display:column>
				<display:column title="Type">
					<select name="beds[<c:out value="${bed_rowNum - 1}" />].bedTypeId">
						<c:forEach var="bedType" items="${agencyManagerForm.map.bedTypes}">
							<c:choose>
								<c:when test="${bedType.id == bed.bedTypeId}">
									<option value="<c:out value="${bedType.id}"/>"
										selected="selected"><c:out value="${bedType.name}" />
									</option>
								</c:when>
								<c:otherwise>
									<option value="<c:out value="${bedType.id}"/>"><c:out
										value="${bedType.name}" /></option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</display:column>
				<display:column title="Room">
					<select name="beds[<c:out value="${bed_rowNum - 1}" />].roomId">
						<c:forEach var="room" items="${agencyManagerForm.map.rooms}">
							<c:choose>
								<c:when test="${room.id == bed.roomId}">
									<option value="<c:out value="${room.id}"/>" selected="selected">
									<c:out value="${room.name}" /></option>
								</c:when>
								<c:otherwise>
									<option value="<c:out value="${room.id}"/>"><c:out
										value="${room.name}" /></option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</display:column>
				<display:column property="reservationEnd" sortable="true"
					title="Reserved Until" />
				<display:column title="Active" sortable="true">
					<c:choose>
						<c:when test="${bed.active}">
							<input type="checkbox"
								name="beds[<c:out value="${bed_rowNum - 1}" />].active"
								checked="checked" />
						</c:when>
						<c:otherwise>
							<input type="checkbox"
								name="beds[<c:out value="${bed_rowNum - 1}" />].active" />
						</c:otherwise>
					</c:choose>
				</display:column>
			</display:table></td>
			<td width="20%"><br />
			<html:submit onclick="agencyManagerForm.method.value='saveBeds';">Save Beds</html:submit>
			</td>
		</tr>
		<tr>
			<td><c:choose>
				<c:when test="${not empty agencyManagerForm.map.rooms}">
					<html:text property="numBeds" />
					<html:submit onclick="agencyManagerForm.method.value='addBeds';">Add Beds</html:submit>
				</c:when>
				<c:otherwise>
					<html:submit disabled="true">Add Beds</html:submit>
				</c:otherwise>
			</c:choose></td>
		</tr>
	</table>
	<%@include file="/common/messages.jsp"%>

</html:form>
