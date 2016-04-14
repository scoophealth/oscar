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

<%@ page import="org.oscarehr.PMmodule.web.formbean.*"%>
<%@page import="org.oscarehr.PMmodule.model.Program"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<html:form action="/PMmodule/ProgramManagerView">

	<html:hidden property="tab" />
	<html:hidden property="subtab" />
	<html:hidden property="vacancyOrTemplateId" />
	<input type="hidden" name="id"
		value="<c:out value="${requestScope.id}"/>" />
	<input type="hidden" name="method" value="view" />

	<script>
		function clickTab(name) {
			document.programManagerViewForm.tab.value=name;
			document.programManagerViewForm.subtab.value=name;
			document.programManagerViewForm.submit();
		}
		function clickTab2(tabName, subtabName) {
			document.programManagerViewForm.tab.value=name;
			document.programManagerViewForm.subtab.value=subtabName;
			document.programManagerViewForm.submit();
		}
		
		function clickLink(tabName, subtabName, id) {
			document.programManagerViewForm.vacancyOrTemplateId.value=id;
			clickTab2(tabName, subtabName);
		}
	</script>

	<table width="100%">
		<tr>
			<td style="text-align: right;" align="right"><c:out
				value="${program.name}" /></td>
		</tr>
	</table>

	<div class="tabs">
	<%
			String selectedTab = request.getParameter("tab");
			String selectedSubtab = request.getParameter("subtab");
				
			if (selectedTab == null || selectedTab.trim().equals("")) {
				selectedTab = ProgramManagerViewFormBean.tabs[0];
			}
			
		%>
	<table cellpadding="0" cellspacing="0" border="0">
		<tr>
			<%
					Program program = (Program) request.getAttribute("program");

					for (int i = 0; i < ProgramManagerViewFormBean.tabs.length; i++) {
						if (ProgramManagerViewFormBean.tabs[i].equalsIgnoreCase("Bed Check") && program.isService()) {
							//break;
							continue;
						}
						
						if (ProgramManagerViewFormBean.tabs[i].equalsIgnoreCase(selectedTab)) {
				%>
			<td style="background-color: #555;"><a href="javascript:void(0)"
				onclick="javascript:clickTab('<%=ProgramManagerViewFormBean.tabs[i]%>');return false;"><%=ProgramManagerViewFormBean.tabs[i]%></a>
			</td>
			<%
						} else {
							if(ProgramManagerViewFormBean.tabs[i].equalsIgnoreCase("General")) {
				%>
			<security:oscarSec roleName="<%=roleName$%>"
				objectName="_pmm_editProgram.general" rights="r">
				<td><a href="javascript:void(0)"
					onclick="javascript:clickTab('<%=ProgramManagerViewFormBean.tabs[i] %>');return false;"><%=ProgramManagerViewFormBean.tabs[i]%></a></td>
			</security:oscarSec>
			<%} else if(ProgramManagerViewFormBean.tabs[i].equalsIgnoreCase("Staff")) {
						%>
			<security:oscarSec roleName="<%=roleName$%>"
				objectName="_pmm_editProgram.staff" rights="r">
				<td><a href="javascript:void(0)"
					onclick="javascript:clickTab('<%=ProgramManagerViewFormBean.tabs[i] %>');return false;"><%=ProgramManagerViewFormBean.tabs[i]%></a></td>
			</security:oscarSec>
			<%} else if(ProgramManagerViewFormBean.tabs[i].equalsIgnoreCase("Function User")) {
						%>
			<security:oscarSec roleName="<%=roleName$%>"
				objectName="_pmm_editProgram.functionUser" rights="r">
				<td><a href="javascript:void(0)"
					onclick="javascript:clickTab('<%=ProgramManagerViewFormBean.tabs[i] %>');return false;"><%=ProgramManagerViewFormBean.tabs[i]%></a></td>
			</security:oscarSec>
			<%} else if(ProgramManagerViewFormBean.tabs[i].equalsIgnoreCase("Teams")) {
						%>
			<security:oscarSec roleName="<%=roleName$%>"
				objectName="_pmm_editProgram.teams" rights="r">
				<td><a href="javascript:void(0)"
					onclick="javascript:clickTab('<%=ProgramManagerViewFormBean.tabs[i] %>');return false;"><%=ProgramManagerViewFormBean.tabs[i]%></a></td>
			</security:oscarSec>
			<%} else if(ProgramManagerViewFormBean.tabs[i].equalsIgnoreCase("Clients")) {
						%>
			<security:oscarSec roleName="<%=roleName$%>"
				objectName="_pmm_editProgram.clients" rights="r">
				<td><a href="javascript:void(0)"
					onclick="javascript:clickTab('<%=ProgramManagerViewFormBean.tabs[i] %>');return false;"><%=ProgramManagerViewFormBean.tabs[i]%></a></td>
			</security:oscarSec>
			<%} else if(ProgramManagerViewFormBean.tabs[i].equalsIgnoreCase("Queue")) {
						%>
			<security:oscarSec roleName="<%=roleName$%>"
				objectName="_pmm_editProgram.queue" rights="r">
				<td><a href="javascript:void(0)"
					onclick="javascript:clickTab('<%=ProgramManagerViewFormBean.tabs[i] %>');return false;"><%=ProgramManagerViewFormBean.tabs[i]%></a></td>
			</security:oscarSec>
			<%} else if(ProgramManagerViewFormBean.tabs[i].equalsIgnoreCase("Access")) {
						%>
			<security:oscarSec roleName="<%=roleName$%>"
				objectName="_pmm_editProgram.access" rights="r">
				<td><a href="javascript:void(0)"
					onclick="javascript:clickTab('<%=ProgramManagerViewFormBean.tabs[i] %>');return false;"><%=ProgramManagerViewFormBean.tabs[i]%></a></td>
			</security:oscarSec>
			<%} else if(ProgramManagerViewFormBean.tabs[i].equalsIgnoreCase("Bed Check")) {
						%>
			<security:oscarSec roleName="<%=roleName$%>"
				objectName="_pmm_editProgram.bedCheck" rights="r">
				<td><a href="javascript:void(0)"
					onclick="javascript:clickTab('<%=ProgramManagerViewFormBean.tabs[i] %>');return false;"><%=ProgramManagerViewFormBean.tabs[i]%></a></td>
			</security:oscarSec>
			<%} else if(ProgramManagerViewFormBean.tabs[i].equalsIgnoreCase("Client Status")) {
						%>
			<security:oscarSec roleName="<%=roleName$%>"
				objectName="_pmm_editProgram.clientStatus" rights="r">
				<td><a href="javascript:void(0)"
					onclick="javascript:clickTab('<%=ProgramManagerViewFormBean.tabs[i] %>');return false;"><%=ProgramManagerViewFormBean.tabs[i]%></a></td>
			</security:oscarSec>
			<%} else if(ProgramManagerViewFormBean.tabs[i].equalsIgnoreCase("Service Restrictions")) {
						%>
			<security:oscarSec roleName="<%=roleName$%>"
				objectName="_pmm_editProgram.serviceRestrictions" rights="r">
				<td><a href="javascript:void(0)"
					onclick="javascript:clickTab('<%=ProgramManagerViewFormBean.tabs[i] %>');return false;"><%=ProgramManagerViewFormBean.tabs[i]%></a></td>
			</security:oscarSec>
				<%} else if(ProgramManagerViewFormBean.tabs[i].equalsIgnoreCase("Vacancies")) {
						%>
			<security:oscarSec roleName="<%=roleName$%>"
				objectName="_pmm_editProgram.vacancies" rights="r">
				<td><a href="javascript:void(0)"
					onclick="javascript:clickTab('<%=ProgramManagerViewFormBean.tabs[i] %>');return false;"><%=ProgramManagerViewFormBean.tabs[i]%></a></td>
			</security:oscarSec>
			<%} else if(ProgramManagerViewFormBean.tabs[i].equalsIgnoreCase("Schedule")) {
						%>
			<security:oscarSec roleName="<%=roleName$%>"
				objectName="_pmm_editProgram.schedules" rights="r">
				<td><a href="javascript:void(0)"
					onclick="javascript:clickTab('<%=ProgramManagerViewFormBean.tabs[i] %>');return false;"><%=ProgramManagerViewFormBean.tabs[i]%></a></td>
			</security:oscarSec>
			<%} else if(ProgramManagerViewFormBean.tabs[i].equalsIgnoreCase("Encounter Types")) {
				%>
	<security:oscarSec roleName="<%=roleName$%>"
		objectName="_pmm_editProgram.encounterTypes" rights="r">
		<td><a href="javascript:void(0)"
			onclick="javascript:clickTab('<%=ProgramManagerViewFormBean.tabs[i] %>');return false;"><%=ProgramManagerViewFormBean.tabs[i]%></a></td>
	</security:oscarSec>
	<%}
						%>
			<%
						}
					}
				%>
			
		</tr>
	</table>
	</div>
	<jsp:include page="/common/messages.jsp" />
	<% if (selectedSubtab != null &&!selectedSubtab.trim().equals("")&& !selectedTab.equals(selectedSubtab)) { %>
		<jsp:include
			page='<%="/PMmodule/Admin/ProgramView/" + selectedSubtab.toLowerCase().replaceAll(" ","_") + ".jsp"%>' />
	<% } else { %>
			<jsp:include
				page='<%="/PMmodule/Admin/ProgramView/" + selectedTab.toLowerCase().replaceAll(" ","_") + ".jsp"%>' />
	<%}%>
</html:form>
