<%@ include file="/taglibs.jsp" %>
<%@ taglib uri="http://www.caisi.ca/plugin-tag" prefix="plugin" %>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>
<html>
<head>
	<title>Welcome to Caisi Core</title>
</head>
<body>
<h2>CAISI Core Management Interface</h2>
<br/>
<html:link action="/SystemMessage">System Messages</html:link>
<br/>
<html:link action="/CaisiRole">CAISI Roles</html:link>
<caisi:isModuleLoad moduleName="survey">
	<br/>
	<html:link target="_blank" action="/mod/surveyComp/SurveyManager.do">Oscar Forms Manager</html:link>
</caisi:isModuleLoad>
</body>
</html>