<%@ include file="/taglibs.jsp" %>

<br/><br/>
		<div class="tabs" id="tabs">
			<table cellpadding="3" cellspacing="0" border="0">
				<tr>
					<th title="Programs">Bed Log</th>
				</tr>
			</table>
		</div>
		
		<table width="100%" border="1" cellspacing="2" cellpadding="3">
			<tr class="b">
				<td width="20%">Enabled:</td>
				<td><c:out value="${bedlog.enabled}"/></td>
			</tr>
			<tr class="b">
				<td width="20%">Check Times:</td>
				<td>
					<c:forEach var="time" items="${bedlog.checkTimes}">
						<c:out value="${time}"/>
						<br/>
					</c:forEach>
				</td>
			</tr>
			<tr class="b">
				<td width="20%">Statuses:</td>
				<td>
					<c:forEach var="status" items="${bedlog.statuses}">
						<c:out value="${status}"/>
						<br/>
					</c:forEach>

				</td>
			</tr>
		</table>
		