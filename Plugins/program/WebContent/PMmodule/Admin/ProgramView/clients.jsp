<%@ include file="/taglibs.jsp" %>
<script>
function assignTeam(id,selectBox) {
	var team_id = selectBox.options[selectBox.selectedIndex].value;
	document.programManagerViewForm.elements['teamId'].value=team_id;
	document.programManagerViewForm.elements['admissionId'].value=id;
	document.programManagerViewForm.method.value='assign_team_client';
	document.programManagerViewForm.submit();

}
</script>
<input type="hidden" name="teamId" value=""/>
<input type="hidden" name="admissionId" value=""/>
<input type="hidden" name="program_name" value="<c:out value="${program_name}"/>"/>
	<br/>
	<%@ include file="/messages.jsp"%>
	<br/>

		<div class="tabs" id="tabs">
			<table cellpadding="3" cellspacing="0" border="0">
				<tr>
					<th title="Programs">Clients</th>
				</tr>
			</table>
		</div>
		<!--  show current clients -->
		<display:table class="b" border="1" cellspacing="2" cellpadding="3" width="100%" id="admission" name="admissions" export="false" pagesize="0" requestURI="/PMmodule/ProgramManagerView.do">
		  <display:setProperty name="paging.banner.placement" value="bottom"/>
		  <display:setProperty name="basic.msg.empty_list" value="No clients currently admitted to this program."/>
		  <display:column property="client.formattedName" sortable="true" title="Name"/>
		  <display:column property="admissionDate" sortable="true" title="Admission Date"/>
	      <display:column property="admissionNotes"  sortable="true" title="Admission Notes" />	
  		  <display:column sortable="true" title="Team">
			<select name="x"  onchange="assignTeam('<c:out value="${admission.id}"/>',this);">
		  		<option value="0">&nbsp;</option>
		  		<c:forEach var="team" items="${teams}">
		  			<c:choose>
		  				<c:when test="${team.id == admission.teamId}">
				  			<option value="<c:out value="${team.id}"/>" selected><c:out value="${team.name}"/></option>
				  		</c:when>
				  		<c:otherwise>
				  			<option value="<c:out value="${team.id}"/>"><c:out value="${team.name}"/></option>
				  		</c:otherwise>
				  	</c:choose>
		  		</c:forEach>
		  		</select>
		  </display:column>
		</display:table>	

		
