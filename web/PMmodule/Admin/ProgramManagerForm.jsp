<%@ include file="/taglibs.jsp"%>

<%@ page import="org.oscarehr.PMmodule.web.formbean.*"%>
<%@ page import="org.oscarehr.PMmodule.model.Program"%>
<%@ page import="org.apache.struts.validator.DynaValidatorForm"%>

<html:form action="/PMmodule/ProgramManager">

	<html:hidden property="view.tab" />
	<input type="hidden" name="id" value="<c:out value="${requestScope.id}"/>" />
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
					<td style="text-align:right;" align="right"><c:out value="${programName}" /></td>
				</tr>
			</table>
			
			<div class="tabs">
				<%
					String selectedTab = request.getParameter("view.tab");
				
					if (selectedTab == null || selectedTab.trim().equals("")) {
						selectedTab = ProgramManagerViewFormBean.tabs[0];
					}
				%>
				<table cellpadding="0" cellspacing="0" border="0">
					<tr>
						<%
							DynaValidatorForm form = (DynaValidatorForm) session.getAttribute("programManagerForm");
							Program program = (Program) form.get("program");
							
							for (int i = 0; i < ProgramManagerViewFormBean.tabs.length; i++) {
								if (ProgramManagerViewFormBean.tabs[i].equalsIgnoreCase("Bed Check") && program.isService()) {
									break;
								}
								
								if (ProgramManagerViewFormBean.tabs[i].equals(selectedTab)) {
						%>
						<td style="background-color: #555;">
							<a href="javascript:void(0)" onclick="javascript:clickTab('<%=ProgramManagerViewFormBean.tabs[i] %>');return false;"><%=ProgramManagerViewFormBean.tabs[i]%></a>
						</td>
						<%
								} else {
						%>
						<td>
							<a href="javascript:void(0)" onclick="javascript:clickTab('<%=ProgramManagerViewFormBean.tabs[i] %>');return false;"><%=ProgramManagerViewFormBean.tabs[i]%></a>
						</td>
						<%
								}
							}
						%>
					</tr>
				</table>
			</div>
			<%@ include file="/common/messages.jsp"%>
			<% System.out.println("/PMmodule/Admin/ProgramEdit/" + selectedTab.toLowerCase().replaceAll(" ","_") + ".jsp");%>
			<jsp:include page="<%="/PMmodule/Admin/ProgramEdit/" + selectedTab.toLowerCase().replaceAll(" ","_") + ".jsp"%>" />
		</c:when>
		<c:otherwise>
			<%@ include file="/common/messages.jsp"%>
			<jsp:include page="/PMmodule/Admin/ProgramEdit/general.jsp" />
		</c:otherwise>
	</c:choose>
</html:form>