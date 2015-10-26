
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
<security:oscarSec roleName="<%=roleName$%>" objectName="_pmm" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_pmm");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<script>
	function search_programs() {
		var form = document.clientManagerForm;
		
		form.method.value='admit_select_program';
		var programName = form.elements['program.name'].value;
		var url = '<html:rewrite action="/PMmodule/ClientManager.do"/>';
			url += '?method=search_programs&program.name=' + programName;
			url += '&formName=clientManagerForm&formElementName=program.name&formElementId=program.id&submit=true';
			
		window.open(url, 'program_search', 'width=500, height=400');
	}

	function do_admission() {
		var form = document.clientManagerForm;
		form.method.value='admit';
		form.submit();
	}	
</script>
<div class="tabs" id="tabs">
<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<th title="Programs">Current Admissions</th>
	</tr>
</table>
</div>
<html:hidden property="program.id" />
<display:table class="simple" cellspacing="2" cellpadding="3"
	id="admission" name="admissions" export="false" pagesize="0"
	requestURI="/PMmodule/ClientManager.do">
	<display:setProperty name="paging.banner.placement" value="bottom" />
	<display:setProperty name="basic.msg.empty_list"
		value="This client is not currently admitted to any programs." />
	<display:column property="programName" sortable="true"
		title="Program Name" />
	<display:column property="admissionDate" sortable="true"
		title="Admission Date" />
	<display:column property="admissionNotes" sortable="true"
		title="Admission Notes" />
</display:table>
<br />
<br />
<div class="tabs" id="tabs">
<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<th title="Programs">Search Programs</th>
	</tr>
</table>
</div>
<table width="100%" border="1" cellspacing="2" cellpadding="3">
	<tr class="b">
		<td width="20%">Agency</td>
		<td><span style="color: red"><i>Not yet implemented</i></span></td>
	</tr>
	<tr class="b">
		<td width="20%">Program Name</td>
		<td><html:text property="program.name" /></td>
	</tr>
</table>
<table>
	<tr>
		<td align="center"><input type="button" value="search"
			onclick="search_programs()" /></td>
		<td align="center"><input type="button" name="reset"
			value="reset" onclick="javascript:resetClientFields();" /></td>
	</tr>
</table>
<br />
<br />
<c:if test="${requestScope.do_admit != null}">
	<table width="100%" border="1" cellspacing="2" cellpadding="3">
		<c:if test="${requestScope.current_admission != null}">
			<tr>
				<td colspan="2"><b style="color: red">Warning:<br />
				This client is currently admitted to a bed program (<c:out
					value="${current_program.name}" />).<br />
				By completing this admission, you will be discharging them from this
				current program.</b></td>
			</tr>
			<tr class="b">
				<td width="20%">Discharge Notes:</td>
				<td><html:textarea cols="50" rows="7"
					property="admission.dischargeNotes" /></td>
			</tr>
		</c:if>
		<tr class="b">
			<td width="20%">Admission Notes:</td>
			<td><html:textarea cols="50" rows="7"
				property="admission.admissionNotes" /></td>
		</tr>
		<tr class="b">
			<td colspan="2"><input type="button" value="Process Admission"
				onclick="do_admission()" /> <input type="button" value="Cancel" /></td>
		</tr>
	</table>
</c:if>
