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
<security:oscarSec roleName="<%=roleName$%>" objectName="_pmm" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_pmm");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<script>

	function validateAndSubmit() {
		var roomIdVal = document.clientManagerForm.elements['roomId'].value;
		if(roomIdVal == '' || roomIdVal == '0') {
			alert('Please choose a room');
			return false;
		}

		var bedIdVal = document.clientManagerForm.elements['bedDemographic.bedId'].value;
		if(bedIdVal == '' || bedIdVal == '0') {
			alert('Please choose a bed');
			return false;
		}

		var untilVal = document.clientManagerForm.elements['strReservationEnd_field'].value;
		if(untilVal == '') {
			alert('Please choose an end date');
			return false;
		}
		
		                            	
		clientManagerForm.method.value='saveBedReservation';
		return true;
	}

</script>

<div class="tabs">
<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<th>Bed/Room Reservation</th>
	</tr>
</table>
</div>

<c:choose>
	<c:when test="${empty bedProgramId}">
		<table class="simple" cellspacing="2" cellpadding="3">
			<tr>
				<td><span style="color: red">Client is not admitted to a
				bed program</span></td>
			</tr>
		</table>
	</c:when>
</c:choose>
<table class="simple" cellspacing="2" cellpadding="3">

	<!-- Begin of Assign Room -------------------------------------------------------------->

	<tr>
		<th width="20%">Assign Room</th>
		<td><c:choose>
			<c:when test="${availableRooms == null}">
				<select property="roomId">
					<option value="" selected="selected">No available rooms</option>
				</select>
			</c:when>
			<c:otherwise>

				<select name="roomId"
					onchange="clientManagerForm.method.value='refreshBedDropDownForReservation';clientManagerForm.submit();">
					<option value="0">Select a room</option>
					<c:forEach var="availableRoom" items="${availableRooms}">
						<c:choose>
							<c:when test="${roomId == availableRoom.id}">
								<option value="<c:out value="${availableRoom.id}"/>"
									selected="selected"><c:out
									value="${availableRoom.name}" /></option>
							</c:when>
							<c:otherwise>
								<option value="<c:out value="${availableRoom.id}"/>"><c:out
									value="${availableRoom.name}" /></option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</select>

			</c:otherwise>
		</c:choose></td>
	</tr>
	<!-- End of Assign Room -------------------------------------------------------------->
	<tr>
		<th width="20%">Assign Bed</th>
		<td><html:select property="bedDemographic.bedId">
			<c:choose>
				<c:when test="${!isAssignedBed}">
					<option value="0" selected="selected"><c:out value="N/A" />
					</option>
				</c:when>
				<c:otherwise>
					<option value="0"></option>
				</c:otherwise>
			</c:choose>

			<c:forEach var="unreservedBed"
				items="${clientManagerForm.map.unreservedBeds}">
				<c:choose>
					<c:when
						test="${unreservedBed.id == clientManagerForm.map.bedDemographic.bedId}">
						<option value="<c:out value="${unreservedBed.id}"/>"
							selected="selected"><c:out
							value="${unreservedBed.roomName}" /> - <c:out
							value="${unreservedBed.name}" /></option>
					</c:when>
					<c:otherwise>
						<option value="<c:out value="${unreservedBed.id}"/>"><c:out
							value="${unreservedBed.roomName}" /> - <c:out
							value="${unreservedBed.name}" /></option>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</html:select></td>
	</tr>
	<tr>
		<th width="20%">Status</th>
		<td><html:select property="bedDemographic.bedDemographicStatusId">
			<c:forEach var="bedDemographicStatus"
				items="${clientManagerForm.map.bedDemographicStatuses}">
				<c:choose>
					<c:when
						test="${bedDemographicStatus.id == clientManagerForm.map.bedDemographic.bedDemographicStatusId}">
						<option value="<c:out value="${bedDemographicStatus.id}"/>"
							selected="selected"><c:out
							value="${bedDemographicStatus.name}" /></option>
					</c:when>
					<c:otherwise>
						<option value="<c:out value="${bedDemographicStatus.id}"/>">
						<c:out value="${bedDemographicStatus.name}" /></option>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</html:select></td>
	</tr>
	<tr>
		<th width="20%">Late Pass</th>
		<td><html:checkbox property="bedDemographic.latePass" /></td>
	</tr>
	<tr>
		<th width="20%">Until</th>
		<td><c:choose>
			<c:when
				test="${clientManagerForm.map.bedDemographic.strReservationEnd == clientManagerForm.map.bedDemographic.infiniteDate}">
				<c:set var="endDate" value="" />
			</c:when>
			<c:otherwise>
				<c:set var="endDate"
					value="${clientManagerForm.map.bedDemographic.strReservationEnd}" />
			</c:otherwise>
		</c:choose> <input type="hidden" name="bedDemographic.strReservationEnd"
			id="strReservationEnd_field_hidden"
			value="<c:out value="${clientManagerForm.map.bedDemographic.strReservationEnd}"/>" />
		<input type="text" name="" id="strReservationEnd_field"
			readonly="readonly" value="<c:out value="${endDate}"/>"
			onchange="setEndDate();" /> <img align="top"
			src="<html:rewrite page="/images/calendar.gif" />"
			id="strReservationEnd_field-button" alt="Reserve Until Calendar"
			title="Reserve Until Calendar" /> <script type="text/javascript">
					
						Calendar.setup(
							{
								inputField :  'strReservationEnd_field',
								ifFormat :    '%Y-%m-%d',
								button :      'strReservationEnd_field-button',
								align :       'cr',
								singleClick : true,
								firstDay :    1
							}
						);
						
						function setEndDate(){
							document.getElementById('strReservationEnd_field_hidden').value = document.getElementById('strReservationEnd_field').value;
						}
					</script></td>
	</tr>
</table>
<table>
	<tr>
		<td><html:submit
			onclick="return validateAndSubmit();">Save</html:submit>
		</td>
		<td><html:reset>Reset</html:reset></td>
	</tr>
</table>
