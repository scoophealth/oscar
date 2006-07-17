<%@ include file="/taglibs.jsp" %>
<script>
	function deleteAccess(id) {
		document.programManagerForm.elements['access.id'].value=id;
		document.programManagerForm.method.value='delete_access';
		document.programManagerForm.submit();
	}
</script>

		<div class="tabs" id="tabs">
			<table cellpadding="3" cellspacing="0" border="0">
				<tr>
					<th title="Programs">Access</th>
				</tr>
			</table>
		</div>

		<display:table class="b" border="1" cellspacing="2" cellpadding="3" width="100%" id="access" name="accesses" export="false" pagesize="0" requestURI="/PMmodule/ProgramManager.do">
		  <display:setProperty name="paging.banner.placement" value="bottom"/>
		  <display:setProperty name="basic.msg.empty_list" value="No access currently defined for this program."/>
		  <display:column sortable="false" title="">  
			<a href="javascript:void(0);" onclick="deleteAccess('<c:out value="${access.id}"/>');return false;">
				Delete
			</a>
		  </display:column>
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


		<br/><br/>
		
		<table width="100%" border="1" cellspacing="2" cellpadding="3">
			<tr class="b">
				<td width="20%">Name :</td>
				<td>
					<html:select property="access.accessTypeId">
						<html:options collection="accessTypes" property="id" labelProperty="name"/>
					</html:select>
					<html:hidden property="access.id"/>				
					<html:hidden property="access.programId"/>
				</td>
			</tr>
			<tr class="b">
				<td width="20%">All Roles:</td>
				<td>
					<html:checkbox property="access.allRoles"/>
				</td>
			</tr>
			<tr class="b">
				<td width="20%">Roles:</td>
				<td>
					<c:forEach var="role" items="${roles}">
						<input name="checked_role" value="<c:out value="${role.id}"/>" type="checkbox"/>&nbsp;<c:out value="${role.name}"/><br/>
					</c:forEach>
				</td>
			</tr>			
			<tr>
				<td colspan="2">
		                <input type="button" value="Save" onclick="this.form.method.value='save_access';this.form.submit()"/>
		                <html:cancel/>
				</td>
			</tr>
		</table>	
