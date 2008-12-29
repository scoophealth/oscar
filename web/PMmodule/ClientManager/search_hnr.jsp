<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="org.oscarehr.common.dao.*"%>
<%@page import="org.oscarehr.common.model.*"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.dao.DemographicDao"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.hnr.ws.client.Client"%>
<%@page import="org.oscarehr.PMmodule.web.SearchHnr"%>
<%@page import="org.apache.commons.lang.time.DateFormatUtils"%>
<%@page import="org.oscarehr.hnr.ws.client.MatchingClientScore"%>
<%@page import="org.oscarehr.caisi_integrator.ws.client.CachedFacility"%>
<%@page import="org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager"%>
<%@page import="org.oscarehr.hnr.ws.client.HnrWs"%>

<%@include file="/layouts/caisi_html_top2.jspf"%>

<%
	int currentDemographicId=Integer.parseInt(request.getParameter("id"));
	Facility currentFacility = (Facility) request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY);
	Provider currentProvider = (Provider) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PROVIDER);
	CaisiIntegratorManager caisiIntegratorManager=(CaisiIntegratorManager)SpringUtils.getBean("caisiIntegratorManager");
	HnrWs hnrWs=caisiIntegratorManager.getHnrWs(currentFacility.getId());

	List<MatchingClientScore> clientsToDisplay=SearchHnr.getSearchResults(currentFacility, currentProvider, currentDemographicId);	
%>


<%@page import="org.apache.commons.lang.StringUtils"%><h3>Search HNR for Health Number</h3>

<h4>Clients</h4>
<form action="manage_integrator_linked_demographics_action.jsp">
	<input type="hidden" name="demographicId" value="<%=currentDemographicId%>" />
	<table style="padding-left:20px" class="genericTable">
		<tr class="genericTableRow">
			<td class="genericTableHeader">Picture</td>
			<td class="genericTableHeader">Score</td>
			<td class="genericTableHeader">Last Name</td>
			<td class="genericTableHeader">First Name</td>
			<td class="genericTableHeader">Birth Date</td>
			<td class="genericTableHeader">HIN</td>
			<td class="genericTableHeader">Gender</td>
		</tr>
	
		<%
			if (clientsToDisplay!=null)
			{
				for (MatchingClientScore temp : clientsToDisplay)
				{
					Client tempClient=temp.getClient();
					
					String bday=null;
					if (tempClient.getBirthDate()!=null) bday=DateFormatUtils.ISO_DATE_FORMAT.format(tempClient.getBirthDate().toGregorianCalendar());
					
					String hnrImageUrl="/images/defaultR_img.jpg";
					org.oscarehr.hnr.ws.client.ClientImage hnrClientImage=hnrWs.getClientImage2("facility="+currentFacility.getName()+", provider="+currentProvider.getFormattedName(), tempClient.getHin());
					if (hnrClientImage!=null) hnrImageUrl="/imageRenderingServlet?source=hnr_client&hin="+tempClient.getHin();
					
					%>
						<tr class="genericTableRow" style="background-color:#f3f3f3">
							<td class="genericTableData"><img style="height:96px; width:96px" src="<%=request.getContextPath()+hnrImageUrl%>" alt="client_<%=tempClient.getHin()%>" /></td>
							<td class="genericTableData" style="vertical-align:middle"><%=temp.getScore()%></td>
							<td class="genericTableData" style="vertical-align:middle"><%=StringUtils.trimToEmpty(tempClient.getLastName())%></td>
							<td class="genericTableData" style="vertical-align:middle"><%=StringUtils.trimToEmpty(tempClient.getFirstName())%></td>
							<td class="genericTableData" style="vertical-align:middle"><%=StringUtils.trimToEmpty(bday)%></td>
							<td class="genericTableData" style="vertical-align:middle"><%=StringUtils.trimToEmpty(tempClient.getHin())%></td>
							<td class="genericTableData" style="vertical-align:middle"><%=tempClient.getGender()==null?"":tempClient.getGender()%></td>
						</tr>
					<%
				}
			}
		%>
	</table>
	<br />
	<input type="button" value="go back" onclick="history.go(-1)" />
</form>

<%@include file="/layouts/caisi_html_bottom2.jspf"%>
