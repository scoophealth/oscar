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
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<script>
    function popupHelp(topic) {
        url = '<html:rewrite page="/common/help.jsp?topic="/>';
        window.open(url + topic,'help','width=450, height=200');
    }
</script>
<% String roleName$ = (String) session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>

<br />
<!-- remove all info about agency because table agency has been truncated -->
<!--  
Your Agency Domain <a href="javascript:void(0)" onclick="popupHelp('agency_domain')">?</a> includes:
<display:table class="simple" cellspacing="2" cellpadding="3" id="agency" name="agencyDomain" export="false" requestURI="/PMmodule/ProviderInfo.do">
    <display:setProperty name="basic.msg.empty_list" value="No agencies." />
</display:table>
<br />
-->
<br />
Your Program Domain
<a href="javascript:void(0)" onclick="popupHelp('program_domain')">?</a>
includes:
<display:table class="simple" cellspacing="2" cellpadding="3"
	id="program" name="programDomain" export="false"
	requestURI="/PMmodule/ProviderInfo.do">
	<display:setProperty name="basic.msg.empty_list" value="No programs." />
	<display:column sortable="true" sortProperty="program.name"
		title="Program Name">
		<a
			href="<html:rewrite action="/PMmodule/ProgramManagerView"/>?id=<c:out value="${program.programId}"/>"><c:out
			value="${program.program.name}" />
		</a>
	</display:column>
	
	<security:oscarSec roleName="<%=roleName$%>" objectName="_pmm_editProgram.vacancies" rights="r">
		<display:column title="">
		<a href="<html:rewrite action="/PMmodule/ProgramManager.do"/>?method=edit&view.tab=vacancy_add&newVacancy=true&id=<c:out value="${program.programId}" />">
			New Vacancy </a>
		</display:column>
	</security:oscarSec>
	
	<display:column property="role.name" sortable="true" title="Role" />
	<display:column property="program.type" sortable="true"
		title="Program Type" />
	<display:column sortable="true" title="Clients in Queue" >
                        <a
                        href="<html:rewrite action="/PMmodule/ProgramManagerView.do"/>?method=view&tab=queue&id=<c:out value="${program.programId}"/>">
                        <c:out value="${program.program.queueSize}"/></a>
        </display:column>

</display:table>
<br />
You belong to the following facilities:
<display:table class="simple" cellspacing="2" cellpadding="3"
	id="facility" name="facilityDomain" export="false"
	requestURI="/PMmodule/ProviderInfo.do">
	<display:setProperty name="basic.msg.empty_list" value="No facilities." />
	<display:column sortable="true" sortProperty="name"
		title="Facility Name">
		<a
			href="<html:rewrite action="/PMmodule/FacilityManager?method=view&"/>id=<c:out value="${facility.id}"/>"><c:out
			value="${facility.name}" /></a>
	</display:column>
	<display:column property="description" sortable="true"
		title="Facility Description" />
</display:table>
