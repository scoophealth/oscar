<%@ include file="/taglibs.jsp"%>
<script>
function search_provider(name) {
	var url = '<html:rewrite action="/PMmodule/ProviderSearch.do"/>';
		url += '?q=' + name;
		url += '&formName=programManagerForm';
		url += '&formElementId=function.providerNo';
		url += '&formElementName=providerName';
	
	var name = document.programManagerForm.elements['providerName'].value;
	
	window.open(url, 'provider_search','width=500, height=400');
}

function deleteFunctionalUser(id) {
	document.programManagerForm.elements['function.id'].value=id;
	document.programManagerForm.method.value='delete_function';
	document.programManagerForm.submit();
}

function editFunctionalUser(id) {
	document.programManagerForm.elements['function.id'].value=id;
	document.programManagerForm.method.value='edit_function';
	document.programManagerForm.submit();
}

function add_functional_user(form) {
	alert('temporarily disabled');
	return false;
/*
	if(form.elements['function.providerNo'].value == '' || form.elements['function.providerNo'].value == 0) {
		alert('You must choose a provider');
		return false;
	}
	form.elements['function.id'].value='0';
	form.method.value='save_function';
	form.submit();
*/
}
</script>
<div class="tabs">
<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<th title="Programs">Functional Users</th>
	</tr>
</table>
</div>
<!--  show current staff -->
<display:table class="simple" cellspacing="2" cellpadding="3" id="functional" name="functional_users" export="false" pagesize="0" requestURI="/PMmodule/ProgramManager.do">
	<display:setProperty name="paging.banner.placement" value="bottom" />
	<display:setProperty name="basic.msg.empty_list" value="No functional users defined for this program" />
	<display:column sortable="false" title="">
		<a onclick="deleteFunctionalUser('<c:out value="${functional.id}"/>');" href="javascript:void(0);"> Delete </a>
	</display:column>
	<display:column property="userType.name" sortable="true" title="Functional User Type" />
	<display:column property="provider.formattedName" sortable="true" title="Provider Name" />
</display:table>
<br />
<br />
<table width="100%" border="1" cellspacing="2" cellpadding="3">
	<tr class="b">
		<td width="20%">Provider :</td>
		<td>
			<html:hidden property="function.id" />
			<html:hidden property="function.providerNo" />
			<%
			String providerName = (String) request.getAttribute("providerName");
			if (providerName == null) {
				providerName = "";
			}
			%>
			<input type="text" name="providerName" size="30" value="<%=providerName%>" />
			<input type="button" value="Search" onclick="search_provider(this.form.providerName.value);" />
		</td>
	</tr>
	<tr class="b">
		<td width="20%">Functional User Type:</td>
		<td>
			<html:select property="function.userTypeId">
				<html:options collection="functionalUserTypes" property="id" labelProperty="name" />
			</html:select>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<input type="button" value="Save" onclick="add_functional_user(this.form)" />
			<html:cancel />
		</td>
	</tr>
</table>
