<%@ include file="/taglibs.jsp" %>
<script>
function deleteTeam(id) {
	document.programManagerForm.elements['team.id'].value=id;
	document.programManagerForm.method.value='delete_team';
	document.programManagerForm.submit();
}

function editTeam(id) {
	document.programManagerForm.elements['team.id'].value=id;
	document.programManagerForm.method.value='edit_team';
	document.programManagerForm.submit();
}

function add_team(form) {
	if(form.elements['team.name'].value == '') {
		alert('You must choose a team name');
		return false;
	}
	form.elements['team.id'].value='0';
	form.method.value='save_team';
	form.submit();

}
</script>

		<div class="tabs" id="tabs">
			<table cellpadding="3" cellspacing="0" border="0">
				<tr>
					<th title="Programs">Team Management</th>
				</tr>
			</table>
		</div>
	
		<!--  show current staff -->
		<display:table class="b" border="1" cellspacing="2" cellpadding="3" width="100%" id="team" name="teams" export="false" pagesize="0" requestURI="/PMmodule/ProgramManager.do">
		  <display:setProperty name="paging.banner.placement" value="bottom"/>
		  <display:setProperty name="basic.msg.empty_list" value="No teams are currently defined for this program."/>
		  <display:column sortable="false" title="">  
			<a onclick="deleteTeam('<c:out value="${team.id}"/>');" href="javascript:void(0);">
				Delete
			</a>
		  </display:column>
		  <display:column property="name" sortable="true" title="Name"/>
		  <display:column sortable="true" title="Staff">
		  	<ul>
		  		<c:forEach var="provider" items="${team.providers}">
		  			<li><c:out value="${provider.provider.formattedName}"/> (<c:out value="${provider.role.name}"/>)</li>
		  		</c:forEach>
		  	</ul>
		  </display:column>
		  <display:column sortable="true" title="Clients">
		  	<ul>
		  		<c:forEach var="admission" items="${team.admissions}">
		  			<li><c:out value="${admission.client.formattedName}"/></li>
		  		</c:forEach>
		  	</ul>
		  </display:column>
		</display:table>
		<br/><br/>
		<table width="100%" border="1" cellspacing="2" cellpadding="3">
			<html:hidden property="team.id"/>
			<tr class="b">
				<td width="20%">Name:</td>
				<td><html:text property="team.name"/></td>
			</tr>
			<tr>
				<td colspan="2">
		                <input type="button" value="Save" onclick="add_team(this.form)"/>
		                <html:cancel/>
				</td>
			</tr>
		</table>
