<%@ include file="/taglibs.jsp"%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>On Call Questionnaire</title>
</head>
<body>

<h3>On Call Questionnaire</h3>
<br />
<br />
<table border="0" width="100%">
	<html:form action="/OnCallQuestionnaire">
		<input type="hidden" name="method" value="save" />
		<input type="hidden" name="providerNo"
			value="<%=request.getParameter("providerNo") %>" />
		<input type="hidden" name="type"
			value="<%=request.getParameter("type") %>" />
		<tr>
			<td>Type of Health problem:</td>
			<td><html:select property="type_health">
				<html:option value=""></html:option>
				<html:option value="Primary Care">Primary Care</html:option>
				<html:option value="Mental Health">Mental Health</html:option>
			</html:select></td>
		</tr>
		<tr>
			<td>Nurse Involved:</td>
			<td><html:select property="nurse">
				<html:option value=""></html:option>
				<html:option value="yes">Yes</html:option>
				<html:option value="no">No</html:option>
			</html:select></td>
		</tr>
		<tr>

			<td>Course of action:</td>
			<td><html:select property="course_action">
				<html:option value=""></html:option>
				<html:option value="sh">SH visit (immediate, 24 hours, same week)</html:option>
				<html:option value="er">ER</html:option>
				<html:option value="medication">Medication</html:option>
				<html:option value="investigations">investigations (24 hours, same weeks)</html:option>
				<html:option value="watch">watch and wait</html:option>
			</html:select></td>
		</tr>
		<tr>
			<td>Required physician consultation:</td>
			<td><html:select property="physician_consult">
				<html:option value=""></html:option>
				<html:option value="yes">Yes</html:option>
				<html:option value="no">No</html:option>
			</html:select></td>
		</tr>
		<tr>
			<td>Date:</td>
			<td><html:text property="date" /></td>
		</tr>
		<tr>
			<td>Time:</td>
			<td><html:text property="time" /></td>
		</tr>
		<tr>
			<td><html:submit /></td>
		</tr>
	</html:form>
</table>
</body>
</html>