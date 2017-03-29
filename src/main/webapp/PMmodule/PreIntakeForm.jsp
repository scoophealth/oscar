
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
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_demographic" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_demographic");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>



<%@ include file="/taglibs.jsp"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.PMmodule.model.Agency"%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>

<script>
	function new_client(demographicId) {
		var f = document.preIntakeForm;
		f.elements['form.demographicId'].value = demographicId;
		f.action.value='new_client';
		f.submit();
	}

	function update_client(demographicId) {
		var f = document.preIntakeForm;
		f.elements['form.demographicId'].value = demographicId;
		f.action.value='update_client';
		f.submit();
	}
</script>
<h4>New Client</h4>
<p>Please enter the following information. The system will try to
determine if the client has already been entered into the system.</p>
<br />
<script>
	function validateSearchForm(form) {
		if(form.elements['form.firstName'].value == '' &&
			form.elements['form.lastName'].value == '' &&
			form.elements['form.monthOfBirth'].value == '' &&
			form.elements['form.dayOfBirth'].value == '' &&
			form.elements['form.yearOfBirth'].value == '' &&
			form.elements['form.healthCardNumber'].value == '' &&
			form.elements['form.healthCardVersion'].value == '') {
			
			alert('You must use atleast 1 of the search fields');
			
			return false;
		}
		
		return true;
	}
</script>
<html:form action="/PMmodule/Intake"
	onsubmit="return validateSearchForm(this)">
	<input type="hidden" name="action" value="do_intake" />
	<html:hidden property="form.demographicId" />
	<table width="50%">
		<tr>
			<td>First Name</td>
			<td><html:text property="form.firstName" /></td>
		</tr>
		<tr>
			<td>Last Name</td>
			<td><html:text property="form.lastName" /></td>
		</tr>
		<tr>
			<td>Date Of Birth</td>
			<td><html:select property="form.monthOfBirth">
				<html:option value="">&nbsp;</html:option>
				<html:option value="01">January</html:option>
				<html:option value="02">February</html:option>
				<html:option value="03">March</html:option>
				<html:option value="04">April</html:option>
				<html:option value="05">May</html:option>
				<html:option value="06">June</html:option>
				<html:option value="07">July</html:option>
				<html:option value="08">August</html:option>
				<html:option value="09">September</html:option>
				<html:option value="10">October</html:option>
				<html:option value="11">November</html:option>
				<html:option value="12">December</html:option>
			</html:select> &nbsp; <html:select property="form.dayOfBirth">
				<html:option value="">&nbsp;</html:option>
				<%
						for (int x = 1; x <= 31; x++) {
						String value = String.valueOf(x);
						if (value.length() == 1) {
							value = "0" + value;
						}
				%>
				<html:option value="<%=value %>" />
				<%
				}
				%>
			</html:select>&nbsp;<html:text property="form.yearOfBirth" size="4" /></td>
		</tr>
		<caisi:isModuleLoad moduleName="GET_OHIP_INFO" reverse="false">
			<tr>
				<td>Health Card</td>
				<td><html:text property="form.healthCardNumber" size="10" />
				&nbsp; <html:text property="form.healthCardVersion" size="2" /></td>
			</tr>
		</caisi:isModuleLoad>
		<tr>
			<td colspan="2"><br />
			<html:submit /></td>
		</tr>
	</table>
	<br />
	<br />
	<c:if test="${requestScope.clients != null}">
		<p>The following possible matches were found in the system. If
		your client is not one of these matches, choose the 'New Client'
		button, otherwise, you can update the existing client file.</p>
		<b>Search Type:&nbsp; Agency</b>
		<p><input type="button" value="New Client"
			onclick="this.form.action.value='new_client';this.form.submit();" />
		<display:table class="simple" cellspacing="2" cellpadding="3"
			id="client" name="clients" export="false" pagesize="10"
			requestURI="/PMmodule/Intake.do">
			<display:setProperty name="paging.banner.placement" value="bottom" />
			<display:setProperty name="basic.msg.empty_list"
				value="No clients found." />
			<display:column sortable="false" title="">
				<span title="Update client intake form"> <a href="#"
					onclick="update_client('<c:out value="${client.demographicNo}"/>');return false;">
				<img border="0" src="images/refresh.gif" /> </a> </span>
			</display:column>
			<display:column property="formattedName" sortable="true" title="Name" />
			<display:column sortable="true" title="Date of Birth">
				<c:out value="${client.yearOfBirth}" />/<c:out
					value="${client.monthOfBirth}" />/<c:out
					value="${client.dateOfBirth}" />
			</display:column>
			<display:column title="EMPI Links">
				<span style="text-decoration: underline"
					title="<c:out value="${client.formattedLinks}"/>"><c:out
					value="${client.numLinks}" /></span>
			</display:column>
		</display:table> <br />
	</c:if>
</html:form>
