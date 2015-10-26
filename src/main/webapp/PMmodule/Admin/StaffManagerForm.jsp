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

<%@ page import="org.oscarehr.PMmodule.web.formbean.*"%>
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
<html:form action="/PMmodule/StaffManager.do">
	<html:hidden property="view.tab" />
	<input type="hidden" name="id"
		value="<c:out value="${requestScope.id}"/>" />
	<input type="hidden" name="method" value="edit" />
	<html:hidden property="provider.providerNo" />
	<c:choose>
		<c:when test="${id != null && id gt 0}">
			<script>
				function clickTab(name) {
					document.staffManagerForm.method.value='edit';
					document.staffManagerForm.elements['view.tab'].value=name;
					document.staffManagerForm.submit();
				}
			</script>
			<table width="100%">
				<tr>
					<td style="text-align: right;" align="right"><c:out
						value="${providerName }" /></td>
				</tr>
			</table>
			<div class="tabs" id="tabs">
			<%
				String selectedTab = request.getParameter("view.tab");
				if (selectedTab == null || selectedTab.trim().equals("")) {
					selectedTab = StaffManagerViewFormBean.tabs[0];
				}
			%>
			<table cellpadding="0" cellspacing="0" border="0">
				<tr>
					<%
					for (int x = 0; x < StaffManagerViewFormBean.tabs.length; x++) {
					%>
					<%
					if (StaffManagerViewFormBean.tabs[x].equals(selectedTab)) {
					%>
					<td style="background-color: #555;"><a
						href="javascript:void(0)"
						onclick="javascript:clickTab('<%=StaffManagerViewFormBean.tabs[x] %>'); return false;"><%=StaffManagerViewFormBean.tabs[x]%></a></td>
					<%
					} else {
					%>
					<td><a href="javascript:void(0)"
						onclick="javascript:clickTab('<%=StaffManagerViewFormBean.tabs[x] %>');return false;"><%=StaffManagerViewFormBean.tabs[x]%></a></td>
					<%
					}
					%>
					<%
					}
					%>
				</tr>
			</table>
			</div>
			<%@ include file="/common/messages.jsp"%>
			<jsp:include
				page='<%="/PMmodule/Admin/StaffEdit/"+selectedTab.toLowerCase().replaceAll(" ","_") + ".jsp"%>' />
		</c:when>
		<c:otherwise>
			<%@ include file="/common/messages.jsp"%>
			<jsp:include page="/PMmodule/Admin/StaffEdit/summary.jsp" />
		</c:otherwise>
	</c:choose>
</html:form>
