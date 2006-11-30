<%@ include file="/taglibs.jsp" %>
<html>
<head>
	<title>Welcome to Caisi Core</title>
</head>
<body>
<h2>CAISI Core Management Interface</h2>
<br/>
<html:link action="/SystemMessage">System Messages</html:link>
<br/>

<html:link action="/issueAdmin.do?method=list">Issue Editor</html:link>
<br/>

<html:link action="/CaisiRole">CAISI Roles</html:link>

<caisi:isModuleLoad moduleName="survey">
<br/>
<html:link target="_blank" action="/mod/caisi/SurveyManager.do">Oscar Forms Manager</html:link>
</caisi:isModuleLoad>

</body>
</html>
