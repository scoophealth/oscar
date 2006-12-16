<%@ include file="/casemgmt/taglibs.jsp" %>

<%@ page import="org.oscarehr.casemgmt.model.*" %>
<%@ page import="org.oscarehr.casemgmt.web.formbeans.*" %>

<table width="100%" border="0"  cellpadding="0" cellspacing="1" bgcolor="#C0C0C0">
<tr class="title">
	<td>Ongoing Concerns</td>
</tr>
<tr>
	<td bgcolor="white"><html:textarea property="cpp.ongoingConcerns" rows="4" cols="85"/></td> 
</tr>
</table>
<html:submit value="save" onclick="this.form.method.value='patientCPPSave'"/>
<logic:messagesPresent message="true">
	   <html:messages id="message" message="true" bundle="casemgmt">
	   		<div style="color:blue"><I><c:out value="${message}"/></I></div>
	    </html:messages>
</logic:messagesPresent>