<!-- Source:web/PMmodule/Admin/User/UserProfile.jsp -->

<%@ include file="/taglibs.jsp"%>


<table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
	<tr>
		<th class="pageTitle" align="center"><span
			id="_ctl0_phBody_lblTitle" align="left">User Management</span></th>
	</tr>
	<tr>
		<td align="left" class="buttonBar2"><html:link
			action="/PMmodule/Admin/UserManager.do"
			style="color:Navy;text-decoration:none;">
			<img border=0 src=<html:rewrite page="/images/Back16.png"/> />&nbsp;Back to User List&nbsp;&nbsp;|</html:link>
		
		</td>

	</tr>
	<tr>
		<td align="left"></td>
	</tr>
	<tr>
		<td height="100%">
		<div
			style="color: Black; background-color: White; border-width: 1px; border-style: Ridge;
                    height: 100%; width: 100%; overflow: auto;" id="scrollBar">

		<table width="100%" cellpadding="0" cellspacing="0" border="10">
			<tr>
				<td align="left" class="clsHomePageHeader">
				<h2>User Profile</h2>
				</td>
			</tr>

		</table>

		<display:table class="simple" cellspacing="2" cellpadding="3"
			id="user" name="profilelist" export="false" pagesize="0"
			requestURI="/PMmodule/Admin/UserManager.do">

			<display:column property="userName" sortable="true"
				title="User ID" group="1" />

			<display:column property="orgcd_desc" sortable="true"
				title="ORG" group="2" />

			<display:column property="roleName" sortable="true"
				title="Role" />
				

		</display:table></div>
		</td>
	</tr>
</table>

