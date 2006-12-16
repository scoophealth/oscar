<%@ include file="/taglibs.jsp"%>
<script>
function removeFromQueue(id) {
	document.programManagerForm.elements['queue.id'].value = id;
	document.programManagerForm.method.value='remove_queue';
	document.programManagerForm.submit();
}
</script>
<html:hidden property="queue.id" />
<div class="tabs" id="tabs">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th title="Programs">Queue</th>
		</tr>
	</table>
</div>
<!--  show current clients -->
<display:table class="simple" cellspacing="2" cellpadding="3" id="queue_entry" name="queue" export="false" pagesize="0" requestURI="/PMmodule/ProgramManager.do">
	<display:setProperty name="paging.banner.placement" value="bottom" />
	<display:setProperty name="basic.msg.empty_list" value="Queue is empty." />
	<display:column sortable="false" title="">
		<a href="javascript:void(0);" onclick="removeFromQueue('<c:out value="${queue_entry.id}"/>')"> Remove </a>
	</display:column>
	<display:column property="clientFormattedName" sortable="true" title="Client Name" />
	<display:column property="referralDate" sortable="true" title="Referral Date" />
	<display:column property="providerFormattedName" sortable="true" title="Referring Provider" />
	<display:column property="temporaryAdmission" sortable="true" title="Temporary Admission" />
	<display:column property="notes" sortable="true" title="Notes" />
</display:table>