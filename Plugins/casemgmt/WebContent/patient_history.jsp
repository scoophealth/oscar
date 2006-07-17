<%@ include file="/taglibs.jsp" %>
<%@ taglib uri="/WEB-INF/tld/struts-nested.tld" prefix="nested" %>
<%@ page import="org.caisi.casemgmt.model.*" %>
<%@ page import="org.caisi.casemgmt.web.formbeans.*" %>

<table width="100%" border="0"  cellpadding="0" cellspacing="1" bgcolor="#C0C0C0">

<tr class="title">
	<td>Social History</td>
	<td>familyHistory</td>
</tr>
<tr>
	<td bgcolor="white"><html:textarea property="cpp.socialHistory" rows="2" cols="40"/></td>
	<td bgcolor="white"><html:textarea property="cpp.familyHistory" rows="2" cols="40"/></td>
</tr>
<tr class="title">
	<td colspan="2">Medical History</td>
</tr>
<tr>
	<td colspan="2" bgcolor="white"><html:textarea property="cpp.medicalHistory" rows="4" cols="85"/></td>
</tr>
</table>
<html:submit value="save" onclick="this.form.method.value='patientCPPSave'"/>
<logic:messagesPresent message="true">
	   <html:messages id="message" message="true">
	   		<div style="color:blue"><I><c:out value="${message}"/></I></div>
	    </html:messages>
</logic:messagesPresent>