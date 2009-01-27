<%@page import="org.oscarehr.PMmodule.web.ManageLinkedClients.LinkedDemographicHolder"%>
<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="org.oscarehr.common.dao.*"%>
<%@page import="org.oscarehr.common.model.*"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.dao.DemographicDao"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.caisi_integrator.ws.client.DemographicWs"%>
<%@page import="org.oscarehr.PMmodule.web.ManageLinkedClients"%>
<%@page import="org.apache.commons.lang.time.DateFormatUtils"%>
<%@page import="org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager"%>
<%@page import="org.oscarehr.caisi_integrator.ws.client.CachedFacility"%>

<%@include file="/layouts/caisi_html_top2.jspf"%>

<%
	DemographicDao demographicDao=(DemographicDao)SpringUtils.getBean("demographicDao");
	CaisiIntegratorManager caisiIntegratorManager=(CaisiIntegratorManager)SpringUtils.getBean("caisiIntegratorManager");

	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));
	Facility currentFacility = (Facility) request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY);
	Provider currentProvider = (Provider) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PROVIDER);
	
	Demographic demographic=demographicDao.getDemographicById(currentDemographicId);
	ArrayList<ManageLinkedClients.LinkedDemographicHolder> demographicsToDisplay=ManageLinkedClients.getDemographicsToDisplay(currentFacility, currentProvider, currentDemographicId);
%>


<h3>Manage Linked Clients</h3>

<h4>Demographic</h4>
<form action="manage_linked_clients_action.jsp">
	<input type="hidden" name="demographicId" value="<%=currentDemographicId%>" />
	<table style="padding-left:20px" class="genericTable">
		<tr class="genericTableRow">
			<td class="genericTableHeader">Linked</td>
			<td class="genericTableHeader">Score</td>
			<td class="genericTableHeader">Facility</td>
			<td class="genericTableHeader">Id</td>
			<td class="genericTableHeader">Last Name</td>
			<td class="genericTableHeader">First Name</td>
			<td class="genericTableHeader">Birth Date</td>
			<td class="genericTableHeader">HIN</td>
			<td class="genericTableHeader">Gender</td>
		</tr>
		<tr class="genericTableRow" style="background-color:#ccffcc">
			<td class="genericTableData"></td>
			<td class="genericTableData"></td>
			<td class="genericTableData">Current facility</td>
			<td class="genericTableData"><%=currentDemographicId%></td>
			<td class="genericTableData"><%=demographic.getLastName()%></td>
			<td class="genericTableData"><%=demographic.getFirstName()%></td>
			<td class="genericTableData"><%=demographic.getFormattedDob()%></td>
			<td class="genericTableData"><%=demographic.getHin()%></td>
			<td class="genericTableData"><%=demographic.getSex()%></td>
		</tr>
	
		<%
			if (demographicsToDisplay!=null)
			{
				for (LinkedDemographicHolder temp : demographicsToDisplay)
				{
					%>
						<tr class="genericTableRow" style="background-color:#f3f3f3">
							<td class="genericTableData"><input type="checkbox" name="linked.<%=temp.linkDestination+'.'+temp.remoteLinkId%>" <%=temp.linked?"checked=\"on\"":""%> /></td>
							<td class="genericTableData"><%=temp.matchingScore%></td>
							<td class="genericTableData"><%=temp.linkDestination%></td>
							<td class="genericTableData"><%=temp.remoteLinkId%></td>
							<td class="genericTableData"><%=temp.lastName%></td>
							<td class="genericTableData"><%=temp.firstName%></td>
							<td class="genericTableData"><%=temp.birthDate%></td>
							<td class="genericTableData"><%=temp.hin%></td>
							<td class="genericTableData"><%=temp.gender%></td>
						</tr>
					<%
				}
			}
		%>
	</table>
	<br />
	<input type="submit" value="save" /> &nbsp;&nbsp; <input type="button" value="cancel" onclick="history.go(-1)" />
</form>

<%@include file="/layouts/caisi_html_bottom2.jspf"%>
