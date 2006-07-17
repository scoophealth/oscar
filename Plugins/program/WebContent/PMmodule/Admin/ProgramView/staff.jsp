<%@ include file="/taglibs.jsp" %>

	<br/>
	<%@ include file="/messages.jsp"%>
	<br/>
	
		<div class="tabs" id="tabs">
			<table cellpadding="3" cellspacing="0" border="0">
				<tr>
					<th title="Programs">Providers</th>
				</tr>
			</table>
		</div>	
		
		<display:table class="b" defaultsort="3" border="1" cellspacing="2" cellpadding="3" width="100%" id="provider" name="providers" export="false" pagesize="0" requestURI="/PMmodule/ProgramManagerView.do">
		  <display:setProperty name="paging.banner.placement" value="bottom"/>
		  <display:column sortable="false" title="Team(s)">
		  	<table width="100%" cellspacing="2" cellpadding="2">
			<c:forEach var="team" items="${provider.teams}">
				<tr>
				<td><c:out value="${team.name }"/></td>
				</tr>
			</c:forEach>
			</table>
		  </display:column>
		  <display:column property="provider.formattedName" sortable="true" title="Name"/>
		  <display:column property="provider.phone"  sortable="true" title="Phone" />
		  <display:column property="provider.providerType"  sortable="true" title="OSCAR Role" />
		  <display:column property="role.name"  sortable="true" title="CAISI Role"/>
		</display:table>
		
		
