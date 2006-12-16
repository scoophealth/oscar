<%@ include file="/survey/taglibs.jsp" %>
<html:html>
	<head>
		<title>Survey Manager</title>
	</head>
	
	<body>
		<html:form action="/SurveyManager">
			<input type="hidden" name="method" value="save"/>
			<html:hidden property="survey.surveyId"/>
			<h3>Please enter details</h3>
			<br/>
			<table>
				<tr>
					<td>Description:</td>
					<td><html:text property="survey.description"/></td>
				</tr>
			</table>
			<html:submit value="save"/>
			<html:cancel/>
		</html:form>
	</body>
</html:html>