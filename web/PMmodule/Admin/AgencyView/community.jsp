<%@ include file="/taglibs.jsp"%>

<c:choose>
	<c:when test="${requestScope.integrator_registered == false}">
		<span style="color:red">&lt;This feature is only available to CAISI systems registered with an integrator&gt;</span>
	</c:when>
	<c:otherwise>
		<br />
		<h4>Integrator Version: <c:out value="${integrator_version}" /></h4>
		<br />
		<div class="tabs" id="tabs">
		<table cellpadding="3" cellspacing="0" border="0">
			<tr>
				<th title="Programs">Agencies</th>
			</tr>
		</table>
		</div>
		<display:table class="simple" cellspacing="2" cellpadding="3" id="agency" name="agencies" export="false" pagesize="0" requestURI="/PMmodule/AgencyManager.do">
			<display:setProperty name="paging.banner.placement" value="bottom" />
			<display:setProperty name="paging.banner.item_name" value="agency" />
			<display:setProperty name="paging.banner.items_name" value="agencies" />
			<display:setProperty name="basic.msg.empty_list" value="No agencies found." />
			<display:column property="id" sortable="true" title="Id" />
			<display:column property="name" sortable="true" title="Name" />
			<display:column property="descr" sortable="true" title="Description" />
			<display:column sortable="true" title="Contact Info">
				<c:out value="${agency.contactName}" />
				<br />
				<c:out value="${agency.contactEmail}" />
				<br />
				<c:out value="${agency.contactPhone}" />
			</display:column>
		</display:table>
		<br />
		<input type="button" value="Refresh Client Data To Integrator" onclick="location.href='<html:rewrite action="/PMmodule/AgencyManager"/>?method=refresh_clients'" />
		<input type="button" value="Refresh Program Data To Integrator" onclick="location.href='<html:rewrite action="/PMmodule/AgencyManager"/>?method=refresh_programs'" />
		<input type="button" value="Refresh Program Participation Data To Integrator" onclick="location.href='<html:rewrite action="/PMmodule/AgencyManager"/>?method=refresh_admissions'" />
		<input type="button" value="Refresh Provider Data To Integrator" onclick="location.href='<html:rewrite action="/PMmodule/AgencyManager"/>?method=refresh_providers'" />
		<input type="button" value="Refresh Referral Data To Integrator" onclick="location.href='<html:rewrite action="/PMmodule/AgencyManager"/>?method=refresh_referrals'" />
	</c:otherwise>
</c:choose>