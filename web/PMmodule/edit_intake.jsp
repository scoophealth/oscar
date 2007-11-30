<%@page import="org.oscarehr.common.dao.IntakeRequiredFieldsDao"%>

<%@include file="/layouts/caisi_html_top.jspf"%>

<%
	String headerStyle="background-color:silver";
%>

<form method="post" action="" >
	<input type="hidden" name="method" value="update" />
	<table>
		<tr>
			<td style="<%=headerStyle%>">Field</td>
			<td style="<%=headerStyle%>">is required</td>
		</tr>
		<tr>
			<td>First Name</td>
			<td><input name="<%=IntakeRequiredFieldsDao.FIELD_FIRST_NAME%>" type="checkbox" <%=IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_FIRST_NAME)?"checked=\"checked\"":""%> /></td>
		</tr>
		<tr>
			<td>Last Name</td>
			<td><input name="<%=IntakeRequiredFieldsDao.FIELD_LAST_NAME%>" type="checkbox" <%=IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_LAST_NAME)?"checked=\"checked\"":""%> /></td>
		</tr>
		<tr>
			<td>Gender</td>
			<td><input name="<%=IntakeRequiredFieldsDao.FIELD_GENDER%>" type="checkbox" <%=IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_GENDER)?"checked=\"checked\"":""%> /></td>
		</tr>
		<tr>
			<td>Birth Date</td>
			<td><input name="<%=IntakeRequiredFieldsDao.FIELD_BIRTH_DATE%>" type="checkbox" <%=IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_BIRTH_DATE)?"checked=\"checked\"":""%> /></td>
		</tr>
		<tr>
			<td>Email</td>
			<td><input name="<%=IntakeRequiredFieldsDao.FIELD_EMAIL%>" type="checkbox" <%=IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_EMAIL)?"checked=\"checked\"":""%> /></td>
		</tr>
		<tr>
			<td>Phone #</td>
			<td><input name="<%=IntakeRequiredFieldsDao.FIELD_PHONE%>" type="checkbox" <%=IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_PHONE)?"checked=\"checked\"":""%> /></td>
		</tr>
		<tr>
			<td>Secondary Phone #</td>
			<td><input name="<%=IntakeRequiredFieldsDao.FIELD_PHONE2%>" type="checkbox" <%=IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_PHONE2)?"checked=\"checked\"":""%> /></td>
		</tr>
		<tr>
			<td>Street</td>
			<td><input name="<%=IntakeRequiredFieldsDao.FIELD_STREET%>" type="checkbox" <%=IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_STREET)?"checked=\"checked\"":""%> /></td>
		</tr>
		<tr>
			<td>City</td>
			<td><input name="<%=IntakeRequiredFieldsDao.FIELD_CITY%>" type="checkbox" <%=IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_CITY)?"checked=\"checked\"":""%> /></td>
		</tr>
		<tr>
			<td>Province</td>
			<td><input name="<%=IntakeRequiredFieldsDao.FIELD_PROVINCE%>" type="checkbox" <%=IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_PROVINCE)?"checked=\"checked\"":""%> /></td>
		</tr>
		<tr>
			<td>Postal Code</td>
			<td><input name="<%=IntakeRequiredFieldsDao.FIELD_POSTAL_CODE%>" type="checkbox" <%=IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_POSTAL_CODE)?"checked=\"checked\"":""%> /></td>
		</tr>
	</table>
	
	<br />
	<input type="button" value="back" onclick="document.location.href='<%=request.getContextPath()%>/PMmodule/ProviderInfo.do'" />&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="save changes" />
</form>

<%@include file="/layouts/caisi_html_bottom.jspf"%>
