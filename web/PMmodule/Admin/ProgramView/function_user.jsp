<%@ include file="/taglibs.jsp"%>
<div class="tabs" id="tabs">
<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<th title="Programs">Function User</th>
	</tr>
</table>
</div>
<display:table class="simple" cellspacing="2" cellpadding="3" id="functional" name="functional_users" export="false" pagesize="0" requestURI="/PMmodule/ProgramManagerView.do">
	<display:setProperty name="paging.banner.placement" value="bottom" />
	<display:setProperty name="basic.msg.empty_list" value="No functional users defined for this program" />
	<display:column property="userType.name" sortable="true" title="User Type" />
	<display:column property="provider.formattedName" sortable="true" title="Provider Name" />
</display:table>
