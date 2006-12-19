<%@ include file="/taglibs.jsp" %>

<div class="tabs">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th>Bed Reservation</th>
		</tr>
	</table>
</div>

<c:choose>
	<c:when test="${empty bedProgramId}">
		<table class="simple" cellspacing="2" cellpadding="3">
			<tr>
				<td>
					<span style="color:red">Client is not admitted to a bed program</span>
				</td>
			</tr>
		</table>
	</c:when>
	<c:otherwise>
		<table class="simple" cellspacing="2" cellpadding="3">
			<tr>
				<th width="20%">Reserved</th>
				<td>
					<html:select property="bedDemographic.bedId">
						<c:forEach var="unreservedBed" items="${clientManagerForm.map.unreservedBeds}">
							<c:choose>
								<c:when test="${unreservedBed.id == clientManagerForm.map.bedDemographic.bedId}">
									<option value="<c:out value="${unreservedBed.id}"/>" selected="selected">
										<c:out value="${unreservedBed.name}" />
									</option>
								</c:when>
								<c:otherwise>
									<option value="<c:out value="${unreservedBed.id}"/>">
										<c:out value="${unreservedBed.name}" />
									</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</html:select>
				</td>
			</tr>
			<tr>
				<th width="20%">Status</th>
				<td>
					<html:select property="bedDemographic.bedDemographicStatusId">
						<c:forEach var="bedDemographicStatus" items="${clientManagerForm.map.bedDemographicStatuses}">
							<c:choose>
								<c:when test="${bedDemographicStatus.id == clientManagerForm.map.bedDemographic.bedDemographicStatusId}">
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
					</html:select>
				</td>
			</tr>
			<tr>
				<th width="20%">Late Pass</th>
				<td>
					<html:checkbox property="bedDemographic.latePass" />
				</td>
			</tr>
			<tr>
				<th width="20%">Reserved Until</th>
				<td>
					<input type="text" name="bedDemographic.strReservationEnd" id="strReservationEnd_field" readonly="readonly" value="<c:out value="${clientManagerForm.map.bedDemographic.strReservationEnd}"/>" />
					<img align="top" src="<html:rewrite page="/images/calendar.gif" />" id="strReservationEnd_field-button" alt="Reserve Until Calendar" title="Reserve Until Calendar" />
					
					<script type="text/javascript">
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
					</script>
				</td>
			</tr>
		</table>
		<table>
			<tr>
				<td>
					<html:submit onclick="clientManagerForm.method.value='saveBedReservation';">Save Bed Reservation</html:submit>
				</td>
				<td>
					<html:reset>Reset</html:reset>
				</td>
			</tr>
		</table>
	</c:otherwise>
</c:choose>
