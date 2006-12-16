<%@ include file="/taglibs.jsp"%>
<div class="tabs" id="tabs">
<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<th title="Programs">Team Management</th>
	</tr>
</table>
</div>
<!--  show current staff -->
<display:table class="simple" cellspacing="2" cellpadding="3" id="team" name="teams" export="false" pagesize="0" requestURI="/PMmodule/ProgramManagerView.do">
	<display:setProperty name="paging.banner.placement" value="bottom" />
	<display:setProperty name="basic.msg.empty_list" value="No teams are currently defined for this program." />
	<display:column property="name" sortable="true" title="Name" />
	<display:column sortable="true" title="Staff">
		<ul>
			<c:forEach var="provider" items="${team.providers}">
				<li><c:out value="${provider.provider.formattedName}" /> (<c:out value="${provider.role.name}" />)</li>
			</c:forEach>
		</ul>
	</display:column>
	<display:column sortable="true" title="Clients">
		<ul>
			<c:forEach var="admission" items="${team.admissions}">
				<li><c:out value="${admission.client.formattedName}" /></li>
			</c:forEach>
		</ul>
	</display:column>
</display:table>
