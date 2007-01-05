<%@ include file="/taglibs.jsp"%>

<script type="text/javascript">
	function popupBedReservationChangeReport(reservedBedId) {
		url = '<html:rewrite page="/PMmodule/ProgramManagerView.do?method=viewBedReservationChangeReport&reservedBedId="/>';
		window.open(url + reservedBedId, 'bedHistoryReport', 'width=800,height=400');
	}
	
	function popupBedCheckReport(programId) {
		url = '<html:rewrite page="/PMmodule/ProgramManagerView.do?method=viewBedCheckReport&programId="/>';
		window.open(url + programId, 'bedCheckReport', 'width=800,height=600');
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
				<display:column property="name" title="Bed" />
				<display:column property="roomName" title="Room" />
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
					<input type="text" name="reservedBeds[<c:out value="${reservedBed_rowNum - 1}" />].reservationEnd" id="reservationEnd_field" readonly="readonly" value="<c:out value="${reservedBed.reservationEnd}"/>" />
					
					<img align="top" src="<html:rewrite page="/images/calendar.gif" />" id="reservationEnd_field-button" alt="Until Calendar" title="Until Calendar" />
					
					<script type="text/javascript">
						Calendar.setup(
							{
								inputField :  'reservationEnd_field',
								ifFormat :    '%Y-%m-%d',
								button :      'reservationEnd_field-button',
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
						<th>Unreserved Beds</th>
					</tr>
				</table>
			</div>
			<display:table class="simple" name="unreservedBeds" uid="unreservedBed" requestURI="/PMmodule/ProgramManagerView.do">
				<display:column title="Client"></display:column>
				<display:column title="Program"></display:column>
			</display:table>
		</td>
	</tr>
</table>