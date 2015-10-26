
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
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.pmm" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin.pmm");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<script type="text/javascript">
function removeFromQueue(id) {
	document.programManagerForm.elements['queue.id'].value = id;
	document.programManagerForm.method.value='remove_queue';
	document.programManagerForm.submit();
}

function removeFromRemoteQueue(remoteReferralId) {
	document.programManagerForm.elements['remoteReferralId'].value = remoteReferralId;
	document.programManagerForm.method.value='remove_remote_queue';
	document.programManagerForm.submit();
}
</script>
<html:hidden property="queue.id" />
<div class="tabs" id="tabs">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th title="Programs">Local Queue</th>
		</tr>
	</table>
</div>
<!--  show current clients -->
<display:table class="simple" cellspacing="2" cellpadding="3" id="queue_entry" name="queue" export="false" pagesize="0" requestURI="/PMmodule/ProgramManager.do">
	<display:setProperty name="paging.banner.placement" value="bottom" />
	<display:setProperty name="basic.msg.empty_list" value="Queue is empty." />
	<display:column sortable="false" title="">
		<a href="javascript:void(0);" onclick="removeFromQueue('<c:out value="${queue_entry.id}"/>');return false;"> Remove </a>
	</display:column>
	<display:column property="clientFormattedName" sortable="true" title="Client Name" />
	<display:column property="referralDate" sortable="true" title="Referral Date" />
	<display:column property="providerFormattedName" sortable="true" title="Referring Provider" />
  	<caisi:isModuleLoad moduleName="pmm.refer.temporaryAdmission.enabled">
		<display:column property="temporaryAdmission" sortable="true" title="Temporary Admission" />
	</caisi:isModuleLoad>
	<display:column property="notes" sortable="true" title="Notes" />
</display:table>

<c:if test="${remoteQueue!=null}">
	<br /><br />

	<input type="hidden" name="remoteReferralId" />

	<div class="tabs" id="tabs">
		<table cellpadding="3" cellspacing="0" border="0">
			<tr>
				<th title="Programs">Remote Queue</th>
			</tr>
		</table>
	</div>
	<!--  show current clients -->
	<display:table class="simple" cellspacing="2" cellpadding="3" id="queue_entry" name="remoteQueue" export="false" pagesize="0" requestURI="/PMmodule/ProgramManager.do">
		<display:setProperty name="paging.banner.placement" value="bottom" />
		<display:setProperty name="basic.msg.empty_list" value="Queue is empty." />
		<display:column sortable="false" title="">
			<a href="javascript:void(0);" onclick="removeFromRemoteQueue('<c:out value="${queue_entry.remoteReferral.remoteReferralId}"/>');return false;"> Remove </a>
		</display:column>
		<display:column property="clientName" sortable="true" title="Client Name" />
		<display:column property="remoteReferral.referralDate" sortable="true" title="Referral Date" />
		<display:column property="providerName" sortable="true" title="Referring Provider" />
		<display:column property="vacancyName" sortable="true"	title="Vacancy Name" />
		<display:column property="remoteReferral.reasonForReferral" sortable="true" title="Notes" />
	</display:table>
</c:if>
