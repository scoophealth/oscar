<%@ include file="/taglibs.jsp"%>
<h3 style="font-color: red"><c:out value="${requestScope.errormsg}" /></h3>
<html:form action="/UnreadTickler.do">
	<input type="hidden" name="method" value="login" />
	<table>
		<tr>
			<td>Username:</td>
			<td><html:text property="username" /></td>
		</tr>
		<tr>
			<td>Password:</td>
			<td><html:text property="password" /></td>
		</tr>
		<tr>
			<td>PIN:</td>
			<td><html:text property="pin" /></td>
		</tr>
		<tr>
			<td colspan="2" align="left"><html:submit /></td>
		</tr>
	</table>
</html:form>