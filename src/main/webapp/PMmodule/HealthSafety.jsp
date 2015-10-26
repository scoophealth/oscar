<%--

    Copyright (c) 2005, 2009 IBM Corporation and others.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    Contributors:
        <Quatro Group Software Systems inc.>  <OSCAR Team>

--%>
<%@ include file="/taglibs.jsp"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_pmm" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_pmm");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>


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
