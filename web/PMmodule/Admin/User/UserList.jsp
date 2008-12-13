<!-- Source:web/PMmodule/Admin/User/UserList.jsp -->
<%String a = "1"; %>
<%@ include file="/taglibs.jsp"%>
<%@page import="com.quatro.common.KeyConstants;"%>
<table width="100%" height="100%" cellpadding="0px" cellspacing="0px"
	border="1" bordercolor="red">
	<tr>
		<th class="pageTitle" align="center"><span align="left">User Management - User
		List</span></th>
	</tr>
	<tr height="23px">
		<td align="left" class="buttonBar2"><html:link
			action="/PMmodule/Admin/SysAdmin.do"
			style="color:Navy;text-decoration:none;">&nbsp;
			<img style="vertical-align: middle" border=0 src=<html:rewrite page="/images/close16t.png"/> />&nbsp;Close&nbsp;|</html:link>
			<security:oscarSec objectName="<%=KeyConstants.FUN_ADMIN_USER %>" rights="<%=KeyConstants.ACCESS_WRITE %>">
			<html:link action="/PMmodule/Admin/UserManager.do?method=preNew"
				style="color:Navy;text-decoration:none;">&nbsp;
				<img style="vertical-align: middle" border=0 src=<html:rewrite page="/images/New16.png"/> />&nbsp;New&nbsp;|</html:link>
			</security:oscarSec>	
		<html:link href="javascript:submitForm('search')"
			style="color:Navy;text-decoration:none;">&nbsp;
			<img style="vertical-align: middle" border=0 src=<html:rewrite page="/images/search16.gif"/> />&nbsp;Search&nbsp;|</html:link>
		<html:link href="javascript:resetForm()"
			style="color:Navy;text-decoration:none;">&nbsp;
			<img style="vertical-align: middle" border=0 src=<html:rewrite page="/images/searchreset.gif"/> />&nbsp;Reset&nbsp;|</html:link>
		</td>

	</tr>
	<tr>
		<td align="left"></td>
	</tr>
	<tr>
		<td>
		<html:form action="/PMmodule/Admin/UserSearch">
			<input type="hidden" name="method" value="search" />
			<div class="axial">
			<table border="0" cellspacing="1" cellpadding="1" width="100%">
				<tr>
					<th align="right" width="20%"><bean-el:message key="UserSearch.userName" bundle="pmm" /></th>
					<td align="left" width="80%"><html:text property="criteria.userName" size="20" maxlength="30"/></td>
				</tr>
				<tr>
					<th align="right" width="20%"><bean-el:message key="UserSearch.lastName" bundle="pmm" />
					</th>
					<td align="left" width="80%"><html:text property="criteria.lastName" size="20" maxlength="30"/></td>
				</tr>
				<tr>
					<th align="right" width="20%"><bean-el:message key="UserSearch.firstName" bundle="pmm" /></th>
					<td align="left" width="80%"><html:text property="criteria.firstName" size="20" maxlength="30"/></td>
				</tr>
				<tr>
					<th align="right" width="20%"><bean-el:message key="UserSearch.active" bundle="pmm" /></th>
					<td align="left" width="80%"><html:select property="criteria.active">
						<html:option value="">Any</html:option>
						<html:option value="1">Yes</html:option>
						<html:option value="0">No</html:option>
					</html:select></td>
				</tr>

				<tr>
					<th align="right" width="20%"><bean-el:message key="UserSearch.roleName" bundle="pmm" /></th>
					<td align="left" width="80%"><html-el:select property="criteria.roleName">
						<html-el:option value="">Any</html-el:option>
						<c:forEach var="role" items="${roles}">
							<html-el:option value="${role.roleName}">
								<c:out value="${role.description}" />
							</html-el:option>
						</c:forEach>
					</html-el:select></td>
				</tr>


			</table>

			</div>

		</html:form></td>
	</tr>
	<tr style="height: 100%">
		<td>
		<div
			style="color: Black; background-color: White; border-width: 1px; border-style: Ridge;
                    height: 100%; width: 100%; overflow: auto;" id="scrollBar">

		<br/><div class="tabs" id="tabs">
		<table cellpadding="3" cellspacing="0" border="0">
			<tr>
				<th title="Programs">Users</th>
			</tr>
		</table>
		</div>
		<display:table class="simple" sort="list" cellspacing="2" cellpadding="3"
			id="user" name="secuserroles" export="false" pagesize="13"
			requestURI="/PMmodule/Admin/UserSearch.do">

            <display:setProperty name="paging.banner.placement" value="bottom" />
			<display:setProperty name="basic.msg.empty_list"
				value="No records found, please enter a filter and click search." />

			<display:column sortable="true" title="User ID" sortProperty="userName">
				<a
					href="<html:rewrite action="/PMmodule/Admin/UserManager.do"/>?method=edit&providerNo=<c:out value="${user.providerNo}" />">
				<c:out value="${user.userName}" /> </a>
			</display:column>

			<display:column sortable="true" title="First Name" sortProperty="providerFName">
				<a
					href="<html:rewrite action="/PMmodule/Admin/UserManager.do"/>?method=edit&providerNo=<c:out value="${user.providerNo}" />">
				<c:out value="${user.providerFName}" /> </a>
			</display:column>
			<display:column sortable="true" title="Last Name" sortProperty="providerLName">
				<a
					href="<html:rewrite action="/PMmodule/Admin/UserManager.do"/>?method=edit&providerNo=<c:out value="${user.providerNo}" />">
				<c:out value="${user.providerLName}" /> </a>
			</display:column>
			<display:column sortable="true" title="Active" sortProperty="activeyn">
				<c:if test="${user.activeyn == 1 }"> Yes </c:if>
				<c:if test="${user.activeyn == 0}"> No </c:if>
			</display:column>
		</display:table></div>
		</td>
	</tr>
</table>










<script language="javascript" type="text/javascript">
<!--

function submitForm(mthd){
	trimInputBox();
	document.forms[0].method.value=mthd;
	document.forms[0].submit();

}

function resetForm(){
		var form = document.forms[0];
		form.elements['criteria.userName'].value='';
		form.elements['criteria.firstName'].value='';
		form.elements['criteria.lastName'].value='';
		form.elements['criteria.active'].selectedIndex = 0;
		form.elements['criteria.roleName'].selectedIndex = 0;
}
				function init()
				{
					var form = document.forms[0];
					form.elements['criteria.userName'].focus();
					form.onkeypress=function() {keypress(event);}
				}
				function keypress(event)
				{
					var keynum;
					if(window.event) // IE
			  		{
			  			keynum = event.keyCode;
			  		}
					else if(event.which) // Netscape/Firefox/Opera
			  		{
			  			keynum = event.which;
			  		}
					if (keynum==13) submitForm('search');
					return true;
				}


//-->
</script>
