<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="org.oscarehr.common.dao.*"%>
<%@page import="org.oscarehr.common.model.*"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.dao.DemographicDao"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.caisi_integrator.ws.client.DemographicInfoWs"%>
<%@page import="org.oscarehr.caisi_integrator.ws.client.CachedDemographic"%>
<%@page import="org.oscarehr.PMmodule.web.ManageIntegratorLinkedDemographics"%>
<%@page import="org.apache.commons.lang.time.DateFormatUtils"%>
<%@page import="org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager"%>
<%@page import="org.oscarehr.caisi_integrator.ws.client.CachedFacility"%>

<%@include file="/layouts/caisi_html_top2.jspf"%>

<%
	DemographicDao demographicDao=(DemographicDao)SpringUtils.getBean("demographicDao");
	CaisiIntegratorManager caisiIntegratorManager=(CaisiIntegratorManager)SpringUtils.getBean("caisiIntegratorManager");

	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));
	int currentFacilityId = (Integer) request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY_ID);
	
	Demographic demographic=demographicDao.getDemographicById(currentDemographicId);
	ArrayList<ManageIntegratorLinkedDemographics.IntegratorLinkedDemographicHolder> demographicsToDisplay=ManageIntegratorLinkedDemographics.getDemographicsToDisplay(currentFacilityId, currentDemographicId);
%>

<h3>Manage integrator linked demographics</h3>

<h4>Demographic</h4>
<form action="manage_integrator_linked_demographics_action.jsp">
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
			for (ManageIntegratorLinkedDemographics.IntegratorLinkedDemographicHolder temp : demographicsToDisplay)
			{
				CachedDemographic tempDemographic=temp.getCachedDemographic();
				CachedFacility tempFacility=caisiIntegratorManager.getRemoteFacility(currentFacilityId, tempDemographic.getFacilityIdIntegerCompositePk().getIntegratorFacilityId());
				%>
					<tr class="genericTableRow" style="background-color:#f3f3f3">
						<td class="genericTableData"><input type="checkbox" name="linked.<%=tempDemographic.getFacilityIdIntegerCompositePk().getIntegratorFacilityId()%>.<%=tempDemographic.getFacilityIdIntegerCompositePk().getCaisiItemId()%>" <%=temp.isLinked()?"checked=\"on\"":""%> <%=temp.isLinked()&&!temp.isDirectlyLinked()?"disabled=\"disabled\"":""%> /></td>
						<td class="genericTableData"><%=temp.getMatchingScore()%></td>
						<td class="genericTableData"><%=tempFacility.getName()%></td>
						<td class="genericTableData"><%=tempDemographic.getFacilityIdIntegerCompositePk().getCaisiItemId()%></td>
						<td class="genericTableData"><%=tempDemographic.getLastName()%></td>
						<td class="genericTableData"><%=tempDemographic.getFirstName()%></td>
						<td class="genericTableData"><%=DateFormatUtils.ISO_DATE_FORMAT.format(tempDemographic.getBirthDate().toGregorianCalendar())%></td>
						<td class="genericTableData"><%=tempDemographic.getHin()%></td>
						<td class="genericTableData"><%=tempDemographic.getGender()%></td>
					</tr>
				<%
			}
		%>
	</table>
	<br />
	<input type="submit" value="save" /> &nbsp;&nbsp; <input type="button" value="cancel" onclick="history.go(-1)" />
</form>

<%@include file="/layouts/caisi_html_bottom2.jspf"%>
