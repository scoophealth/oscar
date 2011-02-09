<%@ include file="/taglibs.jsp"%>
<style>
.sortable {
	background-color: #555;
	color: #555;
}

.b th {
	border-right: 1px solid #333;
	background-color: #ddd;
	color: #ddd;
	border-left: 1px solid #fff;
}

.message {
	color: red;
	background-color: white;
}

.error {
	color: red;
	background-color: white;
}
</style>

<br />
<%@ include file="/common/messages.jsp"%>
<br />

<div class="tabs" id="tabs">
<table cellpadding="3" cellspacing="0" border="0">

	<tr>
		<th title="Programs">Global Role Access</th>
	</tr>
</table>
</div>

<display:table class="simple" cellspacing="2" cellpadding="3" id="item"
	name="default_roles" export="false" pagesize="0"
	requestURI="/PMmodule/Admin/DefaultRoleAccess.do">
	<display:setProperty name="paging.banner.placement" value="bottom" />
	<display:column sortable="false" title="">
		<a
			href="<html:rewrite action="/PMmodule/Admin/DefaultRoleAccess"/>?method=delete&id=<c:out value="${item.id}"/>"
			title="Delete"> Delete </a>
	</display:column>
	<display:column property="caisi_role.name" sortable="true"
		title="Role" />
	<display:column property="access_type.name" sortable="true"
		title="Access Type" />
</display:table>
<br />
<button
	onclick="location.href='<html:rewrite action="/PMmodule/Admin/DefaultRoleAccess"/>?method=edit'">New
Access</button>
