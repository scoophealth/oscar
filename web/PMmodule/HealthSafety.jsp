<%@ include file="/taglibs.jsp"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@page import="org.oscarehr.common.model.Demographic"%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Health and Safety</title>
<script>
function submitForm(form) {
 var message = form.elements['healthsafety.message'].value;
 if(message!=null && message.length==0){
   alert("message can not be empty.");
   return false;
 }  
 if(message!=null && message.length>150){
   alert("message can not exceed 150 characters.");
   return false;
 }  
			
  form.submit();
  opener.document.clientManagerForm.submit();
}
</script>
</head>

<body topmargin="20" leftmargin="10">

<html:form action="/PMmodule/HealthSafety.do">
	<input type="hidden" name="method" value="savehealthSafety" />
	<input type="hidden" name="id"
		value="<c:out value="${requestScope.id}"/>">
	<html:hidden property="healthsafety.userName" />
	<html:hidden property="healthsafety.demographicNo" />
	<table border="2" width="700" cellspacing="0" cellpadding="0">
		<tr>
			<td colspan="3">Message:<br>
			<html:textarea property="healthsafety.message" rows="5"
				style="width:100%;"></html:textarea></td>
		</tr>
		<tr>
			<td width="40%">User Name: <c:out
				value="${healthsafety.userName}" /></td>
			<td width="40%">Date: <fmt:formatDate
				value="${healthsafety.updateDate}" pattern="yyyy-MM-dd" /></td>
			<td width="20%"><input type="button" value="Save"
				onclick="submitForm(document.healthSafetyForm)" /> <input
				type="button" value="Cancel" onclick="window.close()" /></td>
		</tr>
	</table>
</html:form>
</body>
</html>
