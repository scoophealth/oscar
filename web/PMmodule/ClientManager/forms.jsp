<%@ include file="/taglibs.jsp"%>

<input type="hidden" name="clientId" value="" />
<input type="hidden" name="formId" value="" />
<input type="hidden" id="formInstanceId" value="0" />

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

function updateProgramIntake(clientId) {
	var selectBox = getElement('programWithIntakeId');
	var programId = selectBox.options[selectBox.selectedIndex].value;
	
	if (programId != null) {
		location.href = '<html:rewrite action="/PMmodule/GenericIntake/Edit.do"/>' + "?method=update&type=program&clientId=" + clientId + "&programId=" + programId;
		return true;
	}
	
	return false;
}

function printQuickIntake(clientId) {
	url = '<html:rewrite action="/PMmodule/GenericIntake/Edit.do"/>' + "?method=print&type=quick&clientId=" + clientId;
	window.open(url, 'quickIntakePrint', 'width=1024,height=768,scrollbars=1');
}

function printIndepthIntake(clientId) {
	url = '<html:rewrite action="/PMmodule/GenericIntake/Edit.do"/>' + "?method=print&type=indepth&clientId=" + clientId;
	window.open(url, 'indepthIntakePrint', 'width=1024,height=768,scrollbars=1');
}

function printProgramIntake(clientId, programId) {
	url = '<html:rewrite action="/PMmodule/GenericIntake/Edit.do"/>' + "?method=print&type=program&clientId=" + clientId + "&programId=" + programId;
	window.open(url, 'programIntakePrint', 'width=1024,height=768,scrollbars=1');
}

function openSurvey() {
	var selectBox = document.getElementById('form.formId');
	alert("111");
	var formId = selectBox.options[selectBox.selectedIndex].value;	 alert("2222");
	document.clientManagerForm.clientId.value='<c:out value="${client.demographicNo}"/>'; alert("4444");
	document.clientManagerForm.formId.value=formId;
		
	var id = document.getElementById('formInstanceId').value;alert("555");
	
	location.href = '<html:rewrite action="/PMmodule/Forms/SurveyExecute.do"/>' + "?method=survey&formId=" + formId + "&formInstanceId=" + id + "&clientId=" + '<c:out value="${client.demographicNo}"/>';
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
			<td><input type="button" value="Print Preview" onclick="printQuickIntake('<c:out value="${client.demographicNo}" />')" /></td>
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
			<td><input type="button" value="Print Preview" onclick="printIndepthIntake('<c:out value="${client.demographicNo}" />')" /></td>
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
				<input type="button" value="Print Preview" onclick="printProgramIntake('<c:out value="${client.demographicNo}" />', '<c:out value="${intake.programId}" />')" />
				<input type="button" value="Update" onclick="updateProgramIntake('<c:out value="${client.demographicNo}" />', '<c:out value="${intake.programId}" />')" />
			</td>
		</tr>
	</c:forEach>
	<tr>
		<td colspan="3">
			<html:select property="programWithIntakeId">
				<html:options collection="programsWithIntake" property="id" labelProperty="name" />
			</html:select>
			<input type="button" value="Update" onclick="updateProgramIntake('<c:out value="${client.demographicNo}" />')" />
		</td>
	</tr>
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
			<td><input type="button" value="update" onclick="document.clientManagerForm.elements['form.formId'].value='<c:out value="${form.formId}"/>';document.clientManagerForm.elements['formInstanceId'].value='<c:out value="${form.id}"/>';openSurvey();" /></td>
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