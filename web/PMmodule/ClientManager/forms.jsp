<!-- 
/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/
 -->

<%@ include file="/taglibs.jsp"%>
<input type="hidden" name="clientId" value="" />
<input type="hidden" name="formId" value="" />
<script>
function openForm() {
	var url = '<html:rewrite action="/PMmodule/Forms/FollowUp.do"/>';
	url += "?demographicNo=";
	url += '<c:out value="${client.demographicNo}"/>';
	
	location.href = url;
}

function openFormA() {
	var url = '<html:rewrite action="/PMmodule/IntakeA.do"/>';
	url += "?demographicNo=";
	url += '<c:out value="${client.demographicNo}"/>';
	
	location.href = url;
}

function openFormC() {
	var url = '<html:rewrite action="/PMmodule/IntakeC.do"/>';
	url += "?demographicNo=";
	url += '<c:out value="${client.demographicNo}"/>';
	
	location.href = url;
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
			<th title="Programs">Follow Up Form (aka IntakeB)</th>
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
	<c:forEach var="form" items="${FormFollowUp_info}">
		<tr>
			<td width="20%"><c:out value="${form.formDate}" /></td>
			<td><c:out value="${form.providerName}" /></td>
			<td><input type="button" value="View" onclick="alert('Not yet implemented');" /></td>
		</tr>
	</c:forEach>
	<tr>
		<td colspan="3">
			<input type="button" value="Update" onclick="openForm()" />
		</td>
	</tr>
</table>
<br />
<br />

<div class="tabs">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th title="Programs">Form IntakeA</th>
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
	<c:forEach var="form" items="${FormAFollowUp_info}">
		<tr>
			<td width="20%"><c:out value="${form.formDate}" /></td>
			<td><c:out value="${form.providerName}" /></td>
			<td><input type="button" value="View" onclick="alert('Not yet implemented');" /></td>
		</tr>
	</c:forEach>
	<tr>
		<td colspan="3">
			<input type="button" value="Update" onclick="openFormA()" />
		</td>
	</tr>
</table>
<br />
<br />

<div class="tabs">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th title="Programs">Form IntakeC</th>
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
	<c:forEach var="form" items="${FormCFollowUp_info}">
		<tr>
			<td width="20%"><c:out value="${form.formDate}" /></td>
			<td><c:out value="${form.providerName}" /></td>
			<td><input type="button" value="View" onclick="alert('Not yet implemented');" /></td>
		</tr>
	</c:forEach>
	<tr>
		<td colspan="3"><input type="button" value="Update" onclick="openFormC()" /></td>
	</tr>
</table>
<br />
<br />

<div class="tabs">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th title="Programs">User Created Forms</th>
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

New Form:&nbsp;
<html:select property="form.formId" onchange="openSurvey()">
	<html:option value="0">&nbsp;</html:option>
	<html:options collection="survey_list" property="formId" labelProperty="description" />
</html:select>