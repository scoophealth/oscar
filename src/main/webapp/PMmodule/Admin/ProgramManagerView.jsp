<%@ include file="/taglibs.jsp"%>
<%@ page import="org.oscarehr.PMmodule.web.formbean.*"%>

<%@page import="org.oscarehr.PMmodule.model.Program"%>
<html:form action="/PMmodule/ProgramManagerView">

	<html:hidden property="tab" />
	<input type="hidden" name="id"
		value="<c:out value="${requestScope.id}"/>" />
	<input type="hidden" name="method" value="view" />

	<script>
		function clickTab(name) {
			document.programManagerViewForm.tab.value=name;
			document.programManagerViewForm.submit();
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
				%>
			<td><a href="javascript:void(0)"
				onclick="javascript:clickTab('<%=ProgramManagerViewFormBean.tabs[i]%>');return false;"><%=ProgramManagerViewFormBean.tabs[i]%></a>
			</td>
			<%
						}
					}
				%>
		</tr>
	</table>
	</div>
	<jsp:include page="/common/messages.jsp" />
	<jsp:include
		page='<%="/PMmodule/Admin/ProgramView/" + selectedTab.toLowerCase().replaceAll(" ", "_") + ".jsp"%>' />
</html:form>
