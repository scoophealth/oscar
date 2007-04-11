<!--
	Copyright (c) 2001-2002.
	
	Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
	This software is published under the GPL GNU General Public License.
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
	See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
	
	OSCAR TEAM
	
	This software was written for Centre for Research on Inner City Health, St. Michael's Hospital, Toronto, Ontario, Canada
-->
<%@ include file="/taglibs.jsp"%>

<script type="text/javascript">
	function popupBedReservationChangeReport(reservedBedId) {
		url = '<html:rewrite page="/PMmodule/ProgramManagerView.do?method=viewBedReservationChangeReport&reservedBedId="/>';
		window.open(url + reservedBedId, 'bedHistoryReport', 'width=800,height=400');
	}
	
	function popupBedCheckReport(programId) {
		url = '<html:rewrite page="/PMmodule/ProgramManagerView.do?method=viewBedCheckReport&programId="/>';
		window.open(url + programId, 'bedCheckReport', 'width=1000,height=600,scrollbars=1');
	}
</script>

<table width="100%" summary="View program reserved beds">
	<tr>
		<td>
			<div class="tabs">
				<table cellpadding="3" cellspacing="0" border="0">
					<tr>
						<th>Reserved Beds</th>
					</tr>
				</table>
			</div>
			<display:table class="simple" name="sessionScope.programManagerViewForm.reservedBeds" uid="reservedBed" requestURI="/PMmodule/ProgramManagerView.do">
				<display:column>
					<a href="javascript:void(0)" onclick="popupBedReservationChangeReport('<c:out value="${reservedBed.id}" />')">
						<img src="<c:out value="${ctx}" />/images/details.gif" border="0" />
					</a>
				</display:column>
				<display:column property="roomName" title="Room" />
				<display:column property="name" title="Bed" />
				<display:column property="demographicName" title="Client" />
				<!-- status is editable -->
				<display:column title="Status">
					<select name="reservedBeds[<c:out value="${reservedBed_rowNum - 1}" />].statusId">
						<c:forEach var="bedDemographicStatus" items="${bedDemographicStatuses}">
							<c:choose>
								<c:when test="${bedDemographicStatus.id == reservedBed.statusId}">
									<option value="<c:out value="${bedDemographicStatus.id}"/>" selected="selected">
										<c:out value="${bedDemographicStatus.name}" />
									</option>
								</c:when>
								<c:otherwise>
									<option value="<c:out value="${bedDemographicStatus.id}"/>">
										<c:out value="${bedDemographicStatus.name}" />
									</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</display:column>
				<!-- latePass is editable -->
				<display:column title="Late Pass">
					<c:choose>
						<c:when test="${reservedBed.latePass}">
							<input type="checkbox" name="reservedBeds[<c:out value="${reservedBed_rowNum - 1}" />].latePass" checked="checked" />
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="reservedBeds[<c:out value="${reservedBed_rowNum - 1}" />].latePass" />
						</c:otherwise>
					</c:choose>
				</display:column>
				<display:column property="reservationStart" title="Since" />
				<!-- reservationEnd is editable -->
				<display:column title="Until">
					<input type="text" name="reservedBeds[<c:out value="${reservedBed_rowNum - 1}" />].strReservationEnd" id="strReservationEnd_<c:out value="${reservedBed_rowNum - 1}" />_field" readonly="readonly" value="<c:out value="${reservedBed.strReservationEnd}"/>" />
					
					<img align="top" src="<html:rewrite page="/images/calendar.gif" />" id="strReservationEnd_<c:out value="${reservedBed_rowNum - 1}" />_field-button" alt="Until Calendar" title="Until Calendar" />
					
					<script type="text/javascript">
						Calendar.setup(
							{
								inputField :  'strReservationEnd_' + <c:out value="${reservedBed_rowNum - 1}" /> + '_field',
								ifFormat :    '%Y-%m-%d',
								button :      'strReservationEnd_' + <c:out value="${reservedBed_rowNum - 1}" /> + '_field-button',
								align :       'cr',
								singleClick : true,
								firstDay :    1
							}
						);
					</script>
				</display:column>
				<!-- communityProgramId is editable -->
				<display:column title="Discharge To">
					<select name="reservedBeds[<c:out value="${reservedBed_rowNum - 1}" />].communityProgramId">
						<option value="0"></option>
						<c:forEach var="communityProgram" items="${communityPrograms}">
							<option value="<c:out value="${communityProgram.id}"/>"><c:out value="${communityProgram.name}" /></option>
						</c:forEach>
					</select>
				</display:column>
			</display:table>
		</td>
		<td>
			<br />
			<html:submit onclick="programManagerViewForm.method.value='saveReservedBeds';">Save</html:submit>
		</td>
	</tr>
	<tr>
		<td>
			Generate: <a href="javascript:void(0)" onclick="popupBedCheckReport('<c:out value="${id}"/>')">Bed Check Report</a> 
		</td>
	</tr>
	<tr>
		<td>
			<br />
		</td>
	</tr>
	<tr>
		<td>
			<div class="tabs">
				<table cellpadding="3" cellspacing="0" border="0">
					<tr>
						<th>Todays' Expired Reservations</th>
					</tr>
				</table>
			</div>
			<display:table class="simple" name="expiredReservations" uid="expiredReservation">
				<display:column property="bedName" title="Bed" />
				<display:column property="demographicName" title="Client" />
			</display:table>
		</td>
	</tr>
</table>