<!-- 
/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/
 -->

<%@ include file="/taglibs.jsp"%>
<script>
	function popupHelp(topic) {
		url = '<html:rewrite page="/common/help.jsp?topic="/>';
		window.open(url + topic,'help','width=450, height=200');
	}
</script>
Welcome <b><c:out value="${requestScope.provider.formattedName}" /></b>
<br />
<br />
<br />
Your Agency Domain <a href="javascript:void(0)" onclick="popupHelp('agency_domain')">?</a> includes:
<display:table class="simple" cellspacing="2" cellpadding="3" id="agency" name="agencyDomain" export="false" requestURI="/PMmodule/ProviderInfo.do">
	<display:setProperty name="basic.msg.empty_list" value="No agencies." />
	<display:column property="name" sortable="true" title="Name" />
</display:table>
<br />
<br />
Your Program Domain <a href="javascript:void(0)" onclick="popupHelp('program_domain')">?</a> includes:
<display:table class="simple" cellspacing="2" cellpadding="3" id="program" name="programDomain" export="false" requestURI="/PMmodule/ProviderInfo.do">
	<display:setProperty name="basic.msg.empty_list" value="No programs." />
	<display:column sortable="true" sortProperty="program.name" title="Program Name">
		<a href="<html:rewrite action="/PMmodule/ProgramManagerView"/>?id=<c:out value="${program.programId}"/>"><c:out value="${program.program.name}" /></a>
	</display:column>
	<display:column property="role.name" sortable="true" title="Role" />
	<display:column property="program.type" sortable="true" title="Program Type" />
	<display:column property="program.queueSize" sortable="true" title="Clients in Queue" />
</display:table>