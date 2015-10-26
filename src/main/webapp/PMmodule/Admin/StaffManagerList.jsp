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
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.pmm" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin.pmm");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ taglib uri="/WEB-INF/quatro-tag.tld" prefix="quatro"%>

<html:html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Staff Manager</title>
<style>
.sortable {
	background-color: #555;
	color: #555;
}

.b th {
	border-right: 1px solid #333;
	background-color: #ddd;
	color: #ddd;
	border-left: 1px solid #fff;
}

.message {
	color: red;
	background-color: white;
}

.error {
	color: red;
	background-color: white;
}
</style>
<script>
			function ConfirmDelete(name)
			{
				if(confirm("Are you sure you want to delete " + name + " ?")) {
					return true;
				}
				return false;
			}

			function sort() {
			
			}
			
	        function select_facility(ctl) {
//     		  var programId = ctl.options[ctl.selectedIndex].value;
//        	  document.staffManagerForm.elements['view.admissionId'].value=programId;
		      document.staffManagerForm.method.value='search';
		      document.staffManagerForm.submit();
	        }
	        
	        function select_program(ctl) {
//     		  var programId = ctl.options[ctl.selectedIndex].value;
//        	  document.staffManagerForm.elements['view.admissionId'].value=programId;
		      document.staffManagerForm.method.value='search';
		      document.staffManagerForm.submit();
	        }
		</script>
<c:set var="ctx" value="${pageContext.request.contextPath}"
	scope="request" />
<script type="text/javascript"
	src='<c:out value="${ctx}"/>/js/quatroLookup.js'></script>

</head>

<body marginwidth="0" marginheight="0">

<br />
<%@ include file="/common/messages.jsp"%>
<br />

<html:form action="/PMmodule/StaffManager.do">

	<div class="tabs" id="tabs">
	<table cellpadding="3" cellspacing="0" border="0">

		<tr>
			<th title="Programs">Staff List</th>

		</tr>
	</table>
	</div>

	<table width="100%" border="0">
		<tr>
			<td width="35%" style="font-weight: bold">Facilities</td>
			<td width="55%" style="font-weight: bold">Programs</td>
			<td width="10%"><html-el:hidden property="method" value="" /></td>
		</tr>
		<tr>
			<td><html-el:select property="facilityId"
				onchange="select_facility(this);">
				<html-el:option value="0">Any</html-el:option>
				<c:forEach var="facility" items="${facilities}">
					<html-el:option value="${facility.id}">
						<c:out value="${facility.name}" />
					</html-el:option>
				</c:forEach>
			</html-el:select></td>
			<td><html-el:select property="programId"
				onchange="select_program(this);">
				<html-el:option value="0">Any</html-el:option>
				<c:forEach var="program" items="${programs}">
					<html-el:option value="${program.id}">
						<c:out value="${program.name}" />
					</html-el:option>
				</c:forEach>
			</html-el:select></td>
			<td></td>
		</tr>
	</table>
	</br>

	<display:table class="simple" cellspacing="2" cellpadding="3"
		id="provider" name="providers" export="false" pagesize="0"
		requestURI="/PMmodule/StaffManager.do">
		<display:setProperty name="paging.banner.placement" value="bottom" />
		<display:setProperty name="paging.banner.item_name" value="provider" />
		<display:setProperty name="paging.banner.items_name" value="providers" />
		<display:setProperty name="basic.msg.empty_list"
			value="No providers found." />
		<display:column sortable="false" title="">
			<a
				href="<html:rewrite action="/PMmodule/StaffManager.do"/>?method=edit&id=<c:out value="${provider.providerNo}" />">
			Edit </a>
		</display:column>
		<display:column property="formattedName" sortable="true" title="Name" />
		<display:column property="phone" sortable="true" title="Phone" />
		<display:column property="workPhone" sortable="true"
			title="Work Phone" />
			
	</display:table>

</html:form>

</body>
</html:html>
