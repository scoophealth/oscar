
<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>



<%@ include file="/taglibs.jsp"%>
<%@page import="org.oscarehr.PMmodule.web.formbean.*"%>
<style>
.sortable {
	background-color: #555;
	color: #555;
}

.b th {
	border-right: 1px solid #333;
	background-color: #ddd;
	color: #ddd;
	border-left: 1px solid #fff;
}

.message {
	color: red;
	background-color: white;
}

.error {
	color: red;
	background-color: white;
}
</style>

<script>
			function cancel() {
				location.href='<html:rewrite action="/PMmodule/GenericIntake/Search.do"/>';
			}
			
			function submit() {
				var reason = document.clientManagerForm.elements['erconsent.consentReason'].value;
				if(reason == '') {
					alert('Please provide a reason client is visiting your agency');
					return false;
				}
				document.clientManagerForm.method.value='submit_erconsent';
				document.clientManagerForm.submit();
			}
			
		</script>

<br />
<br />
<br />
<html:form action="/PMmodule/ClientManager.do">
	<input type="hidden" name="id"
		value="<c:out value="${requestScope.id}"/>" />
	<input type="hidden" name="method" value="submit_erconsent" />
	<input type="hidden" name="erconsent.consentType"
		value="<%=request.getParameter("consent") %>">

	<table width="100%" cellpadding="3" cellspacing="4">
		<tr>
			<td>Please enter reason client is visiting your agency
			(presenting problem):</td>
		</tr>
		<tr>

		</tr>
		<td><html:textarea property="erconsent.consentReason" rows="8"
			cols="80"></html:textarea></td>
		<tr>
			<td><input type="button" value="submit" onclick="submit()" /> <input
				type="button" value="Cancel" onclick="cancel()" /></td>
	</table>
</html:form>
