<%@ include file="/taglibs.jsp"%>

<%@ page import="org.oscarehr.PMmodule.web.formbean.*"%>
<%@ page import="org.oscarehr.PMmodule.model.Program"%>
<%@ page import="org.apache.struts.validator.DynaValidatorForm"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>

<html:form action="/PMmodule/ProgramManager">

	<html:hidden property="view.tab" />
	<input type="hidden" name="id"
		value="<c:out value="${requestScope.id}"/>" />
	<input type="hidden" name="method" value="edit" />
	<html:hidden property="program.id" />

	<c:choose>
		<c:when test="${id != null && id gt 0}">
			<script>
				function clickTab(name) {
					document.programManagerForm.method.value='edit';
					document.programManagerForm.elements['view.tab'].value=name;
					document.programManagerForm.submit();
				}
			</script>

			<table width="100%">
				<tr>
					<td style="text-align: right;" align="right"><c:out
						value="${programName}" /></td>
				</tr>
			</table>

			<div class="tabs">
			<%
					String selectedTab = request.getParameter("view.tab");
					/*
					if (selectedTab == null || selectedTab.trim().equals("")) {
						selectedTab = ProgramManagerViewFormBean.tabs[0];
					}
					*/
					String roleName$ = (String) session.getAttribute("userrole") + "," + (String) session.getAttribute("user");

				%>
			<table cellpadding="0" cellspacing="0" border="0">
				<tr>
					<%
							DynaValidatorForm form = (DynaValidatorForm) session.getAttribute("programManagerForm");
							Program program = (Program) form.get("program");
							
							for (int i = 0; i < ProgramManagerViewFormBean.tabs.length; i++) {
								if (ProgramManagerViewFormBean.tabs[i].equalsIgnoreCase("Bed Check") && program.isService()) {
									continue;
								}
								
								if (selectedTab != null && !selectedTab.trim().equals("") && ProgramManagerViewFormBean.tabs[i].equals(selectedTab)) {						
						%>
					<td style="background-color: #555;"><a
						href="javascript:void(0)"
						onclick="javascript:clickTab('<%=ProgramManagerViewFormBean.tabs[i] %>');return false;"><%=ProgramManagerViewFormBean.tabs[i]%></a>
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
					<%} 
								%>
					<%
								}
							}
						%>
				</tr>
			</table>
			</div>
			<%@ include file="/common/messages.jsp"%>
			<% //System.out.println("/PMmodule/Admin/ProgramEdit/" + selectedTab.toLowerCase().replaceAll(" ","_") + ".jsp");
				if (selectedTab == null || selectedTab.trim().equals("")) {
			%>
			<security:oscarSec roleName="<%=roleName$%>"
				objectName="_pmm_editProgram.general" rights="r">
				<jsp:include page="/PMmodule/Admin/ProgramEdit/general.jsp" />
			</security:oscarSec>

			<%} else { %>
			<jsp:include
				page='<%="/PMmodule/Admin/ProgramEdit/" + selectedTab.toLowerCase().replaceAll(" ","_") + ".jsp"%>' />
			<%} %>
		</c:when>
		<c:otherwise>
			<%@ include file="/common/messages.jsp"%>
			<jsp:include page="/PMmodule/Admin/ProgramEdit/general.jsp" />
		</c:otherwise>
	</c:choose>
</html:form>