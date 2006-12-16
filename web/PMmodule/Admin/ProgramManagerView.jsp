<%@ include file="/taglibs.jsp"%>
<%@ page import="org.oscarehr.PMmodule.web.formbean.*"%>
<html:form action="/PMmodule/ProgramManagerView">
	
	<html:hidden property="tab" />
	<input type="hidden" name="id" value="<c:out value="${requestScope.id}"/>" />
	<input type="hidden" name="method" value="view" />
	
	<script>
		function clickTab(name) {
			document.programManagerViewForm.tab.value=name;
			document.programManagerViewForm.submit();
		}
	</script>
	
	<table width="100%">
		<tr>
			<td style="text-align: right;" align="right"><c:out value="${program.name}" /></td>
		</tr>
	</table>
	
	<div class="tabs" id="tabs">
		<%
			ProgramManagerViewFormBean formBean = (ProgramManagerViewFormBean) request.getAttribute("programManagerViewForm");
			String selectedTab = formBean.getTab();
			
			if (selectedTab == null || selectedTab.trim().equals("")) {
				selectedTab = ProgramManagerViewFormBean.tabs[0];
			}
		%>
		<table cellpadding="0" cellspacing="0" border="0">
			<tr>
				<%
				for (int x = 0; x < ProgramManagerViewFormBean.tabs.length; x++) {
					if (ProgramManagerViewFormBean.tabs[x].equals(selectedTab)) {
				%>
					<td style="background-color: #555;">
						<a href="javascript:void(0)" onclick="javascript:clickTab('<%=ProgramManagerViewFormBean.tabs[x] %>'); return false;"><%=ProgramManagerViewFormBean.tabs[x]%></a>
					</td>
				<%
					} else {
				%>
					<td>
						<a href="javascript:void(0)" onclick="javascript:clickTab('<%=ProgramManagerViewFormBean.tabs[x] %>');return false;"><%=ProgramManagerViewFormBean.tabs[x]%></a>
					</td>
				<%
					}
				}
				%>
			</tr>
		</table>
	</div>
	<%@ include file="/common/messages.jsp"%>
	<jsp:include page="<%="/PMmodule/Admin/ProgramView/" + selectedTab.toLowerCase().replaceAll(" ", "_") + ".jsp"%>" />
</html:form>
