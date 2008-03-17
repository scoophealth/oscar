<%@ include file="/taglibs.jsp"%>
<%@ page import="org.oscarehr.PMmodule.web.formbean.*"%>
<%@page import="org.oscarehr.PMmodule.web.utils.UserRoleUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.oscarehr.PMmodule.service.ClientManager"%>
<%@page import="org.oscarehr.PMmodule.model.Demographic"%>
<%@page import="org.oscarehr.PMmodule.model.DemographicExt"%>
<%@ page import="oscar.OscarProperties" %>
<%
	// This section is an obnoxtious security check with not so glamourous user feedback, but I don't really
	// care right now as it would be a hacker or improper use that triggers this.
	// As for a better security architecture, that will have to wait for refactoring of the entire system.

	boolean userHasExternalRole=UserRoleUtils.hasRole(request, UserRoleUtils.Roles.external);
	if (userHasExternalRole)
	{
		WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		ClientManager clientManager = (ClientManager) applicationContext.getBean("clientManager");
		DemographicExt demographicExtConsent=clientManager.getDemographicExt(Integer.parseInt(request.getParameter("id")), Demographic.CONSENT_GIVEN_KEY);
		boolean allowed=false;
		
		if (demographicExtConsent!=null)
		{
			Demographic.ConsentGiven consent=Demographic.ConsentGiven.valueOf(demographicExtConsent.getValue());
			if (consent==Demographic.ConsentGiven.ALL || consent==Demographic.ConsentGiven.CIRCLE_OF_CARE)
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
				<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.PMmodule.service.AdmissionManager"%>
<%@page import="org.oscarehr.util.SessionConstants"%>
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
					if ("Bed Reservation".equalsIgnoreCase(ClientManagerFormBean.tabs[x]) || "Forms".equalsIgnoreCase(ClientManagerFormBean.tabs[x]) || "Refer".equalsIgnoreCase(ClientManagerFormBean.tabs[x])) 
						continue;				
				}
					
				if (!admin && ClientManagerFormBean.tabs[x].equalsIgnoreCase("refer")) {
					Boolean b = (Boolean) request.getAttribute("isInProgramDomain");
					if (b == null) {
						continue;
					}
				}
			
				//check role:
				//If the user don't have the role "perform bed assignments" for this program, then tab "perform bed assignments" won't show up.
				if(OscarProperties.getInstance().isTorontoRFQ())  {
                    Object pfb = request.getSession().getAttribute("performBedAssignments");
                    if((pfb != null) && !(((Boolean) pfb).booleanValue()) &&
                            "Bed Reservation".equalsIgnoreCase(ClientManagerFormBean.tabs[x]))
						continue;					
				}
				
				
				if (ClientManagerFormBean.tabs[x].equals(selectedTab)) {
			%>
			<td style="background-color: #555;"><a href="javascript:void(0)" onclick="javascript:clickTab('<%=ClientManagerFormBean.tabs[x] %>'); return false;"><%=ClientManagerFormBean.tabs[x]%></a></td>
			<%
				} else {
			        Integer facilityId=(Integer)request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY_ID);
			        int demographicId=Integer.parseInt((String)request.getAttribute("id"));
					AdmissionManager admissionManager=(AdmissionManager)SpringUtils.beanFactory.getBean("admissionManager");
					boolean activeInFacility=admissionManager.isActiveInFacility(facilityId, demographicId);
					boolean requireActiveTab="Refer".equals(ClientManagerFormBean.tabs[x]) || "Discharge".equals(ClientManagerFormBean.tabs[x]);
					
					if (requireActiveTab && !activeInFacility)
					{
						%>
						<td style="color:silver"><%=ClientManagerFormBean.tabs[x]%></td>
						<%
					}
					else
					{
						%>
						<td><a href="javascript:void(0)" onclick="javascript:clickTab('<%=ClientManagerFormBean.tabs[x] %>');return false;"><%=ClientManagerFormBean.tabs[x]%></a></td>
						<%
					}
				}
			}
			%>
		</tr>
	</table>
	</div>
<%
if(selectedTab.contains("Bed/Room")){
	selectedTab = selectedTab.toLowerCase().replaceAll("bed/room", "bed");
}
%>	
	<%@ include file="/common/messages.jsp"%>
	<jsp:include page='<%="/PMmodule/ClientManager/" + selectedTab.toLowerCase().replaceAll(" ", "_") + ".jsp"%>' />
</html:form>