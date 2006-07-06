<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>

<logic:present name="messages">
	<table width="100%">
		<logic:iterate collection="<%=(java.util.List)request.getAttribute("messages")%>" id="message">
			<logic:equal name="message" property="active" value="true">
				<tr>
					<td><font color="red" size="+1"><bean:write name="message" property="message"/>  </font></td>
				</tr>	
			</logic:equal>
		</logic:iterate>
	</table>
</logic:present>