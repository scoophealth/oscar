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
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@ include file="/taglibs.jsp"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_pmm" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_pmm");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page import="org.oscarehr.PMmodule.web.formbean.*"%>
<%@page import="org.oscarehr.PMmodule.web.utils.UserRoleUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.oscarehr.PMmodule.service.ClientManager"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.common.model.DemographicExt"%>
<%@page import="oscar.OscarProperties"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.PMmodule.service.AdmissionManager"%>
<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="oscar.util.OscarRoleObjectPrivilege"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Properties" %>
<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(session);
%>
<html:form action="/PMmodule/ClientManager.do">

	<html:hidden property="view.tab" />
	<input type="hidden" name="id"
		value="<c:out value="${requestScope.id}"/>" />
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
			<td style="text-align: right;" align="right"><c:out
				value="${client.formattedName }" /></td>
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
			boolean bedRoomReservation = false;
			String roleName = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
			
			ArrayList v = new ArrayList(OscarRoleObjectPrivilege.getPrivilegeProp("_pmm_client.BedRoomReservation"));
			
			if(OscarRoleObjectPrivilege.checkPrivilege(roleName, (Properties)v.get(0), (List<String>)v.get(1), (List<String>)v.get(2), "r")){
				bedRoomReservation = true;
			}
			
			if (session.getAttribute("userrole") != null && ((String) session.getAttribute("userrole")).indexOf("admin") != -1) {
				admin = true;
			}
			
			boolean isExternal=UserRoleUtils.hasRole(request, UserRoleUtils.Roles.external);
			
			for (int x = 0; x < ClientManagerFormBean.tabs.length; x++) {
				
				if (isExternal)
				{
					// "Bed Reservation", "Forms", "Refer"
					if ("Bed Reservation".equalsIgnoreCase(ClientManagerFormBean.tabs[x]) || "Forms".equalsIgnoreCase(ClientManagerFormBean.tabs[x]) || "Refer".equalsIgnoreCase(ClientManagerFormBean.tabs[x])
							|| "Refer to vacancy".equals(ClientManagerFormBean.tabs[x])) 
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
				
				if(!bedRoomReservation && "Bed/Room Reservation".equalsIgnoreCase(ClientManagerFormBean.tabs[x])) 
					continue;
				
				if (ClientManagerFormBean.tabs[x].equals(selectedTab)) {
			%>
			<td style="background-color: #555;"><a href="javascript:void(0)"
				onclick="javascript:clickTab('<%=ClientManagerFormBean.tabs[x] %>'); return false;"><%=ClientManagerFormBean.tabs[x]%></a></td>
			<%
				} else {
			        int demographicId=Integer.parseInt((String)request.getAttribute("id"));
					AdmissionManager admissionManager=(AdmissionManager)SpringUtils.getBean("admissionManager");
					boolean activeInFacility=admissionManager.isActiveInCurrentFacility(loggedInInfo, demographicId);
					boolean requireActiveTab="Refer".equals(ClientManagerFormBean.tabs[x]) || "Discharge".equals(ClientManagerFormBean.tabs[x]) 
							                 || "Refer to vacancy".equals(ClientManagerFormBean.tabs[x]);
					
					if (requireActiveTab && !activeInFacility)
					{
						%>			
			<td style="color: silver"><%=ClientManagerFormBean.tabs[x]%></td>
			<%
					}
					else
					{
						%>
			<td><a href="javascript:void(0)"
				onclick="javascript:clickTab('<%=ClientManagerFormBean.tabs[x] %>');return false;"><%=ClientManagerFormBean.tabs[x]%></a></td>
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
	<jsp:include
		page='<%="/PMmodule/ClientManager/" + selectedTab.toLowerCase().replaceAll(" ", "_") + ".jsp"%>' />
</html:form>
