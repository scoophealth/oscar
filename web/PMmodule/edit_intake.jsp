<%@page import="org.oscarehr.common.dao.IntakeRequiredFieldsDao"%>

<%@include file="/layouts/caisi_html_top.jspf"%>

<%
	String headerStyle="background-color:silver";
%>

<form method="post" action="" >
	<table>
		<tr>
			<td style="<%=headerStyle%>">Field</td>
			<td style="<%=headerStyle%>">is required</td>
		</tr>
		<tr>
			<td>First Name</td>
			<td><input name="<%=IntakeRequiredFieldsDao.FIELD_FIRST_NAME%>" type="checkbox" checked="<%=IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_FIRST_NAME)%>" /></td>
		</tr>
		<tr>
			<td>Last Name</td>
			<td><input name="<%=IntakeRequiredFieldsDao.FIELD_LAST_NAME%>" type="checkbox" checked="<%=IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_LAST_NAME)%>" /></td>
		</tr>
		<tr>
			<td>Gender</td>
			<td><input name="<%=IntakeRequiredFieldsDao.FIELD_GENDER%>" type="checkbox" checked="<%=IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_GENDER)%>" /></td>
		</tr>
		<tr>
			<td>Birth Date</td>
			<td><input name="<%=IntakeRequiredFieldsDao.FIELD_BIRTH_DATE%>" type="checkbox" checked="<%=IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_BIRTH_DATE)%>" /></td>
		</tr>
		<tr>
			<td>Email</td>
			<td><input name="<%=IntakeRequiredFieldsDao.FIELD_EMAIL%>" type="checkbox" checked="<%=IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_EMAIL)%>" /></td>
		</tr>
		<tr>
			<td>Phone #</td>
			<td><input name="<%=IntakeRequiredFieldsDao.FIELD_PHONE%>" type="checkbox" checked="<%=IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_PHONE)%>" /></td>
		</tr>
		<tr>
			<td>Secondary Phone #/td>
			<td><input name="<%=IntakeRequiredFieldsDao.FIELD_PHONE2%>" type="checkbox" checked="<%=IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_PHONE2)%>" /></td>
		</tr>
		<tr>
			<td>Street</td>
			<td><input name="<%=IntakeRequiredFieldsDao.FIELD_STREET%>" type="checkbox" checked="<%=IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_STREET)%>" /></td>
		</tr>
		<tr>
			<td>City</td>
			<td><input name="<%=IntakeRequiredFieldsDao.FIELD_CITY%>" type="checkbox" checked="<%=IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_CITY)%>" /></td>
		</tr>
		<tr>
			<td>Province</td>
			<td><input name="<%=IntakeRequiredFieldsDao.FIELD_PROVINCE%>" type="checkbox" checked="<%=IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_PROVINCE)%>" /></td>
		</tr>
		<tr>
			<td>Postal Code</td>
			<td><input name="<%=IntakeRequiredFieldsDao.FIELD_POSTAL_CODE%>" type="checkbox" checked="<%=IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_POSTAL_CODE)%>" /></td>
		</tr>
	</table>
	
	<br />
	<input type="submit" />
</form>

<%@include file="/layouts/caisi_html_bottom.jspf"%>
