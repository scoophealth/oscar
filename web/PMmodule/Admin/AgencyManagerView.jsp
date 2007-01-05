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
<%@ page import="org.oscarehr.PMmodule.web.formbean.*"%>
<html:form action="/PMmodule/AgencyManager">
	<html:hidden property="view.tab" />
	<input type="hidden" name="id" value="<c:out value="${requestScope.id}"/>" />
	<input type="hidden" name="method" value="view" />
	<div class="tabs" id="tabs">
		<script>
			function clickTab(name) {
				document.agencyManagerForm.elements['view.tab'].value=name;
				document.agencyManagerForm.submit();
			}
		</script>
		<%
			String selectedTab = request.getParameter("view.tab");
		
			if (selectedTab == null || selectedTab.trim().equals("")) {
				selectedTab = AgencyManagerViewFormBean.tabs[0];
			}
		%>
		<table cellpadding="0" cellspacing="0" border="0">
			<tr>
				<%
				for (int x = 0; x < AgencyManagerViewFormBean.tabs.length; x++) {
					if (AgencyManagerViewFormBean.tabs[x].equals(selectedTab)) {
				%>
				<td style="background-color: #555;">
					<a href="javascript:void(0)" onclick="javascript:clickTab('<%=AgencyManagerViewFormBean.tabs[x] %>'); return false;"><%=AgencyManagerViewFormBean.tabs[x]%></a>
				</td>
				<%
					} else {
				%>
				<td>
					<a href="javascript:void(0)" onclick="javascript:clickTab('<%=AgencyManagerViewFormBean.tabs[x] %>');return false;"><%=AgencyManagerViewFormBean.tabs[x]%></a>
				</td>
				<%
					}
				}
				%>
			</tr>
		</table>
	</div>
	<%@include file="/common/messages.jsp"%>
	<jsp:include page="<%="/PMmodule/Admin/AgencyView/" + selectedTab.toLowerCase().replaceAll(" ", "_") + ".jsp"%>" />
</html:form>