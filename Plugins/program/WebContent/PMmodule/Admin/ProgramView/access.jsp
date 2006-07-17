<%@ include file="/taglibs.jsp" %>
<br/><br/>
		<div class="tabs" id="tabs">
			<table cellpadding="3" cellspacing="0" border="0">
				<tr>
					<th title="Programs">Access</th>
				</tr>
			</table>
		</div>

		<display:table class="b" border="1" cellspacing="2" cellpadding="3" width="100%" id="access" name="accesses" export="false" pagesize="0" requestURI="/PMmodule/ProgramManagerView.do">
		  <display:setProperty name="paging.banner.placement" value="bottom"/>
		  <display:setProperty name="basic.msg.empty_list" value="No access currently defined for this program."/>
		  <display:column property="accessType.name" sortable="true" title="Name"/>
		  <display:column property="accessType.type" sortable="true" title="Type"/>
		  <display:column property="allRoles" sortable="true" title="All Roles"/>
	 	  <display:column sortable="true" title="Role(s)">  
			<ul>
				<c:forEach var="role" items="${access.roles}">
					<li><c:out value="${role.name}"/></li>
				</c:forEach>
			</ul>
		  </display:column>      	
		</display:table>			
