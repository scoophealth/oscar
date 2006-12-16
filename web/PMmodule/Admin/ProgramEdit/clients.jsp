<%@ include file="/taglibs.jsp"%>
<script>
function assignTeam(id,selectBox) {
	var team_id = selectBox.options[selectBox.selectedIndex].value;
	document.programManagerForm.elements['admission.teamId'].value=team_id;
	document.programManagerForm.elements['admission.id'].value=id;
	document.programManagerForm.method.value='assign_team_client';
	document.programManagerForm.submit();
}
</script>
<html:hidden property="admission.id" />
<html:hidden property="admission.teamId" />
<div class="tabs">
<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<th title="Programs">Clients</th>
	</tr>
</table>
</div>
<!--  show current clients -->
<display:table class="simple" cellspacing="2" cellpadding="3" id="admission" name="admissions" export="false" pagesize="0" requestURI="/PMmodule/ProgramManager.do">
	<display:setProperty name="paging.banner.placement" value="bottom" />
	<display:setProperty name="basic.msg.empty_list" value="No clients currently admitted to this program." />
	
	<display:column sortable="false" title="">
		<a href="javascript:void(0);return false;" onclick="alert('Please discharge clients from the client manager');"> Discharge </a>
	</display:column>
	<display:column property="client.formattedName" sortable="true" title="Name" />
	<display:column property="admissionDate" sortable="true" title="Admission Date" />
	<display:column sortable="true" title="Team">
		<select name="x" onchange="assignTeam('<c:out value="${admission.id}"/>',this);">
			<option value="0">&nbsp;</option>
			<c:forEach var="team" items="${teams}">
				<c:choose>
					<c:when test="${team.id == admission.teamId}">
						<option value="<c:out value="${team.id}"/>" selected><c:out value="${team.name}" /></option>
					</c:when>
					<c:otherwise>
						<option value="<c:out value="${team.id}"/>"><c:out value="${team.name}" /></option>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</select>
	</display:column>
	<display:column property="temporaryAdmission" sortable="true" title="Temporary Admission" />
	<display:column property="admissionNotes" sortable="true" title="Admission Notes" />
</display:table>
