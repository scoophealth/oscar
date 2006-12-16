<%@ include file="/taglibs.jsp"%>

<div class="tabs" id="tabs">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th title="Programs">General Information</th>
		</tr>
	</table>
</div>
<table width="100%" border="1" cellspacing="2" cellpadding="3">
	<tr class="b">
		<td width="20%">Agency Id:</td>
		<td><c:out value="${requestScope.id}" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Name:</td>
		<td><html:text property="agency.name" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Description:</td>
		<td><html:text property="agency.descr" /></td>
	</tr>
	<tr class="b">
		<td width="20%">HIC:</td>
		<td><html:checkbox property="agency.hic" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Primary Contact Name:</td>
		<td><html:text property="agency.contactName" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Primary Contact Email:</td>
		<td><html:text property="agency.contactEmail" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Primary Contact Phone:</td>
		<td><html:text property="agency.contactPhone" /></td>
	</tr>
	<tr>
		<td colspan="2">
			<input type="button" value="Save" onclick="this.form.method.value='save';this.form.submit()" />
			<html:cancel />
		</td>
	</tr>
</table>
