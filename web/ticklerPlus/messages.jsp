<%-- Error Messages --%>

<table width="100%" border="0" cellpadding="0" cellspacing="1"
	bgcolor="#C0C0C0">

	<logic:messagesPresent>
		<html:messages id="error">
			<tr>
				<td class="error"><c:out value="${error}" /></td>
			</tr>
		</html:messages>
	</logic:messagesPresent>

	<%-- Success Messages --%>
	<logic:messagesPresent message="true">
		<html:messages id="message" message="true">
			<tr>
				<td class="message"><c:out value="${message}" /></td>
			</tr>
		</html:messages>
	</logic:messagesPresent>

</table>