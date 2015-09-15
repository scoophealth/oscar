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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_tickler" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_tickler");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ include file="/ticklerPlus/header.jsp"%>

<jsp:useBean id="formHandler"
	class="org.caisi.tickler.prepared.seaton.consultation.ProcessConsultationBean"
	scope="request">
	<jsp:setProperty name="formHandler" property="*" />
</jsp:useBean>

<script>

function search_demographic() {
	window.open('demographicSearch.jsp?form=myform&elementName=demographic_name&elementId=demographic_no&query=' + document.myform.elements['demographic_name'].value,'demographic_search');
}
function populate_consultation_requests() {
	var form = document.myform;
	if(form.demographic_no.value == '') {
		alert('You must choose a patient\nUse the search button.');
		return;
	}
	form.action.value = 'populate';
	form.submit();
}
function generate_tickler() {
	var form = document.myform;
	form.action.value = 'generate';
	
	var radio_choice = false;
	
	if(form.current_consultation.checked) {
		radio_choice = true;
	} else {
		for (counter = 0; counter < form.current_consultation.length; counter++) {
			if (form.current_consultation[counter].checked) {
				radio_choice = true; 
				break;
			}
		}
	}
	
	if (!radio_choice) {
		alert("Please select a consultation request.");
	} else {
		form.submit();
	}
}
</script>
<tr>
	<td class="searchTitle" colspan="4">Prepared Tickler - Process
	Consultation</td>
</tr>
</table>
<%@ include file="messages.jsp"%>
<br />

<table width="100%" border="0" cellpadding="0" cellspacing="1"
	bgcolor="#C0C0C0">
	<form name="myform" action="../Tickler.do"><input type="hidden"
		name="method" value="<c:out value="${formHandler.method}"/>" /> <input
		type="hidden" name="id" value="<c:out value="${formHandler.id}"/>" />
	<input type="hidden" name="action"
		value="<c:out value="${formHandler.action}"/>" />
	<tr>
		<td class="fieldTitle">Demographic:</td>
		<td class="fieldValue"><input type="hidden" name="demographic_no"
			value="<c:out value="${formHandler.demographic_no}"/>" /> <input
			type="text" name="demographic_name"
			value="<c:out value="${formHandler.demographic_name}"/>" /> <input
			type="button" value="Search" onclick="search_demographic();" /></td>
	</tr>
	<tr>
		<td class="fieldTitle">Consultation Requests</td>
		<td class="fieldValue"><logic:present name="consultations">
			<!-- Show Consultations here -->
			<%
						int consultationSize = ((java.util.List)request.getAttribute("consultations")).size();
						if(consultationSize > 0) {
					%>
			<table border="1" cellspacing="2" cellpadding="1">
				<tr>

					<td align="left" class="fieldTitle">Referral Date</td>
					<td align="left" class="fieldTitle">Name</td>
					<td align="left" class="fieldTitle">Reason</td>
				</tr>
				<c:forEach var="current" items="${consultations}">
					<tr>
						<td><input type="radio" name="current_consultation"
							value="<c:out value="${current.requestId}"/>" /> <fmt:formatDate
							dateStyle="short" value="${current.referalDate}" /></td>
						<td><c:out
							value="${current.professionalSpecialist.firstName}" />&nbsp;<c:out
							value="${current.professionalSpecialist.lastName}" /></td>
						<td><c:out value="${current.reason}" /></td>
					</tr>
				</c:forEach>
				<tr>
					<td colspan="2"><input type="button" value="Generate Tickler"
						onclick="javascript:generate_tickler()" /></td>
				</tr>
			</table>
			<%} else { %>
						No consultation requests were found for this Patient
					<%} %>
		</logic:present> <logic:notPresent name="consultations">
			<input type="button" value="Populate"
				onclick="populate_consultation_requests()" />
		</logic:notPresent></td>
	</tr>
	<tr>
		<td class="fieldValue" colspan="3" align="left"><input
			type="button" class="button" value="Cancel"
			onclick="location.href='/<%=application.getServletContextName()%>/Tickler.do?method=prepared_tickler_list';" />
		</td>
	</tr>
	</form>
</table>

</body>
</html>
