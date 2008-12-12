<%@ include file="/taglibs.jsp"%>
<br />
<%-- Error Messages --%>
<logic:messagesPresent>
	<table width="100%" border="0" cellpadding="0" cellspacing="1"
		bgcolor="#C0C0C0">
		<html:messages id="error" bundle="pmm">
			<tr>
				<td class="error"><c:out value="${error}" /></td>
			</tr>
		</html:messages>
	</table>
</logic:messagesPresent>
<%-- Success Messages --%>
<logic:messagesPresent message="true">
	<table width="100%" border="0" cellpadding="0" cellspacing="1"
		bgcolor="#C0C0C0">
		<html:messages id="message" message="true" bundle="pmm">
			<tr>
				<td class="message"><c:out value="${message}" /></td>
			</tr>
		</html:messages>
	</table>
</logic:messagesPresent>
<br />