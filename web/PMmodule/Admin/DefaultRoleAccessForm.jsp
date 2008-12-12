<%@ include file="/taglibs.jsp"%>

<div class="tabs" id="tabs">
<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<th title="Programs">Access</th>
	</tr>
</table>
</div>

<html:form action="/PMmodule/Admin/DefaultRoleAccess">
	<input type="hidden" name="method" value="save" />
	<html:hidden property="form.id" />

	<table width="100%" border="1" cellspacing="2" cellpadding="3"
		class="b">
		<tr class="b">
			<td width="20%">Role:</td>
			<td><html:select property="form.roleId">
				<html:options collection="roles" property="id" labelProperty="name" />
			</html:select></td>
		</tr>

		<tr class="b">
			<td width="20%">Access Type:</td>
			<td><html:select property="form.accessTypeId">
				<html:options collection="access_types" property="id"
					labelProperty="name" />
			</html:select></td>
		</tr>

		<tr>
			<td colspan="2"><html:submit value="Save" /> <input
				type="button" value="Cancel"
				onclick="location.href='<html:rewrite action="/PMmodule/Admin/DefaultRoleAccess"/>'" />
			</td>
		</tr>
	</table>
</html:form>

