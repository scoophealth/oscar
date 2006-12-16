<%@ include file="/casemgmt/taglibs.jsp" %>
<html>
<head>
	<title>Case Management</title>
	<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>	
	<link rel="stylesheet" href="<c:out value="${ctx}"/>/css/casemgmt.css" type="text/css">
</head>

<body>
<c:if test="${not empty requestScope.success and requestScope.success == false}">
	<h4 style="color:red">Error unlocking note.</h4>
</c:if>
<h2>Please enter password to unlock this note.</h2>
<h5 style="color:red">This note will only be unlocked for the duration of your session. To permanently unlock, click on Edit Note, and remove the password.</h5>
<nested:form action="/CaseManagementView">
	<input type="hidden" name="method" value="do_unlock"/>
	<html:hidden property="noteId"/>
	<table>
		<tr>
			<td class="fieldTitle">Password:</td>
			<td class="fieldValue"><html:password property="password"/></td>
		</tr>
		<tr>
			<td class="fieldValue" colspan="2">
				<input type="button" value="Unlock" onclick="this.form.method.value='do_unlock';this.form.submit();"> 
			</td>
		</tr>
	</table>
</nested:form>
</body>

</html>