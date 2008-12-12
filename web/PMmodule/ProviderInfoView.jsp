<%@ include file="/taglibs.jsp"%>
<script>
    function popupHelp(topic) {
        url = '<html:rewrite page="/common/help.jsp?topic="/>';
        window.open(url + topic,'help','width=450, height=200');
    }
</script>
<c:import url="/SystemMessage.do?method=view" />
<c:import url="/FacilityMessage.do?method=view" />
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
			value="${program.program.name}" /></a>
	</display:column>
	<display:column property="role.name" sortable="true" title="Role" />
	<display:column property="program.type" sortable="true"
		title="Program Type" />
	<display:column property="program.queueSize" sortable="true"
		title="Clients in Queue" />
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