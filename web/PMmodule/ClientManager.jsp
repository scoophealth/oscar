<%@ include file="/taglibs.jsp"%>
<%@ page import="org.oscarehr.PMmodule.web.formbean.*"%>
<%@page import="org.oscarehr.PMmodule.web.utils.UserRoleUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.oscarehr.PMmodule.service.ClientManager"%>
<%@page import="org.oscarehr.PMmodule.model.Demographic"%>
<%@page import="org.oscarehr.PMmodule.model.DemographicExt"%>

<%
	// This section is an obnoxtious security check with not so glamourous user feedback, but I don't really
	// care right now as it would be a hacker or improper use that triggers this.
	// As for a better security architecture, that will have to wait for refactoring of the entire system.

	boolean userHasExternalRole=UserRoleUtils.hasRole(request, UserRoleUtils.Roles.external);
	if (userHasExternalRole)
	{
		WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		ClientManager clientManager = (ClientManager) applicationContext.getBean("clientManager");
		DemographicExt demographicExt=clientManager.getDemographicExt(Integer.parseInt(request.getParameter("id")), Demographic.SHARING_OPTING_KEY);
		boolean allowed=false;
		
		if (demographicExt!=null)
		{
			Demographic.OptingStatus optingStatus=Demographic.OptingStatus.valueOf(demographicExt.getValue());
			if (optingStatus==Demographic.OptingStatus.IMPLICITLY_OPTED_IN || optingStatus==Demographic.OptingStatus.EXPLICITLY_OPTED_IN)
			{
				allowed=true;
			}
		}
			
		if (!allowed)
		{
			// the status won't take affect but it's good practice to put it here, it's the dodgy nested includes which break this.
			response.setStatus(HttpServletResponse.SC_FORBIDDEN, "You do not have the required roles to access this resource.");
			// as a result we'll manually print a forbidden.
			%>
				<span>You do not have the required roles to access this resource.</span>
			<%
			return;
		}
	}
%>
<html:form action="/PMmodule/ClientManager.do">
	
	<html:hidden property="view.tab" />
	<input type="hidden" name="id" value="<c:out value="${requestScope.id}"/>" />
	<input type="hidden" name="method" value="edit" />
	<html:hidden property="client.demographicNo" />
	
	<script>
		function clickTab(name) {
			document.clientManagerForm.method.value='edit';
			document.clientManagerForm.elements['view.tab'].value=name;
			document.clientManagerForm.submit();
		}
	</script>
	
	<table width="100%">
		<tr>
			<td style="text-align: right;" align="right"><c:out value="${client.formattedName }" /></td>
		</tr>
	</table>
	
	<div class="tabs" id="tabs">
	<%
		String selectedTab = request.getParameter("view.tab");
		String tabOverride = (String) request.getAttribute("tab.override");
		
		if (selectedTab == null || selectedTab.trim().equals("")) {
			selectedTab = ClientManagerFormBean.tabs[0];
		}
		
		if (tabOverride != null) {
			selectedTab = tabOverride;
		}
	%>
	<table cellpadding="0" cellspacing="0" border="0">
		<tr>
			<%
			boolean admin = false;
		
			if (session.getAttribute("userrole") != null && ((String) session.getAttribute("userrole")).indexOf("admin") != -1) {
				admin = true;
			}
			
			boolean isExternal=UserRoleUtils.hasRole(request, UserRoleUtils.Roles.external);
			
			for (int x = 0; x < ClientManagerFormBean.tabs.length; x++) {
				
				if (isExternal)
				{
					// "Bed Reservation", "Forms", "Refer"
					if ("Bed Reservation".equalsIgnoreCase(ClientManagerFormBean.tabs[x]) || "Forms".equalsIgnoreCase(ClientManagerFormBean.tabs[x]) || "Refer".equalsIgnoreCase(ClientManagerFormBean.tabs[x])) continue;
				}
					
				if (!admin && ClientManagerFormBean.tabs[x].equalsIgnoreCase("refer")) {
					Boolean b = (Boolean) request.getAttribute("isInProgramDomain");
					if (b == null) {
						continue;
					}
				}
			
				if (ClientManagerFormBean.tabs[x].equals(selectedTab)) {
			%>
			<td style="background-color: #555;"><a href="javascript:void(0)" onclick="javascript:clickTab('<%=ClientManagerFormBean.tabs[x] %>'); return false;"><%=ClientManagerFormBean.tabs[x]%></a></td>
			<%
				} else {
			%>
			<td><a href="javascript:void(0)" onclick="javascript:clickTab('<%=ClientManagerFormBean.tabs[x] %>');return false;"><%=ClientManagerFormBean.tabs[x]%></a></td>
			<%
				}
			}
			%>
		</tr>
	</table>
	</div>
	<%@ include file="/common/messages.jsp"%>
	<jsp:include page="<%="/PMmodule/ClientManager/" + selectedTab.toLowerCase().replaceAll(" ", "_") + ".jsp"%>" />
</html:form>