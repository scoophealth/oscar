<%@ include file="/taglibs.jsp" %>
<%@ page import="org.caisi.casemgmt.model.*" %>
<%@ page import="org.caisi.casemgmt.web.formbeans.*" %>

<table width="100%" border="0"  cellpadding="0" cellspacing="1" bgcolor="#C0C0C0">
<tr class="title">
	<td>Reminders</td>
</tr>
<tr>
	<td bgcolor="white"><html:textarea property="cpp.reminders" rows="4" cols="85"/></td>

</tr>
</table>
<html:submit value="save" onclick="this.form.method.value='patientCPPSave'"/>
<logic:messagesPresent message="true">
	   <html:messages id="message" message="true">
	   		<div style="color:blue"><I><c:out value="${message}"/></I></div>
	    </html:messages>
</logic:messagesPresent>