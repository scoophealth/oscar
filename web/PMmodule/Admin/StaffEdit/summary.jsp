<%@ include file="/taglibs.jsp"%>
<div class="tabs" id="tabs">
<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<th title="Programs">Programs</th>
	</tr>
</table>
</div>
<!--  show current staff -->
<display:table class="simple" cellspacing="2" cellpadding="3" id="program" name="programs" export="false" pagesize="0" requestURI="/PMmodule/StaffManager.do">
	<display:setProperty name="paging.banner.placement" value="bottom" />
	<display:setProperty name="basic.msg.empty_list" value="Not currently assigned to any programs." />
	
	<display:column sortable="true" title="Program Name">
		<a href="<html:rewrite action="/PMmodule/ProgramManager"/>?id=<c:out value="${program.programId}"/>&view.tab=staff&method=edit"><c:out value="${program.programName}" /></a>
	</display:column>
	<display:column property="role.name" sortable="true" title="Role" />
	<display:column sortable="true" title="Team(s)">
		<ul>
			<c:forEach var="team" items="${program.teams }">
				<li><c:out value="${team.name}" /></li>
			</c:forEach>
		</ul>
	</display:column>
</display:table>