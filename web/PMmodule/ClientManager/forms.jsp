<%@ include file="/taglibs.jsp"%>

<input type="hidden" name="clientId" value="" />
<input type="hidden" name="formId" value="" />

<script>
function updateQuickIntake(clientId) {
	location.href = '<html:rewrite action="/PMmodule/GenericIntake/Edit.do"/>' + "?method=update&type=quick&clientId=" + clientId;
}

function updateIndepthIntake(clientId) {
	location.href = '<html:rewrite action="/PMmodule/GenericIntake/Edit.do"/>' + "?method=update&type=indepth&clientId=" + clientId;
}

function updateProgramIntake(clientId, programId) {
	location.href = '<html:rewrite action="/PMmodule/GenericIntake/Edit.do"/>' + "?method=update&type=program&clientId=" + clientId + "&programId=" + programId;
}

function openSurvey() {
	var selectBox = document.clientManagerForm.elements['form.formId'];
	var index = selectBox.selectedIndex;
	var value = selectBox.options[index].value;
	
	document.clientManagerForm.clientId.value='<c:out value="${client.demographicNo}"/>';
	document.clientManagerForm.formId.value=value;
		
	var url = '<html:rewrite action="/PMmodule/Forms/SurveyExecute.do"/>';
	url += "?method=survey&formId=" + value;
	url += "&clientId=";
	url += '<c:out value="${client.demographicNo}"/>';
	
	location.href = url;
}
</script>

<div class="tabs">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th title="Programs">Registration Intake</th>
		</tr>
	</table>
</div>
<table class="simple" cellspacing="2" cellpadding="3">
	<thead>
		<tr>
			<th>Date</th>
			<th>Staff</th>
			<th>Actions</th>
		</tr>
	</thead>
	<c:forEach var="intake" items="${quickIntakes}">
		<tr>
			<td width="20%"><c:out value="${intake.createdOnStr}" /></td>
			<td><c:out value="${intake.staffName}" /></td>
			<td><input type="button" value="View" onclick="alert('Not yet implemented');" /></td>
		</tr>
	</c:forEach>
	<tr>
		<td colspan="3">
			<input type="button" value="Update" onclick="updateQuickIntake('<c:out value="${client.demographicNo}" />')" />
		</td>
	</tr>
</table>
<br />
<br />

<div class="tabs">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th title="Programs">Follow-up Intake</th>
		</tr>
	</table>
</div>
<table class="simple" cellspacing="2" cellpadding="3">
	<thead>
		<tr>
			<th>Date</th>
			<th>Staff</th>
			<th>Actions</th>
		</tr>
	</thead>
	<c:forEach var="intake" items="${indepthIntakes}">
		<tr>
			<td width="20%"><c:out value="${intake.createdOnStr}" /></td>
			<td><c:out value="${intake.staffName}" /></td>
			<td><input type="button" value="View" onclick="alert('Not yet implemented');" /></td>
		</tr>
	</c:forEach>
	<tr>
		<td colspan="3">
			<input type="button" value="Update" onclick="updateIndepthIntake('<c:out value="${client.demographicNo}" />')" />
		</td>
	</tr>
</table>
<br />
<br />

<div class="tabs">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th title="Programs">Program Intakes</th>
		</tr>
	</table>
</div>
<table class="simple" cellspacing="2" cellpadding="3">
	<thead>
		<tr>
			<th>Date</th>
			<th>Staff</th>
			<th>Actions</th>
		</tr>
	</thead>
	<c:forEach var="intake" items="${programIntakes}">
		<tr>
			<td width="20%"><c:out value="${intake.createdOnStr}" /></td>
			<td><c:out value="${intake.staffName}" /></td>
			<td>
				<input type="button" value="View" onclick="alert('Not yet implemented');" />
				<input type="button" value="Update" onclick="updateProgramIntake('<c:out value="${client.demographicNo}" />', '<c:out value="${intake.programId}" />')" />
			</td>
		</tr>
	</c:forEach>
</table>
<br />
<br />

<div class="tabs">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th title="Programs">User Created</th>
		</tr>
	</table>
</div>
<table class="simple" cellspacing="2" cellpadding="3">
	<thead>
		<tr>
			<th>Form Name</th>
			<th>Date</th>
			<th>Staff</th>
			<th>Actions</th>
		</tr>
	</thead>
	<c:forEach var="form" items="${surveys}">
		<tr>
			<td><c:out value="${form.description}" /></td>
			<td><c:out value="${form.dateCreated}" /></td>
			<td><c:out value="${form.username}" /></td>
			<td><input type="button" value="update" onclick="document.clientManagerForm.elements['form.formId'].value='<c:out value="${form.formId}"/>';openSurvey();" /></td>
		</tr>
	</c:forEach>
</table>
<br />
<br />

New User Created Form:&nbsp;
<html:select property="form.formId" onchange="openSurvey()">
	<html:option value="0">&nbsp;</html:option>
	<html:options collection="survey_list" property="formId" labelProperty="description" />
</html:select>