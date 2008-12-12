<%@ include file="/ticklerPlus/header.jsp"%>

<tr>
	<td class="searchTitle" colspan="4">Prepared Ticklers</td>
</tr>
</table>
<%@ include file="messages.jsp"%>

<br />
<table width="30%" border="0" cellpadding="0" cellspacing="1"
	bgcolor="#C0C0C0">
	<html:form action="/Tickler">
		<input type="hidden" name="method" value="prepared_tickler_list" />
		<tr class="title">
			<th width="20%"></th>
			<th width="80%">Name</th>
		</tr>
		<%int index=0; 
	  String bgcolor;
	%>
		<logic:iterate id="preparedTickler"
			collection="<%=request.getAttribute("preparedTicklers")%>">
			<%
		
		if(index++%2!=0) {
			bgcolor="white";
		} else {
			bgcolor="#EEEEFF";
		}
	%>
			<tr bgcolor="<%=bgcolor %>" align="center"
				onMouseOver="this.style.cursor='hand';this.style.backgroundColor='pink';"
				onMouseout="this.style.backgroundColor='<%=bgcolor %>';"
				onclick="location.href='../Tickler.do?method=prepared_tickler_edit&id=<bean:write name="preparedTickler" property="name"/>';">
				<td valign="middle"><input type="checkbox" name="checkbox"
					value="<bean:write name="preparedTickler" property="name"/>" /></td>
				<td><bean:write name="preparedTickler" property="name" /></td>
			</tr>
		</logic:iterate>
	</html:form>
</table>

<table>
	<!-- 
		<tr>
			<td colspan="2"><a href="#" onclick="CheckAll(document.ticklerForm);">Check All</a>&nbsp;<a href="#" onclick="ClearAll(document.ticklerForm);">Clear All</a></td>
		</tr>
	-->
	<tr>
		<td><input type="button" value="New"
			onclick="location.href='Tickler.do?method=prepared_tickler_edit'" /></td>
		<td><input type="button" value="Delete"
			onclick="this.form.method.value='prepared_tickler_delete';this.form.submit();" /></td>
	</tr>
</table>

<br />
<table width="100%">
	<tr>
		<td><html:link action="/Tickler">Back to Ticklers</html:link></td>
	</tr>
</table>
</body>
</html>
