<%@ include file="/taglibs.jsp" %>

	<br/>
	<%@ include file="/messages.jsp"%>
	<br/>
	<div class="tabs" id="tabs">
		<table cellpadding="3" cellspacing="0" border="0">
	
			<tr>
				<th title="Programs">General Information</th>
	
			</tr>
		</table>
	</div>

		<table width="100%" border="1" cellspacing="2" cellpadding="3">
			<tr class="b">
				<td width="20%">Agency Id:</td>
				<td><c:out value="${agency.id}"/></td>
			</tr>
			<tr class="b">
				<td width="20%">Name:</td>
				<td><c:out value="${agency.name}"/></td>
			</tr>
			<tr class="b">
				<td width="20%">Description:</td>
				<td><c:out value="${agency.descr}"/></td>
			</tr>
			<tr class="b">
				<td width="20%">Primary Contact Name:</td>
				<td><c:out value="${agency.contactName}"/></td>
			</tr>
			<tr class="b">
				<td width="20%">Primary Contact Email:</td>
				<td><c:out value="${agency.contactEmail}"/></td>
			</tr>
			<tr class="b">
				<td width="20%">Primary Contact Phone:</td>
				<td><c:out value="${agency.contactPhone}"/></td>
			</tr>
		</table>
		