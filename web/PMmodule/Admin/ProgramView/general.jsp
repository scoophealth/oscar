<%@ include file="/taglibs.jsp"%>
<div class="tabs" id="tabs">
<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<th title="Programs">General Information</th>
	</tr>
</table>
</div>
<table width="100%" border="1" cellspacing="2" cellpadding="3">
	<tr class="b">
		<td width="20%">Name:</td>
		<td><c:out value="${program.name}" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Description:</td>
		<td><c:out value="${program.descr}" /></td>
	</tr>
	<tr class="b">
		<td width="20%">HIC:</td>
		<td><c:out value="${program.hic}" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Type:</td>
		<td><c:out value="${program.type}" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Agency:</td>
		<td><c:out value="${agency.name}" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Location:</td>
		<td><c:out value="${program.location}" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Client Participation:</td>
		<td><c:out value="${program.numOfMembers}" />/<c:out value="${program.maxAllowed}" />&nbsp;(<c:out value="${program.queueSize}" /> waiting)</td>
	</tr>
	<tr class="b">
		<td width="20%">Holding Tank:</td>
		<td><c:out value="${program.holdingTank}" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Allow Batch Admissions:</td>
		<td><c:out value="${program.allowBatchAdmission}" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Allow Batch Discharges:</td>
		<td><c:out value="${program.allowBatchDischarge}" /></td>
	</tr>
</table>
