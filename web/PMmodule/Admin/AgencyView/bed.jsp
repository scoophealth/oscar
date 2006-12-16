<%@ include file="/taglibs.jsp"%>

<table width="100%" summary="View rooms, and beds">
	<tr>
		<td width="80%">
			<div class="tabs" id="viewRoomsHeader">
				<table cellpadding="3" cellspacing="0" border="0">
					<tr>
						<th>Rooms</th>
					</tr>
				</table>
			</div>
			<display:table class="simple" name="rooms" uid="room" pagesize="5" requestURI="/PMmodule/AgencyManager.do">
				<display:setProperty name="paging.banner.placement" value="bottom" />
				
				<display:column property="name" sortable="true" />
				<display:column property="floor" sortable="true" />
				<display:column property="roomTypeName" sortable="true" title="Type" />
				<display:column property="programName" sortable="true" title="Program" />
				<display:column property="active" sortable="true" />
			</display:table>
		</td>
	</tr>
	<tr>
		<td>
			<br />
		</td>
	</tr>
	<tr>
		<td width="80%">
			<div class="tabs" id="viewBedsHeader">
				<table cellpadding="3" cellspacing="0" border="0">
					<tr>
						<th>Beds</th>
					</tr>
				</table>
			</div>
			<display:table class="simple" name="beds" uid="bed" pagesize="10" requestURI="/PMmodule/AgencyManager.do">
				<display:setProperty name="paging.banner.placement" value="bottom" />
				
				<display:column property="name" sortable="true" />
				<display:column property="bedTypeName" sortable="true" title="Type" />
				<display:column property="roomName" sortable="true" title="Room" />
				<display:column property="teamName" sortable="true" title="Team" />
				<display:column property="reservationEnd" sortable="true" title="Reserved Until" />
				<display:column property="active" sortable="true" />
			</display:table>
		</td>
	</tr>
</table>