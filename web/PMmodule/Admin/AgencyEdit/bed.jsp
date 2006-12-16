<%@include file="/taglibs.jsp"%>
<table width="100%" summary="Create and edit rooms, and beds">
	<tr>
		<td width="80%">
			<div class="tabs" id="roomsTab">
				<table cellpadding="3" cellspacing="0" border="0">
					<tr>
						<th>Rooms</th>
					</tr>
				</table>
			</div>
			<display:table class="simple" name="sessionScope.agencyManagerForm.rooms" uid="room" requestURI="/PMmodule/AgencyManager.do" summary="Edit rooms">
				<display:setProperty name="paging.banner.placement" value="bottom" />
			
				<display:column title="Name" sortable="true">
					<html:text property="rooms[${room_rowNum - 1}].name" value="${room.name}" />
				</display:column>
				<display:column title="Floor" sortable="true">
					<html:text property="rooms[${room_rowNum - 1}].floor" value="${room.floor}" />
				</display:column>
				<display:column title="Type">
					<html:select property="rooms[${room_rowNum - 1}].roomTypeId">
						<c:forEach var="roomType" items="${agencyManagerForm.map.roomTypes}">
							<c:choose>
								<c:when test="${roomType.id == room.roomTypeId}">
									<option value="<c:out value="${roomType.id}"/>" selected="selected">
										<c:out value="${roomType.name}" />
									</option>
								</c:when>
								<c:otherwise>
									<option value="<c:out value="${roomType.id}"/>">
										<c:out value="${roomType.name}" />
									</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</html:select>
				</display:column>
				<display:column title="Program">
					<html:select property="rooms[${room_rowNum - 1}].programId">
						<c:forEach var="program" items="${agencyManagerForm.map.programs}">
							<c:choose>
								<c:when test="${program.id == room.programId}">
									<option value="<c:out value="${program.id}"/>" selected="selected">
										<c:out value="${program.name}" />
									</option>
								</c:when>
								<c:otherwise>
									<option value="<c:out value="${program.id}"/>">
										<c:out value="${program.name}" />
									</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</html:select>
				</display:column>
				<display:column title="Active" sortable="true">
					<html:checkbox property="rooms[${room_rowNum - 1}].active" />
				</display:column>
			</display:table>
		</td>
		<td width="20%">
			<br />
			<html:submit onclick="agencyManagerForm.method.value='saveRooms';">Save Rooms</html:submit>
		</td>
	</tr>
	<tr>
		<td>
			<html:submit onclick="agencyManagerForm.method.value='addRoom';">Add Room</html:submit>
		</td>
	</tr>
	<tr>
		<td>
			<br />
		</td>
	</tr>
	<tr>
		<td width="80%">
			<div class="tabs" id="bedsTab">
				<table cellpadding="3" cellspacing="0" border="0">
					<tr>
						<th>Beds</th>
					</tr>
				</table>
			</div>
			<display:table class="simple" name="sessionScope.agencyManagerForm.beds" uid="bed" requestURI="/PMmodule/AgencyManager.do" summary="Edit beds">
				<display:setProperty name="paging.banner.placement" value="bottom" />
			
				<display:column title="Name" sortable="true">
					<html:text property="beds[${bed_rowNum - 1}].name" value="${bed.name}" />
				</display:column>
				<display:column title="Type">
					<html:select property="beds[${bed_rowNum - 1}].bedTypeId">
						<c:forEach var="bedType" items="${agencyManagerForm.map.bedTypes}">
							<c:choose>
								<c:when test="${bedType.id == bed.bedTypeId}">
									<option value="<c:out value="${bedType.id}"/>" selected="selected">
										<c:out value="${bedType.name}" />
									</option>
								</c:when>
								<c:otherwise>
									<option value="<c:out value="${bedType.id}"/>">
										<c:out value="${bedType.name}" />
									</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</html:select>
				</display:column>
				<display:column title="Room">
					<html:select property="beds[${bed_rowNum - 1}].roomId">
						<c:forEach var="room" items="${agencyManagerForm.map.rooms}">
							<c:choose>
								<c:when test="${room.id == bed.roomId}">
									<option value="<c:out value="${room.id}"/>" selected="selected">
										<c:out value="${room.name}" />
									</option>
								</c:when>
								<c:otherwise>
									<option value="<c:out value="${room.id}"/>">
										<c:out value="${room.name}" />
									</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</html:select>
				</display:column>
				<display:column property="reservationEnd" sortable="true" title="Reserved Until" />
				<display:column title="Active" sortable="true">
					<html:checkbox property="beds[${bed_rowNum - 1}].active" />
				</display:column>
			</display:table>
		</td>
		<td width="20%">
			<br />
			<html:submit onclick="agencyManagerForm.method.value='saveBeds';">Save Beds</html:submit>
		</td>
	</tr>
	<tr>
		<td>
			<c:choose>
				<c:when test="${not empty agencyManagerForm.map.rooms}">
					<html:submit onclick="agencyManagerForm.method.value='addBed';">Add Bed</html:submit>
				</c:when>
				<c:otherwise>
					<html:submit disabled="true" onclick="agencyManagerForm.method.value='addBed';">Add Bed</html:submit>
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>